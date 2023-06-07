package cn.rjtech.admin.qcitem;

import cn.hutool.core.date.DateUtil;
import cn.jbolt.common.util.CACHE;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.QcItem;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

/**
 * 质量建模-检验项目（分类）
 *
 * @ClassName: QcItemService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-17 15:31
 */
public class QcItemService extends BaseService<QcItem> {

    private final QcItem dao = new QcItem().dao();

    @Override
    protected QcItem dao() {
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
     * @param isDeleted  删除状态：0. 未删除 1. 已删除
     */
    public Page<QcItem> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isDeleted) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eqBooleanToChar("isDeleted", isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords, "cOrgName", "cQcItemCode", "cQcItemName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("dupdatetime");
        return paginate(sql);
    }

    public Page<Record> pageList(Kv kv) {
        return dbTemplate("qcitem.list", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
    }

    /**
     * 保存
     */
    public Ret save(QcItem qcItem) {
        if (qcItem == null || isOk(qcItem.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //项目编码不能重复
        ValidationUtils.isTrue(findQcItemCode(qcItem.getCQcItemCode()) == null, qcItem.getCQcItemCode() + "：项目编码重复");
        //项目名不能重复
        ValidationUtils.isTrue(findQcItemName(qcItem.getCQcItemName()).isEmpty(), qcItem.getCQcItemName() + "：项目名重复");
        //1、赋值
        String userName = JBoltUserKit.getUserName();
        Long userId = JBoltUserKit.getUserId();
        Date date = new Date();
        qcItem.setIOrgId(getOrgId());
        qcItem.setCOrgCode(getOrgCode());
        qcItem.setCOrgName(getOrgName());
        qcItem.setIsDeleted(false);
        qcItem.setICreateBy(userId);
        qcItem.setCCreateName(userName);
        qcItem.setDCreateTime(date);
        qcItem.setIUpdateBy(userId);
        qcItem.setCUpdateName(userName);
        qcItem.setDUpdateTime(date);
        //2、保存
        boolean result = qcItem.save();
        if (!result){
            return fail(JBoltMsg.FAIL);
        }
        //3、返回保存结果
        return ret(result);
    }

    /**
     * 项目编码是否重复
     */
    public Long findQcItemCode(String cqcItemCode) {
        return queryColumn(selectSql().select(QcItem.CQCITEMCODE).eq(QcItem.CQCITEMCODE, cqcItemCode));
    }

    /**
     * 检验项目名称
     */
    public List<QcItem> findQcItemName(String cqcItemName) {
        return query(selectSql().select(QcItem.CQCITEMNAME).eq(QcItem.CQCITEMNAME, cqcItemName).eq(QcItem.ISDELETED, "0"));
    }

    /**
     * 更新
     */
    public Ret update(QcItem qcItem) {
        if (qcItem == null || notOk(qcItem.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        QcItem dbQcItem = findById(qcItem.getIAutoId());
        if (dbQcItem == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //项目编码不能重复
        ValidationUtils.isTrue(findQcItemCode(qcItem.getCQcItemCode()) == null, qcItem.getCQcItemCode() + "：项目编码重复");
        //项目名不能重复
        ValidationUtils.isTrue(findQcItemName(qcItem.getCQcItemName()).isEmpty(), qcItem.getCQcItemName() + "：项目名重复");
        boolean result = qcItem.update();
        if (!result){
            return fail(JBoltMsg.FAIL);
        }
        return ret(result);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param qcItem 要删除的model
     * @param kv     携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(QcItem qcItem, Kv kv) {
        //addDeleteSystemLog(qcItem.getIAutoId(), JBoltUserKit.getUserId(),qcItem.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param qcItem model
     * @param kv     携带额外参数一般用不上
     */
    @Override
    public String checkInUse(QcItem qcItem, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(QcItem qcItem, String column, Kv kv) {
		/*addUpdateSystemLog(qcItem.getIAutoId(), JBoltUserKit.getUserId(), qcItem.getName(),"的字段["+column+"]值:"+qcItem.get(column));
		switch(column){
		    case "isDeleted":
		        break;
		}*/
        return null;
    }

    public List<Record> list(Kv kv) {
        return dbTemplate("qcitem.list", kv).find();
    }

    public List<Record> options() {
        return dbTemplate("qcitem.list", Kv.of("isenabled", "true")).find();
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        return deleteById(id, true);
    }

    /**
     * 导出excel文件
     */
    public JBoltExcel exportExcelTpl(List<QcItem> datas) {
        //2、创建JBoltExcel
        JBoltExcel jBoltExcel = JBoltExcel
            .create()
            .addSheet(//设置sheet
                JBoltExcelSheet.create("检验项目(分类)")//创建sheet name保持与模板中的sheet一致
                    .setHeaders(//sheet里添加表头
                        JBoltExcelHeader.create("corgcode", "组织编码", 20),
                        JBoltExcelHeader.create("corgname", "组织名称", 20),
                        JBoltExcelHeader.create("cqcitemcode", "检验项目编码", 20),
                        JBoltExcelHeader.create("cqcitemname", "检验项目名称", 20),
                        JBoltExcelHeader.create("ccreatename", "创建人", 20),
                        JBoltExcelHeader.create("dcreatetime", "创建时间", 20)
                    )
                    .setDataChangeHandler((data, index) -> {//设置数据转换处理器
                        //将user_id转为user_name
                        data.changeWithKey("user_id", "user_username", CACHE.me.getUserUsername(data.get("user_id")));
                        data.changeBooleanToStr("is_deleted", "是", "否");
                    })
                    .setModelDatas(2, datas)//设置数据
            )
            .setFileName("检验项目(分类)" + "_" + DateUtil.today());
        //3、返回生成的excel文件
        return jBoltExcel;
    }

}