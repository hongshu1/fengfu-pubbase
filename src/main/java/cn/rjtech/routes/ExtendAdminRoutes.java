package cn.rjtech.routes;

import com.jfinal.config.Routes;
/**
 * MOM平台路由配置
 *
 * @author Kephon
 */
public class ExtendAdminRoutes extends Routes {

    @Override
    public void config() {
        // JBoltAdminAuthInterceptor 请在类上配置@Before(JBoltAdminAuthInterceptor.class)及完整的viewPath

        // MOM平台路由扫描配置
        this.scan("cn.rjtech.common.")
                .scan("cn.rjtech.erp.");
    }

}
