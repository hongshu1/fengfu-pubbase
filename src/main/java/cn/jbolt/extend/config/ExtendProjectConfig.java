package cn.jbolt.extend.config;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

import cn.jbolt.core.base.config.JBoltExtendDatabaseConfig;
import cn.jbolt.core.handler.base.JBoltBaseHandler;
/**
 * 二开专用项目工程配置类
 * @ClassName:  MineProjectConfig   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2020年3月17日   
 */
public class ExtendProjectConfig {
	private static final Log LOG=Log.getLog(ExtendProjectConfig.class);
	/**
	 * 常量配置
	 * @param me
	 */
	public static void configConstant(Constants me) {
		LOG.debug("调用二开扩展配置:configConstant");
	}
	/**
	 * 路由配置
	 * @param me
	 */
	public static void configRoute(Routes me) {
		LOG.debug("调用二开扩展配置:configRoute");
	}
	/**
	 * 插件配置
	 * @param me
	 */
	public static void configPlugin(Plugins me) {
		LOG.debug("调用二开扩展配置:configPlugin");
		//配置其它数据库 如果一个项目 使用JBolt默认数据库作为主源 还有额外其他的数据库的话需要调用下面的配置
		JBoltExtendDatabaseConfig.me().config(me);
		//如果需要二开 增加自己的自动调度任务
		configCron4jPlugin(me);
	}
	
	/**
	 * 配置自动调度插件
	 * @param me
	 */
	private static void configCron4jPlugin(Plugins me) {
		LOG.debug("调用二开扩展配置:configCron4jPlugin");
//	    Cron4jPlugin cron4jPlugin = new Cron4jPlugin();
//	    cron4jPlugin.addTask("0-59/1 * * * *", new WechatMediaDownloaTask());
//	    me.add(cron4jPlugin);
	}
	
	/**
	 * 全局拦截器配置
	 * @param me
	 */
	public static void configInterceptor(Interceptors me) {
		LOG.debug("调用二开扩展配置:configInterceptor");
	}
	/**
	 * 全局handler处理器配置
	 * @param me
	 * @param baseHandler 
	 */
	public static void configHandler(Handlers me, JBoltBaseHandler baseHandler) {
		LOG.debug("调用二开扩展配置:configHandler");
		//配置baseHandler 处理页面basePath pmkey 静态资源html直接访问拦截等
		//baseHandler.unlimited("/assets/plugins/","/neditor/");
	}
	
	/**
	 * 模板引擎配置
	 * @param me
	 */
	public static void configEngine(Engine me) {
		LOG.debug("调用二开扩展配置:configEngine");
	}
	
	/**
	 * 启动后处理
	 */
	public static void onStart() {
		LOG.debug("调用二开扩展配置:onStart");
	}
	
	/**
	 * 结束前处理
	 */
	public static void onStop() {
		LOG.debug("调用二开扩展配置:onStop");
	}
	
	/**
	 * 扩展主库的插件配置 arp和sql模板
	 * @param druidPlugin
	 * @param arp
	 * @param sqlEngine
	 */
	public static void configMainDbPlugin(DruidPlugin druidPlugin, ActiveRecordPlugin arp, Engine sqlEngine) {
		LOG.debug("调用二开扩展配置:configMainDbPlugin");
	}
}
