package cn.rjtech.admin.instockqcformm;

import cn.hutool.core.date.DateUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.instockqcformd.InStockQcFormDService;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InStockQcFormD;
import cn.rjtech.model.momdata.InStockQcFormM;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.List;

/**
 * 在库检 Controller
 *
 * @ClassName: InStockQcFormMAdminController
 * @author: RJ
 * @date: 2023-04-25 15:00
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.RCVDOCQCFORMM)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/instockqcformm", viewPath = "/_view/admin/instockqcformm")
public class InStockQcFormMAdminController extends BaseAdminController {

    @Inject
    private InStockQcFormMService service;
    @Inject
    private RcvDocQcFormMService  rcvDocQcFormMService;
    @Inject
    private InStockQcFormDService inStockQcFormDService;

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
        renderJsonData(service.pageList(getKv()));
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
    public void save() {
        renderJson(service.save(getModel(InStockQcFormM.class, "inStockQcFormM")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(InStockQcFormM.class, "inStockQcFormM")));
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
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 切换toggleIsCompleted
     */
    public void toggleIsCompleted() {
        renderJson(service.toggleIsCompleted(getLong(0)));
    }

    /**
     * 切换toggleIsOk
     */
    public void toggleIsOk() {
        renderJson(service.toggleIsOk(getLong(0)));
    }

    /**
     * 切换toggleIsCpkSigned
     */
    public void toggleIsCpkSigned() {
        renderJson(service.toggleIsCpkSigned(getLong(0)));
    }

    /**
     * 切换toggleIsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleIsDeleted(getLong(0)));
    }

    /*
     * 删除在库检查表
     * */
    public void deleteCheckoutByIautoid() {
        renderJson(service.deleteCheckoutByIautoid(getLong(0)));
    }

    /*
     * 生成
     * */
    public void createTable(@Para(value = "iautoid") Long iautoid) {
        renderJson(service.createTable(iautoid));
    }

    /*
     * 点击检验按钮，跳转到checkout页面
     * */
    public void jumpCheckOut() {
        Record record = service.getCheckoutListByIautoId(getLong(0));
        if (record == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        List tableHeadData = rcvDocQcFormMService.getTableHeadData(record.getLong("iqcformid"));
        set("columns", tableHeadData);
        set("record", record);
        render("checkout.html");
    }

    /**
     * 点击查看按钮，跳转到onlysee页面
     */
    public void jumpOnlySee() {
        Record record = service.getCheckoutListByIautoId(getLong(0));
        if (record == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        List tableHeadData = rcvDocQcFormMService.getTableHeadData(record.getLong("iqcformid"));
        List<InStockQcFormD> stockoutqcformlist = inStockQcFormDService.findByIInStockQcFormMid(getLong(0));
        set("record", record);
        set("columns", tableHeadData);
        set("stockoutqcformlist", stockoutqcformlist);
        render("onlysee.html");
    }

    /*
     * 点击编辑按钮，跳转到编辑页面
     * */
    public void jumpEdit() {
        Record record = service.getCheckoutListByIautoId(getLong(0));
        if (record == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        List<InStockQcFormD> stockoutqcformlist = inStockQcFormDService.findByIInStockQcFormMid(getLong(0));
        List tableHeadData = rcvDocQcFormMService.getTableHeadData(record.getLong("iqcformid"));
        set("record", record);
        set("stockoutqcformlist", stockoutqcformlist);
        set("columns", tableHeadData);
        render("editInStockOutQcFormMTable.html");
    }

    /**
     * 详情页面的table数据
     */
    @UnCheck
    public void getTableDatas() {
        renderJsonData(service.getTableDatas(getKv()));
    }

    /*
     * 在检验页面点击确定
     */
    public void saveCheckOutTable(JBoltPara JboltPara) {
        renderJson(service.saveCheckOutTable(JboltPara));
    }

    /*
     * 在编辑页面点击确定
     */
    public void saveEditTable(JBoltPara JboltPara) {
        renderJson(service.saveEditTable(JboltPara));
    }

    /*
     * 导出详情页
     * */
    public void exportExcel() throws Exception {
        Kv kv = service.getExportData(getLong(0));//instaockqcformm
        renderJxls("instockqcformm.xlsx", kv, "在库检_" + DateUtil.today() + "_成绩表.xlsx");
    }

    /**
     * 根据现品票查询数据
     */
    @UnCheck
    public void findDetailByBarcode(@Para(value = "cbarcode") String cbarcod) {
        renderJsonData(service.findDetailByBarcode(cbarcod));
    }

    /*
     * @desc 新增数据
     * @param cbarcode：现品票
     * qty：数量
     * invcode：存货编码
     * cinvcode1：客户部番
     * cinvname1：部品名称
     * */
    public void saveInStockQcFormByCbarcode() {
        String cbarcode = get("cbarcode");
        BigDecimal iqty = getBigDecimal("iqty");
        String invcode = get("invcode");
        String cinvcode1 = get("cinvcode1");
        String cinvname1 = get("cinvname1");
        String iinventoryid = get("iinventoryid");
        String cdcno  = get("cdcno");
        String cMeasureReason = get("cmeasurereason");
        String iqcformid = get("iqcformid");
        ValidationUtils.notBlank(cbarcode, "现品票不能为空");
        ValidationUtils.notBlank(invcode, "存货编码不能为空");
        ValidationUtils.notBlank(iinventoryid, "存货id不能为空");

        int qty = iqty.setScale(2,BigDecimal.ROUND_DOWN).intValue();
        renderJson(service.saveInStockQcFormByCbarcode(cbarcode, qty, invcode, cinvcode1, cinvname1,iinventoryid, cdcno,
            cMeasureReason,Long.valueOf(iqcformid)));
    }

}
