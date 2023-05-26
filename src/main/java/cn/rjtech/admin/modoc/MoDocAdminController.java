package cn.rjtech.admin.modoc;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventoryroutingconfig.InventoryRoutingConfigService;
import cn.rjtech.admin.inventoryroutingequipment.InventoryRoutingEquipmentService;
import cn.rjtech.admin.inventoryroutinginvc.InventoryRoutingInvcService;
import cn.rjtech.admin.inventoryroutingsop.InventoryRoutingSopService;
import cn.rjtech.admin.moroutingconfig.MoMoroutingconfigService;
import cn.rjtech.admin.otherout.OtherOutService;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.BillNoUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import cn.jbolt.core.base.JBoltMsg;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;

import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 工单管理 Controller
 * @ClassName: MoDocAdminController
 * @author: RJ
 * @date: 2023-04-26 16:15
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/modoc", viewPath = "/_view/admin/modoc")
public class MoDocAdminController extends BaseAdminController {

	@Inject
	private MoDocService service;
	@Inject
	private InventoryService inventoryService; //存货档案
	@Inject
	private InventoryRoutingEquipmentService inventoryRoutingEquipmentService;//物料-设备
	@Inject
	private MoMoroutingconfigService moMoroutingconfigService;
	@Inject
	private InventoryRoutingConfigService inventoryRoutingConfigService;//物料-工艺配置
	@Inject
	private InventoryRoutingSopService inventoryRoutingSopService; //物料建模-工艺说明书
	@Inject
	private InventoryRoutingInvcService inventoryRoutingInvcService; //物料建模-物料

	@Inject
	private OtherOutService otherOutService;//特殊领料单


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
	public void add() {
		render("add.html");
	}

