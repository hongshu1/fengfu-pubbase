package cn.rjtech.admin.cusfieldsmappingm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.cusfieldsmappingform.CusfieldsmappingFormService;
import cn.rjtech.admin.form.FormService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.CusFieldsMappingM;
import cn.rjtech.model.momdata.CusfieldsmappingForm;
import cn.rjtech.model.momdata.Form;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.hutool.core.text.CharPool.COMMA;

/**
 * 系统配置-导入字段配置
 *
 * @ClassName: CusFieldsMappingMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-02 13:16
 */
public class CusFieldsMappingMService extends BaseService<CusFieldsMappingM> {
    
    private final CusFieldsMappingM dao = new CusFieldsMappingM().dao();

    @Inject
    private FormService formService;
    @Inject
    private CusfieldsmappingFormService cusfieldsmappingFormService;

    @Override
    protected CusFieldsMappingM dao() {
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
     * @param cFormatName 格式名称
     * @param isEnabled   是否启用：0. 否 1. 是
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, String keywords, String cFormatName, Boolean isEnabled, String iformids) {
        // 创建sql对象
        Sql sql = selectSql()
                .select(getTableSelectColumnStrWithoutWithPre("m"))
                .from(table(), "m");

        if (StrUtil.isNotBlank(iformids)) {
            sql.innerJoin(cusfieldsmappingFormService.table(), "f", "m.iautoid = f.iCusFieldMappingMid ")
                    .in("f.iformid", iformids);
        }
        
        // sql条件处理
        sql.eq("m.cFormatName", cFormatName);
        sql.eqBooleanToChar("m.isEnabled", isEnabled);
        
        // 关键词模糊查询
        sql.like("m.cFormatName", keywords);
        // 排序
        sql.desc("m.iAutoId")
                .page(pageNumber, pageSize);
        
        Page<Record> page = paginateRecord(sql);
        if (CollUtil.isNotEmpty(page.getList())) {
            for (Record row : page.getList()) {
                row.set("cformnames", formService.getNamesByIformids(cusfieldsmappingFormService.getIformIdsByMid(row.getLong("iautoid"))));
            }
        }
        return page;
    }

    /**
     * 保存
     */
    public Ret save(CusFieldsMappingM cusFieldsMappingM, String iformids) {
        if (cusFieldsMappingM == null || isOk(cusFieldsMappingM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        ValidationUtils.notBlank(iformids, JBoltMsg.PARAM_ERROR);

        List<String> iformidList = StrSplitter.split(iformids, COMMA, true, true);
        for (String iformid : iformidList) {
            Form form = formService.findById(iformid);
            ValidationUtils.notNull(form, "表单参数不合法，请确认选项是否正确");
        }
        
        //if(existsName(cusFieldsMappingM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}

        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        Date now = new Date();

        cusFieldsMappingM.setICreateBy(userId);
        cusFieldsMappingM.setCCreateName(userName);
        cusFieldsMappingM.setDCreateTime(now);
        cusFieldsMappingM.setIUpdateBy(userId);
        cusFieldsMappingM.setCUpdateName(userName);
        cusFieldsMappingM.setDUpdateTime(now);

        tx(() -> {

            ValidationUtils.isTrue(cusFieldsMappingM.save(), ErrorMsg.SAVE_FAILED);

            for (String iformid : iformidList) {
                cusfieldsmappingFormService.save(cusFieldsMappingM.getIAutoId(), Long.parseLong(iformid));
            }
            
            return true;
        });

        // 添加日志
        // addSaveSystemLog(cusFieldsMappingM.getIAutoId(), JBoltUserKit.getUserId(), cusFieldsMappingM.getName()
        return SUCCESS;
    }

    /**
     * 更新
     */
    public Ret update(CusFieldsMappingM cusFieldsMappingM, String iformids) {
        if (cusFieldsMappingM == null || notOk(cusFieldsMappingM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        
        //更新时需要判断数据存在
        CusFieldsMappingM dbCusFieldsMappingM = findById(cusFieldsMappingM.getIAutoId());
        if (dbCusFieldsMappingM == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        
        tx(() -> {
            
            ValidationUtils.notBlank(iformids, "缺少表单参数");
            ValidationUtils.isTrue(cusFieldsMappingM.update(), ErrorMsg.UPDATE_FAILED);

            List<String> iformIdStrList = StrSplitter.split(iformids, COMMA, true, true);
            
            // 删除
            cusfieldsmappingFormService.deleteByIcusFieldsMappingMid(cusFieldsMappingM.getIAutoId());
            // 保存
            List<CusfieldsmappingForm> cusfieldsmappingForms = new ArrayList<>();
            
            for (String iformidStr : iformIdStrList) {
                CusfieldsmappingForm form = new CusfieldsmappingForm();

                form.setIFormId(Long.parseLong(iformidStr));
                form.setICusFieldMappingMid(cusFieldsMappingM.getIAutoId());

                cusfieldsmappingForms.add(form);
            }
            cusfieldsmappingFormService.batchSave(cusfieldsmappingForms);
            
            return true;
        });

        return SUCCESS;
    }

    /**
     * 删除数据后执行的回调
     *
     * @param cusFieldsMappingM 要删除的model
     * @param kv                携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(CusFieldsMappingM cusFieldsMappingM, Kv kv) {
        //addDeleteSystemLog(cusFieldsMappingM.getIAutoId(), JBoltUserKit.getUserId(),cusFieldsMappingM.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param cusFieldsMappingM model
     * @param kv                携带额外参数一般用不上
     */
    @Override
    public String checkInUse(CusFieldsMappingM cusFieldsMappingM, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(CusFieldsMappingM cusFieldsMappingM, String column, Kv kv) {
        //addUpdateSystemLog(cusFieldsMappingM.getIAutoId(), JBoltUserKit.getUserId(), cusFieldsMappingM.getName(),"的字段["+column+"]值:"+cusFieldsMappingM.get(column));
        /*
         switch(column){
         case "isEnabled":
         break;
         }
         */
        return null;
    }

    /**
     * 根据格式名 获取模板
     */
    public CusFieldsMappingM findByCformatName(String cformatname) {
        return findFirst(selectSql().eq(CusFieldsMappingM.IORGID, getOrgId()).eq(CusFieldsMappingM.CFORMATNAME, cformatname).first());
    }
    
}