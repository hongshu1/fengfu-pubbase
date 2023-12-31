package cn.rjtech.admin.approvalform;

import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ApprovalForm;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;

/**
 * 审批流表单 Controller
 *
 * @ClassName: ApprovalFormAdminController
 * @author: RJ
 * @date: 2023-04-18 17:14
 */
@UnCheck
@Path(value = "/admin/approvalform", viewPath = "/_view/admin/approvalform")
public class ApprovalFormAdminController extends BaseAdminController {

    @Inject
    private ApprovalFormService service;

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
//		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKeywords()));
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 新增
     */
    public void add() {
        render("add.html");
    }

    /**
     * 编辑
     */
    public void edit() {
        ApprovalForm approvalForm = service.findById(getLong(0));
        if (approvalForm == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("approvalForm", approvalForm);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(ApprovalForm.class, "approvalForm")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(ApprovalForm.class, "approvalForm")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 拉取资源Dialog
     */
    public void chooseForm() {
        String itemHidden = get("itemHidden");
        set("itemHidden", itemHidden);
        render("resource_list.html");
    }

    /**
     * 获取资源的数据源
     */
    public void resourceList() {
        String itemHidden = get("itemHidden");
        String keywords = StrUtil.trim(get("keywords"));
        Kv kv = Kv.create()
                .setIfNotNull("itemHidden", itemHidden)
                .setIfNotNull("keywords", keywords);
        renderJsonData(service.resourceList(kv, getPageNumber(), getPageSize()));
    }

}
