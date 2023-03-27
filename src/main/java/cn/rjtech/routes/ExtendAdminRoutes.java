package cn.rjtech.routes;

import com.jfinal.config.Routes;

import cn.rjtech.admin.fitemss97.Fitemss97AdminController;
import cn.rjtech.admin.fitemss97class.Fitemss97classAdminController;
import cn.rjtech.admin.fitemss97sub.Fitemss97subAdminController;
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
        this.add("/admin/fitemss97class", Fitemss97classAdminController.class, "/fitemss97class");
        this.add("/admin/fitemss97", Fitemss97AdminController.class, "/fitemss97");
        this.add("/admin/fitemss97sub", Fitemss97subAdminController.class, "/fitemss97sub");
    }

}
