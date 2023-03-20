package cn.rjtech.admin.qcparam;

import com.jfinal.aop.Inject;

import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.upload.UploadFile;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.QcParam;
/**
 * 质量建模-检验参数
 * @ClassName: QcParamAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-17 15:15
 */
@Before(JBoltAdminAuthInterceptor.class)
@CheckPermission(PermissionKey.QCPARAM)
@UnCheckIfSystemAdmin
@Path(value = "/admin/qcparam", viewPath = "/_view/admin/qcparam")
public class QcParamAdminController extends BaseAdminController {

	@Inject
	private QcParamService service;

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
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(QcParam.class, "qcParam")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		QcParam qcParam=service.findById(getLong(0));
		if(qcParam == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("qcParam",qcParam);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(QcParam.class, "qcParam")));
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

	/*
	* 数据导入
	* */
	public void importExcel(){
		String uploadPath= JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
		UploadFile file=getFile("file",uploadPath);
		if(notExcel(file)){
			renderJsonFail("请上传excel文件");
			return;
		}
		renderJson(service.importExcelData(file.getFile()));
	}
}
