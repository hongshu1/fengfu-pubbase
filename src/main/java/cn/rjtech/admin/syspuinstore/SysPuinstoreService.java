package cn.rjtech.admin.syspuinstore;

import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventorychange.InventoryChangeService;
import cn.rjtech.admin.materialsout.MaterialsOutService;
import cn.rjtech.admin.materialsoutdetail.MaterialsOutDetailService;
import cn.rjtech.admin.purchaseorderm.PurchaseOrderMService;
import cn.rjtech.admin.purchasetype.PurchaseTypeService;
import cn.rjtech.admin.rdstyle.RdStyleService;
import cn.rjtech.admin.sysassem.SysAssemService;
import cn.rjtech.admin.sysassem.SysAssemdetailService;
import cn.rjtech.admin.userthirdparty.UserThirdpartyService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDTO;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDTO.Main;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDTO.PreAllocate;
import cn.rjtech.util.BaseInU8Util;
import cn.rjtech.util.ValidationUtils;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 采购入库单
 *
 * @ClassName: SysPuinstoreService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 15:38
 */
public class SysPuinstoreService extends BaseService<SysPuinstore> {

    private final SysPuinstore dao = new SysPuinstore().dao();

    @Override
    protected SysPuinstore dao() {
        return dao;
    }

    @Inject
    private SysPuinstoredetailService syspuinstoredetailservice;
    @Inject
    private PurchaseTypeService       purchaseTypeService;
    @Inject
    private RdStyleService            rdStyleService;
    @Inject
    private VendorService             vendorService;
    @Inject
    private PurchaseOrderMService     purchaseOrderMService;
    @Inject
    private WarehouseService          warehouseService;
    @Inject
    private DictionaryService         dictionaryService;
    @Inject
    private MaterialsOutService       materialsOutService; //材料出库单
    @Inject
    private MaterialsOutDetailService materialsOutDetailService;//材料出库单从表
    @Inject
    private SysAssemService           sysAssemService;//形态转换单主表
    @Inject
    private SysAssemdetailService     sysAssemdetailService;//形态转换单从表
    @Inject
    private InventoryChangeService    changeService;//物料形态对照表
    @Inject
    private InventoryService          inventoryService;

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("syspuinstore.recpor", kv).paginate(pageNumber, pageSize);
        for (Record record : paginate.getList()) {
            Kv podetailKv = new Kv();
            podetailKv.set("vencode", record.get("vencode"));
            podetailKv.set("billno", record.get("billno"));
            podetailKv.set("sourcebillno", record.get("sourcebillno"));
            podetailKv.set("sourcebillid", record.get("sourcebillid"));
            record.set("ibustype", findIBusTypeKey(record.getStr("ibustype")));
        }
        return paginate;
    }

    /*
     * 业务类型
     * */
    public String findIBusTypeKey(String ibustype) {
        if (StrUtil.isNotBlank(ibustype)) {
            List<Dictionary> dictionaryList = dictionaryService.getOptionListByTypeKey("purchase_business_type");
            Dictionary dictionary = dictionaryList.stream().filter(e -> e.getSn().equals(ibustype))
                .findFirst()
                .orElse(new Dictionary());
            return dictionary.getName();

        }
        return "";
    }

    /*
     * 查询edit和onlysee的数据
     * */
    public Record findEditAndOnlySeeByAutoid(String autoid) {
        return dbTemplate("syspuinstore.findEditAndOnlySeeByAutoid", Kv.by("autoid", autoid)).findFirst();
    }


    /**
     * 保存
     */
    public Ret save(SysPuinstore sysPuinstore) {
        if (sysPuinstore == null || isOk(sysPuinstore.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(sysPuinstore.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysPuinstore.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(sysPuinstore.getAutoID(), JBoltUserKit.getUserId(), sysPuinstore.getName());
        }
        return ret(success);
    }

    /*
     * 回到上一步
     * */
    public Ret backStep(SysPuinstore puinstore) {
        Integer iAuditStatus = puinstore.getIAuditStatus();
        puinstore.setIAuditStatus(iAuditStatus - 1);
        tx(() -> {
            puinstore.setAuditPerson(JBoltUserKit.getUserName());
            puinstore.setModifyPerson(JBoltUserKit.getUserName());
            puinstore.setModifyDate(new Date());
            update(puinstore);
            return true;
        });
        return ret(true);
    }

    /*
     * 批量审批
     * */
    public Ret autitByIds(String ids) {
        tx(() -> {
            String[] split = ids.split(",");
            ArrayList<SysPuinstore> puinstoreList = new ArrayList<>();
            Date date = new Date();
            for (String id : split) {
                commonAutitByIds(id, puinstoreList, date);
            }
            //1、批量反审核成功
            batchUpdate(puinstoreList);

            //2、检查是否要生成材料出库单和形态转换单
            checkIsCreateMaterialAndSysAssem(puinstoreList);
            return true;
        });
        return ret(true);
    }

    /*
     * 检查是否要生成材料出库单和形态转换单
     * */
    public void checkIsCreateMaterialAndSysAssem(List<SysPuinstore> puinstoreList) {
        boolean isTwo = false; //单单位、或双单位
        boolean isOutRdCode = false; //入库类型是否为“外作品入库”
        boolean puUnitName = false;//采购单位是否为：KG
        for (SysPuinstore puinstore : puinstoreList) {
            List<SysPuinstoredetail> detailList = syspuinstoredetailservice.findDetailByMasID(puinstore.getAutoID());
            if (!detailList.isEmpty()) {
                SysPuinstoredetail sysPuinstoredetail = detailList.get(0);
                isTwo       = CheckIsTwo(sysPuinstoredetail.getInvCode());
                isOutRdCode = checkIsOutRdCode(puinstore.getRdCode());

                if ("KG".equals(sysPuinstoredetail.getPuUnitName())) {
                    puUnitName = true;
                }
            }

            //2、如果入库类型是外作品入库并且为单单位时：采购入库单“审核通过”生成材料出库单，并且倒扣加工供应商仓库存
            if (isOutRdCode && !isTwo) {
                //2.1、材料出库单
                List<MaterialsOutDetail> materialsOutDetailList = new ArrayList<>();
                MaterialsOut materials = new MaterialsOut();
                createMaterialsOut(puinstore, detailList, materials, materialsOutDetailList);
                materialsOutService.save(materials);
                materialsOutDetailService.batchSave(materialsOutDetailList);

                //todo 2.2、倒扣加工供应商仓库存
            }
            //3、如果单位为重量KG和双单位：采购入库单“审核通过”同时生成材料出库单、形态转换单
            if (puUnitName && isTwo) {
                //3.1、材料出库单
                List<MaterialsOutDetail> materialsOutDetailList = new ArrayList<>();
                MaterialsOut materials = new MaterialsOut();
                createMaterialsOut(puinstore, detailList, materials, materialsOutDetailList);
                materialsOutService.save(materials);
                materialsOutDetailService.batchSave(materialsOutDetailList);

                //todo 3.2、倒扣加工供应商仓库存

                //3.3、形态转换单
                SysAssem sysAssem = new SysAssem();
                List<SysAssemdetail> sysAssemdetailList = new ArrayList<>();
                createSysAssem(sysAssem, detailList, puinstore, sysAssemdetailList);
                sysAssemService.save(sysAssem);
                sysAssemdetailService.batchSave(sysAssemdetailList);
            }
        }
    }

    public boolean CheckIsTwo(String invcode) {
        //1、区分单单位、双单位：基础档案-物料建模-物料形态转换对照表，有维护转换前、转换后的存货就是双单位
        Inventory inventory = inventoryService.findByiInventoryCode(invcode);
        List<Record> changeRecordList = changeService.findInventoryChangeByInventoryId(null != inventory ?
            inventory.getIAutoId() : new Long(0));
        //双单位
        if (!changeRecordList.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean checkIsOutRdCode(String rdcode) {
        RdStyle rdStyle = rdStyleService.findBycSTCode(rdcode);
        if (rdStyle != null && "外作品入库".equals(rdStyle.getCRdName())) {
            return true;
        }
        return false;
    }

    /*
     * 1、外作件入库后倒扣加工供应商仓库存，按物料清单生成材料出库单
     * */
    public void createMaterialsOut(SysPuinstore puinstore, List<SysPuinstoredetail> detailList, MaterialsOut materials,
                                   List<MaterialsOutDetail> materialsOutDetailList) {
        //主表
        materialsOutService.saveMaterialsOutModel(materials, puinstore);
        //从表
        for (SysPuinstoredetail puinstoredetail : detailList) {
            MaterialsOutDetail materialsOutDetail = materialsOutDetailService
                .saveMaterialsOutDetailModel(puinstoredetail, materials.getAutoID());
            materialsOutDetailList.add(materialsOutDetail);
        }
    }

    /*
     * 2、如果此物料单位为重量KG，需要将重量KG转换成数量PCS，从“物料形态转换对照表”中取出对应存货编码及转换率，自动生成形态转换单
     * 创建sysAssemService、sysAssemdetailService
     * */
    public void createSysAssem(SysAssem sysAssem, List<SysPuinstoredetail> detailList,
                               SysPuinstore puinstore, List<SysAssemdetail> sysAssemdetailList) {
        sysAssemService.commonSaveSysAssemModel(sysAssem, puinstore);//主表
        for (SysPuinstoredetail puinstoredetail : detailList) {
            SysAssemdetail sysAssemdetail = sysAssemdetailService
                .saveSysAssemdetailModel(puinstoredetail, sysAssem.getAutoID());//从表
            sysAssemdetailList.add(sysAssemdetail);
        }
    }

    /*
     * 批量审核的公共方法
     * */
    public void commonAutitByIds(String id, ArrayList<SysPuinstore> puinstoreList, Date date) {
        SysPuinstore puinstore = findById(id);
        Integer iAuditStatus = puinstore.getIAuditStatus();
        if (AuditStatusEnum.AWAIT_AUDIT.getValue() == iAuditStatus) {//待审核状态
            //同步U8
            String json = getSysPuinstoreDto(puinstore);
            String post = new BaseInU8Util().base_in(json);
            System.out.println(post);

            // 状态改为已审核
            puinstore.setAuditPerson(JBoltUserKit.getUserName());
            puinstore.setAuditDate(date);
            puinstore.setModifyPerson(JBoltUserKit.getUserName());
            puinstore.setModifyDate(date);
            puinstore.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
            PurchaseOrderM purchaseOrderM = findU8BillNo(puinstore);
            puinstore.setU8BillNo(purchaseOrderM != null ? purchaseOrderM.getCDocNo() : "");
            //
            puinstoreList.add(puinstore);
        }
    }

    /*
     * 批量反审批
     * */
    public Ret resetAutitByIds(String ids) {
        tx(() -> {
            String[] split = ids.split(",");
            ArrayList<SysPuinstore> puinstoreList = new ArrayList<>();
            Date date = new Date();
            for (String id : split) {
                commonResetAutitById(id, puinstoreList, date);
            }
            batchUpdate(puinstoreList);
            return true;
        });
        return ret(true);
    }

    /*
     * 反审批
     * */
    public Ret resetAutitById(String autoid) {
        Date date = new Date();
        boolean tx = tx(() -> {
            List<SysPuinstore> puinstoreList = new ArrayList<>();
            commonResetAutitById(autoid, puinstoreList, date);
            //
            batchUpdate(puinstoreList);
            return true;
        });
        return ret(tx);
    }

    /*
     * 批量反审核的公共方法
     * */
    public void commonResetAutitById(String autoid, List<SysPuinstore> puinstoreList, Date date) {
        SysPuinstore puinstore = findById(autoid);
        Integer iAuditStatus = puinstore.getIAuditStatus();
        String userName = JBoltUserKit.getUserName();
        if (AuditStatusEnum.APPROVED.getValue() == iAuditStatus) { //审核通过状态改为待审核
            puinstore.setU8BillNo("");//将u8的单据号置为空
            puinstore.setAuditPerson(userName);
            puinstore.setAuditDate(date);
            puinstore.setModifyPerson(userName);
            puinstore.setModifyDate(date);
            puinstore.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());//退回待审核
            //
            puinstoreList.add(puinstore);
        }
    }

    /*
     * 编辑页面的审批
     * */
    public Ret editAutit(Long autoid) {
        SysPuinstore sysPuinstore = findById(autoid);
        //1、更新审核人、审核时间、状态
        boolean tx = tx(() -> {
            if (sysPuinstore.getIAuditStatus() == AuditStatusEnum.NOT_AUDIT.getValue()) {
                Date date = new Date();
                sysPuinstore.setAuditPerson(JBoltUserKit.getUserName());
                sysPuinstore.setAuditDate(date);//审核日期
                sysPuinstore.setModifyPerson(JBoltUserKit.getUserName());
                sysPuinstore.setModifyDate(date);
                sysPuinstore.setIAuditStatus(sysPuinstore.getIAuditStatus() + 1);
                Ret ret = update(sysPuinstore);
                //2、同步u8
                /*String json = getSysPuinstoreDto(sysPuinstore);
                String post = new BaseInU8Util().base_in(json);
                System.out.println(post);*/
            }
            return true;
        });

        return ret(tx);
    }

    /*
     * 查看页面的审批
     * */
    public Ret onlyseeAutit(Long autoid) {
        SysPuinstore sysPuinstore = findById(autoid);
        Date date = new Date();
        //1、更新审核人、审核时间、状态
        boolean tx = tx(() -> {

            sysPuinstore.setAuditPerson(JBoltUserKit.getUserName());
            sysPuinstore.setAuditDate(date);//审核日期
            sysPuinstore.setModifyPerson(JBoltUserKit.getUserName());
            sysPuinstore.setModifyDate(date);
            update(sysPuinstore);

            //2、同步于U8
            String json = getSysPuinstoreDto(sysPuinstore);
            String post = new BaseInU8Util().base_in(json);
            System.out.println(post);

            //3、如果成功，给u8的单据号；不成功，把单据号置为空，状态改为审核不通过
            PurchaseOrderM purchaseOrderM = findU8BillNo(sysPuinstore);
            sysPuinstore.setU8BillNo(purchaseOrderM != null ? purchaseOrderM.getCDocNo() : "");

            return true;
        });

        return ret(tx);
    }

    /**
     * 更新
     */
    public Ret update(SysPuinstore sysPuinstore) {
        if (sysPuinstore == null || notOk(sysPuinstore.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        SysPuinstore dbSysPuinstore = findById(sysPuinstore.getAutoID());
        if (dbSysPuinstore == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(sysPuinstore.getName(), sysPuinstore.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysPuinstore.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(sysPuinstore.getAutoID(), JBoltUserKit.getUserId(), sysPuinstore.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysPuinstore 要删除的model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(SysPuinstore sysPuinstore, Kv kv) {
        //addDeleteSystemLog(sysPuinstore.getAutoID(), JBoltUserKit.getUserId(),sysPuinstore.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysPuinstore model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    public String checkInUse(SysPuinstore sysPuinstore, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(SysPuinstore sysPuinstore, String column, Kv kv) {
        //addUpdateSystemLog(sysPuinstore.getAutoID(), JBoltUserKit.getUserId(), sysPuinstore.getName(),"的字段["+column+"]值:"+sysPuinstore.get(column));
        /**
         switch(column){
         case "IsDeleted":
         break;
         }
         */
        return null;
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        //从表的数据
        Ret ret = deleteSysPuinstoredetailByMasId(String.valueOf(id));
        if (ret.isFail()) {
            return fail(JBoltMsg.FAIL);
        }
        //删除主表数据
        deleteById(id);
        return ret(true);
    }

    public Ret deleteSysPuinstoredetailByMasId(String id) {
        List<SysPuinstoredetail> puinstoredetails = syspuinstoredetailservice.findDetailByMasID(id);
        boolean tx = true;
        if (!puinstoredetails.isEmpty()) {
            tx = tx(() -> {
                List<String> collect = puinstoredetails.stream().map(SysPuinstoredetail::getAutoID).collect(Collectors.toList());
                syspuinstoredetailservice.deleteByIds(String.join(",", collect));
                return true;
            });
        }
        return ret(tx);
    }

    /**
     * 批量删除主从表
     */
    public Ret deleteRmRdByIds(String ids) {
        tx(() -> {
            String[] split = ids.split(",");
            for (String id : split) {
                //删除从表
                deleteSysPuinstoredetailByMasId(id);
            }
            //删除主表
            deleteByIds(split);
            return true;
        });
        return ret(true);
    }

    /**
     * 执行JBoltTable表格整体提交
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        if (jBoltTable.getFormRecord() == null) {
            return fail(JBoltMsg.JBOLTTABLE_IS_BLANK);
        }
        SysPuinstore sysPuinstore = jBoltTable.getFormModel(SysPuinstore.class);
        Record record = jBoltTable.getFormRecord();
        //Record detailByParam = findSysPODetailByParam(getKv(record.get("billno"), record.get("sourcebillno")));

        List<Record> saveRecordList = jBoltTable.getSaveRecordList();
        List<Record> updateRecordList = jBoltTable.getUpdateRecordList();

        boolean tx = tx(() -> {
            //1、更新主表
            if (isOk(sysPuinstore.getAutoID())) {
                //更新主表
                SysPuinstore updatePuinstore = findById(sysPuinstore.getAutoID());

                updatePuinstore.setModifyPerson(JBoltUserKit.getUserName());
                updatePuinstore.setModifyDate(new Date());
                saveSysPuinstoreModel(updatePuinstore, record);
                Ret update = update(updatePuinstore);
                if (update.isFail()) {
                    return false;
                }
            } else {
                //2、新增主表
                SysPuinstore savePuinstore = new SysPuinstore();
                savePuinstore.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
                savePuinstore.setCreatePerson(JBoltUserKit.getUserName());
                savePuinstore.setCreateDate(new Date());
                saveSysPuinstoreModel(savePuinstore, record);
                ValidationUtils.isTrue(savePuinstore.save(), JBoltMsg.FAIL);
            }

            if (!saveRecordList.isEmpty()) {
                List<SysPuinstoredetail> saveList = new ArrayList<>();
                saveRecordList(saveRecordList, saveList, sysPuinstore);
                syspuinstoredetailservice.batchSave(saveList);
            }
            if (!updateRecordList.isEmpty()) {
                List<SysPuinstoredetail> updateList = new ArrayList<>();
                updateRecordList(updateRecordList, updateList, sysPuinstore);
                syspuinstoredetailservice.batchUpdate(updateList);
            }

            return true;
        });


        /*tx(() -> {
            //1、如果存在autoid，更新
            if (isOk(sysPuinstore.getAutoID())) {
                //更新主表
                SysPuinstore updateSysPuinstore = findById(sysPuinstore.getAutoID());
                Date date = new Date();
                String userName = JBoltUserKit.getUserName();
                updateSysPuinstore.setModifyPerson(userName);
                updateSysPuinstore.setModifyDate(date);
                saveSysPuinstoreModel(updateSysPuinstore, record, detailByParam);
                Ret update = update(updateSysPuinstore);
                if (update.isFail()) {
                    return false;
                }

                //更新明细表
                List<Record> updateRecordList = jBoltTable.getUpdateRecordList();
                if (updateRecordList == null) {
                    return false;
                }
                List<SysPuinstoredetail> detailList = new ArrayList<>();
                for (int i = 0; i < updateRecordList.size(); i++) {
                    Record updateRecord = updateRecordList.get(i);
                    SysPuinstoredetail detail = syspuinstoredetailservice.findById(updateRecord.get("autoid"));
                    if (null != detail) {
                        detail.setModifyDate(date);
                        detail.setModifyPerson(userName);
                        syspuinstoredetailservice
                            .savedetailModel2(detail, updateSysPuinstore, updateRecord, whcode, detailByParam);
                        syspuinstoredetailservice.update(detail);
                    } else {
                        syspuinstoredetailservice.saveSysPuinstoredetailModel(detailList, updateRecord,
                            updateSysPuinstore, whcode, (i + 1), detailByParam);
                    }
                }
                if (!detailList.isEmpty()) {
                    ValidationUtils.isTrue(syspuinstoredetailservice.batchSave(detailList).length != 0, "保存采购入库单失败");
                }
                //外作件入库后倒扣加工供应商仓库存，按物料清单生成材料出库单
                try {
                    createMaterialsOut(updateSysPuinstore, whcode, detailByParam);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                List<Record> saveRecordList = jBoltTable.getSaveRecordList();
                if (saveRecordList == null) {
                    return false;
                }
                //2、否则新增
                //2.1、新增主表
                SysPuinstore puinstore = new SysPuinstore();
                puinstore.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
                saveSysPuinstoreModel(puinstore, record, detailByParam);
                ValidationUtils.isTrue(puinstore.save(), JBoltMsg.FAIL);
                //2.2、新增明细表
                List<SysPuinstoredetail> detailList = new ArrayList<>();
                for (int i = 0; i < saveRecordList.size(); i++) {
                    Record detailRecord = saveRecordList.get(i);
                    syspuinstoredetailservice
                        .saveSysPuinstoredetailModel(detailList, detailRecord, puinstore, whcode, (i + 1), detailByParam);
                }
                if (!detailList.isEmpty()) {
                    ValidationUtils.isTrue(syspuinstoredetailservice.batchSave(detailList).length != 0, "保存采购入库单失败");
                }
                //外作件入库后倒扣加工供应商仓库存，按物料清单生成材料出库单
                try {
                    createMaterialsOut(puinstore, whcode, detailByParam);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //
            }
            return true;
        });*/
        return ret(tx);
    }

    /*
     * 保存明细表
     * */
    public void saveRecordList(List<Record> saveRecordList, List<SysPuinstoredetail> puinstoredetailList,
                               SysPuinstore sysPuinstore) {
        int i = 1;
        for (Record detailRecord : saveRecordList) {
            SysPuinstoredetail puinstoredetail = new SysPuinstoredetail();
            puinstoredetail.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
            syspuinstoredetailservice.saveSysPuinstoredetailModel(puinstoredetail, detailRecord, sysPuinstore, i);
            puinstoredetailList.add(puinstoredetail);
            i++;
        }
    }

    /*
     * 更新明细表
     * */
    public void updateRecordList(List<Record> updateRecordList, List<SysPuinstoredetail> puinstoredetailList,
                                 SysPuinstore sysPuinstore) {
        int i = 1;
        for (Record updateRecord : updateRecordList) {
            SysPuinstoredetail puinstoredetail = syspuinstoredetailservice.findById(updateRecord.get("autoid"));
            puinstoredetail.setModifyDate(new Date());
            puinstoredetail.setModifyPerson(JBoltUserKit.getUserName());
            syspuinstoredetailservice.saveSysPuinstoredetailModel(puinstoredetail, updateRecord, sysPuinstore, i);
            puinstoredetailList.add(puinstoredetail);
            i++;
        }
    }


    public Kv getKv(String billno, String sourcebillno) {
        Kv kv = new Kv();
        kv.set("billno", billno);
        kv.set("sourcebillno", sourcebillno);
        return kv;
    }

    public void saveSysPuinstoreModel(SysPuinstore puinstore, Record record) {
        puinstore.setBillType(record.get("billtype"));//采购类型
        puinstore.setOrganizeCode(getOrgCode());
        puinstore.setBillNo(UUID.randomUUID().toString());//入库单号，先自己生成
        puinstore.setBillDate(record.get("billdate")); //单据日期
        puinstore.setRdCode(record.get("rdcode"));
        puinstore.setDeptCode(record.get("deptcode"));
        puinstore.setSourceBillNo(record.get("sourcebillno"));//来源单号（订单号）
        puinstore.setSourceBillID(record.getStr("sourcebillid"));//来源单据id
        puinstore.setVenCode(record.get("vencode")); //供应商
        puinstore.setMemo(record.get("memo"));
        //puinstore.setAuditPerson(getOrgName()); //审核人
        //puinstore.setAuditDate(date); //审核时间
        puinstore.setIAuditStatus(0);//
        puinstore.setWhCode(record.getStr("whcode"));
        Warehouse warehouse = warehouseService.findByCwhcode(record.getStr("whcode"));
        puinstore.setWhName(warehouse != null ? warehouse.getCWhName() : "");
        puinstore.setIAuditWay(1);//审批方式：1. 审核 2. 审批流
        puinstore.setIsDeleted(false);
        puinstore.setIBusType(record.getInt("ibustype"));//业务类型

    }

    /*
     * 获取mes采购订单视图
     * */
    public Page<Record> getMesSysPODetails(Kv kv, int size, int PageSize) {
        return dbTemplate("syspuinstore.getMesSysPODetails", kv).paginate(size, PageSize);
    }

    /*
     * 获取仓库名
     * */
    public List<Record> getWareHouseName(Kv kv) {
        return dbTemplate(u8SourceConfigName(), "syspuinstore.getWareHouseName", kv).find();
    }

    /*
     * 组推送u8的json
     * */
    public String getSysPuinstoreDto(SysPuinstore puinstore) {
        SysPuinstoreDTO dto = new SysPuinstoreDTO();
        //主数据
        ArrayList<Main> MainData = new ArrayList<>();
        Kv kv = new Kv();
        kv.set("billno", puinstore.getBillNo());
        kv.set("sourcebillno", puinstore.getSourceBillNo());
        kv.set("sourcebilldid", puinstore.getSourceBillID());
        kv.set("deptcode", puinstore.getDeptCode());

        User user = JBoltUserKit.getUser();
        Vendor vendor = vendorService.findByCode(puinstore.getVenCode());
        List<SysPuinstoredetail> detailList = syspuinstoredetailservice.findDetailByMasID(puinstore.getAutoID());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int i = 1;
        for (SysPuinstoredetail detail : detailList) {
            Record record = findPurchaseOrderDBatchByCBarcode(detail.getSpotTicket());

            Main main = new Main();
            main.setInvName(record.getStr("cinvname"));
            main.setInvCode(record.getStr("cinvcode"));
            main.setBillID(puinstore.getAutoID());//主表id
            main.setBillDid(detail.getAutoID());//明细表id
            main.setBillNoRow(puinstore.getBillNo() + "-" + i);//主表billno-明细表rowno

            main.setIsWhpos("1"); //
            main.setIwhcode(detail.getWhcode());
            main.setVenName(null != vendor ? vendor.getCVenName() : "");
            main.setVenCode(puinstore.getVenCode());
            main.setQty(detail.getQty().stripTrailingZeros().toPlainString());
            main.setOrganizeCode(getOrgCode());
            main.setNum(0);
            main.setIndex(String.valueOf(i));
            main.setPackRate("0");
            main.setISsurplusqty(false);
            main.setCreatePerson(detail.getCreatePerson());
            main.setBarCode(detail.getSpotTicket()); //现品票
            main.setBillNo(puinstore.getBillNo());
            main.setBillDate(puinstore.getBillDate());
            main.setSourceBillNo(detail.getSourceBillNo());
            main.setSourceBillDid(detail.getSourceBillDid());
            main.setSourceBillType(detail.getSourceBillType());
            main.setSourceBillNoRow(detail.getSourceBillNoRow());
            main.setTag("PUInStore");
            main.setIcRdCode(puinstore.getRdCode());
            main.setIposcode(detail.getPosCode()); //库区
            MainData.add(main);
            i = i + 1;
        }

        //其它数据
        PreAllocate preAllocate = new PreAllocate();
        preAllocate.setCreatePerson(user.getUsername());
        preAllocate.setCreatePersonName(puinstore.getCreatePerson());
        preAllocate.setLoginDate(format.format(puinstore.getCreateDate()));
        preAllocate.setOrganizeCode(puinstore.getOrganizeCode());
        preAllocate.setTag("PUInStore");
        preAllocate.setType("PUInStore");
        preAllocate.setUserCode(user.getUsername());
        //放入dto
        dto.setMainData(MainData);
        dto.setPreAllocate(preAllocate);
        dto.setUserCode(user.getUsername());
        dto.setOrganizeCode(puinstore.getOrganizeCode());
        dto.setToken("");
        //返回
        return JSON.toJSONString(dto);
    }


    public Object printData(Kv kv) {
        List<Record> recordList = dbTemplate("syspuinstore.getPrintData", kv).find();
        return recordList;
    }

    public Record findPurchaseOrderDBatchByCBarcode(String cbarcode) {
        return dbTemplate("syspuinstore.findPurchaseOrderDBatchByCBarcode", Kv.by("cbarcode", cbarcode)).findFirst();
    }

    public PurchaseOrderM findU8BillNo(SysPuinstore puinstore) {
        return purchaseOrderMService.findByCOrerNo(puinstore.getSourceBillNo());
    }

}