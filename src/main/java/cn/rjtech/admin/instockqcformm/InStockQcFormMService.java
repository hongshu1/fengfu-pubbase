package cn.rjtech.admin.instockqcformm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.base.config.JBoltConfig;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.instockdefect.InStockDefectService;
import cn.rjtech.admin.instockqcformd.InStockQcFormDService;
import cn.rjtech.admin.instockqcformdline.InstockqcformdLineService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventoryqcform.InventoryQcFormService;
import cn.rjtech.admin.qcform.QcFormService;
import cn.rjtech.admin.rcvdocqcformd.RcvDocQcFormDService;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService.ParamName;
import cn.rjtech.enums.CMeasurePurposeEnum;
import cn.rjtech.enums.IsOkEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.model.momdata.base.BaseInStockQcFormD;
import cn.rjtech.model.momdata.base.BaseInstockqcformdLine;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
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
    @Inject
    private InventoryService          inventoryService;
    @Inject
    private InventoryQcFormService    inventoryQcFormService;
    @Inject
    private RcvDocQcFormMService      rcvDocQcFormMService;
    @Inject
    private RcvDocQcFormDService      rcvDocQcFormDService;

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
        Page<Record> paginate = dbTemplate("instockqcformm.list", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
        return paginate;
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

    /*生成*/
    public Ret createTable(Long iautoid) {
        InStockQcFormM inStockQcFormM = findById(iautoid);
        if (null == inStockQcFormM) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }

        //check动作
        Long iInventoryId = inStockQcFormM.getIInventoryId();
        Inventory inventory = inventoryService.findById(iInventoryId);
        ValidationUtils.notNull(inventory, "存货编码不存在,无法生成单据！！！");

        InventoryQcForm inventoryQcForm = inventoryQcFormService.findByIInventoryId(iInventoryId);
        ValidationUtils.notNull(inventoryQcForm, inventory.getCInvCode() + "：没有在【质量建模-检验适用标准】维护标准表格，无法生成单据！！！");

        Long iQcFormId = null;
        if (inStockQcFormM.getIQcFormId() != null) {
            iQcFormId = inStockQcFormM.getIQcFormId();
        } else {
            iQcFormId = inventoryQcForm.getIQcFormId();
        }
        QcForm qcForm = qcFormService.findById(iQcFormId);
        ValidationUtils.notNull(qcForm, "【质量建模-质量表格设置】检验表格不存在");

        List<Kv> recordList = rcvDocQcFormMService.findByIQcFormId(iQcFormId);
        ValidationUtils.notEmpty(recordList, qcForm.getCQcFormName() + "：【质量建模-质量表格设置】没有维护需要检验的项目");

        if (!inventoryQcForm.getCTypeIds().contains("2")) {
            return fail(qcForm.getCQcFormName() + "：在【质量建模-检验适用标准】页面中没有来料检的检验类型");
        }

        ArrayList<InStockQcFormD> inStockQcFormDS = new ArrayList<>();
        for (Kv map : recordList) {
            InStockQcFormD inStockQcFormD = new InStockQcFormD();
            inStockQcFormD.setIAutoId(JBoltSnowflakeKit.me.nextId());
            inStockQcFormD.setIInStockQcFormMid(iautoid);
            inStockQcFormD.setIQcFormId(iQcFormId);
            Object iqcformtableparamid = map.get("iqcformtableparamid");
            inStockQcFormD.setIFormParamId(iqcformtableparamid != null ? Long.valueOf(iqcformtableparamid.toString()) : null);
            Object iseq = map.get("iseq");
            inStockQcFormD.setISeq(iseq != null ? strToInt(iseq) : null);
//            inStockQcFormD.setCQcFormParamIds(record.getStr("cQcFormParamIds"));
            Object itype = map.get("itype");
            inStockQcFormD.setIType(itype != null ? strToInt(itype) : null);
            inStockQcFormD.setIStdVal(map.getBigDecimal("istdval"));
            inStockQcFormD.setIMaxVal(map.getBigDecimal("imaxval"));
            inStockQcFormD.setIMinVal(map.getBigDecimal("iminval"));
            inStockQcFormD.setCOptions(StrUtil.toString(map.get("coptions")));
            inStockQcFormDS.add(inStockQcFormD);
        }
        inStockQcFormM.setIStatus(1);
        inStockQcFormM.setCUpdateName(JBoltUserKit.getUserName());
        inStockQcFormM.setDUpdateTime(new Date());
        inStockQcFormM.setIUpdateBy(JBoltUserKit.getUserId());
        inStockQcFormM.setCInvQcFormNo(JBoltSnowflakeKit.me.nextIdStr());//减压单号
        
        boolean result = tx(() -> {
            inStockQcFormDService.batchSave(inStockQcFormDS);
            //2、更新PL_RcvDocQcFormM检验结果(istatus)为“待检-1”
            ValidationUtils.isTrue(inStockQcFormM.update(), "生成单据失败！！！");
            return true;
        });

        return ret(result);
    }

    public Integer strToInt(Object obj) {
        return Integer.parseInt(obj.toString());
    }

    /*详情页面的table数据*/
    public List<Record> getTableDatas(Kv kv) {
        List<Record> recordList = rcvDocQcFormMService
            .clearZero(dbTemplate("instockqcformm.getInStockQcFormDByMasId", kv).find());
        for (Record record : recordList) {
            kv.set("iqcformid", record.get("iqcformid"));
            kv.set("iqcformtableparamid", record.get("iformparamid"));
            List<Record> qcFormTableItemList = dbTemplate("instockqcformm.getQcFormTableItemList", kv).find();

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
            List<Record> inStockQcFormDLineList = getInStockQcFormDLineList(record.get("iautoid"));
            if (!inStockQcFormDLineList.isEmpty()) {
                for (int i = 0; i < inStockQcFormDLineList.size(); i++) {
                    inStockQcFormDLineList.get(i).set("name", (i + 1));
                }
                record.set("cvaluelist", inStockQcFormDLineList);
            } else {
                record.set("cvaluelist", getCvaluelist(record.get("iautoid"), record.get("iseq")));
            }
        }
        return recordList;
    }

    public List<Record> getInStockQcFormDLineList(Long iinstockqcformdid) {
        return dbTemplate("instockqcformm.getInStockQcFormDLineList", Kv.by("iinstockqcformdid", iinstockqcformdid)).find();
    }

    /**
     * 根据iautoid查询数据,并跳到检验页面
     */
    public Record getCheckoutListByIautoId(Long iautoId) {
        return dbTemplate("instockqcformm.list", Kv.by("iautoId", iautoId)).findFirst();
    }

    public List<Record> getCvaluelist(Long iinstockqcformdid, int iseq) {
        ArrayList<Record> list = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            Record record = new Record();
            record.set("name", i);
            record.set("iinstockqcformdid", iinstockqcformdid);
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
        Long stockqcformmiautoid = JboltPara.getLong("iinstockqcformmid"); //主表id
        Boolean result = achiveSerializeSubmitList(JboltPara.getJSONArray("serializeSubmitList"), stockqcformmiautoid,
            JboltPara.getString("cmeasurepurpose"), JboltPara.getString("cmeasurereason"),
            JboltPara.getString("cmeasureunit"), JboltPara.getString("cmemo"),
            JboltPara.getString("cdcno"), JboltPara.getString("isok"));

        return ret(result);
    }

    /**
     * 在编辑页面点击确定
     */
    public Ret saveEditTable(JBoltPara JboltPara) {
        if (JboltPara == null || JboltPara.isEmpty()) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        Long instockqcformmiautoid = JboltPara.getLong("iinstockqcformmid"); //主表id

        JSONArray serializeSubmitList = JboltPara.getJSONArray("serializeSubmitList");
        Boolean result = achiveSerializeSubmitList(serializeSubmitList, instockqcformmiautoid,
            JboltPara.getString("cmeasurepurpose"), JboltPara.getString("cmeasurereason"),
            JboltPara.getString("cmeasureunit"), JboltPara.getString("cmemo"),
            JboltPara.getString("cdcno"), JboltPara.getString("isok"));

        return ret(result);
    }

    public Boolean achiveSerializeSubmitList(JSONArray serializeSubmitList, Long instockqcformmid, String cmeasurepurpose, String cmeasurereason, String cmeasureunit, String cmemo, String cdcno, String isok) {
        List<InstockqcformdLine> saveInstockLines = new ArrayList<>();
        List<InstockqcformdLine> updateInstockLines = new ArrayList<>();
        
        boolean tx = tx(() -> {
            for (int i = 0; i < serializeSubmitList.size(); i++) {
                JSONObject jsonObject = serializeSubmitList.getJSONObject(i);
                JSONArray cvaluelist = jsonObject.getJSONArray("cvaluelist");
                for (int j = 0; j < cvaluelist.size(); j++) {
                    JSONObject object = cvaluelist.getJSONObject(j);
                    Long iinstockqcformdid = object.getLong("iinstockqcformdid");
                    String cvalue = object.getString("cvalue");
                    Integer iseq = object.getInteger("iseq");
                    String name = object.getString("name");
                    Long iautoid = object.getLong("iautoid");
                    InstockqcformdLine instockqcformdLine = instockqcformdLineService.findById(iautoid);
                    if (instockqcformdLine != null) {
                        instockqcformdLine.setCValue(cvalue);
                        updateInstockLines.add(instockqcformdLine);
                    } else {
                        InstockqcformdLine saveLine = new InstockqcformdLine();
                        saveLine.setIAutoId(JBoltSnowflakeKit.me.nextId());
                        saveLine.setIInStockQcFormDid(iinstockqcformdid);
                        saveLine.setCValue(cvalue);
                        saveLine.setISeq(iseq);
                        saveInstockLines.add(saveLine);
                    }
                }
            }
            
            //保存line表
            if (!saveInstockLines.isEmpty()) {
                instockqcformdLineService.batchSave(saveInstockLines);
            }
            if (!updateInstockLines.isEmpty()) {
                instockqcformdLineService.batchUpdate(updateInstockLines);
            }

            //更新主表PL_InStockQcFormM
            InStockQcFormM inStockQcFormM = findById(instockqcformmid);
            saveInStockoutQcFormmModel(inStockQcFormM, cmeasurepurpose, cmeasurereason, cmeasureunit, cmemo, cdcno, isok);
            ValidationUtils.isTrue(inStockQcFormM.update(), "检验失败！！！");

            //判断是否生成在库检异常品记录
            saveInDefectModel(isok, instockqcformmid, inStockQcFormM);

            return true;
        });
        return tx;
    }

    /**
     * 给PL_InStockQcFormM传参数
     */
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
        if (StrUtil.isNotBlank(isok)) {
            Integer isOk = Integer.valueOf(isok);
            inStockQcFormM.setIsOk(IsOkEnum.toEnum(isOk).getText());
            inStockQcFormM.setIStatus(isOk.equals(IsOkEnum.NO.getValue()) ? 2 : 3);
            inStockQcFormM.setIsCompleted(true);
            inStockQcFormM.setIsCpkSigned(false);
        }
        inStockQcFormM.setIUpdateBy(JBoltUserKit.getUserId());
        inStockQcFormM.setDUpdateTime(new Date());
        inStockQcFormM.setCUpdateName(JBoltUserKit.getUserName());
    }

    /*
     * 保存异常品记录
     * */
    public void saveInDefectModel(String isok, Long instockqcformmiautoid, InStockQcFormM inStockQcFormM) {
        if (StrUtil.isBlank(isok)) {
            return;
        }
        Integer isOkValue = Integer.valueOf(isok);
        if (ObjUtil.equal(isOkValue, IsOkEnum.NO.getValue())) {
            InStockDefect defect = defectService.findDefectByiInStockQcFormMid(instockqcformmiautoid);
            if (null == defect) {
                InStockDefect inStockDefect = new InStockDefect();
                defectService.saveInStockOutDefectModel(inStockDefect, inStockQcFormM);
                ValidationUtils.isTrue(inStockDefect.save(), "生成在库异常记录失败！！！");
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
        String ids = CollUtil.join(collect, ",");
        boolean tx = tx(() -> {
            //删除从表
            inStockQcFormDService.deleteByIds(ids);
            //删除主表
            deleteById(iautoid);
            return true;
        });
        return ret(tx);
    }

    /*
     * 根据现品票查询数据
     * */
    public Record findDetailByBarcode(String cbarcode) {
        Record record = dbTemplate("instockqcformm.findDetailByBarcode", Kv.by("ccompletebarcode", cbarcode)).findFirst();
        ValidationUtils.notNull(record, cbarcode + "：不存在，请重新扫描");

        InStockQcFormM stockQcFormM = findByCBarcodeAndInvcode(cbarcode, record.getStr("iinventoryid"));
        ValidationUtils.isTrue(stockQcFormM == null, cbarcode + "：数据记录已存在，不需要新增！！！");
        if (StrUtil.isBlank(record.getStr("cinvcode1"))) {
            record.set("cinvcode1", "");
        }
        if (StrUtil.isBlank(record.getStr("iqty"))) {
            record.set("iqty", "");
        }
        if (StrUtil.isBlank(record.getStr("cinvname1"))) {
            record.set("cinvname1", "");
        }
        return record;
    }

    public InStockQcFormM findByCBarcodeAndInvcode(String cbarcode, String iinventoryid) {
        return findFirst("select * from PL_InStockQcFormM where cbarcode=? and iinventoryid=?", cbarcode, iinventoryid);
    }

    /*
     * @desc 新增数据
     * @param cbarcode：现品票
     * qty：数量
     * invcode：存货编码
     * cinvcode1：客户部番
     * cinvname1：部品名称
     * */
    public Ret saveInStockQcFormByCbarcode(String cbarcode, Integer iqty, String invcode, Long iinventoryid, String cdcno,
                                           String cMeasureReason, Long iqcformid) {
        User user = JBoltUserKit.getUser();
        Long userId = JBoltUserKit.getUserId();
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        InStockQcFormM inStockQcFormM = new InStockQcFormM();
        inStockQcFormM.setIAutoId(JBoltSnowflakeKit.me.nextId());
        inStockQcFormM.setIOrgId(getOrgId());
        inStockQcFormM.setCOrgCode(getOrgCode());
        inStockQcFormM.setCOrgName(getOrgName());
        inStockQcFormM.setCInvQcFormNo(String.valueOf(JBoltSnowflakeKit.me.nextId()));//检验单号

        Record record = inventoryQcFormService.findByQcFormId(iqcformid);
//        ValidationUtils.notNull(byQcFormId, "存货编码：" + invcode + " 没有在【质量建模-检验适用标准】维护标准表格，无法生成单据！！！");
        if (record != null){
            inStockQcFormM.setIQcFormId(iqcformid);
        }
        inStockQcFormM.setIInventoryId(iinventoryid);
        inStockQcFormM.setIQty(iqty);
        inStockQcFormM.setCBarcode(cbarcode);
        inStockQcFormM.setCBatchNo(sdf.format(now));//批次号
        inStockQcFormM.setIStatus(0);
        inStockQcFormM.setIsCpkSigned(false);
        inStockQcFormM.setIsCompleted(false);
        inStockQcFormM.setIQcUserId(userId);
        inStockQcFormM.setCDcNo(cdcno);//设变号
        inStockQcFormM.setCMeasureReason(cMeasureReason);
        inStockQcFormM.setICreateBy(userId);
        inStockQcFormM.setCCreateName(user.getName());
        inStockQcFormM.setDCreateTime(now);
        inStockQcFormM.setIUpdateBy(userId);
        inStockQcFormM.setCUpdateName(user.getName());
        inStockQcFormM.setDUpdateTime(now);
        inStockQcFormM.setIsDeleted(false);

        ValidationUtils.isTrue(inStockQcFormM.save(), "在库检验单据创建失败！！！");
        return SUCCESS;
    }

    /*
     * 获取导出数据
     * */
    public Kv getExportData(Long iautoid) throws IOException {
        //1、所有sheet
        List<SheetPage<Record>> pages = new ArrayList<>();
        //2、每个sheet的名字
        List<String> sheetNames = new ArrayList<>();
        //3、主表数据
        Record inStockQcFormMRecord = getCheckoutListByIautoId(iautoid);
        //测定目的
        StringBuilder cMeasurePurpose = new StringBuilder();
        String[] split = inStockQcFormMRecord.getStr("cmeasurepurpose").split(",");
        for (String s : split) {
            if (StrUtil.isNotBlank(s)) {
                String text = CMeasurePurposeEnum.toEnum(Integer.parseInt(s)).getText();
                cMeasurePurpose.append(text).append(",");
            }
        }
        inStockQcFormMRecord.set("cmeasurepurpose", StrUtil.isNotBlank(cMeasurePurpose.toString())
            ? cMeasurePurpose.substring(0, cMeasurePurpose.lastIndexOf(",")) : cMeasurePurpose.toString());
        //4、明细表数据
        List<Record> recordList = getTableDatas(Kv.by("iinstockqcformid", iautoid));
        List<Map<String, Object>> tableHeadData = rcvDocQcFormMService
            .getTableHeadData(inStockQcFormMRecord.getLong("iqcformid"));
        List<ParamName> columnNames = new ArrayList<>();
        for (int i = 0; i < tableHeadData.size(); i++) {
            Map<String, Object> map = tableHeadData.get(i);
            ParamName paramName = new ParamName();
            paramName.setSeq(i);
            paramName.setValue(StrUtil.toString((map.get("cqcitemname"))));
            columnNames.add(paramName);
        }
        inStockQcFormMRecord.set("columnNameList", columnNames);//项目列名

        byte[] imageBytes = null;
        if (StrUtil.isNotBlank(inStockQcFormMRecord.getStr("cpics"))) {
            String cpics = JBoltConfig.BASE_UPLOAD_PATH_PRE + StrUtil.SLASH + inStockQcFormMRecord.getStr("cpics");
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
        inStockQcFormMRecord.set("cpics", imageBytes == null ? "" : imageBytes);

        //5、如果cvalue的列数>10行，分多个页签，核心业务逻辑，对列数进行分组
        commonPageMethod(recordList, inStockQcFormMRecord, pages, sheetNames);
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

                List<InstockqcformdLine> instockqcformdLineList = new ArrayList<>();
                objects.stream().forEach(object -> {
                    InstockqcformdLine instockqcformdLine = JSON.parseObject(JSON.toJSONString(object), InstockqcformdLine.class);
                    instockqcformdLineList.add(instockqcformdLine);
                });
                List<String> cvaluelist = instockqcformdLineList
                    .stream()
                    .sorted(Comparator.comparing(BaseInstockqcformdLine::getISeq))
                    .map(BaseInstockqcformdLine::getCValue)
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