package cn.rjtech.admin.materialsout;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.json.JSONObject;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.SystemLog;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.admin.materialsoutdetail.MaterialsOutDetailService;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 出库管理-材料出库单列表 Service
 * @ClassName: MaterialsOutService
 * @author: RJ
 * @date: 2023-05-13 16:16
 */
public class MaterialsOutService extends BaseService<MaterialsOut> {

	private final MaterialsOut dao = new MaterialsOut().dao();
	@Inject
	private PersonService personservice;

	@Inject
	private MaterialsOutDetailService materialsOutDetailService;

	@Inject
	private FormApprovalService formApprovalService;

	@Override
	protected MaterialsOut dao() {
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
		return dbTemplate(u8SourceConfigName(),"materialsout.paginateAdminDatas",kv).paginate(pageNumber, pageSize);
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

		tx(() -> {
			for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
				String autoId = idStr;
				MaterialsOut materialsOut = findById(autoId);
				ValidationUtils.notNull(materialsOut, JBoltMsg.DATA_NOT_EXIST);

				// TODO 可能需要补充校验组织账套权限
				// TODO 存在关联使用时，校验是否仍在使用

				Integer[] state = {1,2,3};
				for (int i = 0; i < state.length; i++) {
					System.out.println(state[i]);
					if (state[i] ==materialsOut.getIAuditStatus()){
						ValidationUtils.error( "审核中、已审核的记录不允许删除,修改！！！");
					}
				}
				//删除行数据
				materialsOutDetailService.deleteByBatchIds(autoId);
				ValidationUtils.isTrue(materialsOut.delete(), JBoltMsg.FAIL);

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

		final Long[] AutoIDs = {null};
		tx(()->{
			Long headerId = null;
			// 获取Form对应的数据
			if (jBoltTable.formIsNotBlank()) {
				MaterialsOut materialsOut = jBoltTable.getFormModel(MaterialsOut.class,"materialsOut");

				//	行数据为空 不保存
				if ("save".equals(revokeVal)) {
					if (materialsOut.getAutoID() == null && !jBoltTable.saveIsNotBlank() && !jBoltTable.updateIsNotBlank() && !jBoltTable.deleteIsNotBlank()) {
						ValidationUtils.error( "请先添加行数据！");
					}
				}

				if ("submit".equals(revokeVal) && materialsOut.getAutoID() == null) {
					ValidationUtils.error( "请保存后提交审核！！！");
				}


				if (materialsOut.getAutoID() == null && "save".equals(revokeVal)) {
//					保存
//					审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
					materialsOut.setIAuditStatus(param);

					//创建人
					materialsOut.setIcreateby(userId);
					materialsOut.setCcreatename(userName);
					materialsOut.setDcreatetime(nowDate);

					//更新人
					materialsOut.setIupdateby(userId);
					materialsOut.setCupdatename(userName);
					materialsOut.setDupdatetime(nowDate);

					materialsOut.setOrganizeCode(OrgCode);
					materialsOut.setIsDeleted(false);
					save(materialsOut);
					headerId = materialsOut.getAutoID();
				}else {
					if ( param == 1 ){
						//审核人
						materialsOut.setIAuditBy(userId);
						materialsOut.setCAuditName(userName);
						materialsOut.setDAudittime(nowDate);
					}
					materialsOut.setIAuditStatus(param);
					//更新人
					materialsOut.setIupdateby(userId);
					materialsOut.setCupdatename(userName);
					materialsOut.setDupdatetime(nowDate);
					update(materialsOut);
					headerId = materialsOut.getAutoID();
				}
				AutoIDs[0] = materialsOut.getAutoID();
			}

			// 获取待保存数据 执行保存
			if (jBoltTable.saveIsNotBlank()) {
				List<MaterialsOutDetail> lines = jBoltTable.getSaveModelList(MaterialsOutDetail.class);

				Long finalHeaderId = headerId;
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
				materialsOutDetailService.batchSave(lines);
			}
			// 获取待更新数据 执行更新
			if (jBoltTable.updateIsNotBlank()) {
				List<MaterialsOutDetail> lines = jBoltTable.getUpdateModelList(MaterialsOutDetail.class);

				lines.forEach(materialsOutDetail -> {
					//更新人
					materialsOutDetail.setIupdateby(userId);
					materialsOutDetail.setDupdatetime(nowDate);
					materialsOutDetail.setCupdatename(userName);
				});
				materialsOutDetailService.batchUpdate(lines);
			}
			// 获取待删除数据 执行删除
			if (jBoltTable.deleteIsNotBlank()) {
				materialsOutDetailService.deleteByIds(jBoltTable.getDelete());
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
			Ret ret = formApprovalService.judgeType(table(), iautoid, primaryKey(),"T_Sys_OtherOut");
			ValidationUtils.isTrue(ret.isOk(), ret.getStr("msg"));

			// 处理其他业务
			MaterialsOut materialsOut = findById(iautoid);
			materialsOut.setIAuditStatus(1);
			ValidationUtils.isTrue(materialsOut.update(),JBoltMsg.FAIL);
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
			Long userId = JBoltUserKit.getUserId();
			String userName = JBoltUserKit.getUserName();
			Date nowDate = new Date();
			MaterialsOut materialsOut = superFindById(ids);
			//审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
			if (materialsOut.getIAuditStatus() != 1) {
				ValidationUtils.error("订单："+materialsOut.getBillNo()+"状态不支持审核操作！");
			}
			//订单状态：3. 已审核
			materialsOut.setIAuditStatus(2);
			//审核人
			materialsOut.setIAuditBy(userId);
			materialsOut.setCAuditName(userName);
			materialsOut.setDAudittime(nowDate);
			Ret ret = this.pushU8(ids);
			success= materialsOut.update();

			return true;
		});

		return SUCCESS;
	}

	/**
	 * 详情页审核不通过
	 */
	public Ret reject(Long ids) {
		tx(() -> {
			MaterialsOut materialsOut = superFindById(ids);
			if (materialsOut.getIAuditStatus() != 2) {
				ValidationUtils.error("订单："+materialsOut.getBillNo()+"状态不支持反审批操作！");
			}
			materialsOut.setIAuditStatus(1);
			materialsOut.update();

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
			Long userId = JBoltUserKit.getUserId();
			String userName = JBoltUserKit.getUserName();
			Date nowDate = new Date();
			List<MaterialsOut> listByIds = getListByIds(ids);
			if (listByIds.size() > 0) {
				for (MaterialsOut materialsOut : listByIds) {
					//审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
					if (materialsOut.getIAuditStatus() != 1) {
						ValidationUtils.error("订单：" + materialsOut.getBillNo() + "状态不支持审核操作！");
					}
					//订单状态：3. 已审核
					materialsOut.setIAuditStatus(2);
					//审核人
					materialsOut.setIAuditBy(userId);
					materialsOut.setCAuditName(userName);
					materialsOut.setDAudittime(nowDate);
					Ret ret = this.pushU8(ids);
					success = materialsOut.update();
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
			for (MaterialsOut materialsOut :  getListByIds(ids)) {
//			//审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
				if (materialsOut.getIAuditStatus() != 2) {
					ValidationUtils.error("订单："+materialsOut.getBillNo()+"状态不支持反审核操作！");
				}
				materialsOut.setIAuditStatus(1);
				materialsOut.update();
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
		MaterialsOut materialsOut = findById(iAutoId);
		//订单状态：2. 待审批
		materialsOut.setIAuditStatus(0);
		//审核人
		materialsOut.setIAuditBy(null);
		materialsOut.setCAuditName(null);
		materialsOut.setDAudittime(null);
		boolean result = materialsOut.update();
		return ret(result);
	}

	/**
	 * 材料出库单列表 明细
	 * @param kv
	 * @return
	 */
	public List<Record> getMaterialsOutLines(Kv kv){
		return dbTemplate("materialsout.getMaterialsOutLines",kv).find();

	}


	/**
	 * 生产工单查询
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> moDetailData(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("materialsout.moDetailData",kv).paginate(pageNumber, pageSize);
	}


	/**
	 * 材料出库单生产工单明细查询
	 * @param autoid
	 * @return
	 */
	public Record getrcvMODetailList(Long autoid){
		return dbTemplate("materialsout.getrcvMODetailList", Kv.by("autoid",autoid)).findFirst();
	}

	/**
	 *  收发类别数据源
	 */
	public List<Record>  getRDStyleDatas(Kv kv) {
		return dbTemplate("materialsout.getRDStyleDatas", kv).find();
	}


	/**
	 * 获取条码列表
	 * 通过关键字匹配
	 * autocomplete组件使用
	 */
	public List<Record> getBarcodeDatas(Kv kv) {
		return dbTemplate("materialsout.getBarcodeDatas",kv).find();
	}

	public Ret pushU8(String ids) {
		List<Record> list = dbTemplate("materialsout.pushU8List", Kv.by("autoid", ids)).find();

		if (list.size() > 0) {
//          接口参数
			User user = JBoltUserKit.getUser();
			String url = "http://localhost:8081/web/erp/common/vouchProcessDynamicSubmit";

			JSONObject preAllocate = new JSONObject();
			preAllocate.set("userCode",user.getUsername());
			preAllocate.set("organizeCode",this.getdeptid());
			preAllocate.set("CreatePerson",user.getId());
			preAllocate.set("CreatePersonName",user.getName());
			preAllocate.set("loginDate", DateUtil.format(new Date(), "yyyy-MM-dd"));
			preAllocate.set("tag","MaterialOutForMO");
			preAllocate.set("type","MaterialOutForMO");

			JSONArray mainData = new JSONArray();
			list.forEach(record -> {

				JSONObject jsonObject = new JSONObject();
				jsonObject.set("organizeCode",record.get("organizecode"));
				jsonObject.set("deliverqty","");
				jsonObject.set("qty",record.get("qty"));
				jsonObject.set("barcode",record.get("barcode"));
				jsonObject.set("billrowno",record.get("billno"));
				jsonObject.set("billid",record.get("billid"));
				jsonObject.set("billdid",record.get("billdid"));
				jsonObject.set("billnorow",record.get("billno"));
				jsonObject.set("billno",record.get("billno"));
				jsonObject.set("odeptcode",record.get("deptcode"));
				jsonObject.set("odeptname",record.get("cdepname"));
				jsonObject.set("owhcode",record.get("whcode"));
				jsonObject.set("oposcode","");
				jsonObject.set("invcode",record.get("invcode"));
				jsonObject.set("invstd","");
				jsonObject.set("invname","");
				jsonObject.set("sourcebillno","");
				jsonObject.set("sourcebillnorow","");
				jsonObject.set("cusname","");
				jsonObject.set("cuscode","");
				jsonObject.set("Tag","MaterialOutForMO");
				mainData.put(jsonObject);

			});

//            参数装载
			Map<String, Object> data = new HashMap<>();
			data.put("userCode",user.getUsername());
			data.put("organizeCode",this.getdeptid());
			data.put("token","");
			data.put("PreAllocate", preAllocate);
			data.put("MainData", mainData);

//            请求头
			Map<String, String> header = new HashMap<>(5);
			header.put("Content-Type", "application/json");


			SystemLog systemLog = new SystemLog();
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("<span class='text-danger'>[材料出库列表推U8操作]</span>");
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
						int update = update("update T_Sys_OtherOut set AuditStatus ='2' where AutoID IN("+ids+")" );

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
//	public Ret pushU8(MaterialsOut materialsout, List<MaterialsOutDetail> materialsoutdetail) {
//		if(!CollectionUtils.isNotEmpty(materialsoutdetail)){
//			return Ret.fail("数据不能为空");
//		}
//
//		User user = JBoltUserKit.getUser();
//		Map<String, Object> data = new HashMap<>();
//
//		data.put("userCode",user.getUsername());
//		data.put("organizeCode",this.getdeptid());
//		data.put("token","");
//
//		JSONObject preallocate = new JSONObject();
//
//
//		preallocate.set("userCode",user.getUsername());
//		preallocate.set("organizeCode",this.getdeptid());
//		preallocate.set("CreatePerson",user.getId());
//		preallocate.set("CreatePersonName",user.getName());
//		preallocate.set("loginDate", DateUtil.format(new Date(), "yyyy-MM-dd"));
//		preallocate.set("tag","MaterialOutForMO");
//		preallocate.set("type","MaterialOutForMO");
//
//		data.put("PreAllocate",preallocate);
//
//		JSONArray maindata = new JSONArray();
//		materialsoutdetail.stream().forEach(s -> {
//			JSONObject jsonObject = new JSONObject();
//
//
//			maindata.put(jsonObject);
//		});
//		data.put("MainData",maindata);
//
//		//            请求头
//		Map<String, String> header = new HashMap<>(5);
//		header.put("Content-Type", "application/json");
//		String url = "http://localhost:8081/web/erp/common/vouchProcessDynamicSubmit";
//
//		try {
//			String post = HttpApiUtils.httpHutoolPost(url, data, header);
//			com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(post);
//			if (isOk(post)) {
//				if ("201".equals(jsonObject.getString("code"))) {
//					System.out.println(jsonObject);
//					return Ret.ok("提交成功");
//				}
//			}
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//		return Ret.fail("上传u8失败");
//	}

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

	public void saveMaterialsOutModel(MaterialsOut materials,SysPuinstore puinstore,String sourceBillType,String sourceBillDid){
		//materials.setAutoID(JBoltSnowflakeKit.me.nextId());
		materials.setSourceBillType("");
		materials.setSourceBillDid("");
		materials.setRdCode(puinstore.getRdCode());
		materials.setOrganizeCode(getOrgCode());
		materials.setBillNo(puinstore.getBillNo());
		materials.setBillType(puinstore.getBillType());
		materials.setBillDate(getBillDate(puinstore.getBillDate()));
		materials.setDeptCode(puinstore.getDeptCode());
		materials.setWhcode(puinstore.getWhCode());
		materials.setVenCode(puinstore.getVenCode());
		materials.setCcreatename(puinstore.getCCreateName());
		materials.setDcreatetime(puinstore.getDCreateTime());
		materials.setState(1);
		materials.setIAuditStatus(0);
		materials.setMemo(puinstore.getMemo());
	}

	public Date getBillDate(String billDate) {
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		try {
			return format.parse(billDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}