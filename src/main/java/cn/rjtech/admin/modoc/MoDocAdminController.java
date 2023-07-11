package cn.rjtech.admin.modoc;

import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventoryroutingconfig.InventoryRoutingConfigService;
import cn.rjtech.admin.inventoryroutingequipment.InventoryRoutingEquipmentService;
import cn.rjtech.admin.inventoryroutinginvc.InventoryRoutingInvcService;
import cn.rjtech.admin.inventoryroutingsop.InventoryRoutingSopService;
import cn.rjtech.admin.morouting.MoMoroutingService;
import cn.rjtech.admin.moroutingconfig.MoMoroutingconfigService;
import cn.rjtech.admin.otherout.OtherOutService;
import cn.rjtech.admin.uom.UomService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.wms.utils.EncodeUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 工单管理 Controller
 * @ClassName: MoDocAdminController
 * @author: RJ
 * @date: 2023-04-26 16:15
 */
@CheckPermission(PermissionKey.MODOC)
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

	@Inject
	private UomService uomService; //单位

	@Inject
	private MoMoroutingService moMoroutingService; //工艺路线
	@Inject
	private DepartmentService departmentService;


	/**
	 * 首页
	 */
	public void index() {
		render("index.html");
	}

	/**
	 * 数据源
	 */
    @UnCheck
	public void datas() {
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv()));
	}

	/**
	 * 新增
	 */
	public void add() {
		render("add2.html");
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
			//存货
			Inventory inventory = inventoryService.findById(moDoc.getIInventoryId());
			if(inventory!=null){
			  moRecod.set("cinvcode",inventory.getCInvCode());//料品编码
			  moRecod.set("cinvcode1",inventory.getCInvCode1());//客户部番
			  moRecod.set("cinvname1",inventory.getCInvName1());//部品名称
			  moRecod.set("iPkgQty",inventory.getIPkgQty());//包装数量
				Uom uom=uomService.findFirst(Okv.create().
						setIfNotNull(Uom.IAUTOID,inventory.getICostUomId()),Uom.IAUTOID,"desc");
				if(uom!=null) {
					moRecod.set("manufactureuom", uom.getCUomName());//生产单位
				}
				moRecod.set("cinvstd",inventory.getCInvStd());//规格

			}
			//工艺路线
			MoMorouting moMorouting=moMoroutingService.findByImdocId(moDoc.getIAutoId());
			if(moMorouting!=null){
				moRecod.set("croutingname",moMorouting.getCRoutingName());//工艺路线名称

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

	/**
	 * 删除
	 */
	public void  deletemodoc(){
		MoDoc moDoc=service.findById(getLong(0));
		if(moDoc == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		renderJson(service.deletemodoc(moDoc));
	}

	/**
	 * 齐料检查界面
	 */

	public  void  checkmaterialpage(){
		set("imodocid",getLong("imodocid"));
		String isScanned =service.findByisScanned(getLong("imodocid"));
		set("isScanned",isScanned);
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
	public void subSave(String rowid) {
		renderJson(service.subSave(getJBoltTable(),rowid));
	}


	/**
	 * 编辑界面保存
	 * @param
	 */
	public void subUpdata() {
		renderJson(service.subUpdata(getJBoltTable()));
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
		render("personindex2.html");
		//render("persondialog.html");
	}

	/**
	 * 编辑页面
	 */
	public void updatas(){
		Record record=service.getmoDocupdata(getLong(0));
		if(record == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moDoc",record);
		render("add.html");
	}

	/**
	 * 人员信息操作
	 */
	public void personoperationdialog(){
		//operation
		Kv kv = getKv();
		EncodeUtils.encodeUrl(get(InventoryRoutingConfig.PERSONEQUIPMENTJSON), EncodeUtils.UTF_8);
		set("configid",getLong("iautoid"));
		set("imdocid",getLong("imdocid"));
		set("imergerate",kv.get("imergerate"));
		set("configpersonids",kv.get("configpersonids"));
		set(InventoryRoutingConfig.PERSONEQUIPMENTJSON, EncodeUtils.encodeUrl(get(InventoryRoutingConfig.PERSONEQUIPMENTJSON), EncodeUtils.UTF_8) );
		render("person_dialog_index.html");
		//render("persondialog.html");
	}

	/**
	 * 物料
	 */
	public void invc_dialog_index(){
		set("configid",get("iautoid"));
		set("iinventoryid",get("iinventoryid"));
		set("imdocid",getLong("imdocid"));
		render("invc_dialog_index.html");
	}

	/**
	 * 设备
	 */
	public void equipment_dialog_index(){
		Kv kv = getKv();
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

    @UnCheck
	public void getMoPlanNo() {
		renderJsonData(service.generateBarCode());
	}

    @UnCheck
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
		set("imodocid",getLong("imodocid"));
		render("other_dialog_index.html");
	   }
	/**
	 * 特殊领料界面-新增
	 */
	public void  othermaterialadd(){
		 Long imodocid=getLong("imodocid");

		OtherOut otherOut = new OtherOut();
		String billNo = BillNoUtils.getcDocNo(getOrgId(), "LLD", 5);
		Date nowDate = new Date();
		otherOut.setBillNo(billNo);
		MoDoc modoc=service.findById(imodocid);
		if(modoc!=null) {
			Department department=departmentService.findById(modoc.getIDepartmentId());
			if(department!=null) {
				otherOut.setSourceBillDid(String.valueOf(imodocid));
				otherOut.setDeptCode(department.getCDepCode());
			}
		}
		otherOut.setBillDate(nowDate);
		otherOut.setSourceBillType("生产工单");
		otherOut.setType("OtherOutMES");
		set("otherOut",otherOut);

		//set("imdocid",getLong("imdocid"));
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
		if (StrUtil.isBlank(iAutoId)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		renderJson(otherOutService.approve(iAutoId,mark));
	}
	/**
	 * 特殊领料界面-手动关闭
	 */
	public void closeWeekOrder(String iAutoId) {
		if (StrUtil.isBlank(iAutoId)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		renderJson(otherOutService.closeWeekOrder(iAutoId));
	}

	/**
	 * 特殊领料界面-撤回
	 */
	public void recall(String iAutoId) {
		if (StrUtil.isBlank(iAutoId)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		renderJson(otherOutService.recall(iAutoId));
	}

	/**
	 * 推送U8
	 */
	public void pushu8(){
		MoDoc moDoc=service.findById(getLong(0));
		if(moDoc == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		renderJson(service.pushU8(moDoc));
	}

	/**
	 * 存货档案弹出框
	 */
	public void inventoryDialogIndex(){
		render("inventory_dialog_index.html");
	}

	/**
	 * 获取存货档案
	 */
    @UnCheck
	public void getInventoryList(){
		Page<Record> pageList = service.getInventoryList(getPageNumber(),getPageSize(),getKv());
		renderJsonData(pageList);
	}

	@UnCheck
	/**
	 * 选择物料autocomplete数据查询
	 * */
	public void getMouldsAutocomplete(){
		renderJsonData(service.inventoryAutocompleteDatas(get("q"),getInt("limit")));
	}
	/**
	 * 获取操作资质的人员
	 */
    @UnCheck
	public void getPersonByEquipment(){
		Kv kv = getKv();
		Page<Record> page=service.getPersonByEquipment(getPageNumber(),getPageSize(),getKv());
		renderJsonData(page);
	}

	/**
	 * 编辑界面数据
	 */
    @UnCheck
	public void getMoDocbyIinventoryRoutingId(){
		Kv kv = getKv();
		renderJsonData(service.getMoDocbyIinventoryRoutingId(kv.getLong("iMoDocId")));
	}

	/**
	 * 编辑界面工序物料集数据
	 */
    @UnCheck
	public void getMoDocinv(){
		renderJsonData(service.getMoDocinv(getKv()));
	}

	/**
	 * 跳转工序物料集界面
	 */
	public void modoc_inv_index(){
		set("configid",get("iautoid"));
		set("iinventoryid",get("iinventoryid"));
		set("imdocid",get("imdocid"));
		render("modoc_inv_index.html");
	}

	/**
	 * 跳转工序设备集界面
	 */
	public void modoc_equipment_index(){
		set("configid",get("iautoid"));
		set("iinventoryid",get("iinventoryid"));
		set("imdocid",get("imdocid"));
		render("modoc_equipment_index.html");
	}
    
	/**
	 * 跳转工序设备集界面数据
	 */
    @UnCheck
	public void getMoDocEquipment(){
		renderJsonData(service.getMoDocEquipment(getKv()));
	}

	/**
	 * 跳转工序工艺文件
	 */
	public void modoc_drawing_index(){
		set("configid",get("iautoid"));
		set("iinventoryid",get("iinventoryid"));
		set("imdocid",get("imdocid"));
		render("modoc_drawing_index.html");
	}

	/**
	 * 编辑界面工序工艺文件数据
	 */
	public void moDocInventoryRouting(){
		Kv kv = getKv();
		renderJsonData(service.moDocInventoryRouting(getKv()));
	}


}
