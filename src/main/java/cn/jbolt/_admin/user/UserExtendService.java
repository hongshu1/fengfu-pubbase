package cn.jbolt._admin.user;

import cn.jbolt.common.model.UserExtend;
import cn.jbolt.core.service.base.JBoltBaseService;
import com.jfinal.aop.Before;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 用户扩展信息service
 */
public class UserExtendService extends JBoltBaseService<UserExtend> {
    private UserExtend dao = new UserExtend().dao();
    @Override
    protected UserExtend dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return 0;
    }

    @Before(Tx.class)
    public Ret initSaveOneExtend(Long userId) {
        UserExtend extend = new UserExtend();
        extend.setId(userId);
        boolean success = extend.save();
        return success?successWithData(extend):FAIL;
    }
}
