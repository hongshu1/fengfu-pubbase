package cn.rjtech.event;

import cn.jbolt._admin.globalconfig.GlobalConfigService;
import cn.jbolt.core.base.JBoltGlobalConfigKey;
import cn.jbolt.core.base.config.JBoltConfig;
import cn.rjtech.base.event.ExceptionEvent;
import cn.rjtech.util.EmailUtils;
import com.jfinal.aop.Aop;
import net.dreamlu.event.core.EventListener;

/**
 * 异常事件监听
 *
 * @author Kephon
 */
public class ExceptionEventListener {

    private final GlobalConfigService globalConfigService = Aop.get(GlobalConfigService.class);

    /**
     * 监听异常事件
     * 普通邮件：MailKit.send(“收件人”,Arrays.asList(“抄送1″,”抄送2”), “邮件标题”, “邮件内容”);
     * 附件邮件：MailKit.send(“收件人”,Arrays.asList(“抄送1″,”抄送2”), “邮件标题”, “邮件内容”,Arrays.asList(new File(“附件1”),new File(“附件2”)));
     *
     * @param event 异常事件
     */
    @EventListener(async = true)
    public void listenExceptionEvent(ExceptionEvent event) {
        try {
            EmailUtils.sendExceptionMsg(globalConfigService.getConfigValue(JBoltGlobalConfigKey.SYSTEM_NAME), JBoltConfig.pdevIsPro(), event.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
