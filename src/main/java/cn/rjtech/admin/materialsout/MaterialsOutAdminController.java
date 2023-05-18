package cn.rjtech.admin.materialsout;

import cn.rjtech.util.BillNoUtils;
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
import cn.rjtech.model.momdata.MaterialsOut;

import java.util.Date;

/**
 * 出库管理-材料出库单列表 Controller
 * @ClassName: MaterialsOutAdminController
 * @author: RJ
 * @date: 2023-05-13 16:16
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/materialDeliveryList", viewPath = "/_view/admin/materialsout")
public class MaterialsOutAdminController extends BaseAdminController {

	@Inject
	private MaterialsOutService service;

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
		Kv kv =new Kv();
		kv.setIfNotNull("selectparam", get("billno"));
		kv.setIfNotNull("selectparam", get("sourcebillno"));
		kv.setIfNotNull("selectparam", get("whname"));
		kv.setIfNotNull("selectparam", get("deptname"));
		kv.setIfNotNull("iorderstatus", get("iorderstatus"));
		kv.setIfNotNull("startdate", get("startdate"));
		kv.setIfNotNull("enddate", get("enddate"));
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),kv));
	}
   /**
	* 新增
	*/
	public void add() {
		MaterialsOut materialsOut = new MaterialsOut();
		String billNo = BillNoUtils.getcDocNo(getOrgId(), "CLCK", 5);
		Date nowDate = new Date();
		materialsOut.setBillNo(billNo);
		materialsOut.setBillDate(nowDate);
		set("materialsOut",materialsOut);
		render("add().html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		MaterialsOut materialsOut=service.findById(getLong(0)); 
		if(materialsOut == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		};
		System.out.println(materialsOut.getSourceBillDid());
		Record MODetail = service.getrcvMODetailList(materialsOut.getSourceBillDid());
		set("type", get("type"));
		set("MODetail",MODetail);
		set("materialsOut",materialsOut);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MaterialsOut.class, "materialsOut")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MaterialsOut.class, "materialsOut")));
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
	 * JBoltTable 可编辑表格整体提交 多表格
	 */
	public void submitMulti(Integer param, String revokeVal ,String autoid) {
		renderJson(service.submitByJBoltTables(getJBoltTables(),param,revokeVal,autoid));
	}

	/**
	 * 审核
	 */
	public void approve(String iAutoId,Integer mark) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(iAutoId)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		renderJson(service.approve(iAutoId,mark));
	}

	/**
	 * 反审核
	 */
	public void NoApprove(String ids) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(ids)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		renderJson(service.NoApprove(ids));
	}


	/**
	 * 撤回
	 */
	public void recall(String iAutoId) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(iAutoId)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		renderJson(service.recall(iAutoId));
	}

	/**
	 * 材料出库单列表明细
	 */
	public void getMaterialsOutLines() {
		String autoid = get("autoid");
		String OrgCode = getOrgCode();
		Kv kv = new Kv();
		kv.set("autoid",autoid== null? "" :autoid);
		kv.set("OrgCode",OrgCode);
		renderJsonData(service.getMaterialsOutLines(getPageNumber(), getPageSize(), kv));
	}

	/**
	 * 生產工單的选择数据Dialog
	 */
	public void chooseSupplierData() {
		render("modetail.html");
	}

	/**
	 * 生產工單数据源
	 */
	public void moDetailData() {
		Kv kv =new Kv();
		kv.setIfNotNull("monorow", get("monorow"));
		renderJsonData(service.moDetailData(getPageNumber(),getPageSize(),kv));
	}

	/**
	 *  收发类别数据源
	 */
	public void RDStyle() {
		String OrgCode = getOrgCode();
		Kv kv =new Kv();
		kv.setIfNotNull("OrgCode", OrgCode);
		kv.setIfNotNull("bRdFlag", get("bRdFlag"));
		renderJsonData(service.getRDStyleDatas(kv));
	}


}
