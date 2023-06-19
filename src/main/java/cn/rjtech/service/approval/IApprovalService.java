package cn.rjtech.service.approval;

import java.util.List;

/**
 * @author Kephon
 */
public interface IApprovalService {

    /**
     * 处理审批通过的其他业务操作，如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @return 错误信息
     */
    String postApproveFunc(long formAutoId);

    /**
     * 处理审批不通过的其他业务操作，如有异常处理返回错误信息
     *
     * @param formAutoId 单据ID
     * @return 错误信息
     */
    String postRejectFunc(long formAutoId);

    /**
     * 实现反审之前的其他业务操作，如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @param isFirst    是否为审批的第一个节点
     * @param isLast     是否为审批的最后一个节点
     * @return 错误信息
     */
    String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast);

    /**
     * 实现反审之后的其他业务操作, 如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @param isFirst    是否为审批的第一个节点
     * @param isLast     是否为审批的最后一个节点
     * @return 错误信息
     */
    String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast);

    /**
     * 提审前业务，如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @return 错误信息
     */
    String preSubmitFunc(long formAutoId);

    /**
     * 提审后业务处理，如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @return 错误信息
     */
    String postSubmitFunc(long formAutoId);

    /**
     * 撤回审核业务处理，如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @return 错误信息
     */
    String postWithdrawFunc(long formAutoId);

    /**
     * 从审批中，撤回到已保存，业务实现，如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @return 错误信息
     */
    String withdrawFromAuditting(long formAutoId);

    /**
     * 从已审核，撤回到已保存，前置业务实现，如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @return 错误信息
     */
    String preWithdrawFromAuditted(long formAutoId);

    /**
     * 从已审核，撤回到已保存，业务实现，如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @return 错误信息
     */
    String postWithdrawFromAuditted(long formAutoId);

    /**
     * 批量审核（审批）通过，后置业务实现
     *
     * @param formAutoIds 单据IDs
     * @return 错误信息
     */
    String postBatchApprove(List<Long> formAutoIds);
    
    /**
     * 批量审批（审核）不通过，后置业务实现
     *
     * @param formAutoIds 单据IDs
     * @return 错误信息
     */
    String postBatchReject(List<Long> formAutoIds);
    
    /**
     * 批量撤销审批，后置业务实现
     *
     * @param formAutoIds 单据IDs
     * @return 错误信息
     */
    String postBatchBackout(List<Long> formAutoIds);
    

}
