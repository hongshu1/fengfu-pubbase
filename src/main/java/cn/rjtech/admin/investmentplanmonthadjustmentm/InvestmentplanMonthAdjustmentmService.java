package cn.rjtech.admin.investmentplanmonthadjustmentm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.investmentplanmonthadjustmentitem.InvestmentplanMonthAdjustmentItemService;
import cn.rjtech.admin.investmentplanmonthadjustmentitemd.InvestmentplanMonthAdjustmentItemdService;
import cn.rjtech.constants.Constants;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.EffectiveStatusEnum;
import cn.rjtech.enums.InvestmentBudgetTypeEnum;
import cn.rjtech.enums.InvestmentEnum;
import cn.rjtech.model.momdata.InvestmentplanMonthAdjustmentItem;
import cn.rjtech.model.momdata.InvestmentplanMonthAdjustmentItemd;
import cn.rjtech.model.momdata.InvestmentplanMonthAdjustmentm;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang.ArrayUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 投资月度实绩管理 Service
 * @ClassName: InvestmentplanMonthAdjustmentmService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-02-02 17:30
 */
public class InvestmentplanMonthAdjustmentmService extends BaseService<InvestmentplanMonthAdjustmentm> {

	private final InvestmentplanMonthAdjustmentm dao = new InvestmentplanMonthAdjustmentm().dao();

