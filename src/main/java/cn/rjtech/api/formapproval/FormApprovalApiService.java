package cn.rjtech.api.formapproval;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.permission.JBoltUserAuthKit;
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.cache.AuditFormConfigCache;
import cn.rjtech.enums.FormAuditConfigTypeEnum;
import cn.rjtech.model.momdata.Person;
import cn.rjtech.util.MsgEventUtil;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * 表单审批流 Service
 *
 * @ClassName: FormApprovalApiService
 * @author: RJ
 * @date: 2023-04-18 17:26
 */
public class FormApprovalApiService extends JBoltApiBaseService {

    @Inject
    private FormApprovalService service;
    @Inject
    private FormApprovalService adminService;

    /**
     * 判断走审核还是审批
     * 审批的话 保存审批流的基础信息、审批节点信息、节点的详细信息
     * 保存 审批流的审批流程的人员信息
     */
    public JBoltApiRet submit(String formSn, Long formAutoId, String primaryKeyName, String className) {
        Ret ret = service.submit(formSn, formAutoId, primaryKeyName, className);
        return ret.isOk() ? JBoltApiRet.API_SUCCESS : JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }

    /**
     * 适配app 提审接口
     * @return
     */
    public JBoltApiRet submitByJson(JBoltPara submitData){

        JSONArray data = submitData.getJSONArray("data");

        LOG.info("DATA={}",data);

        List<String> errMsg = new ArrayList<>();

        if (data != null){
            for (int i = 0; i < data.size(); i++) {
                JSONObject jsonObject = data.getJSONObject(i);
                String formSn = jsonObject.getString("formSn");
                Long formAutoId = jsonObject.getLong("formAutoId");
                String primaryKeyName = jsonObject.getString("primaryKeyName");
                String className = jsonObject.getString("className");
                String permissionKey = jsonObject.getString("permissionKey");

                ValidationUtils.notBlank(formSn, "缺少表单编码");
                ValidationUtils.validateId(formAutoId, "单据ID");
                ValidationUtils.notBlank(primaryKeyName, "单据ID命名");
                ValidationUtils.notBlank(className, "缺少实现审批通过业务的类名参数");
                ValidationUtils.notBlank(permissionKey, "缺少permissionKey");
                ValidationUtils.isTrue(JBoltUserAuthKit.hasPermission(JBoltUserKit.getUserId(), permissionKey), "您缺少单据的提审权限");

                Ret ret = service.submit(formSn, formAutoId, primaryKeyName, className);
                if (ret.isFail()){

                    // 获取单据信息
                    Record formData = service.getApprovalForm(formSn, primaryKeyName, formAutoId);
                    String billno = isOk(formData.getStr("billno"))?formData.getStr("billno"):Long.toString(formAutoId);
                    errMsg.add("单据："+billno+","+ ret.getStr("msg"));
                } else {

                    // 审批流处理时，获取审批人推送消息
                    if (FormAuditConfigTypeEnum.toEnum(AuditFormConfigCache.ME.getAuditWay(formSn)) == FormAuditConfigTypeEnum.FLOW) {
                        List<Long> list = adminService.getNextApprovalUserIds(formAutoId, 10);
                        if (CollUtil.isNotEmpty(list)) {
                            MsgEventUtil.postApprovalMsgEvent(JBoltUserKit.getUserId(), formSn, primaryKeyName, formAutoId, list);
                        }
                    }
                }
            }

            if (errMsg.size()>0){
               return JBoltApiRet.API_FAIL(CollUtil.join(errMsg, StrUtil.COMMA));
            }

        } else {
            return JBoltApiRet.API_FAIL("没有单据数据");
        }

        return JBoltApiRet.API_SUCCESS;
    }

    /**
     * 审批通过
     *
     * @param formAutoId     单据ID
     * @param formSn         表单
     * @param status         状态
     * @param primaryKeyName 主键名
     * @param isWithinBatch  是否批量审批处理
     * @param remark         审批意见
     */
    public JBoltApiRet approve(Long formAutoId, String formSn, int status, String primaryKeyName, String className, boolean isWithinBatch, String remark) {
        Ret ret = service.approve(formAutoId, formSn, status, primaryKeyName, className, isWithinBatch, remark);
        return ret.isOk() ? JBoltApiRet.API_SUCCESS : JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }

    /**
     * 审批不通过
     */
    public JBoltApiRet reject(Long formAutoId, String formSn, int status, String primaryKeyName, String className, String remark, boolean isWithinBatch) {
        Ret ret = service.reject(formAutoId, formSn, status, primaryKeyName, className, remark, isWithinBatch);

        return ret.isOk() ? JBoltApiRet.API_SUCCESS : JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }

    /**
     * 反审批
     *
     * @param formAutoId     单据ID
     * @param formSn         表单
     * @param primaryKeyName 主键名
     * @param status         状态
     * @param className      处理审批Service的类名
     */
    public Ret reverseApprove(Long formAutoId, String formSn, String primaryKeyName, int status, String className, String remark) {

        Ret ret = service.reverseApprove(formAutoId, formSn, primaryKeyName, status, className, remark);

        return ret.isOk() ? JBoltApiRet.API_SUCCESS : JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }


