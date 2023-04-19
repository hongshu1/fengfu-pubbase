package cn.rjtech.admin.stockoutqcformm;

import com.jfinal.aop.Inject;

import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.extend.config.ExtendUploadFolder;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.StockoutQcFormM;
/**
 * 质量管理-出库检
 * @ClassName: StockoutQcFormMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-12 19:18
 */
@CheckPermission(PermissionKey.STOCKOUTQCFORMM)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/stockoutqcformm", viewPath = "/_view/admin/stockoutqcformm")
public class StockoutQcFormMAdminController extends BaseAdminController {

	@Inject
	private StockoutQcFormMService service;

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
		renderJson(service.save(getModel(StockoutQcFormM.class, "stockoutQcFormM")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		StockoutQcFormM stockoutQcFormM=service.findById(getLong(0));
		if(stockoutQcFormM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("stockoutQcFormM",stockoutQcFormM);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(StockoutQcFormM.class, "stockoutQcFormM")));
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
	 * 检验
	 */
	public void checkout() {
		StockoutQcFormM stockoutQcFormM=service.findById(getLong(0));
		set("stockoutQcFormM",stockoutQcFormM);
		render("checkout.html");
	}

	/**
	 * 查看
	 */
	public void onlysee() {
		StockoutQcFormM stockoutQcFormM=service.findById(getLong(0));
		set("stockoutQcFormM",stockoutQcFormM);
		render("onlysee.html");
	}

	/*
	 * 生成
	 * */
	public void createchecklist(JBoltPara JboltPara){
		//todo 生成检查表
        renderJsonData(null);
	}

	/**
	 * 导入图片
	 */
	public void uploadImage() {
		String uploadPath = JBoltUploadFolder.todayFolder(ExtendUploadFolder.EXTEND_ITEMMASTER_EDITOR_IMAGE + "/inventory" + "/");
		renderJsonData(service.uploadImage(getFiles(ExtendUploadFolder.EXTEND_ITEMMASTER_EDITOR_IMAGE + "/inventory" + "/")));
	}

	/*
	 * 保存或编辑检验表
	 * */
	public void updateTable(JBoltPara JboltPara) {
		renderJson(service.updateEditTable(JboltPara));
	}
}
