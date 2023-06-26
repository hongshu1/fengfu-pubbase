package cn.rjtech.admin.sysmaterialsprepare;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventoryroutinginvc.InventoryRoutingInvcService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.admin.stockbarcodeposition.StockBarcodePositionService;
import cn.rjtech.admin.sysmaterialspreparedetail.SysMaterialspreparedetailService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.*;
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
 * 材料备料表
 *
 * @ClassName: SysMaterialsprepareService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-02 00:06
 */
public class SysMaterialsprepareService extends BaseService<SysMaterialsprepare> {

    @Inject
    private SysMaterialspreparedetailService sysmaterialspreparedetailservice;

    @Inject
    private PersonService personservice;

    @Inject
    private MoDocService moDocS;

    @Inject
    private InventoryRoutingInvcService inventoryroutinginvcservice;

    @Inject
    private InventoryService invent;

    @Inject
    private StockBarcodePositionService stockbarcodepositionservice;

    private final SysMaterialsprepare dao = new SysMaterialsprepare().dao();

    @Override
    protected SysMaterialsprepare dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

//	/**
//	 * 后台管理数据查询
//	 * @param pageNumber 第几页
//	 * @param pageSize   每页几条数据
//     * @param BillType 单据类型;MO 生产订单  SoDeliver销售发货单
//	 * @return
//	 */
//	public Page<SysMaterialsprepare> getAdminDatas(int pageNumber, int pageSize, String BillType) {
//	    //创建sql对象
//	    Sql sql = selectSql().page(pageNumber,pageSize);
//	    //sql条件处理
//        sql.eq("BillType",BillType);
//        //排序
//        sql.desc("AutoID");
//		return paginate(sql);
//	}

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

    public Page<Record> getAdminDatasForauto(int pageNumber, int pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("materialsprepare.Auto", kv).paginate(pageNumber, pageSize);
        return paginate;
    }

    public Page<Record> getManualdatas(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("materialsprepare.getManualdatas", kv).paginate(pageNumber, pageSize);
    }

