package cn.rjtech.admin.formapprovaldroleusers;

import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.FormapprovaldRoleusers;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * 单据角色人员 Service
 *
 * @ClassName: FormapprovaldRoleusersService
 * @author: RJ
 * @date: 2023-06-05 14:32
 */
public class FormapprovaldRoleusersService extends BaseService<FormapprovaldRoleusers> {

    private final FormapprovaldRoleusers dao = new FormapprovaldRoleusers().dao();

    @Override
    protected FormapprovaldRoleusers dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<FormapprovaldRoleusers> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
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
     * @param formapprovaldRoleusers 要删除的model
     * @param kv                     携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(FormapprovaldRoleusers formapprovaldRoleusers, Kv kv) {
        //addDeleteSystemLog(formapprovaldRoleusers.getIautoid(), JBoltUserKit.getUserId(),formapprovaldRoleusers.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param formapprovaldRoleusers 要删除的model
     * @param kv                     携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(FormapprovaldRoleusers formapprovaldRoleusers, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(formapprovaldRoleusers, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    public List<FormapprovaldRoleusers> findByIformApprovaldRoleId(long iFormApprovaldRoleId) {
        return find("SELECT * FROM Bd_FormApprovalD_RoleUsers WHERE iFormApprovaldRoleId = ? ORDER BY iSeq ASC", iFormApprovaldRoleId);
    }

}
