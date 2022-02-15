package cn.jbolt._admin.websocket;

import java.nio.ByteBuffer;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Aop;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

import cn.jbolt._admin.onlineuser.OnlineUserService;

/**
 * JBolt内置websocket服务端处理
 * @ClassName:  JBoltWebSocketServerEndpoint   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2021年5月31日   
 */
@ServerEndpoint("/websocket.ws/{token}")
public class JBoltWebSocketServerEndpoint {
	private static final Log LOG = Log.getLog("JBoltWebsocketLog");
    //会话
    private JBoltWebSocketSession jbsession;
    //command 指令处理器
    private JBoltWebSocketCommandHandler commandHandler;
    //text 文本消息处理器
    private JBoltWebSocketTextMessageHandler textMessageHandler;
    //file 文件传输处理器
    private JBoltWebSocketFileHandler fileHandler;
    public JBoltWebSocketServerEndpoint() {
    	this.commandHandler      = new JBoltWebSocketCommandHandler();
    	this.textMessageHandler  = new JBoltWebSocketTextMessageHandler();
    	this.fileHandler         = new JBoltWebSocketFileHandler();
    }

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam("token")String token,Session session){
    	this.jbsession = new JBoltWebSocketSession(session, token);
        JBoltWebSocketUtil.addClient(token, this);
        LOG.debug("有新连接加入！当前在线人数为" + JBoltWebSocketUtil.getClientSize());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(CloseReason closeReason){
    	JBoltWebSocketUtil.removeClient(jbsession.getToken());
    	LOG.info("websocket closeReason:"+closeReason.getCloseCode().toString());
        LOG.debug("有一连接关闭！当前在线人数为" + JBoltWebSocketUtil.getClientSize());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
    	if(StrKit.isBlank(message)) {
    		LOG.debug("onMessage empty - userId=" + jbsession.getUserId());
    		return;
    	}
    	JBoltWebSocketMsg inMsg = JSON.parseObject(message,JBoltWebSocketMsg.class);
    	Integer type = inMsg.getType();
    	if(type == null) {
    		LOG.debug("onMessage JBoltMsg.type empty- userId=" + jbsession.getUserId());
    		return;
    	}
    	if(type != 1 && type != 2 && type != 3) {
    		LOG.debug("onMessage JBoltMsg.type is not 1/2/3- userId=" + jbsession.getUserId());
    		return;
    	}
    	inMsg.setFrom(jbsession.getToken());
        if(inMsg.getTo() == null) {
        	inMsg.setTo(JBoltWebSocketMsg.TO_SYSTEM);
        }
        JBoltWebSocketMsg outMsg = null;
        switch (type.intValue()) {
	        case JBoltWebSocketMsg.TYPE_COMMAND://执行command handler
	        	outMsg = commandHandler.process(inMsg, jbsession);
	        	break;
	        case JBoltWebSocketMsg.TYPE_TEXT://文本消息
	        	outMsg = textMessageHandler.process(inMsg, jbsession);
	        	break;
			case JBoltWebSocketMsg.TYPE_FILE://文件传输
				outMsg = fileHandler.process(inMsg, jbsession);
				break;
	
			default:
				break;
		}
        if(outMsg != null) {
        	if(outMsg._isToOther()) {
        		sendMessageToUser(outMsg.getTo(),outMsg);
        	}else {
        		//定义了返回消息 就返回给client
        		sendMessage(this.jbsession.getToken(), outMsg);
        	}
        }
      
    }
    
    /**
     * 发送给其他用户
     * @param userId
     * @param outMsg
     */
    private void sendMessageToUser(Object userId, JBoltWebSocketMsg outMsg) {
		OnlineUserService onlineUserService = Aop.get(OnlineUserService.class);
		List<String> tokens = onlineUserService.getSessionListByUserId((Long) userId);
		if(tokens!=null && tokens.size()>0) {
			tokens.forEach((token)->{
				if(StrKit.notBlank(token)) {
					outMsg.setFrom(this.jbsession.getUserId());
					sendMessage(token, outMsg);
				}
			});
		}
	}

	/**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        LOG.error("发生错误", error);
//        String errorMsg = error.getMessage();
//        if(StrKit.notBlank(errorMsg) && errorJBoltMsg.indexOf("Connection timed out")!=-1) {
//        	jbsession.close();
//        }
    }
    
    /**
     * 发送群消息
     */
    protected void sendAllMessage(String message){
    	sendAllMessage(message, true);
    }
    
    /**
     * 发送群消息
     * @param message 消息
     * @param isThis 是否给自己 也发
     */
    protected void sendAllMessage(String message, boolean isThis){
        JBoltWebSocketUtil.getClients().forEach((token,client)->{
        	//如果是自己但是设置了不给自己发的时候 就什么都不做
			if (!isThis && client.equals(this)) {
				return;
			}
			client.sendMessage(message);
        });
    }
    
    /**
     * 发送消息
     * @param toToken
     * @param outMsg
     */
    protected void sendMessage(String toToken, JBoltWebSocketMsg outMsg){
    	sendMessage(JBoltWebSocketUtil.getClient(toToken), outMsg);
    }
    /**
     * 发送消息返回客户端
     * @param client
     * @param outMsg
     */
    protected void sendMessage(JBoltWebSocketServerEndpoint client, JBoltWebSocketMsg outMsg){
    	if (client != null) {
    		if(outMsg.getFrom() == null) {
    			outMsg.setFrom(JBoltWebSocketMsg.FROM_SYSTEM);
			}
    		outMsg.setTo(client.jbsession.getUserId());
    		if(outMsg._isFile()) {
    			client.sendFile(outMsg);
    		}else {
    			client.sendMessage(outMsg.toJson());
    		}
    	}else{
    		this.sendMessage("【系统消息】 不在线");
    	}
    }
    /**
     * 发送消息
     * @param toToken
     * @param message
     */
    protected void sendMessage(String toToken, String message){
    	JBoltWebSocketServerEndpoint me = JBoltWebSocketUtil.getClient(toToken);
    	if (me != null) {
    		me.sendMessage(message);
		}else{
			this.sendMessage("【系统消息】 不在线");
		}
    }
    
    /**
     * 向客户端 发送消息
     */
    protected void sendMessage(String message){
    	if(jbsession.isOpen()) {
    		getSession().getAsyncRemote().sendText(message);
    	}else {
    		LOG.error("客户端异常 closed 无法发送");
    		throw new RuntimeException("客户端异常 closed 无法发送");
    	}
    }
    /**
     * 向客户端 发送文件
     */
    protected void sendFile(JBoltWebSocketMsg fileMsg){
    	if(jbsession.isOpen()) {
        	if(fileMsg.getFrom() == null) {
        		fileMsg.setFrom(JBoltWebSocketMsg.FROM_SYSTEM);
			}
        	getSession().getAsyncRemote().sendBinary(ByteBuffer.wrap((byte[])fileMsg.getData()));
		}else {
			LOG.error("客户端异常 closed 无法发送");
			throw new RuntimeException("客户端异常 closed 无法发送");
		}
    }

    public JBoltWebSocketSession getJbsession() {
    	return jbsession;
    }
    
    public Session getSession() {
    	return jbsession.getSession();
    }
    public Object getUserId() {
    	return jbsession.getUserId();
    }
	public <T> T getUserIdAs() {
		return jbsession.getUserIdAs();
	}
    
}
