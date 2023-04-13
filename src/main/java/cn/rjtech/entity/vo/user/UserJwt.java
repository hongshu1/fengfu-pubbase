package cn.rjtech.entity.vo.user;

import java.io.Serializable;

/**
 * 登录返回的JWT信息
 *
 * @author Kephon
 */
public class UserJwt implements Serializable {

    /**
     * jwt
     */
    private String jboltjwt;
    /**
     * refreshJwt
     */
    private String jboltrefreshjwt;

    public UserJwt() {
    }

    public String getJboltjwt() {
        return jboltjwt;
    }

    public void setJboltjwt(String jboltjwt) {
        this.jboltjwt = jboltjwt;
    }

    public String getJboltrefreshjwt() {
        return jboltrefreshjwt;
    }

    public void setJboltrefreshjwt(String jboltrefreshjwt) {
        this.jboltrefreshjwt = jboltrefreshjwt;
    }

    @Override
    public String toString() {
        return "UserJwt{" +
                "jboltjwt='" + jboltjwt + '\'' +
                ", jboltrefreshjwt='" + jboltrefreshjwt + '\'' +
                '}';
    }

}
