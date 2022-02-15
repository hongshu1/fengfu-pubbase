package cn.jbolt._admin.interceptor;

import java.lang.reflect.Method;
import java.util.Set;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import cn.jbolt.core.Interceptor.JBoltSecurityCheck;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltPermissionCache;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.kit.JBoltControllerKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.util.JBoltArrayUtil;


/**
 * JBolt管理后台权限校验拦截器
 * @ClassName:  AdminAuthInterceptor   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2019年9月12日   
 */
public class JBoltAdminAuthInterceptor implements Interceptor {
	@Override
	public void intercept(Invocation inv) {
		Controller ctl =inv.getController();
		if (!(ctl instanceof JBoltBaseController)) {
			throw new RuntimeException(JBoltMsg.INTERCEPTOR_MUST_EXTEND_JBOLT_BASE_CONTROLLER);
		}
		JBoltBaseController controller=(JBoltBaseController) ctl;
		//是否登录admin后台
		boolean isAdminLogin = controller.isAdminLogin();
		if (!isAdminLogin) {
			JBoltControllerKit.renderAdminInterceptorNotLoginInfo(controller);
			return;
		}
		//登录后拿到userId
		User user = JBoltUserKit.getUser();
		if(user == null ) {
			JBoltControllerKit.renderAdminInterceptorNotLoginInfo(controller);
			return;
		}
		//判断锁屏
//		if(JBoltUserKit.userScreenIsLocked()) {
//			if(JBoltConst.UNLOCKSYSTEM_ACTION_URL.equals(inv.getActionKey())) {
//				inv.invoke();
//				return;
//			}else {
//				//直接访问action
//				if(JBoltControllerKit.isNormal(controller) || JBoltControllerKit.isIframe(controller) || JBoltControllerKit.isDialog(controller)) {
//					JBoltControllerKit.renderSystemLockedPage(controller);
//				}else {
//					//ajax pjax ajaxPortal等访问
//					JBoltControllerKit.renderAdminSystemLockedInfo(controller);
//				}
//				return;
//			}
//		}
		//uncheck是只校验上面的登录 不校验其它 如果controller上直接写了uncheck注解 只要登录了就直接过
		if(JBoltSecurityCheck.isUncheck(controller)){
			inv.invoke();
			return;
		}
		Method method=inv.getMethod();
		//uncheck是只校验上面的登录 不校验其它
		if(JBoltSecurityCheck.isUncheck(method)){
			inv.invoke();
			return;
		}
		//具体有哪些注解
		String[] permissionKeys=null;
		//如果是超管，判断是不是超管默认的，是的话就直接过
		if(user.getIsSystemAdmin()) {
			//如果设置了是超级管理员可以直接访问的权限注解 不校验其它
			if(JBoltSecurityCheck.isUncheckIfSystemAdmin(controller,method)) {
				inv.invoke();
				return;
			}
			// 得到具体的可以校验的注解
			permissionKeys = getPermissionKeys(controller, method);
			if(JBoltArrayUtil.isEmpty(permissionKeys)){
				// 如果没有权限 返回错误信息
				JBoltControllerKit.renderFail(controller,JBoltMsg.INTERCEPTOR_NO_PERMISSIONKEY);
				return;
			}
			boolean checkAll=isCheckAll(controller,method);
			//如果没设置 就得拿到当前访问的actionkey去找到对应权限定义 在判断是不是超管可以直接访问
			boolean isSystemAdminDefault=JBoltSecurityCheck.checkIsSystemAdminDefaultPermission(checkAll,permissionKeys);
			if(isSystemAdminDefault) {
				inv.invoke();
				return;
			}
		}else {
			// 得到具体的可以校验的注解
			permissionKeys = getPermissionKeys(controller, method);
		}
		//上面都没满足 那么就乖乖按照规矩来 分配了角色权限才能过
		if(JBoltArrayUtil.isEmpty(permissionKeys)){
			// 如果没有权限 返回错误信息
			JBoltControllerKit.renderFail(controller,JBoltMsg.INTERCEPTOR_NO_PERMISSIONKEY);
			return;
		}
		//拿到登录用户所分配的角色
		String roleIds = JBoltUserKit.getUserRoleIds();
		//从cache中找到这些角色对应的权限绑定集合
		Set<String> permissionKeySet = JBoltPermissionCache.me.getRolePermissionKeySet(roleIds);
		if (permissionKeySet == null || permissionKeySet.isEmpty()) {
			// 如果没有权限 返回错误信息
			JBoltControllerKit.renderFail(controller,JBoltMsg.INTERCEPTOR_NO_AUTH_ASSIGN);
			return;
		}
		boolean checkAll=isCheckAll(controller,method);
		//检测拦截到正在访问的controller+action上需要校验的权限资源 拿到后去跟缓存里当前用户所在的角色下的所有资源区对比
		boolean exist = JBoltSecurityCheck.checkHasPermission(checkAll,permissionKeySet, permissionKeys);
		if (!exist) {
			// 如果没有权限 返回错误信息
			JBoltControllerKit.renderFail(controller,JBoltMsg.INTERCEPTOR_CHECK_NO_AUTH);
			return;
		}
		// 最后执行action
		inv.invoke();
	}

	

	/**
	 * 判断是否checkall
	 * @param controller
	 * @param method
	 * @return
	 */
	private boolean isCheckAll(Controller controller, Method method) {
		boolean mc=JBoltSecurityCheck.isPermissionCheck(method);
		if(mc){
			CheckPermission per = method.getAnnotation(CheckPermission.class);
			return per.checkAll();
		}
		boolean cc=JBoltSecurityCheck.isPermissionCheck(controller);
		if(cc) {
			CheckPermission per = controller.getClass().getAnnotation(CheckPermission.class);
			return per.checkAll();
		}
		
		return false;
	}




	/**
	 * 得到需要校验的permissionKey
	 * @param controller
	 * @param method
	 * @return
	 */
	private String[] getPermissionKeys(Controller controller, Method method) {
		boolean mc=JBoltSecurityCheck.isPermissionCheck(method);
		boolean cc=JBoltSecurityCheck.isPermissionCheck(controller);
		if(!mc&&!cc){
			return null;
		}
		String[] temps=null;
		if(mc){
			CheckPermission per = method.getAnnotation(CheckPermission.class);
			String[] values = per.value();
			if (values == null || values.length == 0) {
				return null;
			}
			temps=values;
		}
		if(cc&&temps==null){
			CheckPermission per = controller.getClass().getAnnotation(CheckPermission.class);
			String[] values = per.value();
			if (values == null || values.length == 0) {
				return null;
			}
			temps=values;
		}
		
		return temps;
	}
}
