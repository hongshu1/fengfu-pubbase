package cn.rjtech.admin.vendorclass;

import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.render.JBoltByteFileType;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.VendorClass;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.util.List;

/**
 * 往来单位-供应商分类
 *
 * @ClassName: VendorClassAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 10:04
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.VENDORCLASS)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/vendorclass", viewPath = "/_view/admin/vendorclass")
public class VendorClassAdminController extends BaseAdminController {

    @Inject
    private VendorClassService service;

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
        renderJsonData(service.pageList(getKv()));
//		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isDeleted")));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.VENDOR_ADD)
    public void add() {
        if (isOk(get("iautoid"))) {
            VendorClass vendorClass = service.findById(get("iautoid"));
            set("vendorclass", vendorClass);
        }
        render("add.html");
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.VENDOR_ADD)
    public void save() {
        renderJson(service.save(getModel(VendorClass.class, "vendorClass")));
    }

    /**
     * 编辑
     */
    public void edit() {
        VendorClass vendorClass = service.findById(getLong(0));
        if (vendorClass == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("pid", vendorClass.getIPid());
        set("vendorClass", vendorClass);
        render("edit.html");
    }

    /**
     * select数据源 只需要enable=true的
     */
    public void enableOptions() {
        renderJsonData(service.getTreeDatas());
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(VendorClass.class, "vendorClass")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.VENDOR_DELETE)
    public void deleteByIds() {
        renderJson(service.deleteVendorClassByIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 树结构数据源
     */
    public void mgrTree() {
        renderJsonData(service.getMgrTree(getLong("selectId", getLong(0)), getInt("openLevel", 0)));
    }

    /**
     * 导出数据
     */
    @SuppressWarnings("unchecked")
    @CheckPermission(PermissionKey.VENDOR_EXPORT)
    public void dataExport() throws Exception {
		/*List<Record> rows = service.getDataExport(getKv());
		renderJxls("vendorclass.xlsx", Kv.by("rows",rows),"供应商分类_" + DateUtil.today() + ".xlsx");*/
        //1、查询全部
//		 List<VendorClass> vendorClassList =service.findAll();
        List<VendorClass> list = service.list(getKv());
        //2、生成excel文件
        JBoltExcel jBoltExcel = service.exportExcelTpl(list);
        //3、导出
        renderBytesToExcelXlsFile(jBoltExcel);
    }

    /**
     * Excel模板下载
     */
    @SuppressWarnings("unchecked")
    public void downloadTpl() throws Exception {
        renderJxls("vendorclass_import.xlsx", Kv.by("rows", null), "供应商分类导入模板.xlsx");
//        renderBytesToExcelXlsFile(service.getExcelImportTpl().setFileName("供应商分类导入模板"));
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

        String extension = list.get(1);
        ValidationUtils.equals(extension, JBoltByteFileType.XLSX.suffix, "系统只支持xlsx格式的Excel文件");

        renderJson(service.importExcelData(file));
    }

}
