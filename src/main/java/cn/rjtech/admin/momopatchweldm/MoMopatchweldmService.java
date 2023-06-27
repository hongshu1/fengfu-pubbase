package cn.rjtech.admin.momopatchweldm;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.model.momdata.MoMopatchweldm;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.*;

/**
 * 补焊纪录管理 Service
 * @ClassName: MoMopatchweldmService
 * @author: chentao
 * @date: 2023-06-27 10:16
 */
public class MoMopatchweldmService extends BaseService<MoMopatchweldm> implements IApprovalService {

	private final MoMopatchweldm dao = new MoMopatchweldm().dao();

	@Override
	protected MoMopatchweldm dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("momopatchweldm.getMoPatchWeldMList",kv).paginate(pageNumber,pageSize);
		//return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}
	public Record getMoMopatchweldmInfo(Kv kv) {
		return dbTemplate("momopatchweldm.getMoPatchWeldMList",kv).findFirst();
	}

	public List<Record> getMoMopatchwelddList(Long mid){
		List<Record> recordList = dbTemplate("momopatchweldm.getMoMopatchwelddList",Kv.by("mid",mid)).find();
		Map<Long,Record> map = new LinkedHashMap<>();
		for (Record record : recordList){
			Long iMoRoutingConfigId = record.getLong("iMoRoutingConfigId");
			String cEquipmentName = record.get("cEquipmentName") != null ? record.get("cEquipmentName") : " ";
			if (map.containsKey(iMoRoutingConfigId)){
				Record record1 = map.get(iMoRoutingConfigId);
				String name = record1.get("cEquipmentName") != null ? record1.get("cEquipmentName") : " ";
				record1.set("cEquipmentName",name.concat("/").concat(cEquipmentName));
				map.put(iMoRoutingConfigId,record1);
			}else {
				map.put(iMoRoutingConfigId,record);
			}
		}
		List<Record> list = new ArrayList<>(map.values());
		return list;
	}

	/**
	 * 保存
	 * @param moMopatchweldm
	 * @return
	 */
	public Ret save(MoMopatchweldm moMopatchweldm) {
		if(moMopatchweldm==null || isOk(moMopatchweldm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(moMopatchweldm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMopatchweldm.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moMopatchweldm.getIautoid(), JBoltUserKit.getUserId(), moMopatchweldm.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param moMopatchweldm
	 * @return
	 */
	public Ret update(MoMopatchweldm moMopatchweldm) {
		if(moMopatchweldm==null || notOk(moMopatchweldm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoMopatchweldm dbMoMopatchweldm=findById(moMopatchweldm.getIAutoId());
		if(dbMoMopatchweldm==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(moMopatchweldm.getName(), moMopatchweldm.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMopatchweldm.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(moMopatchweldm.getIautoid(), JBoltUserKit.getUserId(), moMopatchweldm.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		return deleteByIds(ids,true);
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		return deleteById(id,true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param moMopatchweldm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoMopatchweldm moMopatchweldm, Kv kv) {
		//addDeleteSystemLog(moMopatchweldm.getIautoid(), JBoltUserKit.getUserId(),moMopatchweldm.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMopatchweldm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoMopatchweldm moMopatchweldm, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMopatchweldm, kv);
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
	 * @param moMopatchweldm 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(MoMopatchweldm moMopatchweldm,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(MoMopatchweldm moMopatchweldm, String column, Kv kv) {
		//addUpdateSystemLog(moMopatchweldm.getIautoid(), JBoltUserKit.getUserId(), moMopatchweldm.getName(),"的字段["+column+"]值:"+moMopatchweldm.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMopatchweldm model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(MoMopatchweldm moMopatchweldm, Kv kv) {
		//这里用来覆盖 检测MoMopatchweldm是否被其它表引用
		return null;
	}

	@Override
	public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
		MoMopatchweldm formUploadM = findById(formAutoId);
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
		MoMopatchweldm formUploadM = findById(formAutoId);
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
		MoMopatchweldm formuploadm = findById(formAutoId);
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
			MoMopatchweldm formUploadM = findById(formAutoId);
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
			MoMopatchweldm formUploadM = findById(formAutoId);
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