package cn.rjtech.admin.investmentplanmonthadjustmentitemd;

import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.InvestmentplanMonthAdjustmentItemd;
import cn.rjtech.util.ValidationUtils;
import static cn.hutool.core.text.StrPool.COMMA;
/**
 * 投资月度实绩项目明细 Service
 * @ClassName: InvestmentplanMonthAdjustmentItemdService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-02-02 17:54
 */
public class InvestmentplanMonthAdjustmentItemdService extends BaseService<InvestmentplanMonthAdjustmentItemd> {

	private final InvestmentplanMonthAdjustmentItemd dao = new InvestmentplanMonthAdjustmentItemd().dao();

	@Override
	protected InvestmentplanMonthAdjustmentItemd dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<InvestmentplanMonthAdjustmentItemd> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iautoid","desc", pageNumber, pageSize, keywords, "name");
	}

	/**
	 * 保存
	 * @param investmentplanMonthAdjustmentItemd
	 * @return
	 */
	public Ret save(InvestmentplanMonthAdjustmentItemd investmentplanMonthAdjustmentItemd) {
		if(investmentplanMonthAdjustmentItemd==null || isOk(investmentplanMonthAdjustmentItemd.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			ValidationUtils.isTrue(investmentplanMonthAdjustmentItemd.save(), ErrorMsg.SAVE_FAILED);


			// TODO 其他业务代码实现

			return true;
		});

		// 添加日志
		// addSaveSystemLog(investmentplanMonthAdjustmentItemd.getIautoid(), JBoltUserKit.getUserId(), investmentplanMonthAdjustmentItemd.getName());
		return SUCCESS;
	}

	/**
	 * 更新
	 */
	public Ret update(InvestmentplanMonthAdjustmentItemd investmentplanMonthAdjustmentItemd) {
		if(investmentplanMonthAdjustmentItemd==null || notOk(investmentplanMonthAdjustmentItemd.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// 更新时需要判断数据存在
			InvestmentplanMonthAdjustmentItemd dbInvestmentplanMonthAdjustmentItemd = findById(investmentplanMonthAdjustmentItemd.getIautoid());
			ValidationUtils.notNull(dbInvestmentplanMonthAdjustmentItemd, JBoltMsg.DATA_NOT_EXIST);

			// TODO 其他业务代码实现
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);

			ValidationUtils.isTrue(investmentplanMonthAdjustmentItemd.update(), ErrorMsg.UPDATE_FAILED);

			return true;
		});

		//添加日志
		//addUpdateSystemLog(investmentplanMonthAdjustmentItemd.getIautoid(), JBoltUserKit.getUserId(), investmentplanMonthAdjustmentItemd.getName());
		return SUCCESS;
	}

	/**
	 * 删除 指定多个ID
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
				long iAutoId = Long.parseLong(idStr);
				InvestmentplanMonthAdjustmentItemd dbInvestmentplanMonthAdjustmentItemd = findById(iAutoId);
				ValidationUtils.notNull(dbInvestmentplanMonthAdjustmentItemd, JBoltMsg.DATA_NOT_EXIST);

				// TODO 可能需要补充校验组织账套权限
				// TODO 存在关联使用时，校验是否仍在使用

				ValidationUtils.isTrue(dbInvestmentplanMonthAdjustmentItemd.delete(), JBoltMsg.FAIL);
			}

			return true;
		});

		// 添加日志
		// InvestmentplanMonthAdjustmentItemd investmentplanMonthAdjustmentItemd = ret.getAs("data");
		// addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, investmentplanMonthAdjustmentItemd.getName());
		return SUCCESS;
	}

	/**
	 * 删除数据后执行的回调
	 * @param investmentplanMonthAdjustmentItemd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(InvestmentplanMonthAdjustmentItemd investmentplanMonthAdjustmentItemd, Kv kv) {
		//addDeleteSystemLog(investmentplanMonthAdjustmentItemd.getIautoid(), JBoltUserKit.getUserId(),investmentplanMonthAdjustmentItemd.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param investmentplanMonthAdjustmentItemd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanDelete(InvestmentplanMonthAdjustmentItemd investmentplanMonthAdjustmentItemd, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(investmentplanMonthAdjustmentItemd, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	public void deleteByItemId(Object iAjustmentItemId) {
		delete(deleteSql().eq("iadjustmentitemid", iAjustmentItemId));
	}

}