package cn.rjtech.admin.approvalform;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.ApprovalForm;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 审批流表单 Service
 *
 * @ClassName: ApprovalFormService
 * @author: RJ
 * @date: 2023-04-18 17:14
 */
public class ApprovalFormService extends BaseService<ApprovalForm> {

    private final ApprovalForm dao = new ApprovalForm().dao();

    @Override
    protected ApprovalForm dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<ApprovalForm> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 后台管理分页查询
     */
    public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {

        String iAutoId = kv.getStr("approvalM.iautoid");

        Kv para = new Kv();
        para.set("id", iAutoId != null ? iAutoId : " ");
        return dbTemplate("approvalform.pageList", para).paginate(pageNumber, pageSize);
    }

    /**
     * 保存
     */
    public Ret save(ApprovalForm approvalForm) {
        if (approvalForm == null || isOk(approvalForm.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(approvalForm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = approvalForm.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(approvalForm.getIautoid(), JBoltUserKit.getUserId(), approvalForm.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(ApprovalForm approvalForm) {
        if (approvalForm == null || notOk(approvalForm.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        ApprovalForm dbApprovalForm = findById(approvalForm.getIAutoId());
        if (dbApprovalForm == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(approvalForm.getName(), approvalForm.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = approvalForm.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(approvalForm.getIautoid(), JBoltUserKit.getUserId(), approvalForm.getName());
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
     * @param approvalForm 要删除的model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(ApprovalForm approvalForm, Kv kv) {
        //addDeleteSystemLog(approvalForm.getIautoid(), JBoltUserKit.getUserId(),approvalForm.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param approvalForm 要删除的model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(ApprovalForm approvalForm, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(approvalForm, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 获取存货数据
     */
    public Page<Record> resourceList(Kv kv, int pageNum, int pageSize) {
        return dbTemplate("approvalform.resourceList", kv).paginate(pageNum, pageSize);
    }
    
}
