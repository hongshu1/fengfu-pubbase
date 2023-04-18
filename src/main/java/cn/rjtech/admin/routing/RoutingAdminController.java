package cn.rjtech.admin.routing;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.admin.bommaster.BomMasterService;
import cn.rjtech.admin.equipmentmodel.EquipmentModelService;
import cn.rjtech.admin.inventorychange.InventoryChangeService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.BomMaster;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;

/**
 * 物料建模-工艺路线
 * @ClassName: BomMasterAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-17 16:28
 */

@Path(value = "/admin/routing", viewPath = "/_view/admin/routing")
public class RoutingAdminController extends BaseAdminController {

	@Inject
	private RoutingService service;
	@Inject
	private InventoryChangeService inventoryChangeService;
	@Inject
	private EquipmentModelService equipmentModelService;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isEnabled"), getBoolean("isDeleted")));
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
		BomMaster bomMaster=service.findById(getLong(0));
		if(bomMaster == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("bomMaster",bomMaster);
		render("edit.html");
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
	 * 切换isEnabled
	 */
	public void toggleIsEnabled() {
		renderJson(service.toggleBoolean(getLong(0),"isEnabled"));
	}

	/**
	 * 切换isDeleted
	 */
	public void toggleIsDeleted() {
		renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}

	/**
	 * 默认给1-100个数据
	 */
	public void inventoryAutocomplete(){
		renderJsonData(inventoryChangeService.inventoryAutocomplete(getPageNumber(), 100, getKv()));
	}

	public void findEquipmentModelAll(){
		renderJsonData(equipmentModelService.getAdminDataNoPage(getKv()));
	}

	public void getDatas(){
		renderJsonData(service.getDatas(getKv()));
	}

	public void getPageData(){
		renderJsonData(service.getPageData(getPageNumber(), getPageSize(), getKv()));
	}

	public void testDel(){
		ok();
	}

	public void copyForm(){
		ValidationUtils.notNull(get(0), "未获取到指定产品id");
		set("oldId", get(0));
		render("_copy_form.html");
	}

	// 拷贝
	public void saveCopy(@Para(value = "oldId") String oldId){

	}

	// 工艺路线
	public void getMasterData() {
		renderJsonData(service.getMaster());
	}

}
