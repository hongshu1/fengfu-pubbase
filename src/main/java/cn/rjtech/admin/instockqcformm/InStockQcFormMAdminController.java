package cn.rjtech.admin.instockqcformm;

import cn.hutool.core.date.DateUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.extend.config.ExtendUploadFolder;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InStockQcFormM;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 在库检 Controller
 *
 * @ClassName: InStockQcFormMAdminController
 * @author: RJ
 * @date: 2023-04-25 15:00
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.NONE)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/instockqcformm", viewPath = "/_view/admin/instockqcformm")
public class InStockQcFormMAdminController extends BaseAdminController {

    @Inject
    private InStockQcFormMService service;

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
     * 生成
     * */
    public void createTable(@Para(value = "iautoid") Long iautoid,
                            @Para(value = "cqcformname") String cqcformname) {
        renderJson(service.createTable(iautoid, cqcformname));
    }

    /*
     * 点击检验按钮，跳转到checkout页面
     * */
    public void jumpCheckOut() {
        InStockQcFormM inStockQcFormM = service.findById(getLong(0));
        Record record = service.getCheckoutListByIautoId(inStockQcFormM.getIAutoId());
        set("instockqcformm", inStockQcFormM);
        set("record", record);
        render("checkout.html");
    }

    /*
     * 点击检验按钮，跳转到checkout页面后，自动加载table的数据
     * */
    public void autoGetCheckOutTableDatas() {
        renderJsonData(service.getCheckOutTableDatas(getKv()));
    }

    /*
     * 在检验页面点击确定
     */
    public void saveCheckOutTable(JBoltPara JboltPara) {
        renderJson(service.saveCheckOutTable(JboltPara));
    }

    /*
     * 删除在库检查表
     * */
    public void deleteCheckoutByIautoid() {
        renderJson(service.deleteCheckoutByIautoid(getLong(0)));
    }

    /**
     * 点击查看按钮，跳转到onlysee页面
     */
    public void jumpOnlySee() {
        InStockQcFormM inStockQcFormM = service.findById(getLong(0));
        Record record = service.getCheckoutListByIautoId(inStockQcFormM.getIAutoId());
        List<Record> stockoutqcformlist = service.getonlyseelistByiautoid(inStockQcFormM.getIAutoId());
        set("stockoutqcformlist", stockoutqcformlist);
        set("instockqcformm", inStockQcFormM);
        set("record", record);
        render("onlysee.html");
    }

    /*
     * 点击查看按钮，跳转到onlysee页面后，自动加载table的数据
     * */
    public void autoGetOnlySeeDatas() {
        renderJsonData(service.getonlyseelistByiautoid(getKv()));
    }

    /*
     * 点击编辑按钮，跳转到编辑页面
     * */
    public void jumpEdit() {
        InStockQcFormM inStockQcFormM = service.findById(getLong(0));
        Record record = service.getCheckoutListByIautoId(inStockQcFormM.getIAutoId());
        List<Record> stockoutqcformlist = service.getonlyseelistByiautoid(inStockQcFormM.getIAutoId());
        set("instockqcformm", inStockQcFormM);
        set("record", record);
        set("stockoutqcformlist", stockoutqcformlist);
        render("editInStockOutQcFormMTable.html");
    }

    /*
     * 在编辑页面点击确定
     */
    public void saveEditTable(JBoltPara JboltPara) {
        renderJson(service.saveEditTable(JboltPara));
    }

    /**
     * 导入图片
     */
    public void uploadImage() {
        String uploadPath = JBoltUploadFolder.todayFolder(ExtendUploadFolder.EXTEND_ITEMMASTER_EDITOR_IMAGE + "/inventory" + "/");
        renderJsonData(service.uploadImage(getFiles(ExtendUploadFolder.EXTEND_ITEMMASTER_EDITOR_IMAGE + "/inventory" + "/")));
    }

    /*
     * 导出详情页
     * */
    public void exportExcel() throws Exception {
        Kv kv = service.getExportData(getLong(0));//instaockqcformm
        renderJxls("instaockqcformm.xlsx", kv, "在库检_" + DateUtil.today() + "_成绩表.xlsx");
    }

    /*
     * @desc 扫描现品票，点击“确定”按钮，表体增加1行在库检任务；如果此存货没有配置检验项目，
     *       需维护相关设置后点击“生成”按钮，生成检查成绩表。
     * @param cbarcode：现品票
     * */
    public void createInStockQcFormByCbarcode(){
        Kv kv = getKv();
        renderJson(service.createInStockQcFormByCbarcode(kv.getStr("cbarcode")));
    }

}
