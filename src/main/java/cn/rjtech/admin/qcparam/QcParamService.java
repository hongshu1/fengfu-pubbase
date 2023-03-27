package cn.rjtech.admin.qcparam;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;

import cn.hutool.core.date.DateUtil;
import cn.jbolt.common.util.CACHE;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;

import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.admin.qcitem.QcItemService;
import cn.rjtech.model.momdata.Operation;
import cn.rjtech.model.momdata.QcItem;
import cn.rjtech.model.momdata.QcParam;
import cn.rjtech.util.ValidationUtils;

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
        Page<Record> recordPage = dbTemplate("qcparam.list", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
        return recordPage;
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
        if (dbQcParam == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(qcParam.getName(), qcParam.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = qcParam.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(qcParam.getIAutoId(), JBoltUserKit.getUserId(), qcParam.getName());
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

    /*
     * 导出excel文件
     * */
    public JBoltExcel exportExcelTpl(List<QcParam> datas) {
        //2、创建JBoltExcel
        JBoltExcel jBoltExcel = JBoltExcel
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
                        data.changeWithKey("user_id", "user_username", CACHE.me.getUserUsername(data.get("user_id")));
                        data.changeBooleanToStr("is_enabled", "是", "否");
                    })
                    .setModelDatas(2, datas)//设置数据
            )
            .setFileName("检验参数" + "_" + DateUtil.today());
        //3、返回生成的excel文件
        return jBoltExcel;
    }

    /*
     * 上传excel文件
     * */
    public Ret importExcelData(File file) {
        StringBuilder errorMsg = new StringBuilder();
        JBoltExcel jBoltExcel = JBoltExcel
            //从excel文件创建JBoltExcel实例
            .from(file)
            //设置工作表信息
            .setSheets(
                JBoltExcelSheet.create("sheet1")
                    //设置列映射 顺序 标题名称
                    .setHeaders(
                        JBoltExcelHeader.create("iqcitemid", "参数项目名称"),
                        JBoltExcelHeader.create("cqcparamname", "参数名称")
                    )
//                    //特殊数据转换器
//                    .setDataChangeHandler((data, index) -> {
//                        try {
//                            String isalary = data.getStr("isalary");
//                            data.change("isalary", new BigDecimal(isalary));
//                        } catch (Exception e) {
//                            errorMsg.append(data.getStr("isalary") + "薪酬填写有误");
//                        }
//                    })
                    //从第几行开始读取
                    .setDataStartRow(2)
            );
        //从指定的sheet工作表里读取数据
        List<QcParam> models = JBoltExcelUtil.readModels(jBoltExcel, "sheet1", QcParam.class, errorMsg);
        if (notOk(models)) {
            if (errorMsg.length() > 0) {
                return fail(errorMsg.toString());
            } else {
                return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
            }
        }
        if (errorMsg.length() > 0) {
            return fail(errorMsg.toString());
        }
        //读取数据没有问题后判断必填字段
        if (models.size() > 0) {
            for (QcParam param : models) {
                if (notOk(param.getIqcitemid())) {
                    return fail("参数项目名称不能为空");
                }
                if (notOk(param.getCQcParamName())) {
                    return fail("参数名称不能为空");
                }
            }
        }
        //判断是否存在这个参数项目
        for (QcParam each : models) {
            List<QcItem> qcItemList = qcItemService.findQcItemName(each.getIqcitemid().toString());
            if (notOk(qcItemList)) {
                return fail(each.getIqcitemid() + "参数项目不存在，请去【检验项目】页面新增！");
            }
        }

        savaModelHandle(models);
        //执行批量操作
        boolean success = tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                batchSave(models);
                return true;
            }
        });
        if (!success) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL);
        }
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
//        param.setIqcitemid();
    }

}