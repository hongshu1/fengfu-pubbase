package cn.jbolt.base;

import java.util.Objects;
import java.util.function.Supplier;

import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.wxaapp.WxaConfigKit;

/**
 * 公众平台API 
 * 利用 use 方法代替拦截器设定 appid
 */
public class JBoltWechatApi {
	private static final ApiResult NO_ASSIGN_TYPE=new ApiResult("{errorCode:-1000,errmsg:\"API调用未指明正确的账号类型\"}");
	public enum Type{
		MP_NORMAL,//服务号 订阅号
		MP_WXA,//小程序
		MP_WORK,//企业微信
	}
	private Type type;
	private String appId;
	/**
	 * 核心调用创建实例对象 传入调用账号的appId
	 * @param appId
	 * @return
	 */
	public static JBoltWechatApi use(String appId) {
		Objects.requireNonNull(appId, "appId can not be null");
		return new JBoltWechatApi(appId);
	}
	/**
	 * 核心调用创建实例对象 传入调用账号的appId
	 * @param appId
	 * @return
	 */
	public static JBoltWechatApi use(String appId,Type type) {
		Objects.requireNonNull(appId, "appId can not be null");
		return new JBoltWechatApi(appId,type);
	}
	
	private JBoltWechatApi(String appId) {
		this(appId, Type.MP_NORMAL);
	}
	
	private JBoltWechatApi(String appId,Type type) {
		this.appId = appId;
		this.type=type;
	}
	/**
	 * 核心call
	 * @param call
	 * @return
	 */
	public <T> T call(Supplier<T> function) {
		T result=null;
		switch (type) {
		case MP_WXA:
			result=callWxa(function);
			break;
		case MP_NORMAL:
			result=callNormal(function);
			break;
		case MP_WORK:
			result=callWork(function);
			break;
		default:
			result=(T)NO_ASSIGN_TYPE;
			break;
		}
		return result;
	}
	/**
	 * 调用微信小程序的
	 * @param call
	 * @return
	 */
	private <T> T callWxa(Supplier<T> function) {
		try {
			WxaConfigKit.setThreadLocalAppId(appId);
			return function.get();
		} finally {
			WxaConfigKit.removeThreadLocalAppId();
		}
	}
	/**
	 * 调用普通服务号 订阅号
	 * @param call
	 * @return
	 */
	private <T> T callNormal(Supplier<T> function) {
		try {
			ApiConfigKit.setThreadLocalAppId(appId);
			return function.get();
		} finally {
			ApiConfigKit.removeThreadLocalAppId();
		}
	}
	
	/**
	 * 调用企业微信
	 * @param call
	 * @return
	 */
	private <T> T callWork(Supplier<T> function) {
		//TODO 暂时不能用 需要等待支持企业微信
		try {
			ApiConfigKit.setThreadLocalAppId(appId);
			return function.get();
		} finally {
			ApiConfigKit.removeThreadLocalAppId();
		}
	}

}



