package cn.rjtech.admin.expensebudgetitemd;

import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.NumberUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.expensebudgetitem.ExpenseBudgetItemService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.DictionaryTypeKeyEnum;
import cn.rjtech.enums.EffectiveStatusEnum;
import cn.rjtech.model.momdata.ExpenseBudgetItem;
import cn.rjtech.model.momdata.ExpenseBudgetItemd;
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
 * 费用预算项目明细 Service
 * @ClassName: ExpenseBudgetItemdService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-09-15 10:04
 */
public class ExpenseBudgetItemdService extends BaseService<ExpenseBudgetItemd> {

	private final ExpenseBudgetItemd dao = new ExpenseBudgetItemd().dao();
	@Inject
	private ExpenseBudgetItemService expenseBudgetItemService;
	@Override
	protected ExpenseBudgetItemd dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
		para.set("ieffectivestatus",EffectiveStatusEnum.CANCLE.getValue());
		Page<Record> paginate = dbTemplate("expensebudgetitemd.paginateAdminList", para).paginate(pageNumber, pageSize);
		for (Record row : paginate.getList()) {
			dataDispose(row);
		}
		return paginate;
	}

	/**
	 * 保存
	 * @param expenseBudgetItemd
	 * @return
	 */
	public Ret save(ExpenseBudgetItemd expenseBudgetItemd) {
		if(expenseBudgetItemd==null || isOk(expenseBudgetItemd.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			ValidationUtils.isTrue(expenseBudgetItemd.save(), ErrorMsg.SAVE_FAILED);


			// TODO 其他业务代码实现

			return true;
		});

		// 添加日志
		// addSaveSystemLog(expenseBudgetItemd.getIautoid(), JBoltUserKit.getUserId(), expenseBudgetItemd.getName());
		return SUCCESS;
	}

	/**
	 * 更新
	 */
	public Ret update(ExpenseBudgetItemd expenseBudgetItemd) {
		if(expenseBudgetItemd==null || notOk(expenseBudgetItemd.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// 更新时需要判断数据存在
			ExpenseBudgetItemd dbExpenseBudgetItemd = findById(expenseBudgetItemd.getIautoid());
			ValidationUtils.notNull(dbExpenseBudgetItemd, JBoltMsg.DATA_NOT_EXIST);

			// TODO 其他业务代码实现
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);

			ValidationUtils.isTrue(expenseBudgetItemd.update(), ErrorMsg.UPDATE_FAILED);

			return true;
		});

		//添加日志
		//addUpdateSystemLog(expenseBudgetItemd.getIautoid(), JBoltUserKit.getUserId(), expenseBudgetItemd.getName());
		return SUCCESS;
	}

	/**
	 * 删除 指定多个ID
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
				long iAutoId = Long.parseLong(idStr);
				ExpenseBudgetItemd dbExpenseBudgetItemd = findById(iAutoId);
				ValidationUtils.notNull(dbExpenseBudgetItemd, JBoltMsg.DATA_NOT_EXIST);

				// TODO 可能需要补充校验组织账套权限
				// TODO 存在关联使用时，校验是否仍在使用

				ValidationUtils.isTrue(dbExpenseBudgetItemd.delete(), JBoltMsg.FAIL);
			}

			return true;
		});

		// 添加日志
		// ExpenseBudgetItemd expenseBudgetItemd = ret.getAs("data");
		// addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, expenseBudgetItemd.getName());
		return SUCCESS;
	}

	/**
	 * 删除数据后执行的回调
	 * @param expenseBudgetItemd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(ExpenseBudgetItemd expenseBudgetItemd, Kv kv) {
		//addDeleteSystemLog(expenseBudgetItemd.getIautoid(), JBoltUserKit.getUserId(),expenseBudgetItemd.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param expenseBudgetItemd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanDelete(ExpenseBudgetItemd expenseBudgetItemd, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(expenseBudgetItemd, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	public List<ExpenseBudgetItemd> findByItemId(Long iautoid) {
		return find(selectSql().eq("iexpenseitemid", iautoid));
	}

    public BigDecimal getAmountSum(Long iexpenseitemid) {
    	ExpenseBudgetItem ExpenseBudgetItem = expenseBudgetItemService.findById(iexpenseitemid);
    	ValidationUtils.notNull(ExpenseBudgetItem, "费用预算项目"+ExpenseBudgetItem.getCbudgetno()+"不存在");
    	return ExpenseBudgetItem.getIamounttotal();
    }

	public int deleteByItemId(Long iexpenseBudgetItemId) {
		return delete(deleteSql().eq("iexpenseitemid", iexpenseBudgetItemId));
	}

	/**
	 * 列表数据处理
	 */
	public void  dataDispose(Record row){
		row.set("cusername", JBoltUserCache.me.getName(row.getLong("icreateby")));
		row.set("beginandenddate", JBoltDateUtil.format(row.getDate("cbegindate"), "yyyy.MM")+"-" + JBoltDateUtil.format(row.getDate("cenddate"), "yyyy.MM"));
		row.set("careertypename",JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.CAREERTYPE.getValue(), row.getStr("careertype")));
		//计算合计列
		BigDecimal nowyearmounthquantity1 = row.getBigDecimal("nowyearmounthquantity1") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity1");
		BigDecimal nowyearmounthquantity2 = row.getBigDecimal("nowyearmounthquantity2") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity2");
		BigDecimal nowyearmounthquantity3 = row.getBigDecimal("nowyearmounthquantity3") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity3");
		BigDecimal nowyearmounthquantity4 = row.getBigDecimal("nowyearmounthquantity4") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity4");
		BigDecimal nowyearmounthquantity5 = row.getBigDecimal("nowyearmounthquantity5") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity5");
		BigDecimal nowyearmounthquantity6 = row.getBigDecimal("nowyearmounthquantity6") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity6");
		BigDecimal nowyearmounthquantity7 = row.getBigDecimal("nowyearmounthquantity7") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity7");
		BigDecimal nowyearmounthquantity8 = row.getBigDecimal("nowyearmounthquantity8") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity8");
		BigDecimal nowyearmounthquantity9 = row.getBigDecimal("nowyearmounthquantity9") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity9");
		BigDecimal nowyearmounthquantity10 = row.getBigDecimal("nowyearmounthquantity10") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity10");
		BigDecimal nowyearmounthquantity11 = row.getBigDecimal("nowyearmounthquantity11") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity11");
		BigDecimal nowyearmounthquantity12 = row.getBigDecimal("nowyearmounthquantity12") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthquantity12");

		nowyearmounthquantity1=	NumberUtil.null2Zero(nowyearmounthquantity1);
		nowyearmounthquantity2=	NumberUtil.null2Zero(nowyearmounthquantity2);
		nowyearmounthquantity3=	NumberUtil.null2Zero(nowyearmounthquantity3);
		nowyearmounthquantity4=	NumberUtil.null2Zero(nowyearmounthquantity4);
		nowyearmounthquantity5=	NumberUtil.null2Zero(nowyearmounthquantity5);
		nowyearmounthquantity6=	NumberUtil.null2Zero(nowyearmounthquantity6);
		nowyearmounthquantity7=	NumberUtil.null2Zero(nowyearmounthquantity7);
		nowyearmounthquantity8=	NumberUtil.null2Zero(nowyearmounthquantity8);
		nowyearmounthquantity9=	NumberUtil.null2Zero(nowyearmounthquantity9);
		nowyearmounthquantity10=NumberUtil.null2Zero(nowyearmounthquantity10);
		nowyearmounthquantity11=NumberUtil.null2Zero(nowyearmounthquantity11);
		nowyearmounthquantity12=NumberUtil.null2Zero(nowyearmounthquantity12);

		BigDecimal totalquantity1 = nowyearmounthquantity1.add(nowyearmounthquantity2).add(nowyearmounthquantity3).add(nowyearmounthquantity4).add(nowyearmounthquantity5)
				.add(nowyearmounthquantity6).add(nowyearmounthquantity7).add(nowyearmounthquantity8).add(nowyearmounthquantity9).add(nowyearmounthquantity10)
				.add(nowyearmounthquantity11).add(nowyearmounthquantity12);
		row.set("totalquantity1", totalquantity1);
		BigDecimal nowyearmounthamount1 = row.getBigDecimal("nowyearmounthamount1") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount1");
		BigDecimal nowyearmounthamount2 = row.getBigDecimal("nowyearmounthamount2") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount2");
		BigDecimal nowyearmounthamount3 = row.getBigDecimal("nowyearmounthamount3") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount3");
		BigDecimal nowyearmounthamount4 = row.getBigDecimal("nowyearmounthamount4") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount4");
		BigDecimal nowyearmounthamount5 = row.getBigDecimal("nowyearmounthamount5") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount5");
		BigDecimal nowyearmounthamount6 = row.getBigDecimal("nowyearmounthamount6") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount6");
		BigDecimal nowyearmounthamount7 = row.getBigDecimal("nowyearmounthamount7") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount7");
		BigDecimal nowyearmounthamount8 = row.getBigDecimal("nowyearmounthamount8") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount8");
		BigDecimal nowyearmounthamount9 = row.getBigDecimal("nowyearmounthamount9") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount9");
		BigDecimal nowyearmounthamount10 = row.getBigDecimal("nowyearmounthamount10") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount10");
		BigDecimal nowyearmounthamount11 = row.getBigDecimal("nowyearmounthamount11") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount11");
		BigDecimal nowyearmounthamount12 = row.getBigDecimal("nowyearmounthamount12") == null ? BigDecimal.ZERO:row.getBigDecimal("nowyearmounthamount12");

		nowyearmounthamount1=NumberUtil.null2Zero(nowyearmounthamount1);
		nowyearmounthamount2=NumberUtil.null2Zero(nowyearmounthamount2);
		nowyearmounthamount3=NumberUtil.null2Zero(nowyearmounthamount3);
		nowyearmounthamount4=NumberUtil.null2Zero(nowyearmounthamount4);
		nowyearmounthamount5=NumberUtil.null2Zero(nowyearmounthamount5);
		nowyearmounthamount6=NumberUtil.null2Zero(nowyearmounthamount6);
		nowyearmounthamount7=NumberUtil.null2Zero(nowyearmounthamount7);
		nowyearmounthamount8=NumberUtil.null2Zero(nowyearmounthamount8);
		nowyearmounthamount9=NumberUtil.null2Zero(nowyearmounthamount9);
		nowyearmounthamount10=NumberUtil.null2Zero(nowyearmounthamount10);
		nowyearmounthamount11=NumberUtil.null2Zero(nowyearmounthamount11);
		nowyearmounthamount12=NumberUtil.null2Zero(nowyearmounthamount12);

		BigDecimal totalamount1 = nowyearmounthamount1.add(nowyearmounthamount2).add(nowyearmounthamount3).add(nowyearmounthamount4).add(nowyearmounthamount5)
				.add(nowyearmounthamount6).add(nowyearmounthamount7).add(nowyearmounthamount8).add(nowyearmounthamount9).add(nowyearmounthamount10)
				.add(nowyearmounthamount11).add(nowyearmounthamount12);
		row.set("totalamount1", totalamount1);
		BigDecimal nextyearmounthquantity1 = row.getBigDecimal("nextyearmounthquantity1") == null ? BigDecimal.ZERO:row.getBigDecimal("nextyearmounthquantity1");
		BigDecimal nextyearmounthquantity2 = row.getBigDecimal("nextyearmounthquantity2") == null ? BigDecimal.ZERO:row.getBigDecimal("nextyearmounthquantity2");
		BigDecimal nextyearmounthquantity3 = row.getBigDecimal("nextyearmounthquantity3") == null ? BigDecimal.ZERO:row.getBigDecimal("nextyearmounthquantity3");
		BigDecimal nextyearmounthamount1 = row.getBigDecimal("nextyearmounthamount1") == null ? BigDecimal.ZERO:row.getBigDecimal("nextyearmounthquantity1");
		BigDecimal nextyearmounthamount2 = row.getBigDecimal("nextyearmounthamount2") == null ? BigDecimal.ZERO:row.getBigDecimal("nextyearmounthquantity2");
		BigDecimal nextyearmounthamount3 = row.getBigDecimal("nextyearmounthamount3") == null ? BigDecimal.ZERO:row.getBigDecimal("nextyearmounthquantity3");

		nextyearmounthquantity1=NumberUtil.null2Zero(nextyearmounthquantity1);
		nextyearmounthquantity2=NumberUtil.null2Zero(nextyearmounthquantity2);
		nextyearmounthquantity3=NumberUtil.null2Zero(nextyearmounthquantity3);

		nextyearmounthamount1=NumberUtil.null2Zero(nextyearmounthamount1);
		nextyearmounthamount2=NumberUtil.null2Zero(nextyearmounthamount2);
		nextyearmounthamount3=NumberUtil.null2Zero(nextyearmounthamount3);

		BigDecimal totalquantity2 = nowyearmounthquantity4.add(nowyearmounthquantity5).add(nowyearmounthquantity6).add(nowyearmounthquantity7).add(nowyearmounthquantity8)
				.add(nowyearmounthquantity9).add(nowyearmounthquantity10).add(nowyearmounthquantity11).add(nowyearmounthquantity12).add(nextyearmounthquantity1)
				.add(nextyearmounthquantity2).add(nextyearmounthquantity3);

		BigDecimal totalamount2 = nowyearmounthamount4.add(nowyearmounthamount5).add(nowyearmounthamount6).add(nowyearmounthamount7).add(nowyearmounthamount8)
				.add(nowyearmounthamount9).add(nowyearmounthamount10).add(nowyearmounthamount11).add(nowyearmounthamount12).add(nextyearmounthamount1)
				.add(nextyearmounthamount2).add(nextyearmounthamount3);
		row.set("totalquantity2", totalquantity2);
		row.set("totalamount2", totalamount2);
	}
}