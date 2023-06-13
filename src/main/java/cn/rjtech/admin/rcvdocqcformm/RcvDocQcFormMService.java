package cn.rjtech.admin.rcvdocqcformm;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.util.JBoltRealUrlUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.instockqcformm.InStockQcFormMService;
import cn.rjtech.admin.rcvdocdefect.RcvDocDefectService;
import cn.rjtech.admin.rcvdocqcformd.RcvDocQcFormDService;
import cn.rjtech.admin.rcvdocqcformdline.RcvdocqcformdLineService;
import cn.rjtech.admin.syspuinstore.SysPuinstoreService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.enums.CMeasurePurposeEnum;
import cn.rjtech.enums.IsOkEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.excel.SheetPage;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.jfinal.aop.Inject;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * 质量管理-来料检
 *
 * @ClassName: RcvDocQcFormMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-11 11:44
 */
public class RcvDocQcFormMService extends BaseService<RcvDocQcFormM> {

    private final RcvDocQcFormM dao = new RcvDocQcFormM().dao();

    @Inject
    private RcvDocQcFormDService     rcvDocQcFormDService;     //质量管理-来料检单行配置表
    @Inject
    private RcvdocqcformdLineService rcvdocqcformdLineService; //质量管理-来料检明细列值表
    @Inject
    private RcvDocDefectService      rcvDocDefectService;      //质量管理-来料异常品记录
    @Inject
    private InStockQcFormMService    inStockQcFormMService;
    @Inject
    private SysPuinstoreService      sysPuinstoreService;      //采购入库单
    @Inject
    private VendorService            vendorService;                       //供应商档案

