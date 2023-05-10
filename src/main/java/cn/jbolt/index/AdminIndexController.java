package cn.jbolt.index;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.globalconfig.GlobalConfigService;
import cn.jbolt._admin.onlineuser.OnlineUserService;
import cn.jbolt._admin.topnav.TopnavService;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.api.HttpMethod;
import cn.jbolt.core.base.JBoltGlobalConfigKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.base.config.JBoltConfig;
import cn.jbolt.core.cache.*;
import cn.jbolt.core.common.enums.JBoltLoginState;
import cn.jbolt.core.common.enums.UserTypeEnum;
import cn.jbolt.core.consts.JBoltConst;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.handler.base.JBoltBaseHandler;
import cn.jbolt.core.kit.JBoltControllerKit;
import cn.jbolt.core.kit.JBoltSaasTenantKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.*;
import cn.jbolt.core.para.JBoltNoUrlPara;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.service.JBoltLoginLogUtil;
import cn.jbolt.core.service.JBoltOrgService;
import cn.rjtech.admin.userorg.UserOrgService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Kv;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Ret;

import javax.servlet.http.HttpSession;
import java.io.File;

/**
 * 系统后台主入口
 * @ClassName:  AdminIndexController
 * @author: JFinal学院-小木 QQ：909854136
 * @date:   2020年3月8日
 */
public class AdminIndexController extends JBoltBaseController {
	@Inject
	private UserService userService;
    @Inject
    private UserOrgService userOrgService;
	@Inject
	private GlobalConfigService globalConfigService;
	@Inject
	private TopnavService topnavService;
	@Inject
	private OnlineUserService onlineUserService;
    @Inject
    private JBoltOrgService orgService;
	@UnCheck
	@Before(JBoltNoUrlPara.class)
	public void index(){
		render("index.html");
	}

	@UnCheck
	public void menu(){
		set("leftMenus", JBoltPermissionCache.me.getRoleMenus(JBoltUserKit.getUserId(), JBoltGlobalConfigCache.me.getLongConfigValue(JBoltGlobalConfigKey.MOM_APP_ID), JBoltGlobalConfigCache.me.getLongConfigValue(JBoltGlobalConfigKey.MOM_APPLICATION_ID), JBoltUserKit.getUserRoleIds()));
		render("menu.html");
	}
	@UnCheck
	public void lockSystem(){
		renderJson(onlineUserService.lockCurrentUserScreen());
	}
	@UnCheck
	public void unLockSystem(){
		String password=get("password");
		if(notOk(password)) {
			renderJsonFail("请输入登录密码");
		}else {
			boolean success=userService.checkPwd(JBoltUserKit.getUserId(),password);
			if(success) {
				renderJson(onlineUserService.unlockCurrentUserScreen());
			}else {
				renderJsonFail("密码不正确");
			}
		}

	}

	@CheckPermission("dashboard")
	@UnCheckIfSystemAdmin
	public void dashboard(){
		render("dashboard.html");
	}

