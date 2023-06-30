package cn.rjtech.admin.stockoutqcformm;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.base.config.JBoltConfig;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.instockqcformm.InStockQcFormMService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventoryqcform.InventoryQcFormService;
import cn.rjtech.admin.qcform.QcFormService;
import cn.rjtech.admin.qcformparam.QcFormParamService;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService.ParamName;
import cn.rjtech.admin.stockoutdefect.StockoutDefectService;
import cn.rjtech.admin.stockoutqcformd.StockoutQcFormDService;
import cn.rjtech.admin.stockoutqcformdline.StockoutqcformdLineService;
import cn.rjtech.enums.CMeasurePurposeEnum;
import cn.rjtech.enums.IsOkEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.util.excel.SheetPage;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.collections4.ListUtils;
import org.jxls.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 质量管理-出库检
 *
 * @ClassName: StockoutQcFormMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-12 19:18
 */
public class StockoutQcFormMService extends BaseService<StockoutQcFormM> {

    private final StockoutQcFormM dao = new StockoutQcFormM().dao();

    @Inject
    private RcvDocQcFormMService       rcvDocQcFormMService;
    @Inject
    private StockoutqcformdLineService stockoutqcformdLineService;
    @Inject
    private StockoutQcFormDService     stockoutQcFormDService;
    @Inject
    private StockoutDefectService      stockoutDefectService; //出库检异常品单
    @Inject
    private InStockQcFormMService      inStockQcFormMService;
    @Inject
    private InventoryQcFormService     inventoryQcFormService;
    @Inject
    private InventoryService           inventoryService;
    @Inject
    private QcFormParamService         qcFormParamService;
    @Inject
    private QcFormService              qcFormService;

    @Override
    protected StockoutQcFormM dao() {
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
     */
    public Page<StockoutQcFormM> getAdminDatas(int pageNumber, int pageSize) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //排序
        sql.desc("id");
        return paginate(sql);
    }

