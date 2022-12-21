package cn.jbolt._admin.onlineuser;

import com.jfinal.aop.Aop;
import com.jfinal.log.Log;
import com.jfinal.plugin.cron4j.ITask;

import cn.jbolt.core.base.config.JBoltConfig;
/**
 * 每隔30秒执行一次删除任务 和 转离线任务
 * @ClassName:  JBoltOnlineUserClearTask   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2021年3月23日   
 *    
 */
public class JBoltOnlineUserClearTask implements ITask {
	private static final Log LOG = Log.getLog("JBoltCron4jLog");

	@Override
	public void run() {
		OnlineUserService service=Aop.get(OnlineUserService.class);
		if(!JBoltConfig.DEV_MODE) {
			LOG.debug("定时任务:每隔一分钟清理一次离线和过期用户 - 开始执行");
		}
		service.deleteOfflineAndExpirationUser();
		if(!JBoltConfig.DEV_MODE) {
			LOG.debug("定时任务:每隔一分钟清理一次离线和过期用户 - 执行完毕");
		}
	}

	@Override
	public void stop() {
		if(!JBoltConfig.DEV_MODE) {
			LOG.debug("stop 完成一次定时清理任务：每隔一分钟清理一次离线和过期用户信息");
		}
	}

}
