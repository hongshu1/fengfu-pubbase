package cn.rjtech.admin.moroutingconfigoperation;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MoMoroutingconfigOperation;
/**
 * 制造工单-存货工艺配置工序 Controller
 * @ClassName: MoMoroutingconfigOperationAdminController
 * @author: RJ
 * @date: 2023-05-09 16:49
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/moroutingconfigoperation", viewPath = "/_view/admin/moroutingconfigoperation")
public class MoMoroutingconfigOperationAdminController extends BaseAdminController {

	@Inject
	private MoMoroutingconfigOperationService service;

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
		MoMoroutingconfigOperation moMoroutingconfigOperation=service.findById(getLong(0));
		if(moMoroutingconfigOperation == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moMoroutingconfigOperation",moMoroutingconfigOperation);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MoMoroutingconfigOperation.class, "moMoroutingconfigOperation")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MoMoroutingconfigOperation.class, "moMoroutingconfigOperation")));
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
