package cn.rjtech.annotations;

import cn.rjtech.enums.BusObjectTypeEnum;
import cn.rjtech.enums.DataOperationEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限注解
 *
 * @author Kephon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckDataPermission {

    /**
     * 操作类型
     */
    BusObjectTypeEnum type() default BusObjectTypeEnum.DEPTARTMENT;

    /**
     * 业务对象类型
     */
    DataOperationEnum operation();

}
