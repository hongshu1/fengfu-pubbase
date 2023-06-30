package cn.rjtech.admin.currentstock;


import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltUserAuthKit;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.stockchekvouch.StockChekVouchService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.StockCheckVouch;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
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
     */
    @UnCheck
	public void datas() {
		renderJsonData(service.datas_calculate(getPageNumber(),getPageSize(),getKv()));
	}


	/**
	 * 列表也数据
	 */
    @UnCheck
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
	* 完成盘点，发起审批,修改盘点单单头状态
	* */
   @CheckPermission(PermissionKey.CURRENT_STOCK_SUBMIT)
   public void submit(@Para(value = "formSn") String formSn,
					  @Para(value = "formAutoId") Long formAutoId,
					  @Para(value = "primaryKeyName") String primaryKeyName,
					  @Para(value = "className") String className,
					  @Para(value = "permissionKey") String permissionKey) {
	   ValidationUtils.notBlank(formSn, "缺少表单编码");
	   ValidationUtils.validateId(formAutoId, "单据ID");
	   ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
	   ValidationUtils.notBlank(className, "缺少实现审批通过业务的类名参数");
	   ValidationUtils.isTrue(JBoltUserAuthKit.hasPermission(JBoltUserKit.getUserId(), permissionKey), "您缺少单据的提审权限");

	   service.preSubmitFunc(formAutoId);
	}


	/**
	 * 驳回,修改盘点单单头状态
	 * */
	@CheckPermission(PermissionKey.CURRENT_STOCK_REJECT)
	public void reject(@Para(value = "formAutoId") Long formAutoId,
					   @Para(value = "formSn") String formSn,
					   @Para(value = "status") Integer status,
					   @Para(value = "primaryKeyName") String primaryKeyName,
					   @Para(value = "className") String className,@Para(value = "remark") String remark) {
		ValidationUtils.validateId(formAutoId, "单据ID");
		ValidationUtils.notBlank(formSn, "表单编码不能为空");
		ValidationUtils.validateIntGt0(status, "审批状态");
		ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
		ValidationUtils.notBlank(className, "处理审批的Service类名");
		service.postRejectFunc(formAutoId,false);
	}

	/**
	 * 审核通过,修改盘点单单头状态,并推单给戴工
	 * */
	@CheckPermission(PermissionKey.CURRENT_STOCK_APPROVE)
	public void approve(@Para(value = "formAutoId") Long formAutoId,
						@Para(value = "formSn") String formSn,
						@Para(value = "status") Integer status,
						@Para(value = "primaryKeyName") String primaryKeyName,
						@Para(value = "className") String className,@Para(value = "remark") String remark) {
		ValidationUtils.validateId(formAutoId, "单据ID");
		ValidationUtils.notBlank(formSn, "表单编码不能为空");
		ValidationUtils.validateIntGt0(status, "审批状态");
		ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
		ValidationUtils.notBlank(className, "缺少实现审批通过业务的类名参数");
		service.postApproveFunc(formAutoId, false);
	}

	/**
	 * 撤回审核
	 */
	@CheckPermission(PermissionKey.CURRENT_STOCK_WITHDRAW)
	public void withdraw(@Para(value = "formAutoId") Long formAutoId,
						 @Para(value = "formSn") String formSn,
						 @Para(value = "primaryKeyName") String primaryKeyName,
						 @Para(value = "className") String className,
						 @Para(value = "permissionKey") String permissionKey) {
		ValidationUtils.validateId(formAutoId, "单据ID");
		ValidationUtils.notBlank(formSn, "表单编码不能为空");
		ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
		ValidationUtils.notBlank(className, "缺少实现审批通过后的业务类名");
		ValidationUtils.isTrue(JBoltUserAuthKit.hasPermission(JBoltUserKit.getUserId(), permissionKey), "您缺少“撤回”的权限");

		service.postWithdrawFunc(formAutoId);
	}

	/**
	 * 反审批
	 * @param formAutoId 业务id
	 */
	@CheckPermission(PermissionKey.CURRENT_STOCK_REVERSE_APPROVE)
	public void reverseApprove(@Para(value = "formAutoId") Long formAutoId,
							   @Para(value = "formSn") String formSn,
							   @Para(value = "status") Integer status,
							   @Para(value = "primaryKeyName") String primaryKeyName,
							   @Para(value = "className") String className,@Para(value = "remark") String remark) {
		ValidationUtils.validateId(formAutoId, "单据ID");
		ValidationUtils.notBlank(formSn, "表单编码不能为空");
		ValidationUtils.validateIntGt0(status, "审批状态");
		ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
		ValidationUtils.notBlank(className, "处理审批的Service类名");
		service.preReverseApproveFunc(formAutoId, false, false);
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



//	public void agree() {
//		Kv kv = getKv();
//		Ret ret = service.agree(stockChekVouchService.findById(kv.get("mid")));
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
