package cn.rjtech.admin.bomcompare;

import cn.hutool.core.util.ObjUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.admin.bomm.BomMService;
import cn.rjtech.admin.bommtrl.BommTrlService;
import cn.rjtech.admin.equipmentmodel.EquipmentModelService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.BomCompare;
import cn.rjtech.model.momdata.BomM;
import cn.rjtech.model.momdata.BommTrl;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;

/**
 * 物料建模-Bom清单
 *
 * @ClassName: BomCompareAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-01 10:50
 */
@CheckPermission(PermissionKey.NOME)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/bomcompare", viewPath = "/_view/admin/bomcompare")
public class BomCompareAdminController extends BaseAdminController {

	@Inject
	private BomCompareService service;
	@Inject
	private InventoryService inventoryService;
	@Inject
	private BomMService bomMService;
	@Inject
	private EquipmentModelService equipmentModelService;
	@Inject
	private BommTrlService bommTrlService;
	
   /**
	* 首页
	*/
	public void index(@Para(value = "id") Long id, @Para(value = "isChildren") Boolean isChildren) {
		Kv kv = getKv();
		if (ObjUtil.isNotNull(id)){
			bomMService.setBomRecord(id, isChildren, true, kv);
			setAttrs(kv);
		}
		render("index.html");
	}
   /**
	* 数据源
	*/
   @UnCheck
	public void datas() {
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getInt("iRawType"), getBoolean("isOutSourced"), getBoolean("isDeleted")));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add.html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(BomCompare.class, "bomCompare")));
	}

   /**
	* 编辑
	*/
   @CheckPermission(PermissionKey.BOMMASTER_VERSION_EDIT)
	public void edit() {
		BomM bomM = bomMService.findById(getLong(0));
		ValidationUtils.notNull(bomM, JBoltMsg.DATA_NOT_EXIST);
		Kv kv = getKv();
		bomMService.setBomRecord(getLong(0), false, true, kv);
		setAttrs(kv);
		render("manual_form.html");
//		getBomMaster(bomM);
//		render("/_view/admin/bommaster/edit.html");
	}
	
	@CheckPermission(PermissionKey.BOMMASTER_VERSION_EDIT)
	public void info(){
		BomM bomM = bomMService.findById(getLong(0));
		ValidationUtils.notNull(bomM, JBoltMsg.DATA_NOT_EXIST);
		Kv kv = getKv();
		bomMService.setBomRecord(getLong(0), false, true, kv);
		setAttrs(kv);
		render("manual_form.html");
		/*BomSourceTypeEnum bomSourceTypeEnum = BomSourceTypeEnum.toEnum(bomM.getIType());
		ValidationUtils.notNull(bomSourceTypeEnum, "未知新增类型");
		BomSourceTypeEnum manualTypeAdd = BomSourceTypeEnum.MANUAL_TYPE_ADD;
		Kv kv = getKv();
		kv.set(BomM.ISVIEW , 1);
		if (manualTypeAdd.getValue() == bomSourceTypeEnum.getValue()){
			bomMService.setBomRecord(getLong(0), false, true, kv);
			setAttrs(kv);
			render("manual_form.html");
			return;
		}
		getBomMaster(bomM);
		setAttrs(kv);
		render("/_view/admin/bommaster/edit.html");*/
	}
	@CheckPermission(PermissionKey.BOMMASTER_BOMCOMPARE_FILEINFO)
	public void fileInfo(){
		
		BommTrl bommTrl = bommTrlService.findById(getLong(0));
		ValidationUtils.notNull(bommTrl, "为找导入文件数据");
		Kv kv = getKv();
		BomM bomM = bomMService.findById(bommTrl.getIBomMid());
		getBomMaster(bomM);
		setAttrs(kv);
		render("/_view/admin/bommaster/edit.html");
	}

    @UnCheck
	private void getBomMaster(BomM bomMaster) {
		set("equipmentModel", equipmentModelService.findById(bomMaster.getIEquipmentModelId()));
		set("inventory", inventoryService.findById(bomMaster.getIInventoryId()));
		set("bomMaster", bomMaster);
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(BomCompare.class, "bomCompare")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

   /**
	* 切换isOutSourced
	*/
	public void toggleIsOutSourced() {
	    renderJson(service.toggleBoolean(getLong(0),"isOutSourced"));
	}

   /**
	* 切换isDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}
	
	@CheckPermission(PermissionKey.BOMCOMPARE_ADD)
	public void manualForm(@Para(value = "pid") Long pid, @Para(value = "isChildren") Boolean isChildren){
		Kv kv = getKv();
		bomMService.setBomRecord(pid, isChildren, false, kv);
		setAttrs(kv);
		render("manual_form.html");
	}
	
	public void bomcompareSplit(){
		keepPara();
	    render("_table_split.html");
    }
	
	/**
	 * 拉取资源Dialog
	 */
	public void chooseItem(){
		keepPara();
		render("resource_list.html");
	}
	
	public void resourceList(){
		renderJsonData(inventoryService.resourceList(getPageNumber(), getPageSize(), getKv()));
	}

	@CheckPermission(PermissionKey.BOMMASTER_VERSION_SUBMIT)
	public void submitForm(){
		renderJsonData(service.submitForm(getJBoltTable()));
	}
}
