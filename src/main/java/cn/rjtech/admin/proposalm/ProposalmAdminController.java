package cn.rjtech.admin.proposalm;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt._admin.globalconfig.GlobalConfigService;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.annotation.CheckDataPermission;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.common.enums.BusObjectTypeEnum;
import cn.jbolt.core.common.enums.DataOperationEnum;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.expensebudget.ExpenseBudgetService;
import cn.rjtech.admin.expensebudgetitem.ExpenseBudgetItemService;
import cn.rjtech.admin.expensebudgetitemd.ExpenseBudgetItemdService;
import cn.rjtech.admin.investmentplan.InvestmentPlanService;
import cn.rjtech.admin.investmentplanitem.InvestmentPlanItemService;
import cn.rjtech.admin.investmentplanitemd.InvestmentPlanItemdService;
import cn.rjtech.admin.period.PeriodService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.admin.proposalattachment.ProposalAttachmentService;
import cn.rjtech.admin.proposald.ProposaldService;
import cn.rjtech.admin.subjectm.SubjectmService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.cache.UserCache;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.IsEnableEnum;
import cn.rjtech.enums.ProposaldTypeEnum;
import cn.rjtech.enums.ServiceTypeEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 禀议管理 Controller
 *
 * @ClassName: ProposalmAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-09-22 11:56
 */
@CheckPermission(PermissionKey.PROPOSALM)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/proposalm", viewPath = "/_view/admin/proposalm")
public class ProposalmAdminController extends BaseAdminController {

    @Inject
    private ProposalmService service;
    @Inject
    private SubjectmService subjectmService;
    @Inject
    private ProposaldService proposaldService;
    @Inject
    private DepartmentService departmentService;
    @Inject
    private GlobalConfigService globalConfigService;
    @Inject
    private ExpenseBudgetService expenseBudgetService;
    @Inject
    private InvestmentPlanService investmentPlanService;
    @Inject
    private ExpenseBudgetItemService expenseBudgetItemService;
    @Inject
    private PersonService personService;
    @Inject
    private ProposalAttachmentService proposalAttachmentService;
    @Inject
    private ExpenseBudgetItemdService expenseBudgetItemdService;
    @Inject
    private InvestmentPlanItemService investmentPlanItemService;
    @Inject
    private InvestmentPlanItemdService investmentPlanItemdService;
    @Inject
    private PeriodService periodService;

    /**
     * 首页
     */
    public void index() {
        render("index.html");
    }

    /**
     * 审批列表
     */
    @CheckPermission(PermissionKey.PROPOSALM_AUDIT_INDEX)
    public void auditIndex() {
        render("audit_index.html");
    }

    /**
     * 数据源
     */
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
    public void datas() {
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }


    /**
     * 新增禀议书-参照预算/投资
     */
    public void addBudgetOrInvestmentPlan(@Para(value = "expensebudgetitemid") String expensebudgetitemid, @Para(value = "investmentplanitemid") String investmentplanitemid) {
        String itemidandtypes = "";
        if (JBoltStringUtil.isNotBlank(expensebudgetitemid)) itemidandtypes += expensebudgetitemid + ",";
        if (JBoltStringUtil.isNotBlank(investmentplanitemid)) itemidandtypes += investmentplanitemid + ",";
        ValidationUtils.notBlank(itemidandtypes, "请选择费用/计划!");
        itemidandtypes = itemidandtypes.substring(0, itemidandtypes.lastIndexOf(","));
        String cdepcode = service.isSameCdepCode(itemidandtypes);
        ValidationUtils.notBlank(cdepcode, JBoltMsg.PARAM_ERROR);
        User user = JBoltUserKit.getUser();
        Proposalm proposalm = new Proposalm();
        proposalm.setDApplyDate(new Date());
        proposalm.setCDepCode(cdepcode);
        proposalm.setIsSupplemental(false);
        Person contro = personService.findById(UserCache.ME.getPersonId(user.getId(), getOrgId()));
        if (null != contro) {
            proposalm.setCApplyPersonCode(contro.getStr("cpsn_num"));
            proposalm.setCApplyPersonName(user.getName());
        }
        set("proposalds", getProposaldDatas(expensebudgetitemid, investmentplanitemid, proposalm, false));
        set("proposalm", proposalm);
        set("cdepname", departmentService.getCdepName(proposalm.getCDepCode()));
        render("add.html");

    }

