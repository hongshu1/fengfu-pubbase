package cn.rjtech.admin.investmentplanmonthadjustmentm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.annotations.RequestLimit;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.interceptor.RequestLimitInterceptor;
import cn.rjtech.model.momdata.InvestmentplanMonthAdjustmentm;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 投资月度实绩管理 Controller
 * @ClassName: InvestmentplanMonthAdjustmentmAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-02-02 17:30
 */
@CheckPermission(PermissionKey.INVESTMENT_MONTH_ADJUSTMENTM)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/investmentplanmonthadjustmentm", viewPath = "/_view/admin/investmentplanmonthadjustmentm")
public class InvestmentplanMonthAdjustmentmAdminController extends BaseAdminController {

	@Inject
	private InvestmentplanMonthAdjustmentmService service;

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
	* 新增
	*/
	@CheckPermission(PermissionKey.INVESTMENT_MONTH_ADJUSTMENTM_ADD)
	public void add() {
		render("add.html");
	}

   /**
	* 编辑
	*/
	@CheckPermission(PermissionKey.INVESTMENT_MONTH_ADJUSTMENTM_EDIT)
	public void edit() {
		InvestmentplanMonthAdjustmentm investmentplanMonthAdjustmentm = service.findById(useIfPresent(getLong(0)));
		ValidationUtils.notNull(investmentplanMonthAdjustmentm, JBoltMsg.DATA_NOT_EXIST);

		set("investmentplanMonthAdjustmentm", investmentplanMonthAdjustmentm);
		render("edit.html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(useIfValid(InvestmentplanMonthAdjustmentm.class, "investmentplanMonthAdjustmentm")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(useIfValid(InvestmentplanMonthAdjustmentm.class, "investmentplanMonthAdjustmentm")));
	}

   /**
	* 批量删除
	*/
	@CheckPermission(PermissionKey.INVESTMENT_MONTH_ADJUSTMENTM_DELETE)
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}

	/**
	 * 打开选择投资计划界面
	 * */
	public void openPageChooseInvestmentPlan(){
		render("choose_investment_plan.html");
	}
	/**
     * 查询未完成项目数据
     * */
    public void findUnfinishInvestmentPlanItemDatas(){
    	renderJsonData(service.findUnfinishInvestmentPlanItemDatas(getKv()));
    }
    /**
     * 修改详情查看月度实绩项目详情数据
     * */
    public void findInvestmentPlanAdjustmentItemDatas(){
    	renderJsonData(service.findInvestmentPlanAdjustmentItemDatas(getKv()));
    }
    
	/**
     * 投资计划月度管理新增-表格提交
     */
    @UnCheck
    @RequestLimit(time=30,count=1)
    @Before(RequestLimitInterceptor.class)
    public void saveTableByAdd() {
        renderJson(service.saveTableSubmitByAdd(getJBoltTable()));
    }
    
    /**
     * 投资计划月度管理更新-表格提交
     */
    @UnCheck
    @RequestLimit(time=30,count=1)
    @Before(RequestLimitInterceptor.class)
    public void saveTableByUpdate() {
        renderJson(service.saveTableByUpdate(getJBoltTable()));
    }
    /**
	 * 生效
	 * */
	@CheckPermission(PermissionKey.INVESTMENT_MONTH_ADJUSTMENTM_EFFECT)
	public void effect(){
		renderJson(service.effect(getLong(0)));
	}
	/**
	 * 撤销生效
	 * */
	@CheckPermission(PermissionKey.INVESTMENT_MONTH_ADJUSTMENTM_UNEFFECT)
	public void unEffect(){
		renderJson(service.unEffect(getLong(0)));
	}
}
