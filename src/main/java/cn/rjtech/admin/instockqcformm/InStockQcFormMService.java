package cn.rjtech.admin.instockqcformm;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.util.JBoltRealUrlUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.instockdefect.InStockDefectService;
import cn.rjtech.admin.instockqcformd.InStockQcFormDService;
import cn.rjtech.admin.instockqcformdline.InstockqcformdLineService;
import cn.rjtech.admin.qcform.QcFormService;
import cn.rjtech.enums.AppearanceEnum;
import cn.rjtech.enums.CMeasurePurposeEnum;
import cn.rjtech.enums.IsOkEnum;
import cn.rjtech.enums.OverallSizeEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.model.momdata.base.BaseInStockQcFormD;
import cn.rjtech.util.excel.SheetPage;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * 在库检 Service
 *
 * @ClassName: InStockQcFormMService
 * @author: RJ
 * @date: 2023-04-25 15:00
 */
public class InStockQcFormMService extends BaseService<InStockQcFormM> {

    private final InStockQcFormM dao = new InStockQcFormM().dao();

    @Inject
    private InStockDefectService      defectService;
    @Inject
    private InStockQcFormDService     inStockQcFormDService;
    @Inject
    private InstockqcformdLineService instockqcformdLineService;
    @Inject
    private QcFormService             qcFormService;

