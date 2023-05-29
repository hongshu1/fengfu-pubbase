package cn.rjtech.u8.pojo.req;

import cn.rjtech.u8.pojo.resp.U8ApiResp;

import java.io.Serializable;
import java.util.List;

/**
 * @author Kephon
 */
public abstract class BaseReqData<K extends BaseHead, T extends BaseBody, R extends U8ApiResp> implements Serializable {

    protected String org;

    protected K head;

    protected List<T> body;

    public BaseReqData() {
    }

    public BaseReqData(String org, K head, List<T> body) {
        this.org = org;
        this.head = head;
        this.body = body;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public K getHead() {
        return head;
    }

    public void setHead(K head) {
        this.head = head;
    }

    public List<T> getBody() {
        return body;
    }

    public void setBody(List<T> body) {
        this.body = body;
    }

    public abstract Class<R> getResponseClass();
}
