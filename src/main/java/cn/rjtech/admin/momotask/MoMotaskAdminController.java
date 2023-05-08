package cn.rjtech.admin.momotask;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MoMotask;
/**
 * 制造工单任务 Controller
 * @ClassName: MoMotaskAdminController
 * @author: chentao
 * @date: 2023-04-28 15:19
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
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
