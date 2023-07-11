package cn.rjtech.admin.codingrulem;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.CodingRuleM;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

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
    @UnCheck
    public void datas() {
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
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
        renderJson(service.removeByIds(get("ids")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.CODINGRULEM_DEL)
    public void delete() {
        renderJson(service.removeByIds(get("ids")));
    }
    
    @CheckPermission(PermissionKey.CODINGRULEM_ADD)
    public void saveTableSubmit() {
        renderJson(service.saveTableSubmit(getJBoltTable()));
    }
    
    /**
     * 详情
     */
    public void info(){
        CodingRuleM codingRuleM = service.findById(getLong(0));
        if (codingRuleM == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("codingRuleM", codingRuleM);
        set("isView", 1);
        render("edit.html");
    }
}
