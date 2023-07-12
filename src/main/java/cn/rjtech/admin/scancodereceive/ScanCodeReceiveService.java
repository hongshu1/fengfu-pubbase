package cn.rjtech.admin.scancodereceive;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.admin.syspuinstore.SysPuinstoreService;
import cn.rjtech.admin.syspuinstore.SysPuinstoredetailService;
import cn.rjtech.admin.syspureceive.SysPureceiveService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.model.momdata.*;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 双单位扫码收货
 * @ClassName: ScanCodeReceiveService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-10 10:01
 */
public class ScanCodeReceiveService extends BaseService<SysPureceive> implements IApprovalService {

	private final SysPureceive dao=new SysPureceive().dao();

	@Override
	protected SysPureceive dao() {
		return dao;
	}

	@Inject
	private ScanCodeReceiveDetailService scanCodeReceiveDetailService;

	@Inject
	private RcvDocQcFormMService rcvdocqcformmservice;

	@Inject
	private VendorService vendorservice;

    @Inject
    private SysPureceiveService sysPureceiveService;

    @Inject
    private SysPuinstoreService syspuinstoreservice;

	@Inject
	private SysPuinstoredetailService syspuinstoredetailservice;

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @param keywords   关键词
     * @param BillType 到货单类型;采购PO  委外OM
     * @param IsDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<SysPureceive> getAdminDatas(int pageNumber, int pageSize, String keywords, String BillType, Boolean IsDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("BillType",BillType);
        sql.eqBooleanToChar("IsDeleted",IsDeleted);
        //关键词模糊查询
        sql.like("repositoryName",keywords);
        //排序
        sql.desc("AutoID");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param sysPureceive
	 * @return
	 */
	public Ret save(SysPureceive sysPureceive) {
		if(sysPureceive==null || isOk(sysPureceive.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(sysPureceive.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysPureceive.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(sysPureceive.getAutoID(), JBoltUserKit.getUserId(), sysPureceive.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param sysPureceive
	 * @return
	 */
	public Ret update(SysPureceive sysPureceive) {
		if(sysPureceive==null || notOk(sysPureceive.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SysPureceive dbSysPureceive=findById(sysPureceive.getAutoID());
		if(dbSysPureceive==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(sysPureceive.getName(), sysPureceive.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysPureceive.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(sysPureceive.getAutoID(), JBoltUserKit.getUserId(), sysPureceive.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param sysPureceive 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SysPureceive sysPureceive, Kv kv) {
		//addDeleteSystemLog(sysPureceive.getAutoID(), JBoltUserKit.getUserId(),sysPureceive.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param sysPureceive model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SysPureceive sysPureceive, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SysPureceive sysPureceive, String column, Kv kv) {
		//addUpdateSystemLog(sysPureceive.getAutoID(), JBoltUserKit.getUserId(), sysPureceive.getName(),"的字段["+column+"]值:"+sysPureceive.get(column));
		/**
		switch(column){
		    case "IsDeleted":
		        break;
		}
		*/
		return null;
	}
	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("scancodereceive.recpor", kv).paginate(pageNumber, pageSize);
	}
	/**
	 * 批量删除主从表
	 */
	public Ret deleteRmRdByIds(String ids) {
		tx(() -> {
			deleteByIds(ids);
			String[] split = ids.split(",");
			for(String s : split){
				delete("DELETE T_Sys_PUReceiveDetail   where  MasID = ?",s);
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
			delete("DELETE T_Sys_PUReceiveDetail   where  MasID = ?",id);
			return true;
		});
		return SUCCESS;
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
		Db.use(dataSourceConfigName()).tx(() -> {

			//判断form是否为空
			if (jBoltTable.formIsNotBlank()) {
				SysPureceive sysPureceive = jBoltTable.getFormModel(SysPureceive.class,"sysPureceive");
                sysPureceive.setOrganizeCode(getOrgCode());
				String autoID = sysPureceive.getAutoID();

//				新增 根据供应商分组
				if (notOk(autoID)) {
					ValidationUtils.isTrue(jBoltTable.saveIsNotBlank(), "行数据为空，不允许保存！");
					if (jBoltTable.saveIsNotBlank()) {
						List<SysPureceivedetail> saveModelList = jBoltTable.getSaveModelList(SysPureceivedetail.class);
						Map<String, List<SysPureceivedetail>> supplierCodeAndDetailList = new HashMap<>();

						saveModelList.forEach(sysPureceivedetail -> {
							String venCode = sysPureceivedetail.getVenCode();
							ValidationUtils.isTrue(isOk(venCode), "请选择有供应商的订单");
							List<SysPureceivedetail> pureceivedetailList = supplierCodeAndDetailList.get(venCode);
							if (pureceivedetailList != null) {
								pureceivedetailList.add(sysPureceivedetail);
							} else {
								pureceivedetailList = new ArrayList<>();
								pureceivedetailList.add(sysPureceivedetail);
								supplierCodeAndDetailList.put(venCode, pureceivedetailList);
							}
						});

						List<SysPureceivedetail> saveDetails = new ArrayList<>();

						for (Map.Entry<String,List<SysPureceivedetail>> entry: supplierCodeAndDetailList.entrySet()){

							String venCode = entry.getKey();
							List<SysPureceivedetail> list = entry.getValue();

							SysPureceive tarSySPuReceive = new SysPureceive().put(sysPureceive);
							String billNo = BillNoUtils.genCode(getOrgCode(),table());
							tarSySPuReceive.setBillNo(billNo);
							tarSySPuReceive.setVenCode(venCode);
							tarSySPuReceive.setOrganizeCode(getOrgCode());
							tarSySPuReceive.setDupdatetime(nowDate);
							tarSySPuReceive.setIcreateby(user.getId());
							tarSySPuReceive.setDcreatetime(nowDate);
							tarSySPuReceive.setIupdateby(user.getId());
							tarSySPuReceive.setDupdatetime(nowDate);
							tarSySPuReceive.setCcreatename(user.getName());
							tarSySPuReceive.setCupdatename(user.getName());
							tarSySPuReceive.setIsDeleted(false);
							tarSySPuReceive.save();
							String id = tarSySPuReceive.getAutoID();

							headerId.set(id);

							list.forEach(sysPureceivedetail -> {
								sysPureceivedetail.setMasID(id);
								sysPureceivedetail.setIcreateby(user.getId());
								sysPureceivedetail.setDcreatetime(nowDate);
								sysPureceivedetail.setIupdateby(user.getId());
								sysPureceivedetail.setDupdatetime(nowDate);
								sysPureceivedetail.setCcreatename(user.getName());
								sysPureceivedetail.setCupdatename(user.getName());
								sysPureceivedetail.setIsDeleted(false);
								saveDetails.add(sysPureceivedetail);
							});
						}
						scanCodeReceiveDetailService.batchSave(saveDetails, saveDetails.size());
					}
				} else {
					String supplierCode = sysPureceive.getVenCode();
					if (jBoltTable.saveIsNotBlank()) {
						List<SysPureceivedetail> saveModelList = jBoltTable.getSaveModelList(SysPureceivedetail.class);

						saveModelList.forEach(sysPureceivedetail -> {
							String venCode = sysPureceivedetail.getVenCode();
							ValidationUtils.isTrue(isOk(venCode) || Objects.equals(supplierCode, venCode),
									"不同供应商，不允许更新");

						});

						sysPureceive.setIupdateby(user.getId());
						sysPureceive.setDupdatetime(nowDate);
						sysPureceive.setCupdatename(user.getName());
						sysPureceive.update();

						saveModelList.forEach(sysPureceivedetail -> {
							sysPureceivedetail.setMasID(sysPureceive.getAutoID());
							sysPureceivedetail.setIcreateby(user.getId());
							sysPureceivedetail.setDcreatetime(nowDate);
							sysPureceivedetail.setIupdateby(user.getId());
							sysPureceivedetail.setDupdatetime(nowDate);
							sysPureceivedetail.setCcreatename(user.getName());
							sysPureceivedetail.setCupdatename(user.getName());
							sysPureceivedetail.setIsDeleted(false);
						});
						scanCodeReceiveDetailService.batchSave(saveModelList, saveModelList.size());
					}

                    headerId.set(autoID);
				}
			}

			//更新
			if (jBoltTable.updateIsNotBlank()) {
				List<SysPureceivedetail> updateModelList = jBoltTable.getUpdateModelList(SysPureceivedetail.class);
				updateModelList.forEach(sysPureceivedetail -> {
					sysPureceivedetail.setIupdateby(user.getId());
					sysPureceivedetail.setDupdatetime(nowDate);
					sysPureceivedetail.setCupdatename(user.getName());
				});
				scanCodeReceiveDetailService.batchUpdate(updateModelList, updateModelList.size());
			}

			//获取待删除数据 执行删除
			if (jBoltTable.deleteIsNotBlank()) {
				scanCodeReceiveDetailService.deleteByIds(jBoltTable.getDelete());
			}

			return true;
		});
		return SUCCESS.set("autoid",headerId.get());
	}


	//可编辑表格提交-新增数据
	private void saveTableSubmitDatas(JBoltTable jBoltTable,SysPureceive sysotherin,Long veniAutoId){
		List<Record> list = jBoltTable.getSaveRecordList();
		if(CollUtil.isEmpty(list)) return;
		ArrayList<SysPureceivedetail> sysdetaillist = new ArrayList<>();
		Date now = new Date();
		for (int i=0;i<list.size();i++) {
			SysPureceivedetail sysPureceivedetail = new SysPureceivedetail();
			Record row = list.get(i);
			row.set("MasID", sysotherin.getAutoID());
			row.set("AutoID", JBoltSnowflakeKit.me.nextId());
			row.set("CreateDate", now);
			row.set("ModifyDate", now);
			if(null == list.get(i).get("isinitial") || "".equals(list.get(i).get("isinitial"))){
				row.set("IsInitial", false);
				sysPureceivedetail.setIsInitial("0");
			}else {
				//推送初物 PL_RcvDocQcFormM 来料
				this.insertRcvDocQcFormM(row,sysotherin,veniAutoId);
				sysPureceivedetail.setIsInitial("1");
			}
			sysPureceivedetail.setMasID(sysotherin.getAutoID());
			sysPureceivedetail.setSourceBillType(row.getStr("sourcebilltype"));
			sysPureceivedetail.setSourceBillNo(row.getStr("sourcebillno"));
			sysPureceivedetail.setSourceBillNoRow(row.getStr("sourcebillnorow"));
			sysPureceivedetail.setSourceBillDid(row.getStr("sourcebilldid"));
			sysPureceivedetail.setSourceBillID(row.getStr("sourcebilldid"));
//			sysPureceivedetail.setRowNo(Integer.valueOf(row.getStr("rowno")));
			sysPureceivedetail.setWhcode(row.getStr("whcode"));
			sysPureceivedetail.setQty(new BigDecimal(row.get("qty").toString()));
			sysPureceivedetail.setBarcode(row.get("barcode"));
			sysPureceivedetail.setDcreatetime(now);
			sysPureceivedetail.setDupdatetime(now);

			sysdetaillist.add(sysPureceivedetail);

		}
		scanCodeReceiveDetailService.batchSave(sysdetaillist);
	}
	//可编辑表格提交-修改数据
	private void updateTableSubmitDatas(JBoltTable jBoltTable,SysPureceive sysotherin,Long veniAutoId){
		List<Record> list = jBoltTable.getUpdateRecordList();
		if(CollUtil.isEmpty(list)) return;
		ArrayList<SysPureceivedetail> sysdetaillist = new ArrayList<>();
		Date now = new Date();
		for(int i = 0;i < list.size(); i++){
			SysPureceivedetail sysPureceivedetail = new SysPureceivedetail();
			Record row = list.get(i);
			row.set("ModifyDate", now);

			if(null == list.get(i).get("isinitial") || "".equals(list.get(i).get("isinitial"))){
				row.set("IsInitial", false);
			}else {
				//推送初物 PL_RcvDocQcFormM 来料
				this.insertRcvDocQcFormM(row,sysotherin,veniAutoId);
				sysPureceivedetail.setIsInitial("1");
			}
			sysPureceivedetail.setMasID(sysotherin.getAutoID());
			sysPureceivedetail.setSourceBillType(row.getStr("sourcebilltype"));
			sysPureceivedetail.setSourceBillNo(row.getStr("sourcebillno"));
			sysPureceivedetail.setSourceBillNoRow(row.getStr("sourcebillnorow"));
			sysPureceivedetail.setSourceBillDid(row.getStr("sourcebilldid"));
			sysPureceivedetail.setSourceBillID(row.getStr("sourcebilldid"));
//			sysPureceivedetail.setRowNo(Integer.valueOf(row.getStr("rowno")));
			sysPureceivedetail.setWhcode(row.getStr("whcode"));
			sysPureceivedetail.setQty(new BigDecimal(row.get("qty").toString()));
			sysPureceivedetail.setBarcode(row.get("barcode"));
			sysPureceivedetail.setDcreatetime(now);
			sysPureceivedetail.setDupdatetime(now);

			sysdetaillist.add(sysPureceivedetail);

		}
		scanCodeReceiveDetailService.batchUpdate(sysdetaillist);
	}
	//可编辑表格提交-删除数据
	private void deleteTableSubmitDatas(JBoltTable jBoltTable){
		Object[] ids = jBoltTable.getDelete();
		if(ArrayUtil.isEmpty(ids)) return;
		scanCodeReceiveDetailService.deleteByIds(ids);
	}

	/**
	 * 后台管理数据查询
	 * @return
	 */
	public List<Record> getVenCodeDatas(Kv kv) {
		List<Record> records = dbTemplate("scancodereceive.venCode", kv).find();
		return records;
	}


	/**
	 * 后台管理数据查询
	 * @return
	 */
	public List<Record> getWhcodeDatas(Kv kv) {
		return dbTemplate(u8SourceConfigName(), "scancodereceive.Whcode", kv).find();
	}

	/**
	 * 时间转字符串
	 */
	public String dateToString(Date date) {
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf3.format(date);
	}
	/**
	 * 字符串转时间
	 */
	public Date stringToDate(String str) throws ParseException {
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf3.parse(str);
	}


	/**
	 * 推送 PL_RcvDocQcFormM ;//来料检验
	 */
	public void insertRcvDocQcFormM(Record row,SysPureceive sys,Long veniAutoId){
		Date date = new Date();
		RcvDocQcFormM rcvDocQcFormM = new RcvDocQcFormM();
		rcvDocQcFormM.setIOrgId(getOrgId());
		rcvDocQcFormM.setCOrgCode(getOrgCode());
		rcvDocQcFormM.setCOrgName(getOrgName());
		if(null == row.getStr("sourcebillno") || "".equals(row.getStr("sourcebillno"))){
			rcvDocQcFormM.setCRcvDocQcFormNo("test");
		}else {
			rcvDocQcFormM.setCRcvDocQcFormNo(row.getStr("sourcebillno"));
		}
		//质检表格ID
		rcvDocQcFormM.setIQcFormId(1L);
		rcvDocQcFormM.setIRcvDocId(Long.valueOf(sys.getAutoID()));
		rcvDocQcFormM.setCRcvDocNo(sys.getBillNo());
		if(null == row.getStr("iinventoryid") || "".equals(row.getStr("iinventoryid"))){
			rcvDocQcFormM.setIInventoryId(100L);
		}else {
			rcvDocQcFormM.setIInventoryId(Long.valueOf(row.getStr("iinventoryid").trim()));
		}
		rcvDocQcFormM.setIVendorId(veniAutoId);
		rcvDocQcFormM.setDRcvDate(sys.getDcreatetime());
		rcvDocQcFormM.setIQty(Double.valueOf(String.valueOf(row.getStr("qty").trim())).intValue());
		//批次号
		rcvDocQcFormM.setCBatchNo("1111111Test");
		rcvDocQcFormM.setIStatus(0);
		rcvDocQcFormM.setIsCpkSigned(false);
		rcvDocQcFormM.setIMask(2);
		rcvDocQcFormM.setIsCpkSigned(false);
		rcvDocQcFormM.setICreateBy(JBoltUserKit.getUser().getId());
		rcvDocQcFormM.setCCreateName(JBoltUserKit.getUser().getName());
		rcvDocQcFormM.setDCreateTime(date);
		rcvDocQcFormM.setIUpdateBy(JBoltUserKit.getUser().getId());
		rcvDocQcFormM.setCUpdateName(JBoltUserKit.getUser().getName());
		rcvDocQcFormM.setDUpdateTime(date);
		rcvDocQcFormM.setIsDeleted(false);
		rcvdocqcformmservice.save(rcvDocQcFormM);
	}

	/**
	 * 获取双单位条码数据
	 * @return
	 */
	public Record getBarcodeDatas(Kv kv) {

		Record firstRecord = dbTemplate("scancodereceive.findAfterInvData",kv).findFirst();

		if (firstRecord!=null){
			/*String aftercode = firstRecord.getStr("aftercode");
			String orgCode = getOrgCode();
			kv.set("keywords",aftercode);
			kv.set("orgCode",orgCode);
			kv.set("limit",1);
			return dbTemplate("scancodereceive.getResource",kv).findFirst();*/
			return firstRecord;
		} else {
		    ValidationUtils.error( "未查找到该物料的双单位，请先维护物料的形态对照表");
        }
		return null;
	}

	/**
	 * 根据条件获取资源
	 * @param kv
	 * @return
	 */
	public List<Record> getResource(Kv kv){
		kv.setIfNotNull("orgCode", getOrgCode());
		List<Record> list = dbTemplate("scancodereceive.getResource", kv).find();
		ValidationUtils.isTrue(list.size()!=0, "找不到该现品票信息");
		return list;
	}

	/**
	 * 获取库区
	 * @param kv
	 * @return
	 */
	public List<Record> findWhArea(Kv kv){
		kv.setIfNotNull("orgCode", getOrgCode());
		return dbTemplate("scancodereceive.findWhArea",kv).find();
	}

    /**
     * 推送 PL_RcvDocQcFormM ;//来料检验
     */
    public List<RcvDocQcFormM> insertRcvDocQcFormM(List<Record> list, SysPureceive sysPureceive, User user) {
        Date date = new Date();

        List<RcvDocQcFormM> rcvList = new ArrayList<>();
        list.forEach(record -> {
            RcvDocQcFormM rcvDocQcFormM = new RcvDocQcFormM();

            rcvDocQcFormM.setIOrgId(getOrgId());
            rcvDocQcFormM.setCOrgCode(getOrgCode());
            rcvDocQcFormM.setCOrgName(getOrgName());
            if (StrUtil.isBlank(record.getStr("sourcebillno"))) {
                rcvDocQcFormM.setCRcvDocQcFormNo("test");
            } else {
                rcvDocQcFormM.setCRcvDocQcFormNo(record.getStr("sourcebillno"));
            }

            // 质检表格ID
            Long qcIAutoId = record.getLong("qciautoid");
            if (isOk(qcIAutoId)){
                rcvDocQcFormM.setIQcFormId(qcIAutoId);
                rcvDocQcFormM.setIStatus(1);
                //设变号
                rcvDocQcFormM.setCDcNo(record.get("cdccode"));
            } else {
                rcvDocQcFormM.setIStatus(0);
            }

            rcvDocQcFormM.setIRcvDocId(Long.valueOf(sysPureceive.getAutoID()));
            rcvDocQcFormM.setCRcvDocNo(sysPureceive.getBillNo());
            rcvDocQcFormM.setIInventoryId(record.getLong("iinventoryid"));
            rcvDocQcFormM.setIVendorId(record.getLong("veniautoid"));
            rcvDocQcFormM.setDRcvDate(sysPureceive.getDcreatetime());
            rcvDocQcFormM.setIQty(Double.valueOf(record.getStr("qty").trim()).intValue());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            // 批次号(收货日期)
            rcvDocQcFormM.setCBatchNo(formatter.format(sysPureceive.getDcreatetime()));
            rcvDocQcFormM.setIsCpkSigned(false);

            String isIQc1 = record.getStr("isiqc1");
            Integer imask = 2;
            if (isOk(isIQc1) && "1".equals(isIQc1)){
                imask = 1;
            }

            rcvDocQcFormM.setIMask(imask);
            rcvDocQcFormM.setIsCpkSigned(false);
            rcvDocQcFormM.setICreateBy(user.getId());
            rcvDocQcFormM.setCCreateName(user.getName());
            rcvDocQcFormM.setDCreateTime(date);
            rcvDocQcFormM.setIUpdateBy(user.getId());
            rcvDocQcFormM.setCUpdateName(user.getName());
            rcvDocQcFormM.setDUpdateTime(date);
            rcvDocQcFormM.setIsDeleted(false);
            rcvList.add(rcvDocQcFormM);
        });

        return rcvList;
    }

//	--------------------------------------------
//	实现接口方法

	@Override
	public String postApproveFunc(long formAutoId, boolean isWithinBatch) {

        Date now = new Date();
        User user = JBoltUserKit.getUser();
        SysPureceive sysPureceive = findById(formAutoId);
        //查从表数据
        List<Record> list = scanCodeReceiveDetailService.findListByMasId(formAutoId);
        Set<String> itemSet = new HashSet<>();
        Map<String, Record> rcMap = new HashMap<>();
        List<Record> puiList = new ArrayList<>();

        for (Record l : list) {
            String isIQc1 = l.getStr("isiqc1");
            String isInitial = l.getStr("isinitial");
            String cinvcode = l.getStr("invcode");
            //判断存货开关是否打开 或者 收料单行是否开启初物,开则推来料检验单
            if ((isOk(isIQc1) && "1".equals(isIQc1)) || isOk(isInitial) && "1".equals(isInitial)) {
                if (itemSet.contains(cinvcode)) {
                    BigDecimal qty = l.getBigDecimal("qty");
                    Record record = rcMap.get(cinvcode);
                    BigDecimal decimal = record.getBigDecimal("qty");
                    BigDecimal add = decimal.add(qty);
                    record.set("qty", add);
                } else {
                    rcMap.put(cinvcode, l);
                    itemSet.add(cinvcode);
                }
            } else {
                puiList.add(l);
            }
        }

        tx(() -> {
            if (rcMap.size() > 0) {

                List<Record> rcvList = new ArrayList<>();
                rcMap.forEach((k, v) -> {
                    rcvList.add(v);
                });
                List<RcvDocQcFormM> rcvDocQcFormMS = insertRcvDocQcFormM(rcvList, sysPureceive, user);
                rcvdocqcformmservice.batchSave(rcvDocQcFormMS, rcvDocQcFormMS.size());
            }

            if (puiList.size() > 0) {

				SysPureceivedetail first = scanCodeReceiveDetailService.findFirst("select * from " +
						"T_Sys_PUReceiveDetail where MasID = '" + sysPureceive.getAutoID() + "' and isDeleted = '0' " +
						"and Barcode is not null");

				Kv para = new Kv();
				para.set("barcode",first.getBarcode());
				Record resource = dbTemplate("scancodereceive.getOrder", para).findFirst();

				SysPuinstore sysPuinstore = new SysPuinstore();

                sysPuinstore.setBillNo(BillNoUtils.genCode(getOrgCode(),syspuinstoreservice.table()));
                sysPuinstore.setBillType(sysPureceive.getBillType());
                sysPuinstore.setBillDate(DateUtil.formatDate(now));
                sysPuinstore.setOrganizeCode(getOrgCode());
                sysPuinstore.setSourceBillNo(sysPureceive.getBillNo());
                sysPuinstore.setSourceBillID(sysPureceive.getAutoID());
                sysPuinstore.setVenCode(sysPureceive.getVenCode());
                sysPuinstore.setCCreateName(user.getName());
                sysPuinstore.setDCreateTime(now);
                sysPuinstore.setCUpdateName(user.getName());
                sysPuinstore.setDUpdateTime(now);
                sysPuinstore.setWhCode(sysPureceive.getWhCode());
                sysPuinstore.setWhName(sysPureceive.getWhName());
                sysPuinstore.setIAuditStatus(0);
                sysPuinstore.setICreateBy(sysPureceive.getIcreateby());
                sysPuinstore.setDCreateTime(new Date());
                sysPuinstore.setIUpdateBy(sysPureceive.getIupdateby());
                sysPuinstore.setDUpdateTime(new Date());
                sysPuinstore.setIsDeleted(false);
                sysPuinstore.setDeptCode(sysPureceive.getDeptCode());
                sysPuinstore.setIBusType(1);
                sysPuinstore.setRdCode(sysPureceive.getRdCode());
				sysPuinstore.setBillType(resource.getStr("ipurchasetypeid"));
				sysPuinstore.setDeptCode(resource.getStr("cdepcode"));
				sysPuinstore.setIBusType(Integer.valueOf(resource.getStr("orderibustype")));
				sysPuinstore.setRdCode(resource.getStr("scrdcode"));
                sysPuinstore.save();

                String autoID = sysPuinstore.getAutoID();
                String whCode = sysPuinstore.getWhCode();

                List<SysPuinstoredetail> pureceivedetailList = new ArrayList<>();

				for (int i = 0; i < puiList.size(); i++) {
					Record record = puiList.get(i);

                    //往采购订单入库表插入信息
                    SysPuinstoredetail sysPuinstoredetail = new SysPuinstoredetail();
                    sysPuinstoredetail.setMasID(autoID);
                    sysPuinstoredetail.setWhcode(whCode);
                    sysPuinstoredetail.setSourceBillType(record.getStr("sourcebilltype"));
                    sysPuinstoredetail.setSourceBillNo(record.getStr("sourcebillno"));
                    sysPuinstoredetail.setSourceBillNoRow(record.getStr("sourcebillno") + "-" + record.get("rowno"));
                    sysPuinstoredetail.setSourceBillDid(record.getStr("sourcebilldid"));
                    sysPuinstoredetail.setSourceBillID(record.getStr("sourcebillid"));
                    sysPuinstoredetail.setRowNo(record.get("rowno"));
                    sysPuinstoredetail.setPosCode(record.getStr("poscode"));
                    sysPuinstoredetail.setQty(record.getBigDecimal("qty"));
                    sysPuinstoredetail.setTrackType(record.getStr("tracktype"));
                    sysPuinstoredetail.setCCreateName(user.getUsername());
                    sysPuinstoredetail.setDCreateTime(now);
                    sysPuinstoredetail.setBarCode(record.getStr("barcode"));
                    sysPuinstoredetail.setPuUnitCode(record.getStr("cuomcode"));
                    sysPuinstoredetail.setPuUnitName(record.getStr("cuomname"));
                    sysPuinstoredetail.setIsDeleted(false);
                    sysPuinstoredetail.setInvcode(record.getStr("invcode"));
                    pureceivedetailList.add(sysPuinstoredetail);

                }
				syspuinstoredetailservice.batchSave(pureceivedetailList);
            }

            return true;
        });
		return null;
	}

    @Override
	public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
		return null;
	}

	@Override
	public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {

		SysPureceive sysPureceive = findById(formAutoId);
        String autoID = sysPureceive.getAutoID();
        SysPuinstore sysPuinstore = syspuinstoreservice.findFirst("select * from T_Sys_PUInStore where SourceBillID =" +
                " '" + autoID + "' and isDeleted = '0'");
        if (isOk(sysPuinstore)){

            if (sysPuinstore.getIAuditStatus() > 1){
                return "已推入库单且入库单已审核/批，不予反审";
            } else {
                syspuinstoreservice.deleteByAutoId(Long.parseLong(sysPuinstore.getAutoID()));
            }

        }

        return null;
	}

	@Override
	public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
		return null;
	}

	@Override
	public String preSubmitFunc(long formAutoId) {
        SysPureceive byId = findById(formAutoId);
        System.out.println("byId===>"+byId);
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

        SysPureceive sysPureceive = findById(formAutoId);
        String autoID = sysPureceive.getAutoID();
        SysPuinstore sysPuinstore = syspuinstoreservice.findFirst("select * from T_Sys_PUInStore where SourceBillID =" +
                " '" + autoID + "' and isDeleted = '0'");
        if (isOk(sysPuinstore)){

            if (sysPuinstore.getIAuditStatus() > 1){
                return "已推入库单且入库单已审核/批，不予反审";
            } else {
                syspuinstoreservice.deleteByAutoId(Long.parseLong(sysPuinstore.getAutoID()));
            }

        }

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
