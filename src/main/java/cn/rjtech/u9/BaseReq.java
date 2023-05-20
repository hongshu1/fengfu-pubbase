package cn.rjtech.u9;


import cn.rjtech.config.U9ApiConfigKey;
import cn.rjtech.util.ValidationUtils;

/**
 * 基础请求对象
 */
public abstract class BaseReq {

    public BaseReq() {
        ValidationUtils.isTrue(U9ApiConfigKey.isU9ApiEnabled(), "当前运行配置环境，无法调用U9接口");
    }

    public abstract String getRequestXml(String orgCode, String proc);

}
