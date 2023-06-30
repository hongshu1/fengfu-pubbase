package cn.rjtech.admin.syspureceive;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.sysenumeration.SysEnumerationService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SysPureceive;
import cn.rjtech.model.momdata.SysPureceivedetail;
import cn.rjtech.model.momdata.Warehouse;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 采购收料单
 *
 * @ClassName: SysPureceiveAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-10 10:01
 */
@CheckPermission(PermissionKey.MATERIALRECEIPTLIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/materialReceiptList", viewPath = "/_view/admin/sysPureceive")
public class SysPureceiveAdminController extends BaseAdminController {

    @Inject
    private SysPureceiveService service;
    @Inject
    private WarehouseService warehouseservice;
    @Inject
    private SysEnumerationService sysenumerationservice;
    @Inject
    private SysPureceivedetailService syspureceivedetailservice;

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
        renderJson(service.save(getModel(SysPureceive.class, "sysPureceive")));
    }

    /**
     * 编辑
     */
    public void edit(@Para(value = "autoid") String autoid) {
        SysPureceive sysPureceive = service.findById(autoid);
        if (sysPureceive == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }

        // 关联查询出仓库查其名称
        SysPureceivedetail pureceivedetail = syspureceivedetailservice.findFirstByMasId(sysPureceive.getAutoID());
        if (ObjUtil.isNotNull(pureceivedetail) && StrUtil.isNotBlank(pureceivedetail.getWhcode())) {
            Warehouse warehouse = warehouseservice.findByCwhcode(pureceivedetail.getWhcode());
            if (ObjUtil.isNotNull(warehouse)) {
                set("whname", warehouse.getCWhName());
            }
            set("whname", warehouse.getCWhName());
        }

        // 关联查询用户名字
        if (null != sysPureceive.getIcreateby()) {
            set("username", sysenumerationservice.getUserName(sysPureceive.getIcreateby()));
        }
        set("edit", Optional.ofNullable(getBoolean("edit")).orElse(false));
        set("sysPureceive", sysPureceive);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysPureceive.class, "sysPureceive")));
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
    public void submitAll() {
        renderJson(service.submitAll(getJBoltTable(), JBoltUserKit.getUser()));
    }

    /**
     * 供应商数据源
     */
    public void venCode() {
        renderJsonData(service.getVenCodeDatas(getKv()));
    }

    /**
     * 仓库数据源
     */
    public void Whcode() {
        renderJsonData(service.getWhcodeDatas(getKv()));
    }

    /**
     * 入库类别
     */
    @UnCheck
    public void selectRdCode() {
        renderJsonData(service.selectRdCode(getKv()));
    }

    /**
     * 库区数据源
     */
    public void wareHousepos() {
        String whcode = get("whcode1");

        Kv kv = getKv();

        if (StrUtil.isNotBlank(whcode)) {
            Warehouse warehouse = warehouseservice.findByCwhcode(whcode);
            if (ObjUtil.isNotNull(warehouse)) {
                kv.set("whcodeid", warehouse.getIAutoId());
            }
        }

        renderJsonData(service.getwareHousepos(kv));
    }

    /**
     * 条码数据源
     */
    @UnCheck
    public void barcodeDatas() {
        String whcode1 = get("whcode1");
        ValidationUtils.notNull(whcode1, "请优先选择仓库。");
        Record whcodeid = service.getWhcodeid(whcode1);
        String iautoid = whcodeid.getStr("iautoid");
//        System.out.println("iautoid"+iautoid);
        List<Record> barcodeDatas = service.getBarcodeDatas(get("q"), getInt("limit", 10), get("orgCode", getOrgCode()), null,whcode1);
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
            for (int i = 0; i < barcodeDatas.size(); i++) {
                Record record = barcodeDatas.get(i);
                if(!iautoid.equals(record.getStr("iwarehouseid"))){
                    record.set("poscode",null);
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
        String whcode1 = get("whcode1");
        ValidationUtils.notNull(whcode1, "请优先选择仓库。");
        Record whcodeid = service.getWhcodeid(whcode1);
        String iautoid = whcodeid.getStr("iautoid");
        Record barcodeDatas = service.barcode(Kv.by("barcode", barcode));
        if(null != barcode &&  !"".equals(barcode)){
            if(!iautoid.equals(barcodeDatas.getStr("iwarehouseid"))){
                barcodeDatas.set("poscode",null);

            }
        }
        renderJsonData(barcodeDatas);
    }

    /**
     * 提审批
     */
    public void submit(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.validateId(iautoid, "id");

        renderJson(service.submit(iautoid));
    }

    /**
     * 撤回已提审批
     */
    public void withdraw(Long iAutoId) {
        ValidationUtils.validateId(iAutoId, "iAutoId");

         renderJson(service.withdraw(iAutoId));
    }

    /**
     * 审核通过
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
     * 反审批
     */
    public void reverseApprove(String ids) {
        if (StrUtil.isBlank(ids)) {
            renderFail(JBoltMsg.PARAM_ERROR);
            return;
        }
        renderJson(service.reverseApprove(ids));
    }

    /**
     * 批量审核通过（批量审批也走这里）
     */
    public void batchApprove(@Para(value = "ids") String ids) {
        if (StrUtil.isBlank(ids)) {
            renderFail(JBoltMsg.PARAM_ERROR);
            return;
        }
        renderJson(service.process(ids));
    }


    /**
     * 批量反审核
     */
    public void batchReverseApprove(@Para(value = "ids") String ids) {
        if (StrUtil.isBlank(ids)) {
            renderFail(JBoltMsg.PARAM_ERROR);
            return;
        }
        renderJson(service.noProcess(ids));
    }
    /**
     * 批量审批不通过
     */
    public void batchReject(@Para(value = "ids") String ids) {
        if (StrUtil.isBlank(ids)) {
            renderFail(JBoltMsg.PARAM_ERROR);
            return;
        }
        renderJson(service.batchReject(ids));
    }

}
