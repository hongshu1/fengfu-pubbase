package cn.rjtech.admin.rcvdocqcformm;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.rcvdocqcformd.RcvDocQcFormDService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.RcvDocQcFormD;
import cn.rjtech.model.momdata.RcvDocQcFormM;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

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
    private RcvDocQcFormMService service;              //质量管理-来料检验表
    @Inject
    private RcvDocQcFormDService rcvDocQcFormDService; //质量管理-来料检单行配置表

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
     * 数据源
     */
    public void datas() {
        renderJsonData(service.pageList(getKv()));
    }

    /**
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
        service.checkAutoCreateRcvDocQcFormD(record.getLong("iautoid"));
        // 表头项目
        List tableHeadData = service.getTableHeadData(record.getLong("iqcformid"));
        set("columns", tableHeadData);
        if (StrUtil.isBlank(record.getStr("cmeasurereason"))) {
            record.set("cmeasurereason", record.getStr("cmeasure"));
        }
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
        List<RcvDocQcFormD> docparamlist = rcvDocQcFormDService.findByIRcvDocQcFormMId(record.get("iautoid"));
        // 表头项目
        List tableHeadData = service.getTableHeadData(record.getLong("iqcformid"));
        set("columns", tableHeadData);
        set("docparamlist", docparamlist);
        set("record", record);
        render("onlysee.html");
    }

    /**
     * 打开编辑页面
     */
    public void editrcvDocQcFormD() {
        Record record = service.getCheckoutListByIautoId(getLong(0));
        if (record == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        //List<Record> docparamlist = service.getonlyseelistByiautoid(rcvDocQcFormM.getIAutoId());
        List<RcvDocQcFormD> docparamlist = rcvDocQcFormDService.findByIRcvDocQcFormMId(record.get("iautoid"));
        // 表头项目
        List tableHeadData = service.getTableHeadData(record.getLong("iqcformid"));
        set("columns", tableHeadData);
        set("docparamlist", docparamlist);
        set("record", record);
        render("editTable.html");
    }

    /**
     * 进入子页面自动加载table的数据
     */
    @UnCheck
    public void getCheckOutTableDatas() {
        renderJsonData(service.getCheckOutTableDatas(getKv()));
    }

    /**
     * 在检验页面点击确定
     */
    public void saveCheckOutTable(JBoltPara JboltPara) {
        renderJson(service.saveCheckOutTable(JboltPara));
    }

    /**
     * 在编辑页面点击确定
     */
    public void saveEditTable(JBoltPara JboltPara) {
        renderJson(service.saveEditTable(JboltPara));
    }

    /**
     * 切换isCompleted
     */
    public void toggleIsCompleted() {
        renderJson(service.toggleBoolean(getLong(0), "isCompleted"));
    }

    /**
     * 切换isOk
     */
    public void toggleIsOk() {
        renderJson(service.toggleBoolean(getLong(0), "isOk"));
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
    public void exportExcel() throws Exception {
        Kv kv = service.getExportData(getLong(0));//instaockqcformm
        renderJxls("rcvdocqcformm.xlsx", kv, "来料检_" + DateUtil.today() + "_成绩表.xlsx");
    }

}
