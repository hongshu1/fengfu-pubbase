package cn.rjtech.admin.syspuinstore;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.materialsout.MaterialsOutService;
import cn.rjtech.admin.purchasetype.PurchaseTypeService;
import cn.rjtech.admin.rdstyle.RdStyleService;
import cn.rjtech.model.momdata.*;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDTO;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDTO.Main;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDTO.PreAllocate;
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
    private MaterialsOutService       materialsOutService;
    @Inject
    private RdStyleService            rdStyleService;

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber      第几页
     * @param pageSize        每页几条数据
     * @param keywords        关键词
     * @param BillType        到货单类型;采购PO  委外OM
     * @param procureType     采购类型
     * @param warehousingType 入库类别
     */
    public Page<SysPuinstore> getAdminDatas(int pageNumber, int pageSize, String keywords, String BillType, String procureType,
                                            String warehousingType) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("BillType", BillType);
        sql.eq("procureType", procureType);
        sql.eq("warehousingType", warehousingType);
        //关键词模糊查询
        sql.likeMulti(keywords, "repositoryName", "deptName", "remark");
        //排序
        sql.desc("AutoID");
        return paginate(sql);
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
     * 批量审批
     * */
    public Ret autitByIds(String ids) {
        tx(() -> {
            String[] split = ids.split(",");
            for (String id : split) {
                Ret autit = autit(Long.valueOf(id));
                System.out.println(autit);
                //ValidationUtils.isTrue(,JBoltMsg.FAIL);
            }
            return true;
        });
        return ret(true);
    }

    /*
     * 批量反审批
     * */
    public Ret resetAutitByIds(String ids) {
        tx(() -> {
            String[] split = ids.split(",");
            for (String id : split) {
                resetAutitById(id);
            }
            return true;
        });
        return ret(true);
    }

    public Ret resetAutitById(String autoid) {
        SysPuinstore sysPuinstore = findById(autoid);
        if (sysPuinstore.getIAuditStatus().equals("4")) {
            return fail("当前状态为审批不通过，无法继续审批!");
        }
        //1、更新审核人、审核时间、状态
        Date date = new Date();
        sysPuinstore.setAuditPerson(JBoltUserKit.getUserName());
        sysPuinstore.setAuditDate(date);
        sysPuinstore.setModifyPerson(JBoltUserKit.getUserName());
        sysPuinstore.setModifyDate(date);
        String iAuditStatus = String.valueOf(sysPuinstore.getIAuditStatus());
        sysPuinstore.setIAuditStatus(Integer.valueOf(countDeleteState(iAuditStatus)));
        Ret ret = update(sysPuinstore);
        return ret;
    }

    /*
     * 审批
     * */
    public Ret autit(Long autoid) {
        SysPuinstore sysPuinstore = findById(autoid);
        //1、更新审核人、审核时间、状态
        Date date = new Date();
        sysPuinstore.setAuditPerson(JBoltUserKit.getUserName());
        sysPuinstore.setAuditDate(date);
        sysPuinstore.setModifyPerson(JBoltUserKit.getUserName());
        sysPuinstore.setModifyDate(date);
        String iAuditStatus = String.valueOf(sysPuinstore.getIAuditStatus());
        sysPuinstore.setIAuditStatus(Integer.valueOf(countAddState(iAuditStatus)));
        Ret ret = update(sysPuinstore);
        //2、推送u8入库
        /*String json = getSysPuinstoreDto(sysPuinstore);
        String post = new BaseInU8Util().base_in(json);
        System.out.println(post);*/
        return ret;
    }

    /*
     * 修改审批的状态
     * */
    public String countAddState(String state) {
        return String.valueOf((Integer.valueOf(state) + 1));
    }

    /*
     * 修改批量反审批的状态
     * */
    public String countDeleteState(String state) {
        return String.valueOf((Integer.valueOf(state) - 1));
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
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("syspuinstore.recpor", kv).paginate(pageNumber, pageSize);
        for (Record record : paginate.getList()) {
            PurchaseType purchaseType = purchaseTypeService.findById(record.get("billtype"));
            Kv podetailKv = new Kv();
            podetailKv.set("vencode", record.get("vencode"));
            podetailKv.set("billno", record.get("billno"));
            podetailKv.set("sourcebillno", record.get("sourcebillno"));
            podetailKv.set("sourcebillid", record.get("sourcebillid"));
            Record detailByParam = findSysPODetailByParam(podetailKv);
            record.set("invname", detailByParam.get("invname"));
//            record.set("venname", detailByParam.get("venname"));
            if (null != purchaseType){
                record.set("cptcode", purchaseType.getCPTCode());
                record.set("cptname", purchaseType.getCPTName());
            }
        }
        return paginate;
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        //从表的数据
        deleteSysPuinstoredetailByMasId(String.valueOf(id));
        //删除主表数据
        deleteById(id);
        return ret(true);
    }

    public Ret deleteSysPuinstoredetailByMasId(String id) {
        List<SysPuinstoredetail> puinstoredetails = syspuinstoredetailservice.findDetailByMasID(id);
        if (!puinstoredetails.isEmpty()) {
            tx(() -> {
                List<String> collect = puinstoredetails.stream().map(SysPuinstoredetail::getAutoID).collect(Collectors.toList());
                syspuinstoredetailservice.deleteByIds(String.join(",", collect));
                return true;
            });
        }
        return ret(true);
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
        Record detailByParam = findSysPODetailByParam(getKv(record.get("billno"), record.get("sourcebillno")));
        String whcode = record.get("whcode");//仓库
        tx(() -> {
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
                System.out.println(update.get("state"));

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
        });
        return SUCCESS;
    }

    /*
     * 1、外作件入库后倒扣加工供应商仓库存，按物料清单生成材料出库单
     * */
    public void createMaterialsOut(SysPuinstore puinstore, String whcode, Record detailByParam) throws Exception {
        RdStyle rdStyle = rdStyleService.findBycSTCode(puinstore.getRdCode());
        if (!rdStyle.getCRdName().equals("外作品入库")) {
            return;
        }
        MaterialsOut materials = new MaterialsOut();
        materials.setAutoID(JBoltSnowflakeKit.me.nextId());
        materials.setSourceBillType(detailByParam.getStr("sourcebilltype"));
        materials.setSourceBillDid(detailByParam.getStr("sourcebilldid"));
        materials.setRdCode(puinstore.getRdCode());
        materials.setOrganizeCode(getOrgCode());
        materials.setBillNo(puinstore.getBillNo());
        materials.setBillType(puinstore.getBillType());
        materials.setBillDate(getBillDate(puinstore.getBillDate()));
        materials.setDeptCode(puinstore.getDeptCode());
        materials.setWhcode(whcode);
        materials.setVenCode(puinstore.getVenCode());
        materials.setAuditPerson(puinstore.getAuditPerson());
        materials.setAuditDate(puinstore.getAuditDate());
        materials.setCreatePerson(puinstore.getCreatePerson());
        materials.setCreateDate(puinstore.getCreateDate());
        materials.setModifyPerson(puinstore.getModifyPerson());
        materials.setModifyDate(puinstore.getModifyDate());
        materials.setState(1);
        materials.setMemo(puinstore.getMemo());
        materialsOutService.save(materials);
    }

    public Kv getKv(String billno, String sourcebillno) {
        Kv kv = new Kv();
        kv.set("billno", billno);
        kv.set("sourcebillno", sourcebillno);
        return kv;
    }

    public Date getBillDate(String billDate) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        try {
            return format.parse(billDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveSysPuinstoreModel(SysPuinstore puinstore, Record record, Record detailByParam) {
        Date date = new Date();
        puinstore.setBillType(record.get("billtype"));//采购类型
        puinstore.setOrganizeCode(getOrgCode());
        puinstore.setBillNo(record.get("billno"));
        puinstore.setBillDate(detailByParam.getStr("billdate")); //单据日期
        puinstore.setRdCode(record.get("rdcode"));
        puinstore.setDeptCode(record.get("deptcode"));
        puinstore.setSourceBillNo(record.get("sourcebillno"));//来源单号（订单号）
        puinstore.setSourceBillID(detailByParam.getStr("sourcebillid"));//来源单据id
        puinstore.setVenCode(record.get("vencode")); //供应商
        puinstore.setMemo(record.get("memo"));
        puinstore.setCreatePerson(JBoltUserKit.getUserName());
        puinstore.setCreateDate(date);
        puinstore.setAuditPerson(getOrgName()); //审核人
        puinstore.setAuditDate(date); //审核时间
        puinstore.setIAuditStatus(1);

    }

    /*
     * 获取采购订单视图
     * */
    public Page<Record> getSysPODetail(Kv kv, int size, int PageSize) {
        return dbTemplate(u8SourceConfigName(), "syspuinstore.getSysPODetail", kv).paginate(size, PageSize);
    }

    /*
     * 获取采购订单视图，不分页
     * */
    public Record findSysPODetailByParam(Kv kv) {
        return dbTemplate(u8SourceConfigName(), "syspuinstore.getSysPODetail", kv).findFirst();
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

        Record podetail = findSysPODetailByParam(kv);

        List<SysPuinstoredetail> detailList = syspuinstoredetailservice.findDetailByMasID(puinstore.getAutoID());
        int i = 1;
        for (SysPuinstoredetail detail : detailList) {
            Main main = new Main();
            main.setIsWhpos("1"); //
            main.setIwhcode(detail.getWhcode());
            main.setInvName(podetail.get("invname"));
            main.setVenName(podetail.get("venname"));
            main.setVenCode(puinstore.getVenCode());
            main.setQty(detail.getQty().toString());
            main.setOrganizeCode(getOrgCode());
            main.setInvCode(podetail.get(""));
            main.setNum(0);
            main.setIndex(String.valueOf(i));
            main.setPackRate("0");
            main.setISsurplusqty(false);
            main.setCreatePerson(detail.getCreatePerson());
            main.setBarCode(detail.getSpotTicket()); //现品票
            main.setBillNo(puinstore.getBillNo());
            main.setBillID(podetail.getStr("billid"));
            main.setBillNoRow(podetail.getStr("billnorow"));
            main.setBillDate(puinstore.getBillDate());
            main.setBillDid(podetail.getStr("billdid"));
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
        preAllocate.setCreatePerson(puinstore.getCreatePerson());
        preAllocate.setCreatePersonName(puinstore.getCreatePerson());
        preAllocate.setLoginDate(String.valueOf(puinstore.getCreateDate()));
        preAllocate.setOrganizeCode(puinstore.getOrganizeCode());
        preAllocate.setTag("PUInStore");
        preAllocate.setType("PUInStore");
        preAllocate.setUserCode(puinstore.getCreatePerson());
        //放入dto
        dto.setMainData(MainData);
        dto.setPreAllocate(preAllocate);
        dto.setUserCode(puinstore.getCreatePerson());
        dto.setOrganizeCode(puinstore.getOrganizeCode());
        dto.setToken("");
        //返回
        return JSON.toJSONString(dto);
    }
}