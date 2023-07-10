package cn.rjtech.admin.container;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.render.JBoltByteFileType;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Container;
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
 * 仓库建模-容器档案
 *
 * @ClassName: ContainerAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 14:48
 */
@CheckPermission(PermissionKey.CONTAINER)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/container", viewPath = "/_view/admin/container")
public class ContainerAdminController extends BaseAdminController {

    @Inject
    private ContainerService service;

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
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 出入库数据源
     */
    public void data() {
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKv().set("isInner", 5)));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.CONTAINER_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.CONTAINER_ADD)
    public void save() {
        renderJson(service.save(getModel(Container.class, "container")));
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.CONTAINER_EDIT)
    public void edit() {
        Container container = service.findById(getLong(0));
        if (container == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("container", container);
        render("edit.html");
    }

    /**
     * 更新
     */
    @CheckPermission(PermissionKey.CONTAINER_EDIT)
    public void update() {
        renderJson(service.update(getModel(Container.class, "container")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.CONTAINER_DELETE)
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.deleted(getLong(0)));
    }

    /**
     * 切换isInner
     */
    public void toggleIsInner() {
        renderJson(service.toggleBoolean(getLong(0), "isInner"));
    }

    /**
     * 切换isEnabled
     */
    public void toggleIsEnabled() {
        renderJson(service.toggleBoolean(getLong(0), "isEnabled"));
    }

    /**
     * Excel模板下载
     */
    @SuppressWarnings("unchecked")
    public void downloadTpl() throws Exception {
        renderJxls("container_import.xlsx", Kv.by("rows", null), "容器档案导入模板.xlsx");
    }

    /**
     * 容器档案Excel导入数据库
     */
    @SuppressWarnings("unchecked")
    @CheckPermission(PermissionKey.CONTAINER_IMPORT)
    public void importExcelData() {
        UploadFile uploadFile = getFile("file");
        ValidationUtils.notNull(uploadFile, "上传文件不能为空");

        File file = uploadFile.getFile();

        List<String> list = StrUtil.split(uploadFile.getOriginalFileName(), StrUtil.DOT);

        // 截取最后一个“.”之前的文件名，作为导入格式名
        String cformatName = list.get(0);

        String extension = list.get(1);

        ValidationUtils.equals(extension, JBoltByteFileType.XLSX.suffix, "系统只支持xlsx格式的Excel文件");
        renderJson(service.importExcelData(file, cformatName));
    }

    /**
     * 导出数据
     */
    @SuppressWarnings("unchecked")
    @CheckPermission(PermissionKey.CONTAINER_EXPORT)
    public void dataExport() throws Exception {
        List<Record> rows = service.list(getKv());
        for (Record row : rows) {
            row.put("isinner", StrUtil.equals("1", row.getStr("isinner")) ? "社外" : "社内");
        }
        renderJxls("container.xlsx", Kv.by("rows", rows), "容器档案_" + DateUtil.today() + ".xlsx");
    }

    /**
     * 入库
     */
    @CheckPermission(PermissionKey.CONTAINER_RK)
    public void rk() {
        //标识-入库
        set("mark", "1");
        render("crk_form.html");
    }

    /**
     * 出库
     */
    @CheckPermission(PermissionKey.CONTAINER_CK)
    public void ck() {
        //标识-出库
        set("mark", "0");
        render("crk_form.html");
    }

    /**
     * 容器下拉信息查询
     */
    public void crkSelectData(String mark) {
        Kv kv = Kv.create();
        kv.set("isInner", mark);
        kv.set("isEnabled", 1);
        renderJsonData(service.list(kv));
    }

    /**
     * 出入库数据源
     */
    public void dataIndex(String mark) {
        set("mark", mark);
        render("crk_index.html");
    }

    /**
     * 出入库数据源
     */
    public void crkData(String mark) {
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKv().set("isInner", mark).set("isEnabled", 1)));
    }

    /**
     * 容器出入库处理
     */
    @CheckPermission(PermissionKey.CONTAINER_SUBMIT)
    public void handleData(String mark) {
        renderJsonData(service.handleData(getJBoltTable(), mark));
    }

    /**
     * 容器打印数据
     */
    @CheckPermission(PermissionKey.CONTAINER_PRINT)
    public void printData() {
        renderJsonData(service.getPrintDataCheck(getKv()));
    }

    @UnCheck
    public void options() {
        renderJsonData(service.options());
    }

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
