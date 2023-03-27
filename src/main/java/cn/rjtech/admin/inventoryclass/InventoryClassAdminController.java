package cn.rjtech.admin.inventoryclass;

import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.upload.UploadFile;
import cn.rjtech.model.momdata.InventoryClass;
import java.util.List;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.InventoryClass;
/**
 * 物料建模-存货分类
 * @ClassName: InventoryClassAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-23 09:02
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.INVENTORY_RECORD)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/inventoryclass", viewPath = "/_view/admin/inventoryclass")
public class InventoryClassAdminController extends BaseAdminController {

	@Inject
	private InventoryClassService service;

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
		renderJsonData(service.getAdminDatas(getKeywords()));
	}

   /**
	* 新增
	*/
	public void add() {
		Long aLong = getLong(0);
		set("pid",aLong);
		render("add.html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(InventoryClass.class, "inventoryClass")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		InventoryClass inventoryClass=service.findById(getLong(0));
		if(inventoryClass == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("inventoryClass",inventoryClass);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InventoryClass.class, "inventoryClass")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 得到树形结构数据
	*/
	public void tree() {
		renderJsonData(service.getTreeDatas());
	}

   /**
	* 进入import_excel.html
	*/
	public void initImportExcel() {
		render("import_excel.html");
	}

   /**
	* 下载导入模板
	*/
	public void downloadTpl() {
		renderBytesToExcelXlsFile(service.getImportExcelTpl().setFileName("物料建模-存货分类导入模板"));
	}

   /**
	* 执行导入excel
	*/
	public void importExcel() {
		String uploadPath=JBoltUploadFolder.todayFolder(JBoltUploadFolder.IMPORT_EXCEL_TEMP_FOLDER);
        UploadFile file=getFile("file",uploadPath);
        if(notExcel(file)){
            renderJsonFail("请上传excel文件");
            return;
        }
        renderJson(service.importExcel(file.getFile()));
	}

    /**
	* 执行导出excel 所有数据
	*/
	public void exportExcelAll() {
	    List<InventoryClass> datas = service.findAll();
	    if(notOk(datas)){
	        renderJsonFail("无有效数据导出");
	        return;
	    }
		renderBytesToExcelXlsxFile(service.exportExcel(datas).setFileName("物料建模-存货分类"));
	}

	/**
	 * 树结构数据源
	 */
	public void mgrTree() {
		renderJsonData(service.getMgrTree(getLong("selectId", getLong(0)), getInt("openLevel", 0)));
	}

	/**
	 * 获取所有的分类
	 */
	public void options(){
		renderJsonData(service.selectClassList(getKv()));
	}
}
