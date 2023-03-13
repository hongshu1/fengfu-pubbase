package cn.rjtech.base.event;

import java.io.Serializable;

/**
 * @Description 自定义事件
 * @Author Create by Alvin
 * @Date 2020-09-21 13:11
 */
public class ExceptionEvent implements Serializable {

    private final String msg;

    public ExceptionEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "ExceptionEvent{" +
                "msg='" + msg + '\'' +
                '}';
    }

}
