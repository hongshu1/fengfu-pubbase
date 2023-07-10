package cn.rjtech.admin.cusfieldsmappingd;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.admin.cusfieldsmappingform.CusfieldsmappingFormService;
import cn.rjtech.admin.cusfieldsmappingm.CusFieldsMappingMService;
import cn.rjtech.admin.form.FormService;
import cn.rjtech.admin.formfield.FormFieldService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.CusFieldsMappingD;
import cn.rjtech.model.momdata.CusFieldsMappingM;
import cn.rjtech.model.momdata.FormField;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;

import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

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
    private FormService formService;
    @Inject
    private CusFieldsMappingDService service;
    @Inject
    private FormFieldService formFieldService;
    @Inject
    private CusFieldsMappingMService cusFieldsMappingMService;
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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getLong("icusfieldsmappingmid"), getKeywords(), getBoolean("isEncoded"), getBoolean("isEnabled")));
    }

    /**
     * 新增
     */
    public void add(@Para(value = "icusfieldsmappingmid") Long icusfieldsmappingmid) {
        ValidationUtils.validateId(icusfieldsmappingmid, JBoltMsg.PARAM_ERROR);

        CusFieldsMappingM m = cusFieldsMappingMService.findById(icusfieldsmappingmid);
        ValidationUtils.notNull(m, "映射字段配置记录不存在");
        ValidationUtils.isTrue(!m.getIsDeleted(), "映射字段配置记录已被删除");

        List<Long> iformids = cusfieldsmappingFormService.getIformIdsByMid(m.getIAutoId());

        CusFieldsMappingD cusFieldsMappingD = new CusFieldsMappingD()
                .setICusFieldsMappingMid(icusfieldsmappingmid)
                .setIsEncoded(true);
        
        set("cusfieldsmappingm", m);
        set("cusFieldsMappingD", cusFieldsMappingD);
        set("iformids", iformids);
        set("cformnames", CollUtil.join(formService.getNamesByIformids(iformids), COMMA));
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

        if (ObjUtil.isNotNull(cusFieldsMappingD.getIFormFieldId())) {
            FormField formField = formFieldService.findById(cusFieldsMappingD.getIFormFieldId());
            ValidationUtils.notNull(formField, "表单字段不存在");
            set("formField", formField);
        }
        
        CusFieldsMappingM m = cusFieldsMappingMService.findById(cusFieldsMappingD.getICusFieldsMappingMid());
        ValidationUtils.notNull(m, "映射字段配置记录不存在");
        ValidationUtils.isTrue(!m.getIsDeleted(), "映射字段配置记录已被删除");

        List<Long> iformids = cusfieldsmappingFormService.getIformIdsByMid(m.getIAutoId());

        set("cusfieldsmappingm", m);
        set("cusFieldsMappingD", cusFieldsMappingD);
        set("iformids", iformids);
        set("cformnames", CollUtil.join(formService.getNamesByIformids(iformids), COMMA));
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

    /**
     * AjaxPortal使用
     */
    public void codingrule() {
        keepPara();
        render("_coding_rule.html");
    }

    public void saveTableSubmit() {
        renderJson(service.saveTableSubmit(getJBoltTable()));
    }

    /**
     * 定制规则
     */
    public void rule(@Para(value = "icusfieldsmappingdid") Long icusfieldsmappingdid,
                     @Para(value = "icusfieldsmappingmid") Long icusfieldsmappingmid) {
        ValidationUtils.validateId(icusfieldsmappingmid, JBoltMsg.PARAM_ERROR);
        
        if (ObjUtil.isNotNull(icusfieldsmappingdid)) {
            CusFieldsMappingD cusFieldsMappingD = service.findById(icusfieldsmappingdid);
            ValidationUtils.notNull(cusFieldsMappingD, JBoltMsg.DATA_NOT_EXIST);

            if (ObjUtil.isNotNull(cusFieldsMappingD.getIFormFieldId())) {
                FormField formField = formFieldService.findById(cusFieldsMappingD.getIFormFieldId());
                ValidationUtils.notNull(formField, "表单字段不存在");
                set("formField", formField);
            }

            set("cusFieldsMappingD", cusFieldsMappingD);
        }

        keepPara();
        
        List<Long> iformids = cusfieldsmappingFormService.getIformIdsByMid(icusfieldsmappingmid);
        set("iformids", iformids);
        set("cformnames", CollUtil.join(formService.getNamesByIformids(iformids), COMMA));
        render("rule.html");
    }
    
}
