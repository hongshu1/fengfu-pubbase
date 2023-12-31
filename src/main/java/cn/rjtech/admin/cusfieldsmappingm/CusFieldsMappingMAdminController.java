package cn.rjtech.admin.cusfieldsmappingm;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.cusfieldsmappingform.CusfieldsmappingFormService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.CusFieldsMappingM;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;

import static cn.hutool.core.text.StrPool.COMMA;

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
    @Inject
    private CusfieldsmappingFormService cusfieldsmappingFormService;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), get("cformatname"), getBoolean("isEnabled"), get("iformids")));
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
        set("iformids", CollUtil.join(cusfieldsmappingFormService.getIformIdsByMid(cusFieldsMappingM.getIAutoId()), COMMA));
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(CusFieldsMappingM.class, "cusFieldsMappingM"), get("iformids")));
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

    /**
     * 映射字段列表
     */
    public void cusfieldsmappingd(@Para(value = "icusfieldsmappingmid") Long icusfieldsmappingmid) {
        ValidationUtils.validateId(icusfieldsmappingmid, JBoltMsg.PARAM_ERROR);
        
        keepPara();
        set("iformids", CollUtil.join(cusfieldsmappingFormService.getIformIdsByMid(icusfieldsmappingmid), COMMA));
        render("_cusfieldsmappingd.html");
    }

}
