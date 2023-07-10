package cn.rjtech.api.user;

import cn.jbolt.core.api.JBoltApiKit;
import cn.jbolt.core.api.JBoltApiRet;
import cn.jbolt.core.api.JBoltApplyJWT;
import cn.jbolt.core.api.httpmethod.JBoltHttpPost;
import cn.jbolt.core.crossorigin.CrossOrigin;
import cn.jbolt.core.model.LoginLog;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.service.JBoltLoginLogUtil;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.base.NullDataResult;
import cn.rjtech.entity.vo.user.UserInfoVo;
import cn.rjtech.entity.vo.user.UserJwtVo;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.tx.Tx;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * 用户API
 *
 * @author Kephon
 */
@ApiDoc
@CrossOrigin
public class UserApiController extends BaseApiController {

    @Inject
    private UserApiService service;

    /**
     * 以后台用户账号登录
     *
     * @param username 用户名
     * @param password 密码，参考管理端的加密算法实现
     * @param orgId    登录的组织ID，APP前台必填
     */
    @ApiDoc(result = UserJwtVo.class)
    @Before(Tx.class)
    @JBoltHttpPost
    @JBoltApplyJWT
    public void login(@Para(value = "username") String username,
                      @Para(value = "password") String password,
                      @Para(value = "orgId") Long orgId
//                      ,@Para(value = "mac") String mac
                      ) {
        ValidationUtils.notBlank(username, "缺少用户名");
        ValidationUtils.notBlank(password, "缺少密码");
        ValidationUtils.validateId(orgId, "登录组织");
//        ValidationUtils.notBlank(mac, "未获取到MAC地址");
        LoginLog log= JBoltLoginLogUtil.createLoginLog(getRequest());
//        renderJBoltApiRet(service.login(username, password, orgId, mac, log.getLoginIp()));
        renderJBoltApiRet(service.login(username, password, orgId, log.getLoginIp()));
    }

    /**
     * 获取用户个人信息
     */
    @ApiDoc(result = UserInfoVo.class)
    @UnCheck
    public void personal() {
        renderJBoltApiRet(JBoltApiRet.successWithData(JBoltApiKit.getApiUser()));
    }

    /**
     * 获取人员角色权限
     *
     * @param permissionKey 权限
     * @param pkey          父级权限
     *                      result:{title:权限名称,permission_key:权限key}
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void getUserPermissionKeyList(@Para(value = "permissionKey") String permissionKey,
                                         @Para(value = "pkey") String pkey) {
        renderJBoltApiRet(service.getUserPermissionList(permissionKey, pkey));
    }

}
