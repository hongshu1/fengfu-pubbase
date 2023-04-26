package cn.rjtech.admin.stockoutqcformm;

import java.util.List;

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
	* 数据源
	*/
	public void datas() {
		Page<Record> recordPage = service.pageList(getKv());
		renderJsonData(recordPage);
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
		Record record = service.getCheckoutListByIautoId(stockoutQcFormM.getIAutoId());
		List<Record> stockoutqcformlist = service.getonlyseelistByiautoid(stockoutQcFormM.getIAutoId());
		set("stockoutqcformlist", stockoutqcformlist);
		set("stockoutqcformm", stockoutQcFormM);
		set("record", record);
		render("editstockoutqcformmTable.html");
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
	 * 导入图片
	 */
	public void uploadImage() {
		String uploadPath = JBoltUploadFolder.todayFolder(ExtendUploadFolder.EXTEND_ITEMMASTER_EDITOR_IMAGE + "/inventory" + "/");
		renderJsonData(service.uploadImage(getFiles(ExtendUploadFolder.EXTEND_ITEMMASTER_EDITOR_IMAGE + "/inventory" + "/")));
	}

	/*
	 * 在编辑页面点击确定
	 * */
	public void editTable(JBoltPara JboltPara) {
		renderJson(service.editTable(JboltPara));
	}

	/*
	 * 在检验页面点击确定
	 * */
	public void editCheckOutTable(JBoltPara JboltPara) {
		renderJson(service.editCheckOutTable(JboltPara));
	}

	/**
	 * 点击查看时，进入弹窗自动加载table的数据
	 */
	public void getonlyseeDatas() {
		renderJsonData(service.getonlyseelistByiautoid(getKv()));
	}

	/*
	 * 生成
	 * */
	public void createTable(JBoltPara jBoltPara) {
		renderJson(service.createTable(jBoltPara));
	}

	/**
	 * 切换isCpkSigned
	 */
	public void toggleIsCpkSigned() {
		renderJson(service.toggleBoolean(getLong(0), "isCpkSigned"));
	}
}
