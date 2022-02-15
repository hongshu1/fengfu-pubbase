package cn.jbolt.wxa.api;

import com.jfinal.aop.Inject;
import com.jfinal.core.ActionKey;

import cn.jbolt.core.api.JBoltApiBaseController;
import cn.jbolt.core.api.JBoltApiBindUserBean;
import cn.jbolt.core.api.JBoltApplyJWT;
import cn.jbolt.core.api.JBoltReApplyJWT;
import cn.jbolt.core.api.httpmethod.JBoltHttpPost;
import cn.jbolt.core.crossorigin.CrossOrigin;
import cn.jbolt.core.para.JBoltPara;
/**
 * JBolt平台 后端 小程序API
 * @ClassName:  WxaApiController   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2020年3月16日   
 */
@CrossOrigin
public class JBoltWxaApiController extends JBoltApiBaseController {
	@Inject
	private JBoltWxaApiService wxapApiService;
	
	/**
	 * wxLogin 
	  * 只允许POST提交
	  * 签发JWT
	 */
	@JBoltApplyJWT
	@JBoltHttpPost
	public void login(String code) {
		//调动service业务逻辑执行code转session获取openId等
		renderJBoltApiRet(wxapApiService.login(code));
	}
	/**
	 * 解密注册用户信息
	 */
	public void decryptUserInfo(JBoltPara param) {
		//调动service业务逻辑执行解密注册
		renderJBoltApiRet(wxapApiService.decryptUserInfo(param));
	}
	
	/**
	 * 解密手机号授权登录信息
	 */
	public void decryptPhoneNumber(JBoltPara param) {
		//调动service业务逻辑执行解密手机号注册
		renderJBoltApiRet(wxapApiService.decryptPhoneNumber(param));
	}
	
	/**
	 * 通过用户名和密码 绑定系统系统内其他用户表与小程序用户
	 * 比如有学生表 教师表 系统内置user表等具体在JBoltApiBindUserBean.java里有常量定义
	 * 必须post提交
	 * 必须携带jwt 执行上面login之后才能调用这个bind
	 */
	@JBoltHttpPost
	@JBoltReApplyJWT
	public void bindOtherUser(JBoltPara param) {
		//调动service业务逻辑执行系统用户绑定逻辑
		renderJBoltApiRet(wxapApiService.bindOtherUser(param.getInteger("type"),param.getString("username"),param.getString("password")));
	}

	/**
	 * 通过系统用户 用户名和密码 登录
	 */
	@JBoltHttpPost
	@JBoltReApplyJWT
	public void loginSystemUser(JBoltPara param) {
		//调动service业务逻辑执行系统用户绑定逻辑
		renderJBoltApiRet(wxapApiService.bindOtherUser(JBoltApiBindUserBean.TYPE_SYSTEM_USER,param.getString("username"),param.getString("password")));
	}
	
	/**
	 * 解绑
	 * 必须post提交
	 * 必须携带jwt
	 */
	@JBoltHttpPost
	@JBoltReApplyJWT
	public void unbindOtherUser(JBoltPara param) {
		//调动service业务逻辑执行系统用户解除绑定逻辑
		renderJBoltApiRet(wxapApiService.unbindOtherUser(param.getInteger("type"),param.get("userId")));
	}

	/**
	 * 获取自己的用户信息
	 * @param param
	 */
	@ActionKey("/api/wxa/user/me")
	public void myWechatUserInfo() {
		renderJBoltApiRet(wxapApiService.getMyWechatUserInfo());
	}
}
