package cn.rjtech.admin.syspureceive;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.inventorymfginfo.InventoryMfgInfoService;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.admin.syspuinstore.SysPuinstoreService;
import cn.rjtech.admin.syspuinstore.SysPuinstoredetailService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 采购收料单
 *
 * @ClassName: SysPureceiveService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-10 10:01
 */
public class SysPureceiveService extends BaseService<SysPureceive> implements IApprovalService {

    private final SysPureceive dao = new SysPureceive().dao();

    @Inject
    private VendorService vendorservice;
    @Inject
    private WarehouseService warehouseservice;
    @Inject
    private SysPuinstoreService syspuinstoreservice;
    @Inject
    private RcvDocQcFormMService rcvdocqcformmservice;
    @Inject
    private InventoryMfgInfoService inventoryMfgInfoService;
    @Inject
    private SysPureceivedetailService syspureceivedetailservice;
    @Inject
    private SysPuinstoredetailService syspuinstoredetailservice;


    @Override
    protected SysPureceive dao() {
        return dao;
    }

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
     * @param BillType   到货单类型;采购PO  委外OM
     * @param IsDeleted  删除状态：0. 未删除 1. 已删除
     */
    public Page<SysPureceive> getAdminDatas(int pageNumber, int pageSize, String keywords, String BillType, Boolean IsDeleted) {
        // 创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        // sql条件处理
        sql.eq("BillType", BillType);
        sql.eqBooleanToChar("IsDeleted", IsDeleted);
        // 关键词模糊查询
        sql.like("repositoryName", keywords);
        // 排序
        sql.desc("AutoID");
        return paginate(sql);
    }

