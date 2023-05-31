package cn.rjtech.admin.precesionConfig;

import cn.jbolt.core.model.GlobalConfig;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import com.jfinal.aop.Before;
import com.jfinal.core.Path;

import java.util.List;

/**
 * 精度设置-在库检单行配置 Controller
 *
 * @ClassName: PrecesionConfigAdminController
 * @author: RJ
 * @date: 2023-05-31 13:08
 */

@UnCheck
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/precesionconfig", viewPath = "/_view/admin/precesionconfig")
public class PrecesionConfigAdminController  extends BaseAdminController {




}
