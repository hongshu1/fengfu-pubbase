package cn.rjtech.admin.cusfieldsmappingform;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.CusfieldsmappingForm;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 系统配置-导入字段映射表单
 *
 * @ClassName: CusfieldsmappingFormAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-02 13:52
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/cusfieldsmappingform", viewPath = "/_view/admin/cusfieldsmappingform")
public class CusfieldsmappingFormAdminController extends BaseAdminController {

    @Inject
    private CusfieldsmappingFormService service;

    /**
     * 首页
     */
    public void index() {
        render("index().html");
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
        render("add().html");
    }

    /**
     * 编辑
     */
    public void edit() {
        CusfieldsmappingForm cusfieldsmappingForm = service.findById(getLong(0));
        if (cusfieldsmappingForm == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("cusfieldsmappingForm", cusfieldsmappingForm);
        render("edit().html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(CusfieldsmappingForm.class, "cusfieldsmappingForm")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }


}
