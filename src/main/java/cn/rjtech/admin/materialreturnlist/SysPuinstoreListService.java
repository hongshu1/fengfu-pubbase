package cn.rjtech.admin.materialreturnlist;

import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.materialsout.MaterialsOutService;
import cn.rjtech.admin.purchasetype.PurchaseTypeService;
import cn.rjtech.admin.rdstyle.RdStyleService;
import cn.rjtech.admin.syspuinstore.SysPuinstoreService;
import cn.rjtech.admin.syspuinstore.SysPuinstoredetailService;
import cn.rjtech.model.momdata.SysPuinstore;
import cn.rjtech.model.momdata.SysPuinstoredetail;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 出库管理-物料退货列表 Service
 * @ClassName: SysPuinstoreService
 * @author: RJ
 * @date: 2023-05-19 10:49
 */
public class SysPuinstoreListService extends BaseService<SysPuinstore> {

	private final SysPuinstore dao = new SysPuinstore().dao();

	@Override
	protected SysPuinstore dao() {
		return dao;
	}

	@Inject
	private SysPuinstoredetailService syspuinstoredetailservice;
	@Inject
	private PurchaseTypeService purchaseTypeService;
	@Inject
	private MaterialsOutService materialsOutService;
	@Inject
	private RdStyleService rdStyleService;
	@Inject
	private SysPuinstoreService sysPuinstoreService;

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("materialreturnlist.paginateAdminDatas",kv).paginate(pageNumber, pageSize);
	}

	/**
	 * 保存
	 * @param sysPuinstore
	 * @return
	 */
	public Ret save(SysPuinstore sysPuinstore) {
		if(sysPuinstore==null || isOk(sysPuinstore.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(sysPuinstore.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysPuinstore.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(sysPuinstore.getAutoid(), JBoltUserKit.getUserId(), sysPuinstore.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param sysPuinstore
	 * @return
	 */
	public Ret update(SysPuinstore sysPuinstore) {
		if(sysPuinstore==null || notOk(sysPuinstore.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SysPuinstore dbSysPuinstore=findById(sysPuinstore.getAutoID());
		if(dbSysPuinstore==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(sysPuinstore.getName(), sysPuinstore.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysPuinstore.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(sysPuinstore.getAutoid(), JBoltUserKit.getUserId(), sysPuinstore.getName());
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
				SysPuinstore puinstore = findById(autoId);
				ValidationUtils.notNull(puinstore, JBoltMsg.DATA_NOT_EXIST);

				// TODO 可能需要补充校验组织账套权限
				// TODO 存在关联使用时，校验是否仍在使用

				//删除行数据
				delete("delete from T_Sys_PUInStoreDetail where MasID = '"+autoId+"'");
				ValidationUtils.isTrue(puinstore.delete(), JBoltMsg.FAIL);

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
	 * 删除行数据
	 * @param autoId
	 * @return
	 */
	public Ret deleteList(String autoId) {
		tx(() -> {
			ValidationUtils.notNull(autoId, JBoltMsg.DATA_NOT_EXIST);
			delete("delete from T_Sys_PUInStoreDetail where autoId = '"+autoId+"'");
			return true;
		});
		return SUCCESS;

	}

	/**
	 * 删除数据后执行的回调
	 * @param sysPuinstore 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SysPuinstore sysPuinstore, Kv kv) {
		//addDeleteSystemLog(sysPuinstore.getAutoid(), JBoltUserKit.getUserId(),sysPuinstore.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param sysPuinstore 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(SysPuinstore sysPuinstore, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(sysPuinstore, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}


	public Ret submitByJBoltTables(JBoltTableMulti jboltTableMulti, String param, String revokeVal) {
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
			String headerId = null;
			// 获取Form对应的数据
			if (jBoltTable.formIsNotBlank()) {
				SysPuinstore puinstore = jBoltTable.getFormModel(SysPuinstore.class,"puinstore");
				//	行数据为空 不保存
				if ("save".equals(revokeVal)) {
					if (puinstore.getAutoID() == null && !jBoltTable.saveIsNotBlank() && !jBoltTable.updateIsNotBlank() && !jBoltTable.deleteIsNotBlank()) {
						ValidationUtils.isTrue(false, "请先添加行数据！");
					}
				}

				if ("submit".equals(revokeVal) && puinstore.getAutoID() == null) {
					ValidationUtils.isTrue(false, "请保存后提交审核！！！");
				}

				if (puinstore.getAutoID() == null && "save".equals(revokeVal)) {
//					保存
//					订单状态：1=已保存，2=待审核，3=已审核
					puinstore.setState(param);
					puinstore.setCreateDate(nowDate);
					puinstore.setOrganizeCode(OrgCode);
					puinstore.setCreatePerson(userName);
					puinstore.setModifyDate(nowDate);
					puinstore.setModifyPerson(userName);
					save(puinstore);
					headerId = puinstore.getAutoID();
				}else {
					if ( param == "2" ){
						puinstore.setAuditDate(nowDate);
						puinstore.setAuditPerson(userName);
					}
					puinstore.setState(param);
					puinstore.setModifyDate(nowDate);
					puinstore.setModifyPerson(userName);
					update(puinstore);
					headerId = puinstore.getAutoID();
				}
			}

			// 获取待保存数据 执行保存
			if (jBoltTable.saveIsNotBlank()) {
				List<SysPuinstoredetail> lines = jBoltTable.getSaveModelList(SysPuinstoredetail.class);

				String finalHeaderId = headerId;
				lines.forEach(otherOutDetail -> {
					otherOutDetail.setMasID(finalHeaderId);
					otherOutDetail.setCreateDate(nowDate);
					otherOutDetail.setCreatePerson(userName);
					otherOutDetail.setModifyDate(nowDate);
					otherOutDetail.setModifyPerson(userName);
				});
				syspuinstoredetailservice.batchSave(lines);
			}
			// 获取待更新数据 执行更新
			if (jBoltTable.updateIsNotBlank()) {
				List<SysPuinstoredetail> lines = jBoltTable.getUpdateModelList(SysPuinstoredetail.class);

				lines.forEach(materialsOutDetail -> {
					materialsOutDetail.setModifyDate(nowDate);
					materialsOutDetail.setModifyPerson(userName);
				});
				syspuinstoredetailservice.batchUpdate(lines);
			}
			// 获取待删除数据 执行删除
			if (jBoltTable.deleteIsNotBlank()) {
				syspuinstoredetailservice.deleteByIds(jBoltTable.getDelete());
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
		List<SysPuinstore> listByIds = getListByIds(iAutoId);
		if (listByIds.size() > 0) {
			for (SysPuinstore puinstore : listByIds) {
				Integer state = Integer.valueOf(puinstore.getState());
				if (state != 2) {
					return warn("订单："+puinstore.getBillNo()+"状态不支持审核操作！");
				}
				//订单状态：3. 已审核
				puinstore.setState("3");
				puinstore.setAuditDate(nowDate);
				puinstore.setAuditPerson(userName);
				success= puinstore.update();
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
		for (SysPuinstore puinstore :  getListByIds(ids)) {
			Integer state = Integer.valueOf(puinstore.getState());
//			订单状态： 3. 已审批
			if (state != 3) {
				return warn("订单："+puinstore.getBillNo()+"状态不支持反审批操作！");
			}

			//订单状态： 2. 待审批
			puinstore.setState("2");
			puinstore.update();
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
		SysPuinstore puinstore = findById(iAutoId);
		//订单状态：2. 待审批
		puinstore.setState("1");
		puinstore.setAuditDate(null);
		puinstore.setAuditPerson(null);
		boolean result = puinstore.update();
		return ret(result);
	}

	/**
	 * 材料出库单列表 明细
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> getmaterialReturnListLines(int pageNumber, int pageSize, Kv kv){
		return dbTemplate("materialreturnlist.getmaterialReturnListLines",kv).paginate(pageNumber, pageSize);

	}

}