    @Override
    protected RcvDocQcFormM dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber  第几页
     * @param pageSize    每页几条数据
     * @param keywords    关键词
     * @param isCompleted 是否已完成：0. 未完成 1. 已完成
     * @param isCpkSigned 是否CPK数值的输入内容按正负数显示：0. 否 1. 是
     * @param isOk        是否判定合格：0. 否 1. 是
     * @param IsDeleted   删除状态：0. 未删除 1. 已删除
     */
    public Page<RcvDocQcFormM> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isCompleted,
                                             Boolean isCpkSigned, Boolean isOk, Boolean IsDeleted) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eqBooleanToChar("isCompleted", isCompleted);
        sql.eqBooleanToChar("isCpkSigned", isCpkSigned);
        sql.eqBooleanToChar("isOk", isOk);
        sql.eqBooleanToChar("IsDeleted", IsDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords, "cOrgName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
        return paginate(sql);
    }

    public Page<Record> pageList(Kv kv) {
        return dbTemplate("rcvdocqcformm.list", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
    }

    /**
     * ·
     * 根据表格ID生成table
     */
    public Ret createTable(Long iautoid, String cqcformname) {
        RcvDocQcFormM rcvDocQcFormM = findById(iautoid);
        if (null == rcvDocQcFormM) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //1、根据表格ID查询数据
        Long iQcFormId = rcvDocQcFormM.getIQcFormId();//表格ID
        List<Record> recordList = dbTemplate("rcvdocqcformm.getCheckoutList", Kv.by("iqcformid", iQcFormId)).find();
        if (recordList.isEmpty()) {
            return fail(cqcformname + "：没有检验项目，无法生成来料检查表");
        }
        ArrayList<RcvDocQcFormD> rcvDocQcFormDS = new ArrayList<>();
        for (Record record : recordList) {
            RcvDocQcFormD rcvDocQcFormD = new RcvDocQcFormD();//质量管理-来料检单行配置表
            rcvDocQcFormD.setIAutoId(JBoltSnowflakeKit.me.nextId());
            rcvDocQcFormD.setIRcvDocQcFormMid(iautoid);//来料检id
            rcvDocQcFormD.setIQcFormId(iQcFormId);//检验表格ID
            rcvDocQcFormD.setIFormParamId(record.getLong("iqcformtableitemid"));//检验项目ID
            rcvDocQcFormD.setISeq(record.get("iSeq"));
//            rcvDocQcFormD.setISubSeq(record.get("iSubSeq"));
            rcvDocQcFormD.setCQcFormParamIds(record.getStr("cQcFormParamIds"));
            rcvDocQcFormD.setIType(record.get("iType"));
            rcvDocQcFormD.setIStdVal(record.get("iStdVal"));
            rcvDocQcFormD.setIMaxVal(record.get("iMaxVal"));
            rcvDocQcFormD.setIMinVal(record.get("iMinVal"));
            rcvDocQcFormD.setCOptions(record.get("cOptions"));
            rcvDocQcFormDS.add(rcvDocQcFormD);
        }
        rcvDocQcFormDService.batchSave(rcvDocQcFormDS);

        //2、更新PL_RcvDocQcFormM检验结果(istatus)为“待检-1”
        rcvDocQcFormM.setIStatus(1);
        Ret ret = update(rcvDocQcFormM);
        if (ret.isFail()) {
            return ret;
        }
        return SUCCESS;
    }

    /**
     * 根据iautoid查询数据,并跳到检验页面
     */
    public Record getCheckoutListByIautoId(Long iautoId) {
        return dbTemplate("rcvdocqcformm.list", Kv.by("iautoid", iautoId)).findFirst();
    }

    /**
     * 点击检验时，进入弹窗自动加载table的数据
     */
    public List<Record> getCheckOutTableDatas(Kv kv) {
        List<Record> recordList = clearZero(dbTemplate("rcvdocqcformm.findChecoutListByIformParamid", kv).find());
        recordList.stream().forEach(record -> {
            record.set("cvaluelist", getCvaluelist());
        });
        return recordList;
    }

    public List<Integer> getCvaluelist() {
        return Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    /**
     * 在检验页面点击确定
     */
    public Ret saveCheckOutTable(JBoltPara JboltPara) {
        if (JboltPara == null || JboltPara.isEmpty()) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        Long docqcformmiautoid = JboltPara.getLong("docqcformmiautoid"); //主表id
        JSONArray serializeSubmitList = JboltPara.getJSONArray("serializeSubmitList");

        Boolean result = achieveSerializeSubmitList(serializeSubmitList, docqcformmiautoid,
            JboltPara.getString("cmeasurepurpose"), JboltPara.getString("cmeasurereason"),
            JboltPara.getString("cmeasureunit"), JboltPara.getString("cmemo"),
            JboltPara.getString("cdcno"), JboltPara.getString("isok"));
        return ret(result);
    }

    /*
     * 实现检验页面的serializeSubmitList
     * */
    public Boolean achieveSerializeSubmitList(JSONArray serializeSubmitList, Long docqcformmiautoid, String cmeasurepurpose,
                                              String cmeasurereason, String cmeasureunit, String cmemo, String cdcno,
                                              String isok) {
        List<RcvdocqcformdLine> rcvdocqcformdLines = new ArrayList<>();
        boolean result = tx(() -> {
            for (int i = 0; i < serializeSubmitList.size(); i++) {
                JSONObject jsonObject = serializeSubmitList.getJSONObject(i);
                String ircvdocqcformdid = jsonObject.getString("iautoid");
                String iseq = jsonObject.getString("iseq");
                JSONArray serializeElement = jsonObject.getJSONArray("serializeElement");
                JSONArray elementList = serializeElement.getJSONArray(0);
                for (int j = 0; j < elementList.size(); j++) {
                    JSONObject object = elementList.getJSONObject(j);
                    String name = object.getString("name");
                    String cvalue = object.getString("value");

                    RcvdocqcformdLine rcvdocqcformdLine = new RcvdocqcformdLine();//质量管理-来料检明细列值表
                    saveRcvdocqcformdLineModel(rcvdocqcformdLine, ircvdocqcformdid, iseq, cvalue);
                    rcvdocqcformdLines.add(rcvdocqcformdLine);
                }
            }
            //保存line
            rcvdocqcformdLineService.batchSave(rcvdocqcformdLines);

            /*
             * 来料检表（PL_RcvDocQcFormM）
             * 1.如果isok=0，代表不合格，将iStatus更新为2，isCompleted更新为1；
             * 2.如果isok=1，代表合格，将iStatus更新为3，isCompleted更新为1
             */
            RcvDocQcFormM docQcFormM = findById(docqcformmiautoid);
            saveDocQcFormMModel(docQcFormM, cmeasurepurpose, cmeasurereason, cmeasureunit, cmemo, cdcno, isok);
            update(docQcFormM);

            //判断生成异常品单，还是采购入库单
            saveSysPuinstore(isok, docqcformmiautoid, docQcFormM);
            return true;
        });
        return result;
    }

    /*
     * 判断生成异常品单，还是采购入库单
     * */
    public Boolean saveSysPuinstore(String isok, Long docqcformmiautoid, RcvDocQcFormM docQcFormM) {
        boolean flag = true;
        if (Integer.valueOf(isok).equals(IsOkEnum.NO.getValue())) {//如果结果为不合格，记录不良品
            RcvDocDefect defect = rcvDocDefectService
                .findStockoutDefectByiRcvDocQcFormMid(docqcformmiautoid);
            if (null == defect) {
                RcvDocDefect rcvDocDefect = new RcvDocDefect();
                rcvDocDefectService.saveRcvDocDefectModel(rcvDocDefect, docQcFormM);
                Ret success = rcvDocDefectService.save(rcvDocDefect);
                flag = success.isOk();
            }
        } else if (Integer.valueOf(isok).equals(IsOkEnum.YES.getValue())) {//如果合格，自动生成采购入库单
            SysPuinstore sysPuinstore = new SysPuinstore();
            saveSysPuinstoreModel(sysPuinstore, docQcFormM);
            // Ret success = sysPuinstoreService.save(sysPuinstore);
            //flag =success.isOk();
        }
        return flag;
    }

    /*
     * 传给采购入库单的参数
     * */
    public void saveSysPuinstoreModel(SysPuinstore sysPuinstore, RcvDocQcFormM docQcFormM) {
        Date date = new Date();
        Vendor vendor = vendorService.findById(docQcFormM.getIVendorId());
        sysPuinstore.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
        sysPuinstore.setBillType("");//到货单类型：采购PO  委外OM
        sysPuinstore.setOrganizeCode(getOrgCode());//组织编码
        sysPuinstore.setBillNo(""); //入库单号
        sysPuinstore.setBillDate(new Date().toString()); //入库日期
        sysPuinstore.setVenCode(vendor.getCVenCode()); //供应商编码
        sysPuinstore.setMemo("");
        sysPuinstore.setCCreateName(JBoltUserKit.getUserName());
        sysPuinstore.setDCreateTime(date);
        sysPuinstore.setCAuditName("");//审核人
        sysPuinstore.setAuditDate(date);//审核日期
        sysPuinstore.setDUpdateTime(date);//修改日期
        sysPuinstore.setCUpdateName("");//修改人
        sysPuinstore.setIAuditStatus(1);//状态 1已保存 2待审批 3已审批 4审批不通过
    }

    /**
     * 点击查看时，进入弹窗自动加载table的数据
     */
    public List<Record> getonlyseelistByiautoid(Kv kv) {
        kv.set("ircvdocqcformmid", kv.get("iautoid"));
        List<Record> recordList = dbTemplate("rcvdocqcformm.getonlyseelistByiautoid", kv).find();
        List<Record> clearRecordList = clearZero(recordList);

        Map<Object, List<Record>> map = clearRecordList.stream()
            .collect(Collectors.groupingBy(p -> p.get("iautoid"), Collectors.toList()));

        List<Record> records = new ArrayList<>();
        for (Entry<Object, List<Record>> entry : map.entrySet()) {
            List<Record> value = entry.getValue();
            for (int i = 0; i < value.size(); i++) {
                value.get(i).set("name", (i + 1));
            }
            Record record = new Record();
            Record record1 = value.get(0);
            record.set("cvalueList", value);
            record.set("coptions", record1.get("coptions"));
            record.set("cqcformparamids", record1.get("cqcformparamids"));
            record.set("cqcitemname", record1.get("cqcitemname"));
            record.set("cqcparamname", record1.get("cqcparamname"));
            record.set("iautoid", record1.get("iautoid"));
            record.set("iformparamid", record1.get("iformparamid  "));
            record.set("imaxval", record1.get("imaxval"));
            record.set("iminval", record1.get("iminval"));
            record.set("iqcformid", record1.get("iqcformid"));
            record.set("ircvdocqcformmid", record1.get("ircvdocqcformmid"));
            record.set("iseq", record1.get("iseq"));
            record.set("istdval", record1.get("istdval"));
            record.set("isubseq", record1.get("isubseq"));
            record.set("itype", record1.get("itype"));
            records.add(record);
        }
        List<Record> resultRecord =
            records.stream().sorted(Comparator.comparing(e -> e.getInt("iseq"))).collect(Collectors.toList());
        return resultRecord;
    }

    public List<Record> getonlyseelistByiautoid(Long iautoid) {
        Kv kv = new Kv();
        kv.set("ircvdocqcformmid", iautoid);
        List<Record> recordList = dbTemplate("rcvdocqcformm.getonlyseelistByiautoid", kv).find();
        List<Record> clearRecordList = clearZero(recordList);

        Map<Object, List<Record>> map = clearRecordList.stream()
            .collect(Collectors.groupingBy(p -> p.get("iautoid"), Collectors.toList()));
        List<Record> docparamlist = new ArrayList<>();
        for (Entry<Object, List<Record>> entry : map.entrySet()) {
            docparamlist = entry.getValue();
            break;
        }
        return docparamlist;
    }

    /**
     * 清除多余的零
     */
    public List<Record> clearZero(List<Record> recordList) {
        recordList.stream().forEach(e -> {
            e.set("istdval", e.getBigDecimal("istdval").stripTrailingZeros().toPlainString());
            e.set("imaxval", e.getBigDecimal("imaxval").stripTrailingZeros().toPlainString());
            e.set("iminval", e.getBigDecimal("iminval").stripTrailingZeros().toPlainString());
        });
        return recordList;
    }

    /**
     * 保存
     */
    public Ret save(RcvDocQcFormM rcvDocQcFormM) {
        if (rcvDocQcFormM == null || isOk(rcvDocQcFormM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(rcvDocQcFormM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = rcvDocQcFormM.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(rcvDocQcFormM.getIAutoId(), JBoltUserKit.getUserId(), rcvDocQcFormM.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(RcvDocQcFormM rcvDocQcFormM) {
        if (rcvDocQcFormM == null || notOk(rcvDocQcFormM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        RcvDocQcFormM dbRcvDocQcFormM = findById(rcvDocQcFormM.getIAutoId());
        if (dbRcvDocQcFormM == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = rcvDocQcFormM.update();
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param rcvDocQcFormM 要删除的model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(RcvDocQcFormM rcvDocQcFormM, Kv kv) {
        //addDeleteSystemLog(rcvDocQcFormM.getIAutoId(), JBoltUserKit.getUserId(),rcvDocQcFormM.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param rcvDocQcFormM model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    public String checkInUse(RcvDocQcFormM rcvDocQcFormM, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(RcvDocQcFormM rcvDocQcFormM, String column, Kv kv) {
        //addUpdateSystemLog(rcvDocQcFormM.getIAutoId(), JBoltUserKit.getUserId(), rcvDocQcFormM.getName(),"的字段["+column+"]值:"+rcvDocQcFormM.get(column));
        /**
         switch(column){
         case "isCompleted":
         break;
         case "isCpkSigned":
         break;
         case "isOk":
         break;
         }
         */
        return null;
    }

    /**
     * 上传图片
     */
    public List<String> uploadImage(List<UploadFile> files) {
        List<String> imgList = new ArrayList<>();
        if (ObjectUtil.isEmpty(files)) {
            return imgList;
        }
        for (UploadFile uploadFile : files) {
            String localUrl = FileUtil.normalize(JBoltRealUrlUtil.get(JFinal.me().getConstants().getBaseUploadPath() + '/'
                + uploadFile.getUploadPath() + '/' + uploadFile.getFileName()));
            imgList.add(localUrl);
        }
        return imgList;
    }

    /**
     * 在编辑页面点击确定
     */
    public Ret saveEditTable(JBoltPara JboltPara) {
        if (JboltPara == null || JboltPara.isEmpty()) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        Long docqcformmiautoid = JboltPara.getLong("docqcformmiautoid"); //主表id
        String isok = JboltPara.getString("isok");
        //是否合格不能为空
        if (StringUtils.isBlank(isok)) {
            return fail("请判定是否合格");
        }
        Boolean result = achiveEditSerializeSubmitList(JboltPara.getJSONArray("serializeSubmitList"), docqcformmiautoid,
            JboltPara.getString("cmeasurepurpose"), JboltPara.getString("cmeasurereason"),
            JboltPara.getString("cmeasureunit"), JboltPara.getString("cmemo"),
            JboltPara.getString("cdcno"), isok);

        return ret(result);
    }

    /*
     * 实现编辑页面的SerializeSubmitList
     * */
    public Boolean achiveEditSerializeSubmitList(JSONArray serializeSubmitList, Long docqcformmiautoid, String cmeasurepurpose,
                                                 String cmeasurereason, String cmeasureunit, String cmemo, String cdcno,
                                                 String isok) {
        List<RcvdocqcformdLine> editRcvdocqcformdLines = new ArrayList<>();
        List<RcvdocqcformdLine> saveRcvdocqcformdLines = new ArrayList<>();
        boolean result = tx(() -> {
            for (int i = 0; i < serializeSubmitList.size(); i++) {
                JSONObject jsonObject = serializeSubmitList.getJSONObject(i);
                System.out.println("new Gson().toJson(jsonObject)====>"+new Gson().toJson(jsonObject));
                String iseq = jsonObject.getString("iseq");
                Long ircvdocqcformmid = jsonObject.getLong("iautoid");
                JSONArray cvaluelist = jsonObject.getJSONArray("cvaluelist");
                JSONArray serializeElement = jsonObject.getJSONArray("serializeElement");
                JSONArray elementList = serializeElement.getJSONArray(0);
                for (int j = 0; j < elementList.size(); j++) {
                    JSONObject object = elementList.getJSONObject(j);
                    String cvalue = object.getString("value");
                    JSONObject cvaluelistJSONObject = cvaluelist.getJSONObject(j);
                    Long lineiautoid = cvaluelistJSONObject.getLong("lineiautoid");
                    if (lineiautoid != null) {
                        RcvdocqcformdLine rcvdocqcformdLine = rcvdocqcformdLineService.findById(lineiautoid);//质量管理-来料检明细列值表
                        rcvdocqcformdLine.setCValue(cvalue);
                        editRcvdocqcformdLines.add(rcvdocqcformdLine);
                    } else {
                        //如果没有，代表是新增页的数据
                        RcvdocqcformdLine rcvdocqcformdLine = new RcvdocqcformdLine();//质量管理-出库检明细列值表
                        saveRcvdocqcformdLineModel(rcvdocqcformdLine, String.valueOf(ircvdocqcformmid), iseq, cvalue);
                        saveRcvdocqcformdLines.add(rcvdocqcformdLine);
                    }
                }
            }
            //更新line
            if (!editRcvdocqcformdLines.isEmpty()) {
                rcvdocqcformdLineService.batchUpdate(editRcvdocqcformdLines);
            }
            if (!saveRcvdocqcformdLines.isEmpty()) {
                rcvdocqcformdLineService.batchSave(saveRcvdocqcformdLines);
            }
            //更新来料检主表
            RcvDocQcFormM docQcFormM = findById(docqcformmiautoid);
            saveDocQcFormMModel(docQcFormM, cmeasurepurpose, cmeasurereason, cmeasureunit, cmemo, cdcno, isok);
            update(docQcFormM);
            return true;
        });
        return result;
    }

    /**
     * 给主表传参
     */
    public void saveDocQcFormMModel(RcvDocQcFormM docQcFormM, String cmeasurepurpose,
                                    String cmeasurereason, String cmeasureunit, String cmemo, String cdcno, String isok) {
        docQcFormM.setCMeasurePurpose(cmeasurepurpose);//测定目的
        docQcFormM.setCMeasureReason(cmeasurereason);//测定理由
        docQcFormM.setCMeasureUnit(cmeasureunit); //测定单位
        docQcFormM.setCMemo(cmemo);//备注
        docQcFormM.setCDcNo(cdcno); //设变号
        Integer isOk = Integer.valueOf(isok);
        docQcFormM.setIsOk(IsOkEnum.toEnum(isOk).getText());//是否合格
        docQcFormM.setIStatus(isOk.equals(IsOkEnum.NO.getValue()) ? 2 : 3);
        docQcFormM.setIsCompleted(true);
        docQcFormM.setIsCpkSigned(false);
    }

    /**
     * 给质量管理-来料检明细列值传参
     */
    public void saveRcvdocqcformdLineModel(RcvdocqcformdLine rcvdocqcformdLine,
                                           String iautoid, String iseq, String cvalue) {
        rcvdocqcformdLine.setIAutoId(JBoltSnowflakeKit.me.nextId());
        rcvdocqcformdLine.setIRcvDocQcFormDid(Long.valueOf(iautoid));
        rcvdocqcformdLine.setISeq(Integer.valueOf(iseq));
        rcvdocqcformdLine.setCValue(cvalue);
    }

    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap<String, Object>(new KeyCompareUtil());
        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * 从小到大排序
     */
    public static class KeyCompareUtil implements Comparator<String> {

        @Override
        public int compare(String s1, String s2) {
            return Integer.valueOf(s1).compareTo(Integer.valueOf(s2));
        }
    }

    /**
     * object 转 list
     *
     * @param obj   需要转换的List对象
     * @param clazz List中元素的class
     */
    public static <T> List<T> oobjectToList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        // 判断 obj 是否包含 List 类型
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                // 使用Class.cast做类型转换
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    /*
     * 获取导出数据
     * */
    public Kv getExportData(Long iautoid) {
        //1、所有sheet
        List<SheetPage<Record>> pages = new ArrayList<>();
        //2、每个sheet的名字
        List<String> sheetNames = new ArrayList<>();
        sheetNames.add("sheet1");
        sheetNames.add("sheet2");
        sheetNames.add("sheet3");
        //3、主表数据
        RcvDocQcFormM rcvDocQcFormM = findById(iautoid);
        //测定目的
        String cMeasurePurpose = "";
        String[] split = rcvDocQcFormM.getCMeasurePurpose().split(",");
        for (int i = 0; i < split.length; i++) {
            if (StringUtils.isNotBlank(split[i])) {
                String text = CMeasurePurposeEnum.toEnum(Integer.valueOf(split[i])).getText();
                cMeasurePurpose += text + ",";
            }
        }
        rcvDocQcFormM.setCMeasurePurpose(StringUtils.isNotBlank(cMeasurePurpose)
            ? cMeasurePurpose.substring(0, cMeasurePurpose.lastIndexOf(",")) : cMeasurePurpose);
        //4、明细表数据
        List<Record> recordList = getonlyseelistByiautoid(Kv.by("iautoid", iautoid));
        //5、如果cvalue的列数>10行，分多个页签
        Record data = recordList.get(0);
        //核心业务逻辑，对列数进行分组
        inStockQcFormMService.commonPageMethod(data, recordList, rcvDocQcFormM, pages);

        return Kv.by("pages", pages).set("sheetNames", sheetNames);
    }

}