    public Page<Record> getDetail(int pageNumber, int pageSize, Kv kv) {
        //先进先出
        List<Record> objects = new ArrayList<>();
        int j=0;
        String BATCH="";
        BigDecimal QTYTOLL = new BigDecimal("0");
        List<Record> recordList = dbTemplate("materialsprepare.xianjinxianchu", kv).find();
        if (recordList.size()==0){
            return null;
        }
        //集合第一个数据的存货编码
        String cinvcode=recordList.get(0).get("cInvCode");
        for (int i=0;i<recordList.size();i++){
            //判断存货编码是否相等
            if (recordList.get(i).get("cInvCode").equals(cinvcode)){
                //获得集合中数据的物料总计划数量
                BigDecimal planIqty=recordList.get(i).get("planIqty");
                //判断备料数量是否饱和
                if (QTYTOLL.compareTo(planIqty)<1){
                    //备料数量累加
                    QTYTOLL=QTYTOLL.add(recordList.get(i).get("QTY"));
                    //判断前后数据批次号是否相同(同一存货编码不同批次号)
                    //如果批次号不相等，新增行
                    if (BATCH!=recordList.get(i).get("Batch")){
                        Record record = new Record();
                        //存货编码
                        record.set("cInvCode",recordList.get(i).get("cInvCode"));
                        //批次号
                        record.set("Batch",recordList.get(i).get("Batch"));
                        record.set("cInvCode1",recordList.get(i).get("cInvCode1"));
                        record.set("cInvName1",recordList.get(i).get("cInvName1"));
                        record.set("planIqty",recordList.get(i).get("planIqty"));
                        record.set("Billno",recordList.get(i).get("Billno"));
                        record.set("SourceBillID",recordList.get(i).get("SourceBillID"));
                        record.set("cInvStd",recordList.get(i).get("cInvStd"));
                        record.set("cUomName",recordList.get(i).get("cUomName"));
                        record.set("WhCode",recordList.get(i).get("WhCode"));
                        record.set("PosCode",recordList.get(i).get("PosCode"));
                        //获取已备料数量
                        Kv kv1 = new Kv();
                        kv1.set("iAutoId",recordList.get(i).get("SourceBillID"));
                        kv1.set("InvCode",recordList.get(i).get("cInvCode"));
                        kv1.set("Batch",recordList.get(i).get("Batch"));
                        List<Record> recordOfHasBeenPrepared = dbTemplate("materialsprepare.HasBeenPrepared", kv1).find();
                        BigDecimal HasBeenPrepared = new BigDecimal(0);
                        //判断是否已有备料
                        if (recordOfHasBeenPrepared.size()<1){
                            record.set("num",0);

                        }else {
                            for (int t=1;t<recordOfHasBeenPrepared.size();t++){
                                HasBeenPrepared.add(recordOfHasBeenPrepared.get(t).get("Qty"));
                            }
                            record.set("num",HasBeenPrepared);
                        }
                        HasBeenPrepared=new BigDecimal(0);
                        objects.add(record);
                        j++;
                    }
                    BATCH=recordList.get(i).get("Batch");
                }
            }else {
                cinvcode=recordList.get(i).get("cInvCode");
                Record record = new Record();
                //存货编码
                record.set("cInvCode",recordList.get(i).get("cInvCode"));
                //批次号
                record.set("Batch",recordList.get(i).get("Batch"));
                record.set("cInvCode1",recordList.get(i).get("cInvCode1"));
                record.set("cInvName1",recordList.get(i).get("cInvName1"));
                record.set("planIqty",recordList.get(i).get("planIqty"));
                record.set("Billno",recordList.get(i).get("Billno"));
                record.set("SourceBillID",recordList.get(i).get("SourceBillID"));
                record.set("cInvStd",recordList.get(i).get("cInvStd"));
                record.set("cUomName",recordList.get(i).get("cUomName"));
                record.set("WhCode",recordList.get(i).get("WhCode"));
                record.set("PosCode",recordList.get(i).get("PosCode"));
                //获取已备料数量
                Kv kv1 = new Kv();
                kv1.set("iAutoId",recordList.get(i).get("SourceBillID"));
                kv1.set("InvCode",recordList.get(i).get("cInvCode"));
                kv1.set("Batch",recordList.get(i).get("Batch"));
                List<Record> recordOfHasBeenPrepared = dbTemplate("materialsprepare.HasBeenPrepared", kv1).find();
                BigDecimal HasBeenPrepared = new BigDecimal(0);
                //判断是否已有备料
                if (recordOfHasBeenPrepared.size()<1){
                    record.set("num",0);

                }else {
                    for (int t=1;t<recordOfHasBeenPrepared.size();t++){
                        HasBeenPrepared.add(recordOfHasBeenPrepared.get(t).get("Qty"));
                    }
                    record.set("num",HasBeenPrepared);
                }
                HasBeenPrepared=new BigDecimal(0);
                objects.add(record);
                j++;
            }
        }
        //处理页量页码信息
        int totalRow=j;
        int totalPage=totalRow/pageSize;
        if (totalPage*pageSize<totalRow){
            totalPage++;
        }
        //已经实现数据根据计划需求数量先进先出,测试目标:动态展示先进先出,且数量不超过计划数量
        return new Page(objects,pageNumber, pageSize,totalPage,totalRow);
    }

    public Page<Record> getBarcodedatas(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("materialsprepare.getBarcodedatas", kv).paginate(pageNumber, pageSize);
    }

    public Page<Record> getBarcodedatas1(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("materialsprepare.getBarcodedatas1", kv).paginate(pageNumber, pageSize);
    }

