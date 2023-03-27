package cn.rjtech.admin.customerclass;

import cn.hutool.core.date.DateUtil;
import cn.jbolt.common.config.JBoltUploadFolder;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.CustomerClass;
import com.jfinal.upload.UploadFile;

import java.util.List;

/**
 * 往来单位-客户分类
 * @ClassName: CustomerClassAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-21 11:01
 */
@CheckPermission(PermissionKey.CUSTOMER)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/customerclass", viewPath = "/_view/admin/customerclass")
public class CustomerClassAdminController extends BaseAdminController {

	@Inject
	private CustomerClassService service;

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
		if(isOk(get("autoid"))){
			CustomerClass customerclass=service.findById(get("autoid"));
			set("pid", customerclass.getIAutoId());
		}
		render("add.html");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		CustomerClass customerclass=service.findById(getLong(0));
		if(customerclass == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("pid", customerclass.getIPid());
		set("customerclass",customerclass);
		render("edit.html");
	}

	/**
	 * 保存
	 */
	public void save() {
		renderJson(service.save(getModel(CustomerClass.class, "customerclass")));
	}

	/**
	 * 更新
	 */
	public void update() {
		renderJson(service.update(getModel(CustomerClass.class, "customerclass")));
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
	 * 树结构数据源
	 */
	public void mgrTree(){
		renderJsonData(service.getMgrTree(getLong("selectId",getLong(0)),getInt("openLevel",0)));
	}

	/**
	 * select数据源 只需要enable=true的
	 */
	public void enableOptions() {
		renderJsonData(service.getTreeDatas(true,true));
	}

	/**
	 * 导出数据
	 */
	@SuppressWarnings("unchecked")
	public void dataExport() throws Exception {
		List<Record> rows = service.getDataExport(getKv());

		renderJxls("customerclass.xlsx", Kv.by("rows", rows), "客户分类_" + DateUtil.today() + ".xlsx");
	}

	/**
	 * Excel模板下载
	 */
	@SuppressWarnings("unchecked")
	public void downloadTpl() throws Exception {
		renderJxls("customerclass_import.xlsx", Kv.by("rows", null), "客户分类导入模板.xlsx");
	}

	/**
	 * 客户档案Excel导入数据库
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

}
