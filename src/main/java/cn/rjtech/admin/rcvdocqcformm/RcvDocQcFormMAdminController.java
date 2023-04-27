package cn.rjtech.admin.rcvdocqcformm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jfinal.aop.Inject;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.util.JBoltRealUrlUtil;
import cn.jbolt.extend.config.ExtendUploadFolder;
import cn.rjtech.admin.rcvdocqcformd.RcvDocQcFormDService;
import cn.rjtech.admin.rcvdocqcformdline.RcvdocqcformdLineService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;

import com.jfinal.core.JFinal;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;

import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;

import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.RcvDocQcFormD;
import cn.rjtech.model.momdata.RcvDocQcFormM;

/**
 * 质量管理-来料检
 *
 * @ClassName: RcvDocQcFormMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-11 11:44
 */
@CheckPermission(PermissionKey.RCVDOCQCFORMM)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/rcvdocqcformm", viewPath = "/_view/admin/rcvdocqcformm")
public class RcvDocQcFormMAdminController extends BaseAdminController {

    @Inject
    private RcvDocQcFormMService     service;              //质量管理-来料检验表
    @Inject
    private RcvDocQcFormDService     rcvDocQcFormDService; //质量管理-来料检单行配置表
    @Inject
    private RcvdocqcformdLineService rcvdocqcformdLineService; //质量管理-来料检明细列值表

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
        Page<Record> recordPage = service.pageList(getKv());
        renderJsonData(recordPage);
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
        renderJson(service.save(getModel(RcvDocQcFormM.class, "rcvDocQcFormM")));
    }

    /**
     * 编辑
     */
    public void edit() {
        RcvDocQcFormM rcvDocQcFormM = service.findById(getLong(0));
        if (rcvDocQcFormM == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("rcvdocqcformm", rcvDocQcFormM);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(RcvDocQcFormM.class, "rcvDocQcFormM")));
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
     * 切换isCompleted
     */
    public void toggleIsCompleted() {
        renderJson(service.toggleBoolean(getLong(0), "isCompleted"));
    }

    /**
     * 切换isCpkSigned
     */
    public void toggleIsCpkSigned() {
        renderJson(service.toggleBoolean(getLong(0), "isCpkSigned"));
    }

    /**
     * 切换isOk
     */
    public void toggleIsOk() {
        renderJson(service.toggleBoolean(getLong(0), "isOk"));
    }

    /**
     * 导入图片
     */
    public void uploadImage() {
        String uploadPath = JBoltUploadFolder.todayFolder(ExtendUploadFolder.EXTEND_ITEMMASTER_EDITOR_IMAGE + "/inventory" + "/");
        renderJsonData(service.uploadImage(getFiles(ExtendUploadFolder.EXTEND_ITEMMASTER_EDITOR_IMAGE + "/inventory" + "/")));
    }

    /**
     * 跳转到检验页面
     */
    public void checkout() {
        RcvDocQcFormM rcvDocQcFormM = service.findById(getLong(0));
        Record record = service.getCheckoutListByIautoId(rcvDocQcFormM.getIAutoId());
        set("rcvdocqcformm", rcvDocQcFormM);
        set("record", record);
        render("checkout.html");
    }

    /*
     * 点击检验时，进入弹窗自动加载table的数据
     * */
    public void getCheckOutTableDatas() {
        renderJsonData(service.getCheckOutTableDatas(getKv()));
    }

    /**
     * 打开onlysee页面
     */
    public void onlysee() {
        RcvDocQcFormM rcvDocQcFormM = service.findById(getLong(0));
        Record record = service.getCheckoutListByIautoId(rcvDocQcFormM.getIAutoId());
        List<Record> docparamlist = service.getonlyseelistByiautoid(rcvDocQcFormM.getIAutoId());
        set("docparamlist", docparamlist);
        set("rcvdocqcformm", rcvDocQcFormM);
        set("record", record);
        render("onlysee.html");
    }

    /**
     * 打开编辑页面
     */
    public void editrcvDocQcFormD() {
        RcvDocQcFormM rcvDocQcFormM = service.findById(getLong(0));
        Record record = service.getCheckoutListByIautoId(rcvDocQcFormM.getIAutoId());
        List<Record> docparamlist = service.getonlyseelistByiautoid(rcvDocQcFormM.getIAutoId());
        set("docparamlist", docparamlist);
        set("rcvdocqcformm", rcvDocQcFormM);
        set("record", record);
        render("editTable.html");
    }

    /**
     * 点击查看时，进入弹窗自动加载table的数据
     */
    public void getonlyseeDatas() {
        renderJsonData(service.getonlyseelistByiautoid(getKv()));
    }

    /*
     * 在编辑页面点击确定
     * */
    public void editTable(JBoltPara JboltPara) {
        renderJson(service.editTable(JboltPara));
    }

    /*
     * 在检验页面点击确定
     * */
    public void editCheckOutTable(JBoltPara JboltPara) {
        renderJson(service.editCheckOutTable(JboltPara));
    }

    /*
     * 生成
     * */
    public void createTable(@Para(value = "iautoid") Long iautoid,
                            @Para(value = "cqcformname") String cqcformname) {
        renderJson(service.createTable(iautoid,cqcformname));
    }
}
