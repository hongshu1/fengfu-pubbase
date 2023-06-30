package cn.rjtech.admin.bommasterinv;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.BomMasterInv;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 基础档案-母件物料存货集（新增/修改版本变更时处理，定时处理更新）
 *
 * @ClassName: BomMasterInvAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-12 10:19
 */
@CheckPermission(PermissionKey.NOME)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/bommasterinv", viewPath = "/_view/admin/bommasterinv")
public class BomMasterInvAdminController extends BaseAdminController {

    @Inject
    private BomMasterInvService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize()));
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
        renderJson(service.save(getModel(BomMasterInv.class, "bomMasterInv")));
    }

    /**
     * 编辑
     */
    public void edit() {
        BomMasterInv bomMasterInv = service.findById(getLong(0));
        if (bomMasterInv == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("bomMasterInv", bomMasterInv);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(BomMasterInv.class, "bomMasterInv")));
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


}
