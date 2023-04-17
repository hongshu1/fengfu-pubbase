package cn.rjtech.entity.vo.user;

import java.io.Serializable;

/**
 * 登录用户对象
 *
 * @author Kephon
 */
public class User implements Serializable {

    /**
     * 用户ID
     */
    private Long userId;

    public User() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
