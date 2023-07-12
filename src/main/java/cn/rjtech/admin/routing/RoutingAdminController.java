package cn.rjtech.admin.routing;

import cn.hutool.core.date.DateUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.admin.equipmentmodel.EquipmentModelService;
import cn.rjtech.admin.inventorychange.InventoryChangeService;
import cn.rjtech.admin.inventoryroutingconfig.InventoryRoutingConfigService;
import cn.rjtech.admin.invpart.InvPartService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InvPart;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 物料建模-工艺路线
 *
 * @ClassName: BomMasterAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-17 16:28
 */
@CheckPermission(PermissionKey.NOME)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/routing", viewPath = "/_view/admin/routing")
public class RoutingAdminController extends BaseAdminController {

    @Inject
    private RoutingService service;
    @Inject
    private InventoryChangeService inventoryChangeService;
    @Inject
    private EquipmentModelService equipmentModelService;
    @Inject
    private InventoryRoutingConfigService inventoryRoutingConfigService;
    @Inject
    private InvPartService invPartService;

    /**
     * 首页
     */
    public void index() {
        render("index.html");
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
        Record routing = service.findByIdRoutingVersion(getLong(0));
        if (routing == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("routing", routing);
        render("edit.html");
    }

    public void info(){
        InvPart invPart = invPartService.findById(getLong(0));
        if (invPart == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        Record routing = service.findByIdRoutingVersion(invPart.getIInventoryRoutingId());
        set("isView", 1);
        set("routing", routing);
        render("edit.html");
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
     * 切换isEnabled
     */
    public void toggleIsEnabled() {
        renderJson(service.toggleBoolean(getLong(0), "isEnabled"));
    }

    /**
     * 切换isDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleBoolean(getLong(0), "isDeleted"));
    }

    /**
     * 默认给1-100个数据
     */
    public void inventoryAutocomplete() {
        renderJsonData(inventoryChangeService.inventoryAutocomplete(getPageNumber(), 100, getKv()));
    }

    @UnCheck
    public void findEquipmentModelAll() {
        renderJsonData(equipmentModelService.getAdminDataNoPage(getKv()));
    }

    @UnCheck
    public void getRoutingDetails() {
        renderJsonData(service.getRoutingDetails(getKv()));
    }

    /**
     * 工艺路线
     */
    @UnCheck
    public void findRoutingAll() {
        renderJsonData(service.findRoutingAll(getKv()));
    }

    public void table() {
        render("table.html");
    }

    public void versionTable() {
        render("version_table.html");
    }

    @UnCheck
    public void versionAll() {
        renderJsonData(service.findRoutingVersion(getPageNumber(), getPageSize(), getKv()));
    }

    @UnCheck
    public void getRoutingConfigDetail() {
        renderJsonData(inventoryRoutingConfigService.dataList(getLong(0)));
    }

    public void audit(@Para(value = "routingId") Long routingId,
                      @Para(value = "status") Integer status) {
        renderJsonData(service.audit(routingId, status));
    }

    @CheckPermission(PermissionKey.ROUTING_EXPORT)
    public void exportExcel() throws Exception {
        List<Record> rows = service.getRoutingDetails(getKv());
        if (notOk(rows)) {
            renderJsonFail("无有效数据导出");
            return;
        }

        renderJxls("routing.xlsx", Kv.by("rows", rows), "工艺路线_" + DateUtil.today() + ".xlsx");
    }

    @CheckPermission(PermissionKey.ROUTING_VERSION_EXPORT)
    public void exportVersionExcel() throws Exception {
        List<Record> routingVersion = service.findRoutingVersionExport(getKv());
        if (notOk(routingVersion)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderJxls("versionrouting.xlsx", Kv.by("rows", routingVersion), "版本记录_" + DateUtil.today() + ".xlsx");
    }
}
