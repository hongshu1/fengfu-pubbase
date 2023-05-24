package cn.rjtech.admin.stockcheckvouch;

import java.util.List;
import java.util.stream.Collectors;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Person;
import cn.rjtech.model.momdata.StockCheckVouch;
import cn.rjtech.model.momdata.Workclass;
import cn.rjtech.util.Util;
import cn.rjtech.wms.utils.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

/**
 * 盘点单
 *
 * @ClassName: StockCheckVouchAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-21 10:47
 */
@CheckPermission(PermissionKey.STOCKCHECKVUCH)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/stockcheckvouch", viewPath = "/_view/admin/stockcheckvouch")
public class StockCheckVouchAdminController extends BaseAdminController {

    @Inject
    private StockCheckVouchService service;
    @Inject
    private PersonService          personService;

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
//        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("CheckType")));
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
        renderJson(service.save(getModel(StockCheckVouch.class, "stockCheckVouch")));
    }

    /*
     * 完成盘点
     * */
    public void saveSubmit() {
        renderJson(service.saveSubmit(getKv()));
    }

    /*
     * 保存记录
     * */
    public void saveCheckVouchDetails(JBoltPara jBoltPara) {
        JBoltPara jBoltPara1 = jBoltPara;
        Kv kv = getKv();
        renderJson(service.saveCheckVouchDetails(getKv()));
    }

    /*
     * 新增
     * */
    public void addFormSave() {
        renderJson(service.addFormSave(getKv()));
    }

    /**
     * 编辑
     */
    public void edit() {
        StockCheckVouch stockCheckVouch = service.findById(getLong(0));
        if (stockCheckVouch == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("stockCheckVouch", stockCheckVouch);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(StockCheckVouch.class, "stockCheckVouch")));
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

    /*
     * 继续盘点
     * */
    public void keepCheckVouch() {
        StockCheckVouch stockCheckVouch = service.findById(getLong(0));
        if (stockCheckVouch == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("stockcheckvouch", stockCheckVouch);
        render("stockEdit.html");
    }

    /*
     * 加载继续盘点页面的数据
     * */
    public void keepCheckVouchDatas() {
        renderJsonData(service.keepCheckVouchDatas(getKv()));
    }

    /*
     * 加载下半段table的数据
     * */
    public void findDetailVouchDatas() {
        renderJsonData(service.findDetailVouchDatas(getKv()));
    }

    /*
     * 继续盘点
     * */
    public void onlysee() {
        StockCheckVouch stockCheckVouch = service.findById(getLong(0));
        List<Record> records = service.findThreeParamsByAutoid(Kv.by("autoid", getLong(0)));
        if (stockCheckVouch == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("stockcheckvouch", stockCheckVouch);
        set("cwhname", records.get(0).getStr("cwhname"));
        String careaname = "";
        for (Record record : records) {
            careaname = record.getStr("careaname") + ",";
        }
        Person person = personService.findById(stockCheckVouch.getCheckPerson());
        if (person != null) {
            set("cpsn_name", person.getCpsnName());
        }
        if (StringUtils.isNotBlank(careaname)) {
            set("careaname", careaname.substring(0, careaname.length() - 1));
        }
        render("onlysee.html");
    }

    /*
     * 跳转到审核页面
     * */
    public void audit() {
        StockCheckVouch stockCheckVouch = service.findById(getLong(0));
        if (stockCheckVouch == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("stockcheckvouch", stockCheckVouch);
        render("audit.html");
    }

    /*
     * 驳回
     * */
    public void reject() {
        renderJson(service.reject(getKv()));
    }

    /*
     * 审核通过
     * */
    public void approveAudit() {
        renderJson(service.approveAudit(getKv()));
    }

    /*
     * 导出选中
     * */
    public void exportExcelByIds() {
        String ids = get("ids");
        if (notOk(ids)) {
            renderJsonFail("未选择有效数据，无法导出");
            return;
        }
        List<Record> datas = service.list(Kv.create().setIfNotBlank("ids", Util.getInSqlByIds(ids)));
        if (notOk(datas)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        JBoltExcel jBoltExcel = service.exportExcel(datas);
        renderBytesToExcelXlsFile(jBoltExcel);
    }

    /*
     * 导出全部
     * */
    public void exportExcelAll() {
        //1、查询所有要导出的数据
        List<Record> datas = service.list(getKv());
        if (notOk(datas)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        //2、创建JBoltExcel
        JBoltExcel jBoltExcel = service.exportExcel(datas);
        //3、导出
        renderBytesToExcelXlsFile(jBoltExcel);
    }

}
