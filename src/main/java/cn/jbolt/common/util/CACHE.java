package cn.jbolt.common.util;

import cn.hutool.core.util.ClassUtil;
import cn.jbolt._admin.dept.DeptService;
import cn.jbolt._admin.qiniu.QiniuBucketService;
import cn.jbolt._admin.qiniu.QiniuService;
import cn.jbolt._admin.user.UserService;
import cn.jbolt._admin.userconfig.UserConfigService;
import cn.rjtech.admin.uomclass.UomclassService;
import cn.rjtech.admin.workclass.WorkClassService;
import cn.rjtech.admin.appdevcenter.ApplicationService;
import cn.rjtech.admin.wechat.autoreply.WechatReplyContentService;
import cn.rjtech.admin.wechat.user.WechatUserService;
import cn.jbolt.common.model.Qiniu;
import cn.jbolt.common.model.QiniuBucket;
import cn.jbolt.common.model.WechatUser;
import cn.jbolt.core.base.config.JBoltConfig;
import cn.jbolt.core.bean.Option;
import cn.jbolt.core.bean.OptionBean;
import cn.jbolt.core.cache.JBoltCache;
import cn.jbolt.core.cache.JBoltCacheKit;
import cn.jbolt.core.cache.JBoltCacheParaValidator;
import cn.jbolt.core.cache.JBoltWechatConfigCache;
import cn.jbolt.core.consts.JBoltConst;
import cn.jbolt.core.model.Application;
import cn.jbolt.core.model.Dept;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.JBoltCommonService;
import cn.rjtech.model.momdata.Uomclass;
import cn.rjtech.model.momdata.Workclass;

import com.jfinal.aop.Aop;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.jfinal.weixin.sdk.msg.out.OutMsg;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * 全局CACHE操作工具类
 *
 * @ClassName: CACHE
 * @author: JFinal学院-小木 QQ：909854136
 * @date: 2019年11月13日
 */
public class CACHE extends JBoltCacheParaValidator {
	public static final CACHE me = new CACHE();
	public static final String JBOLT_WECAHT_KEYWORDS_CACHE_NAME = "jbolt_cache_wechat_keywords";
	private UserService userService = Aop.get(UserService.class);
	private UserConfigService userConfigService = Aop.get(UserConfigService.class);
	private WechatUserService wechatUserService = Aop.get(WechatUserService.class);
	private ApplicationService applicationService = Aop.get(ApplicationService.class);
	private WechatReplyContentService wechatReplyContentService = Aop.get(WechatReplyContentService.class);
	private DeptService deptService = Aop.get(DeptService.class);
	private QiniuService qiniuService = Aop.get(QiniuService.class);
	private QiniuBucketService qiniuBucketService = Aop.get(QiniuBucketService.class);
	private WorkClassService workclassService = Aop.get(WorkClassService.class);
	private UomclassService uomclassService = Aop.get(UomclassService.class);

	private String buildCacheKey(String pre, Object value) {
		return JBoltConst.JBOLT_CACHE_DEFAULT_PREFIX + pre + value.toString();
	}

	/**
	 * put
	 *
	 * @param key
	 * @param value
	 */
	public void put(String key, Object value) {
		JBoltCacheKit.put(JBoltConfig.JBOLT_CACHE_NAME, key, value);
	}

	/**
	 * get
	 *
	 * @param <T>
	 * @param key
	 * @return
	 */
	public <T> T get(String key) {
		return JBoltCacheKit.get(JBoltConfig.JBOLT_CACHE_NAME, key);
	}

	/**
	 * 从cache获取到公众平台默认回复
	 *
	 * @return
	 */
	public OutMsg getWechcatDefaultOutMsg(String appId, String openId) {
		if (StrKit.isBlank(appId)) {
			return null;
		}
		return JBoltCacheKit.get(JBoltConfig.JBOLT_CACHE_NAME, buildCacheKey("mpaureply_defaultmsg_", appId), new IDataLoader() {
			@Override
			public Object load() {
				return wechatReplyContentService.getWechcatDefaultOutMsg(appId, openId);
			}
		});
	}

	/**
	 * 从cache获取到公众平台关注回复
	 *
	 * @param appId
	 * @param openId
	 * @return
	 */
	public OutMsg getWechcatSubscribeOutMsg(String appId, String openId) {
		if (StrKit.isBlank(appId)) {
			return null;
		}
		return JBoltCacheKit.get(JBoltConfig.JBOLT_CACHE_NAME, buildCacheKey("mpaureply_subscribemsg_", appId), new IDataLoader() {
			@Override
			public Object load() {
				return wechatReplyContentService.getWechcatSubscribeOutMsg(appId, openId);
			}
		});
	}

