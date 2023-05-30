package cn.rjtech.admin.workshiftm;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.render.JBoltByteFileType;
import cn.rjtech.model.momdata.Workshiftm;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 生产班次 Controller
 *
 * @ClassName: WorkshiftmAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-27 09:25
 */
@CheckPermission(PermissionKey.WORKSHIFTM)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/workshiftm", viewPath = "/_view/admin/workshiftm")
public class WorkshiftmAdminController extends JBoltBaseController {

	@Inject
	private WorkshiftmService service;

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
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv()));
	}
	/**
	* 数据源
	*/
	public void getSelect() {
		renderJsonData(service.getSelect());
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
		Workshiftm workshiftm=service.findById(getLong(0));
		if(workshiftm == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("workshiftm",workshiftm);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(Workshiftm.class, "workshiftm")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Workshiftm.class, "workshiftm")));
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


	public void updateEditTable() {
		renderJson(service.updateEditTable(getJBoltTable(), JBoltUserKit.getUserId(), new Date()));
	}

	/**
	 * 导出数据
	 */
	@SuppressWarnings("unchecked")
	public void dataExport() throws Exception {
		List<Record> rows = service.getDataExport(getKv());

		renderJxls("workshiftm.xlsx", Kv.by("rows", rows), "生产班次_" + DateUtil.today() + ".xlsx");

	}
	@SuppressWarnings("unchecked")
	public void downloadTemplate() throws Exception {
		renderJxls("badness_import.xlsx", Kv.by("rows", null), "不良项目导入模板.xlsx");
	}


	/**
	 * Excel模板下载
	 */
	@SuppressWarnings("unchecked")
	public void downloadTpl() throws Exception {
		renderJxls("workshiftm_import.xlsx", Kv.by("rows", null), "生产班次导入模板.xlsx");
	}

	/**
	 * 生产班次Excel导入数据库
	 */
	public void importExcel(){
		UploadFile uploadFile = getFile("file");
		ValidationUtils.notNull(uploadFile, "上传文件不能为空");
		File file = uploadFile.getFile();
		List<String> list = StrUtil.split(uploadFile.getOriginalFileName(), StrUtil.DOT);
		// 截取最后一个“.”之前的文件名，作为导入格式名
		String cformatName = list.get(0);
		String extension = list.get(1);
		ValidationUtils.equals(extension, JBoltByteFileType.XLSX.suffix, "系统只支持xlsx格式的Excel文件");
		renderJson(service.importExcelData(file,cformatName));
	}

}
