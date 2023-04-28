package cn.rjtech.admin.stockoutqcformm;

import java.util.List;

import com.google.gson.Gson;
import com.jfinal.aop.Inject;

import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.extend.config.ExtendUploadFolder;
import cn.rjtech.admin.stockoutqcformd.StockoutQcFormDService;
import cn.rjtech.admin.stockoutqcformdline.StockoutqcformdLineService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;

import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.RcvDocQcFormM;
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
	private StockoutQcFormMService     service;
	@Inject
	private StockoutqcformdLineService stockoutqcformdLineService;
	@Inject
	private StockoutQcFormDService     stockoutQcFormDService;

   /**
	* 首页
	*/
	public void index() {
		render("index.html");
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
	* 数据源
	*/
	public void datas() {
		renderJsonData(service.pageList(getKv()));
	}

	/*
	 * 生成
	 * */
	public void createTable(@Para(value = "iautoid") Long iautoid,
							@Para(value = "cqcformname") String cqcformname) {
		renderJson(service.createTable(iautoid,cqcformname));
	}

	/**
	 * 跳转到检验页面
	 */
	public void checkout() {
		StockoutQcFormM stockoutQcFormM = service.findById(getLong(0));
		Record record = service.getCheckoutListByIautoId(stockoutQcFormM.getIAutoId());
		set("stockoutqcformm", stockoutQcFormM);
		set("record", record);
		render("checkout.html");
	}

	/*
	 * 点击检验时，进入弹窗自动加载table的数据
	 * */
	public void getCheckOutTableDatas() {
		renderJsonData(service.getCheckOutTableDatas(getKv()));
	}

	/*
	 * 在检验页面点击确定
	 * */
	public void saveCheckOutTable(JBoltPara JboltPara) {
		renderJson(service.saveCheckOutTable(JboltPara));
	}

	/**
	 * 打开onlysee页面
	 */
	public void onlysee() {
		StockoutQcFormM stockoutQcFormM = service.findById(getLong(0));
		Record record = service.getCheckoutListByIautoId(stockoutQcFormM.getIAutoId());
		List<Record> stockoutqcformlist = service.getonlyseelistByiautoid(stockoutQcFormM.getIAutoId());
		set("stockoutqcformlist", stockoutqcformlist);
		set("stockoutqcformm", stockoutQcFormM);
		set("record", record);
		render("onlysee.html");
	}

	/**
	 * 点击查看时，进入弹窗自动加载table的数据
	 */
	public void getonlyseeDatas() {
		renderJsonData(service.getonlyseelistByiautoid(getKv()));
	}

   /**
	* 编辑
	*/
	public void edit() {
		StockoutQcFormM stockoutQcFormM=service.findById(getLong(0));
		Record record = service.getCheckoutListByIautoId(stockoutQcFormM.getIAutoId());
		List<Record> stockoutqcformlist = service.getonlyseelistByiautoid(stockoutQcFormM.getIAutoId());
		set("stockoutqcformlist", stockoutqcformlist);
		set("stockoutqcformm", stockoutQcFormM);
		set("record", record);
		render("editstockoutqcformmTable.html");
	}

	/*
	 * 在编辑页面点击确定
	 * */
	public void saveEditTable(JBoltPara JboltPara) {
		renderJson(service.saveEditTable(JboltPara));
	}


	/**
	 * 导入图片
	 */
	public void uploadImage() {
		String uploadPath = JBoltUploadFolder.todayFolder(ExtendUploadFolder.EXTEND_ITEMMASTER_EDITOR_IMAGE + "/inventory" + "/");
		renderJsonData(service.uploadImage(getFiles(ExtendUploadFolder.EXTEND_ITEMMASTER_EDITOR_IMAGE + "/inventory" + "/")));
	}

	/**
	 * 切换isCpkSigned
	 */
	public void toggleIsCpkSigned() {
		renderJson(service.toggleBoolean(getLong(0), "isCpkSigned"));
	}
}
