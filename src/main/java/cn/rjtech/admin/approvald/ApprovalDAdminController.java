package cn.rjtech.admin.approvald;

import cn.jbolt.core.kit.JBoltUserKit;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ApprovalD;

import java.util.List;

/**
 * 审批流节点 Controller
 * @ClassName: ApprovalDAdminController
 * @author: RJ
 * @date: 2023-04-18 17:04
 */
@CheckPermission(PermissionKey.APPROVAL_CONFIG)
@UnCheckIfSystemAdmin
@Path(value = "/admin/approvald", viewPath = "/_view/admin/approvald")
public class ApprovalDAdminController extends BaseAdminController {

	@Inject
	private ApprovalDService service;

   /**
	* 首页
	*/
	public void index() {
		render("index().html");
	}

  	/**
	* 数据源
	*/
	public void datas()
	{
		String autoid = get("approvalM.iautoid");
		Kv kv = new Kv();
		kv.set("id",autoid==null?' ':autoid);
//		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKeywords()));
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),kv));
	}

   /**
	* 新增
	*/
	public void add() {
		String id = get("id");
		ValidationUtils.isTrue(id!=null, "请先保存审批流配置基础信息");

		List<ApprovalD> listByMid = service.findListByMid(id);

		int size = listByMid.size();
		int iSeq = size+1;

		ApprovalD approvalD = new ApprovalD();
		approvalD.setIApprovalMid(Long.parseLong(id));
		approvalD.setISeq(iSeq);
		set("approvalD",approvalD);
		render("add().html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		ApprovalD approvalD=service.findById(getLong(0));
		if(approvalD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("approvalD",approvalD);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(ApprovalD.class, "approvalD")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(ApprovalD.class, "approvalD")));
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
	 * 选择人员Dialog
	 */
	public void choosePerson(){
		set("itemHidden", get("itemHidden"));
		render("dialog/choose_users.html");
	}

	/**
	 * 选择人员数据源
	 */
	public void getPerson(){
		String key = get("key");
		Kv kv = getKv();
		kv.setIfNotNull("keys", key);
		renderJsonData(service.getPerson(getPageNumber(), getPageSize(), kv));
	}

    /**
     * 选择角色Dialog
     */
    public void chooseRole(){
        set("roleHidden", get("roleHidden"));
        render("dialog/choose_roles.html");
    }

    /**
     * 选择角色数据源
     */
    public void getRole(){
        Kv kv = getKv();
        renderJsonData(service.getRole(getPageNumber(), getPageSize(), kv));
    }

	/**
	 * 下拉框选择人员数据源
	 */
	public void selectPerson(){
		String key = get("key");
		Kv kv = new Kv();
		kv.setIfNotNull("keys", key);
		renderJsonData(service.selectPerson(kv));
	}

	/**
	 * targetDialog 数据源
	 */
	public void returnNull(){
		renderJsonData(null);
	}

	/**
	 * 人员行表数据源
	 */
	public void lineDatas(){
        String Mid = get("approvalD.iautoid");
        Kv kv = new Kv();
        kv.set("mid",Mid==null?' ':Mid);
        renderJsonData(service.lineDatas(getPageNumber(),getPageSize(),kv));
	}

    /**
     * 人员行表数据源
     */
    public void roleDatas(){
        String Mid = get("approvalD.iautoid");
        Kv kv = new Kv();
		kv.set("mid",Mid==null?' ':Mid);
        renderJsonData(service.roleDatas(getPageNumber(),getPageSize(),kv));
    }

    /**
     * 查询role上所有用户列表进入页面
     */
    public void roleUsers() {
        set("roleId", getLong(0));
        render("dialog/users.html");
    }

    /**
     * 保存方法
     */
    public void submit(){
        renderJson(service.submitByJBoltTables(getJBoltTables()));
    }

    /**
     * 排序 上移
     */
    @Before(Tx.class)
    public void up() {
        renderJson(service.up(getLong(0)));
    }

    /**
     * 排序 下移
     */
    @Before(Tx.class)
    public void down() {
        renderJson(service.down(getLong(0)));
    }

}
