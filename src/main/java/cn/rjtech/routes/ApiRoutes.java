package cn.rjtech.routes;

import cn.rjtech.api.org.OrgApiController;
import cn.rjtech.api.rcvdocqcformm.RcvDocQcFormMApiController;
import cn.rjtech.api.stockoutqcformm.StockOutQcFormMApiController;
import cn.rjtech.api.user.UserApiController;
import cn.rjtech.common.CommonApiController;
import com.jfinal.config.Routes;

/**
 * API路由
 *
 * @author Kephon
 */
public class ApiRoutes extends Routes {

    @Override
    public void config() {
        this.add("/api/org", OrgApiController.class);
        this.add("/api/user", UserApiController.class);
        this.add("/api/erp/common", CommonApiController.class);
        this.add("/api/stockoutqcformm", StockOutQcFormMApiController.class);
        this.add("/api/rcvdocqcformm", RcvDocQcFormMApiController.class);
    }

}
