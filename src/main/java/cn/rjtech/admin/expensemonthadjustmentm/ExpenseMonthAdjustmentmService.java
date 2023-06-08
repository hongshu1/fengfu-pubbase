package cn.rjtech.admin.expensemonthadjustmentm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.expensemonthadjustmentd.ExpenseMonthAdjustmentdService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.EffectiveStatusEnum;
import cn.rjtech.model.momdata.ExpenseMonthAdjustmentd;
import cn.rjtech.model.momdata.ExpenseMonthAdjustmentm;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang.ArrayUtils;

import java.util.Date;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 费用月度实绩调整 Service
 * @ClassName: ExpenseMonthAdjustmentmService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-12-09 14:58
 */
public class ExpenseMonthAdjustmentmService extends BaseService<ExpenseMonthAdjustmentm> {

	@Inject
	private ExpenseMonthAdjustmentdService expenseMonthAdjustmentdService;
	@Inject
	private DepartmentService departmentService;
	
	private final ExpenseMonthAdjustmentm dao = new ExpenseMonthAdjustmentm().dao();

	@Override
	protected ExpenseMonthAdjustmentm dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
		Page<Record> pageList = dbTemplate("expensemonthadjustmentm.paginateAdminDatas",para).paginate(pageNumber, pageSize);
		for (Record row : pageList.getList()) {
			row.set("cusername", JBoltUserCache.me.getUserName(row.getLong("icreateby")));
			row.set("ceffectiveusername", row.getLong("ieffectiveby") == null ?"":JBoltUserCache.me.getUserName(row.getLong("ieffectiveby")));
		}
		return pageList;
	}

	/**
	 * 保存
	 * @param expenseMonthAdjustmentm
	 * @return
	 */
	public Ret save(ExpenseMonthAdjustmentm expenseMonthAdjustmentm) {
		if(expenseMonthAdjustmentm==null || isOk(expenseMonthAdjustmentm.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			ValidationUtils.isTrue(expenseMonthAdjustmentm.save(), ErrorMsg.SAVE_FAILED);


			// TODO 其他业务代码实现

			return true;
		});

		// 添加日志
		// addSaveSystemLog(expenseMonthAdjustmentm.getIautoid(), JBoltUserKit.getUserId(), expenseMonthAdjustmentm.getName());
		return SUCCESS;
	}

	/**
	 * 更新
	 */
	public Ret update(ExpenseMonthAdjustmentm expenseMonthAdjustmentm) {
		if(expenseMonthAdjustmentm==null || notOk(expenseMonthAdjustmentm.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// 更新时需要判断数据存在
			ExpenseMonthAdjustmentm dbExpenseMonthAdjustmentm = findById(expenseMonthAdjustmentm.getIautoid());
			ValidationUtils.notNull(dbExpenseMonthAdjustmentm, JBoltMsg.DATA_NOT_EXIST);

			// TODO 其他业务代码实现
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);

			ValidationUtils.isTrue(expenseMonthAdjustmentm.update(), ErrorMsg.UPDATE_FAILED);

			return true;
		});

		//添加日志
		//addUpdateSystemLog(expenseMonthAdjustmentm.getIautoid(), JBoltUserKit.getUserId(), expenseMonthAdjustmentm.getName());
		return SUCCESS;
	}

	/**
	 * 删除 指定多个ID
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
				long iAutoId = Long.parseLong(idStr);
				ExpenseMonthAdjustmentm dbExpenseMonthAdjustmentm = findById(iAutoId);
				ValidationUtils.notNull(dbExpenseMonthAdjustmentm, JBoltMsg.DATA_NOT_EXIST);
				
				List<ExpenseMonthAdjustmentd> dList = expenseMonthAdjustmentdService.findbyMainId(dbExpenseMonthAdjustmentm.getIautoid());
				for (ExpenseMonthAdjustmentd expenseMonthAdjustmentd : dList) {
					ValidationUtils.isTrue(expenseMonthAdjustmentd.delete(), ErrorMsg.DELETE_FAILED);
				}
				// TODO 可能需要补充校验组织账套权限
				// TODO 存在关联使用时，校验是否仍在使用
				ValidationUtils.isTrue(dbExpenseMonthAdjustmentm.delete(), ErrorMsg.DELETE_FAILED);
			}
			return true;
		});

		// 添加日志
		// ExpenseMonthAdjustmentm expenseMonthAdjustmentm = ret.getAs("data");
		// addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, expenseMonthAdjustmentm.getName());
		return SUCCESS;
	}

	/**
	 * 删除数据后执行的回调
	 * @param expenseMonthAdjustmentm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(ExpenseMonthAdjustmentm expenseMonthAdjustmentm, Kv kv) {
		//addDeleteSystemLog(expenseMonthAdjustmentm.getIautoid(), JBoltUserKit.getUserId(),expenseMonthAdjustmentm.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param expenseMonthAdjustmentm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanDelete(ExpenseMonthAdjustmentm expenseMonthAdjustmentm, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(expenseMonthAdjustmentm, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}
	/**
	 * 新增表格提交
	 * */
	public Ret saveTableByAdd(JBoltTable jBoltTable) {
		ExpenseMonthAdjustmentm expenseMonthAdjustmentm = jBoltTable.getFormModel(ExpenseMonthAdjustmentm.class,"expenseMonthAdjustmentm");
		ValidationUtils.notNull(expenseMonthAdjustmentm, "表单参数为空!");
		Date now = new Date();
		expenseMonthAdjustmentm.setIorgid(getOrgId());
		expenseMonthAdjustmentm.setIorgcode(getOrgCode());
		expenseMonthAdjustmentm.setIeffectivestatus(EffectiveStatusEnum.INVAILD.getValue());
		expenseMonthAdjustmentm.setIcreateby(JBoltUserKit.getUserId());
		expenseMonthAdjustmentm.setDcreatetime(now);
		tx(()->{
			ValidationUtils.isTrue(expenseMonthAdjustmentm.save(), ErrorMsg.SAVE_FAILED);
			List<ExpenseMonthAdjustmentd> saveList = jBoltTable.getSaveModelList(ExpenseMonthAdjustmentd.class);
			for (ExpenseMonthAdjustmentd expenseMonthAdjustmentd : saveList) {
				expenseMonthAdjustmentd.setIadjustmentmid(expenseMonthAdjustmentm.getIautoid());
			}
			expenseMonthAdjustmentdService.batchSave(saveList);
			return true;
		});
		return successWithData(expenseMonthAdjustmentm.keep("iautoid"));
	}
	/**
	 * 修改界面查询明细表数据
	 * */
	public List<Record> findExpensemonthadjustmentdDatas(Long iAdjustmentmId) {
		List<Record> list = dbTemplate("expensemonthadjustmentm.findExpensemonthadjustmentdDatas",Kv.by("iadjustmentmid", iAdjustmentmId)).find();
		for (Record record : list) {
			record.set("cdepname", departmentService.getCdepName(record.getStr("cdepcode")));
		}
		return list;
	}
	
	/**
	 * 编辑表格提交
	 * */
	public Ret saveTableByUpdate(JBoltTable jBoltTable) {
		ExpenseMonthAdjustmentm expenseMonthAdjustmentm = jBoltTable.getFormModel(ExpenseMonthAdjustmentm.class,"expenseMonthAdjustmentm");
		ValidationUtils.notNull(expenseMonthAdjustmentm, "表单参数为空!");
		expenseMonthAdjustmentm.setIorgid(getOrgId());
		expenseMonthAdjustmentm.setIorgcode(getOrgCode());
		expenseMonthAdjustmentm.setIupdateby(JBoltUserKit.getUserId());
		expenseMonthAdjustmentm.setDupdatetime(new Date());
		tx(()->{
			ValidationUtils.isTrue(expenseMonthAdjustmentm.update(), ErrorMsg.UPDATE_FAILED);
			List<ExpenseMonthAdjustmentd> saveList = jBoltTable.getSaveModelList(ExpenseMonthAdjustmentd.class);
			if(CollUtil.isNotEmpty(saveList)){
				for (ExpenseMonthAdjustmentd expenseMonthAdjustmentd : saveList) {
					expenseMonthAdjustmentd.setIadjustmentmid(expenseMonthAdjustmentm.getIautoid());
				}
				expenseMonthAdjustmentdService.batchSave(saveList);
			}
			List<ExpenseMonthAdjustmentd> updateList = jBoltTable.getUpdateModelList(ExpenseMonthAdjustmentd.class);
			if(CollUtil.isNotEmpty(updateList)){
				expenseMonthAdjustmentdService.batchUpdate(updateList);
			}
			Object[] ids = jBoltTable.getDelete();
			if(ArrayUtils.isNotEmpty(ids)){
				expenseMonthAdjustmentdService.deleteByIds(ids);
			}
			return true;
		});
		return successWithData(expenseMonthAdjustmentm.keep("iautoid"));
	}
	/**
	 * 生效
	 * */
	public Ret effect(Long iautoid) {
		ExpenseMonthAdjustmentm expenseMonthAdjustmentm = findById(iautoid);
		ValidationUtils.isTrue(expenseMonthAdjustmentm.getIeffectivestatus() == EffectiveStatusEnum.INVAILD.getValue(), "请选择未生效数据进行操作!");
		tx(()->{
			Long userid = JBoltUserKit.getUserId();
			Date now = new Date();
			expenseMonthAdjustmentm.setIeffectivestatus(EffectiveStatusEnum.EFFECTIVED.getValue());
			expenseMonthAdjustmentm.setIeffectiveby(JBoltUserKit.getUserId());
			expenseMonthAdjustmentm.setDeffectivetime(new Date());
			expenseMonthAdjustmentm.setIupdateby(userid);
			expenseMonthAdjustmentm.setDupdatetime(now);
			ValidationUtils.isTrue(expenseMonthAdjustmentm.update(), ErrorMsg.UPDATE_FAILED);
			return true;
		});
		return SUCCESS;
	}
	/**
	 * 撤销生效
	 * */
	public Ret unEffect(Long iautoid) {
		ExpenseMonthAdjustmentm expenseMonthAdjustmentm = findById(iautoid);
		ValidationUtils.isTrue(expenseMonthAdjustmentm.getIeffectivestatus() == EffectiveStatusEnum.EFFECTIVED.getValue(), "请选择已生效数据进行操作!");
		tx(()->{
			Long userid = JBoltUserKit.getUserId();
			Date now = new Date();
			expenseMonthAdjustmentm.setIeffectivestatus(EffectiveStatusEnum.INVAILD.getValue());
			expenseMonthAdjustmentm.setIeffectiveby(null);
			expenseMonthAdjustmentm.setDeffectivetime(null);
			expenseMonthAdjustmentm.setIupdateby(userid);
			expenseMonthAdjustmentm.setDupdatetime(now);
			ValidationUtils.isTrue(expenseMonthAdjustmentm.update(), ErrorMsg.UPDATE_FAILED);
			return true;
		});
		return SUCCESS;
	}
}