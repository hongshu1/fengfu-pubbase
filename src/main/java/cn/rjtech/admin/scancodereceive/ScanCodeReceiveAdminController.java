package cn.rjtech.admin.scancodereceive;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.sysenumeration.SysEnumerationService;
import cn.rjtech.admin.syspureceive.SysPureceiveService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SysPureceive;
import cn.rjtech.model.momdata.SysPureceivedetail;
import cn.rjtech.model.momdata.Warehouse;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 双单位扫码收货
 *
 * @ClassName: ScanCodeReceiveAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-10 10:01
 */
@CheckPermission(PermissionKey.DOUBLE_CODE_SCANNING_SHIPMENT)
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
    private SysPureceiveService sysPureceiveService;
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
        render("add.html");
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
        String autoid = get("autoid");
        SysPureceive sysPureceive = service.findById(autoid);
        if (sysPureceive == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("sysPureceive", sysPureceive);
        keepPara();
        render("edit.html");
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

    /**
     * 库区数据源
     */
    public void findWhArea() {
        String WhCode = get("WhCode");
        ValidationUtils.notBlank(WhCode, "请先选择仓库");
        String q = get("q");
        Kv kv = new Kv();
        kv.set("keywords",q);
        kv.set("whCode",WhCode);
        renderJsonData(service.findWhArea(kv));
    }

    /**
     * 获取资源
     */
    public void getResource(){
        String q = get("q");
        if (notOk(q)){
            renderJsonSuccess();
            return;
        }
        String itemHidden = get("itemHidden");
        String groupCode = get("groupCode");
        String sourceBillType = get("sourceBillType");
        Kv kv = new Kv();
        kv.set("keywords",q);
        kv.setIfNotNull("sourceBillType", sourceBillType);
        kv.setIfNotNull("combination", groupCode);
        kv.setIfNotNull("itemHidden", itemHidden);
        kv.setIfNotNull("barcodetype", "转换前");
        renderJsonData(service.getResource(kv));
    }

    /**
     * 查询双单位条码数据
     */
    public void getBarCodeData(){
        String itemCode = get("itemCode");
        String supplier = get("supplier");
        String sourceBillType = get("sourceBillType");
        Kv kv = new Kv();
        kv.set("itemCode",itemCode);
        kv.setIfNotNull("sourceBillType", sourceBillType);
        kv.set("supplier",notOk(supplier)?' ':supplier);
        renderJsonData(service.getBarcodeDatas(kv));
    }
}
