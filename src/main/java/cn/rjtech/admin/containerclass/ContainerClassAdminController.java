package cn.rjtech.admin.containerclass;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ContainerClass;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.util.List;

/**
 * 分类管理
 *
 * @ClassName: ContainerClassAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 16:16
 */
@CheckPermission(PermissionKey.CONTAINERCLASS)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/containerclass", viewPath = "/_view/admin/containerclass")
public class ContainerClassAdminController extends BaseAdminController {

    @Inject
    private ContainerClassService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize()));
    }

    /**
     * 数据源
     */
    @UnCheck
    public void list() {
        Okv kv = Okv.create();
        kv.set("IsEnabled", 1);
        kv.set("IsDeleted", 0);

        renderJsonData(service.getCommonList(kv, "dCreateTime", "desc"));
//		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize()));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.CONTAINERCLASS_ADD)
    public void add() {
        set("containerClass", service.getContainerClassCode());
        render("add.html");
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.CONTAINERCLASS_ADD)
    public void save() {
        renderJson(service.save(getModel(ContainerClass.class, "containerClass")));
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.CONTAINERCLASS_EDIT)
    public void edit() {
        ContainerClass containerClass = service.findById(getLong(0));
        if (containerClass == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("containerClass", containerClass);
        render("edit.html");
    }

    /**
     * 更新
     */
    @CheckPermission(PermissionKey.CONTAINERCLASS_EDIT)
    public void update() {
        renderJson(service.update(getModel(ContainerClass.class, "containerClass")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.CONTAINERCLASS_DELETE)
    public void deleteByIds() {
        Kv kv = getKv();
        renderJson(service.delete(kv.getStr("ids")));
    }

    /**
     * Excel模板下载
     */
    @SuppressWarnings("unchecked")
    public void downloadTpl() throws Exception {
        renderJxls("containerClass_import.xlsx", Kv.by("rows", null), "容器分类导入模板.xlsx");
    }

    /**
     * 容器档案Excel导入数据库
     */
    public void importExcel() {
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
        UploadFile file = getFile("file", uploadPath);
        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }
        renderJson(service.importExcelData(file.getFile()));
    }

    /**
     * 导出数据
     */
    @SuppressWarnings("unchecked")
    @CheckPermission(PermissionKey.CONTAINERCLASS_EXPORT)
    public void dataExport() throws Exception {
        List<Record> rows = service.list(getKv());
        for (Record row : rows) {
            row.put("isenabled", StrUtil.equals("1", row.getStr("isenabled")) ? "是" : "否");
        }
        renderJxls("containerClass.xlsx", Kv.by("rows", rows), "容器分类_" + DateUtil.today() + ".xlsx");
    }

    @CheckPermission(PermissionKey.CONTAINERCLASS_IMPORT)
    public void importExcelClass() {
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
        UploadFile file = getFile("file", uploadPath);
        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }
        renderJson(service.importExcelClass(file.getFile()));
    }

}
