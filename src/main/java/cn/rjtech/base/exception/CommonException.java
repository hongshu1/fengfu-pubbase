package cn.rjtech.base.exception;

/**
 * @Description 自定义业务异常
 * @Author Create by Alvin
 * @Date 2020-10-27 14:02
 */
public class CommonException extends RuntimeException {

    private final String errorCode;

    public CommonException(String msssage) {
        super(msssage);
        this.errorCode = "fail";
    }

    public CommonException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

}
