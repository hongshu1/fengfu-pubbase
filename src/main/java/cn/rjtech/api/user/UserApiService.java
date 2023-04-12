package cn.rjtech.api.user;

import cn.jbolt._admin.permission.PermissionService;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.admin.wechat.user.WechatUserService;
import cn.jbolt.common.model.WechatUser;
import cn.jbolt.common.util.CACHE;
import cn.jbolt.core.api.*;
import cn.jbolt.core.common.enums.JBoltLoginState;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.OrgService;
import cn.rjtech.enums.BoolCharEnum;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

/**
 * 用户API
 *
 * @author Kephon
 */
public class UserApiService extends JBoltApiBaseService {

    @Inject
    private OrgService orgService;
    @Inject
    private UserService userService;
    @Inject
    private WechatUserService wechatUserService;
    @Inject
    private PermissionService permissionService;

    public JBoltApiRet getUserPermissionList(String permissionKey, String pkey){
        return JBoltApiRet.API_SUCCESS_WITH_DATA(permissionService.getUserPermissionList(permissionKey, pkey));
    }

    public JBoltApiRet login(String username, String password, Long orgId) {
        Record org = orgService.findById(orgId);
        ValidationUtils.notNull(org, "组织不存在");
        ValidationUtils.equals(BoolCharEnum.YES.getValue(), org.getStr("enable"), "该组织已被禁用");

        // 通过用户名去找用户
        User user = userService.findFirst(Okv.by("username", username));
        if (user == null || notOk(user.getPwdSalt())) {
            return JBoltApiRet.API_FAIL("用户信息异常，账号密码设置有误");
        }

        String userPassword = user.getPassword();
        String hashPassword = userService.calFrontendPasswordWithSalt(password, user.getPwdSalt());
        if (!hashPassword.equals(userPassword)) {
            return JBoltApiRet.API_FAIL(JBoltLoginState.USERNAME_PWD_ERROR.getText());
        }

        // 公众平台ID
        Long mpId = JBoltApiKit.getApplication().getLinkTargetId();
        // 微信用户
        WechatUser wechatUser = CACHE.me.getApiWechatUserByApiUserId(mpId, user.getId());

        // 通过系统用户查询当前用户是否存在于WechatUser中
        if (null == wechatUser) {
            Ret ret = wechatUserService.addSysWechatUser(mpId, user.getId(), user.getName());
            wechatUser = ret.getAs("data");
        }

        JBoltApiUserBean userBean = new JBoltApiUserBean(JBoltApiKit.getApplicationId(), user.getId(), user.getName(), user.getIsSystemAdmin(), orgId, org.getStr("code"));

        JBoltApiUser apiUser = JBoltApiKit.processBindUser(userBean, wechatUser.getBindUser());

        // 创建JWT
        String[] jwts = JBoltApiJwtManger.me().createJBoltApiTokens(JBoltApiKit.getApplication(), apiUser, JBoltApiJwtManger.REFRESH_JWT_SURPLUS_TTL, JBoltApiJwtManger.REFRESH_JWT_SURPLUS_TTL * 2);
        ValidationUtils.notEmpty(jwts, "生成jwt失败");

        return JBoltApiRet.successWithData(Okv.by(JBoltApiJwtManger.JBOLT_API_TOKEN_KEY, jwts[0]).set(JBoltApiJwtManger.JBOLT_API_REFRESH_TOKEN_KEY, jwts[1]));
    }

}
