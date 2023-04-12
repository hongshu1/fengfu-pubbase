package cn.rjtech.common.vouchrollbackref;

import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.common.model.VouchRollBackRef;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 回滚参照信息 Controller
 *
 * @ClassName: VouchRollBackRefAdminController
 * @author: lidehui
 * @date: 2023-01-31 16:36
 */
@CheckPermission(PermissionKey.NONE)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/vouchrollbackref", viewPath = "/_view/admin/vouchrollbackref")
public class VouchRollBackRefAdminController extends BaseAdminController {

	@Inject
	private VouchRollBackRefService service;

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
		VouchRollBackRef vouchRollBackRef=service.findById(getLong(0));
		if(vouchRollBackRef == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("vouchRollBackRef",vouchRollBackRef);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(VouchRollBackRef.class, "vouchRollBackRef")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(VouchRollBackRef.class, "vouchRollBackRef")));
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