	/**
	 * 删除微信公众平台默认自动回复消息
	 *
	 * @param mpId
	 */
	public void removeWechcatDefaultOutMsg(Long mpId) {
		removeWechcatDefaultOutMsg(JBoltWechatConfigCache.me.getAppId(mpId));
	}

	/**
	 * 删除微信公众平台关注自动回复消息
	 *
	 * @param mpId
	 */
	public void removeWechcatSubscribeOutMsg(Long mpId) {
		removeWechcatSubscribeOutMsg(JBoltWechatConfigCache.me.getAppId(mpId));
	}

	/**
	 * 删除微信公众平台关注自动回复消息
	 *
	 * @param appId
	 */
	public void removeWechcatSubscribeOutMsg(String appId) {
		if (StrKit.notBlank(appId)) {
			JBoltCacheKit.remove(JBoltConfig.JBOLT_CACHE_NAME, buildCacheKey("mpaureply_subscribemsg_", appId));
		}
	}

	/**
	 * 删除微信公众平台默认自动回复消息
	 *
	 * @param appId
	 */
	public void removeWechcatDefaultOutMsg(String appId) {
		if (StrKit.notBlank(appId)) {
			JBoltCacheKit.remove(JBoltConfig.JBOLT_CACHE_NAME, buildCacheKey("mpaureply_defaultmsg_", appId));
		}
	}



	/**
	 * 根据mpid和openId拿到wechat用户信息 api专用
	 *
	 * @param mpId
	 * @param openId
	 * @return
	 */
	public WechatUser getApiWechatUserByMpOpenId(Long mpId, String openId) {
		if (mpId == null || StrKit.isBlank(openId)) {
			return null;
		}
		return JBoltCacheKit.get(JBoltConfig.JBOLT_CACHE_NAME,
				buildCacheKey("api_wechat_user_openid_", mpId.toString() + "_" + openId), new IDataLoader() {
					@Override
					public Object load() {
						return wechatUserService.getApiWechatUserByOpenId(mpId, openId);
					}
				});
	}

	/**
	 * 根据mpid和openId删除wechat用户信息 api专用
	 *
	 * @param mpId
	 * @param openId
	 */
	public void removeApiWechatUserByMpOpenId(Long mpId, String openId) {
		if (mpId != null && StrKit.notBlank(openId)) {
			JBoltCacheKit.remove(JBoltConfig.JBOLT_CACHE_NAME,
					buildCacheKey("api_wechat_user_openid_", mpId.toString() + "_" + openId));
		}
	}

	/**
	 * 通过用户名 拿到用户信息
	 *
	 * @param username
	 * @return
	 */
	public User getUserByUsername(String username) {
		User user = userService.getCacheByKey(username);
		if (user != null) {
			user.removeNullValueAttrs().remove("password");
		}
		return user;
	}

	/**
	 * 用过用户名拿到用户ID
	 *
	 * @param username
	 * @return
	 */
	public Object getUserIdByUsername(String username) {
		User user = getUserByUsername(username);
		return user == null ? null : user.getId();
	}


	/**
	 * 从缓存里获取wechatUser
	 *
	 * @param mpId
	 * @param id
	 * @return
	 */
	public WechatUser getApiWechatUserByApiUserId(Long mpId, Object id) {
		return JBoltCacheKit.get(JBoltConfig.JBOLT_CACHE_NAME, "wecaht_user_" + mpId + "_" + id, new IDataLoader() {
			@Override
			public Object load() {
				WechatUser wechatUser = wechatUserService.findByIdToWechatUser(mpId, id);
				if (wechatUser != null) {
					wechatUser.removeNullValueAttrs().remove("last_auth_time","first_auth_time","first_login_time","last_login_time","checked_time","remark","group_id","tag_ids","subscribe_scene","qr_scene","qr_scene_str","check_code","session_key","update_time");
				}
				return wechatUser;
			}
		});
	}

	/**
	 * 删除wechatUser
	 */
	public void removeApiWechatUser(Long mpId, Object id) {
		JBoltCacheKit.remove(JBoltConfig.JBOLT_CACHE_NAME, "wecaht_user_" + mpId + "_" + id);
	}



