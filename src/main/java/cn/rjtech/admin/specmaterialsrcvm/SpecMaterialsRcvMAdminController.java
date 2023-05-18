package cn.rjtech.admin.specmaterialsrcvm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SpecMaterialsRcvM;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 制造工单-特殊领料单主表 Controller
 * @ClassName: SpecMaterialsRcvMAdminController
 * @author: RJ
 * @date: 2023-04-24 10:24
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/SpecMaterialsRcvM", viewPath = "/_view/admin/SpecMaterialsRcvM")
public class SpecMaterialsRcvMAdminController extends BaseAdminController {

	@Inject
	private SpecMaterialsRcvMService service;

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
		render("add().html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		SpecMaterialsRcvM specMaterialsRcvM=service.findById(getLong(0)); 
		if(specMaterialsRcvM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("specMaterialsRcvM",specMaterialsRcvM);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(SpecMaterialsRcvM.class, "specMaterialsRcvM")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(SpecMaterialsRcvM.class, "specMaterialsRcvM")));
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
