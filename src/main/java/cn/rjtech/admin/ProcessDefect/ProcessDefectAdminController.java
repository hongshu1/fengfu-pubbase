package cn.rjtech.admin.ProcessDefect;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.SpecMaterialsRcvM.SpecMaterialsRcvMService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ProcessDefect;
import cn.rjtech.model.momdata.SpecMaterialsRcvM;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Okv;

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

	@Inject
	private SpecMaterialsRcvMService specMaterialsRcvMService;

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
		kv.setIfNotNull("cInvCode1", get("cInvCode1"));
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
		ProcessDefect processDefect = service.findById(get("iautoid"));
		SpecMaterialsRcvM specMaterialsRcvM = specMaterialsRcvMService.findById(get("iissueid"));
		set("iautoid", get("iautoid"));
		set("type", get("type"));
		set("iissueid", get("iissueid"));
		if (isNull( get("iautoid"))){
			set("processDefect", processDefect);
			set("specMaterialsRcvM", specMaterialsRcvM);
			render("add2.html");
		}else {
			if (processDefect.getIStatus() == 1) {
				set("processDefect", processDefect);
				set("specMaterialsRcvM", specMaterialsRcvM);
				set("isfirsttime", (processDefect.getIsFirstTime() == true) ? "首发" : "再发");
				set("iresptype", (processDefect.getIRespType() == 1) ? "本工序" : "其他");
				render("add3.html");
			} else if (processDefect.getIStatus() == 2) {
				int getCApproach = Integer.parseInt(processDefect.getCApproach());
				set("capproach", (getCApproach == 1) ? "返修" : "报废");
				set("isfirsttime", (processDefect.getIsFirstTime() == true) ? "首发" : "再发");
				set("iresptype", (processDefect.getIRespType() == 1) ? "本工序" : "其他");
				set("processDefect", processDefect);
				set("specMaterialsRcvM", specMaterialsRcvM);
				render("add4.html");
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
