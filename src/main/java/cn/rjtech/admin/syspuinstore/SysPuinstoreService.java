package cn.rjtech.admin.syspuinstore;

import static cn.hutool.core.text.StrPool.COMMA;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt.core.base.JBoltMsg;
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
import cn.rjtech.admin.rdstyle.RdStyleService;
import cn.rjtech.admin.subcontractorderm.SubcontractOrderMService;
import cn.rjtech.admin.sysassem.SysAssemService;
import cn.rjtech.admin.sysassem.SysAssemdetailService;
import cn.rjtech.admin.syspureceive.SysPureceiveService;
import cn.rjtech.admin.syspureceive.SysPureceivedetailService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.model.momdata.base.BaseSysPureceivedetail;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDTO;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDTO.Main;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDTO.PreAllocate;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDeleteDTO;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDeleteDTO.data;
import cn.rjtech.util.BaseInU8Util;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.StringUtils;

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
    private RdStyleService            rdStyleService;
    @Inject
    private VendorService             vendorService;
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
    private SubcontractOrderMService  subcontractOrderMService;//委外订单主表
    @Inject
    private PurchaseOrderMService     purchaseOrderMService;//采购订单主表
    @Inject
    private InventoryChangeService    inventoryChangeService;//物料形态对照表
    @Inject
    private SysPureceiveService       pureceiveService;
    @Inject
    private SysPureceivedetailService pureceivedetailService;

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
     * 反审批
     * */
    public String resetAutitById(String autoid) {
        Date date = new Date();
        SysPuinstore puinstore = findById(autoid);
        List<SysPuinstore> puinstoreList = new ArrayList<>();
        //打u8接口，通知u8删除单据，然后更新mom平台的数据
        String json = getSysPuinstoreDeleteDTO(puinstore.getU8BillNo());
        try {
            String post = new BaseInU8Util().deleteVouchProcessDynamicSubmitUrl(json);
            LOG.info(post);
        } catch (Exception ex) {
            return ex.getMessage();
        }
        //
        User user = JBoltUserKit.getUser();
        puinstore.setU8BillNo(null);//将u8的单据号置为空
        puinstore.setCAuditName(user.getUsername());
        puinstore.setAuditDate(date);
        puinstore.setCUpdateName(user.getUsername());
        puinstore.setDUpdateTime(date);
        puinstore.setIUpdateBy(JBoltUserKit.getUserId());
        puinstoreList.add(puinstore);
        //更新
        batchUpdate(puinstoreList);
        return null;
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
    public Ret deleteByAutoId(Long id) {
        tx(() -> {
            //1、未审核状态下直接删除
            checkDelete(id);
            SysPuinstore puinstore = findById(id);

            //从表的数据
            Ret ret = deleteSysPuinStoreDetailByMasId(String.valueOf(id));
            if (ret.isFail()) {
                return false;
            }
            //删除主表数据
            deleteById(id);
            return true;
        });
        return SUCCESS;
    }

    public void checkDelete(Long id) {
        SysPuinstore puinstore = findById(id);
        ValidationUtils.equals(AuditStatusEnum.NOT_AUDIT.getValue(), puinstore.getIAuditStatus(),
            puinstore.getBillNo() + "：不是未审核状态，不可以删除！！");
    }

    public Ret deleteSysPuinStoreDetailByMasId(String id) {
        List<SysPuinstoredetail> puinstoredetails = syspuinstoredetailservice.findDetailByMasID(id);
        if (!puinstoredetails.isEmpty()) {
            List<String> collect = puinstoredetails.stream().map(SysPuinstoredetail::getAutoID).collect(Collectors.toList());
            syspuinstoredetailservice.deleteByIds(String.join(",", collect));
        }
        return SUCCESS;
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

        Record formRecord = jBoltTable.getFormRecord();
        SysPuinstore sysPuinstore = jBoltTable.getFormModel(SysPuinstore.class);
        Record record = jBoltTable.getFormRecord();

        List<Record> saveRecordList = jBoltTable.getSaveRecordList();
        List<Record> updateRecordList = jBoltTable.getUpdateRecordList();
        Object[] ids = jBoltTable.getDelete();

        tx(() -> {
            SysPuinstore puinstore = new SysPuinstore();
            //1、更新主表
            if (isOk(sysPuinstore.getAutoID())) {
                //更新主表
                SysPuinstore updatePuinstore = findById(sysPuinstore.getAutoID());

                saveSysPuinstoreModel(updatePuinstore, record);
                this.update(updatePuinstore);

                puinstore = updatePuinstore;
            } else {
                //2、新增主表
                SysPuinstore savePuinstore = new SysPuinstore();
                savePuinstore.setIAuditStatus(AuditStatusEnum.NOT_AUDIT.getValue());//未审核状态
                savePuinstore.setCCreateName(JBoltUserKit.getUserName());
                savePuinstore.setDCreateTime(new Date());
                savePuinstore.setICreateBy(JBoltUserKit.getUserId());
                saveSysPuinstoreModel(savePuinstore, record);
                ValidationUtils.isTrue(savePuinstore.save(), JBoltMsg.FAIL);

                puinstore = savePuinstore;
            }

            String ordertype = formRecord.getStr("ordertype");
            if (saveRecordList != null && !saveRecordList.isEmpty()) {
                List<SysPuinstoredetail> saveList = new ArrayList<>();
                saveRecordList(saveRecordList, saveList, puinstore, ordertype);
                syspuinstoredetailservice.batchSave(saveList);
            }
            if (updateRecordList != null && !updateRecordList.isEmpty()) {
                List<SysPuinstoredetail> updateList = new ArrayList<>();
                updateRecordList(updateRecordList, updateList, puinstore, ordertype);
                syspuinstoredetailservice.batchUpdate(updateList);
            }
            if (ids != null && !ArrayUtil.isEmpty(ids)) {
                for (Object detailAutoid : ids) {
                    syspuinstoredetailservice.deleteById(detailAutoid);
                }
            }

            return true;
        });

        return successWithData(sysPuinstore.keep("autoid"));
    }

    /*
     * 保存明细表
     * */
    public void saveRecordList(List<Record> saveRecordList, List<SysPuinstoredetail> puinstoredetailList,
                               SysPuinstore sysPuinstore, String sourcebilltype) {
        int i = 1;
        for (Record detailRecord : saveRecordList) {
            SysPuinstoredetail puinstoredetail = new SysPuinstoredetail();
            Inventory inventory = inventoryService.findBycInvCode(detailRecord.getStr("invcode"));
            Kv kv = new Kv();
            kv.set("sourcebillno", sysPuinstore.getSourceBillNo());
            kv.set("iInventoryId", null != inventory ? inventory.getIAutoId() : "");
            Record record = dbTemplate("syspuinstore.getSourceBillIdAndDid", kv).findFirst();
            if (record != null) {
                puinstoredetail.setSourceBillID(record.getStr("sourcebillid")); //来源单据ID(订单id)
                puinstoredetail.setSourceBillDid(record.getStr("sourcebilldid")); //来源单据DID;采购或委外单身ID
            }
            puinstoredetail.setSourceBillType(sourcebilltype);//采购PO  委外OM（采购类型）
            syspuinstoredetailservice.saveSysPuinstoredetailModel(puinstoredetail, detailRecord, sysPuinstore, i);
            puinstoredetailList.add(puinstoredetail);
            i++;
        }
    }

    /*
     * 更新明细表
     * */
    public void updateRecordList(List<Record> updateRecordList, List<SysPuinstoredetail> puinstoredetailList,
                                 SysPuinstore sysPuinstore, String sourcebilltype) {
        int i = 1;
        for (Record updateRecord : updateRecordList) {
            SysPuinstoredetail puinstoredetail = syspuinstoredetailservice.findById(updateRecord.get("autoid"));
            puinstoredetail.setDUpdateTime(new Date());
            puinstoredetail.setCUpdateName(JBoltUserKit.getUserName());
            puinstoredetail.setSourceBillType(sourcebilltype);//采购PO  委外OM（采购类型）
            syspuinstoredetailservice.saveSysPuinstoredetailModel(puinstoredetail, updateRecord, sysPuinstore, i);
            puinstoredetailList.add(puinstoredetail);
            i++;
        }
    }

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
        puinstore.setCUpdateName(JBoltUserKit.getUserName());
        puinstore.setDUpdateTime(new Date());
        puinstore.setIUpdateBy(JBoltUserKit.getUserId());
    }

    /*
     * 获取mes采购订单视图
     * */
    public Page<Record> getMesSysPODetails(Kv kv, int size, int PageSize, String ordertype) {
        Page<Record> recordPage = new Page<>();
        if (ObjUtil.equal(ordertype, "PO")) {
            recordPage = dbTemplate("syspuinstore.getMesSysPODetailsByPO", kv).paginate(size, PageSize);
        } else if (ObjUtil.equal(ordertype, "OM")) {
            recordPage = dbTemplate("syspuinstore.getMesSysPODetailsByOM", kv).paginate(size, PageSize);
        }
        return recordPage;
    }

    public List<Record> getInsertPuinstoreDetail(Kv kv) {
        List<Record> recordList = null;
        if (ObjUtil.equal(kv.getStr("ordertype"), "PO")) {
            recordList = dbTemplate("syspuinstore.getInsertPuinstoreDetailByPO", kv.set("limit", 20)).find();
        } else if (ObjUtil.equal(kv.getStr("ordertype"), "OM")) {
            recordList = dbTemplate("syspuinstore.getInsertPuinstoreDetailByOM", kv.set("limit", 20)).find();
        }
        return recordList;
    }

    /*
     * 获取仓库名
     * */
    public List<Record> getWareHouseName(Kv kv) {
        return dbTemplate(u8SourceConfigName(), "syspuinstore.getWareHouseName", kv).find();
    }

    /*
     * 是否为双单位扫码
     * */
    public boolean checkPUReceiveSpecial(SysPureceive pureceive) {
        //双单位
        if (pureceive != null && ObjUtil.equal(pureceive.getType(), "PUReceiveSpecial")) {
            return true;
        }
        return false;
    }

    /*
     * 组推送u8的json
     * */
    public String getSysPuinstoreDto(SysPuinstore puinstore) {

        //主数据
        int i = 1;
        ArrayList<Main> MainData = new ArrayList<>();
        User user = JBoltUserKit.getUser();
        List<SysPuinstoredetail> detailList = syspuinstoredetailservice.findDetailByMasID(puinstore.getAutoID());
        //双单位的收料单
        List<SysPuinstoredetail> receiveSpecialList = new ArrayList<>();
        //双单位扫码，只需要推送转换前的数据
        SysPureceive pureceive = pureceiveService.findByBillNo(puinstore.getSourceBillNo());
        boolean special = checkPUReceiveSpecial(pureceive);
        if (special) {
            List<SysPureceivedetail> pureceivedetails = pureceivedetailService.findFirstBy(pureceive.getAutoID());
            List<String> invcodes = pureceivedetails.stream()
                .filter(e -> ObjUtil.equal(e.getBarcodeType(), "转换前"))
                .map(BaseSysPureceivedetail::getInvcode)
                .collect(Collectors.toList());
            for (SysPuinstoredetail detail : detailList) {
                if (invcodes.contains(detail.getInvcode())) {
                    receiveSpecialList.add(detail);
                }
            }
        }
        //双单位
        if (!receiveSpecialList.isEmpty()) {
            commonMakeUpMainData(receiveSpecialList, puinstore, MainData);
        } else {
            //来料收获
            commonMakeUpMainData(detailList, puinstore, MainData);
        }

        //其它数据
        SysPuinstoreDTO dto = new SysPuinstoreDTO();
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

    public void commonMakeUpMainData(List<SysPuinstoredetail> detailList, SysPuinstore puinstore,
                                     ArrayList<Main> MainData) {
        int i = 1;
        Vendor vendor = vendorService.findByCode(puinstore.getVenCode());
        for (SysPuinstoredetail detail : detailList) {
            Main main = new Main();
            if (StrUtil.isNotBlank(detail.getBarCode())) {
                main.setBarCode(detail.getBarCode()); //现品票+版本号
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
            main.setIsWhpos("1");
            main.setNum(0);
            main.setPackRate("0");
            main.setQty(detail.getQty().stripTrailingZeros().toPlainString());
            main.setSourceBillNoRow(detail.getSourceBillNoRow());
            main.setVenCode(puinstore.getVenCode());
            main.setVenName(null != vendor ? vendor.getCVenName() : "");
            main.setIndex(String.valueOf(i));
            main.setIposcode(detail.getPosCode()); //库区
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

    }

    public Object printData(Kv kv) {
        return dbTemplate("syspuinstore.getPrintData", kv).find();
    }

    /*
     * 批量审核通过
     * */
    public Ret batchApprove(String ids) {
        String[] split = ids.split(",");
        ArrayList<SysPuinstore> puinstoreList = new ArrayList<>();
        Date date = new Date();

        tx(() -> {
            for (String id : split) {
                SysPuinstore puinstore = findById(id);
                //同步U8
                String json = getSysPuinstoreDto(puinstore);
                setSysPuinstoreU8Billno(json, puinstore);
                puinstore.setCAuditName(JBoltUserKit.getUserName());
                puinstore.setAuditDate(date);
                puinstore.setCUpdateName(JBoltUserKit.getUserName());
                puinstore.setDUpdateTime(date);
                //
                puinstoreList.add(puinstore);


                SysPureceive pureceive = pureceiveService.findByBillNo(puinstore.getSourceBillNo());
                boolean special = checkPUReceiveSpecial(pureceive);
                if (special) {
                    List<SysPuinstoredetail> detailList = syspuinstoredetailservice.findDetailByMasID(puinstore.getAutoID());
                    SysAssem sysAssem = new SysAssem();
                    List<SysAssemdetail> sysAssemdetailList = new ArrayList<>();
                    createSysAssem(sysAssem, detailList, puinstore, sysAssemdetailList);
                    ValidationUtils.isTrue(sysAssem.save(), "生成形态转换单失败！！！");
                    sysAssemdetailService.batchSave(sysAssemdetailList);
                }
                //2.2 委外订单
                Integer corderno = subcontractOrderMService.findOderNoIsNotExists(puinstore.getSourceBillNo());
                if (corderno != null) {
                    //todo checkIsCreateMaterialAndSysAssem(puinstoreList);
                }
            }
            //批量审核成功
            batchUpdate(puinstoreList);

            return true;
        });

        return SUCCESS;
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

    public void setSysPuinstoreU8Billno(String json, SysPuinstore puinstore) {
        tx(() -> {
            String u8Billno = new BaseInU8Util().base_in(json);
            puinstore.setU8BillNo(u8Billno);
            return true;
        });
    }

    /*
     * 批量审批（审核）不通过
     * */
    public Ret batchReverseApprove(String ids) {
        tx(() -> {
            String[] split = ids.split(",");
            ArrayList<SysPuinstore> puinstoreList = new ArrayList<>();
            Date date = new Date();
            Long userId = JBoltUserKit.getUserId();
            for (String id : split) {
                SysPuinstore puinstore = findById(id);
                puinstore.setIUpdateBy(userId);
                puinstore.setDUpdateTime(date);
                puinstore.setCUpdateName(JBoltUserKit.getUserName());
                //
                puinstoreList.add(puinstore);
            }
            batchUpdate(puinstoreList);
            return true;
        });

        return SUCCESS;
    }

    /*
     * 审核通过
     * */
    public String approve(Long autoid) {
        SysPuinstore sysPuinstore = findById(autoid);
        // 校验订单状态
        Date date = new Date();
        User user = JBoltUserKit.getUser();
        sysPuinstore.setCAuditName(user.getUsername());
        sysPuinstore.setAuditDate(date);//审核日期
        sysPuinstore.setDUpdateTime(date);
        sysPuinstore.setCUpdateName(user.getUsername());
        String json = getSysPuinstoreDto(sysPuinstore);

        String u8Billno = "";
        //2、同步于U8
        try {
            u8Billno = new BaseInU8Util().base_in(json);
        } catch (Exception ex) {
            return ex.getMessage();
        }
        //3、如果成功，给u8的单据号；不成功，把单据号置为空，状态改为审核不通过
        sysPuinstore.setU8BillNo(u8Billno);
        if (!sysPuinstore.update()) {
            return "审核通过失败！！!";
        }
        //2、采购订单，如果是双单位收货，需要生成形态转换单
        SysPureceive pureceive = pureceiveService.findByBillNo(sysPuinstore.getSourceBillNo());
        boolean special = checkPUReceiveSpecial(pureceive);
        if (special) {
            List<SysPuinstoredetail> detailList = syspuinstoredetailservice.findDetailByMasID(sysPuinstore.getAutoID());
            SysAssem sysAssem = new SysAssem();
            List<SysAssemdetail> sysAssemdetailList = new ArrayList<>();
            createSysAssem(sysAssem, detailList, sysPuinstore, sysAssemdetailList);
            ValidationUtils.isTrue(sysAssem.save(), "生成形态转换单失败！！！");
            sysAssemdetailService.batchSave(sysAssemdetailList);
        }
        return null;
    }

    /*
     * 审核不通过
     * */
    public String reject(Long autoid) {
        // 校验订单状态
        Date date = new Date();
        User user = JBoltUserKit.getUser();
        SysPuinstore sysPuinstore = findById(autoid);

        sysPuinstore.setCUpdateName(user.getUsername());
        sysPuinstore.setCAuditName(user.getUsername());
        sysPuinstore.setDUpdateTime(date);
        sysPuinstore.setAuditDate(date);
        if (!sysPuinstore.update()) {
            return "审核不通过失败";
        }
        return null;
    }

    /*
     * 审核通过
     * */
    @Override
    public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
        return approve(formAutoId);
    }

    /*
     * 审核不通过
     * */
    @Override
    public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
        return reject(formAutoId);
    }

    /**
     * 实现反审之前的其他业务操作，如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @param isFirst    是否为审批的第一个节点
     * @param isLast     是否为审批的最后一个节点
     * @return 错误信息
     */
    @Override
    public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        if (isLast) {
            return resetAutitById(String.valueOf(formAutoId));
        }
        return null;
    }

    /**
     * 实现反审之后的其他业务操作, 如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @param isFirst    是否为审批的第一个节点
     * @param isLast     是否为审批的最后一个节点
     * @return 错误信息
     */
    @Override
    public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        return null;
    }

    /*
     * 提交审核
     * */
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

    /**
     * 批量审批（审核）通过
     *
     * @param formAutoIds 单据IDs
     * @return 错误信息
     */
    @Override
    public String postBatchApprove(List<Long> formAutoIds) {
        this.batchApprove(StringUtils.join(formAutoIds, COMMA));
        return null;
    }

    /**
     * 批量审批（审核）不通过
     *
     * @param formAutoIds 单据IDs
     * @return 错误信息
     */
    @Override
    public String postBatchReject(List<Long> formAutoIds) {
        this.batchReverseApprove(StringUtils.join(formAutoIds, COMMA));
        return null;
    }

    @Override
    public String postBatchBackout(List<Long> formAutoIds) {
        return null;
    }

    /*
     * 扫描现品票
     * */
    public Record scanBarcode(String barcode, String orderType) {
        SysPuinstoredetail sysPuinstoredetail = syspuinstoredetailservice.findFirstByBarcode(barcode);
        if (sysPuinstoredetail != null) {
            SysPuinstore puinstore = findById(sysPuinstoredetail.getMasID());
            ValidationUtils.isTrue(sysPuinstoredetail == null,
                String.format("现品票 %s 已存在于 %s 入库单号，不能重复！！", barcode, puinstore.getBillNo()));
        }
        //采购订单
        Record record = null;
        if (ObjUtil.equal(orderType, "PO")) {
            record = dbTemplate("syspuinstore.scanPurchaseOrderBarcode", Kv.by("barcode", barcode).set("orgCode", getOrgCode()))
                .findFirst();
        } else if (ObjUtil.equal(orderType, "OM")) {
            //委外订单
            record = dbTemplate("syspuinstore.scanSubcontractOrderBarcode",
                Kv.by("barcode", barcode).set("orgCode", getOrgCode())).findFirst();
        }
        ValidationUtils.notNull(record, barcode + "：现品票不存在，请重新扫描！！！");
        return record;
    }

    /**
     * 获取条码列表
     * 通过关键字匹配
     * autocomplete组件使用
     */
    public List<Record> getBarcodeDatas(String q, String ordertype, String sourcebillno) {
        List<Record> recordList = null;
        if (ObjUtil.equal(ordertype, "PO")) {
            recordList = dbTemplate("syspuinstore.scanPurchaseOrderBarcode", Kv.by("q", q).set("limit", 20).set("orgCode",
                getOrgCode()).set("sourcebillno", sourcebillno)).find();
        } else if (ObjUtil.equal(ordertype, "OM")) {
            recordList = dbTemplate("syspuinstore.scanSubcontractOrderBarcode", Kv.by("q", q).set("limit", 20).set("orgCode",
                getOrgCode()).set("sourcebillno", sourcebillno)).find();
        }
        return recordList;
    }
}