    private List<Record> getBudgetProposaldDatas(String iexpensebudgetitemids) {
        List<Record> items = expenseBudgetItemService.findByIautoids(iexpensebudgetitemids);
        ValidationUtils.notEmpty(items, "预算项目不存在");

        List<Record> proposalds = new ArrayList<>();

        for (Record item : items) {
            // 主表记录
            ExpenseBudget eb = expenseBudgetService.findById(item.getLong("iexpenseid"));

            proposalds.add(new Record()
                    .set("isourceid", item.getLong("iautoid"))
                    .set("itype", ProposaldTypeEnum.SOURCE.getValue())
                    .set("cbudgetno", item.getStr("cbudgetno"))
                    .set("clowestsubjectname", subjectmService.getName(item.getLong("ilowestsubjectid")))
                    .set("ibudgetmoney", expenseBudgetItemdService.getAmountSum(item.getLong("iautoid")))
                    .set("citemname", item.getStr("citemname"))
                    .set("cunit", item.getStr("cunit"))
                    .set("ccurrency", "人民币")
                    .set("nflat", 1)
                    .set("itaxrate", globalConfigService.getTaxRate())
                    .set("cBudgetDepCode", eb.getStr("cdepcode"))
                    .set("cbudgetdepname", departmentService.getCdepName(eb.getStr("cdepcode")))
                    .set("isubitem", 0)
            );
        }
        return proposalds;
    }


