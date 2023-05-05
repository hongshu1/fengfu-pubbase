package cn.rjtech.admin.cusfieldsmappingd;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.admin.cusfieldsmappingform.CusfieldsmappingFormService;
import cn.rjtech.admin.cusfieldsmappingm.CusFieldsMappingMService;
import cn.rjtech.admin.form.FormService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.CusFieldsMappingD;
import cn.rjtech.model.momdata.CusFieldsMappingM;
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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isEncoded"), getBoolean("isEnabled")));
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
                .setICusFieldsMappingMid(icusfieldsmappingmid);
        
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

    /**
     * AjaxPortal使用
     */
    public void codingrule() {
        keepPara();
        render("_coding_role.html");
    }

}