    public Ret submitByJBoltTable(Long id) {
        SysMaterialsprepare sysMaterialsprepare = new SysMaterialsprepare();
        //获取当前用户信息？
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        tx(() -> {
            //通过 id 判断是新增还是修改
            MoDoc modoc = moDocS.findFirst("select * from Mo_MoDoc where iAutoId=?", id);
            sysMaterialsprepare.setSourceBillID(id);
            sysMaterialsprepare.setSourceBillNo(modoc.getCMoDocNo());
            sysMaterialsprepare.setBillType("自动作成");
            sysMaterialsprepare.setOrganizeCode(getOrgCode());
            sysMaterialsprepare.setCcreatename(user.getName());
            sysMaterialsprepare.setDcreatetime(now);
            sysMaterialsprepare.setIcreateby(user.getId());
            sysMaterialsprepare.setBillDate(DateUtil.format(now, "yyyy-MM-dd HH:mm:ss"));
            sysMaterialsprepare.setBillNo("BL" + DateUtil.format(new Date(), "yyyyMMdd") + RandomUtil.randomNumbers(6));
            //主表新增
            ValidationUtils.isTrue(sysMaterialsprepare.save(), ErrorMsg.SAVE_FAILED);
            //从表的操作
            // 获取保存数据（执行保存，通过 getSaveRecordList）
//            saveTableSubmitDatas(sysMaterialsprepare, id);
            //修改工单状态
            modoc.setIStatus(3);
            modoc.setIUpdateBy(user.getId());
            modoc.setCUpdateName(user.getName());
            modoc.setDUpdateTime(now);
            ValidationUtils.isTrue(modoc.update(), ErrorMsg.UPDATE_FAILED);
            //获取修改数据（执行修改，通过 getUpdateRecordList）
            //获取删除数据（执行删除，通过 getDelete）
            return true;
        });
        return SUCCESS;
    }

    private void saveTableSubmitDatas(SysMaterialsprepare sysMaterialsprepare, Long id) {
        Kv kv = new Kv();
        kv.set("imodocid", String.valueOf(id));
        List<Record> records = dbTemplate("materialsprepare.test", kv).find();
        if (CollUtil.isEmpty(records)) return;
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        ArrayList<SysMaterialspreparedetail> sysMaterialspreparedetails = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            SysMaterialspreparedetail sysMaterialspreparedetail = new SysMaterialspreparedetail();
            sysMaterialspreparedetail.setMasID(Long.valueOf(sysMaterialsprepare.getAutoID()));
            sysMaterialspreparedetail.setAutoID(String.valueOf(JBoltSnowflakeKit.me.nextId()));
            sysMaterialspreparedetail.setPosCode(records.get(i).getStr("PosCode"));
            sysMaterialspreparedetail.setBarcode(records.get(i).getStr("Barcode"));
            sysMaterialspreparedetail.setInvCode(records.get(i).getStr("cInvCode"));
            sysMaterialspreparedetail.setNum(new BigDecimal(0));
            sysMaterialspreparedetail.setQty(records.get(i).getBigDecimal("Qty"));
            sysMaterialspreparedetail.setPackRate(records.get(i).getBigDecimal("PackRate"));
//            sysMaterialspreparedetail.setSourceBillType();
//            sysMaterialspreparedetail.setSourceBillNo()
//            sysMaterialspreparedetail.setSourceBillNoRow()
//            sysMaterialspreparedetail.setSourceBillID()
//            sysMaterialspreparedetail.setSourceBillDid()
//            sysMaterialspreparedetail.setMemo()
            sysMaterialspreparedetail.setState(String.valueOf(0));
            sysMaterialspreparedetail.setIsDeleted(false);
            sysMaterialspreparedetail.setIcreateby(user.getId());
            sysMaterialspreparedetail.setCcreatename(user.getName());
            sysMaterialspreparedetail.setDcreatetime(now);
//            sysMaterialspreparedetail.setIupdateby()
//            sysMaterialspreparedetail.setCupdatename()
//            sysMaterialspreparedetail.setDupdatetime()
            sysMaterialspreparedetails.add(sysMaterialspreparedetail);
        }
        sysmaterialspreparedetailservice.batchSave(sysMaterialspreparedetails);
    }

    private void updateTableSubmitDatas(JBoltTable jBoltTable, SysMaterialsprepare sysMaterialsprepare) {
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) return;
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        ArrayList<SysMaterialspreparedetail> sysMaterialspreparedetails = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            SysMaterialspreparedetail sysMaterialspreparedetail = new SysMaterialspreparedetail();
            sysMaterialspreparedetail.setMasID(Long.valueOf(sysMaterialsprepare.getAutoID()));