    /**
     * 新增禀议书-参照预算/投资: 设置禀议书新增界面的内容，包括项目和表单的数据
     * 费用预算每个项目的总金额是不含税的，税率默认取全局参数，项目的预算总金额(含税)需要计算
     * 投资计划每个项目的总金额是含税的，税率默认取编制时录入的税率，项目的预算总金额(不含税)需要计算
     *
     * @param isSupplemental
     */
    private List<Record> getProposaldDatas(String expensebudgetitemid, String investmentplanitemid, Proposalm proposalm, Boolean isSupplemental) {
        List<Record> proposalds = new ArrayList<>();
        BigDecimal ibudgetmoneyTotal = BigDecimal.ZERO;//整份禀议书的预算总金额(不含税)
        BigDecimal ibudgetsumTotal = BigDecimal.ZERO;//整份禀议书的预算总金额(含税)
        //全局参数的税率：费用预算编制未维护税率，默认使用全局参数中的税率，投资计划编制时维护了税率直接取编制维护的说率
        BigDecimal iTaxRate = globalConfigService.getTaxRate();
        BigDecimal iTaxRateMul = iTaxRate.multiply(BigDecimal.valueOf(100L).setScale(2, RoundingMode.HALF_UP));
        //参照费用预算
        if (notNull(expensebudgetitemid)) {
            List<Record> expenseBudgets = expenseBudgetItemService.findByIautoids(expensebudgetitemid);
            for (Record item : expenseBudgets) {
                // 主表记录
                //ExpenseBudget eb = expenseBudgetService.findById(item.getLong("iexpenseid"));
                //费用项目的预算总金额(不含税)
                BigDecimal ibudgetmoney = expenseBudgetItemdService.getAmountSum(item.getLong("iautoid"));
                //费用项目的预算总金额(含税)
                BigDecimal ibudgetsum = ibudgetmoney.multiply(BigDecimal.ONE.add(iTaxRate)).setScale(2, RoundingMode.HALF_UP);
                //禀议书的预算总金额(不含税)累加
                ibudgetmoneyTotal = ibudgetmoneyTotal.add(ibudgetmoney);
                //禀议书的预算总金额(含税)累加
                ibudgetsumTotal = ibudgetsumTotal.add(ibudgetsum);
                proposalds.add(new Record()
                        .set("isourceid", item.getLong("iautoid"))
                        .set("itype", isSupplemental ? ProposaldTypeEnum.NEW.getValue() : ProposaldTypeEnum.SOURCE.getValue())
                        .set("cbudgetno", item.getStr("cbudgetno"))
                        .set("clowestsubjectname", subjectmService.getName(item.getLong("ilowestsubjectid")))
                        .set("ibudgetmoney", ibudgetmoney)
                        .set("ibudgetsum", ibudgetsum)
                        .set("citemname", item.getStr("citemname"))
                        .set("cunit", item.getStr("cunit"))
                        .set("ccurrency", "人民币")
                        .set("nflat", 1)
                        .set("itaxrate", iTaxRateMul)
                        /*.set("cBudgetDepCode", eb.getStr("cdepcode"))*/
                        /*.set("cbudgetdepname", departmentService.getCdepName(eb.getStr("cdepcode")))*/
                        .set("isubitem", IsEnableEnum.YES.getValue())
                        .set("isourcetype", 1)
                        .set("iprojectcardid", item.getLong("iprojectcardid"))
                );
            }
        }
        //参照投资计划
        if (notNull(investmentplanitemid)) {
            List<Record> investmentPlans = investmentPlanItemService.findByIautoids(investmentplanitemid);
            for (Record item : investmentPlans) {
                BigDecimal planItemITaxRate = item.getBigDecimal("itaxrate") == null ? iTaxRateMul : item.getBigDecimal("itaxrate");
                BigDecimal planItemITaxRateCal = planItemITaxRate.divide(BigDecimal.valueOf(100L), 2, RoundingMode.HALF_UP);
                // 主表记录
                //InvestmentPlan plan = investmentPlanService.findById(item.getLong("iplanid"));
                //投资计划项目预算总金额(含税)
                BigDecimal ibudgetsum = investmentPlanItemdService.getAmountSum(item.getLong("iautoid"));
                //投资计划项目预算总金额(不含税) ： 税率取投资计划项目编制时录入的税率，追加的项目默认去全局参数的
                BigDecimal ibudgetmoney = ibudgetsum.divide(BigDecimal.ONE.add(planItemITaxRateCal), 2, RoundingMode.HALF_UP);
                //禀议书的预算总金额(不含税)累加
                ibudgetmoneyTotal = ibudgetmoneyTotal.add(ibudgetmoney);
                //禀议书的预算总金额(含税)累加
                ibudgetsumTotal = ibudgetsumTotal.add(ibudgetsum);
                proposalds.add(new Record()
                        .set("isourceid", item.getLong("iautoid"))
                        .set("itype", isSupplemental ? ProposaldTypeEnum.NEW.getValue() : ProposaldTypeEnum.SOURCE.getValue())
                        .set("cbudgetno", item.getStr("cplanno"))
                        .set("ibudgetmoney", ibudgetmoney)
                        .set("ibudgetsum", ibudgetsum)
                        .set("citemname", item.getStr("citemname"))
                        .set("cunit", item.getStr("cunit"))
                        .set("ccurrency", "人民币")
                        .set("nflat", 1)
                        .set("itaxrate", planItemITaxRate)
                        .set("isubitem", IsEnableEnum.YES.getValue())
                        .set("isourcetype", 2)
                        .set("iprojectcardid", item.getLong("iprojectcardid"))
                );
            }
        }
        proposalm.setIBudgetMoney(ibudgetmoneyTotal);
        proposalm.setIBudgetSum(ibudgetsumTotal);
        return proposalds;
    }

