package cn.rjtech.admin.stockoutqcformm;

import cn.hutool.core.date.DateUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.admin.stockoutqcformd.StockoutQcFormDService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.StockoutQcFormD;
import cn.rjtech.model.momdata.StockoutQcFormM;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 质量管理-出库检
 *
 * @ClassName: StockoutQcFormMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-12 19:18
 */
@CheckPermission(PermissionKey.STOCKOUTQCFORMM)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/stockoutqcformm", viewPath = "/_view/admin/stockoutqcformm")
public class StockoutQcFormMAdminController extends BaseAdminController {

    @Inject
    private StockoutQcFormMService service;
    @Inject
    private RcvDocQcFormMService   rcvDocQcFormMService;  //质量管理-来料检验表
    @Inject
    private StockoutQcFormDService stockoutQcFormDService;

    /**
     * 首页
     */
    public void index() {
        render("index.html");
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
        renderJson(service.save(getModel(StockoutQcFormM.class, "stockoutQcFormM")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(StockoutQcFormM.class, "stockoutQcFormM")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    /**
     * 数据源
     */
    public void datas() {
        renderJsonData(service.pageList(getKv()));
    }

    /*
     * 生成
     */
    public void createTable(@Para(value = "iautoid") Long iautoid) {
        renderJson(service.createTable(iautoid));
    }

    /**
     * 跳转到检验页面
     */
    public void checkout() {
        Record record = service.getCheckoutListByIautoId(getLong(0));
        if (record == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        //判断是否要先生成从表数据
        service.checkAutoCreateStockoutQcFormD(record.getLong("iautoid"));
        // 表头项目
        List tableHeadData = rcvDocQcFormMService.getTableHeadData(record.getLong("iqcformid"));
        set("columns", tableHeadData);
        set("record", record);
        render("checkout.html");
    }

    /**
     * 打开onlysee页面
     */
    public void onlysee() {
        Record record = service.getCheckoutListByIautoId(getLong(0));
        if (record == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        List<StockoutQcFormD> stockoutqcformlist = stockoutQcFormDService.findByIStockoutQcFormMid(getLong(0));
        // 表头项目
        List tableHeadData = rcvDocQcFormMService.getTableHeadData(record.getLong("iqcformid"));
        set("columns", tableHeadData);
        set("stockoutqcformlist", stockoutqcformlist);
        set("record", record);
        render("onlysee.html");
    }

    /**
     * 编辑
     */
    public void edit() {
        Record record = service.getCheckoutListByIautoId(getLong(0));
        if (record == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        List<StockoutQcFormD> stockoutqcformlist = stockoutQcFormDService.findByIStockoutQcFormMid(getLong(0));
        // 表头项目
        List tableHeadData = rcvDocQcFormMService.getTableHeadData(record.getLong("iqcformid"));
        set("stockoutqcformlist", stockoutqcformlist);
        set("record", record);
        render("editstockoutqcformmTable.html");
    }

    /*详情页面的table数据*/
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

    /**
     * 切换isCpkSigned
     */
    public void toggleIsCpkSigned() {
        renderJson(service.toggleBoolean(getLong(0), "isCpkSigned"));
    }

    /*
     * 导出详情页
     * */
    public void exportExcel() throws Exception{
        Kv kv = service.getExportData(getLong(0));//instaockqcformm
        renderJxls("stockoutqcformm.xlsx", kv, "出货检_" + DateUtil.today() + "_成绩表.xlsx");
    }
}
