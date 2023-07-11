package cn.rjtech.admin.sysproductin;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.rdstyle.RdStyleService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Department;
import cn.rjtech.model.momdata.RdStyle;
import cn.rjtech.model.momdata.SysProductin;
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
 * 产成品入库单
 * @ClassName: SysProductinAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-08 09:56
 */
@CheckPermission(PermissionKey.PRODUCTINLIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/productInList", viewPath = "/_view/admin/sysProductin")
public class SysProductinAdminController extends BaseAdminController {

    @Inject
    private SysProductinService service;

    @Inject
    private WarehouseService warehouseservice;

    @Inject
    private RdStyleService rdstyleservice;

    @Inject
    private DepartmentService departmentservice;

    @Inject
    private UserService userService;

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
    @CheckPermission(PermissionKey.PRODUCTIN_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(SysProductin.class, "sysProductin")));
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.PRODUCTIN_EDIT)
    public void edit(@Para(value = "autoid") String autoid) {
        SysProductin sysProductin = service.findById(autoid);
        if (sysProductin == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        // todo 待优化
        // 仓库
        Warehouse first = warehouseservice.findFirst("select *   from Bd_Warehouse where cWhCode=?", sysProductin.getWhcode());
        if (null != first && null != first.getCWhName()) {
            set("cwhname", first.getCWhName());
        }
        // 入库类型
        RdStyle first1 = rdstyleservice.findFirst("select * from Bd_Rd_Style where crdcode=?", sysProductin.getRdCode());
        if (null != first1 && null != first1.getCRdName()) {
            set("crdname", first1.getCRdName());
        }
        // 生产部门
        Department first2 = departmentservice.findFirst("select * from Bd_Department where cDepCode=?", sysProductin.getDeptCode());
        if (null != first2 && null != first2.getCDepName()) {
            set("cdepname", first2.getCDepName());
        }
        set("edit", Optional.ofNullable(getBoolean("edit")).orElse(false));
        set("sysProductin", sysProductin);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysProductin.class, "sysProductin")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.PRODUCTIN_DELETE_ALL)
    public void deleteByIds() {
        renderJson(service.deleteRmRdByIds(get("ids")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.PRODUCTIN_DELETE)
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
     * 获取仓库下拉
     */
    @UnCheck
    public void getWarehouse() {
        renderJsonData(service.getwareHouseDatas(getKv()));
    }

    /**
     * 获取入库类别下拉
     */
    @UnCheck
    public void getRdStyle() {
        renderJsonData(service.getRdStyleDatas(getKv()));
    }

    /**
     * 获取生产部门下拉
     */
    @UnCheck
    public void getDepartment() {
        renderJsonData(service.getDepartmentDatas(getKv()));
    }


    /**
     * 撤回已提审
     */
    public void withdraw(Long iAutoId) {
        ValidationUtils.validateId(iAutoId, "iAutoId");

        // renderJson(service.withdraw(iAutoId));
    }

    /**
     * 审批通过
     */
    public void approve(String ids) {
        renderJson(service.approve(getLong(0)));

    }

    /**
     * 审批不通过
     */
    public void reject(String ids) {
        renderJson(service.reject(getLong(0)));
    }




    /**
     * 条码数据源
     */
    @UnCheck
    public void barcodeDatas() {
//        String whcode1 = get("whcode1");
//        ValidationUtils.notNull(whcode1, "请优先选择仓库。");
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
//        String whcode1 = get("whcode1");
//        ValidationUtils.notNull(whcode1, "请优先选择仓库。");
        Record barcodeDatas = service.barcode(Kv.by("barcode", barcode));

        renderJsonData(barcodeDatas);
    }

}
