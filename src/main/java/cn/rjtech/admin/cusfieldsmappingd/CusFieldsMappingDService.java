package cn.rjtech.admin.cusfieldsmappingd;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.cusfieldsmappingdcodingrule.CusfieldsmappingdCodingruleService;
import cn.rjtech.admin.cusfieldsmappingm.CusFieldsMappingMService;
import cn.rjtech.admin.formfield.FormFieldService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.CusFieldsMappingRuleEnum;
import cn.rjtech.model.momdata.CusFieldsMappingD;
import cn.rjtech.model.momdata.CusFieldsMappingM;
import cn.rjtech.util.AutoExcelUtil;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import net.fenghaitao.parameters.FieldSetting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    private FormFieldService formFieldService;
    @Inject
    private CusFieldsMappingMService cusFieldsMappingMService;
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
     * @param icusfieldsmappingmid
     * @param keywords             关键词
     * @param isEncoded            是否编码字段：0. 否 1. 是
     * @param isEnabled            是否启用：0. 否 1. 是
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Long icusfieldsmappingmid, String keywords, Boolean isEncoded, Boolean isEnabled) {
        // 创建sql对象
        Sql sql = selectSql()
                .select("d.*, f.cfieldname ")
                .from(table(), "d")
                .leftJoin(formFieldService.table(), "f", "d.iformfieldid = f.iautoid ")
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
        ValidationUtils.isTrue(notExistsDuplicate(cusFieldsMappingD.getICusFieldsMappingMid(), cusFieldsMappingD.getCFormFieldCode()), "字段重复错误");
    }

//    /**
//     * 导入映射
//     */
//    public Ret getImportDatas(File file) {
//
//        AutoExcelUtil.readExcel(file.getAbsolutePath(), )
//    }

    public List<FieldSetting> genFieldSettings(String cformatname) {
        // 根据指定的模板名，获取导入的字段
        CusFieldsMappingM m = cusFieldsMappingMService.findByCformatName(cformatname);
        ValidationUtils.notNull(m, "导入模板不存在");
        ValidationUtils.isTrue(!m.getIsDeleted(), "导入模板已被删除");
        
        // 获取导入字段列表
        List<CusFieldsMappingD> cusFieldsMappingDs = findByIcusFieldsMappingMid(m.getIAutoId());
        ValidationUtils.notEmpty(cusFieldsMappingDs, "导入字段列表未定义");

        List<FieldSetting> fieldSettings = new ArrayList<>();
        return fieldSettings;
    }

    private List<CusFieldsMappingD> findByIcusFieldsMappingMid(long iCusFieldsMappingMid) {
        return find(selectSql().eq(CusFieldsMappingD.ICUSFIELDSMAPPINGMID, iCusFieldsMappingMid).asc("iseq"));
    }
    
}