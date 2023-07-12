package cn.rjtech.admin.vendor;

import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.render.JBoltByteFileType;
import cn.rjtech.admin.vendoraddr.VendorAddrService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Vendor;
import cn.rjtech.model.momdata.VendorAddr;
import cn.rjtech.model.momdata.base.BaseVendorAddr;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 往来单位-供应商档案
 *
 * @ClassName: VendorAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-21 17:16
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.VENDOR)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/vendor", viewPath = "/_view/admin/vendor")
public class VendorAdminController extends BaseAdminController {

    @Inject
    private VendorService     service;
    @Inject
    private VendorAddrService vendorAddrService;

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
//		renderJsonData(service.pageList(getKv()));
        Page<Record> adminDatas = service
            .getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isEnabled"), getBoolean("isDeleted"),
                getKv());
        renderJsonData(adminDatas);
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.VENDOR_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(Vendor.class, "vendor")));
    }

    /**
     * JBoltTable 可编辑表格整体提交
     */
    @Before(Tx.class)
    @CheckPermission(PermissionKey.VENDOR_SUBMIT)
    public void submit() {
        renderJson(service.submitByJBoltTable(getJBoltTable()));
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.VENDOR_EDIT)
    public void edit() {
        Vendor vendor = service.findById(getLong(0));
        if (vendor == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        Kv kv = new Kv();
        kv.set("ivendorid", vendor.getIAutoId());
        List<VendorAddr> list = vendorAddrService.list(kv);
        String addr = vendor.getCProvince() + "," + vendor.getCCity() + "," + vendor.getCCounty();
        vendor.setCProvince(addr);
        BigDecimal iTaxRate = vendor.getITaxRate();
        if (iTaxRate != null) {
            vendor.setITaxRate(iTaxRate.setScale(2, BigDecimal.ROUND_DOWN));
        }
        set("vendor", vendor);
        set("vendoraddr", !list.isEmpty() ? list.get(0) : "");
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(Vendor.class, "vendor")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.VENDOR_DELETE)
    public void deleteByIds() {
        renderJson(service.deleteByAutoids(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.deleteByAutoid(getLong(0)));
    }

    /*
     * 删除供应商地址的关联记录
     */
    public void deleteVendorAddrById(Long vendorIautoId) {
        Vendor vendor = service.findById(vendorIautoId);
        Kv kv = new Kv();
        kv.set("ivendorid", vendor.getIAutoId());
        List<VendorAddr> list = vendorAddrService.list(kv);
        if (!list.isEmpty()) {
            List<Long> collect = list.stream().map(BaseVendorAddr::getIAutoId).collect(Collectors.toList());
            vendorAddrService.deleteByIds(collect.toArray());
        }
    }

    /**
     * 切换isEnabled
     */
    public void toggleIsEnabled() {
        renderJson(service.toggleIsenabled(getLong(0)));
    }

    @UnCheck
    public void options() {
        renderJsonData(service.options());
    }

    /**
     * 选项列表
     */
    @UnCheck
    public void autocomplete() {
        renderJsonData(service.getAutocompleteList(get("q"), getInt("limit", 10)));
    }

    /**
     * Excel模板下载
     */
    @SuppressWarnings("unchecked")
    public void downloadTpl() throws Exception {
        renderJxls("vendor_import.xlsx", Kv.by("rows", null), "供应商导入模板.xlsx");
    }

    /**
     * 供应商分类Excel导入数据库
     */
    @CheckPermission(PermissionKey.VENDOR_IMPORT)
    public void importExcel() {
        UploadFile uploadFile = getFile("file");
        ValidationUtils.notNull(uploadFile, "上传文件不能为空");

        File file = uploadFile.getFile();
        
        List<String> list = StrUtil.split(uploadFile.getOriginalFileName(), StrUtil.DOT);
        ValidationUtils.equals(list.get(1), JBoltByteFileType.XLSX.suffix, "系统只支持xlsx格式的Excel文件");

        renderJson(service.importExcelData(file));
    }
    
}