    private List<Record> getInvestmentPlanProposaldDatas(String iinvestmentplanitemids) {
        List<Record> items = investmentPlanItemService.findByIautoids(iinvestmentplanitemids);
        ValidationUtils.notEmpty(items, "计划项目不存在");

        List<Record> proposalds = new ArrayList<>();

        for (Record item : items) {
            // 主表记录
            InvestmentPlan plan = investmentPlanService.findById(item.getLong("iplanid"));

            proposalds.add(new Record()
                    .set("isourceid", item.getLong("iautoid"))

                    .set("itype", ProposaldTypeEnum.SOURCE.getValue())
                    .set("cbudgetno", item.getStr("cplanno"))
                    .set("ibudgetmoney", investmentPlanItemdService.getAmountSum(item.getLong("iautoid")))
                    .set("citemname", item.getStr("citemname"))
                    .set("iquantity", 0)
                    .set("iunitprice", 0)
                    .set("cunit", item.getStr("cunit"))
                    .set("ccurrency", "人民币")
                    .set("nflat", 1)
                    .set("itaxrate", item.getBigDecimal("itaxrate"))
                    .set("cbudgetdepcode", plan.getStr("cdepcode"))
                    .set("cbudgetdepname", departmentService.getCdepName(plan.getStr("cdepcode")))
                    .set("isubitem", 0)
            );
        }
        return proposalds;
    }

    public void checkBudgetItem(@Para(value = "cdepcode") String cdepcode,
                                @Para(value = "iexpensebudgetitemids") String iexpensebudgetitemids) {
        ValidationUtils.notBlank(cdepcode, "缺少部门编码参数");
        ValidationUtils.notBlank(iexpensebudgetitemids, "缺少预算项目ID");

        // 校验用户数据权限
        //DataPermissionKit.validateAccess(JBoltUserKit.getUser(), cdepcode);

        renderJson(expenseBudgetItemService.checkBudgetItem(iexpensebudgetitemids));
    }

    public void checkPlanItem(@Para(value = "cdepcode") String cdepcode,
                              @Para(value = "iinvestmentplanitemids") String iinvestmentplanitemids) {
        ValidationUtils.notBlank(cdepcode, "缺少部门编码参数");
        ValidationUtils.notBlank(iinvestmentplanitemids, "缺少计划项目ID");

        // 校验用户数据权限
        //DataPermissionKit.validateAccess(JBoltUserKit.getUser(), cdepcode);

        renderJson(investmentPlanItemService.checkPlanItem(iinvestmentplanitemids));
    }

    public void checkBudgetItemAndPlanItem(@Para(value = "expensebudgetitemid") String expensebudgetitemid, @Para(value = "investmentplanitemid") String investmentplanitemid, @Para(value = "isSupplemental") Boolean isSupplemental) {
        String allItemIds = "";
        if (JBoltStringUtil.isNotBlank(expensebudgetitemid)) allItemIds += expensebudgetitemid + ",";
        if (JBoltStringUtil.isNotBlank(investmentplanitemid)) allItemIds += investmentplanitemid + ",";
        ValidationUtils.notBlank(allItemIds, "请选择费用/计划");
        allItemIds = allItemIds.substring(0, allItemIds.lastIndexOf(","));
        //String cdepcode = service.isSameCdepCode(allItemIds);
        // 校验用户数据权限
        //DataPermissionKit.validateAccess(JBoltUserKit.getUser(), cdepcode);
        List<Record> proposalds = null;
        //追加禀议-对追加选定的项目进行处理后回填到表格
        if (isSupplemental != null) proposalds = getProposaldDatas(expensebudgetitemid, investmentplanitemid, new Proposalm(), isSupplemental);
        renderJsonData(Okv.by("proposalds", proposalds));
    }

    /**
     * 获取禀议项目的初始数据
     */
    public void budgetData(@Para(value = "iexpensebudgetitemids") String iexpensebudgetitemids) {
        ValidationUtils.notBlank(iexpensebudgetitemids, JBoltMsg.PARAM_ERROR);

        ok(getBudgetProposaldDatas(iexpensebudgetitemids));
    }

