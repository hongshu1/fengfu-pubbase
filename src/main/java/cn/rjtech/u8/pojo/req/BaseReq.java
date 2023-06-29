package cn.rjtech.u8.pojo.req;


import cn.jbolt.core.kit.U8DataSourceKit;
import cn.rjtech.util.ValidationUtils;

import java.io.Serializable;

/**
 * 基础请求对象
 *
 * @author Kephon
 */
public abstract class BaseReq implements Serializable {

    public BaseReq() {
    }

    public BaseReq(String orgCode) {
        ValidationUtils.isTrue(U8DataSourceKit.ME.getIsU8ApiEnabled(orgCode), "当前运行配置环境，无法调用U8接口");
    }

    public abstract String getRequestXml(String orgCode, String proc);

}
