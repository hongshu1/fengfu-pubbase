package cn.rjtech.admin.apsweekscheduledqty;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ApsWeekscheduledQty;
/**
 * 月周生产计划排产明细数量 Controller
 * @ClassName: ApsWeekscheduledQtyAdminController
 * @author: chentao
 * @date: 2023-04-21 10:52
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/apsweekscheduledqty", viewPath = "/_view/admin/apsweekscheduledqty")
public class ApsWeekscheduledQtyAdminController extends BaseAdminController {

	@Inject
	private ApsWeekscheduledQtyService service;

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
		ApsWeekscheduledQty apsWeekscheduledQty=service.findById(getLong(0)); 
		if(apsWeekscheduledQty == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("apsWeekscheduledQty",apsWeekscheduledQty);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(ApsWeekscheduledQty.class, "apsWeekscheduledQty")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(ApsWeekscheduledQty.class, "apsWeekscheduledQty")));
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
	* 切换toggleIsWeekend
	*/
	public void toggleIsWeekend() {
		renderJson(service.toggleIsWeekend(getLong(0)));
	}

  /**
	* 切换toggleIsModified
	*/
	public void toggleIsModified() {
		renderJson(service.toggleIsModified(getLong(0)));
	}

  /**
	* 切换toggleIsLocked
	*/
	public void toggleIsLocked() {
		renderJson(service.toggleIsLocked(getLong(0)));
	}


}
