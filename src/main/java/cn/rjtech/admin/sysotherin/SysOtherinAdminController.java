package cn.rjtech.admin.sysotherin;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.rdstyle.RdStyleService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.RdStyle;
import cn.rjtech.model.momdata.SysOtherin;
import cn.rjtech.model.momdata.Vendor;
import cn.rjtech.model.momdata.Warehouse;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 其它入库单
 * @ClassName: SysOtherinAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-05 17:57
 */

@CheckPermission(PermissionKey.OTHER_IN_LIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/otherInList", viewPath = "/_view/admin/sysotherin")
public class SysOtherinAdminController extends BaseAdminController {

    @Inject
    private SysOtherinService service;

    @Inject
    private WarehouseService warehouseservice;

    @Inject
    private RdStyleService rdstyleservice;

    @Inject
    private VendorService vendorservice;

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
        renderJson(service.save(getModel(SysOtherin.class, "sysOtherin")));
    }

    /**
     * 编辑
     */
    public void edit() {
        SysOtherin sysOtherin = service.findById(getLong("autoid"));
        if (sysOtherin == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        // todo 待优化
        // 仓库
        Warehouse first = warehouseservice.findFirst("select *   from Bd_Warehouse where cWhCode=?", sysOtherin.getWhcode());
        if (null != first && null != first.getCWhName()) {
            set("cwhname", first.getCWhName());
        }
        // 入库类型
        RdStyle first1 = rdstyleservice.findFirst("select * from Bd_Rd_Style where crdcode=?", sysOtherin.getRdCode());
        if (null != first1 && null != first1.getCRdName()) {
            set("crdname", first1.getCRdName());
        }
        // 查供应商名称
        if (null != sysOtherin.getVenCode()) {
            Vendor first2 = vendorservice.findFirst("select * from Bd_Vendor where cVenCode = ?", sysOtherin.getVenCode());
            if (null != first2) {
                set("venname", first2.getCVenName());
            }
        }
        // 查业务类型名称
        if (null != sysOtherin.getBillType()) {
            String billtypeNmea = service.getBilltypeNmea(sysOtherin.getBillType());
            if (null != billtypeNmea) {
                set("cbtchname", billtypeNmea);
            }
        }


        Boolean edit = getBoolean("edit");
        set("edit", Optional.ofNullable(getBoolean("edit")).orElse(false));
        set("sysotherin", sysOtherin);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysOtherin.class, "sysOtherin")));
    }

    /**
     * 批量删除主从表
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
     * 新增-可编辑表格-批量提交
     */
    @Before(Tx.class)
    public void submitAll() {
        renderJson(service.submitByJBoltTable(getJBoltTable()));
    }

    /**
     * 查询业务类型类型
     */
    public void billtype() {
        renderJsonData(service.billtype(getKv()));
    }

    /**
     * 条码数据源
     */
    @UnCheck
    public void barcodeDatas() {
        List<Record> barcodeDatas = service.getBarcodeDatas(get("q"), getInt("limit", 10), get("orgCode", getOrgCode()));
        String barcode = get("detailHidden");
        if(null != barcode &&  !"".equals(barcode)){
            String[] split = barcode.split(",");
            for (int i = 0; i < split.length; i++) {
                String s = split[i].replaceAll("'", "");
                Iterator<Record> iterator = barcodeDatas.iterator();
                while (iterator.hasNext()) {
                    Record r = iterator.next();
                    if (r.getStr("barcode").equals(s)) {
                        iterator.remove();
                    }
                }
            }
        }

        renderJsonData(barcodeDatas);
    }

    /**
     * 根据条码带出其他数据
     */
    @UnCheck
    public void barcode(@Para(value = "barcode") String barcode) {
        ValidationUtils.notBlank(barcode, "请扫码");
        Record barcodeDatas = service.barcode(Kv.by("barcode", barcode));

        renderJsonData(barcodeDatas);
    }

}
