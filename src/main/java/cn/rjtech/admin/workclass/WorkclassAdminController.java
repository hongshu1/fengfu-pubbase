package cn.rjtech.admin.workclass;

import cn.hutool.core.date.DateUtil;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.common.util.CACHE;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
//import cn.rjtech.admin.person.PersonService;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.rjtech.model.momdata.Workclass;
import cn.rjtech.util.Util;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.NotAction;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.util.List;

/**
 * 工种档案 Controller
 *
 * @ClassName: WorkclassAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-09 16:21
 */
@CheckPermission(PermissionKey.WORKCLASS)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/workclass", viewPath = "/_view/admin/workclass")
public class WorkclassAdminController extends JBoltBaseController {

    @Inject
    private WorkClassService service;
//	@Inject
//	private PersonService personService;

    /**
     * 首页
     */
    public void index() {
        System.out.println("getControllerPath===>" + getControllerPath());
        System.out.println("getViewPath===>" + getViewPath());
        render("index.html");
    }

    /**
     * 数据源
     */
    public void datas() {
        renderJsonData(service.pageList(getKv()));
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
        Workclass workclass = service.findById(getLong(0));
        if (workclass == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("workclass", workclass);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(Workclass.class, "workclass")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(Workclass.class, "workclass")));
    }

    /**
     * 批量删除
     */
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

    /*
     * 导出选中的数据
     * */
    public void exportExcelByIds() {
        String ids = get("ids");
        if (notOk(ids)) {
            renderJsonFail("未选择有效数据，无法导出");
            return;
        }
        List<Record> recordList = service.list(Kv.create().setIfNotBlank("ids", Util.getInSqlByIds(ids)));
        if (notOk(recordList)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        //2、创建JBoltExcel
        JBoltExcel jBoltExcel = service.exportExcelTpl(service.getListByIds(ids));
        //3、导出
        renderBytesToExcelXlsFile(jBoltExcel);
		/*for(Record r:data){
//			r.put("ilevel",personService.formatPattenSn(r.getStr("ilevel"),"work_level"));
		}
		try {
			renderJxls("工种档案导出模板.xls", Kv.by("rows", data), "工种档案(选中导出)_" + DateUtil.today() + ".xlsx");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
    }

    /*
     * 导出所有数据
     * */
    public void exportExcelAll() {
//		List<Record> datas = service.list(getKv());
        List<Workclass> datas = service.findAll();
        if (notOk(datas)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        //2、创建JBoltExcel
        JBoltExcel jBoltExcel = service.exportExcelTpl(datas);
        //3、导出
        renderBytesToExcelXlsFile(jBoltExcel);
		/*for(Record r:rows){
//			r.put("ilevel",personService.formatPattenSn(r.getStr("ilevel"),"work_level"));
		}
		try {
			renderJxls("工种档案导出模板.xlsx", Kv.by("rows", rows), "工种档案_" + DateUtil.today() + ".xlsx");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
    }

    @SuppressWarnings("unchecked")
    public void downloadTpl() throws Exception {
//		renderJxls("workclass_import.xlsx", Kv.by("rows", null), "工种档案导入模板.xlsx");
        renderBytesToExcelXlsFile(service.getExcelImportTpl().setFileName("工种档案导入模板"));
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

    public void options() {
        renderJsonData(service.options());
    }

}
