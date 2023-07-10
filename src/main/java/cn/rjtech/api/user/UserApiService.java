package cn.rjtech.api.user;

import cn.hutool.core.date.DateTime;
import cn.jbolt._admin.permission.PermissionService;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.api.*;
import cn.jbolt.core.common.enums.JBoltLoginState;
import cn.jbolt.core.model.Org;
import cn.jbolt.core.model.User;
import cn.rjtech.admin.org.OrgService;
import cn.rjtech.admin.pad.PadService;
import cn.rjtech.model.momdata.Pad;
import cn.rjtech.model.momdata.PadLoginLog;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Okv;
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
    private PermissionService permissionService;

    @Inject
    private PadService padService;

    public JBoltApiRet getUserPermissionList(String permissionKey, String pkey){
        return JBoltApiRet.API_SUCCESS_WITH_DATA(permissionService.getUserPermissionList(permissionKey, pkey));
    }
    public JBoltApiRet login(String username, String password, Long orgId, String ip) {
//    public JBoltApiRet login(String username, String password, Long orgId, String mac, String ip) {
        Org org = orgService.findById(orgId);
        ValidationUtils.notNull(org, "组织不存在");
        ValidationUtils.isTrue(org.getEnable(), "该组织已被禁用");

//        //通过MAC地址获取平板信息
//        Record pad=padService.getPadByMac(mac);
//        if (pad == null) {
//            return JBoltApiRet.API_FAIL("该设备未在平板端设置找到登记记录");
//        }

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

        JBoltApiUserBean userBean = new JBoltApiUserBean(JBoltApiKit.getApplicationId(), user.getId(), user.getName(), true, orgId, org.getOrgCode());

        // 创建JWT
        String[] jwts = JBoltApiJwtManger.me().createJBoltApiTokens(JBoltApiKit.getApplication(), userBean, JBoltApiJwtManger.REFRESH_JWT_SURPLUS_TTL, JBoltApiJwtManger.REFRESH_JWT_SURPLUS_TTL * 2);
        ValidationUtils.notEmpty(jwts, "生成jwt失败");

//        PadLoginLog padLoginLog=new PadLoginLog();
//        padLoginLog.setIOrgId(org.getId());
//        padLoginLog.setCOrgCode(org.getOrgCode());
//        padLoginLog.setCOrgName(org.getOrgName());
//        padLoginLog.setIPadId(pad.getLong("iautoid"));
//        padLoginLog.setIUserId(user.getId());
//        padLoginLog.setCIp(ip);
//        padLoginLog.setICreateBy(user.getId());
//        padLoginLog.setCCreateName(user.getUsername());
//        padLoginLog.setDCreateTime(new DateTime());
//        padLoginLog.save();

        Okv ret = Okv.by(JBoltApiJwtManger.JBOLT_API_TOKEN_KEY, jwts[0])
                .set(JBoltApiJwtManger.JBOLT_API_REFRESH_TOKEN_KEY, jwts[1])
                .set("orgcode", org.getOrgCode());
        return JBoltApiRet.successWithData(ret);
    }

}
