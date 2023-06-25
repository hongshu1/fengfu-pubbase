package cn.rjtech.admin.currentstock;


import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.stockchekvouch.StockChekVouchService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.StockCheckVouch;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
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
@CheckPermission(PermissionKey.CURRENT_STOCK)
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
	   StockCheckVouch stockchekvouch = stockChekVouchService.findById(autoid);

	   set("bill", stockchekvouch);
	   set("isapp",0);
	   render("stockEdit.html");
   }

	public void autocompleteWareHouse() {
		renderJsonData(service.autocompleteWareHouse(getKv()));
	}

	public void autocompletePosition() {
		renderJsonData(service.autocompletePosition(getKv()));
	}

	public void autocompleteUser() {
		renderJsonData(service.autocompleteUser(getKv()));
	}
	/**
	 * 新增提交
	 * */
   public void save(){
	   Kv kv = getKv();
	   String checktype = kv.getStr("checktype");
	   String whcode = kv.getStr("whcode");
	   String poscode = kv.getStr("poscode");

	   StockCheckVouch stockcheckvouch=new StockCheckVouch();
	   stockcheckvouch.setCheckType(checktype);
	   stockcheckvouch.setWhCode(whcode);
	   stockcheckvouch.setPoscodes(poscode);
	   stockcheckvouch.setStatus("0");
	   stockcheckvouch.setIsDeleted("0");
	   ValidationUtils.notNull(whcode,"仓库为空!");
	   ValidationUtils.notNull(checktype,"盘点方式为空!");

	   renderJson(service.save2(stockcheckvouch));
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
	* 完成盘点，发起审批,修改盘点单单头状态
	* */
	public void submit(Long formAutoId) {
		service.preSubmitFunc(formAutoId);
	}


	/**
	 * 驳回,修改盘点单单头状态
	 * */
	public void reject(Long formAutoId, boolean isWithinBatch) {
		service.postRejectFunc(formAutoId, isWithinBatch);
	}

	/**
	 * 审核通过,修改盘点单单头状态,并推单给戴工
	 * */
	public void approve(long formAutoId, boolean isWithinBatch) {
		service.postApproveFunc(formAutoId, isWithinBatch);
	}

	/**
	 * 撤回审核
	 */
	public void withdraw(Long formAutoId) {
		service.postWithdrawFunc(formAutoId);
	}

	/**
	 * 反审批
	 * @param formAutoId 业务id
	 */
	public void reverseApprove(long formAutoId, boolean isFirst, boolean isLast) {
		service.preReverseApproveFunc(formAutoId, isFirst, isLast);
	}
//
//	public void agree() {
//		Kv kv = getKv();
//		kv.put("iupdateby", JBoltUserKit.getUserId());
//		kv.put("cupdatename", JBoltUserKit.getUserName());
//		kv.put("dupdatetime", new Date());
//		Ret ret = service.agree(kv);
//		renderJsonData(ret);
//
//	}
//
//	/**
//	 * 完成盘点,修改盘点单单头状态
//	 * */
//	public void okCheck() {
//		Kv kv = getKv();
//		List<StockCheckVouch> autoId = stockChekVouchService.getListByIds((String) kv.get("mid"));
//		StockCheckVouch byId = autoId.get(0);
//
//		byId.setStatus("1");
//		Ret ret = stockChekVouchService.update(byId);
//		renderJsonData(ret);
//	}

}
