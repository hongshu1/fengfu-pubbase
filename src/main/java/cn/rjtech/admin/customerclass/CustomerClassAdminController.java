package cn.rjtech.admin.customerclass;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.render.JBoltByteFileType;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.CustomerClass;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.util.List;

/**
 * 往来单位-客户分类
 *
 * @ClassName: CustomerClassAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-21 11:01
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.CUSTOMER)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/customerclass", viewPath = "/_view/admin/customerclass")
public class CustomerClassAdminController extends BaseAdminController {

    @Inject
    private CustomerClassService service;

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
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKeywords()));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.CUSTOMER_ADD)
    public void add() {
        if (isOk(get("autoid"))) {
            CustomerClass customerclass = service.findById(get("autoid"));
            set("pid", customerclass.getIAutoId());
        }
        render("add.html");
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.CUSTOMER_EDIT)
    public void edit() {
        CustomerClass customerclass = service.findById(getLong(0));
        if (customerclass == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("pid", customerclass.getIPid());
        set("customerclass", customerclass);
        render("edit.html");
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.CUSTOMER_ADD)
    public void save() {
        renderJson(service.save(getModel(CustomerClass.class, "customerclass")));
    }

    /**
     * 更新
     */
    @CheckPermission(PermissionKey.CUSTOMER_EDIT)
    public void update() {
        renderJson(service.update(getModel(CustomerClass.class, "customerclass")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.CUSTOMER_DELETE)
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 切换toggleIsdeleted
     */
    public void toggleIsdeleted() {
        renderJson(service.toggleIsdeleted(getLong(0)));
    }

    /**
     * 切换toggleIsenabled
     */
    public void toggleIsenabled() {
        renderJson(service.toggleIsenabled(getLong(0)));
    }

    /**
     * 树结构数据源
     */
    public void mgrTree() {
        renderJsonData(service.getMgrTree(getLong("selectId", getLong(0)), getInt("openLevel", 0)));
    }

    /**
     * select数据源 只需要enable=true的
     */
    public void enableOptions() {
        renderJsonData(service.getTreeDatas(true, true));
    }

    /**
     * 导出数据
     */
    @SuppressWarnings("unchecked")
    public void dataExport() throws Exception {
        List<Record> rows = service.getDataExport(getKv());

        renderJxls("customerclass.xlsx", Kv.by("rows", rows), "客户分类_" + DateUtil.today() + ".xlsx");
    }

    /**
     * Excel模板下载
     */
    @SuppressWarnings("unchecked")
    public void downloadTpl() throws Exception {
        renderJxls("customerclass_import.xlsx", Kv.by("rows", null), "客户档案分类.xlsx");
    }

    /**
     * 客户档案Excel导入数据库
     */
    @CheckPermission(PermissionKey.CUSTOMER_IMPORT)
    public void importExcel() {
        UploadFile uploadFile = getFile("file");
        ValidationUtils.notNull(uploadFile, "上传文件不能为空");
        
        File file = uploadFile.getFile();
        List<String> list = StrUtil.split(uploadFile.getOriginalFileName(), StrUtil.DOT);
        
        ValidationUtils.equals(list.get(1), JBoltByteFileType.XLSX.suffix, "系统只支持xlsx格式的Excel文件");
        
        renderJson(service.importExcelData(file));
    }

}
