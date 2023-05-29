package cn.rjtech.admin.investmentplanmonthadjustmentitem;

import java.util.List;

import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import static cn.hutool.core.text.StrPool.COMMA;
import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.InvestmentplanMonthAdjustmentItem;
import cn.rjtech.util.ValidationUtils;

/**
 * 投资月度实绩项目 Service
 * @ClassName: InvestmentplanMonthAdjustmentItemService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-02-02 17:48
 */
public class InvestmentplanMonthAdjustmentItemService extends BaseService<InvestmentplanMonthAdjustmentItem> {

	private final InvestmentplanMonthAdjustmentItem dao = new InvestmentplanMonthAdjustmentItem().dao();

	@Override
	protected InvestmentplanMonthAdjustmentItem dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<InvestmentplanMonthAdjustmentItem> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iautoid","desc", pageNumber, pageSize, keywords, "name");
	}

	/**
	 * 保存
	 * @param investmentplanMonthAdjustmentItem
	 * @return
	 */
	public Ret save(InvestmentplanMonthAdjustmentItem investmentplanMonthAdjustmentItem) {
		if(investmentplanMonthAdjustmentItem==null || isOk(investmentplanMonthAdjustmentItem.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			ValidationUtils.isTrue(investmentplanMonthAdjustmentItem.save(), ErrorMsg.SAVE_FAILED);


			// TODO 其他业务代码实现

			return true;
		});

		// 添加日志
		// addSaveSystemLog(investmentplanMonthAdjustmentItem.getIautoid(), JBoltUserKit.getUserId(), investmentplanMonthAdjustmentItem.getName());
		return SUCCESS;
	}

	/**
	 * 更新
	 */
	public Ret update(InvestmentplanMonthAdjustmentItem investmentplanMonthAdjustmentItem) {
		if(investmentplanMonthAdjustmentItem==null || notOk(investmentplanMonthAdjustmentItem.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// 更新时需要判断数据存在
			InvestmentplanMonthAdjustmentItem dbInvestmentplanMonthAdjustmentItem = findById(investmentplanMonthAdjustmentItem.getIautoid());
			ValidationUtils.notNull(dbInvestmentplanMonthAdjustmentItem, JBoltMsg.DATA_NOT_EXIST);

			// TODO 其他业务代码实现
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);

			ValidationUtils.isTrue(investmentplanMonthAdjustmentItem.update(), ErrorMsg.UPDATE_FAILED);

			return true;
		});

		//添加日志
		//addUpdateSystemLog(investmentplanMonthAdjustmentItem.getIautoid(), JBoltUserKit.getUserId(), investmentplanMonthAdjustmentItem.getName());
		return SUCCESS;
	}

	/**
	 * 删除 指定多个ID
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
				long iAutoId = Long.parseLong(idStr);
				InvestmentplanMonthAdjustmentItem dbInvestmentplanMonthAdjustmentItem = findById(iAutoId);
				ValidationUtils.notNull(dbInvestmentplanMonthAdjustmentItem, JBoltMsg.DATA_NOT_EXIST);

				// TODO 可能需要补充校验组织账套权限
				// TODO 存在关联使用时，校验是否仍在使用

				ValidationUtils.isTrue(dbInvestmentplanMonthAdjustmentItem.delete(), JBoltMsg.FAIL);
			}

			return true;
		});

		// 添加日志
		// InvestmentplanMonthAdjustmentItem investmentplanMonthAdjustmentItem = ret.getAs("data");
		// addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, investmentplanMonthAdjustmentItem.getName());
		return SUCCESS;
	}

	/**
	 * 删除数据后执行的回调
	 * @param investmentplanMonthAdjustmentItem 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(InvestmentplanMonthAdjustmentItem investmentplanMonthAdjustmentItem, Kv kv) {
		//addDeleteSystemLog(investmentplanMonthAdjustmentItem.getIautoid(), JBoltUserKit.getUserId(),investmentplanMonthAdjustmentItem.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param investmentplanMonthAdjustmentItem 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanDelete(InvestmentplanMonthAdjustmentItem investmentplanMonthAdjustmentItem, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(investmentplanMonthAdjustmentItem, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	public List<InvestmentplanMonthAdjustmentItem>  findByIadjustmentmId(Long iAdjustmentmMid) {
		return find(selectSql().eq("iadjustmentmid", iAdjustmentmMid));
	}

}