	/**
	 * 通过七牛账号ID 获取七牛账号
	 *
	 * @param id
	 * @return
	 */
	public Qiniu getQiniu(Object id) {
		return qiniuService.findById(id);
	}

	/**
	 * 通过七牛账号SN 获取七牛账号
	 *
	 * @param sn
	 * @return
	 */
	public Qiniu getQiniuBySn(String sn) {
		return qiniuService.getCacheByKey(sn);
	}

	/**
	 * 通过七牛账号ID 获取七牛Name
	 *
	 * @param id
	 * @return
	 */
	public String getQiniuName(Object id) {
		Qiniu qiniu = getQiniu(id);
		return qiniu == null ? null : qiniu.getName();
	}

	/**
	 * 通过七牛账号SN 获取七牛Name
	 *
	 * @param sn
	 * @return
	 */
	public String getQiniuName(String sn) {
		Qiniu qiniu = getQiniuBySn(sn);
		return qiniu == null ? null : qiniu.getName();
	}

	/**
	 * 获得bucket所在七牛账号
	 *
	 * @param bucketSn
	 * @return
	 */
	public Qiniu getQiniuByBucketSn(String bucketSn) {
		return getQiniu(getQiniuIdByBucketSn(bucketSn));
	}

	/**
	 * 获得bucket所在七牛账号Id
	 *
	 * @param bucketSn
	 * @return
	 */
	public Object getQiniuIdByBucketSn(String bucketSn) {
		QiniuBucket qiniuBucket = getQiniuBucketBySn(bucketSn);
		return qiniuBucket == null ? null : qiniuBucket.getQiniuId();
	}

	/**
	 * 根据bucket Id获取qiniubucket
	 *
	 * @param id
	 * @return
	 */
	public QiniuBucket getQiniuBucket(Object id) {
		return qiniuBucketService.findById(id);
	}

	/**
	 * 根据bucket sn获取qiniubucket
	 *
	 * @param sn
	 * @return
	 */
	public QiniuBucket getQiniuBucketBySn(String sn) {
		return qiniuBucketService.getCacheByKey(sn);
	}

	/**
	 * 获取默认七牛账号
	 *
	 * @return
	 */
	public Qiniu getQiniuDefault() {
		return qiniuService.getDefault();
	}

	/**
	 * 获取指定七牛账号下的默认bucket
	 *
	 * @param qiniuId
	 * @return
	 */
	public QiniuBucket getQiniuDefaultBucket(Object qiniuId) {
		return qiniuBucketService.getQiniuBucketDefault(qiniuId);
	}

	/**
	 * 从后台获取 平台pc内置自身app
	 *
	 * @return
	 */
	public Application getPcInnerPlatformApplication() {
		return JBoltCacheKit.get(JBoltConfig.JBOLT_CACHE_NAME, "jbolt_pc_inner_platform_app", new IDataLoader() {
			@Override
			public Object load() {
				return applicationService.checkAndInitPcInnerPlatformApplication();
			}
		});
	}

	/**
	 * 删除平台pc内置自身app
	 */
	public void removePcInnerPlatformApplication() {
		JBoltCacheKit.remove(JBoltConfig.JBOLT_CACHE_NAME, "jbolt_pc_inner_platform_app");
	}

	/**
	 * 获取项目里的枚举类
	 * @return
	 */
	public static List<Option> getCodeGenEnums(){
		return JBoltCacheKit.get(JBoltConfig.JBOLT_CACHE_NAME, "codeGen_enums",new IDataLoader() {
			@Override
			public Object load() {
				return getJboltCodeGenEnums();
			}
		});
	}

	private static List<Option> getJboltCodeGenEnums() {
		Set<Class<?>> enums = ClassUtil.scanPackage("cn.jbolt.core", filter->{
			return 	filter.isEnum();
		});
		List<Option> options = new ArrayList<>();
		if(enums!=null) {
			enums.forEach(en->{
				options.add(new OptionBean(en.getName()));
			});
		}
		Set<Class<?>> enums2 = ClassUtil.scanPackage(null, filter->{
			return 	filter.isEnum();
		});
		if(enums2!=null && enums2.size()>0) {
			enums2.forEach(en->{
				options.add(new OptionBean(en.getName()));
			});
		}
		if(options.size()>0){
			//排序
			options.sort(new Comparator<Option>() {
				@Override
				public int compare(Option o1, Option o2) {
					return o1.getText().compareTo(o2.getText());
				}
			});
		}
		return options;
	}

