package cn.rjtech.admin.cusfieldsmappingd;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.util.JBoltListMap;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.cusfieldsmappingdcodingrule.CusfieldsmappingdCodingruleService;
import cn.rjtech.admin.cusfieldsmappingform.CusfieldsmappingFormService;
import cn.rjtech.admin.cusfieldsmappingm.CusFieldsMappingMService;
import cn.rjtech.admin.form.FormService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.CusFieldsMappingRuleEnum;
import cn.rjtech.enums.CusfieldsMappingCharEnum;
import cn.rjtech.enums.SeparatorCharEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.AutoExcelUtil;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import net.fenghaitao.imports.DataSet;
import net.fenghaitao.parameters.FieldSetting;
import net.fenghaitao.parameters.ImportPara;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 系统配置-导入字段配置明细
 *
 * @ClassName: CusFieldsMappingDService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-02 13:41
 */
public class CusFieldsMappingDService extends BaseService<CusFieldsMappingD> {

    private final CusFieldsMappingD dao = new CusFieldsMappingD().dao();

    @Inject
    private FormService formService;
    @Inject
    private CusFieldsMappingMService cusFieldsMappingMService;
    @Inject
    private CusfieldsmappingFormService cusfieldsmappingFormService;
    @Inject
    private CusfieldsmappingdCodingruleService cusfieldsmappingdCodingruleService;

