package cn.rjtech.admin.rcvdocqcformm;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.util.JBoltRealUrlUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.instockqcformm.InStockQcFormMService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventoryqcform.InventoryQcFormService;
import cn.rjtech.admin.qcform.QcFormService;
import cn.rjtech.admin.qcformparam.QcFormParamService;
import cn.rjtech.admin.qcformtableitem.QcFormTableItemService;
import cn.rjtech.admin.rcvdocdefect.RcvDocDefectService;
import cn.rjtech.admin.rcvdocqcformd.RcvDocQcFormDService;
import cn.rjtech.admin.rcvdocqcformdline.RcvdocqcformdLineService;
import cn.rjtech.admin.syspuinstore.SysPuinstoreService;
import cn.rjtech.admin.syspuinstore.SysPuinstoredetailService;
import cn.rjtech.admin.syspureceive.SysPureceiveService;
import cn.rjtech.admin.syspureceive.SysPureceivedetailService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.enums.CMeasurePurposeEnum;
import cn.rjtech.enums.IsOkEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
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

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

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
    private RcvDocQcFormDService      rcvDocQcFormDService;     //质量管理-来料检单行配置表
    @Inject
    private RcvdocqcformdLineService  rcvdocqcformdLineService; //质量管理-来料检明细列值表
    @Inject
    private RcvDocDefectService       rcvDocDefectService;      //质量管理-来料异常品记录
    @Inject
    private InStockQcFormMService     inStockQcFormMService;
    @Inject
    private SysPuinstoreService       sysPuinstoreService;      //采购入库单
    @Inject
    private VendorService             vendorService;            //供应商档案
    @Inject
    private InventoryService          inventoryService;
    @Inject
    private InventoryQcFormService    inventoryQcFormService;
    @Inject
    private QcFormTableItemService    qcFormTableItemService;
    @Inject
    private QcFormParamService        qcFormParamService;
    @Inject
    private QcFormService             qcFormService;
    @Inject
    private SysPureceiveService       sysPureceiveService;//收料单
    @Inject
    private SysPureceivedetailService sysPureceivedetailService;//收料单明细表
    @Inject
    private SysPuinstoredetailService syspuinstoredetailservice;

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

    /*生成检验表格和更新状态*/
    public Ret createTable(Long iautoid) {
        RcvDocQcFormM rcvDocQcFormM = findById(iautoid);
        if (null == rcvDocQcFormM) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //1、生成的时候没有iqcformid，只有存货编码
        Long iInventoryId = rcvDocQcFormM.getIInventoryId();
        Inventory inventory = inventoryService.findById(iInventoryId);
        ValidationUtils.notNull(inventory, "存货编码不存在,无法生成或检验！！！");

        InventoryQcForm inventoryQcForm = inventoryQcFormService.findByIInventoryId(iInventoryId);
        ValidationUtils.notNull(inventoryQcForm, inventory.getCInvCode() + "：没有在【质量建模-检验适用标准】维护标准表格");

        Long iQcFormId = inventoryQcForm.getIQcFormId();
        QcForm qcForm = qcFormService.findById(iQcFormId);
        ValidationUtils.notNull(qcForm, "【质量建模-质量表格设置】检验表格不存在");

        List<Map<String, Object>> recordList = findByIQcFormId(iQcFormId);
        ValidationUtils.notEmpty(recordList, qcForm.getCQcFormName() + "：【质量建模-质量表格设置】没有维护需要检验的项目");

        //2、更新PL_RcvDocQcFormM检验结果(istatus)为“待检：1”
        rcvDocQcFormM.setIQcFormId(inventoryQcForm.getIQcFormId());//bd_qcform的主键
        rcvDocQcFormM.setIStatus(1);
        rcvDocQcFormM.setCUpdateName(JBoltUserKit.getUserName());
        rcvDocQcFormM.setDUpdateTime(new Date());
        rcvDocQcFormM.setIUpdateBy(JBoltUserKit.getUserId());

        List<RcvDocQcFormD> formDList = new ArrayList<>();
        for (Map<String, Object> map : recordList) {
            RcvDocQcFormD qcFormD = new RcvDocQcFormD();
            rcvDocQcFormDService.saveRcvDocQcFormDModel(qcFormD, iautoid, iQcFormId, Integer.valueOf(map.get("iseq").toString()),
                Integer.valueOf(map.get("itype").toString()), map.get("istdval"), map.get("imaxval"), map.get("iminval"),
                map.get("coptions"), map.get("iqcformtableparamid").toString());
            formDList.add(qcFormD);
        }

        tx(() -> {
            //更新主表状态
            ValidationUtils.isTrue(rcvDocQcFormM.update(), "生成或检验失败！");
            //生成从表数据
            rcvDocQcFormDService.batchSave(formDList);
            return true;
        });

        return SUCCESS;
    }

    /**
     * 根据iautoid查询数据,并跳到检验页面
     */
    public Record getCheckoutListByIautoId(Long iautoId) {
        return dbTemplate("rcvdocqcformm.list", Kv.by("iautoid", iautoId)).findFirst();
    }

    /*
     * 跳转检验时，判断是否要自动生成明细表
     * */
    public void checkAutoCreateRcvDocQcFormD(Long iRcvDocQcFormMid) {
        List<RcvDocQcFormD> formDList = rcvDocQcFormDService.findByIRcvDocQcFormMId(iRcvDocQcFormMid);
        if (formDList.isEmpty()) {
            createTable(iRcvDocQcFormMid);
        }
    }

    /**
     * 点击检验时，进入弹窗自动加载table的数据
     */
    public List<Record> getCheckOutTableDatas(Kv kv) {
        List<Record> recordList = clearZero(dbTemplate("rcvdocqcformm.getCheckOutTableDatas", kv).find());
        for (Record record : recordList) {
            kv.set("iqcformid", record.get("iqcformid"));
            kv.set("iqcformtableparamid", record.get("iformparamid"));
            List<Record> qcFormTableItemList = dbTemplate("rcvdocqcformm.getQcFormTableItemList", kv).find();

            List<Map<String, Object>> headData = getTableHeadData(record.getLong("iqcformid"));
            //1.1、qcFormTableItemList的cQcItemName按照mapList的cqcitemname排序
            for (int i = 0; i < headData.size(); i++) {
                Map<String, Object> map = headData.get(i);
                for (int j = 0; j < qcFormTableItemList.size(); j++) {
                    Record qcFormRecord = qcFormTableItemList.get(j);
                    if (ObjUtil.equal(map.get("iqcitemid"), qcFormRecord.get("iqcformitemid"))) {
                        Collections.swap(qcFormTableItemList, j, i);
                    }
                }
            }

            //1、paramnamelist
            List<String> paramnamelist = qcFormTableItemList.stream().map(e -> e.getStr("cQcParamName"))
                .collect(Collectors.toList());
            record.set("paramnamelist", paramnamelist);
            //2、明细表有值，显示明细表的数据；否则显示显示空值
            List<Record> rcvdocqcformdLineList = getRcvdocqcformdLineList(record.get("iautoid"));
            if (!rcvdocqcformdLineList.isEmpty()) {
                for (int i = 0; i < rcvdocqcformdLineList.size(); i++) {
                    rcvdocqcformdLineList.get(i).set("name", (i + 1));
                }
                record.set("cvaluelist", rcvdocqcformdLineList);
            } else {
                record.set("cvaluelist", getCvaluelist(record.get("iautoid"), record.get("iseq")));
            }
        }
        return recordList;
    }

    public List<Record> getRcvdocqcformdLineList(Long iRcvDocQcFormDid) {
        return dbTemplate("rcvdocqcformm.getRcvdocqcformdLineList", Kv.by("iRcvDocQcFormDid", iRcvDocQcFormDid)).find();
    }

    public List<Record> getCvaluelist(Long ircvdocqcformdid, int iseq) {
        ArrayList<Record> list = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            Record record = new Record();
            record.set("name", i);
            record.set("ircvdocqcformdid", ircvdocqcformdid);
            record.set("iseq", iseq);
            record.set("cvalue", "");
            record.set("iautoid", "");
            list.add(record);
        }
        return list;
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

    public Boolean achieveSerializeSubmitList(JSONArray serializeSubmitList, Long docqcformmiautoid, String cmeasurepurpose,
                                              String cmeasurereason, String cmeasureunit, String cmemo, String cdcno,
                                              String isok) {
        List<RcvdocqcformdLine> saveRcvdocqcformdLines = new ArrayList<>();
        List<RcvdocqcformdLine> updateRcvdocqcformdLines = new ArrayList<>();
        boolean result = tx(() -> {
            Date now = new Date();
            for (int i = 0; i < serializeSubmitList.size(); i++) {
                JSONObject jsonObject = serializeSubmitList.getJSONObject(i);
                JSONArray cvaluelist = jsonObject.getJSONArray("cvaluelist");
                for (int j = 0; j < cvaluelist.size(); j++) {
                    JSONObject object = cvaluelist.getJSONObject(j);
                    String ircvdocqcformdid = object.getString("ircvdocqcformdid");
                    String cvalue = object.getString("cvalue");
                    String iseq = object.getString("iseq");
                    String name = object.getString("name");
                    Long iautoid = object.getLong("iautoid");
                    RcvdocqcformdLine line = rcvdocqcformdLineService.findById(iautoid);
                    if (line != null) {
                        line.setCValue(cvalue);
                        updateRcvdocqcformdLines.add(line);
                    } else {
                        RcvdocqcformdLine rcvdocqcformdLine = new RcvdocqcformdLine();//质量管理-来料检明细列值表
                        saveRcvdocqcformdLineModel(rcvdocqcformdLine, ircvdocqcformdid, iseq, cvalue);
                        saveRcvdocqcformdLines.add(rcvdocqcformdLine);
                    }
                }
            }
            //保存line
            if (!saveRcvdocqcformdLines.isEmpty()) {
                rcvdocqcformdLineService.batchSave(saveRcvdocqcformdLines);
            }
            if (!updateRcvdocqcformdLines.isEmpty()) {
                rcvdocqcformdLineService.batchUpdate(updateRcvdocqcformdLines);
            }

            /*
             * 来料检表（PL_RcvDocQcFormM）
             * 1.如果isok=0，代表不合格，将iStatus更新为2，isCompleted更新为1；
             * 2.如果isok=1，代表合格，将iStatus更新为3，isCompleted更新为1
             */
            RcvDocQcFormM docQcFormM = findById(docqcformmiautoid);
            saveDocQcFormMModel(docQcFormM, cmeasurepurpose, cmeasurereason, cmeasureunit, cmemo, cdcno, now, isok);
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
    public void saveSysPuinstore(String isok, Long docqcformmiautoid, RcvDocQcFormM docQcFormM) {
        if (StrUtil.isBlank(isok)) {
            return;
        }
        Integer isOkValue = Integer.valueOf(isok);
        if (ObjUtil.equal(isOkValue, IsOkEnum.NO.getValue())) {//如果结果为不合格，生成“来料异常品记录”
            RcvDocDefect defect = rcvDocDefectService
                .findStockoutDefectByiRcvDocQcFormMid(docqcformmiautoid);
            if (null == defect) {
                RcvDocDefect rcvDocDefect = new RcvDocDefect();
                rcvDocDefectService.saveRcvDocDefectModel(rcvDocDefect, docQcFormM);
                ValidationUtils.isTrue(rcvDocDefect.save(), "创建来料异常品记录单据失败");
            }
        } else if (ObjUtil.equal(isOkValue, IsOkEnum.YES.getValue())) {//如果合格，自动生成采购入库单
            SysPuinstore sysPuinstore = new SysPuinstore();
            List<SysPuinstoredetail> sysPuinstoredetails = new ArrayList<>();
            saveSysPuinstoreModel(sysPuinstore, docQcFormM, sysPuinstoredetails);
            //保存采购入库单主表数据
            ValidationUtils.isTrue(sysPuinstore.save(), "创建采购入库单据失败");
            //保存采购入库单从表数据
            syspuinstoredetailservice.batchSave(sysPuinstoredetails);
        }
    }

    /*传给采购入库单主表的参数*/
    public void saveSysPuinstoreModel(SysPuinstore sysPuinstore, RcvDocQcFormM docQcFormM,
                                      List<SysPuinstoredetail> sysPuinstoredetails) {
        Date date = new Date();
        Vendor vendor = vendorService.findById(docQcFormM.getIVendorId());
        SysPureceive sysPureceive = sysPureceiveService.findById(docQcFormM.getIRcvDocId());
        sysPuinstore.setOrganizeCode(getOrgCode());//组织编码
        sysPuinstore.setBillNo(String.valueOf(JBoltSnowflakeKit.me.nextId())); //入库单号
        sysPuinstore.setBillDate(DateUtil.formatDate(date)); //入库日期
        sysPuinstore.setVenCode(vendor.getCVenCode()); //供应商编码
        sysPuinstore.setMemo(docQcFormM.getCMemo());
        sysPuinstore.setCCreateName(JBoltUserKit.getUserName());
        sysPuinstore.setDCreateTime(date);
        //状态 0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
        sysPuinstore.setIAuditStatus(0);
        sysPuinstore.setSourceBillNo(sysPureceive.getSourceBillNo());
        sysPuinstore.setSourceBillID(sysPureceive.getSourceBillID());
        sysPuinstore.setWhCode(sysPureceive.getWhCode());
        sysPuinstore.setWhName(sysPureceive.getWhName());
        sysPuinstore.setIAuditWay(0);
        sysPuinstore.setIsDeleted(false);
        sysPuinstore.setICreateBy(JBoltUserKit.getUserId());

        //采购入库从表
        List<SysPureceivedetail> list = sysPureceivedetailService.findFirstBy(sysPureceive.getAutoID());
        saveSysPuinstoreDetail(sysPuinstoredetails, list, sysPuinstore);
    }

    /*传给采购入库单从表的参数*/
    public void saveSysPuinstoreDetail(List<SysPuinstoredetail> sysPuinstoredetails,
                                       List<SysPureceivedetail> list, SysPuinstore sysPuinstore) {
        int i = 1;
        for (SysPureceivedetail detail : list) {
            Record record = dbTemplate("syspureceive.purchaseOrderD", Kv.by("barcode", detail.getBarcode()))
                .findFirst();
            SysPuinstoredetail sysPuinstoredetail = new SysPuinstoredetail();
            sysPuinstoredetail.setMasID(sysPuinstore.getAutoID());
            sysPuinstoredetail.setSourceBillType(detail.getSourceBillType());
            sysPuinstoredetail.setSourceBillNo(detail.getSourceBillNo());
            sysPuinstoredetail.setSourceBillNoRow(detail.getSourceBillNo() + "-" + i);
            sysPuinstoredetail.setSourceBillDid(detail.getSourceBillDid());
            sysPuinstoredetail.setSourceBillID(detail.getSourceBillID());
            sysPuinstoredetail.setWhcode(detail.getWhcode());
            sysPuinstoredetail.setPosCode(detail.getPosCode());
            sysPuinstoredetail.setQty(detail.getQty());
            sysPuinstoredetail.setRowNo(i);
            sysPuinstoredetail.setTrackType(detail.getTrackType());
            sysPuinstoredetail.setCCreateName(JBoltUserKit.getUserName());
            sysPuinstoredetail.setDCreateTime(new Date());
            sysPuinstoredetail.setSpotTicket(detail.getBarcode());
            sysPuinstoredetail.setPuUnitCode(record.getStr("puunitcode"));
            sysPuinstoredetail.setPuUnitName(record.getStr("puunitname"));
            sysPuinstoredetail.setIsDeleted(false);
            sysPuinstoredetail.setInvcode(record.getStr("cinvcode"));

            //主表的数据
            sysPuinstore.setBillType(record.getStr("ipurchasetypeid"));//采购类型：采购PO  委外OM
            sysPuinstore.setDeptCode(record.getStr("cdepcode"));
            //业务类型：0：采购入库，1：委外入库
            sysPuinstore.setIBusType(Integer.valueOf(record.getStr("ibustype")));
            sysPuinstore.setRdCode(record.getStr("scrdcode"));

            sysPuinstoredetails.add(sysPuinstoredetail);
            i++;
        }
    }

    /**
     * 点击查看时，进入弹窗自动加载table的数据
     */
    /*public List<Record> getonlyseelistByiautoid(Kv kv) {
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
    }*/

    /*public List<Record> getonlyseelistByiautoid(Long iautoid) {
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
    }*/

    /**
     * 去零
     */
    public List<Record> clearZero(List<Record> recordList) {
        for (Record record : recordList) {
            if (record.getBigDecimal("istdval") != null) {
                record.set("istdval", record.getBigDecimal("istdval").stripTrailingZeros().toPlainString());
            }
            if (record.getBigDecimal("imaxval") != null) {
                record.set("imaxval", record.getBigDecimal("imaxval").stripTrailingZeros().toPlainString());
            }
            if (record.getBigDecimal("imaxval") != null) {
                record.set("iminval", record.getBigDecimal("iminval").stripTrailingZeros().toPlainString());
            }
        }
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
    /*public List<String> uploadImage(List<UploadFile> files) {
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
    }*/

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
        ValidationUtils.notBlank(isok, "请判定是否合格");

        Boolean result = achieveSerializeSubmitList(JboltPara.getJSONArray("serializeSubmitList"), docqcformmiautoid,
            JboltPara.getString("cmeasurepurpose"), JboltPara.getString("cmeasurereason"),
            JboltPara.getString("cmeasureunit"), JboltPara.getString("cmemo"),
            JboltPara.getString("cdcno"), isok);

        return ret(result);
    }

    /*
     * 实现编辑页面的SerializeSubmitList
     * */
    /*public Boolean achiveEditSerializeSubmitList(JSONArray serializeSubmitList, Long docqcformmiautoid, String cmeasurepurpose,
                                                 String cmeasurereason, String cmeasureunit, String cmemo, String cdcno,
                                                 String isok) {
        List<RcvdocqcformdLine> editRcvdocqcformdLines = new ArrayList<>();
        List<RcvdocqcformdLine> saveRcvdocqcformdLines = new ArrayList<>();
        boolean result = tx(() -> {
            Date now = new Date();
            for (int i = 0; i < serializeSubmitList.size(); i++) {
                JSONObject jsonObject = serializeSubmitList.getJSONObject(i);
                System.out.println("new Gson().toJson(jsonObject)====>" + new Gson().toJson(jsonObject));
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
            saveDocQcFormMModel(docQcFormM, cmeasurepurpose, cmeasurereason, cmeasureunit, cmemo, cdcno, now, isok);
            update(docQcFormM);
            return true;
        });
        return result;
    }*/

    /**
     * 给主表传参
     */
    public void saveDocQcFormMModel(RcvDocQcFormM docQcFormM, String cmeasurepurpose, String cmeasurereason,
                                    String cmeasureunit, String cmemo, String cdcno, Date now, String isok) {
        docQcFormM.setCMeasurePurpose(cmeasurepurpose);//测定目的
        docQcFormM.setCMeasureReason(cmeasurereason);//测定理由
        docQcFormM.setCMeasureUnit(cmeasureunit); //测定单位
        docQcFormM.setCMemo(cmemo);//备注
        docQcFormM.setCDcNo(cdcno); //设变号
        docQcFormM.setIsCpkSigned(false);
        docQcFormM.setIUpdateBy(JBoltUserKit.getUserId());
        docQcFormM.setDUpdateTime(now);
        docQcFormM.setCUpdateName(JBoltUserKit.getUserName());
        if (StrUtil.isNotBlank(isok)) {
            Integer isOk = Integer.valueOf(isok);
            docQcFormM.setIsOk(IsOkEnum.toEnum(isOk).getText());//是否合格
            docQcFormM.setIStatus(ObjUtil.equal(isOk, IsOkEnum.NO.getValue()) ? 2 : 3);
            docQcFormM.setIsCompleted(true);
        }
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

    public List<RcvDocQcFormM> findFirstBycRcvDocNo(String crcvdocno) {
        return find("select * from  PL_RcvDocQcFormM where cRcvDocNo = ? ", crcvdocno);
    }

    /*查询检验项目、检验参数*/
    public List<Map<String, Object>> findByIQcFormId(Long iqcformId) {
        List<Record> records = findRecords("SELECT * FROM Bd_QcFormTableParam WHERE iQcFormId = ?  ORDER BY iSeq ASC", iqcformId);
        List<QcFormTableItem> qcFormTableItemList = qcFormTableItemService.findByFormId(iqcformId);

        List<Map<String, Object>> mapList = new ArrayList<>();

        for (Record record : records) {
            Long id = record.getLong(QcFormTableParam.IAUTOID);
            for (QcFormTableItem qcFormTableItem : qcFormTableItemList) {
                // 校验当前id是否存在
                if (ObjUtil.equal(id, qcFormTableItem.getIQcFormTableParamId())) {
                    record.set(String.valueOf(qcFormTableItem.getIQcFormItemId()), qcFormTableItem.getIQcFormParamId());
                    record.set("iqcformtableparamid", id);
                }
            }
            mapList.add(record.getColumns());
        }
        return mapList;
    }

    /*参数项目名称：检查项目、规格公差、检查方法*/
    public List<Map<String, Object>> getTableHeadData(Long formId) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<Record> qcFormItemList = qcFormService.getItemCombinedListByPId(Kv.by("iqcformid", formId));
        List<Record> qcFormParamList = qcFormParamService.getQcFormParamListByPId(formId);
        Map<Long, List<Record>> itemParamByItemMap = qcFormParamList.stream()
            .collect(Collectors.groupingBy(obj -> obj.getLong("iqcitemid")));
        for (Record qcFormItemRecord : qcFormItemList) {
            Long qcItemId = qcFormItemRecord.getLong("iqcitemid");
            if (itemParamByItemMap.containsKey(qcItemId)) {
                List<Record> list = itemParamByItemMap.get(qcItemId);
                List<Map<String, Object>> maps = new ArrayList<>();

                for (Record itemRecord : list) {
                    maps.add(itemRecord.getColumns());
                }
                qcFormItemRecord.set("compares", maps);
            }
            mapList.add(qcFormItemRecord.getColumns());
        }
        return mapList;
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
        Record rcvDocQcFormMRecord = getCheckoutListByIautoId(iautoid);
        //测定目的
        String cMeasurePurpose = "";
        String[] split = rcvDocQcFormMRecord.getStr("cmeasurepurpose").split(",");
        for (int i = 0; i < split.length; i++) {
            if (StringUtils.isNotBlank(split[i])) {
                String text = CMeasurePurposeEnum.toEnum(Integer.valueOf(split[i])).getText();
                cMeasurePurpose += text + ",";
            }
        }
        rcvDocQcFormMRecord.set("cmeasurepurpose", StringUtils.isNotBlank(cMeasurePurpose)
            ? cMeasurePurpose.substring(0, cMeasurePurpose.lastIndexOf(",")) : cMeasurePurpose);
        //4、明细表数据
        List<RcvDocQcFormD> formDList = rcvDocQcFormDService.findByIRcvDocQcFormMId(iautoid);
        //5、如果cvalue的列数>10行，分多个页签
        List<Record> recordList = getCheckOutTableDatas(Kv.by("ircvdocqcformmid", iautoid));
        //核心业务逻辑，对列数进行分组
        rcvDocQcFormMRecord.set("columns", "");
        commPageMethod2(recordList, rcvDocQcFormMRecord, pages);

        return Kv.by("pages", pages).set("sheetNames", sheetNames);
    }

    /*来料检、出库检、在库检的公共导出方法*/
    public void commPageMethod2(List<Record> recordList, Object obj, List<SheetPage<Record>> pages) {
        int iseq = 1;
        List<List<Object>> partition = ListUtils.partition(objToList(recordList.get(0).getObject("cvaluelist")), 10);
        for (int i = 0; i < partition.size(); i++) {
            ArrayList<Record> records = new ArrayList<>();
            for (Record record : recordList) {
                List<List<Object>> partitionList = ListUtils.partition(objToList(record.getObject("cvaluelist")), 10);
                List<Object> objects = partitionList.get(i);
                List<String> cvaluelist = new ArrayList<>();

                Record childRecord = new Record();
                childRecord.set("cqcitemname", record.getStr(""));
                childRecord.set("cqcparamname", record.getStr(""));
                childRecord.set("imaxval", record.getStr("imaxval"));
                childRecord.set("iminval", record.getStr("iminval"));
                childRecord.set("istdval", record.getStr("istdval"));
                childRecord.set("itype", record.getStr("itype"));
                childRecord.set("options", record.getStr("options"));
                childRecord.set("seq", iseq);
                childRecord.set("cvaluelist", cvaluelist);
                childRecord.set("itemlist","paramnamelist");
                records.add(childRecord);
                //
                iseq++;

                //records有项目，每个项目有分组的列值，以10列为一组
                SheetPage<Record> page = new SheetPage<>();
                // sheet名称
                String sheetName = "sheet" + (i + 1);
                page.setSheetName(sheetName);
                // 主表
                page.setMaster(obj);
                // 明细
                page.setDetails(records);
                pages.add(page);
            }
        }
    }

    /*
     * 将object对象转为list
     * */
    public List<Object> objToList(Object obj) {
        List<Object> list = new ArrayList<Object>();
        if (obj instanceof ArrayList<?>) {
            for (Object o : (List<?>) obj) {
                list.add(o);
            }
            return list;
        }
        return null;
    }

}