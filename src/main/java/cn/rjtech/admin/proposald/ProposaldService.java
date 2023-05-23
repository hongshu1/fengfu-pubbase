package cn.rjtech.admin.proposald;

import static cn.hutool.core.text.StrPool.COMMA;
import java.util.List;
import java.util.stream.Collectors;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.expensebudgetitemd.ExpenseBudgetItemdService;
import cn.rjtech.model.momdata.Proposald;
import cn.rjtech.util.ValidationUtils;

/**
 * 禀议项目 Service
 *
 * @ClassName: ProposaldService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-11 16:43
 */
public class ProposaldService extends BaseService<Proposald> {

    private final Proposald dao = new Proposald().dao();

    @Inject
    private ExpenseBudgetItemdService expenseBudgetItemdService;
    @Override
    protected Proposald dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public List<Record> getList(Kv para) {
    	List<Record> list = dbTemplate(u8SourceConfigName(), "proposald.getList", para).find();
    	/*if(CollUtil.isNotEmpty(list)){
	    	for (Record record : list) {
	    		if(record.getInt("isourcetype") == ProposalmSourceTypeEnum.EXPENSE_BUDGET.getValue()){
	    			record.set("ibudgetmoney", record.getBigDecimal("ibudgetmoney"));
	    			record.set("ibudgetsum", expenseBudgetItemdService.getAmountSum(record.getLong("isourceid")));
	    		}else if(record.getInt("isourcetype") == ProposalmSourceTypeEnum.INVESTMENT_PLAN.getValue()){
	    			record.set("ibudgetmoney", expenseBudgetItemdService.getAmountSum(record.getLong("isourceid")));
	    		}
	    			
			}
    	}*/
    	return list;
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        tx(() -> {
            for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
                long iAutoId = Long.parseLong(idStr);
                Proposald dbProposald = findById(iAutoId);
                ValidationUtils.notNull(dbProposald, JBoltMsg.DATA_NOT_EXIST);

                // TODO 可能需要补充校验组织账套权限
                // TODO 存在关联使用时，校验是否仍在使用

                ValidationUtils.isTrue(dbProposald.delete(), JBoltMsg.FAIL);
            }

            return true;
        });

        // 添加日志
        // Proposald proposald = ret.getAs("data");
        // addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, proposald.getName());
        return SUCCESS;
    }

    /**
     * 删除数据后执行的回调
     *
     * @param proposald 要删除的model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Proposald proposald, Kv kv) {
        //addDeleteSystemLog(proposald.getIautoid(), JBoltUserKit.getUserId(),proposald.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param proposald 要删除的model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(Proposald proposald, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(proposald, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    public void deleteByMultiIds(Object[] delete) {
        delete("DELETE FROM PL_ProposalD WHERE iautoid IN (" + ArrayUtil.join(delete, COMMA) + ")");
    }

    public boolean hasRecords(long iproposalmid) {
        return null != queryInt("SELECT TOP 1 1 FROM PL_ProposalD WHERE iproposalmid = ? ", iproposalmid);
    }

    public boolean deleteByIproposalmid(long iproposalmid) {
        return delete("DELETE FROM PL_ProposalD WHERE iproposalmid = ? ", iproposalmid) > 0;
    }

    public List<Record> findByiProposalMid(long iproposalmid) {
        List<Record> records = getList(Kv.by("iproposalmid", iproposalmid));
        return records;
    }

    public List<Record> findByiProposalMid(Long iproposalmid, String noiniproposalds) {
        ValidationUtils.notNull(iproposalmid, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notBlank(noiniproposalds, JBoltMsg.PARAM_ERROR);
        List<Record> records = getList(Kv.by("iproposalmid", iproposalmid)).stream().filter(record -> !noiniproposalds.contains(record.getStr("iautoid"))).collect(Collectors.toList());
        ValidationUtils.notEmpty(records, "没有可新增申购数据");
        return records;
    }
}