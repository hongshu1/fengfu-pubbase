package cn.rjtech.admin.moroutingconfigperson;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MoMoroutingconfigPerson;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 制造工单-工单工艺人员配置 Controller
 * @ClassName: MoMoroutingconfigPersonAdminController
 * @author: RJ
 * @date: 2023-05-09 16:48
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/moroutingconfigperson", viewPath = "/_view/admin/moroutingconfigperson")
public class MoMoroutingconfigPersonAdminController extends BaseAdminController {

    @Inject
    private MoMoroutingconfigPersonService service;

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
        renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv()));
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
        MoMoroutingconfigPerson moMoroutingconfigPerson=service.findById(getLong(0));
        if(moMoroutingconfigPerson == null){
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("moMoroutingconfigPerson",moMoroutingconfigPerson);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(MoMoroutingconfigPerson.class, "moMoroutingconfigPerson")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(MoMoroutingconfigPerson.class, "moMoroutingconfigPerson")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids"),getLong("imodocid")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }


    public void saveEquipmentPerson(){
        Long configid = getLong("configid");



        renderJson(service.saveEquipmentPerson(getJBoltTable(),configid));
    }


}
