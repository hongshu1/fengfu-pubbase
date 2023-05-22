package cn.rjtech.admin.momoroutingsop;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MoMoroutingsop;
/**
 *  Controller
 * @ClassName: MoMoroutingsopAdminController
 * @author: RJ
 * @date: 2023-05-20 11:49
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/momoroutingsop", viewPath = "/_view/admin/momoroutingsop")
public class MoMoroutingsopAdminController extends BaseAdminController {

	@Inject
	private MoMoroutingsopService service;

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
		MoMoroutingsop moMoroutingsop=service.findById(getLong(0)); 
		if(moMoroutingsop == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moMoroutingsop",moMoroutingsop);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MoMoroutingsop.class, "moMoroutingsop")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MoMoroutingsop.class, "moMoroutingsop")));
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

	public void dataList() {
		renderJsonData(service.dataList(getKv()));
	}


}
