package cn.rjtech.config;

import cn.jbolt.core.base.config.JBoltConfig;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

/**
 * U9接口调用配置
 */
public class U9ApiConfigKey {

    private static final String U9API_PATH = "u9apipath.properties";
    private static final Prop U9API_PROP = PropKit.use(U9API_PATH);

    /**
     * 是否开启U9调用
     */
    public static boolean isU9ApiEnabled() {
        return JBoltConfig.prop.getBoolean("u9.api.enabled");
    }
    /**
     * U9组织请求标识
     */
    public static String U9Entcode(){
        return JBoltConfig.prop.get("u9api.entcode");
    }

    /**
     * U9Api服务地址
     */
    public static String U9ApiPath(){
        return JBoltConfig.prop.get("u9api.path");
    }

    /**
     * 生产订单
     */
    public static String commoncreatemo(){
        return U9ApiPath() + U9API_PROP.get("u9api.commoncreatemo");
    }
    /**
     * 审核生产订单
     */
    public static String commonapprovemo(){
        return U9ApiPath() + U9API_PROP.get("u9api.commonapprovemo");
    }
    /**
     * 弃审生产订单
     */
    public static String commonunapprovemo(){
        return U9ApiPath() + U9API_PROP.get("u9api.commonunapprovemo");
    }
    /**
     * 删除生产订单
     */
    public static String commondeletemo(){
        return U9ApiPath() + U9API_PROP.get("u9api.commondeletemo");
    }

    /**
     * 入库单
     */
    public static String commoncreatervcrpt(){
        return U9ApiPath() + U9API_PROP.get("u9api.commoncreatervcrpt");
    }
    /**
     * 审核入库单
     */
    public static String commonapprovervcrpt(){
        return U9ApiPath() + U9API_PROP.get("u9api.commonapprovervcrpt");
    }
    /**
     * 弃审入库单
     */
    public static String commonunapprovervcrpt(){
        return U9ApiPath() + U9API_PROP.get("u9api.commonunapprovervcrpt");
    }
    /**
     * 删除入库单
     */
    public static String commondeletervcrpt(){
        return U9ApiPath() + U9API_PROP.get("u9api.commondeletervcrpt");
    }

    /**
     * 杂发单
     */
    public static String commoncreatemis(){
        return U9ApiPath() + U9API_PROP.get("u9api.commoncreatemis");
    }
    /**
     * 审核杂发单
     */
    public static String commonapprovemis(){
        return U9ApiPath() + U9API_PROP.get("u9api.commonapprovemis");
    }
    /**
     * 弃审杂发单
     */
    public static String commonunapprovemis(){
        return U9ApiPath() + U9API_PROP.get("u9api.commonunapprovemis");
    }
    /**
     * 删除杂发单
     */
    public static String commondeletemis(){
        return U9ApiPath() + U9API_PROP.get("u9api.commondeletemis");
    }

    /**
     * 请购单
     */
    public static String commoncreatepr(){
        return U9ApiPath() + U9API_PROP.get("u9api.commoncreatepr");
    }
    /**
     * 审核请购单
     */
    public static String commonapprovepr(){
        return U9ApiPath() + U9API_PROP.get("u9api.commonapprovepr");
    }
    /**
     * 弃审请购单
     */
    public static String commonunapprovepr(){
        return U9ApiPath() + U9API_PROP.get("u9api.commonunapprovepr");
    }
    /**
     * 删除请购单
     */
    public static String commondeletepr(){
        return U9ApiPath() + U9API_PROP.get("u9api.commondeletepr");
    }

    /**
     * 领料单
     */
    public static String commoncreateissue(){
        return U9ApiPath() + U9API_PROP.get("u9api.commoncreateissue");
    }
    /**
     * 审核领料单
     */
    public static String commonapproveissue(){
        return U9ApiPath() + U9API_PROP.get("u9api.commonapproveissue");
    }
    /**
     * 弃审领料单
     */
    public static String commonunapproveissue(){
        return U9ApiPath() + U9API_PROP.get("u9api.commonunapproveissue");
    }
    /**
     * 删除领料单
     */
    public static String commondeleteissue(){
        return U9ApiPath() + U9API_PROP.get("u9api.commondeleteissue");
    }

}
