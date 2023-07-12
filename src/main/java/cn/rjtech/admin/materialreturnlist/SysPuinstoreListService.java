package cn.rjtech.admin.materialreturnlist;

import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.syspuinstore.SysPuinstoreService;
import cn.rjtech.admin.syspuinstore.SysPuinstoredetailService;
import cn.rjtech.model.momdata.SysPuinstore;
import cn.rjtech.model.momdata.SysPuinstoredetail;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.StringUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
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
public class SysPuinstoreListService extends BaseService<SysPuinstore> implements IApprovalService {

	private final SysPuinstore dao = new SysPuinstore().dao();

	@Override
	protected SysPuinstore dao() {
		return dao;
	}

	@Inject
	private SysPuinstoredetailService syspuinstoredetailservice;
	@Inject
	private SysPuinstoreService sysPuinstoreService;

	/**
	 * 后台管理分页查询
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("materialreturnlist.paginateAdminDatas",kv).paginate(pageNumber, pageSize);
	}

	/**
	 * 保存
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
	 */
	public Ret delete(Long autoId) {
		tx(() -> {
			ValidationUtils.notNull(autoId, JBoltMsg.DATA_NOT_EXIST);
			update("UPDATE T_Sys_PUInStore SET isDeleted ='1' WHERE AutoID='"+autoId+"'");
			return true;
		});
		return SUCCESS;
//		return deleteById(id,true);
	}

	/**
	 * 删除行数据
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
     *
     * @param sysPuinstore 要删除的model
     * @param kv           携带额外参数一般用不上
     */
	@Override
	protected String afterDelete(SysPuinstore sysPuinstore, Kv kv) {
		//addDeleteSystemLog(sysPuinstore.getAutoid(), JBoltUserKit.getUserId(),sysPuinstore.getName());
		return null;
	}

