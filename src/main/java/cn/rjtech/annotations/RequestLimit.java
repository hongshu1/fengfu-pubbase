package cn.rjtech.annotations;

import java.lang.annotation.*;

/**
 * 请求限制拦截注解
 *
 * @author Kephon
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequestLimit {

    /**
     * 允许访问的次数，默认值MAX_VALUE
     */
    long count() default Integer.MAX_VALUE;

    /**
     * 时间段，单位为秒，默认值一分钟
     */
    int time() default 60;

}