	@Override
	protected InvestmentplanMonthAdjustmentm dao() {
		return dao;
	}
	@Inject
	private InvestmentplanMonthAdjustmentItemService investmentplanMonthAdjustmentItemService;
	@Inject
	private InvestmentplanMonthAdjustmentItemdService investmentplanMonthAdjustmentItemdService;
	@Inject
	private DepartmentService departmentService;

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv paras) {
		Page<Record> pageList = dbTemplate("investmentplanmonthadjustmentm.paginateAdminDatas",paras).paginate(pageNumber, pageSize);
		for (Record row : pageList.getList()) {
			row.set("createusername", JBoltUserCache.me.getUserName(row.getLong("icreateby")));
			row.set("effectiveusername", JBoltUserCache.me.getUserName(row.getLong("ieffectiveby")));
		}
		return pageList;
	}

	/**
	 * 保存
	 * @param investmentplanMonthAdjustmentm
	 * @return
	 */
	public Ret save(InvestmentplanMonthAdjustmentm investmentplanMonthAdjustmentm) {
		if(investmentplanMonthAdjustmentm==null || isOk(investmentplanMonthAdjustmentm.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			ValidationUtils.isTrue(investmentplanMonthAdjustmentm.save(), ErrorMsg.SAVE_FAILED);


			// TODO 其他业务代码实现

			return true;
		});

		// 添加日志
		// addSaveSystemLog(investmentplanMonthAdjustmentm.getIautoid(), JBoltUserKit.getUserId(), investmentplanMonthAdjustmentm.getName());
		return SUCCESS;
	}

	/**
	 * 更新
	 */
	public Ret update(InvestmentplanMonthAdjustmentm investmentplanMonthAdjustmentm) {
		if(investmentplanMonthAdjustmentm==null || notOk(investmentplanMonthAdjustmentm.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// 更新时需要判断数据存在
			InvestmentplanMonthAdjustmentm dbInvestmentplanMonthAdjustmentm = findById(investmentplanMonthAdjustmentm.getIautoid());
			ValidationUtils.notNull(dbInvestmentplanMonthAdjustmentm, JBoltMsg.DATA_NOT_EXIST);

			// TODO 其他业务代码实现
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);

			ValidationUtils.isTrue(investmentplanMonthAdjustmentm.update(), ErrorMsg.UPDATE_FAILED);

			return true;
		});

		//添加日志
		//addUpdateSystemLog(investmentplanMonthAdjustmentm.getIautoid(), JBoltUserKit.getUserId(), investmentplanMonthAdjustmentm.getName());
		return SUCCESS;
	}

	/**
	 * 删除 指定多个ID
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
				long iAutoId = Long.parseLong(idStr);
				InvestmentplanMonthAdjustmentm dbInvestmentplanMonthAdjustmentm = findById(iAutoId);
				ValidationUtils.notNull(dbInvestmentplanMonthAdjustmentm, JBoltMsg.DATA_NOT_EXIST);
				ValidationUtils.isTrue(dbInvestmentplanMonthAdjustmentm.getIeffectivestatus() == EffectiveStatusEnum.INVAILD.getValue(), "请选择未生效的数据删除");
				List<InvestmentplanMonthAdjustmentItem> itemList = investmentplanMonthAdjustmentItemService.findByIadjustmentmId(dbInvestmentplanMonthAdjustmentm.getIautoid());
				for (InvestmentplanMonthAdjustmentItem item : itemList) {
					investmentplanMonthAdjustmentItemdService.deleteByItemId(item.getIautoid());
					investmentplanMonthAdjustmentItemService.deleteById(item.getIautoid());
				}
				// TODO 可能需要补充校验组织账套权限
				// TODO 存在关联使用时，校验是否仍在使用
				ValidationUtils.isTrue(dbInvestmentplanMonthAdjustmentm.delete(), JBoltMsg.FAIL);
			}
			return true;
		});

		// 添加日志
		// InvestmentplanMonthAdjustmentm investmentplanMonthAdjustmentm = ret.getAs("data");
		// addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, investmentplanMonthAdjustmentm.getName());
		return SUCCESS;
	}

	/**
	 * 删除数据后执行的回调
	 * @param investmentplanMonthAdjustmentm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(InvestmentplanMonthAdjustmentm investmentplanMonthAdjustmentm, Kv kv) {
		//addDeleteSystemLog(investmentplanMonthAdjustmentm.getIautoid(), JBoltUserKit.getUserId(),investmentplanMonthAdjustmentm.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param investmentplanMonthAdjustmentm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanDelete(InvestmentplanMonthAdjustmentm investmentplanMonthAdjustmentm, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(investmentplanMonthAdjustmentm, kv);
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
	 * 查询投资计划未完成项目: 
	 * */
	public List<Record> findUnfinishInvestmentPlanItemDatas(Kv para) {
		List<Record> list = dbTemplate("investmentplanmonthadjustmentm.findUnfinishInvestmentPlanItemDatas",para).find();
		fillRow(list);
		return list;
	}
	/**
	 * 查询投资月度实绩项目明细
	 * */
	public List<Record> findInvestmentPlanAdjustmentItemDatas(Kv para){
		List<Record> list = dbTemplate("investmentplanmonthadjustmentm.findInvestmentPlanAdjustmentItemDatas",para).find();
		fillRow(list);
		return list;
	}
	private void fillRow(List<Record> list){
		if(CollUtil.isNotEmpty(list)){
			for (Record record : list) {
	            Constants.fillPlanItem(record);
	            record.set("cdepname", departmentService.getCdepName(record.getStr("cdepcode")));
	            record.set("cbudgettypedesc", InvestmentBudgetTypeEnum.toEnum(record.getInt("ibudgettype")).getText());
			}	
		}
	}
	/**
	 * 投资月度实绩新增
	 * */
	public Ret saveTableSubmitByAdd(JBoltTable jBoltTable) {
		Date now = new Date();
		Record formRecord = jBoltTable.getFormRecord();
		ValidationUtils.notNull(formRecord, JBoltMsg.PARAM_ERROR);
		List<Record> list = jBoltTable.getSaveRecordList();
		ValidationUtils.notEmpty(list, "月度实绩项目不能为空!");
		InvestmentplanMonthAdjustmentm investmentplanMonthAdjustmentm = new InvestmentplanMonthAdjustmentm(); 
		Long iAdjustmentmId = JBoltSnowflakeKit.me.nextId();
		investmentplanMonthAdjustmentm.setIautoid(iAdjustmentmId);
		investmentplanMonthAdjustmentm.setDadjustdate(JBoltDateUtil.getDate(formRecord.getStr("dadjustdate"), "YYYY-MM"));
		investmentplanMonthAdjustmentm.setCmemo(formRecord.getStr("cmemo"));
		investmentplanMonthAdjustmentm.setIorgcode(getOrgCode());
		investmentplanMonthAdjustmentm.setIorgid(getOrgId());
		investmentplanMonthAdjustmentm.setIeffectivestatus(EffectiveStatusEnum.INVAILD.getValue());
		investmentplanMonthAdjustmentm.setIcreateby(JBoltUserKit.getUserId());
		investmentplanMonthAdjustmentm.setDcreatetime(now);
		tx(()->{
			investmentplanMonthAdjustmentm.save();
			saveInvestmentPlanItemForSubmitTableDatas(jBoltTable,iAdjustmentmId);
			return true;
		});
		return SUCCESS;
	}
	/**
	 * 投资月度实绩修改
	 * */
	public Ret saveTableByUpdate(JBoltTable jBoltTable) {
		InvestmentplanMonthAdjustmentm investmentplanMonthAdjustmentm = jBoltTable.getFormModel(InvestmentplanMonthAdjustmentm.class,"investmentplanMonthAdjustmentm");
		ValidationUtils.notNull(investmentplanMonthAdjustmentm, JBoltMsg.PARAM_ERROR);
		Long iAdjustmentmId = investmentplanMonthAdjustmentm.getIautoid();
		ValidationUtils.notNull(iAdjustmentmId, JBoltMsg.PARAM_ERROR);
		tx(()->{
			saveInvestmentPlanItemForSubmitTableDatas(jBoltTable, iAdjustmentmId);
			updateInvestmentPlanItemForSubmitTableDatas(jBoltTable);
			deleteInvestmentPlanItemForSubmitTableDatas(jBoltTable);
			return true;
		});
		return SUCCESS;
	}
	private void saveInvestmentPlanItemForSubmitTableDatas(JBoltTable jBoltTable,Long iAdjustmentmId) {
		List<Record> list = jBoltTable.getSaveRecordList();
		List<InvestmentplanMonthAdjustmentItem> itemList = new ArrayList<InvestmentplanMonthAdjustmentItem>();
		List<InvestmentplanMonthAdjustmentItemd> investmentplanMonthAdjustmentItemdList = new ArrayList<InvestmentplanMonthAdjustmentItemd>();
		if(CollUtil.isEmpty(list)) return;
 		for (Record saveRow : list) {
			InvestmentplanMonthAdjustmentItem investmentplanMonthAdjustmentItem = new InvestmentplanMonthAdjustmentItem();
			Long iautoid = JBoltSnowflakeKit.me.nextId();
			Long iPlanId = saveRow.getLong("iplanid");
			investmentplanMonthAdjustmentItem.setIautoid(iautoid);
			investmentplanMonthAdjustmentItem.setIadjustmentmid(iAdjustmentmId);
			investmentplanMonthAdjustmentItem.setIplanid(iPlanId);
			itemList.add(investmentplanMonthAdjustmentItem);
			constructInvestmentPlanItemd(saveRow,iautoid,investmentplanMonthAdjustmentItemdList);
		}
		investmentplanMonthAdjustmentItemService.batchSave(itemList);
		investmentplanMonthAdjustmentItemdService.batchSave(investmentplanMonthAdjustmentItemdList);
	}
	
	private void updateInvestmentPlanItemForSubmitTableDatas(JBoltTable jBoltTable) {
		List<Record> list = jBoltTable.getUpdateRecordList();
		if(CollUtil.isEmpty(list)) return;
		List<InvestmentplanMonthAdjustmentItemd> investmentplanMonthAdjustmentItemdList = new ArrayList<InvestmentplanMonthAdjustmentItemd>();
		for (Record row : list) {
			Long iAjustmentItemId = row.getLong("iautoid");
			constructInvestmentPlanItemd(row,iAjustmentItemId,investmentplanMonthAdjustmentItemdList);
			investmentplanMonthAdjustmentItemdService.deleteByItemId(iAjustmentItemId);
			investmentplanMonthAdjustmentItemdService.batchSave(investmentplanMonthAdjustmentItemdList);
		}
	}
	private void deleteInvestmentPlanItemForSubmitTableDatas(JBoltTable jBoltTable) {
		Object[] ids = jBoltTable.getDelete();
		if(ArrayUtils.isEmpty(ids)) return;
		for (Object id : ids) {
			investmentplanMonthAdjustmentItemdService.deleteByItemId(id);
			investmentplanMonthAdjustmentItemService.deleteById(id);
		}
	}
	/**
	 * 投资月度实绩新增时，构建每行中的投资月度实绩项目明细数据
	 * */
	private void constructInvestmentPlanItemd(Record row,Long itemid,
			List<InvestmentplanMonthAdjustmentItemd> investmentplanMonthAdjustmentItemdList) {
		ValidationUtils.notNull(itemid, "月度实绩项目ID为空");
		//第一期
		String cperiodprogress1 = row.getStr("cperiodprogress1");
		Date dperioddate1 = row.getDate(InvestmentEnum.DPERIODDATE1.getField());
		BigDecimal iamount1 = row.getBigDecimal(InvestmentEnum.IAMOUNT1.getField());
		if(dperioddate1 != null && iamount1 !=null){
			InvestmentplanMonthAdjustmentItemd investmentplanMonthAdjustmentItemd = new InvestmentplanMonthAdjustmentItemd();
			investmentplanMonthAdjustmentItemd.setIautoid(JBoltSnowflakeKit.me.nextId());
			investmentplanMonthAdjustmentItemd.setIadjustmentitemid(itemid);
			investmentplanMonthAdjustmentItemd.setIperiodnum(1);
			investmentplanMonthAdjustmentItemd.setCperiodprogress(cperiodprogress1);
			investmentplanMonthAdjustmentItemd.setDperioddate(dperioddate1);
			investmentplanMonthAdjustmentItemd.setIamount(iamount1);
			investmentplanMonthAdjustmentItemdList.add(investmentplanMonthAdjustmentItemd);
		}
		//第二期
		String cperiodprogress2 = row.getStr("cperiodprogress2");
		Date dperioddate2 = row.getDate(InvestmentEnum.DPERIODDATE2.getField());
		BigDecimal iamount2 = row.getBigDecimal(InvestmentEnum.IAMOUNT2.getField());
		if(dperioddate2 != null && iamount2 !=null){
			InvestmentplanMonthAdjustmentItemd investmentplanMonthAdjustmentItemd = new InvestmentplanMonthAdjustmentItemd();
			investmentplanMonthAdjustmentItemd.setIautoid(JBoltSnowflakeKit.me.nextId());
			investmentplanMonthAdjustmentItemd.setIadjustmentitemid(itemid);
			investmentplanMonthAdjustmentItemd.setIperiodnum(2);
			investmentplanMonthAdjustmentItemd.setCperiodprogress(cperiodprogress2);
			investmentplanMonthAdjustmentItemd.setDperioddate(dperioddate2);
			investmentplanMonthAdjustmentItemd.setIamount(iamount2);
			investmentplanMonthAdjustmentItemdList.add(investmentplanMonthAdjustmentItemd);
		}
		//第三期
		String cperiodprogress3 = row.getStr("cperiodprogress3");
		Date dperioddate3 = row.getDate(InvestmentEnum.DPERIODDATE3.getField());
		BigDecimal iamount3 = row.getBigDecimal(InvestmentEnum.IAMOUNT3.getField());
		if(dperioddate3 != null && iamount3 !=null){
			InvestmentplanMonthAdjustmentItemd investmentplanMonthAdjustmentItemd = new InvestmentplanMonthAdjustmentItemd();
			investmentplanMonthAdjustmentItemd.setIautoid(JBoltSnowflakeKit.me.nextId());
			investmentplanMonthAdjustmentItemd.setIadjustmentitemid(itemid);
			investmentplanMonthAdjustmentItemd.setIperiodnum(3);
			investmentplanMonthAdjustmentItemd.setCperiodprogress(cperiodprogress3);
			investmentplanMonthAdjustmentItemd.setDperioddate(dperioddate3);
			investmentplanMonthAdjustmentItemd.setIamount(iamount3);
			investmentplanMonthAdjustmentItemdList.add(investmentplanMonthAdjustmentItemd);
		}
		//第四期
		String cperiodprogress4 = row.getStr("cperiodprogress4");
		Date dperioddate4 = row.getDate(InvestmentEnum.DPERIODDATE4.getField());
		BigDecimal iamount4 = row.getBigDecimal(InvestmentEnum.IAMOUNT4.getField());
		if(dperioddate4 != null && iamount4 !=null){
			InvestmentplanMonthAdjustmentItemd investmentplanMonthAdjustmentItemd = new InvestmentplanMonthAdjustmentItemd();
			investmentplanMonthAdjustmentItemd.setIautoid(JBoltSnowflakeKit.me.nextId());
			investmentplanMonthAdjustmentItemd.setIadjustmentitemid(itemid);
			investmentplanMonthAdjustmentItemd.setIperiodnum(4);
			investmentplanMonthAdjustmentItemd.setCperiodprogress(cperiodprogress4);
			investmentplanMonthAdjustmentItemd.setDperioddate(dperioddate4);
			investmentplanMonthAdjustmentItemd.setIamount(iamount4);
			investmentplanMonthAdjustmentItemdList.add(investmentplanMonthAdjustmentItemd);
		}
		//第五期
		String cperiodprogress5 = row.getStr("cperiodprogress5");
		Date dperioddate5 = row.getDate(InvestmentEnum.DPERIODDATE5.getField());
		BigDecimal iamount5 = row.getBigDecimal(InvestmentEnum.IAMOUNT5.getField());
		if(dperioddate5 != null && iamount5 !=null){
			InvestmentplanMonthAdjustmentItemd investmentplanMonthAdjustmentItemd = new InvestmentplanMonthAdjustmentItemd();
			investmentplanMonthAdjustmentItemd.setIautoid(JBoltSnowflakeKit.me.nextId());
			investmentplanMonthAdjustmentItemd.setIadjustmentitemid(itemid);
			investmentplanMonthAdjustmentItemd.setIperiodnum(5);
			investmentplanMonthAdjustmentItemd.setCperiodprogress(cperiodprogress5);
			investmentplanMonthAdjustmentItemd.setDperioddate(dperioddate5);
			investmentplanMonthAdjustmentItemd.setIamount(iamount5);
			investmentplanMonthAdjustmentItemdList.add(investmentplanMonthAdjustmentItemd);
		}
		//第六期
		String cperiodprogress6 = row.getStr("cperiodprogress6");
		Date dperioddate6 = row.getDate(InvestmentEnum.DPERIODDATE6.getField());
		BigDecimal iamount6 = row.getBigDecimal(InvestmentEnum.IAMOUNT6.getField());
		if(JBoltStringUtil.isNotBlank(cperiodprogress6) && dperioddate6 != null && iamount6 !=null){
			InvestmentplanMonthAdjustmentItemd investmentplanMonthAdjustmentItemd = new InvestmentplanMonthAdjustmentItemd();
			investmentplanMonthAdjustmentItemd.setIautoid(JBoltSnowflakeKit.me.nextId());
			investmentplanMonthAdjustmentItemd.setIadjustmentitemid(itemid);
			investmentplanMonthAdjustmentItemd.setIperiodnum(6);
			investmentplanMonthAdjustmentItemd.setCperiodprogress(cperiodprogress6);
			investmentplanMonthAdjustmentItemd.setDperioddate(dperioddate6);
			investmentplanMonthAdjustmentItemd.setIamount(iamount6);
			investmentplanMonthAdjustmentItemdList.add(investmentplanMonthAdjustmentItemd);
		}
	}
	/**
	 * 生效
	 * */
	public Ret effect(Long iautoid) {
		tx(()->{
			Date now = new Date();
			InvestmentplanMonthAdjustmentm investmentplanMonthAdjustmentm = findById(iautoid);
			ValidationUtils.isTrue(investmentplanMonthAdjustmentm.getIeffectivestatus() == EffectiveStatusEnum.INVAILD.getValue(), "请选择未生效的实绩数据!");
			investmentplanMonthAdjustmentm.setIeffectivestatus(EffectiveStatusEnum.EFFECTIVED.getValue());
			investmentplanMonthAdjustmentm.setIeffectiveby(JBoltUserKit.getUserId());
			investmentplanMonthAdjustmentm.setDeffectivetime(now);
			investmentplanMonthAdjustmentm.setIupdateby(JBoltUserKit.getUserId());
			investmentplanMonthAdjustmentm.setDupdatetime(now);
			ValidationUtils.isTrue(investmentplanMonthAdjustmentm.update(), ErrorMsg.UPDATE_FAILED);
			return true;
		});
		return SUCCESS;
	}
	/**
	 * 撤销生效
	 * */
	public Ret unEffect(Long iautoid) {
		tx(()->{
			Date now = new Date();
			InvestmentplanMonthAdjustmentm investmentplanMonthAdjustmentm = findById(iautoid);
			ValidationUtils.isTrue(investmentplanMonthAdjustmentm.getIeffectivestatus() == EffectiveStatusEnum.EFFECTIVED.getValue(), "请选择已生效的实绩数据!");
			investmentplanMonthAdjustmentm.setIeffectivestatus(EffectiveStatusEnum.INVAILD.getValue());
			investmentplanMonthAdjustmentm.setIeffectiveby(null);
			investmentplanMonthAdjustmentm.setDeffectivetime(null);
			investmentplanMonthAdjustmentm.setIupdateby(JBoltUserKit.getUserId());
			investmentplanMonthAdjustmentm.setDupdatetime(now);
			ValidationUtils.isTrue(investmentplanMonthAdjustmentm.update(), ErrorMsg.UPDATE_FAILED);
			return true;
		});
		return SUCCESS;
	}
}