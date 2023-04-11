package cn.rjtech.entity.vo.base;

import java.io.Serializable;

/**
 * 空data响应对象
 *
 * @author Kephon
 */
public class NullDataResult implements Serializable {

    /**
     * 请求状态码：0为成功，其他为异常
     */
    private int code;
    /**
     * 操作结果提示信息
     */
    private String msg;

    public NullDataResult() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "NullDataResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

}
