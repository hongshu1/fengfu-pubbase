package cn.rjtech.admin.auditformconfig;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.AuditFormConfig;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 审批表单配置 Service
 *
 * @ClassName: AuditFormConfigService
 * @author: RJ
 * @date: 2023-04-18 17:18
 */
public class AuditFormConfigService extends BaseService<AuditFormConfig> {

    private final AuditFormConfig dao = new AuditFormConfig().dao();

    @Override
    protected AuditFormConfig dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<AuditFormConfig> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 后台管理分页查询
     */
    public Page<AuditFormConfig> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
        return daoTemplate("auditformconfig.pageList", kv).paginate(pageNumber, pageSize);
    }

    /**
     * 保存
     */
    public Ret save(AuditFormConfig auditFormConfig) {
        if (auditFormConfig == null || isOk(auditFormConfig.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(auditFormConfig.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = auditFormConfig.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(auditFormConfig.getIautoid(), JBoltUserKit.getUserId(), auditFormConfig.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(AuditFormConfig auditFormConfig) {
        if (auditFormConfig == null || notOk(auditFormConfig.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        AuditFormConfig dbAuditFormConfig = findById(auditFormConfig.getIAutoId());
        if (dbAuditFormConfig == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(auditFormConfig.getName(), auditFormConfig.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = auditFormConfig.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(auditFormConfig.getIautoid(), JBoltUserKit.getUserId(), auditFormConfig.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        return deleteByIds(ids, true);
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        return deleteById(id, true);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param auditFormConfig 要删除的model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(AuditFormConfig auditFormConfig, Kv kv) {
        //addDeleteSystemLog(auditFormConfig.getIautoid(), JBoltUserKit.getUserId(),auditFormConfig.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param auditFormConfig 要删除的model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(AuditFormConfig auditFormConfig, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(auditFormConfig, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换isdeleted属性
     */
    public Ret toggleIsDeleted(Long id) {
        return toggleBoolean(id, "IsDeleted");
    }

    /**
     * 切换isenabled属性
     */
    public Ret toggleIsEnabled(Long id) {
        return toggleBoolean(id, "isEnabled");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param auditFormConfig 要toggle的model
     * @param column          操作的哪一列
     * @param kv              携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(AuditFormConfig auditFormConfig, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(AuditFormConfig auditFormConfig, String column, Kv kv) {
        //addUpdateSystemLog(auditFormConfig.getIautoid(), JBoltUserKit.getUserId(), auditFormConfig.getName(),"的字段["+column+"]值:"+auditFormConfig.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param auditFormConfig model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    public String checkInUse(AuditFormConfig auditFormConfig, Kv kv) {
        //这里用来覆盖 检测AuditFormConfig是否被其它表引用
        return null;
    }

    /**
     * 获取存货数据
     */
    public Page<Record> resourceList(Kv kv, int pageNum, int pageSize) {
        return dbTemplate("auditformconfig.resourceList", kv).paginate(pageNum, pageSize);
    }

    /**
     * 可编辑表格提交
     *
     * @param jBoltTable 编辑表格提交内容
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        // 当前操作人员  当前时间
        User user = JBoltUserKit.getUser();
        
        Date nowDate = new Date();
        Long orgId = getOrgId();
        String orgCode = getOrgCode();
        String orgName = getOrgName();
        
        LOG.info("saveTable===>" + jBoltTable.getSave());
        LOG.info("updateTable===>" + jBoltTable.getUpdate());
        LOG.info("deleteTable===>" + Arrays.toString(jBoltTable.getDelete()));
        LOG.info("form===>" + jBoltTable.getForm());

        tx(() -> {
            AuditFormConfig formBean = jBoltTable.getFormModel(AuditFormConfig.class, "auditFormConfig");
            Boolean isEnabled = formBean.getIsEnabled();
            Integer iType = formBean.getIType();

            if (jBoltTable.saveIsNotBlank()) {
                List<AuditFormConfig> saveModelList = jBoltTable.getSaveModelList(AuditFormConfig.class);
                List<AuditFormConfig> saveList = new ArrayList<>();
                saveModelList.forEach(auditFormConfig -> {

                    /*
                     * 类型为 审批 的话 需要先定义审批流
                     */
                    if (iType == 1) {
                        Long iFormId = auditFormConfig.getIFormId();
                        String cFormName = auditFormConfig.getCFormName();
                        List<Record> record = findRecords("select * from Bd_ApprovalForm where iFormId = ? ", iFormId);
                        ValidationUtils.isTrue(record.size() > 0, "需要先在审批流配置定义表单" + cFormName + "的审批流");
                    }

                    auditFormConfig.setIsEnabled(isEnabled);
                    auditFormConfig.setIType(iType);
                    auditFormConfig.setICreateBy(user.getId());
                    auditFormConfig.setCCreateName(user.getName());
                    auditFormConfig.setDCreateTime(nowDate);
                    auditFormConfig.setIUpdateBy(user.getId());
                    auditFormConfig.setCUpdateName(user.getName());
                    auditFormConfig.setDUpdateTime(nowDate);
                    auditFormConfig.setIOrgId(orgId);
                    auditFormConfig.setCOrgCode(orgCode);
                    auditFormConfig.setCOrgName(orgName);
                    saveList.add(auditFormConfig);
                });
                batchSave(saveList);
            }

            if (jBoltTable.updateIsNotBlank()) {
                List<AuditFormConfig> updateModelList = jBoltTable.getUpdateModelList(AuditFormConfig.class);
                List<AuditFormConfig> updateList = new ArrayList<>();
                updateModelList.forEach(auditFormConfig -> {

                    /*
                     * 类型为 审批 的话 需要先定义审批流
                     */
                    if (iType == 1) {
                        Long iFormId = auditFormConfig.getIFormId();
                        String cFormName = auditFormConfig.getCFormName();
                        List<Record> record = findRecords("select * from Bd_ApprovalForm where iFormId = ? ", iFormId);
                        ValidationUtils.notEmpty(record, "需要先在审批流配置定义表单[" + cFormName + "]的审批流");
                    }

                    auditFormConfig.setIsEnabled(isEnabled);
                    auditFormConfig.setIType(iType);
                    auditFormConfig.setIUpdateBy(user.getId());
                    auditFormConfig.setCUpdateName(user.getName());
                    auditFormConfig.setDUpdateTime(nowDate);
                    updateList.add(auditFormConfig);
                });
                
                batchUpdate(updateList);
            }

            if (jBoltTable.deleteIsNotBlank()) {
                deleteByIds(jBoltTable.getDelete());
            }
            return true;
        });
        return SUCCESS;
    }

    /**
     * 行数据源
     */
    public List<AuditFormConfig> listData(Kv kv) {
        Long iAutoId = kv.getLong("auditFormConfig.iAutoId");
        List<AuditFormConfig> list = new ArrayList<>();

        if (iAutoId != null) {
            AuditFormConfig formConfig = findById(iAutoId);
            list.add(formConfig);
        }

        return list;
    }

    /**
     * 通过表单编码找到审核审批配置
     */
    public AuditFormConfig findByFormSn(String formSn) {
        return findFirst("select * from Bd_AuditFormConfig where cFormSn = ? AND isEnabled = '1' AND IsDeleted = '0' ", formSn);
    }

    /**
     * 获取审批方式
     */
    public Integer getAuditWayByCformSn(String cformsn) {
        return queryInt("SELECT iType FROM Bd_AuditFormConfig WHERE cFormSn = ? AND isEnabled = '1' AND IsDeleted = '0' ", cformsn);
    }
    
}
