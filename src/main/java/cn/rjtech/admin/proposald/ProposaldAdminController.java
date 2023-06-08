package cn.rjtech.admin.proposald;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Proposald;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;

import java.util.ArrayList;

/**
 * 禀议项目 Controller
 *
 * @ClassName: ProposaldAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-11 16:43
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/proposald", viewPath = "/_view/admin/proposald")
public class ProposaldAdminController extends BaseAdminController {

    @Inject
    private ProposaldService service;

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
    public void list() {
        if (StrUtil.isBlank(get("iproposalmid"))) {
            renderJsonData(CollUtil.empty(ArrayList.class));
            return;
        }
        renderJsonData(service.getList(getKv()));
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
        Proposald proposald = service.findById(useIfPresent(getLong(0)));
        ValidationUtils.notNull(proposald, JBoltMsg.DATA_NOT_EXIST);

        set("proposald", proposald);
        render("edit.html");
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    @UnCheck
    public void findByiProposalMid(@Para(value = "iproposalmid") Long iproposalmid, @Para(value = "noiniproposalds") String noiniproposalds) {
        ValidationUtils.notNull(iproposalmid, "禀议书主表ID为空");

        if (notNull(noiniproposalds))
        {
            renderJsonData(service.findByiProposalMid(iproposalmid, noiniproposalds));
        }else{
            renderJsonData(service.findByiProposalMid(iproposalmid));
        }
    }

}
