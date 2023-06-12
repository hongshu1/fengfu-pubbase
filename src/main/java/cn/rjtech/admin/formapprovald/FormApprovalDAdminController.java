package cn.rjtech.admin.formapprovald;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.FormApprovalD;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;

/**
 * 表单审批流审批节点 Controller
 *
 * @ClassName: FormApprovalDAdminController
 * @author: RJ
 * @date: 2023-04-18 17:29
 */
@Before(JBoltAdminAuthInterceptor.class)
@UnCheck
@Path(value = "/admin/formapprovald", viewPath = "/_view/admin/formapprovald")
public class FormApprovalDAdminController extends BaseAdminController {

    @Inject
    private FormApprovalDService service;

    /**
     * 首页
     */
    public void index() {
        render("index.html");
    }

    /**
     * 数据源
     */
    public void datas() {
        String iFormObjectId = get("iFormObjectId");
        Kv kv = new Kv();
        kv.set("formId", isOk(iFormObjectId) ? iFormObjectId : ' ');
        renderJsonData(service.datas(getPageNumber(), getPageSize(), kv));
    }

    /**
     * 历史数据源
     */
    public void historyDatas() {
        String approvalId = get("approvalId");
        Kv kv = new Kv();
        kv.set("approvalId", isOk(approvalId) ? approvalId : ' ');
        renderJsonData(service.historyDatas(getPageNumber(), getPageSize(), kv));
    }

    /**
     * 历史详情页
     */
    public void historyDialog(){
        String approvalId = get("approvalId");
        set("approvalId", approvalId);
        render("../approvalm/approval_history_detail.html");
    }

    /**
     * 历史列表
     */
    public void historyList(){
        String iFormObjectId = get("iFormObjectId");
        Kv kv = new Kv();
        kv.set("formId", isOk(iFormObjectId) ? iFormObjectId : ' ');
        renderJsonData(service.historyList(getPageNumber(), getPageSize(), kv));
    }

    /**
     * 新增
     */
    public void add() {
        render("add.html");
    }

    /**
     * 编辑
     */
    public void edit() {
        FormApprovalD approvalD = service.findById(getLong(0));

        if (approvalD == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        Integer approvaling = service.isApprovaling(getLong(0));

        set("approvaling",approvaling);
        set("formApprovalD", approvalD);
        render("edit.html");
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 切换toggleIsDirectOnMissing
     */
    public void toggleIsDirectOnMissing() {
        renderJson(service.toggleIsDirectOnMissing(getLong(0)));
    }

    /**
     * 查询节点上所有审批人进入页面
     */
    public void userList() {
        set("approvalDid", getLong(0));
        System.out.println("getLong(0)" + getLong(0));
        render("users.html");
    }

    /**
     * 审批人列表数据源
     */
    public void userDatas() {
        String approvalDId = get("approvalDid");
        Kv kv = new Kv();
        kv.set("approvalDId", isOk(approvalDId) ? approvalDId : ' ');
        renderJsonData(service.userDatas(getPageNumber(), getPageSize(), kv));
    }

    /**
     * 人员行表数据源
     */
    public void lineDatas() {
        String Mid = get("formApprovalD.iautoid");
        Kv kv = new Kv();
        kv.set("mid", Mid == null ? ' ' : Mid);
        renderJsonData(service.lineDatas(getPageNumber(), getPageSize(), kv));
    }

    /**
     * 人员行表数据源
     */
    public void roleDatas() {
        String Mid = get("formApprovalD.iautoid");
        Kv kv = new Kv();
        kv.set("mid", Mid == null ? ' ' : Mid);
        renderJsonData(service.roleDatas(getPageNumber(), getPageSize(), kv));
    }

    /**
     * 保存方法
     */
    public void submit() {
        renderJson(service.submitByJBoltTables(getJBoltTables()));
    }

    /**
     * 查询role上所有用户列表进入页面
     */
    public void roleUsers() {
        set("autoId", getLong("autoId"));
        set("roleId", getLong("roleId"));
        set("approvaling", getInt("approvaling"));
        render("dialog/users.html");
    }

    /**
     * 选择角色Dialog
     */
    public void chooseRole() {
        set("roleHidden", get("roleHidden"));
        render("dialog/choose_roles.html");
    }

    /**
     * 打开用户Dialog
     */
    public void chooseUsersDialog(){
        Long roleId = getLong("roleId");
        Long aLong = getLong("autoId");
        String itemHidden = get("itemHidden");
        set("itemHidden",itemHidden);
        set("roleId", roleId);
        set("autoId", aLong);
        render("dialog/choose_role_users.html");
    }

    /**
     * 添加角色人员
     */
    public void chooseUsers(){
        Long roleId = getLong("roleId");
        Long aLong = getLong("autoId");
        String itemHidden = get("itemHidden");
        Kv kv = new Kv();
        kv.set("roleId",roleId);
        kv.set("autoId",aLong);
        kv.setIfNotNull("itemHidden",itemHidden);
        renderJsonData(service.chooseUsers(getPageNumber(), getPageSize(),kv));
    }

    /**
     * 角色人员数据源
     */
    public void roleUserData(){
        Long aLong = getLong("autoId");
        Kv kv = new Kv();
        kv.set("id",aLong);
        renderJsonData(service.roleUsers(getPageNumber(), getPageSize(), kv));
    }

    /**
     * 保存角色人员方法
     */
    public void saveRoleUser(){
        renderJson(service.saveRoleUser(getJBoltTable()));
    }
}
