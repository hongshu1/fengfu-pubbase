package cn.rjtech.admin.spotcheckparam;

import java.util.List;

import com.jfinal.aop.Inject;

import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.rjtech.admin.qcitem.QcItemService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.QcItem;
import cn.rjtech.model.momdata.QcParam;
import cn.rjtech.model.momdata.SpotCheckParam;
import cn.rjtech.util.Util;

/**
 * 点检建模-点检参数
 * @ClassName: SpotCheckParamAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 09:51
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.SPOTCHECKPARAM)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/spotcheckparam", viewPath = "/_view/admin/spotcheckparam")
public class SpotCheckParamAdminController extends BaseAdminController {

	@Inject
	private SpotCheckParamService service;
	@Inject
	private QcItemService  qcItemService;

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
		renderJsonData(service.pageList(getKv()));
//		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isEnabled"), getBoolean("IsDeleted")));
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
		renderJson(service.save(getModel(SpotCheckParam.class, "spotCheckParam")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SpotCheckParam spotCheckParam=service.findById(getLong(0));
		if(spotCheckParam == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("spotCheckParam",spotCheckParam);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(SpotCheckParam.class, "spotCheckParam")));
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
	* 切换IsDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"IsDeleted"));
	}

	/*
	 * 导出选中
	 * */
	public void exportExcelByIds() {
		String ids = get("ids");
		if (notOk(ids)) {
			renderJsonFail("未选择有效数据，无法导出");
			return;
		}
		List<Record> data = service.list(Kv.create().setIfNotBlank("ids", Util.getInSqlByIds(ids)));
		if (notOk(data)) {
			renderJsonFail("无有效数据导出");
			return;
		}
		List<SpotCheckParam> params = service.getListByIds(ids);
		for (SpotCheckParam param : params) {
			QcItem qcItem = qcItemService.findById(param.getIQcItemId());
			param.put("cqcitemname",qcItem.getCQcItemName());
		}
		//2、生成excel文件
		JBoltExcel jBoltExcel = service.exportExcelTpl(params);
		//3、导出
		renderBytesToExcelXlsFile(jBoltExcel);
	}

	/*
	 * 导出全部
	 * */
	public void exportExcelAll() {
		List<Record> rows = service.list(getKv());
		if (notOk(rows)) {
			renderJsonFail("无有效数据导出");
			return;
		}
		List<SpotCheckParam> params = service.findAll();
		for (SpotCheckParam param : params) {
			QcItem qcItem = qcItemService.findById(param.getIQcItemId());
			param.put("cqcitemname",qcItem.getCQcItemName());
		}
		//2、生成excel文件
		JBoltExcel jBoltExcel = service.exportExcelTpl(params);
		//3、导出
		renderBytesToExcelXlsFile(jBoltExcel);
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

	@SuppressWarnings("unchecked")
	public void downloadTpl() throws Exception {
		renderBytesToExcelXlsFile(service.getExcelImportTpl().setFileName("点检参数导入模板"));
	}


}
