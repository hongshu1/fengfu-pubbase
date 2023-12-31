package cn.rjtech.admin.momotask;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MoMotask;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 制造工单任务 Controller
 *
 * @ClassName: MoMotaskAdminController
 * @author: chentao
 * @date: 2023-04-28 15:19
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.NONE)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/momotask", viewPath = "/_view/admin/momotask")
public class MoMotaskAdminController extends BaseAdminController {

	@Inject
	private MoMotaskService service;

   /**
	* 首页
	*/
	public void index() {
		render("index.html");
	}

  	/**
	* 数据源
	*/
    @UnCheck
	public void datas() {
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv()));
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
		MoMotask moMotask=service.findById(getLong(0));
		if(moMotask == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moMotask",moMotask);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MoMotask.class, "moMotask")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MoMotask.class, "moMotask")));
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
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}


}
