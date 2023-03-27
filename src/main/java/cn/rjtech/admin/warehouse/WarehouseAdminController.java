package cn.rjtech.admin.warehouse;

import cn.hutool.core.date.DateUtil;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.model.momdata.Person;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.NotAction;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.Warehouse;
import com.jfinal.upload.UploadFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * 仓库建模-仓库档案
 * @ClassName: WarehouseAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-20 16:47
 */
@CheckPermission(PermissionKey.WAREHOUSE)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/warehouse", viewPath = "/_view/admin/warehouse")
public class WarehouseAdminController extends BaseAdminController {

	@Inject
	private WarehouseService service;

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
		Page<Record> page = service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv());
		renderJsonData(page);
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
		renderJson(service.save(getModel(Warehouse.class, "warehouse")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		Warehouse warehouse=service.findById(getLong(0));
		if(warehouse == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("warehouse",warehouse);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Warehouse.class, "warehouse")));
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
	* 切换bFreeze
	*/
	public void toggleBFreeze() {
	    renderJson(service.toggleBoolean(getLong(0),"bFreeze"));
	}

   /**
	* 切换bMRP
	*/
	public void toggleBMRP() {
	    renderJson(service.toggleBoolean(getLong(0),"bMRP"));
	}

   /**
	* 切换bShop
	*/
	public void toggleBShop() {
	    renderJson(service.toggleBoolean(getLong(0),"bShop"));
	}

   /**
	* 切换bInCost
	*/
	public void toggleBInCost() {
	    renderJson(service.toggleBoolean(getLong(0),"bInCost"));
	}

   /**
	* 切换bInAvailCalcu
	*/
	public void toggleBInAvailCalcu() {
	    renderJson(service.toggleBoolean(getLong(0),"bInAvailCalcu"));
	}

   /**
	* 切换bProxyWh
	*/
	public void toggleBProxyWh() {
	    renderJson(service.toggleBoolean(getLong(0),"bProxyWh"));
	}

   /**
	* 切换bBondedWh
	*/
	public void toggleBBondedWh() {
	    renderJson(service.toggleBoolean(getLong(0),"bBondedWh"));
	}

   /**
	* 切换bWhAsset
	*/
	public void toggleBWhAsset() {
	    renderJson(service.toggleBoolean(getLong(0),"bWhAsset"));
	}

   /**
	* 切换bCheckSubitemCost
	*/
	public void toggleBCheckSubitemCost() {
	    renderJson(service.toggleBoolean(getLong(0),"bCheckSubitemCost"));
	}

   /**
	* 切换bEB
	*/
	public void toggleBEB() {
	    renderJson(service.toggleBoolean(getLong(0),"bEB"));
	}

   /**
	* 切换isDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}

	//仓库下拉信息查询
	public void list(){
		renderJsonData(service.list(getKv()));
	}


	/**
	 * Excel模板下载
	 */
	@SuppressWarnings("unchecked")
	public void downloadTpl() throws Exception {
		renderJxls("warehouse_import.xlsx", Kv.by("rows", null), "仓库档案导入模板.xlsx");
	}


	/**
	 * 导入
	 */
	public void importExcel(){
		String uploadPath= JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
		UploadFile file=getFile("file",uploadPath);
		if(notExcel(file)){
			renderJsonFail("请上传excel文件");
			return;
		}
		renderJson(service.importExcelData(file.getFile()));
	}

	public void options(){
		renderJsonData(service.options());
	}
}
