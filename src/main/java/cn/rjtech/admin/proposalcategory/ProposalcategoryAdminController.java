package cn.rjtech.admin.proposalcategory;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.common.enums.DictionaryTypeMode;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Proposalcategory;
import cn.rjtech.util.ValidationUtils;

/**
 * 禀议类型区分 Controller
 *
 * @ClassName: ProposalcategoryAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-09-14 11:46
 */
@CheckPermission(PermissionKey.PROPOSALCATEGORY)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/proposalcategory", viewPath = "/_view/admin/proposalcategory")
public class ProposalcategoryAdminController extends BaseAdminController {

    @Inject
    private ProposalcategoryService service;

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
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 下级级数据源
     */
    public void downDatas() {
        renderJsonData(service.downPaginateAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 主新增页面
     */
    public void hostAdd() {
        render("add.html");
    }

    /**
     * 下级新增
     */
    public void downAdd() {
        Kv kv = getKv();
        Long pid = kv.getLong("typeId");
        if (notOk(pid)) {
            renderFormFail(JBoltMsg.PARAM_ERROR);
            return;
        }
        Proposalcategory proposalcategory = service.findById(pid);
        if (proposalcategory == null) {
            renderFormFail(JBoltMsg.PARAM_ERROR);
            return;
        }
        if (proposalcategory.getIlevel() == DictionaryTypeMode.LEVEL_MULTI.getValue()) {
            set("needPidSelect", true);
        }
        set("pid", pid);
        render("down_add.html");
    }

    /**
     * 编辑
     */
    public void edit() {
        Proposalcategory proposalcategory = service.findById(useIfPresent(getLong(0)));
        ValidationUtils.notNull(proposalcategory, JBoltMsg.DATA_NOT_EXIST);

        set("proposalcategory", proposalcategory);
        render("edit.html");
    }

    /**
     * 下级编辑
     */
    public void downEdit() {
        Kv kv = getKv();
        Long iautoid = kv.getLong("iautoid");
        if (notOk(iautoid)) {
            renderFormFail(JBoltMsg.PARAM_ERROR);
            return;
        }
        Proposalcategory proposalcategory = service.findById(iautoid);
        if (proposalcategory == null) {
            renderFormFail(JBoltMsg.PARAM_ERROR);
            return;
        }
        set("proposalcategory", proposalcategory);
        render("down_edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(useIfValid(Proposalcategory.class, "proposalcategory")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(useIfValid(Proposalcategory.class, "proposalcategory")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get(0)));
    }

    /**
     * 清空分类下的字典数据
     */
    public void clearByType() {
        renderJson(service.clearByTypeId(getLong("typeId")));
    }

    /**
     * 禀议类别区分根据主编号查询
     */
    @UnCheck
    public void numberSelect(String cCategoryCode) {
        renderJsonData(service.numberSelect(cCategoryCode));
    }

    @UnCheck
    public void treeSelectOptions() {
        renderJsonData(service.getAllRoleTreeDatas());
    }

}