	@Clear
	public void relogin(){
        set(JBoltConst.USER_TYPE, getCookie(JBoltConst.USER_TYPE, "MOM"));
		render("relogin.html");
	}
	/**
	 * 手动操作退出系统
	 */
	@Clear
	public void logout(){
		HttpSession session=getSession();
		if(session!=null) {
			session.invalidate();
		}
		//当前用户自行离线
		onlineUserService.currentUserLogout();
		//删除cookie存的sessionId
		removeCookie(JBoltConst.JBOLT_SESSIONID_KEY);
		removeCookie(JBoltConst.JBOLT_SESSIONID_REFRESH_TOKEN);
		removeCookie(JBoltConst.JBOLT_KEEPLOGIN_KEY);
        String userType = getCookie(JBoltConst.USER_TYPE, "MOM");
		redirect(JBoltBaseHandler.processBasepath(getRequest()) + (UserTypeEnum.SRM.getValue().equalsIgnoreCase(userType) ? "admin/srmLogin" : StrUtil.EMPTY));
	}
	/**
	 * 进入一个异端顶替登录下线的提示界面
	 */
	@Clear
	public void terminalOffline() {
		setMsg(JBoltMsg.ADMIN_TERMINAL_OFFLINE_PAGE_MSG);
		render("offline.html");
	}
	/**
	 * 进入一个被强制下线的界面
	 */
	@Clear
	public void forcedOffline() {
		setMsg(JBoltMsg.ADMIN_TERMINAL_OFFLINE_PAGE_MSG);
		render("offline.html");
	}
	/**
	 * 登录
	 */
	@Clear(JBoltAdminAuthInterceptor.class)
	public void login(){
		//判断不是post就是进登录页面
		if(getRequest().getMethod().equalsIgnoreCase("post")==false) {
			render(getLoginFileName());
			return;
		}
		//创建登录日志
		LoginLog log=JBoltLoginLogUtil.createLoginLog(getRequest());
		log.setUsername(get("username"));

		//根据全局配置判断是否需要验证码 默认需要
		boolean checkCaptcha=JBoltGlobalConfigCache.me.isJBoltLoginUseCapture();
		if(checkCaptcha){
			boolean checkSuccess=validateCaptcha("captcha");
			if(!checkSuccess) {
				log.setLoginState(JBoltLoginState.CAPTURE_ERROR.getValue());
				log.save();
				renderJsonFail(JBoltLoginState.CAPTURE_ERROR.getText());
				return;
			}
		}

        Long orgId = getLong("orgId");
        ValidationUtils.validateId(orgId, "缺少组织参数");
        Org org = orgService.findById(orgId);
        ValidationUtils.isTrue(org.getEnable(), "该组织机构已被禁用");

		Ret ret=userService.getUser(get("username"),get("password"));
		User user = ret.isFail()?null:ret.getAs("data");
		//检测用户名密码是否正确输入并得到user
		if(user==null){
			log.setLoginState(JBoltLoginState.USERNAME_PWD_ERROR.getValue());
			log.save();
			renderJson(ret);
			return;
		}

        // 校验组织权限
        if (!user.getIsSystemAdmin()) {
            ValidationUtils.isTrue(userOrgService.checkUserHasOrg(user.getId(), orgId), ErrorMsg.ORG_ACCESS_DENIED);
        }

		log.setUserId(user.getId());
		//检测用户是否禁用
		if(user.getEnable()==null||user.getEnable()==false){
			log.setLoginState(JBoltLoginState.ENABLE_ERROR.getValue());
			log.save();
			renderJsonFail(JBoltLoginState.ENABLE_ERROR.getText());
			return;
		}

		//检测角色权限分配
		if(notOk(user.getRoles())&&(user.getIsSystemAdmin()==null||user.getIsSystemAdmin()==false)){
			log.setLoginState(JBoltLoginState.NOT_ASSIGN_ROLE_ERROR.getValue());
			log.save();
			renderJsonFail(JBoltLoginState.NOT_ASSIGN_ROLE_ERROR.getText());
			return;
		}

		log.setLoginState(JBoltLoginState.LOGIN_SUCCESS.getValue());
		log.save();

		//处理用户登录信息 异地登录异常信息
		boolean isRemoteLogin=userService.processUserRemoteLogin(user,log);
		userService.processUserLoginInfo(user,isRemoteLogin,log);
		//登录后的处理
		afterLogin(user,log, orgId, org.getOrgCode(), UserTypeEnum.MOM.getValue());
		renderJsonSuccess();
	}
	/**
	 * 获取登录页文件名
	 * @return
	 */
	private String getLoginFileName() {
		String fileName = JBoltGlobalConfigCache.me.getLoginFile();
		if(isOk(fileName)) {
			String filePath = PathKit.getWebRootPath()+File.separator+"_view"+File.separator+"_admin"+File.separator+"index"+File.separator+fileName;
			if(!FileUtil.exist(filePath)) {
				fileName = JBoltConst.JBOLT_ADMIN_LOGIN_DEFAULT_FILE;
			}
		}else {
			fileName = JBoltConst.JBOLT_ADMIN_LOGIN_DEFAULT_FILE;
		}
		return fileName;
	}
	/**
	 * 登录后设置用户相关的登录cookie
	 * @param user
	 * @param log
	 */
	private void afterLogin(User user, LoginLog log, Long orgId, String orgCode, String userType) {
		//处理onlineUser
		boolean keeplogin = getCheckBoxBoolean("keepLogin");
		Kv result=onlineUserService.processUserLogin(keeplogin,user,log, orgId, orgCode);
		if(result!=null && !result.isEmpty()) {
			int keepLoginSeconds = result.getInt("keepLoginSeconds");
			String sessionId = result.getStr("sessionId");
			//设置时长根据参数配置中的值 如果没配置或者获取配置异常 就按照默认8小时
			setCookie(JBoltConst.JBOLT_SESSIONID_KEY,sessionId,keepLoginSeconds,JFinal.me().getContextPath(),true);
			//设置refreshToken jwt
			int refreshTokenLiveSeconds = keepLoginSeconds + 28800;
			setCookie(JBoltConst.JBOLT_SESSIONID_REFRESH_TOKEN,onlineUserService.genNewSessionIdRefreshToken(user,sessionId,refreshTokenLiveSeconds, orgId, orgCode),refreshTokenLiveSeconds,JFinal.me().getContextPath(),true);
			//设置是否keepLogin
			setCookie(JBoltConst.JBOLT_KEEPLOGIN_KEY,keeplogin?"true":"false",refreshTokenLiveSeconds,JFinal.me().getContextPath(),true);
            // 设置用户类型
            setCookie(JBoltConst.USER_TYPE, userType, keepLoginSeconds, JFinal.me().getContextPath(),true);
		}
		//设置用户样式配置的cookie
		resetUserConfigCookie(user.getId());
	}
	/**
	 * 跳转到锁屏页面
	 */
	public void redirectToScreenLock() {
		JBoltControllerKit.renderSystemLockedPage(this);
	}
	/**
	 * 登录后重置登录页面用户设置cookie
	 * @param userId
	 */
	private void resetUserConfigCookie(Long userId) {
		int seconds = 31536000;
		//365天不失效 cookie
//		boolean glass=JBoltUserConfigCache.me.getJBoltLoginFormStyleGlass(userId);
//		setCookie("jbolt_login_glassStyle",glass+"",seconds,JFinal.me().getContextPath());
		boolean blur=JBoltUserConfigCache.me.getJBoltLoginBgimgBlur(userId);
		setCookie("jbolt_login_bgimgBlur",blur+"" ,seconds,JFinal.me().getContextPath());
		boolean nest=JBoltUserConfigCache.me.getJBoltLoginNest(userId);
		setCookie("jbolt_login_nest",nest+"" ,seconds,JFinal.me().getContextPath());
	}

