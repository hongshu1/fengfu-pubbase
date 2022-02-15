package cn.jbolt._admin.websocket;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.Session;

import cn.jbolt.core.cache.JBoltOnlineUserCache;
/**
 * JBolt平台 websocket session
 * @ClassName:  JBoltWebSocketSession   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2021年10月2日   
 */
public class JBoltWebSocketSession {
	//会话
    private Session session;
    //登录用户授权的TOKEN
    private String token;
    //用户UserId
    private Object userId;
    public JBoltWebSocketSession(Session session,String token) {
    	this.session = session;
    	this.token   = token;
    	this.userId = JBoltOnlineUserCache.me.getUserIdBySessionId(token);
    }
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Object getUserId() {
		return userId;
	}
	public void setUserId(Object userId) {
		this.userId = userId;
	}
	/**
	 * 返回指定类型userId 
	 * @param <T>
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public <T> T getUserIdAs() {
	    return (T) userId;
    }
    /**
     * 关闭session
     */
    public void close() {
    	if(session!=null) {
    		try {
    			session.close();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    }
   /**
    * 关闭session
    * @param closeReason
    */
    public void close(CloseReason closeReason) {
    	if(session!=null) {
    		try {
				session.close(closeReason);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    
    public boolean isOpen() {
		return session==null?false:session.isOpen();
	}
}
