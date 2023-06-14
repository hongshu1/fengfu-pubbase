package cn.rjtech.cache;

import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.rjtech.admin.userorg.UserOrgService;
import cn.rjtech.model.main.UserOrg;
import com.jfinal.aop.Aop;

/**
 * 用户缓存类
 *
 * @author Kephon
 */
public class UserCache extends JBoltUserCache {

    public static final UserCache ME = new UserCache();

    private final UserOrgService userOrgService = Aop.get(UserOrgService.class);

    /**
     * 获取用户的人员ID
     */
    public Long getPersonId(long userId, long orgId) {
        UserOrg userOrg = userOrgService.getUserOrg(userId, orgId);
        return ObjUtil.isNull(userOrg) || ObjUtil.isNull(userOrg.getIPersonId()) ? null : userOrg.getIPersonId();
    }

}
