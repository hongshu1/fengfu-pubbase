package cn.jbolt.starter;

import com.jfinal.server.undertow.UndertowConfig;

import cn.jbolt.core.server.JBoltServer;
/**
 * 项目服务器自定义
 * @ClassName:  JBoltServer   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2021年8月7日   
 *    
 */
public class ProjectServer extends JBoltServer {
	protected ProjectServer(UndertowConfig undertowConfig) {
		super(undertowConfig);
	}
	
	 

}
