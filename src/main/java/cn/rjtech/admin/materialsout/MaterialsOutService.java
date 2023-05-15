package cn.rjtech.admin.materialsout;

import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;
import cn.rjtech.admin.materialsoutdetail.MaterialsOutDetailService;
import cn.rjtech.admin.otheroutdetail.OtherOutDetailService;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 出库管理-材料出库单列表 Service
 * @ClassName: MaterialsOutService
 * @author: RJ
 * @date: 2023-05-13 16:16
 */
public class MaterialsOutService extends BaseService<MaterialsOut> {

	private final MaterialsOut dao = new MaterialsOut().dao();

	@Override
	protected MaterialsOut dao() {
		return dao;
	}

	@Inject
	private MaterialsOutDetailService materialsOutDetailService;

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("materialsout.paginateAdminDatas",kv).paginate(pageNumber, pageSize);
	}

	/**
	 * 保存
	 * @param materialsOut
	 * @return
	 */
	public Ret save(MaterialsOut materialsOut) {
		if(materialsOut==null || isOk(materialsOut.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(materialsOut.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=materialsOut.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(materialsOut.getAutoid(), JBoltUserKit.getUserId(), materialsOut.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param materialsOut
	 * @return
	 */
	public Ret update(MaterialsOut materialsOut) {
		if(materialsOut==null || notOk(materialsOut.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MaterialsOut dbMaterialsOut=findById(materialsOut.getAutoID());
		if(dbMaterialsOut==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(materialsOut.getName(), materialsOut.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=materialsOut.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(materialsOut.getAutoid(), JBoltUserKit.getUserId(), materialsOut.getName());
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
	 * @param materialsOut 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MaterialsOut materialsOut, Kv kv) {
		//addDeleteSystemLog(materialsOut.getAutoid(), JBoltUserKit.getUserId(),materialsOut.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param materialsOut 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MaterialsOut materialsOut, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(materialsOut, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}



	public Ret submitByJBoltTables(JBoltTableMulti jboltTableMulti, Integer param, String revokeVal, String autoid) {
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

		tx(()->{
			Long headerId = null;
			// 获取Form对应的数据
			if (jBoltTable.formIsNotBlank()) {
				MaterialsOut materialsOut = jBoltTable.getFormBean(MaterialsOut.class);

				//	行数据为空 不保存
				if ("save".equals(revokeVal)) {
					if (materialsOut.getAutoID() == null && !jBoltTable.saveIsNotBlank() && !jBoltTable.updateIsNotBlank() && !jBoltTable.deleteIsNotBlank()) {
						ValidationUtils.isTrue(false, "请先添加行数据！");
					}
				}


				if (materialsOut.getAutoID() == null && "save".equals(revokeVal)) {
//					保存
//					订单状态：1=已保存，2=待审核，3=已审核
					materialsOut.setState(param);

					materialsOut.setCreateDate(nowDate);
					materialsOut.setOrganizeCode(OrgCode);
					materialsOut.setCreatePerson(userName);
					materialsOut.setModifyDate(nowDate);
					materialsOut.setModifyPerson(userName);
					save(materialsOut);
					headerId = materialsOut.getAutoID();
				}else {
					if ( param == 2 ){
						materialsOut.setAuditDate(nowDate);
						materialsOut.setAuditPerson(userName);
					}
					materialsOut.setState(param);
					materialsOut.setModifyDate(nowDate);
					materialsOut.setModifyPerson(userName);
					update(materialsOut);
					headerId = materialsOut.getAutoID();
				}
			}

			// 获取待保存数据 执行保存
			if (jBoltTable.saveIsNotBlank()) {
				List<MaterialsOutDetail> lines = jBoltTable.getSaveModelList(MaterialsOutDetail.class);

				Long finalHeaderId = headerId;
				lines.forEach(otherOutDetail -> {
					otherOutDetail.setMasID(finalHeaderId);
					otherOutDetail.setCreateDate(nowDate);
					otherOutDetail.setCreatePerson(userName);
					otherOutDetail.setModifyDate(nowDate);
					otherOutDetail.setModifyPerson(userName);
				});
				materialsOutDetailService.batchSave(lines);
			}
			// 获取待更新数据 执行更新
			if (jBoltTable.updateIsNotBlank()) {
				List<MaterialsOutDetail> lines = jBoltTable.getUpdateModelList(MaterialsOutDetail.class);

				lines.forEach(materialsOutDetail -> {
					materialsOutDetail.setModifyDate(nowDate);
					materialsOutDetail.setModifyPerson(userName);
				});
				materialsOutDetailService.batchUpdate(lines);
			}
			// 获取待删除数据 执行删除
			if (jBoltTable.deleteIsNotBlank()) {
				materialsOutDetailService.deleteByIds(jBoltTable.getDelete());
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
		List<MaterialsOut> listByIds = getListByIds(iAutoId);
		if (listByIds.size() > 0) {
			for (MaterialsOut materialsOut : listByIds) {
				if (materialsOut.getState() != 2) {
					return warn("订单："+materialsOut.getBillNo()+"状态不支持审核操作！");
				}
				//订单状态：3. 已审核
				materialsOut.setState(3);
				materialsOut.setAuditDate(nowDate);
				materialsOut.setAuditPerson(userName);
				success= materialsOut.update();
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
		for (MaterialsOut materialsOut :  getListByIds(ids)) {
//			订单状态： 3. 已审批
			if (materialsOut.getState() != 3) {
				return warn("订单："+materialsOut.getBillNo()+"状态不支持反审批操作！");
			}

			//订单状态： 2. 待审批
			materialsOut.setState(2);
			materialsOut.update();
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
		MaterialsOut materialsOut = findById(iAutoId);
		//订单状态：2. 待审批
		materialsOut.setState(1);
		materialsOut.setAuditDate(null);
		materialsOut.setAuditPerson(null);
		boolean result = materialsOut.update();
		return ret(result);
	}

	/**
	 * 材料出库单列表 明细
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> getMaterialsOutLines(int pageNumber, int pageSize, Kv kv){
		return dbTemplate("materialsout.getMaterialsOutLines",kv).paginate(pageNumber, pageSize);

	}


	/**
	 * 生产工单查询
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> moDetailData(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate(u8SourceConfigName(),"materialsout.moDetailData",kv).paginate(pageNumber, pageSize);
	}



}