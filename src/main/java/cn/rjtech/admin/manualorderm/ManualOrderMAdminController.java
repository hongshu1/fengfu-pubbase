package cn.rjtech.admin.manualorderm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JBoltDateRange;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.admin.cusordersum.CusOrderSumService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.ManualOrderStatusEnum;
import cn.rjtech.model.momdata.ManualOrderM;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;

/**
 * 客户订单-手配订单主表
 *
 * @ClassName: ManualOrderMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 15:18
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.MANUAL_ORDER)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/manualorderm", viewPath = "/_view/admin/manualorderm")
public class ManualOrderMAdminController extends BaseAdminController {

    @Inject
    private ManualOrderMService service;
    @Inject
    private CusOrderSumService cusOrderSumService;

    /**
     * 首页
     */
    public void index() {
        render("index.html");
    }

    /**
     * 数据源
     */
    @UnCheck
    public void datas() {
        JBoltDateRange dateRange = getDateRange();
        Date startTime = dateRange.getStartDate();
        Date endTime = dateRange.getEndDate();
        Kv kv = getKv();
        kv.set("starttime", startTime);
        kv.set("endtime", endTime);
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), kv));
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
        JBoltTable jBoltTable = getJBoltTable();
        ManualOrderM manualOrderM = jBoltTable.getFormBean(ManualOrderM.class, "manualOrderM");
        renderJson(service.saveForm(manualOrderM, jBoltTable));
    }

    /**
     * 编辑
     */
    public void edit() {
        Page<Record> datas = service.getAdminDatas(1, 1, Kv.by("iAutoId", getLong(0)));
        ValidationUtils.notNull(datas, JBoltMsg.DATA_NOT_EXIST);
        ValidationUtils.isTrue(datas.getList().size() > 0, JBoltMsg.DATA_NOT_EXIST);
        set("manualOrderM", datas.getList().get(0));
        render("edit.html");
    }

    /**
     * 审核
     */
    public void audit() {
        ManualOrderM manualOrderM = service.findById(getLong("id"));
        if (manualOrderM == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        
        manualOrderM.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
        manualOrderM.setIOrderStatus(ManualOrderStatusEnum.AUDITTED.getValue());
        Ret stockoutQcFormM = service.createStockoutQcFormM(manualOrderM.getICustomerId(), manualOrderM.getIAutoId());
        
        cusOrderSumService.algorithmSum();
        
        if (stockoutQcFormM.isFail())
            renderJson(stockoutQcFormM);
        else
            renderJson(service.update(manualOrderM));
    }

    /**
     * 撤回
     */
    public void reply() {
        ManualOrderM manualOrderM = service.findById(getLong(0));
        if (manualOrderM == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        manualOrderM.setIOrderStatus(1);
        renderJson(service.update(manualOrderM));
    }

    /**
     * 关闭
     */
    public void colse() {
        ManualOrderM manualOrderM = service.findById(getLong(0));
        if (manualOrderM == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        manualOrderM.setIOrderStatus(6);
        renderJson(service.update(manualOrderM));
    }

    /**
     * 查看
     */
    public void info() {
        Page<Record> datas = service.getAdminDatas(1, 1, Kv.by("iAutoId", getLong(0)));
        ValidationUtils.notNull(datas, JBoltMsg.DATA_NOT_EXIST);
        ValidationUtils.isTrue(datas.getList().size() > 0, JBoltMsg.DATA_NOT_EXIST);
        set("manualOrderM", datas.getList().get(0));
        set("view", 1);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(ManualOrderM.class, "manualOrderM")));
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

    public void inventory_dialog_index() {
        render("inventory_dialog_index.html");
    }

    public void batchAudit() {
        renderJson(service.batchHandle(getKv(), 3, new int[]{2}));
    }

    public void batchAntiAudit() {
        renderJson(service.batchHandle(getKv(), 2, new int[]{3}));
    }

    public void batchClose() {
        renderJson(service.batchHandle(getKv(), 6, new int[]{3}));
    }

    public void batchDetect() {
        renderJson(service.deleteByIds(get("ids")));
       // renderJson(service.batchDetect(getKv()));
    }

    /**
     * 模板下载
     */
    @SuppressWarnings("unchecked")
    public void downloadTpl() {
        try {
            renderJxls("manualorderm.xlsx", Kv.by("rows", null), "手配订单.xlsx");
        }catch (Exception e)
        {
            ValidationUtils.isTrue(false, "模板下载失败");
        }
    }
}
