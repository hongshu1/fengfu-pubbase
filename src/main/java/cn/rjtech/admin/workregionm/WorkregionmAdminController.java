package cn.rjtech.admin.workregionm;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.service.JBoltFileService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.admin.warehousearea.WarehouseAreaService;
import cn.rjtech.model.momdata.Workregionm;
import cn.rjtech.util.Util;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.NotAction;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 工段档案 Controller
 *
 * @ClassName: WorkregionmAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-01 20:42
 */
@CheckPermission(PermissionKey.WORKREGIONM)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/workregionm", viewPath = "/_view/admin/workregionm")
public class WorkregionmAdminController extends JBoltBaseController {

    @Inject
    private WorkregionmService service;
    @Inject
    private PersonService personService;
    @Inject
    private WarehouseService warehouseService;
    @Inject
    private JBoltFileService jboltFileService;
    @Inject
    private WarehouseAreaService warehouseAreaService;

    /**
     * 首页
     */
    public void index() {
        render("index.html");
    }

    /**
     * 产线选择
     */
    @UnCheck
    public void select_dialog_index() {
        render("select_dialog_index.html");
    }

    /**
     * 数据源
     */
    @UnCheck
    public void datas() {
        renderJsonData(service.pageList(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.WORKREGIONM_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.WORKREGIONM_EDIT)
    public void edit() {
        Workregionm workregionm = service.findById(getLong(0));
        if (workregionm == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
//        if (isOk(workregionm) && isOk(workregionm.getIPersonId())) {
//            Person person = personService.findById(workregionm.getIPersonId());
//            if (person != null) {
//                set("personname", person.getCpsnName());
//            }
//        }

        set("workregionm", workregionm);
        render("edit.html");
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.WORKREGIONM_ADD)
    public void save() {
        renderJson(service.save(getModel(Workregionm.class, "workregionm")));
    }

    /**
     * 更新
     */
    @CheckPermission(PermissionKey.WORKREGIONM_EDIT)
    public void update() {
        renderJson(service.update(getModel(Workregionm.class, "workregionm")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.WORKREGIONM_DELETE)
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 切换toggleIsenabled
     */
    public void toggleIsenabled() {
        renderJson(service.toggleIsenabled(getLong(0)));
    }

    /**
     * 切换toggleIsdeleted
     */
    public void toggleIsdeleted() {
        renderJson(service.toggleIsdeleted(getLong(0)));
    }

    /**
     * 工段长列表
     */
    public void personTable() {
        render("person_table.html");
    }

    @CheckPermission(PermissionKey.WORKREGIONM_EXPORT)
    public void exportExcelByIds() throws Exception {
        String ids = get("ids");
        if (notOk(ids)) {
            renderJsonFail("未选择有效数据，无法导出");
            return;
        }
        List<Record> data = service.list(Kv.create().setIfNotBlank("ids", Util.getInSqlByIds(ids)));
        if (notOk(data)) {
            renderJsonFail("无有效数据导出");
            return;
        }

        renderJxls("work.xlsx", Kv.by("rows", data), "产线档案_" + DateUtil.today() + ".xlsx");
    }
    @CheckPermission(PermissionKey.WORKREGIONM_EXPORT)
    public void exportExcelAll() throws Exception {
        List<Record> rows = service.list(getKv());
        if (notOk(rows)) {
            renderJsonFail("无有效数据导出");
            return;
        }

        renderJxls("work.xlsx", Kv.by("rows", rows), "产线档案_" + DateUtil.today() + ".xlsx");
    }

    public void downloadTpl() throws Exception {
        renderBytesToExcelXlsxFile(service.exportExcelTpl(null));
    }

    @CheckPermission(PermissionKey.WORKREGIONM_IMPORT)
    public void importExcel() {
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
        UploadFile file = getFile("file", uploadPath);
        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }
        renderJson(service.importExcel(file.getFile()));
    }

    @NotAction
    public void exportDataChange(List<Record> data) {
        for (Record record : data) {
            record.put("djobdate", DateUtil.formatDate(record.getDate("djobdate")));
        }
    }

    public void dataList() {
        renderJsonData(service.dataList());
    }

    /**
     * 通过部门获取关联产线
     */
    @UnCheck
    public void getOptionsByDept() {
        String pid = get("pid");
        if (notOk(pid)) {
            renderJsonData(new ArrayList<>());
            return;
        }
        renderJsonData(service.list(Kv.of("idepid", Long.valueOf(pid)).setIfNotNull("isenabled", "true")));
    }

    /**
     * 获取产线列表
     */
    @UnCheck
    public void options() {
        renderJsonData(service.list(Kv.of("isenabled", "true")));
    }

    /**
     * 查询产线列表
     */
    @UnCheck
    public void selectWorkRegionMList() {
        renderJsonData(service.selectWorkRegionMList());
    }

    @UnCheck
    public void findByWarehouse() {
        renderJsonData(warehouseService.findByWarehouse());
    }

    @UnCheck
    public void findByWareHouseId(@Para(value = "iWarehouseId") Long iWarehouseId) {
        if (ObjUtil.isNull(iWarehouseId)) {
            renderJsonSuccess();
            return;
        }
        renderJsonData(warehouseAreaService.findByWareHouseId(iWarehouseId));
    }

    @UnCheck
    public void findPersonPage() {
        renderJsonData(personService.paginateDatas(getPageNumber(), getPageSize(), getKv()));
    }

    public void uploadImages() {
        //上传到今天的文件夹下
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_IMAGE_UPLOADER);
        List<UploadFile> files = getFiles(uploadPath);
        if (files == null || files.size() == 0) {
            renderJsonFail("请选择图片后上传");
            return;
        }
        StringBuilder msg = new StringBuilder();
        files.forEach(file -> {
            if (notImage(file)) {
                msg.append(file.getFileName()).append("不是图片类型文件;");
            }
        });
        if (msg.length() > 0) {
            renderJsonFail(msg.toString());
            return;
        }

        List<String> retFiles = new ArrayList<>();
        Ret ret;
        StringBuilder errormsg = new StringBuilder();
        for (UploadFile uploadFile : files) {
            ret = jboltFileService.saveImageFile(uploadFile, uploadPath);
            if (ret.isOk()) {
                retFiles.add(ret.getStr("data"));
            } else {
                errormsg.append(uploadFile.getOriginalFileName()).append("上传失败;");
            }
        }
        if (retFiles.size() == 0) {
            renderJsonFail(errormsg.toString());
            return;
        }
        renderJsonData(retFiles, errormsg.toString());
    }

}
