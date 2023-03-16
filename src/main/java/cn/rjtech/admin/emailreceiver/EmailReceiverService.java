package cn.rjtech.admin.emailreceiver;

import cn.jbolt.common.model.EmailReceiver;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.constants.ErrorMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * 邮件接收人 Service
 *
 * @ClassName: EmailReceiverService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-03-03 09:23
 */
public class EmailReceiverService extends BaseService<EmailReceiver> {

    private final EmailReceiver dao = new EmailReceiver().dao();

    @Override
    protected EmailReceiver dao() {
        return dao;
    }

    /**
     * 后台管理分页查询true
     */
    public Page<EmailReceiver> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("id", "desc", pageNumber, pageSize, keywords, "email");
    }

    /**
     * 保存
     */
    public Ret save(EmailReceiver emailReceiver) {
        ValidationUtils.assertNull(emailReceiver.getId(), JBoltMsg.PARAM_ERROR);

        tx(() -> {
            // ValidationUtils.isTrue(notExists(columnName, value), Msg.DATA_SAME_NAME_EXIST);
            ValidationUtils.isTrue(emailReceiver.save(), ErrorMsg.SAVE_FAILED);

            // TODO 其他业务代码实现

            return true;
        });

        // 添加日志
        // addSystemLog(emailReceiver.getId(), JBoltUserKit.getUserId(), SystemLog.TYPE_SAVE, SystemLog.TARGETTYPE_xxx, emailReceiver.getName())
        return SUCCESS;
    }

    /**
     * 更新
     */
    public Ret update(EmailReceiver emailReceiver) {
        ValidationUtils.isTrue(isOk(emailReceiver.getId()), JBoltMsg.PARAM_ERROR);

        tx(() -> {
            // 更新时需要判断数据存在
            EmailReceiver dbEmailReceiver = findById(emailReceiver.getId());
            ValidationUtils.notNull(dbEmailReceiver, JBoltMsg.DATA_NOT_EXIST);

            // TODO 其他业务代码实现
            // ValidationUtils.isTrue(notExists(columnName, value), Msg.DATA_SAME_NAME_EXIST);

            ValidationUtils.isTrue(emailReceiver.update(), ErrorMsg.UPDATE_FAILED);

            return true;
        });

        // 添加日志
        // addSystemLog(emailReceiver.getId(), JBoltUserKit.getUserId(), SystemLog.TYPE_UPDATE, SystemLog.TARGETTYPE_xxx, emailReceiver.getName())
        return SUCCESS;
    }

    /**
     * 切换禁用启用状态
     */
    public Ret toggleEnable(Long id) {
        // 说明:如果需要日志处理 就解开下面部分内容 如果不需要直接删掉即可
        Ret ret = toggleBoolean(id, "enable");
        if (ret.isOk()) {
            // 添加日志
            // EmailReceiver emailReceiver = ret.getAs("data");
            // addUpdateSystemLog(id, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_EMAILRECEIVER, emailReceiver.getName(),"的启用状态:"+emailReceiver.getEnable());
        }
        return ret;
    }

    public List<String> getEmailList(short receiverType) {
        return dbTemplate("emailreceiver.getEmailList", Okv.by("receiver_type", receiverType)).query();
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.EMAIL_RECEIVER.getValue();
    }

}