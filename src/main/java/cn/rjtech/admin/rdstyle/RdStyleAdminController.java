package cn.rjtech.admin.rdstyle;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.RdStyle;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 收发类别 Controller
 *
 * @ClassName: RdStyleAdminController
 * @author: WYX
 * @date: 2023-03-24 09:48
 */
@CheckPermission(PermissionKey.RDSTYLE)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/rdstyle", viewPath = "/_view/admin/rdstyle")
public class RdStyleAdminController extends BaseAdminController {

    @Inject
    private RdStyleService service;

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
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKeywords()));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.RDSTYLE_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 编辑
     */
    public void edit() {
        RdStyle rdStyle = service.findById(getLong(0));
        if (rdStyle == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("rdStyle", rdStyle);
        render("edit.html");
    }

    @UnCheck
    public void selectRdStyle() {
        RdStyle rdStyle = service.findById(getKv().getLong("autoid"));
        renderJsonData(rdStyle);
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.RDSTYLE_ADD)
    public void save() {
        renderJson(service.save(getModel(RdStyle.class, "rdStyle")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(RdStyle.class, "rdStyle")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.RDSTYLE_DELETE)
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
     * 切换toggleBRetail
     */
    public void toggleBRetail() {
        renderJson(service.toggleBRetail(getLong(0)));
    }

    /**
     * 切换toggleBRdFlag
     */
    public void toggleBRdFlag() {
        renderJson(service.toggleBRdFlag(getLong(0)));
    }

    /**
     * 切换toggleIsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleIsDeleted(getLong(0)));
    }

    /**
     * 树结构数据源
     */
    public void mgrTree() {
        renderJsonData(service.getMgrTree(getLong("selectId", getLong(0)), getInt("openLevel", 0)));
    }

    /**
     * select数据源 只需要IsDeletede=true的
     */
    public void enableOptions() {
        renderJsonData(service.getTreeDatas(true, true));
    }

    /**
     * 首页右边详情保存
     */
    @CheckPermission(PermissionKey.RDSTYLE_SUBMIT)
    public void save1() {
        Long autoid = getLong("autoid");
        String crdcode = get("crdcode");
        String crdname = get("crdname");
        RdStyle rdStyle = new RdStyle();
        rdStyle.setIAutoId(autoid);
        rdStyle.setCRdCode(crdcode);
        rdStyle.setCRdName(crdname);
        renderJson(service.update(rdStyle));
    }

    /**
     * 销售类型_新增_出库
     */
    @UnCheck
    public void getSaleType() {
        renderJsonData(service.getSaleType(true, true));
    }

    /**
     * 采购类型_新增_入库
     */
    @UnCheck
    public void getPurchaseType() {
        renderJsonData(service.getPurchaseType(true, true));
    }

    /**
     * 收类别编码
     */
    @UnCheck
    public void options(){
     renderJsonData(service.getCvrrcodeType("收","0"));
    }
    
    /**
     * 发类别编码
     */
    @UnCheck
    public void options1(){
        renderJsonData(service.getCvrscodeType("发","0"));
    }

    @UnCheck
    public void getoptions(){
        renderJsonData(service.getoptions());
    }

}
