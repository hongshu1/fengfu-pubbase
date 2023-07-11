package cn.rjtech.admin.transvouch;

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
import cn.rjtech.admin.transvouchdetail.TransVouchDetailService;
import cn.rjtech.model.momdata.TransVouch;
import cn.rjtech.model.momdata.TransVouchDetail;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.HttpApiUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.*;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 出库管理-调拨单列表 Service
 * @ClassName: TransVouchService
 * @author: RJ
 * @date: 2023-05-11 14:54
 */
public class TransVouchService extends BaseService<TransVouch> implements IApprovalService{

	private final TransVouch dao = new TransVouch().dao();

	@Override
	protected TransVouch dao() {
		return dao;
	}

	@Inject
	private TransVouchDetailService transVouchDetailService;

	/**
	 * 后台管理分页查询
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("transvouch.paginateAdminDatas",kv).paginate(pageNumber, pageSize);
	}

	/**
	 * 调拨单列表 明细
	 */
	public List<Record> getTransVouchLines(Kv kv){
		return dbTemplate("transvouch.getTransVouchLines",kv).find();

	}

	/**
	 * 保存
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
	 */
	public Ret deleteByBatchIds(String ids) {
	tx(() -> {
		for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
            TransVouch transVouch = findById(idStr);
			ValidationUtils.notNull(transVouch, JBoltMsg.DATA_NOT_EXIST);
			//软删除
//			transVouch.setIsDeleted(true);
//			transVouch.update();

			// TODO 可能需要补充校验组织账套权限
			// TODO 存在关联使用时，校验是否仍在使用

			Integer[] state = {1,2,3};
            for (Integer integer : state) {
                System.out.println(integer);
                if (integer.equals(transVouch.getIAuditStatus())) {
                    ValidationUtils.error("审核中、已审核的记录不允许删除,修改！！！");
                }
            }
			//删除行数据
			transVouchDetailService.deleteByBatchIds(idStr);
			ValidationUtils.isTrue(transVouch.delete(), JBoltMsg.FAIL);

		}
		return true;
	});
		return SUCCESS;
}

	/**
	 * 删除
	 */
	public Ret delete(Long id) {
		return deleteById(id,true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param transVouch 要删除的model
	 * @param kv 携带额外参数一般用不上
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
	 */
	@Override
	public String checkCanDelete(TransVouch transVouch, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(transVouch, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
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

	/**
	 * 转出仓库
	 */
	public List<Record> warehouse(Kv kv) {
		return dbTemplate("transvouch.warehouse", kv.set("OrgCode",getOrgCode())).find();
	}

	/**
	 * 转入仓库
	 */
	public List<Record> iwarehouse(Kv kv) {
		return dbTemplate("transvouch.iwarehouse", kv.set("OrgCode",getOrgCode())).find();
	}

	public Ret submitByJBoltTables(JBoltTableMulti jboltTableMulti) {
		if (jboltTableMulti == null || jboltTableMulti.isEmpty()) {
			return fail(JBoltMsg.JBOLTTABLE_IS_BLANK);
		}

		// 这里可以循环遍历 保存处理每个表格 也可以 按照name自己get出来单独处理

		JBoltTable jBoltTable = jboltTableMulti.getJBoltTable("table2");
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

		final String[] AutoIDs = {null};
		tx(()->{
			String headerId = null;
			// 获取Form对应的数据
			if (jBoltTable.formIsNotBlank()) {
				TransVouch transVouch = jBoltTable.getFormModel(TransVouch.class,"transVouch");

				//	行数据为空 不保存
				if (transVouch.getAutoID() == null && !jBoltTable.saveIsNotBlank() && !jBoltTable.updateIsNotBlank() && !jBoltTable.deleteIsNotBlank()) {
					ValidationUtils.error( "请先添加行数据！");
				}

				if (transVouch.getAutoID() == null) {
//					审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
					transVouch.setIAuditStatus(0);
					transVouch.setOrganizeCode(OrgCode);
					transVouch.setBillNo(billNo);
					transVouch.setBillType("TransVouch");
					transVouch.setIsDeleted(false);

					//创建人
					transVouch.setIcreateBy(userId);
					transVouch.setDcreateTime(nowDate);
					transVouch.setCcreateName(userName);
					//更新人
					transVouch.setIupdateBy(userId);
					transVouch.setDupdateTime(nowDate);
					transVouch.setCupdateName(userName);
					save(transVouch);
					headerId = transVouch.getAutoID();
				} else {
					//更新人
					transVouch.setIupdateBy(userId);
					transVouch.setDupdateTime(nowDate);
					transVouch.setCupdateName(userName);
					update(transVouch);
					headerId = transVouch.getAutoID();
				}
				AutoIDs[0] = transVouch.getAutoID();
			}

			// 获取待保存数据 执行保存
			if (jBoltTable.saveIsNotBlank()) {
				List<TransVouchDetail> lines = jBoltTable.getSaveModelList(TransVouchDetail.class);

				String finalHeaderId = headerId;
				lines.forEach(transVouchDetail -> {
					transVouchDetail.setMasID(Long.valueOf(finalHeaderId));
					//创建人
					transVouchDetail.setIcreateby(userId);
					transVouchDetail.setDcreatetime(nowDate);
					transVouchDetail.setCcreatename(userName);
					//更新人
					transVouchDetail.setIupdateby(userId);
					transVouchDetail.setDupdatetime(nowDate);
					transVouchDetail.setCupdatename(userName);
				});
				transVouchDetailService.batchSave(lines);
			}
			// 获取待更新数据 执行更新
			if (jBoltTable.updateIsNotBlank()) {
				List<TransVouchDetail> lines = jBoltTable.getUpdateModelList(TransVouchDetail.class);
				String finalHeaderId = headerId;
				lines.forEach(transVouchDetail -> {
					//更新人
					transVouchDetail.setIupdateby(userId);
					transVouchDetail.setDupdatetime(nowDate);
					transVouchDetail.setCupdatename(userName);
				});
				transVouchDetailService.batchUpdate(lines);
			}
			// 获取待删除数据 执行删除
			if (jBoltTable.deleteIsNotBlank()) {
				transVouchDetailService.deleteByIds(jBoltTable.getDelete());
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
	public Record barcode(Kv kv) {
        return dbTemplate("materialsout.getBarcodeDatas", kv).findFirst();
	}

	public Ret pushU8(String id) {
		List<Record> list = dbTemplate("transvouch.pushU8List", Kv.by("autoid", id)).find();
		if (list.size() > 0) {
//          接口参数
			User user = JBoltUserKit.getUser();
			String url = "http://localhost:8081/web/erp/common/vouchProcessDynamicSubmit";
			String userCode = user.getUsername();
			Long userId = user.getId();
			String Password = user.getPassword();
			String type = "TransVouch";
			Record record1 = list.get(0);
			String organizecode = record1.get("organizecode");
			String nowDate = DateUtil.format(new Date(), "yyyy-MM-dd");

			JSONObject preAllocate = new JSONObject();
			preAllocate.put("userCode",userCode);
			preAllocate.put("Password",Password);
			preAllocate.put("organizeCode",organizecode);
			preAllocate.put("CreatePerson",userId);
			preAllocate.put("CreatePersonName",user.getName());
			preAllocate.put("loginDate", nowDate);
			preAllocate.put("tag",type);
			preAllocate.put("type",type);

			JSONArray mainData = new JSONArray();
			list.forEach(record -> {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("invstd", record.get("invstd"));
				jsonObject.put("iwhcode", record.get("iwhcode"));
				jsonObject.put("iwhname", record.get("iwhname"));
				jsonObject.put("iposcode", record.get("iposcode"));
				jsonObject.put("iposname", record.get("iposname"));
				jsonObject.put("owhcode", record.get("owhcode"));
				jsonObject.put("owhname", record.get("owhname"));
				jsonObject.put("oposcode", record.get("oposcode"));
				jsonObject.put("invname", record.get("invname"));
				jsonObject.put("vt_id", "");
				jsonObject.put("billDate", record.get("billdate"));
				jsonObject.put("invcode", record.get("invcode"));
				jsonObject.put("userCode", userCode);
				jsonObject.put("ispack", "0");
				jsonObject.put("organizeCode", organizecode);
				jsonObject.put("qty", record.get("qty"));
				jsonObject.put("tag", type);
				jsonObject.put("barcode", record.get("barcode"));
				jsonObject.put("oposname", record.get("oposname"));
				jsonObject.put("ORdType", record.get("ordtype"));
				jsonObject.put("ORdName", record.get("ordname"));
				jsonObject.put("ORdCode", record.get("ordcode"));
				jsonObject.put("ODeptName", record.get("odeptname"));
				jsonObject.put("ODeptCode", record.get("odeptcode"));
				jsonObject.put("IDeptName", record.get("ideptname"));
				jsonObject.put("IDeptCode", record.get("ideptcode"));
				jsonObject.put("IRdName", record.get("irdname"));
				jsonObject.put("IRdCode", record.get("irdcode"));
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
			stringBuilder.append("<span class='text-danger'>[调拨单推U8操作]</span>");
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
//				systemLog.setTitle(stringBuilder.toString());
//				systemLog.save();
			}
			return fail("请求失败");
		} else {
			ValidationUtils.error("查无此单");
		}

		return SUCCESS;
	}

	@Override
	public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
		Long userId = JBoltUserKit.getUserId();
		String userName = JBoltUserKit.getUserName();
		Date nowDate = new Date();
		TransVouch transVouch = findById(formAutoId);
		transVouch.setIAuditBy(userId);
		transVouch.setCAuditName(userName);
		transVouch.setDAuditTime(nowDate);
		String ids = String.valueOf(transVouch.getAutoID());
		this.pushU8(ids);
		transVouch.update();
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
		TransVouch transVouch = findById(formAutoId);
		transVouch.setIAuditBy(null);
		transVouch.setCAuditName(null);
		transVouch.setDAuditTime(null);
		transVouch.update();
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
		Long userId = JBoltUserKit.getUserId();
		String userName = JBoltUserKit.getUserName();
		Date nowDate = new Date();
		/*
		 * List转换String类型
		 */
		if (formAutoIds.size()>0){
			StringBuilder buffer = new StringBuilder();
            for (Long formAutoId : formAutoIds) {
                buffer.append(formAutoId).append(",");
            }
			String ids = buffer.substring(0, buffer.length() - 1);
			List<TransVouch> listByIds = getListByIds(ids);
			if (listByIds.size() > 0) {
				for (TransVouch transVouch : listByIds) {
					//审核人
					transVouch.setIAuditBy(userId);
					transVouch.setCAuditName(userName);
					transVouch.setDAuditTime(nowDate);
//					this.pushU8(ids);
					transVouch.update();
				}
			}
		}
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