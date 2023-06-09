package cn.rjtech.admin.investmentplanitemd;

import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.investmentplanitem.InvestmentPlanItemService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.InvestmentPlanItem;
import cn.rjtech.model.momdata.InvestmentPlanItemd;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 投资计划项目明细 Service
 * @ClassName: InvestmentPlanItemdService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-18 09:38
 */
public class InvestmentPlanItemdService extends BaseService<InvestmentPlanItemd> {

	private final InvestmentPlanItemd dao = new InvestmentPlanItemd().dao();
	@Inject
	private InvestmentPlanItemService investmentPlanItemService;
	@Override
	protected InvestmentPlanItemd dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 */

	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
		return dbTemplate(u8SourceConfigName(),"investmentplanitemd.paginateAdminDatas",para).paginate(pageNumber,pageSize);
	}

	/**
	 * 保存
	 */
	public Ret save(InvestmentPlanItemd investmentPlanItemd) {
		if(investmentPlanItemd==null || isOk(investmentPlanItemd.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			ValidationUtils.isTrue(investmentPlanItemd.save(), ErrorMsg.SAVE_FAILED);


			// TODO 其他业务代码实现

			return true;
		});

		// 添加日志
		// addSaveSystemLog(investmentPlanItemd.getIautoid(), JBoltUserKit.getUserId(), investmentPlanItemd.getName());
		return SUCCESS;
	}

	/**
	 * 更新
	 */
	public Ret update(InvestmentPlanItemd investmentPlanItemd) {
		if(investmentPlanItemd==null || notOk(investmentPlanItemd.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// 更新时需要判断数据存在
			InvestmentPlanItemd dbInvestmentPlanItemd = findById(investmentPlanItemd.getIAutoId());
			ValidationUtils.notNull(dbInvestmentPlanItemd, JBoltMsg.DATA_NOT_EXIST);

			// TODO 其他业务代码实现
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);

			ValidationUtils.isTrue(investmentPlanItemd.update(), ErrorMsg.UPDATE_FAILED);

			return true;
		});

		//添加日志
		//addUpdateSystemLog(investmentPlanItemd.getIautoid(), JBoltUserKit.getUserId(), investmentPlanItemd.getName());
		return SUCCESS;
	}

	/**
	 * 删除 指定多个ID
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
				long iAutoId = Long.parseLong(idStr);
				InvestmentPlanItemd dbInvestmentPlanItemd = findById(iAutoId);
				ValidationUtils.notNull(dbInvestmentPlanItemd, JBoltMsg.DATA_NOT_EXIST);

				// TODO 可能需要补充校验组织账套权限
				// TODO 存在关联使用时，校验是否仍在使用

				ValidationUtils.isTrue(dbInvestmentPlanItemd.delete(), JBoltMsg.FAIL);
			}

			return true;
		});

		// 添加日志
		// InvestmentPlanItemd investmentPlanItemd = ret.getAs("data");
		// addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, investmentPlanItemd.getName());
		return SUCCESS;
	}

    /**
     * 删除数据后执行的回调
     *
     * @param investmentPlanItemd 要删除的model
     * @param kv                  携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(InvestmentPlanItemd investmentPlanItemd, Kv kv) {
        //addDeleteSystemLog(investmentPlanItemd.getIautoid(), JBoltUserKit.getUserId(),investmentPlanItemd.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param investmentPlanItemd 要删除的model
     * @param kv                  携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(InvestmentPlanItemd investmentPlanItemd, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(investmentPlanItemd, kv);
    }

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	public List<InvestmentPlanItemd> findByInvestmentItemId(Long iplanitemid) {
		return find(selectSql().eq("iplanitemid", iplanitemid));
	}

    public BigDecimal getAmountSum(long iplanitemid) {
    	InvestmentPlanItem investmentPlanItem = investmentPlanItemService.findById(iplanitemid);
    	ValidationUtils.notNull(investmentPlanItem, "投资计划项目"+investmentPlanItem.getCplanno()+"为空");
    	return investmentPlanItem.getIamounttotal();
    }
    /**
     * 根据投资计划项目ID删除所有项目明细
     * */
	public void deleteByItemId(Long iplanitemid) {
		delete(deleteSql().eq("iplanitemid", iplanitemid));
	}

}