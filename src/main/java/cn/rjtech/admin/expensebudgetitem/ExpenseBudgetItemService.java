package cn.rjtech.admin.expensebudgetitem;

import static cn.hutool.core.text.StrPool.COMMA;
import java.util.List;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.kit.U8DataSourceKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.expensebudget.ExpenseBudgetService;
import cn.rjtech.admin.period.PeriodService;
import cn.rjtech.enums.DictionaryTypeKeyEnum;
import cn.rjtech.enums.FinishStatusEnum;
import cn.rjtech.enums.IsEnableEnum;
import cn.rjtech.enums.ProposalmSourceTypeEnum;
import cn.rjtech.enums.ServiceTypeEnum;
import cn.rjtech.model.momdata.ExpenseBudget;
import cn.rjtech.model.momdata.ExpenseBudgetItem;
import cn.rjtech.model.momdata.Period;
import cn.rjtech.util.ValidationUtils;

/**
 * 费用预算项目 Service
 *
 * @ClassName: ExpenseBudgetItemService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-09-15 10:02
 */
public class ExpenseBudgetItemService extends BaseService<ExpenseBudgetItem> {

    private final ExpenseBudgetItem dao = new ExpenseBudgetItem().dao();

    @Inject
    private DepartmentService departmentService;
    @Inject
    private ExpenseBudgetService expenseBudgetService;
    @Inject
    private PeriodService periodService;
    @Override
    protected ExpenseBudgetItem dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<ExpenseBudgetItem> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "desc", pageNumber, pageSize, keywords, "name");
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        tx(() -> {
            for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
                long iAutoId = Long.parseLong(idStr);
                ExpenseBudgetItem dbExpenseBudgetItem = findById(iAutoId);
                ValidationUtils.notNull(dbExpenseBudgetItem, JBoltMsg.DATA_NOT_EXIST);

                // TODO 可能需要补充校验组织账套权限
                // TODO 存在关联使用时，校验是否仍在使用

                ValidationUtils.isTrue(dbExpenseBudgetItem.delete(), JBoltMsg.FAIL);
            }

            return true;
        });

        // 添加日志
        // ExpenseBudgetItem expenseBudgetItem = ret.getAs("data");
        // addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, expenseBudgetItem.getName());
        return SUCCESS;
    }

    /**
     * 删除数据后执行的回调
     *
     * @param expenseBudgetItem 要删除的model
     * @param kv                携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(ExpenseBudgetItem expenseBudgetItem, Kv kv) {
        //addDeleteSystemLog(expenseBudgetItem.getIautoid(), JBoltUserKit.getUserId(),expenseBudgetItem.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param expenseBudgetItem 要删除的model
     * @param kv                携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(ExpenseBudgetItem expenseBudgetItem, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(expenseBudgetItem, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    public boolean isNotExistsBudgetNo(String cbudgetNo) {
        return findFirst(selectSql().eq("cbudgetno", cbudgetNo)) == null;
    }

    public List<ExpenseBudgetItem> findByMainId(Long iexpenseid) {
        return find(selectSql().eq("iExpenseId", iexpenseid));
    }

    public Page<Record> paginateMdatas(Integer pageNumber, Integer pageSize, Kv para) {
        User user = JBoltUserKit.getUser();
        //数据权限控制
        para.set("iorgid", getOrgId())
                .set("isSystemAdmin", user.getIsSystemAdmin())
                .set("iservicetype",ServiceTypeEnum.EXPENSE_BUDGET.getValue())
				.set("istatus",FinishStatusEnum.UNFINISHED.getValue())
				.set("isfreeze",IsEnableEnum.NO.getValue());
        Long iperiodid = para.getLong("iperiodid");
        ExpenseBudget expenseBudget = new ExpenseBudget();
        Period period = periodService.findById(iperiodid);
        expenseBudget.setCbegindate(period.getDstarttime());
        expenseBudget.setCenddate(period.getDendtime());
        expenseBudgetService.constructDynamicsDbColumn(expenseBudget,para);
        Page<Record> page = dbTemplate("expensebudgetitem.paginateMdatas", para).paginate(pageNumber, pageSize);
        if (CollUtil.isNotEmpty(page.getList())) {
            for (Record row : page.getList()) {
            	row.set("cdepname", departmentService.getCdepName(row.getStr("cdepcode")));
                row.set("careertypename", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.CAREERTYPE.getValue(), row.getStr("careertype")));
            }
        }
        return page;
    }
    public List<Record> findByIautoids(String iautoids) {
        return findRecords("SELECT * FROM PL_Expense_Budget_Item WHERE iautoid IN (" + iautoids + ")");
    }

    public List<ExpenseBudgetItem> getEdittingBudgetItems(String iexpensebudgetitemids) {
        Okv para = Okv.by("isourcetype", ProposalmSourceTypeEnum.EXPENSE_BUDGET.getValue())
                .set("iexpensebudgetitemids", iexpensebudgetitemids);

        return daoTemplate("expensebudgetitem.getEdittingBudgetItems", para).find();
    }

    public Ret checkBudgetItem(String iexpensebudgetitemids) {
        List<ExpenseBudgetItem> edittingItems = getEdittingBudgetItems(iexpensebudgetitemids);
        if (CollUtil.isNotEmpty(edittingItems)) {
            StringBuilder errMsg = new StringBuilder();
            for (ExpenseBudgetItem item : edittingItems) {
                errMsg.append(item.getCitemname()).append(StrUtil.DASHED).append(item.getCbudgetno()).append("<br/>");
            }
            return fail(errMsg.append("存在于编辑状态的禀议书中，请排除以上选项").toString());
        }
        return SUCCESS;
    }

    /**
     * 根据禀议细表主键获取数据
     * @param kv
     * @return
     */
    public ExpenseBudgetItem getByProposaldIatuoid(Kv kv) {
        return daoTemplate("expensebudgetitem.getByProposaldIatuoid", kv).findFirst();
    }

    /**
     *费用预算差异列表数据
     */
    public List<Record> differencesManagementDatas(Kv para) {
    	para.set("u8dbname",U8DataSourceKit.ME.getU8DbName(getOrgCode()));
    	return dbTemplate("expensebudgetitem.differencesManagementDatas",para).find();
    }
    /**
     *	根据费用预算主表ID查询没有生成项目卡片的预算项目(用于生效)
     * */
	public List<ExpenseBudgetItem> findNotExistsProjectCardExpenseBudgetItem(Long iexpenseid) {
		Kv para = Kv.by("iservicetype", ServiceTypeEnum.EXPENSE_BUDGET.getValue()).set("iorgid",getOrgId()).set("iexpenseid",iexpenseid);
		return daoTemplate("expensebudgetitem.findNotExistsProjectCardExpenseBudgetItem",para).find();
	}
	/**
	 * 根据预算号删除费用预算项目数据
	 * */
	public void deleteByCbudgetNo(String ccode) {
		delete(deleteSql().eq("cbudgetno", ccode));
	}
}