    /**
     * 获取禀议项目的初始数据
     */
    public void planData(@Para(value = "iinvestmentplanitemids") String iinvestmentplanitemids) {
        ValidationUtils.notBlank(iinvestmentplanitemids, JBoltMsg.PARAM_ERROR);

        ok(getInvestmentPlanProposaldDatas(iinvestmentplanitemids));
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.PROPOSALM_VIEW)
    public void edit(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.validateId(iautoid, "禀议书主表ID");
        Proposalm proposalm = service.findById(iautoid);
        ValidationUtils.notNull(proposalm, JBoltMsg.DATA_NOT_EXIST);
        Long isourceproposalmid = proposalm.getISourceProposalId();
        if (isourceproposalmid != null) {
            Proposalm sourceProposalm = service.findById(isourceproposalmid);
            set("sourceinatmoney", sourceProposalm.getINatMoney());
            set("sourceinatsum", sourceProposalm.getINatSum());
        }
        set("proposalm", proposalm);
        set("cdepname", departmentService.getCdepName(proposalm.getCDepCode()));
        // 如果是已审核的，设置为只读
        switch (AuditStatusEnum.toEnum(proposalm.getIAuditStatus())) {
            case AWAIT_AUDIT:
            case APPROVED:
                set("readonly", "readonly");
                break;
            default:
                break;
        }

        render("edit.html");
    }

    public void printData(@Para(value = "iautoid") Long iautoid) {
        renderJson(service.printData(iautoid));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.PROPOSALM_DELETE)
    @CheckDataPermission(operation = DataOperationEnum.DELETE, type = BusObjectTypeEnum.DEPTARTMENT)
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 选择费用预算列表
     */
    @UnCheck
    public void chooseExpenseBudget(@Para(value = "iperiodid") Long iperiodid, @Para(value = "cdepcode") String cdepcode, @Para(value = "isourceproposalid") Long isourceproposalid) {
        Period period = periodService.findById(iperiodid);
        keepPara();
        if (period != null) {
            List<Record> yearColumnTxtList = new ArrayList<>();
            List<String> monthColumnTxtList = new ArrayList<>();
            List<Record> quantityAndAmountColumnList = new ArrayList<>();
            periodService.calcDynamicExpenseBudgetTableColumn(period.getDstarttime(), period.getDendtime(), yearColumnTxtList, monthColumnTxtList, quantityAndAmountColumnList);
            set("period", period);
            set("yearcolumntxtlist", yearColumnTxtList);
            set("monthcolumntxtlist", monthColumnTxtList);
            set("quantityandamountcolumnlist", quantityAndAmountColumnList);
        }
        render("choose_expensebudget.html");
    }

    /**
     * 选择投资计划
     */
    @UnCheck
    public void chooseInvestmentPlan(@Para(value = "iperiodid") Long iperiodid, @Para(value = "cdepcode") String cdepcode) {
        Period period = periodService.findById(iperiodid);
        keepPara();
        set("period", period);
        render("choose_investmentplan.html");
    }

    /**
     * 参考费用预算或者投资计划按钮点击选择期间页面
     */
    @CheckPermission(PermissionKey.PROPOSALM_SAVE)
    public void choosePeriod() {
        render("choose_period.html");
    }

