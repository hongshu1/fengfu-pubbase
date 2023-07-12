package cn.rjtech.admin.momaterialsreturnm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MoMaterialsreturnm;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;

/**
 * 制造工单-生产退料主表 Controller
 *
 * @ClassName: MoMaterialsreturnmAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-25 16:32
 */
@CheckPermission(PermissionKey.API_MOMATERIALSRETURNM)
@UnCheckIfSystemAdmin
@Path(value = "/admin/momaterialsreturnm", viewPath = "/_view/admin/momaterialsreturnm")
public class MoMaterialsreturnmAdminController extends BaseAdminController {

    @Inject
    private MoMaterialsreturnmService service;

    /**
     * 首页
     */
    public void index() {
        keepPara();
        render("index.html");
    }

    /**
     * 数据源
     */
    public void datas() {
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.MOMATERIALSRETURNM_ADD)
    public void add(@Para(value = "imodocid") Long imodocid) {
        ValidationUtils.validateId(imodocid, "imodocid");

        keepPara();
        render("add.html");
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.MOMATERIALSRETURNM_EDIT)
    public void edit() {
        MoMaterialsreturnm moMaterialsreturnm = service.findById(getLong(0));
        if (moMaterialsreturnm == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("moMaterialsreturnm", moMaterialsreturnm);
        render("edit.html");
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.MOMATERIALSRETURNM_ADD)
    public void save() {
        renderJson(service.save(getModel(MoMaterialsreturnm.class, "moMaterialsreturnm")));
    }

    /**
     * 更新
     */
    @CheckPermission(PermissionKey.MOMATERIALSRETURNM_EDIT)
    public void update() {
        renderJson(service.update(getModel(MoMaterialsreturnm.class, "moMaterialsreturnm")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.MOMATERIALSRETURNM_DEL)
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.MOMATERIALSRETURNM_DEL)
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 切换toggleIsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleIsDeleted(getLong(0)));
    }

    /**
     * 新增退料
     */
    @CheckPermission(PermissionKey.MOMATERIALSRETURNM_ADD)
    public void addMoMaterialsreturn(@Para(value = "imodocid") Long imodocid) {
        ValidationUtils.validateId(imodocid, "生产工单ID");

        renderJsonData(service.addMoMaterialsreturn(imodocid, getJBoltTable()));
    }

    /**
     * 明细列表
     */
    public void getMoMaterialsreturnList() {
        renderJsonData(service.getMoMaterialsreturnList(getPageNumber(), getPageSize(), getKv()));
    }

    public void details() {
        MoMaterialsreturnm moMaterialsreturnm = service.findById(getLong(0));
        if (moMaterialsreturnm == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("moMaterialsreturnm", moMaterialsreturnm);
        render("detail.html");
    }

    /**
     * 查询单条现品票（生产退料）
     */
    public void addBarcode() {
        renderJsonData(service.getBycBarcodeInfo(get("barcode")));
    }

    /**
     *  查询全部现品票（生产退料）
     */
    public void getmomaterialscanusedlogList() {
        renderJsonData(service.getBycBarcodeList(getPageNumber(),getPageSize()));
    }

    /**
     * 保存新增生产退料
     */
    @CheckPermission(PermissionKey.MOMATERIALSRETURNM_ADD)
    public void saveTableSubmit() {
        renderJson(service.saveTableSubmit(getJBoltTable()));
    }

    /**
     * 生产退料详情
     */
    public void getmomaterialscanuseMList() {
        renderJsonData(service.getModandMomlist(get("iautoid")));
    }

}
