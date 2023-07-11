package cn.rjtech.admin.spotcheckformm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.inventoryspotcheckform.InventorySpotCheckFormService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.spotcheckform.SpotCheckFormService;
import cn.rjtech.admin.spotcheckformd.SpotCheckFormDService;
import cn.rjtech.admin.spotcheckformdline.SpotcheckformdLineService;
import cn.rjtech.admin.spotcheckformparam.SpotCheckFormParamService;
import cn.rjtech.admin.spotcheckformtableitem.SpotCheckFormTableItemService;
import cn.rjtech.admin.spotcheckformtableparam.SpotCheckFormTableParamService;
import cn.rjtech.cache.FormApprovalCache;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.AuditWayEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 始业点检表管理
 * @ClassName: SpotCheckFormMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-29 09:16
 */
public class SpotCheckFormMService extends BaseService<SpotCheckFormM> implements IApprovalService {
    
	private final SpotCheckFormM dao=new SpotCheckFormM().dao();
    
	@Override
	protected SpotCheckFormM dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }
    
    @Inject
    private MoDocService moDocService;
    @Inject
	private SpotCheckFormService spotCheckFormService;
    @Inject
    private SpotCheckFormDService spotCheckFormDService;
    @Inject
    private SpotCheckFormParamService spotCheckFormParamService;
    @Inject
    private SpotcheckformdLineService spotcheckformdLineService;
    @Inject
	private SpotCheckFormTableItemService spotCheckFormTableItemService;
    @Inject
    private InventorySpotCheckFormService inventorySpotCheckFormService;
    @Inject
    private SpotCheckFormTableParamService spotCheckFormTableParamService;

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv para) {
		Page<Record> page = dbTemplate("spotcheckformm.list", para).paginate(pageNumber, pageSize);
		for (Record record : page.getList()) {
			// 审核中，并且单据审批方式为审批流
			if (ObjUtil.equals(AuditStatusEnum.AWAIT_AUDIT.getValue(), record.getInt(IAUDITSTATUS)) && ObjUtil.equals(AuditWayEnum.FLOW.getValue(), record.getInt(IAUDITWAY))) {
				record.put("approvalusers", FormApprovalCache.ME.getNextApprovalUserNames(record.getLong("iautoid"), 5));
			}
		}
		return page;

	}

	/**
	 * 保存
	 */
	public Ret save(SpotCheckFormM spotCheckFormM) {
		if(spotCheckFormM==null || isOk(spotCheckFormM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=spotCheckFormM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(spotCheckFormM.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 */
	public Ret update(SpotCheckFormM spotCheckFormM) {
		if(spotCheckFormM==null || notOk(spotCheckFormM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SpotCheckFormM dbSpotCheckFormM=findById(spotCheckFormM.getIAutoId());
		if(dbSpotCheckFormM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=spotCheckFormM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(spotCheckFormM.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param spotCheckFormM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(SpotCheckFormM spotCheckFormM, Kv kv) {
		//addDeleteSystemLog(spotCheckFormM.getIAutoId(), JBoltUserKit.getUserId(),spotCheckFormM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param spotCheckFormM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SpotCheckFormM spotCheckFormM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 *制造工单入口数据
	 */
	public List<Record> getAdminDatas2( Kv kv) {
		List<Record> records = moDocService.findByModecIdProcessDatas(kv.getLong("modocid"));
		//根据工艺路线获取对应的工序
		if (ObjUtil.isNotNull(records)) {
			//通过工序名称获取对应的点检数据
			for (Record record : records) {
				Record operationName = inventorySpotCheckFormService.findByInventoryIdAndOperationName(record.getLong("iinventoryid"), record.getStr("coperationname"), kv.getInt("itype"));
				if (ObjUtil.isNotNull(operationName)) {
					record.set("ispotcheckformid",operationName.getStr("ispotcheckformid"));
					record.set("cspotcheckformname",operationName.getStr("cspotcheckformname"));
				}

				//通过表格id工序id工单id获取已保存的数据
					SpotCheckFormM m = findBySpotCheckFormIdAndRoutingConfigIdAndModocId(record.getLong("ispotcheckformid"), kv.getLong("modocid"), record.getLong("routingconfigid"), kv.getInt("itype"));
					if (ObjUtil.isNotNull(m)){
					record.set("spotcheckformmid",m.getIAutoId());
					record.set("cpersonname",m.getCCreateName());
					record.set("dcreatetime2",m.getDCreateTime());
						String s = m.getIAuditStatus() == 0 ? "未审核" : m.getIAuditStatus() == 1 ? "待审核" :
								m.getIAuditStatus() == 2 ? "审核通过" : "审核不通过";
					record.set("iauditstatus",s);
						record.set("peoplename",m.getCPersonName());
						record.set("peoplenameid",m.getIPersonId());
					}else {
						record.set("iauditstatus","未生成");
						record.set("peoplename",JBoltUserKit.getUserName());
						record.set("peoplenameid",JBoltUserKit.getUserId());
					}
				record.set("itype",kv.getStr("itype"));
				if (ObjUtil.isNull(operationName)) {
					record.set("iauditstatus","未有点检表");
				}
				}
		}

		return records;
	}
	public SpotCheckFormM findBySpotCheckFormIdAndRoutingConfigIdAndModocId(Long ispotcheckformid,Long modocid,
																			Long routingConfigId,int itype){
		return  findFirst("select * from PL_SpotCheckFormM where iMoDocId=? and iMoRoutingConfigId=? and iSpotCheckFormId=?   and itype=?",
				modocid,routingConfigId,ispotcheckformid,itype);
	}

	/**
	 * 获取设备名称并拼接
	 */
	public Record getEquipment(Long routingConfigId){
		List<Record> equipmentList = moDocService.getMoDocEquipment(Kv.by("configid", routingConfigId));
		StringBuilder cequipmentnames =new StringBuilder();
		StringBuilder cequipmentids =new StringBuilder();
		for (Record record1 : equipmentList) {
			cequipmentnames.append(	record1.getStr("cequipmentname")).append(",");
			cequipmentids.append(	record1.getStr("iequipmentid")).append(",");
		}
		String keywordStr="";
		String keywordStr2="";
		if (equipmentList.size()>0) {
			keywordStr = cequipmentnames.deleteCharAt(cequipmentnames.length() - 1).toString();
			keywordStr2 = cequipmentids.deleteCharAt(cequipmentids.length() - 1).toString();
		}
		return  new Record().set("cequipmentnames",keywordStr).set("cequipmentids",keywordStr2);
	}
	/**
	 * 根据id获取数据
	 */
	public  Record getData(Long iAutoId){
		return dbTemplate("spotcheckformm.list",Kv.by("id",iAutoId)).findFirst();
	}

	/**
	 * 标题数据处理
	 */
	public List<Map<String, Object>> lineRoll(List<Record> formItemLists,String iprodformid) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		// 标题选择值
		if (ObjUtil.isNotNull(formItemLists)){

			for (int i=0; i<formItemLists.size(); i++){

				Record item = formItemLists.get(i);
				String qcItemId = item.getStr("iqcitemid");

				boolean flag = false;
				List<Map<String, Object>> itemParamList = new ArrayList<>();
				for (Record object :formItemLists){
					if (qcItemId.equals(object.getStr("iqcitemid"))){
						flag = true;
						itemParamList.add(object.getColumns());
					}
				}
				if (flag){
					item.put("compares", itemParamList);
				}
				mapList.add(item.getColumns());
			}
		}

		if (CollUtil.isNotEmpty(mapList)){

			mapList.sort((o1, o2) -> {
                Integer map1 = Integer.valueOf(o1.get("iseq").toString());
                Integer map2 = Integer.valueOf(o2.get("iseq").toString());
                return map1.compareTo(map2);
            });
			return mapList;
		}
		return null;
	}

	public List<Map<String, Object>> lineRoll2(List<Record> byIdGetDetail,String spotcheckformmid) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		String iseq = "";
		String id = "";
		if (ObjUtil.isNotNull(byIdGetDetail)){
			for (int i=0; i<byIdGetDetail.size(); i++){
				Record item = byIdGetDetail.get(i);
				String qcItemId = item.getStr("iseq");
				if (!qcItemId.equals(iseq)) {
					iseq=qcItemId;
					boolean flag = false;
					List<Map<String, Object>> itemParamList = new ArrayList<>();
					for (Record object :byIdGetDetail){
						if (!object.getStr("iAutoId").equals(id)) {
							id=object.getStr("iAutoId");
							if (StrUtil.isNotBlank(spotcheckformmid)) {
							List<SpotCheckFormD> spotCheckFormDS = spotCheckFormDService.find("select * from PL_SpotCheckFormD where iSpotCheckFormMid=?", spotcheckformmid);
							for (SpotCheckFormD spotCheckFormD : spotCheckFormDS) {
							if (ObjUtil.isNotNull(spotCheckFormD)) {
							SpotcheckformdLine bySpotCheckFormDId = spotcheckformdLineService.findBySpotCheckFormDId(spotCheckFormD.getIAutoId());
							if (object.getInt("ieq").equals(bySpotCheckFormDId.getISeq())){
								object.set("cValue",bySpotCheckFormDId.getCValue());
								object.set("iStdVal",spotCheckFormD.getIStdVal());
								object.set("iMaxVal",spotCheckFormD.getIMaxVal());
								object.set("iMinVal",spotCheckFormD.getIMinVal());

							}
							}
							}
							}
						}
						if (qcItemId.equals(object.getStr("iseq"))){
							flag = true;
							itemParamList.add(object.getColumns());
						}

					}
					if (flag){
						item.put("compares", itemParamList);
					}
					mapList.add(item.getColumns());
				}
			}

		}
		if (CollUtil.isNotEmpty(mapList)){

			mapList.sort((o1, o2) -> {
                Integer map1 = Integer.valueOf(o1.get("proditemiseq").toString());
                Integer map2 = Integer.valueOf(o2.get("proditemiseq").toString());
                return map1.compareTo(map2);
            });
			return mapList;
		}
		return null;
	}
	/**
	 *新增保存
	 * @return
	 */
	public Ret submitForm(String formJsonDataStr, String tableJsonDataStr) {
		ValidationUtils.notBlank(formJsonDataStr, JBoltMsg.JBOLTTABLE_IS_BLANK);
		ValidationUtils.notBlank(tableJsonDataStr, JBoltMsg.JBOLTTABLE_IS_BLANK);

		JSONArray qcParamJsonData = JSONObject.parseArray(tableJsonDataStr);
		ValidationUtils.notEmpty(qcParamJsonData, JBoltMsg.JBOLTTABLE_IS_BLANK);

		JSONObject formJsonData = JSONObject.parseObject(formJsonDataStr);
		AtomicReference<String> id = new AtomicReference<>("");

		tx(()->{
			SpotCheckFormM spotCheckFormM2=null;
			if (StrUtil.isNotBlank(formJsonData.getString("spotcheckformmid"))) {
				String string = formJsonData.getString("spotcheckformmid");
				spotCheckFormM2= findById(Long.valueOf(string));
				id.set(string);
			}
			//主表数据
			if (StrUtil.isBlank(formJsonData.getString("spotcheckformmid"))) {
				if (ObjUtil.isNull(spotCheckFormM2)) {
					spotCheckFormM2 = new SpotCheckFormM();
				}
				//单号
				String routingconfigid = formJsonData.getString("routingconfigid");
				Long modocid = formJsonData.getLong("modocid");
				MoDoc moDoc = moDocService.findById(modocid);
				spotCheckFormM2.setIMoDocId(modocid);
				spotCheckFormM2.setCMoDocNo(moDoc.getCMoDocNo());
				spotCheckFormM2.setIMoRoutingConfigId(routingconfigid);
				//工序名称
				String coperationname = formJsonData.getString("coperationname");
				spotCheckFormM2.setCOperationName(coperationname);
				//设备名称

				spotCheckFormM2.setISpotCheckFormId(formJsonData.getLong("iprodformid"));
				spotCheckFormM2.setIType(formJsonData.getInteger("itype"));
				//点检记录
				spotCheckFormM2.setCDesc(formJsonData.getString("cdesc"));
				spotCheckFormM2.setCMethod(formJsonData.getString("cmethod"));

				//基础数据
				User user = JBoltUserKit.getUser();
				Date date = new Date();
				spotCheckFormM2.setCOrgCode(getOrgCode());
				spotCheckFormM2.setCOrgName(getOrgName());
				spotCheckFormM2.setIOrgId(getOrgId());
				spotCheckFormM2.setICreateBy(user.getId());
				spotCheckFormM2.setCCreateName(user.getName());
				spotCheckFormM2.setDCreateTime(date);
				spotCheckFormM2.setCUpdateName(user.getName());
				spotCheckFormM2.setDUpdateTime(date);
				spotCheckFormM2.setIUpdateBy(user.getId());
				spotCheckFormM2.setIAuditStatus(0);
				spotCheckFormM2.setIPersonId(user.getId());
				spotCheckFormM2.setCPersonName(user.getName());
				spotCheckFormM2.setIPersonId(user.getId());
				ValidationUtils.isTrue(spotCheckFormM2.save(), "保存失败");
				id.set(String.valueOf(spotCheckFormM2.getIAutoId()));

				//细表数据
				ArrayList<SpotCheckFormD> prodFormDS = new ArrayList<>();
				ArrayList<SpotcheckformdLine> prodformdLines = new ArrayList<>();
				for (int i = 0; i < qcParamJsonData.size(); i++) {
					JSONObject jsonObject = qcParamJsonData.getJSONObject(i);
					//子表
					SpotCheckFormD prodFormD = new SpotCheckFormD();
					prodFormD.setIAutoId(JBoltSnowflakeKit.me.nextId());
					prodFormD.setISpotCheckFormMid(spotCheckFormM2.getIAutoId());
					prodFormD.setISpotCheckFormId(spotCheckFormM2.getISpotCheckFormId());
					SpotCheckFormTableParam spotCheckFormTableParam = spotCheckFormTableParamService.findFirst("SELECT * FROM Bd_SpotCheckFormTableParam WHERE iSpotCheckFormId = ?  ORDER BY iSeq ASC", spotCheckFormM2.getISpotCheckFormId());
					List<Record> list = spotCheckFormParamService.findRecords("SELECT * FROM Bd_SpotCheckFormParam WHERE iSpotCheckFormId = ?  ORDER BY iItemSeq ASC", spotCheckFormM2.getISpotCheckFormId());
					StringBuilder ids = new StringBuilder();
					for (Record record : list) {
						ids.append(record.getStr("iautoid")).append(",");
					}
					String keywordStr2="";
					if (list.size()>0) {
						keywordStr2 = ids.deleteCharAt(ids.length() - 1).toString();
					}

					prodFormD.setISpotCheckFormParamId(spotCheckFormTableParam.getIAutoId());
					prodFormD.setISeq(jsonObject.getInteger("iseq"));
					prodFormD.setCSpotCheckFormParamIds(keywordStr2);
					prodFormD.setIType(jsonObject.getInteger("itype"));
					if (StrUtil.isNotBlank(jsonObject.getString("istdval"))){
						prodFormD.setIStdVal(jsonObject.getBigDecimal("istdval"));
					}
					if (StrUtil.isNotBlank(jsonObject.getString("imaxval"))){
						prodFormD.setIMaxVal(jsonObject.getBigDecimal("imaxval"));
					}
					if (StrUtil.isNotBlank(jsonObject.getString("iminval"))){
						prodFormD.setIMinVal(jsonObject.getBigDecimal("iminval"));
					}
					if (StrUtil.isNotBlank(jsonObject.getString("coptions"))){
						prodFormD.setCOptions(jsonObject.getString("coptions"));
					}
					prodFormDS.add(prodFormD);
					//明细列值表数据
					SpotcheckformdLine prodformdLine = new SpotcheckformdLine();
					prodformdLine.setISpotCheckFormDid(prodFormD.getIAutoId());
					prodformdLine.setISeq(prodFormD.getISeq());
					if (StrUtil.isNotBlank(jsonObject.getString("cvalue"))){
						prodformdLine.setCValue(jsonObject.getString("cvalue"));
					}
					prodformdLines.add(prodformdLine);
				}
				spotCheckFormDService.batchSave(prodFormDS);
				spotcheckformdLineService.batchSave(prodformdLines);
			}else {
				SpotCheckFormM spotCheckFormM = findById(formJsonData.getString("spotcheckformmid"));
				User user = JBoltUserKit.getUser();
				Date date = new Date();
				spotCheckFormM.setCUpdateName(user.getName());
				spotCheckFormM.setDUpdateTime(date);
				spotCheckFormM.setIUpdateBy(user.getId());
				spotCheckFormM.setISpotCheckFormId(formJsonData.getLong("iprodformid"));
				spotCheckFormM.setCDesc(formJsonData.getString("cdesc"));
				spotCheckFormM.setCMethod(formJsonData.getString("cmethod"));
				ValidationUtils.isTrue(spotCheckFormM.update(), "修改失败");
				//根据主表id获取数据
				List<SpotCheckFormD> formDList = spotCheckFormDService.findByPid(spotCheckFormM.getIAutoId());
				for (SpotCheckFormD spotcheckformd : formDList) {
					SpotcheckformdLine bySpotCheckFormDId = spotcheckformdLineService.findBySpotCheckFormDId(spotcheckformd.getIAutoId());
					bySpotCheckFormDId.delete();
					spotcheckformd.delete();
				}
				//细表数据
				ArrayList<SpotCheckFormD> prodFormDS = new ArrayList<>();
				ArrayList<SpotcheckformdLine> prodformdLines = new ArrayList<>();
				for (int i = 0; i < qcParamJsonData.size(); i++) {
					JSONObject jsonObject = qcParamJsonData.getJSONObject(i);
					//子表
					SpotCheckFormD prodFormD = new SpotCheckFormD();
					prodFormD.setIAutoId(JBoltSnowflakeKit.me.nextId());
					prodFormD.setISpotCheckFormMid(spotCheckFormM.getIAutoId());
					prodFormD.setISpotCheckFormId(spotCheckFormM.getISpotCheckFormId());
					SpotCheckFormTableParam spotCheckFormTableParam = spotCheckFormTableParamService.findFirst("SELECT * FROM Bd_SpotCheckFormTableParam WHERE iSpotCheckFormId = ?  ORDER BY iSeq ASC", spotCheckFormM2.getISpotCheckFormId());
					List<Record> list = spotCheckFormParamService.findRecords("SELECT * FROM Bd_SpotCheckFormParam WHERE iSpotCheckFormId = ?  ORDER BY iItemSeq ASC", spotCheckFormM2.getISpotCheckFormId());
					StringBuilder ids = new StringBuilder();
					for (Record record : list) {
						ids.append(record.getStr("iautoid")).append(",");
					}
					String keywordStr2="";
					if (list.size()>0) {
						keywordStr2 = ids.deleteCharAt(ids.length() - 1).toString();
					}
					prodFormD.setISpotCheckFormParamId(spotCheckFormTableParam.getIAutoId());
					prodFormD.setCSpotCheckFormParamIds(keywordStr2);
					prodFormD.setISeq(jsonObject.getInteger("iseq"));
					prodFormD.setIType(jsonObject.getInteger("itype"));
					if (StrUtil.isNotBlank(jsonObject.getString("istdval"))){
						prodFormD.setIStdVal(jsonObject.getBigDecimal("istdval"));
					}
					if (StrUtil.isNotBlank(jsonObject.getString("imaxval"))){
						prodFormD.setIMaxVal(jsonObject.getBigDecimal("imaxval"));
					}
					if (StrUtil.isNotBlank(jsonObject.getString("iminval"))){
						prodFormD.setIMinVal(jsonObject.getBigDecimal("iminval"));
					}
					if (StrUtil.isNotBlank(jsonObject.getString("coptions"))){
						prodFormD.setCOptions(jsonObject.getString("coptions"));
					}
					prodFormDS.add(prodFormD);
					//明细列值表数据
					SpotcheckformdLine prodformdLine = new SpotcheckformdLine();
					prodformdLine.setISpotCheckFormDid(prodFormD.getIAutoId());
					prodformdLine.setISeq(prodFormD.getISeq());
					if (StrUtil.isNotBlank(jsonObject.getString("cvalue"))){
						prodformdLine.setCValue(jsonObject.getString("cvalue"));
					}
					prodformdLines.add(prodformdLine);
				}

				spotCheckFormDService.batchSave(prodFormDS);
				spotcheckformdLineService.batchSave(prodformdLines);

			}
				return true;
		});
		SpotCheckFormM m = new SpotCheckFormM();
		m.setIAutoId(Long.valueOf(id.get()));
		return successWithData(m.keep("iautoid"));
	}

	/**
	 * 提审前业务，如有异常返回错误信息
	 */
	@Override
	public String preSubmitFunc(long formAutoId) {
		SpotCheckFormM prodFormM = findById(formAutoId);

		switch (AuditStatusEnum.toEnum(prodFormM.getIAuditStatus())) {
			// 已保存
			case NOT_AUDIT:
				// 不通过
			case REJECTED:

				break;
			default:
				return "订单非已保存状态";
		}

		return null;
	}

	@Override
	public String postSubmitFunc(long formAutoId) {
		return null;
	}

	/**
	 * 处理审批通过的其他业务操作，如有异常返回错误信息
	 *
	 * @param formAutoId 单据ID
	 * @return 错误信息
	 */
	@Override
	public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
		SpotCheckFormM formUploadM = findById(formAutoId);
		// 审核状态修改
		formUploadM.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
		formUploadM.setIUpdateBy(JBoltUserKit.getUserId());
		formUploadM.setCUpdateName(JBoltUserKit.getUserName());
		formUploadM.setDUpdateTime(new Date());
		formUploadM.setIAuditBy(JBoltUserKit.getUserId());
		formUploadM.setCAuditName(JBoltUserKit.getUserName());
		formUploadM.setDSubmitTime(new Date());
		formUploadM.update();
		return null;
	}

	/**
	 * 处理审批不通过的其他业务操作，如有异常处理返回错误信息
	 */
	@Override
	public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
		SpotCheckFormM formUploadM = findById(formAutoId);
		// 审核状态修改
		formUploadM.setIAuditStatus(AuditStatusEnum.REJECTED.getValue());
		formUploadM.setIUpdateBy(JBoltUserKit.getUserId());
		formUploadM.setCUpdateName(JBoltUserKit.getUserName());
		formUploadM.setDUpdateTime(new Date());
		formUploadM.setCAuditName(JBoltUserKit.getUserName());
		formUploadM.setIAuditBy(JBoltUserKit.getUserId());
		formUploadM.setDSubmitTime(new Date());
		formUploadM.update();
		return null;
	}


	@Override
	public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
		return null;
	}

	@Override
	public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
		return null;
	}

	@Override
	public String postWithdrawFunc(long formAutoId) {
		return null;
	}

	@Override
	public String withdrawFromAuditting(long formAutoId) {
		ValidationUtils.isTrue(updateColumn(formAutoId, "iAuditStatus", AuditStatusEnum.NOT_AUDIT.getValue()).isOk(), "撤回失败");

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
	/**
	 * 批量审核（审批）通过，后置业务实现
	 */
	@Override
	public String postBatchApprove(List<Long> formAutoIds) {
		for (Long formAutoId : formAutoIds) {
			SpotCheckFormM formUploadM = findById(formAutoId);
			// 审核状态修改
			formUploadM.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
			formUploadM.setIUpdateBy(JBoltUserKit.getUserId());
			formUploadM.setCUpdateName(JBoltUserKit.getUserName());
			formUploadM.setDUpdateTime(new Date());
			formUploadM.setIAuditBy(JBoltUserKit.getUserId());
			formUploadM.setCAuditName(JBoltUserKit.getUserName());
			formUploadM.setDSubmitTime(new Date());
			formUploadM.update();
		}
		return null;
	}
	/**
	 * 批量审批（审核）不通过，后置业务实现
	 */
	@Override
	public String postBatchReject(List<Long> formAutoIds) {
		for (Long formAutoId : formAutoIds) {
			SpotCheckFormM spotCheckFormM = findById(formAutoId);
			// 审核状态修改
			spotCheckFormM.setIAuditStatus(AuditStatusEnum.REJECTED.getValue());
			spotCheckFormM.setIUpdateBy(JBoltUserKit.getUserId());
			spotCheckFormM.setCUpdateName(JBoltUserKit.getUserName());
			spotCheckFormM.setDUpdateTime(new Date());
			spotCheckFormM.setIAuditBy(JBoltUserKit.getUserId());
			spotCheckFormM.setCAuditName(JBoltUserKit.getUserName());
			spotCheckFormM.setDSubmitTime(new Date());
			spotCheckFormM.update();
		}
		return null;
	}

	@Override
	public String postBatchBackout(List<Long> formAutoIds) {
		return null;
	}

	public List<Map<String, Object>> getTableHeadData(Long formId) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		 if (ObjUtil.isNotNull(formId)){
			List<Record> qcFormItemList = spotCheckFormService.getItemCombinedListByPId(Kv.by("iqcformid", formId));
			List<Record> qcFormParamList = spotCheckFormParamService.getQcFormParamListByPId(formId);
			Map<Long, List<Record>> itemParamByItemMap = qcFormParamList.stream().collect(Collectors.groupingBy(obj -> obj.getLong("iqcitemid")));
			for (Record qcFormItemRecord : qcFormItemList){
				Long qcItemId = qcFormItemRecord.getLong("iqcitemid");
				if (itemParamByItemMap.containsKey(qcItemId)){
					List<Record> list = itemParamByItemMap.get(qcItemId);
					List<Map<String, Object>> maps = new ArrayList<>();

					for (Record itemRecord : list){
						maps.add(itemRecord.getColumns());
					}
					qcFormItemRecord.set("compares", maps);
				}
				mapList.add(qcFormItemRecord.getColumns());
			}
		}

		if (CollUtil.isNotEmpty(mapList)){

			mapList.sort((o1, o2) -> {
				Integer map1 = Integer.valueOf(o1.get("iseq").toString());
				Integer map2 = Integer.valueOf(o2.get("iseq").toString());
				return map1.compareTo(map2);
			});
			return mapList;
		}
		return null;
	}

	public List<Map<String, Object>> findByFormId(Long formId,String  pid){
		List<Record> records = findRecords("SELECT * FROM Bd_SpotCheckFormTableParam WHERE iSpotCheckFormId = ?  ORDER BY iSeq ASC", formId);
		List<SpotCheckFormTableItem> qcFormTableItemList = spotCheckFormTableItemService.findByFormId(formId);

		List<Map<String, Object>> mapList = new ArrayList<>();

		for (Record record : records){
			if (StrUtil.isNotBlank(pid)) {
				List<SpotCheckFormD> formDList = spotCheckFormDService.find("select * from PL_SpotCheckFormD where iSpotCheckFormMid=?", pid);
				for (SpotCheckFormD spotCheckFormD : formDList) {
					if (ObjUtil.isNotNull(spotCheckFormD)) {
						SpotcheckformdLine checkFormDId = spotcheckformdLineService.findBySpotCheckFormDId(spotCheckFormD.getIAutoId());
					if (record.getInt("iseq").equals(checkFormDId.getISeq())){
						record.set("cValue",checkFormDId.getCValue());
						record.set("iStdVal",spotCheckFormD.getIStdVal());
						record.set("iMaxVal",spotCheckFormD.getIMaxVal());
						record.set("iMinVal",spotCheckFormD.getIMinVal());

					}
					}
				}
			}
			Long id = record.getLong(SpotCheckFormTableParam.IAUTOID);
			for (SpotCheckFormTableItem qcFormTableItem : qcFormTableItemList){
				// 校验当前id是否存在
				if (id.equals(qcFormTableItem.getISpotCheckFormTableParamId())){
					record.set(String.valueOf(qcFormTableItem.getISpotCheckFormItemId()), qcFormTableItem.getISpotCheckFormParamId());
				}
			}
			mapList.add(record.getColumns());
		}
		return mapList;
	}

}