package cn.rjtech.admin.sysproductin;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONObject;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.model.momdata.Person;
import cn.rjtech.model.momdata.SysProductin;
import cn.rjtech.model.momdata.SysProductindetail;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.HttpApiUtils;
import cn.smallbun.screw.core.util.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.json.JSONArray;

import java.math.BigDecimal;
import java.util.*;

/**
 * 产成品入库单
 * @ClassName: SysProductinService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-08 09:56
 */
public class SysProductinService extends BaseService<SysProductin> {
    private final SysProductin dao = new SysProductin().dao();

    @Override
    protected SysProductin dao() {
        return dao;
    }

    @Inject
    private PersonService personservice;

    @Inject
    private FormApprovalService formApprovalService;

    @Inject
    private SysProductindetailService sysproductindetailservice;

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     * @param pageNumber      第几页
     * @param pageSize        每页几条数据
     * @param keywords        关键词
     * @param SourceBillType  来源类型;MO生产工单
     * @param BillType        业务类型
     * @param state           状态 1已保存 2待审批 3已审批 4审批不通过
     * @param IsDeleted       删除状态：0. 未删除 1. 已删除
     * @param warehousingType 入库类别
     * @return
     */
    public Page<SysProductin> getAdminDatas(int pageNumber, int pageSize, String keywords, String SourceBillType, String BillType, String state, Boolean IsDeleted, String warehousingType) {
        // 创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        // sql条件处理
        sql.eq("SourceBillType", SourceBillType);
        sql.eq("BillType", BillType);
        sql.eq("state", state);
        sql.eqBooleanToChar("IsDeleted", IsDeleted);
        sql.eq("warehousingType", warehousingType);
        // 关键词模糊查询
        sql.likeMulti(keywords, "deptName", "remark", "repositoryName");
        // 排序
        sql.desc("AutoID");
        return paginate(sql);
    }

