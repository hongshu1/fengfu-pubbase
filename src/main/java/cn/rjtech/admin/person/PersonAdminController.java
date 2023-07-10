package cn.rjtech.admin.person;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.annotation.CheckDataPermission;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.common.enums.BusObjectTypeEnum;
import cn.jbolt.core.common.enums.DataOperationEnum;
import cn.jbolt.core.model.User;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Person;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.util.List;

/**
 * 人员档案 Controller
 *
 * @ClassName: PersonAdminController
 * @author: heming
 * @date: 2023-03-21 15:11
 */
@CheckPermission(PermissionKey.PERSON_INDEX)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/person", viewPath = "/_view/admin/person")
public class PersonAdminController extends BaseAdminController {

    @Inject
    private PersonService service;
    @Inject
    private UserService userService;

    /**
     * 首页
     */
    public void index() {
        render("index.html");
    }

    /**
     * 数据源
     */
    @CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
    public void datas() {
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.PERSON_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.PERSON_EDIT)
    public void edit() {
        Person person = service.findById(getLong(0));
        if (person == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        Record rc = person.toRecord();
        User user = userService.findById(rc.getLong("iuserid"));
        rc.set("cusername", user == null ? null : user.getName());
        rc.set("sysworkage", service.calcSysworkage(rc.getDate("dhiredate")));
        set("person", rc);
        render("edit.html");
    }

    /**
     * 保存
     */

    public void save() {
        renderJson(service.save(getModel(Person.class, "person")));
    }

    /**
     * 更新
     */

    public void update() {
        renderJson(service.update(getModel(Person.class, "person")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.PERSON_DELETE)
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 删除
     */
    @UnCheck
    public void deleteByAjax() {
        renderJson(service.deleteByAjax());
    }

    /**
     * 切换toggleBPsnPerson
     */
    public void toggleBPsnPerson() {
        renderJson(service.toggleBPsnPerson(getLong(0)));
    }

    /**
     * 切换toggleBProbation
     */
    public void toggleBProbation() {
        renderJson(service.toggleBProbation(getLong(0)));
    }

    /**
     * 切换toggleBDutyLock
     */
    public void toggleBDutyLock() {
        renderJson(service.toggleBDutyLock(getLong(0)));
    }

    /**
     * 切换toggleBpsnshop
     */
    public void toggleBpsnshop() {
        renderJson(service.toggleBpsnshop(getLong(0)));
    }

    /**
     * 切换toggleIsEnabled
     */
    public void toggleIsEnabled() {
        renderJson(service.toggleIsEnabled(getLong(0)));
    }

    /**
     * 切换toggleIsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleIsDeleted(getLong(0)));
    }

    /**
     * 表格提交
     */
    @CheckDataPermission(operation = DataOperationEnum.EDIT, type = BusObjectTypeEnum.DEPTARTMENT)
    public void submitTable() {
        renderJson(service.submitTable(getJBoltTable()));
    }

    /**
     * 数据导入界面
     */
    @CheckPermission(PermissionKey.PERSON_IMPORT)
    public void importExcelIndex() {
        render("import_excel_index.html");
    }

    /**
     * 模板下载
     */
    @UnCheck
    @SuppressWarnings("unchecked")
    public void downloadTpl() throws Exception {
        // 设置下载头
        renderJxls("personDownloadTpl.xlsx", Kv.create(), "人员档案导入.xlsx");
    }

    /**
     * 数据导入
     */
    @UnCheck
    public void importExcelDatas() {
        //上传到今天的文件夹下
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_FILE_UPLOADER);
        List<UploadFile> files = getFiles(uploadPath);
        if (!isOk(files)) {
            renderBootFileUploadFail("文件上传失败!");
            return;
        }
        for (UploadFile file : files) {
            if (notExcel(file)) {
                renderJsonFail("请上传excel文件");
                return;
            }
        }
        renderJson(service.importExcelDatas(files, getKv()));
    }

    @UnCheck
    public void autocomplete() {
        renderJsonData(service.getAutocompleteList(get("q"), getInt("limit", 10), true, "cPsn_Num,cPsn_Name"));
    }

    @UnCheck
    public void autocompleteWithDept() {
        renderJsonData(service.getAutocompleteListWithDept(get("cdepcode"), get("q"), getInt("limit", 10)));
    }

    @UnCheck
    public void getAutocompleteDatasContainSubDep() {
        renderJsonData(service.getAutocompleteDatasContainSubDep(get("cdepcode"), get("q"), getInt("limit", 10)));
    }
    
    /**
     * 选择页面
     */
    @UnCheck
    public void choose() {
        keepPara();
        render("choose.html");
    }

}