    /**
     * 保存
     */
    public Ret save(SysPureceive sysPureceive) {
        if (sysPureceive == null || isOk(sysPureceive.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        boolean success = sysPureceive.save();
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(SysPureceive sysPureceive) {
        if (sysPureceive == null || notOk(sysPureceive.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // 更新时需要判断数据存在
        SysPureceive dbSysPureceive = findById(sysPureceive.getAutoID());
        if (dbSysPureceive == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = sysPureceive.update();
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysPureceive 要删除的model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(SysPureceive sysPureceive, Kv kv) {
        return null;
    }


    public String getVenName(String vencode) {
        Vendor first = vendorservice.findFirst("select * from  Bd_Vendor where cVenCode = ?", vencode);
        return first.getCVenName();
    }

    /**
     * 检测是否可以删除
     *
     * @param sysPureceive model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    public String checkInUse(SysPureceive sysPureceive, Kv kv) {
        // 这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(SysPureceive sysPureceive, String column, Kv kv) {
        return null;
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("syspureceive.recpor", kv).paginate(pageNumber, pageSize);
    }

    /**
     * 批量删除主从表
     */
    public Ret deleteRmRdByIds(String ids) {
        tx(() -> {
            List<SysPureceive> sysPureceives = find("select *  from T_Sys_PUReceive where AutoID in (" + ids + ")");
            for (SysPureceive s : sysPureceives) {
                if (!"0".equals(String.valueOf(s.getIAuditStatus())) && !"3".equals(String.valueOf(s.getIAuditStatus()))) {
                    ValidationUtils.error("收料编号：" + s.getBillNo() + "单据状态已改变，不可删除！");
                }
                if (!s.getIcreateby().equals(JBoltUserKit.getUser().getId())) {
                    ValidationUtils.error("当前登录人:" + JBoltUserKit.getUser().getName() + ",单据创建人为:" + s.getCcreatename() + " 不可删除!!!");
                }
            }
            String[] split = ids.split(",");
            deleteByIds(ids);
            for (String s : split) {
                delete("DELETE T_Sys_PUReceiveDetail   where  MasID = ?", s);
            }
            return true;
        });
        return SUCCESS;
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        tx(() -> {
            SysPureceive byId = findById(id);
            if (!"0".equals(String.valueOf(byId.getIAuditStatus())) && !"3".equals(String.valueOf(byId.getIAuditStatus()))) {
                ValidationUtils.error("收料编号：" + byId.getBillNo() + "单据状态已改变，不可删除！");
            }
            if (!byId.getIcreateby().equals(JBoltUserKit.getUser().getId())) {
                ValidationUtils.error("当前登录人:" + JBoltUserKit.getUser().getName() + ",单据创建人为:" + byId.getCcreatename() + " 不可删除!!!");
            }
            deleteById(id);
            delete("DELETE T_Sys_PUReceiveDetail   where  MasID = ?", id);
            return true;
        });
        return SUCCESS;
    }


    /**
     * 后台管理数据查询
     */
    public List<Record> getVenCodeDatas(Kv kv) {
        return dbTemplate("syspureceive.venCode", kv).find();
    }

    /**
     * 后台管理数据查询
     */
    public List<Record> getWhcodeDatas(Kv kv) {
        return dbTemplate("syspureceive.Whcode", kv).find();
    }

    /**
     * 后台管理数据查询
     */
    public List<Record> selectRdCode(Kv kv) {
        return dbTemplate("syspureceive.selectRdCode", kv).find();
    }

    /**
     * 货区
     */
    public List<Record> getwareHousepos(Kv kv) {
        return dbTemplate("syspureceive.wareHousepos", kv).find();
    }

    /**
     * 推送 PL_RcvDocQcFormM ;//来料检验
     */
    public RcvDocQcFormM insertRcvDocQcFormM(Record row, SysPureceive sys, User user, Long veniAutoId, Integer imask) {
        Date date = new Date();
        Record barcode = dbTemplate("syspureceive.purchaseOrderD", Kv.by("barcode", row.getStr("barcode"))).findFirst();
        RcvDocQcFormM rcvDocQcFormM = new RcvDocQcFormM();

        rcvDocQcFormM.setIOrgId(getOrgId());
        rcvDocQcFormM.setCOrgCode(getOrgCode());
        rcvDocQcFormM.setCOrgName(getOrgName());

        if (StrUtil.isBlank(row.getStr("sourcebillno"))) {
            rcvDocQcFormM.setCRcvDocQcFormNo("test");
        } else {
            rcvDocQcFormM.setCRcvDocQcFormNo(row.getStr("sourcebillno"));
        }

        // 质检表格ID
        if (null != barcode && null != barcode.getStr("cinvcode")) {
            Record iq = dbTemplate("syspureceive.InventoryQcForm", Kv.by("cinvcode", barcode.getStr("cinvcode"))).findFirst();
            if (null != iq && null != iq.getLong("iautoid")) {
                rcvDocQcFormM.setIQcFormId(iq.getLong("iautoid"));
                rcvDocQcFormM.setIStatus(1);
                //设变号
                rcvDocQcFormM.setCDcNo(iq.get("cDcCode"));
            } else {
                rcvDocQcFormM.setIStatus(0);
            }
        } else {
            rcvDocQcFormM.setIStatus(0);
        }
        rcvDocQcFormM.setIRcvDocId(Long.valueOf(sys.getAutoID()));
        rcvDocQcFormM.setCRcvDocNo(sys.getBillNo());
        rcvDocQcFormM.setIInventoryId(row.getLong("iinventoryid"));
        rcvDocQcFormM.setIVendorId(veniAutoId);
        rcvDocQcFormM.setDRcvDate(sys.getDcreatetime());
        rcvDocQcFormM.setIQty(Double.valueOf(row.getStr("qty").trim()).intValue());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        // 批次号(收货日期)
        rcvDocQcFormM.setCBatchNo(formatter.format(sys.getDcreatetime()));
        rcvDocQcFormM.setIsCpkSigned(false);
        rcvDocQcFormM.setIMask(imask);
        rcvDocQcFormM.setIsCpkSigned(false);
        rcvDocQcFormM.setICreateBy(user.getId());
        rcvDocQcFormM.setCCreateName(user.getName());
        rcvDocQcFormM.setDCreateTime(date);
        rcvDocQcFormM.setIUpdateBy(user.getId());
        rcvDocQcFormM.setCUpdateName(user.getName());
        rcvDocQcFormM.setDUpdateTime(date);
        rcvDocQcFormM.setIsDeleted(false);

        return rcvDocQcFormM;
    }

    /**
     * 获取条码列表
     * 通过关键字匹配
     * autocomplete组件使用
     */
    public List<Record> getBarcodeDatas(String q, Integer limit, String orgCode, String vencode, String whcode1) {
        return dbTemplate("syspureceive.getBarcodeDatas", Kv.by("q", q).set("limit", limit).set("orgCode", orgCode).set("vencode", vencode)).find();
    }

    public Record getWhcodeid(String whcode1) {
        return dbTemplate("syspureceive.Whcode", Kv.by("q", whcode1)).findFirst();
    }


    /**
     * 获取条码列表
     * 通过关键字匹配
     * autocomplete组件使用
     */
    public Record barcode(Kv kv) {
        //先查询条码是否已添加
        Record first = dbTemplate("syspureceive.barcodeDatas", kv).findFirst();
        if (null != first) {
            ValidationUtils.error("条码为：" + kv.getStr("barcode") + "的数据已经存在，请勿重复录入。");
        }
        first = dbTemplate("syspureceive.barcode", kv).findFirst();
        ValidationUtils.notNull(first, "未查到条码为：" + kv.getStr("barcode") + "的数据,请核实再录入。");
        return first;
    }

    /**
     * 通过采购订单判断u8是否有数据
     */
    public boolean existSourcebillno(String sourcebillno) {
        List<Record> records = dbTemplate(u8SourceConfigName(), "syspureceive.getsourcebillno",
                Kv.by("sourcebillno", sourcebillno)).find();
        return CollUtil.isNotEmpty(records);
    }

    /**
     * 业务逻辑发送改变，以下重新开发一个保存功能
     */
    public Ret submitAll(JBoltTable jBoltTable, User user) {
        String operationType = jBoltTable.getFormRecord().getStr("operationType");

        if ("submit".equals(operationType)) {
            if (jBoltTable.getSaveRecordList() == null && jBoltTable.getDelete() == null && jBoltTable.getUpdateRecordList() == null) {
                return fail("行数据不能为空");
            }
        }
        // 采购订单推u8才可以添加
        List<Record> list = jBoltTable.getSaveRecordList();
        if (null != list) {
            for (Record record : list) {
                ValidationUtils.isTrue(this.existSourcebillno(record.getStr("sourcebillno")), "订单编号：" + record.getStr("sourcebillno") + " 没有推U8");
            }
        }
        List<Record> list1 = jBoltTable.getUpdateRecordList();
        if (null != list1) {
            for (Record record : list1) {
                ValidationUtils.isTrue(this.existSourcebillno(record.getStr("sourcebillno")), "订单编号：" + record.getStr("sourcebillno") + " 没有推U8");
            }
        }

        SysPureceive sysPureceive = jBoltTable.getFormModel(SysPureceive.class, "sysPureceive");

        Date now = new Date();

        HashMap<String, String> vencodemap = new HashMap<>();

        tx(() -> {
            if (sysPureceive.getAutoID() != null) {
                sysPureceive.setIupdateby(user.getId());
                sysPureceive.setCupdatename(user.getName());
                sysPureceive.setDupdatetime(now);
                // 主表修改
                sysPureceive.update();
            }

            // 从表的操作 获取新增数据
            saveData(jBoltTable, sysPureceive, operationType, user, vencodemap);
            // 获取修改数据（执行修改）
            updateData(jBoltTable, sysPureceive, operationType, user, vencodemap);
            // 获取删除数据（执行删除）
            deleteData(jBoltTable);

            return true;
        });

        return Ret.ok().set("autoid", sysPureceive.getAutoID());
    }

    private void saveData(JBoltTable jBoltTable, SysPureceive sysPureceive, String operationType, User user, HashMap<String, String> map) {
        List<Record> list = jBoltTable.getSaveRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }

        ArrayList<SysPureceivedetail> sysdetaillist = new ArrayList<>();

        Date now = new Date();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            SysPureceivedetail sysPureceivedetail = new SysPureceivedetail();
            Record row = list.get(i);
            sysPureceivedetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysPureceivedetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysPureceivedetail.setSourceBillNoRow(row.getStr("sourcebillnorow"));
            sysPureceivedetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysPureceivedetail.setSourceBillID(row.getStr("sourcebilldid"));
            // 获取供应商字段
            String vencode = row.getStr("vencode");
            ValidationUtils.notBlank(vencode, "条码：" + row.get("barcode") + " 供应商数据不能为空");
            sysPureceivedetail.setVenCode(vencode);
            sysPureceivedetail.setWhcode(sysPureceive.getWhCode());
            this.determineQty(row, i);
            sysPureceivedetail.setPosCode(row.getStr("poscode"));
            sysPureceivedetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysPureceivedetail.setBarcode(row.get("barcode"));
            sysPureceivedetail.setDcreatetime(now);
            sysPureceivedetail.setDupdatetime(now);
            sysPureceivedetail.setIsDeleted(false);
            String s = this.insertSysPureceive(sysPureceivedetail, sysPureceive, row, operationType, map);
            sysPureceivedetail.setMasID(s);
            if (StrUtil.isBlank(row.getStr("isinitial")) || row.getStr("isinitial").equals("false")) {
                sysPureceivedetail.setIsInitial("0");
            } else {
                sysPureceivedetail.setIsInitial("1");
                arrayList.add(row.getStr("cinvcode"));
            }
            sysdetaillist.add(sysPureceivedetail);
        }
        //有一个存货是初物，存货编码一致的都是初物 (第一条是，第二条不是无法guo)
        if (null != arrayList) {
            for (String al : arrayList) {
                for (SysPureceivedetail sd : sysdetaillist) {
                    //通过条码查询 存货id
                    Record barcode = dbTemplate("syspureceive.purchaseOrderD", Kv.by("barcode", sd.getBarcode())).findFirst();
                    if (al.equals(barcode.getStr("cInvCode"))) {
                        sd.setIsInitial("1");
                    }
                }
            }
        }
        syspureceivedetailservice.batchSave(sysdetaillist);
    }


    public SysPureceive findFirstBySourceBillNo(String sourceBillNo) {
        return findFirst("select *  from T_Sys_PUReceive where SourceBillNo = ? ", sourceBillNo);
    }

    public SysPureceive findByBillNo(String billno) {
        return findFirst("select *  from T_Sys_PUReceive where billno = ? ", billno);
    }

    private void updateData(JBoltTable jBoltTable, SysPureceive sysPureceive, String operationType, User user,
                            HashMap<String, String> map) {
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }
        ArrayList<SysPureceivedetail> sysdetaillist = new ArrayList<>();
        Date now = new Date();
        //是初物
        ArrayList<String> arrayList = new ArrayList<>();
        //不是初物
        ArrayList<String> noarrayList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            SysPureceivedetail sysPureceivedetail = new SysPureceivedetail();
            Record row = list.get(i);
            sysPureceivedetail.setAutoID(row.getStr("autoid"));
            sysPureceivedetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysPureceivedetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysPureceivedetail.setSourceBillNoRow(row.getStr("sourcebillnorow"));
            sysPureceivedetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysPureceivedetail.setSourceBillID(row.getStr("sourcebilldid"));
            // 获取供应商字段
            String vencode = row.getStr("vencode");
            ValidationUtils.notBlank(vencode, "条码：" + row.get("barcode") + " 供应商数据不能为空");
            sysPureceivedetail.setVenCode(vencode);
            sysPureceivedetail.setWhcode(sysPureceive.getWhCode());
            this.determineQty(row, i);
            sysPureceivedetail.setPosCode(row.getStr("poscode"));
            sysPureceivedetail.setQty(row.getBigDecimal("qty"));
            sysPureceivedetail.setBarcode(row.get("barcode"));
            sysPureceivedetail.setDupdatetime(now);
            sysPureceivedetail.setIsDeleted(false);
            //状态为保存状态的可以拆单 其他状态接是修改操作
//            if (String.valueOf(AuditStatusEnum.NOT_AUDIT.getValue()).equals(sysPureceive.getIAuditStatus())) {
//                String s = this.insertSysPureceive(sysPureceivedetail, sysPureceive, row, operationType, map);
//                sysPureceivedetail.setMasID(s);
//            }
            if (StrUtil.isBlank(row.getStr("isinitial")) || row.getStr("isinitial").equals("false")) {
                sysPureceivedetail.setIsInitial("0");
                noarrayList.add(row.getStr("cinvcode"));
            } else {
                sysPureceivedetail.setIsInitial("1");
                arrayList.add(row.getStr("cinvcode"));
            }
            sysdetaillist.add(sysPureceivedetail);
        }
        syspureceivedetailservice.batchUpdate(sysdetaillist);


        //由于 此表格的数据只读取修改的数据  所以需要查出所有的从表 然后进行修改
        List<SysPureceivedetail> firstBy = syspureceivedetailservice.findFirstBy(sysPureceive.getAutoID());
        //有一个存货是初物，存货编码一致的都是初物
        if (null != arrayList) {
            for (String al : arrayList) {
                for (SysPureceivedetail sd : firstBy) {
                    //通过条码查询 存货id
                    Record barcode = dbTemplate("syspureceive.purchaseOrderD", Kv.by("barcode", sd.getBarcode())).findFirst();
                    if (al.equals(barcode.getStr("cInvCode"))) {
                        sd.setIsInitial("1");
                    }
                }
            }
        }
        //如果修改了一个存货不是初物，所有的存货编码一致的都不是
        if (null != noarrayList) {
            for (String al : noarrayList) {
                for (SysPureceivedetail sd : firstBy) {
                    //通过条码查询 存货id
                    Record barcode = dbTemplate("syspureceive.purchaseOrderD", Kv.by("barcode", sd.getBarcode())).findFirst();
                    if (al.equals(barcode.getStr("cInvCode"))) {
                        sd.setIsInitial("0");
                    }
                }
            }
        }
        syspureceivedetailservice.batchUpdate(firstBy);


    }

    private void deleteData(JBoltTable jBoltTable) {
        Object[] ids = jBoltTable.getDelete();
        if (ArrayUtil.isEmpty(ids)) {
            return;
        }
        syspureceivedetailservice.deleteByIds(ids);
    }

    /**
     * 根据行表数据 通过 供应商(取MES的采购订单)判断头表是否需要添加头表数据，返回 头表id
     */
    public String insertSysPureceive(SysPureceivedetail sysPureceivedetail, SysPureceive sysPureceive, Record record,
                                     String operationType, HashMap<String, String> map) {
        // SysPureceive first = findFirst("select *  from T_Sys_PUReceive where VenCode=?", sysPureceivedetail.getVenCode())
        if (!map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                // 判断是否包含指定的key
                if (key.equals(sysPureceivedetail.getVenCode())) {
                    return value;
                }
            }
        }

        User user = JBoltUserKit.getUser();

        Date now = new Date();

        // 需要先创建头表数据
        sysPureceive.setSourceBillNo(sysPureceivedetail.getSourceBillNo());
        sysPureceive.setVenCode(sysPureceivedetail.getVenCode());

        Warehouse warehouse = warehouseservice.findByCwhcode(sysPureceive.getWhCode());
        ValidationUtils.notNull(warehouse, "仓库记录不存在");
        sysPureceive.setBillType(record.getStr("billtype"));
        sysPureceive.setWhName(warehouse.getCWhName());
        sysPureceive.setRdCode(record.getStr("rdcode"));
        sysPureceive.setOrganizeCode(getOrgCode());
        sysPureceive.setIcreateby(user.getId());
        sysPureceive.setCcreatename(user.getName());
        sysPureceive.setDcreatetime(now);
        sysPureceive.setIupdateby(user.getId());
        sysPureceive.setCupdatename(user.getName());
        sysPureceive.setDupdatetime(now);

        sysPureceive.setBillDate(DateUtil.formatDate(now));


        if ("submit".equals(operationType)) {
            // 待后续审批流修改状态
            sysPureceive.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
        } else {
            sysPureceive.setIAuditStatus(AuditStatusEnum.NOT_AUDIT.getValue());
        }
        sysPureceive.setDupdatetime(now);
        sysPureceive.setBillNo(BillNoUtils.genCode(getOrgCode(), table()));
        sysPureceive.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
        sysPureceive.setIsDeleted(false);

        ValidationUtils.isTrue(sysPureceive.save(), ErrorMsg.SAVE_FAILED);

        map.put(sysPureceivedetail.getVenCode(), sysPureceive.getAutoID());

        return sysPureceive.getAutoID();
    }

    /**
     * 查询当前入库数量是否大于仓库最大存储数
     */
    public void determineQty(Record row, int i) {
        Record first = dbTemplate("syspureceive.paginateAdminDatas", Kv.by("careacode", row.getStr("poscode"))).findFirst();
        if (null != first && !"".equals(first)) {
            Integer imaxcapacity = first.getInt("imaxcapacity");
            if (null != imaxcapacity && !"".equals(imaxcapacity)) {
                BigDecimal bigDecimal = new BigDecimal(imaxcapacity);
                BigDecimal qty = row.getBigDecimal("qty");
                if (ObjUtil.isNotNull(imaxcapacity)) {
                    if (qty.compareTo(bigDecimal) == 1) {
                        ValidationUtils.error(String.format("第%d行%s现品票号实收数量超出对应库存最大存储量，请选择其他库区录入", i + 1, row.get("barcode")));
                    }
                }
            }
        }
    }

    /**
     * 更新状态
     *
     * @param autoId      主键ID
     * @param beforeState 审批前状态
     * @param afterState  审批后状态
     * @return true, 更新成功
     */
    private boolean update(String autoId, String beforeState, String afterState) {
        return update("UPDATE T_Sys_PUReceive SET iAuditStatus = ? WHERE autoid = ? AND iAuditStatus = ? ", afterState, autoId, beforeState) > 0;
    }



    //往采购订单入库主表插入
    public String installsyspuinstore(SysPureceive byId, Date now, User user, Record barcode) {
        SysPuinstore sysPuinstore = new SysPuinstore();
        sysPuinstore.setBillNo(BillNoUtils.genCode(getOrgCode(), syspuinstoreservice.table()));
        sysPuinstore.setBillDate(DateUtil.formatDate(now));
        sysPuinstore.setOrganizeCode(getOrgCode());
        sysPuinstore.setSourceBillNo(byId.getBillNo());
        sysPuinstore.setVenCode(byId.getVenCode());
        sysPuinstore.setCCreateName(user.getName());
        sysPuinstore.setDCreateTime(now);
        sysPuinstore.setCUpdateName(user.getName());
        sysPuinstore.setDUpdateTime(now);
        sysPuinstore.setWhCode(byId.getWhCode());
        sysPuinstore.setWhName(byId.getWhName());
        sysPuinstore.setIAuditStatus(0);
        sysPuinstore.setIsDeleted(false);
        sysPuinstore.setICreateBy(JBoltUserKit.getUserId());
        sysPuinstore.setBillType(barcode.getStr("ipurchasetypeid"));
        sysPuinstore.setDeptCode(barcode.getStr("cdepcode"));
        sysPuinstore.setIBusType(Integer.valueOf(barcode.getStr("ibustype")));
        sysPuinstore.setRdCode(barcode.getStr("scrdcode"));
        sysPuinstore.setIUpdateBy(JBoltUserKit.getUserId());
        syspuinstoreservice.save(sysPuinstore);
        return sysPuinstore.getAutoID();
    }

    public void check(String ids) {
        String[] split = ids.split(",");
        for (String p : split) {
            List<SysPureceive> sysPureceives = find("select *  from T_Sys_PUReceive where AutoID in (" + p + ")");
            for (SysPureceive s : sysPureceives) {
                if ("0".equals(String.valueOf(s.getIAuditStatus()))) {
                    ValidationUtils.error( "收料编号：" + s.getBillNo() + "单据未提交审核或审批！！");
                }
                if ("2".equals(String.valueOf(s.getIAuditStatus())) || "3".equals(String.valueOf(s.getIAuditStatus()))) {
                    ValidationUtils.error( "收料编号：" + s.getBillNo() + "流程已结束！！");
                }
            }
        }
    }

    public void checkbelow(String ids) {
        String[] split = ids.split(",");
        for (String p : split) {
            List<SysPureceive> sysPureceives = find("select *  from T_Sys_PUReceive where AutoID in (" + p + ")");
            for (SysPureceive s : sysPureceives) {
                if ("0".equals(String.valueOf(s.getIAuditStatus()))) {
                    ValidationUtils.error( "收料编号：" + s.getBillNo() + " 单据，流程未开始，不可反审！！");
                }
                if ("1".equals(String.valueOf(s.getIAuditStatus()))) {
                    ValidationUtils.error( "收料编号：" + s.getBillNo() + " 单据，流程未结束，不可反审！！");
                }
                //查出从表
                List<SysPureceivedetail> firstBy = syspureceivedetailservice.findFirstBy(s.getAutoID());
                if (firstBy.isEmpty()) return;
                for (SysPureceivedetail d : firstBy) {
                    SysPuinstoredetail firstByBarcode = syspuinstoredetailservice.findFirstByBarcode(d.getBarcode());
                    if (null != firstByBarcode) {
                        SysPuinstore byId = syspuinstoreservice.findById(firstByBarcode.getMasID());
                        if (null != byId) {
                            if (!"0".equals(String.valueOf(byId.getIAuditStatus()))) {
                                ValidationUtils.error( "采购入库编号：" + byId.getBillNo() + " 单据，不是未审核状态！！");
                            }
                        }
                    }
                }
                // 通过主表的入库单号 查质检单数据
                List<RcvDocQcFormM> firstBycRcvDocNo = rcvdocqcformmservice.findFirstBycRcvDocNo(s.getBillNo());
                if (null != firstBycRcvDocNo) {
                    for (RcvDocQcFormM r : firstBycRcvDocNo) {
                        if (!"0".equals(String.valueOf(r.getIStatus()))) {
                            ValidationUtils.error( "来料检 收料单号为：" + r.getCRcvDocNo() + " 单据，已生成下游单据，无法反审！！");
                        }
                    }
                }
            }
        }
    }

    //实现反审之前的操作
    public String checkbelowtwo(long formAutoId) {
        List<SysPureceive> sysPureceives = find("select *  from T_Sys_PUReceive where AutoID in (" + formAutoId + ")");
        for (SysPureceive s : sysPureceives) {
            if ("0".equals(String.valueOf(s.getIAuditStatus()))) {
                return "收料编号：" + s.getBillNo() + " 单据，流程未开始，不可反审！！";
            }
            if ("1".equals(String.valueOf(s.getIAuditStatus()))) {
                return "收料编号：" + s.getBillNo() + " 单据，流程未结束，不可反审！！";
            }
            //查出从表
            List<SysPureceivedetail> firstBy = syspureceivedetailservice.findFirstBy(s.getAutoID());
            if (firstBy.isEmpty()) return "收料编号：" + s.getBillNo() + " 从表数据不能为空！！";
            for (SysPureceivedetail d : firstBy) {
                SysPuinstoredetail firstByBarcode = syspuinstoredetailservice.findFirstByBarcode(d.getBarcode());
                if (null != firstByBarcode) {
                    SysPuinstore byId = syspuinstoreservice.findById(firstByBarcode.getMasID());
                    if (null != byId) {
                        if ("1".equals(String.valueOf(byId.getIAuditStatus())) || "2".equals(String.valueOf(byId.getIAuditStatus()))) {
                            return "采购入库编号：" + byId.getBillNo() + " 单据，已生成下游单据，无法反审！！";
                        }
                    }
                }
            }
            // 通过主表的入库单号 查质检单数据
            List<RcvDocQcFormM> firstBycRcvDocNo = rcvdocqcformmservice.findFirstBycRcvDocNo(s.getBillNo());
            if (null != firstBycRcvDocNo) {
                for (RcvDocQcFormM r : firstBycRcvDocNo) {
                    if ("2".equals(String.valueOf(r.getIStatus())) || "3".equals(String.valueOf(r.getIStatus()))) {
                        return "来料检 收料单号为：" + r.getCRcvDocNo() + " 单据，已生成下游单据，无法反审！！";
                    }
                }
            }
        }
        return null;
    }

    public void delectbelow(String ids) {
        String[] split = ids.split(",");
        for (String p : split) {
            List<SysPureceive> sysPureceives = find("select *  from T_Sys_PUReceive where AutoID in (" + p + ")");
            for (SysPureceive s : sysPureceives) {
                //查出从表
                List<SysPureceivedetail> firstBy = syspureceivedetailservice.findFirstBy(s.getAutoID());
                if (firstBy.isEmpty()) return;
                for (SysPureceivedetail d : firstBy) {
                    if ("0".equals(d.getIsInitial())) {
                        SysPuinstoredetail firstByBarcode = syspuinstoredetailservice.findFirstByBarcode(d.getBarcode());
                        if (null != firstByBarcode && null != firstByBarcode.getAutoID()) {
                            String autoID = firstByBarcode.getMasID();
                            //删除从表
                            syspuinstoredetailservice.deleteByIds(firstByBarcode.getAutoID());
                            SysPuinstore byId = syspuinstoreservice.findById(autoID);
                            List<SysPuinstoredetail> detailByMasID = syspuinstoredetailservice.findDetailByMasID(byId.getAutoID());
                            if (detailByMasID.isEmpty()) {
                                // 从表没数据才删除 主表
                                syspuinstoreservice.deleteByIds(byId.getAutoID());
                            }
                        }
                    } else {
                        // 通过主表的入库单号 查质检单数据
                        List<RcvDocQcFormM> firstBycRcvDocNo = rcvdocqcformmservice.findFirstBycRcvDocNo(s.getBillNo());
                        if (null != firstBycRcvDocNo) {
                            for (RcvDocQcFormM r : firstBycRcvDocNo) {
                                rcvdocqcformmservice.deleteByIds(String.valueOf(r.getIAutoId()));
                            }
                        }
                    }

                }
            }
        }
    }

    //反审后的操作
    public void delectbelowtwo(long formAutoId) {
        List<SysPureceive> sysPureceives = find("select *  from T_Sys_PUReceive where AutoID in (" + formAutoId + ")");
        for (SysPureceive s : sysPureceives) {
            //查出从表
            List<SysPureceivedetail> firstBy = syspureceivedetailservice.findFirstBy(s.getAutoID());
            if (firstBy.isEmpty()) return;
            for (SysPureceivedetail d : firstBy) {
                SysPuinstoredetail firstByBarcode = syspuinstoredetailservice.findFirstByBarcode(d.getBarcode());
                if (null != firstByBarcode && null != firstByBarcode.getAutoID()) {
                    String autoID = firstByBarcode.getMasID();
                    //删除从表
                    syspuinstoredetailservice.deleteByIds(firstByBarcode.getAutoID());
                    SysPuinstore byId = syspuinstoreservice.findById(autoID);
                    List<SysPuinstoredetail> detailByMasID = syspuinstoredetailservice.findDetailByMasID(byId.getAutoID());
                    if (detailByMasID.isEmpty()) {
                        // 从表没数据才删除 主表
                        syspuinstoreservice.deleteByIds(byId.getAutoID());

                    }
                }
                // 通过主表的入库单号 查质检单数据
                List<RcvDocQcFormM> firstBycRcvDocNo = rcvdocqcformmservice.findFirstBycRcvDocNo(s.getBillNo());
                if (null != firstBycRcvDocNo && firstBycRcvDocNo.size() > 0) {
                    for (RcvDocQcFormM r : firstBycRcvDocNo) {
                        rcvdocqcformmservice.deleteByIds(String.valueOf(r.getIAutoId()));
                    }
                }
            }
        }
    }

    //反审后的操作
    public void delectbelowtwo(List<Long> formAutoId) {
        for (Long q : formAutoId) {
            List<SysPureceive> sysPureceives = find("select *  from T_Sys_PUReceive where AutoID in (" + q + ")");
            for (SysPureceive s : sysPureceives) {
                //查出从表
                List<SysPureceivedetail> firstBy = syspureceivedetailservice.findFirstBy(s.getAutoID());
                if (firstBy.isEmpty()) return;
                for (SysPureceivedetail d : firstBy) {
                    if ("0".equals(d.getIsInitial())) {
                        SysPuinstoredetail firstByBarcode = syspuinstoredetailservice.findFirstByBarcode(d.getBarcode());
                        if (null != firstByBarcode && null != firstByBarcode.getAutoID()) {
                            String autoID = firstByBarcode.getMasID();
                            //删除从表
                            syspuinstoredetailservice.deleteByIds(firstByBarcode.getAutoID());
                            SysPuinstore byId = syspuinstoreservice.findById(autoID);
                            List<SysPuinstoredetail> detailByMasID = syspuinstoredetailservice.findDetailByMasID(byId.getAutoID());
                            if (detailByMasID.isEmpty()) {
                                // 从表没数据才删除 主表
                                syspuinstoreservice.deleteByIds(byId.getAutoID());

                            }
                        }
                    } else {
                        // 通过主表的入库单号 查质检单数据
                        List<RcvDocQcFormM> firstBycRcvDocNo = rcvdocqcformmservice.findFirstBycRcvDocNo(s.getBillNo());
                        if (null != firstBycRcvDocNo && firstBycRcvDocNo.size() > 0) {
                            for (RcvDocQcFormM r : firstBycRcvDocNo) {
                                rcvdocqcformmservice.deleteByIds(String.valueOf(r.getIAutoId()));
                            }
                        }
                    }

                }
            }
        }
    }


    //审核通过后的业务逻辑
    public void passage(String ids) {
        tx(() -> {
            Date now = new Date();
            User user = JBoltUserKit.getUser();
            String[] split = ids.split(",");
            for (String s : split) {
                SysPureceive byId = findById(s);

                //查从表数据
                List<SysPureceivedetail> firstBy = syspureceivedetailservice.findFirstBy(s);
                boolean once = true;
                String autoID = "";
                int i = 1;
                Long veniAutoId = vendorservice.queryAutoIdByCvencode(byId.getVenCode());
                ArrayList<RcvDocQcFormM> rcvDocQcFormM = new ArrayList<>();
                for (SysPureceivedetail f : firstBy) {
                    Kv kv = new Kv();
                    kv.set("barcode", f.getBarcode());
                    Record row = dbTemplate("syspureceive.tuibarcode", kv).findFirst();
                    // 判断存货开关是否打开,开 推来料检验单，
                    boolean button = inventoryMfgInfoService.getIsIqc1(row.getStr("cinvcode"));
                    List<RcvDocQcFormM> tempForms = new ArrayList<>();
                    Record barcode = dbTemplate("syspureceive.purchaseOrderD", Kv.by("barcode", f.getBarcode())).findFirst();
                    if (button) {
                        if (!rcvDocQcFormM.isEmpty()) {
                            for (RcvDocQcFormM formM : rcvDocQcFormM) {
                                if (String.valueOf(formM.getIInventoryId()).equals(barcode.getStr("iinventoryId"))) {
                                    formM.setIQty(this.add(formM.getIQty(), f.getQty()));
                                } else {
                                    RcvDocQcFormM rcvDocQcFormM1 = this.insertRcvDocQcFormM(barcode, byId, user, veniAutoId, 1);
                                    tempForms.add(rcvDocQcFormM1); // 将需要添加的元素放入临时列表
                                }
                            }
                        } else {
                            RcvDocQcFormM rcvDocQcFormM1 = this.insertRcvDocQcFormM(barcode, byId, user, veniAutoId, 1);
                            tempForms.add(rcvDocQcFormM1); // 将需要添加的元素放入临时列表
                        }
                        // 将临时列表中的元素添加到原始列表中
                        rcvDocQcFormM.addAll(tempForms);
                    } else {
                        if (f.getIsInitial().equals("0")) {
                            //根据id查出从表的数据，生成采购入库列表 一个收料单号对应一个入库单号。 排除是初物的数据
                            //根据条码查出 采购订单主表数据，添加到入库主表信息，然后加入从表数据（拆分 原则 是否初物字段）
                            //根据条码查询出采购订单从表以及主表信息
                            //修改主表新增(后加 缺少字段在这里补)
                            if (once) {
                                autoID = this.installsyspuinstore(byId, now, user, barcode);
                                once = false;
                            }
                            //往采购订单入库表插入信息
                            SysPuinstoredetail sysPuinstoredetail = new SysPuinstoredetail();
                            sysPuinstoredetail.setMasID(autoID);
                            sysPuinstoredetail.setSourceBillType(f.getSourceBillType());
                            sysPuinstoredetail.setSourceBillNo(f.getSourceBillNo());
                            sysPuinstoredetail.setSourceBillNoRow(f.getSourceBillNo() + "-" + i);
                            sysPuinstoredetail.setSourceBillDid(f.getSourceBillDid());
                            sysPuinstoredetail.setSourceBillID(f.getSourceBillID());
//                            sysPuinstoredetail.setRowNo(f.getRowNo());
                            sysPuinstoredetail.setWhcode(f.getWhcode());
                            sysPuinstoredetail.setPosCode(f.getPosCode());
                            sysPuinstoredetail.setQty(f.getQty());
                            sysPuinstoredetail.setRowNo(i);
                            sysPuinstoredetail.setTrackType(f.getTrackType());
                            sysPuinstoredetail.setCCreateName(user.getUsername());
                            sysPuinstoredetail.setDCreateTime(now);
                            sysPuinstoredetail.setBarCode(f.getBarcode());
                            sysPuinstoredetail.setPuUnitCode(barcode.getStr("puunitcode"));
                            sysPuinstoredetail.setPuUnitName(barcode.getStr("puunitname"));
                            sysPuinstoredetail.setIsDeleted(false);
                            sysPuinstoredetail.setInvcode(barcode.getStr("cinvcode"));
                            syspuinstoredetailservice.save(sysPuinstoredetail);
                            i++;
                        } else {
                            if (!rcvDocQcFormM.isEmpty()) {
                                for (RcvDocQcFormM formM : rcvDocQcFormM) {
                                    if (String.valueOf(formM.getIInventoryId()).equals(barcode.getStr("iinventoryId"))) {
                                        formM.setIQty(this.add(formM.getIQty(), f.getQty()));
                                    } else {
                                        RcvDocQcFormM rcvDocQcFormM1 = this.insertRcvDocQcFormM(barcode, byId, user, veniAutoId, 2);
                                        tempForms.add(rcvDocQcFormM1); // 将需要添加的元素放入临时列表
                                    }
                                }
                            } else {
                                RcvDocQcFormM rcvDocQcFormM1 = this.insertRcvDocQcFormM(barcode, byId, user, veniAutoId, 2);
                                tempForms.add(rcvDocQcFormM1); // 将需要添加的元素放入临时列表
                            }
                            // 将临时列表中的元素添加到原始列表中
                            rcvDocQcFormM.addAll(tempForms);
                        }
                    }
                }
                rcvdocqcformmservice.batchSave(rcvDocQcFormM);
            }


            return true;
        });
    }

    public Integer add(Integer in, BigDecimal bi) {
        BigDecimal bigDecimal = new BigDecimal(in.toString());
        BigDecimal add = bigDecimal.add(bi);
        return add.intValue();
    }

    /**
     * todo  审核通过
     */
    @Override
    public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
        tx(() -> {
            this.passagetwo(formAutoId);
            return true;
        });
        return null;
    }

    /**
     * 审核不通过
     */
    @Override
    public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
        return null;
    }

