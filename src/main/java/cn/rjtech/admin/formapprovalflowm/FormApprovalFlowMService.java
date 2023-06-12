package cn.rjtech.admin.formapprovalflowm;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.FormApprovalFlowM;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * 单据审批流 Service
 *
 * @ClassName: FormApprovalFlowMService
 * @author: RJ
 * @date: 2023-05-05 10:19
 */
public class FormApprovalFlowMService extends BaseService<FormApprovalFlowM> {

    private final FormApprovalFlowM dao = new FormApprovalFlowM().dao();

    @Override
    protected FormApprovalFlowM dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<FormApprovalFlowM> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 保存
     */
    public Ret save(FormApprovalFlowM formApprovalFlowM) {
        if (formApprovalFlowM == null || isOk(formApprovalFlowM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(formApprovalFlowM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = formApprovalFlowM.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(formApprovalFlowM.getIautoid(), JBoltUserKit.getUserId(), formApprovalFlowM.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(FormApprovalFlowM formApprovalFlowM) {
        if (formApprovalFlowM == null || notOk(formApprovalFlowM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        FormApprovalFlowM dbFormApprovalFlowM = findById(formApprovalFlowM.getIAutoId());
        if (dbFormApprovalFlowM == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(formApprovalFlowM.getName(), formApprovalFlowM.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = formApprovalFlowM.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(formApprovalFlowM.getIautoid(), JBoltUserKit.getUserId(), formApprovalFlowM.getName());
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
     * @param formApprovalFlowM 要删除的model
     * @param kv                携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(FormApprovalFlowM formApprovalFlowM, Kv kv) {
        //addDeleteSystemLog(formApprovalFlowM.getIautoid(), JBoltUserKit.getUserId(),formApprovalFlowM.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param formApprovalFlowM 要删除的model
     * @param kv                携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(FormApprovalFlowM formApprovalFlowM, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(formApprovalFlowM, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    public List<FormApprovalFlowM> findByIapprovalId(long iapprovalid) {
        return find("select * from Bd_FormApprovalFlowM where iApprovalId = ? ", iapprovalid);
    }
    
    public FormApprovalFlowM findFirstByIapprovalDid(long iApprovalDid) {
        return findFirst("select TOP 1 * from Bd_FormApprovalFlowM where iApprovalDid = ? ", iApprovalDid);
    }

}
