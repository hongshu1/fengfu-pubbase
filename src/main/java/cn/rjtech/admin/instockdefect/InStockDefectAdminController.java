package cn.rjtech.admin.instockdefect;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.instockqcformm.InStockQcFormMService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InStockDefect;
import cn.rjtech.model.momdata.InStockQcFormM;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Okv;

/**
 * 在库异常记录 Controller
 * @ClassName: InStockDefectAdminController
 * @author: RJ
 * @date: 2023-04-25 14:58
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/InStockDefect", viewPath = "/_view/admin/InStockDefect")
public class InStockDefectAdminController extends BaseAdminController {

	@Inject
	private InStockDefectService service;

	@Inject
	private InStockQcFormMService inStockQcFormMService;

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
		Okv kv = new Okv();
		kv.setIfNotNull("cdocno", get("cdocno"));
		kv.setIfNotNull("imodocid", get("imodocid"));
		kv.setIfNotNull("cinvcode", get("cinvcode"));
		kv.setIfNotNull("cinvcode1", get("cinvcode1"));
		kv.setIfNotNull("cinvname", get("cinvname"));
		kv.setIfNotNull("istatus", get("istatus"));
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
		InStockDefect inStockDefect = service.findById(get("iautoid"));
		InStockQcFormM inStockQcFormM = inStockQcFormMService.findFirst("select t1.*, t2.cInvCode, t2.cInvName, t2.cInvCode1\n" +
				"from PL_InStockQcFormM t1\n" +
				"LEFT JOIN Bd_Inventory t2 ON t2.iAutoId = t1.iInventoryId \n" +
				"where t1.iAutoId = '"+get("iinstockqcformmid")+"'");
		set("iautoid", get("iautoid"));
		set("type", get("type"));
		if (isNull(get("iautoid"))) {
			set("inStockDefect", inStockDefect);
			set("inStockQcFormM", inStockQcFormM);
			render("add2.html");
		} else {
			if (inStockDefect.getIStatus() == 1) {
				set("istatus", (inStockDefect.getIsFirstTime() == true) ? "首发" : "再发");
				set("iresptype", (inStockDefect.getIRespType() == 1) ? "供应商" : (inStockDefect.getIRespType() == 2 ? "工程内":"其他"));
				set("inStockDefect", inStockDefect);
				set("inStockQcFormM", inStockQcFormM);
				render("add3.html");
			} else if (inStockDefect.getIStatus() == 2) {
				int getCApproach = Integer.parseInt(inStockDefect.getCApproach());
				set("capproach", (getCApproach == 1) ? "报废" : (getCApproach == 2 ? "返修":"退货"));
				set("istatus", (inStockDefect.getIsFirstTime() == true) ? "首发" : "再发");
				set("iresptype", (inStockDefect.getIRespType() == 1) ? "供应商" : (inStockDefect.getIRespType() == 2 ? "工程内":"其他"));
				set("inStockDefect", inStockDefect);
				set("inStockQcFormM", inStockQcFormM);
				render("add4.html");
			}
		}


	}

   /**
	* 编辑
	*/
	public void edit() {
		InStockDefect inStockDefect=service.findById(getLong(0)); 
		if(inStockDefect == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("inStockDefect",inStockDefect);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(InStockDefect.class, "inStockDefect")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InStockDefect.class, "inStockDefect")));
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
	* 切换toggleIsFirstTime
	*/
	public void toggleIsFirstTime() {
		renderJson(service.toggleIsFirstTime(getLong(0)));
	}

  /**
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}

	/**
	 * 保存与更新
	 */
	public void updateEditTable() {
		renderJson(service.updateEditTable(getKv()));
	}

	/**
	 * 生成二维码
	 */
	public void erm() {
		InStockDefect inStockDefect=service.findById(getLong(0));
		if(inStockDefect == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		renderQrCode(inStockDefect.getCDocNo(),500,600);
	}

}
