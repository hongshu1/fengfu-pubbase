package cn.rjtech.admin.formapproval;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.interfaces.ICallbackFunc;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.rjtech.base.exception.ParameterException;
import cn.rjtech.cache.UserCache;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.AuditTypeEnum;
import cn.rjtech.enums.AuditWayEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.omg.PortableInterceptor.SUCCESSFUL;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 表单审批流 Service
 *
 * @ClassName: FormApprovalService
 * @author: RJ
 * @date: 2023-04-18 17:26
 */
public class FormApprovalApiService extends JBoltApiBaseService {

    @Inject
    private FormApprovalService service;

    /**
     * 判断走审核还是审批
     * 审批的话 保存审批流的基础信息、审批节点信息、节点的详细信息
     * 保存 审批流的审批流程的人员信息
     */
    public JBoltApiRet submit(String formSn, Long formAutoId, String primaryKeyName, String className) {
        Ret ret = service.submit(formSn, formAutoId, primaryKeyName, className);
        return ret.isOk()?JBoltApiRet.API_SUCCESS:JBoltApiRet.API_FAIL(ret.getStr("msg"));
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
    public JBoltApiRet approve(Long formAutoId, String formSn, int status, String primaryKeyName, String className,
                       boolean isWithinBatch,String remark) {
        Ret ret = service.approve(formAutoId, formSn, status, primaryKeyName, className, isWithinBatch, remark);
        return ret.isOk()?JBoltApiRet.API_SUCCESS:JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }

    /**
     * 审批不通过
     */
    public JBoltApiRet reject(Long formAutoId, String formSn, int status, String primaryKeyName, String className,
                      String remark, boolean isWithinBatch) {

        Ret ret = service.reject(formAutoId, formSn, status, primaryKeyName, className, remark, isWithinBatch);

        return ret.isOk()?JBoltApiRet.API_SUCCESS:JBoltApiRet.API_FAIL(ret.getStr("msg"));
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

        return ret.isOk()?JBoltApiRet.API_SUCCESS:JBoltApiRet.API_FAIL(ret.getStr("msg"));
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

        return ret.isOk()?JBoltApiRet.API_SUCCESS:JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }

    /**
     * 批量审批
     */
    public JBoltApiRet batchApprove(String ids, String formSn, String primaryKeyName, String className) {

        Ret ret = service.batchApprove(ids, formSn, primaryKeyName, className);

        return ret.isOk()?JBoltApiRet.API_SUCCESS:JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }

    /**
     * 批量审核不通过
     */
    public JBoltApiRet batchReject(String ids, String formSn, String primaryKeyName, String className) {

        Ret ret = service.batchReject(ids, formSn, primaryKeyName, className);

        return ret.isOk()?JBoltApiRet.API_SUCCESS:JBoltApiRet.API_FAIL(ret.getStr("msg"));
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

        return ret.isOk()?JBoltApiRet.API_SUCCESS:JBoltApiRet.API_FAIL(ret.getStr("msg"));
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
    public JBoltApiRet approveByStatus(String formSn, long formAutoId, String primaryKeyName, String className,
                             boolean isWithinBatch) {

        Ret ret = service.approveByStatus(formSn, formAutoId, primaryKeyName, className, isWithinBatch);

        return ret.isOk()?JBoltApiRet.API_SUCCESS:JBoltApiRet.API_FAIL(ret.getStr("msg"));
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
    public JBoltApiRet rejectByStatus(String formSn, Long formAutoId, String primaryKeyName, String className,
                            boolean isWithinBatch) {

        Ret ret = service.rejectByStatus(formSn, formAutoId, primaryKeyName, className, isWithinBatch);

        return ret.isOk()?JBoltApiRet.API_SUCCESS:JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }

    /**
     * 反审核，公用实现
     *
     * @param formSn         表单编码
     * @param formAutoId     单据ID
     * @param primaryKeyName 主键名
     * @param className      类名
     */
    public JBoltApiRet reverseApproveByStatus(String formSn, Long formAutoId, String primaryKeyName, String className) {

        Ret ret = service.reverseApproveByStatus(formSn, formAutoId, primaryKeyName, className);

        return ret.isOk()?JBoltApiRet.API_SUCCESS:JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }

    /**
     * 批量审核通过，公用实现
     *
     * @param ids            单据IDs
     * @param formSn         表单编码
     * @param primaryKeyName 主键名
     * @param className      实现审批业务的类名
     */
    public JBoltApiRet batchApproveByStatus(String ids, String formSn, String primaryKeyName, String className) {

        Ret ret = service.batchApproveByStatus(ids, formSn, primaryKeyName, className);

        return ret.isOk()?JBoltApiRet.API_SUCCESS:JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }

    /**
     * 批量审核不通过，公用实现
     *
     * @param ids            单据IDs
     * @param formSn         表单编码
     * @param primaryKeyName 主键名
     * @param className      实现审批业务的类名
     */
    public JBoltApiRet batchRejectByStatus(String ids, String formSn, String primaryKeyName, String className) {

        Ret ret = service.batchRejectByStatus(ids, formSn, primaryKeyName, className);

        return ret.isOk()?JBoltApiRet.API_SUCCESS:JBoltApiRet.API_FAIL(ret.getStr("msg"));
    }

}
