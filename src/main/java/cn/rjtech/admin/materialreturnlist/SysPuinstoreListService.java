package cn.rjtech.admin.materialreturnlist;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.SystemLog;
import cn.jbolt.core.model.User;
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
import cn.rjtech.wms.utils.HttpApiUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.*;

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
	 * @param autoId
	 * @return
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
			String whcode = null;
			// 获取Form对应的数据
			if (jBoltTable.formIsNotBlank()) {
				SysPuinstore puinstore = jBoltTable.getFormModel(SysPuinstore.class,"puinstore");
				Record record = jBoltTable.getFormRecord();
				whcode = record.get("whcode");//仓库


//				detailByParam = dbTemplate("syspureceive.purchaseOrderD", kv).findFirst();
//				detailByParam = sysPuinstoreService.findSysPODetailByParam(kv);

				//	行数据为空 不保存
				if ("save".equals(revokeVal)) {
					if (puinstore.getAutoID() == null && !jBoltTable.saveIsNotBlank() && !jBoltTable.updateIsNotBlank() && !jBoltTable.deleteIsNotBlank()) {
						ValidationUtils.error( "请先添加行数据！");
					}
				}

				if ("submit".equals(revokeVal) && puinstore.getAutoID() == null) {
					ValidationUtils.error( "请保存后提交审核！！！");
				}


				if (puinstore.getAutoID() == null && "save".equals(revokeVal)) {
//					保存
//					订单状态：0=待审核，1=未审核，2=已审核. 3=审核不通过
					puinstore.setIAuditStatus(param);
					puinstore.setDCreateTime(nowDate);
					puinstore.setOrganizeCode(OrgCode);
					puinstore.setCCreateName(userName);
					puinstore.setDUpdateTime(nowDate);
					puinstore.setCUpdateName(userName);
					puinstore.setIsDeleted(false);
					save(puinstore);
					headerId = puinstore.getAutoID();
				}else {
					Integer  a = Integer.valueOf(param);
					if ( a  == 2){
						puinstore.setAuditDate(nowDate);
						puinstore.setCAuditName(userName);
					}
					puinstore.setIAuditStatus(param);
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
							BigDecimal a=new BigDecimal(2);
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
							BigDecimal a=new BigDecimal(2);
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
				String finalWhcodes = whcode;
				lines.forEach(materialsOutDetail -> {
					Object qtys = materialsOutDetail.get("qty");
					System.out.println(qtys);
					materialsOutDetail.setMasID(finalHeaderId);
					materialsOutDetail.setWhcode(finalWhcodes);
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
				String finalWhcodes1 = whcode;
				lines.forEach(materialsOutDetail -> {
					materialsOutDetail.setWhcode(finalWhcodes1);
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
				Integer state = Integer.valueOf(puinstore.getIAuditStatus());
				if (state != 1) {
					return warn("订单："+puinstore.getBillNo()+"状态不支持审核操作！");
				}
				//订单状态：2. 已审核
				puinstore.setIAuditStatus(2);
				puinstore.setAuditDate(nowDate);
				puinstore.setCAuditName(userName);
				success= puinstore.update();
				this.pushU8(iAutoId);
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
			Integer state = Integer.valueOf(puinstore.getIAuditStatus());
//			订单状态： 2. 已审批
			if (state != 2) {
				return warn("订单："+puinstore.getBillNo()+"状态不支持反审批操作！");
			}

			//订单状态： 1. 待审批
			puinstore.setIAuditStatus(1);
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
		puinstore.setIAuditStatus(1);
		puinstore.setAuditDate(null);
		puinstore.setCAuditName(null);
		boolean result = puinstore.update();
		return ret(result);
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
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> getmaterialReturnListLines(int pageNumber, int pageSize, Kv kv){
		return dbTemplate("materialreturnlist.getmaterialReturnListLines",kv).paginate(pageNumber, pageSize);

	}

	/**
	 * 整单退货出库单列表 明细

	 * @param kv
	 * @return
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
////		先查询条码是否已添加
		Record first = dbTemplate("materialreturnlist.barcodeDatas", kv).findFirst();
		if(null == first){
			ValidationUtils.isTrue( false,"条码为：" + kv.getStr("barcode") + "采购入库没有此数据！！！");
		}
		Record first2 = dbTemplate("materialreturnlist.barcode", kv).findFirst();
		return first2;
	}



	public Ret pushU8(String id) {
		List<Record> list = dbTemplate("materialreturnlist.pushU8List", Kv.by("autoid", id)).find();

		if (list.size() > 0) {
//          接口参数
			User user = JBoltUserKit.getUser();
			String url = "http://localhost:8081/web/erp/common/vouchProcessDynamicSubmit";
			String userCode = user.getUsername();
			Long userId = user.getId();
			String type = "PUInStore";
			Record record1 = list.get(0);
			String organizecode = record1.get("organizecode");
			String nowDate = DateUtil.format(new Date(), "yyyy-MM-dd");

			JSONObject preAllocate = new JSONObject();
			preAllocate.put("userCode",userCode);
			preAllocate.put("organizeCode",organizecode);
			preAllocate.put("CreatePerson",userId);
			preAllocate.put("CreatePersonName",user.getName());
			preAllocate.put("loginDate", nowDate);
			preAllocate.put("tag",type);
			preAllocate.put("type",type);
			JSONArray mainData = new JSONArray();
			list.forEach(record -> {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("IsWhpos","1");
				jsonObject.put("iwhcode", record.get("iwhcode"));
				jsonObject.put("InvName", record.get("invname"));
				jsonObject.put("VenName", record.get("venname"));
				jsonObject.put("VenCode", record.get("vencode"));
				jsonObject.put("Qty", record.get("qty"));
				jsonObject.put("organizeCode", organizecode);
				jsonObject.put("InvCode", record.get("invcode"));
				jsonObject.put("Num", "0");
				jsonObject.put("index", "1");
				jsonObject.put("PackRate", "0");
				jsonObject.put("ISsurplusqty", "false");
				jsonObject.put("CreatePerson", userCode);
				jsonObject.put("BarCode", record.get("barcode"));
				jsonObject.put("BillNo", record.get("billno"));
				jsonObject.put("BillID", "1000000003");
				jsonObject.put("BillNoRow", "PO23050601-1");
				jsonObject.put("BillDate", record.get("billdate"));
				jsonObject.put("BillDid", "1000000003");
				jsonObject.put("sourceBillNo", record.get("sourcebillno"));
				jsonObject.put("sourceBillDid", record.get("sourcebilldid"));
				jsonObject.put("sourceBillID", record.get("sourcebillid"));
				jsonObject.put("sourceBillType", record.get("sourcebilltype"));
				jsonObject.put("SourceBillNoRow", record.get("sourcebillnorow"));
				jsonObject.put("tag", type);
				jsonObject.put("IcRdCode", record.get("icrdcode"));
				jsonObject.put("iposcode", record.get("iposcode"));
				mainData.add(jsonObject);

			});

//            参数装载
			Map<String, Object> data = new HashMap<>();
			data.put("organizeCode", organizecode);
			data.put("userCode", userCode);
			data.put("PreAllocate", preAllocate);
			data.put("MainData", mainData);

//            请求头
			Map<String, String> header = new HashMap<>(5);
			header.put("Content-Type", "application/json");


			SystemLog systemLog = new SystemLog();
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("<span class='text-danger'>[材料退货列表推U8操作]</span>");
//            stringBuilder.append("<span class='text-primary'>[url="+url+"]</span>");
//            stringBuilder.append("<span class='text-danger'>[参数={"+JSONObject.toJSONString(data)+"}]</span>");
			systemLog.setType(2);
			systemLog.setCreateTime(new Date());
			systemLog.setUserId(JBoltUserKit.getUserId());
			systemLog.setUserName(JBoltUserKit.getUserUserName());
			systemLog.setOpenType(1);
			systemLog.setTargetId(1L);
			systemLog.setTargetType(1);
			Ret ret = new Ret();
			try {
				String post = HttpApiUtils.httpHutoolPost(url, data, header);
				if (isOk(post)) {
					JSONObject parseObject = JSONObject.parseObject(post);
					stringBuilder.append("<span class='text-primary'>[成功返回参数=").append(post).append("]</span>");
					String code = parseObject.getString("code");
					String msg = parseObject.getString("message");

					LOG.info("data====" + data);
					if ("200".equals(code)) {
						String[] s = msg.split(",");
						String bill = s[0];
						LOG.info("s===>" + bill);
						LOG.info("data====" + data);

//						int update = update("update T_Sys_SOReturn set U9BillNo = '" + bill + "', status = '2' where" + " AutoID " + "= " + "'" + id + "'");

//						return update == 1 ? ret.setOk().set("msg", msg) : ret.setFail().set("msg",
//								"推送数据失败," + "失败原因" + msg);
					}
					return ret.setFail().set("msg", "推送数据失败," + "失败原因" + msg);
				} else {
					stringBuilder.append("<span class='text-primary'>[失败返回参数=").append(post).append("]</span>");
					return fail("请求失败");
				}
			} catch (Exception e) {
				stringBuilder.append("<span class='text-primary'>[失败异常=").append(e.getMessage()).append("]</span>");
				e.printStackTrace();
			} finally {
				systemLog.setTitle(stringBuilder.toString());
				systemLog.save();
			}
			return fail("请求失败");
		} else {
			ValidationUtils.error("查无此单");
		}

		return SUCCESS;
	}

}