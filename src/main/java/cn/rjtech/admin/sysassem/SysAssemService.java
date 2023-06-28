package cn.rjtech.admin.sysassem;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.demandpland.DemandPlanDService;
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.admin.purchaseorderd.PurchaseOrderDService;
import cn.rjtech.admin.purchaseorderdbatch.PurchaseOrderDBatchService;
import cn.rjtech.admin.purchaseorderdqty.PurchaseorderdQtyService;
import cn.rjtech.admin.purchaseorderm.PurchaseOrderMService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.HttpApiUtils;
import cn.smallbun.screw.core.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import org.json.JSONArray;

import java.math.BigDecimal;
import java.util.*;

/**
 * 组装拆卸及形态转换单
 *
 * @ClassName: SysAssemService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 09:45
 */
public class SysAssemService extends BaseService<SysAssem> implements IApprovalService {

    private final SysAssem dao = new SysAssem().dao();

    @Override
    protected SysAssem dao() {
        return dao;
    }

    @Inject
    private PurchaseOrderMService purchaseordermservice;

    @Inject
    private PurchaseOrderDService purchaseOrderDService;

    @Inject
    private PurchaseOrderDBatchService purchaseOrderDBatchService;

    @Inject
    private PersonService personservice;

    @Inject
    private SysAssemdetailService sysassemdetailservice;

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    @Inject
    private FormApprovalService formApprovalService;

    @Inject
    private PurchaseorderdQtyService purchaseorderdQtyService;

    @Inject
    private InventoryService inventoryService;

