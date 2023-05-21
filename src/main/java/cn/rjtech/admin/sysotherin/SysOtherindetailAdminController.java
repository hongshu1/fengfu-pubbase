package cn.rjtech.admin.sysotherin;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SysOtherindetail;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 其它入库单明细
 * @ClassName: SysOtherindetailAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-05 17:57
 */
@CheckPermission(PermissionKey.OTHER_IN_LIST)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/sysotherindetail", viewPath = "/_view/admin/sysotherindetail")
public class SysOtherindetailAdminController extends BaseAdminController {

    @Inject
    private SysOtherindetailService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), get("TrackType"), get("SourceBillType"), get("projectTypeCode"), get("projectTypeName")));
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
        renderJson(service.save(getModel(SysOtherindetail.class, "sysOtherindetail")));
    }

    /**
     * 编辑
     */
    public void edit() {
        SysOtherindetail sysOtherindetail = service.findById(getLong(0));
        if (sysOtherindetail == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("sysOtherindetail", sysOtherindetail);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysOtherindetail.class, "sysOtherindetail")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteRmRdByIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    public void findEditTableDatas() {
        renderJsonData(service.findEditTableDatas(getKv()));
    }
}
