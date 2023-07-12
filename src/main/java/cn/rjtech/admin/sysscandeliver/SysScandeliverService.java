package cn.rjtech.admin.sysscandeliver;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.syssaledeliverplandetail.SysSaledeliverplandetailService;
import cn.rjtech.admin.transvouchdetail.TransVouchDetailService;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * (双)扫码发货
 * @ClassName: SysScandeliverService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-22 09:47
 */
public class SysScandeliverService extends BaseService<SysScandeliver> {
	private final SysScandeliver dao=new SysScandeliver().dao();
	@Override
	protected SysScandeliver dao() {
		return dao;
	}

	@Inject
	private SysScandeliverdetailService sysscandeliverdetailservice;
	@Inject
	private SysSaledeliverplandetailService saledeliverplandetailService;
	@Inject
	private TransVouchDetailService transVouchDetailService;

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
     * @param SourceBillType 来源类型;DP 销售计划
     * @param isDeleted 删除状态默认0，1已删除
     * @param state 状态0保存，1已提交
	 * @return
	 */
	public Page<SysScandeliver> getAdminDatas(int pageNumber, int pageSize, String SourceBillType, String billno, Boolean isDeleted, Boolean state) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("SourceBillType",SourceBillType);
        sql.eq("BillNo",billno);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        sql.eqBooleanToChar("state",state);
        //排序
        sql.desc("AutoID");
		return paginate(sql);
	}

	/**
	 * 批量删除主从表
	 */
	public Ret deleteRmRdByIds(String ids) {
		tx(() -> {
			deleteByIds(ids);
			String[] split = ids.split(",");
			for(String s : split){
				delete("DELETE T_Sys_ScanDeliverDetail   where  MasID = ?",s);
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
		tx(() -> {
			deleteById(id);
			delete("DELETE T_Sys_ScanDeliverDetail   where  MasID = ?",id);
			return true;
		});
		return SUCCESS;
	}

	/**
	 * 保存
	 * @param sysScandeliver
	 * @return
	 */
	public Ret save(SysScandeliver sysScandeliver) {
		if(sysScandeliver==null || isOk(sysScandeliver.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(sysScandeliver.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysScandeliver.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(sysScandeliver.getAutoID(), JBoltUserKit.getUserId(), sysScandeliver.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param sysScandeliver
	 * @return
	 */
	public Ret update(SysScandeliver sysScandeliver) {
		if(sysScandeliver==null || notOk(sysScandeliver.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SysScandeliver dbSysScandeliver=findById(sysScandeliver.getAutoID());
		if(dbSysScandeliver==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(sysScandeliver.getName(), sysScandeliver.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysScandeliver.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(sysScandeliver.getAutoID(), JBoltUserKit.getUserId(), sysScandeliver.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param sysScandeliver 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SysScandeliver sysScandeliver, Kv kv) {
		//addDeleteSystemLog(sysScandeliver.getAutoID(), JBoltUserKit.getUserId(),sysScandeliver.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param sysScandeliver model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SysScandeliver sysScandeliver, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SysScandeliver sysScandeliver, String column, Kv kv) {
		//addUpdateSystemLog(sysScandeliver.getAutoID(), JBoltUserKit.getUserId(), sysScandeliver.getName(),"的字段["+column+"]值:"+sysScandeliver.get(column));
		/**
		switch(column){
		    case "isDeleted":
		        break;
		    case "state":
		        break;
		}
		*/
		return null;
	}

	public List<Record> rcvpland(Kv kv) {
		return dbTemplate("sysscandeliver.rcvpland", kv).find();
	}

	public List<Record> customeraddr(Kv kv) {
		return dbTemplate("sysscandeliver.customeraddr", kv).find();
	}


	/**
	 * 可编辑表格提交
	 *
	 * @param jBoltTable 编辑表格提交内容
	 * @return
	 */
	public Ret submitByJBoltTable(JBoltTable jBoltTable) {
		//当前操作人员  当前时间
		User user = JBoltUserKit.getUser();
		Date nowDate = new Date();
		System.out.println("saveTable===>" + jBoltTable.getSave());
		System.out.println("updateTable===>" + jBoltTable.getUpdate());
		System.out.println("deleteTable===>" + jBoltTable.getDelete());
		System.out.println("form===>" + jBoltTable.getForm());
		AtomicReference<String> headerId = new AtomicReference<>();
		tx(() -> {

			String cusCode = " ";

			//判断form是否为空
			if (jBoltTable.formIsNotBlank()) {
				SysScandeliver sysScandeliver = jBoltTable.getFormModel(SysScandeliver.class, "sysscandeliver");

				cusCode = sysScandeliver.getCusCode();

				String autoID = sysScandeliver.getAutoID();

				List<SysScandeliverdetail> detailList = new ArrayList<>();

				if (notOk(autoID)) {
					ValidationUtils.isTrue(jBoltTable.saveIsNotBlank(), "行数据为空，不允许保存！");
					sysScandeliver.setOrganizeCode(getOrgCode());
					sysScandeliver.setICreateBy(user.getId());
					sysScandeliver.setDCreateTime(nowDate);
					sysScandeliver.setIUpdateBy(user.getId());
					sysScandeliver.setDUpdateTime(nowDate);
					sysScandeliver.setCUpdateName(user.getName());
					sysScandeliver.setCCreateName(user.getName());
					sysScandeliver.setIsDeleted(false);
					sysScandeliver.save();
				} else {
					sysScandeliver.setIUpdateBy(user.getId());
					sysScandeliver.setDUpdateTime(nowDate);
					sysScandeliver.setCUpdateName(user.getName());
					sysScandeliver.update();
				}

				headerId.set(sysScandeliver.getAutoID());


			if (jBoltTable.saveIsNotBlank()) {
				List<SysScandeliverdetail> saveModelList = jBoltTable.getSaveModelList(SysScandeliverdetail.class);

				saveModelList.forEach(sysScandeliverdetail -> {
					sysScandeliverdetail.setMasID(headerId.get());
					sysScandeliverdetail.setICreateBy(user.getId());
					sysScandeliverdetail.setDCreateTime(nowDate);
					sysScandeliverdetail.setIUpdateBy(user.getId());
					sysScandeliverdetail.setDUpdateTime(nowDate);
					sysScandeliverdetail.setCUpdateName(user.getName());
					sysScandeliverdetail.setCCreateName(user.getName());
					sysScandeliverdetail.setIsDeleted(false);
				});
//                    保存
				sysscandeliverdetailservice.batchSave(saveModelList,saveModelList.size());
				detailList.addAll(saveModelList);
			}

			//更新
			if (jBoltTable.updateIsNotBlank()) {
				List<SysScandeliverdetail> updateModelList = jBoltTable.getUpdateModelList(SysScandeliverdetail.class);
				updateModelList.forEach(sysScandeliverdetail -> {
					sysScandeliverdetail.setIUpdateBy(user.getId());
					sysScandeliverdetail.setDUpdateTime(nowDate);
					sysScandeliverdetail.setCUpdateName(user.getName());
				});
				sysscandeliverdetailservice.batchUpdate(updateModelList, updateModelList.size());
				detailList.addAll(updateModelList);
			}

			//获取待删除数据 执行删除
			if (jBoltTable.deleteIsNotBlank()) {
				sysscandeliverdetailservice.deleteByIds(jBoltTable.getDelete());
			}

//			在客户档案找到该客户的发货类型 1、调拨单 2、销售发货单
			Record cusShipment = findFirstRecord("select cShipment from Bd_Customer where cCusCode = '" + cusCode + "' and isDeleted = '0'");
			if (isOk(cusShipment)){

				if ("1".equals(cusShipment.getStr("cshipment"))){
						saveTransVouch(sysScandeliver,detailList);
				} else {
						saveSaleDeliverP(sysScandeliver, detailList);
				}

			} else {
				ValidationUtils.error( "该客户未配置出货单据类型");
			}
		}
			return true;
		});
		return SUCCESS.set("autoid",headerId.get());
	}



	//可编辑表格提交-新增数据
	private void saveTableSubmitDatas(JBoltTable jBoltTable,SysScandeliver sysotherin){
		List<Record> list = jBoltTable.getSaveRecordList();
		if(CollUtil.isEmpty(list)) return;
		ArrayList<SysScandeliverdetail> sysproductindetail = new ArrayList<>();
		Date now = new Date();
		User user = JBoltUserKit.getUser();
		for (int i=0;i<list.size();i++) {
			Record row = list.get(i);
			SysScandeliverdetail sysdetail = new SysScandeliverdetail();
			sysdetail.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
//            row.set("iautoid", JBoltSnowflakeKit.me.nextId());
			sysdetail.setBarcode(row.get("barcode"));
			sysdetail.setPackBarcode(row.get("ccontainercode"));
			sysdetail.setInvCode(row.get("cinvcode"));
			sysdetail.setWhCode(row.get("whcode"));
			sysdetail.setQty(new BigDecimal(row.get("qty").toString()));
			sysdetail.setMasID(sysotherin.getAutoID());
			sysdetail.setSourceBillType(row.getStr("sourcebilltype"));
			sysdetail.setSourceBillNo(row.getStr("sourcebillno"));
			sysdetail.setSourceBillDid(row.getStr("sourcebilldid"));
			sysdetail.setSourceBillID(row.getStr("sourcebilldid"));
			sysproductindetail.add(sysdetail);
		}
		sysscandeliverdetailservice.batchSave(sysproductindetail);
	}
	//可编辑表格提交-修改数据
	private void updateTableSubmitDatas(JBoltTable jBoltTable,SysScandeliver sysotherin){
		List<Record> list = jBoltTable.getUpdateRecordList();
		if(CollUtil.isEmpty(list)) return;
		ArrayList<SysScandeliverdetail> sysproductindetail = new ArrayList<>();
		Date now = new Date();
		User user = JBoltUserKit.getUser();
		for(int i = 0;i < list.size(); i++){
			Record row = list.get(i);
			SysScandeliverdetail sysdetail = new SysScandeliverdetail();
			sysdetail.setAutoID(row.get("autoid").toString());
			sysdetail.setBarcode(row.get("barcode"));
			sysdetail.setInvCode(row.get("cinvcode"));
			System.out.println(row.get("whcode").toString());
			sysdetail.setWhCode(row.get("whcode").toString());
			sysdetail.setQty(new BigDecimal(row.get("qty").toString()));
			sysdetail.setMasID(sysotherin.getAutoID());
			sysdetail.setSourceBillType(row.getStr("sourcebilltype"));
			sysdetail.setSourceBillNo(row.getStr("sourcebillno"));
			sysdetail.setSourceBillDid(row.getStr("sourcebilldid"));
			sysdetail.setSourceBillID(row.getStr("sourcebilldid"));
			sysproductindetail.add(sysdetail);

		}
		sysscandeliverdetailservice.batchUpdate(sysproductindetail);
	}
	//可编辑表格提交-删除数据
	private void deleteTableSubmitDatas(JBoltTable jBoltTable){
		Object[] ids = jBoltTable.getDelete();
		if(ArrayUtil.isEmpty(ids)) return;
		sysscandeliverdetailservice.deleteByIds(ids);
	}


	/**
	 * 获取车次号相关数据
	 * @return
	 */
	public List<Record> getCarData(Kv kv){
		kv.setIfNotNull("orgId", getOrgId());
		return dbTemplate("sysscandeliver.getCarData", kv).find();
	}

	/**
	 * 获取客户地址
	 * @param kv
	 * @return
	 */
	public List<Record> getCustAddr(Kv kv){
		return dbTemplate("sysscandeliverone.getCustAddr", kv).find();
	}

    /**
     * 扫码获取资源
     * @param kv
     * @return
     */
	public List<Record> getResource(Kv kv){
        List<Record> list = dbTemplate("sysscandeliver.getResource", kv).find();
        ValidationUtils.isTrue(list != null && list.size() > 0, "找不到该现品票与传票号对应的存货");
        return list;
    }

	/**
	 * 推销售发货单数据
	 * @param sysScandeliver
	 * @param list
	 */
	public void saveSaleDeliverP(SysScandeliver sysScandeliver, List<SysScandeliverdetail> list){
		User user = JBoltUserKit.getUser();
		Date nowDate = new Date();
		SysSaledeliverplan saledeliverplan = new SysSaledeliverplan();
		saledeliverplan.setSourceBillType(sysScandeliver.getSourceBillType());
		saledeliverplan.setSourceBillID(sysScandeliver.getSourceBillID());
		saledeliverplan.setSourceBillType("扫码出货");
		saledeliverplan.setOrganizeCode(sysScandeliver.getOrganizeCode());
		saledeliverplan.setBillNo(sysScandeliver.getBillNo());
		saledeliverplan.setBillType(sysScandeliver.getBillType());
		saledeliverplan.setBillDate(sysScandeliver.getBillDate());
		saledeliverplan.setBillNo(sysScandeliver.getBillNo());
		saledeliverplan.setIcreateby(user.getId());
		saledeliverplan.setDcreatetime(nowDate);
		saledeliverplan.setIupdateby(user.getId());
		saledeliverplan.setDupdatetime(nowDate);
		saledeliverplan.setCcreatename(user.getName());
		saledeliverplan.setCupdatename(user.getName());
		saledeliverplan.setIsDeleted(false);
		saledeliverplan.save();

		String headerId = saledeliverplan.getAutoID();

		List<SysSaledeliverplandetail> newList = new ArrayList<>();

		for (int i = 0; i < list.size(); i++) {
			SysSaledeliverplandetail saledeliverplandetail = new SysSaledeliverplandetail();
			SysScandeliverdetail sysScandeliverdetail = list.get(i);
			saledeliverplandetail.setMasID(headerId);
			saledeliverplandetail.setInvCode(sysScandeliverdetail.getInvCode());
			saledeliverplandetail.setBarcode(sysScandeliverdetail.getBarcode());
			saledeliverplandetail.setWhCode(sysScandeliverdetail.getWhCode());
			saledeliverplandetail.setQty(sysScandeliverdetail.getQty());
			saledeliverplandetail.setSourceBillNo(sysScandeliverdetail.getSourceBillNo());
			saledeliverplandetail.setSourceBillID(sysScandeliverdetail.getSourceBillID());
			saledeliverplandetail.setSourceBillDid(sysScandeliverdetail.getSourceBillDid());
			saledeliverplandetail.setSourceBillType(sysScandeliverdetail.getSourceBillType());

			saledeliverplandetail.setIcreateby(user.getId());
			saledeliverplandetail.setDcreatetime(nowDate);
			saledeliverplandetail.setIupdateby(user.getId());
			saledeliverplandetail.setDupdatetime(nowDate);
			saledeliverplandetail.setCcreatename(user.getName());
			saledeliverplandetail.setCupdatename(user.getName());
			saledeliverplandetail.setIsDeleted(false);
			newList.add(saledeliverplandetail);
		}
		saledeliverplandetailService.batchSave(newList,newList.size());
	}


	/**
	 * 推调拨单数据
	 * @param sysScandeliver
	 * @param list
	 */
	public void saveTransVouch(SysScandeliver sysScandeliver, List<SysScandeliverdetail> list){
		User user = JBoltUserKit.getUser();
		Date nowDate = new Date();
		TransVouch transVouch = new TransVouch();
		transVouch.setSourceBillType(sysScandeliver.getSourceBillType());
		transVouch.setSourceBillDid(sysScandeliver.getSourceBillDid());
		transVouch.setOrganizeCode(sysScandeliver.getOrganizeCode());
		transVouch.setBillNo(sysScandeliver.getBillNo());
		transVouch.setBillType(sysScandeliver.getBillType());
		transVouch.setBillDate(nowDate);
		transVouch.setBillNo(sysScandeliver.getBillNo());
		transVouch.setIcreateBy(user.getId());
		transVouch.setDcreateTime(nowDate);
		transVouch.setIupdateBy(user.getId());
		transVouch.setDupdateTime(nowDate);
		transVouch.setCcreateName(user.getName());
		transVouch.setCupdateName(user.getName());
		transVouch.setIsDeleted(false);
		transVouch.save();

		Long headerId = transVouch.getAutoID();

		List<TransVouchDetail> newList = new ArrayList<>();

		for (int i = 0; i < list.size(); i++) {
			TransVouchDetail transVouchDetail = new TransVouchDetail();
			SysScandeliverdetail sysScandeliverdetail = list.get(i);
			transVouchDetail.setMasID(headerId);
			transVouchDetail.setInvCode(sysScandeliverdetail.getInvCode());
			transVouchDetail.setBarcode(sysScandeliverdetail.getBarcode());
			transVouchDetail.setQty(sysScandeliverdetail.getQty());
			transVouchDetail.setSourceBillNo(sysScandeliverdetail.getSourceBillNo());
			transVouchDetail.setSourceBillID(sysScandeliverdetail.getSourceBillID());
			transVouchDetail.setSourceBillDid(sysScandeliverdetail.getSourceBillDid());
			transVouchDetail.setSourceBillType(sysScandeliverdetail.getSourceBillType());

			transVouchDetail.setIcreateby(user.getId());
			transVouchDetail.setDcreatetime(nowDate);
			transVouchDetail.setIupdateby(user.getId());
			transVouchDetail.setDupdatetime(nowDate);
			transVouchDetail.setCcreatename(user.getName());
			transVouchDetail.setCupdatename(user.getName());
			transVouchDetail.setIsDeleted(false);
			newList.add(transVouchDetail);
		}
		transVouchDetailService.batchSave(newList,newList.size());
	}
}
