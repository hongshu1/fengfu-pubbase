package cn.rjtech.admin.StockoutDefect;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.stockoutdefect.StockoutDefectService;
import cn.rjtech.admin.stockoutqcformm.StockoutQcFormMService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.StockoutDefect;
import cn.rjtech.model.momdata.StockoutQcFormM;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Okv;

/**
 * 出库异常记录 Controller
 * @ClassName: StockoutDefectAdminController
 * @author: RJ
 * @date: 2023-04-25 09:26
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/StockoutDefect", viewPath = "/_view/admin/StockoutDefect")
public class StockoutDefectAdminController extends BaseAdminController {

	@Inject
	private StockoutDefectService service;

	@Inject
	private StockoutQcFormMService stockoutQcFormMService;

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
		kv.setIfNotNull("cDocNo", get("cDocNo"));
		kv.setIfNotNull("iMoDocId", get("iMoDocId"));
		kv.setIfNotNull("cInvCode", get("cInvCode"));
		kv.setIfNotNull("cInvCode1", get("cInvCode1"));
		kv.setIfNotNull("cInvName", get("cInvName"));
		if (isNull(get("iStatus"))){
			kv.setIfNotNull("iStatus", get(0));
		}else {
			kv.setIfNotNull("iStatus", get("iStatus"));
		}
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
		StockoutDefect stockoutDefect = service.findById(get("iautoid"));
		StockoutQcFormM stockoutQcFormM = stockoutQcFormMService.findFirst("select t1.*, t2.cInvCode, t2.cInvName, t2.cInvCode1, t3.cCusName\n" +
				"from PL_StockoutQcFormM t1\n" +
				"LEFT JOIN Bd_Inventory t2 ON t2.iAutoId = t1.iInventoryId \n" +
				"LEFT JOIN Bd_Customer t3 ON t3.iAutoId = t1.iCustomerId \n" +
				"where t1.iAutoId = '"+get("istockoutqcformmid")+"'");
		set("iautoid", get("iautoid"));
		set("type", get("type"));
		if (isNull(get("iautoid"))) {
			set("stockoutDefect", stockoutDefect);
			set("stockoutQcFormM", stockoutQcFormM);
			render("add2.html");
		} else {
			if (stockoutDefect.getIStatus() == 1) {
				set("istatus", (stockoutDefect.getIsFirstTime() == true) ? "首发" : "再发");
				set("iresptype", (stockoutDefect.getIRespType() == 1) ? "供应商" : (stockoutDefect.getIRespType() == 2 ? "工程内":"其他"));
				set("stockoutDefect", stockoutDefect);
				set("stockoutQcFormM", stockoutQcFormM);
				render("add3.html");
			} else if (stockoutDefect.getIStatus() == 2) {
				int getCApproach = Integer.parseInt(stockoutDefect.getCApproach());
				set("capproach", (getCApproach == 1) ? "报废" : (getCApproach == 2 ? "返修":"退货"));
				set("istatus", (stockoutDefect.getIsFirstTime() == true) ? "首发" : "再发");
				set("iresptype", (stockoutDefect.getIRespType() == 1) ? "供应商" : (stockoutDefect.getIRespType() == 2 ? "工程内":"其他"));
				set("stockoutDefect", stockoutDefect);
				set("stockoutQcFormM", stockoutQcFormM);
				render("add4.html");
			}
		}


	}

	/**
	 * 编辑
	 */
	public void edit() {
		StockoutDefect stockoutDefect=service.findById(getLong(0));
		if(stockoutDefect == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("stockoutDefect",stockoutDefect);
		render("edit.html");
	}

	/**
	 * 保存
	 */
	public void save() {
		renderJson(service.save(getModel(StockoutDefect.class, "stockoutDefect")));
	}

	/**
	 * 更新
	 */
	public void update() {
		renderJson(service.update(getModel(StockoutDefect.class, "stockoutDefect")));
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
		renderJson(service.updateEditTable(getJBoltTable(), getKv()));
	}

	/**
	 * 生成二维码
	 */
	public void erm() {
		StockoutDefect stockoutDefect=service.findById(getLong(0));
		if(stockoutDefect == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		renderQrCode(stockoutDefect.getCDocNo(),500,600);
	}

}