	/**
	 * 验证码
	 */
	@Clear
	public void captcha(){
		renderJBoltCaptcha(JBoltGlobalConfigCache.me.getConfigValue(JBoltGlobalConfigKey.JBOLT_LOGIN_CAPTURE_TYPE));
	}


	/**
	 * 获取当前用户Cookie里的TOKEN-jboltid
	 */
	@UnCheck
	public void myToken(){
		Kv kv = Kv.by("token",JBoltUserKit.getUserSessionId());
		if(JBoltConfig.SAAS_ENABLE && JBoltSaasTenantKit.me.isSelfRequest()) {
			kv.set("tenantSn",JBoltSaasTenantKit.me.getSn());
		}
		renderJsonData(kv);
	}

    @Clear(JBoltAdminAuthInterceptor.class)
    public void srmLogin() {
        // 判断不是post就是进登录页面
        if(!HttpMethod.POST.name().equalsIgnoreCase(getRequest().getMethod())) {
            render("srm_login3.html");
            return;
        }

        //创建登录日志
        LoginLog log=JBoltLoginLogUtil.createLoginLog(getRequest());
        log.setUsername(get("username"));

        //根据全局配置判断是否需要验证码 默认需要
        boolean checkCaptcha=JBoltGlobalConfigCache.me.isJBoltLoginUseCapture();
        if(checkCaptcha){
            boolean checkSuccess=validateCaptcha("captcha");
            if(!checkSuccess) {
                log.setLoginState(JBoltLoginState.CAPTURE_ERROR.getValue());
                log.save();
                renderJsonFail(JBoltLoginState.CAPTURE_ERROR.getText());
                return;
            }
        }

        Long orgId = getLong("orgId");
        ValidationUtils.validateId(orgId, "组织参数");

        Org org = orgService.findById(orgId);
        ValidationUtils.notNull(org, "组织不存在");
        ValidationUtils.isTrue(org.getEnable(), "该组织已被禁用");

        Ret ret=userService.getUser(get("username"),get("password"));
        User user = ret.isFail()?null:ret.getAs("data");
        //检测用户名密码是否正确输入并得到user
        if(user==null){
            log.setLoginState(JBoltLoginState.USERNAME_PWD_ERROR.getValue());
            log.save();
            renderJson(ret);
            return;
        }

        UserType userType = UserTypeCache.ME.get(user.getUserTypeId());
        ValidationUtils.equals(UserTypeEnum.SRM.getValue(), userType.getUsertypeCode(), "您登录的账号，非SRM平台用户");

        log.setUserId(user.getId());
        //检测用户是否禁用
        if(user.getEnable()==null|| !user.getEnable()){
            log.setLoginState(JBoltLoginState.ENABLE_ERROR.getValue());
            log.save();
            renderJsonFail(JBoltLoginState.ENABLE_ERROR.getText());
            return;
        }

        //检测角色权限分配
        if(notOk(user.getRoles())&&(user.getIsSystemAdmin()==null|| !user.getIsSystemAdmin())){
            log.setLoginState(JBoltLoginState.NOT_ASSIGN_ROLE_ERROR.getValue());
            log.save();
            renderJsonFail(JBoltLoginState.NOT_ASSIGN_ROLE_ERROR.getText());
            return;
        }

        log.setLoginState(JBoltLoginState.LOGIN_SUCCESS.getValue());
        log.save();

        //处理用户登录信息 异地登录异常信息
        boolean isRemoteLogin=userService.processUserRemoteLogin(user,log);
        userService.processUserLoginInfo(user,isRemoteLogin,log);
        //登录后的处理
        afterLogin(user,log, orgId, org.getStr("code"), UserTypeEnum.SRM.getValue());
        renderJsonSuccess();
    }

    /**
     * 切换登录组织
     */
    @Clear(JBoltAdminAuthInterceptor.class)
    public void switchOrg() {
        String userType = getCookie(JBoltConst.USER_TYPE, "MOM");
        if (HttpMethod.GET.name().equalsIgnoreCase(getRequest().getMethod())) {
            set(JBoltConst.USER_TYPE, userType);
            render("switch_org.html");
            return;
        }

        Long orgId = getLong("orgId");
        ValidationUtils.validateId(orgId, "组织参数");

        Org org = orgService.findById(orgId);
        ValidationUtils.notNull(org, "组织不存在");
        ValidationUtils.isTrue(org.getEnable(), "该组织已被禁用");

        String sessionId = getCookie(JBoltConst.JBOLT_SESSIONID_KEY);

        // 查询当前会话用户
        OnlineUser onlineUser = JBoltOnlineUserCache.me.getBySessionId(sessionId);
        onlineUser.setOrgId(orgId);
        onlineUser.setOrgCode(org.getStr("code"));
        onlineUser.update();
        // 删除当前用户的缓存
        onlineUserService.deleteCacheByKey(sessionId);

        renderJsonSuccess();
    }

}
