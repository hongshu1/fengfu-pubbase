package cn.jbolt.common.config;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.IDruidStatViewAuth;
import com.jfinal.template.Engine;
import com.jfinal.upload.OreillyCos;
import com.oreilly.servlet.multipart.FileRenamePolicy;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.onlineuser.JBoltOnlineUserClearTask;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.admin.appdevcenter.AppDevCenterAdminRoutes;
import cn.jbolt.admin.wechat.mpinfo.WechatMpinfoType;
import cn.jbolt.apitest.ApiTestRoutes;
import cn.jbolt.base.JBoltDruidStatViewHandler;
import cn.jbolt.common.model.PrivateMessage;
import cn.jbolt.common.model.SysNotice;
import cn.jbolt.common.model.SysNoticeReader;
import cn.jbolt.common.model.Todo;
import cn.jbolt.common.ureport.IUreportViewAuth;
import cn.jbolt.common.ureport.JBoltUreportViewHandler;
import cn.jbolt.common.util.CACHE;
import cn.jbolt.core.Interceptor.JBoltOnlineUserGlobalInterceptor;
import cn.jbolt.core.base.config.JBoltConfig;
import cn.jbolt.core.base.config.JBoltProjectConfig;
import cn.jbolt.core.enumutil.JBoltEnum;
import cn.jbolt.core.handler.base.JBoltBaseHandler;
import cn.jbolt.core.kit.JBoltSaasTenantAccessibleProcessor;
import cn.jbolt.core.kit.JBoltSaasTenantIdToNameProcessor;
import cn.jbolt.core.kit.JBoltSaasTenantIdToSnProcessor;
import cn.jbolt.core.kit.JBoltSaasTenantSnToIdProcessor;
import cn.jbolt.core.kit.JBoltSaasTenantSnToNameProcessor;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.model.base.JBoltModelConfig;
import cn.jbolt.core.permission.JBoltUserAuthKit;
import cn.jbolt.core.plugin.JBoltActiveRecordPlugin;
import cn.jbolt.extend.cache.CacheExtend;
import cn.jbolt.extend.config.ExtendProjectConfig;
import cn.jbolt.index.AdminRoutes;
import cn.jbolt.index.WebRoutes;
import cn.jbolt.index.WechatAdminRoutes;
import cn.jbolt.index.WechatApiRoutes;
import cn.jbolt.index.WechatRoutes;
import cn.jbolt.index.WechatTestRoutes;
import net.dreamlu.event.EventPlugin;

/**
 * 此项目工程的配置类
 * 
 * @ClassName: ProjectConfig
 * @author: JFinal学院-小木 QQ：909854136
 * @date: 2022年2月13日
 */
public class ProjectConfig extends JBoltProjectConfig {
	/**
	 * 配置系统常量
	 */
	@Override
	public void configConstants(Constants me) {
		// 设置404渲染视图
		me.setError404View("/_view/_admin/common/msg/404.html");
		// 设置500渲染视图
		me.setError500View("/_view/_admin/common/msg/500.html");
		// 二开项目常量配置
		ExtendProjectConfig.configConstant(me);
	}

	/**
	 * 配置路由
	 */
	@Override
	public void configRoutes(Routes me) {
		// 后台管理 主模块路由配置
		me.add(new AdminRoutes());
		// 后台管理 微信模块路由配置
		me.add(new WechatAdminRoutes());
		// 微信服务器与本服务通讯使用的前端路由
		me.add(new WechatRoutes());
		// 网站前端访问 路由配置
		me.add(new WebRoutes());

		// 后台管理 系统Api应用开发中心模块路由配置
		me.add(new AppDevCenterAdminRoutes());

		// 后台管理 系统微信API路由配置
		me.add(new WechatApiRoutes());

		// test专用的路由 正式上线请删掉
		me.add(new ApiTestRoutes());
		// 微信公众号H5测试
		me.add(new WechatTestRoutes());
		// 二开专用扩展路由配置
		ExtendProjectConfig.configRoute(me);
		// 额外扫描公用controller
		me.add(new Routes() {
			@Override
			public void config() {
				this.addInterceptor(new JBoltAdminAuthInterceptor());
				this.scan("cn.jbolt.common");
			}
		});
		//添加自己的
//		me.add(new Routes() {
//			@Override
//			public void config() {
//				this.scan("cn.jbolt.xxx");
//			}
//		});
	}

