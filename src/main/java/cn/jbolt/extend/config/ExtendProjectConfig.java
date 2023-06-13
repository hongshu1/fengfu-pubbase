package cn.jbolt.extend.config;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.Setting;
import cn.jbolt.core.cache.JBoltDeptCache;
import cn.jbolt.core.cache.OrgCache;
import cn.jbolt.core.handler.base.JBoltBaseHandler;
import cn.jbolt.core.kit.*;
import cn.rjtech.cache.AuditFormConfigCache;
import cn.rjtech.config.MomConfigKey;
import cn.rjtech.config.RedisConfig;
import cn.rjtech.enjoy.directive.BracketDirective;
import cn.rjtech.kit.ReflectionTemplateFn;
import cn.rjtech.routes.ApiRoutes;
import cn.rjtech.routes.ExtendAdminRoutes;
import cn.rjtech.serializer.StringRedisSerializer;
import cn.rjtech.util.Util;
import com.jfinal.config.*;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.template.Engine;
import com.jfplugin.mail.MailPlugin;

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
		//调用使用可视化生成器生成的路由配置信息
		ProjectCodeGenRoutesConfig.config(me);

        // 后台二开路由配置
        me.add(new ExtendAdminRoutes());

        me.add(new Routes() {
            @Override
            public void config() {
                this.scan("cn.rjtech.common.controller")
                        .scan("cn.rjtech.base.controller");
            }
        });

        me.add(new ApiRoutes());
	}
	/**
	 * 插件配置
	 * @param me
	 * @param cron4jPlugin
	 */
	public static void configPlugin(Plugins me, Cron4jPlugin cron4jPlugin) {
		LOG.debug("调用二开扩展配置:configPlugin");
		//事务配置
		configTx();
		//如果需要二开 增加自己的自动调度任务
		configCron4jPlugin(me,cron4jPlugin);
        // 邮件发送插件
        configMailPlugin(me);
        // 配置redis
        configRedisPlugin(me);
	}

	/**
	 * 事务相关自定义配置txFun等
	 */
	private static void configTx() {
		LOG.debug("调用二开扩展配置:configTx");
	}

	/**
	 * 配置自动调度插件
	 * @param me
	 * @param cron4jPlugin
	 */
	private static void configCron4jPlugin(Plugins me,Cron4jPlugin cron4jPlugin) {
		LOG.debug("调用二开扩展配置:configCron4jPlugin");
		//可以直接在 cron4jPlugin 中addTask
//		cron4jPlugin.addTask("0-59/1 * * * *", new WechatMediaDownloaTask());
		// 定时备份任务
		// cron4jPlugin.addTask("0-59/1 * * * *", new BackupConfigTask());
		//也可以自己添加新的cron4jPlugin
//	    Cron4jPlugin cron4jPlugin2 = new Cron4jPlugin();
//	    cron4jPlugin2.addTask("0-59/1 * * * *", new WechatMediaDownloaTask());
//	    me.add(cron4jPlugin2);
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
		//baseHandler.unlimited("/assets/plugins/","/neditor/")

        // 配置baseHandler 处理页面basePath pmkey 静态资源html直接访问拦截等
        baseHandler.unlimited("/assets/plugins/", "/admin/druid/monitor/", "/neditor/", "/assets/apidocs/");
	}

	/**
	 * 模板引擎配置
	 * @param me
	 */
	public static void configEngine(Engine me) {
		LOG.debug("调用二开扩展配置:configEngine");

        me.addDirective("bracket", BracketDirective.class);

        // 添加GlobalConfig的访问
        me.addSharedObject("MomConfigKey", new MomConfigKey());
        // 添加OrgAccessKit的访问
        me.addSharedObject("OrgAccessKit", new OrgAccessKit());
        // 添加组织缓存
        me.addSharedObject("OrgCache", OrgCache.ME);
        // 添加部门缓存
        me.addSharedObject("DeptCache", JBoltDeptCache.me);
        me.addSharedObject("ObjUtil", new ObjUtil());
        me.addSharedObject("StrUtil", new StrUtil());
        me.addSharedObject("AuditFormConfigCache", AuditFormConfigCache.ME);
		
		me.addSharedStaticMethod(Util.class);
        me.addSharedMethod(new ReflectionTemplateFn());
	}

	/**
	 * 启动后处理
	 */
	public static void onStart() {
		LOG.debug("调用二开扩展配置:onStart");

        // 初始化U8数据源
        U8DataSourceKit.ME.init();
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
        sqlEngine.addSharedStaticMethod(ConfigKit.class);
		arp.addSqlTemplate("/cn/rjtech/admin/_all_sql.sql");
	}

	/**
	 * 扩展数据源插件配置 arp和sql模板
	 * @param dbPlugin
	 * @param arp
	 * @param sqlEngine
	 * @param configName
	 * @param dbSetting
	 */
	public static void configExtendDbPlugins(DruidPlugin dbPlugin, ActiveRecordPlugin arp, Engine sqlEngine, String configName, Setting dbSetting){
		LOG.debug("调用二开扩展配置:configExtendDbPlugins");
        sqlEngine.addSharedStaticMethod(ConfigKit.class);
        sqlEngine.addSharedStaticMethod(DataPermissionKit.class);
		arp.addSqlTemplate("/cn/rjtech/admin/_all_sql.sql");
	}

	/**
	 * 手动管理添加 新开项目的需要给租户分表的Model
	 */
	public static void configProjectSaasSeparateTableModels() {
		LOG.debug("调用二开扩展配置:configProjectSaasSeparateTableModels");
	}

	/**
	 * 配置租户获得当前是否可访问的处理器
	 * @return
	 */
	public static JBoltSaasTenantAccessibleProcessor configSaasTenantAccessibleProcessor() {
		return null;
	}

	/**
	 * 配置租户列表获取的处理器
	 * @return
	 */
	public static JBoltSaasTenantGetAllProcessor configSaasTenantAllDatasProcessor() {
		return null;
	}

	/**
	 * 配置租户ID转sn的转换器
	 * @return
	 */
	public static JBoltSaasTenantIdToSnProcessor configSaasTenantIdToSnProcessor() {
		return null;
	}
	/**
	 * 配置租户ID转Name的转换器
	 * @return
	 */
	public static JBoltSaasTenantIdToNameProcessor configSaasTenantIdToNameProcessor() {
		return null;
	}

	/**
	 * 配置租户sn转name的转换器
	 * @return
	 */
	public static JBoltSaasTenantSnToNameProcessor configSaasTenantSnToNameProcessor() {
		return null;
	}

	/**
	 * 配置租户SN转ID的转换器
	 * @return
	 */
	public static JBoltSaasTenantSnToIdProcessor configSaasTenantSnToIdProcessor() {
		return null;
	}

    private static void configMailPlugin(Plugins me) {
        // 邮件发送插件
        me.add(new MailPlugin(PropKit.use("mail.properties").getProperties()));
    }

    private static void configRedisPlugin(Plugins me) {
        // Redis插件,用于存储jfinal-weixin产生的Access_Token,以便于能将应用部署在集群环境中
        // 因为jfinal-weixin的Access_Token是缓存到内存中，不采用中间件缓存的话，在集群环境中，会出现Access_Token蹿的情况;
        if (StrUtil.isBlank(RedisConfig.getRedisPassword())) {
            RedisPlugin redisPlugin = new RedisPlugin(RedisConfig.getRedisCacheName1(), RedisConfig.getRedisHost());
            me.add(redisPlugin);

            RedisPlugin stringRedisPlugin = new RedisPlugin(RedisConfig.getRedisCacheName2(), RedisConfig.getRedisHost());
            stringRedisPlugin.setSerializer(new StringRedisSerializer());
            me.add(stringRedisPlugin);
        } else {
            RedisPlugin redisPlugin = new RedisPlugin(RedisConfig.getRedisCacheName1(), RedisConfig.getRedisHost(), RedisConfig.getRedisPassword());
            me.add(redisPlugin);

            RedisPlugin stringRedisPlugin = new RedisPlugin(RedisConfig.getRedisCacheName2(), RedisConfig.getRedisHost(), RedisConfig.getRedisPassword());
            stringRedisPlugin.setSerializer(new StringRedisSerializer());
            me.add(stringRedisPlugin);
        }
    }

}
