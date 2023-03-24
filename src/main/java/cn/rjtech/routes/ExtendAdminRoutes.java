package cn.rjtech.routes;

import com.jfinal.config.Routes;

import cn.rjtech.admin.forgeigncurrency.ForgeignCurrencyAdminController;
import cn.rjtech.admin.person.PersonAdminController;
import cn.rjtech.admin.settlestyle.SettleStyleAdminController;

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
        this.setBaseViewPath("/_view/admin");
        this.add("/admin/forgeigncurrency", ForgeignCurrencyAdminController.class, "/forgeigncurrency");
        this.add("/admin/person", PersonAdminController.class, "/person");
        this.add("/admin/settlestyle", SettleStyleAdminController.class, "/settlestyle");
    }

}
