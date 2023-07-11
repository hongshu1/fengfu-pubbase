package cn.rjtech.admin.barcodereport.barcodetracepage;

import cn.jbolt._admin.hiprint.HiprintTplService;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.util.JBoltCamelCaseUtil;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 社内现品票汇总管理 Controller
 * @author Kephon
 */
@CheckPermission(PermissionKey.BARCODE_TRACEPAGE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/barcodeReport/BarcodeTracePage", viewPath = "/_view/admin/barcodereport/barcodetracepage")
public class BarcodeTracePageAdminController extends BaseAdminController {

    @Inject
    private BarcodeTracePageAdminService service;
    @Inject
    private HiprintTplService tplService;


    /**
     * 首页
     */
    public void index(){
        render("index.html");
    }

    /**
     *跳转详情页面
     */
    public void details(){
        Kv kv = getKv();
        //现品票
        set("barcode",kv.getStr("barcode"));
        //单号
        set("billno",kv.getStr("billno"));
        render("details.html");
    }


    /**
     * 社内现品票汇总管理数据源
     * */
    public void newdatas(){
        Kv kv = getKv();
        //现品票
        set("barcode",kv.getStr("barcode"));
        //单号
        set("billno",kv.getStr("billno"));
        String startTime =(String) kv.get("starttime");
        String endtime =(String) kv.get("endtime");
        renderJsonData(service.newdatas(getPageSize(),getPageNumber(),kv));
    }



    /**
     * 获取打印数据
     */
    @CheckPermission(PermissionKey.BARCODE_TRACEPAGE_PRINT)
    public void PrintData() {
        Kv kv = getKv();
        String ids = kv.getStr("ids");
        if (ids != null) {
            String[] split = ids.split(",");
            String sqlids = "";
            for (String id : split) {
                sqlids += "'" + id + "',";
            }
            ValidationUtils.isTrue(sqlids.length() > 0, "请至少选择一条数据!");
            sqlids = sqlids.substring(0, sqlids.length() - 1);
            kv.set("sqlids", sqlids);
        }
        renderJsonData(service.PrintData(getPageSize(),getPageNumber(),kv));
    }



    /***
     * 勾选导出
     */
    @CheckPermission(PermissionKey.BARCODE_TRACEPAGE_EXPORT)
    public void downloadChecked() throws Exception{
        Kv kv = getKv();
        String ids = kv.getStr("ids");
        if (ids != null) {
            String[] split = ids.split(",");
            String sqlids = "";
            for (String id : split) {
                sqlids += "'" + id + "',";
            }
            ValidationUtils.isTrue(sqlids.length() > 0, "请至少选择一条数据!");
            sqlids = sqlids.substring(0, sqlids.length() - 1);
            kv.set("sqlids", sqlids);
        }
        List<Record> records = service.getBarcodeTotalList(DataSourceConstants.U8,kv);
        JBoltCamelCaseUtil.keyToCamelCase(records);
        //2、创建JBoltExcel
        JBoltExcel jBoltExcel = JBoltExcel
                .create()//创建JBoltExcel
                .addSheet(//设置sheet
                        JBoltExcelSheet.create("社内现品票汇总管理")
                                .setHeaders(1,//sheet里添加表头
                                        JBoltExcelHeader.create("billno", "单据单号", 20),
                                        JBoltExcelHeader.create("vencode", "供应商编码", 20),
                                        JBoltExcelHeader.create("otherinvcode", "客户部番", 20),
                                        JBoltExcelHeader.create("otherinvname", "部品名称", 20),
                                        JBoltExcelHeader.create("invstd", "规格", 40),
                                        JBoltExcelHeader.create("poscode", "主记录单位编码", 40),
                                        JBoltExcelHeader.create("barcode", "现品票条码", 40),
                                        JBoltExcelHeader.create("version", "版本号", 40),
                                        JBoltExcelHeader.create("qty", "数量", 40),
                                        JBoltExcelHeader.create("ScheduleDate", "计划到货日期", 40),
                                        JBoltExcelHeader.create("whname", "仓库名称", 40),
                                        JBoltExcelHeader.create("posname", "库区名称", 40),
                                        JBoltExcelHeader.create("createdate", "创建日期", 40)
                                ).setDataChangeHandler((data, index) -> {
                        })
                                .setRecordDatas(2, records)//设置数据
                ).setFileName("社内现品票汇总管理-" + new SimpleDateFormat("yyyyMMdd").format(new Date()));
        //3、导出
        renderBytesToExcelXlsFile(jBoltExcel);

    }
}
