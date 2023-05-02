package cn.rjtech.admin.cusfieldmappingform;

import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.CusfieldmappingForm;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 系统配置-导入字段映射表单
 *
 * @ClassName: CusfieldmappingFormAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-02 13:52
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/cusfieldmappingform", viewPath = "/_view/admin/cusfieldmappingform")
public class CusfieldmappingFormAdminController extends BaseAdminController {

    @Inject
    private CusfieldmappingFormService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize()));
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
        renderJson(service.save(getModel(CusfieldmappingForm.class, "cusfieldmappingForm")));
    }

    /**
     * 编辑
     */
    public void edit() {
        CusfieldmappingForm cusfieldmappingForm = service.findById(getLong(0));
        if (cusfieldmappingForm == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("cusfieldmappingForm", cusfieldmappingForm);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(CusfieldmappingForm.class, "cusfieldmappingForm")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }


}