//            sysMaterialspreparedetail.setModifyDate(now);
//            sysMaterialspreparedetail.setModifyPerson(user.getName());
            sysMaterialspreparedetail.setBarcode(row.get("barcode"));
            sysMaterialspreparedetail.setSourceBillNoRow(row.get("sourcebillnorow"));
            sysMaterialspreparedetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysMaterialspreparedetail.setPosCode(row.getStr("poscode"));
            sysMaterialspreparedetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysMaterialspreparedetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysMaterialspreparedetail.setSourceBillID(row.getStr("sourcebilldid"));
            sysMaterialspreparedetail.setMemo(row.getStr("memo"));
//			sysMaterialspreparedetail.setSourceType(row.getStr("sourcebilltype"));
//			sysMaterialspreparedetail.setAssemType(row.getStr("assemtype"));
//			sysMaterialspreparedetail.setWhCode(row.getStr("whcode"));
//			sysMaterialspreparedetail.setCombinationNo(Integer.valueOf(row.getStr("combinationno")));
//			sysMaterialspreparedetail.setRowNo(Integer.valueOf(row.getStr("rowno")));
//			sysMaterialspreparedetail.setTrackType(row.getStr("tracktype"));

            sysMaterialspreparedetails.add(sysMaterialspreparedetail);

        }
        sysmaterialspreparedetailservice.batchUpdate(sysMaterialspreparedetails);

        System.out.println("开始上传u8开始上传u8开始上传u8开始上传u8开始上传u8开始上传u8" + new Date());
        Ret ret = pushU8(sysMaterialsprepare, sysMaterialspreparedetails);
        System.out.println(new Date() + "u8上传结束，u8上传结束，u8上传结束，u8上传结束，u8上传结束" + ret);
    }

    private void deleteTableSubmitDatas(JBoltTable jBoltTable) {
        Object[] ids = jBoltTable.getDelete();
        if (ArrayUtil.isEmpty(ids)) return;
        sysmaterialspreparedetailservice.deleteByIds(ids);
    }

    public Ret pushU8(SysMaterialsprepare sysMaterialsprepare, List<SysMaterialspreparedetail> sysMaterialspreparedetails) {
        if (!CollectionUtils.isNotEmpty(sysMaterialspreparedetails)) {
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
        sysMaterialspreparedetails.stream().forEach(s -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("iwhname", "");
            jsonObject.set("invcode", "");
            jsonObject.set("userCode", user.getUsername());
            jsonObject.set("organizeCode", this.getdeptid());
            jsonObject.set("OWhCode", s.getPosCode());
            jsonObject.set("owhname", "");
            jsonObject.set("barcode", s.getBarcode());
            jsonObject.set("invstd", "");
            jsonObject.set("invname", "");
//            jsonObject.set("CreatePerson", s.getCreatePerson());
            jsonObject.set("qty", s.getQty());
            jsonObject.set("CreatePersonName", user.getName());
            jsonObject.set("IRdName", "");
            jsonObject.set("ORdName", "");
            jsonObject.set("Tag", "AssemVouch");
            jsonObject.set("VouchTemplate", "");
//			jsonObject.set("IWhCode",s.getWhCode());
//			jsonObject.set("BillType",s.getAssemType());
//			jsonObject.set("IRdType",sysassem.getIRdCode());
//			jsonObject.set("ORdType",sysassem.getORdCode());
//			jsonObject.set("IDeptCode",sysassem.getDeptCode());
//			jsonObject.set("ODeptCode",sysassem.getDeptCode());
//			jsonObject.set("RowNo",s.getRowNo());

            maindata.add(jsonObject);
        });
        data.set("MainData", maindata);

        //请求头
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Ret.msg("上传u8失败");
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

    public Page<Record> getgetManualAdddatas(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("materialsprepare.getManualAdddatas", kv).paginate(pageNumber, pageSize);
    }
}