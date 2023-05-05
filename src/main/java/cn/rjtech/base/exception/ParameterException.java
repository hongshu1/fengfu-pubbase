package cn.rjtech.base.exception;

import cn.rjtech.enums.ErrorEnums;

/**
 * @Description 参数错误异常，采用异常抛出的方式处理错误信息
 * @Author Create by Alvin
 * @Date 2020-10-27 14:02
 */
public class ParameterException extends cn.jbolt.core.exception.ParameterException {

    public ParameterException(String message) {
        super(message);
    }

    public ParameterException(ErrorEnums errorEnums) {
        super(errorEnums.getCode(), errorEnums.getMsg());
    }

}
