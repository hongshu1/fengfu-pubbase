package cn.rjtech.admin.purchased;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.barcodeencodingm.BarcodeencodingmService;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.expensebudget.ExpenseBudgetService;
import cn.rjtech.admin.expensebudgetitem.ExpenseBudgetItemService;
import cn.rjtech.admin.investmentplan.InvestmentPlanService;
import cn.rjtech.admin.investmentplanitem.InvestmentPlanItemService;
import cn.rjtech.admin.projectcard.ProjectCardService;
import cn.rjtech.cache.PurchasedCache;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.IsEnableEnum;
import cn.rjtech.enums.ProposalmSourceTypeEnum;
import cn.rjtech.enums.ServiceTypeEnum;
import cn.rjtech.model.momdata.ExpenseBudget;
import cn.rjtech.model.momdata.ExpenseBudgetItem;
import cn.rjtech.model.momdata.InvestmentPlan;
import cn.rjtech.model.momdata.InvestmentPlanItem;
import cn.rjtech.model.momdata.ProjectCard;
import cn.rjtech.model.momdata.Purchased;
import cn.rjtech.util.ValidationUtils;
import static cn.hutool.core.util.StrUtil.COMMA;
/**
 * 申购单管理 Service
 *
 * @ClassName: PurchasedService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-09-14 15:03
 */
public class PurchasedService extends BaseService<Purchased> {

    private final Purchased dao = new Purchased().dao();

    @Override
    protected Purchased dao() {
        return dao;
    }

    @Inject
    private BarcodeencodingmService barcodeencodingmService;
    @Inject
    private ExpenseBudgetService expenseBudgetService;
    @Inject
    private ExpenseBudgetItemService expenseBudgetItemService;
    @Inject
    private InvestmentPlanService investmentPlanService;
    @Inject
    private InvestmentPlanItemService investmentPlanItemService;
    @Inject
    private DepartmentService departmentService;
    @Inject
    private ProjectCardService projectCardService;

