package cn.rjtech.common.columsmapdetail;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.common.model.Columsmapdetail;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 字段映射 Controller
 *
 * @ClassName: ColumsmapdetailAdminController
 * @author: lidehui
 * @date: 2023-02-01 11:09
 */
@CheckPermission(PermissionKey.NONE)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/columsmapdetail", viewPath = "/_view/admin/columsmapdetail")
public class ColumsmapdetailAdminController extends BaseAdminController {

	@Inject
	private ColumsmapdetailService service;

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
		Columsmapdetail columsmapdetail=service.findById(getLong(0));
		if(columsmapdetail == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("columsmapdetail",columsmapdetail);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(Columsmapdetail.class, "columsmapdetail")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Columsmapdetail.class, "columsmapdetail")));
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
