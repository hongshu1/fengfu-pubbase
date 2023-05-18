package cn.rjtech.admin.syssaledeliver;

import cn.hutool.core.date.DateUtil;
import cn.rjtech.util.Util;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.SysSaledeliver;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 销售出库
 * @ClassName: SysSaledeliverAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 13:05
 */
@CheckPermission(PermissionKey.SALES_DELIVERY_LIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/salesDeliveryList", viewPath = "/_view/admin/sysSaledeliver")
public class SysSaledeliverAdminController extends BaseAdminController {

    @Inject
    private SysSaledeliverService service;

    /**
     * 首页
     */
    public void index() {
        render("index().html");
    }

    /**
     * 数据源
     */
    public void datas() {
        //获取参数
        Kv kv = getKv();
        String sortColumn = getSortColumn("createDate");
        String sortType = getSortType("desc");
        kv.setIfNotNull("sortColumn", sortColumn);
        kv.setIfNotNull("sortType", sortType);
        String billNo = StringUtils.trim(kv.getStr("BillNo"));
        String cCusCode = StringUtils.trim(kv.getStr("cCusCode"));
        String whName = StringUtils.trim(kv.getStr("whName"));
        kv.setIfNotNull("billNo",billNo);
        kv.setIfNotNull("cCusCode",cCusCode);
        kv.setIfNotNull("whName",whName);
        setDefaultSortInfo(sortColumn, sortType);
        renderJsonData(service.getAdminDatas(kv));
    }

    /**
     * 执行导出excel 根据查询form表单
     */
    public void exportExcelByForm() {
        //获取参数
        Kv kv = getKv();
        String billNo = StringUtils.trim(kv.getStr("BillNo"));
        String cCusCode = StringUtils.trim(kv.getStr("cCusCode"));
        String whName = StringUtils.trim(kv.getStr("whName"));
        kv.setIfNotNull("billNo",billNo);
        kv.setIfNotNull("cCusCode",cCusCode);
        kv.setIfNotNull("whName",whName);
        List<Record> rows = service.getAdminDataOfRecord(kv);
        if (notOk(rows)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderBytesToExcelXlsxFile(service.exportExcel(rows).setFileName("销售出库单列表_" + DateUtil.today()));
    }

    /**
     * 执行导出excel 根据表格选中数据
     */
    public void exportExcelByCheckedIds() {
        String ids = get("ids");
        if(notOk(ids)){
            renderJsonFail("未选择有效数据，无法导出");
            return;
        }
        List<Record> rows = service.getAdminDataOfRecord(Kv.create().setIfNotBlank("ids", Util.getInSqlByIds(ids)));
        if(notOk(rows)){
            renderJsonFail("无有效数据导出");
            return;
        }
        renderBytesToExcelXlsxFile(service.exportExcel(rows).setFileName("销售出库单列表_" + DateUtil.today()));
    }

    /**
     * 获取行数据
     */
    public void getLineData() {
        Long masId = useIfPresent(getLong("sysSaledeliver.masId",1L));
        if (notOk(masId)){
            renderJsonFail(JBoltMsg.PARAM_ERROR);
            return;
        }
        Kv kv = new Kv();
        kv.set("masId", masId);
        renderJsonData(service.getLineData(getPageNumber(),getPageSize(),kv));
    }

    /**
     * 新增
     */
    public void add() {
        render("add().html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(SysSaledeliver.class, "sysSaledeliver")));
    }

    /**
     * 编辑
     */
    public void edit() {
        SysSaledeliver sysSaledeliver = service.findById(getLong(0));
        if (sysSaledeliver == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("sysSaledeliver", sysSaledeliver);
        render("edit().html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysSaledeliver.class, "sysSaledeliver")));
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


}
