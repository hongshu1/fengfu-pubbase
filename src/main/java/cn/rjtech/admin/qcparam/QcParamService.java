package cn.rjtech.admin.qcparam;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.qcitem.QcItemService;
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.model.momdata.QcItem;
import cn.rjtech.model.momdata.QcParam;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.*;

/**
 * 质量建模-检验参数
 *
 * @ClassName: QcParamService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-17 15:15
 */
public class QcParamService extends BaseService<QcParam> {

    private final QcParam dao = new QcParam().dao();

    @Inject
    private QcItemService qcItemService;

    @Override
    protected QcParam dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     * @param keywords   关键词
     * @param isEnabled  是否启用：0. 否 1. 是
     * @param isDeleted  删除状态：0. 未删除 1. 已删除
     */
    public Page<QcParam> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isEnabled, Boolean isDeleted) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eqBooleanToChar("isEnabled", isEnabled);
        sql.eqBooleanToChar("isDeleted", isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords, "cOrgName", "cQcParamName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
        return paginate(sql);
    }

    public Page<Record> pageList(Kv kv) {
        return dbTemplate("qcparam.list", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
    }

    /**
     * 保存
     */
    public Ret save(QcParam qcParam) {
        if (qcParam == null || isOk(qcParam.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        qcParam.setIAutoId(JBoltSnowflakeKit.me.nextId());
        saveQcParam(qcParam);
        boolean success = qcParam.save();
        if (!success){
            return fail(JBoltMsg.FAIL);
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(QcParam qcParam) {
        if (qcParam == null || notOk(qcParam.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        QcParam dbQcParam = findById(qcParam.getIAutoId());
        if (null == dbQcParam) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = qcParam.update();
        if (!success){
            return fail(JBoltMsg.FAIL);
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param qcParam 要删除的model
     * @param kv      携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(QcParam qcParam, Kv kv) {
        //addDeleteSystemLog(qcParam.getIAutoId(), JBoltUserKit.getUserId(),qcParam.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param qcParam model
     * @param kv      携带额外参数一般用不上
     */
    @Override
    public String checkInUse(QcParam qcParam, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(QcParam qcParam, String column, Kv kv) {
        //addUpdateSystemLog(qcParam.getIAutoId(), JBoltUserKit.getUserId(), qcParam.getName(),"的字段["+column+"]值:"+qcParam.get(column));
        /**
         switch(column){
         case "isEnabled":
         break;
         case "isDeleted":
         break;
         }
         */
        return null;
    }

    public List<Record> list(Kv kv) {
        return dbTemplate("qcparam.list", kv).find();
    }

    /**
     * 切换isenabled属性
     */
    public Ret toggleIsenabled(Long id) {
        return toggleBoolean(id, "isEnabled");
    }

    /**
     * 导出excel文件
     */
    public JBoltExcel exportExcelTpl(List<QcParam> datas) {
        //2、创建JBoltExcel
        //3、返回生成的excel文件
        return JBoltExcel
            .create()
            .addSheet(//设置sheet
                JBoltExcelSheet.create("检验参数")//创建sheet name保持与模板中的sheet一致
                    .setHeaders(//sheet里添加表头
                        JBoltExcelHeader.create("cqcitemname", "参数项目名称", 20),
                        JBoltExcelHeader.create("cqcparamname", "参数名称", 20),
//                        JBoltExcelHeader.create("isenabled", "是否启用", 20),
                        JBoltExcelHeader.create("ccreatename", "创建人", 20),
                        JBoltExcelHeader.create("dcreatetime", "创建时间", 20)
                    )
                    .setDataChangeHandler((data, index) -> {//设置数据转换处理器
                        //将user_id转为user_name
                        data.changeWithKey("user_id", "user_username", JBoltUserCache.me.getUserName(data.getLong("user_id")));
                        data.changeBooleanToStr("is_enabled", "是", "否");
                    })
                    .setModelDatas(2, datas)//设置数据
            )
            .setFileName("检验参数" + "_" + DateUtil.today());
    }

    /**
     * 上传excel文件
     */
    public Ret importExcelData(File file) {
        List<Record> records = CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, table());
        if (notOk(records)) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
        }

        Date now = new Date();
        // 检验项目
        Map<String, QcItem> qcitemMap = new HashMap<>();

        for (Record record : records) {
            String cqcitemname = record.getStr("iQcItemId");

            QcItem qcitem = qcitemMap.get(cqcitemname);

            if (ObjUtil.isNull(qcitem)) {
                qcitem = qcItemService.findBycQcItemName(cqcitemname);
                ValidationUtils.notNull(qcitem, String.format("检验项目“%s”不存在", cqcitemname));
                qcitemMap.put(cqcitemname, qcitem);
            }
            if (StrUtil.isBlank(record.getStr("cQcParamName"))) {
                return fail("检验参数名称不能为空");
            }

            record.set("iAutoId", JBoltSnowflakeKit.me.nextId());
            record.set("iQcItemId", qcitem.getIAutoId());
            record.set("cQcParamName", record.getStr("cQcParamName"));
            record.set("iCreateBy", JBoltUserKit.getUserId());
            record.set("dCreateTime", now);
            record.set("cCreateName", JBoltUserKit.getUserName());
            record.set("iOrgId", getOrgId());
            record.set("cOrgCode", getOrgCode());
            record.set("cOrgName", getOrgName());
            record.set("iUpdateBy", JBoltUserKit.getUserId());
            record.set("dUpdateTime", now);
            record.set("cUpdateName", JBoltUserKit.getUserName());
            record.set("isDeleted",0);
            record.set("isEnabled",true);
        }

        // 执行批量操作
        tx(() -> {
            batchSaveRecords(records);
            return true;
        });
        return SUCCESS;
    }

    public void savaModelHandle(List<QcParam> models) {
        for (QcParam param : models) {
            saveQcParam(param);
            List<QcItem> qcItemList = qcItemService.findQcItemName(param.getIqcitemid().toString());
            param.setIqcitemid(qcItemList.get(0).getIAutoId());
        }
    }

    public void saveQcParam(QcParam param) {
        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        Date date = new Date();
        param.setCOrgCode(getOrgCode());
        param.setCOrgName(getOrgName());
        param.setICreateBy(userId);
        param.setCCreateName(userName);
        param.setDCreateTime(date);
        param.setDUpdateTime(date);
        param.setCUpdateName(userName);
        param.setIUpdateBy(userId);
        param.setIsDeleted(false);
        param.setIsEnabled(true);
    }

    public List<QcParam> findByIqcItemId(Long iQcItemId){
        return find("select * from Bd_QcParam where iQcItemId=?",iQcItemId);
    }

    /**
     * 从系统导入字段配置，获得导入的数据
     */
    public Ret importExcelClass(File file) {
        StringBuilder errorMsg = new StringBuilder();
        JBoltExcel excel = JBoltExcel
                // 从excel文件创建JBoltExcel实例
                .from(file)
                // 设置工作表信息
                .setSheets(
                        JBoltExcelSheet.create("Sheet1")
                                // 设置列映射 顺序 标题名称
                                .setHeaders(2,
                                        JBoltExcelHeader.create("cQcItemName", "检验项目名称"),
                                        JBoltExcelHeader.create("cQcParamName", "检验参数名称")
                                )
                                // 从第三行开始读取
                                .setDataStartRow(3)
                );

        // 从指定的sheet工作表里读取数据
        List<Record> rows = JBoltExcelUtil.readRecords(excel, 0, true, errorMsg);

        List<QcParam> addList=new ArrayList<>();

        Date now = new Date();
        
        for (Record record : rows) {
            String cQcItemName = record.getStr("cQcItemName");
            if (StrUtil.isBlank(cQcItemName)) {
                return fail("检验项目名称不能为空");
            }
            if (StrUtil.isBlank(record.getStr("cQcParamName"))) {
                return fail("检验参数名称不能为空");
            }


            QcItem qcItem = qcItemService.findBycQcItemName(cQcItemName);
            if(qcItem==null){
                return fail(cQcItemName+",查无此项目");
            }

            record.set("iAutoId", JBoltSnowflakeKit.me.nextId());
            record.set("iOrgId", getOrgId());
            record.set("cOrgCode", getOrgCode());
            record.set("cOrgName", getOrgName());
            record.set("iCreateBy", JBoltUserKit.getUserId());
            record.set("dCreateTime", now);
            record.set("cCreateName", JBoltUserKit.getUserName());
            record.set("isEnabled",1);
            record.set("isDeleted",0);
            record.set("iUpdateBy", JBoltUserKit.getUserId());
            record.set("dUpdateTime", now);
            record.set("cUpdateName", JBoltUserKit.getUserName());

            QcParam qcParam=new QcParam();
            qcParam.put(record);
            qcParam.setIqcitemid(qcItem.getIAutoId());
            addList.add(qcParam);
        }

        // 执行批量操作
        tx(() -> {
            for (QcParam qcParam : addList) {
                qcParam.save();
            }
            return true;
        });
        return SUCCESS;
    }

}