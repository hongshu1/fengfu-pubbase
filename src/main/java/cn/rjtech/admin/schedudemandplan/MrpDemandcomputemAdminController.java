package cn.rjtech.admin.schedudemandplan;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MrpDemandcomputem;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 物料需求计划 Controller
 * @ClassName: MrpDemandcomputemAdminController
 * @author: chentao
 * @date: 2023-05-02 10:00
 */
@CheckPermission(PermissionKey.DEMAND_PLAN2)
@UnCheckIfSystemAdmin
@Path(value = "/admin/mrpdemandcomputem", viewPath = "/_view/admin/schedudemandplan")
public class MrpDemandcomputemAdminController extends BaseAdminController {

	@Inject
	private MrpDemandcomputemService service;

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
		MrpDemandcomputem mrpDemandcomputem=service.findById(getLong(0)); 
		if(mrpDemandcomputem == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("mrpDemandcomputem",mrpDemandcomputem);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MrpDemandcomputem.class, "mrpDemandcomputem")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MrpDemandcomputem.class, "mrpDemandcomputem")));
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
