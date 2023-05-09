package cn.rjtech.admin.qcform;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.qcformitem.QcFormItemService;
import cn.rjtech.admin.qcformparam.QcFormParamService;
import cn.rjtech.admin.qcformtableitem.QcFormTableItemService;
import cn.rjtech.admin.qcformtableparam.QcFormTableParamService;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 质量建模-检验表格
 *
 * @ClassName: QcFormService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-27 17:53
 */
public class QcFormService extends BaseService<QcForm> {
    
    private final QcForm dao = new QcForm().dao();

    @Override
    protected QcForm dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    @Inject
    private QcFormItemService qcFormItemService;
    @Inject
    private QcFormParamService qcFormParamService;
    @Inject
    private QcFormTableParamService qcFormTableParamService;
    @Inject
    private QcFormTableItemService qcFormTableItemService;

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     * @param keywords   关键词
     * @param isDeleted  删除状态：0. 未删除 1. 已删除
     * @param isEnabled  是否启用：0. 否 1. 是
     */
    public Page<QcForm> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isDeleted, Boolean isEnabled) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eqBooleanToChar("isDeleted", isDeleted);
        sql.eqBooleanToChar("isEnabled", isEnabled);
        //关键词模糊查询
        sql.likeMulti(keywords, "cOrgName", "cQcFormName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
        return paginate(sql);
    }

    /**
     * 后台管理数据查询
     */
    public Page<Record> getAdminDatas(int pageSize, int pageNumber, Okv kv) {
        return dbTemplate(dao()._getDataSourceConfigName(), "qcform.AdminDatas", kv).paginate(pageNumber, pageSize);
    }

    /**
     * 保存
     */
    public Ret save(QcForm qcForm, Long orgId, Long userId, String orgCode, String orgName, String userName, Date date) {
        if (qcForm == null) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        qcForm.setIOrgId(orgId);
        qcForm.setCOrgCode(orgCode);
        qcForm.setCOrgName(orgName);
        
        qcForm.setICreateBy(userId);
        qcForm.setCCreateName(userName);
        qcForm.setDCreateTime(date);
        
        qcForm.setIUpdateBy(userId);
        qcForm.setCUpdateName(userName);
        qcForm.setDUpdateTime(date);
        qcForm.setIsDeleted(false);
    
        QcForm obj = queryByQcName(qcForm.getCQcFormName(), null);
        ValidationUtils.isTrue(ObjectUtil.isEmpty(obj), "表格名称不能重复");
        ValidationUtils.notNull(qcForm.getIAutoId(), "未获取到主键id");
        //if(existsName(qcForm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = qcForm.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(qcForm.getIAutoId(), JBoltUserKit.getUserId(), qcForm.getName())
        }
        return ret(success);
    }
    
    public QcForm queryByQcName(String qcFormName, Long id){
        ValidationUtils.notBlank(qcFormName, "表格名称不能为空");
        String sqlStr = "SELECT * FROM Bd_QcForm WHERE cQcFormName = ?";
        if (ObjectUtil.isNotNull(id)){
            sqlStr = sqlStr+" AND iAutoId <> '"+id+"'";
        }
        return findFirst(sqlStr, qcFormName);
    }

    /**
     * 更新
     */
    public Ret update(QcForm qcForm, Long userId, String userName, Date date) {
        if (qcForm == null || notOk(qcForm.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        QcForm dbQcForm = findById(qcForm.getIAutoId());
        if (dbQcForm == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        dbQcForm.setCMemo(qcForm.getCMemo());
        dbQcForm.setCQcFormName(qcForm.getCQcFormName());
        dbQcForm.setIsEnabled(qcForm.getIsEnabled());
        dbQcForm.setIUpdateBy(userId);
        dbQcForm.setCUpdateName(userName);
        dbQcForm.setDUpdateTime(date);
        QcForm obj = queryByQcName(dbQcForm.getCQcFormName(), dbQcForm.getIAutoId());
        ValidationUtils.isTrue(ObjectUtil.isEmpty(obj), "表格名称不能重复");
        //if(existsName(qcForm.getName(), qcForm.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST)}
        boolean success = dbQcForm.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(qcForm.getIAutoId(), JBoltUserKit.getUserId(), qcForm.getName())
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param qcForm 要删除的model
     * @param kv     携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(QcForm qcForm, Kv kv) {
        //addDeleteSystemLog(qcForm.getIAutoId(), JBoltUserKit.getUserId(),qcForm.getName())
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param qcForm model
     * @param kv     携带额外参数一般用不上
     */
    @Override
    public String checkInUse(QcForm qcForm, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(QcForm qcForm, String column, Kv kv) {
        //addUpdateSystemLog(qcForm.getIAutoId(), JBoltUserKit.getUserId(), qcForm.getName(),"的字段["+column+"]值:"+qcForm.get(column))
        /*
         switch(column){
         case "isDeleted"
         break;
         case "isEnabled"
         break;
         }
         */
        return null;
    }

    /**
     * 按主表qcformitem查询列表qcform
     */
    public List<Record> getItemCombinedListByPId(Kv kv) {
        return dbTemplate("qcformitem.formItemLists", kv).find();
    }

    /**
     * 按主表qcformparam查询列表
     */
    public Page<Record> getQcFormParamListByPId(int pageNumber, int pageSize, Kv para) {
        return dbTemplate("qcformparam.formParamList", para).paginate(pageNumber, pageSize);
    }

    /**
     * 按主表qcformtableparam查询列表
     */
    public Page<Record> getQcFormTableParamListByPId(int pageNumber, int pageSize, Okv kv) {
        return dbTemplate("qcformtableparam.formTableParamList", kv).paginate(pageNumber, pageSize);
    }

    /**
     * qcformitem可编辑表格提交
     *
     * @param jBoltTable 编辑表格提交内容
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        QcForm qcForm = jBoltTable.getFormModel(QcForm.class, "qcForm");
        ValidationUtils.notNull(qcForm, JBoltMsg.PARAM_ERROR);

        Long userId = JBoltUserKit.getUserId();
        String name = JBoltUserKit.getUserName();
        Date nowDate = new Date();

        tx(() -> {
            // 修改
            if (isOk(qcForm.getIAutoId())) {
                qcForm.setCUpdateName(name);
                qcForm.setIUpdateBy(userId);
                qcForm.setDUpdateTime(nowDate);
                ValidationUtils.isTrue(qcForm.update(), JBoltMsg.FAIL);
            } else {
                //新增
                qcForm.setCCreateName(name);
                qcForm.setICreateBy(userId);
                qcForm.setDCreateTime(nowDate);
                qcForm.setCUpdateName(name);
                qcForm.setIUpdateBy(userId);
                qcForm.setDUpdateTime(nowDate);
                qcForm.setIsDeleted(false);
                ValidationUtils.isTrue(qcForm.save(), JBoltMsg.FAIL);
            }

            // 判断table是否为空
            if (jBoltTable.saveIsNotBlank()) {
                List<QcFormItem> saveQcFormItem = jBoltTable.getSaveModelList(QcFormItem.class);
                
                int k = 0;
                for (QcFormItem saveQcFormItemLine : saveQcFormItem) {
                    k++;
                    saveQcFormItemLine.setIQcFormId(qcForm.getIAutoId());
                    Record BdQcFormItem = findFirstRecord(("SELECT max(iSeq) as iseq FROM Bd_QcFormItem WHERE iQcFormId = '" + qcForm.getIAutoId() + "' AND isDeleted = 0"));
                    Integer iseq = BdQcFormItem.getInt("iseq");
                    if (notNull(iseq)) {
                        saveQcFormItemLine.setISeq(iseq + k);
                    } else {
                        saveQcFormItemLine.setISeq(k);
                    }
                    saveQcFormItemLine.setIsDeleted(false);
                }
                qcFormItemService.batchSave(saveQcFormItem);
            }
            
            // 更新
            if (jBoltTable.updateIsNotBlank()) {
                List<QcFormItem> updateQcFormItem = jBoltTable.getUpdateModelList(QcFormItem.class);
                updateQcFormItem.forEach(updateQcFormItemLine -> {
                });
                qcFormItemService.batchUpdate(updateQcFormItem);
            }

//			// 获取待删除数据 执行删除
//			if (jBoltTable.deleteIsNotBlank()) {
//				qcFormItemService.deleteByIds(jBoltTable.getDelete())
//			}
            return true;
        });
        return successWithData(qcForm.keep("iautoid"));
    }

    /**
     * qcformparam可编辑表格提交
     *
     * @param jBoltTable 编辑表格提交内容
     */
    public Ret QcFormParamJBoltTable(JBoltTable jBoltTable) {
        QcForm qcForm = jBoltTable.getFormModel(QcForm.class, "qcForm");
        ValidationUtils.notNull(qcForm, JBoltMsg.PARAM_ERROR);

        Long userId = JBoltUserKit.getUserId();
        String name = JBoltUserKit.getUserName();
        Date nowDate = new Date();

        tx(() -> {
            // 修改
            if (isOk(qcForm.getIAutoId())) {
                qcForm.setCUpdateName(name);
                qcForm.setIUpdateBy(userId);
                qcForm.setDUpdateTime(nowDate);
                ValidationUtils.isTrue(qcForm.update(), JBoltMsg.FAIL);
            } else {
                // 新增
                qcForm.setCCreateName(name);
                qcForm.setICreateBy(userId);
                qcForm.setDCreateTime(nowDate);
                qcForm.setCUpdateName(name);
                qcForm.setIUpdateBy(userId);
                qcForm.setDUpdateTime(nowDate);
                qcForm.setIsDeleted(false);
                ValidationUtils.isTrue(qcForm.save(), JBoltMsg.FAIL);
            }

            List<QcFormParam> saveQcFormParam = jBoltTable.getSaveModelList(QcFormParam.class);
            int k = 0;
            for (QcFormParam saveQcFormParamLine : saveQcFormParam) {
                Long iqcformitemid = saveQcFormParamLine.get("iqcformitemid");
                k++;
                Record BdQcFormParam = findFirstRecord(("SELECT iItemParamSeq FROM Bd_QcFormParam WHERE iQcFormItemId = '" + iqcformitemid + "' AND isDeleted = 0"));
                if (notNull(BdQcFormParam)) {
                    int isep = BdQcFormParam.size();
                    saveQcFormParamLine.setIItemParamSeq(isep + k);
                } else {
                    saveQcFormParamLine.setIItemParamSeq(k);
                }
                saveQcFormParamLine.setIsDeleted(false);
            }
            qcFormParamService.batchSave(saveQcFormParam);
            //更新
            if (jBoltTable.updateIsNotBlank()) {
                List<QcFormParam> updateQcFormParam = jBoltTable.getUpdateModelList(QcFormParam.class);
                updateQcFormParam.forEach(updateQcFormItemLine -> {

                });
                qcFormParamService.batchUpdate(updateQcFormParam);
            }
            // 获取待删除数据 执行删除
//			if (jBoltTable.deleteIsNotBlank()) {
//				qcFormParamService.deleteByIds(jBoltTable.getDelete())
//			}
            return true;
        });
        return SUCCESS;
    }

    /**
     * 检验报告记录详情表头一
     */
    public List<Record> qcformtableparamOneTitle(Long iAutoId) {
        return dbTemplate("qcform.qcformtableparamOneTitle", Kv.by("iQcFormId", iAutoId)).find();
    }

    /**
     * 检验报告记录详情表头二
     */
    public List<Record> qcformtableparamTwoTitle(Long iAutoId) {
        return dbTemplate("qcform.qcformtableparamTwoTitle", Kv.by("iQcFormId", iAutoId)).find();
    }

    /**
     * 拉取客户资源
     */
    public List<Record> customerList(Kv kv) {
        kv.set("orgCode", getOrgCode());
        return dbTemplate("qcformtableparam.customerList", kv).find();
    }

    public List<Record> options() {
        return dbTemplate("qcform.AdminDatas", Kv.of("isenabled", "true")).find();
    }
    
    public Ret submitForm(String formJsonDataStr, String qcItemTableJsonDataStr, String qcParamTableJsonDataStr, String tableJsonDataStr){
        ValidationUtils.notBlank(formJsonDataStr, JBoltMsg.JBOLTTABLE_IS_BLANK);
        ValidationUtils.notBlank(qcItemTableJsonDataStr, JBoltMsg.JBOLTTABLE_IS_BLANK);
        ValidationUtils.notBlank(qcParamTableJsonDataStr, JBoltMsg.JBOLTTABLE_IS_BLANK);
        ValidationUtils.notBlank(tableJsonDataStr, JBoltMsg.JBOLTTABLE_IS_BLANK);
        
        JSONArray qcParamJsonData = JSONObject.parseArray(qcParamTableJsonDataStr);
        ValidationUtils.notEmpty(qcParamJsonData, JBoltMsg.JBOLTTABLE_IS_BLANK);
    
        JSONObject formJsonData = JSONObject.parseObject(formJsonDataStr);
        QcForm qcFom = createQcFom(formJsonData.getLong(QcForm.IAUTOID), formJsonData.getString(QcForm.CQCFORMNAME), formJsonData.getString(QcForm.CMEMO), Boolean.valueOf(formJsonData.getString(QcForm.ISENABLED)));
        // 用于记录新增是的主键id
        QcForm qcFormNew = new QcForm();
        Long orgId = getOrgId();
        String orgCode = getOrgCode();
        String orgName = getOrgName();
    
        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        DateTime date = DateUtil.date();
        Long formId = qcFom.getIAutoId();
        // 判断是否新增
        
        if (ObjectUtil.isNull(formId)){
            formId = JBoltSnowflakeKit.me.nextId();
            qcFormNew.setIAutoId(formId);
        }
        
        List<QcFormItem> qcFormItemList = qcFormItemService.createQcFormItemList(formId, false, JSONObject.parseArray(qcItemTableJsonDataStr));
        Map<Long, QcFormItem> qcFormItemMap = qcFormItemList.stream().collect(Collectors.toMap(QcFormItem::getIQcItemId, Function.identity(), (key1, key2) -> key2));
        for (int i=0; i<qcParamJsonData.size(); i++){
            JSONObject jsonObject = qcParamJsonData.getJSONObject(i);
            Long qcItemId = jsonObject.getLong(QcFormParam.IQCITEMID.toLowerCase());
            if (qcFormItemMap.containsKey(qcItemId)){
                QcFormItem qcFormItem = qcFormItemMap.get(qcItemId);
                jsonObject.put(QcFormParam.IQCFORMITEMID.toLowerCase(), qcFormItem.getIAutoId());
            }
        }
    
        List<QcFormParam> qcFormParamList = qcFormParamService.createQcFormParamList(formId, qcParamJsonData);
        JSONArray tableJsonData = JSONObject.parseArray(tableJsonDataStr);
        List<QcFormTableParam> qcFormTableParamList = qcFormTableParamService.createQcFormTableParamList(formId, tableJsonData);
        List<QcFormTableItem> qcFormTableItemList = qcFormTableItemService.createQcFormTableItemList(formId, tableJsonData);
    
        
        tx(() -> {
            // 新增
            if (ObjectUtil.isNull(qcFom.getIAutoId())){
                qcFom.setIAutoId(qcFormNew.getIAutoId());
                save(qcFom, orgId, userId, orgCode, orgName, userName, date);
            }else {
                // 先删除后添加
                removeById(qcFom.getIAutoId());
                
                update(qcFom, userId, userName, date);
            }
           
            // 保存项目
            qcFormItemService.batchSave(qcFormItemList);
            // 保存参数
            qcFormParamService.batchSave(qcFormParamList);
            // 保存记录行数据
            qcFormTableParamService.batchSave(qcFormTableParamList);
            // 保存记录行数据中间表
            qcFormTableItemService.batchSave(qcFormTableItemList);
            return true;
        });
        
        // 修改 removeById
        
//
        return SUCCESS;
    }
    
    
    public QcForm createQcFom(Long id, String qcFormName, String memo, Boolean isEnabled){
        QcForm qcForm = new QcForm();
        qcForm.setIAutoId(id);
        qcForm.setCQcFormName(qcFormName);
        qcForm.setCMemo(memo);
        qcForm.setIsEnabled(isEnabled);
        return qcForm;
    }
    
    /**
     * 删除关联表数据，重新添加
     * @param id
     */
    public void removeById(Long id){
        qcFormItemService.removeByQcFormId(id);
        qcFormParamService.removeByQcFormId(id);
        qcFormTableParamService.removeByQcFormId(id);
        qcFormTableItemService.removeByQcFormId(id);
    }
    
}
