package cn.rjtech.admin.form;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.Form;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

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

    @Override
    protected Form dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<Form> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "cFormCode,cFormName");
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
                .select("iautoid, " + iformcategoryid + " AS ipid, cformcode AS ccode, cformname AS cname")
                .eq(Form.ISDELETED, ZERO_STR)
                .eq(Form.IFORMCATEGORYID, iformcategoryid);

        return findRecord(sql);
    }

    public List<String> getNamesByIformids(List<Long> iautoids) {
        return query(selectSql().select(Form.CFORMNAME).eq(Form.ISDELETED, ZERO_STR).in(Form.IAUTOID, iautoids));
    }

    public String getNameByFormId(String formId) {
        Form form = findById(formId);
        return form==null?null:form.getCFormName();
    }
    
}
