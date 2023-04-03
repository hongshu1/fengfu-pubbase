package cn.rjtech.admin.warehousearea;

import cn.hutool.core.date.DateUtil;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.model.momdata.WarehouseArea;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.util.List;

/**
 * 库区档案 Controller
 *
 * @ClassName: WarehouseAreaAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-02 20:43
 */
@CheckPermission(PermissionKey.WAREHOUSE_AREA)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/warehousearea", viewPath = "/_view/admin/warehousearea")
public class WarehouseAreaAdminController extends JBoltBaseController {

	@Inject
	private WarehouseAreaService service;

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
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(), getKv()));
	}

	public void list(){
		renderJsonData(service.list(getKv()));
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
		WarehouseArea warehouseArea=service.findById(getLong(0));
		if(warehouseArea == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("warehouseArea",warehouseArea);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(WarehouseArea.class, "warehouseArea")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(WarehouseArea.class, "warehouseArea")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}

  /**
	* 切换toggleIsdeleted
	*/
	public void toggleIsdeleted() {
		renderJson(service.toggleIsdeleted(getLong(0)));
	}

  /**
	* 切换toggleIsenabled
	*/
	public void toggleIsenabled() {
		renderJson(service.toggleIsenabled(getLong(0)));
	}

	/**
	 * 导出数据
	 */
	@SuppressWarnings("unchecked")
	public void dataExport() throws Exception {
		List<Record> rows = service.list(getKv());
		renderJxls("warehousearea.xlsx", Kv.by("rows", rows), "库区列表_" + DateUtil.today() + ".xlsx");
	}




	/**
	 * Excel模板下载
	 */
	@SuppressWarnings("unchecked")
	public void downloadTpl() throws Exception {
		renderJxls("warehousearea_import.xlsx", Kv.by("rows", null), "库区档案导入模板.xlsx");
	}

	/**
	 * 生产班次Excel导入数据库
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

	/**
	 * 库区打印数据
	 *
	 */
	public void printData(){
		renderJsonData(service.getPrintDataCheck(getKv()));
	}

}
