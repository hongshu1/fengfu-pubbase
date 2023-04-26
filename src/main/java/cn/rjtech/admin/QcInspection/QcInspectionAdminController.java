package cn.rjtech.admin.QcInspection;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.QcInspection;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 工程内品质巡查 Controller
 * @ClassName: QcInspectionAdminController
 * @author: RJ
 * @date: 2023-04-26 13:49
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/QcInspection", viewPath = "/_view/admin/QcInspection")
public class QcInspectionAdminController extends BaseAdminController {

	@Inject
	private QcInspectionService service;

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
		QcInspection qcInspection=service.findById(getLong(0)); 
		if(qcInspection == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("qcInspection",qcInspection);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(QcInspection.class, "qcInspection")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(QcInspection.class, "qcInspection")));
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

  /**
	* 切换toggleIsFirstCase
	*/
	public void toggleIsFirstCase() {
		renderJson(service.toggleIsFirstCase(getLong(0)));
	}


}
