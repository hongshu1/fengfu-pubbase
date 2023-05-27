package cn.rjtech.admin.sysotherin;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONObject;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.common.CommonController;
import cn.rjtech.common.columsmap.ColumsmapService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.Person;
import cn.rjtech.model.momdata.SysOtherin;
import cn.rjtech.model.momdata.SysOtherindetail;
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
 * 其它入库单
 * @ClassName: SysOtherinService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-05 17:57
 */
public class SysOtherinService extends BaseService<SysOtherin> {
    private final SysOtherin dao = new SysOtherin().dao();

    @Override
    protected SysOtherin dao() {
        return dao;
    }

    @Inject
    private SysOtherindetailService sysotherindetailservice;

    @Inject
    private PersonService personservice;

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     * @return
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        // 查业务类型
        Page<Record> paginate = dbTemplate("sysotherin.recpor", kv).paginate(pageNumber, pageSize);
        return paginate;
    }

    /**
     * 保存
     *
     * @param sysOtherin
     * @return
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
     * @param sysOtherin
     * @return
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
     * @param sysOtherin 要删除的model
     * @param kv         携带额外参数一般用不上
     * @return
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
     * @return
     */
    @Override
    public String checkInUse(SysOtherin sysOtherin, Kv kv) {
        // 这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public Ret delete(Long id) {
        tx(() -> {
            deleteById(id);
            delete("DELETE T_Sys_OtherInDetail   where  MasID = ?", id);
            return true;
        });
        return ret(true);
    }

    /**
     * 批量删除主从表
     */
    public Ret deleteRmRdByIds(String ids) {
        tx(() -> {
            deleteByIds(ids);
            String[] split = ids.split(",");
            for (String s : split) {
                delete("DELETE T_Sys_OtherInDetail   where  MasID = ?", s);
            }
            return true;
        });
        return ret(true);
    }


    /**
     * 执行JBoltTable表格整体提交
     *
     * @param jBoltTable
     * @return
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        if (jBoltTable.getSaveRecordList() == null && jBoltTable.getDelete() == null && jBoltTable.getUpdateRecordList() == null) {
            return Ret.msg("行数据不能为空");
        }
        SysOtherin sysotherin = jBoltTable.getFormModel(SysOtherin.class, "sysotherin");
        // 获取当前用户信息？
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        tx(() -> {
            // 通过 id 判断是新增还是修改
            if (sysotherin.getAutoID() == null) {
                sysotherin.setOrganizeCode(getOrgCode());
                sysotherin.setCreatePerson(user.getName());
                sysotherin.setCreateDate(now);
                sysotherin.setModifyPerson(user.getName());
                sysotherin.setAuditPerson(user.getName());
                sysotherin.setState("1");
                sysotherin.setModifyDate(now);
                // 主表新增
                ValidationUtils.isTrue(sysotherin.save(), ErrorMsg.SAVE_FAILED);
            } else {
                sysotherin.setModifyPerson(user.getName());
                sysotherin.setModifyDate(now);
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
        return SUCCESS;
    }

    // 可编辑表格提交-新增数据
    private void saveTableSubmitDatas(JBoltTable jBoltTable, SysOtherin sysotherin) {
        List<Record> list = jBoltTable.getSaveRecordList();
        if (CollUtil.isEmpty(list)) return;
        ArrayList<SysOtherindetail> systailsList = new ArrayList<>();
        Date now = new Date();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            SysOtherindetail sysOtherindetail = new SysOtherindetail();

            sysOtherindetail.setMasID(Long.valueOf(sysotherin.getAutoID()));
            sysOtherindetail.setModifyPerson(JBoltUserKit.getUser().getName());
            sysOtherindetail.setModifyDate(now);
            sysOtherindetail.setModifyDate(now);
            sysOtherindetail.setCreateDate(now);
            sysOtherindetail.setCreatePerson(JBoltUserKit.getUser().getName());
            sysOtherindetail.setBarcode(row.get("barcode"));
            sysOtherindetail.setInvCode(row.get("cinvcode"));
            sysOtherindetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysOtherindetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysOtherindetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysOtherindetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysOtherindetail.setSourceBillID(row.getStr("sourcebilldid"));

            systailsList.add(sysOtherindetail);

        }
        sysotherindetailservice.batchSave(systailsList);
    }

    // 可编辑表格提交-修改数据
    private void updateTableSubmitDatas(JBoltTable jBoltTable, SysOtherin sysotherin) {
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) return;
        ArrayList<SysOtherindetail> systailsList = new ArrayList<>();
        Date now = new Date();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            SysOtherindetail sysOtherindetail = new SysOtherindetail();
            sysOtherindetail.setMasID(Long.valueOf(sysotherin.getAutoID()));
            sysOtherindetail.setModifyPerson(JBoltUserKit.getUser().getName());
            sysOtherindetail.setModifyDate(now);
            sysOtherindetail.setCreatePerson(JBoltUserKit.getUser().getName());
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

        // 测试调用接口
        System.out.println("```````````````````````````````"+ new Date());
        Ret ret = pushU8(sysotherin, systailsList);
        System.out.println("```````````````````````````````"+ret);
    }

    // 可编辑表格提交-删除数据
    private void deleteTableSubmitDatas(JBoltTable jBoltTable) {
        Object[] ids = jBoltTable.getDelete();
        if (ArrayUtil.isEmpty(ids)) return;
        sysotherindetailservice.deleteByIds(ids);

    }


    //推送u8数据接口
    public Ret pushU8(SysOtherin sysotherin,List<SysOtherindetail> sysotherindetail) {
        if(!CollectionUtils.isNotEmpty(sysotherindetail)){
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
        preallocate.set("loginDate",DateUtil.format(new Date(), "yyyy-MM-dd"));
        preallocate.set("tag","OtherIn");
        preallocate.set("type","OtherIn");

        data.put("PreAllocate",preallocate);

        JSONArray maindata = new JSONArray();
        sysotherindetail.stream().forEach(s -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("invstd","");
            jsonObject.set("iwhcode",s.getPosCode());
            jsonObject.set("barcodeqty","");
            jsonObject.set("defwhcode",sysotherin.getWhcode());
            jsonObject.set("organizecode",this.getdeptid());
            jsonObject.set("invname","");
            jsonObject.set("index","1");
            jsonObject.set("vt_id","");
            jsonObject.set("defwhname","");
            jsonObject.set("billDate",DateUtil.format(s.getCreateDate(), "yyyy-MM-dd"));
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