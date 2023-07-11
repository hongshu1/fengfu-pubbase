package cn.rjtech.admin.formuploadm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.formuploadcategory.FormUploadCategoryService;
import cn.rjtech.admin.formuploadd.FormUploadDService;
import cn.rjtech.admin.workregionm.WorkregionmService;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.model.momdata.FormUploadD;
import cn.rjtech.model.momdata.FormUploadM;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.*;

/**
 * 记录上传
 *
 * @ClassName: FormUploadMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-29 15:35
 */
public class FormUploadMService extends BaseService<FormUploadM> implements IApprovalService {

    private final FormUploadM dao = new FormUploadM().dao();

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
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv para) {
        Page<Record> paginate = dbTemplate("formuploadm.getAdminDatas", para).paginate(pageNumber, pageSize);
        for (Record record : paginate.getList()) {
            if (ObjUtil.isNotNull(workregionmService.findById(record.getStr("iworkregionmid")))) {
                record.set("iworkregionmid", workregionmService.findById(record.getStr("iworkregionmid")).getCWorkName());
            }
            if (ObjUtil.isNotNull(formUploadCategoryService.findById(record.getStr("icategoryid")))) {
                record.set("icategoryid", formUploadCategoryService.findById(record.getStr("icategoryid")).getCCategoryName());
            }
        }
        return paginate;
    }

    /**
     * 保存
     */
    public Ret save(FormUploadM formUploadM) {
        if (formUploadM == null || isOk(formUploadM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(formUploadM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = formUploadM.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(formUploadM.getIAutoId(), JBoltUserKit.getUserId(), formUploadM.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(FormUploadM formUploadM) {
        if (formUploadM == null || notOk(formUploadM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        FormUploadM dbFormUploadM = findById(formUploadM.getIAutoId());
        if (dbFormUploadM == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(formUploadM.getName(), formUploadM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = formUploadM.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(formUploadM.getIAutoId(), JBoltUserKit.getUserId(), formUploadM.getName());
        }
        return ret(success);
    }

    /**
     * 批量保存
     */
    public Ret saveTableSubmit(JBoltTable jBoltTable) {
        FormUploadM formUploadM = jBoltTable.getFormModel(FormUploadM.class, "formUploadM");

        Record formRecord = jBoltTable.getFormRecord();
        ValidationUtils.notNull(jBoltTable.getFormRecord(), JBoltMsg.PARAM_ERROR);

        if (jBoltTable.saveIsNotBlank() && jBoltTable.updateIsBlank() && !"".equals(formRecord.getStr("operationType"))) {
            return fail("请先保存数据！");
        }
        int size = formUploadDService.findByPid(formRecord.getLong("formUploadM.iAutoId")).size();

        if (jBoltTable.saveIsBlank() && jBoltTable.updateIsBlank() && StrUtil.isBlank(formRecord.getStr("cattachments2"))
                && size <= 0) {
            return fail("附件不可为空！");
        }

        //主表数据
        User user = JBoltUserKit.getUser();
        Date date = new Date();
        tx(() -> {

            if (formUploadM.getIAutoId() == null) {
                //基础数据
                formUploadM.setCOrgCode(getOrgCode());
                formUploadM.setCOrgName(getOrgName());
                formUploadM.setIOrgId(getOrgId());
                formUploadM.setICreateBy(user.getId());
                formUploadM.setCCreateName(user.getName());
                formUploadM.setDCreateTime(date);
                formUploadM.setCUpdateName(user.getName());
                formUploadM.setDUpdateTime(date);
                formUploadM.setIUpdateBy(user.getId());
                formUploadM.setIAuditStatus(0);
                formUploadM.setIAuditWay(2);
                ValidationUtils.isTrue(formUploadM.save(), "保存失败");
            } else {
                formUploadM.setIUpdateBy(user.getId());
                formUploadM.setCUpdateName(user.getName());
                formUploadM.setDUpdateTime(date);
                ValidationUtils.isTrue(formUploadM.update(), "修改失败");
            }

            saveTableSubmitDatas(jBoltTable, formUploadM);
            updateTableSubmitDatas(jBoltTable);
            deleteTableSubmitDatas(jBoltTable);
            return true;
        });
        return successWithData(formUploadM.keep("iautoid"));
    }

    private void deleteTableSubmitDatas(JBoltTable jBoltTable) {
        if (jBoltTable.deleteIsNotBlank()) {
            formUploadDService.deleteByIds(jBoltTable.getDelete());
        }
    }

    private void updateTableSubmitDatas(JBoltTable jBoltTable) {
        if (jBoltTable.updateIsNotBlank()) {
            ArrayList<FormUploadD> formUploadDS = new ArrayList<>();
            List<Record> updateRecordList = jBoltTable.getUpdateRecordList();
            for (Record updateRecord : updateRecordList) {
                FormUploadD formUploadD = formUploadDService.findById(updateRecord.getStr("iautoid"));
                formUploadD.setCAttachments(updateRecord.getStr("cattachments2"));
                formUploadD.setCMemo(updateRecord.getStr("cmemo"));
                formUploadDS.add(formUploadD);
            }
            formUploadDService.batchUpdate(formUploadDS);
        }
    }

    private void saveTableSubmitDatas(JBoltTable jBoltTable, FormUploadM formUploadM) {
        Record formRecord = jBoltTable.getFormRecord();
        //图片数据处理
        List<Record> saveRecords = jBoltTable.getSaveRecordList();
        if (StrUtil.isNotBlank(formRecord.getStr("cattachments2"))) {
            if (CollUtil.isNotEmpty(saveRecords)) {
                for (Record saveRecord : saveRecords) {
                    if (formRecord.getStr("cattachments2").contains(saveRecord.getStr("cattachments2"))) {
                        String replace = formRecord.getStr("cattachments2").replace(saveRecord.getStr("cattachments2"), "");
                        formRecord.set("cattachments2", replace);
                    }
                }
            }
        }

        if (jBoltTable.saveIsNotBlank()) {
            ArrayList<FormUploadD> formUploadDS = new ArrayList<>();
            for (Record saveRecord : saveRecords) {
                FormUploadD formUploadD = new FormUploadD();
                formUploadD.setIFormUploadMid(formUploadM.getIAutoId());
                formUploadD.setCAttachments(saveRecord.getStr("cattachments"));
                formUploadD.setCMemo(saveRecord.getStr("cmemo"));
                formUploadDS.add(formUploadD);
            }
            for (String cattachment : StrSplitter.split(formRecord.getStr("cattachments2"), "-", true, true)) {
                FormUploadD formUploadD = new FormUploadD();
                formUploadD.setIFormUploadMid(formUploadM.getIAutoId());
                formUploadD.setCAttachments(cattachment);
                formUploadDS.add(formUploadD);
            }
            formUploadDService.batchSave(formUploadDS);
        } else {
            ArrayList<FormUploadD> formUploadDS = new ArrayList<>();
            for (String cattachment : StrSplitter.split(formRecord.getStr("cattachments2"), "-", true, true)) {
                if (StrUtil.isNotBlank(cattachment)){
                    FormUploadD formUploadD = new FormUploadD();
                    formUploadD.setIFormUploadMid(formUploadM.getIAutoId());
                    formUploadD.setCAttachments(cattachment);
                    formUploadDS.add(formUploadD);
                }
            }
            formUploadDService.batchSave(formUploadDS);
        }
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
                record.set("iauditby", user.getId());
                record.set("daudittime", new Date());
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
        if (formUploadM.getIAuditStatus() != 0) {
            if (formUploadM.getIAuditStatus()!=3) {
                return fail("该数据状态为：" + s + "不可删除！");
            }
        }
        List<Record> service = formUploadDService.findByPid(id);
        if (service.size() > 0) {
            StringBuilder sqlids = new StringBuilder();
            for (Record record : service) {
                sqlids.append(record.getStr("iautoid")).append(",");
            }
            sqlids = new StringBuilder(sqlids.substring(0, sqlids.length() - 1));
            formUploadDService.deleteByIds(sqlids.toString(), ",");
        }

        deleteById(id);
        return SUCCESS;
    }

    /**
     * Api接口首页数据
     */
    public Page<Record> getApiAdminDatas(Integer pageNumber, Integer pageSize, Kv para) {
        Page<Record> paginate = dbTemplate("formuploadm.getAdminDatas", para).paginate(pageNumber, pageSize);
        for (Record record : paginate.getList()) {
            if (ObjUtil.isNotNull(workregionmService.findById(record.getStr("iworkregionmid")))) {
                record.set("iworkregionmname", workregionmService.findById(record.getStr("iworkregionmid")).getCWorkName());
            }
            if (ObjUtil.isNotNull(formUploadCategoryService.findById(record.getStr("icategoryid")))) {
                record.set("icategoryidname", formUploadCategoryService.findById(record.getStr("icategoryid")).getCCategoryName());
            }
        }
        return paginate;
    }

    /**
     * 主表数据
     */
    public Ret saveTableSubmitApi(Long iautoid, String iworkregionmid, String icategoryid, Date ddate, JSONArray formuploaddsv) {

        tx(() -> {
            //修改
            if (ObjUtil.isNotNull(iautoid)) {
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
            } else {
                FormUploadM formUploadM = new FormUploadM();
                //基础数据
                formUploadM.setCOrgCode(getOrgCode());
                formUploadM.setCOrgName(getOrgName());
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
    public Map<String, Object> detailsApi(Integer pageNumber, Integer pageSize, Kv kv) {
        Map<String, Object> map = new HashMap<>();
        FormUploadM formUploadM = findById(kv.getStr("iautoid"));
        map.put("formuploadm", formUploadM);
        Page<Record> page = formUploadDService.findByPid2(pageNumber, pageSize, formUploadM.getIAutoId());
        map.put("page", page);
        return map;
    }

    /**
     * 提审前业务，如有异常返回错误信息
     */
    @Override
    public String preSubmitFunc(long formAutoId) {
        FormUploadM formuploadm = findById(formAutoId);

        switch (AuditStatusEnum.toEnum(formuploadm.getIAuditStatus())) {
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
        FormUploadM formUploadM = findById(formAutoId);
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
        FormUploadM formUploadM = findById(formAutoId);
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
            FormUploadM formUploadM = findById(formAutoId);
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
            FormUploadM formUploadM = findById(formAutoId);
            // 审核状态修改
            formUploadM.setIAuditStatus(AuditStatusEnum.REJECTED.getValue());
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

    @Override
    public String postBatchBackout(List<Long> formAutoIds) {
        return null;
    }

    /**
     * 批量删除
     */
    public Ret deleteByBatchIds(String ids) {
        List<FormUploadM> list = getListByIds(ids);
        List<FormUploadM> notAuditList = new ArrayList<>();
        for (FormUploadM formuploadm : list) {
            if (AuditStatusEnum.NOT_AUDIT.getValue() != formuploadm.getIAuditStatus()) {
                notAuditList.add(formuploadm);
            }

            formuploadm.setIsDeleted(true);
        }

        ValidationUtils.isTrue(notAuditList.size() == 0, "存在非已保存订单");
        ValidationUtils.isTrue(batchUpdate(list).length > 0, JBoltMsg.FAIL);
        batchUpdate(notAuditList);
        return SUCCESS;
    }
}