package cn.rjtech.admin.codingruled;

import cn.jbolt._admin.dictionary.DictionaryTypeKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.form.FormService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.CodingRuleD;
import cn.rjtech.model.momdata.CusFieldsMappingD;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 系统设置-编码规则明细
 *
 * @ClassName: CodingRuleDService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-04 13:56
 */
public class CodingRuleDService extends BaseService<CodingRuleD> {

    private final CodingRuleD dao = new CodingRuleD().dao();

    @Inject
    private FormService formService;

    @Override
    protected CodingRuleD dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber     第几页
     * @param pageSize       每页几条数据
     * @param icodingrulemid 类型： 1. 手工输入 2. 流水号 3. 手工输入 4. 2位年 5. 2位月 6. 2位日
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Long icodingrulemid) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("icodingrulemid", icodingrulemid);
        //排序
        sql.desc("iAutoId");
        Page<Record> paginates = paginateRecord(sql);
        List<Record> list = paginates.getList();
        list.forEach(row -> {
            row.set("cseparatorname", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.encoding_field_separator.name(), row.getStr("cseparator")));
        });
        return paginates;
    }

    /**
     * 保存
     */
    public Ret save(CodingRuleD codingRuleD) {
        if (codingRuleD == null || isOk(codingRuleD.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(codingRuleD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = codingRuleD.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(codingRuleD.getIAutoId(), JBoltUserKit.getUserId(), codingRuleD.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(CodingRuleD codingRuleD) {
        if (codingRuleD == null || notOk(codingRuleD.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        CodingRuleD dbCodingRuleD = findById(codingRuleD.getIAutoId());
        if (dbCodingRuleD == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(codingRuleD.getName(), codingRuleD.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = codingRuleD.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(codingRuleD.getIAutoId(), JBoltUserKit.getUserId(), codingRuleD.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param codingRuleD 要删除的model
     * @param kv          携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(CodingRuleD codingRuleD, Kv kv) {
        //addDeleteSystemLog(codingRuleD.getIAutoId(), JBoltUserKit.getUserId(),codingRuleD.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param codingRuleD model
     * @param kv          携带额外参数一般用不上
     */
    @Override
    public String checkInUse(CodingRuleD codingRuleD, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    public void deleteByMultiIds(String ids) {
        delete("DELETE FROM Bd_CodingRuleD WHERE iautoid IN (" + ids + ")");
    }

}