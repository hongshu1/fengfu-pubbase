package cn.rjtech.admin.form;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.datasource.JBoltTableMetaUtil;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.formfield.FormFieldService;
import cn.rjtech.enums.FormFieldEnum;
import cn.rjtech.model.momdata.Form;
import cn.rjtech.model.momdata.FormField;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.generator.ColumnMeta;
import com.jfinal.plugin.activerecord.generator.TableMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 表单管理 Service
 *
 * @ClassName: FormService
 * @author: RJ
 * @date: 2023-04-18 17:24
 */
public class FormService extends BaseService<Form> {

    private final Form dao = new Form().dao();

    @Inject
    private FormFieldService formFieldService;

    @Override
    protected Form dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, String keywords,Kv para) {

        Page<Record> list = dbTemplate("form.paginateAdminDatas").paginate(pageNumber,pageSize);
        for (Record row : list.getList()) {
            BigDecimal iformcategoryid = row.getBigDecimal("iformcategoryid");
            para.set("iatuoid",iformcategoryid);
            String Cname = dbTemplate("form.getFormCategoryByCname", para).queryStr();
            row.set("Cname",Cname);
        }
//        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "cFormCode,cFormName");
        return list;
    }

    /**
     * 保存
     */
    public Ret save(Form form) {
        if (form == null || isOk(form.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(form.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}

        String orgCode = getOrgCode();
        Long orgId = getOrgId();
        String orgName = getOrgName();
        User user = JBoltUserKit.getUser();
        Date date = new Date();

        form.setIOrgId(orgId);
        form.setCOrgCode(orgCode);
        form.setCOrgName(orgName);
        form.setICreateBy(user.getId());
        form.setCCreateName(user.getName());
        form.setDCreateTime(date);
        form.setIUpdateBy(user.getId());
        form.setCUpdateName(user.getName());
        form.setDUpdateTime(date);
        boolean success = form.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(form.getIautoid(), JBoltUserKit.getUserId(), form.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(Form form) {
        if (form == null || notOk(form.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        Form dbForm = findById(form.getIAutoId());
        if (dbForm == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(form.getName(), form.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        User user = JBoltUserKit.getUser();
        Date date = new Date();

        form.setIUpdateBy(user.getId());
        form.setCUpdateName(user.getName());
        form.setDUpdateTime(date);
        boolean success = form.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(form.getIautoid(), JBoltUserKit.getUserId(), form.getName());
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
     * @param form 要删除的model
     * @param kv   携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Form form, Kv kv) {
        // addDeleteSystemLog(form.getIautoid(), JBoltUserKit.getUserId(),form.getName())
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param form 要删除的model
     * @param kv   携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(Form form, Kv kv) {
        // 如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(form, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
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
     * @param form   要toggle的model
     * @param column 操作的哪一列
     * @param kv     携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(Form form, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(Form form, String column, Kv kv) {
        // addUpdateSystemLog(form.getIautoid(), JBoltUserKit.getUserId(), form.getName(),"的字段["+column+"]值:"+form.get(column))
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param form model
     * @param kv   携带额外参数一般用不上
     */
    @Override
    public String checkInUse(Form form, Kv kv) {
        //这里用来覆盖 检测Form是否被其它表引用
        return null;
    }

    public List<Record> getDatasForJsTree(long iformcategoryid) {
        Sql sql = selectSql()
                .select("iautoid AS id, " + iformcategoryid + " AS pid, cformcode AS ccode, cformname AS cname, 2 AS itype")
                .eq(Form.ISDELETED, ZERO_STR)
                .eq(Form.IFORMCATEGORYID, iformcategoryid);

        return findRecord(sql);
    }

    public List<Record> getTreeDatas(long iformcategoryid) {
        Sql sql = selectSql()
                .select("iautoid, " + iformcategoryid + " AS ipid, cformcode AS ccode, cformname AS cname, cCode as fieldcode")
                .eq(Form.ISDELETED, ZERO_STR)
                .eq(Form.IFORMCATEGORYID, iformcategoryid);

        return findRecord(sql);
    }

    public List<String> getNamesByIformids(List<Long> iautoids) {
        return query(selectSql().select(Form.CFORMNAME).eq(Form.ISDELETED, ZERO_STR).in(Form.IAUTOID, iautoids));
    }

    public String getNameByFormId(String formId) {
        Form form = findById(formId);
        return form == null ? null : form.getCFormName();
    }

    public Ret autoGen(Long iautoid) {
        Form form = findById(iautoid);
        ValidationUtils.notNull(form, JBoltMsg.DATA_NOT_EXIST);
        ValidationUtils.isTrue(!form.getIsDeleted(), JBoltMsg.DATA_NOT_EXIST);
        ValidationUtils.isTrue(formFieldService.notExists(iautoid), "表单已存在字段，若要重新生成，请清空后再生成");

        TableMeta tableMeta = JBoltTableMetaUtil.getTableMeta(dataSourceConfigName(), dbType(), form.getCFormCode(), false);
        ValidationUtils.notNull(tableMeta, "获取数据表元数据失败");

        List<ColumnMeta> columnMetas = tableMeta.columnMetas;
        ValidationUtils.notEmpty(columnMetas, "数据表字段不能为空");

        List<FormField> formFields = new ArrayList<>();
        
        for (ColumnMeta meta : columnMetas) {

            String colLow = meta.name.toLowerCase();
            
            // id字段跳过、状态
            if (colLow.endsWith("id") || colLow.endsWith("status") || colLow.endsWith("state")) {
                continue;
            }

            // 跳过已知字段
            switch (colLow) {
                case "corgcode":
                case "corgname":
                case "icreateby":
                case "iupdateby":
                case "ccreatename":
                case "cupdatename":
                case "dcreatetime":
                case "dupdatetime":
                case "isdeleted":
                    continue;
                default:
                    break;
            }

            String javaType = meta.javaType.toLowerCase();
            
            // 跳过指定类型
            switch (javaType) {
                case "java.lang.boolean":
                case "java.lang.long":
                    continue;
                default:
                     break;
            }

            FormField formField = new FormField()
                    .setIFormId(form.getIAutoId())
                    .setIAutoId(JBoltSnowflakeKit.me.nextId())
                    .setCFieldCode(meta.name)
                    .setCFieldName(meta.remarks)
                    .setIsImportField(false);

            switch (javaType) {
                case "java.lang.string":
                    formField.setCFieldTypeSn(FormFieldEnum.STRING.getValue());
                    break;
                case "java.lang.integer":
                case "java.lang.float":
                case "java.lang.double":
                case "java.math.bigdecimal":
                    formField.setCFieldTypeSn(FormFieldEnum.NUMBER.getValue());
                    break;
                case "java.util.date":
                    formField.setCFieldTypeSn(FormFieldEnum.DATE.getValue());
                    break;
                default:
                    break;
            }

            formFields.add(formField);
        }

        tx(() -> {

            formFieldService.batchSave(formFields);

            return true;
        });
        
        return SUCCESS;
    }

    public Form findByCformSn(String tableName) {
        Sql sql = selectSql().eq(Form.CFORMCODE, tableName).eq(Form.ISDELETED, ZERO_STR).first();
        return findFirst(sql);
    }
    
}