    @Inject
    private DemandPlanDService demandPlanDService;

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     * @param keywords   关键词
     * @param BillType   单据类型;采购PO  委外OM
     * @param state      状态 1已保存 2待审批 3已审批 4审批不通过
     * @param IsDeleted  删除状态：0. 未删除 1. 已删除
     */
    public Page<SysAssem> getAdminDatas(int pageNumber, int pageSize, String keywords, String BillType, String state,
                                        Boolean IsDeleted) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("BillType", BillType);
        sql.eq("state", state);
        sql.eqBooleanToChar("IsDeleted", IsDeleted);
        //关键词模糊查询
        sql.like("deptName", keywords);
        //排序
        sql.desc("AutoID");
        return paginate(sql);
    }

    /**
     * 保存
     */
    public Ret save(SysAssem sysAssem) {
        if (sysAssem == null || isOk(sysAssem.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(sysAssem.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysAssem.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(sysAssem.getAutoID(), JBoltUserKit.getUserId(), sysAssem.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(SysAssem sysAssem) {
        if (sysAssem == null || notOk(sysAssem.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        SysAssem dbSysAssem = findById(sysAssem.getAutoID());
        if (dbSysAssem == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(sysAssem.getName(), sysAssem.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysAssem.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(sysAssem.getAutoID(), JBoltUserKit.getUserId(), sysAssem.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysAssem 要删除的model
     * @param kv       携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(SysAssem sysAssem, Kv kv) {
        //addDeleteSystemLog(sysAssem.getAutoID(), JBoltUserKit.getUserId(),sysAssem.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysAssem model
     * @param kv       携带额外参数一般用不上
     */
    @Override
    public String checkInUse(SysAssem sysAssem, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(SysAssem sysAssem, String column, Kv kv) {
        //addUpdateSystemLog(sysAssem.getAutoID(), JBoltUserKit.getUserId(), sysAssem.getName(),"的字段["+column+"]值:"+sysAssem.get(column));
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
        Page<Record> paginate = dbTemplate("sysassem.recpor", kv).paginate(pageNumber, pageSize);
        return paginate;
    }

    /**
     * 批量删除主从表
     */
    public Ret deleteRmRdByIds(String ids) {
        tx(() -> {
            deleteByIds(ids);
            String[] split = ids.split(",");
            for (String s : split) {
                delete("DELETE T_Sys_AssemDetail   where  MasID = ?", s);
            }
            return true;
        });
        return ret(true);
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        tx(() -> {
            deleteById(id);
            delete("DELETE T_Sys_AssemDetail   where  MasID = ?", id);
            return true;
        });
        return ret(true);
    }

    /**
     * 执行JBoltTable表格整体提交
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        SysAssem sysotherin = jBoltTable.getFormModel(SysAssem.class, "sysAssem");
        //获取当前用户信息？
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        tx(() -> {
            //通过 id 判断是新增还是修改
            if (sysotherin.getAutoID() == null) {
                sysotherin.setOrganizeCode(getOrgCode());
                sysotherin.setBillNo(JBoltSnowflakeKit.me.nextIdStr());
                sysotherin.setIcreateby(user.getId());
                sysotherin.setCcreatename(user.getName());
                sysotherin.setDcreatetime(now);
                sysotherin.setIupdateby(user.getId());
                sysotherin.setCupdatename(user.getName());
                sysotherin.setDupdatetime(now);
                sysotherin.setIsDeleted(false);
                //主表新增
                ValidationUtils.isTrue(sysotherin.save(), ErrorMsg.SAVE_FAILED);
            } else {
                //主表修改
                sysotherin.setCupdatename(user.getName());
                sysotherin.setDupdatetime(now);
                ValidationUtils.isTrue(sysotherin.update(), ErrorMsg.UPDATE_FAILED);
            }
            //从表的操作
            if (jBoltTable.saveIsNotBlank()) {
                List<SysAssemdetail> saveModelList = jBoltTable.getSaveModelList(SysAssemdetail.class);
                saveModelList.forEach(sysAssemdetail->{
                    sysAssemdetail.setMasID(sysotherin.getAutoID());
                    sysAssemdetail.setIcreateby(user.getId());
                    sysAssemdetail.setDcreatetime(now);
                    sysAssemdetail.setIupdateby(user.getId());
                    sysAssemdetail.setDupdatetime(now);
                    sysAssemdetail.setCcreatename(user.getName());
                    sysAssemdetail.setCupdatename(user.getName());
                    sysAssemdetail.setIsDeleted(false);
                });
                sysassemdetailservice.batchSave(saveModelList);
            }
            if (jBoltTable.updateIsNotBlank()) {
                List<SysAssemdetail> updateModelList = jBoltTable.getUpdateModelList(SysAssemdetail.class);
                updateModelList.forEach(sysAssemdetail->{
                    sysAssemdetail.setIupdateby(user.getId());
                    sysAssemdetail.setDupdatetime(now);
                    sysAssemdetail.setCupdatename(user.getName());
                });
                sysassemdetailservice.batchUpdate(updateModelList, updateModelList.size());
            }
            if (jBoltTable.deleteIsNotBlank()) {
                sysassemdetailservice.deleteByIds(jBoltTable.getDelete());
            }

//            // 获取保存数据（执行保存，通过 getSaveRecordList）
//            saveTableSubmitDatas(jBoltTable, sysotherin);
//            //获取修改数据（执行修改，通过 getUpdateRecordList）
//            updateTableSubmitDatas(jBoltTable, sysotherin);
//            //获取删除数据（执行删除，通过 getDelete）
//            deleteTableSubmitDatas(jBoltTable);
            return true;
        });
        return Ret.ok().set("autoid", sysotherin.getAutoID());
    }

    //可编辑表格提交-新增数据
    private void saveTableSubmitDatas(JBoltTable jBoltTable, SysAssem sysotherin) {
        List<Record> list = jBoltTable.getSaveRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        ArrayList<SysAssemdetail> sysAssemdetails = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            SysAssemdetail sysAssemdetail = new SysAssemdetail();
            sysAssemdetail.setMasID(sysotherin.getAutoID());
            sysAssemdetail.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
            sysotherin.setIupdateby(user.getId());
            sysotherin.setCupdatename(user.getName());
            sysotherin.setDupdatetime(now);
            sysAssemdetail.setBarcode(row.get("barcode"));
            sysAssemdetail.setSourceBillNoRow(row.get("sourcebillnorow"));
            sysAssemdetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysAssemdetail.setSourceType(row.getStr("sourcebilltype"));
            sysAssemdetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysAssemdetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysAssemdetail.setSourceBillID(row.getStr("sourcebilldid"));
            sysAssemdetail.setAssemType(row.getStr("assemtype"));
            sysAssemdetail.setWhCode(row.getStr("whcode"));
            sysAssemdetail.setPosCode(row.getStr("poscode"));
            sysAssemdetail.setRowNo(Integer.valueOf(row.getStr("rowno") == null ? "0" : row.getStr("rowno")));
            sysAssemdetail.setTrackType(row.getStr("tracktype"));
            sysAssemdetail.setMemo(row.getStr("memo"));

            sysAssemdetails.add(sysAssemdetail);
        }
        sysassemdetailservice.batchSave(sysAssemdetails);
    }

    //可编辑表格提交-修改数据
    private void updateTableSubmitDatas(JBoltTable jBoltTable, SysAssem sysotherin) {
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        ArrayList<SysAssemdetail> sysAssemdetails = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            SysAssemdetail sysAssemdetail = new SysAssemdetail();
            sysAssemdetail.setMasID(sysotherin.getAutoID());
            sysotherin.setIupdateby(user.getId());
            sysotherin.setCupdatename(user.getName());
            sysotherin.setDupdatetime(now);
            sysAssemdetail.setBarcode(row.get("barcode"));
            sysAssemdetail.setSourceBillNoRow(row.get("sourcebillnorow"));
            sysAssemdetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysAssemdetail.setSourceType(row.getStr("sourcebilltype"));
            sysAssemdetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysAssemdetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysAssemdetail.setSourceBillID(row.getStr("sourcebilldid"));
            sysAssemdetail.setAssemType(row.getStr("assemtype"));
            sysAssemdetail.setWhCode(row.getStr("whcode"));
            sysAssemdetail.setPosCode(row.getStr("poscode"));
            sysAssemdetail.setRowNo(Integer.valueOf(row.getStr("rowno")));
            sysAssemdetail.setTrackType(row.getStr("tracktype"));
            sysAssemdetail.setMemo(row.getStr("memo"));

            sysAssemdetails.add(sysAssemdetail);

        }
        sysassemdetailservice.batchUpdate(sysAssemdetails);

    }

    //可编辑表格提交-删除数据
    private void deleteTableSubmitDatas(JBoltTable jBoltTable) {
        Object[] ids = jBoltTable.getDelete();
        if (ArrayUtil.isEmpty(ids)) {
            return;
        }
        sysassemdetailservice.deleteByIds(ids);
    }

    public List<Record> getdictionary(Kv kv) {
        return dbTemplate("sysassem.dictionary", kv).find();
    }

    public List<Record> style(Kv kv) {
        return dbTemplate("sysassem.style", kv).find();
    }

    public List<Record> getBarcodeDatas(String q, Integer limit, String orgCode) {
        return dbTemplate("sysassem.getBarcodeDatas", Kv.by("q", q).set("limit", limit).set("orgCode", orgCode)).find();
    }


    //推送u8数据接口
    public Ret pushU8(SysAssem sysassem, List<SysAssemdetail> sysassemdetail) {
        if (!CollectionUtils.isNotEmpty(sysassemdetail)) {
            return Ret.ok().msg("数据不能为空");
        }

        User user = JBoltUserKit.getUser();
        JSONObject data = new JSONObject();

        data.set("userCode", user.getUsername());
        data.set("organizeCode", this.getdeptid());
        data.set("token", "");

        JSONObject preallocate = new JSONObject();

        preallocate.set("userCode", user.getUsername());
        preallocate.set("password", "123456");
        preallocate.set("organizeCode", this.getdeptid());
        preallocate.set("CreatePerson", user.getId());
        preallocate.set("CreatePersonName", user.getName());
        preallocate.set("loginDate", DateUtil.format(new Date(), "yyyy-MM-dd"));
        preallocate.set("tag", "AssemVouch");
        preallocate.set("type", "AssemVouch");

        data.set("PreAllocate", preallocate);
        ArrayList<Object> maindata = new ArrayList<>();

        sysassemdetail.stream().forEach(s -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("IWhCode", s.getWhCode());
            jsonObject.set("iwhname", "");
            jsonObject.set("invcode", s.getInvcode());
            jsonObject.set("userCode", user.getUsername());
            jsonObject.set("organizeCode", this.getdeptid());
            jsonObject.set("OWhCode", s.getPosCode());
            jsonObject.set("owhname", "");
            jsonObject.set("barcode", s.getBarcode());
            jsonObject.set("invstd", "");
            jsonObject.set("invname", "");
            jsonObject.set("CreatePerson", s.getIcreateby());
            jsonObject.set("BillType", s.getAssemType());
            jsonObject.set("qty", s.getQty());
            jsonObject.set("CreatePersonName", user.getName());
            jsonObject.set("IRdName", "");
            jsonObject.set("IRdType", sysassem.getIRdCode());
            jsonObject.set("ORdName", "");
            jsonObject.set("ORdType", sysassem.getORdCode());
            jsonObject.set("Tag", "AssemVouch");
            jsonObject.set("IDeptCode", sysassem.getDeptCode());
            jsonObject.set("ODeptCode", sysassem.getDeptCode());
            jsonObject.set("VouchTemplate", "");
            jsonObject.set("RowNo", s.getRowNo());

            maindata.add(jsonObject);
        });
        data.set("MainData", maindata);
        System.out.println(data);
        //            请求头
        Map<String, String> header = new HashMap<>(5);
        header.put("Content-Type", "application/json");
        String url = "http://localhost:8081/web/erp/common/vouchProcessDynamicSubmit";

        try {
            String post = HttpApiUtils.httpHutoolPost(url, data, header);
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(post);
            if (isOk(post)) {
                if ("200".equals(jsonObject.getString("code"))) {
                    return Ret.ok().setOk().data(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Ret.fail("上传u8失败");
    }


    //通过当前登录人名称获取部门id
    public String getdeptid() {
        String dept = "001";
        User user = JBoltUserKit.getUser();
        Person person = personservice.findFirstByUserId(user.getId());
        if (null != person && "".equals(person)) {
            dept = person.getCOrgCode();
        }
        return dept;
    }

    public void commonSaveSysAssemModel(SysAssem sysAssem, SysPuinstore puinstore) {
        //sysAssem.setAutoID();
        sysAssem.setBillType(puinstore.getBillType());
        sysAssem.setOrganizeCode(getOrgCode());
        sysAssem.setBillNo(puinstore.getBillNo());
        sysAssem.setBillDate(puinstore.getBillDate());
        sysAssem.setDeptCode(puinstore.getDeptCode());
        sysAssem.setIRdCode(puinstore.getRdCode());//入库类型
        //sysAssem.setORdCode();//出库类型
        sysAssem.setMemo(puinstore.getMemo());
        sysAssem.setCcreatename(JBoltUserKit.getUserName());
        sysAssem.setDcreatetime(new Date());
        //sysAssem.setAuditPerson();
        //sysAssem.setAuditDate();
        sysAssem.setState("1");
        //sysAssem.setIAuditWay();
        //sysAssem.setDSubmitTime();
        sysAssem.setIAuditStatus(0);
        //sysAssem.setDAuditTime();
        //sysAssem.setIsDeleted();

    }

    /**
     * 根据条件获取资源
     * @param kv
     * @return
     */
    public List<Record> getResource(Kv kv){
        kv.setIfNotNull("orgCode", getOrgCode());
        List<Record> list = dbTemplate("sysassem.getResource", kv).find();
        ValidationUtils.isTrue(list.size()!=0, "找不到该现品票信息");
        return list;
    }

    /**
     * 根据条件获取资源
     * @param kv
     * @return
     */
    public Record getBarcode(Kv kv){
        kv.setIfNotNull("orgCode", getOrgCode());
        Record list = dbTemplate("sysassem.getBarcode", kv).findFirst();
        ValidationUtils.isTrue(list.size()!=0, "找不到该现品票信息");
        return list;
    }

    /**
     * 获取双单位条码数据
     * @return
     */
    public Record getBarcodeDatas(Kv kv) {
        String ibeforeinventoryid = kv.getStr("ibeforeinventoryid");
        Record firstRecord = findFirstRecord("select t2.cInvCode as beforeCode, t3.cInvCode as afterCode,t3.cInvCode as invcode,t3.cinvname,t3.cInvCode ,t3.cInvCode1,t3.cInvName1,t3.cInvStd as cinvstd,\n" +
                "t3.iAutoId,uom.cUomCode,uom.cUomName as purchasecuomname,uom.cUomName as  puunitname\n" +
                "         from Bd_InventoryChange t1\n" +
                "         left join Bd_Inventory t2 on t1.iBeforeInventoryId = t2.iAutoId\n" +
                "         left join Bd_Inventory t3 on t1.iAfterInventoryId = t3.iAutoId\n" +
                "         LEFT JOIN Bd_Uom uom on t3.iPurchaseUomId = uom.iAutoId\n" +
                "where t1.iBeforeInventoryId = '" + ibeforeinventoryid + "'");
        if (firstRecord==null){
            ValidationUtils.isTrue(false, "未查找到该物料的双单位，请先维护物料的形态对照表");
        }
        firstRecord.set("sourcebillno",kv.get("sourcebillno"));
        firstRecord.set("vencode",kv.get("vencode"));
        firstRecord.set("venname",kv.get("venname"));
        firstRecord.set("ibeforeinventoryid",kv.get("ibeforeinventoryid"));
        firstRecord.set("iafterinventoryid",kv.get("iafterinventoryid"));

        return firstRecord;
    }

    /**
     * 提审批
     */
    public Ret submit(Long iautoid) {
        tx(() -> {

            Ret ret = formApprovalService.submit(table(), iautoid, primaryKey(), "cn.rjtech.admin.sysassem.SysAssemService");
            ValidationUtils.isTrue(ret.isOk(), ret.getStr("msg"));

            return true;
        });
        return SUCCESS;
    }

    /**
     * 处理审批通过的其他业务操作，如有异常返回错误信息
     *
     * @param formAutoId    单据ID
     * @param isWithinBatch 是否为批量处理
     * @return 错误信息
     */
    @Override
    public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
        tx(() -> {
            //添加现品票 ,todo 推送u8
            Ret passagetwo = this.passagetwo(formAutoId);

            return true;
        });
        return null;

    }

    @Override
    public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
        return null;
    }

    @Override
    public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        return null;
    }

    /**
     * 反审之后的操作
     * @param formAutoId 单据ID
     * @param isFirst    是否为审批的第一个节点
     * @param isLast     是否为审批的最后一个节点
     * @return
     */
    @Override
    public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        if(isLast){
            //删除现品票 todo 删除u8 单据
            this.delectbelowtwo(formAutoId);
        }
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

    /**
     * 从已审核，撤回到已保存，业务实现，如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @return
     */
    @Override
    public String postWithdrawFromAuditted(long formAutoId) {
        //删除现品票 todo 删除u8 单据
        this.delectbelowtwo(formAutoId);
        return null;
    }

    /**
     * 批量审核（审批）通过，后置业务实现
     *
     * @param formAutoIds 单据IDs
     * @return 错误信息
     */
    @Override
    public String postBatchApprove(List<Long> formAutoIds) {
        this.passagetwo(formAutoIds);
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

    //审核通过后的业务逻辑
    public Ret passagetwo(Long formAutoId) {
        SysAssem byId = findById(formAutoId);
        byId.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
        byId.setIAuditWay(AuditStatusEnum.AWAIT_AUDIT.getValue());
        byId.update();
        //获取转换前的所有数据
        List<SysAssemdetail> firstBy = sysassemdetailservice.findFirstBy(formAutoId.toString());
        List<PurchaseOrderDBatch> purchaseOrderDBatchList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(firstBy)){
            for(SysAssemdetail detail : firstBy){
                //生成现品票
                this.cashNotTransaction(formAutoId,detail,purchaseOrderDBatchList);
                //对转换前的现品票扣减失效
                PurchaseOrderDBatch first1 = purchaseOrderDBatchService.findFirst("select * from  PS_PurchaseOrderDBatch where cCompleteBarcode = ?", detail.getBarcode());
                first1.setIsEffective(false);
                purchaseOrderDBatchService.update(first1);
            }
        }
        //推送u8，查出转换后从表数据
        List<SysAssemdetail> first = sysassemdetailservice.findFirst(formAutoId.toString());
        ArrayList<SysAssemdetail> merge = this.merge(first);
        System.out.println("开始上传u8开始上传u8开始上传u8开始上传u8开始上传u8开始上传u8" + new Date());
        Ret ret = this.pushU8(byId, merge);
        System.out.println(new Date() + "u8上传结束，u8上传结束，u8上传结束，u8上传结束，u8上传结束" + ret);
        return ret;
    }

    //批量审核通过后的业务逻辑
    public void passagetwo(List<Long> formAutoId) {
        for (Long s : formAutoId) {
            SysAssem byId = findById(s);
            byId.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
            byId.setIAuditWay(AuditStatusEnum.AWAIT_AUDIT.getValue());
            byId.update();
            //获取转换前的所有数据
            List<SysAssemdetail> firstBy = sysassemdetailservice.findFirstBy(s.toString());
            List<PurchaseOrderDBatch> purchaseOrderDBatchList = new ArrayList<>();
            if(!CollectionUtils.isEmpty(firstBy)){
                for(SysAssemdetail detail : firstBy){
                    //生成现品票
                    this.cashNotTransaction(s,detail,purchaseOrderDBatchList);
                    //对转换前的现品票扣减失效
                    PurchaseOrderDBatch first1 = purchaseOrderDBatchService.findFirst("select * from  PS_PurchaseOrderDBatch where cCompleteBarcode = ?", detail.getBarcode());
                    first1.setIsEffective(false);
                    purchaseOrderDBatchService.update(first1);
                }
            }
            //推送u8，查出转换后从表数据
            List<SysAssemdetail> first = sysassemdetailservice.findFirst(s.toString());
            ArrayList<SysAssemdetail> merge = this.merge(first);
            System.out.println("开始上传u8开始上传u8开始上传u8开始上传u8开始上传u8开始上传u8" + new Date());
            Ret ret = this.pushU8(byId, merge);
            System.out.println(new Date() + "u8上传结束，u8上传结束，u8上传结束，u8上传结束，u8上传结束" + ret);
        }
    }

    //生成现品票
    public void cashNotTransaction(Long s,SysAssemdetail detail,List<PurchaseOrderDBatch> purchaseOrderDBatchList) {
        //通过现品票获取到采购订单等相关的数据
        String barcode = detail.getBarcode();
        //采购订单现品票
        PurchaseOrderDBatch first1 = purchaseOrderDBatchService.findFirst("select * from  PS_PurchaseOrderDBatch where cCompleteBarcode = ?", barcode);
        //采购订单从表
        PurchaseOrderD first2 = purchaseOrderDService.findFirst("select * from  PS_PurchaseOrderD where iAutoId = ?", first1.getIPurchaseOrderDid());
        //采购订单主表
        PurchaseOrderM first3 = purchaseordermservice.findFirst("select * from  PS_PurchaseOrderM where iAutoId = ?", first2.getIPurchaseOrderMid());
        //采购订单明细数量
        PurchaseorderdQty first4 = purchaseorderdQtyService.findFirst("select d.iInventoryId,qty.* from PS_PurchaseOrderD_Qty qty LEFT JOIN  PS_PurchaseOrderD d ON d.iAutoId = qty.iPurchaseOrderDid where qty.iPurchaseOrderDid = ?", first2.getIAutoId());
        //获取转换后的从表数据
        SysAssemdetail first5 = sysassemdetailservice.findFirst(s.toString(), detail.getCombination());
        //通过存货编码获取单位
        Inventory first6 = inventoryService.findFirst("select t3.*,uom.cUomCode,uom.cUomName FROM Bd_Inventory t3 LEFT JOIN Bd_Uom uom on t3.iPurchaseUomId = uom.iAutoId where t3.cInvCode = ?", detail.getInvcode());
        //采购订单现品票表只有数量
        BigDecimal sourceQty = first5.getBigDecimal("qty");
//        if("KG".equals(first6.getStr("cuomname"))){
//            sourceQty = first5.getBigDecimal("weight");
//        }else {
//            sourceQty = first5.getBigDecimal("qty");
//        }
        // 源数量，数量应该是转换后的
//                    BigDecimal sourceQty = first4.getBigDecimal(PurchaseorderdQty.IQTY);
        Long purchaseOrderDId = first4.getLong(PurchaseorderdQty.IPURCHASEORDERDID);
//        Long iPurchaseOrderdQtyId = first4.getLong(PurchaseorderdQty.IAUTOID);
        Long iPurchaseOrderdQtyId = Long.valueOf(first5.getAutoID());
        Long inventoryId = first4.getLong(PurchaseOrderD.IINVENTORYID);
        String dateStr = demandPlanDService.getDate(first4.getStr(PurchaseorderdQty.IYEAR), first4.getInt(PurchaseorderdQty.IMONTH),first4.getInt(PurchaseorderdQty.IDATE));
        DateTime planDate = DateUtil.parse(dateStr, DatePattern.PURE_DATE_PATTERN);
        // 包装数量
        BigDecimal pkgQty = first4.getBigDecimal(Inventory.IPKGQTY);
        // 包装数量为空或者为0，生成一张条码，原始数量/打包数量
        if (ObjectUtil.isNull(pkgQty) || BigDecimal.ZERO.compareTo(pkgQty) == 0 || sourceQty.compareTo(pkgQty) <= 0) {
            String barCode = purchaseOrderDBatchService.generateBarCode();
            PurchaseOrderDBatch purchaseOrderDBatch = purchaseOrderDBatchService.createPurchaseOrderDBatch(purchaseOrderDId, iPurchaseOrderdQtyId, inventoryId, planDate, sourceQty, barCode);
            purchaseOrderDBatchList.add(purchaseOrderDBatch);
        }else {
            // 源数量/包装数量 （向上取）
            int count = sourceQty.divide(pkgQty, 0, BigDecimal.ROUND_UP).intValue();
            for (int i = 0; i < count; i++) {
                // count-1： 69/10; 9
                String barCode = purchaseOrderDBatchService.generateBarCode();
                if (i == count - 1) {
                    BigDecimal qty = sourceQty.subtract(BigDecimal.valueOf(i).multiply(pkgQty));
                    PurchaseOrderDBatch purchaseOrderDBatch = purchaseOrderDBatchService.createPurchaseOrderDBatch(purchaseOrderDId, iPurchaseOrderdQtyId, inventoryId, planDate, qty, barCode);
                    purchaseOrderDBatchList.add(purchaseOrderDBatch);
                    break;
                }
                PurchaseOrderDBatch purchaseOrderDBatch = purchaseOrderDBatchService.createPurchaseOrderDBatch(purchaseOrderDId, iPurchaseOrderdQtyId, inventoryId, planDate, pkgQty, barCode);
                purchaseOrderDBatchList.add(purchaseOrderDBatch);
            }
        }
        purchaseOrderDBatchService.batchSave(purchaseOrderDBatchList);
    }

    //根据存货编码合并数量和重量
    public ArrayList<SysAssemdetail> merge(List<SysAssemdetail> first){
        //先把存货编码相同的数量合并
        ArrayList<SysAssemdetail> merge = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(first)) {
            for(SysAssemdetail sys : first ){
                boolean have = true;
                if(CollectionUtils.isNotEmpty(merge)){
                    for (SysAssemdetail me : merge){
                        if(me.getInvcode().equals(sys.getInvcode())){
                            me.setQty(me.getQty().add(sys.getQty()));
                            me.setWeight(new BigDecimal(me.getWeight()).add(new BigDecimal(sys.getWeight())).toString());
                            //找到，就不用新增
                            have = false;
                        }
                    }
                    if(have){
                        merge.add(sys);
                    }
                }else {
                    merge.add(sys);
                }
            }
        }
        return merge;
    }

    //反审后的操作
    public void delectbelowtwo(long formAutoId){
        List<SysAssemdetail> firstBy = sysassemdetailservice.findFirstByall(formAutoId);
        if(CollectionUtils.isNotEmpty(firstBy)){
            for (SysAssemdetail s : firstBy ){
                if(s.getAssemType().equals("转换前")){
                    //对转换前的现品票扣减失效
                    PurchaseOrderDBatch first1 = purchaseOrderDBatchService.findFirst("select * from  PS_PurchaseOrderDBatch where cCompleteBarcode = ?", s.getBarcode());
                    first1.setIsEffective(true);
                    purchaseOrderDBatchService.update(first1);
                }else {
                 delete("DELETE PS_PurchaseOrderDBatch where iPurchaseOrderdQtyId = ?",s.getAutoID());
                }
            }
        }
        //todo 删除u8的数据

    }


}