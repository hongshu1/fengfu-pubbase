package cn.rjtech.admin.prodformm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.prodform.ProdFormService;
import cn.rjtech.admin.prodformd.ProdFormDService;
import cn.rjtech.admin.prodformdline.ProdformdLineService;
import cn.rjtech.admin.workregionm.WorkregionmService;
import cn.rjtech.admin.workshiftm.WorkshiftmService;
import cn.rjtech.cache.FormApprovalCache;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.AuditWayEnum;
import cn.rjtech.model.momdata.ProdFormD;
import cn.rjtech.model.momdata.ProdFormM;
import cn.rjtech.model.momdata.ProdformdLine;
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

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 制造管理-生产表单主表
 * @ClassName: ProdFormMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-28 10:04
 */
public class ProdFormMService extends BaseService<ProdFormM> implements IApprovalService {
	private final ProdFormM dao=new ProdFormM().dao();
	@Override
	protected ProdFormM dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    @Inject
    private ProdFormService prodFormService;
    @Inject
    private ProdFormDService prodFormDService;
    @Inject
    private WorkshiftmService workshiftmService;
    @Inject
    private WorkregionmService workregionmService;
    @Inject
    private ProdformdLineService prodformdLineService;

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv para) {
		Page<Record> page = dbTemplate("prodformm.getAdminDatas", para).paginate(pageNumber, pageSize);
		for (Record record : page.getList()) {
			record.set("iprodformid",prodFormService.findById(record.getStr("iprodformid")).getCProdFormName());
			record.set("iworkregionmid",workregionmService.findById(record.getStr("iworkregionmid")).getCWorkName());
			record.set("iworkshiftmid",workshiftmService.findById(record.getStr("iworkshiftmid")).getCworkshiftname());
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
	public Ret save(ProdFormM prodFormM) {
		if(prodFormM==null || isOk(prodFormM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=prodFormM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(prodFormM.getIAutoId(), JBoltUserKit.getUserId(), prodFormM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 */
	public Ret update(ProdFormM prodFormM) {
		if(prodFormM==null || notOk(prodFormM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ProdFormM dbProdFormM=findById(prodFormM.getIAutoId());
		if(dbProdFormM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=prodFormM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(prodFormM.getIAutoId(), JBoltUserKit.getUserId(), prodFormM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param prodFormM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(ProdFormM prodFormM, Kv kv) {
		//addDeleteSystemLog(prodFormM.getIAutoId(), JBoltUserKit.getUserId(),prodFormM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param prodFormM model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkInUse(ProdFormM prodFormM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
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

	public List<Map<String, Object>> lineRoll2(List<Record> byIdGetDetail) {
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
							ProdFormD prodFormD = prodFormDService.findFirst("select * from PL_ProdFormD where iProdFormParamId=?", object.getLong("iAutoId"));
							if (ObjUtil.isNotNull(prodFormD)) {
								List<ProdformdLine> list = prodformdLineService.findByProdFormDId(prodFormD.getIAutoId());
								object.set("cValue",list.get(0).getCValue());
								object.set("iStdVal",prodFormD.getIStdVal());
								object.set("iMaxVal",prodFormD.getIMaxVal());
								object.set("iMinVal",prodFormD.getIMinVal());
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
		ProdFormM prodFormM2= findById(formJsonData.getString("prodFormM.iautoid"));


		//主表数据
		if (StrUtil.isBlank(formJsonData.getString("prodFormM.iautoid"))) {
			if (ObjUtil.isNull(prodFormM2)) {
				prodFormM2 = new ProdFormM();
			}
			prodFormM2.setIProdFormId(formJsonData.getLong("prodFormM.iProdFormId"));
			prodFormM2.setIWorkRegionMid(formJsonData.getLong("prodFormM.iWorkRegionMid"));
			prodFormM2.setIWorkShiftMid(formJsonData.getLong("prodFormM.iWorkShiftMid"));
			prodFormM2.setDDate(formJsonData.getDate("prodFormM.ddate"));
			//基础数据
			User user = JBoltUserKit.getUser();
			Date date = new Date();
			prodFormM2.setCOrgCode(getOrgCode());
			prodFormM2.setCOrgName(getOrgName());
			prodFormM2.setIOrgId(getOrgId());
			prodFormM2.setICreateBy(user.getId());
			prodFormM2.setCCreateName(user.getName());
			prodFormM2.setDCreateTime(date);
			prodFormM2.setCUpdateName(user.getName());
			prodFormM2.setDUpdateTime(date);
			prodFormM2.setIUpdateBy(user.getId());
			prodFormM2.setIAuditStatus(0);
			ValidationUtils.isTrue(prodFormM2.save(), "保存失败");

			//细表数据
			ArrayList<ProdFormD> prodFormDS = new ArrayList<>();
			ArrayList<ProdformdLine> prodformdLines = new ArrayList<>();
			for (int i = 0; i < qcParamJsonData.size(); i++) {
			JSONObject jsonObject = qcParamJsonData.getJSONObject(i);
				//子表
			ProdFormD prodFormD = new ProdFormD();
			prodFormD.setIAutoId(JBoltSnowflakeKit.me.nextId());
			prodFormD.setIProdFormMid(prodFormM2.getIAutoId());
			prodFormD.setIProdFormId(prodFormM2.getIProdFormId());
			String prodformtableparamid = StrSplitter.split(jsonObject.getString("prodformtableparamid"), COMMA, true, true).get(0);
			prodFormD.setIProdFormParamId(Long.valueOf(prodformtableparamid));
			prodFormD.setISeq(jsonObject.getInteger("iseq"));
			prodFormD.setCProdFormParamIds(jsonObject.getString("ProdParamid"));
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
				ProdformdLine prodformdLine = new ProdformdLine();
				prodformdLine.setIProdFormDid(prodFormD.getIAutoId());
				prodformdLine.setISeq(prodFormD.getISeq());
				if (StrUtil.isNotBlank(jsonObject.getString("cvalue"))){
					prodformdLine.setCValue(jsonObject.getString("cvalue"));
				}
				prodformdLines.add(prodformdLine);
			}
			prodFormDService.batchSave(prodFormDS);
			prodformdLineService.batchSave(prodformdLines);
		}else {
			ProdFormM prodFormM = findById(formJsonData.getString("prodFormM.iautoid"));
			User user = JBoltUserKit.getUser();
			Date date = new Date();
			prodFormM.setCUpdateName(user.getName());
			prodFormM.setDUpdateTime(date);
			prodFormM.setIUpdateBy(user.getId());
			if (ObjUtil.isNotNull(formJsonData.getLong("prodFormM.iProdFormId"))) {
				prodFormM.setIProdFormId(formJsonData.getLong("prodFormM.iProdFormId"));
			}
			if (ObjUtil.isNotNull(formJsonData.getLong("prodFormM.iWorkRegionMid"))) {
				prodFormM.setIWorkRegionMid(formJsonData.getLong("prodFormM.iWorkRegionMid"));
			}
			if (ObjUtil.isNotNull(formJsonData.getLong("prodFormM.iWorkShiftMid"))){
				prodFormM.setIWorkShiftMid(formJsonData.getLong("prodFormM.iWorkShiftMid"));
			}
			if(ObjUtil.isNotNull(formJsonData.getDate("prodFormM.ddate"))){
				prodFormM.setDDate(formJsonData.getDate("prodFormM.ddate"));
			}
			ValidationUtils.isTrue(prodFormM.update(), "修改失败");
			//根据主表id获取数据
			List<ProdFormD> formDList = prodFormDService.findByPid(prodFormM.getIAutoId());
			for (ProdFormD prodFormD : formDList) {
				List<ProdformdLine> list = prodformdLineService.findByProdFormDId(prodFormD.getIAutoId());
				for (ProdformdLine prodformdLine : list) {
					prodformdLine.delete();
				}
				prodFormD.delete();
			}
			//细表数据
			ArrayList<ProdFormD> prodFormDS = new ArrayList<>();
			ArrayList<ProdformdLine> prodformdLines = new ArrayList<>();
			for (int i = 0; i < qcParamJsonData.size(); i++) {
				JSONObject jsonObject = qcParamJsonData.getJSONObject(i);
				//子表
				ProdFormD prodFormD = new ProdFormD();
				prodFormD.setIAutoId(JBoltSnowflakeKit.me.nextId());
				prodFormD.setIProdFormMid(prodFormM.getIAutoId());
				prodFormD.setIProdFormId(prodFormM.getIProdFormId());
				String prodformtableparamid = StrSplitter.split(jsonObject.getString("prodformtableparamid"), COMMA, true, true).get(0);
				prodFormD.setIProdFormParamId(Long.valueOf(prodformtableparamid));
				prodFormD.setISeq(jsonObject.getInteger("iseq"));
				prodFormD.setCProdFormParamIds(jsonObject.getString("ProdParamid"));
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
				ProdformdLine prodformdLine = new ProdformdLine();
				prodformdLine.setIProdFormDid(prodFormD.getIAutoId());
				prodformdLine.setISeq(prodFormD.getISeq());
				if (StrUtil.isNotBlank(jsonObject.getString("cvalue"))){
					prodformdLine.setCValue(jsonObject.getString("cvalue"));
				}
				prodformdLine.save();
			}
			prodFormDService.batchSave(prodFormDS);
			//prodformdLineService.batchSave(prodformdLines);

		}

		return successWithData(prodFormM2.keep("iautoid"));
	}

	/**
	 * 提审前业务，如有异常返回错误信息
	 */
	@Override
	public String preSubmitFunc(long formAutoId) {
		ProdFormM prodFormM = findById(formAutoId);

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
		ProdFormM formUploadM = findById(formAutoId);
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
		ProdFormM formUploadM = findById(formAutoId);
		// 审核状态修改
		formUploadM.setIAuditStatus(AuditStatusEnum.REJECTED.getValue());
		formUploadM.setIUpdateBy(JBoltUserKit.getUserId());
		formUploadM.setCUpdateName(JBoltUserKit.getUserName());
		formUploadM.setDUpdateTime(new Date());
		formUploadM.setIAuditBy(JBoltUserKit.getUserId());
		formUploadM.setCAuditName(JBoltUserKit.getUserName());
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
			ProdFormM formUploadM = findById(formAutoId);
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
			ProdFormM prodFormM = findById(formAutoId);
			// 审核状态修改
			prodFormM.setIAuditStatus(AuditStatusEnum.REJECTED.getValue());
			prodFormM.setIUpdateBy(JBoltUserKit.getUserId());
			prodFormM.setCUpdateName(JBoltUserKit.getUserName());
			prodFormM.setDUpdateTime(new Date());
			prodFormM.setIAuditBy(JBoltUserKit.getUserId());
			prodFormM.setCAuditName(JBoltUserKit.getUserName());
			prodFormM.setDSubmitTime(new Date());
			prodFormM.update();
		}
		return null;
	}

	@Override
	public String postBatchBackout(List<Long> formAutoIds) {
		return null;
	}
}