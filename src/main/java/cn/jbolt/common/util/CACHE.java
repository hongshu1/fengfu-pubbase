package cn.jbolt.common.util;

import cn.hutool.core.util.ClassUtil;
import cn.jbolt.core.base.config.JBoltConfig;
import cn.jbolt.core.bean.Option;
import cn.jbolt.core.bean.OptionBean;
import cn.jbolt.core.cache.JBoltCache;
import cn.jbolt.core.consts.JBoltConst;
import cn.jbolt.core.service.base.JBoltCommonService;
import com.jfinal.plugin.ehcache.IDataLoader;
import cn.jbolt.core.cache.JBoltCacheKit;
import cn.jbolt.core.cache.JBoltCacheParaValidator;

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

}
