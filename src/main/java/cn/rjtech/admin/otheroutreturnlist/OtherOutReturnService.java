package cn.rjtech.admin.otheroutreturnlist;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.otheroutdetail.OtherOutDetailService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.model.momdata.OtherOut;
import cn.rjtech.model.momdata.OtherOutDetail;
import cn.rjtech.model.momdata.Person;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.HttpApiUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.*;

/**
 * 出库管理-特殊领料出库 Service
 *
 * @ClassName: OtherOutService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-22 16:33
 */
public class OtherOutReturnService extends BaseService<OtherOut> {

	private final OtherOut dao = new OtherOut().dao();

	@Inject
	private PersonService personservice;

	@Override
	protected OtherOut dao() {
		return dao;
	}

	@Inject
	private OtherOutDetailService otherOutDetailService;

	/**
	 * 后台管理分页查询
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("otheroutreturnlist.paginateAdminDatas",kv).paginate(pageNumber, pageSize);
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
	 */
	public Ret deleteByBatchIds(String ids) {
		return deleteByIds(ids,true);
	}

	/**
	 * 删除
	 */
	public Ret delete(Long id) {
		return deleteById(id,true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param otherOut 要删除的model
	 * @param kv 携带额外参数一般用不上
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
	 */
	@Override
	public String checkCanDelete(OtherOut otherOut, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(otherOut, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}



	/**
	 * 特殊领料单列表 明细
	 */
	public Page<Record> getOtherOutLinesReturnLines(int pageNumber, int pageSize, Kv kv){
		return dbTemplate("otheroutreturnlist.getOtherOutLinesReturnLines",kv).paginate(pageNumber, pageSize);

	}

	public List<Record> treturnQty(Kv kv) {
        return dbTemplate("otheroutreturnlist.treturnQty",kv).find();
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

		tx(()->{
			String headerId = null;
			// 获取Form对应的数据
			if (jBoltTable.formIsNotBlank()) {
				OtherOut otherOut = jBoltTable.getFormModel(OtherOut.class,"otherOut");


				//	行数据为空 不保存

				if (otherOut.getAutoID() == null && !jBoltTable.saveIsNotBlank() && !jBoltTable.updateIsNotBlank() && !jBoltTable.deleteIsNotBlank()) {
					ValidationUtils.error( "请先添加行数据！");
				}



				List<Record> jBoltTableSaveRecordList = jBoltTable.getUpdateRecordList();
				if (jBoltTableSaveRecordList!= null){
					int k = 0;
					for (int i = 0; i < jBoltTableSaveRecordList.size(); i++) {
						k++;
						Record record = jBoltTableSaveRecordList.get(i);
						//输入数量
						BigDecimal qty = record.getBigDecimal("qty");
						//可退数量
						BigDecimal qtys = record.getBigDecimal("qtys");

						if (notNull(record.get("qty"))){
							BigDecimal num1 = new BigDecimal(-1);
							BigDecimal value = qtys.multiply(num1);
							int flag = qty.compareTo(value);

							//物料退货判断
							if (flag<0){
								ValidationUtils.error( "第" + k + "行退货数量超出现存数（" + qtys + "）！！！");
							}
						}else {
							ValidationUtils.error( "第"+k+"行出库数量为空！！！");
						}

					}
				}
				System.out.println("====="+otherOut.getAutoID());

				if (otherOut.getAutoID() == null ) {
					otherOut.setOrganizeCode(OrgCode);
					//创建人
					otherOut.setICreateBy(userId);
					otherOut.setDCreateTime(nowDate);
					otherOut.setCCreateName(userName);
					//更新人
					otherOut.setIUpdateBy(userId);
					otherOut.setDupdateTime(nowDate);
					otherOut.setCUpdateName(userName);
					otherOut.setIsDeleted(false);
					otherOut.setIAuditStatus(0);
					otherOut.setType("OtherOutMES");
					save(otherOut);
					save(otherOut);
					headerId = otherOut.getAutoID();
				} else {
					//更新人
					otherOut.setIUpdateBy(userId);
					otherOut.setDupdateTime(nowDate);
					otherOut.setCUpdateName(userName);
					update(otherOut);
					headerId = otherOut.getAutoID();
				}
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
		return SUCCESS;
	}


	/**
	 * 后台管理分页查询
	 */
	public Page<Record> getReturnDataS(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("otheroutreturnlist.getReturnDataS",kv).paginate(pageNumber, pageSize);
	}

	/**
	 * 删除行数据
	 */
	public Ret deleteList(String autoId) {
		tx(() -> {
			ValidationUtils.notNull(autoId, JBoltMsg.DATA_NOT_EXIST);
			delete("delete from T_Sys_OtherOutDetail where autoId = '"+autoId+"'");
			return true;
		});
		return SUCCESS;
	}

	public List<OtherOut> getpushu(Kv kv) {
        return daoTemplate("otheroutreturnlist.pushu", kv).find();
	}

	//推送u8数据接口
	public Ret pushU8(OtherOut otherout, List<OtherOutDetail> otheroutdetail) {
		if(CollUtil.isEmpty(otheroutdetail)){
			return fail("数据不能为空");
		}

		User user = JBoltUserKit.getUser();
		JSONObject data = new JSONObject();

		data.set("userCode",user.getUsername());
		data.set("organizeCode",this.getCorgcode());
		data.set("token","");

		JSONObject preallocate = new JSONObject();


		preallocate.set("userCode",user.getUsername());
		preallocate.set("organizeCode",this.getCorgcode());
		preallocate.set("CreatePerson",user.getId());
		preallocate.set("CreatePersonName",user.getName());
		preallocate.set("loginDate", DateUtil.format(new Date(), "yyyy-MM-dd"));
		preallocate.set("tag","OtherOut");
		preallocate.set("type","OtherOut");

		data.set("PreAllocate",preallocate);

		ArrayList<Object> maindata = new ArrayList<>();
		otheroutdetail.forEach(s -> {
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
		return fail("上传u8失败");
	}

	public String getCorgcode(){
		Person person = personservice.findFirstByUserId(JBoltUserKit.getUserId());
		return null == person ? null : person.getCOrgCode();
	}

}