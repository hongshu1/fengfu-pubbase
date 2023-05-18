package cn.rjtech.admin.apsannualpland;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ApsAnnualpland;
/**
 * 年度计划 Controller
 * @ClassName: ApsAnnualplandAdminController
 * @author: chentao
 * @date: 2023-04-03 16:31
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/apsannualpland", viewPath = "/_view/admin/apsannualpland")
public class ApsAnnualplandAdminController extends BaseAdminController {

	@Inject
	private ApsAnnualplandService service;

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
		ApsAnnualpland apsAnnualpland=service.findById(getLong(0)); 
		if(apsAnnualpland == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("apsAnnualpland",apsAnnualpland);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(ApsAnnualpland.class, "apsAnnualpland")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(ApsAnnualpland.class, "apsAnnualpland")));
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
