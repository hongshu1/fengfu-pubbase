package cn.rjtech.admin.formuploadm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.admin.formuploadcategory.FormUploadCategoryService;
import cn.rjtech.admin.formuploadd.FormUploadDService;
import cn.rjtech.admin.workregionm.WorkregionmService;
import cn.rjtech.model.momdata.FormUploadD;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;

import java.util.*;

import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.FormUploadM;
import com.jfinal.plugin.activerecord.Record;


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

		List<Record> saveRecords = jBoltTable.getSaveRecordList();
		if (formRecord.getStr("cattachments")!=null){
			if (CollUtil.isNotEmpty(saveRecords)) {
				Record record = saveRecords.get(saveRecords.size() - 1);
				if (!Objects.equals(record.getStr("cattachments"), formRecord.getStr("cattachments"))) {
					saveRecords.add(new Record().set("cattachments",formRecord.getStr("cattachments")));
				}
			}
		}
		if (jBoltTable.saveIsNotBlank()&&jBoltTable.updateIsBlank()&&!"".equals(formRecord.getStr("operationType"))){
			return  fail("请先保存数据！");
		}
		//附件不可为空
		if("".equals(formRecord.getStr("formUploadM.iAutoId"))){
			if (jBoltTable.saveIsBlank()&&jBoltTable.updateIsBlank()) {
				return  fail("附件不可为空！");
			}
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
				//审核
				if (ObjUtil.isNotNull(formRecord.getStr("operationType"))&&!"".equals(formRecord.getStr("operationType"))) {
					if (formRecord.getInt("operationType")==2||formRecord.getInt("operationType")==3) {

						formUploadM1.setIAuditStatus(formRecord.getInt("operationType"));
						formUploadM1.setIAuditBy(user.getId());
						formUploadM1.setCAuditName(user.getName());
						formUploadM1.setDAuditTime(new Date());
					}
				 if(formRecord.getInt("operationType")==1){
						formUploadM1.setIAuditStatus(formRecord.getInt("operationType"));
						formUploadM1.setDSubmitTime(new Date());
					}
					//反审
					if (formRecord.getInt("operationType")==10){
						formUploadM1.setIAuditStatus(1);
						formUploadM1.setDAuditTime(new Date());
					}
				}else {
					formUploadM1.setIWorkRegionMid(formRecord.getLong("formUploadM.iWorkRegionMid"));
					formUploadM1.setICategoryId(formRecord.getLong("formUploadM.iCategoryId"));
					formUploadM1.setDDate(formRecord.getDate("formUploadM.ddate"));
				}


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
				"已保存" : formUploadM.getIAuditStatus() == 1 ?
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
	public Ret saveTableSubmitApi(Kv kv) {

		Record record = JSON.parseObject(kv.getStr("formuploadm"), Record.class);
		List<Kv> array = JSON.parseArray("formuploadds", Kv.class);
		tx(() -> {
			//修改
			if (ObjUtil.isNotNull(record.getStr("iautoid"))){
				FormUploadM formUploadM = findById(record.getStr("iautoid"));
				formUploadM.setIWorkRegionMid(record.getLong("iworkregionmid"));
				formUploadM.setICategoryId(record.getLong("icategoryid"));
				formUploadM.setDDate(record.getDate("ddate"));
				formUploadM.setDUpdateTime(new Date());
				formUploadM.setIUpdateBy(JBoltUserKit.getUserId());
				formUploadM.setCUpdateName(JBoltUserKit.getUserName());
				//细表数据
				for (Kv kv1 : array) {
					FormUploadD formUploadD = formUploadDService.findById(kv1.getStr("iautoid"));
					formUploadD.setCMemo(kv1.getStr("cmemo"));
					formUploadD.setCAttachments(kv1.getStr("cattachments"));
				}

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
				formUploadM.setIWorkRegionMid(record.getLong("iworkregionmid"));
				formUploadM.setICategoryId(record.getLong("icategoryid"));
				formUploadM.setDDate(record.getDate("ddate"));
				formUploadM.setIAuditStatus(0);
				ValidationUtils.isTrue(formUploadM.save(), "保存失败");
				//子表数据
				ArrayList<FormUploadD> formUploadDS = new ArrayList<>();
				for (Kv kv1 : array) {
					FormUploadD formUploadD = new FormUploadD();
					formUploadD.setIFormUploadMid(formUploadM.getIAutoId());
					formUploadD.setCMemo(kv1.getStr("cmemo"));
					formUploadD.setCAttachments(kv1.getStr("cattachments"));
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
	public Map<String,Object> detailsApi(Integer pageNumber, Integer pageSize, Kv iautoid) {
		Map<String, Object> map = new HashMap<>();
		FormUploadM formUploadM = findById(iautoid);
		map.put("formuploadm",formUploadM);
		Page<Record> page = formUploadDService.findByPid2(pageNumber, pageSize, formUploadM.getIAutoId());
		map.put("page",page);
		return map;
	}
}