package cn.rjtech.admin.currentstock;


import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.render.JBoltByteFileType;
import cn.jbolt.core.util.JBoltCamelCaseUtil;
import cn.rjtech.admin.stockchekvouch.StockChekVouchService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.StockCheckVouch;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * 盘点单 Controller
 *
 * @ClassName: CurrentStockController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-31 14:38
 */
@CheckPermission(PermissionKey.CURRENTSTOCK)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/currentstock", viewPath = "_view/admin/currentstock")
public class CurrentStockController extends BaseAdminController {

	@Inject
	CurrentStockService service;
	@Inject
	StockChekVouchService stockChekVouchService;


   /**
	* 首页
	*/
	public void index() {
		render("index.html");
	}

	/**
	细表新增选择数据
	 */
	public void CurrentStockBySelect(){
		Kv kv = getKv();
		String whcode = kv.getStr("whcode");
		ValidationUtils.notNull(whcode,"请选择仓库!");
		keepPara();

		render("currentstockbyselect.html");
	}


	/**
	 * 盘点条码 明细
	 */
	@UnCheck
	public void getStockCheckVouchBarcodeLines() {
		String autoid = get("autoid");
		String OrgCode = getOrgCode();
		Kv kv = new Kv();
		kv.set("autoid",autoid== null? "" :autoid);
		kv.set("OrgCode",OrgCode);
		renderJsonData(service.getStockCheckVouchBarcodeLines(kv));
	}

	/**
	 * 根据现品票带出其他数据
	 */
	@UnCheck
	public void barcode(@Para(value = "barcode") String barcode,
						@Para(value = "autoid") Long autoid,
						@Para(value = "detailHidden") String detailHidden,
						@Para(value = "poscodes") String poscodes, //库区
						@Para(value = "whcode") String whcode //仓库
						) {
		ValidationUtils.notBlank(barcode, "请扫码");

		renderJsonData(service.barcode(Kv.by("barcode", barcode).set("autoid",autoid).set("detailHidden",detailHidden).set("whcode",whcode).set("poscodes",poscodes)));
	}


    /**
     * 盘点表主表 数据明细
     */
    @UnCheck
	public void getStockCheckVouchDatas() {
		renderJsonData(service.getStockCheckVouchDatas(getPageNumber(),getPageSize(),getKv()));
	}

	/**
	 * 盘点单物料详情列表 数据明细
	 * */
	public void StockCheckVouchDetailDatas() {
		renderJsonData(service.StockCheckVouchDetailDatas(getKv()));
	}



	public void barcodeDatas() {
		renderJsonData(service.barcodeDatas(getPageNumber(),getPageSize(),getKv()));
	}


	@CheckPermission(PermissionKey.CURRENTSTOCK_ADD)
	public void add(){
		render("add.html");
   }
   /**
    * 继续盘点页面*/
   public void stockEdit(){
	   StockCheckVouch stockChekVouch=service.findById(getLong(0));
	   if(stockChekVouch == null){
		   renderFail(JBoltMsg.DATA_NOT_EXIST);
		   return;
	   }
	   set("poscodes",  stockChekVouch.getPoscodes());
	   set("stockchekvouch", stockChekVouch);
	   set("bill", stockChekVouch);
	   set("isapp",0);
	   render("stockEdit.html");
   }
	/**仓库*/
	public void autocompleteWareHouse() {
		Kv kv = getKv();
		kv.setIfNotNull("OrgCode",getOrgCode());
		renderJsonData(service.autocompleteWareHouse(kv));
	}
	/**库位*/
	public void autocompletePosition() {
		Kv kv = getKv();
		kv.set("whcode", get("whcode") == null ? "" : get("whcode"));
		kv.setIfNotNull("OrgCode",getOrgCode());
		renderJsonData(service.autocompletePosition(kv));
	}

	/**
	 *  库区数据源
	 */
	public void posHouse() {
		renderJsonData(service.getposHouseDatas(getKv()));
	}

	/**
	 *  料品分类数据源
	 */
	public void InventoryClass() {
		renderJsonData(service.getInventoryClassDatas(getKv()));
	}

	/**
	 * JBoltTable 可编辑表格整体提交 多表格
	 */
	@Before(Tx.class)
	public void saveTableSubmit() {
		renderJson(service.saveTableSubmit(getJBoltTables()));
	}

    @UnCheck
	public void autocompleteUser() {
		renderJsonData(service.autocompleteUser(getKv()));
	}
    
