package cn.rjtech.u8.pojo.req;


import java.io.Serializable;

import cn.rjtech.config.AppConfig;
import cn.rjtech.util.ValidationUtils;

/**
 * 基础请求对象
 *
 * @author Kephon
 */
public abstract class BaseReq implements Serializable {

    public BaseReq() {
        ValidationUtils.isTrue(AppConfig.isU8ApiEnabled(), "当前运行配置环境，无法调用U8接口");
    }

    public abstract String getRequestXml(String orgCode, String proc);

}
