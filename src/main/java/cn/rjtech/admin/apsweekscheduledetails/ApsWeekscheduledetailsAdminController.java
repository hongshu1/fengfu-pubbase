package cn.rjtech.admin.apsweekscheduledetails;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ApsWeekscheduledetails;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 月周生产计划排产明细 Controller
 * @ClassName: ApsWeekscheduledetailsAdminController
 * @author: chentao
 * @date: 2023-04-21 10:51
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/apsweekscheduledetails", viewPath = "/_view/admin/apsweekscheduledetails")
public class ApsWeekscheduledetailsAdminController extends BaseAdminController {

	@Inject
	private ApsWeekscheduledetailsService service;

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
		ApsWeekscheduledetails apsWeekscheduledetails=service.findById(getLong(0)); 
		if(apsWeekscheduledetails == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("apsWeekscheduledetails",apsWeekscheduledetails);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(ApsWeekscheduledetails.class, "apsWeekscheduledetails")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(ApsWeekscheduledetails.class, "apsWeekscheduledetails")));
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
