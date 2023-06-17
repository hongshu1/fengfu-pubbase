package cn.rjtech.admin.otherdeliverylist;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.json.JSONObject;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.SystemLog;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.admin.otheroutdetail.OtherOutDetailService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.HttpApiUtils;
import cn.smallbun.screw.core.util.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.json.JSONArray;

import java.util.*;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 出库管理-其他出库单列表 Service
 * @ClassName: OtherOutService
 * @author: RJ
 * @date: 2023-05-17 09:35
 */
public class OtherOutDeliveryService extends BaseService<OtherOut> {

	private final OtherOut dao = new OtherOut().dao();

	@Inject
	private PersonService personservice;
	@Inject
	private FormApprovalService formApprovalService;
	@Inject
	private OtherOutDetailService otherOutDetailService;

	@Override
	protected OtherOut dao() {
		return dao;
	}


	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("otherdeliverylist.paginateAdminDatas",kv).paginate(pageNumber, pageSize);
	}

	/**
	 * 其他出库单列表 明细
	 * @param kv
	 * @return
	 */
	public List<Record> getOtherOutLines(Kv kv){
		return dbTemplate("otherdeliverylist.getOtherOutLines",kv).find();

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

//				otherOut.getStatus()
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

		final String[] AutoIDs = {null};
		tx(()->{
			String headerId = null;
			// 获取Form对应的数据
			if (jBoltTable.formIsNotBlank()) {
				OtherOut otherOut = jBoltTable.getFormModel(OtherOut.class,"otherOut");

				//	行数据为空 不保存
				if ("save".equals(revokeVal)) {
					if (otherOut.getAutoID() == null && !jBoltTable.saveIsNotBlank() && !jBoltTable.updateIsNotBlank() && !jBoltTable.deleteIsNotBlank()) {
						ValidationUtils.error( "请先添加行数据！");
					}
				}

				if ("submit".equals(revokeVal) && otherOut.getAutoID() == null) {
					ValidationUtils.error( "请保存后提交审核！！！");
				}


				if (otherOut.getAutoID() == null && "save".equals(revokeVal)) {
//					保存
//					订单状态：1=已保存，2=待审核，3=已审核
					otherOut.setIAuditStatus(param);

					otherOut.setICreateBy(userId);
					otherOut.setDCreateTime(nowDate);
					otherOut.setCCreateName(userName);
					otherOut.setOrganizeCode(OrgCode);
					otherOut.setIUpdateBy(userId);
					otherOut.setDupdateTime(nowDate);
					otherOut.setCUpdateName(userName);
					otherOut.setIsDeleted(false);
					otherOut.setType("OtherOut");
					save(otherOut);
					headerId = otherOut.getAutoID();
				}else {
					if ( param == 1 ){
						otherOut.setAuditDate(nowDate);
						otherOut.setAuditPerson(userName);
					}
					otherOut.setIAuditStatus(param);
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
					//创建人
					otherOutDetail.setIcreateby(userId);
					otherOutDetail.setDcreatetime(nowDate);
					otherOutDetail.setCcreatename(userName);
					//更新人
					otherOutDetail.setIupdateby(userId);
					otherOutDetail.setDupdatetime(nowDate);
					otherOutDetail.setCupdatename(userName);

					otherOutDetail.setMasID(finalHeaderId);
				});
				otherOutDetailService.batchSave(lines);
			}
			// 获取待更新数据 执行更新
			if (jBoltTable.updateIsNotBlank()) {
				List<OtherOutDetail> lines = jBoltTable.getUpdateModelList(OtherOutDetail.class);

				lines.forEach(materialsOutDetail -> {
					//更新人
					materialsOutDetail.setIupdateby(userId);
					materialsOutDetail.setDupdatetime(nowDate);
					materialsOutDetail.setCupdatename(userName);
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

	public List<Record> otherOutBarcodeDatas(String q, String orgCode) {
		return dbTemplate("otherdeliverylist.otherOutBarcodeDatas",Kv.by("q", q).set("orgCode",orgCode)).find();
	}


	/**
	 * 详情页提审
	 */
	public Ret submit(Long iautoid) {
		tx(() -> {
			Ret ret = formApprovalService.submit(table(), iautoid, primaryKey(),"T_Sys_OtherOut");
			ValidationUtils.isTrue(ret.isOk(), ret.getStr("msg"));

			// 处理其他业务
			OtherOut otherOut = findById(iautoid);
			otherOut.setIAuditStatus(1);
			ValidationUtils.isTrue(otherOut.update(),JBoltMsg.FAIL);
			return true;
		});
		return SUCCESS;
	}

	/**
	 * 详情页审核
	 */
	public Ret approve(String ids) {
		tx(() -> {
			boolean success = false;
			String userName = JBoltUserKit.getUserName();
			Date nowDate = new Date();
			OtherOut otherOut = superFindById(ids);
			//审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
			if (otherOut.getIAuditStatus() != 1) {
				ValidationUtils.error("订单："+otherOut.getBillNo()+"状态不支持审核操作！");
			}
			//订单状态：3. 已审核
			otherOut.setIAuditStatus(2);
			otherOut.setAuditDate(nowDate);
			otherOut.setAuditPerson(userName);
			Ret ret = this.pushU8(ids);
			success= otherOut.update();

			return true;
		});

		return SUCCESS;
	}

	/**
	 * 详情页审核不通过
	 */
	public Ret reject(Long ids) {
		tx(() -> {
			OtherOut otherOut = superFindById(ids);
			if (otherOut.getIAuditStatus() != 2) {
				ValidationUtils.error("订单："+otherOut.getBillNo()+"状态不支持反审批操作！");
			}
			otherOut.setIAuditStatus(1);
			otherOut.update();

			return true;
		});
		return SUCCESS;
	}


	/**
	 * 批量审核
	 * @param ids
	 * @return
	 */
	public Ret batchApprove(String ids, Integer mark) {
		tx(() -> {
			boolean success = false;
			String userName = JBoltUserKit.getUserName();
			Date nowDate = new Date();
			List<OtherOut> listByIds = getListByIds(ids);
			if (listByIds.size() > 0) {
				for (OtherOut otherOut : listByIds) {
					//审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
					if (otherOut.getIAuditStatus() != 1) {
						ValidationUtils.error("订单：" + otherOut.getBillNo() + "状态不支持审核操作！");
					}
					//订单状态：3. 已审核
					otherOut.setIAuditStatus(2);
					otherOut.setAuditDate(nowDate);
					otherOut.setAuditPerson(userName);
					Ret ret = this.pushU8(ids);
					success = otherOut.update();
				}
			}
			return true;
		});
		return SUCCESS;
	}



	/**
	 * 批量反审核
	 * @param ids
	 * @return
	 */
	public Ret batchReverseApprove(String ids) {
		tx(() -> {
		//TODO数据同步暂未开发 现只修改状态
		for (OtherOut otherOut :  getListByIds(ids)) {
//			//审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
			if (otherOut.getIAuditStatus() != 2) {
				ValidationUtils.error("订单："+otherOut.getBillNo()+"状态不支持反审批操作！");
			}
			otherOut.setIAuditStatus(1);
			otherOut.update();
		}

		return true;
	});
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
		OtherOut otherOut = findById(iAutoId);
		otherOut.setIAuditStatus(0);
		otherOut.setAuditDate(null);
		otherOut.setAuditPerson(null);
		boolean result = otherOut.update();
		return ret(result);
	}


	public Ret pushU8(String ids) {
		List<Record> list = dbTemplate("otherdeliverylist.pushU8List", Kv.by("autoid", ids)).find();

		if (list.size() > 0) {
//          接口参数
			User user = JBoltUserKit.getUser();
			String url = "http://localhost:8081/web/erp/common/vouchProcessDynamicSubmit";
			String userCode = user.getUsername();
			Long userId = user.getId();
			String type = "OtherOut";
			Record record1 = list.get(0);
			String organizecode = record1.get("organizecode");
			String nowDate = DateUtil.format(new Date(), "yyyy-MM-dd");

			JSONObject preAllocate = new JSONObject();
			preAllocate.set("CreatePerson",userId);
			preAllocate.set("CreatePersonName",user.getName());
			preAllocate.set("loginDate", nowDate);
			preAllocate.set("tag",type);
			preAllocate.set("type",type);

			JSONArray mainData = new JSONArray();
			list.forEach(record -> {
				JSONObject jsonObject = new JSONObject();
				jsonObject.set("invstd","");
				jsonObject.set("owhcode",record.get("whcode"));
				jsonObject.set("invname","");
				jsonObject.set("index","1");
				jsonObject.set("vt_id","");
				jsonObject.set("billDate",record.get("billDate"));
				jsonObject.set("lineColor","");
				jsonObject.set("invcode",record.get("invcode"));
				jsonObject.set("userCode",userCode);
				jsonObject.set("iswhpos","true");
				jsonObject.set("ispack","0");
				jsonObject.set("organizeCode",organizecode);
				jsonObject.set("qty",record.get("qty"));
				jsonObject.set("Tag","OtherOut");
				jsonObject.set("oposcode","");
				jsonObject.set("barcode",record.get("barcode"));
				jsonObject.set("ORdName",record.get("rdname"));
				jsonObject.set("ORdCode",record.get("rdcode"));
				jsonObject.set("ODeptCode",record.get("cdepcode"));
				jsonObject.set("ODeptName",record.get("cdepname"));
				jsonObject.set("ORdType",record.get("type"));
				mainData.put(jsonObject);

			});

//            参数装载
			Map<String, Object> data = new HashMap<>();
			data.put("organizeCode", organizecode);
			data.put("userCode", userCode);
			data.put("token","");
			data.put("PreAllocate", preAllocate);
			data.put("MainData", mainData);

//            请求头
			Map<String, String> header = new HashMap<>(5);
			header.put("Content-Type", "application/json");


			SystemLog systemLog = new SystemLog();
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("<span class='text-danger'>[其他出库列表推U8操作]</span>");
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
					com.alibaba.fastjson.JSONObject parseObject = com.alibaba.fastjson.JSONObject.parseObject(post);
					stringBuilder.append("<span class='text-primary'>[成功返回参数=").append(post).append("]</span>");
					String code = parseObject.getString("code");
					String msg = parseObject.getString("message");

					LOG.info("data====" + data);
					if ("200".equals(code)) {
						String[] s = msg.split(",");
						String bill = s[0];
						LOG.info("s===>" + bill);
						LOG.info("data====" + data);
						int update = update("update T_Sys_OtherOut set IAuditStatus ='2' where AutoID IN("+ids+")" );

						return update == 1 ? ret.setOk().set("msg", msg) : ret.setFail().set("msg",
								"推送数据失败," + "失败原因" + msg);
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


	//推送u8数据接口
	public Ret pushU8S(OtherOut otherout, List<OtherOutDetail> otheroutdetail) {
		if(!CollectionUtils.isNotEmpty(otheroutdetail)){
			return Ret.fail("数据不能为空");
		}

		User user = JBoltUserKit.getUser();
		JSONObject data = new JSONObject();

		data.set("userCode",user.getUsername());
		data.set("organizeCode",this.getdeptid());
		data.set("token","");

		JSONObject preallocate = new JSONObject();


		preallocate.set("userCode",user.getUsername());
		preallocate.set("organizeCode",this.getdeptid());
		preallocate.set("CreatePerson",user.getId());
		preallocate.set("CreatePersonName",user.getName());
		preallocate.set("loginDate", DateUtil.format(new Date(), "yyyy-MM-dd"));
		preallocate.set("tag","OtherOut");
		preallocate.set("type","OtherOut");

		data.put("PreAllocate",preallocate);

		ArrayList<Object> maindata = new ArrayList<>();
		otheroutdetail.stream().forEach(s -> {
			JSONObject jsonObject = new JSONObject();
			jsonObject.set("invstd","");
			jsonObject.set("owhcode",otherout.getWhcode());
			jsonObject.set("invname","");
			jsonObject.set("index","1");
			jsonObject.set("vt_id","");
			jsonObject.set("billDate",otherout.getBillDate());
			jsonObject.set("lineColor","");
			jsonObject.set("invcode",s.getInvCode());
			jsonObject.set("userCode",user.getUsername());
			jsonObject.set("iswhpos","true");
			jsonObject.set("ispack","0");
			jsonObject.set("organizeCode",otherout.getOrganizeCode());
			jsonObject.set("qty",s.getQty());
			jsonObject.set("Tag","OtherOut");
			jsonObject.set("oposcode","");
			jsonObject.set("barcode",s.getBarcode());
			jsonObject.set("ORdName","其他出库");
			jsonObject.set("ORdCode",otherout.getRdCode());
			jsonObject.set("ODeptCode",otherout.getDeptCode());
			jsonObject.set("ODeptName","");
			jsonObject.set("ORdType",otherout.getType());

			maindata.add(jsonObject);
		});
		data.set("MainData",maindata);

		//            请求头
		Map<String, String> header = new HashMap<>(5);
		header.put("Content-Type", "application/json");
		String url = "http://localhost:8081/web/erp/common/vouchProcessDynamicSubmit";

		try {
			String post = HttpApiUtils.httpHutoolPost(url, data, header);
			com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(post);
			if (isOk(post)) {
				if ("201".equals(jsonObject.getString("code"))) {
					System.out.println(jsonObject);
					return Ret.ok("提交成功");
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return Ret.fail("上传u8失败");
	}

	//通过当前登录人名称获取部门id
	public String getdeptid(){
		String dept = "001";
		User user = JBoltUserKit.getUser();
		Person person = personservice.findFirstByUserId(user.getId());
		if(null != person && "".equals(person)){
			dept = person.getCOrgCode();
		}
		return dept;
	}

}