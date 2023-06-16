package cn.rjtech.admin.formuploadm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.admin.formuploadcategory.FormUploadCategoryService;
import cn.rjtech.admin.formuploadd.FormUploadDService;
import cn.rjtech.admin.workregionm.WorkregionmService;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.model.momdata.FormUploadD;
import cn.rjtech.model.momdata.FormUploadM;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.*;
import java.util.stream.Collectors;

import static cn.hutool.core.text.StrPool.COMMA;


/**
 * 记录上传
 * @ClassName: FormUploadMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-29 15:35
 */
public class FormUploadMService extends BaseService<FormUploadM> {
	private final FormUploadM dao=new FormUploadM().dao();
	@Override
	protected FormUploadM dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	@Inject
	private FormUploadDService formUploadDService;
	@Inject
	private WorkregionmService workregionmService;
	@Inject
	private FormUploadCategoryService formUploadCategoryService;
	@Inject
	private FormApprovalService formApprovalService;
	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv para) {
		Page<Record> paginate = dbTemplate("formuploadm.getAdminDatas", para).paginate(pageNumber, pageSize);
		for (Record record : paginate.getList()) {
			if (ObjectUtil.isNotNull(workregionmService.findById(record.getStr("iworkregionmid")))){
				record.set("iworkregionmid",workregionmService.findById(record.getStr("iworkregionmid")).getCWorkName());
			}
			if (ObjUtil.isNotNull(formUploadCategoryService.findById(record.getStr("icategoryid")))){
				record.set("icategoryid",formUploadCategoryService.findById(record.getStr("icategoryid")).getCCategoryName());
			}
		}
		return  paginate;
	}

	/**
	 * 保存
	 * @param formUploadM
	 * @return
	 */
	public Ret save(FormUploadM formUploadM) {
		if(formUploadM==null || isOk(formUploadM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(formUploadM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formUploadM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(formUploadM.getIAutoId(), JBoltUserKit.getUserId(), formUploadM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param formUploadM
	 * @return
	 */
	public Ret update(FormUploadM formUploadM) {
		if(formUploadM==null || notOk(formUploadM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		FormUploadM dbFormUploadM=findById(formUploadM.getIAutoId());
		if(dbFormUploadM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(formUploadM.getName(), formUploadM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formUploadM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(formUploadM.getIAutoId(), JBoltUserKit.getUserId(), formUploadM.getName());
		}
		return ret(success);
	}

	/**
	 *批量保存
	 */
    public Ret saveTableSubmit( JBoltTable jBoltTable) {
		Record formRecord = jBoltTable.getFormRecord();
		ValidationUtils.notNull(jBoltTable.getFormRecord(), JBoltMsg.PARAM_ERROR);
		FormUploadM formUploadM = new FormUploadM();
		//图片数据处理
		List<Record> saveRecords = jBoltTable.getSaveRecordList();
		if (StrUtil.isNotBlank(formRecord.getStr("cattachments"))){
			if (CollUtil.isNotEmpty(saveRecords)) {
				for (Record saveRecord : saveRecords) {
					if (formRecord.getStr("cattachments").contains(saveRecord.getStr("cattachments"))) {
						String replace = formRecord.getStr("cattachments").replace(","+saveRecord.getStr("cattachments"), "");
						formRecord.set("cattachments",replace);
					}
				}
			}
		}
		if (jBoltTable.saveIsNotBlank()&&jBoltTable.updateIsBlank()&&!"".equals(formRecord.getStr("operationType"))){
			return  fail("请先保存数据！");
		}
		int size = formUploadDService.findByPid(formRecord.getLong("formUploadM.iAutoId")).size();

		if (jBoltTable.saveIsBlank()&&jBoltTable.updateIsBlank()&& StrUtil.isBlank(formRecord.getStr("cattachments"))
				&&size<=0) {
				return  fail("附件不可为空！");
			}

		//主表数据
		User user = JBoltUserKit.getUser();
		tx(() -> {

			if (notOk(formRecord.getStr("formUploadM.iAutoId"))) {
				//基础数据
				formUploadM.setCOrgCode(getOrgCode());
				formUploadM.setCOrgName(getOrgName());
				formUploadM.setCFormSn("PL_FormUploadM");
				formUploadM.setIOrgId(getOrgId());
				formUploadM.setICreateBy(user.getId());
				formUploadM.setCCreateName(user.getName());
				formUploadM.setDCreateTime(new Date());
				formUploadM.setCUpdateName(user.getName());
				formUploadM.setDUpdateTime(new Date());
				formUploadM.setIUpdateBy(user.getId());
				formUploadM.setIWorkRegionMid(formRecord.getLong("formUploadM.iWorkRegionMid"));
				formUploadM.setICategoryId(formRecord.getLong("formUploadM.iCategoryId"));
				formUploadM.setDDate(formRecord.getDate("formUploadM.ddate"));
				formUploadM.setIAuditStatus(0);
				ValidationUtils.isTrue(formUploadM.save(), "保存失败");
			}else {
				FormUploadM formUploadM1 = findById(formRecord.getStr("formUploadM.iAutoId"));
					formUploadM1.setIWorkRegionMid(formRecord.getLong("formUploadM.iWorkRegionMid"));
					formUploadM1.setICategoryId(formRecord.getLong("formUploadM.iCategoryId"));
					formUploadM1.setDDate(formRecord.getDate("formUploadM.ddate"));
				formUploadM1.update();
			}
			if (jBoltTable.saveIsNotBlank()){
				ArrayList<FormUploadD> formUploadDS = new ArrayList<>();
				for (Record saveRecord : saveRecords) {
					FormUploadD formUploadD = new FormUploadD();
					if (ObjUtil.isNotNull(formRecord.getStr("formUploadM.iAutoId"))) {
						formUploadD.setIFormUploadMid(formRecord.getLong("formUploadM.iAutoId"));
					}else {
						formUploadD.setIFormUploadMid(formUploadM.getIAutoId());
					}
					formUploadD.setCAttachments(saveRecord.getStr("cattachments"));
					formUploadD.setCMemo(saveRecord.getStr("cmemo"));
					formUploadDS.add(formUploadD);
				}
				for (String cattachment : StrSplitter.split(formRecord.getStr("cattachments"), COMMA, true, true)) {
					FormUploadD formUploadD = new FormUploadD();
					if (ObjUtil.isNotNull(formRecord.getStr("formUploadM.iAutoId"))) {
						formUploadD.setIFormUploadMid(formRecord.getLong("formUploadM.iAutoId"));
					}else {
						formUploadD.setIFormUploadMid(formUploadM.getIAutoId());
					}
					formUploadD.setCAttachments(cattachment);
					formUploadDS.add(formUploadD);
				}
				formUploadDService.batchSave(formUploadDS);
			}else {
				ArrayList<FormUploadD> formUploadDS = new ArrayList<>();
				for (String cattachment : StrSplitter.split(formRecord.getStr("cattachments"), COMMA, true, true)) {
					FormUploadD formUploadD = new FormUploadD();
					if (ObjUtil.isNotNull(formRecord.getStr("formUploadM.iAutoId"))) {
						formUploadD.setIFormUploadMid(formRecord.getLong("formUploadM.iAutoId"));
					}else {
						formUploadD.setIFormUploadMid(formUploadM.getIAutoId());
					}
					formUploadD.setCAttachments(cattachment);
					formUploadDS.add(formUploadD);
				}
				formUploadDService.batchSave(formUploadDS);
			}
			if (jBoltTable.updateIsNotBlank()){
				ArrayList<FormUploadD> formUploadDS = new ArrayList<>();
				List<Record> updateRecordList = jBoltTable.getUpdateRecordList();
				for (Record updateRecord : updateRecordList) {
					FormUploadD formUploadD = formUploadDService.findById(updateRecord.getStr("iautoid"));
					formUploadD.setCAttachments(updateRecord.getStr("cattachments"));
					formUploadD.setCMemo(updateRecord.getStr("cmemo"));
					formUploadDS.add(formUploadD);
				}
				formUploadDService.batchUpdate(formUploadDS);
			}

			if (jBoltTable.deleteIsNotBlank()) {
				formUploadDService.deleteByIds(jBoltTable.getDelete());
			}
			return true;
		});
		return	success("autoid",String.valueOf(formUploadM.getIAutoId()));
	}

	/**
	 * 批量生成
	 */
	public Ret batchHandle(Kv kv, int status) {
		List<Record> records = getDatasByIds(kv);
		//主表数据
		User user = JBoltUserKit.getUser();
		if (records != null && records.size() > 0) {
			for (Record record : records) {
				record.set("iauditstatus", status);
				record.set("iauditby",user.getId());
				record.set("daudittime",new Date());
				updateRecord(record);
			}
			//batchUpdateRecords(records);
		}
		return SUCCESS;
	}


	public List<Record> getDatasByIds(Kv kv) {
		String ids = kv.getStr("ids");
		if (ids != null) {
			String[] split = ids.split(",");
			StringBuilder sqlids = new StringBuilder();
			for (String id : split) {
				sqlids.append("'").append(id).append("',");
			}
			ValidationUtils.isTrue(sqlids.length() > 0, "请至少选择一条数据!");
			sqlids = new StringBuilder(sqlids.substring(0, sqlids.length() - 1));
			kv.set("sqlids", sqlids.toString());
		}
		return dbTemplate("formuploadm.getDatasByIds", kv).find();
	}

	/**
	 * 删除
	 */
	public Ret delete(Long id) {
		FormUploadM formUploadM = findById(id);
		String s = formUploadM.getIAuditStatus() == 0 ?
				"未审核" : formUploadM.getIAuditStatus() == 1 ?
				"待审核" : formUploadM.getIAuditStatus() == 2 ? "审核通过" : "审核不通过";
		if (formUploadM.getIAuditStatus()!=0) {
			return  fail("该数据状态为："+s+"不可删除！");
		}
		List<Record> service = formUploadDService.findByPid(id);
		if (service.size()>0) {
			StringBuilder sqlids = new StringBuilder();
			for (Record record : service) {
				sqlids.append(record.getStr("iautoid")).append(",");
			}
			sqlids = new StringBuilder(sqlids.substring(0, sqlids.length() - 1));
			formUploadDService.deleteByIds(sqlids.toString(),",");
		}

		deleteById(id);
		return ret(true);
	}

	/**
	 *Api接口首页数据
	 */
	public Page<Record> getApiAdminDatas(Integer pageNumber, Integer pageSize, Kv para) {
		Page<Record> paginate = dbTemplate("formuploadm.getAdminDatas", para).paginate(pageNumber, pageSize);
		for (Record record : paginate.getList()) {
			if (ObjectUtil.isNotNull(workregionmService.findById(record.getStr("iworkregionmid")))){
				record.set("iworkregionmname",workregionmService.findById(record.getStr("iworkregionmid")).getCWorkName());
			}
			if (ObjUtil.isNotNull(formUploadCategoryService.findById(record.getStr("icategoryid")))){
				record.set("icategoryidname",formUploadCategoryService.findById(record.getStr("icategoryid")).getCCategoryName());
			}
		}
		return  paginate;
	}

	/**
	 * 主表数据
	 */
	public Ret saveTableSubmitApi(String iautoid, String iworkregionmid, String icategoryid, Date ddate, JSONArray formuploaddsv) {

		tx(() -> {
			//修改
			if (ObjUtil.isNotNull(iautoid)){
				FormUploadM formUploadM = findById(iautoid);
				formUploadM.setIWorkRegionMid(Long.parseLong(iworkregionmid));
				formUploadM.setICategoryId(Long.parseLong(icategoryid));
				formUploadM.setDDate(ddate);
				formUploadM.setDUpdateTime(new Date());
				formUploadM.setIUpdateBy(JBoltUserKit.getUserId());
				formUploadM.setCUpdateName(JBoltUserKit.getUserName());
				ArrayList<FormUploadD> formUploadDS = new ArrayList<>();
				//细表数据
				for (int i = 0; i < formuploaddsv.size(); i++) {
					JSONObject jsonObject = formuploaddsv.getJSONObject(i);
					FormUploadD formUploadD = formUploadDService.findById(jsonObject.getString("iautoid"));
					formUploadD.setCMemo(jsonObject.getString("cmemo"));
					formUploadD.setCAttachments(jsonObject.getString("cattachments"));
					formUploadDS.add(formUploadD);
				}
				ValidationUtils.isTrue(formUploadM.update(), "修改失败");
				formUploadDService.batchUpdate(formUploadDS);
				//新增
			}else {
				FormUploadM formUploadM = new FormUploadM();
				//基础数据
				formUploadM.setCOrgCode(getOrgCode());
				formUploadM.setCOrgName(getOrgName());
				formUploadM.setCFormSn("PL_FormUploadM");
				formUploadM.setIOrgId(getOrgId());
				formUploadM.setICreateBy(JBoltUserKit.getUserId());
				formUploadM.setCCreateName(JBoltUserKit.getUserName());
				formUploadM.setDCreateTime(new Date());
				formUploadM.setCUpdateName(JBoltUserKit.getUserName());
				formUploadM.setDUpdateTime(new Date());
				formUploadM.setIUpdateBy(JBoltUserKit.getUserId());
				formUploadM.setIWorkRegionMid(Long.parseLong(iworkregionmid));
				formUploadM.setICategoryId(Long.parseLong(icategoryid));
				formUploadM.setDDate(ddate);
				formUploadM.setIAuditStatus(0);
				ValidationUtils.isTrue(formUploadM.save(), "保存失败");
				//子表数据
				ArrayList<FormUploadD> formUploadDS = new ArrayList<>();
				for (int i = 0; i < formuploaddsv.size(); i++) {
					JSONObject jsonObject = formuploaddsv.getJSONObject(i);
					FormUploadD formUploadD = new FormUploadD();
					formUploadD.setIFormUploadMid(formUploadM.getIAutoId());
					formUploadD.setCMemo(jsonObject.getString("cmemo"));
					formUploadD.setCAttachments(jsonObject.getString("cattachments"));
					formUploadDS.add(formUploadD);
				}
				formUploadDService.batchSave(formUploadDS);
			}



			return true;
		});
		return SUCCESS;
	}

	/**
	 * 修改详情API接口
	 */
	public Map<String,Object> detailsApi(Integer pageNumber, Integer pageSize, Kv kv) {
		Map<String, Object> map = new HashMap<>();
		FormUploadM formUploadM = findById(kv.getStr("iautoid"));
		map.put("formuploadm",formUploadM);
		Page<Record> page = formUploadDService.findByPid2(pageNumber, pageSize, formUploadM.getIAutoId());
		map.put("page",page);
		return map;
	}

	/**
	 * 提交审批
	 */
	public Ret submit(Long iautoid) {
		tx(() -> {
			Ret ret = formApprovalService.submit(table(), iautoid, primaryKey(),"");
			ValidationUtils.isTrue(ret.isOk(), ret.getStr("msg"));

			FormUploadM formUploadM = findById(iautoid);
			formUploadM.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
			formUploadM.setIAuditWay(1);
			formUploadM.setIUpdateBy(JBoltUserKit.getUserId());
			formUploadM.setCUpdateName(JBoltUserKit.getUserName());
			formUploadM.setDUpdateTime(new Date());
			ValidationUtils.isTrue(formUploadM.update(), JBoltMsg.FAIL);
			return true;
		});
		return SUCCESS;
	}

	/**
	 * 审批通过
	 */
	public Ret approve(Long iautoid) {
		tx(() -> {
			// 校验订单状态
			FormUploadM formUploadM = findById(iautoid);
			ValidationUtils.equals(AuditStatusEnum.AWAIT_AUDIT.getValue(), formUploadM.getIAuditStatus(), "该记录非待审核状态");
			formApprovalService.approveByStatus(table(), primaryKey(), iautoid, (fromAutoId) -> null, (fromAutoId) -> {
				ValidationUtils.isTrue(updateColumn(iautoid, "iAuditStatus", AuditStatusEnum.APPROVED.getValue()).isOk(), JBoltMsg.FAIL);
				return null;
			});
			return true;
		});
		return SUCCESS;
	}

	/**
	 * 审批不通过
	 */
	public Ret reject(Long iautoid) {
		tx(() -> {
			// 数据同步暂未开发 现只修改状态
			formApprovalService.rejectByStatus(table(), primaryKey(), iautoid, (fromAutoId) -> null, (fromAutoId) -> {
				ValidationUtils.isTrue(updateColumn(iautoid, "iAuditStatus", AuditStatusEnum.REJECTED.getValue()).isOk(), JBoltMsg.FAIL);
				return null;
			});
			return true;
		});
		return SUCCESS;
	}

	/**
	 * 批量审核
	 * @param ids
	 * @return
	 */
	public Ret batchApprove(String ids) {
		tx(() -> {
			String table = table();
			String primaryKey = primaryKey();
			formApprovalService.batchApproveByStatus(table,primaryKey , ids, (formAutoId) -> null, (formAutoId) -> {
				List<FormUploadM> list = getListByIds(ids);
				list = list.stream().filter(Objects::nonNull).map(item -> {
					item.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
					return item;
				}).collect(Collectors.toList());
				ValidationUtils.isTrue(batchUpdate(list).length > 0, JBoltMsg.FAIL);

				return null;
			});
			return true;
		});
		return SUCCESS;
	}

	/**
	 * 批量反审批
	 *
	 * @param ids
	 * @return
	 */
	public Ret batchReverseApprove(String ids) {
		tx(() -> {
			List<FormUploadM> list = getListByIds(ids);
			// 非已审批数据
			List<FormUploadM> noApprovedDatas = list.stream().filter(item -> !(item.getIAuditStatus() == AuditStatusEnum.APPROVED.getValue())).collect(Collectors.toList());
			ValidationUtils.isTrue(noApprovedDatas.size() <= 0, "存在非已审批数据");

			for (FormUploadM formuploadm : list) {
				// 处理订单审批数据
				formApprovalService.reverseApproveByStatus(formuploadm.getIAutoId(), table(), primaryKey(), (formAutoId) -> null, (formAutoId) ->
				{
					// 处理订单状态
					formuploadm.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
					return null;
				});
			}

			ValidationUtils.isTrue(batchUpdate(list).length > 0, "批量反审批失败");

			return true;
		});
		return SUCCESS;
	}
}