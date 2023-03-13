package cn.rjtech.base.controller;

import cn.jbolt.core.api.JBoltApiBaseController;
import cn.jbolt.core.kit.OrgAccessKit;

/**
 * MOM 平台API扩展实现
 *
 * @author Kephon
 */
public class BaseApiController extends JBoltApiBaseController {

    /**
     * 获取登录的组织ID
     *
     * @return 可能为NULL，即非组织方式登录
     */
    protected Long getOrgId() {
        return OrgAccessKit.getOrgId();
    }

    /**
     * 获取登录的组织编码
     *
     * @return 可能为NULL，即非组织方式登录
     */
    protected String getOrgCode() {
        return OrgAccessKit.getOrgCode();
    }

    /**
     * 获取登录的组织编码
     *
     * @return 可能为NULL，即非组织方式登录
     */
    protected String getOrgName() {
        return OrgAccessKit.getOrgName();
    }

}