    @Override
    protected CusFieldsMappingD dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber           第几页
     * @param pageSize             每页几条数据
     * @param icusfieldsmappingmid 映射主表ID
     * @param keywords             关键词
     * @param isEncoded            是否转换编码：0. 否 1. 是
     * @param isEnabled            是否启用：0. 否 1. 是
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Long icusfieldsmappingmid, String keywords, Boolean isEncoded, Boolean isEnabled) {
        // 创建sql对象
        Sql sql = selectSql()
                .select("d.*")
                .from(table(), "d")
                .page(pageNumber, pageSize);

        sql.eq("d.icusfieldsmappingmid", icusfieldsmappingmid);

        // sql条件处理
        sql.eqBooleanToChar("d.isEncoded", isEncoded);
        sql.eqBooleanToChar("d.isEnabled", isEnabled);

        // 关键词模糊查询
        sql.like("d.cCusFieldName", keywords);

        // 排序
        sql.asc("d.iseq");

        Page<Record> page = paginateRecord(sql);
        if (CollUtil.isNotEmpty(page.getList())) {
            for (Record row : page.getList()) {
                row.set("cruletypename", CusFieldsMappingRuleEnum.toEnum(row.getInt("iruletype")).getText());
            }
        }
        return page;
    }

    /**
     * 保存
     */
    public Ret save(CusFieldsMappingD cusFieldsMappingD) {
        if (cusFieldsMappingD == null || isOk(cusFieldsMappingD.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        tx(() -> {
            // 获取序号
            int iseq = getMaxIseq(cusFieldsMappingD.getICusFieldsMappingMid());
            cusFieldsMappingD.setISeq(iseq);

            ValidationUtils.isTrue(cusFieldsMappingD.save(), ErrorMsg.SAVE_FAILED);

            return true;
        });

        // 添加日志
        // addSaveSystemLog(cusFieldsMappingD.getIAutoId(), JBoltUserKit.getUserId(), cusFieldsMappingD.getName())
        return successWithData(cusFieldsMappingD.keep("iautoid"));
    }

    /**
     * 更新
     */
    public Ret update(CusFieldsMappingD cusFieldsMappingD) {
        if (cusFieldsMappingD == null || notOk(cusFieldsMappingD.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        //更新时需要判断数据存在
        CusFieldsMappingD dbCusFieldsMappingD = findById(cusFieldsMappingD.getIAutoId());
        if (dbCusFieldsMappingD == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }

        // 非定制
        if (CusFieldsMappingRuleEnum.toEnum(cusFieldsMappingD.getIRuleType()) == CusFieldsMappingRuleEnum.NONE) {
            if (!cusFieldsMappingD.getIsEncoded()) {
                cusFieldsMappingD.setCDemo(StrUtil.EMPTY);
            }
        } else {
            cusFieldsMappingD.setIsEncoded(false);
            cusFieldsMappingD.setCDemo(StrUtil.EMPTY);
        }

        tx(() -> {
            ValidationUtils.isTrue(cusFieldsMappingD.update(), ErrorMsg.UPDATE_FAILED);

            return true;
        });

        //if(existsName(cusFieldsMappingD.getName(), cusFieldsMappingD.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}

        // 添加日志
        // addUpdateSystemLog(cusFieldsMappingD.getIAutoId(), JBoltUserKit.getUserId(), cusFieldsMappingD.getName())
        return successWithData(cusFieldsMappingD.keep("iautoid"));
    }

    /**
     * 删除数据后执行的回调
     *
     * @param cusFieldsMappingD 要删除的model
     * @param kv                携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(CusFieldsMappingD cusFieldsMappingD, Kv kv) {
        //addDeleteSystemLog(cusFieldsMappingD.getIAutoId(), JBoltUserKit.getUserId(),cusFieldsMappingD.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param cusFieldsMappingD model
     * @param kv                携带额外参数一般用不上
     */
    @Override
    public String checkInUse(CusFieldsMappingD cusFieldsMappingD, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(CusFieldsMappingD cusFieldsMappingD, String column, Kv kv) {
        //addUpdateSystemLog(cusFieldsMappingD.getIAutoId(), JBoltUserKit.getUserId(), cusFieldsMappingD.getName(),"的字段["+column+"]值:"+cusFieldsMappingD.get(column));
        /*
         switch(column){
         case "isEncoded":
         break;
         case "isEnabled":
         break;
         }
         */
        return null;
    }

    public Ret saveTableSubmit(JBoltTable jBoltTable) {
        ValidationUtils.notNull(jBoltTable, JBoltMsg.PARAM_ERROR);

        CusFieldsMappingD cusFieldsMappingD = jBoltTable.getFormModel(CusFieldsMappingD.class, "cusFieldsMappingD");
        ValidationUtils.notNull(cusFieldsMappingD, JBoltMsg.PARAM_ERROR);

        tx(() -> {

            // 新增
            if (null == cusFieldsMappingD.getIAutoId()) {
                doSave(cusFieldsMappingD, jBoltTable);
            }
            // 修改
            else {
                doUpdate(cusFieldsMappingD, jBoltTable);
            }

            return true;
        });

        return successWithData(cusFieldsMappingD.keep("iautoid"));
    }

    private int getMaxIseq(Long iCusFieldsMappingMid) {
        Integer iseq = queryInt(selectSql().select("MAX(iseq)").eq(CusFieldsMappingD.ICUSFIELDSMAPPINGMID, iCusFieldsMappingMid));
        return (null == iseq ? 0 : iseq) + 1;
    }

    private void doSave(CusFieldsMappingD cusFieldsMappingD, JBoltTable jBoltTable) {
        // 获取序号
        int iseq = getMaxIseq(cusFieldsMappingD.getICusFieldsMappingMid());
        cusFieldsMappingD.setISeq(iseq);

        ValidationUtils.isTrue(cusFieldsMappingD.save(), ErrorMsg.SAVE_FAILED);

        List<Record> save = jBoltTable.getSaveRecordList();
        ValidationUtils.notEmpty(save, "编码字段必须定义编码规则");

        doSaveCodingRules(cusFieldsMappingD, save);

        // 校验字段是否重复
        ValidationUtils.isTrue(notExistsDuplicate(cusFieldsMappingD.getICusFieldsMappingMid(), cusFieldsMappingD.getCFormFieldCode()), "字段重复错误");
    }

    private boolean notExistsDuplicate(Long iCusFieldsMappingMid, String cFormFieldCode) {
        Sql sql = selectSql().select(CusFieldsMappingD.CFORMFIELDCODE)
                .eq(CusFieldsMappingD.ICUSFIELDSMAPPINGMID, iCusFieldsMappingMid)
                .eq(CusFieldsMappingD.CFORMFIELDCODE, cFormFieldCode)
                .groupBy(CusFieldsMappingD.CFORMFIELDCODE)
                .having("COUNT(*) > 1");

        return CollUtil.isEmpty(query(sql));
    }

    private void doSaveCodingRules(CusFieldsMappingD cusFieldsMappingD, List<Record> save) {
        // 获取当前的最大序号
        int maxSeq = cusfieldsmappingdCodingruleService.getMaxIseq(cusFieldsMappingD.getIAutoId());

        for (Record row : save) {
            maxSeq++;

            row.set("iseq", maxSeq);

            row.keep("iseq", "itype", "cseparator", "ilength")
                    .set("iautoid", JBoltSnowflakeKit.me.nextId())
                    .set("icusfieldsmappingdid", cusFieldsMappingD.getIAutoId());
        }
        cusfieldsmappingdCodingruleService.batchSaveRecords(save);
    }

    private void doUpdate(CusFieldsMappingD cusFieldsMappingD, JBoltTable jBoltTable) {
        CusFieldsMappingD dbCusFieldsMappingD = findById(cusFieldsMappingD.getIAutoId());
        ValidationUtils.notNull(dbCusFieldsMappingD, JBoltMsg.DATA_NOT_EXIST);

        // 非定制
        if (CusFieldsMappingRuleEnum.toEnum(cusFieldsMappingD.getIRuleType()) == CusFieldsMappingRuleEnum.NONE) {
            if (!cusFieldsMappingD.getIsEncoded()) {
                cusFieldsMappingD.setCDemo(StrUtil.EMPTY);
            }
        } else {
            cusFieldsMappingD.setIsEncoded(false);
            cusFieldsMappingD.setCDemo(StrUtil.EMPTY);
        }

        ValidationUtils.isTrue(cusFieldsMappingD.update(), ErrorMsg.UPDATE_FAILED);

        // 删除
        if (ArrayUtil.isNotEmpty(jBoltTable.getDelete())) {
            cusfieldsmappingdCodingruleService.deleteByMultiIds(ArrayUtil.join(jBoltTable.getDelete(), COMMA));
        }

        // 新增
        List<Record> save = jBoltTable.getSaveRecordList();
        if (CollUtil.isNotEmpty(save)) {
            doSaveCodingRules(cusFieldsMappingD, save);
        }

        // 修改
        List<Record> update = jBoltTable.getUpdateRecordList();
        if (CollUtil.isNotEmpty(update)) {
            for (Record row : update) {
                row.keep("iseq", "itype", "cseparator", "ilength", "iautoid");
            }
            cusfieldsmappingdCodingruleService.batchUpdateRecords(update);
        }

        // 校验字段是否重复
        if (ObjUtil.notEqual(cusFieldsMappingD.getCFormFieldCode(), dbCusFieldsMappingD.getCFormFieldCode())) {
            ValidationUtils.isTrue(notExistsDuplicate(cusFieldsMappingD.getICusFieldsMappingMid(), cusFieldsMappingD.getCFormFieldCode()), "字段重复错误");
        }
    }

    /**
     * 根据上传的文件及表名获取导入的数据，适用在后端处理excel数据导入保存处理
     *
     * @param file      上传的文件
     * @param tableName 表名
     * @return 导入的数据
     */
    public List<Record> getImportRecordsByTableName(File file, String tableName) {
        Form form = formService.findByCformSn(tableName);
        ValidationUtils.notNull(form, "表单不存在");

        CusfieldsmappingForm cusfieldsmappingForm = cusfieldsmappingFormService.findFirstByIformId(form.getIAutoId());
        ValidationUtils.notNull(cusfieldsmappingForm, "导入配置记录不存在");

        CusFieldsMappingM cusFieldsMappingM = cusFieldsMappingMService.findById(cusfieldsmappingForm.getICusFieldMappingMid());
        LOG.info("{}", cusFieldsMappingM);

        return getImportRecords(file, cusFieldsMappingM);
    }

    /**
     * 根据上传的文件及导入格式名
     *
     * @param file              上传的文件
     * @param cusFieldsMappingM 格式名
     * @return 导入的数据
     */
    public List<Record> getImportRecords(File file, CusFieldsMappingM cusFieldsMappingM) {
        List<Map<String, Object>> rowDatas = getImportDataMaps(file, cusFieldsMappingM);

        List<Record> rows = new ArrayList<>();
        for (Map<String, Object> row : rowDatas) {
            rows.add(new Record().setColumns(row));
        }
        return rows;
    }

    /**
     * 获取导入的数据
     */
    public List<Map<String, Object>> getImportDataMaps(File file, CusFieldsMappingM cusFieldsMappingM) {
        // 获取导入字段列表
        List<CusFieldsMappingD> cusFieldsMappingDs = findByIcusFieldsMappingMid(cusFieldsMappingM.getIAutoId());
        ValidationUtils.notEmpty(cusFieldsMappingDs, "导入字段列表未定义");

        List<ImportPara> importParas = new ArrayList<ImportPara>() {{
            add(new ImportPara(0, genFieldSettings(cusFieldsMappingDs), 1, 2));
        }};

        DataSet dataSet = AutoExcelUtil.readExcel(file.getAbsolutePath(), importParas);

        Set<String> set = dataSet.getSheets();
        ValidationUtils.isTrue(set.size() == 1, "导入Excel的工作簿只能定义一个");

        String sheet = set.iterator().next();

        List<Map<String, Object>> rows = dataSet.get(sheet);

        ValidationUtils.notEmpty(rows, String.format("Excel工作簿 “%s”，导入数据不能为空", sheet));

        // 编码规则
        JBoltListMap<String, CusfieldsmappingdCodingrule> ruleMap = new JBoltListMap<>();

        // 检查是否存在编码转换的字段
        for (CusFieldsMappingD cusFieldsMappingD : cusFieldsMappingDs) {
            // 编码字段
            if (cusFieldsMappingD.getIsEncoded()) {

                List<CusfieldsmappingdCodingrule> rules = cusfieldsmappingdCodingruleService.findByIcusfieldsMappingDid(cusFieldsMappingD.getIAutoId());
                ValidationUtils.notEmpty(rules, "缺少编码转换规则");

                String filed = cusFieldsMappingD.getCFormFieldCode().toLowerCase();

                ruleMap.addItems(filed, rules);
            }
        }

        List<Map<String, Object>> rowDatas = new ArrayList<>();

        // 处理空行数据
        for (Map<String, Object> rowData : rows) {
            // 移除null的所有键值对
            MapUtil.removeNullValue(rowData);

            if (MapUtil.isNotEmpty(rowData)) {
                rowDatas.add(rowData);
            }
        }

        if (CollUtil.isNotEmpty(ruleMap)) {
            // 转换编码字段
            for (Map<String, Object> row : rowDatas) {
                // 遍历行数据
                for (Map.Entry<String, Object> entry : row.entrySet()) {

                    if (ruleMap.containsKey(entry.getKey()) && entry.getValue() instanceof String) {

                        List<CusfieldsmappingdCodingrule> rules = ruleMap.get(entry.getKey());

                        String value = (String) entry.getValue();
                        ValidationUtils.notBlank(value, String.format("编码字段“%s”不能为空", entry.getKey()));

                        int valueLength = value.length();

                        StringBuilder newCode = new StringBuilder();

                        int length = 0;

                        for (CusfieldsmappingdCodingrule rule : rules) {
                            switch (CusfieldsMappingCharEnum.toEnum(rule.getIType())) {
                                case CODE:
                                    newCode.append(value, length, Math.min(valueLength, length + rule.getILength()));
                                    length += rule.getILength();
                                    break;
                                case SEPARATOR:
                                    if (valueLength > length) {
                                        newCode.append(SeparatorCharEnum.toEnum(rule.getCSeparator()).getText());
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }

                        entry.setValue(newCode);
                    }
                }
            }
        }
        return rowDatas;
    }

    /**
     * 获取excel的导入数据，适用在前端需要读取excel文件的内容，不适用后台导入处理
     */
    public Ret getImportDatas(File file, String cformatname) {
        // 根据指定的模板名，获取导入的字段
        CusFieldsMappingM m = cusFieldsMappingMService.findByCformatName(cformatname);
        ValidationUtils.notNull(m, "导入模板不存在");
        ValidationUtils.isTrue(!m.getIsDeleted(), "导入模板已被删除");

        List<Map<String, Object>> rowDatas = getImportDataMaps(file, m);

        return successWithData(rowDatas);
    }

    /**
     * 导入字段映射处理
     */
    public List<FieldSetting> genFieldSettings(List<CusFieldsMappingD> cusFieldsMappingDs) {
        // -------------------------------------
        // 标准字段映射导入
        // -------------------------------------
        List<FieldSetting> fieldSettings = new ArrayList<>();
        for (CusFieldsMappingD d : cusFieldsMappingDs) {
            // 非定制规则字段
            if (CusFieldsMappingRuleEnum.toEnum(d.getIRuleType()) == CusFieldsMappingRuleEnum.NONE) {
                // 字段名（使用小写）、导入字段名
                fieldSettings.add(new FieldSetting(d.getCFormFieldCode().toLowerCase(), d.getCCusFieldName()));
            }
        }

        // -------------------------------------
        // 定制字段类型处理
        // -------------------------------------
        CusFieldsMappingD lastCusFieldsMappingD = cusFieldsMappingDs.get(cusFieldsMappingDs.size() - 1);

        CusFieldsMappingRuleEnum ruleType = CusFieldsMappingRuleEnum.toEnum(lastCusFieldsMappingD.getIRuleType());

        switch (ruleType) {
            case NONE:
            default:
                break;
            case ANNUAL:
                for (int i = 1; i <= 12; i++) {
                    fieldSettings.add(new FieldSetting("iqty" + i, i + "月"));
                }
                break;
            case MONTHLY:
                for (int i = 1; i <= 31; i++) {
                    fieldSettings.add(new FieldSetting("iqty" + i, i + "日"));
                }
                break;
            case WEEKLY:
                for (int i = 1; i <= 7; i++) {
                    fieldSettings.add(new FieldSetting("iqty" + i, i + "日"));
                }
                break;
        }

        return fieldSettings;
    }

    private List<CusFieldsMappingD> findByIcusFieldsMappingMid(long iCusFieldsMappingMid) {
        return find(selectSql().eq(CusFieldsMappingD.ICUSFIELDSMAPPINGMID, iCusFieldsMappingMid).asc("iseq"));
    }

}