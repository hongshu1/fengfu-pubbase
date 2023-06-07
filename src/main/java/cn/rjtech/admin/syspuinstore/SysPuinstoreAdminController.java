package cn.rjtech.admin.syspuinstore;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SysPuinstore;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 采购入库单
 * @ClassName: SysPuinstoreAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 15:38
 */
@CheckPermission(PermissionKey.PURCHASERECEIPTLIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/purchaseReceiptList", viewPath = "/_view/admin/sysPuinstore")
public class SysPuinstoreAdminController extends BaseAdminController {

    @Inject
    private SysPuinstoreService service;

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
     * 新增
     */
    public void add() {
        render("add.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(SysPuinstore.class, "sysPuinstore")));
    }

    /**
     * 编辑
     */
    public void edit() {
        SysPuinstore sysPuinstore = service.findById(getLong(0));
        if (sysPuinstore == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("sysPuinstore", sysPuinstore);
        render("edit.html");
    }

    /*
     * 批量审批
     * */
    public void autitByIds() {
        renderJson(service.resetAutitByIds(get("ids")));
    }

    /*
     * 批量反审批
     * */
    public void resetAutitByIds() {
        renderJson(service.autitByIds(get("ids")));
    }

    /*
     * 反审批
     * */
    public void resetAutitById() {
        Kv kv = getKv();
        renderJson(service.resetAutitById(kv.getStr("autoid")));
    }

    /*
     * 审批
     * */
    public void autit() {
        Kv kv = getKv();
        Long aLong = getLong(0);
        Long autoid = null;
        if (kv.getLong("autoid") != null) {
            autoid = kv.getLong("autoid");
        } else if (aLong != null) {
            autoid = aLong;
        }
        renderJson(service.autit(autoid));
    }

    /*
     * 查看
     * */
    public void onlysee() {
        SysPuinstore sysPuinstore = service.findById(getLong(0));
        if (sysPuinstore == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("sysPuinstore", sysPuinstore);
        render("onlysee.html");
    }

    /*
     * 打印
     * */
    public void printSysPuinstore() {
        //TODO 待定
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysPuinstore.class, "sysPuinstore")));
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
     * 切换IsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleBoolean(getLong(0), "IsDeleted"));
    }

    /**
     * 新增-可编辑表格-批量提交
     */
    @Before(Tx.class)
    public void submitAll() {
        renderJson(service.submitByJBoltTable(getJBoltTable()));
    }

    /**
     * 订单号的选择数据Dialog
     */
    public void chooseSysPuinstoreData() {
        render("sysPuinstoreDialog.html");
    }

    /*
     * 获取采购订单视图的订单号
     * */
    public void getSysPODetail() {
        Page<Record> recordPage = service.getSysPODetail(getKv(), getPageNumber(), getPageSize());
        renderJsonData(recordPage);
    }

    /*
     * 获取采购订单视图MES的订单号
     * */
    public void getMesSysPODetails() {
        Page<Record> recordPage = service.getMesSysPODetails(getKv(), getPageNumber(), getPageSize());
        renderJsonData(recordPage);
    }

    /*
     * 获取仓库名
     * */
    public void getWareHouseName() {
        renderJson(service.getWareHouseName(getKv()));
    }
}
