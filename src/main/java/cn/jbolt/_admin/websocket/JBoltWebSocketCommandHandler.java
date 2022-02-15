package cn.jbolt._admin.websocket;

import java.util.Date;

import com.jfinal.aop.Aop;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

import cn.jbolt._admin.msgcenter.SysNoticeService;
import cn.jbolt._admin.msgcenter.TodoService;
import cn.jbolt._admin.websocket.extend.JBoltWebSocketExtendCommandHandler;
/**
 * JBolt websocket 指令处理器
 * @ClassName:  JBoltWebSocketCommandHandler   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2021年10月1日   
 */
public class JBoltWebSocketCommandHandler {
	private static final Log LOG = Log.getLog("JBoltWebsocketLog");
	public static final JBoltWebSocketCommandHandler me = new JBoltWebSocketCommandHandler();
	/**
	 * 核心指令处理器逻辑
	 * @param inMsg
	 * @param session
	 * @return
	 */
	public JBoltWebSocketMsg process(JBoltWebSocketMsg inMsg, JBoltWebSocketSession session) {
		String command = inMsg.getCommand();
		if(StrKit.isBlank(command)) {
			LOG.error("无效的command,不予处理");
			return null;
		}
		JBoltWebSocketMsg outMsg = null;
		//拿到指令 switch判断并处理
		switch (command) {
			case JBoltWebSocketCommand.PING://心跳检测
//				LOG.debug("收到心跳检测ping");
				outMsg = inMsg.toCommandRetOutMsg(JBoltWebSocketCommand.PONG,null);
				break;
			case JBoltWebSocketCommand.SERVER_TIME://服务器时间
				outMsg = inMsg.toCommandRetOutMsg(new Date());
				break;
			case JBoltWebSocketCommand.MSGCENTER_CHECK_UNREAD://检测前端消息中心是否有未读
				outMsg = processMsgCenterCheckUnreadCommand(inMsg, session.getUserId());
				break;
			default://没有内置处理 就去扩展里找
				outMsg = JBoltWebSocketExtendCommandHandler.me.process(inMsg, session);
				break;
		}
		
		//有消息返回给客户端 就返回JBoltWebSocketMsg 没有就null
		return outMsg;
	}
	
	/**
	 * 处理检测消息中心是否有用户未读
	 * @param inMsg
	 * @param userId
	 * @return
	 */
	protected JBoltWebSocketMsg processMsgCenterCheckUnreadCommand(JBoltWebSocketMsg inMsg, Object userId) {
		SysNoticeService sysNoticeService = Aop.get(SysNoticeService.class);
		boolean needRedDot = sysNoticeService.existUnread(userId);
		if(!needRedDot) {
			TodoService todoService = Aop.get(TodoService.class);
			needRedDot = todoService.existUnread(userId);
			if(!needRedDot) {
				todoService.existNeedProcess(userId);
			}
//			if(!needRedDot) {
//				needRedDot = privateMessageService.existUnread(userId);
//			}
		}
		return inMsg.toCommandRetOutMsg(needRedDot);
	}
}
