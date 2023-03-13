package cn.rjtech.admin.usertype;

import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.UserType;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.service.UserTypeService;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 用户类型
 *
 * @ClassName: UserTypeAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-12 16:09
 */
@Before(JBoltAdminAuthInterceptor.class)
@CheckPermission(PermissionKey.USER_TYPE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/usertype", viewPath = "/_view/admin/usertype")
public class UserTypeAdminController extends JBoltBaseController {

    @Inject
    private UserTypeService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getSortColumn("id"), getSortType("desc"), get("usertypeCode"), get("usertypeName"), getBoolean("isInner"), getBoolean("isSysPreset"), getBoolean("isEnabled"), getBoolean("isDeleted", false)));
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
    @Before(Tx.class)
    public void save() {
        renderJson(service.save(getModel(UserType.class, "userType"), JBoltUserKit.getUser(), new Date()));
    }

    /**
     * 编辑
     */
    public void edit() {
        UserType userType = service.findById(getLong(0));
        if (userType == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("userType", userType);
        render("edit.html");
    }

    /**
     * 更新
     */
    @Before(Tx.class)
    public void update() {
        renderJson(service.update(getModel(UserType.class, "userType")));
    }

    /**
     * 批量删除
     */
    @Before(Tx.class)
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
     * 删除
     */
    @Before(Tx.class)
    public void delete() {
        renderJson(service.deleteById(getLong("ids")));
    }

    /**
     * 进入import_excel.html
     */
    public void initImportExcel() {
        render("import_excel.html");
    }

    /**
     * 下载导入模板
     */
    public void downloadTpl() {
        renderBytesToExcelXlsFile(service.getImportExcelTpl().setFileName("用户类型导入模板"));
    }

    /**
     * 执行导入excel
     */
    public void importExcel() {
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.IMPORT_EXCEL_TEMP_FOLDER);
        UploadFile file = getFile("file", uploadPath);
        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }
        renderJson(service.importExcel(file.getFile()));
    }

    /**
     * 执行导出excel 根据查询form表单
     */
    public void exportExcelByForm() {
        Page<UserType> pageData = service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getSortColumn("id"), getSortType("desc"), get("usertypeCode"), get("usertypeName"), getBoolean("isInner"), getBoolean("isSysPreset"), getBoolean("isEnabled"), getBoolean("isDeleted", false));
        if (notOk(pageData.getTotalRow())) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderBytesToExcelXlsxFile(service.exportExcel(pageData.getList()).setFileName("用户类型"));
    }

    /**
     * 执行导出excel 根据表格选中数据
     */
    public void exportExcelByCheckedIds() {
        String ids = get("ids");
        if (notOk(ids)) {
            renderJsonFail("未选择有效数据，无法导出");
            return;
        }
        List<UserType> datas = service.getListByIds(ids);
        if (notOk(datas)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderBytesToExcelXlsxFile(service.exportExcel(datas).setFileName("用户类型"));
    }

    /**
     * 切换is_inner
     */
    @Before(Tx.class)
    public void toggleIsInner() {
        renderJson(service.toggleBoolean(getLong(0), "is_inner"));
    }

    /**
     * 切换is_sys_preset
     */
    @Before(Tx.class)
    public void toggleIsSysPreset() {
        renderJson(service.toggleBoolean(getLong(0), "is_sys_preset"));
    }

    /**
     * 切换is_enabled
     */
    @Before(Tx.class)
    public void toggleIsEnabled() {
        renderJson(service.toggleBoolean(getLong(0), "is_enabled"));
    }

    /**
     * 批量恢复假删数据
     */
    @Before(Tx.class)
    public void recoverByIds() {
        renderJson(service.recoverByIds(get("ids")));
    }

    /**
     * 批量 真删除
     */
    @Before(Tx.class)
    public void realDeleteByIds() {
        renderJson(service.realDeleteByIds(get("ids")));
    }

    public void test() {
       Set<String> set =  TableMapping.me().getTable(UserType.class).getColumnNameSet();
        System.out.println(JSON.toJSONString(set));
        renderJson(set);
    }

    public void options() {
        renderJsonData(service.getOptionList("usertype_name", "id", Okv.by("is_deleted", "0")));
    }

}
