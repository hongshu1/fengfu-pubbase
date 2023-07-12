package cn.rjtech.admin.uptimem;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.uptimed.UptimeDService;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.model.momdata.UptimeD;
import cn.rjtech.model.momdata.UptimeM;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 稼动时间管理 Service
 * @ClassName: UptimeMService
 * @author: chentao
 * @date: 2023-06-28 10:30
 */
public class UptimeMService extends BaseService<UptimeM> implements IApprovalService {

	private final UptimeM dao = new UptimeM().dao();

	@Inject
	private UptimeDService uptimeDService;

	@Override
	protected UptimeM dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("uptimem.getUptimeMList",kv).paginate(pageNumber,pageSize);
	}

	public Record getUptimeMInfo(Kv kv) {
		return dbTemplate("uptimem.getUptimeMList",kv).findFirst();
	}
	/**
	 * 保存
	 * @param uptimeM
	 * @return
	 */
	public Ret save(UptimeM uptimeM) {
		if(uptimeM==null || isOk(uptimeM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(uptimeM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=uptimeM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(uptimeM.getIautoid(), JBoltUserKit.getUserId(), uptimeM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param uptimeM
	 * @return
	 */
	public Ret update(UptimeM uptimeM) {
		if(uptimeM==null || notOk(uptimeM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		UptimeM dbUptimeM=findById(uptimeM.getIAutoId());
		if(dbUptimeM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(uptimeM.getName(), uptimeM.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=uptimeM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(uptimeM.getIautoid(), JBoltUserKit.getUserId(), uptimeM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		String[] idsArray = ids.split(",");
		update("UPDATE PL_UptimeM SET isDeleted = 1 WHERE iAutoId IN ("+ ArrayUtil.join(idsArray,",")+")");
		return SUCCESS;
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		update("UPDATE PL_UptimeM SET isDeleted = 1 WHERE iAutoId = ?",id);
		return SUCCESS;
	}

	/**
	 * 删除数据后执行的回调
	 * @param uptimeM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(UptimeM uptimeM, Kv kv) {
		//addDeleteSystemLog(uptimeM.getIautoid(), JBoltUserKit.getUserId(),uptimeM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param uptimeM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(UptimeM uptimeM, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(uptimeM, kv);
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
	 * 切换isdeleted属性
	 */
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "isDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param uptimeM 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(UptimeM uptimeM,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(UptimeM uptimeM, String column, Kv kv) {
		//addUpdateSystemLog(uptimeM.getIautoid(), JBoltUserKit.getUserId(), uptimeM.getName(),"的字段["+column+"]值:"+uptimeM.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param uptimeM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(UptimeM uptimeM, Kv kv) {
		//这里用来覆盖 检测UptimeM是否被其它表引用
		return null;
	}

	public Record getUptimeTplInfo(Kv kv) {
		return dbTemplate("uptimem.getUptimeTplInfo",kv).findFirst();
	}

	public List<Record> getUptimeDList(Kv kv) {
		if (isOk(kv.get("iautoid"))){
			return dbTemplate("uptimem.getUptimeDList2",kv).find();
		}
		if (isOk(kv.get("iworkregionmid")) && isOk(kv.get("iworkshiftmid"))){
			return dbTemplate("uptimem.getUptimeDList",kv).find();
		}
		return new ArrayList<>();
	}

	public Long updateAndSave(JBoltTable jBoltTable) {
		Long mid;
		Date newDate = new Date();
		String updateOrSave = jBoltTable.getParamToStr("updateOrSave");
		UptimeM model = jBoltTable.getFormModel(UptimeM.class, "uptimeM");
		List<UptimeD> updateRecords = jBoltTable.getUpdateModelList(UptimeD.class);

		if (updateOrSave.equals("save")){
			mid = JBoltSnowflakeKit.me.nextId();

			model.setIAutoId(mid);
			model.setIOrgId(getOrgId());
			model.setCOrgCode(getOrgCode());
			model.setCOrgName(getOrgName());
			model.setICreateBy(JBoltUserKit.getUserId());
			model.setCCreateName(JBoltUserKit.getUserName());
			model.setDCreateTime(newDate);
			model.setIUpdateBy(JBoltUserKit.getUserId());
			model.setCUpdateName(JBoltUserKit.getUserName());
			model.setDUpdateTime(newDate);

			List<UptimeD> list = new ArrayList<>();
			JSONObject jsonObject = jBoltTable.getForm();
			Object tableDataArray = jsonObject.get("tableDataArray");
			if (tableDataArray != null && !tableDataArray.equals("[]")  && !tableDataArray.equals("null")){
				list = JSONArray.parseArray(tableDataArray.toString(),UptimeD.class);
				for (UptimeD uptimeD : list){
					uptimeD.setIUptimeMid(mid);
				}
			}

			List<UptimeD> finalList = list;
			tx(() -> {
				model.save();
				uptimeDService.batchSave(finalList, 500);
				return true;
			});
		}else {
			mid = model.getIAutoId();
			model.setIUpdateBy(JBoltUserKit.getUserId());
			model.setCUpdateName(JBoltUserKit.getUserName());
			model.setDUpdateTime(newDate);

			tx(() -> {
				model.update();
				if (CollUtil.isNotEmpty(updateRecords)) {
					uptimeDService.batchUpdate(updateRecords, 500);
				}
				return true;
			});
		}
		return mid;
	}


	//---------------------app----------------------
	public Record getUptimeTplInfoList(Kv kv) {
		Record record = dbTemplate("uptimem.getUptimeTplInfo",kv).findFirst() != null ? dbTemplate("uptimem.getUptimeTplInfo",kv).findFirst() : new Record();
		List<Record> recordList = dbTemplate("uptimem.getUptimeDList",kv).find();
		record.set("dataList",recordList);
		return record;
	}

	public Record getUptimeMInfoList(Kv kv) {
		Record record = dbTemplate("uptimem.getUptimeMList",kv).findFirst();
		List<Record> recordList = dbTemplate("uptimem.getUptimeDList2",kv).find();
		record.set("dataList",recordList);
		return record;
	}

	public Long updateAndSaveApi(Integer updateOrSaveType,String dataStr,String dataListStr) {
		Long mid;
		Date newDate = new Date();
		UptimeM model = JSONObject.parseObject(dataStr,UptimeM.class);
		List<UptimeD> updateRecords = new ArrayList<>();
		if (dataListStr != null && !dataListStr.equals("[]")  && !dataListStr.equals("null")){
			updateRecords = JSONArray.parseArray(dataListStr,UptimeD.class);
		}

		//新增
		if (updateOrSaveType == 1){
			mid = JBoltSnowflakeKit.me.nextId();

			model.setIAutoId(mid);
			model.setIOrgId(getOrgId());
			model.setCOrgCode(getOrgCode());
			model.setCOrgName(getOrgName());
			model.setICreateBy(JBoltUserKit.getUserId());
			model.setCCreateName(JBoltUserKit.getUserName());
			model.setDCreateTime(newDate);
			model.setIUpdateBy(JBoltUserKit.getUserId());
			model.setCUpdateName(JBoltUserKit.getUserName());
			model.setDUpdateTime(newDate);

			for (UptimeD uptimeD : updateRecords){
				uptimeD.setIUptimeMid(mid);
			}
			List<UptimeD> finalUpdateRecords1 = updateRecords;
			tx(() -> {
				model.save();
				uptimeDService.batchSave(finalUpdateRecords1, 500);
				return true;
			});
		}else {
			mid = model.getIAutoId();
			model.setIUpdateBy(JBoltUserKit.getUserId());
			model.setCUpdateName(JBoltUserKit.getUserName());
			model.setDUpdateTime(newDate);

			List<UptimeD> finalUpdateRecords = updateRecords;
			tx(() -> {
				model.update();
				uptimeDService.batchUpdate(finalUpdateRecords, 500);
				return true;
			});
		}
		return mid;
	}

	public Ret revocationUptimeMById(Long id) {
		update("UPDATE PL_UptimeM SET iAuditStatus = 0 WHERE iAutoId = ?",id);
		return SUCCESS;
	}









	@Override
	public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
		UptimeM formUploadM = findById(formAutoId);
		// 审核状态修改
		formUploadM.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
		formUploadM.setIUpdateBy(JBoltUserKit.getUserId());
		formUploadM.setCUpdateName(JBoltUserKit.getUserName());
		formUploadM.setDUpdateTime(new Date());
		formUploadM.setIAuditBy(JBoltUserKit.getUserId());
		formUploadM.setCAuditName(JBoltUserKit.getUserName());
		formUploadM.setDSubmitTime(new Date());
		formUploadM.update();
		return null;
	}

	@Override
	public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
		UptimeM formUploadM = findById(formAutoId);
		// 审核状态修改
		formUploadM.setIAuditStatus(AuditStatusEnum.REJECTED.getValue());
		formUploadM.setIUpdateBy(JBoltUserKit.getUserId());
		formUploadM.setCUpdateName(JBoltUserKit.getUserName());
		formUploadM.setDUpdateTime(new Date());
		formUploadM.setIAuditBy(JBoltUserKit.getUserId());
		formUploadM.setCAuditName(JBoltUserKit.getUserName());
		formUploadM.setDSubmitTime(new Date());
		formUploadM.update();
		return null;
	}

	@Override
	public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
		return null;
	}

	@Override
	public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
		return null;
	}

	@Override
	public String preSubmitFunc(long formAutoId) {
		UptimeM formuploadm = findById(formAutoId);
		switch (AuditStatusEnum.toEnum(formuploadm.getIAuditStatus())) {
			// 已保存
			case NOT_AUDIT:
				// 不通过
			case REJECTED:

				break;
			default:
				return "订单非已保存状态";
		}
		return null;
	}

	@Override
	public String postSubmitFunc(long formAutoId) {
		return null;
	}

	@Override
	public String postWithdrawFunc(long formAutoId) {
		return null;
	}

	@Override
	public String withdrawFromAuditting(long formAutoId) {
		ValidationUtils.isTrue(updateColumn(formAutoId, "iAuditStatus", AuditStatusEnum.NOT_AUDIT.getValue()).isOk(), "撤回失败");
		return null;
	}

	@Override
	public String preWithdrawFromAuditted(long formAutoId) {
		return null;
	}

	@Override
	public String postWithdrawFromAuditted(long formAutoId) {
		return null;
	}

	@Override
	public String postBatchApprove(List<Long> formAutoIds) {
		for (Long formAutoId : formAutoIds) {
			UptimeM formUploadM = findById(formAutoId);
			// 审核状态修改
			formUploadM.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
			formUploadM.setIUpdateBy(JBoltUserKit.getUserId());
			formUploadM.setCUpdateName(JBoltUserKit.getUserName());
			formUploadM.setDUpdateTime(new Date());
			formUploadM.setIAuditBy(JBoltUserKit.getUserId());
			formUploadM.setCAuditName(JBoltUserKit.getUserName());
			formUploadM.setDSubmitTime(new Date());
			formUploadM.update();
		}
		return null;
	}

	@Override
	public String postBatchReject(List<Long> formAutoIds) {
		for (Long formAutoId : formAutoIds) {
			UptimeM formUploadM = findById(formAutoId);
			// 审核状态修改
			formUploadM.setIAuditStatus(AuditStatusEnum.REJECTED.getValue());
			formUploadM.setIUpdateBy(JBoltUserKit.getUserId());
			formUploadM.setCUpdateName(JBoltUserKit.getUserName());
			formUploadM.setDUpdateTime(new Date());
			formUploadM.setIAuditBy(JBoltUserKit.getUserId());
			formUploadM.setCAuditName(JBoltUserKit.getUserName());
			formUploadM.setDSubmitTime(new Date());
			formUploadM.update();
		}
		return null;
	}

	@Override
	public String postBatchBackout(List<Long> formAutoIds) {
		return null;
	}

}