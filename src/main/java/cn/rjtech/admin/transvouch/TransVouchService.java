package cn.rjtech.admin.transvouch;

import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;

import cn.rjtech.admin.transvouchdetail.TransVouchDetailService;
import cn.rjtech.model.momdata.OtherOut;
import cn.rjtech.model.momdata.TransVouchDetail;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.TransVouch;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 出库管理-调拨单列表 Service
 * @ClassName: TransVouchService
 * @author: RJ
 * @date: 2023-05-11 14:54
 */
public class TransVouchService extends BaseService<TransVouch> {

	private final TransVouch dao = new TransVouch().dao();

	@Override
	protected TransVouch dao() {
		return dao;
	}

	@Inject
	private TransVouchDetailService transVouchDetailService;

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */

	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("transvouch.paginateAdminDatas",kv).paginate(pageNumber, pageSize);
	}


	/**
	 * 调拨单列表 明细
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> getTransVouchLines(int pageNumber, int pageSize, Kv kv){
		return dbTemplate("transvouch.getTransVouchLines",kv).paginate(pageNumber, pageSize);

	}

	/**
	 * 保存
	 * @param transVouch
	 * @return
	 */
	public Ret save(TransVouch transVouch) {
		if(transVouch==null || isOk(transVouch.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(transVouch.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=transVouch.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(transVouch.getAutoid(), JBoltUserKit.getUserId(), transVouch.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param transVouch
	 * @return
	 */
	public Ret update(TransVouch transVouch) {
		if(transVouch==null || notOk(transVouch.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		TransVouch dbTransVouch=findById(transVouch.getAutoID());
		if(dbTransVouch==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(transVouch.getName(), transVouch.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=transVouch.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(transVouch.getAutoid(), JBoltUserKit.getUserId(), transVouch.getName());
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
	 * @param transVouch 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(TransVouch transVouch, Kv kv) {
		//addDeleteSystemLog(transVouch.getAutoid(), JBoltUserKit.getUserId(),transVouch.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param transVouch 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(TransVouch transVouch, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(transVouch, kv);
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
	 * 拉取产线和产线编码
	 */
	public List<Record> workRegionMList(Kv kv) {
		kv.set("orgCode", getOrgCode());
		return dbTemplate("transvouch.workRegionMList", kv).find();
	}


	public Ret submitByJBoltTables(JBoltTableMulti jboltTableMulti, Integer param, String revokeVal) {
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
		String billNo = BillNoUtils.getcDocNo(getOrgId(), "SCD", 5);


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

		tx(()->{
			String headerId = null;
			// 获取Form对应的数据
			if (jBoltTable.formIsNotBlank()) {
				TransVouch transVouch = jBoltTable.getFormModel(TransVouch.class,"transVouch");


				//	行数据为空 不保存
				if ("save".equals(revokeVal)) {
					if (transVouch.getAutoID() == null && !jBoltTable.saveIsNotBlank() && !jBoltTable.updateIsNotBlank() && !jBoltTable.deleteIsNotBlank()) {
						ValidationUtils.isTrue(false, "请先添加行数据！");
					}
				}
				if ("submit".equals(revokeVal) && transVouch.getAutoID() == null) {
					ValidationUtils.isTrue(false, "请保存后提交审核！！！");
				}

				if (transVouch.getAutoID() == null) {
//					保存
//					状态：1=已保存，2=待审核，3=已审核
					transVouch.setState(param);

					transVouch.setCreateDate(nowDate);
					transVouch.setOrganizeCode(OrgCode);
					transVouch.setCreatePerson(userName);
					transVouch.setModifyDate(nowDate);
					transVouch.setModifyPerson(userName);
					transVouch.setBillNo(billNo);
					transVouch.setBillType("手工新增");
					save(transVouch);
					headerId = transVouch.getAutoID();
				} else {
					if ( param == 2 ){
						transVouch.setAuditDate(nowDate);
						transVouch.setAuditPerson(userName);
					}
					transVouch.setState(param);
					transVouch.setModifyDate(nowDate);
					transVouch.setModifyPerson(userName);
					update(transVouch);
					headerId = transVouch.getAutoID();
				}
			}

			// 获取待保存数据 执行保存
			if (jBoltTable.saveIsNotBlank()) {
				List<TransVouchDetail> lines = jBoltTable.getSaveModelList(TransVouchDetail.class);

				Long finalHeaderId = Long.valueOf(headerId);
				lines.forEach(transVouchDetail -> {
					transVouchDetail.setMasID(finalHeaderId);
					transVouchDetail.setCreateDate(nowDate);
					transVouchDetail.setCreatePerson(userName);
					transVouchDetail.setModifyDate(nowDate);
					transVouchDetail.setModifyPerson(userName);
				});
				transVouchDetailService.batchSave(lines);
			}
			// 获取待更新数据 执行更新
			if (jBoltTable.updateIsNotBlank()) {
				List<TransVouchDetail> lines = jBoltTable.getUpdateModelList(TransVouchDetail.class);

				lines.forEach(transVouchDetail -> {
					transVouchDetail.setModifyDate(nowDate);
					transVouchDetail.setModifyPerson(userName);
				});
				transVouchDetailService.batchUpdate(lines);
			}
			// 获取待删除数据 执行删除
			if (jBoltTable.deleteIsNotBlank()) {
				transVouchDetailService.deleteByIds(jBoltTable.getDelete());
			}

			return true;
		});
		return SUCCESS;
	}


	/**
	 * 审核
	 * @param iAutoId
	 * @return
	 */
	public Ret approve(String iAutoId,Integer mark) {
		boolean success = false;
		String userName = JBoltUserKit.getUserName();
		Date nowDate = new Date();
			List<TransVouch> listByIds = getListByIds(iAutoId);
			if (listByIds.size() > 0) {
				for (TransVouch transVouch : listByIds) {
					if (transVouch.getState() != 2) {
						return warn("订单："+transVouch.getBillNo()+"状态不支持审核操作！");
					}
					//订单状态：3. 已审核
					transVouch.setState(3);
					transVouch.setAuditDate(nowDate);
					transVouch.setAuditPerson(userName);
					success= transVouch.update();
				}
			}

		return SUCCESS;
	}


	/**
	 * 批量反审批
	 * @param ids
	 * @return
	 */
	public Ret NoApprove(String ids) {
		//TODO数据同步暂未开发 现只修改状态
		for (TransVouch transVouch :  getListByIds(ids)) {
//			订单状态： 3. 已审批
			if (transVouch.getState() != 3) {
				return warn("订单："+transVouch.getBillNo()+"状态不支持反审批操作！");
			}

			//订单状态： 2. 待审批
			transVouch.setState(2);
			transVouch.update();
		}
		return SUCCESS;
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
		TransVouch transVouch = findById(iAutoId);
		//订单状态：2. 待审批
		transVouch.setState(1);
		transVouch.setAuditDate(null);
		transVouch.setAuditPerson(null);
		boolean result = transVouch.update();
		return ret(result);
	}



}