package cn.jbolt._admin.websocket;
/**
 * JBolt里 websocket通讯 command定义
 * @ClassName:  JBoltWebSocketCommand   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2021年10月2日   
 *    
 */
public interface JBoltWebSocketCommand {
	public static final String PING = "ping";
	public static final String PONG = "pong";
	public static final String SERVER_TIME = "server_time";
	public static final String MSGCENTER_CHECK_UNREAD = "msgcenter_check_unread";
	public static final String CHECK_LAST_PWD_UPDATE_TIME = "check_last_pwd_update_time";
}
