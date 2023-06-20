package cn.rjtech.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.rjtech.admin.emailreceiver.EmailReceiverService;
import cn.rjtech.constants.Constants;
import cn.rjtech.enums.EmailReceiverTypeEnum;
import com.jfinal.aop.Aop;
import com.jfinal.log.Log;
import com.jfplugin.mail.MailKit;

import java.util.List;

/**
 * @Description TODO
 * @Author Create by Alvin
 * @Date 2020-10-27 9:43
 */
public class EmailUtils {

    private static final Log LOG = Log.getLog(EmailUtils.class);

    private EmailUtils() {
        // ignored
    }

    /**
     * 发送异常通知
     */
    public static void sendExceptionMsg(String appName, boolean isProdEnv, String errMsg) {
        // 异常邮件接收人
        List<String> receivers = Aop.get(EmailReceiverService.class).getEmailList(EmailReceiverTypeEnum.EXCEPTION.getValue());
        if (CollUtil.isEmpty(receivers)) {
            LOG.warn("缺少异常通知接收人，请在系统管理 > 邮件接收人 中进行配置");
            return;
        }

        MailKit.send(receivers.get(0), receivers.size() > 1 ? ListUtil.sub(receivers, 1, receivers.size()) : null, String.format("【%s】应用[%s]异常通知", Constants.getProdEnv(isProdEnv), appName), errMsg);

        LOG.info("Exception Email has been sent...");
    }

    /**
     * 发送邮件
     *
     * @param emails  邮箱地址
     * @param subject 主题
     * @param text    邮件内容
     */
    public static void sendEmail(List<String> emails, String subject, String text) {
        // 第一个为收件人，其他定义为抄送
        String email = emails.remove(0);
        sendEmail(email, CollUtil.isNotEmpty(emails) ? emails : null, subject, text);
    }

    /**
     * 发送邮件，当发送失败的时候，发送异常通知
     *
     * @param email   邮件接收人
     * @param cc      抄送
     * @param subject 主题
     * @param text    邮件内容
     */
    private static void sendEmail(String email, List<String> cc, String subject, String text) {
        try {
            MailKit.send(email, cc, subject, text);
        } catch (Exception e) {
            LOG.error("邮件发送失败, email: {}, subject: {}, text: {}, 错误信息: {}", email, subject, text, e.getLocalizedMessage());
            e.printStackTrace();
            ExceptionEventUtil.postSendEmailFailMsg(email, subject, text, Util.getStackMsg(e));
        }
    }
}