    /**
     * 后台管理分页查询
     *
     * @param pageNumber
     * @param pageSize
     * @param keywords
     * @return
     */
    public Page<Purchased> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "desc", pageNumber, pageSize, keywords, "name");
    }

    /**
     * 保存
     *
     * @param purchased
     * @return
     */
    public Ret save(Purchased purchased) {
        if (purchased == null || isOk(purchased.getIautoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        tx(() -> {
            // ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);


            ValidationUtils.isTrue(purchased.save(), ErrorMsg.SAVE_FAILED);


            // TODO 其他业务代码实现

            return true;
        });

        // 添加日志
        // addSaveSystemLog(purchased.getIautoid(), JBoltUserKit.getUserId(), purchased.getName());
        return SUCCESS;
    }

    /**
     * 更新
     */
    public Ret update(Purchased purchased) {
        if (purchased == null || notOk(purchased.getIautoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        tx(() -> {
            // 更新时需要判断数据存在
            Purchased dbPurchased = findById(purchased.getIautoid());
            ValidationUtils.notNull(dbPurchased, JBoltMsg.DATA_NOT_EXIST);

            // TODO 其他业务代码实现
            // ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);

            ValidationUtils.isTrue(purchased.update(), ErrorMsg.UPDATE_FAILED);

            return true;
        });

        //添加日志
        //addUpdateSystemLog(purchased.getIautoid(), JBoltUserKit.getUserId(), purchased.getName());
        return SUCCESS;
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        tx(() -> {
            for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
                long iAutoId = Long.parseLong(idStr);
                Purchased dbPurchased = findById(iAutoId);
                ValidationUtils.notNull(dbPurchased, JBoltMsg.DATA_NOT_EXIST);

                // TODO 可能需要补充校验组织账套权限
                // TODO 存在关联使用时，校验是否仍在使用

                ValidationUtils.isTrue(dbPurchased.delete(), JBoltMsg.FAIL);
            }

            return true;
        });

        // 添加日志
        // Purchased purchased = ret.getAs("data");
        // addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, purchased.getName());
        return SUCCESS;
    }

    /**
     * 删除数据后执行的回调
     *
     * @param purchased 要删除的model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Purchased purchased, Kv kv) {
        //addDeleteSystemLog(purchased.getIautoid(), JBoltUserKit.getUserId(),purchased.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param purchased 要删除的model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(Purchased purchased, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(purchased, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     *
     * @return
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 根据申购编号获取数据
     *
     * @param ipurchaseid
     * @return
     */
    public List<Record> findByPurchaseId(Long ipurchaseid) {
        List<Record> list = findRecord(selectSql().eq("iPurchaseId", ipurchaseid).orderBy("iprojectcardid", "asc"));
        for (int i = 0; i < list.size(); i++) {
            Record item = list.get(i);
            item.put("currencyname", PurchasedCache.me.getCurrencyName(item.getStr("ccurrency")));
            item.put("cvenname", PurchasedCache.me.getCvenname(item.getStr("cvencode")));
            // 设置预算编码
            String cbudgetno = "";
            BigDecimal ibudgetmoney = BigDecimal.ZERO;
            if (item.getLong("isourcetype") == ProposalmSourceTypeEnum.EXPENSE_BUDGET.getValue()) {
                ExpenseBudgetItem expenseBudgetItem = expenseBudgetItemService.findById(item.getLong("isourceid"));
                cbudgetno = Optional.ofNullable(expenseBudgetItem).map(ExpenseBudgetItem::getCbudgetno).orElse(null);
                ibudgetmoney = expenseBudgetItem.getIamounttotal();
            } else if (item.getLong("isourcetype") == ProposalmSourceTypeEnum.INVESTMENT_PLAN.getValue()) {
                InvestmentPlanItem investmentPlanItem = investmentPlanItemService.findById(item.getLong("isourceid"));
                cbudgetno = Optional.ofNullable(investmentPlanItem).map(InvestmentPlanItem::getCplanno).orElse(null);
                ibudgetmoney = investmentPlanItem.getIamounttotal();
            }
            item.put("cbudgetno", cbudgetno);
            item.put("ibudgetmoney", ibudgetmoney);
            item.put("index", i + 1);
            item.put("ddemandate", JBoltDateUtil.format(item.getDate("ddemandate"), "YYYY-MM-dd"));

        }
        return list;
    }
    /**
     * 根据申购编号获取数据
     * @param ipurchaseid
     * @return
     */
    public List<Record> refBudgetDatas(Long ipurchaseid) {
        List<Record> list = findRecord(selectSql().eq("iPurchaseId", ipurchaseid).orderBy("iprojectcardid", "asc"));
        for (int i = 0; i < list.size(); i++) {
            Record item = list.get(i);
            item.put("currencyname", PurchasedCache.me.getCurrencyName(item.getStr("ccurrency")));
            item.put("cvenname", PurchasedCache.me.getCvenname(item.getStr("cvencode")));
            // 设置预算编码
            String cbudgetno = "";
            BigDecimal ibudgetmoney = BigDecimal.ZERO;
            if (item.getLong("isourcetype") == ProposalmSourceTypeEnum.EXPENSE_BUDGET.getValue()) {
                ExpenseBudgetItem expenseBudgetItem = expenseBudgetItemService.findById(item.getLong("isourceid"));
                cbudgetno = Optional.ofNullable(expenseBudgetItem).map(ExpenseBudgetItem::getCbudgetno).orElse(null);
                ibudgetmoney = expenseBudgetItem.getIamounttotal();
            } else if (item.getLong("isourcetype") == ProposalmSourceTypeEnum.INVESTMENT_PLAN.getValue()) {
                InvestmentPlanItem investmentPlanItem = investmentPlanItemService.findById(item.getLong("isourceid"));
                cbudgetno = Optional.ofNullable(investmentPlanItem).map(InvestmentPlanItem::getCplanno).orElse(null);
                ibudgetmoney = investmentPlanItem.getIamounttotal();
            }
            item.put("cbudgetno", cbudgetno);
            item.put("ibudgetmoney", ibudgetmoney);
            item.put("ibudgetmoneyhidden", ibudgetmoney);
            Long iprojectcardid = item.getLong("iprojectcardid");
            item.put("ibudgetalreadypurchasemoney", ObjectUtil.equal(item.getInt("isubitem"), IsEnableEnum.NO.getValue()) ? getIBudgetalreadypurchasemoney(iprojectcardid,ipurchaseid):BigDecimal.ZERO);
            item.put("index", i + 1);
            item.put("ddemandate", JBoltDateUtil.format(item.getDate("ddemandate"), "YYYY-MM-dd"));

        }
        return list;
    }
    /**
     * 参照预算-获取申购单项目的已申购金额(原币无税)(不包含本申购单的申购金额)
     * */
    private BigDecimal getIBudgetalreadypurchasemoney(Long iprojectcardid,Long ipurchaseid){
    	List<Purchased> purchasedList = find(selectSql().eq("iprojectcardid", iprojectcardid).notEq("ipurchaseid", ipurchaseid));
    	BigDecimal ibudgetalreadypurchasemoney = BigDecimal.ZERO;
    	for (Purchased purchased : purchasedList) {
    		ibudgetalreadypurchasemoney = ibudgetalreadypurchasemoney.add(purchased.getInatmoney());
		}
    	return ibudgetalreadypurchasemoney;
    }
    /**
     * 获取含税金额汇总
     *
     * @param imid
     * @return
     */
    public BigDecimal getTotalAmountByImid(Long imid) {
        List<Purchased> purchaseds = findByImid(imid);
        if (notOk(purchaseds)) {
            return BigDecimal.ZERO;
        }
        return purchaseds.stream().filter(Objects::nonNull).map(Purchased::getItotalamount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Purchased> findByImid(Long imid) {
    	List<Purchased> list = find(selectSql().eq("iPurchaseId", imid));
    	if(CollUtil.isNotEmpty(list)){
    		for (Purchased purchased : list) {
    			Long iprojectcardid = purchased.getIprojectcardid();
    			ProjectCard projectcard = projectCardService.findById(iprojectcardid);
    			if(ObjectUtil.equal(projectcard.getIservicetype(), ServiceTypeEnum.EXPENSE_BUDGET.getValue())){
    				ExpenseBudgetItem expenseBudgetItem =  expenseBudgetItemService.findById(purchased.getIsourceid());
    				ExpenseBudget expenseBudget = expenseBudgetService.findById(expenseBudgetItem.getIexpenseid());
    				purchased.put("cdepname",departmentService.getCdepName(expenseBudget.getCdepcode()));
    				purchased.put("cbudgetno",expenseBudgetItem.getCbudgetno());
    			}else{
    				InvestmentPlanItem investmentPlanItem =  investmentPlanItemService.findById(purchased.getIsourceid());
    				InvestmentPlan investmentPlan = investmentPlanService.findById(investmentPlanItem.getIplanid());
    				purchased.put("cdepname",departmentService.getCdepName(investmentPlan.getCdepcode()));
    				purchased.put("cbudgetno",investmentPlanItem.getCplanno());
    			}
			}
    	}
    	return list;
    }
}