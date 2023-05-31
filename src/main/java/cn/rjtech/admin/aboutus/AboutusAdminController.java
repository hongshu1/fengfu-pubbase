package cn.rjtech.admin.aboutus;

import cn.jbolt._admin.globalconfig.GlobalConfigService;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.config.MomConfigKey;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 关于我们
 *
 * @author Kephon
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@CheckPermission(PermissionKey.ABOUT_US)
@Path(value = "/admin/aboutus", viewPath = "_view/admin/aboutus")
public class AboutusAdminController extends BaseAdminController {

    @Inject
    private GlobalConfigService globalConfigService;

    /**
     * 编辑页
     */
    public void index() {
        set("configs", globalConfigService.getCommonList(Okv.by("type_key", MomConfigKey.ABOUT_US)));
        render("index.html");
    }

    /**
     * 保存
     */
    @SuppressWarnings("unchecked")
    public void save() {
        Kv para = getKv();

        List<Record> recordList = new ArrayList<>();

        for (Object r : para.entrySet()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) r;

            recordList.add(new Record().set("id", entry.getKey()).set("config_value", entry.getValue()));
        }

        renderJson(globalConfigService.updateRecords(recordList));
    }



}
