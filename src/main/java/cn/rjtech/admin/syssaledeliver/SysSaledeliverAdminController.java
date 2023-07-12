package cn.rjtech.admin.syssaledeliver;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.util.JBoltCamelCaseUtil;
import cn.rjtech.admin.syssaledeliverdetail.SysSaledeliverdetailService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SysSaledeliver;
import cn.rjtech.model.momdata.SysSaledeliverdetail;
import cn.rjtech.util.Util;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.IRow;
import com.jfinal.plugin.activerecord.Record;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 销售出库
 * @ClassName: SysSaledeliverAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 13:05
 */
@CheckPermission(PermissionKey.SALES_DELIVERY_LIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/salesDeliveryList", viewPath = "/_view/admin/sysSaledeliver")
public class SysSaledeliverAdminController extends BaseAdminController {

    @Inject
    private SysSaledeliverService service;

    @Inject
    private SysSaledeliverdetailService syssaledeliverdetailservice;

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
        //获取参数
        Kv kv = getKv();
        String sortColumn = getSortColumn("createDate");
        String sortType = getSortType("desc");
        kv.setIfNotNull("sortColumn", sortColumn);
        kv.setIfNotNull("sortType", sortType);
        String billNo = StrUtil.trim(kv.getStr("BillNo"));
        String cCusCode = StrUtil.trim(kv.getStr("cCusCode"));
        String whName = StrUtil.trim(kv.getStr("whName"));
        kv.setIfNotNull("billNo",billNo);
        kv.setIfNotNull("cCusCode",cCusCode);
        kv.setIfNotNull("whName",whName);
        setDefaultSortInfo(sortColumn, sortType);
        renderJsonData(service.getAdminDatas(kv));
    }

    /**
     * 执行导出excel 根据查询form表单
     */
    public void exportExcelByForm() {
        //获取参数
        Kv kv = getKv();
        String billNo = StrUtil.trim(kv.getStr("BillNo"));
        String cCusCode = StrUtil.trim(kv.getStr("cCusCode"));
        String whName = StrUtil.trim(kv.getStr("whName"));
        kv.setIfNotNull("billNo",billNo);
        kv.setIfNotNull("cCusCode",cCusCode);
        kv.setIfNotNull("whName",whName);
        List<Record> rows = service.getAdminDataOfRecord(kv);
        if (notOk(rows)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderBytesToExcelXlsxFile(service.exportExcel(rows).setFileName("销售出库单列表_" + DateUtil.today()));
    }

    /**
     * 执行导出excel 根据表格选中数据
     */
    public void exportExcelByCheckedIds() {
        String ids = get("ids");
        if(notOk(ids)){
            renderJsonFail("未选择有效数据，无法导出");
            return;
        }
        List<Record> rows = service.getAdminDataOfRecord(Kv.create().setIfNotBlank("ids", Util.getInSqlByIds(ids)));
        if(notOk(rows)){
            renderJsonFail("无有效数据导出");
            return;
        }
        renderBytesToExcelXlsxFile(service.exportExcel(rows).setFileName("销售出库单列表_" + DateUtil.today()));
    }

    /**
     * 获取行数据
     */
    @UnCheck
    public void getLineData() {
        Long masId = useIfPresent(getLong("sysSaledeliver.masId",1L));
        if (notOk(masId)){
            renderJsonFail(JBoltMsg.PARAM_ERROR);
            return;
        }
        Kv kv = new Kv();
        kv.set("masId", masId);
        renderJsonData(service.getLineData(getPageNumber(),getPageSize(),kv));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.SALES_DELIVERY_LIST_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(SysSaledeliver.class, "sysSaledeliver")));
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.SALES_DELIVERY_LIST_EDIT)
    public void edit() {
        SysSaledeliver sysSaledeliver = service.findById(getLong(0));
        if (sysSaledeliver == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        IRow<Record> formDatas = service.getFormDatas(sysSaledeliver.getAutoID());
        set("sysSaledeliver", formDatas);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysSaledeliver.class, "sysSaledeliver")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.SALES_DELIVERY_LIST_DELETE)
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.SALES_DELIVERY_LIST_DELETE)
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    public void pushu(){
        Kv kv = new Kv();
        kv.set("ids",get("ids"));
        List<SysSaledeliver> getpushu = service.getpushu(kv);
        if(CollUtil.isEmpty(getpushu)){
            return ;
        }
        getpushu.stream().forEach(s -> {
            Kv kv1 = new Kv();
            kv1.set("id",s.getAutoID());
            List<SysSaledeliverdetail> getpushudetail1 = syssaledeliverdetailservice.getpushudetail(kv1);
//            System.out.println("开始u8，开始u8，开始u8，开始u8，开始u8"+new Date());
            Ret ret = service.pushU8(s, getpushudetail1);
            renderJson(ret);
//            System.out.println(ret);
//            System.out.println("结束u8，结束u8，结束u8，结束u8，结束u8"+new Date());
        });
    }

    /***
     * 勾选导出
     */
    @CheckPermission(PermissionKey.SALES_DELIVERY_LIST_EXPORT)
    public void downloadChecked(){
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

        String sqlTemplate = "sysSaleDeliver.pageList";
        List<Record> list = service.download(kv, sqlTemplate);
        JBoltCamelCaseUtil.keyToCamelCase(list);
        //2、创建JBoltExcel
        JBoltExcel jBoltExcel = JBoltExcel
                .create()//创建JBoltExcel
                .addSheet(//设置sheet
                        JBoltExcelSheet.create("销售出库单列表")
                                .setHeaders(1,//sheet里添加表头
                                        JBoltExcelHeader.create("sourcebill", "来源单号", 20),
                                        JBoltExcelHeader.create("billno", "出库单号", 20),
                                        JBoltExcelHeader.create("erpbillno", "ERP单据号", 20),
                                        JBoltExcelHeader.create("billdate", "出库日期", 20),
                                        JBoltExcelHeader.create("invcode", "存货编码", 20),
                                        JBoltExcelHeader.create("whname", "仓库名称", 40),
                                        JBoltExcelHeader.create("rdcode", "出库类别", 40),
                                        JBoltExcelHeader.create("billtype", "业务类型", 40),
                                        JBoltExcelHeader.create("salenum", "业务号", 40),
                                        JBoltExcelHeader.create("depname", "销售部门", 40),
                                        JBoltExcelHeader.create("salename", "业务员", 40),
                                        JBoltExcelHeader.create("cusname", "客户简称", 40),
                                        JBoltExcelHeader.create("auditdate", "审批日期", 40),
                                        JBoltExcelHeader.create("memo", "备注", 40),
                                        JBoltExcelHeader.create("createperson", "创建人", 40),
                                        JBoltExcelHeader.create("createdate", "创建日期", 40)
                                ).setDataChangeHandler((data, index) -> {
                        })
                                .setRecordDatas(2, list)//设置数据
                ).setFileName("销售出库单列表-" + new SimpleDateFormat("yyyyMMdd").format(new Date()));
        //3、导出
        renderBytesToExcelXlsFile(jBoltExcel);

    }


}
