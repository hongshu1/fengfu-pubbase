package cn.rjtech.admin.uptimem;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.UptimeM;
/**
 * 稼动时间管理 Controller
 * @ClassName: UptimeMAdminController
 * @author: chentao
 * @date: 2023-06-28 10:30
 */
@CheckPermission(PermissionKey.UPTIMEM)
@UnCheckIfSystemAdmin
@Path(value = "/admin/uptimem", viewPath = "/_view/admin/uptimem")
public class UptimeMAdminController extends BaseAdminController {

	@Inject
	private UptimeMService service;

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
   @CheckPermission(PermissionKey.UPTIMEM_ADD)
	public void add() {
		set("updateOrSave","save");
		render("add.html");
	}

   /**
	* 编辑
	*/
   @CheckPermission(PermissionKey.UPTIMEM_EDIT)
	public void edit() {
		UptimeM uptimeM=service.findById(getLong("iautoid"));
		if(uptimeM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("uptimeM",uptimeM);
		set("updateOrSave","update");
		set("readonly",getBoolean("readonly"));

		Record record = service.getUptimeMInfo(Kv.by("mid",getLong("iautoid")));
		set("recordInfo",record);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(UptimeM.class, "uptimeM")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(UptimeM.class, "uptimeM")));
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
   @CheckPermission(PermissionKey.UPTIMEM_DELETE)
	public void delete() {
		renderJson(service.delete(getLong("iautoid")));
	}

  /**
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}

	public void getUptimeTplInfo(){
		renderJsonData(service.getUptimeTplInfo(getKv()),"");
	}

	public void getUptimeDList(){
		renderJsonData(service.getUptimeDList(getKv()),"");
	}

	public void updateAndSave(){
		renderJsonData(service.updateAndSave(getJBoltTable()),"");
	}



	//---------------------app----------------------
	public void getPageList() {
		int pageNumber = getInt("pageNumber");
		int pageSize = getInt("pageSize");
		renderJsonData(service.paginateAdminDatas(pageNumber,pageSize,getKv()));
	}
	public void getUptimeTplInfoList() {
		renderJsonData(service.getUptimeTplInfoList(getKv()));
	}
	public void getUptimeMInfoList() {
		renderJsonData(service.getUptimeMInfoList(getKv()));
	}

	public void updateAndSaveApi() {
		renderJsonData(service.updateAndSaveApi(getInt("updateOrSaveType"),get("data"),get("dataList")));
	}

}
