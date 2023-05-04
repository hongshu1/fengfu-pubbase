package cn.rjtech.admin.instockqcformm;

import java.util.List;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.extend.config.ExtendUploadFolder;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InStockQcFormM;

import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Record;

/**
 * 在库检 Controller
 *
 * @ClassName: InStockQcFormMAdminController
 * @author: RJ
 * @date: 2023-04-25 15:00
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
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
    public void edit() {
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

}
