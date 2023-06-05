package cn.rjtech.admin.scanlog;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ScanLog;
/**
 * 扫描日志 Controller
 * @ClassName: ScanLogAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-05 10:00
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/scanlog", viewPath = "/_view/admin/scanlog")
public class ScanLogAdminController extends BaseAdminController {

	@Inject
	private ScanLogService service;

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
		ScanLog scanLog=service.findById(getInt(0)); 
		if(scanLog == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("scanLog",scanLog);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(ScanLog.class, "scanLog")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(ScanLog.class, "scanLog")));
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
		renderJson(service.delete(getInt(0)));
	}


}
