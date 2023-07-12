package cn.rjtech.admin.syssaledeliverplan;

import cn.hutool.core.util.ObjUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SysSaledeliverplan;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;


/**
 * 销售出货(计划)
 *
 * @ClassName: SysSaledeliverplanAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 10:01
 */
@CheckPermission(PermissionKey.SYSSALEDELIVERPLAN)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/salesShipmentList", viewPath = "/_view/admin/syssaledeliverplan")
public class SysSaledeliverplanAdminController extends BaseAdminController {

    @Inject
    private SysSaledeliverplanService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 新增-跳转到弹窗确认页面
     */
    public void addConfirm() {
        render("addConfirm.html");
    }

    /**
     * 跳转到订单页面
     */
    public void saleDeliverBillnoDialog() {
        Kv kv = getKv();
        Long icustomerid = kv.getLong("icustomerid");
        ValidationUtils.isTrue(icustomerid != null, "请先选择客户");
        set("icustomerid", icustomerid);
        render("saleDeliverBillnoDialog.html");
    }

    /*
     * 获取相关订单号
     * */
    public void saleDeliverBillNoList() {
        renderJsonData(service.getSaleDeliverBillNoList(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 新增
     */
    public void add() {
        Kv kv = getKv();
        Record record = new Record();
        record.set("icustomerid", kv.get("icustomerid"));//客户id
        record.set("customerccuscode", kv.get("customerccuscode"));//客户编码
        record.set("starttime", kv.get("starttime"));
        record.set("endtime", kv.get("endtime"));
        record.set("ccusabbname", kv.get("ccusabbname"));//客户简称
        record.set("sourcebillid", kv.get("sourcebillid"));//订单表主键id
        record.set("corderno", kv.get("corderno"));//订单号
        set("syssaledeliverplan", record);
        render("add.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(SysSaledeliverplan.class, "sysSaledeliverplan")));
    }

    /**
     * 编辑
     */
    public void edit() {
        SysSaledeliverplan sysSaledeliverplan = service.findById(getLong("autoid"));
        if (sysSaledeliverplan == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        Record record = service.findRecordByAutoid(Kv.by("autoid", getLong("autoid")));
        if (ObjUtil.equal(record.getStr("sourcebilltype"), "手工新增")) {
            Record corderReocrd = service.findCOrderNoBySourceBillId(Kv.by("iautoid", record.get("sourcebillid")));
            record.set("corderno", corderReocrd.getStr("corderno"));
        }
        set("syssaledeliverplan", record);
        keepPara();
        render("edit.html");
    }

    public void findTableDatas() {
        renderJsonData(service.findTableDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 新增-可编辑表格-批量提交
     */
    public void submitAll() {
        renderJson(service.submitByJBoltTable(getJBoltTable()));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysSaledeliverplan.class, "sysSaledeliverplan")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteRmRdByIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 获取销售类型下拉  /admin/saletype/selectData
     */

    /**
     * 获取地址下拉 Bd_CustomerAddr
     */
    public void customeraddr() {
        renderJsonData(service.customeraddr(getKv()));
    }

    /**
     * 获取发运方式下拉
     */
    public void rdstyle() {
        renderJsonData(service.rdstyle(getKv()));
    }

    /**
     * 获取付款条件下拉
     */
    public void settlestyle() {
        renderJsonData(service.settlestyle(getKv()));
    }

    /**
     * 获取币种下拉
     */
    public void foreigncurrency() {
        renderJsonData(service.foreigncurrency(getKv()));
    }

    /*
     * 根据barcode加载数据
     * */
    public void getBarcodeDatas() {
        List<Record> recordList = service.getBarcodeDatas(get("q"), getKv());
        renderJsonData(recordList);
    }

    public void scanBarcode() {
        List<Record> recordList = service.scanBarcode(getKv());
        renderJsonData(recordList);
    }

    /*
     * 根据invcode加载数据
     * */
    public void getDatasByInvcode() {
        List<Record> recordList = service.getDatasByInvcode(get("q"), getKv());
        renderJsonData(recordList);
    }
}