    @Override
    protected InStockQcFormM dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<InStockQcFormM> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /*
     * 查询主数据
     * */
    public Page<Record> pageList(Kv kv) {
        return dbTemplate("instockqcformm.list", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
    }

    /**
     * 保存
     */
    public Ret save(InStockQcFormM inStockQcFormM) {
        if (inStockQcFormM == null || isOk(inStockQcFormM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(inStockQcFormM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = inStockQcFormM.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(inStockQcFormM.getIautoid(), JBoltUserKit.getUserId(), inStockQcFormM.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(InStockQcFormM inStockQcFormM) {
        if (inStockQcFormM == null || notOk(inStockQcFormM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        InStockQcFormM dbInStockQcFormM = findById(inStockQcFormM.getIAutoId());
        if (dbInStockQcFormM == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(inStockQcFormM.getName(), inStockQcFormM.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = inStockQcFormM.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(inStockQcFormM.getIautoid(), JBoltUserKit.getUserId(), inStockQcFormM.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        return deleteByIds(ids, true);
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        return deleteById(id, true);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param inStockQcFormM 要删除的model
     * @param kv             携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(InStockQcFormM inStockQcFormM, Kv kv) {
        //addDeleteSystemLog(inStockQcFormM.getIautoid(), JBoltUserKit.getUserId(),inStockQcFormM.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param inStockQcFormM 要删除的model
     * @param kv             携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(InStockQcFormM inStockQcFormM, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(inStockQcFormM, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换iscompleted属性
     */
    public Ret toggleIsCompleted(Long id) {
        return toggleBoolean(id, "isCompleted");
    }

    /**
     * 切换isok属性
     */
    public Ret toggleIsOk(Long id) {
        return toggleBoolean(id, "isOk");
    }

    /**
     * 切换iscpksigned属性
     */
    public Ret toggleIsCpkSigned(Long id) {
        return toggleBoolean(id, "isCpkSigned");
    }

    /**
     * 切换isdeleted属性
     */
    public Ret toggleIsDeleted(Long id) {
        return toggleBoolean(id, "IsDeleted");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param inStockQcFormM 要toggle的model
     * @param column         操作的哪一列
     * @param kv             携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(InStockQcFormM inStockQcFormM, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(InStockQcFormM inStockQcFormM, String column, Kv kv) {
        //addUpdateSystemLog(inStockQcFormM.getIautoid(), JBoltUserKit.getUserId(), inStockQcFormM.getName(),"的字段["+column+"]值:"+inStockQcFormM.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param inStockQcFormM model
     * @param kv             携带额外参数一般用不上
     */
    @Override
    public String checkInUse(InStockQcFormM inStockQcFormM, Kv kv) {
        //这里用来覆盖 检测InStockQcFormM是否被其它表引用
        return null;
    }

    /**
     * 根据表格ID生成table
     */
    public Ret createTable(Long iautoid, String cqcformname) {
        InStockQcFormM inStockQcFormM = findById(iautoid);
        if (null == inStockQcFormM) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //1、根据表格ID查询数据
        Long iQcFormId = inStockQcFormM.getIQcFormId();//表格ID
        //2、质检表格是否存在
        QcForm qcForm = qcFormService.findById(iQcFormId);
        if (null == qcForm) {
            return fail(iQcFormId + "：质检表格不存在，无法生成在库检验表");
        }
        List<Record> recordList = dbTemplate("instockqcformm.getCheckoutList", Kv.by("iqcformid", iQcFormId)).find();
        if (recordList.isEmpty()) {
            return fail(cqcformname + "：没有检验项目，无法生成在库检验表");
        }
        ArrayList<InStockQcFormD> inStockQcFormDS = new ArrayList<>();
        boolean result = tx(() -> {
            for (Record record : recordList) {
                InStockQcFormD inStockQcFormD = new InStockQcFormD();//质量管理-在库检单行配置
                inStockQcFormD.setIAutoId(JBoltSnowflakeKit.me.nextId());
                inStockQcFormD.setIInStockQcFormMid(iautoid);//来料检id
                inStockQcFormD.setIQcFormId(iQcFormId);//检验表格ID
                inStockQcFormD.setIFormParamId(record.getLong("iqcformtableitemid"));//Bd_QcFormTableItem的iautoid
                inStockQcFormD.setISeq(record.get("iSeq"));
//                inStockQcFormD.setISubSeq(record.get("iSeq"));
                inStockQcFormD.setCQcFormParamIds(record.getStr("cQcFormParamIds"));
                inStockQcFormD.setIType(record.get("iType"));
                inStockQcFormD.setIStdVal(record.get("iStdVal"));
                inStockQcFormD.setIMaxVal(record.get("iMaxVal"));
                inStockQcFormD.setIMinVal(record.get("iMinVal"));
                inStockQcFormD.setCOptions(record.get("cOptions"));
                inStockQcFormDS.add(inStockQcFormD);
            }
            inStockQcFormDService.batchSave(inStockQcFormDS);

            //2、更新PL_RcvDocQcFormM检验结果(istatus)为“待检-1”
            inStockQcFormM.setIStatus(1);
            Ret ret = update(inStockQcFormM);
            return true;
        });
        return ret(result);
    }


    /**
     * 根据iautoid查询数据,并跳到检验页面
     */
    public Record getCheckoutListByIautoId(Long iautoId) {
        return dbTemplate("instockqcformm.list", Kv.by("iautoId", iautoId)).findFirst();
    }

    /**
     * 点击检验按钮，跳转到checkout页面后，自动加载table的数据
     */
    public List<Record> getCheckOutTableDatas(Kv kv) {
        List<Record> recordList = clearZero(dbTemplate("instockqcformm.findChecoutListByIformParamid", kv).find());
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
        Long stockqcformmiautoid = JboltPara.getLong("instockqcformmiautoid"); //主表id
        Boolean result = achiveChecOutSerializeSubmitList(JboltPara.getJSONArray("serializeSubmitList"), stockqcformmiautoid,
            JboltPara.getString("cmeasurepurpose"), JboltPara.getString("cmeasurereason"),
            JboltPara.getString("cmeasureunit"), JboltPara.getString("cmemo"),
            JboltPara.getString("cdcno"), JboltPara.getString("isok"));

        return ret(result);
    }

    /*
     * 实现检验页面的SerializeSubmitList
     * */
    public Boolean achiveChecOutSerializeSubmitList(JSONArray serializeSubmitList, Long instockqcformmiautoid,
                                                    String cmeasurepurpose, String cmeasurereason, String cmeasureunit,
                                                    String cmemo, String cdcno, String isok) {
        List<InstockqcformdLine> instockqcformdLines = new ArrayList<>();
        boolean result = tx(() -> {
            for (int i = 0; i < serializeSubmitList.size(); i++) {
                JSONObject jsonObject = serializeSubmitList.getJSONObject(i);
                Long instockoutqcformdid = jsonObject.getLong("iautoid");
                String iseq = jsonObject.getString("iseq");
                JSONArray serializeElement = jsonObject.getJSONArray("serializeElement");
                JSONArray elementList = serializeElement.getJSONArray(0);
                for (int j = 0; j < elementList.size(); j++) {
                    JSONObject object = elementList.getJSONObject(j);
                    String cvalue = object.getString("value");

                    InstockqcformdLine instockqcformdLine = new InstockqcformdLine();//质量管理-出库检明细列值表
                    saveInStockQcFormdLineModel(instockqcformdLine, instockoutqcformdid, iseq, cvalue);
                    instockqcformdLines.add(instockqcformdLine);
                }
            }
            //保存line
            if (!instockqcformdLines.isEmpty()) {
                instockqcformdLineService.batchSave(instockqcformdLines);
            }
            /*
             * 出库检表
             * 1.如果isok=0，代表不合格，将iStatus更新为2，isCompleted更新为1；
             * 2.如果isok=1，代表合格，将iStatus更新为3，isCompleted更新为1
             */
            InStockQcFormM inStockQcFormM = findById(instockqcformmiautoid);
            saveInStockoutQcFormmModel(inStockQcFormM, cmeasurepurpose,
                cmeasurereason, cmeasureunit, cmemo, cdcno, isok);
            update(inStockQcFormM);

            //isok=0，代表检验结果不合格，生成在库异常品记录
            saveInDefectModel(isok, instockqcformmiautoid, inStockQcFormM);
            return true;
        });
        return result;
    }

    public void saveInStockQcFormdLineModel(InstockqcformdLine instockqcformdLine, Long instockoutqcformdid,
                                            String iseq, String cvalue) {
        instockqcformdLine.setIAutoId(JBoltSnowflakeKit.me.nextId());
        instockqcformdLine.setIInStockQcFormDid(instockoutqcformdid);
        instockqcformdLine.setISeq(Integer.valueOf(iseq));
        instockqcformdLine.setCValue(cvalue);
    }

    public void saveInStockoutQcFormmModel(InStockQcFormM inStockQcFormM, String cmeasurepurpose, String cmeasurereason,
                                           String cmeasureunit, String cmemo, String cdcno, String isok) {
        //测定目的
        inStockQcFormM.setCMeasurePurpose(cmeasurepurpose);
        //测定理由
        inStockQcFormM.setCMeasureReason(cmeasurereason);
        //测定单位
        inStockQcFormM.setCMeasureUnit(cmeasureunit);
        inStockQcFormM.setCMemo(cmemo);
        //设变号
        inStockQcFormM.setCDcNo(cdcno);
        //是否合格
        Integer isOk = Integer.valueOf(isok);
        inStockQcFormM.setIsOk(IsOkEnum.toEnum(isOk).getText());
        inStockQcFormM.setIStatus(isOk.equals(IsOkEnum.NO.getValue()) ? 2 : 3);
        inStockQcFormM.setIsCompleted(true);
        inStockQcFormM.setIsCpkSigned(false);
    }

    /*
     * 保存异常品记录
     * */
    public void saveInDefectModel(String isok, Long instockqcformmiautoid, InStockQcFormM inStockQcFormM) {
        if (Integer.valueOf(isok).equals(IsOkEnum.NO.getValue())) {
            InStockDefect defect = defectService.findDefectByiInStockQcFormMid(instockqcformmiautoid);
            if (null == defect) {
                InStockDefect inStockDefect = new InStockDefect();
                defectService.saveInStockOutDefectModel(inStockDefect, inStockQcFormM);
                boolean save = inStockDefect.save();
            }
        }
    }

    /*
     * 删除在库检查表
     * */
    public Ret deleteCheckoutByIautoid(Long iautoid) {
        //1、删除
        List<InStockQcFormD> inStockQcFormDList = inStockQcFormDService.findByIInStockQcFormMid(iautoid);
        List<Long> collect = inStockQcFormDList.stream().map(BaseInStockQcFormD::getIAutoId).collect(Collectors.toList());
        String ids = StringUtils.join(collect, ",");
        Ret ret = inStockQcFormDService.deleteByIds(ids);
        //2、更新主表状态
        InStockQcFormM stockQcFormM = findById(iautoid);
        stockQcFormM.setIStatus(0);
        update(stockQcFormM);
        return ret;
    }

    /**
     * 跳转到onlysee页面
     */
    public List<Record> getonlyseelistByiautoid(Long iautoid) {
        Kv kv = new Kv();
        kv.set("iinstockqcformmid", iautoid);
        List<Record> clearRecordList = clearZero(dbTemplate("instockqcformm.getonlyseelistByiautoid", kv).find());

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
     * 点击检验时，进入弹窗自动加载table的数据
     */
    public List<Record> getonlyseelistByiautoid(Kv kv) {
        kv.set("iinstockqcformmid", kv.get("iautoid"));
        List<Record> recordList = dbTemplate("instockqcformm.getonlyseelistByiautoid", kv).find();
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

    /**
     * 在编辑页面点击确定
     */
    public Ret saveEditTable(JBoltPara JboltPara) {
        if (JboltPara == null || JboltPara.isEmpty()) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        Long instockqcformmiautoid = JboltPara.getLong("instockqcformmiautoid"); //主表id
        //是否合格不能为空
        if (StringUtils.isBlank(JboltPara.getString("isok"))) {
            return fail("请判定是否合格");
        }
        JSONArray serializeSubmitList = JboltPara.getJSONArray("serializeSubmitList");
        Boolean result = achiveEditSerializeSubmitList(serializeSubmitList, instockqcformmiautoid,
            JboltPara.getString("cmeasurepurpose"), JboltPara.getString("cmeasurereason"),
            JboltPara.getString("cmeasureunit"), JboltPara.getString("cmemo"),
            JboltPara.getString("cdcno"), JboltPara.getString("isok"));

        return ret(result);
    }

    /*
     * 实现编辑页面的serializeSubmitList
     * */
    public Boolean achiveEditSerializeSubmitList(JSONArray serializeSubmitList, Long instockqcformmiautoid,
                                                 String cmeasurepurpose, String cmeasurereason, String cmeasureunit,
                                                 String cmemo, String cdcno, String isok) {
        List<InstockqcformdLine> updateInstockqcformdLines = new ArrayList<>();
        List<InstockqcformdLine> saveInstockqcformdLines = new ArrayList<>();
        boolean result = tx(() -> {
            for (int i = 0; i < serializeSubmitList.size(); i++) {
                JSONObject jsonObject = serializeSubmitList.getJSONObject(i);
                String iseq = jsonObject.getString("iseq");
                Long instockoutqcformdid = jsonObject.getLong("iautoid");
                JSONArray cvaluelist = jsonObject.getJSONArray("cvaluelist");
                JSONArray serializeElement = jsonObject.getJSONArray("serializeElement");
                JSONArray elementList = serializeElement.getJSONArray(0);
                for (int j = 0; j < elementList.size(); j++) {
                    JSONObject object = elementList.getJSONObject(j);
                    String cvalue = object.getString("value");
                    JSONObject cvaluelistJSONObject = cvaluelist.getJSONObject(j);
                    Long lineiautoid = cvaluelistJSONObject.getLong("lineiautoid");
                    if (lineiautoid != null) {
                        InstockqcformdLine instockqcformdLine = instockqcformdLineService.findById(lineiautoid);//质量管理-来料检明细列值表
                        instockqcformdLine.setCValue(cvalue);
                        updateInstockqcformdLines.add(instockqcformdLine);
                    } else {
                        //如果没有，代表是新增页的数据
                        InstockqcformdLine instockqcformdLine = new InstockqcformdLine();//质量管理-出库检明细列值表
                        saveInStockQcFormdLineModel(instockqcformdLine, instockoutqcformdid, iseq, cvalue);
                        saveInstockqcformdLines.add(instockqcformdLine);
                    }
                }
            }
            //更新line
            if (!updateInstockqcformdLines.isEmpty()) {
                instockqcformdLineService.batchUpdate(updateInstockqcformdLines);
            }
            if (!saveInstockqcformdLines.isEmpty()) {
                instockqcformdLineService.batchSave(saveInstockqcformdLines);
            }
            InStockQcFormM stockQcFormM = findById(instockqcformmiautoid);
            saveInStockoutQcFormmModel(stockQcFormM, cmeasurepurpose, cmeasurereason, cmeasureunit, cmemo, cdcno, isok);
            update(stockQcFormM);
            //如果不合格，保存异常品单
            saveInDefectModel(isok, instockqcformmiautoid, stockQcFormM);

            return true;
        });
        return result;
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
        InStockQcFormM inStockQcFormM = findById(iautoid);
        //测定目的
        String cMeasurePurpose = "";
        String[] split = inStockQcFormM.getCMeasurePurpose().split(",");
        for (int i = 0; i < split.length; i++) {
            if (StringUtils.isNotBlank(split[i])) {
                String text = CMeasurePurposeEnum.toEnum(Integer.valueOf(split[i])).getText();
                cMeasurePurpose += text + ",";
            }
        }
        inStockQcFormM.setCMeasurePurpose(StringUtils.isNotBlank(cMeasurePurpose)
            ? cMeasurePurpose.substring(0, cMeasurePurpose.lastIndexOf(",")) : cMeasurePurpose);
        //4、明细表数据
        List<Record> recordList = getonlyseelistByiautoid(Kv.by("iautoid", iautoid));
        //5、如果cvalue的列数>10行，分多个页签
        Record data = recordList.get(0);
        //核心业务逻辑，对列数进行分组
        commonPageMethod(data, recordList, inStockQcFormM, pages);

        return Kv.by("pages", pages).set("sheetNames", sheetNames);
    }

    /*
     * 公共方法，在库检、来料检、出库检都可以调用
     * */
    public void commonPageMethod(Record data, List<Record> recordList, Object obj, List<SheetPage<Record>> pages) {
        List<List<Object>> partition = ListUtils.partition(objToList(data.getObject("cvaluelist")), 10);
        for (int i = 0; i < partition.size(); i++) {
            ArrayList<Record> records = new ArrayList<>();
            int iseq = 1;
            for (Record record : recordList) {
                List<List<Object>> partitionList = ListUtils.partition(objToList(record.getObject("cvaluelist")), 10);
                Record childRecord = new Record();
                List<Object> objects = partitionList.get(i);
                List<String> cvaluelist = new ArrayList<>();
                objects.stream().forEach(object -> {
                    JSONObject jsonObject = (JSONObject) JSON.toJSON(object);
                    /*
                     * 1、如果itype=3，是“整体尺寸”的OverallSizeEnum下拉框
                     * 2、如果itype=5，是“整体外观”的下拉框,多选框
                     * */
                    switch (jsonObject.getInteger("itype")) {
                        case 3:
                            if (checkCValueIsNotBlank(jsonObject.getString("cvalue"))) {
                                cvaluelist.add(OverallSizeEnum.toEnum(jsonObject.getInteger("cvalue")).getText());
                            } else {
                                cvaluelist.add(jsonObject.getString("cvalue"));
                            }
                            break;
                        case 5:
                            //多选框的话，把所有选中的值都查出来
                            String[] cvalues = jsonObject.getString("cvalue").split(",");
                            String cvalue = "";
                            for (int valueLength = 0; valueLength < cvalues.length; valueLength++) {
                                if (StringUtils.isNotBlank(cvalues[valueLength])) {
                                    String text = AppearanceEnum.toEnum(Integer.valueOf(cvalues[valueLength])).getText();
                                    cvalue += text + ",";
                                }
                            }
                            cvaluelist.add(StringUtils.isNotBlank(cvalue) ?
                                cvalue.substring(0, cvalue.lastIndexOf(",")) : cvalue);
                            break;
                        default:
                            cvaluelist.add(jsonObject.getString("cvalue"));
                            break;
                    }
                });
                childRecord.set("cqcitemname", record.get("cqcitemname"));
                childRecord.set("cqcparamname", record.get("cqcparamname"));
                childRecord.set("imaxval", record.get("imaxval"));
                childRecord.set("iminval", record.get("iminval"));
                childRecord.set("istdval", record.get("istdval"));
                childRecord.set("itype", record.get("itype"));
                if (record.getStr("itype").equals("1")) {
                    String coptions = "(" + record.getStr("imaxval") + ")" +
                        "(" + record.getStr("istdval") + ")" + "(" + record.getStr("iminval") + ")";
                    childRecord.set("options", coptions);
                } else {
                    childRecord.set("options", record.get("coptions"));
                }
                childRecord.set("seq", iseq);
                childRecord.set("cvaluelist", cvaluelist);
                records.add(childRecord);
                iseq = iseq + 1;
            }
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

    public boolean checkCValueIsNotBlank(String cvalue) {
        boolean isblank = false;
        if (StringUtils.isNotBlank(cvalue)) {
            return isblank = true;
        }
        return isblank;
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

    /*
     * @desc 扫描现品票，点击“确定”按钮，表体增加1行在库检任务；如果此存货没有配置检验项目，
     *       需维护相关设置后点击“生成”按钮，生成检查成绩表。
     * @param cbarcode：现品票
     * */
    public Ret createInStockQcFormByCbarcode(String cbarcode){
        Ret ret = new Ret();
        return ret;
    }
}