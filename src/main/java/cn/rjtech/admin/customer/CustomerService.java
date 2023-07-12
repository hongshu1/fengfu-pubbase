package cn.rjtech.admin.customer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.customeraddr.CustomerAddrService;
import cn.rjtech.admin.customerworkdays.CustomerWorkDaysService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.Customer;
import cn.rjtech.model.momdata.CustomerAddr;
import cn.rjtech.model.momdata.CustomerWorkDays;
import cn.rjtech.model.momdata.Vendor;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 往来单位-客户档案
 *
 * @ClassName: CustomerService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-21 15:10
 */
public class CustomerService extends BaseService<Customer> {

    private final Customer dao = new Customer().dao();

    @Override
    protected Customer dao() {
        return dao;
    }

    @Inject
    private VendorService vendorService;
    @Inject
    private CustomerAddrService addrService;
    @Inject
    private CustomerWorkDaysService workDaysService;

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理分页查询
     */
    public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("customer.paginateAdminDatas", kv.set("iorgid", getOrgId())).paginate(pageNumber, pageSize);
        //return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    public List<Record> getAdminDatas(Kv kv) {
        return dbTemplate("customer.paginateAdminDatas", kv.set("orgId", getOrgId())).find();
        //return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 保存
     */
    public Ret save(Customer customerm) {
        if (customerm == null || isOk(customerm.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(customerm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = customerm.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(customerm.getIautoid(), JBoltUserKit.getUserId(), customerm.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(Customer customerm) {
        if (customerm == null || notOk(customerm.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        Customer dbCustomerm = findById(customerm.getIAutoId());
        if (dbCustomerm == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(customerm.getName(), customerm.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = customerm.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(customerm.getIautoid(), JBoltUserKit.getUserId(), customerm.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        return deleteByIds(ids, true);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param customerm 要删除的model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Customer customerm, Kv kv) {
        //addDeleteSystemLog(customerm.getIautoid(), JBoltUserKit.getUserId(),customerm.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param customerm 要删除的model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(Customer customerm, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(customerm, kv);
    }

    /**
     * 切换isdeleted属性
     */
    public Ret toggleIsdeleted(Long id) {
        return toggleBoolean(id, "isDeleted");
    }

    /**
     * 切换isenabled属性
     */
    public Ret toggleIsenabled(Long id) {
        return toggleBoolean(id, "isEnabled");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param customerm 要toggle的model
     * @param column    操作的哪一列
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(Customer customerm, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(Customer customerm, String column, Kv kv) {
        //addUpdateSystemLog(customerm.getIautoid(), JBoltUserKit.getUserId(), customerm.getName(),"的字段["+column+"]值:"+customerm.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param customerm model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkInUse(Customer customerm, Kv kv) {
        //这里用来覆盖 检测Customerm是否被其它表引用
        return null;
    }

    public Customer findByCustomermCode(String customercode) {
        return findFirst(Okv.by("cCusCode", customercode).set("isDeleted", false), "iautoid", "asc");
    }

    /**
     * 多个可编辑表格同时提交
     */
    public Ret submitByJBoltTables(JBoltTableMulti jboltTableMulti) {
        if (jboltTableMulti == null || jboltTableMulti.isEmpty()) {
            return fail(JBoltMsg.JBOLTTABLE_IS_BLANK);
        }

        //这里可以循环遍历 保存处理每个表格 也可以 按照name自己get出来单独处理
//		jboltTableMulti.entrySet().forEach(en->{
//			JBoltTable jBoltTable = en.getValue();
        JBoltTable jBoltTable = jboltTableMulti.getJBoltTable("table1");
        JBoltTable jBoltTable2 = jboltTableMulti.getJBoltTable("table2");
        Date now = new Date();

        Customer customerm = jBoltTable.getFormModel(Customer.class, "customerm");
        ValidationUtils.notNull(customerm, JBoltMsg.PARAM_ERROR);

        tx(() -> {
            //修改
            if (isOk(customerm.getIAutoId())) {
                customerm.setIUpdateBy(JBoltUserKit.getUserId());
                customerm.setDUpdateTime(now);
                customerm.setCUpdateName(JBoltUserKit.getUserName());
                ValidationUtils.isTrue(customerm.update(), JBoltMsg.FAIL);
            } else {
                //新增
                //编码是否存在
                ValidationUtils.isTrue(findByCustomermCode(customerm.getCCCCode()) == null, "编码重复");
                customerm.setIAutoId(JBoltSnowflakeKit.me.nextId());
                customerm.setICreateBy(JBoltUserKit.getUserId());
                customerm.setCCreateName(JBoltUserKit.getUserName());
                customerm.setDCreateTime(now);
                customerm.setIUpdateBy(JBoltUserKit.getUserId());
                customerm.setCUpdateName(JBoltUserKit.getUserName());
                customerm.setDUpdateTime(now);
                customerm.setCOrgCode(getOrgCode());
                customerm.setCOrgName(getOrgName());
                customerm.setIOrgId(getOrgId());
                customerm.setISource(SourceEnum.MES.getValue());

                ValidationUtils.isTrue(customerm.save(), JBoltMsg.FAIL);
            }


            //新增
            List<CustomerAddr> saveRecords = jBoltTable.getSaveModelList(CustomerAddr.class);
            if (CollUtil.isNotEmpty(saveRecords)) {
                for (CustomerAddr row : saveRecords) {
                    row.setICustomerId(customerm.getIAutoId());

                }
                addrService.batchSave(saveRecords, 500);
            }

            List<CustomerWorkDays> saveModelList = jBoltTable2.getSaveModelList(CustomerWorkDays.class);
            if (CollUtil.isNotEmpty(saveModelList)) {
                for (CustomerWorkDays row : saveModelList) {
                    row.setICustomerId(customerm.getIAutoId());
                }
                workDaysService.batchSave(saveModelList, 500);
            }

            //修改
            List<CustomerAddr> updateRecords = jBoltTable.getUpdateModelList(CustomerAddr.class);
            if (CollUtil.isNotEmpty(updateRecords)) {
                addrService.batchUpdate(updateRecords, 500);
            }

            List<CustomerWorkDays> updateModelList = jBoltTable2.getUpdateModelList(CustomerWorkDays.class);
            if (CollUtil.isNotEmpty(updateModelList)) {
                workDaysService.batchUpdate(updateModelList, 500);
            }

            // 删除
            Object[] deletes = jBoltTable.getDelete();
            if (ArrayUtil.isNotEmpty(deletes)) {
                addrService.deleteMultiByIds(deletes);
            }

            Object[] deletes2 = jBoltTable2.getDelete();
            if (ArrayUtil.isNotEmpty(deletes2)) {
                workDaysService.deleteMultiByIds(deletes2);
            }

            return true;
        });

        return SUCCESS;
    }

    /**
     * 表格提交
     */
    public Ret updateEditTable(JBoltTable jBoltTable, Long userId, Date now) {
        Customer customerm = jBoltTable.getFormModel(Customer.class, "customerm");
        ValidationUtils.notNull(customerm, JBoltMsg.PARAM_ERROR);

        tx(() -> {
            //修改
            if (isOk(customerm.getIAutoId())) {
                customerm.setIUpdateBy(JBoltUserKit.getUserId());
                customerm.setDUpdateTime(now);
                customerm.setCUpdateName(JBoltUserKit.getUserName());
                ValidationUtils.isTrue(customerm.update(), JBoltMsg.FAIL);
            } else {
                //新增
                //编码是否存在
                ValidationUtils.isTrue(findByCustomermCode(customerm.getCCCCode()) == null, "编码重复");
                customerm.setIAutoId(JBoltSnowflakeKit.me.nextId());
                customerm.setICreateBy(JBoltUserKit.getUserId());
                customerm.setCCreateName(JBoltUserKit.getUserName());
                customerm.setDCreateTime(now);
                customerm.setIUpdateBy(JBoltUserKit.getUserId());
                customerm.setCUpdateName(JBoltUserKit.getUserName());
                customerm.setDUpdateTime(now);
                customerm.setCOrgCode(getOrgCode());
                customerm.setCOrgName(getOrgName());
                customerm.setIOrgId(getOrgId());

                ValidationUtils.isTrue(customerm.save(), JBoltMsg.FAIL);
            }

            // 新增
            List<CustomerAddr> saveRecords = jBoltTable.getSaveModelList(CustomerAddr.class);
            if (CollUtil.isNotEmpty(saveRecords)) {
                for (CustomerAddr row : saveRecords) {
                    row.setICustomerId(customerm.getIAutoId());
                }
                addrService.batchSave(saveRecords, 500);
            }

            // 修改
            List<CustomerAddr> updateRecords = jBoltTable.getUpdateModelList(CustomerAddr.class);
            if (CollUtil.isNotEmpty(updateRecords)) {
                addrService.batchUpdate(updateRecords, 500);
            }

            // 删除
            Object[] deletes = jBoltTable.getDelete();
            if (ArrayUtil.isNotEmpty(deletes)) {
                addrService.deleteMultiByIds(deletes);
            }

            return true;
        });

        return SUCCESS;
    }

    public List<Record> getDataExport(Kv kv) {
        return dbTemplate("customerm.paginateAdminDatas", kv).find();
    }

    public Ret importExcelData(File file) {
        List<Sheet> sheets = ExcelUtil.getReader(file).getSheets();
        StringBuilder errorMsg = new StringBuilder();
        if (sheets.size() > 1) {
            //第一个sheet为客户档案
            String sheetName1 = sheets.get(0).getSheetName();
            JBoltExcelSheet jBoltExcelSheet1 = JBoltExcelSheet.create(sheetName1);
            //设置sheet表格参数
            List<JBoltExcelHeader> headers = new ArrayList<>();
            headers.add(JBoltExcelHeader.create("ccccode", "客户分类编码", 50));
            headers.add(JBoltExcelHeader.create("ccuscode", "客户编码", 50));
            headers.add(JBoltExcelHeader.create("ccusname", "客户名称", 50));
            headers.add(JBoltExcelHeader.create("ccusabbname", "简称", 50));
            headers.add(JBoltExcelHeader.create("ccusmnemcode", "助记码", 50));
            headers.add(JBoltExcelHeader.create("carea", "地区", 50));
            headers.add(JBoltExcelHeader.create("ccusdepart", "分管部门编码", 50));
            headers.add(JBoltExcelHeader.create("ccuspperson", "分管人员编码", 50));
            headers.add(JBoltExcelHeader.create("ccurrency", "币种编码", 50));
            headers.add(JBoltExcelHeader.create("ccustype", "客户管理类型编码", 50));
            headers.add(JBoltExcelHeader.create("cvencode", "对应供应商编码", 50));
            headers.add(JBoltExcelHeader.create("ccustomerlevelsn", "客户级别编码", 50));
            headers.add(JBoltExcelHeader.create("csettleway", "结算方式编码", 50));
            headers.add(JBoltExcelHeader.create("cshipment", "出货单据类型编码", 50));
            headers.add(JBoltExcelHeader.create("ccusattribute", "客户属性编码", 50));

            jBoltExcelSheet1.setHeaders(2, headers);
            jBoltExcelSheet1.setDataStartRow(3);
            JBoltExcel jBoltExcel = JBoltExcel.from(file).setSheets(jBoltExcelSheet1);

            List<Record> customerms = JBoltExcelUtil.readRecords(jBoltExcel, sheetName1, true, errorMsg);

            /*//第二个sheet为客户档案详情
            String sheetName2 = sheets.get(1).getSheetName();
            JBoltExcelSheet jBoltExcelSheet2 = JBoltExcelSheet.create(sheetName2);
            //设置sheet表格参数
            List<JBoltExcelHeader> headers2 = new ArrayList<>();
            headers2.add(JBoltExcelHeader.create("cdistrictcode", "位置编码", 50));
            headers2.add(JBoltExcelHeader.create("cdistrictname", "位置名称", 50));
            headers2.add(JBoltExcelHeader.create("csearchcode", "搜索码", 50));
            headers2.add(JBoltExcelHeader.create("ideliveryadvance", "发货提前期", 50));
            headers2.add(JBoltExcelHeader.create("cplancode", "计划代码", 50));
            headers2.add(JBoltExcelHeader.create("iwarehouseid", "发货仓库", 50));
            headers2.add(JBoltExcelHeader.create("ccontactcode", "联系人编码", 50));
            headers2.add(JBoltExcelHeader.create("ccontactname", "联系人姓名", 50));
            headers2.add(JBoltExcelHeader.create("ccountry", "国家/地区", 50));
            headers2.add(JBoltExcelHeader.create("cprovince", "省／自治区", 50));
            headers2.add(JBoltExcelHeader.create("ccity", "城市", 50));
            headers2.add(JBoltExcelHeader.create("cdistrict", "区县", 50));
            headers2.add(JBoltExcelHeader.create("cdistrictname", "地址", 50));
            headers2.add(JBoltExcelHeader.create("cpostcode", "邮政编码", 50));
            headers2.add(JBoltExcelHeader.create("cmobile", "移动电话", 50));
            headers2.add(JBoltExcelHeader.create("ctele", "固定电话", 50));
            headers2.add(JBoltExcelHeader.create("cfax", "传真", 50));
            headers2.add(JBoltExcelHeader.create("cemail", "电子邮件", 50));

            jBoltExcelSheet2.setHeaders(2, headers2);
            jBoltExcelSheet2.setDataStartRow(3);
            JBoltExcel jBoltExcel2 = JBoltExcel.from(file).setSheets(jBoltExcelSheet2);
            List<Record> customerds = JBoltExcelUtil.readRecords(jBoltExcel2, sheetName2, true, errorMsg);
*/
            List<Record> customerds = new ArrayList<>();

            System.out.println("customerms===>"+customerms);
            System.out.println("customerds===>"+customerds);

            excelSave(customerms, customerds, errorMsg);
        }

        return SUCCESS;
    }

    /**
     * 保存导入数据
     */
    public void excelSave(List<Record> customerms, List<Record> customerds, StringBuilder errorMsg) {
        int i = 1;

        Kv customerKv = new Kv();
        Date now = new Date();

        //处理主表增加
        List<Customer> customermList = new ArrayList<>();
        for (Record recordm : customerms) {

            Customer customer = new Customer();

            customer.setCCCCode(recordm.getStr("ccccode"));
            customer.setCCusCode(recordm.getStr("ccuscode"));
            customer.setCCusName(recordm.getStr("ccusname"));
            customer.setCCusAbbName(recordm.getStr("ccusabbname"));
            customer.setCCusMnemCode(recordm.getStr("ccusmnemcode"));
            customer.setCCounty(recordm.getStr("carea"));
            customer.setCCusDepart(recordm.getStr("ccusdepart"));
            String ccuspperson = recordm.getStr("ccuspperson");
            if (isOk(ccuspperson)) {
                Record person = dbTemplate("customer.getPerson", Kv.create().set("code", ccuspperson)).findFirst();
                customer.setIDutyUserId(person.getLong("iautoid"));
            }
//
            customer.setCCurrency(recordm.getStr("ccurrency"));
            customer.setCCusType(recordm.getStr("ccustype"));
            customer.setCVenCode(recordm.getStr("cvencode"));
            customer.setCCustomerLevelSn(recordm.getStr("ccustomerlevelsn"));
            customer.setCSettleWay(recordm.getStr("csettleway"));
            customer.setCShipment(recordm.getStr("cshipment"));
            customer.setCCusAttribute(recordm.getStr("ccusattribute"));
            customer.setICreateBy(JBoltUserKit.getUserId());
            customer.setIUpdateBy(JBoltUserKit.getUserId());
            customer.setDCreateTime(new Date());
            customer.setDUpdateTime(new Date());
            customer.setCCreateName(JBoltUserKit.getUserName());
            customer.setCUpdateName(JBoltUserKit.getUserName());
            customer.setISource(SourceEnum.MES.getValue());
            customermList.add(customer);
        }

        tx(() -> {
			batchSave(customermList);
//			customerdService.batchSave(customerdList);
            return true;
        });
    }

    /**
     * 供应商数据源
     */
    public Page<Record> findVendorPage(int pageNumber, int pageSize, Kv kv) {
        return vendorService.dbTemplate("customer.findVendor", kv).paginate(pageNumber, pageSize);
    }

    /**
     * 根据供应商编码查询供应商
     */
    public Vendor findVendorByCode(String vendorCode) {
        Kv kv = new Kv();
        kv.set("vendorCode", vendorCode);
        return vendorService.daoTemplate("customer.findVendorByCode", kv).findFirst();
    }

    public Record findByVendorName(String cVenItem) {
        return vendorService.dbTemplate("customer.findVendor", Okv.by("cVenItem", cVenItem)).findFirst();
    }

    /**
     * 供应商数据源
     */
    public List<Record> findVendorList(Kv kv) {
        if (kv.containsKey("q")) {
            kv.set("keywords", kv.getStr("q"));
        }
        return vendorService.dbTemplate("customer.findVendor", kv).find();
    }

}
