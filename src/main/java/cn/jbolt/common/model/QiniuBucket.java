package cn.jbolt.common.model;

import cn.jbolt._admin.cache.JBoltQiniuCache;
import cn.jbolt.common.model.base.BaseQiniuBucket;
import cn.jbolt.common.util.CACHE;
import cn.jbolt.core.annotation.JBoltAutoCache;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.cache.JBoltUserCache;

/**
 * 七牛bucket配置
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "main" , table = "jb_qiniu_bucket" , primaryKey = "id" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
@JBoltAutoCache(keyCache = true, column = "sn")
public class QiniuBucket extends BaseQiniuBucket<QiniuBucket> {
	public String getQiniuName() {
		return JBoltQiniuCache.me.getName(getQiniuId());
	}
	public String getRegionName() {
		return JBoltDictionaryCache.me.getNameBySn("qiniu_region", getRegion());
	}
	public String getCreateUserName() {
		return JBoltUserCache.me.getName(getCreateUserId());
	}
	public String getUpdateUserName() {
		return JBoltUserCache.me.getName(getUpdateUserId());
	}
}
