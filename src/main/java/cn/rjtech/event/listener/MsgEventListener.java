package cn.rjtech.event.listener;

import cn.jbolt._admin.msgcenter.TodoService;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.event.ApprovalMsgEvent;
import cn.rjtech.util.ExceptionEventUtil;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Db;
import net.dreamlu.event.core.EventListener;

/**
 * 消息通知异步监听
 *
 * @author Kephon
 */
@EventListener
public class MsgEventListener {

    private final TodoService todoService = Aop.get(TodoService.class);

    /**
     * 审批消息通知监听
     */
    @EventListener(async = true)
    public void listenApprovalMsgEvent(ApprovalMsgEvent event) {
        try {
            // 保存消息处理
            Db.use(DataSourceConstants.MAIN).tx(() -> {

                return true;
            });
            
            // 发送邮件处理
            
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionEventUtil.postExceptionEvent(e);
        }
    }

}