    /**
     * 参照费用预算/投资计划
     */
    @UnCheck
    public void chooseExpenseBudgetOrInvestmentPlan(@Para(value = "iperiodIds") String iperiodIds) {
        ValidationUtils.notBlank(iperiodIds, JBoltMsg.PARAM_ERROR);
        List<Period> periodList = periodService.findListModelByIds(iperiodIds);
        Period expenseBudgetPeriod = null;
        Period investmentPlanPeriod = null;
        for (Period period : periodList) {
            if (period.getIservicetype() == ServiceTypeEnum.EXPENSE_BUDGET.getValue()) expenseBudgetPeriod = period;
            if (period.getIservicetype() == ServiceTypeEnum.INVESTMENT_PLAN.getValue()) investmentPlanPeriod = period;
        }
        set("expenseBudgetPeriod", expenseBudgetPeriod);
        set("investmentPlanPeriod", investmentPlanPeriod);
        render("choose_expensebudget_or_investmentplan_v2.html");
    }

    /**
     * 追加禀议-追加选定（参照费用/投资）功能需要查询原禀议书部门的生效状态的费用或者投资
     */
    @UnCheck
    public void chooseEffectivedExpenseBudgetOrInvestmentPlan(@Para(value = "isourceproposalid") Long isourceproposalid) {
        ValidationUtils.notNull(isourceproposalid, "原禀议书ID为空!");
        Proposalm proposalm = service.findById(isourceproposalid);
        ValidationUtils.notNull(proposalm, "原禀议书为空!");
        String cdepcode = proposalm.getCDepCode();
        Kv para = Kv.by("cdepcode", cdepcode);
        ExpenseBudget expenseBudget = expenseBudgetService.findEffectivedExpenseBudgetByDeptCode(para);
        InvestmentPlan investmentPlan = investmentPlanService.findEffectivedInvestmentByDeptCode(para);
        Period expenseBudgetPeriod = null;
        Period investmentPlanPeriod = null;
        if (expenseBudget != null) expenseBudgetPeriod = periodService.findById(expenseBudget.getIPeriodId());
        if (investmentPlan != null) investmentPlanPeriod = periodService.findById(investmentPlan.getIPeriodId());
        keepPara();
        set("expenseBudgetPeriod", expenseBudgetPeriod);
        set("investmentPlanPeriod", investmentPlanPeriod);
        set("cdepcode", cdepcode);
        render("choose_supplemental_expensebudget_or_investmentplan_v2.html");
    }

    /**
     * 编制禀议书界面-选择费用/投资项目
     */
    @UnCheck
    public void editProposalmChooseExpenseBudgetOrInvestmentPlan(@Para(value = "cdepcode") String cdepcode, @Para(value = "iprojectcardids") String iprojectcardids) {
        ValidationUtils.notNull(cdepcode, "禀议书部门为空!");
        Kv para = Kv.by("cdepcode", cdepcode);
        ExpenseBudget expenseBudget = expenseBudgetService.findEffectivedExpenseBudgetByDeptCode(para);
        InvestmentPlan investmentPlan = investmentPlanService.findEffectivedInvestmentByDeptCode(para);
        Period expenseBudgetPeriod = null;
        Period investmentPlanPeriod = null;
        if (expenseBudget != null) expenseBudgetPeriod = periodService.findById(expenseBudget.getIPeriodId());
        if (investmentPlan != null) investmentPlanPeriod = periodService.findById(investmentPlan.getIPeriodId());
        keepPara();
        set("expenseBudgetPeriod", expenseBudgetPeriod);
        set("investmentPlanPeriod", investmentPlanPeriod);
        set("cdepcode", cdepcode);
        render("editproposalm_choose_expensebudget_or_investmentplan_v2.html");
    }

