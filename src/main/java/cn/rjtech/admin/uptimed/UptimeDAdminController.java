package cn.rjtech.admin.uptimed;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.UptimeD;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 稼动时间纪录 Controller
 * @ClassName: UptimeDAdminController
 * @author: chentao
 * @date: 2023-06-28 10:31
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/uptimed", viewPath = "/_view/admin/uptimed")
public class UptimeDAdminController extends BaseAdminController {

	@Inject
	private UptimeDService service;

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
		UptimeD uptimeD=service.findById(getLong(0)); 
		if(uptimeD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("uptimeD",uptimeD);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(UptimeD.class, "uptimeD")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(UptimeD.class, "uptimeD")));
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


}
