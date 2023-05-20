package cn.rjtech.admin.inventoryroutingsop;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.model.JboltFile;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.extend.config.ExtendUploadFolder;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InventoryRoutingSop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.upload.UploadFile;

/**
 * 物料建模-存货工序作业指导书
 * @ClassName: InventoryRoutingSopAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 10:06
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.INVENTORY_RECORD)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/inventoryroutingsop", viewPath = "/_view/admin/inventoryroutingsop")
public class InventoryRoutingSopAdminController extends BaseAdminController {

	@Inject
	private InventoryRoutingSopService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isEnabled")));
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
		renderJson(service.save(getModel(InventoryRoutingSop.class, "inventoryRoutingSop")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		InventoryRoutingSop inventoryRoutingSop=service.findById(getLong(0));
		if(inventoryRoutingSop == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("inventoryRoutingSop",inventoryRoutingSop);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InventoryRoutingSop.class, "inventoryRoutingSop")));
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

	public void file_dialog_index() {
		Long configid = getLong("configid");
		set("configid",configid);
		render("file_dialog_index.html");
	}

	public void dataList(){
		renderJsonData(service.dataList(getLong("configid")));
	}

	/**
	 * 上传文件
	 */
	public void uploadFile(){
		Long configid=getLong("configid");
		//上传到今天的文件夹下
		String uploadPath= JBoltUploadFolder.todayFolder(ExtendUploadFolder.EXTEND_ITEMROUTINGDRAWING_EDITOR_FILE);
		UploadFile file=getFile("file",uploadPath);
		if(file==null) {
			renderJsonFail("上传失败");
			return;
		}
		//第一种方式 返回jboltFile
		renderJsonData(service.saveJBoltFile(file, uploadPath, JboltFile.FILE_TYPE_ATTACHMENT,configid));

	}

	public void saveDrawing() {
		Long configid = getLong("configid");
		renderJson(service.saveDrawing(getJBoltTable(), configid));
	}
}
