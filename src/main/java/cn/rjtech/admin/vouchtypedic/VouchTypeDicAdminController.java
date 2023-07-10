package cn.rjtech.admin.vouchtypedic;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.VouchTypeDic;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Okv;

/**
 * 基础档案-单据业务类型字典
 *
 * @ClassName: VouchTypeDicAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-25 16:32
 */
@CheckPermission(PermissionKey.VOUCHTYPEDIC)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/vouchtypedic", viewPath = "/_view/admin/vouchtypedic")
public class VouchTypeDicAdminController extends BaseAdminController {

    @Inject
    private VouchTypeDicService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("cVTChName"), get("cBTChName")));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.VOUCHTYPEDIC_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.VOUCHTYPEDIC_ADD)
    public void save() {
        renderJson(service.save(getModel(VouchTypeDic.class, "vouchTypeDic")));
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.VOUCHTYPEDIC_EDIT)
    public void edit() {
        VouchTypeDic vouchTypeDic = service.findById(getLong(0));
        if (vouchTypeDic == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("vouchTypeDic", vouchTypeDic);
        render("edit.html");
    }

    /**
     * 更新
     */
    @CheckPermission(PermissionKey.VOUCHTYPEDIC_EDIT)
    public void update() {
        renderJson(service.update(getModel(VouchTypeDic.class, "vouchTypeDic")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.VOUCHTYPEDIC_DELETE)
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.VOUCHTYPEDIC_DELETE)
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    @UnCheck
    public void options() {
        renderJsonData(service.getCommonList("cvbtid,cvtchname,cbtchname", Okv.by("iorgid", getOrgId())));
    }

}
