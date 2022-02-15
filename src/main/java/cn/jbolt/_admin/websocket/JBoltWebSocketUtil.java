package cn.jbolt._admin.websocket;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jfinal.aop.Aop;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

import cn.jbolt._admin.onlineuser.OnlineUserService;
/**
 * JBoltWebsocket工具类
 * @ClassName:  JBoltWebSocketUtil   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2021年10月26日   
 */
public class JBoltWebSocketUtil {
	private static final Log LOG = Log.getLog("JBoltWebsocketLog");
	//客户端map
    private static final Map<String, JBoltWebSocketServerEndpoint> CLIENTS = new ConcurrentHashMap<String, JBoltWebSocketServerEndpoint>();
	
    /**
     * 添加新客户端
     * @param token
     * @param client
     */
    public static void addClient(String token, JBoltWebSocketServerEndpoint client) {
    	CLIENTS.put(token, client);
    }
    /**
     * 移除客户端
     * @param token
     */
    public static void removeClient(String token) {
    	CLIENTS.remove(token);
    }
    /**
     * 获取指定客户端
     * @param token
     * @return
     */
	public static JBoltWebSocketServerEndpoint getClient(String token) {
		return CLIENTS.get(token);
	}
	/**
	 * 获取客户端数量
	 * @return
	 */
	public static int getClientSize() {
		return CLIENTS.size();
	}
	

	/**
	 * 获取所有客户端
	 * @return
	 */
	public static Map<String, JBoltWebSocketServerEndpoint> getClients() {
		return CLIENTS;
	}
	
	/**
	 * 发送全部
	 * @param outMsg
	 */
	public static void sendAllMessage(JBoltWebSocketMsg outMsg) {
		CLIENTS.forEach((token,client)->{
			client.sendMessage(token, outMsg);
		});
	}
	
	/**
	 * 发送全部
	 * @param outMsg
	 */
	public static void sendMessage(JBoltWebSocketMsg outMsg) {
		Object from = outMsg.getFrom();
		Object to   = outMsg.getTo();
		if((from == null && to == null) || to.equals(JBoltWebSocketMsg.TO_SYSTEM)) {
			throw new RuntimeException("请指定from或者to");
		}
		if(to.toString().length()>19) {
			sendMessage(to.toString(), outMsg);
			return;
		}
		sendMessageToUser(to, outMsg);
	}
	
	/**
	 * 发送消息
	 * @param toToken
	 * @param outMsg
	 */
	public static void sendMessage(String toToken,JBoltWebSocketMsg outMsg) {
		JBoltWebSocketServerEndpoint client = getClient(toToken);
		if(client != null) {
			if(outMsg.getFrom() == null) {
				outMsg.setFrom(JBoltWebSocketMsg.FROM_SYSTEM);
			}
			client.sendMessage(outMsg.toJson());
		}
	}
	
	
	 /**
     * 发送给指定用户
     * @param userId
     * @param outMsg
     */
    public static void sendMessageToUser(Object userId, JBoltWebSocketMsg outMsg) {
		OnlineUserService onlineUserService = Aop.get(OnlineUserService.class);
		List<String> tokens = onlineUserService.getSessionListByUserId((Long) userId);
		if(tokens!=null && tokens.size()>0) {
			tokens.forEach((token)->{
				if(StrKit.notBlank(token)) {
					sendMessage(token, outMsg);
				}
			});
		}
	}
    
    /**
     * 批量发送给多人
     * @param userIds
     * @param outMsg
     */
	public static void sendMessageToUsers(Object[] userIds, JBoltWebSocketMsg outMsg) {
		if(userIds == null || userIds.length == 0) {
			return;
		}
		for(Object userId:userIds) {
			sendMessageToUser(userId, outMsg);
		}
	}
	
	/**
	 * 指定多部门用户发送
	 * @param postIds
	 * @param outMsg
	 */
	public static void sendMessageToUserByPosts(Object[] postIds, JBoltWebSocketMsg outMsg) {
		if(postIds == null || postIds.length == 0) {
			return;
		}
		for(Object postId:postIds) {
			sendMessageToUserByPost(postId, outMsg);
		}
	}
	
	/**
	 * 指定部门用户发送
	 * @param postId
	 * @param outMsg
	 */
	public static void sendMessageToUserByPost(Object postId, JBoltWebSocketMsg outMsg) {
		OnlineUserService onlineUserService = Aop.get(OnlineUserService.class);
		List<String> tokens = onlineUserService.getSessionListByPostId(postId);
		if(tokens!=null && tokens.size()>0) {
			tokens.forEach((token)->{
				if(StrKit.notBlank(token)) {
					sendMessage(token, outMsg);
				}
			});
		}
	}
	
	/**
	 * 发送消息给指定多个部门下用户
	 * @param deptIds
	 * @param outMsg
	 */
	public static void sendMessageToUserByDepts(Object[] deptIds, JBoltWebSocketMsg outMsg) {
		if(deptIds == null || deptIds.length == 0) {
			return;
		}
		for(Object deptId:deptIds) {
			sendMessageToUserByDept(deptId, outMsg);
		}
	}
	
	
	
	/**
	 * 发送消息给指定部门下用户
	 * @param deptId
	 * @param outMsg
	 */
	public static void sendMessageToUserByDept(Object deptId, JBoltWebSocketMsg outMsg) {
		OnlineUserService onlineUserService = Aop.get(OnlineUserService.class);
		List<String> tokens = onlineUserService.getSessionListByDeptId(deptId);
		if(tokens!=null && tokens.size()>0) {
			tokens.forEach((token)->{
				if(StrKit.notBlank(token)) {
					sendMessage(token, outMsg);
				}
			});
		}
	}
	/**
	 * 发送消息给指定多个角色下用户
	 * @param roleIds
	 * @param outMsg
	 */
	public static void sendMessageToUserByRoles(Object[] roleIds, JBoltWebSocketMsg outMsg) {
		if(roleIds == null || roleIds.length == 0) {
			return;
		}
		for(Object roleId:roleIds) {
			sendMessageToUserByRole(roleId, outMsg);
		}
	}
	
	

	/**
	 * 发送消息给指定角色下用户
	 * @param roleId
	 * @param outMsg
	 */
	public static void sendMessageToUserByRole(Object roleId, JBoltWebSocketMsg outMsg) {
		OnlineUserService onlineUserService = Aop.get(OnlineUserService.class);
		List<String> tokens = onlineUserService.getSessionListByRoleId(roleId);
		if(tokens!=null && tokens.size()>0) {
			tokens.forEach((token)->{
				if(StrKit.notBlank(token)) {
					sendMessage(token, outMsg);
				}
			});
		}
	}

    
    
}
