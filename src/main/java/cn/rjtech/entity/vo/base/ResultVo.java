package cn.rjtech.entity.vo.base;

import java.io.Serializable;

/**
 * 抽象响应实体对象，用于实现返回接口文档的对象
 *
 * @author Kephon
 */
public abstract class ResultVo<T> implements Serializable {

    /**
     * 请求状态码：0为成功，其他为异常
     */
    protected int code;
    /**
     * 操作结果提示信息
     */
    protected String msg;
    /**
     * 响应结果对象
     */
    protected T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseVo{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

}
