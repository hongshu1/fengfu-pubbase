package cn.rjtech.admin.syssaledeliver;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.util.JBoltCamelCaseUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.model.momdata.Person;
import cn.rjtech.model.momdata.SysSaledeliver;
import cn.rjtech.model.momdata.SysSaledeliverdetail;
import cn.rjtech.wms.utils.HttpApiUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.*;

/**
 * 销售出库
 * @ClassName: SysSaledeliverService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 13:05
 */
public class SysSaledeliverService extends BaseService<SysSaledeliver> {
    private final SysSaledeliver dao = new SysSaledeliver().dao();

    @Override
    protected SysSaledeliver dao() {
        return dao;
    }

    @Inject
    private PersonService personservice;

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 查询
     * @param kv
     * @return
     */
    public Page<Record> getAdminDatas(Kv kv) {
        Page<Record> recordPage = dbTemplate("sysSaleDeliver.pageList", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
        JBoltCamelCaseUtil.keyToCamelCase(recordPage.getList());
        return recordPage;
    }


    /**
     * 查询form页面数据
     */
    public Record getFormDatas(String AutoID) {
        Kv kv = new Kv();
        kv.setIfNotNull("AutoID",AutoID);
        kv.setIfNotNull("OrganizeCode",getOrgCode());
        Record recordPage = dbTemplate("sysSaleDeliver.getFormDatas", kv).findFirst();
        return recordPage;
    }


    /**
     * 导出订单列表
     */
    public List<Record> download(Kv kv, String sqlTemplate) {
        kv.setIfNotNull("OrganizeCode",getOrgCode());
        List<Record> list = dbTemplate(sqlTemplate, kv).find();
        return list;
    }

    /**
     * 返回List<Record>列表
     * @param kv
     * @return
     */
    public List<Record> getAdminDataOfRecord(Kv kv) {
        List<Record> lists = dbTemplate("sysSaleDeliver.exportList", kv).find();
        JBoltCamelCaseUtil.keyToCamelCase(lists);
        return lists;
    }

    public List<SysSaledeliver> getpushu(Kv kv) {
        List<SysSaledeliver> lists = daoTemplate("sysSaleDeliver.pushu", kv).find();
        return lists;
    }



    /**
     * 获取行数据
     * @param pageNumber
     * @param pageSize
     * @param kv
     * @return
     */
    public Page<Record> getLineData(int pageNumber, int pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("sysSaleDeliver.getLineData",kv).paginate(pageNumber, pageSize);
        JBoltCamelCaseUtil.keyToCamelCase(paginate.getList());
        return	paginate;
    }

    /**
     * 生成要导出的Excel
     * @return
     */
    public JBoltExcel exportExcel(List<Record> records) {
        return JBoltExcel
                // 创建
                .create()
                // 设置工作表
                .setSheets(
                        // 设置工作表 列映射 顺序 标题名称
                        JBoltExcelSheet
                                .create()
                                // 表头映射关系
                                .setHeaders(1,
                                        JBoltExcelHeader.create("billNo", "出库单号", 15),
                                        JBoltExcelHeader.create("erpBillNo", "ERP单据号", 15),
                                        JBoltExcelHeader.create("billdate", "出库日期", 15),
                                        JBoltExcelHeader.create("whName", "仓库名称", 15),
                                        JBoltExcelHeader.create("rdName", "出库类别", 15),
                                        JBoltExcelHeader.create("billType", "业务类型", 15),
                                        JBoltExcelHeader.create("deptName", "销售部门", 15),
                                        JBoltExcelHeader.create("saleName", "业务员", 15),
                                        JBoltExcelHeader.create("cCusName", "客户简称", 15),
                                        JBoltExcelHeader.create("auditDate", "审核日期", 15),
                                        JBoltExcelHeader.create("memo", "备注", 15),
                                        JBoltExcelHeader.create("createPerson", "创建人", 15),
                                        JBoltExcelHeader.create("createDate", "创建时间", 15)
                                ).setDataChangeHandler((data,index) ->{
                                    Date createdTime = data.getDate("createDate");
                                    String fmtCreatedTime= DateUtil.format(createdTime, "yyyy-MM-dd");
                                    data.put("createdTime",fmtCreatedTime);
                                })
                                // 设置导出的数据源 来自于数据库查询出来的Model List
                                .setRecordDatas(2, records)
                );
    }



    /**
     * 保存
     * @param sysSaledeliver
     * @return
     */
    public Ret save(SysSaledeliver sysSaledeliver) {
        if (sysSaledeliver == null || isOk(sysSaledeliver.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // if(existsName(sysSaledeliver.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysSaledeliver.save();
        if (success) {
            // 添加日志
            // addSaveSystemLog(sysSaledeliver.getAutoID(), JBoltUserKit.getUserId(), sysSaledeliver.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     *
     * @param sysSaledeliver
     * @return
     */
    public Ret update(SysSaledeliver sysSaledeliver) {
        if (sysSaledeliver == null || notOk(sysSaledeliver.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // 更新时需要判断数据存在
        SysSaledeliver dbSysSaledeliver = findById(sysSaledeliver.getAutoID());
        if (dbSysSaledeliver == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        // if(existsName(sysSaledeliver.getName(), sysSaledeliver.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysSaledeliver.update();
        if (success) {
            // 添加日志
            // addUpdateSystemLog(sysSaledeliver.getAutoID(), JBoltUserKit.getUserId(), sysSaledeliver.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysSaledeliver 要删除的model
     * @param kv             携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(SysSaledeliver sysSaledeliver, Kv kv) {
        // addDeleteSystemLog(sysSaledeliver.getAutoID(), JBoltUserKit.getUserId(),sysSaledeliver.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysSaledeliver model
     * @param kv             携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkInUse(SysSaledeliver sysSaledeliver, Kv kv) {
        // 这里用来覆盖 检测是否被其它表引用
        return null;
    }


    //推送u8数据接口
    public Ret pushU8(SysSaledeliver syssaledeliver, List<SysSaledeliverdetail> syssaledeliverdetail) {
        if(CollUtil.isEmpty(syssaledeliverdetail)){
            return fail("数据不能为空");
        }

        User user = JBoltUserKit.getUser();
        JSONObject data = new JSONObject();

        data.set("userCode",user.getUsername());
        data.set("organizeCode",this.getdeptid());
        data.set("token","");

        JSONObject preallocate = new JSONObject();


        preallocate.set("userCode",user.getUsername());
        preallocate.set("organizeCode",this.getdeptid());
        preallocate.set("CreatePerson",user.getId());
        preallocate.set("CreatePersonName",user.getName());
        preallocate.set("loginDate", DateUtil.format(new Date(), "yyyy-MM-dd"));
        preallocate.set("tag","ProductionIn");
        preallocate.set("type","ProductionIn");

        data.set("PreAllocate",preallocate);

        ArrayList<Object> maindata = new ArrayList<>();
        syssaledeliverdetail.stream().forEach(s -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("owhcode",s.getWhCode());
            jsonObject.set("odeptcode",this.getdeptid());
            jsonObject.set("oposcode","");
            jsonObject.set("invcode",s.getInvCode());
            jsonObject.set("userCode",user.getUsername());
            jsonObject.set("organizeCode",this.getdeptid());
            jsonObject.set("Qty",s.getQty());
            jsonObject.set("cuscode","");
            jsonObject.set("barcode",s.getBarcode());
            jsonObject.set("invstd","");
            jsonObject.set("invname","");
            jsonObject.set("index","1");
            jsonObject.set("billDate",syssaledeliver.getBillDate());
            jsonObject.set("billid","");
            jsonObject.set("billdid","");
            jsonObject.set("billno",syssaledeliver.getBillNo());
            jsonObject.set("billrowno","");
            jsonObject.set("billnorow","");
            jsonObject.set("sourcebilldid",s.getSourceBillDid());
            jsonObject.set("sourcebillnorow",s.getSourceBIllNoRow());
            jsonObject.set("sourcebillno",s.getSourceBillNo());
            jsonObject.set("sourcebillrowno","");
            jsonObject.set("Tag","SaleDispatch");
            jsonObject.set("cusname","");
            jsonObject.set("ORdType",s.getTrackType());

            maindata.add(jsonObject);
        });
        data.set("MainData",maindata);

        //            请求头
        Map<String, String> header = new HashMap<>(5);
        header.put("Content-Type", "application/json");
        String url = "http://localhost:8081/web/erp/common/vouchProcessDynamicSubmit";

        try {
            String post = HttpApiUtils.httpHutoolPost(url, data, header);
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(post);
            if (isOk(post)) {
                if ("200".equals(jsonObject.getString("code"))) {
                    return Ret.ok("提交成功");
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


}