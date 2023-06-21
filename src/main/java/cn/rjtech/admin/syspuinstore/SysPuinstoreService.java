package cn.rjtech.admin.syspuinstore;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventorychange.InventoryChangeService;
import cn.rjtech.admin.materialsout.MaterialsOutService;
import cn.rjtech.admin.materialsoutdetail.MaterialsOutDetailService;
import cn.rjtech.admin.purchaseorderm.PurchaseOrderMService;
import cn.rjtech.admin.purchasetype.PurchaseTypeService;
import cn.rjtech.admin.rdstyle.RdStyleService;
import cn.rjtech.admin.sysassem.SysAssemService;
import cn.rjtech.admin.sysassem.SysAssemdetailService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDTO;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDTO.Main;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDTO.PreAllocate;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDeleteDTO;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDeleteDTO.data;
import cn.rjtech.util.BaseInU8Util;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 采购入库单
 *
 * @ClassName: SysPuinstoreService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 15:38
 */
public class SysPuinstoreService extends BaseService<SysPuinstore> implements IApprovalService {

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
    @Inject
    private FormApprovalService       formApprovalService;

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
            record.set("ibustype", findIBusTypeKey(record.getStr("ibustype"), "purchase_business_type"));
        }
        return paginate;
    }

    /*
     * 字典维护的数据
     * */
    public String findIBusTypeKey(String ibustype, String key) {
        if (StrUtil.isNotBlank(ibustype)) {
            List<Dictionary> dictionaryList = dictionaryService.getOptionListByTypeKey(key);
            Dictionary dictionary = dictionaryList.stream().filter(e -> e.getSn().equals(ibustype))
                .findFirst()
                .orElse(new Dictionary());
            return dictionary.getName();

        }
        return "";
    }

    public SysPuinstoredetail getSourceBillType(String masid) {
        return syspuinstoredetailservice.findFirst("select * from T_Sys_PUInStoreDetail where masid = ? ", masid);
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
                isTwo       = CheckIsTwo(sysPuinstoredetail.getInvcode());
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
        String sourceBillType = "";
        String sourceBillDid = "";
        if (!detailList.isEmpty()) {
            sourceBillType = detailList.get(0).getSourceBillType();
            sourceBillDid  = detailList.get(0).getSourceBillDid();
        }
        //主表
        materialsOutService.saveMaterialsOutModel(materials, puinstore, sourceBillType, sourceBillDid);
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
            String u8Billno = new BaseInU8Util().base_in(json);
            LOG.info(u8Billno);

            // 状态改为已审核
            puinstore.setCAuditName(JBoltUserKit.getUserName());
            puinstore.setAuditDate(date);
            puinstore.setCUpdateName(JBoltUserKit.getUserName());
            puinstore.setDUpdateTime(date);
            puinstore.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
            //PurchaseOrderM purchaseOrderM = findU8BillNo(puinstore);
            puinstore.setU8BillNo(u8Billno);
            puinstore.setIAuditWay(1);
            //
            puinstoreList.add(puinstore);
        }
    }

    /*
     * 反审批
     * */
    public Ret resetAutitById(String autoid) {
        Date date = new Date();
        SysPuinstore puinstore = findById(autoid);
        if (puinstore.getIAuditStatus() != AuditStatusEnum.APPROVED.getValue() &&
            puinstore.getIAuditStatus() != AuditStatusEnum.REJECTED.getValue()) {
            ValidationUtils.isTrue(false, "只有已审核或审核不通过的状态才可以反审核");
        }
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
        //打u8接口，通知u8删除单据，然后更新mom平台的数据
        String json = getSysPuinstoreDeleteDTO(puinstore.getU8BillNo());
        String post = new BaseInU8Util().deleteVouchProcessDynamicSubmitUrl(json);
        LOG.info(post);
        //
        User user = JBoltUserKit.getUser();
        puinstore.setU8BillNo(null);//将u8的单据号置为空
        puinstore.setCAuditName(user.getUsername());
        puinstore.setAuditDate(date);
        puinstore.setCUpdateName(user.getUsername());
        puinstore.setDUpdateTime(date);
        puinstore.setIUpdateBy(JBoltUserKit.getUserId());
        puinstore.setIAuditStatus(0);//退回待审核，0：反审核状态
        //
        puinstoreList.add(puinstore);
    }

    /*
     * 编辑页面的审批
     * */
    /*public Ret editAutit(Long autoid) {
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
                String json = getSysPuinstoreDto(sysPuinstore);
                String post = new BaseInU8Util().base_in(json);
                LOG.info(post);
            }
            return true;
        });

        return ret(tx);
    }*/

    /*
     * 查看页面的审批
     * */
    public Ret onlyseeAutit(Long autoid) {
        SysPuinstore sysPuinstore = findById(autoid);
        Date date = new Date();
        //1、更新审核人、审核时间、状态
        boolean tx = tx(() -> {

            checkApproveAndReject(autoid);
            /*sysPuinstore.setAuditPerson(JBoltUserKit.getUserName());
            sysPuinstore.setAuditDate(date);//审核日期
            sysPuinstore.setModifyPerson(JBoltUserKit.getUserName());
            sysPuinstore.setModifyDate(date);
            update(sysPuinstore);

            //2、同步于U8
            String json = getSysPuinstoreDto(sysPuinstore);
            String post = new BaseInU8Util().base_in(json);
            LOG.info(post);

            //3、如果成功，给u8的单据号；不成功，把单据号置为空，状态改为审核不通过
            PurchaseOrderM purchaseOrderM = findU8BillNo(sysPuinstore);
            sysPuinstore.setU8BillNo(purchaseOrderM != null ? purchaseOrderM.getCDocNo() : "");*/

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
    public Ret deleteByAutoid(Long id) {
        tx(() -> {
            //1、未审核状态下直接删除
            checkDelete(id);
            SysPuinstore puinstore = findById(id);

            //从表的数据
            Ret ret = deleteSysPuinstoredetailByMasId(String.valueOf(id));
            if (ret.isFail()) {
                return false;
            }
            //删除主表数据
            deleteById(id);
            return true;
        });
        return ret(true);
    }

    public void checkDelete(Long id) {
        SysPuinstore puinstore = findById(id);
        ValidationUtils.equals(AuditStatusEnum.NOT_AUDIT.getValue(), puinstore.getIAuditStatus(),
            puinstore.getBillNo() + "：不是未审核状态，不可以删除！！");
    }

    public Ret deleteSysPuinstoredetailByMasId(String id) {
        List<SysPuinstoredetail> puinstoredetails = syspuinstoredetailservice.findDetailByMasID(id);
        if (!puinstoredetails.isEmpty()) {
            List<String> collect = puinstoredetails.stream().map(SysPuinstoredetail::getAutoID).collect(Collectors.toList());
            syspuinstoredetailservice.deleteByIds(String.join(",", collect));
        }
        return ret(true);
    }

    /*
     * 获取删除的json
     * */
    public String getSysPuinstoreDeleteDTO(String u8billno) {
        User user = JBoltUserKit.getUser();

        Record userRecord = findU8UserByUserCode(user.getUsername());
        Record u8Record = findU8RdRecord01Id(u8billno);

        SysPuinstoreDeleteDTO deleteDTO = new SysPuinstoreDeleteDTO();
        data data = new data();
        data.setAccid(getOrgCode());
        data.setPassword(userRecord.get("u8_pwd"));
//        Long userid = userRecord.getLong("userid");
        data.setUserID(userRecord.get("u8_code"));
        Long id = u8Record.getLong("id");
        data.setVouchId(String.valueOf(id));//u8单据id
        deleteDTO.setData(data);

        return JSON.toJSONString(deleteDTO);
    }

    public Record findU8UserByUserCode(String userCode) {
        return dbTemplate(u8SourceConfigName(), "syspuinstore.findU8UserByUserCode", Kv.by("usercode", userCode)).findFirst();
    }

    public Record findU8RdRecord01Id(String cCode) {
        return dbTemplate(u8SourceConfigName(), "syspuinstore.findU8RdRecord01Id", Kv.by("cCode", cCode)).findFirst();
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

        List<Record> saveRecordList = jBoltTable.getSaveRecordList();
        List<Record> updateRecordList = jBoltTable.getUpdateRecordList();
        Object[] ids = jBoltTable.getDelete();

        boolean tx = tx(() -> {
            //1、更新主表
            if (isOk(sysPuinstore.getAutoID())) {
                //更新主表
                SysPuinstore updatePuinstore = findById(sysPuinstore.getAutoID());

                updatePuinstore.setCUpdateName(JBoltUserKit.getUserName());
                updatePuinstore.setDUpdateTime(new Date());
                saveSysPuinstoreModel(updatePuinstore, record);
                this.update(updatePuinstore);
                //ValidationUtils.isTrue(updatePuinstore.update(), "提交审核失败");
            } else {
                //2、新增主表
                SysPuinstore savePuinstore = new SysPuinstore();
                //savePuinstore.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
                savePuinstore.setCCreateName(JBoltUserKit.getUserName());
                savePuinstore.setDCreateTime(new Date());
                savePuinstore.setIAuditStatus(AuditStatusEnum.NOT_AUDIT.getValue());//未审核状态
                saveSysPuinstoreModel(savePuinstore, record);
                ValidationUtils.isTrue(savePuinstore.save(), JBoltMsg.FAIL);
            }

            if (saveRecordList != null && !saveRecordList.isEmpty()) {
                List<SysPuinstoredetail> saveList = new ArrayList<>();
                saveRecordList(saveRecordList, saveList, sysPuinstore);
                syspuinstoredetailservice.batchSave(saveList);
            }
            if (updateRecordList != null && !updateRecordList.isEmpty()) {
                List<SysPuinstoredetail> updateList = new ArrayList<>();
                updateRecordList(updateRecordList, updateList, sysPuinstore);
                syspuinstoredetailservice.batchUpdate(updateList);
            }
            if (ids != null && !ArrayUtil.isEmpty(ids)) {
                for (Object detailAutoid : ids) {
                    syspuinstoredetailservice.deleteById(detailAutoid);
                }
            }

            return true;
        });
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
//            puinstoredetail.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
            Inventory inventory = inventoryService.findBycInvCode(detailRecord.getStr("invcode"));
            Kv kv = new Kv();
            kv.set("sourcebillno", sysPuinstore.getSourceBillNo());
            kv.set("iInventoryId", null != inventory ? inventory.getIAutoId() : "");
            Record record = dbTemplate("syspuinstore.getSourceBillIdAndDid", kv).findFirst();
            if (record != null) {
                puinstoredetail.setSourceBillID(record.getStr("sourcebillid")); //来源单据ID(订单id)
                puinstoredetail.setSourceBillDid(record.getStr("sourcebilldid")); //来源单据DID;采购或委外单身ID
            }
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
            puinstoredetail.setDUpdateTime(new Date());
            puinstoredetail.setCUpdateName(JBoltUserKit.getUserName());
            syspuinstoredetailservice.saveSysPuinstoredetailModel(puinstoredetail, updateRecord, sysPuinstore, i);
            puinstoredetailList.add(puinstoredetail);
            i++;
        }
    }


   /* public Kv getKv(String billno, String sourcebillno) {
        Kv kv = new Kv();
        kv.set("billno", billno);
        kv.set("sourcebillno", sourcebillno);
        return kv;
    }*/

    public void saveSysPuinstoreModel(SysPuinstore puinstore, Record record) {
        puinstore.setBillType(record.get("billtype"));//采购类型
        puinstore.setOrganizeCode(getOrgCode());
        puinstore.setBillNo(record.get("billno"));//入库单号，先自己生成
        puinstore.setBillDate(record.get("billdate")); //单据日期
        puinstore.setRdCode(record.get("rdcode"));
        puinstore.setDeptCode(record.get("deptcode"));
        puinstore.setSourceBillNo(record.get("sourcebillno"));//来源单号（订单号）
        puinstore.setSourceBillID(record.getStr("sourcebillid"));//来源单据id
        puinstore.setVenCode(record.get("vencode")); //供应商
        puinstore.setMemo(record.get("memo"));
        puinstore.setWhCode(record.getStr("whcode"));
        Warehouse warehouse = warehouseService.findByCwhcode(record.getStr("whcode"));
        puinstore.setWhName(warehouse != null ? warehouse.getCWhName() : "");
        puinstore.setIAuditWay(1);//审批方式：1. 审核 2. 审批流
        puinstore.setIsDeleted(false);
        if (StrUtil.isNotBlank(record.get("ibustype"))) {
            puinstore.setIBusType(record.getInt("ibustype"));//业务类型
        }
    }

    /*
     * 获取mes采购订单视图
     * */
    public Page<Record> getMesSysPODetails(Kv kv, int size, int PageSize) {
        return dbTemplate("syspuinstore.getMesSysPODetails", kv).paginate(size, PageSize);
    }

    public List<Record> getInsertPuinstoreDetail(Kv kv) {
        return dbTemplate("syspuinstore.getInsertPuinstoreDetail", kv.set("limit", 20)).find();
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
        int i = 1;
        for (SysPuinstoredetail detail : detailList) {
            Main main = new Main();
            if (StrUtil.isNotBlank(detail.getSpotTicket())) {
//                Record record = getBarcodeVersion(puinstore.getSourceBillNo(), detail.getSpotTicket());
                main.setBarCode(detail.getSpotTicket()); //现品票+版本号
            } else {
                main.setBarCode(detail.getInvcode());//传invcode
            }
            main.setBillDate(puinstore.getBillDate());
            main.setBillDid(detail.getAutoID());//明细表id
            main.setBillID(puinstore.getAutoID());//主表id
            main.setBillNo(puinstore.getBillNo());
            main.setBillNoRow(puinstore.getBillNo() + "-" + i);//主表billno-明细表rowno
            main.setCreatePerson(detail.getCCreateName());
            main.setISsurplusqty(false);
            main.setIcRdCode(puinstore.getRdCode());
            main.setIsWhpos("1"); //
            main.setNum(0);
            main.setPackRate("0");
            main.setQty(detail.getQty().stripTrailingZeros().toPlainString());
            main.setSourceBillNoRow(detail.getSourceBillNoRow());
            main.setVenCode(puinstore.getVenCode());
            main.setVenName(null != vendor ? vendor.getCVenName() : "");
            main.setIndex(String.valueOf(i));
            main.setIposcode(""); //库区
            main.setIwhcode(detail.getWhcode());
            main.setOrganizeCode(getOrgCode());
            main.setSourceBillDid(detail.getSourceBillDid());
            main.setSourceBillNo(detail.getSourceBillNo());
            String order_type = findIBusTypeKey(detail.getSourceBillType(), "order_type");
            if (StrUtil.isBlank(order_type)) {
                main.setSourceBillType("PO");//采购=PO,委外=OM
            } else {
                main.setSourceBillType(order_type);//采购=PO,委外=OM
            }

            main.setTag("PUInStore");
            MainData.add(main);
            i = i + 1;
        }

        //其它数据
        PreAllocate preAllocate = new PreAllocate();
        preAllocate.setCreatePerson(user.getUsername());
        preAllocate.setCreatePersonName(puinstore.getCCreateName());
        preAllocate.setLoginDate(DateUtil.formatDate(puinstore.getDCreateTime()));
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

    public Record getBarcodeVersion(String corderno, String barcode) {
        Kv kv = new Kv();
        kv.set("corderno", corderno);
        kv.set("barcode", barcode);
        return dbTemplate("syspuinstore.getBarcodeVersion", kv).findFirst();
    }

    public Object printData(Kv kv) {
        return dbTemplate("syspuinstore.getPrintData", kv).find();
    }

    /*
     * 批量审核通过
     * */
    public Ret batchApprove(String ids) {
        String[] split = ids.split(",");
        checkIAuditStatus(ids);
        ArrayList<SysPuinstore> puinstoreList = new ArrayList<>();
        Date date = new Date();
        for (String id : split) {
            SysPuinstore puinstore = findById(id);
            //同步U8
            String json = getSysPuinstoreDto(puinstore);
            setSysPuinstoreU8Billno(json, puinstore);

            // 状态改为已审核
            puinstore.setCAuditName(JBoltUserKit.getUserName());
            puinstore.setAuditDate(date);
            puinstore.setCUpdateName(JBoltUserKit.getUserName());
            puinstore.setDUpdateTime(date);
            puinstore.setIAuditStatus(2);
            puinstore.setIAuditWay(1);
            //
            puinstoreList.add(puinstore);
        }
        tx(() -> {
            //1、批量审核成功
            batchUpdate(puinstoreList);

            //2、todo 委外订单检查是否要生成材料出库单和形态转换单
            //checkIsCreateMaterialAndSysAssem(puinstoreList);
            return true;
        });

        return SUCCESS;
    }

    public void setSysPuinstoreU8Billno(String json, SysPuinstore puinstore) {
        tx(() -> {
            String u8Billno = new BaseInU8Util().base_in(json);
            System.out.println(u8Billno);
            puinstore.setU8BillNo(u8Billno);
            return true;
        });
    }

    /*
     * 检查批量审核中的所有数据状态
     * */
    public void checkIAuditStatus(String ids) {
        List<SysPuinstore> puinstores = find("select * from T_Sys_PUInStore where autoid in (" + ids + ")");
        for (SysPuinstore puinstore : puinstores) {
            //未审核状态下不能批量审核
            if (puinstore.getIAuditStatus() == AuditStatusEnum.NOT_AUDIT.getValue()) {
                ValidationUtils.isTrue(false, puinstore.getBillNo() + "：单据未提交审核或审批，不能批量审核！！");
            } else if (puinstore.getIAuditStatus() == AuditStatusEnum.APPROVED.getValue()
                || puinstore.getIAuditStatus() == AuditStatusEnum.REJECTED.getValue()) {
                ValidationUtils.isTrue(false, puinstore.getBillNo() + "：审核流程已结束，不能批量审核！！");
            }
        }
    }

    /*
     * 批量反审核
     * */
    public Ret batchReverseApprove(String ids) {
        tx(() -> {
            String[] split = ids.split(",");
            checkReverseApprove(ids);
            ArrayList<SysPuinstore> puinstoreList = new ArrayList<>();
            Date date = new Date();
            for (String id : split) {
                commonResetAutitById(id, puinstoreList, date);
            }
            //1、批量反审核成功
            batchUpdate(puinstoreList);
            return true;
        });

        return SUCCESS;
    }

    public void checkReverseApprove(String ids) {
        List<SysPuinstore> puinstores = find("select * from T_Sys_PUInStore where autoid in (" + ids + ")");
        for (SysPuinstore puinstore : puinstores) {
            //未审核状态下不能批量审核
            if (puinstore.getIAuditStatus() == AuditStatusEnum.AWAIT_AUDIT.getValue()) {
                ValidationUtils.error( puinstore.getBillNo() + "：待审核状态下，不能批量反审核！！");
            } else if (puinstore.getIAuditStatus() == AuditStatusEnum.NOT_AUDIT.getValue()) {
                ValidationUtils.error(puinstore.getBillNo() + "：单据未提交审核或审批，不能批量反审核！！");
            } else if (puinstore.getIAuditStatus() == AuditStatusEnum.REJECTED.getValue()) {
                ValidationUtils.error(puinstore.getBillNo() + "：审核未通过，不能批量反审核！！");
            }
        }
    }

    /*
     * 审核通过
     * */
    public Ret approve(Long autoid) {
        SysPuinstore sysPuinstore = findById(autoid);
        // 校验订单状态
        checkApproveAndReject(autoid);
        Date date = new Date();
        User user = JBoltUserKit.getUser();
        ValidationUtils.equals(AuditStatusEnum.AWAIT_AUDIT.getValue(), sysPuinstore.getIAuditStatus(), "订单非待审核状态");

        sysPuinstore.setCAuditName(user.getUsername());
        sysPuinstore.setAuditDate(date);//审核日期
        sysPuinstore.setDUpdateTime(date);
        sysPuinstore.setCUpdateName(user.getUsername());
        sysPuinstore.setIAuditStatus(2);
        String json = getSysPuinstoreDto(sysPuinstore);
        tx(() -> {
            //2、同步于U8
            String u8Billno = new BaseInU8Util().base_in(json);

            //3、如果成功，给u8的单据号；不成功，把单据号置为空，状态改为审核不通过
            sysPuinstore.setU8BillNo(u8Billno);
            ValidationUtils.isTrue(sysPuinstore.update(), "审核通过失败！！");

            return true;
        });

        return SUCCESS;
    }

    /*
     * 审核不通过
     * */
    public Ret reject(Long autoid) {
        tx(() -> {
            // 校验订单状态
            checkApproveAndReject(autoid);
            Date date = new Date();
            User user = JBoltUserKit.getUser();
            SysPuinstore sysPuinstore = findById(autoid);
            ValidationUtils.equals(AuditStatusEnum.AWAIT_AUDIT.getValue(), sysPuinstore.getIAuditStatus(), "订单非待审核状态");

            sysPuinstore.setCUpdateName(user.getUsername());
            sysPuinstore.setCAuditName(user.getUsername());
            sysPuinstore.setDUpdateTime(date);
            sysPuinstore.setAuditDate(date);
            sysPuinstore.setDAuditTime(date);
            sysPuinstore.setIAuditStatus(AuditStatusEnum.REJECTED.getValue());
            ValidationUtils.isTrue(sysPuinstore.update(), "审核不通过失败");
            return true;
        });

        return SUCCESS;
    }

    public void checkApproveAndReject(long id) {
        SysPuinstore puinstore = findById(id);
        if (AuditStatusEnum.NOT_AUDIT.getValue() == puinstore.getIAuditStatus()) {
            ValidationUtils.error("入库单号：" + puinstore.getBillNo() + " 单据未提交审核或审批！！");
        } else if (AuditStatusEnum.APPROVED.getValue() == puinstore.getIAuditStatus() || AuditStatusEnum.REJECTED.getValue() == puinstore.getIAuditStatus()) {
            ValidationUtils.error("入库单号：" + puinstore.getBillNo() + " 流程已结束！！");
        }
    }

    @Override
    public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
        return null;
    }

    @Override
    public String postRejectFunc(long formAutoId) {
        return null;
    }

    @Override
    public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        return null;
    }

    @Override
    public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        return null;
    }

    @Override
    public String preSubmitFunc(long formAutoId) {
        return null;
    }

    @Override
    public String postSubmitFunc(long formAutoId) {
        return null;
    }

    @Override
    public String postWithdrawFunc(long formAutoId) {
        return null;
    }

    @Override
    public String withdrawFromAuditting(long formAutoId) {
        return null;
    }

    @Override
    public String preWithdrawFromAuditted(long formAutoId) {
        return null;
    }

    @Override
    public String postWithdrawFromAuditted(long formAutoId) {
        return null;
    }

    @Override
    public String postBatchApprove(List<Long> formAutoIds) {
        return null;
    }

    @Override
    public String postBatchReject(List<Long> formAutoIds) {
        return null;
    }

    @Override
    public String postBatchBackout(List<Long> formAutoIds) {
        return null;
    }
}