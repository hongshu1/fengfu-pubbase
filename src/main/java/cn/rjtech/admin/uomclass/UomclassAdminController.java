package cn.rjtech.admin.uomclass;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.render.JBoltByteFileType;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.model.momdata.Uomclass;
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
 * 计量单位组档案 Controller
 *
 * @ClassName: UomclassAdminController
 * @author: chentao
 * @date: 2022-11-02 17:26
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.UOMCLASS)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/uomclass", viewPath = "/_view/admin/uomclass")
public class UomclassAdminController extends JBoltBaseController {

    @Inject
    private UomclassService service;
    @Inject
    private PersonService personService;

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
    @CheckPermission(PermissionKey.UOMCLASS_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.UOMCLASS_EDIT)
    public void edit() {
        Uomclass uomclass = service.findById(getLong(0));
        if (uomclass == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("uomclass", uomclass);
        render("edit.html");
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.UOMCLASS_ADD)
    public void save() {
        renderJson(service.save(getModel(Uomclass.class, "uomclass")));
    }

    /**
     * 更新
     */
    @CheckPermission(PermissionKey.UOMCLASS_EDIT)
    public void update() {
        renderJson(service.update(getModel(Uomclass.class, "uomclass")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.UOMCLASS_DELETE)
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
     * 数据源 查询全部启用数据
     */
    public void dataList() {
        renderJsonData(service.dataList(true));
    }

    /**
     * 树结构数据源
     */
    public void mgrTree() {
        renderJsonData(service.getMgrTree(getLong("selectId", getLong(0)), getInt("openLevel", 0)));
    }

    @SuppressWarnings("unchecked")
    @CheckPermission(PermissionKey.UOMCLASS_EXPORT)
    public void exportExcelAll() throws Exception {
        List<Record> record = service.findUomClassRecord();
        if (notOk(record)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        for (Record r : record) {
            r.put("cuomclasssn", personService.formatPattenSn(r.getStr("cuomclasssn"), "uom_type"));
            r.put("isdefault", StrUtil.equals("1", r.getStr("isdefault")) ? "是" : "否");
        }
        renderJxls("uomclass.xlsx", Kv.by("rows", record), "计量单位组数据导出_" + DateUtil.today() + ".xlsx");
    }

    @SuppressWarnings("unchecked")
    public void downloadTpl() throws Exception {
        renderJxls("uomclass_import.xlsx", Kv.by("rows", null), "计量单位组导入模板.xlsx");
    }

    @CheckPermission(PermissionKey.UOMCLASS_IMPORT)
    public void importExcel() {
        UploadFile uploadFile = getFile("file");
        ValidationUtils.notNull(uploadFile, "上传文件不能为空");
        
        File file = uploadFile.getFile();
        List<String> list = StrUtil.split(uploadFile.getOriginalFileName(), StrUtil.DOT);
        
        ValidationUtils.equals(list.get(1), JBoltByteFileType.XLSX.suffix, "系统只支持xlsx格式的Excel文件");
        renderJson(service.importExcelData(file));
    }

    @UnCheck
    public void options() {
        renderJsonData(service.getOptions());
    }



}
