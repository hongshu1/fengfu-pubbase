package cn.rjtech.admin.proposalattachment;

import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.ProposalAttachment;
import com.jfinal.kit.Kv;

import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 禀议书附件 Service
 *
 * @ClassName: ProposalAttachmentService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-17 17:14
 */
public class ProposalAttachmentService extends BaseService<ProposalAttachment> {

    private final ProposalAttachment dao = new ProposalAttachment().dao();

    @Override
    protected ProposalAttachment dao() {
        return dao;
    }

    /**
     * 删除数据后执行的回调
     *
     * @param proposalAttachment 要删除的model
     * @param kv                 携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(ProposalAttachment proposalAttachment, Kv kv) {
        //addDeleteSystemLog(proposalAttachment.getIautoid(), JBoltUserKit.getUserId(),proposalAttachment.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param proposalAttachment 要删除的model
     * @param kv                 携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(ProposalAttachment proposalAttachment, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(proposalAttachment, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    public void deleteByIproposalmid(long iproposalmid) {
        delete("DELETE FROM PL_Proposal_Attachment WHERE iproposalmid = ? ", iproposalmid);
    }

    public List<ProposalAttachment> getList(long iproposalmid) {
        return find("SELECT * FROM PL_Proposal_Attachment WHERE iproposalmid = ? ", iproposalmid);
    }

    public void deleteByMultiIds(Object[] delete) {
        delete("DELETE FROM PL_Proposal_Attachment WHERE iautoid IN (" + ArrayUtil.join(delete, COMMA) + ")");
    }

    public List<ProposalAttachment> findByIproposalmid(long iproposalmid) {
        return dao.find("SELECT * FROM PL_Proposal_Attachment WHERE iproposalmid = ? ", iproposalmid);
    }

}