    /**
     * todo 实现反审之前的其他业务操作，如有异常返回错误信息
     */
    @Override
    public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        String checkbelowtwo = null;
        //最后一个节点才判断下游单据状态
//        if (isLast) {
            checkbelowtwo = this.checkbelowtwo(formAutoId);
//        }
        return checkbelowtwo;
    }

    /**
     * todo 实现反审之后的其他业务操作, 如有异常返回错误信息
     */
    @Override
    public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        if (isLast) {
            this.delectbelowtwo(formAutoId);
        }
        return null;
    }

    /**
     * 提审前业务，如有异常返回错误信息
     */
    @Override
    public String preSubmitFunc(long formAutoId) {
        return null;
    }

    /**
     * 提审后业务处理，如有异常返回错误信息
     */
    @Override
    public String postSubmitFunc(long formAutoId) {
        return null;
    }

    /**
     * 撤回审核业务处理，如有异常返回错误信息，没走完的，不用做业务出来
     */
    @Override
    public String postWithdrawFunc(long formAutoId) {
        return null;
    }

    /**
     * 从审批中，撤回到已保存，业务实现，如有异常返回错误信息
     */
    @Override
    public String withdrawFromAuditting(long formAutoId) {
        return null;
    }

    /**
     * todo 从已审核，撤回到已保存，前置业务实现，如有异常返回错误信息
     */
    @Override
    public String preWithdrawFromAuditted(long formAutoId) {
        return this.checkbelowtwo(formAutoId);
    }

    /**
     * todo 从已审核，撤回到已保存，业务实现，如有异常返回错误信息
     */
    @Override
    public String postWithdrawFromAuditted(long formAutoId) {
        this.delectbelowtwo(formAutoId);
        return null;
    }

    /**
     * todo 批量审核（审批）通过，后置业务实现
     */
    @Override
    public String postBatchApprove(List<Long> formAutoIds) {
        tx(() -> {
            this.passagetwo(formAutoIds);
            return true;
        });
        //业务逻辑

        return null;
    }

    /**
     * 批量审批（审核）不通过，后置业务实现
     */
    @Override
    public String postBatchReject(List<Long> formAutoIds) {
        return null;
    }

    /**
     * todo 批量撤销审批，后置业务实现，需要做业务出来
     */
    @Override
    public String postBatchBackout(List<Long> formAutoIds) {
        this.delectbelowtwo(formAutoIds);
        return null;
    }


    //审核通过后的业务逻辑
    public void passagetwo(List<Long> formAutoId) {
        tx(() -> {
            Date now = new Date();
            User user = JBoltUserKit.getUser();

            for (Long s : formAutoId) {
                SysPureceive byId = findById(s);
                //查从表数据
                List<SysPureceivedetail> firstBy = syspureceivedetailservice.findFirstBy(s.toString());
                boolean once = true;
                String autoID = "";
                int i = 1;
                Long veniAutoId = vendorservice.queryAutoIdByCvencode(byId.getVenCode());
                ArrayList<RcvDocQcFormM> rcvDocQcFormM = new ArrayList<>();
                for (SysPureceivedetail f : firstBy) {
                    Kv kv = new Kv();
                    kv.set("barcode", f.getBarcode());
                    Record row = dbTemplate("syspureceive.tuibarcode", kv).findFirst();
                    //判断存货开关是否打开,开 推来料检验单，
                    boolean button = inventoryMfgInfoService.getIsIqc1(row.getStr("cinvcode"));
                    List<RcvDocQcFormM> tempForms = new ArrayList<>();
                    Record barcode = dbTemplate("syspureceive.purchaseOrderD", Kv.by("barcode", f.getBarcode())).findFirst();
                    if (button) {
                        if (!rcvDocQcFormM.isEmpty()) {
                            for (RcvDocQcFormM formM : rcvDocQcFormM) {
                                if (String.valueOf(formM.getIInventoryId()).equals(barcode.getStr("iinventoryId"))) {
                                    formM.setIQty(this.add(formM.getIQty(), f.getQty()));
                                } else {
                                    RcvDocQcFormM rcvDocQcFormM1 = this.insertRcvDocQcFormM(barcode, byId, user, veniAutoId, 1);
                                    tempForms.add(rcvDocQcFormM1); // 将需要添加的元素放入临时列表
                                }
                            }
                        } else {
                            RcvDocQcFormM rcvDocQcFormM1 = this.insertRcvDocQcFormM(barcode, byId, user, veniAutoId, 1);
                            tempForms.add(rcvDocQcFormM1); // 将需要添加的元素放入临时列表
                        }
                        // 将临时列表中的元素添加到原始列表中
                        rcvDocQcFormM.addAll(tempForms);
                    } else {
                        if (f.getIsInitial().equals("0")) {
                            //根据id查出从表的数据，生成采购入库列表 一个收料单号对应一个入库单号。 排除是初物的数据
                            //根据条码查出 采购订单主表数据，添加到入库主表信息，然后加入从表数据（拆分 原则 是否初物字段）
                            //根据条码查询出采购订单从表以及主表信息
                            //修改主表新增(后加 缺少字段在这里补)
                            if (once) {
                                autoID = this.installsyspuinstore(byId, now, user, barcode);
                                once = false;
                            }
                            //往采购订单入库表插入信息
                            SysPuinstoredetail sysPuinstoredetail = new SysPuinstoredetail();
                            sysPuinstoredetail.setMasID(autoID);
                            sysPuinstoredetail.setSourceBillType(f.getSourceBillType());
                            sysPuinstoredetail.setSourceBillNo(f.getSourceBillNo());
                            sysPuinstoredetail.setSourceBillNoRow(f.getSourceBillNo() + "-" + i);
                            sysPuinstoredetail.setSourceBillDid(f.getSourceBillDid());
                            sysPuinstoredetail.setSourceBillID(f.getSourceBillID());
//                            sysPuinstoredetail.setRowNo(f.getRowNo());
                            sysPuinstoredetail.setWhcode(f.getWhcode());
                            sysPuinstoredetail.setPosCode(f.getPosCode());
                            sysPuinstoredetail.setQty(f.getQty());
                            sysPuinstoredetail.setRowNo(i);
                            sysPuinstoredetail.setTrackType(f.getTrackType());
                            sysPuinstoredetail.setCCreateName(user.getUsername());
                            sysPuinstoredetail.setDCreateTime(now);
                            sysPuinstoredetail.setBarCode(f.getBarcode());
                            sysPuinstoredetail.setPuUnitCode(barcode.getStr("puunitcode"));
                            sysPuinstoredetail.setPuUnitName(barcode.getStr("puunitname"));
                            sysPuinstoredetail.setIsDeleted(false);
                            sysPuinstoredetail.setInvcode(barcode.getStr("cinvcode"));
                            syspuinstoredetailservice.save(sysPuinstoredetail);
                            i++;
                        } else {
                            if (!rcvDocQcFormM.isEmpty()) {
                                for (RcvDocQcFormM formM : rcvDocQcFormM) {
                                    if (String.valueOf(formM.getIInventoryId()).equals(barcode.getStr("iinventoryId"))) {
                                        formM.setIQty(this.add(formM.getIQty(), f.getQty()));
                                    } else {
                                        RcvDocQcFormM rcvDocQcFormM1 = this.insertRcvDocQcFormM(barcode, byId, user, veniAutoId, 2);
                                        tempForms.add(rcvDocQcFormM1); // 将需要添加的元素放入临时列表
                                    }
                                }
                            } else {
                                RcvDocQcFormM rcvDocQcFormM1 = this.insertRcvDocQcFormM(barcode, byId, user, veniAutoId, 2);
                                tempForms.add(rcvDocQcFormM1); // 将需要添加的元素放入临时列表
                            }
                            // 将临时列表中的元素添加到原始列表中
                            rcvDocQcFormM.addAll(tempForms);
                        }
                    }
                }
                rcvdocqcformmservice.batchSave(rcvDocQcFormM);
            }


            return true;
        });
    }

    //审核通过后的业务逻辑
    public void passagetwo(Long formAutoId) {
        tx(() -> {
            Date now = new Date();
            User user = JBoltUserKit.getUser();
            SysPureceive byId = findById(formAutoId);
            //查从表数据
            List<SysPureceivedetail> firstBy = syspureceivedetailservice.findFirstBy(formAutoId.toString());
            boolean once = true;
            String autoID = "";
            int i = 1;
            Long veniAutoId = vendorservice.queryAutoIdByCvencode(byId.getVenCode());
            ArrayList<RcvDocQcFormM> rcvDocQcFormM = new ArrayList<>();
            for (SysPureceivedetail f : firstBy) {
                Kv kv = new Kv();
                kv.set("barcode", f.getBarcode());
                Record row = dbTemplate("syspureceive.tuibarcode", kv).findFirst();
                //判断存货开关是否打开,开 推来料检验单，
                boolean button = inventoryMfgInfoService.getIsIqc1(row.getStr("cinvcode"));
                List<RcvDocQcFormM> tempForms = new ArrayList<>();
                Record barcode = dbTemplate("syspureceive.purchaseOrderD", Kv.by("barcode", f.getBarcode())).findFirst();
                if (button) {
                    if (!rcvDocQcFormM.isEmpty()) {
                        for (RcvDocQcFormM formM : rcvDocQcFormM) {
                            if (String.valueOf(formM.getIInventoryId()).equals(barcode.getStr("iinventoryId"))) {
                                formM.setIQty(this.add(formM.getIQty(), f.getQty()));
                            } else {
                                RcvDocQcFormM rcvDocQcFormM1 = this.insertRcvDocQcFormM(barcode, byId, user, veniAutoId, 1);
                                tempForms.add(rcvDocQcFormM1); // 将需要添加的元素放入临时列表
                            }
                        }
                    } else {
                        RcvDocQcFormM rcvDocQcFormM1 = this.insertRcvDocQcFormM(barcode, byId, user, veniAutoId, 1);
                        tempForms.add(rcvDocQcFormM1); // 将需要添加的元素放入临时列表
                    }
                    // 将临时列表中的元素添加到原始列表中
                    rcvDocQcFormM.addAll(tempForms);
                } else {
                    if (f.getIsInitial().equals("0")) {
                        //根据id查出从表的数据，生成采购入库列表 一个收料单号对应一个入库单号。 排除是初物的数据
                        //根据条码查出 采购订单主表数据，添加到入库主表信息，然后加入从表数据（拆分 原则 是否初物字段）
                        //根据条码查询出采购订单从表以及主表信息
                        //修改主表新增(后加 缺少字段在这里补)
                        if (once) {
                            autoID = this.installsyspuinstore(byId, now, user, barcode);
                            once = false;
                        }
                        //往采购订单入库表插入信息
                        SysPuinstoredetail sysPuinstoredetail = new SysPuinstoredetail();
                        sysPuinstoredetail.setMasID(autoID);
                        sysPuinstoredetail.setSourceBillType(f.getSourceBillType());
                        sysPuinstoredetail.setSourceBillNo(f.getSourceBillNo());
                        sysPuinstoredetail.setSourceBillNoRow(f.getSourceBillNo() + "-" + i);
                        sysPuinstoredetail.setSourceBillDid(f.getSourceBillDid());
                        sysPuinstoredetail.setSourceBillID(f.getSourceBillID());
//                            sysPuinstoredetail.setRowNo(f.getRowNo());
                        sysPuinstoredetail.setWhcode(f.getWhcode());
                        sysPuinstoredetail.setPosCode(f.getPosCode());
                        sysPuinstoredetail.setQty(f.getQty());
                        sysPuinstoredetail.setRowNo(i);
                        sysPuinstoredetail.setTrackType(f.getTrackType());
                        sysPuinstoredetail.setCCreateName(user.getUsername());
                        sysPuinstoredetail.setDCreateTime(now);
                        sysPuinstoredetail.setBarCode(f.getBarcode());
                        sysPuinstoredetail.setPuUnitCode(barcode.getStr("puunitcode"));
                        sysPuinstoredetail.setPuUnitName(barcode.getStr("puunitname"));
                        sysPuinstoredetail.setIsDeleted(false);
                        sysPuinstoredetail.setInvcode(barcode.getStr("cinvcode"));
                        syspuinstoredetailservice.save(sysPuinstoredetail);
                        i++;
                    } else {
                        if (!rcvDocQcFormM.isEmpty()) {
                            for (RcvDocQcFormM formM : rcvDocQcFormM) {
                                if (String.valueOf(formM.getIInventoryId()).equals(barcode.getStr("iinventoryId"))) {
                                    formM.setIQty(this.add(formM.getIQty(), f.getQty()));
                                } else {
                                    RcvDocQcFormM rcvDocQcFormM1 = this.insertRcvDocQcFormM(barcode, byId, user, veniAutoId, 2);
                                    tempForms.add(rcvDocQcFormM1); // 将需要添加的元素放入临时列表
                                }
                            }
                        } else {
                            RcvDocQcFormM rcvDocQcFormM1 = this.insertRcvDocQcFormM(barcode, byId, user, veniAutoId, 2);
                            tempForms.add(rcvDocQcFormM1); // 将需要添加的元素放入临时列表
                        }
                        // 将临时列表中的元素添加到原始列表中
                        rcvDocQcFormM.addAll(tempForms);
                    }
                }
            }
            rcvdocqcformmservice.batchSave(rcvDocQcFormM);


            return true;
        });
    }
}

