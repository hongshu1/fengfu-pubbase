package cn.rjtech.admin.formfield;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.config.DictionaryTypeKey;
import cn.rjtech.model.momdata.Form;
import cn.rjtech.model.momdata.FormField;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.template.stat.ast.For;

import java.util.List;

/**
 * 系统配置-表单字段
 *
 * @ClassName: FormFieldService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-29 20:18
 */
public class FormFieldService extends BaseService<FormField> {
    
    private final FormField dao = new FormField().dao();

    @Override
    protected FormField dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber    第几页
     * @param pageSize      每页几条数据
     * @param keywords      关键词
     * @param cFieldTypeSn  字段类型
     * @param isImportField 是否导入字段
     * @param isDeleted     删除状态
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, String keywords, String cFieldTypeSn, Boolean isImportField, Boolean isDeleted) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("cFieldTypeSn", cFieldTypeSn);
        sql.eqBooleanToChar("isImportField", isImportField);
        sql.eqBooleanToChar("isDeleted", isDeleted);
        //关键词模糊查询
        sql.like("cFieldName", keywords);
        //排序
        sql.desc("iAutoId");
        Page<Record> page = paginateRecord(sql);
        if (CollUtil.isNotEmpty(page.getList())) {
            for (Record row : page.getList()) {
                row.set("cfieldtypename", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.FIELD_TYPE, row.getStr("cfieldtypesn")));
            }
        }
        return page;
    }

    /**
     * 保存
     */
    public Ret save(FormField formField) {
        if (formField == null || isOk(formField.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(formField.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = formField.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(formField.getIAutoId(), JBoltUserKit.getUserId(), formField.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(FormField formField) {
        if (formField == null || notOk(formField.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        FormField dbFormField = findById(formField.getIAutoId());
        if (dbFormField == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(formField.getName(), formField.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = formField.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(formField.getIAutoId(), JBoltUserKit.getUserId(), formField.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param formField 要删除的model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(FormField formField, Kv kv) {
        //addDeleteSystemLog(formField.getIAutoId(), JBoltUserKit.getUserId(),formField.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param formField model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkInUse(FormField formField, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(FormField formField, String column, Kv kv) {
        //addUpdateSystemLog(formField.getIAutoId(), JBoltUserKit.getUserId(), formField.getName(),"的字段["+column+"]值:"+formField.get(column));
        /*
         switch(column){
         case "isImportField":
         break;
         }
         */
        return null;
    }

    public List<Record> getAutocompleteList(long iformid, String keywords, Integer limit) {
        Sql sql = selectSql()
                .eq(FormField.ISDELETED, ZERO_STR)
                .eq(FormField.IFORMID, iformid)
                .likeMulti(keywords, FormField.CFIELDCODE, FormField.CFIELDNAME)
                .page(1, limit);

        return findRecord(sql);
    }
    
}