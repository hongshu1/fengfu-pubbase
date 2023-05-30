package cn.rjtech.u8.pojo.resp;

import java.io.Serializable;

/**
 * U8请求响应
 *
 * @author Kephon
 */
public class U8ApiResp implements Serializable {

    protected String code;
    protected String message;

    public U8ApiResp() {
    }

    public U8ApiResp(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "U8ApiResult{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

}