    /**
     * 检测是否可以删除
     *
     * @param sysPuinstore 要删除的model
     * @param kv           携带额外参数一般用不上
     */
	@Override
	public String checkCanDelete(SysPuinstore sysPuinstore, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(sysPuinstore, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
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
				SysPuinstore puinstore = jBoltTable.getFormModel(SysPuinstore.class,"puinstore");



//				detailByParam = dbTemplate("syspureceive.purchaseOrderD", kv).findFirst();
//				detailByParam = sysPuinstoreService.findSysPODetailByParam(kv);

				//	行数据为空 不保存
				if (puinstore.getAutoID() == null && !jBoltTable.saveIsNotBlank() && !jBoltTable.updateIsNotBlank() && !jBoltTable.deleteIsNotBlank()) {
					ValidationUtils.error( "请先添加行数据！");
				}

				if (puinstore.getAutoID() == null) {
//					保存
//					订单状态：0=待审核，1=未审核，2=已审核. 3=审核不通过
					puinstore.setIAuditStatus(0);
					//创建人
					puinstore.setICreateBy(userId);
					puinstore.setDCreateTime(nowDate);
					puinstore.setCCreateName(userName);

					//更新人
					puinstore.setIUpdateBy(userId);
					puinstore.setDUpdateTime(nowDate);
					puinstore.setCUpdateName(userName);


					puinstore.setOrganizeCode(OrgCode);
					puinstore.setIsDeleted(false);
					save(puinstore);
					headerId = puinstore.getAutoID();
				}else {
					//更新人
					puinstore.setIUpdateBy(userId);
					puinstore.setDUpdateTime(nowDate);
					puinstore.setCUpdateName(userName);
					update(puinstore);
					headerId = puinstore.getAutoID();
				}
				AutoIDs[0] = puinstore.getAutoID();
			}

			// 获取待保存数据 执行保存
			if (jBoltTable.saveIsNotBlank()) {
				List<SysPuinstoredetail> lines = jBoltTable.getSaveModelList(SysPuinstoredetail.class);
				//判断物料退货数量
				List<Record> jBoltTableSaveRecordList = jBoltTable.getSaveRecordList();
				int k = 0;
				if (jBoltTableSaveRecordList != null) {
					for (int i = 0; i < jBoltTableSaveRecordList.size(); i++) {
						k++;
						Record record = jBoltTableSaveRecordList.get(i);
						if (!isNull(record.get("iqty"))){
							//输入数量
							BigDecimal qty = record.getBigDecimal("qty");
							//当前单行数量
							BigDecimal iqty = record.getBigDecimal("iqty");
							BigDecimal a= BigDecimal.valueOf(2);
							double cha = qty.subtract(iqty).doubleValue();
							double value = iqty.multiply(a).doubleValue();
							//物料退货判断
							if (value + cha < 0){
								ValidationUtils.error( "第" + k + "行退货数量超出现存数（" + iqty + "）！！！");
							}
						}else {
							//输入数量
							BigDecimal qty = record.getBigDecimal("qty");
							//当前单行数量
							BigDecimal qtys = record.getBigDecimal("qtys");
							//判断条件
							BigDecimal a= BigDecimal.valueOf(2);
							double cha = qty.subtract(qtys).doubleValue();
							double value = qtys.multiply(a).doubleValue();
							//整单退货判断
							if (value + cha < 0){
								ValidationUtils.error( "第" + k + "行退货数量超出现存数（" + qtys + "）！！！");
							}
						}
					}
				}
				String finalHeaderId = headerId;

				lines.forEach(materialsOutDetail -> {
					Object qtys = materialsOutDetail.get("qty");
					System.out.println(qtys);
					materialsOutDetail.setMasID(finalHeaderId);

					materialsOutDetail.setDCreateTime(nowDate);
					materialsOutDetail.setCCreateName(userName);
					materialsOutDetail.setDUpdateTime(nowDate);
					materialsOutDetail.setCUpdateName(userName);
				});
				syspuinstoredetailservice.batchSave(lines);
			}
			// 获取待更新数据 执行更新
			if (jBoltTable.updateIsNotBlank()) {
				List<SysPuinstoredetail> lines = jBoltTable.getUpdateModelList(SysPuinstoredetail.class);

				String finalHeaderId = headerId;
				lines.forEach(materialsOutDetail -> {
					materialsOutDetail.setDUpdateTime(nowDate);
					materialsOutDetail.setCUpdateName(userName);
				});
				syspuinstoredetailservice.batchUpdate(lines);
			}
			// 获取待删除数据 执行删除
			if (jBoltTable.deleteIsNotBlank()) {
				syspuinstoredetailservice.deleteByIds(jBoltTable.getDelete());
			}

			return true;
		});
		return SUCCESS.set("AutoID", AutoIDs[0]);
	}

	/**
	 * 获取条码列表
	 * 通过关键字匹配
	 * autocomplete组件使用
	 */
	public List<Record> getBarcodeDatas(Kv kv) {
		return dbTemplate("materialreturnlist.getBarcodeDatas",kv).find();
	}

	/**
	 * 查看所有材料出库单列表 明细
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> getmaterialReturnLists(int pageNumber, int pageSize, Kv kv){
		return dbTemplate("materialreturnlist.getmaterialReturnLists",kv).paginate(pageNumber, pageSize);

	}
	/**
	 * 查询头表编码的名称明细
	 */
	public Record getstockoutQcFormMList(String autoid,String OrgCode){
		return dbTemplate("materialreturnlist.getmaterialReturnLists", Kv.by("autoid",autoid).set("OrgCode",OrgCode)).findFirst();
	}


	/**
	 * 材料出库单列表 明细
	 */
	public Page<Record> getmaterialReturnListLines(int pageNumber, int pageSize, Kv kv){
		return dbTemplate("materialreturnlist.getmaterialReturnListLines",kv).paginate(pageNumber, pageSize);

	}

	/**
	 * 整单退货出库单列表 明细
	 */
	public List<Record> getmaterialLines(Kv kv){
		return dbTemplate("materialreturnlist.getmaterialLines",kv).find();
	}

	/**
	 * 获取入库订单
	 * */
	public Page<Record> getSysPODetail(Kv kv, int size, int PageSize) {
		return dbTemplate("materialreturnlist.getSysPODetail", kv).paginate(size, PageSize);
	}

	/**
	 * 获取条码列表
	 * 通过关键字匹配
	 * autocomplete组件使用
	 */
	public Record barcode(Kv kv) {
        // 先查询条码是否已添加
		Record first = dbTemplate("materialreturnlist.barcodeDatas", kv).findFirst();
        ValidationUtils.notNull(first, "条码为：" + kv.getStr("barcode") + "采购入库没有此数据！！！");
        
        return dbTemplate("materialreturnlist.getBarcodes", kv).findFirst();
	}

	@Override
	public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
		sysPuinstoreService.batchApprove(StringUtils.join(formAutoId, COMMA));
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
		SysPuinstore sysPuinstore = findById(formAutoId);
		sysPuinstore.setIAuditBy(null);
		sysPuinstore.setCAuditName(null);
		sysPuinstore.setDAuditTime(null);
		sysPuinstore.update();
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
		sysPuinstoreService.batchApprove(StringUtils.join(formAutoIds, COMMA));
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