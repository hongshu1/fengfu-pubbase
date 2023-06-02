package cn.rjtech.admin.period;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import static cn.hutool.core.text.StrPool.COMMA;
import cn.hutool.core.text.StrSplitter;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.expensebudget.ExpenseBudgetService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.Period;
import cn.rjtech.util.ValidationUtils;

/**
 * 期间管理 Service
 * @ClassName: PeriodService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-09-16 09:33
 */
public class PeriodService extends BaseService<Period> {

	private final Period dao = new Period().dao();

	@Override
	protected Period dao() {
		return dao;
	}

	@Inject
	private UserService userService;
	@Inject
	private ExpenseBudgetService expenseBudgetService;

	/**
	 * 后台管理分页查询
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
		Page<Record> paginate = dbTemplate("period.paginateAdminDatas", para).paginate(pageNumber, pageSize);
		for (Record record : paginate.getList()) {
			record.set("icreateby",JBoltUserCache.me.getUserName(record.getLong("icreateby")));
			if ( record.getStr("iUpdateBy")!=null){
				record.set("iupdateby", JBoltUserCache.me.getUserName(record.getLong("iupdateby")));
			}
			String dstarttime = JBoltDateUtil.format(record.getDate("dstarttime"), "yyyy-MM");
			String dendtime = JBoltDateUtil.format(record.getDate("dendtime"), "yyyy-MM");
			record.set("dstarttime",dstarttime);
			record.set("dendtime",dendtime);
		}
		return paginate;
	}

	/**
	 * 保存
	 */
	public Ret save(Period period) {
		if(isOk(period.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		if (period.getIservicetype()==1) {
			ValidationUtils.notNull(period.getDstarttime(),"费用预算必须设置开始年月");
			ValidationUtils.notNull(period.getDendtime(),"费用预算必须设置截止年月");
			//检查日期截止年月是否大于开始年月
			Date dstarttime = period.getDstarttime();
			Date dendtime = period.getDendtime();
			ValidationUtils.isTrue(dstarttime.before(dendtime),"截止年月不可小于开始年月");
		}
        period.setIorgid(getOrgId());
		period.setCorgcode(getOrgCode());
		period.setIcreateby(JBoltUserKit.getUserId());
		period.setDcreatetime(new Date());
		tx(() -> {
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			ValidationUtils.isTrue(period.save(), ErrorMsg.SAVE_FAILED);
			// TODO 其他业务代码实现
			return true;
		});

		// 添加日志
		// addSaveSystemLog(period.getIautoid(), JBoltUserKit.getUserId(), period.getName());
		return SUCCESS;
	}

	/**
	 * 更新
	 */
	public Ret update(Period period) {
		if(period==null || notOk(period.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		if (period.getIservicetype()==1) {
			ValidationUtils.notNull(period.getDstarttime(),"费用预算必须设置开始年月");
			ValidationUtils.notNull(period.getDendtime(),"费用预算必须设置截止年月");
			//检查日期截止年月是否大于开始年月
			Date dstarttime = period.getDstarttime();
			Date dendtime = period.getDendtime();
			ValidationUtils.isTrue(dstarttime.before(dendtime),"截止年月不可小于开始年月");
		}
		period.setIupdateby(JBoltUserKit.getUserId());
		period.setDupdatetime(new Date());
		period.setIorgid(getOrgId());
		period.setCorgcode(getOrgCode());
		tx(() -> {
			// 更新时需要判断数据存在
			Period dbPeriod = findById(period.getIautoid());
			ValidationUtils.notNull(dbPeriod, JBoltMsg.DATA_NOT_EXIST);

			// TODO 其他业务代码实现
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);

			ValidationUtils.isTrue(period.update(), ErrorMsg.UPDATE_FAILED);

			return true;
		});

		//添加日志
		//addUpdateSystemLog(period.getIautoid(), JBoltUserKit.getUserId(), period.getName());
		return SUCCESS;
	}

	/**
	 * 删除 指定多个ID
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
				long iAutoId = Long.parseLong(idStr);
                Period dbPeriod = findById(iAutoId);
				Boolean isenabled = dbPeriod.getIsenabled();
				ValidationUtils.isTrue(!isenabled,"已经启动的期间不可删除.");
				ValidationUtils.notNull(dbPeriod, JBoltMsg.DATA_NOT_EXIST);

				// TODO 可能需要补充校验组织账套权限
				// TODO 存在关联使用时，校验是否仍在使用

				ValidationUtils.isTrue(dbPeriod.delete(), JBoltMsg.FAIL);
			}

			return true;
		});

		// 添加日志
		// Period period = ret.getAs("data");
		// addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, period.getName());
		return SUCCESS;
	}

	/**
	 * 删除数据后执行的回调
	 * @param period 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(Period period, Kv kv) {
		//addDeleteSystemLog(period.getIautoid(), JBoltUserKit.getUserId(),period.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param period 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanDelete(Period period, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(period, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	/**
	 * 切换isenabled属性
	 */
	public Ret toggleIsenabled(Long id) {
		//检查是否存在未审核的预算
		//if (isenabled){
		//	int size = expenseBudgetService.find("select * from PL_Expense_Budget where iPeriodId = ? and  iAuditStatus =0 ", id).size();
		//	ValidationUtils.isTrue(size==0,"使用该期间的费用预算还未审核,不可关闭该期间");
		//}
		////查询是否存在已经启用的业务类型和预算类型一致的数据
        //int size = find("select * from Bas_Period where   isenabled= 1 and iautoid !=? and iServiceType = ?", period.getIautoid(),period.getIservicetype()).size();
        //ValidationUtils.isTrue(size==0,"该业务类型存在已经启动的期间,请关闭历史期间再启动");
        return toggleBoolean(id, "isenabled");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param period 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanToggle(Period period,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Period period, String column, Kv kv) {
		//addUpdateSystemLog(period.getIautoid(), JBoltUserKit.getUserId(), period.getName(),"的字段["+column+"]值:"+period.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param period model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkInUse(Period period, Kv kv) {
		//这里用来覆盖 检测Period是否被其它表引用
		return null;
	}
	/**
	 * 费用预算模板下载查询期间
	 * */
	public Record findPeriodByDownTpl(Kv para) {
		return dbTemplate("period.findPeriodByKv",para.set("iorgid",getOrgId())).findFirst();
	}
	/**
	 * 根据启用期间的起止日期计算费用预算新增和修改界面可编辑表格的动态列
	 * */	
	public void calcDynamicExpenseBudgetTableColumn(Date dstarttime, Date dendtime, List<Record> yearColumnTxtList,
			List<String> monthColumnTxtList, List<Record> quantityAndAmountColumnList) {
		int colspan = 0;
        Date dendTimeElse = JBoltDateUtil.toDate(JBoltDateUtil.format(dendtime, "yyyy-MM"),"yyyy-MM");
        while(JBoltDateUtil.toDate(JBoltDateUtil.format(dstarttime, "yyyy-MM"),"yyyy-MM").compareTo(dendTimeElse) != 1){
        	colspan++;
        	int dstarttimemonth = Integer.parseInt(JBoltDateUtil.format(dstarttime, JBoltDateUtil.MM));
        	int dstarttimeyear = Integer.parseInt(JBoltDateUtil.format(dstarttime, "yyyy"));
        	//年度是否结束标识:是否为年度最后一个月,12月或者期间结束日期的月份
        	boolean isYearFinalFlg = dstarttimemonth == 12
            		|| JBoltDateUtil.toDate(JBoltDateUtil.format(dstarttime, "yyyy-MM"),"yyyy-MM").compareTo(dendTimeElse) == 0;
        	//年度金额合计列的data-column属性
        	String totaldatacolumn = "itotal"+JBoltDateUtil.format(dstarttime, "yyyy");
        	if(isYearFinalFlg){
        		Record yearColumnRc = new Record();
        		yearColumnRc.set("colname", JBoltDateUtil.format(dstarttime, "yyyy")+"年");
        		yearColumnRc.set("colspan", colspan*2);
        		yearColumnRc.set("totaldatacolumn", totaldatacolumn);
        		colspan = 0;
        		yearColumnTxtList.add(yearColumnRc);
        	}
        	String monthColumnTxt = dstarttimemonth + "月";
        	monthColumnTxtList.add(monthColumnTxt);
        	Record quantityAndAmountColumn = new Record();
        	quantityAndAmountColumn.set("quantitydatacolumn", "iquantity"+dstarttimeyear+dstarttimemonth);
        	quantityAndAmountColumn.set("amountdatacolumn", "iamount"+dstarttimeyear+dstarttimemonth);
        	if(isYearFinalFlg){
        		quantityAndAmountColumn.set("totaldatacolumn", totaldatacolumn);	
        	}
        	quantityAndAmountColumnList.add(quantityAndAmountColumn);
        	dstarttime = JBoltDateUtil.nextMonthFirstDay(dstarttime);
        }
	}

	public List<Period> findListModelByIds(String iperiodids) {
		return daoTemplate("period.findListModelByIds", Kv.by("iperiodids", iperiodids)).find();
	}

}