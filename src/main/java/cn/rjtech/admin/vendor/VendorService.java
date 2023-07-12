package cn.rjtech.admin.vendor;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.admin.vendoraddr.VendorAddrService;
import cn.rjtech.admin.vendorclass.VendorClassService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.enums.IsEnableEnum;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.model.momdata.base.BaseVendorAddr;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 往来单位-供应商档案
 *
 * @ClassName: VendorService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-21 17:16
 */
public class VendorService extends BaseService<Vendor> {

    private final Vendor dao = new Vendor().dao();

    @Override
    protected Vendor dao() {
        return dao;
    }

    @Inject
    private VendorClassService       vendorClassService;
    @Inject
    private VendorAddrService        vendorAddrService;
    @Inject
    private WarehouseService         warehouseService;
    @Inject
    private PersonService            personService;
    @Inject
    private DepartmentService        departmentService;

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     * @param keywords   关键词
     * @param isEnabled  是否启用：0. 停用 1. 启用
     * @param isDeleted  删除状态：0. 未删除 1. 已删除
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isEnabled, Boolean isDeleted,
                                      Kv kv) {
        kv.set("corgcode", getOrgCode());
        Page<Record> paginate = dbTemplate("vendor.getAdminDatas", kv).paginate(pageNumber, pageSize);
        for (Record record : paginate.getList()) {
            Department dept = departmentService.findByCdepcode(getOrgId(),record.getStr("cvendepart"));
            record.set("cvendepart", null != dept ? dept.getCDepName() : "");
            Person person = personService.findById(record.getStr("idutypersonid"));
            record.set("cvenpperson", person != null ? person.getCpsnName() : "");
        }
        return paginate;
    }

    public Page<Record> pageList(Kv kv) {
        kv.set("corgcode", getOrgCode());
        Page<Record> recordPage = dbTemplate("vendor.list", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
        List<Record> list = recordPage.getList();
        for (Record record : list) {
            record.set("isource", SourceEnum.toEnum(record.getInt("isource")).getValue());
        }
        return recordPage;
    }


    /**
     * 获取数据
     */

    public List<Record> List() {
        Kv kv = new Kv();
        kv.set("corgcode", getOrgCode());
        List<Record> records = dbTemplate("vendor.list", kv.set("isenabled", "true")).find();
        return records;
    }

    /**
     * 保存
     */
    public Ret save(Vendor vendor) {
        if (vendor == null || isOk(vendor.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //供应商编码不能重复
        ValidationUtils.isTrue(StrUtil.isBlank(findcVenCodeInfo(vendor.getCVenCode())), vendor.getCVenCode() + " 供应商编码不能重复！");
        boolean success = vendor.save();
        return ret(success);
    }

    public void saveVendorModel(Vendor vendor, Warehouse warehouse) {
        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        Date date = new Date();
        vendor.setICreateBy(userId);
        vendor.setIUpdateBy(userId);
        vendor.setCCreateName(userName);
        vendor.setCUpdateName(userName);
        vendor.setDCreateTime(date);
        vendor.setDUpdateTime(date);
        vendor.setCOrgName(getOrgName());
        vendor.setCOrgCode(getOrgCode());//StrUtil.isNotBlank(getOrgCode())?getOrgCode():"999"
        vendor.setIsDeleted(false);
        vendor.setIsEnabled(true);
        vendor.setISource(SourceEnum.MES.getValue());
        vendor.setIOMWhId(warehouse.getIAutoId());//委外仓id
        vendor.setCOMWhCode(warehouse.getCWhCode());//委外仓编码
        vendor.setCCountry(StrUtil.isNotBlank(vendor.getCCountry()) ? vendor.getCCountry() : "中国");
        Person person = personService.findById(vendor.getIDutyPersonId());
        vendor.setCVenPPerson(null != person ? person.getCpsnNum() : ""); //专管业务员编码
    }

    public String findcVenCodeInfo(String cvencode) {
        Sql sql = selectSql().select(Vendor.CVENCODE).eq(Vendor.CVENCODE, cvencode);
        return queryColumn(sql);
    }

    /**
     * 执行JBoltTable表格整体提交
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        if (jBoltTable == null || jBoltTable.isBlank()) {
            return fail(JBoltMsg.JBOLTTABLE_IS_BLANK);
        }

        Record fromRecord = jBoltTable.getFormRecord();
        Object iautoid = fromRecord.get("iautoid");
        tx(() -> {
            if (isOk(iautoid)) {
                //2、执行更新
                Vendor udpateVendor = jBoltTable.getFormBean(Vendor.class);
                //获取表头
                //如果供应商编码不一样 判断是否重复
                Vendor vendor1 = findById(udpateVendor.getIAutoId());
                if (!vendor1.getCVenCode().equals(udpateVendor.getCVenCode())) {
                    ValidationUtils
                        .isTrue(StrUtil.isBlank(findcVenCodeInfo(udpateVendor.getCVenCode())),
                            udpateVendor.getCVenCode() + " 供应商编码不能重复！");
                }
                String[] split = udpateVendor.getCProvince().split(",");
                setSplitCProvince(udpateVendor, split);
                udpateVendor.setIUpdateBy(JBoltUserKit.getUserId());
                udpateVendor.setCUpdateName(JBoltUserKit.getUserName());
                udpateVendor.setDUpdateTime(new Date());
                udpateVendor.setCOrgName(getOrgName());
                udpateVendor.setCOrgCode(getOrgCode());
                Record formRecord = jBoltTable.getFormRecord();
                udpateVendor.setCProperty(formRecord.getStr("cproperty2"));//供应商属性
                Person person = personService.findById(udpateVendor.getIDutyPersonId());
                udpateVendor.setCVenPPerson(person != null ? person.getCpsnNum() : "");
                Record record = vendorClassService.findRecordByCVCCode(udpateVendor.getCVCCode());
                udpateVendor.setIVendorClassId(record != null ? record.get("iautoid") : new Long(0));
                if (jBoltTable.updateIsNotBlank()) {
                    //获取并更新表单
                    List<VendorAddr> updateModelList1 = jBoltTable.getUpdateModelList(VendorAddr.class);
                    vendorAddrService.batchUpdate(updateModelList1);
                } else if (jBoltTable.saveIsNotBlank()) {
                    if (jBoltTable.saveIsNotBlank()) {
                        //获取表单Form对应的数据--bd_vendoradd
                        List<VendorAddr> vendorAddrs = jBoltTable.getSaveModelList(VendorAddr.class);
                        if (!vendorAddrs.isEmpty()) {
                            VendorAddr vendorAddr = vendorAddrs.get(0);
                            udpateVendor.setCVenAddress(vendorAddr.getCAddr()); //地址
                            udpateVendor.setCVenPostCode(vendorAddr.getCPostCode()); //邮政编码
                            udpateVendor.setCVenPhone(vendorAddr.getCPhone());//固定电话
                            udpateVendor.setCVenFax(vendorAddr.getCFax());//传真
                            udpateVendor.setCVenEmail(vendorAddr.getCEmail());//email地址
                            udpateVendor.setCVenPerson(vendorAddr.getCContactName());//联系人
                            udpateVendor.setCVenHand(vendorAddr.getCMobile());//手机、移动电话
                        }
                        for (VendorAddr vendorAddr : vendorAddrs) {
                            vendorAddr.setIVendorId(udpateVendor.getIAutoId());//供应商主键id
                            vendorAddrService.saveVendorAddrModel(vendorAddr);
                        }
                        vendorAddrService.batchSave(vendorAddrs);
                    }
                }
                ValidationUtils.isTrue(udpateVendor.update(), "更新失败");

            } else {
                //新增
                Vendor saveVendor = new Vendor();
                saveVendor.setIAutoId(JBoltSnowflakeKit.me.nextId());
                saveVendor.setCVenCode(fromRecord.getStr("cvencode"));
                saveVendor.setCVenDepart(fromRecord.getStr("cVenDepart"));
                saveVendor.setCOMWhCode(fromRecord.getStr("comwhcode"));
                saveVendor.setITaxRate(fromRecord.getBigDecimal("itaxrate"));
                saveVendor.setCProvince(fromRecord.getStr("cprovince"));
                saveVendor.setCVenName(fromRecord.getStr("cvenname"));
                saveVendor.setCVenAbbName(fromRecord.getStr("cvenabbname"));
                saveVendor.setDVenDevDate(
                    StrUtil.isNotBlank(fromRecord.getStr("dvendevdate")) ? fromRecord.getDate("dvendevdate") : new Date());
                saveVendor.setCCurrency(fromRecord.getStr("ccurrency"));
                saveVendor.setCVCCode(fromRecord.getStr("cvccode"));
                saveVendor.setCVenMnemCode(fromRecord.getStr("cVenMnemCode"));
                saveVendor.setCMemo(fromRecord.getStr("cmemo"));
                saveVendor.setCProperty(fromRecord.getStr("cproperty2"));
                saveVendor.setIsEnabled(true);
                saveVendor.setIDutyPersonId(fromRecord.getLong("idutypersonid"));//专管业务员id
                //查询默认委外仓
                Warehouse warehouse = warehouseService.findByWhCode(saveVendor.getCOMWhCode());
                saveVendorModel(saveVendor, warehouse);
                //这里需要根据自己的需要 从Form这个JSON里去获取自己需要的数据
                if (StrUtil.isNotBlank(fromRecord.getStr("cprovince"))) {
                    String[] split = fromRecord.getStr("cprovince").split(",");
                    setSplitCProvince(saveVendor, split);
                }
                Record record = vendorClassService.findRecordByCVCCode(saveVendor.getCVCCode());
                saveVendor.setIVendorClassId(record != null ? record.get("iautoid") : new Long(0));

                //保存从表
                if (jBoltTable.saveIsNotBlank()) {
                    //获取表单Form对应的数据--bd_vendoradd
                    List<VendorAddr> vendorAddrs = jBoltTable.getSaveModelList(VendorAddr.class);
                    if (!vendorAddrs.isEmpty()) {
                        VendorAddr vendorAddr = vendorAddrs.get(0);
                        saveVendor.setCVenAddress(vendorAddr.getCAddr()); //地址
                        saveVendor.setCVenPostCode(vendorAddr.getCPostCode()); //邮政编码
                        saveVendor.setCVenPhone(vendorAddr.getCPhone());//固定电话
                        saveVendor.setCVenFax(vendorAddr.getCFax());//传真
                        saveVendor.setCVenEmail(vendorAddr.getCEmail());//email地址
                        saveVendor.setCVenPerson(vendorAddr.getCContactName());//联系人
                        saveVendor.setCVenHand(vendorAddr.getCMobile());//手机、移动电话
                    }
                    for (VendorAddr vendorAddr : vendorAddrs) {
                        vendorAddr.setIVendorId(saveVendor.getIAutoId());//供应商主键id
                        vendorAddrService.saveVendorAddrModel(vendorAddr);
                    }
                    vendorAddrService.batchSave(vendorAddrs);
                } else if (jBoltTable.updateIsNotBlank()) {
                    List<VendorAddr> updateModelList1 = jBoltTable.getUpdateModelList(VendorAddr.class);
                    vendorAddrService.batchUpdate(updateModelList1);
                }
                //保存主表
                ValidationUtils.isTrue(saveVendor.save(), "保存失败");
            }
            //3、获取待删除数据 执行删除
            if (jBoltTable.deleteIsNotBlank()) {
            }
            return true;
        });

        return SUCCESS;
    }

    public void setSplitCProvince(Vendor vendor, String[] split) {
        for (int i = 0; i < split.length; i++) {
            vendor.setCProvince(split.length > 0 ? split[0] : "");//省份
            vendor.setCCity(split.length > 1 ? split[1] : "");//城市
            vendor.setCCounty(split.length > 2 ? split[2] : "");//区县
        }
    }

    /**
     * 更新
     */
    public Ret update(Vendor vendor) {
        if (vendor == null || notOk(vendor.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        Vendor dbVendor = findById(vendor.getIAutoId());
        if (dbVendor == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = vendor.update();
        return ret(success);
    }

    public Ret deleteByAutoids(String ids) {
        String[] split = ids.split(",");
        for (String autoid : split) {
            this.deleteByAutoid(Long.valueOf(autoid));
        }
        return SUCCESS;
    }

    public Ret deleteByAutoid(Long autoid) {
        tx(() -> {
            Vendor vendor = findById(autoid);
            Kv kv = new Kv();
            kv.set("ivendorid", vendor.getIAutoId());
            //从表
            List<VendorAddr> list = vendorAddrService.list(kv);
            if (!list.isEmpty()) {
                List<Long> collect = list.stream().map(BaseVendorAddr::getIAutoId).collect(Collectors.toList());
                vendorAddrService.deleteByIds(collect.toArray());
            }
            //主表
            deleteById(autoid);
            return true;
        });
        return SUCCESS;
    }

    /**
     * 删除数据后执行的回调
     *
     * @param vendor 要删除的model
     * @param kv     携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Vendor vendor, Kv kv) {
        //addDeleteSystemLog(vendor.getIAutoId(), JBoltUserKit.getUserId(),vendor.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param vendor model
     * @param kv     携带额外参数一般用不上
     */
    @Override
    public String checkInUse(Vendor vendor, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(Vendor vendor, String column, Kv kv) {
        //addUpdateSystemLog(vendor.getIAutoId(), JBoltUserKit.getUserId(), vendor.getName(),"的字段["+column+"]值:"+vendor.get(column));
        /**
         switch(column){
         case "isEnabled":
         break;
         }
         */
        return null;
    }

    public List<Record> options() {
        return dbTemplate("vendor.findColumns", Kv.of("isenabled", "1")).find();
    }

    public void deleteMultiByIds(Object[] deletes) {
        delete("DELETE FROM Bd_Vendor WHERE iautoid IN (" + ArrayUtil.join(deletes, COMMA) + ") ");
    }

    /**
     * 切换isenabled属性
     */
    public Ret toggleIsenabled(Long id) {
        return toggleBoolean(id, "isEnabled");
    }

    public Vendor findByName(String vendorName) {
        return findFirst("SELECT * FROM Bd_Vendor v WHERE isDeleted = 0 AND isEnabled = 1 AND v.cvenname = ? AND v.cOrgCode=?",
            vendorName, getOrgCode());
    }

    public Vendor findByCode(String cvencode) {
        return findFirst("SELECT * FROM Bd_Vendor v WHERE isDeleted = 0 AND isEnabled = 1 AND v.cvencode = ? and v.cOrgCode=?",
            cvencode, getOrgCode());
    }

    public Record getRecprdByCVenCode(String cvencode) {
        return findFirstRecord(selectSql().eq("cvencode", cvencode).eq("isdeleted", IsEnableEnum.NO.getValue())
            .eq("isenabled", IsEnableEnum.NO.getValue()));
    }

    public List<Record> getVendorList(Kv kv) {
        kv.set("corgcode", getOrgCode());
        return dbTemplate("vendor.getVendorList", kv).find();
    }

    public Page<Record> getVendorPaginate(Integer pageNumber, Integer pageSize, Kv para) {
        para.set("iorgid", getOrgId());
        return dbTemplate("vendor.getVendorList", para).paginate(pageNumber, pageSize);
    }

    public List<Record> getAutocompleteList(String q, int limit) {
        Okv para = Okv.by("q", q)
            .set("limit", limit).set("corgcode", getOrgCode());
        return dbTemplate("vendor.getAutocompleteList", para).find();
    }

    public Long queryAutoIdByCvencode(String cvencode) {
        return queryLong("select iautoid from Bd_Vendor where cVenCode = ? AND cOrgCode = ? ", cvencode, getOrgCode());
    }

    public List<Vendor> findByCVCCodeAndiVendorClassId(String cVCCode, Long iVendorClassId) {
        return find("select * from bd_vendor where cVCCode = ? and iVendorClassId = ?", cVCCode, iVendorClassId);
    }

    /**
     * 供应商excel导入数据库
     */
    public Ret importExcelData(File file) {
        // 使用字段配置维护
        List<Record> vendors = CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, table());
        ValidationUtils.notEmpty(vendors, "导入数据不能为空");

        // 读取数据没有问题后判断必填字段
        if (vendors.size() > 0) {
            Date now = new Date();

            for (Record row : vendors) {
                if(notOk(row.getStr("cvccode"))){
                    return fail("供应商分类编码不能为空");
                }
                if (notOk(row.getStr("cvencode"))) {
                    return fail("供应商编码不能为空");
                }
                if (notOk(row.getStr("cvenname"))) {
                    return fail("供应商名称不能为空");
                }
                if (notOk(row.getStr("cvenabbname"))) {
                    return fail("供应商简称不能为空");
                }
                if (notOk(row.getStr("cvenmnemcode"))) {
                    return fail("助记码不能为空");
                }
                Vendor vendor = findByCode(row.getStr("cvencode"));
                ValidationUtils.assertNull(vendor, row.getStr("cvencode") + "：供应商编码已存在，不能重复");

                VendorClass vendorClass = vendorClassService.findByCVCCode(row.getStr("cvccode"));
                ValidationUtils.notNull(vendorClass, String.format("供应商分类“%s”不存在", row.getStr("cvccode")));

                if(StrUtil.isNotBlank(row.getStr("cvendepart"))){
                    Department department = departmentService.findByCdepcode(getOrgId(), row.getStr("cvendepart"));
                    ValidationUtils.notNull(department, String.format("分管部门“%s”不存在", row.getStr("cvendepart")));
                }

                row.keep(ArrayUtil.toArray(TableMapping.me().getTable(Vendor.class).getColumnNameSet(), String.class));

                row.set("iautoid", JBoltSnowflakeKit.me.nextId());
                row.set("IOrgId", getOrgId());
                row.set("COrgCode", getOrgCode());
                row.set("COrgName", getOrgName());
                row.set("cVenCode",row.getStr("cvencode"));//供应商编码
                row.set("cVenName",row.getStr("cvenname"));//供应商名称
                row.set("cVenMnemCode",row.getStr("cvenmnemcode"));//助记码
                row.set("cVenDepart",row.getStr("cvendepart"));//分管部门编码
                row.set("cVenAbbName",row.getStr("cvenabbname"));//供应商简称
                row.set("iTaxRate",row.get("itaxrate"));//税率
                row.set("ISource", SourceEnum.MES.getValue());
                row.set("isEnabled","1");
                row.set("IsDeleted", ZERO_STR);
                row.set("dcreatetime", now);
                row.set("ICreateBy", JBoltUserKit.getUserId());
                row.set("ccreatename", JBoltUserKit.getUserName());
                row.set("DUpdateTime", now);
                row.set("IUpdateBy", JBoltUserKit.getUserId());
                row.set("CUpdateName", JBoltUserKit.getUserName());
                row.set("iVendorClassId",vendorClass.getIAutoId());//分类id
            }
        }

        // 执行批量操作
        boolean success = tx(() -> {
            batchSaveRecords(vendors);
            return true;
        });
        return SUCCESS;
    }
}
