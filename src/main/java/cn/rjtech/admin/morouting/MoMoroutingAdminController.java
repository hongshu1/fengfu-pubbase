package cn.rjtech.admin.morouting;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
/**
 * 制造工单-生产工艺路线 Controller
 * @ClassName: MoMoroutingAdminController
 * @author: RJ
 * @date: 2023-05-09 16:38
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/morouting", viewPath = "/_view/admin/morouting")
public class MoMoroutingAdminController extends BaseAdminController {

	@Inject
	private MoMoroutingService service;

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
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKeywords()));
	}


}
