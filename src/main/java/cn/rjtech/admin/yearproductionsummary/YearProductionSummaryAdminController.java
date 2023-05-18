package cn.rjtech.admin.yearproductionsummary;

import cn.hutool.core.date.DateUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.rjtech.util.Util;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 年度生产计划汇总
 *
 * @author ：佛山市瑞杰科技有限公司
 * @description ：YearProductionSummaryAdminController
 * @date ：2023/3/20 14:36
 */
//@CheckPermission(PermissionKey.SUMMARY_OF_ANNUAL_PRODUCTION_PLAN)
@UnCheckIfSystemAdmin
@Path(value = "/admin/yearproductionsummary", viewPath = "/_view/admin/yearproductionsummary")
public class YearProductionSummaryAdminController extends JBoltBaseController {

    public void index() {
        set("layYear", JBoltDateUtil.getYear());
        render("index.html");
    }

    public void datas() {
        renderJson();
    }

    /**
     * 导出选中
     * */
    public void exportExcelByIds() {
//        String ids = get("ids");
//        if (notOk(ids)) {
//            renderJsonFail("未选择有效数据，无法导出");
//            return;
//        }
//        List<Record> data = service.list(Kv.create().setIfNotBlank("ids", Util.getInSqlByIds(ids)));
//        if (notOk(data)) {
//            renderJsonFail("无有效数据导出");
//            return;
//        }
//        for (Record r : data) {
//        }
//        try {
//            renderJxls("检验项目（分类）.xlsx", Kv.by("rows", data), "检验项目（分类）(选中导出)_" + DateUtil.today() + ".xlsx");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 导出全部
     * */
    public void exportExcelAll() {
//        List<Record> rows = service.list(getKv());
//        if (notOk(rows)) {
//            renderJsonFail("无有效数据导出");
//            return;
//        }
//        for (Record r : rows) {
//        }
//        try {
//            renderJxls("检验项目（分类）.xlsx", Kv.by("rows", rows), "检验项目（分类）" + DateUtil.today() + ".xlsx");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}