	/**
	 * 新增提交
	 * */
	@CheckPermission(PermissionKey.CURRENTSTOCK_ADD)
   public void save(){
	   Kv kv = getKv();

	   String checktype = kv.getStr("checktype");
	   String cdepperson = kv.getStr("cdepperson");
	   String whcode = kv.getStr("whcode");
	   String poscode = kv.getStr("poscode");

	   StockCheckVouch stockcheckvouch=new StockCheckVouch();
	   stockcheckvouch.setCheckType(checktype);
	   stockcheckvouch.setWhCode(whcode);
	   stockcheckvouch.setPoscodes(poscode);
	   stockcheckvouch.setCheckPerson(cdepperson);
	   stockcheckvouch.setIsDeleted(false);
	   ValidationUtils.notNull(whcode,"仓库为空!");
	   ValidationUtils.notNull(checktype,"盘点方式为空!");

	   renderJson(service.save2(stockcheckvouch));
   }
   public void a(){
	   Kv kv = getKv();
	   String checktype = kv.getStr("checktype");
	   String cdepperson = kv.getStr("cdepperson");
	   String checktypetrue = kv.getStr("checktypetrue");
	   String whcode = kv.getStr("whcode");
	   String poscode = kv.getStr("poscode");
	   set(checktype,"checktype");
	   set(cdepperson,"cdepperson");
	   set(checktypetrue,"checktypetrue");
	   set(whcode,"whcode");
	   set(poscode,"poscode");
	   render("stockForm.html");
   }

	@CheckPermission(PermissionKey.CURRENTSTOCK_ADD)
   public void saveSubmit(){
   	renderJson(service.saveSubmit(getKv()));
   }

	/**
	 * 逻辑删除
	 * @param kv 业务id
	 */
	@CheckPermission(PermissionKey.CURRENTSTOCK_DELETE)
	public void delete(Kv kv) {
		StockCheckVouch mid = stockChekVouchService.findById(kv.get("mid"));
		mid.setIsDeleted(false);
		stockChekVouchService.update(mid);
	}

	/**
	 * 批量删除
	 */
	@CheckPermission(PermissionKey.CURRENTSTOCK_DELETE)
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}

	/***
	 * 勾选导出
	 */
	@CheckPermission(PermissionKey.CURRENTSTOCK_EXPORT)
	public void downloadChecked(){
		Kv kv = getKv();
		String ids = kv.getStr("ids");
		if (ids != null) {
			String[] split = ids.split(",");
			String sqlids = "";
			for (String id : split) {
				sqlids += "'" + id + "',";
			}
			ValidationUtils.isTrue(sqlids.length() > 0, "请至少选择一条数据!");
			sqlids = sqlids.substring(0, sqlids.length() - 1);
			kv.set("sqlids", sqlids);
		}

		String sqlTemplate = "currentstock.getStockCheckVouchBarcodeLines";
		List<Record> list = service.download(kv, sqlTemplate);
		JBoltCamelCaseUtil.keyToCamelCase(list);
		//2、创建JBoltExcel
		JBoltExcel jBoltExcel = JBoltExcel
				.create()//创建JBoltExcel
				.addSheet(//设置sheet
						JBoltExcelSheet.create("盘点条码列表")
								.setHeaders(1,//sheet里添加表头
										JBoltExcelHeader.create("posname", "库位名称", 20),
										JBoltExcelHeader.create("invcode", "存货编码", 20),
										JBoltExcelHeader.create("cinvcode1", "客户部番", 20),
										JBoltExcelHeader.create("cinvname1", "部品名称", 20),
										JBoltExcelHeader.create("cinvstd", "规格", 40),
										JBoltExcelHeader.create("cuomname", "主计量单位", 40),
										JBoltExcelHeader.create("iqty", "数量", 40),
										JBoltExcelHeader.create("qty", "实盘数量", 40),
										JBoltExcelHeader.create("barcode", "现品票", 40),
										JBoltExcelHeader.create("cversion", "批次号", 40),
										JBoltExcelHeader.create("dplandate", "生产日期", 40)
								).setDataChangeHandler((data, index) -> {
						})
								.setRecordDatas(2, list)//设置数据
				).setFileName("盘点条码列表-" + new SimpleDateFormat("yyyyMMdd").format(new Date()));
		//3、导出
		renderBytesToExcelXlsFile(jBoltExcel);

	}
	@CheckPermission(PermissionKey.CURRENTSTOCK_EXPORT)
	public void importExcelClass() {
		Long autoid = getLong("autoid");
		String whcode = get("whcode");
		String poscodes = service.pos(get("poscodes"));
		String uploadPath= JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
		System.out.println("===========>"+uploadPath);
		UploadFile file=getFile("file",uploadPath);
		if(notExcel(file)){
			renderJsonFail("请上传excel文件");
			return;
		}
		Ret ret = service.importExcelClass(file.getFile(),autoid,whcode,poscodes);
		renderJson(ret);
	}

	/**
	 * 执行导入excel
	 */
	@CheckPermission(PermissionKey.CURRENTSTOCK_IMPORT)
	public void importExcel() {
		Long autoid = getLong("autoid");
		String whcode = get("whcode");
		String poscodes = service.pos(get("poscodes"));
		UploadFile uploadFile = getFile("file");
		ValidationUtils.notNull(uploadFile, "上传文件不能为空");
        
		File file = uploadFile.getFile();
		List<String> list = StrUtil.split(uploadFile.getOriginalFileName(), StrUtil.DOT);
		ValidationUtils.equals(list.get(1), JBoltByteFileType.XLSX.suffix, "系统只支持xlsx格式的Excel文件");

        renderJson(service.importExcel(file, autoid, whcode, poscodes));
	}
    
}
