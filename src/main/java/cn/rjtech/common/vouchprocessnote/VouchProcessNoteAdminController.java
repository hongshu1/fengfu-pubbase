package cn.rjtech.common.vouchprocessnote;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.common.model.VouchProcessNote;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * Controller
 *
 * @ClassName: VouchProcessNoteAdminController
 * @author: lidehui
 * @date: 2023-01-30 17:15
 */
@CheckPermission(PermissionKey.NONE)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/vouchprocessnote", viewPath = "/_view/admin/vouchprocessnote")
public class VouchProcessNoteAdminController extends BaseAdminController {

	@Inject
	private VouchProcessNoteService service;

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
		VouchProcessNote vouchProcessNote=service.findById(getLong(0));
		if(vouchProcessNote == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("vouchProcessNote",vouchProcessNote);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(VouchProcessNote.class, "vouchProcessNote")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(VouchProcessNote.class, "vouchProcessNote")));
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