	/**
	 * 获取项目中的caches
	 * @return
	 */
	public static List<Option> getCodeGenCaches() {
		return JBoltCacheKit.get(JBoltConfig.JBOLT_CACHE_NAME, "codeGen_caches",new IDataLoader() {
			@Override
			public Object load() {
				return getJboltCodeGenCaches();
			}
		});
	}

	private static List<Option> getJboltCodeGenCaches() {
		Set<Class<?>> caches = ClassUtil.scanPackageBySuper("cn.jbolt.core",JBoltCache.class);
		List<Option> options = new ArrayList<>();
		if(caches!=null && caches.size()>0) {
			caches.forEach(en->{
				options.add(new OptionBean(en.getName()));
			});
		}
		Set<Class<?>> caches2 = ClassUtil.scanPackageBySuper(null,JBoltCache.class);
		if(caches2!=null && caches2.size()>0) {
			caches2.forEach(en->{
				options.add(new OptionBean(en.getName()));
			});
		}
		if(options.size()>0){
			//排序
			options.sort(new Comparator<Option>() {
				@Override
				public int compare(Option o1, Option o2) {
					return o1.getText().compareTo(o2.getText());
				}
			});
		}
		return options;
	}

	/**
	 * 获取项目中的Service
	 * @return
	 */
	public static List<Option> getCodeGenServices() {
		return JBoltCacheKit.get(JBoltConfig.JBOLT_CACHE_NAME, "codeGen_services",new IDataLoader() {
			@Override
			public Object load() {
				return getJboltCodeGenServices();
			}
		});
	}

	private static List<Option> getJboltCodeGenServices() {
		Set<Class<?>> coreServices = ClassUtil.scanPackageBySuper("cn.jbolt.core", JBoltCommonService.class);
		List<Option> options = new ArrayList<>();
		if(coreServices!=null) {
			coreServices.forEach(en->{
				options.add(new OptionBean(en.getName()));
			});
		}
		Set<Class<?>> service2 = ClassUtil.scanPackageBySuper(null,JBoltCommonService.class);
		if(service2!=null && service2.size()>0) {
			service2.forEach(en->{
				options.add(new OptionBean(en.getName()));
			});
		}
		int size = options.size();
		if(size>0){
			//排序
			options.sort(new Comparator<Option>() {
				@Override
				public int compare(Option o1, Option o2) {
					return o1.getText().compareTo(o2.getText());
				}
			});
		}
		return options;
	}

	/**
	 * 删除codegen用的caches
	 */
	public void removeCodeGenCaches() {
		JBoltCacheKit.remove(JBoltConfig.JBOLT_CACHE_NAME, "codeGen_caches");
	}

	/**
	 * 删除codegen用的enums
	 */
	public void removeCodeGenEnums() {
		JBoltCacheKit.remove(JBoltConfig.JBOLT_CACHE_NAME, "codeGen_enums");
	}

	/**
	 * 删除codegen用的services
	 */
	public void removeCodeGenServices() {
		JBoltCacheKit.remove(JBoltConfig.JBOLT_CACHE_NAME, "codeGen_services");
	}

    /**
     * 通过用户id拿到用户名
     */

    public String getUserUsernameById(Object id){
        User user = getUser(id);
        return user==null ? null:user.getUsername();
    }

    /**
     * 通过ID获得User
     *
     * @return
     */
    public User getUser(Object id) {
        return userService.findById(id);
    }

    /**
     * 通过部门名称获取部门ID
     * @param sn
     * @return
     */
    public Long getDeptIdByCode(String sn) {
        //部门Service未实现
        Dept dept = deptService.getCacheByKey(sn);
        return dept==null?null:dept.getId();
    }

	public Long getWorkClassIdByCode(String code) {
		Workclass workclass = workclassService.getCacheByKey(code);
		return notOk(workclass)?null:workclass.getIautoid();
	}

	/**
	 * 获得用户用户名
	 *
	 * @param id
	 * @return
	 */
	public String getUserUsername(Object id) {
		User user = getUser(id);
		return user == null ? "" : user.getUsername();
	}

    public Long getUomClassIdByCode(String code) {
		Uomclass uomclass = uomclassService.getCacheByKey(code);
		return notOk(uomclass)?null:uomclass.getIAutoId();
    }
}
