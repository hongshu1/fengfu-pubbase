package cn.rjtech.wms.print.organize;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.jbolt.extend.controller.BaseMesAdminController;
import cn.rjtech.common.model.Organize;
import cn.rjtech.config.AppConfig;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Okv;

import java.util.Date;

/**
 * WMS组织 Controller
 *
 * @ClassName: OrganizeAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-24 16:54
 */
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/organize", viewPath = "/_view/admin/organize")
public class OrganizeAdminController extends BaseMesAdminController {

    @Inject
    private OrganizeService service;

    /**
     * 首页
     */
    public void index() {
        render("index.html");
    }

    /**
     * 数据源
     */
    public void datas() {
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKeywords()));
    }

    /**
     * 新增
     */
    public void add() {
        render("add.html");
    }

    /**
     * 编辑
     */
    public void edit() {
        Organize organize = service.findById(useIfPresent(getLong(0)));
        ValidationUtils.notNull(organize, JBoltMsg.DATA_NOT_EXIST);

        set("organize", organize);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(useIfValid(Organize.class, "organize")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(useIfValid(Organize.class, "organize")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    @Clear(JBoltAdminAuthInterceptor.class)
    public void getDbSource() {
        // 获取组织信息
        Organize org = service.getOrgByCode(getOrgCode());

        String strdate= JBoltDateUtil.format(new Date(),"yyyy-MM-dd");

        Okv ret = Okv.by("jdbc_url", AppConfig.getJdbcUrl() + ";DatabaseName=" + org.getErpdbname())
                .set("OrganizeCode", org.getOrganizecode()).set("strdate",strdate)
                // 从配置文件读取打印地址
                .set("print_url", AppConfig.getPrintAddressUrl());

        renderJsonData(ret);
    }

}