    /**
     * 保存
     * @param sysProductin
     * @return
     */
    public Ret save(SysProductin sysProductin) {
        if (sysProductin == null || isOk(sysProductin.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        boolean success = sysProductin.save();
        return ret(success);
    }

    /**
     * 更新
     * @param sysProductin
     * @return
     */
    public Ret update(SysProductin sysProductin) {
        if (sysProductin == null || notOk(sysProductin.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // 更新时需要判断数据存在
        SysProductin dbSysProductin = findById(sysProductin.getAutoID());
        if (dbSysProductin == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = sysProductin.update();
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     * @param sysProductin 要删除的model
     * @param kv           携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(SysProductin sysProductin, Kv kv) {
        return null;
    }

    /**
     * 检测是否可以删除
     * @param sysProductin model
     * @param kv           携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkInUse(SysProductin sysProductin, Kv kv) {
        // 这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(SysProductin sysProductin, String column, Kv kv) {
        return null;
    }

    /**
     * 后台管理数据查询
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     * @return
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("sysproductin.recpor", kv).paginate(pageNumber, pageSize);
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
                delete("DELETE T_Sys_ProductInDetail   where  MasID = ?", s);
            }
            return true;
        });
        return SUCCESS;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public Ret delete(Long id) {
        tx(() -> {
            deleteById(id);
            delete("DELETE T_Sys_ProductInDetail   where  MasID = ?", id);
            return true;
        });
        return SUCCESS;
    }

    /**
     * 执行JBoltTable表格整体提交
     * @param jBoltTable
     * @return
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
//        if (jBoltTable.getSaveRecordList() == null && jBoltTable.getDelete() == null && jBoltTable.getUpdateRecordList() == null) {
//            return fail("行数据不能为空");
//        }
        SysProductin sysotherin = jBoltTable.getFormModel(SysProductin.class, "sysProductin");
        // 获取当前用户信息？
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        tx(() -> {
            // 通过 id 判断是新增还是修改
            if (sysotherin.getAutoID() == null) {
                sysotherin.setOrganizeCode(getOrgCode());
                sysotherin.setBillNo(JBoltSnowflakeKit.me.nextIdStr());
                sysotherin.setIcreateby(user.getId());
                sysotherin.setCcreatename(user.getName());
                sysotherin.setDcreatetime(now);
                sysotherin.setIupdateby(user.getId());
                sysotherin.setCupdatename(user.getName());
                sysotherin.setDupdatetime(now);
                sysotherin.setIAuditStatus(AuditStatusEnum.NOT_AUDIT.getValue());
                sysotherin.setIsDeleted(false);
                // 主表新增
                ValidationUtils.isTrue(sysotherin.save(), ErrorMsg.SAVE_FAILED);
            } else {
                sysotherin.setIupdateby(user.getId());
                sysotherin.setCupdatename(user.getName());
                sysotherin.setDupdatetime(now);
                // 主表修改
                ValidationUtils.isTrue(sysotherin.update(), ErrorMsg.UPDATE_FAILED);
            }
            // 从表的操作
            // 获取保存数据（执行保存，通过 getSaveRecordList）
            saveTableSubmitDatas(jBoltTable, sysotherin);
            // 获取修改数据（执行修改，通过 getUpdateRecordList）
            updateTableSubmitDatas(jBoltTable, sysotherin);
            // 获取删除数据（执行删除，通过 getDelete）
            deleteTableSubmitDatas(jBoltTable);
            return true;
        });
        return Ret.ok().set("autoid", sysotherin.getAutoID());
    }

    // 可编辑表格提交-新增数据
    private void saveTableSubmitDatas(JBoltTable jBoltTable, SysProductin sysotherin) {
        List<Record> list = jBoltTable.getSaveRecordList();
        if (CollUtil.isEmpty(list)) return;
        ArrayList<SysProductindetail> sysproductindetail = new ArrayList<>();
        Date now = new Date();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            SysProductindetail sysdetail = new SysProductindetail();
            sysdetail.setBarcode(row.get("barcode"));
            sysdetail.setInvCode(row.get("cinvcode"));
            sysdetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysdetail.setMasID(sysotherin.getAutoID());
            sysdetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysdetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysdetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysdetail.setSourceBillID(row.getStr("sourcebilldid"));
            sysdetail.setDcreatetime(now);
            sysdetail.setDcreatetime(now);
            sysdetail.setIsDeleted(false);
            sysproductindetail.add(sysdetail);
        }
        sysproductindetailservice.batchSave(sysproductindetail);
    }

    // 可编辑表格提交-修改数据
    private void updateTableSubmitDatas(JBoltTable jBoltTable, SysProductin sysotherin) {
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) return;
        ArrayList<SysProductindetail> sysproductindetail = new ArrayList<>();
        Date now = new Date();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            SysProductindetail sysdetail = new SysProductindetail();
            sysdetail.setAutoID(row.get("autoid").toString());
            sysdetail.setBarcode(row.get("barcode"));
            sysdetail.setInvCode(row.get("cinvcode"));
            sysdetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysdetail.setMasID(sysotherin.getAutoID());
            sysdetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysdetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysdetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysdetail.setSourceBillID(row.getStr("sourcebilldid"));
            sysdetail.setDcreatetime(now);
            sysdetail.setDcreatetime(now);
            sysproductindetail.add(sysdetail);

        }
        sysproductindetailservice.batchUpdate(sysproductindetail);

    }

    // 可编辑表格提交-删除数据
    private void deleteTableSubmitDatas(JBoltTable jBoltTable) {
        Object[] ids = jBoltTable.getDelete();
        if (ArrayUtil.isEmpty(ids)) return;
        sysproductindetailservice.deleteByIds(ids);
    }

    public List<Record> getwareHouseDatas(Kv kv) {
        return dbTemplate("sysproductin.wareHouse", kv).find();
    }

    public List<Record> getRdStyleDatas(Kv kv) {
        return dbTemplate("sysproductin.RdStyle", kv).find();
    }

    public List<Record> getDepartmentDatas(Kv kv) {
        return dbTemplate("sysproductin.Department", kv).find();
    }

    public Record selectName(String username) {
        Kv kv = new Kv();
        kv.set("username", username);
        return dbTemplate("sysproductin.selectname", kv).findFirst();
    }



    //推送u8数据接口
    public Ret pushU8(SysProductin sysproductin, List<SysProductindetail> sysproductindetail) {
        if(!CollectionUtils.isNotEmpty(sysproductindetail)){
            return Ret.ok().msg("数据不能为空");
        }

        User user = JBoltUserKit.getUser();
        JSONObject data = new JSONObject();

        data.set("userCode",user.getUsername());
        data.set("organizeCode",this.getdeptid());
        data.set("token","");

        JSONObject preallocate = new JSONObject();


        preallocate.set("userCode",user.getUsername());
        preallocate.set("organizeCode",this.getdeptid());
        preallocate.set("CreatePerson",user.getUsername());
        preallocate.set("CreatePersonName",user.getName());
        preallocate.set("loginDate", DateUtil.format(new Date(), "yyyy-MM-dd"));
        preallocate.set("tag","ProductionIn");
        preallocate.set("type","ProductionIn");

        data.put("PreAllocate",preallocate);

        ArrayList<Object> maindata = new ArrayList<>();
        sysproductindetail.stream().forEach(s -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("Tag","ProductionIn");
            jsonObject.set("BillDate",sysproductin.getBillDate());
            jsonObject.set("organizecode",this.getdeptid());
            jsonObject.set("iwhcode",sysproductin.getWhcode());
            jsonObject.set("iposcode",s.getPosCode());
            jsonObject.set("iposname","");
            jsonObject.set("ispack","0");
            jsonObject.set("iswhpos","0");
            jsonObject.set("index","1");
            jsonObject.set("packrate","1");
            jsonObject.set("barcode",s.getBarcode());
            jsonObject.set("batch","");
//            jsonObject.set("sourcebillnorow","");
//            jsonObject.set("sourcebillrowno",s.getSourceBIllNoRow());
//            jsonObject.set("sourcebillno",s.getSourceBillNo());
//            jsonObject.set("sourcebilltype",s.getSourceBillType());
            jsonObject.set("sourcebillnorow","8080000001");
            jsonObject.set("sourcebillrowno","8080000001-1");
            jsonObject.set("sourcebillno",sysproductin.getBillNo());
            jsonObject.set("sourcebilltype","MO");
            jsonObject.set("sourcebillqty",s.getQty().toString());
            jsonObject.set("sourcebilldid",s.getSourceBillDid());
            jsonObject.set("invcode",s.getInvCode());
            jsonObject.set("invname","1");
            jsonObject.set("invstd","");
            jsonObject.set("qty",s.getQty().toString());
            jsonObject.set("noreceivedqty","");
            jsonObject.set("receivedqty","");
            jsonObject.set("vencode",sysproductin.getVenCode());
            jsonObject.set("encodingname","产成品条码");
            jsonObject.set("encodingcode","ProductionBarCode");
            jsonObject.set("IDeptCode",this.getdeptid());
            jsonObject.set("IRdType","101");
            jsonObject.set("IRdName","生产入库");
            jsonObject.set("IRdCode","101");

            maindata.add(jsonObject);
        });
        data.set("MainData",maindata);
        System.out.println("```````````"+data);

        //            请求头
        Map<String, String> header = new HashMap<>(5);
        header.put("Content-Type", "application/json");
        String url = "http://localhost:8081/web/erp/common/vouchProcessDynamicSubmit";
        try {
            String post = HttpApiUtils.httpHutoolPost(url, data, header);
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(post);
            if (isOk(post)) {
                if ("201".equals(jsonObject.getString("code"))) {
                    return Ret.ok().setOk().data(jsonObject);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return fail("上传u8失败");
    }


    //通过当前登录人名称获取部门id
    public String getdeptid(){
        String dept = "001";
        User user = JBoltUserKit.getUser();
        Person person = personservice.findFirstByUserId(user.getId());
        if(null != person && "".equals(person)){
            dept = person.getCOrgCode();
        }
        return dept;
    }

    /**
     * 提审批
     */
    public Ret submit(Long iautoid) {
        tx(() -> {

            Ret ret = formApprovalService.submit(table(), iautoid, primaryKey(), "cn.rjtech.admin.sysproductin.SysProductinService");
            ValidationUtils.isTrue(ret.isOk(), ret.getStr("msg"));

            return true;
        });
        return SUCCESS;
    }

    /**
     * 审核通过
     */
    public Ret approve(Long ids) {
        tx(() -> {
            //业务逻辑
            this.check(String.valueOf(ids));
//            this.passage(String.valueOf(ids));
            String[] split = String.valueOf(ids).split(",");
            for (String s : split) {
                SysProductin byId = findById(s);
                byId.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
                byId.update();
            }
            return true;
        });
        return SUCCESS;
    }

    /**
     * 审核不通过
     */
    public Ret reject(Long ids) {
        tx(() -> {
            //业务逻辑
            this.check(String.valueOf(ids));
            //调用u8接口删除数据。

            String[] split = String.valueOf(ids).split(",");
            for (String s : split) {
                SysProductin byId = findById(s);
                byId.setIAuditStatus(AuditStatusEnum.REJECTED.getValue());
                byId.update();
            }
            return true;
        });
        return SUCCESS;
    }

    /**
     * 审核通过
     */
    public Ret process(String ids) {
        tx(() -> {
            this.check(ids);
            String[] split = ids.split(",");
            for (String s : split) {
                SysProductin byId = findById(s);
                byId.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
                byId.setIAuditWay(AuditStatusEnum.AWAIT_AUDIT.getValue());
                byId.update();
            }
            //业务逻辑
            this.passage(ids);
            return true;
        });
        return SUCCESS;
    }
    /**
     * 批量反审核
     */
    public Ret noProcess(String ids) {
        tx(() -> {
            //反审，调用删除U8数据接口
            String[] split = ids.split(",");
            for (String s : split) {
                SysProductin byId = findById(s);
                byId.setIAuditStatus(AuditStatusEnum.NOT_AUDIT.getValue());
                byId.setIAuditWay(null);
                byId.update();
            }

            return true;
        });
        return SUCCESS;
    }


    public void check(String ids) {
        String[] split = ids.split(",");
        for (String p : split) {
            List<SysProductin> SysProductin = find("select *  from T_Sys_ProductIn where AutoID in (" + p + ")");
            for (SysProductin s : SysProductin) {
                if ("0".equals(String.valueOf(s.getIAuditStatus()))) {
                    ValidationUtils.isTrue(false, "收料编号：" + s.getBillNo() + "单据未提交审核或审批！！");
                }
                if ("2".equals(String.valueOf(s.getIAuditStatus())) || "3".equals(String.valueOf(s.getIAuditStatus()))) {
                    ValidationUtils.isTrue(false, "收料编号：" + s.getBillNo() + "流程已结束！！");
                }
            }
        }
    }

    //推u8
    public void passage(String ids){
        String[] split = ids.split(",");
        for (String s : split) {
            SysProductin sysProductin = findFirst("select *  from T_Sys_ProductIn where AutoID in (" + s + ")");
            List<SysProductindetail> sysProductindetails = sysproductindetailservice.find("select *  from T_Sys_ProductInDetail where MasID in (" + sysProductin.getAutoID() + ")");
            // 测试调用接口
            System.out.println("```````````````````````````````"+ new Date());
            Ret ret = pushU8(sysProductin, sysProductindetails);
            System.out.println(new Date()+"```````````````````````````````"+ret);
        }

    }
    public void checkbelow(String ids) {
        String[] split = ids.split(",");
        for (String p : split) {
            List<SysProductin> sysProductins = find("select *  from T_Sys_PUReceive where AutoID in (" + p + ")");
            for (SysProductin s : sysProductins) {
                if ("0".equals(String.valueOf(s.getIAuditStatus()))) {
                    ValidationUtils.isTrue(false, "收料编号：" + s.getBillNo() + " 单据，流程未开始，不可反审！！");
                }
                if ("1".equals(String.valueOf(s.getIAuditStatus()))) {
                    ValidationUtils.isTrue(false, "收料编号：" + s.getBillNo() + " 单据，流程未结束，不可反审！！");
                }

            }
        }
    }


    /**
     * 获取条码列表
     * 通过关键字匹配
     * autocomplete组件使用
     */
    public List<Record> getBarcodeDatas(String q, Integer limit, String orgCode) {
        return dbTemplate("sysproductin.getBarcodeDatas", Kv.by("q", q).set("limit", limit).set("orgCode", orgCode)).find();
    }

    /**
     * 获取条码列表
     * 通过关键字匹配
     * autocomplete组件使用
     */
    public Record barcode(Kv kv) {
        //先查询条码是否已添加
        Record first = dbTemplate("sysproductin.barcodeDatas", kv).findFirst();
        if (null != first) {
            ValidationUtils.isTrue(false, "条码为：" + kv.getStr("barcode") + "的数据已经存在，请勿重复录入。");
        }
        first = dbTemplate("syspureceive.barcode", kv).findFirst();
        ValidationUtils.notNull(first, "未查到条码为：" + kv.getStr("barcode") + "的数据,请核实再录入。");
        return first;
    }

}