    /**
     * 参照费用预算/投资计划数据源
     */
    @UnCheck
    public void mdatas() {
        renderJsonData(service.paginateMdatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 追加禀议
     */
    @CheckPermission(PermissionKey.PROPOSALM_SAVE)
    public void addProposalM(@Para(value = "iproposalmid") Long iproposalmid) {
        ValidationUtils.validateId(iproposalmid, "禀议书主表ID");
        Proposalm proposalm = service.findById(iproposalmid);
        ValidationUtils.notNull(proposalm, "禀议书不存在");
        // 校验数据权限
        //DataPermissionKit.validateAccess(JBoltUserKit.getUser(), proposalm.getCDepCode());
        //检验是否存在未生效的申购单,存在不能追加禀议
        //service.isExistsInvaildPurchase(iproposalmid);
        // 原禀议项目
        List<Record> proposalds = proposaldService.findByiProposalMid(iproposalmid);
        for (Record proposald : proposalds) {
            // 预算项目
            proposald.remove("iautoid", "iproposalmid");
        }

        List<ProposalAttachment> attachments = proposalAttachmentService.findByIproposalmid(proposalm.getIAutoId());
        if (CollUtil.isNotEmpty(attachments)) {
            for (ProposalAttachment attachment : attachments) {
                attachment.remove("iautoid", "iproposalmid");
            }
        }
        proposalm.setISourceProposalId(proposalm.getIAutoId())
                .setCSourceProposalNo(proposalm.getCProposalNo())
                .setDApplyDate(new Date()).setIsSupplemental(true)
                .remove("iautoid", "cproposalno", "iauditstatus", "ieffectivestatus");
        set("cdepname", departmentService.getCdepName(proposalm.getCDepCode()));
        set("proposalm", proposalm);
        set("proposalds", proposalds);
        set("attachments", attachments);
        set("sourceinatmoney", proposalm.getINatMoney());//原禀议书申请金额(不含税) 只用于展示 不需要保存
        set("sourceinatsum", proposalm.getINatSum());//原禀议书申请金额(含税) 只用于展示 不需要保存
        render("add.html");
    }

    /**
     * 新增/修改禀议书
     */
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.EDIT, type = BusObjectTypeEnum.DEPTARTMENT)
    public void saveTableSubmit() {
        renderJson(service.saveTableSubmit(getJBoltTables(), JBoltUserKit.getUser(), new Date()));
    }

    /**
     * 撤销提审
     */
    @CheckPermission(PermissionKey.PROPOSALM_WITHDRAW)
    public void withdraw(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.validateId(iautoid, "禀议书主表ID");

        renderJson(service.withdraw(iautoid));
    }

    /**
     * 生效
     */
    @CheckPermission(PermissionKey.PROPOSALM_EFFECT)
    public void effect(@Para(value = "iautoid") Long iproposalmid) {
        ValidationUtils.validateId(iproposalmid, "禀议书ID");

        renderJson(service.effect(iproposalmid, JBoltUserKit.getUserId(), new Date()));
    }

    /**
     * 上传附件页面
     */
    @UnCheck
    public void attachment() {
        render("proposald_attachment_add.html");
    }

    // ----------------------------------------------------
    // 禀议书明细
    // ----------------------------------------------------

    /**
     * 禀议明细数据首页
     */
    @CheckPermission(PermissionKey.STATISTIC_ANALYSIS_PROPOSAL_DETAIL)
    public void detailIndex() {
        render("detail.html");
    }

    /**
     * 禀议明细数据
     */
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
    public void detaiDatas() {
        renderJsonData(service.paginateDetails(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 禀议明细数据导出
     */
    @UnCheck
    public void detaiDatasExport() {
        renderBytesToExcelXlsFile(service.getExcelReport(getKv()).setFileName("禀议明细Excel"));
    }

    /**
     * 移除行
     */
    public void deleteByAjax() {
        renderJson(service.deleteByAjax());
    }

    @UnCheck
    public void validateProposalMoneyIsExceed() {
        renderJsonData(service.validateProposalMoneyIsExceed(getKv()));
    }
    /**
     * 查看审批界面
     * */
    @UnCheck
    public void proposalmFormApprovalFlowIndex(){
    	Proposalm proposalm = service.findById(getLong("iautoid"));
    	set("proposalm", proposalm);
    	render("approve_process_index.html");
    }
    @CheckPermission(PermissionKey.PROPOSALM_UNEFFECT)
    public void uneffect(){
    	renderJson(service.uneffect(getLong()));
    }
}
