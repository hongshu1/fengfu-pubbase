package cn.rjtech.admin.mopick;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
/**
 * 材料耗用 Controller
 * @ClassName: MoMopickitemdBatchAdminController
 * @author: RJ
 * @date: 2023-05-09 15:36
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/mopick", viewPath = "/_view/admin/mopick")
public class MoMopickitemdBatchAdminController extends BaseAdminController {

	@Inject
	private MoMopickitemdBatchService service;

   /**
	* 首页
	*/
	public void index() {
		render("index().html");
	}
  	
  	/**
	* 数据源
	*/
	public void datas() {
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKeywords()));
	}


}
