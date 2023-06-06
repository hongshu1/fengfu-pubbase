package cn.rjtech.admin.sysproductin;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
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
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.apache.commons.lang3.StringUtils;

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
    public void edit() {
        SysProductin sysProductin = service.findById(getLong(0));
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
        // 制单人
        Record record = service.selectName(sysProductin.getCreatePerson());
        if (null != record && null != record.get("name")) {
            set("name", record.get("name"));
        }
        // 审核人
        Record record1 = service.selectName(sysProductin.getAuditPerson());
        if (null != record1 && null != record1.get("name")) {
            set("sname", record1.get("name"));
        }
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
     * 获取仓库下拉
     */
    public void getWarehouse() {
        renderJsonData(service.getwareHouseDatas(getKv()));
    }

    /**
     * 获取入库类别下拉
     */
    public void getRdStyle() {
        renderJsonData(service.getRdStyleDatas(getKv()));
    }

    /**
     * 获取生产部门下拉
     */
    public void getDepartment() {
        renderJsonData(service.getDepartmentDatas(getKv()));
    }


    /**
     * 提审
     */
    public void submit(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.validateId(iautoid, "id");

        renderJson(service.submit(iautoid));
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
        ValidationUtils.notBlank(ids, JBoltMsg.PARAM_ERROR);

        // renderJson(service.approve(ids));
    }

    /**
     * 审批不通过
     */
    public void reject(String ids) {
        if (StringUtils.isEmpty(ids)) {
            renderFail(JBoltMsg.PARAM_ERROR);
            return;
        }
        // renderJson(service.reject(ids))
    }


}
