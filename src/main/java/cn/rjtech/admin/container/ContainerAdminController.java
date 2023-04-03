package cn.rjtech.admin.container;


import cn.hutool.core.date.DateUtil;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.Container;
import com.jfinal.upload.UploadFile;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 仓库建模-容器档案
 * @ClassName: ContainerAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 14:48
 */

@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.CONTAINER)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/container", viewPath = "/_view/admin/container")
public class ContainerAdminController extends BaseAdminController {

	@Inject
	private ContainerService service;

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
	 * 出入库数据源
	 */
	public void data() {
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv().set("isInner",5)));
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
		renderJson(service.save(getModel(Container.class, "container")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		Container container=service.findById(getLong(0));
		if(container == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("container",container);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Container.class, "container")));
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
		renderJson(service.deleted(getLong(0)));
	}

   /**
	* 切换isInner
	*/
	public void toggleIsInner() {
	    renderJson(service.toggleBoolean(getLong(0),"isInner"));
	}

   /**
	* 切换isEnabled
	*/
	public void toggleIsEnabled() {
	    renderJson(service.toggleBoolean(getLong(0),"isEnabled"));
	}

	/**
	 * Excel模板下载
	 */
	@SuppressWarnings("unchecked")
	public void downloadTpl() throws Exception {
		renderJxls("container_import.xlsx", Kv.by("rows", null), "容器档案导入模板.xlsx");
	}

	/**
	 * 容器档案Excel导入数据库
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
	 * 导出数据
	 */
	@SuppressWarnings("unchecked")
	public void dataExport() throws Exception {
		List<Record> rows = service.list(getKv());
		for (Record row : rows) {
			row.put("isinner", StringUtils.equals("1", row.getStr("isinner")) ? "社外" : "社内");
		}
		renderJxls("container.xlsx", Kv.by("rows", rows), "容器档案_" + DateUtil.today() + ".xlsx");
	}

	/**
	 * 入库
	 */
	public void rk() {
		//标识-入库
		set("mark", "1");
		render("crk_form.html");
	}

	/**
	 * 出库
	 */
	public void ck() {
		//标识-出库
		set("mark", "0");
		render("crk_form.html");
	}

	/**
	 * 容器下拉信息查询
	 */
	public void crkSelectData(String mark){
		Kv kv = Kv.create();
		kv.set("isInner", mark);
		kv.set("isEnabled", 1);
		renderJsonData(service.list(kv));
	}

	/**
	 * 出入库数据源
	 */
	public void dataIndex(String mark) {
		set("mark", mark);
		render("crk_index.html");
	}
	/**
	 * 出入库数据源
	 */
	public void crkData(String mark) {
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv().set("isInner",mark).set("isEnabled", 1)));
	}

	/**
	 * 容器出入库处理
	 *
	 */
	public void handleData(String mark){
		renderJsonData(service.handleData(getJBoltTable(),mark));
	}

	/**
	 * 容器打印数据
	 *
	 */
	public void printData(){
		renderJsonData(service.getPrintDataCheck(getKv()));
	}
}