	/**
	 * 查看
	 */
	public void details() {
		//edit();
		MoDoc moDoc=service.findById(getLong(0));
		if(moDoc == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		Record moRecod=moDoc.toRecord();
		if(isOk(moDoc.getIInventoryId())){
			Inventory inventory = inventoryService.findById(moDoc.getIInventoryId());
			if(inventory!=null){
			  moRecod.set("cinvcode",inventory.getCInvCode());
			}
		}
		set("moDoc",moRecod);
		//拼上生产任务数据
		HashMap<String, String> stringStringHashMap = service.getJob(getLong(0));
		set("productionTasks",stringStringHashMap);
		// 拼上 科系名称,存货编码,客户部番,部品名称,生产单位,规格,产线名称,班次名称,工艺路线名称
		render("_detailsform.html");
	}

	public void delete(){
		MoDoc moDoc=service.findById(getLong(0));
		if(moDoc == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		renderJson(service.updateStatus(moDoc));
	}
	public  void  checkmaterialpage(){
		render("checkmaterialpage.html");
	}


	/**
	 * 新增
	 */
	public void edit() {
		MoDoc moDoc=service.findById(getLong(0));
		if(moDoc == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}

		if(isOk(moDoc.getIInventoryId())){
			Inventory inventory = inventoryService.findById(moDoc.getIInventoryId());
			if(inventory!=null){
				Record moRecod=moDoc.toRecord();
				moRecod.set("cinvcode",inventory.getCInvCode());
				set("moDoc",moRecod);
				render("edit.html");
				return;
			}
		}
		set("moDoc",moDoc);
		render("edit.html");
	}

	/**
	 * 保存
	 */
	public void save() {

		renderJson(service.save(getJBoltTable()));
	}

	/**
	 * 保存
	 * @param rowid
	 */
	public void saveDoc(String   rowid) {

		renderJson(service.saveDoc(getJBoltTable(),rowid));
	}

	/**
	 * 提交
	 * @param rowid
	 */
	public void subSave(String   rowid) {

		renderJson(service.subSave(getJBoltTable(),rowid));
	}

	/**
	 * 关闭
	 * @param rowid
	 */
	public void stopSave(String rowid) {

		renderJson(service.stopSave(getJBoltTable(),rowid));
	}
	/**
	 * 人员信息
	 */
	public void persondialog(){
		//operation
		set("configid",getLong("iautoid"));
		set("imdocid",getLong("imdocid"));
		render("opindex.html");
		//render("persondialog.html");
	}

	/**
	 * 人员信息操作
	 */
	public void personoperationdialog(){
		//operation
		set("configid",getLong("iautoid"));
		set("imdocid",getLong("imdocid"));
		render("person_dialog_index.html");
		//render("persondialog.html");
	}

	/**
	 * 物料
	 */
	public void invc_dialog_index(){
		set("configid",getLong("iautoid"));
		set("iinventoryid",getLong("iinventoryid"));
		set("imdocid",getLong("imdocid"));
		render("invc_dialog_index.html");
	}

	/**
	 * 设备
	 */
	public void equipment_dialog_index(){
		set("configid",getLong("iautoid"));
		set("imdocid",getLong("imdocid"));
		render("equipment_dialog_index.html");
	}

	/**
	 * 工艺文件
	 */
	public void drawing_dialog_index() {
		set("configid", getLong("iautoid"));
		set("imdocid",getLong("imdocid"));
		render("drawing_dialog_index.html");

	}
	/**
	 * 更新
	 */
	public void update() {
		renderJson(service.update(getModel(MoDoc.class, "moDoc")));
	}

	public void getMoPlanNo() {

		renderJsonData(service.generateBarCode());
	}

	public void getDocdetail() {
		Long imodocid=getLong("imodocid");
		Long iinventoryroutingid=getLong("iinventoryroutingid");
		renderJsonData(service.getDocdetail(imodocid,iinventoryroutingid));
	}

	/**
	 * 物料
	 * @param configid
	 * @param imdocid
	 */
	public void  inventoryroutinginvcDataList(Long configid,Long imdocid){

		if(!isOk(imdocid)) {
			List<Record> recordList = inventoryRoutingInvcService.dataList(Kv.by("configid", configid));
			if (recordList.isEmpty()) {
				MoMoroutingconfig moMoroutingconfig = moMoroutingconfigService.findById(configid);
				if (moMoroutingconfig != null) {

					InventoryRoutingConfig inventoryRoutingConfig = inventoryRoutingConfigService.findFirst(Okv.create().
									set(InventoryRoutingConfig.IINVENTORYROUTINGID, moMoroutingconfig.getIInventoryRoutingId())
									.set(InventoryRoutingConfig.COPERATIONNAME, moMoroutingconfig.getCOperationName()),
							InventoryRoutingConfig.IAUTOID, "desc");
					recordList = inventoryRoutingInvcService.dataList(Kv.by("configid", inventoryRoutingConfig.getIAutoId()));
				}
				renderJsonData(recordList);
				return;
			}
		}
		MoMoroutingconfig moMoroutingconfig=moMoroutingconfigService.findById(configid);
		if(moMoroutingconfig!=null){

			InventoryRoutingConfig inventoryRoutingConfig=inventoryRoutingConfigService.findFirst(Okv.create().
							set(InventoryRoutingConfig.IINVENTORYROUTINGID,moMoroutingconfig.getIInventoryRoutingId())
							.set(InventoryRoutingConfig.COPERATIONNAME,moMoroutingconfig.getCOperationName()),
					InventoryRoutingConfig.IAUTOID,"desc");
			renderJsonData(inventoryRoutingInvcService.dataList(Kv.by("configid",inventoryRoutingConfig.getIAutoId())));
			return;
		}else{
			renderJsonData(inventoryRoutingInvcService.dataList(Kv.by("configid",configid)));
		}
	}

	/**
	 * 工艺文件
	 * @param configid
	 * @param imdocid
	 */
	public void  inventoryroutingsopDataList(Long configid,Long imdocid){
		if(!isOk(imdocid)) {
			List<InventoryRoutingSop> recordList = inventoryRoutingSopService.dataList(configid);

			if (recordList.isEmpty()) {
				MoMoroutingconfig moMoroutingconfig = moMoroutingconfigService.findById(configid);
				if (moMoroutingconfig != null) {

					InventoryRoutingConfig inventoryRoutingConfig = inventoryRoutingConfigService.findFirst(Okv.create().
									set(InventoryRoutingConfig.IINVENTORYROUTINGID, moMoroutingconfig.getIInventoryRoutingId())
									.set(InventoryRoutingConfig.COPERATIONNAME, moMoroutingconfig.getCOperationName()),
							InventoryRoutingConfig.IAUTOID, "desc");
					recordList = inventoryRoutingSopService.dataList(inventoryRoutingConfig.getIAutoId());
				}
				renderJsonData(recordList);
				return;
			}
		}
		MoMoroutingconfig moMoroutingconfig=moMoroutingconfigService.findById(configid);
		if(moMoroutingconfig!=null){

			InventoryRoutingConfig inventoryRoutingConfig=inventoryRoutingConfigService.findFirst(Okv.create().
							set(InventoryRoutingConfig.IINVENTORYROUTINGID,moMoroutingconfig.getIInventoryRoutingId())
							.set(InventoryRoutingConfig.COPERATIONNAME,moMoroutingconfig.getCOperationName()),
					InventoryRoutingConfig.IAUTOID,"desc");
			renderJsonData(inventoryRoutingSopService.dataList(inventoryRoutingConfig.getIAutoId()));
			return;
		}else{
			renderJsonData(inventoryRoutingSopService.dataList(configid));
		}
	}

	/**
	 * 获取设备
	 * @param configid
	 * @param imdocid
	 */
	public void inventoryroutingequipmentDataList(Long configid,Long imdocid){
		if(!isOk(imdocid)) {
			List<Record> recordList = inventoryRoutingEquipmentService.dataList(Kv.by("configid", configid));
			if (recordList.isEmpty()) {
				MoMoroutingconfig moMoroutingconfig = moMoroutingconfigService.findById(configid);
				if (moMoroutingconfig != null) {

					InventoryRoutingConfig inventoryRoutingConfig = inventoryRoutingConfigService.findFirst(Okv.create().
									set(InventoryRoutingConfig.IINVENTORYROUTINGID, moMoroutingconfig.getIInventoryRoutingId())
									.set(InventoryRoutingConfig.COPERATIONNAME, moMoroutingconfig.getCOperationName()),
							InventoryRoutingConfig.IAUTOID, "desc");
					recordList = inventoryRoutingEquipmentService.dataList(Kv.by("configid", inventoryRoutingConfig.getIAutoId()));
				}
				renderJsonData(recordList);
				return;
			}
		}
		MoMoroutingconfig moMoroutingconfig=moMoroutingconfigService.findById(configid);
		if(moMoroutingconfig!=null){

			InventoryRoutingConfig inventoryRoutingConfig=inventoryRoutingConfigService.findFirst(Okv.create().
							set(InventoryRoutingConfig.IINVENTORYROUTINGID,moMoroutingconfig.getIInventoryRoutingId())
							.set(InventoryRoutingConfig.COPERATIONNAME,moMoroutingconfig.getCOperationName()),
					InventoryRoutingConfig.IAUTOID,"desc");
			renderJsonData(inventoryRoutingEquipmentService.dataList(Kv.by("configid",inventoryRoutingConfig.getIAutoId())));
			return;
		}else{
			renderJsonData(inventoryRoutingEquipmentService.dataList(Kv.by("configid",configid)));
		}
	}

	/**
	 * 特殊领料界面
	 */
       public void  othermaterialindex(){
		set("imdocid",getLong("imdocid"));
		render("other_dialog_index.html");
	   }
	/**
	 * 特殊领料界面-新增
	 */
	public void  othermaterialadd(){
		OtherOut otherOut = new OtherOut();
		String billNo = BillNoUtils.getcDocNo(getOrgId(), "LLD", 5);
		Date nowDate = new Date();
		otherOut.setBillNo(billNo);
		otherOut.setBillDate(nowDate);
		otherOut.setSourceBillType("生产工单");
		otherOut.setType("OtherOutMES");
		set("otherOut",otherOut);

		set("imdocid",getLong("imdocid"));
		render("other_dialog_add.html");
	}
	/**
	 * 特殊领料界面-编辑
	 */
	public void  othermaterialedit(){
		OtherOut otherOut=otherOutService.findById(getLong(0));
		if(otherOut == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("otherOut",otherOut);
		set("type", get("type"));
		render("edit.html");
		set("imdocid",getLong("imdocid"));
		render("other_dialog_edit.html");
	}
	/**
	 * 特殊领料界面-审批
	 */
	public void othermaterialapprove(String iAutoId,Integer mark) {
		if (StringUtils.isEmpty(iAutoId)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		renderJson(otherOutService.approve(iAutoId,mark));
	}
	/**
	 * 特殊领料界面-手动关闭
	 */
	public void closeWeekOrder(String iAutoId) {
		if (StringUtils.isEmpty(iAutoId)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		renderJson(otherOutService.closeWeekOrder(iAutoId));
	}

	/**
	 * 特殊领料界面-撤回
	 */
	public void recall(String iAutoId) {
		if (StringUtils.isEmpty(iAutoId)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		renderJson(otherOutService.recall(iAutoId));
	}
}
