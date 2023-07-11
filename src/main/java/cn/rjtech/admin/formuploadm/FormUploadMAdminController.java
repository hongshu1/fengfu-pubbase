package cn.rjtech.admin.formuploadm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.MultipleUploadFile;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.service.JBoltFileService;
import cn.rjtech.admin.formuploadd.FormUploadDService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.FormUploadM;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Ret;
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
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/formUploadM", viewPath = "/_view/admin/formuploadm")
public class FormUploadMAdminController extends BaseAdminController {

	@Inject
	private FormUploadMService service;

	@Inject
	private JBoltFileService jboltFileService;
	@Inject
	private FormUploadDService formUploadDService;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(),getKv()));
	}

   /**
	* 新增
	*/
   @CheckPermission(PermissionKey.FORMUPLOADM_ADD)
	public void add() {
		set("iauditstatus",true);
		render("add.html");
	}
	/**
	 * 新增附件
	 */
	public void attachment(@Para(value = "iautoid") Long iautoid ,
						   @Para(value = "cattachments")String cattachments) {
		set("iautoid",iautoid);
		set("cattachments",cattachments);
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
   @CheckPermission(PermissionKey.FORMUPLOADM_EDIT)
   public void edit(@Para(value = "iautoid") Long iautoid) {
		ValidationUtils.validateId(iautoid, "记录上传ID");

		FormUploadM formUploadM=service.findById(iautoid);
		if(formUploadM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("formUploadM",formUploadM);
		String s = formUploadM.getIAuditStatus() == 0 ?
				"已保存" : formUploadM.getIAuditStatus() == 1 ?
				"待审核" : formUploadM.getIAuditStatus() == 2 ? "审核通过" : "审核不通过";
		set("status",s);
		keepPara();
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
	@CheckPermission(PermissionKey.FORMUPLOADM_ADD_SUBMIT)
	public void submitAll(){
		renderJson(service.saveTableSubmit(getJBoltTable()));
	}

	/**
	 * 删除
	 */
	@CheckPermission(PermissionKey.FORMUPLOADM_DELETE)
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}

	/**
	 * 批量删除
	 */
	@CheckPermission(PermissionKey.FORMUPLOADM_DELETE)
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}
    
}
