package cn.rjtech.admin.ProcessDefect;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ProcessDefect;
/**
 * 制程异常品记录 Controller
 * @ClassName: ProcessDefectAdminController
 * @author: RJ
 * @date: 2023-04-21 15:49
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/ProcessDefect", viewPath = "/_view/admin/ProcessDefect")
public class ProcessDefectAdminController extends BaseAdminController {

	@Inject
	private ProcessDefectService service;

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
		Okv kv =new Okv();
		kv.setIfNotNull("cDocNo", get("cDocNo"));
		kv.setIfNotNull("iMoDocId", get("iMoDocId"));
		kv.setIfNotNull("iInventoryId", get("iInventoryId"));
		kv.setIfNotNull("cInvName", get("cInvName"));
		kv.setIfNotNull("cInvCode", get("cInvCode"));
		kv.setIfNotNull("iStatus", get("iStatus"));
		kv.setIfNotNull("startdate", get("startdate"));
		kv.setIfNotNull("enddate", get("enddate"));
		renderJsonData(service.paginateAdminDatas(getPageSize(), getPageNumber(), kv));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add.html");
	}



	public void add2() {
		set("iautoid", get("iautoid"));
		set("type", get("type"));
		set("iissueid", get("iissueid"));
		if (isNull( get("iautoid"))){
//			Mo_SpecMaterialsRcvM
//			 SpecMaterialsRcvm = service.findById(get("iissueid"));
			render("add2.html");
		}else {
			ProcessDefect processDefect = service.findById(get("iautoid"));
			if (processDefect.getIStatus() == 1) {
				set("processDefect", processDefect);
				render("add2.html");
			} else if (processDefect.getIStatus() == 2) {
				int getCApproach = Integer.parseInt(processDefect.getCApproach());
				set("capproach", (getCApproach == 1) ? "返修" : "报废");
				set("isfirsttime", (processDefect.getIsFirstTime() == true) ? "首发" : "再发");
				set("iresptype", (processDefect.getIRespType() == 1) ? "本工序" : "其他");
				set("processDefect", processDefect);
				render("add3.html");
			}
		}

	}

   /**
	* 编辑
	*/
	public void edit() {
		ProcessDefect processDefect=service.findById(getLong(0)); 
		if(processDefect == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("processDefect",processDefect);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(ProcessDefect.class, "processDefect")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(ProcessDefect.class, "processDefect")));
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


	public void ProcessDefectupdateEditTable(){
		renderJson(service.updateEditTable(getJBoltTable(), getKv()));
	}


}
