package cn.rjtech.admin.MoDoc;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MoDoc;
/**
 * 在库检 Controller
 * @ClassName: MoDocAdminController
 * @author: RJ
 * @date: 2023-04-26 16:15
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/MoDoc", viewPath = "/_view/admin/MoDoc")
public class MoDocAdminController extends BaseAdminController {

	@Inject
	private MoDocService service;

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
		MoDoc moDoc=service.findById(getLong(0));
		if(moDoc == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moDoc",moDoc);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MoDoc.class, "moDoc")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MoDoc.class, "moDoc")));
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
