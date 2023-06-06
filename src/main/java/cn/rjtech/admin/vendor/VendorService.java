package cn.rjtech.admin.vendor;

import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.admin.vendoraddr.VendorAddrService;
import cn.rjtech.admin.vendorclass.VendorClassService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.enums.IsEnableEnum;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

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
    private VendorClassService vendorClassService;
    @Inject
    private VendorAddrService  vendorAddrService;
    @Inject
    private WarehouseService   warehouseService;
    @Inject
    private PersonService      personService;
    @Inject
    private DepartmentService  departmentService;

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
    public Page<Vendor> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isEnabled, Boolean isDeleted,
                                      Kv kv) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eqBooleanToChar("isEnabled", isEnabled);
        sql.eqBooleanToChar("isDeleted", isDeleted);
        sql.eq("cVenName", kv.get("cvenname"));//供应商编码
        sql.eq("cVenCode", kv.get("cvencode"));//供应商名称
        sql.eq("iAutoid", kv.get("iventorclassid"));//供应商分类id
        //关键词模糊查询
        sql.likeMulti(keywords, "cOrgName", "cVenName", "cVenAbbName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("dUpdateTime");
        Page<Vendor> paginate = paginate(sql);
        for (Vendor vendor : paginate.getList()) {
            Department dept = departmentService.findById(vendor.getCVenDepart());
            vendor.setCVenDepart(null != dept ? dept.getCDepName() : "");
            Person person = personService.findById(vendor.getIDutyPersonId());
            vendor.set("cvenpperson", person != null ? person.getCpsnName() : "");
        }
        return paginate;
    }

    public Page<Record> pageList(Kv kv) {
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

    public List<Record>  List(){
      Kv kv=new Kv();
        List<Record> records = dbTemplate("vendor.list",kv.set("isenabled","true")).find();
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
        ValidationUtils.isTrue(StringUtils.isBlank(findcVenCodeInfo(vendor.getCVenCode())), vendor.getCVenCode() + " 供应商编码不能重复！");

        //查询默认委外仓
        Warehouse warehouse = warehouseService.findByWhCode(vendor.getCOMWhCode());
        saveVendorModel(vendor, warehouse);
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
        vendor.setCOrgCode(getOrgCode());//StringUtils.isNotBlank(getOrgCode())?getOrgCode():"999"
        vendor.setIsDeleted(false);
        vendor.setIsEnabled(true);
        vendor.setISource(SourceEnum.MES.getValue());
        vendor.setIOMWhId(warehouse.getIAutoId());//委外仓id
        vendor.setCOMWhCode(warehouse.getCWhCode());//委外仓编码
        vendor.setCCountry(StringUtils.isNotBlank(vendor.getCCountry()) ? vendor.getCCountry() : "中国");
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
        //1、执行保存
        if (jBoltTable.saveIsNotBlank()) {
            //获取表单Form对应的数据--bd_vendoradd
            List<VendorAddr> vendorAddrs = jBoltTable.getSaveModelList(VendorAddr.class);
            //获取表头对应的数据--bd_vendor
            if (jBoltTable.formIsNotBlank()) {
                //这里需要根据自己的需要 从Form这个JSON里去获取自己需要的数据
                Record fromRecord = jBoltTable.getFormRecord();
                Vendor vendor = new Vendor();
                vendor.setCVenCode(fromRecord.getStr("cvencode"));
                vendor.setCVenDepart(fromRecord.getStr("cVenDepart"));
                vendor.setCOMWhCode(fromRecord.getStr("comwhcode"));
                vendor.setITaxRate(fromRecord.getBigDecimal("itaxrate"));
                vendor.setCProvince(fromRecord.getStr("cprovince"));
                vendor.setCVenName(fromRecord.getStr("cvenname"));
                vendor.setCVenAbbName(fromRecord.getStr("cvenabbname"));
                vendor.setDVenDevDate(StringUtils.isNotBlank(fromRecord.getStr("dvendevdate")) ?
                    fromRecord.getDate("dvendevdate") : new Date());
                vendor.setCCurrency(fromRecord.getStr("ccurrency"));
                vendor.setCVCCode(fromRecord.getStr("cvccode"));
                vendor.setCVenMnemCode(fromRecord.getStr("cVenMnemCode"));
                vendor.setCMemo(fromRecord.getStr("cmemo"));
                vendor.setCProperty(fromRecord.getStr("cproperty2"));
                vendor.setIsEnabled(true);
                Long idutypersonid = fromRecord.getLong("idutypersonid");
                vendor.setIDutyPersonId(idutypersonid);//专管业务员id
                if (StringUtils.isNotBlank(fromRecord.getStr("cprovince"))) {
                    String[] split = fromRecord.getStr("cprovince").split(",");
                    setSplitCProvince(vendor,split);
                }
                if (!vendorAddrs.isEmpty()) {
                    VendorAddr vendorAddr = vendorAddrs.get(0);
                    vendor.setCVenAddress(vendorAddr.getCAddr()); //地址
                    vendor.setCVenPostCode(vendorAddr.getCPostCode()); //邮政编码
                    vendor.setCVenPhone(vendorAddr.getCPhone());//固定电话
                    vendor.setCVenFax(vendorAddr.getCFax());//传真
                    vendor.setCVenEmail(vendorAddr.getCEmail());//email地址
                    vendor.setCVenPerson(vendorAddr.getCContactName());//联系人
                    vendor.setCVenHand(vendorAddr.getCMobile());//手机、移动电话
                }
                Record record = vendorClassService.findRecordByCVCCode(vendor.getCVCCode());
                vendor.setIVendorClassId(record != null ? record.get("iautoid") : new Long(0));
                Vendor checkVendorIsBlank = findById(fromRecord.get("iautoid"));
                if (null == checkVendorIsBlank) {
                    //表头保存
                    Ret retVendor = save(vendor);
                    if (retVendor.isFail()) {
                        return retVendor;
                    }
                } else {
                    vendor.setIAutoId(fromRecord.getLong("iautoid"));
                    update(vendor);
                }
                //表单保存
                for (VendorAddr vendorAddr : vendorAddrs) {
                    vendorAddr.setIVendorId(vendor.getIAutoId());//供应商主键id
                    vendorAddrService.saveVendorAddrModel(vendorAddr);
                }
                vendorAddrService.batchSave(vendorAddrs);
            }
        }
        //2、执行更新
        if (jBoltTable.updateIsNotBlank()) {
            //获取表头
            Vendor vendor = jBoltTable.getFormBean(Vendor.class);
            //如果供应商编码不一样 判断是否重复
            Vendor vendor1 = findById(vendor.getIAutoId());
            if (!vendor1.getCVenCode().equals(vendor.getCVenCode())) {
                ValidationUtils
                    .isTrue(StringUtils.isBlank(findcVenCodeInfo(vendor.getCVenCode())), vendor.getCVenCode() + " 供应商编码不能重复！");
            }
            String[] split = vendor.getCProvince().split(",");
            setSplitCProvince(vendor,split);
            vendor.setIUpdateBy(JBoltUserKit.getUserId());
            vendor.setCUpdateName(JBoltUserKit.getUserName());
            vendor.setDUpdateTime(new Date());
            vendor.setCOrgName(getOrgName());
            vendor.setCOrgCode(getOrgCode());
            Record formRecord = jBoltTable.getFormRecord();
            vendor.setCProperty(formRecord.getStr("cproperty2"));//供应商属性
            Person person = personService.findById(vendor.getIDutyPersonId());
            vendor.setCVenPPerson(person.getCpsnNum());

            //获取表单
            List<VendorAddr> updateModelList1 = jBoltTable.getUpdateModelList(VendorAddr.class);
            update(vendor);
            vendorAddrService.batchUpdate(updateModelList1);
        }
        //3、获取待删除数据 执行删除
        if (jBoltTable.deleteIsNotBlank()) {
        }

        return SUCCESS;
    }

    public void setSplitCProvince(Vendor vendor,String[] split){
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
        return findFirst("SELECT * FROM Bd_Vendor v WHERE isDeleted = 0 AND isEnabled = 1 AND v.cvenname = ?", vendorName);
    }

    public Vendor findByCode(String cvencode) {
        return findFirst("SELECT * FROM Bd_Vendor v WHERE isDeleted = 0 AND isEnabled = 1 AND v.cvencode = ?", cvencode);
    }

    public Record getRecprdByCVenCode(String cvencode) {
        return findFirstRecord(selectSql().eq("cvencode", cvencode).eq("isdeleted", IsEnableEnum.NO.getValue())
            .eq("isenabled", IsEnableEnum.NO.getValue()));
    }

    public List<Record> getVendorList(Kv kv) {
        return dbTemplate("vendor.getVendorList", kv).find();
    }

    public Page<Record> getVendorPaginate(Integer pageNumber, Integer pageSize, Kv kv) {
        return dbTemplate("vendor.getVendorList", kv).paginate(pageNumber, pageSize);
    }

    public List<Record> getAutocompleteList(String q, int limit) {
        Okv para = Okv.by("q", q)
            .set("limit", limit);

        return dbTemplate("vendor.getAutocompleteList", para).find();
    }

    public Long queryAutoIdByCvencode(String cvencode) {
        return queryLong("select iautoid from Bd_Vendor where cVenCode = ? AND cOrgCode = ? ", cvencode, getOrgCode());
    }

}
