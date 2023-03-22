package cn.rjtech.admin.workregionm;

import cn.hutool.core.date.DateUtil;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.model.momdata.Workregionm;
import cn.rjtech.util.Util;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.NotAction;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
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
        renderJsonData(service.pageList(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 新增
     */
    public void add() {
        render("add.html");
    }

    /**
     * 编辑
     */
    public void edit() {
        Workregionm workregionm = service.findById(getLong(0));
        if (workregionm == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        if (isOk(workregionm) && isOk(workregionm.getIPersonId())) {
//            String personName = CACHE.me.getPersonNameById(workregionm.getIpersonid());
//            set("personname", personName);
        }

        set("workregionm", workregionm);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(Workregionm.class, "workregionm")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(Workregionm.class, "workregionm")));
    }

    /**
     * 批量删除
     */
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

    @SuppressWarnings("unchecked")
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
        renderJxls("workregionm.xlsx", Kv.by("rows", data), "产线档案(选中导出)_" + DateUtil.today() + ".xlsx");
    }

    @SuppressWarnings("unchecked")
    public void exportExcelAll() throws Exception {
        List<Record> rows = service.list(getKv());
        if (notOk(rows)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderJxls("workregionm.xlsx", Kv.by("rows", rows), "产线档案_" + DateUtil.today() + ".xlsx");
    }

    @SuppressWarnings("unchecked")
    public void downloadTpl() throws Exception {
        renderJxls("workregionm_import.xlsx", Kv.by("rows", null), "产线档案导入模板.xlsx");
    }

    public void importExcel() {
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
        UploadFile file = getFile("file", uploadPath);
        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }
        renderJson(service.importExcelData(file.getFile()));
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
    public void selectWorkRegionMList() {
        renderJsonData(service.selectWorkRegionMList());
    }
    
    public void findByWarehouse(){
        renderJsonData(service.findByWarehouse());
    }
    
    public void findByWareHouseId(){
        renderJsonData(service.findByWareHouseId(getLong("iWarehouseId")));
    }
}
