package cn.rjtech.admin.department;

import cn.hutool.core.util.ObjUtil;
import cn.jbolt._admin.globalconfig.GlobalConfigService;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.config.MesConfigKey;
import cn.rjtech.model.momdata.Department;
import cn.rjtech.model.momdata.Person;
import cn.rjtech.util.Util;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.util.List;

/**
 * 组织建模-部门档案
 *
 * @ClassName: DepartmentAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 11:55
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.DEPARTMENT)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/department", viewPath = "/_view/admin/department")
public class DepartmentAdminController extends BaseAdminController {

    @Inject
    private DepartmentService service;
    @Inject
    private PersonService personService;
    @Inject
    private GlobalConfigService globalConfigService;

    /**
     * 首页
     */
    public void index() {
        render("tree_index.html");
    }

    /**
     * 数据源
     */
    @UnCheck
    public void datas() {
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv(), getSortColumn("iAutoId"), getSortType("desc")));
    }

    /**
     * 数据源
     */
    @UnCheck
    public void treeDatas() {
        renderJsonData(service.getTreeTableDatas(getKv()));
    }

    /**
     * 数据源
     */
    @UnCheck
    public void treeDatasForProposalSystem() {
        Kv para = getKv();
        String depGrade = globalConfigService.getConfigValue(MesConfigKey.DEP_GRADE);
        depGrade = JBoltStringUtil.isBlank(depGrade) ? "3" : depGrade;
        para.set("idepgrade", depGrade);
        para.set("isEnabled", "true");
        renderJsonData(service.treeDatasForProposalSystem(para));
    }

    /**
     * 数据源
     */
    @UnCheck
    public void treeDatasForProposalReport() {
        Kv para = getKv();
        para.set("isProposal", "true");
        para.set("isEnabled", "true");
        renderJsonData(service.treeDatasForProposalSystem(para));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.DEPARTMENT_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 保存
     */
    @CheckPermission(PermissionKey.DEPARTMENT_ADD)
    public void save() {
        renderJson(service.save(getModel(Department.class, "department")));
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.DEPARTMENT_EDIT)
    public void edit() {
        Department department = service.findById(getLong(0));
        if (department == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        if (ObjUtil.isNotNull(department.getIDutyUserId())) {
            Person person = personService.findById(department.getIDutyUserId());
            if (ObjUtil.isNotNull(person)) {
                set("cdeppersonname", person.getCpsnName());
            }
        }
        if (ObjUtil.isNotNull(department.getCDepLeader())) {
            Person person = personService.findById(department.getCDepLeader());
            if (ObjUtil.isNotNull(person)) {
                set("cdepleadername", person.getCpsnName());
            }
        }
        if (ObjUtil.isNotNull(department.getIPid())) {
            Department pDepartment = service.findById(department.getIPid());
            if (ObjUtil.isNotNull(pDepartment)) {
                set("pname", pDepartment.getCDepName());
            }
        }
        set("department", department);
        render("edit.html");
    }

    /**
     * 更新
     */
    @CheckPermission(PermissionKey.DEPARTMENT_EDIT)
    public void update() {
        renderJson(service.update(getModel(Department.class, "department")));
    }


    public void refreshAllEndGrade() {
        renderJson(service.refreshAllEndGrade());
    }


    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.DEPARTMENT_DEL)
    public void deleteByIds() {
        renderJson(service.delByIds(get("ids")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.DEPARTMENT_DEL)
    public void delete() {
        Department department = service.findById(getLong(0));
        ValidationUtils.notNull(department, JBoltMsg.DATA_NOT_EXIST);
        service.checkRepetitionData(true, "ipid", get(0), "该部门存在下级部门，不允许删除");
        department.setIsDeleted(true);
        renderJson(service.update(department));
    }

    /**
     * 切换bDepEnd
     */
    public void toggleBDepEnd() {
        renderJson(service.toggleBoolean(getLong(0), "bDepEnd"));
    }

    /**
     * 切换isEnabled
     */
    public void toggleIsEnabled() {
        renderJson(service.toggleBoolean(getLong(0), "isEnabled"));
    }

    /**
     * 切换isApsInvoled
     */
    public void toggleIsApsInvoled() {
        renderJson(service.toggleBoolean(getLong(0), "isApsInvoled"));
    }

    /**
     * 切换isDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleBoolean(getLong(0), "isDeleted"));
    }

    /**
     * 切换isProposal
     */
    public void toggleIsProposal() {
        renderJson(service.toggleBoolean(getLong(0), "isProposal"));
    }

    /**
     * 人员列表
     */
    public void personTable() {
        render("person_table.html");
    }

    @UnCheck
    public void findPersonPage() {
        renderJsonData(personService.paginateDatas(getPageNumber(), getPageSize(), getKv()));
    }


    /**
     * 执行导入
     */
    @CheckPermission(PermissionKey.DEPARTMENT_IMPORT)
    public void importExcel() {
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);

        UploadFile file = getFile("file", uploadPath);
        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }

        renderJson(service.importRecordsFromExcel(file.getFile()));
    }


    /**
     * 下拉框选择人员数据源
     */
    @UnCheck
    public void selectPerson() {
        String key = get("key");
        Kv kv = new Kv();
        kv.setIfNotNull("cpersonname", key);
        renderJsonData(personService.findAll(kv));
    }

    @UnCheck
    public void findPersonAll() {
        renderJsonData(personService.findAll(getKv()));
    }

    @UnCheck
    public void findParentData(Long excludeId) {
        renderJsonData(service.findParentData(excludeId));
    }

    @CheckPermission(PermissionKey.DEPARTMENT_EXPORT)
    public void exportExcelByIds() throws Exception {
        String ids = get("ids");
        if (notOk(ids)) {
            renderJsonFail("未选择有效数据，无法导出");
            return;
        }
        List<Record> data = service.findAll(Kv.create().setIfNotBlank("ids", Util.getInSqlByIds(ids)));
        if (notOk(data)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderBytesToExcelXlsFile(service.exportExcelTpl(data));
    }

    @CheckPermission(PermissionKey.DEPARTMENT_EXPORT)
    public void exportExcelAll() throws Exception {
        List<Record> rows = service.findAll(getKv());
        if (notOk(rows)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderBytesToExcelXlsFile(service.exportExcelTpl(rows));
    }

    @UnCheck
    public void downloadTpl() throws Exception {
        renderBytesToExcelXlsFile(service.exportExcelTpl(null));
    }

    @UnCheck
    public void getTreeTableDatas() {
        List<Record> datas = service.findAll(getKv());
        List<Record> trees = service.convertToRecordTree(datas, Department.IAUTOID, Department.IPID, (p) -> this.notOk(p.getLong(Department.IPID)));
        renderJsonData(trees);
    }

    @UnCheck
    public void options() {
        renderJsonData(service.findAll());
    }

    /**
     * 部门数据源
     */
    @UnCheck
    public void list() {
        Okv kv = Okv.by("IsEnabled", "1")
                .set("IsDeleted", "0");
        
        renderJsonData(service.getCommonList(kv, "dCreateTime", "desc"));
    }

}
