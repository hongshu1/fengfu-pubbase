package cn.rjtech.admin.sysmaterialsprepare;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.BillNoUtils;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 材料备料表
 *
 * @ClassName: SysMaterialsprepareService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-12 18:28
 */
public class SysMaterialsprepareService extends BaseService<SysMaterialsprepare> {
    @Inject
    private SysMaterialspreparedetailService1 serviceD;

    @Inject
    private InventoryService invent;

    @Inject
    private MoDocService moDocS;

    @Inject
    private PersonService personservice;

    private final SysMaterialsprepare dao = new SysMaterialsprepare().dao();

    @Override
    protected SysMaterialsprepare dao() {
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
     * @param ;MO        生产订单  SoDeliver销售发货单
     * @return
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("materialsprepare.recpor", kv).paginate(pageNumber, pageSize);
        return paginate;
    }

    /**
     * 保存
     *
     * @param sysMaterialsprepare
     * @return
     */
    public Ret save(SysMaterialsprepare sysMaterialsprepare) {
        if (sysMaterialsprepare == null || isOk(sysMaterialsprepare.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        boolean success = sysMaterialsprepare.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(sysMaterialsprepare.getAutoID(), JBoltUserKit.getUserId(), sysMaterialsprepare.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     *
     * @param sysMaterialsprepare
     * @return
     */
    public Ret update(SysMaterialsprepare sysMaterialsprepare) {
        if (sysMaterialsprepare == null || notOk(sysMaterialsprepare.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        SysMaterialsprepare dbSysMaterialsprepare = findById(sysMaterialsprepare.getAutoID());
        if (dbSysMaterialsprepare == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(sysMaterialsprepare.getName(), sysMaterialsprepare.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysMaterialsprepare.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(sysMaterialsprepare.getAutoID(), JBoltUserKit.getUserId(), sysMaterialsprepare.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysMaterialsprepare 要删除的model
     * @param kv                  携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(SysMaterialsprepare sysMaterialsprepare, Kv kv) {
        //addDeleteSystemLog(sysMaterialsprepare.getAutoID(), JBoltUserKit.getUserId(),sysMaterialsprepare.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysMaterialsprepare model
     * @param kv                  携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkInUse(SysMaterialsprepare sysMaterialsprepare, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    public List<Record> options() {
        return dbTemplate("materialsprepare.findColumns", Kv.of("isenabled", "1")).find();
    }

    public List<Record> options1() {
        return dbTemplate("materialsprepare.findColumns1", Kv.of("isenabled", "1")).find();
    }

    /**
     * 材料出库单列表 明细
     *
     * @param pageNumber
     * @param pageSize
     * @param kv
     * @return
     */
    public Page<Record> getMaterialsOutLines(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("materialsprepare.getMaterialsOutLines", kv).paginate(pageNumber, pageSize);
    }

    public Page<Record> getMaterialsOutLines1(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("materialsprepare.getMaterialsOutLines1", kv).paginate(pageNumber, pageSize);
    }

    public Page<Record> getMaterialsOutLines2(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("materialsprepare.getMaterialsOutLines2", kv).paginate(pageNumber, pageSize);
    }

    public Page<Record> getAdminDatasForauto(int pageNumber, int pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("materialsprepare.Auto", kv).paginate(pageNumber, pageSize);
        return paginate;
    }

    public Ret submitByJBoltTable(String sourcebillid, MoDoc modoc) {
        SysMaterialsprepare sysMaterialsprepare = new SysMaterialsprepare();
        SysMaterialspreparedetail sysMaterialspreparedetail = new SysMaterialspreparedetail();
        String billNo = BillNoUtils.getcDocNo(getOrgId(), "QTCK", 5);
        Date nowDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(nowDate);
        User user = JBoltUserKit.getUser();
        tx(() -> {
            sysMaterialsprepare.setBillNo(billNo);
            sysMaterialsprepare.setBillDate(date);
            sysMaterialsprepare.setSourceBillID(sourcebillid);
            sysMaterialsprepare.setCreatePerson(user.getName());
            sysMaterialsprepare.setCreateDate(nowDate);
            sysMaterialsprepare.setOrganizeCode(getOrgCode());
            sysMaterialsprepare.setSourceBillNo(modoc.getCMoDocNo());
            // 主表新增
            ValidationUtils.isTrue(sysMaterialsprepare.save(), ErrorMsg.SAVE_FAILED);
            // 从表的操作
            // 获取保存数据（执行保存，通过 getSaveRecordList）
            saveTableSubmitDatas(sysMaterialsprepare, nowDate, modoc);
            changeiStatues(modoc);
            return true;
        });
        return SUCCESS;
    }

    // 可编辑表格提交-新增数据
    private void saveTableSubmitDatas(SysMaterialsprepare sysMaterialsprepare, Date nowDate, MoDoc modoc) {
        SysMaterialspreparedetail sysMaterialspreparedetail = new SysMaterialspreparedetail();
        sysMaterialspreparedetail.setMasID(Long.valueOf(sysMaterialsprepare.getAutoID()));
        sysMaterialspreparedetail.setModifyPerson(JBoltUserKit.getUser().getName());
        sysMaterialspreparedetail.setModifyDate(nowDate);
        sysMaterialspreparedetail.setCreateDate(nowDate);
        sysMaterialspreparedetail.setCreatePerson(JBoltUserKit.getUser().getName());
//        sysMaterialspreparedetail.setBarcode();
        sysMaterialspreparedetail.setInvCode(invent.findFirst("select *   from Bd_Inventory where iAutoId=?", modoc.getIInventoryId()).getCInvCode());
//        sysMaterialspreparedetail.setQty();
//        sysMaterialspreparedetail.setSourceBillType();
//        sysMaterialspreparedetail.setSourceBillNo();
//        sysMaterialspreparedetail.setSourceBillDid();
//        sysMaterialspreparedetail.setSourceBillID();
//        sysMaterialspreparedetail.setSourceBillNoRow();
//        sysMaterialspreparedetail.setMemo();
//        sysMaterialspreparedetail.setPackRate();
//        sysMaterialspreparedetail.setNum();
//        sysMaterialspreparedetail.setPosCode();
        serviceD.save(sysMaterialspreparedetail);
    }

    private void changeiStatues(MoDoc modoc) {
        modoc.setIStatus(3);
        moDocS.update(modoc);
    }

    //推送U8
    public Ret pushU8(SysMaterialsprepare sysMaterialsprepare,List<SysMaterialspreparedetail> sysMaterialspreparedetail) {
        if(!CollectionUtils.isNotEmpty(sysMaterialspreparedetail)){
            return Ret.ok().msg("数据不能为空");
        }

        User user = JBoltUserKit.getUser();
        Map<String, Object> data = new HashMap<>();

        data.put("userCode",user.getUsername());
        data.put("organizeCode",this.getdeptid());
        data.put("token","");

        JSONObject preallocate = new JSONObject();


        preallocate.set("userCode",user.getUsername());
        preallocate.set("organizeCode",this.getdeptid());
        preallocate.set("CreatePerson",user.getId());
        preallocate.set("CreatePersonName",user.getName());
        preallocate.set("loginDate", DateUtil.format(new Date(), "yyyy-MM-dd"));
        preallocate.set("tag","MaterialOutForMO");
        preallocate.set("type","MaterialOutForMO");

        data.put("PreAllocate",preallocate);

        JSONArray maindata = new JSONArray();
        sysMaterialspreparedetail.stream().forEach(s -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("organizeCode",this.getdeptid());
            jsonObject.set("deliverqty","");
            jsonObject.set("qty",s.getQty());
            jsonObject.set("barcode",s.getBarcode());
            jsonObject.set("billrowno",s.getQty());
            jsonObject.set("billid","");
            jsonObject.set("billdid","");
            jsonObject.set("billnorow","");
            jsonObject.set("billno",sysMaterialsprepare.getBillNo());
            jsonObject.set("odeptcode","");
            jsonObject.set("odeptname","");
            jsonObject.set("owhcode","");
            jsonObject.set("oposcode",s.getPosCode());
            jsonObject.set("invcode",s.getInvCode());
            jsonObject.set("invstd","");
            jsonObject.set("invname","");
            jsonObject.set("sourcebillno","");
            jsonObject.set("sourcebillnorow","");
            jsonObject.set("cusname","");
            jsonObject.set("cuscode","");
            jsonObject.set("Tag","MaterialOutForMO");


            maindata.put(jsonObject);
        });
        data.put("MainData",maindata);

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
        return Ret.msg("上传u8失败");
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

}