package cn.rjtech.admin.scancodereceive;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.sysenumeration.SysEnumerationService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SysPureceive;
import cn.rjtech.model.momdata.SysPureceivedetail;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 双单位扫码收货
 *
 * @ClassName: ScanCodeReceiveAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-10 10:01
 */
@CheckPermission(PermissionKey.SCANCODERECEIVE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/scancodereceive", viewPath = "/_view/admin/scancodereceive")
public class ScanCodeReceiveAdminController extends BaseAdminController {

    @Inject
    private ScanCodeReceiveService service;
    @Inject
    private UserService userService;
    @Inject
    private ScanCodeReceiveDetailService syspureceivedetailservice;

    @Inject
    private SysEnumerationService sysenumerationservice;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 新增
     */
    public void add() {
        render("add().html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(SysPureceive.class, "sysPureceive")));
    }

    /**
     * 编辑
     */
    public void edit() {
        SysPureceive sysPureceive = service.findById(getLong(0));
        if (sysPureceive == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        //todo （仓库数据在从表） 关联查询出仓库编码,然后换数据源U8 查其名称
        SysPureceivedetail first = syspureceivedetailservice.findFirst("select * from  T_Sys_PUReceiveDetail where MasID = ?", sysPureceive.getAutoID());
        if (first != null && null != first.getWhcode()) {
            set("whname", sysenumerationservice.getWhname(first.getWhcode()));
        }
        //todo 关联查询用户名字
        if (null != sysPureceive.getCreatePerson()) {
            set("username", sysenumerationservice.getUserName(sysPureceive.getCreatePerson()));
        }
        //todo 换数据源U8 查供应商名称
        if (null != sysPureceive.getVenCode()) {
            set("venname", sysenumerationservice.getVenName(sysPureceive.getVenCode()));
        }
        set("sysPureceive", sysPureceive);
        render("edit().html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysPureceive.class, "sysPureceive")));
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

    /**
     * 切换IsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleBoolean(getLong(0), "IsDeleted"));
    }

    /**
     * 新增-可编辑表格-批量提交
     */
    @Before(Tx.class)
    public void submitAll() {
        renderJson(service.submitByJBoltTable(getJBoltTable()));
    }

    /**
     * 供应商数据源
     */
    public void venCode() {
        renderJsonData(service.getVenCodeDatas(getKv()));
    }


    /**
     * 仓库数据源
     */
    public void Whcode() {
        renderJsonData(service.getWhcodeDatas(getKv()));
    }
}
