package cn.rjtech.admin.mrpdemandforecastm;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MrpDemandforecastm;
/**
 * 物料需求预示主表 Controller
 * @ClassName: MrpDemandforecastmAdminController
 * @author: chentao
 * @date: 2023-05-06 15:15
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/mrpdemandforecastm", viewPath = "/_view/admin/mrpdemandforecastm")
public class MrpDemandforecastmAdminController extends BaseAdminController {

	@Inject
	private MrpDemandforecastmService service;

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
		MrpDemandforecastm mrpDemandforecastm=service.findById(getLong(0)); 
		if(mrpDemandforecastm == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("mrpDemandforecastm",mrpDemandforecastm);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MrpDemandforecastm.class, "mrpDemandforecastm")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MrpDemandforecastm.class, "mrpDemandforecastm")));
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
