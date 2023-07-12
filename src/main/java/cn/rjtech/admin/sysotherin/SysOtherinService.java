package cn.rjtech.admin.sysotherin;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.admin.vouchtypedic.VouchTypeDicService;
import cn.rjtech.config.AppConfig;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.model.momdata.SysOtherin;
import cn.rjtech.model.momdata.SysOtherindetail;
import cn.rjtech.model.momdata.VouchTypeDic;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDeleteDTO;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.util.xml.XmlUtil;
import cn.rjtech.wms.utils.HttpApiUtils;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 其它入库单
 *
 * @ClassName: SysOtherinService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-05 17:57
 */
public class SysOtherinService extends BaseService<SysOtherin> implements IApprovalService {
    
    private final SysOtherin dao = new SysOtherin().dao();

    @Override
    protected SysOtherin dao() {
        return dao;
    }

    @Inject
    private PersonService personservice;
    @Inject
    private VouchTypeDicService vouchtypedicservice;
    @Inject
    private SysOtherindetailService sysotherindetailservice;

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
        return dbTemplate("sysotherin.recpor", kv).paginate(pageNumber, pageSize);
    }

    /**
     * 保存
     */
    public Ret save(SysOtherin sysOtherin) {
        if (sysOtherin == null || isOk(sysOtherin.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        boolean success = sysOtherin.save();
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(SysOtherin sysOtherin) {
        if (sysOtherin == null || notOk(sysOtherin.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // 更新时需要判断数据存在
        SysOtherin dbSysOtherin = findById(sysOtherin.getAutoID());
        if (dbSysOtherin == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = sysOtherin.update();
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysOtherin 要删除的model
     * @param kv         携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(SysOtherin sysOtherin, Kv kv) {
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysOtherin model
     * @param kv         携带额外参数一般用不上
     */
    @Override
    public String checkInUse(SysOtherin sysOtherin, Kv kv) {
        // 这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        tx(() -> {
            SysOtherin s = findFirst("select *  from T_Sys_OtherIn where AutoID in (" + id + ")");
            if ("1".equals(String.valueOf(s.getIAuditStatus())) || "2".equals(String.valueOf(s.getIAuditStatus()))) {
                ValidationUtils.error( "编号：" + s.getBillNo() + "单据状态已改变，不可删除！");
            }
            if(!s.getIcreateby().equals(JBoltUserKit.getUser().getId())){
                ValidationUtils.error( "当前登录人:"+JBoltUserKit.getUser().getName()+",单据创建人为:" + s.getCcreatename() + " 不可删除!!!");
            }
            deleteById(id);
            delete("DELETE T_Sys_OtherInDetail   where  MasID = ?", id);
            return true;
        });
        return SUCCESS;
    }

    /**
     * 批量删除主从表
     */
    public Ret deleteRmRdByIds(String ids) {
        tx(() -> {
            List<SysOtherin> sysOtherins = find("select *  from T_Sys_OtherIn where AutoID in (" + ids + ")");
            for (SysOtherin s : sysOtherins) {
                if ("1".equals(String.valueOf(s.getIAuditStatus())) || "2".equals(String.valueOf(s.getIAuditStatus()))) {
                    ValidationUtils.error( "编号：" + s.getBillNo() + "单据状态已改变，不可删除！");
                }
                if(!s.getIcreateby().equals(JBoltUserKit.getUser().getId())){
                    ValidationUtils.error( "当前登录人:"+JBoltUserKit.getUser().getName()+",单据创建人为:" + s.getCcreatename() + " 不可删除!!!");
                }
            }
            deleteByIds(ids);
            String[] split = ids.split(",");
            for (String s : split) {
                delete("DELETE T_Sys_OtherInDetail   where  MasID = ?", s);
            }
            return true;
        });
        return SUCCESS;
    }


    /**
     * 执行JBoltTable表格整体提交
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        SysOtherin sysotherin = jBoltTable.getFormModel(SysOtherin.class, "sysotherin");

        // 获取当前用户信息？
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        HashMap<String, String> vencodemap = new HashMap<>();
        HashMap<String, String> invcodemap = new HashMap<>();

        tx(() -> {
            // 通过 id 判断是新增还是修改
            if (sysotherin.getAutoID() != null) {
                sysotherin.setIupdateby(user.getId());
                sysotherin.setCupdatename(user.getName());
                sysotherin.setDupdatetime(now);
                // 主表修改
                ValidationUtils.isTrue(sysotherin.update(), ErrorMsg.UPDATE_FAILED);
            }
            // 从表的操作
            // 获取保存数据（执行保存，通过 getSaveRecordList）
            saveTableSubmitDatas(jBoltTable, sysotherin,invcodemap,vencodemap);
            // 获取修改数据（执行修改，通过 getUpdateRecordList）
            updateTableSubmitDatas(jBoltTable, sysotherin);
            // 获取删除数据（执行删除，通过 getDelete）
            deleteTableSubmitDatas(jBoltTable);
            return true;
        });
        return successWithData(sysotherin.keep("autoid"));
    }

    // 可编辑表格提交-新增数据
    private void saveTableSubmitDatas(JBoltTable jBoltTable, SysOtherin sysotherin,HashMap<String, String> invcodemap,HashMap<String, String> vencodemap) {
        List<Record> list = jBoltTable.getSaveRecordList();
        if (CollUtil.isEmpty(list)) return;
        User user = JBoltUserKit.getUser();
        ArrayList<SysOtherindetail> systailsList = new ArrayList<>();
        Date now = new Date();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            SysOtherindetail sysOtherindetail = new SysOtherindetail();


            sysOtherindetail.setIcreateby(user.getId());
            sysOtherindetail.setCcreatename(user.getName());
            sysOtherindetail.setDcreatetime(now);
            sysOtherindetail.setIupdateby(user.getId());
            sysOtherindetail.setCupdatename(user.getName());
            sysOtherindetail.setDupdatetime(now);
            sysOtherindetail.setIsDeleted(false);
            sysOtherindetail.setBarcode(row.get("barcode"));
            sysOtherindetail.setInvCode(row.get("cinvcode"));
            sysOtherindetail.setVenCode(row.get("vencode"));
            sysOtherindetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysOtherindetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysOtherindetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysOtherindetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysOtherindetail.setSourceBillID(row.getStr("sourcebilldid"));

            String s = this.insertSysOtherIn(sysOtherindetail, row, invcodemap, vencodemap, sysotherin);
            sysOtherindetail.setMasID(Long.valueOf(s));

            systailsList.add(sysOtherindetail);

        }
        sysotherindetailservice.batchSave(systailsList);
    }

    // 可编辑表格提交-修改数据
    private void updateTableSubmitDatas(JBoltTable jBoltTable, SysOtherin sysotherin) {
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) return;
        User user = JBoltUserKit.getUser();
        ArrayList<SysOtherindetail> systailsList = new ArrayList<>();
        Date now = new Date();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            SysOtherindetail sysOtherindetail = new SysOtherindetail();
            sysOtherindetail.setMasID(Long.valueOf(sysotherin.getAutoID()));
            sysOtherindetail.setIupdateby(user.getId());
            sysOtherindetail.setCupdatename(user.getName());
            sysOtherindetail.setDupdatetime(now);
            sysOtherindetail.setAutoID(Long.valueOf(row.get("autoid")));
            sysOtherindetail.setBarcode(row.get("barcode"));
            sysOtherindetail.setInvCode(row.get("cinvcode"));
            sysOtherindetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysOtherindetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysOtherindetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysOtherindetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysOtherindetail.setSourceBillID(row.getStr("sourcebilldid"));

            systailsList.add(sysOtherindetail);

        }
        sysotherindetailservice.batchUpdate(systailsList);

    }

    // 可编辑表格提交-删除数据
    private void deleteTableSubmitDatas(JBoltTable jBoltTable) {
        Object[] ids = jBoltTable.getDelete();
        if (ArrayUtil.isEmpty(ids)) return;
        sysotherindetailservice.deleteByIds(ids);

    }

    //根据从表数据生成主表id
    public String insertSysOtherIn(SysOtherindetail sysOtherindetail,Record record,HashMap<String, String> invcodemap,HashMap<String, String> vencodemap,SysOtherin sysOtherin){
        //空的是走 存货编码
        if(Objects.isNull(sysOtherindetail.getBarcode())){
            if(CollUtil.isNotEmpty(invcodemap)){
                for (Map.Entry<String, String> entry : vencodemap.entrySet()) {
                    return entry.getValue();
                }
            }
            //走新增
            User user = JBoltUserKit.getUser();
            Date now = new Date();

            sysOtherin.setBillNo(BillNoUtils.genCode(getOrgCode(), table()));
            sysOtherin.setSourceBillDid(sysOtherindetail.getSourceBillDid());
            sysOtherin.setVenCode(sysOtherindetail.getVenCode());
            sysOtherin.setOrganizeCode(getOrgCode());
            sysOtherin.setIcreateby(user.getId());
            sysOtherin.setCcreatename(user.getName());
            sysOtherin.setDcreatetime(now);
            sysOtherin.setIupdateby(user.getId());
            sysOtherin.setCupdatename(user.getName());
            sysOtherin.setDupdatetime(now);
            sysOtherin.setBillDate(DateUtil.formatDate(now));

            ValidationUtils.isTrue(sysOtherin.save(), ErrorMsg.SAVE_FAILED);
            vencodemap.put(sysOtherin.getVenCode(), sysOtherin.getAutoID());
            return sysOtherin.getAutoID();
        }else {
            //走条码，按照供应商分类
            if(CollUtil.isNotEmpty(vencodemap)){
                for (Map.Entry<String, String> entry : vencodemap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    // 判断是否包含指定的key
                    if (key.equals(sysOtherindetail.getVenCode())) {
                        return value;
                    }
                }
            }
            //走新增
            User user = JBoltUserKit.getUser();
            Date now = new Date();
            sysOtherin.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
            sysOtherin.setBillNo(BillNoUtils.genCode(getOrgCode(), table()));
            sysOtherin.setSourceBillDid(sysOtherindetail.getSourceBillDid());
            sysOtherin.setVenCode(sysOtherindetail.getVenCode());
            sysOtherin.setOrganizeCode(getOrgCode());
            sysOtherin.setIcreateby(user.getId());
            sysOtherin.setCcreatename(user.getName());
            sysOtherin.setDcreatetime(now);
            sysOtherin.setIupdateby(user.getId());
            sysOtherin.setCupdatename(user.getName());
            sysOtherin.setDupdatetime(now);
            sysOtherin.setBillDate(DateUtil.formatDate(now));
            sysOtherin.setIsDeleted(false);

            ValidationUtils.isTrue(sysOtherin.save(), ErrorMsg.SAVE_FAILED);
            vencodemap.put(sysOtherin.getVenCode(), sysOtherin.getAutoID());
            return sysOtherin.getAutoID();
        }

    }

    //推送u8数据接口
    public String pushU8(SysOtherin sysotherin,List<SysOtherindetail> sysotherindetail) {
        if(CollUtil.isEmpty(sysotherindetail)){
            return "数据不能为空";
        }

        User user = JBoltUserKit.getUser();
        JSONObject data = new JSONObject();
        data.set("userCode",user.getUsername());
        data.set("organizeCode", getOrgCode());
        data.set("token","");
        JSONObject preallocate = new JSONObject();
        preallocate.set("userCode",user.getUsername());
        preallocate.set("organizeCode", getOrgCode());
        preallocate.set("CreatePerson",user.getId());
        preallocate.set("CreatePersonName",user.getName());
        preallocate.set("loginDate",DateUtil.format(new Date(), "yyyy-MM-dd"));
        preallocate.set("tag","OtherIn");
        preallocate.set("type","OtherIn");
        data.set("PreAllocate",preallocate);
        ArrayList<Object> maindata = new ArrayList<>();
        sysotherindetail.stream().forEach(s -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("invstd","");
            jsonObject.set("iwhcode",s.getPosCode());
            jsonObject.set("barcodeqty","");
            jsonObject.set("defwhcode",sysotherin.getWhcode());
            jsonObject.set("organizecode", getOrgCode());
            jsonObject.set("invname","");
            jsonObject.set("index","1");
            jsonObject.set("vt_id","");
            jsonObject.set("defwhname","");
            jsonObject.set("billDate",DateUtil.format(s.getDcreatetime(), "yyyy-MM-dd"));
            jsonObject.set("iwhcode",sysotherin.getWhcode());
            jsonObject.set("invcode",s.getInvCode());
            jsonObject.set("userCode",user.getUsername());
            jsonObject.set("iswhpos","1");
            jsonObject.set("defposcode","");
            jsonObject.set("ispack","0");
            jsonObject.set("qty",s.getQty());
            jsonObject.set("iposcode","");
            jsonObject.set("Tag","OtherIn");
            jsonObject.set("barcode",s.getBarcode());
            jsonObject.set("IDeptCode","");
            jsonObject.set("IDeptName","");
            jsonObject.set("IRdType","101");
            jsonObject.set("IRdName","其他入库");
            jsonObject.set("IRdCode","101");
            maindata.add(jsonObject);
        });
        data.set("MainData",maindata);

        //            请求头
        Map<String, String> header = new HashMap<>(5);
        header.put("Content-Type", "application/json");
        String url = "http://localhost:8081/web/erp/common/vouchProcessDynamicSubmit";
        try {
            String post = HttpApiUtils.httpHutoolPost(url, data, header);
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(post, JsonObject.class);
            if (isOk(post)) {
                if ("200".equals(jsonObject.get("code").getAsString())) {
                    SysOtherin byId = findById(sysotherin.getAutoID());
                    byId.setU8BillNo(this.extractU8Billno(jsonObject.get("message").getAsString()));
                    byId.update();
                    return null;
                }else {
                    return "上传u8接口报错:"+jsonObject.get("message").getAsString();
                }
            }else {
                return "上传u8接口返回数据为null";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "上传u8失败:"+e.getMessage();
        }
    }
    /**
     * 提取字符串里面的数字
     */
    public String extractU8Billno(String message) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(message);
        return m.replaceAll("").trim();
    }
    /*
     * 获取删除的json
     * */
    public String getDeleteDTO(String u8billno) {
        User user = JBoltUserKit.getUser();
        
        Record userRecord = this.findU8UserByUserCode(user.getUsername());
        Record u8Record = this.findU8RdRecord01Id(u8billno);
        
        SysPuinstoreDeleteDTO deleteDTO = new SysPuinstoreDeleteDTO();
        SysPuinstoreDeleteDTO.data data = new SysPuinstoreDeleteDTO.data();
        
        data.setAccid(getOrgCode());
        data.setPassword(userRecord.get("u8_pwd"));
        data.setUserID(userRecord.get("u8_code"));
        Long id = u8Record.getLong("id");
        data.setVouchId(String.valueOf(id));//u8单据id
        deleteDTO.setData(data);

        String json = JSON.toJSONString(deleteDTO);
        LOG.info(json);
        return json;
    }
    public Record findU8UserByUserCode(String userCode) {
        return dbTemplate(u8SourceConfigName(), "syspuinstore.findU8UserByUserCode", Kv.by("usercode", userCode)).findFirst();
    }
    public Record findU8RdRecord01Id(String cCode) {
        return dbTemplate(u8SourceConfigName(), "sysotherin.findU8RdRecord01Id", Kv.by("cCode", cCode)).findFirst();
    }

    /**
     * 审核通过
     */
    public Ret process(String ids) {
        tx(() -> {
            this.check(ids);
            String[] split = ids.split(",");
            for (String s : split) {
                SysOtherin byId = findById(s);
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
     * 审核通过
     */
    public Ret approve(Long ids) {
        tx(() -> {
            //业务逻辑
            this.check(String.valueOf(ids));
//            this.passage(String.valueOf(ids));
            String[] split = String.valueOf(ids).split(",");
            for (String s : split) {
                SysOtherin byId = findById(s);
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
                SysOtherin byId = findById(s);
                byId.setIAuditStatus(AuditStatusEnum.REJECTED.getValue());
                byId.update();
            }
            return true;
        });
        return SUCCESS;
    }
    /**
     * 批量反审核
     */
    public Ret noProcess(String ids) {
        tx(() -> {
            //业务逻辑
            this.checkbelow(ids);
            //反审，调用删除U8数据接口
            String[] split = ids.split(",");
            for (String s : split) {
                SysOtherin byId = findById(s);
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
            List<SysOtherin> sysOtherins = find("select *  from T_Sys_OtherIn where AutoID in (" + p + ")");
            for (SysOtherin s : sysOtherins) {
                if ("0".equals(String.valueOf(s.getIAuditStatus()))) {
                    ValidationUtils.error( "收料编号：" + s.getBillNo() + "单据未提交审核或审批！！");
                }
                if ("2".equals(String.valueOf(s.getIAuditStatus())) || "3".equals(String.valueOf(s.getIAuditStatus()))) {
                    ValidationUtils.error( "收料编号：" + s.getBillNo() + "流程已结束！！");
                }
            }
        }
    }


    //推u8
    public void passage(String ids){
        String[] split = ids.split(",");
        for (String s : split) {
            SysOtherin sysOtherin = findFirst("select *  from T_Sys_OtherIn where AutoID in (" + s + ")");
            List<SysOtherindetail> sysOtherindetails = sysotherindetailservice.find("select *  from T_Sys_OtherIn where AutoID in (" + sysOtherin.getAutoID() + ")");
            // 测试调用接口
            System.out.println("```````````````````````````````"+ new Date());
            String s1 = pushU8(sysOtherin, sysOtherindetails);
            System.out.println(new Date()+"```````````````````````````````"+s1);
        }

    }
    public void checkbelow(String ids) {
        String[] split = ids.split(",");
        for (String p : split) {
            List<SysOtherin> sysOtherins = find("select *  from T_Sys_PUReceive where AutoID in (" + p + ")");
            for (SysOtherin s : sysOtherins) {
                if ("0".equals(String.valueOf(s.getIAuditStatus()))) {
                    ValidationUtils.error( "收料编号：" + s.getBillNo() + " 单据，流程未开始，不可反审！！");
                }
                if ("1".equals(String.valueOf(s.getIAuditStatus()))) {
                    ValidationUtils.error( "收料编号：" + s.getBillNo() + " 单据，流程未结束，不可反审！！");
                }

            }
        }
    }

    /**
     * todo  审核通过
     */
    @Override
    public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
        //添加现品票 ,todo 推送u8
        return this.passagetwo(formAutoId);
    }

    /**
     * 审核不通过
     */
    @Override
    public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
        return null;
    }

    /**
     * 实现反审之前的其他业务操作，如有异常返回错误信息
     */
    @Override
    public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        return null;
    }

    /**
     * todo 实现反审之后的其他业务操作, 如有异常返回错误信息
     */
    @Override
    public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        if(isLast){
            return this.delectbelowtwo(formAutoId);
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
     *  从已审核，撤回到已保存，前置业务实现，如有异常返回错误信息
     */
    @Override
    public String preWithdrawFromAuditted(long formAutoId) {
        return null;
    }

    /**
     * todo 从已审核，撤回到已保存，业务实现，如有异常返回错误信息
     */
    @Override
    public String postWithdrawFromAuditted(long formAutoId) {
      return this.delectbelowtwo(formAutoId);
    }

    /**
     * todo 批量审核（审批）通过，后置业务实现
     */
    @Override
    public String postBatchApprove(List<Long> formAutoIds) {
        return this.passagetwo(formAutoIds);
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
        return this.delectbelowtwo(formAutoIds);
    }


    public List<Record> billtype(Kv kv) {
        return dbTemplate("sysotherin.billtype", kv).find();
    }

    /**
     * 获取条码列表
     * 通过关键字匹配
     * autocomplete组件使用
     */
    public List<Record> getBarcodeDatas(String q, Integer limit, String orgCode) {
        return dbTemplate("sysotherin.getBarcodeDatas", Kv.by("q", q).set("limit", limit).set("orgCode", orgCode)).find();
    }

    public  String getBilltypeNmea(String cbtid){
        VouchTypeDic first = vouchtypedicservice.findFirst("select * from Bd_VouchTypeDic where cbtid = ?", cbtid);
        return first.getCBTChName();
    }

    /**
     * 获取条码列表
     * 通过关键字匹配
     * autocomplete组件使用
     */
    public Record barcode(Kv kv) {
        //先查询条码是否已添加
        Record first = dbTemplate("sysotherin.barcodeDatas", kv).findFirst();
        if (null != first) {
            ValidationUtils.error( "条码为：" + kv.getStr("barcode") + "的数据已经存在，请勿重复录入。");
        }
        first = dbTemplate("sysotherin.barcode", kv).findFirst();
        ValidationUtils.notNull(first, "未查到条码为：" + kv.getStr("barcode") + "的数据,请核实再录入。");
        return first;
    }


    //审核通过后的业务逻辑
    public String passagetwo(Long formAutoId) {
        SysOtherin byId = findById(formAutoId);
        List<SysOtherindetail> sysOtherindetails = sysotherindetailservice.find("select * from T_Sys_OtherInDetail  where MasID = ? ", byId.getAutoID());
        return this.pushU8(byId,sysOtherindetails);
    }

    //反审后的操作
    public String delectbelowtwo(long formAutoId){
        //主表数据
        SysOtherin byId = findById(formAutoId);
        //todo 删除u8的数据
        String deleteDTO = this.getDeleteDTO(byId.getU8BillNo());
        String s = this.deleteVouchProcessDynamicSubmitUrl(deleteDTO);
        byId.setU8BillNo(null);
        byId.update();
        return s;
    }

    //批量审核通过后的业务逻辑
    public String passagetwo(List<Long> formAutoId) {
        if(CollectionUtil.isEmpty(formAutoId))return "主表id不能为空";
        String s1 = null;
        for (Long s : formAutoId){
            SysOtherin byId = findById(s);
            List<SysOtherindetail> sysOtherindetails = sysotherindetailservice.find("select * from T_Sys_OtherInDetail  where MasID = ? ", byId.getAutoID());
            s1 = this.pushU8(byId, sysOtherindetails);
        }
        return s1;
    }

    //批量反审后的操作
    public String delectbelowtwo(List<Long> formAutoId){
        if(CollectionUtil.isEmpty(formAutoId)) {
            return "主表id不能为空";
        }
        String post = null;
        for (Long s : formAutoId) {
            //主表数据
            SysOtherin byId = findById(s);
            //todo 删除u8的数据
            String deleteDTO = this.getDeleteDTO(byId.getU8BillNo());
            post = this.deleteVouchProcessDynamicSubmitUrl(deleteDTO);
            byId.setU8BillNo(null);
            byId.update();
        }
        return post;
    }

    /*
     * 通知U8删其他入库单
     * */
    public String deleteVouchProcessDynamicSubmitUrl(String json) {
        String vouchSumbmitUrl = AppConfig.getStockApiUrl() + "/OtherInUnConfirmV1";
        Map<String, Object> para = new HashMap<>();
        para.put("dataJson", json);
        String inUnVouchGet = HttpUtil.post(vouchSumbmitUrl, para);
        String res = XmlUtil.readObjectFromXml(inUnVouchGet);
        LOG.info("res: {}", res);
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
        String code = jsonObject.getString("code");
        String message = StrUtil.nullToDefault(jsonObject.getString("message"), jsonObject.getString("msg"));
        if(!"200".equals(code))return message;

        String deleteUrl = AppConfig.getStockApiUrl() + "/OtherInDeleteV1";
        String deleteGet1 = HttpUtil.get(deleteUrl + "?dataJson=" + json);
//        LOG.info("rdeleteGet1: {}", deleteGet1);
//        String deleteGet = HttpUtil.post(deleteUrl, para);
//        LOG.info("deleteGet: {}", deleteGet);
//        LOG.info("para: {}", para);
        String deleteRes = XmlUtil.readObjectFromXml(deleteGet1);
        com.alibaba.fastjson.JSONObject deleteJsonObject = JSON.parseObject(deleteRes);
        String deleteCode = deleteJsonObject.getString("code");
        String deleteMessage = StrUtil.nullToDefault(deleteJsonObject.getString("message"), deleteJsonObject.getString("msg"));
        LOG.info("deleteRes: {}", deleteRes);
        if("200".equals(deleteCode)){
            return null;
        }else {
            return deleteMessage;
        }
    }
}
