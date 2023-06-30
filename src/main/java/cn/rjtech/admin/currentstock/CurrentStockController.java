package cn.rjtech.admin.currentstock;


import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.*;
import cn.rjtech.admin.stockchekvouch.StockChekVouchService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.StockCheckVouch;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;


/**
 * 盘点单 Controller
 *
 * @ClassName: CurrentStockController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-31 14:38
 */
@CheckPermission(PermissionKey.CURRENTSTOCK)
@UnCheckIfSystemAdmin
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
	 * 选择数据源
	 * */
	public void CurrentStockByDatas(){
		renderJsonData(service.CurrentStockByDatas(getPageNumber(),getPageSize(),getKv()));

	}
	/**
	 * 列表也数据
	 * */
	public void datas() {
		renderJsonData(service.datas_calculate(getPageNumber(),getPageSize(),getKv()));
	}


	/**
	 * 列表也数据
	 * */
	public void getdatas() {
		renderJsonData(service.getdatas(getPageNumber(),getPageSize(),getKv()));
	}

	/**
	 * 盘点单物料详情列表
	 * */
	public void invDatas() {
		renderJsonData(service.invDatas(getKv()));
	}



	public void barcodeDatas() {
		renderJsonData(service.barcodeDatas(getPageNumber(),getPageSize(),getKv()));
	}



	public void add(){
		render("add.html");
   }
   /**
    * 继续盘点页面*/
   public void stockEdit(){
	   Kv kv = getKv();
	   String autoid = kv.getStr("autoid");
	   StockCheckVouch stockChekVouch = stockChekVouchService.findById(autoid);
	   set("SysStockchekvouch", stockChekVouch);
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

    @UnCheck
	public void autocompleteUser() {
		renderJsonData(service.autocompleteUser(getKv()));
	}
    
	/**
	 * 新增提交
	 * */
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
	   stockcheckvouch.setIsDeleted("0");
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

   /**
	* 盘点单
	* */
	public void jboltTableSubmit(){
		renderJson(service.jboltTableSubmit(getJBoltTable()));
	}

   public void saveSubmit(){
   	renderJson(service.saveSubmit(getKv()));
   }

	/**
	 * 逻辑删除
	 * @param kv 业务id
	 */
	public void delete(Kv kv) {
		StockCheckVouch mid = stockChekVouchService.findById(kv.get("mid"));
		mid.setIsDeleted("1");
		stockChekVouchService.update(mid);
	}
}
