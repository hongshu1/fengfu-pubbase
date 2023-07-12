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
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
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
import cn.rjtech.model.momdata.MoDoc;
import cn.rjtech.model.momdata.Person;
import cn.rjtech.model.momdata.SysMaterialsprepare;
import cn.rjtech.model.momdata.SysMaterialspreparedetail;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.HttpApiUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

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
        List<Record> list = dbTemplate("materialsprepare.recpor", kv).find();
        if (list.size()>=1){
            for (int l=0;l<list.size();l++){
                //判定备料单种类
                if (list.get(l).get("BillType").equals("手工作成")){
                    //备料单主表ID
                    String zhuID=list.get(l).get("AutoID");
                    Kv kv0=new Kv();
                    kv0.set("AutoID",zhuID);
                    List<Record> records65 = dbTemplate("materialsprepare.hh", kv0).find();

                    BigDecimal tag1=new BigDecimal(0);
                    BigDecimal tag2=new BigDecimal(0);
                    //各子件物料的计划数量
                    for (int c=0;c<records65.size();c++){
                        BigDecimal Allqty=records65.get(c).get("Qty");
                        String invcode=records65.get(c).get("InvCode");

                        Kv kv74=new Kv();
                        kv74.set("AutoID",zhuID);
                        kv74.set("invcode",invcode);
                        List<Record> records55 = dbTemplate("materialsprepare.bb", kv74).find();

                        BigDecimal nnn=new BigDecimal(0);
                        //计算已备料数量
                        for (int q=0;q<records55.size();q++){
                            nnn=nnn.add(records55.get(q).get("Qty"));
                        }
                        tag1=tag1.add(Allqty);
                        tag2=tag2.add(nnn);
                    }

                    if (tag1.compareTo(tag2)==1){
                        list.get(l).set("isFinish","备料中");
                    }
                    if (tag1.compareTo(tag2)==0){
                        list.get(l).set("isFinish","已完成");
                    }

                    if (tag2.compareTo(BigDecimal.ZERO)==0){
                        list.get(l).set("isFinish","待备料");
                    }
                    continue;
                }

                //备料单ID
                String autoID = list.get(l).get("AutoID");
                //工单ID
                String mdID = list.get(l).get("mdID").toString();
                //查询子件物料集
                Kv kv1 = new Kv();
                kv1.set("imodocid",mdID);
                List<Record> zijianwuliaojiS = dbTemplate("materialsprepare.zijianwuliaoji", kv1).find();
                //计算子件物料计划总数
                if (zijianwuliaojiS!=null && !zijianwuliaojiS.isEmpty()){
                    BigDecimal qtyAll = new BigDecimal(0);
                    for (int c=0;c<zijianwuliaojiS.size();c++){
                        qtyAll=qtyAll.add(zijianwuliaojiS.get(c).get("planIqty"));
                    }
                    //查询已备料子件数量
                    Kv kv2 = new Kv();
                    kv2.set("autoID",autoID);
                    List<Record> yibeiliao = dbTemplate("materialsprepare.checkQty", kv2).find();
                    BigDecimal qtyAll1 = new BigDecimal(0);
                    String daxiao="";
                    if (yibeiliao!=null && !yibeiliao.isEmpty()){
                        for (int r=0;r<yibeiliao.size();r++){
                            qtyAll1=qtyAll1.add(yibeiliao.get(r).get("Qty"));
                        }
                        //进行大小对比
                        int o = qtyAll.compareTo(qtyAll1);
                        switch(o){
                            case 0 :
                                daxiao="已完成";
                            case 1 :
                                daxiao="备料中";
                        }
                    }else {
                        daxiao="待备料";
                    }
                    list.get(l).set("isFinish",daxiao);
                }
            }
        }
        //处理页量页码信息
        int totalRow=list.size();
        int totalPage=totalRow/pageSize;
        if (totalPage*pageSize<totalRow){
            totalPage++;
        }
        return new Page(list,pageNumber, pageSize,totalPage,totalRow);
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

    //传入备料主单号
    public Page<Record> getDetail(int pageNumber, int pageSize, Kv kv) {
        if (kv.get("billno").equals("")){
            return null;
        }
        //先判断是自动作成还是手动作成
        Record record2 = dbTemplate("materialsprepare.kk", kv).findFirst();
        if (record2.get("BillType").toString().equals("手工作成")){
            //最后展示在表格的数据
            ArrayList<Record> objects1 = new ArrayList<>();
            ArrayList<Record> objects20 = new ArrayList<>();
            Record record5=new Record();
            //备料细表
            List<Record> record3s = dbTemplate("materialsprepare.tt", kv).find();
            for (int b=0;b<record3s.size();b++){
                //该存货编码
                String invcodee=record3s.get(b).get("InvCode");
                //该存货编码的计划数量
                BigDecimal qtyy=record3s.get(b).get("Qty");
                Kv kv1=new Kv();
                kv1.set("InvCode",invcodee);
                //有库存的子件物料,单个存货编码,批次号已排序
                List<Record> record4s = dbTemplate("materialsprepare.cc", kv1).find();
                //累计物料数量
                BigDecimal qtyyy = new BigDecimal(0);
                if (record4s.size()>0){
                    ArrayList<String> test = new ArrayList<>();
                    for (int d=0;d<record4s.size();d++){
                        qtyyy=qtyyy.add(record4s.get(d).get("Qty"));
                        if (qtyyy.compareTo(qtyy)<1){
                            test.add(record4s.get(d).get("Batch"));
                        }
                    }
                    //集合对批次号去重
                    Set set = new HashSet();
                    List newList = new ArrayList();
                    for (Iterator iter = test.iterator(); iter.hasNext();) {
                        Object element = iter.next();
                        if (set.add(element))
                            newList.add(element);
                    }
                    test.clear();
                    test.addAll(newList);

                    Kv kv6=new Kv();
                    kv6.set("cinvcode",invcodee);
                    Record record6 = dbTemplate("materialsprepare.rr", kv6).findFirst();
                    for (int x=0;x<test.size();x++){
                        record5.set("billno",kv.get("billno"));
                        record5.set("cmodocno",record2.get("SourceBillNo"));
                        record5.set("cinvcode",invcodee);
                        record5.set("cinvcode1",record6.get("cInvCode1"));
                        record5.set("cinvname1",record6.get("cInvName1"));
                        record5.set("cinvstd",record6.get("cInvStd"));
                        record5.set("cuomname",record6.get("cUomName"));
                        record5.set("whcode",record4s.get(0).get("WhCode"));
                        record5.set("poscode",record4s.get(0).get("PosCode"));
                        record5.set("planiqty",qtyy);
                        //获取已备料数量
                        Kv kv10 = new Kv();
                        kv10.set("icode",invcodee);
                        kv10.set("masid",Long.valueOf(record2.get("AutoID")));
                        List<Record> recordOfHasBeenPreparedss = dbTemplate("materialsprepare.vv", kv10).find();
                        BigDecimal HasBeenPrepared = new BigDecimal(0);
                        //判断是否已有备料
                        if (recordOfHasBeenPreparedss.size()<1){
                            record5.set("num",0);
                        }else {
                            for (int t=0;t<recordOfHasBeenPreparedss.size();t++){
                                HasBeenPrepared=HasBeenPrepared.add(recordOfHasBeenPreparedss.get(t).get("Qty"));
                            }
                            record5.set("num",HasBeenPrepared);
                        }
                        record5.set("scanqty",0);
                        record5.set("batch",test.get(x));
                        objects1.add(record5);
                    }
                    //无库存
                }else {
                    Record record000=new Record();
                    record000.set("billno",kv.get("billno"));
                    record000.set("cmodocno",record2.get("SourceBillNo"));
                    record000.set("cinvcode",invcodee);
                    Kv kv56=new Kv();
                    kv56.set("cinvcode",invcodee);
                    Record record67 = dbTemplate("materialsprepare.rr", kv56).findFirst();
                    record000.set("cinvcode1",record67.get("cInvCode1"));
                    record000.set("cinvname1",record67.get("cInvName1"));
                    record000.set("cinvstd",record67.get("cInvStd"));
                    record000.set("cuomname",record67.get("cUomName"));
//                    record000.set("whcode",);
//                    record000.set("poscode",);
                    record000.set("planiqty",qtyy);
                    record000.set("Batch","无库存");
                    objects20.add(record000);
                    System.out.println(objects20);
                }
            }
            System.out.println(objects20);
            if (objects20.size()>0){
                for (int e=0;e<objects20.size();e++){
                    objects1.add(objects20.get(e));
                }
            }

            //处理页量页码信息
            int totalRow=objects1.size();
            int totalPage=totalRow/pageSize;
            if (totalPage*pageSize<totalRow){
                totalPage++;
            }
            //已经实现数据根据计划需求数量先进先出,测试目标:动态展示先进先出,且数量不超过计划数量
            return new Page(objects1,pageNumber, pageSize,totalPage,totalRow);
        }
        //自动作成
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
                    if (!BATCH.equals(recordList.get(i).get("Batch").toString())){
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
                        record.set("cMoDocNo",recordList.get(i).get("cMoDocNo"));
                        record.set("WhCode",recordList.get(i).get("WhCode"));
                        record.set("PosCode",recordList.get(i).get("PosCode"));
                        record.set("scanqty",0);
                        //获取已备料数量
                        Kv kv1 = new Kv();
                        kv1.set("iAutoId",recordList.get(i).get("AutoID"));
                        kv1.set("InvCode",recordList.get(i).get("cInvCode"));
                        List<Record> recordOfHasBeenPrepared = dbTemplate("materialsprepare.HasBeenPrepared", kv1).find();
                        BigDecimal HasBeenPrepared = new BigDecimal(0);
                        //判断是否已有备料
                        if (recordOfHasBeenPrepared.size()<1){
                            record.set("num",0);
                        }else {
                            for (int t=0;t<recordOfHasBeenPrepared.size();t++){
                                HasBeenPrepared=HasBeenPrepared.add(recordOfHasBeenPrepared.get(t).get("Qty"));
                            }
                            record.set("num",HasBeenPrepared);
                        }
                        HasBeenPrepared=new BigDecimal(0);
                        objects.add(record);
                        j++;
                        //添加没有库存的物料
                        if (i==recordList.size()-2){
                            List<Record> withoutStocks = dbTemplate("materialsprepare.xianjinxianchuALL", kv).find();
                            if (withoutStocks.size()>=1){
                                for (int n=0;n<withoutStocks.size();n++){
                                    Record record1 = new Record();
                                    record1.set("cInvCode",withoutStocks.get(n).get("cInvCode"));
                                    record1.set("Batch","无库存");
                                    record1.set("cMoDocNo",withoutStocks.get(n).get("cMoDocNo"));
                                    record1.set("cInvCode1",withoutStocks.get(n).get("cInvCode1"));
                                    record1.set("cInvName1",withoutStocks.get(n).get("cInvName1"));
                                    record1.set("planIqty",withoutStocks.get(n).get("planIqty"));
                                    record1.set("Billno",record.get("Billno"));
                                    record1.set("SourceBillID",withoutStocks.get(n).get("SourceBillID"));
                                    record1.set("cInvStd",withoutStocks.get(n).get("cInvStd"));
                                    record1.set("cUomName",withoutStocks.get(n).get("cUomName"));
                                    record1.set("WhCode",withoutStocks.get(n).get("WhCode"));
                                    record1.set("PosCode",withoutStocks.get(n).get("PosCode"));
                                    objects.add(record1);
                                    j++;
                                }
                            }
                        }
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
                record.set("cMoDocNo",recordList.get(i).get("cMoDocNo"));
                record.set("cUomName",recordList.get(i).get("cUomName"));
                record.set("WhCode",recordList.get(i).get("WhCode"));
                record.set("PosCode",recordList.get(i).get("PosCode"));
                record.set("scanqty",0);
                //获取已备料数量
                Kv kv1 = new Kv();
                kv1.set("iAutoId",recordList.get(i).get("AutoID"));
                kv1.set("InvCode",recordList.get(i).get("cInvCode"));
                List<Record> recordOfHasBeenPrepared = dbTemplate("materialsprepare.HasBeenPrepared", kv1).find();
                BigDecimal HasBeenPrepared = new BigDecimal(0);
                //判断是否已有备料
                if (recordOfHasBeenPrepared.size()<1){
                    record.set("num",0);

                }else {
                    for (int t=0;t<recordOfHasBeenPrepared.size();t++){
                        HasBeenPrepared=HasBeenPrepared.add(recordOfHasBeenPrepared.get(t).get("Qty"));
                    }
                    record.set("num",HasBeenPrepared);
                }
                HasBeenPrepared=new BigDecimal(0);
                objects.add(record);
                j++;
                //添加没有库存的物料
                if (i==recordList.size()-2){
                    List<Record> withoutStocks = dbTemplate("materialsprepare.xianjinxianchuALL", kv).find();
                    if (withoutStocks.size()>=1){
                        for (int n=0;n<withoutStocks.size();n++){
                            Record record1 = new Record();
                            record1.set("cInvCode",withoutStocks.get(n).get("cInvCode"));
                            record1.set("Batch","无库存");
                            record1.set("cMoDocNo",withoutStocks.get(n).get("cMoDocNo"));
                            record1.set("cInvCode1",withoutStocks.get(n).get("cInvCode1"));
                            record1.set("cInvName1",withoutStocks.get(n).get("cInvName1"));
                            record1.set("planIqty",withoutStocks.get(n).get("planIqty"));
                            record1.set("Billno",record.get("Billno"));
                            record1.set("SourceBillID",withoutStocks.get(n).get("SourceBillID"));
                            record1.set("cInvStd",withoutStocks.get(n).get("cInvStd"));
                            record1.set("cUomName",withoutStocks.get(n).get("cUomName"));
                            record1.set("WhCode",withoutStocks.get(n).get("WhCode"));
                            record1.set("PosCode",withoutStocks.get(n).get("PosCode"));
                            objects.add(record1);
                            j++;
                        }
                    }
                }
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
        if (CollUtil.isEmpty(sysMaterialspreparedetails)) {
            return fail("数据不能为空");
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
                    return successWithData(jsonObject);
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
        return dept;
    }

    public Page<Record> getgetManualAdddatas(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("materialsprepare.getManualAdddatas", kv).paginate(pageNumber, pageSize);
    }

    public Page<Record> manualmanual(int pageNumber, int pageSize, List list) {
        ArrayList<Record> records = new ArrayList<>();
        for (int z=0;z<list.size();z++){
            Kv kv = new Kv();
            kv.set("iAutoId",list.get(z));
            Record record = dbTemplate("materialsprepare.mm", kv).findFirst();
            records.add(record);
        }
        //处理页量页码信息
        int totalRow=records.size();
        int totalPage=totalRow/pageSize;
        if (totalPage*pageSize<totalRow){
            totalPage++;
        }
        return new Page(records,pageNumber, pageSize,totalPage,totalRow);
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
                                        JBoltExcelHeader.create("cinvcode", "存货编码", 15),
                                        JBoltExcelHeader.create("cinvcode1", "客户部番", 15),
                                        JBoltExcelHeader.create("cinvname1", "部品名称", 15),
                                        JBoltExcelHeader.create("cinvstd", "规格", 15),
                                        JBoltExcelHeader.create("cuomname", "库存单位", 15),
                                        JBoltExcelHeader.create("", "计划数量", 15),
                                        JBoltExcelHeader.create("", "出库仓库", 15),
                                        JBoltExcelHeader.create("", "出库库区", 15),
                                        JBoltExcelHeader.create("", "已备料数量", 15),
                                        JBoltExcelHeader.create("", "剩余备料数量", 15),
                                        JBoltExcelHeader.create("batch", "可用批次号", 15),
                                        JBoltExcelHeader.create("statename", "齐料扫码检查", 15)
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
     * 导出订单列表
     */
    public List<Record> download(Kv kv, String sqlTemplate) {
        kv.setIfNotNull("OrganizeCode",getOrgCode());
        List<Record> list = dbTemplate(sqlTemplate, kv).find();
        return list;
    }

}