package cn.rjtech.admin.mrpdemandplanm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MrpDemandplanm;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 物料到货计划主表 Controller
 * @ClassName: MrpDemandplanmAdminController
 * @author: chentao
 * @date: 2023-05-06 15:17
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/mrpdemandplanm", viewPath = "/_view/admin/mrpdemandplanm")
public class MrpDemandplanmAdminController extends BaseAdminController {

	@Inject
	private MrpDemandplanmService service;

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
		MrpDemandplanm mrpDemandplanm=service.findById(getLong(0)); 
		if(mrpDemandplanm == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("mrpDemandplanm",mrpDemandplanm);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MrpDemandplanm.class, "mrpDemandplanm")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MrpDemandplanm.class, "mrpDemandplanm")));
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