	/**
	 * 配置模板引擎
	 */
	@Override
	public void configEngines(Engine me) {
		// 配置自定义指令
		// 配置共享对象
		// 添加CACHE访问
		me.addSharedObject("CACHE", CACHE.me);
		// 添加扩展CACHE访问
		me.addSharedObject("CacheExtend", CacheExtend.me);
		// 添加sessionKey的访问
		me.addSharedObject("SessionKey", new SessionKey());
		// 添加PermissionKey的访问
		me.addSharedObject("PermissionKey", new PermissionKey());

		// 将枚举添加到模板里
		JBoltEnum.addEnjoyEngineShareObject(me, WechatMpinfoType.class);

		// 配置layout共享
		// 后台主pjax加载结构layout
		me.addSharedFunction("/_view/_admin/common/__admin_layout.html");
		// 后台所有Dialog的页面 都是用这个layout
		me.addSharedFunction("/_view/_admin/common/__admin_dialog_layout.html");
		// 后台所有Iframe的页面 都是用这个layout
		me.addSharedFunction("/_view/_admin/common/__admin_iframe_layout.html");
		// 后台独立单页 都是用这个layout
		me.addSharedFunction("/_view/_admin/common/__admin_singlepage_layout.html");
		// 后台所有JBoltLayer组件加载的页面 都是用这个layout
		me.addSharedFunction("/_view/_admin/common/__admin_jboltlayer_layout.html");
		// 配置JBolt 自动识别Layout
		me.addSharedFunction("/_view/_admin/common/__jbolt_layout.html");
		// 常用前端组件
		me.addSharedFunction("/_view/_admin/common/__jboltassets.html");

		// 二开扩展配置 模板引擎
		ExtendProjectConfig.configEngine(me);
	}

	/**
	 * 配置系统插件
	 */
	@Override
	public void configPlugins(Plugins me) {
		// 配置调度
		configCron4jPlugin(me);
		// 配置JFinal Event插件
		configJFinalEvent(me);
		
		//二开配置扩展插件
		ExtendProjectConfig.configPlugin(me);
	}

	/**
	 * 配置JFinal event
	 * 
	 * @param me
	 */
	private void configJFinalEvent(Plugins me) {
		// 初始化插件
		EventPlugin eventPlugin = new EventPlugin();
		// 设置为异步 或者线程池 二选一 异步默认单线程
		// eventPlugin.async();

		// 使用eventPlugin.threadPool(ExecutorService executorService)`自定义线程池。
		ExecutorService fixedThreadPool = new ThreadPoolExecutor(16, 32, 0L, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(1024));
		eventPlugin.threadPool(fixedThreadPool);
		// 执行扫描 多个package 使用分号;隔开 例如 pa;pb
		eventPlugin.enableClassScan().scanJar().scanPackage("cn.jbolt._admin.event");
		me.add(eventPlugin);
	}

	/**
	 * 配置自动调度插件
	 * 
	 * @param me
	 */
	private void configCron4jPlugin(Plugins me) {
		Cron4jPlugin cron4jPlugin = new Cron4jPlugin();
		cron4jPlugin.addTask("0-59/1 * * * *", new JBoltOnlineUserClearTask());
		me.add(cron4jPlugin);
		
	}

	/**
	 * 配置拦截器
	 */
	@Override
	public void configInterceptors(Interceptors me) {
		me.addGlobalActionInterceptor(new SessionInViewInterceptor());
		me.addGlobalActionInterceptor(new JBoltOnlineUserGlobalInterceptor());
		//二开配置扩展全局拦截器
		ExtendProjectConfig.configInterceptor(me);
	}

	/**
	 * 配置Sentinel限流
	 */
	@Override
	protected void configSentinel() {

	}

	/**
	 * 配置处理器handler
	 */
	@Override
	protected void configHandlers(Handlers me, JBoltBaseHandler baseHandler) {
		if (JBoltConfig.JBOLT_UREPORT_ENABLE) {
			// 配置ureport权限控制
			configUreport(me);
		}
		
		//二开配置扩展全局处理器
		ExtendProjectConfig.configHandler(me,baseHandler);
	}

	/**
	 * 配置
	 */
	@Override
	protected void configWebsocketHandler(Handlers me) {
		me.add(new UrlSkipHandler("^/websocket.ws", false));
	}

	@Override
	protected void configDruidMonitor(Handlers me) {
		JBoltDruidStatViewHandler dvh = new JBoltDruidStatViewHandler("/admin/druid/monitor", new IDruidStatViewAuth() {
			public boolean isPermitted(HttpServletRequest request) {
				// 从当前线程里拿到数据
				User user = JBoltUserKit.getUser();
				if (user == null) {
					return false;
				}
				if (user.getEnable() == false) {
					return false;
				}
				// 锁屏了不让看
				if (JBoltUserKit.userScreenIsLocked()) {
					return false;
				}
				// 超级管理可以直接看
				if (user.getIsSystemAdmin()) {
					return true;
				}
				boolean has = JBoltUserAuthKit.hasPermission(user.getId(), true, PermissionKey.DRUID_MONITOR);
				return has;
			}
		});
		me.add(dvh);
	}

