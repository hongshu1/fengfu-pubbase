package cn.rjtech.admin.investmentplanmanage;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.investmentplan.InvestmentPlanService;
import cn.rjtech.admin.period.PeriodService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InvestmentPlan;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Record;

@CheckPermission(PermissionKey.INVESTMENT_PLAN_MANAGE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/investmentplanmanage", viewPath = "/_view/admin/investmentplanmanage")
public class InvestmentPlanManageAdminController extends BaseAdminController {
	@Inject
	private InvestmentPlanManageService service;
	@Inject
	private InvestmentPlanService investmentPlanService;	
	@Inject
	private PeriodService periodService;
	@Inject
	private DepartmentService departmentService;
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
     * 详情
     */
	@CheckPermission(PermissionKey.INVESTMENT_PLAN_MANAGE_DETAIL)
    public void detail() {
        Record investmentPlan = service.findInvestmentPlanDataForDetail(getLong(0));
        set("investmentPlan", investmentPlan);
        render("detail.html");
    }
    
    /**
     * 投资计划编制详情界面查询投资计划项目数据
     * */
    public void findInvestmentPlanItemForDetail(@Para("investmentPlanId") Long investmentPlanId) {
        renderJsonData(service.findInvestmentPlanItemForDetail(investmentPlanId));
    }
    /**
     *	投资计划生效 
     * */
    @CheckPermission(PermissionKey.INVESTMENT_PLAN_MANAGE_EFFECT)
    public void effect(){
    	renderJson(service.effect(getLong(0)));
    }
    /**
     *	投资计划失效
     * */
    @CheckPermission(PermissionKey.INVESTMENT_PLAN_MANAGE_CANCLE)
    public void cancle(){
    	renderJson(service.cancle(getLong(0)));
    }
    
    /**
     * 查看审批界面
     * */
    @UnCheck
    public void investmentFormApprovalFlowIndex(){
    	InvestmentPlan investmentPlan = investmentPlanService.findById(getLong("iautoid"));
    	set("investmentPlan", investmentPlan);
    	render("approve_process_index.html");
    }
}
