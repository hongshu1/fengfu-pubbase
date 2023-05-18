package cn.rjtech.admin.workshiftd;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.model.momdata.Workshiftd;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 生产班次明细 Controller
 *
 * @ClassName: WorkshiftdAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-27 09:25
 */
@CheckPermission(PermissionKey.WORKSHIFTM)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/workshiftd", viewPath = "/_view/admin/workshiftd")
public class WorkshiftdAdminController extends JBoltBaseController {

	@Inject
	private WorkshiftdService service;

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
		renderJsonData(service.list(get("iworkshiftmid")));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add().html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		Workshiftd workshiftd=service.findById(getLong(0));
		if(workshiftd == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("workshiftd",workshiftd);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(Workshiftd.class, "workshiftd")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Workshiftd.class, "workshiftd")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}


}
