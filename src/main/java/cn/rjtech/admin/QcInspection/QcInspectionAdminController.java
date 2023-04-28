package cn.rjtech.admin.QcInspection;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.model.JboltFile;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.service.JBoltFileService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.QcInspection;
import cn.rjtech.wms.utils.StringUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.upload.UploadFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 工程内品质巡查 Controller
 * @ClassName: QcInspectionAdminController
 * @author: RJ
 * @date: 2023-04-26 13:49
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/QcInspection", viewPath = "/_view/admin/QcInspection")
public class QcInspectionAdminController extends BaseAdminController {

	@Inject
	private QcInspectionService service;

	@Inject
	private JBoltFileService jboltFileService;

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
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKeywords()));
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
		QcInspection qcInspection=service.findById(getLong(0)); 
		if(qcInspection == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("qcInspection",qcInspection);
		render("edit.html");
	}

	public void edit2() {
		set("iautoid", get("iautoid"));
		set("type", get("type"));
		QcInspection qcInspection = service.findById(get("iautoid"));
		if (isNull(get("iautoid"))) {
			render("add2.html");
		}else {
			String supplierInfoId = qcInspection.getCMeasureAttachments();

			if (notNull(qcInspection.getCMeasureAttachments())){
				set("files", service.getFilesById(supplierInfoId));
			}
			set("qcInspection", qcInspection);
			render("edit2.html");
		}




	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(QcInspection.class, "qcInspection")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(QcInspection.class, "qcInspection")));
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
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}

  /**
	* 切换toggleIsFirstCase
	*/
	public void toggleIsFirstCase() {
		renderJson(service.toggleIsFirstCase(getLong(0)));
	}

	/**
	 * 拉取部门和人员资源
	 */
	public void DepartmentList() {
		//列表排序
		String cus = get("q");
		Kv kv = new Kv();
		kv.set("cus", StringUtils.trim(cus));
		renderJsonData(service.DepartmentList(kv));

	}

	public void updateEditTable() {
		renderJson(service.updateEditTable(getJBoltTable(), getKv()));
	}


	/**
	 * 多文件上传
	 */
	public void uploadFiles() {
		//上传到今天的文件夹下
		String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.PCN_FILE_UPLOADER);
		List<UploadFile> files = getFiles(uploadPath);
		if (files == null || files.size() == 0) {
			renderJsonFail("请选择文件后上传");
			return;
		}
		List<JboltFile> retFiles = new ArrayList<JboltFile>();
		JboltFile jboltFile;
		StringBuilder errorMsg = new StringBuilder();
		for (UploadFile uploadFile : files) {
			jboltFile = jboltFileService.saveJBoltFile(uploadFile, uploadPath, JboltFile.FILE_TYPE_ATTACHMENT);
			if (jboltFile != null) {
				retFiles.add(jboltFile);
			} else {
				errorMsg.append(uploadFile.getOriginalFileName() + "上传失败;");
			}
		}
		if (retFiles.size() == 0) {
			renderJsonFail(errorMsg.toString());
			return;
		}
		renderJsonData(retFiles, errorMsg.toString());
	}


}
