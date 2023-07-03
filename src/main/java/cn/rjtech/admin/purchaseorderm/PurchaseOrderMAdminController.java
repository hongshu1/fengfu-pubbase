package cn.rjtech.admin.purchaseorderm;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.hiprint.HiprintTplService;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.demandplanm.DemandPlanMService;
import cn.rjtech.admin.exch.ExchService;
import cn.rjtech.admin.foreigncurrency.ForeignCurrencyService;
import cn.rjtech.admin.inventorychange.InventoryChangeService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.admin.purchaseorderdbatch.PurchaseOrderDBatchService;
import cn.rjtech.admin.purchaseorderdbatchversion.PurchaseOrderDBatchVersionService;
import cn.rjtech.admin.purchasetype.PurchaseTypeService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.admin.vendoraddr.VendorAddrService;
import cn.rjtech.annotations.RequestLimit;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.enums.SourceTypeEnum;
import cn.rjtech.model.momdata.Exch;
import cn.rjtech.model.momdata.Person;
import cn.rjtech.model.momdata.PurchaseOrderM;
import cn.rjtech.model.momdata.Vendor;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.List;


/**
 * 采购/委外订单-采购订单主表
 *
 * @ClassName: PurchaseOrderMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-12 15:19
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.NOME)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/purchaseorderm", viewPath = "/_view/admin/purchaseorderm")
public class PurchaseOrderMAdminController extends BaseAdminController {

    @Inject
    private PurchaseOrderMService service;
    @Inject
    private ForeignCurrencyService foreignCurrencyService;
    @Inject
    private PurchaseTypeService purchaseTypeService;
    @Inject
    private VendorService vendorService;
    @Inject
    private DemandPlanMService demandPlanMService;
    @Inject
    private PersonService personService;
    @Inject
    private VendorAddrService vendorAddrService;
    @Inject
    private PurchaseOrderDBatchService purchaseOrderDBatchService;
    @Inject
    private PurchaseOrderDBatchVersionService purchaseOrderDBatchVersionService;
    @Inject
    private HiprintTplService tplService;

    @Inject
    private InventoryChangeService inventoryChangeService;
    @Inject
    private ExchService exchService;


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
    public void add(@Para(value = "beginDate") String beginDate,
                    @Para(value = "endDate") String endDate,
                    @Para(value = "iVendorId") String iVendorId,
                    @Para(value = "processType") Integer processType,
                    @Para(value = "iSourceType") Integer iSourceType) {

        Vendor vendor = vendorService.findById(iVendorId);
        ValidationUtils.notNull(vendor, "供应商记录不存在");
        Record record = new Record();
        setAttrs(service.getDateMap(beginDate, endDate, iVendorId, processType, iSourceType));
        record.set(PurchaseOrderM.ITYPE, iSourceType);
        record.set(PurchaseOrderM.IVENDORID, vendor.getIAutoId());
        record.set(PurchaseOrderM.DBEGINDATE, beginDate);
        record.set(PurchaseOrderM.DENDDATE, endDate);

        if (ObjUtil.isNotNull(vendor.getITaxRate())) {
            record.set(PurchaseOrderM.ITAXRATE, vendor.getITaxRate().stripTrailingZeros().stripTrailingZeros());
        }

        record.set(PurchaseOrderM.CCURRENCY, vendor.getCCurrency());
        Exch exch = exchService.getNameByLatestExch(getOrgId(), vendor.getCCurrency());
        // 汇率
        if (ObjUtil.isNotNull(exch)) {
            record.set(PurchaseOrderM.IEXCHANGERATE, exch.getNflat());
        }

        record.set(PurchaseOrderM.IDUTYUSERID, vendor.getIDutyPersonId());
        Person person = personService.findById(vendor.getIDutyPersonId());
        if (ObjUtil.isNotNull(person)) {
            set("personname", person.getCpsnName());
        }
        record.set(PurchaseOrderM.IDEPARTMENTID, vendor.getCVenDepart());
        // 带出供应商下的业务员，币种，税率
        set("purchaseOrderM", record);

        set(Vendor.CVENNAME, vendor.getCVenName());
        if (SourceTypeEnum.BLANK_PURCHASE_TYPE.getValue() == iSourceType) {
            render("blank_add.html");
            return;
        }
        render("add.html");
    }

    public void checkData(@Para(value = "beginDate") String beginDate,
                          @Para(value = "endDate") String endDate,
                          @Para(value = "iVendorId") String iVendorId,
                          @Para(value = "processType") Integer processType,
                          @Para(value = "iSourceType") Integer iSourceType) {
        ValidationUtils.notBlank(beginDate, "请选择日期范围");
        ValidationUtils.notBlank(endDate, "请选择日期范围");
        ValidationUtils.notBlank(iVendorId, "请选择供应商");

        ValidationUtils.notNull(iSourceType, "缺少来源类型");
        SourceTypeEnum sourceTypeEnum = SourceTypeEnum.toEnum(iSourceType);
        ValidationUtils.notNull(sourceTypeEnum, "未知来源类型");
        if (SourceTypeEnum.MATERIAL_PLAN_TYPE.getValue() == iSourceType) {
            List<Record> list = demandPlanMService.getVendorDateList(beginDate, endDate, iVendorId, processType);
            ValidationUtils.notEmpty(list, "该时间范围未找到该供应商所需求物料");
        }
        ok();
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(PurchaseOrderM.class, "purchaseOrderM")));
    }

    /**
     * 编辑 isView: 判断是否只有查看功能
     */
    public void edit(@Para(value = "isView") String isView) {
        PurchaseOrderM purchaseOrderM = service.findById(getLong(0));
        if (purchaseOrderM == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        Person person = personService.findById(purchaseOrderM.getIDutyUserId());
        if (ObjUtil.isNotNull(person)) {
            set("personname", person.getCpsnName());
        }

        Vendor vendor = vendorService.findById(purchaseOrderM.getIVendorId());
        ValidationUtils.notNull(vendor, "未找到供应商数据");
        set(Vendor.CVENNAME, vendor.getCVenName());
        if (StrUtil.isNotBlank(isView)) {
            set("isView", 1);
        }
        if (ObjUtil.isNotNull(purchaseOrderM.getITaxRate())) {
            purchaseOrderM.setITaxRate(purchaseOrderM.getITaxRate().stripTrailingZeros());
        }
        if (ObjUtil.isNotNull(purchaseOrderM.getIExchangeRate())) {
            purchaseOrderM.setIExchangeRate(purchaseOrderM.getIExchangeRate().stripTrailingZeros());
        }
        set("purchaseOrderM", purchaseOrderM);
        setAttrs(service.getDateMap(purchaseOrderM));
        if (SourceTypeEnum.BLANK_PURCHASE_TYPE.getValue() == purchaseOrderM.getIType()) {
            render("blank_edit.html");
            return;
        }
        render("edit.html");
    }

    public void cash() {
        PurchaseOrderM purchaseOrderM = service.findById(getLong(0));
        if (purchaseOrderM == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        Person person = personService.findById(purchaseOrderM.getIDutyUserId());
        if (ObjUtil.isNotNull(person)) {
            set("personname", person.getCpsnName());
        }

        Vendor vendor = vendorService.findById(purchaseOrderM.getIVendorId());
        ValidationUtils.notNull(vendor, "未找到供应商数据");
        set(Vendor.CVENNAME, vendor.getCVenName());
        set("purchaseOrderM", purchaseOrderM);
        render("cash.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(PurchaseOrderM.class, "purchaseOrderM")));
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
     * 新增作成页面
     */
    public void consummate() {
        keepPara();
        render("consummate.html");
    }

    @UnCheck
    public void findForeignCurrencyAll() {
        renderJsonData(foreignCurrencyService.findAll(getKv()));
    }

    @UnCheck
    public void findPurchaseType() {
        renderJsonData(purchaseTypeService.selectAll(getPageNumber(),getPageSize(),getKv()));
    }

    @UnCheck
    public void findByiVendorId(@Para(value = "vendorId") String vendorId,
                                @Para(value = "id") String id) {
        renderJsonData(vendorAddrService.findList(getKv()));
    }

    /**
     * 保存
     */
    public void submit(@Para(value = "tableData") String dataStr,
                       @Para(value = "formData") String formStr,
                       @Para(value = "invTableData") String invTableData,
                       @Para(value = "type") String type) {
        renderJson(service.submit(dataStr, formStr, invTableData, type, getKv()));
    }

    public void operationalState(@Para(value = "id") Long id,
                                 @Para(value = "type") Integer type) {
        renderJsonData(service.operationalState(id, type));
    }

    /**
     * 删除操作
     */
    public void del() {
        renderJsonData(service.del(getLong(0)));
    }

    /**
     * 撤回操作
     */
    public void withdraw() {
        renderJsonData(service.withdraw(getLong(0)));
    }

    /**
     * 提审接口
     */
    public void arraignment() {
        renderJsonData(service.arraignment(getLong(0)));
    }

    /**
     * 关闭
     */
    public void close() {
        renderJsonData(service.close(getLong(0)));
    }

    /**
     * 生成现成票
     */
    public void generateCash() {
        renderJsonData(service.generateCash(getLong(0)));
    }

    /**
     * 审核接口
     */
    public void audit() {
        renderJsonData(service.audit(getLong(0)));
    }

    public void batchGenerateCash(@Para(value = "ids") String ids) {
        renderJsonData(service.batchGenerateCash(ids));
    }

    public void batchDel(@Para(value = "ids") String ids) {
        renderJsonData(service.batchDel(ids));
    }

    @UnCheck
    public void findPurchaseOrderDBatch() {
        renderJsonData(purchaseOrderDBatchService.findByPurchaseOrderMId(getPageNumber(), getPageSize(), getKv()));
    }

    public void updateHideInvalid(@Para(value = "id") Long id,
                                  @Para(value = "hideInvalid") String hideInvalid) {
        renderJsonData(service.updateHideInvalid(id, Boolean.valueOf(hideInvalid)));
    }

    @RequestLimit(time = 15, count = 1)
    public void updateOrderBatch(@Para(value = "purchaseOrderMId") Long purchaseOrderMId,
                                 @Para(value = "id") Long id,
                                 @Para(value = "cVersion") String cVersion,
                                 @Para(value = "qty") BigDecimal qty) {
        renderJsonData(purchaseOrderDBatchService.updateOrderBatch(purchaseOrderMId, id, cVersion, qty));
    }

    @UnCheck
    public void findPurchaseOrderDBatchVersion() {
        renderJsonData(purchaseOrderDBatchVersionService.findByPurchaseOrderMid(getPageNumber(), getPageSize(), getKv()));
    }

    public void saveSubmit() {
        renderJson(service.saveSubmit(getJBoltTable()));
    }

    @UnCheck
    public void findPurchaseOrderD(@Para(value = "purchaseOrderMId") Long purchaseOrderMId) {
        renderJsonData(service.findPurchaseOrderD(purchaseOrderMId));
    }

    public void inventory_dialog_index() {
        keepPara();
        render("inventory_dialog_index.html");
    }

    /**
     * 默认给1-100个数据
     */
    public void inventoryPage() {
        renderJsonData(inventoryChangeService.inventoryAutocomplete(getPageNumber(), getPageSize(), getKv()));
    }

    @UnCheck
    public void findPrintPurchaseorderm() {
//          获取模板
//        HiprintTpl hiprintTpl= tplService.getCacheByKey("109607");
//        hiprintTpl.getContent();
//        set("hiprintTpl",hiprintTpl.getContent());
//         Kv kv = getKv();
        Page<Record> byPurchaseOrderMId = purchaseOrderDBatchService.findByPurchaseOrderMId(getPageNumber(), getPageSize(), getKv());
        renderJsonData(byPurchaseOrderMId.getList().get(1));
    }

    public void pushPurchase(@Para(value = "iautoid") Long iautoid) {
        renderJsonData(service.pushPurchase(iautoid));
    }

    /**
     * 现品票导出PDF
     */
    @SuppressWarnings("unchecked")
    public void purchaseordermOne(@Para(value = "iautoid") Long iautoid,@Para(value = "page") Integer page,
    @Para(value = "type") Integer type) throws Exception {
        Kv data = service.pageOnePdf(iautoid, page,type);
        String Version="";
        if(type==0){
            Version="purchaseOrderDBatch.xlsx";
        }else {
            Version="purchaseOrderDBatchcVersion.xlsx";
        }
        renderJxlsToPdf(Version, data, String.format("订货清单_%s.pdf", DateUtil.today()));
    }
}


