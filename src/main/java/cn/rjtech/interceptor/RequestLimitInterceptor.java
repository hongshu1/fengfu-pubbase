package cn.rjtech.interceptor;

import cn.hutool.extra.servlet.ServletUtil;
import cn.rjtech.annotations.RequestLimit;
import cn.rjtech.base.redis.RedisKeys;
import cn.rjtech.base.redis.RedisUtil;
import cn.rjtech.enums.ErrorEnums;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.HashKit;
import com.jfinal.weixin.sdk.kit.IpKit;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 注解式拦截器, 添加@RequestLimit注解到具体的方法或类中
 *
 * @author Kephon
 */
public class RequestLimitInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        RequestLimit limit = inv.getMethod().getAnnotation(RequestLimit.class);
        HttpServletRequest request = inv.getController().getRequest();

        // IP、请求URI
        String ip = IpKit.getRealIp(inv.getController().getRequest());
        String uri = request.getRequestURI();
        Map<String, String> params = ServletUtil.getParamMap(request);

        // 请求数限制缓存key
        String requestLimitKey = RedisKeys.getRequestLimitKey(HashKit.md5(uri.concat(params.toString())), ip);
        // 访问计数
        Long visitNum = RedisUtil.incr(requestLimitKey);
        if (visitNum == 1) {
            // 首次访问需要在redis中增加过期时间
            RedisUtil.expire(requestLimitKey, limit.time());
        }
        ValidationUtils.isTrue(visitNum <= limit.count(), ErrorEnums.FREEQUENT_REQUEST_ERROR);

        inv.invoke();
    }

}
