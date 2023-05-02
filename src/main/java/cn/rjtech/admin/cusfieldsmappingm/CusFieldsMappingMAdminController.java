package cn.rjtech.admin.cusfieldsmappingm;

import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.CusFieldsMappingM;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 系统配置-导入字段配置
 *
 * @ClassName: CusFieldsMappingMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-02 13:16
 */
@CheckPermission(PermissionKey.CUS_FIELDS_MAPPINGM)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/cusfieldsmappingm", viewPath = "/_view/admin/cusfieldsmappingm")
public class CusFieldsMappingMAdminController extends BaseAdminController {

    @Inject
    private CusFieldsMappingMService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), get("cFormatName"), getBoolean("isEnabled")));
    }

    /**
     * 新增
     */
    public void add() {
        render("add.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(CusFieldsMappingM.class, "cusFieldsMappingM"), get("iformids")));
    }

    /**
     * 编辑
     */
    public void edit() {
        CusFieldsMappingM cusFieldsMappingM = service.findById(getLong(0));
        if (cusFieldsMappingM == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("cusFieldsMappingM", cusFieldsMappingM);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(CusFieldsMappingM.class, "cusFieldsMappingM")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    /**
     * 切换isEnabled
     */
    public void toggleIsEnabled() {
        renderJson(service.toggleBoolean(getLong(0), "isEnabled"));
    }

}
