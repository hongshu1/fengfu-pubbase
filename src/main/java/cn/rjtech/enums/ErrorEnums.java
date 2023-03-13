package cn.rjtech.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kephon
 */
public enum ErrorEnums {

    /**
     * 错误信息枚举
     */
    ILLEGAL_ARGUMENT_ERROR("400", "非法参数"),
    INTERNAL_SERVER_ERROR("500", "INTERNAL SERVER ERROR"),
    ACCESS_DENIED("403", "禁止访问"),
    FREEQUENT_REQUEST_ERROR("1000", "频繁请求错误"),
    TOKEN_NOT_PRESENT("1001", "缺少访问凭证"),
    SESSION_INVALID("1002", "访问失败，请重新登录"),
    WECHAT_AUTH_REQUIRED("1003", "需要微信授权访问"),
    INVALID_CREDENTIAL("1009", "登录凭证无效"),
    PERMISSION_ERROR("1010", "您没有操作权限"),
    COMMON_ERROR("9999", "未知错误"),

    ADDRESS_INVALID("2002", "地址需要完善"),
    LON_LAT_NOT_PRESENT("2003", "定位不完整信息"),

    // 小程序相关错误
    CODE_REQUIRED("3000", "缺少code参数"),
    INVALID_CODE("3001", "code参数无效"),
    WXA_SESSION_EXPIRED("3002", "sessionJson is blank"),
    SESSION_KEY_BLANK("3003", "sessionKey is blank"),
    USERINFO_CHECK_FAIL("3004", "UserInfo check fail"),

    // 设备访问相关
    CLIENT_MACHINE_NOT_AUTH("3005", "设备未授权访问"),
    CLIENT_MACHINE_DISABLED("3006", "设备已被禁止访问"),

    ACCESS_DENIED_IP("20005", "禁止访问的主机IP"),
    SIGN_NOT_PRESENT("20000", "缺少签名参数"),
    SIGN_INVALID("20004", "签名无效"),
    TIMESTAMP_NOT_PRESENT("20001", "缺少timestamp参数"),
    TIMESTAMP_INVALID("20002", "timestamp参数不合法"),
    TIMESTAMP_EXPIRED("20003", "timestamp已过期");

    private static final Map<String, String> ERROR_CODE_MAP = new HashMap<>();

    static {
        ErrorEnums[] values = ErrorEnums.values();
        for (ErrorEnums ec : values) {
            ERROR_CODE_MAP.put(ec.getCode(), ec.getMsg());
        }
    }

    private final String code;
    private final String msg;

    ErrorEnums(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 获取错误提示的信息
     *
     * @param code 错误码
     * @return 错误提示信息
     */
    public static String getErrorMsg(String code) {
        return ERROR_CODE_MAP.get(code);
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public int getErrorCode() {
        return Integer.parseInt(code);
    }

}
