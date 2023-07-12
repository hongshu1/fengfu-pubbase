package cn.rjtech.admin.sysassem;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.admin.purchaseorderdbatch.PurchaseOrderDBatchService;
import cn.rjtech.admin.sysassembarcode.SysAssembarcodeService;
import cn.rjtech.config.AppConfig;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.*;
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
    private PersonService personservice;
    @Inject
    private InventoryService inventoryService;
    @Inject
    private DepartmentService departmentservice;
    @Inject
    private SysAssemdetailService sysassemdetailservice;
    @Inject
    private SysAssembarcodeService sysassembarcodeservice;
    @Inject
    private PurchaseOrderDBatchService purchaseOrderDBatchService;

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
            List<SysAssem> sysAssems = find("select *  from T_Sys_Assem where AutoID in (" + ids + ")");
            for (SysAssem s : sysAssems) {
                if ("1".equals(String.valueOf(s.getIAuditStatus())) || "2".equals(String.valueOf(s.getIAuditStatus()))) {
                    ValidationUtils.isTrue(false, "收料编号：" + s.getBillNo() + "单据状态已改变，不可删除！");
                }
                if(!s.getIcreateby().equals(JBoltUserKit.getUser().getId())){
                    ValidationUtils.error( "当前登录人:"+JBoltUserKit.getUser().getName()+",单据创建人为:" + s.getCcreatename() + " 不可删除!!!");
                }
            }
            deleteByIds(ids);
            String[] split = ids.split(",");
            for (String s : split) {
                List<SysAssemdetail> sysAssemdetails = sysassemdetailservice.find("select * from T_Sys_AssemDetail where MasID = ? ", s);
                if(CollectionUtil.isNotEmpty(sysAssemdetails)) {
                    for (SysAssemdetail d : sysAssemdetails) {
                        delete("DELETE T_Sys_AssemBarcode   where  MasID = ?", d.getAutoID());
                    }
                }
                delete("DELETE T_Sys_AssemDetail   where  MasID = ?", s);
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
            SysAssem byId = findById(id);
            if ("2".equals(String.valueOf(byId.getIAuditStatus())) || "1".equals(String.valueOf(byId.getIAuditStatus()))) {
                ValidationUtils.isTrue(false, "收料编号：" + byId.getBillNo() + "单据状态已改变，不可删除！");
            }
            if(!byId.getIcreateby().equals(JBoltUserKit.getUser().getId())){
                ValidationUtils.error( "当前登录人:"+JBoltUserKit.getUser().getName()+",单据创建人为:" + byId.getCcreatename() + " 不可删除!!!");
            }
            deleteById(id);
            List<SysAssemdetail> sysAssemdetails = sysassemdetailservice.find("select * from T_Sys_AssemDetail where MasID = ? ", id);
            if(CollectionUtil.isNotEmpty(sysAssemdetails)) {
                for (SysAssemdetail d : sysAssemdetails) {
                    delete("DELETE T_Sys_AssemBarcode   where  MasID = ?", d.getAutoID());
                }
            }
            delete("DELETE T_Sys_AssemDetail   where  MasID = ?", id);
            return true;
        });
        return SUCCESS;
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
                sysotherin.setBillNo(BillNoUtils.genCode(getOrgCode(), table()));
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
//            if (jBoltTable.saveIsNotBlank()) {
//                List<SysAssemdetail> saveModelList = jBoltTable.getSaveModelList(SysAssemdetail.class);
//                saveModelList.forEach(sysAssemdetail->{
//                    sysAssemdetail.setMasID(sysotherin.getAutoID());
//                    sysAssemdetail.setIcreateby(user.getId());
//                    sysAssemdetail.setDcreatetime(now);
//                    sysAssemdetail.setIupdateby(user.getId());
//                    sysAssemdetail.setDupdatetime(now);
//                    sysAssemdetail.setCcreatename(user.getName());
//                    sysAssemdetail.setCupdatename(user.getName());
//                    sysAssemdetail.setIsDeleted(false);
//                });
//                sysassemdetailservice.batchSave(saveModelList);
//            }
//            if (jBoltTable.updateIsNotBlank()) {
//                List<SysAssemdetail> updateModelList = jBoltTable.getUpdateModelList(SysAssemdetail.class);
//                updateModelList.forEach(sysAssemdetail->{
//                    sysAssemdetail.setIupdateby(user.getId());
//                    sysAssemdetail.setDupdatetime(now);
//                    sysAssemdetail.setCupdatename(user.getName());
//                });
//                sysassemdetailservice.batchUpdate(updateModelList, updateModelList.size());
//            }
//            if (jBoltTable.deleteIsNotBlank()) {
//                sysassemdetailservice.deleteByIds(jBoltTable.getDelete());
//            }

            // 获取保存数据（执行保存，通过 getSaveRecordList）
            saveTableSubmitDatas(jBoltTable, sysotherin);
            //获取修改数据（执行修改，通过 getUpdateRecordList）
            updateTableSubmitDatas(jBoltTable, sysotherin);
            //获取删除数据（执行删除，通过 getDelete）
            deleteTableSubmitDatas(jBoltTable);
            return true;
        });
        return successWithData(sysotherin.keep("autoid"));
    }

    //可编辑表格提交-新增数据
    private void saveTableSubmitDatas(JBoltTable jBoltTable, SysAssem sysotherin) {
        List<Record> list = jBoltTable.getSaveRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        ArrayList<SysAssembarcode> sysassembarcodeList = new ArrayList<>();
        for (Record row : list) {
            SysAssemdetail sysAssemdetail = new SysAssemdetail();
            sysAssemdetail.setMasID(sysotherin.getAutoID());
            sysAssemdetail.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
            // todo 唐工 说形态转换的没有来源
//            sysAssemdetail.setBarcode(row.get("barcode"));
//            sysAssemdetail.setSourceBillNoRow(row.getStr("sourcebillnorow"));
//            sysAssemdetail.setQty(new BigDecimal(row.get("qty").toString()));
//            sysAssemdetail.setSourceType(row.getStr("sourcebilltype"));
            sysAssemdetail.setSourceBillNo(row.getStr("sourcebillno"));
//            sysAssemdetail.setSourceBillDid(row.getStr("sourcebilldid"));
//            sysAssemdetail.setSourceBillID(row.getStr("sourcebilldid"));
            sysAssemdetail.setAssemType(row.getStr("assemtype"));
            sysAssemdetail.setWhCode(row.getStr("whcode"));
            sysAssemdetail.setPosCode(row.getStr("poscode"));
            sysAssemdetail.setRowNo(Integer.valueOf(row.getStr("rowno") == null ? "0" : row.getStr("rowno")));
            sysAssemdetail.setTrackType(row.getStr("tracktype"));
            sysAssemdetail.setMemo(row.getStr("memo"));
            sysAssemdetail.setCombination(row.getInt("combination"));
            sysAssemdetail.setVenCode(row.getStr("vencode"));
            sysAssemdetail.setIcreateby(user.getId());
            sysAssemdetail.setCcreatename(user.getName());
            sysAssemdetail.setDcreatetime(now);
            sysAssemdetail.setIupdateby(user.getId());
            sysAssemdetail.setCupdatename(user.getName());
            sysAssemdetail.setDupdatetime(now);
            sysAssemdetail.setIsDeleted(false);
            sysAssemdetail.save();

            SysAssembarcode sysAssembarcode = new SysAssembarcode();
            sysAssembarcode.setBarcode(row.getStr("barcode"));
            sysAssembarcode.setQty(new BigDecimal(row.getStr("qty")));
            sysAssembarcode.setInvCode(row.getStr("cinvcode"));
            sysAssembarcode.setMasID(sysAssemdetail.getAutoID());
            sysAssembarcode.setIcreateby(user.getId());
            sysAssembarcode.setCcreatename(user.getName());
            sysAssembarcode.setDcreatetime(now);
            sysAssembarcode.setIupdateby(user.getId());
            sysAssembarcode.setCupdatename(user.getName());
            sysAssembarcode.setDupdatetime(now);
            sysAssembarcode.setIsDeleted(false);


            sysassembarcodeList.add(sysAssembarcode);
        }
        sysassembarcodeservice.batchSave(sysassembarcodeList);

    }

    //可编辑表格提交-修改数据
    private void updateTableSubmitDatas(JBoltTable jBoltTable, SysAssem sysotherin) {
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        ArrayList<SysAssembarcode> sysassembarcodeList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            SysAssemdetail sysAssemdetail = new SysAssemdetail();
            sysAssemdetail.setMasID(sysotherin.getAutoID());
            sysAssemdetail.setAutoID(row.get("autoid").toString());
            // todo 唐工 说形态转换的没有来源
//            sysAssemdetail.setSourceBillNoRow(row.getStr("sourcebillnorow"));
//            sysAssemdetail.setSourceType(row.getStr("sourcebilltype"));
            sysAssemdetail.setSourceBillNo(row.getStr("sourcebillno"));
//            sysAssemdetail.setSourceBillDid(row.getStr("sourcebilldid"));
//            sysAssemdetail.setSourceBillID(row.getStr("sourcebilldid"));
            sysAssemdetail.setAssemType(row.getStr("assemtype"));
            sysAssemdetail.setWhCode(row.getStr("whcode"));
            sysAssemdetail.setPosCode(row.getStr("poscode"));
            sysAssemdetail.setRowNo(Integer.valueOf(row.getStr("rowno") == null ? "0" : row.getStr("rowno")));
            sysAssemdetail.setTrackType(row.getStr("tracktype"));
            sysAssemdetail.setMemo(row.getStr("memo"));
            sysAssemdetail.setVenCode(row.getStr("vencode"));
            sysAssemdetail.setCombination(row.getInt("combination"));
            sysAssemdetail.setIcreateby(user.getId());
            sysAssemdetail.setCcreatename(user.getName());
            sysAssemdetail.setDcreatetime(now);
            sysAssemdetail.setIupdateby(user.getId());
            sysAssemdetail.setCupdatename(user.getName());
            sysAssemdetail.setDupdatetime(now);
            sysAssemdetail.setIsDeleted(false);
            sysassemdetailservice.update(sysAssemdetail);

            SysAssembarcode sysAssembarcode = new SysAssembarcode();
            sysAssembarcode.setAutoID(row.get("barautoid").toString());
            sysAssembarcode.setBarcode(row.getStr("barcode"));
            sysAssembarcode.setQty(new BigDecimal(row.getStr("qty")));
            sysAssembarcode.setInvCode(row.getStr("cinvcode"));
            sysAssembarcode.setMasID(sysAssemdetail.getAutoID());
            sysAssembarcode.setIcreateby(user.getId());
            sysAssembarcode.setCcreatename(user.getName());
            sysAssembarcode.setDcreatetime(now);
            sysAssembarcode.setIupdateby(user.getId());
            sysAssembarcode.setCupdatename(user.getName());
            sysAssembarcode.setDupdatetime(now);
            sysAssembarcode.setIsDeleted(false);

            sysassembarcodeList.add(sysAssembarcode);

        }
        sysassembarcodeservice.batchUpdate(sysassembarcodeList);

    }

    //可编辑表格提交-删除数据
    private void deleteTableSubmitDatas(JBoltTable jBoltTable) {
        Object[] ids = jBoltTable.getDelete();
        if (ArrayUtil.isEmpty(ids)) {
            return;
        }
        sysassemdetailservice.deleteByIds(ids);
//        sysassemdetailservice.delect
        String s1 = ids.toString();
        String[] split = s1.split(",");
        for (String s : split) {
            delete("DELETE T_Sys_AssemBarcode   where  MasID = ?", s);
        }
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
    public String pushU8(SysAssem sysassem, List<SysAssemdetail> sysassemdetail) {
        if (CollUtil.isEmpty(sysassemdetail)) {
            return "推u8从表数据不能为空";
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
            if("转换前".equals(s.getAssemType())){
                SysAssembarcode first = sysassembarcodeservice.findFirst("select * from T_Sys_AssemBarcode where MasID =  ? and Barcode is not null", s.getAutoID());
                this.setjson(s,user,first,sysassem,maindata);
            }else {//转换后
                List<SysAssembarcode> sysAssembarcodes = sysassembarcodeservice.find("select * from T_Sys_AssemBarcode where MasID = ?  and Barcode is not null", s.getAutoID());
               for(SysAssembarcode first : sysAssembarcodes){
                   this.setjson(s,user,first,sysassem,maindata);
               }
            }
        });
        data.set("MainData", maindata);
        System.out.println(data);
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
                    SysAssem byId = findById(sysassem.getAutoID());
                    byId.setU8BillNo(this.extractU8Billno(jsonObject.get("message").getAsString()));
                    byId.update();
                    return null;
                }else {
                    return "上传u8接口报错:"+jsonObject.get("message").getAsString();
                }
            }else {
                return "上传u8接口返回数据为null";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "上传u8失败:"+e.getMessage();
        }

    }


    public void setjson(SysAssemdetail s, User user,SysAssembarcode first,SysAssem sysassem,ArrayList<Object> maindata){
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("IWhCode", s.getWhCode());
        jsonObject.set("iwhname", "");
        jsonObject.set("invcode", first.getInvCode());
        jsonObject.set("userCode", user.getUsername());
        jsonObject.set("organizeCode", this.getdeptid());
        jsonObject.set("OWhCode", s.getPosCode());
        jsonObject.set("owhname", "");
        jsonObject.set("barcode", first.getBarcode());
        jsonObject.set("invstd", "");
        jsonObject.set("invname", "");
        jsonObject.set("CreatePerson", s.getIcreateby());
        jsonObject.set("BillType", s.getAssemType());
        jsonObject.set("qty", first.getQty());
        jsonObject.set("CreatePersonName", user.getName());
        jsonObject.set("IRdName", "");
        jsonObject.set("IRdType", sysassem.getIRdCode());
        jsonObject.set("ORdName", "");
        jsonObject.set("ORdType", sysassem.getORdCode());
        jsonObject.set("Tag", "AssemVouch");
        jsonObject.set("IDeptCode",departmentservice.getRefDepId(sysassem.getDeptCode()));
        jsonObject.set("ODeptCode",departmentservice.getRefDepId(sysassem.getDeptCode()));
        jsonObject.set("VouchTemplate", "");
        jsonObject.set("RowNo", s.getRowNo());
        maindata.add(jsonObject);
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
        Record userRecord = findU8UserByUserCode(user.getUsername());
        Record u8Record = findU8RdRecord01Id(u8billno);
        SysPuinstoreDeleteDTO deleteDTO = new SysPuinstoreDeleteDTO();
        SysPuinstoreDeleteDTO.data data = new SysPuinstoreDeleteDTO.data();
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
        return dbTemplate(u8SourceConfigName(), "sysassem.findU8RdRecord01Id", Kv.by("cCode", cCode)).findFirst();
    }


    //通过当前登录人名称获取部门id
    public String getdeptid() {
        String dept = "001";
        User user = JBoltUserKit.getUser();
        Person person = personservice.findFirstByUserId(user.getId());
        if(null != person && "".equals(person)){
            dept = person.getCOrgCode();
        }
        return dept;
    }

    public void commonSaveSysAssemModel(SysAssem sysAssem, SysPuinstore puinstore) {
        Date date = new Date();
        sysAssem.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
        sysAssem.setBillType(puinstore.getBillType());
        sysAssem.setOrganizeCode(getOrgCode());
        sysAssem.setBillNo(puinstore.getBillNo());
        sysAssem.setBillDate(puinstore.getBillDate());
        sysAssem.setDeptCode(puinstore.getDeptCode());
        sysAssem.setIRdCode(puinstore.getRdCode());//入库类型
        //sysAssem.setORdCode();//出库类型
        sysAssem.setMemo(puinstore.getMemo());
        sysAssem.setCcreatename(JBoltUserKit.getUserName());
        sysAssem.setCupdatename(JBoltUserKit.getUserName());
        sysAssem.setDcreatetime(date);
        sysAssem.setDupdatetime(date);
        sysAssem.setIupdateby(JBoltUserKit.getUserId());
        sysAssem.setIcreateby(JBoltUserKit.getUserId());
        sysAssem.setIAuditWay(1); //审核
        sysAssem.setIAuditStatus(1);//待审核
        sysAssem.setDSubmitTime(puinstore.getDSubmitTime());
        sysAssem.setIAuditStatus(0);
        sysAssem.setIsDeleted(false);
    }

    /**
     * 根据条件获取资源
     * @param kv
     * @return
     */
    public List<Record> getResource(Kv kv){
        kv.setIfNotNull("orgCode", getOrgCode());
        List<Record> list = dbTemplate("sysassem.getResource", kv).find();
        ValidationUtils.notNull(list, "找不到该现品票信息");
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
        ValidationUtils.notNull(list, "找不到该现品票信息");
        return list;
    }

    /**
     * 获取双单位条码数据
     * @return
     */
    public Record getBarcodeDatas(Kv kv) {
        String ibeforeinventoryid = kv.getStr("ibeforeinventoryid");
        Record firstRecord = findFirstRecord("select t2.cInvCode as beforeCode, t3.cInvCode as afterCode,t3.cInvCode as invcode,t3.cinvname,t3.cInvCode ,t3.cInvCode1,t3.cInvName1,t3.cInvStd as cinvstd,\n" +
                "t3.iAutoId,uom.cUomCode,uom.cUomName ,uom.cUomName as purchasecuomname,wh.cWhCode as whcode\n" +
                "     ,wh.cWhName as whname\n" +
                "     ,area.cAreaCode as poscode\n" +
                "     ,area.cAreaName as posname\n" +
                "         from Bd_InventoryChange t1\n" +
                "         left join Bd_Inventory t2 on t1.iBeforeInventoryId = t2.iAutoId\n" +
                "         left join Bd_Inventory t3 on t1.iAfterInventoryId = t3.iAutoId\n" +
                "         LEFT JOIN Bd_Uom uom on t3.iInventoryUomId1 = uom.iAutoId\n" +
                "         left join Bd_InventoryChange change on change.iBeforeInventoryId=t3.iAutoId\n" +
                "         left join Bd_InventoryStockConfig config on config.iInventoryId = t3.iAutoId\n" +
                "         left join Bd_Warehouse_Area area on area.iAutoId = config.iWarehouseAreaId\n" +
                "         left join Bd_Warehouse wh on wh.iAutoId = config.iWarehouseId\n" +
                "where t1.iBeforeInventoryId = '" + ibeforeinventoryid + "'");
        if (firstRecord==null){
            ValidationUtils.error( "未查找到该物料的双单位，请先维护物料的形态对照表");
        }
        firstRecord.set("sourcebillno",kv.get("sourcebillno"));
        firstRecord.set("vencode",kv.get("vencode"));
        firstRecord.set("venname",kv.get("venname"));
        firstRecord.set("ibeforeinventoryid",kv.get("ibeforeinventoryid"));
        firstRecord.set("iafterinventoryid",kv.get("iafterinventoryid"));
        firstRecord.set("qty",kv.get("qty"));
        return firstRecord;
    }

    /**
     * todo  审核通过
     */
    @Override
    public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
        //添加现品票 ,todo 推送u8
        return this.passagetwo(formAutoId) ;

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
     * todo 实现反审之后的其他业务操作, 如有异常返回错误信息
     */
    @Override
    public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        if(isLast){
            //删除现品票 todo 删除u8 单据
            return this.delectbelowtwo(formAutoId);
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

    /**
     *  todo 从已审核，撤回到已保存，前置业务实现，如有异常返回错误信息
     */
    @Override
    public String preWithdrawFromAuditted(long formAutoId) {
        //查看u8数据是否可删

        return null;
    }

    /**
     * todo 从已审核，撤回到已保存，业务实现，如有异常返回错误信息
     */
    @Override
    public String postWithdrawFromAuditted(long formAutoId) {
        //删除现品票 todo 删除u8 单据
        return this.delectbelowtwo(formAutoId);
    }

    /**
     * todo 批量审核（审批）通过，后置业务实现
     */
    @Override
    public String postBatchApprove(List<Long> formAutoIds) {
        return this.passagetwo(formAutoIds);
    }

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

    //审核通过后的业务逻辑
    public String passagetwo(Long formAutoId) {
        SysAssem byId = findById(formAutoId);
        //获取转换后的所有数据
        List<SysAssemdetail> firstBy = sysassemdetailservice.findFirst(formAutoId.toString());
        if(CollUtil.isNotEmpty(firstBy)){
            for(SysAssemdetail detail : firstBy){
                //生成现品票
                this.cashNotTransaction(formAutoId,detail);
            }
        }
        //查转换前，转换后的数据
        List<SysAssemdetail> firstByall = sysassemdetailservice.findFirstByall(formAutoId);

        String s = this.pushU8(byId, firstByall);

        return s;
    }

    //批量审核通过后的业务逻辑 todo 推u8 是否有批量推
    public String passagetwo(List<Long> formAutoId) {
        String s1 = null;
        for (Long s : formAutoId) {
            SysAssem byId = findById(s);

            //获取转换后的所有数据
            List<SysAssemdetail> firstBy = sysassemdetailservice.findFirst(formAutoId.toString());
            if(CollUtil.isNotEmpty(firstBy)){
                for(SysAssemdetail detail : firstBy){
                    //生成现品票
                    this.cashNotTransaction(s,detail);
                }
            }
            //查转换前，转换后的数据
            List<SysAssemdetail> firstByall = sysassemdetailservice.findFirstByall(s);
            s1 = this.pushU8(byId, firstByall);
        }
        return s1;
    }

    //生成现品票
    public void cashNotTransaction(Long s,SysAssemdetail detail) {
        SysAssembarcode first7 = sysassembarcodeservice.findFirst("select * from T_Sys_AssemBarcode where MasID = ? and isDeleted = '0'", detail.getAutoID());
        //获取转换后的数量
        BigDecimal sourceQty = first7.getQty();
        //根据存货编码获取包装数量
        Inventory first8 = inventoryService.findFirst("select * from Bd_Inventory where cInvCode = ?", first7.getInvCode());
        BigDecimal pkgQty = new BigDecimal(first8.getIPkgQty());

        //保存当前包装数量
        detail.setIPkgQty(pkgQty.intValue());
        sysassemdetailservice.update(detail);
        // 包装数量为空或者为0，生成一张条码，原始数量/打包数量

        if (ObjUtil.isNull(pkgQty) || BigDecimal.ZERO.compareTo(pkgQty) == 0 || sourceQty.compareTo(pkgQty) <= 0) {
            this.insysAssembarcode(first7,sourceQty);
        }else {
            // 源数量/包装数量 （向上取）
            int count = sourceQty.divide(pkgQty, 0, BigDecimal.ROUND_UP).intValue();
            for (int i = 0; i < count; i++) {
                BigDecimal qty = pkgQty;
                if (i == count - 1) {
                    qty = sourceQty.subtract(BigDecimal.valueOf(i).multiply(pkgQty));
                }
                this.insysAssembarcode(first7,qty);
            }
        }

    }

    //新增条码
    public void insysAssembarcode(SysAssembarcode first7,BigDecimal qty){
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        String barCode = purchaseOrderDBatchService.generateBarCode().concat("-00");
        SysAssembarcode sysAssembarcode = new SysAssembarcode();
        sysAssembarcode.setIcreateby(user.getId());
        sysAssembarcode.setCcreatename(user.getName());
        sysAssembarcode.setDcreatetime(now);
        sysAssembarcode.setIupdateby(user.getId());
        sysAssembarcode.setCupdatename(user.getName());
        sysAssembarcode.setDupdatetime(now);
        sysAssembarcode.setIsDeleted(false);
        sysAssembarcode.setBarcode(barCode);
        sysAssembarcode.setInvCode(first7.getInvCode());
        sysAssembarcode.setMasID(first7.getMasID());
        sysAssembarcode.setQty(qty);
        sysAssembarcode.save();
    }



    //反审后的操作
    public String delectbelowtwo(long formAutoId){
        //主表数据
        SysAssem byId = findById(formAutoId);
        //从表数据
        List<SysAssemdetail> firstBy = sysassemdetailservice.findFirstByall(formAutoId);
        if(CollUtil.isNotEmpty(firstBy)){
            for (SysAssemdetail s : firstBy ){
                if(!"转换前".equals(s.getAssemType())) {
                    delete("DELETE T_Sys_AssemBarcode where MasID = ? and Barcode is not null and isDeleted = '0'",s.getAutoID());
                }
            }
        }
        //todo 删除u8的数据
        String s = this.deleteVouchProcessDynamicSubmitUrl(this.getDeleteDTO(byId.getU8BillNo()));
        byId.setU8BillNo(null);
        byId.update();
        return s;
    }
    // 批量反审后的操作
    public String delectbelowtwo(List<Long> formAutoId){
        String s1 = null;
        for (Long d : formAutoId) {
            //主表数据
            SysAssem byId = findById(d);
            //从表数据
            List<SysAssemdetail> firstBy = sysassemdetailservice.findFirstByall(d);
            if (CollUtil.isNotEmpty(firstBy)) {
                for (SysAssemdetail s : firstBy) {
                    if (!"转换前".equals(s.getAssemType())) {
                        delete("DELETE T_Sys_AssemBarcode where MasID = ? and Barcode is not null and isDeleted = '0'", s.getAutoID());
                    }
                }
            }
            //todo 删除u8的数据
            s1 = this.deleteVouchProcessDynamicSubmitUrl(this.getDeleteDTO(byId.getU8BillNo()));
            byId.setU8BillNo(null);
            byId.update();
        }
        return s1;
    }


    /*
     * 通知U8删其他入库单
     * */
    public String deleteVouchProcessDynamicSubmitUrl(String json) {
        String vouchSumbmitUrl = AppConfig.getStockApiUrl() + "/ShapeChangVouchUnConfirmV1";
        Map<String, Object> para = new HashMap<>();
        para.put("dataJson", json);
        String inUnVouchGet = HttpUtil.post(vouchSumbmitUrl, para);
        String res = XmlUtil.readObjectFromXml(inUnVouchGet);
        LOG.info("res: {}", res);
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
        String code = jsonObject.getString("code");
        String message = StrUtil.nullToDefault(jsonObject.getString("message"), jsonObject.getString("msg"));
        if(!"200".equals(code))return message;

        String deleteUrl = AppConfig.getStockApiUrl() + "/ShapeChangVouchDeleteV1";
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