	@Override
	protected void configMainDbPlugins(DruidPlugin dbPlugin, JBoltActiveRecordPlugin arp, Engine sqlEngine) {
		//处理二开配置扩展
		ExtendProjectConfig.configMainDbPlugin(dbPlugin,arp,sqlEngine);
	}

	/**
	 * 配置ureport 报表设计器
	 * 
	 * @param me
	 */
	private void configUreport(Handlers me) {
		JBoltUreportViewHandler ureportViewHandler = new JBoltUreportViewHandler(new IUreportViewAuth() {
			@Override
			public boolean isPermitted(HttpServletRequest request, String target) {
				// 从当前线程里拿到数据
				User user = JBoltUserKit.getUser();
				if (user == null) {
					return false;
				}
				if (user.getEnable() == false) {
					return false;
				}
				// 锁屏了不让看
				if (JBoltUserKit.userScreenIsLocked()) {
					return false;
				}
				// 超级管理可以直接看
				if (user.getIsSystemAdmin()) {
					return true;
				}
				boolean has = false;
				if (target.indexOf("/ureport/designer") != -1 || target.indexOf("/ureport/datasource") != -1) {
					has = JBoltUserAuthKit.hasPermission(user.getId(), true, PermissionKey.UREPORT_DESIGNER);
				} else {
					has = JBoltUserAuthKit.hasPermission(user.getId(), false, PermissionKey.UREPORT_DESIGNER,
							PermissionKey.UREPORT_DETAIL);
				}
				return has;
			}
		});
		me.add(ureportViewHandler);
	}

	/**
	 * 配置cos组件
	 */
	@Override
	protected void configCos() {
		OreillyCos.setFileRenamePolicy(new FileRenamePolicy() {
			@Override
			public File rename(File file) {
				String path = file.getPath();
				String ext = FileUtil.getSuffix(path);
				String name = FileUtil.getName(path);
				if (StrKit.isBlank(ext) && name.equals("blob")) {
					ext = "jpg";
				}
				return new File(file.getParent(), IdUtil.fastSimpleUUID() + "." + ext);
			}
		});
	}

	@Override
	public void onStop() {
		//调用默认
		super.onStop();
		//二开扩展配置服务器关闭前处理
		ExtendProjectConfig.onStop();
	}
	
	@Override
	public void onStart() {
		//调用默认
		super.onStart();
		//0、自动初始化数据
		JBoltAutoInitData.me.exe();
		//1、自动执行一些需要升级的操作
		JBoltAutoUpgrade.me.exe();
		//2、配置action report输出位置
		JBoltConfig.configActionReportWriter();
		//3、配置JBoltAutoCacheLog
		JBoltConfig.configJBoltAutoCacheLog();
		//4、配置assets version
		JBoltConfig.configAssetsVersion();
		//4、配置微信公众平台
		JBoltConfig.configWechat();
		
		//二开扩展配置服务器启动后处理
		ExtendProjectConfig.onStart();
	}

	@Override
	protected void configSaas(Handlers me, JBoltBaseHandler baseHandler) {
		
	}
	/**
	 * 配置租户SN转ID的转换器
	 */
	@Override
	protected JBoltSaasTenantSnToIdProcessor getSaasTenantSnToIdProcessor() {
		return null;
	}
	/**
	 * 配置租户sn转name的转换器
	 */
	@Override
	protected JBoltSaasTenantSnToNameProcessor getSaasTenantSnToNameProcessor() {
		return null;
	}
	/**
	 * 配置租户ID转Name的转换器
	 */
	@Override
	protected JBoltSaasTenantIdToNameProcessor getSaasTenantIdToNameProcessor() {
		return null;
	}
	/**
	 * 配置租户ID转sn的转换器
	 */
	@Override
	protected JBoltSaasTenantIdToSnProcessor getSaasTenantIdToSnProcessor() {
		return null;
	}
	/**
	 * 配置租户获得当前是否可访问的处理器
	 */
	@Override
	protected JBoltSaasTenantAccessibleProcessor getSaasTenantAccessibleProcessor() {
		return null;
	}

	/**
	 * 手动管理添加 新开项目的需要给租户分表的Model
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void configProjectSaasSeparateTableModels() {
		//项目里不包含在jbolt_core中的需要分表的加入model
		JBoltModelConfig.me.addProjectSeparateModels(
				SysNotice.class,
				SysNoticeReader.class,
				Todo.class,
				PrivateMessage.class
				);
		//二开业务专用的配置调用
		ExtendProjectConfig.configProjectSaasSeparateTableModels();
	}
}
