package cn.rjtech.admin.formuploadm;

import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.bean.MultipleUploadFile;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.service.JBoltFileService;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.FormUploadM;
import com.jfinal.upload.UploadFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录上传
 * @ClassName: FormUploadMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-29 15:35
 */
@CheckPermission(PermissionKey.FORMUPLOADM)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/formUploadM", viewPath = "/_view/admin/formuploadm")
public class FormUploadMAdminController extends BaseAdminController {

	@Inject
	private FormUploadMService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(),getKv()));
	}

   /**
	* 新增
	*/
	public void add() {
		set("iauditstatus",true);
		render("add.html");
	}
	/**
	 * 新增附件
	 */
	public void attachment() {
		render("upload_add.html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(FormUploadM.class, "formUploadM")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		FormUploadM formUploadM=service.findById(getLong(0));
		if(formUploadM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("formUploadM",formUploadM);
		String s = formUploadM.getIAuditStatus() == 0 ?
				"已保存" : formUploadM.getIAuditStatus() == 1 ?
				"待审核" : formUploadM.getIAuditStatus() == 2 ? "审核通过" : "审核不通过";
		set("status",s);
		set("iauditstatus",false);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(FormUploadM.class, "formUploadM")));
	}

	/**
	 * 上传文件 同步批量上传文件
	 */
	public void upload() {
		//上传到今天的文件夹下
		String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_FILE_UPLOADER);
		List<UploadFile> files = getFiles(uploadPath);
		if (!isOk(files)) {
			renderBootFileUploadFail("文件上传失败!");
			return;
		}
		List<MultipleUploadFile> retFiles = new ArrayList<>();
		Ret ret;
		for (UploadFile uploadFile : files) {
			ret = jboltFileService.saveVideoFile(uploadFile, uploadPath);
			retFiles.add(new MultipleUploadFile(uploadFile.getOriginalFileName(), uploadFile.getOriginalFileName(), ret.getStr("data")));
		}
		renderJsonData(retFiles);
	}

	/**
	 * 批量保存
	 */
	public void saveTableSubmit(){
		renderJsonData(service.saveTableSubmit(getJBoltTable()));
	}

	public void batchAudit() {
		renderJson(service.batchHandle(getKv(), 2));
	}

	public void batchAntiAudit() {
		renderJson(service.batchHandle(getKv(), 1));
	}

	/**
	 * 删除
	 */
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}
}
