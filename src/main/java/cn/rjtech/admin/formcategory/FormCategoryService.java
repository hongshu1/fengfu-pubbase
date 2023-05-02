package cn.rjtech.admin.formcategory;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.form.FormService;
import cn.rjtech.model.momdata.FormCategory;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 系统设置-表单类别
 *
 * @ClassName: FormCategoryService
 * @author: Kephon
 * @date: 2023-05-01 22:03
 */
public class FormCategoryService extends BaseService<FormCategory> {

    private final FormCategory dao = new FormCategory().dao();

    @Inject
    private FormService formService;

    @Override
    protected FormCategory dao() {
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
     * @param keywords   关键词
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, String keywords) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //关键词模糊查询
        sql.like("cName", keywords);
        //排序
        sql.desc("iAutoId");
        Page<Record> page = paginateRecord(sql);
        if (CollUtil.isNotEmpty(page.getList())) {
            for (Record row : page.getList()) {
                if (row.getLong("ipid") > 0) {
                    row.set("cpname", getName(row.getLong("ipid")));
                }
            }
        }
        return page;
    }

    public String getName(long ipid) {
        return queryColumn(selectSql().select(FormCategory.CNAME).eq(FormCategory.IAUTOID, ipid));
    }

    /**
     * 保存
     */
    public Ret save(FormCategory formCategory) {
        if (formCategory == null || isOk(formCategory.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        Date now = new Date();

//        if (existsName(formCategory.getName())) {
//            return fail(JBoltMsg.DATA_SAME_NAME_EXIST);
//        }
        
        formCategory.setICreateBy(userId);
        formCategory.setCCreateName(userName);
        formCategory.setDCreateTime(now);
        formCategory.setIUpdateBy(userId);
        formCategory.setCUpdateName(userName);
        formCategory.setDUpdateTime(now);

        boolean success = formCategory.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(formCategory.getIAutoId(), JBoltUserKit.getUserId(), formCategory.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(FormCategory formCategory) {
        if (formCategory == null || notOk(formCategory.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        
        //更新时需要判断数据存在
        FormCategory dbFormCategory = findById(formCategory.getIAutoId());
        if (dbFormCategory == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        
        //if(existsName(formCategory.getName(), formCategory.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = formCategory.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(formCategory.getIAutoId(), JBoltUserKit.getUserId(), formCategory.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param formCategory 要删除的model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(FormCategory formCategory, Kv kv) {
        //addDeleteSystemLog(formCategory.getIAutoId(), JBoltUserKit.getUserId(),formCategory.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param formCategory model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    public String checkInUse(FormCategory formCategory, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    public List<FormCategory> getTreeDatas(String keywords) {
        Sql sql = selectSql().eq(FormCategory.ISDELETED, ZERO_STR);
        
        if (StrUtil.isNotBlank(keywords)) {
            sql.bracketLeft()
                    .like(FormCategory.CCODE, keywords)
                    .or()
                    .like(FormCategory.CNAME, keywords)
                    .bracketRight();
        }

        List<FormCategory> datas = find(sql);
        return convertToModelTree(datas, "iautoid", "ipid", (p) -> notOk(p.getIPid()));
    }

    public List<Record> getTreeDatas() {
        Sql sql = selectSql().eq(FormCategory.ISDELETED, ZERO_STR);
        
        List<Record> datas = findRecord(sql);

        List<Record> allDatas = new ArrayList<>();

        if (CollUtil.isNotEmpty(datas)) {
            allDatas.addAll(datas);
            
            for (Record data : datas) {
                allDatas.addAll(formService.getTreeDatas(data.getLong("iautoid")));
            }
        }
        return convertToRecordTree(allDatas, "iautoid", "ipid", (p) -> notOk(p.getLong("ipid")));
    }

    public List<JsTreeBean> getJsTreeDatas(int openLevel, String keywords) {
        Sql sql = selectSql()
                .select("iautoid AS id, ipid AS pid, ccode, cname, 1 AS itype ")
                .eq(FormCategory.ISDELETED, ZERO_STR);

        if (StrUtil.isNotBlank(keywords)) {
            sql.bracketLeft()
                    .like(FormCategory.CCODE, keywords)
                    .or()
                    .like(FormCategory.CNAME, keywords)
                    .bracketRight();
        }

        List<Record> datas = findRecord(sql);
        
        List<Record> allDatas = new ArrayList<>();
        
        if (CollUtil.isNotEmpty(datas)) {
            allDatas.addAll(datas);
            
            for (Record row : datas) {
                allDatas.addAll(formService.getDatasForJsTree(row.getLong("id")));
            }
        }
        return convertJsRecordTree(allDatas, null, openLevel, null, "cname", null,null, null,"所有表单", true);
    }
    
}