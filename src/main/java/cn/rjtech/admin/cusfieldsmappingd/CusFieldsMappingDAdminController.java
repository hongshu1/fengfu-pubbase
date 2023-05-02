package cn.rjtech.admin.cusfieldsmappingd;

import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.CusFieldsMappingD;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 系统配置-导入字段配置明细
 *
 * @ClassName: CusFieldsMappingDAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-02 13:41
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/cusfieldsmappingd", viewPath = "/_view/admin/cusfieldsmappingd")
public class CusFieldsMappingDAdminController extends BaseAdminController {

    @Inject
    private CusFieldsMappingDService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isEncoded"), getBoolean("isEnabled")));
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
        renderJson(service.save(getModel(CusFieldsMappingD.class, "cusFieldsMappingD")));
    }

    /**
     * 编辑
     */
    public void edit() {
        CusFieldsMappingD cusFieldsMappingD = service.findById(getLong(0));
        if (cusFieldsMappingD == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("cusFieldsMappingD", cusFieldsMappingD);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(CusFieldsMappingD.class, "cusFieldsMappingD")));
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
     * 切换isEncoded
     */
    public void toggleIsEncoded() {
        renderJson(service.toggleBoolean(getLong(0), "isEncoded"));
    }

    /**
     * 切换isEnabled
     */
    public void toggleIsEnabled() {
        renderJson(service.toggleBoolean(getLong(0), "isEnabled"));
    }


}
