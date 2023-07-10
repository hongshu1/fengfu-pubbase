package cn.rjtech.admin.otherout;

import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.admin.otheroutdetail.OtherOutDetailService;
import cn.rjtech.enums.WeekOrderStatusEnum;
import cn.rjtech.model.momdata.MonthOrderM;
import cn.rjtech.model.momdata.OtherOut;
import cn.rjtech.model.momdata.OtherOutDetail;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 出库管理-特殊领料单列表 Service
 * @ClassName: OtherOutService
 * @author: RJ
 * @date: 2023-05-06 15:05
 */
public class OtherOutService extends BaseService<OtherOut> implements IApprovalService {

	private final OtherOut dao = new OtherOut().dao();

	@Override
	protected OtherOut dao() {
		return dao;
	}


	@Inject
	private OtherOutDetailService otherOutDetailService;
	@Inject
	private FormApprovalService formApprovalService;


	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("otherout.paginateAdminDatas",kv).paginate(pageNumber, pageSize);
	}


	/**
	 * 特殊领料单列表 明细
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> getOtherOutLines(int pageNumber, int pageSize, Kv kv){
		return dbTemplate("otherout.getOtherOutLines",kv).paginate(pageNumber, pageSize);

	}

	/**
	 * 保存
	 * @param otherOut
	 * @return
	 */
	public Ret save(OtherOut otherOut) {
		if(otherOut==null || isOk(otherOut.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(otherOut.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=otherOut.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(otherOut.getAutoid(), JBoltUserKit.getUserId(), otherOut.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param otherOut
	 * @return
	 */
	public Ret update(OtherOut otherOut) {
		if(otherOut==null || notOk(otherOut.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		OtherOut dbOtherOut=findById(otherOut.getAutoID());
		if(dbOtherOut==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(otherOut.getName(), otherOut.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=otherOut.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(otherOut.getAutoid(), JBoltUserKit.getUserId(), otherOut.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
				String autoId = idStr;
				OtherOut otherOut = findById(autoId);
				ValidationUtils.notNull(otherOut, JBoltMsg.DATA_NOT_EXIST);

				// TODO 可能需要补充校验组织账套权限
				// TODO 存在关联使用时，校验是否仍在使用

				//删除行数据
				otherOutDetailService.deleteByBatchIds(autoId);
				ValidationUtils.isTrue(otherOut.delete(), JBoltMsg.FAIL);

			}
			return true;
		});
		return SUCCESS;
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
	 * @param otherOut 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(OtherOut otherOut, Kv kv) {
		//addDeleteSystemLog(otherOut.getAutoid(), JBoltUserKit.getUserId(),otherOut.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param otherOut 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(OtherOut otherOut, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(otherOut, kv);
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
	 * 获取存货档案列表
	 * 通过关键字匹配
	 * autocomplete组件使用
	 */
	public List<Record> getAutocompleteData(String q, Integer limit) {
		return dbTemplate("otherout.getAutocompleteData",Kv.by("q", q).set("limit",limit)).find();
	}
	/**
	 * 获取条码列表
	 * 通过关键字匹配
	 * autocomplete组件使用
	 */
	public List<Record> getBarcodeDatas(Kv kv) {
		return dbTemplate("otherout.getBarcodeDatas",kv).find();
	}

	public Ret submitByJBoltTables(JBoltTableMulti jboltTableMulti) {
		if (jboltTableMulti == null || jboltTableMulti.isEmpty()) {
			return fail(JBoltMsg.JBOLTTABLE_IS_BLANK);
		}

		// 这里可以循环遍历 保存处理每个表格 也可以 按照name自己get出来单独处理

		JBoltTable jBoltTable = jboltTableMulti.getJBoltTable("table2");
		//  jboltTableMulti.entrySet().forEach(en->{
		//  BoltTable jBoltTable = en.getValue();
		// 获取额外传递参数 比如订单ID等信息 在下面数据里可能用到
		if (jBoltTable.paramsIsNotBlank()) {
			System.out.println(jBoltTable.getParams().toJSONString());
		}

		//	当前操作人员  当前时间 单号
		Long userId = JBoltUserKit.getUserId();
		String userName = JBoltUserKit.getUserName();
		Date nowDate = new Date();
		String OrgCode =getOrgCode();


		System.out.println("saveTable===>" + jBoltTable.getSave());
		System.out.println("updateTable===>" + jBoltTable.getUpdate());
		System.out.println("deleteTable===>" + jBoltTable.getDelete());
		System.out.println("form===>" + jBoltTable.getForm());

		//  检查填写是否有误
		List<Record> saveRecordList = jBoltTable.getSaveRecordList();
		List<Record> updateRecordList = jBoltTable.getUpdateRecordList();
		List<Record> recordList = new ArrayList<>();

		if (jBoltTable.saveIsNotBlank()) {
			recordList.addAll(saveRecordList);
		}


		if (jBoltTable.updateIsNotBlank()) {
			recordList.addAll(updateRecordList);
		}

		final String[] AutoIDs = {null};
		tx(()->{
			String headerId = null;
			// 获取Form对应的数据
			if (jBoltTable.formIsNotBlank()) {
				OtherOut otherOut = jBoltTable.getFormModel(OtherOut.class,"otherOut");


				//	行数据为空 不保存
				if (otherOut.getAutoID() == null && !jBoltTable.saveIsNotBlank() && !jBoltTable.updateIsNotBlank() && !jBoltTable.deleteIsNotBlank()) {
					ValidationUtils.error( "请先添加行数据！");
				}



				if (otherOut.getAutoID() == null) {
					//保存
//					//审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
					otherOut.setIAuditStatus(0);
					otherOut.setICreateBy(userId);
					otherOut.setDCreateTime(nowDate);
					otherOut.setCCreateName(userName);
					otherOut.setOrganizeCode(OrgCode);
					otherOut.setIUpdateBy(userId);
					otherOut.setDupdateTime(nowDate);
					otherOut.setCUpdateName(userName);
					otherOut.setType("OtherOutMES");
					otherOut.setIsDeleted(false);
					save(otherOut);
					headerId = otherOut.getAutoID();
				} else {
					otherOut.setIUpdateBy(userId);
					otherOut.setDupdateTime(nowDate);
					otherOut.setCUpdateName(userName);
					update(otherOut);
					headerId = otherOut.getAutoID();
				}
				AutoIDs[0] = otherOut.getAutoID();
			}

			// 获取待保存数据 执行保存
			if (jBoltTable.saveIsNotBlank()) {
				List<OtherOutDetail> lines = jBoltTable.getSaveModelList(OtherOutDetail.class);

				String finalHeaderId = headerId;
				lines.forEach(otherOutDetail -> {
					otherOutDetail.setMasID(finalHeaderId);
					//创建人
					otherOutDetail.setIcreateby(userId);
					otherOutDetail.setDcreatetime(nowDate);
					otherOutDetail.setCcreatename(userName);
					//更新人
					otherOutDetail.setIupdateby(userId);
					otherOutDetail.setDupdatetime(nowDate);
					otherOutDetail.setCupdatename(userName);
				});
				otherOutDetailService.batchSave(lines);
			}
			// 获取待更新数据 执行更新
			if (jBoltTable.updateIsNotBlank()) {
				List<OtherOutDetail> lines = jBoltTable.getUpdateModelList(OtherOutDetail.class);

				lines.forEach(otherOutDetail -> {
					//更新人
					otherOutDetail.setIupdateby(userId);
					otherOutDetail.setDupdatetime(nowDate);
					otherOutDetail.setCupdatename(userName);
				});
				otherOutDetailService.batchUpdate(lines);
			}
			// 获取待删除数据 执行删除
			if (jBoltTable.deleteIsNotBlank()) {
				otherOutDetailService.deleteByIds(jBoltTable.getDelete());
			}

			return true;
		});
		return SUCCESS.set("AutoID", AutoIDs[0]);
	}

	/**
	 * 详情页提审
	 */

	public Ret submit(Long iautoid) {
		tx(() -> {

			// 根据审批状态
			Ret ret = formApprovalService.submit(table(), iautoid, primaryKey(),"cn.rjtech.admin.otherout.OtherOutService");
			ValidationUtils.isTrue(ret.isOk(), ret.getStr("msg"));

			// 处理其他业务
//			OtherOut otherOut = findById(iautoid);
//			otherOut.setIAuditStatus(1);
//			ValidationUtils.isTrue(otherOut.update(),JBoltMsg.FAIL);
			return true;
		});

		return SUCCESS;
	}

	/**
	 * 审批
	 * @param iAutoId
	 * @return
	 */
	public Ret approve(String iAutoId,Integer mark) {
		boolean success = false;
		if (mark == 1) {
			OtherOut otherOut = findById(iAutoId);
			if (otherOut == null ||notOk(otherOut.getAutoID())) {
				return fail(JBoltMsg.PARAM_ERROR);
			}
			//订单状态：2. 待审批
			otherOut.setStatus(2);
			//审核状态： 1. 待审核
			otherOut.setAuditStatus(1);
			success = otherOut.update();
			if (success) {
				//添加日志
				addUpdateSystemLog(otherOut.getAutoID(), JBoltUserKit.getUserId(), otherOut.getAutoID().toString());
			}
		} else {
			List<OtherOut> listByIds = getListByIds(iAutoId);
			//TODO 由于审批流程暂未开发 先审批提审即通过
			if (listByIds.size() > 0) {
				for (OtherOut otherOut : listByIds) {
					if (otherOut.getStatus() != 2) {
						return warn("订单："+otherOut.getBillNo()+"状态不支持审批操作！");
					}
					//订单状态：3. 已审批
					otherOut.setStatus(3);
					//审核状态：2. 审核通过
					otherOut.setAuditStatus(2);
					success= otherOut.update();
//					if(success) {
//						//添加日志
//						addUpdateSystemLog(otherOut.getAutoID(), JBoltUserKit.getUserId(), otherOut.getAutoID().toString());
//					}
				}
			}
		}

//		List<WeekOrderD> weekOrderDS = weekOrderDService.findByMId(iAutoId);
//		if (weekOrderDS.size() > 0) cusOrderSumService.handelWeekOrder(weekOrderDS);

//		return ret(success);
		return SUCCESS;
	}


	/**
	 * 批量反审批
	 * @param ids
	 * @return
	 */
	public Ret NoApprove(String ids) {
		//TODO数据同步暂未开发 现只修改状态
		for (OtherOut otherOut :  getListByIds(ids)) {
			//订单状态： 3. 已审批
			if (otherOut.getStatus() != 3) {
				return warn("订单："+otherOut.getBillNo()+"状态不支持反审批操作！");
			}
			//审核状态：1. 待审核
			otherOut.setAuditStatus(1);
			//订单状态： 2. 待审批
			otherOut.setStatus(2);
			otherOut.update();
		}
		return SUCCESS;
	}

	/**
	 * 关闭功能
	 * @param iAutoId
	 * @return
	 */
	public Ret closeWeekOrder(String iAutoId) {
		if( notOk(iAutoId)) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		OtherOut otherOut = findById(iAutoId);
		//订单状态：7. 已关闭
		otherOut.setStatus(7);
		boolean result = otherOut.update();
		return ret(result);

	}

	/**
	 * 撤回
	 * @param iAutoId
	 * @return
	 */
	public Ret recall(String iAutoId) {
		if( notOk(iAutoId)) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		OtherOut otherOut = findById(iAutoId);
//		//订单状态：2. 待审批
//		otherOut.setStatus(1);
		//审核状态： 1. 待审批
		otherOut.setIAuditStatus(0);
		boolean result = otherOut.update();
		return ret(result);
	}


	/**
	 * 获取现品票
	 * @param  kv
	 * @return
	 */
	public List<Record> gettable2(Kv kv) {
		List<Record> Table2InvCode = dbTemplate("otheroutreturnlist.getTable2InvCode", kv).find();

		final String[] barCode = {null};
		List<String> InvCodesList = new ArrayList<>();
		for (Record InvCode :Table2InvCode){
			String itemCode = InvCode.getStr("InvCode");
			if (!InvCodesList.contains(itemCode)){
				InvCodesList.add(itemCode);
			}
		}

		if (InvCodesList.size()>0){
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < InvCodesList.size(); i++) {

				buffer.append("\'"+InvCodesList.get(i)+"\',");
			}
			String invCodes = buffer.substring(0, buffer.length() - 1);
			kv.set("invcode",invCodes);
		}

		List<Record> TableBarcodeData = this.TableBarcodeData(kv);
		if (TableBarcodeData.size() == 0){
			ValidationUtils.error( "请输入正确的现品票！！！");
		}
		return TableBarcodeData;
	}

	/**
	 * 获取条码列表
	 * 通过关键字匹配
	 * autocomplete组件使用
	 */
	public List<Record> TableBarcodeData(Kv kv) {
		return dbTemplate("otherout.TableBarcodeData",kv).find();
	}


	/**
	 * 处理审批通过的其他业务操作，如有异常返回错误信息
	 *
	 * @param formAutoId 单据ID
	 * @return 错误信息
	 */
	@Override
	public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
		return null;
	}

	@Override
	public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
		return null;
	}

	@Override
	public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
		return null;
	}

	@Override
	public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
		OtherOut otherOut = findById(formAutoId);
		otherOut.setIAuditBy(null);
		otherOut.setCAuditName(null);
		otherOut.setDAuditTime(null);
		otherOut.update();
		return null;
	}

	@Override
	public String preSubmitFunc(long formAutoId) {
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
		return null;
	}

	@Override
	public String postBatchReject(List<Long> formAutoIds) {
		return null;
	}

	@Override
	public String postBatchBackout(List<Long> formAutoIds) {
		return null;
	}
}