    public Page<Record> pageList(Kv kv) {
        return dbTemplate("stockoutqcformm.list", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
    }

    /*
     * 生成
     * @param iautoid：主表id
     * */
    public Ret createTable(Long iautoid) {
        StockoutQcFormM stockoutQcFormM = findById(iautoid);
        if (null == stockoutQcFormM) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //1、生成的时候没有iqcformid，只有存货编码
        Long iInventoryId = stockoutQcFormM.getIInventoryId();
        Inventory inventory = inventoryService.findById(iInventoryId);
        ValidationUtils.notNull(inventory, "存货编码不存在,无法生成单据或检验单据！！！");

        InventoryQcForm inventoryQcForm = inventoryQcFormService.findByIInventoryId(iInventoryId);
        ValidationUtils.notNull(inventoryQcForm, inventory.getCInvCode() + "：没有在【质量建模-检验适用标准】维护标准表格");

        Long iQcFormId = null;
        if (stockoutQcFormM.getIQcFormId() != null) {
            iQcFormId = stockoutQcFormM.getIQcFormId();
        } else {
            iQcFormId = inventoryQcForm.getIQcFormId();
        }
        QcForm qcForm = qcFormService.findById(iQcFormId);
        ValidationUtils.notNull(qcForm, "【质量建模-质量表格设置】检验表格不存在");

        if (!inventoryQcForm.getCTypeIds().contains("3")) {
            return fail(qcForm.getCQcFormName() + "：在【质量建模-检验适用标准】页面中没有出货检检验类型");
        }

        List<Map<String, Object>> recordList = rcvDocQcFormMService.findByIQcFormId(iQcFormId);
        ValidationUtils.notEmpty(recordList, qcForm.getCQcFormName() + "：【质量建模-质量表格设置】没有维护需要检验的项目");

        //2、更新PL_RcvDocQcFormM检验结果(istatus)为“待检：1”
        stockoutQcFormM.setIQcFormId(inventoryQcForm.getIQcFormId());//bd_qcform的主键
        stockoutQcFormM.setIStatus(1);
        stockoutQcFormM.setCUpdateName(JBoltUserKit.getUserName());
        stockoutQcFormM.setDUpdateTime(new Date());
        stockoutQcFormM.setCStockoutQcFormNo(JBoltSnowflakeKit.me.nextIdStr());//检验单号
        stockoutQcFormM.setIUpdateBy(JBoltUserKit.getUserId());

        List<StockoutQcFormD> formDList = new ArrayList<>();
        for (Map<String, Object> map : recordList) {
            StockoutQcFormD qcFormD = new StockoutQcFormD();
            stockoutQcFormDService.saveStockQcFormDModel(qcFormD, iautoid, iQcFormId, map.get("iseq"),
                map.get("itype"), map.get("istdval"), map.get("imaxval"), map.get("iminval"),
                map.get("coptions"), map.get("iqcformtableparamid"));
            formDList.add(qcFormD);
        }

        tx(() -> {
            //更新主表状态
            ValidationUtils.isTrue(stockoutQcFormM.update(), "生成或检验失败！！！");
            //生成从表数据
            stockoutQcFormDService.batchSave(formDList);
            return true;
        });

        return ret(true);
    }

    /**
     * 根据iautoid查询数据,并跳到检验页面
     */
    public Record getCheckoutListByIautoId(Long iautoId) {
        return dbTemplate("stockoutqcformm.list", Kv.by("iautoId", iautoId)).findFirst();
    }

    public void checkAutoCreateStockoutQcFormD(Long iStockoutQcFormMid) {
        List<StockoutQcFormD> formDList = stockoutQcFormDService.findByIStockoutQcFormMid(iStockoutQcFormMid);
        if (formDList.isEmpty()) {
            createTable(iStockoutQcFormMid);
        }
    }

    /*
     * 加载详情table的数据
     * */
    public List<Record> getTableDatas(Kv kv) {
        List<Record> recordList = clearZero(dbTemplate("stockoutqcformm.getStockoutQcFormDByMasid", kv).find());
        for (Record record : recordList) {
            kv.set("iqcformid", record.get("iqcformid"));
            kv.set("iqcformtableparamid", record.get("iformparamid"));
            List<Record> qcFormTableItemList = dbTemplate("stockoutqcformm.getQcFormTableItemList", kv).find();

            List<Map<String, Object>> headData = rcvDocQcFormMService.getTableHeadData(record.getLong("iqcformid"));
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
            List<Record> stockQcFormDLineList = getStockQcFormDLineList(record.get("iautoid"));
            if (!stockQcFormDLineList.isEmpty()) {
                for (int i = 0; i < stockQcFormDLineList.size(); i++) {
                    stockQcFormDLineList.get(i).set("name", (i + 1));
                }
                record.set("cvaluelist", stockQcFormDLineList);
            } else {
                record.set("cvaluelist", getCvaluelist(record.get("iautoid"), record.get("iseq")));
            }
        }
        return recordList;
    }

    public List<Record> getStockQcFormDLineList(Long istockoutqcformdid) {
        return dbTemplate("stockoutqcformm.getStockQcFormDLineList", Kv.by("istockoutqcformdid", istockoutqcformdid)).find();
    }

    public List<Record> getCvaluelist(Long istockoutqcformdid, int iseq) {
        ArrayList<Record> list = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            Record record = new Record();
            record.set("name", i);
            record.set("istockoutqcformdid", istockoutqcformdid);
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
        Long istockoutqcformid = JboltPara.getLong("istockoutqcformid"); //主表id
        Boolean result = achieveSerializeSubmitList(JboltPara.getJSONArray("serializeSubmitList"), istockoutqcformid,
            JboltPara.getString("cmeasurepurpose"), JboltPara.getString("cmeasurereason"),
            JboltPara.getString("cmeasureunit"), JboltPara.getString("cmemo"),
            JboltPara.getString("cdcno"), JboltPara.getString("isok"), JboltPara.getString("cbatchno"));

        return ret(result);
    }

    /**
     * 在编辑页面点击确定
     */
    public Ret saveEditTable(JBoltPara JboltPara) {
        if (JboltPara == null || JboltPara.isEmpty()) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        Long istockoutqcformid = JboltPara.getLong("istockoutqcformid"); //主表id

        JSONArray serializeSubmitList = JboltPara.getJSONArray("serializeSubmitList");
        Boolean result = achieveSerializeSubmitList(serializeSubmitList, istockoutqcformid,
            JboltPara.getString("cmeasurepurpose"), JboltPara.getString("cmeasurereason"),
            JboltPara.getString("cmeasureunit"), JboltPara.getString("cmemo"),
            JboltPara.getString("cdcno"), JboltPara.getString("isok"), JboltPara.getString("cbatchno"));

        return ret(result);
    }

    public Boolean achieveSerializeSubmitList(JSONArray serializeSubmitList, Long istockoutqcformmid,
                                              String cmeasurepurpose, String cmeasurereason, String cmeasureunit,
                                              String cmemo, String cdcno, String isok, String cbatchno) {
        List<StockoutqcformdLine> updateStockoutqcformdLines = new ArrayList<>();
        List<StockoutqcformdLine> saveStockoutqcformdLines = new ArrayList<>();
        boolean tx = tx(() -> {
            for (int i = 0; i < serializeSubmitList.size(); i++) {
                JSONObject jsonObject = serializeSubmitList.getJSONObject(i);
                JSONArray cvaluelist = jsonObject.getJSONArray("cvaluelist");
                for (int j = 0; j < cvaluelist.size(); j++) {
                    JSONObject object = cvaluelist.getJSONObject(j);
                    Long istockoutqcformdid = object.getLong("istockoutqcformdid");
                    String cvalue = object.getString("cvalue");
                    Integer iseq = object.getInteger("iseq");
                    String name = object.getString("name");
                    Long iautoid = object.getLong("iautoid");
                    StockoutqcformdLine line = stockoutqcformdLineService.findById(iautoid);
                    if (line != null) {
                        line.setCValue(cvalue);
                        updateStockoutqcformdLines.add(line);
                    } else {
                        StockoutqcformdLine stockoutqcformdLine = new StockoutqcformdLine();
                        stockoutqcformdLine.setIAutoId(JBoltSnowflakeKit.me.nextId());
                        stockoutqcformdLine.setIStockoutQcFormDid(istockoutqcformdid);
                        stockoutqcformdLine.setCValue(cvalue);
                        stockoutqcformdLine.setISeq(iseq);
                        saveStockoutqcformdLines.add(stockoutqcformdLine);
                    }
                }
            }

            //保存或更新
            if (!saveStockoutqcformdLines.isEmpty()) {
                stockoutqcformdLineService.batchSave(saveStockoutqcformdLines);
            }
            if (!updateStockoutqcformdLines.isEmpty()) {
                stockoutqcformdLineService.batchUpdate(updateStockoutqcformdLines);
            }

            //更新主表
            StockoutQcFormM stockoutQcFormM = findById(istockoutqcformmid);
            stockoutQcFormM.setCBatchNo(cbatchno);
            saveStockoutQcFormmModel(stockoutQcFormM, cmeasurepurpose, cmeasurereason, cmeasureunit, cmemo, cdcno, isok);
            ValidationUtils.isTrue(stockoutQcFormM.update(), "检验失败！！！");

            //判断是否生成出货检异常品记录
            saveStockoutDefect(isok, istockoutqcformmid, stockoutQcFormM);
            return true;
        });
        return tx;
    }

    public void saveStockoutDefect(String isok, Long istockoutqcformmid, StockoutQcFormM stockoutQcFormM) {
        if (StrUtil.isBlank(isok)) {
            return;
        }
        Integer isOkValue = Integer.valueOf(isok);
        if (ObjUtil.equal(isOkValue, IsOkEnum.NO.getValue())) {
            StockoutDefect defect = stockoutDefectService.findStockoutDefectByiStockoutQcFormMid(istockoutqcformmid);
            if (null == defect) {
                StockoutDefect stockoutDefect = new StockoutDefect();
                stockoutDefectService.savestockoutDefectmodel(stockoutDefect, stockoutQcFormM);
                ValidationUtils.isTrue(stockoutDefect.save(), "生成出货检异常品记录失败！！！");
            }
        }
    }

    /**
     * 保存
     */
    public Ret save(StockoutQcFormM stockoutQcFormM) {
        if (stockoutQcFormM == null || isOk(stockoutQcFormM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        boolean success = stockoutQcFormM.save();
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(StockoutQcFormM stockoutQcFormM) {
        if (stockoutQcFormM == null || notOk(stockoutQcFormM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        StockoutQcFormM dbStockoutQcFormM = findById(stockoutQcFormM.getIAutoId());
        if (dbStockoutQcFormM == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = stockoutQcFormM.update();
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param stockoutQcFormM 要删除的model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(StockoutQcFormM stockoutQcFormM, Kv kv) {
        //addDeleteSystemLog(stockoutQcFormM.getId(), JBoltUserKit.getUserId(),stockoutQcFormM.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param stockoutQcFormM model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    public String checkInUse(StockoutQcFormM stockoutQcFormM, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * 给出库检主表传参
     */
    public void saveStockoutQcFormmModel(StockoutQcFormM stockoutQcFormM, String cmeasurepurpose,
                                         String cmeasurereason, String cmeasureunit, String cmemo, String cdcno, String isok) {
        //测定目的
        stockoutQcFormM.setCMeasurePurpose(cmeasurepurpose);
        //测定理由
        stockoutQcFormM.setCMeasureReason(cmeasurereason);
        //测定单位
        stockoutQcFormM.setCMeasureUnit(cmeasureunit);
        //备注
        stockoutQcFormM.setCMemo(cmemo);
        //设变号
        stockoutQcFormM.setCDcNo(cdcno);
        //是否合格
        if (StrUtil.isNotBlank(isok)) {
            Integer isOk = Integer.valueOf(isok);
            stockoutQcFormM.setIsOk(IsOkEnum.toEnum(isOk).getText());
            stockoutQcFormM.setIStatus(isOk.equals(IsOkEnum.NO.getValue()) ? 2 : 3);
            stockoutQcFormM.setIsCompleted(true);
            stockoutQcFormM.setIsCpkSigned(false);
        }
        stockoutQcFormM.setCUpdateName(JBoltUserKit.getUserName());
        stockoutQcFormM.setDUpdateTime(new Date());
        stockoutQcFormM.setIUpdateBy(JBoltUserKit.getUserId());
    }

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


    /*
     * 获取导出数据
     * */
    public Kv getExportData(Long iautoid) throws Exception{
        //1、所有sheet
        List<SheetPage<Record>> pages = new ArrayList<>();
        //2、每个sheet的名字
        List<String> sheetNames = new ArrayList<>();
        //3、主表数据
        Record stockoutrecord = getCheckoutListByIautoId(iautoid);
        //测定目的
        String cMeasurePurpose = "";
        String[] split = stockoutrecord.getStr("cmeasurepurpose").split(",");
        for (int i = 0; i < split.length; i++) {
            if (StrUtil.isNotBlank(split[i])) {
                String text = CMeasurePurposeEnum.toEnum(Integer.valueOf(split[i])).getText();
                cMeasurePurpose += text + ",";
            }
        }
        stockoutrecord.set("cmeasurepurpose", StrUtil.isNotBlank(cMeasurePurpose.toString())
            ? cMeasurePurpose.substring(0, cMeasurePurpose.lastIndexOf(",")) : cMeasurePurpose.toString());
        //4、明细表数据
        List<Record> recordList = getTableDatas(Kv.by("istockoutqcformmid", iautoid));
        List<Map<String, Object>> tableHeadData = rcvDocQcFormMService.getTableHeadData(stockoutrecord.get("iqcformid"));
        List<ParamName> columnNames = new ArrayList<>();
        for (int i = 0; i < tableHeadData.size(); i++) {
            Map<String, Object> map = tableHeadData.get(i);
            ParamName paramName = new ParamName();
            paramName.setSeq(i);
            paramName.setValue(StrUtil.toString((map.get("cqcitemname"))));
            columnNames.add(paramName);
        }
        stockoutrecord.set("columnNameList", columnNames);//项目列名

        byte[] imageBytes = null;
        if (StrUtil.isNotBlank(stockoutrecord.getStr("cpics"))) {
            String cpics = JBoltConfig.BASE_UPLOAD_PATH_PRE + StrUtil.SLASH + stockoutrecord.getStr("cpics");
            File file = new File(cpics);
            if (file.exists()) {
                File[] files = file.listFiles();
                for (File file1 : files) {
                    String name = file1.getName();
                    System.out.println(name);
                }
                FileInputStream fileInputStream = new FileInputStream(cpics);
                if (fileInputStream != null) {
                    imageBytes = Util.toByteArray(fileInputStream);
                }
            }
        }
        stockoutrecord.set("cpics", imageBytes == null ? "" : imageBytes);

        stockoutrecord.set("cpics", imageBytes);
        //5、如果cvalue的列数>10行，分多个页签，核心业务逻辑，对列数进行分组
        commonPageMethod(recordList, stockoutrecord, pages, sheetNames);
        return Kv.by("pages", pages).set("sheetNames", sheetNames);
    }

    /*来料检、出库检、在库检的公共导出方法*/
    public void commonPageMethod(List<Record> recordList, Object obj, List<SheetPage<Record>> pages, List<String> sheetNames) {
        List<List<Object>> partition = ListUtils.partition(objToList(recordList.get(0).getObject("cvaluelist")), 10);
        for (int i = 0; i < partition.size(); i++) {
            int iseq = 1;
            ArrayList<Record> records = new ArrayList<>();
            for (Record record : recordList) {
                List<List<Object>> partitionList = ListUtils.partition(objToList(record.getObject("cvaluelist")), 10);
                List<Object> objects = partitionList.get(i);

                List<StockoutqcformdLine> stockoutqcformdLines = new ArrayList<>();
                objects.stream().forEach(object -> {
                    StockoutqcformdLine stockoutqcformdLine = JSON.parseObject(JSON.toJSONString(object), StockoutqcformdLine.class);
                    stockoutqcformdLines.add(stockoutqcformdLine);
                });
                List<String> cvaluelist = stockoutqcformdLines
                    .stream()
                    .sorted(Comparator.comparing(StockoutqcformdLine::getISeq))
                    .map(StockoutqcformdLine::getCValue)
                    .collect(Collectors.toList());
                Record childRecord = new Record();
                childRecord.set("imaxval", record.getStr("imaxval"));
                childRecord.set("iminval", record.getStr("iminval"));
                childRecord.set("istdval", record.getStr("istdval"));
                childRecord.set("itype", record.getStr("itype"));
                childRecord.set("options", record.getStr("options"));
                childRecord.set("seq", iseq);
                childRecord.set("cvaluelist", cvaluelist);
                List<Object> paramnamelist = objToList(record.get("paramnamelist"));
                List<ParamName> paramnamelists = new ArrayList<>();
                for (int o1 = 0; o1 < paramnamelist.size(); o1++) {
                    ParamName paramName = new ParamName();
                    paramName.setSeq(o1);
                    paramName.setValue(StrUtil.toString(paramnamelist.get(o1)));
                    paramnamelists.add(paramName);
                }
                childRecord.set("paramnamelist", paramnamelists); //具体项目名称
                records.add(childRecord);
                //
                iseq++;
            }
            //records有项目，每个项目有分组的列值，以10列为一组
            SheetPage<Record> page = new SheetPage<>();
            // sheet名称
            String sheetName = "sheet" + (i + 1);
            page.setSheetName(sheetName);
            sheetNames.add(sheetName);
            // 主表
            page.setMaster(obj);
            // 明细
            page.setDetails(records);
            pages.add(page);
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