    /**
     * 撤回，已适配审核/审批流
     *
     * @param formSn         表名
     * @param primaryKeyName 主键名
     * @param formAutoId     单据ID
     * @param className      实现审批业务的类名
     */
    public JBoltApiRet withdraw(String formSn, String primaryKeyName, long formAutoId, String className) {

        Ret ret = service.withdraw(formSn, primaryKeyName, formAutoId, className);

        return ret.isOk() ? JBoltApiRet.API_SUCCESS : JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }

    /**
     * 批量审批
     */
    public JBoltApiRet batchApprove(String ids, String formSn, String primaryKeyName, String className) {

        Ret ret = service.batchApprove(ids, formSn, primaryKeyName, className);

        return ret.isOk() ? JBoltApiRet.API_SUCCESS : JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }

    /**
     * 批量审核不通过
     */
    public JBoltApiRet batchReject(String ids, String formSn, String primaryKeyName, String className) {
        Ret ret = service.batchReject(ids, formSn, primaryKeyName, className);

        return ret.isOk() ? JBoltApiRet.API_SUCCESS : JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }

//    /**
//     * 批量反审批
//     */
//    public JBoltApiRet batchReverseApprove(String ids, String formSn, String primaryKeyName, String className) {
//
//         Ret ret = service.batchReject(ids, formSn, primaryKeyName, className);
//         return ret.isOk()?JBoltApiRet.API_SUCCESS:JBoltApiRet.API_FAIL(ret.getStr("msg"));
//    }

    /**
     * 批量撤销审批
     */
    public JBoltApiRet batchBackout(String ids, String formSn, String primaryKeyName, String className) {
        Ret ret = service.batchBackout(ids, formSn, primaryKeyName, className);

        return ret.isOk() ? JBoltApiRet.API_SUCCESS : JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }

    /**
     * 查询审批过程待审批的人员
     */
    public JBoltApiRet getNextApprovalUserIds(Long formAutoId, Integer size) {
        List<Long> list = service.getNextApprovalUserIds(formAutoId, size);
        return JBoltApiRet.successWithData(list);
    }

    /**
     * 查询审批过程待审批的人员
     */
    public List<Long> getNextApprovalUserNames(Long formAutoId, Integer size) {
        return null;
    }

    /**
     * 判断是否为审批第一人
     */
    public Boolean isFirstReverse(Long formId) {
        return null;
    }

    /**
     * 根据用户ID获取人员信息
     */
    public Person findPersonByUserId(Long userId) {
        return null;
    }

    /**
     * 审核通过，公用实现
     *
     * @param formSn         表单编码
     * @param formAutoId     单据ID
     * @param primaryKeyName 主键名
     * @param className      类名
     * @param isWithinBatch  是否为批量审核调用
     */
    public JBoltApiRet approveByStatus(String formSn, long formAutoId, String primaryKeyName, String className, boolean isWithinBatch, long userId) {
        Ret ret = service.approveByStatus(formSn, formAutoId, primaryKeyName, className, isWithinBatch, userId);

        return ret.isOk() ? JBoltApiRet.API_SUCCESS : JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }

    /**
     * 审核不通过，公用实现
     *
     * @param formSn         表单编码
     * @param formAutoId     单据ID
     * @param primaryKeyName 主键名
     * @param className      类名
     * @param isWithinBatch  是否为批处理
     */
    public JBoltApiRet rejectByStatus(String formSn, Long formAutoId, String primaryKeyName, String className, boolean isWithinBatch, long userId) {
        Ret ret = service.rejectByStatus(formSn, formAutoId, primaryKeyName, className, isWithinBatch, userId);

        return ret.isOk() ? JBoltApiRet.API_SUCCESS : JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }

    /**
     * 反审核，公用实现
     *
     * @param formSn         表单编码
     * @param formAutoId     单据ID
     * @param primaryKeyName 主键名
     * @param className      类名
     */
    public JBoltApiRet reverseApproveByStatus(String formSn, Long formAutoId, String primaryKeyName, String className, long userId) {
        Ret ret = service.reverseApproveByStatus(formSn, formAutoId, primaryKeyName, className, userId);

        return ret.isOk() ? JBoltApiRet.API_SUCCESS : JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }

    /**
     * 批量审核通过，公用实现
     *
     * @param ids            单据IDs
     * @param formSn         表单编码
     * @param primaryKeyName 主键名
     * @param className      实现审批业务的类名
     */
    public JBoltApiRet batchApproveByStatus(String ids, String formSn, String primaryKeyName, String className, long userId) {
        Ret ret = service.batchApproveByStatus(ids, formSn, primaryKeyName, className, userId);

        return ret.isOk() ? JBoltApiRet.API_SUCCESS : JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }

    /**
     * 批量审核不通过，公用实现
     *
     * @param ids            单据IDs
     * @param formSn         表单编码
     * @param primaryKeyName 主键名
     * @param className      实现审批业务的类名
     */
    public JBoltApiRet batchRejectByStatus(String ids, String formSn, String primaryKeyName, String className, long userId) {
        Ret ret = service.batchRejectByStatus(ids, formSn, primaryKeyName, className, userId);

        return ret.isOk() ? JBoltApiRet.API_SUCCESS : JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }

}
