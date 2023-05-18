package cn.rjtech.admin.codingrulem;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.form.FormService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.CodingRuleM;
import cn.rjtech.model.momdata.Form;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.template.stat.ast.For;

/**
 * 系统配置-编码规则
 *
 * @ClassName: CodingRuleMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-04 14:00
 */
@CheckPermission(PermissionKey.CODING_RULEM)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/codingrulem", viewPath = "/_view/admin/codingrulem")
public class CodingRuleMAdminController extends BaseAdminController {

    @Inject
    private CodingRuleMService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), get("cFormTypeSn"), getInt("iCodingType"), getBoolean("IsDeleted")));
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
        renderJson(service.save(getModel(CodingRuleM.class, "codingRuleM"), get("iFormId")));
    }

    /**
     * 编辑
     */
    public void edit() {
        CodingRuleM codingRuleM = service.findById(getLong(0));
        if (codingRuleM == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }

        set("codingRuleM", codingRuleM);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(CodingRuleM.class, "codingRuleM")));
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

    public void saveTableSubmit() {
        renderJson(service.saveTableSubmit(getJBoltTable()));
    }

}
