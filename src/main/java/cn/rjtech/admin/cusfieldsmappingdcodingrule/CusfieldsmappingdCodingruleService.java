package cn.rjtech.admin.cusfieldsmappingdcodingrule;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt._admin.dictionary.DictionaryTypeKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.CusfieldsmappingdCodingrule;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 系统设置-导入字段编码规则
 *
 * @ClassName: CusfieldsmappingdCodingruleService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-02 13:47
 */
public class CusfieldsmappingdCodingruleService extends BaseService<CusfieldsmappingdCodingrule> {
    
    private final CusfieldsmappingdCodingrule dao = new CusfieldsmappingdCodingrule().dao();

    @Override
    protected CusfieldsmappingdCodingrule dao() {
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
     * @param icusfieldsmappingdid
     * @param iType                编码字符类型：1. 编码 2. 分隔符
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Long icusfieldsmappingdid, Integer iType) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("iType", iType)
                .eq(CusfieldsmappingdCodingrule.ICUSFIELDSMAPPINGDID, icusfieldsmappingdid);
        
        //排序
        sql.asc("iseq");
        Page<Record> page = paginateRecord(sql);
        if (CollUtil.isNotEmpty(page.getList())) {
            for (Record row : page.getList()) {
                row.set("cname", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.encoding_field_type.name(), row.getStr("itype")));
                row.set("cchar", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.encoding_field_separator.name(), row.getStr("cseparator")));
            }
        }
        return page;
    }

    /**
     * 保存
     */
    public Ret save(CusfieldsmappingdCodingrule cusfieldsmappingdCodingrule) {
        if (cusfieldsmappingdCodingrule == null || isOk(cusfieldsmappingdCodingrule.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(cusfieldsmappingdCodingrule.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = cusfieldsmappingdCodingrule.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(cusfieldsmappingdCodingrule.getIAutoId(), JBoltUserKit.getUserId(), cusfieldsmappingdCodingrule.getName())
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(CusfieldsmappingdCodingrule cusfieldsmappingdCodingrule) {
        if (cusfieldsmappingdCodingrule == null || notOk(cusfieldsmappingdCodingrule.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        CusfieldsmappingdCodingrule dbCusfieldsmappingdCodingrule = findById(cusfieldsmappingdCodingrule.getIAutoId());
        if (dbCusfieldsmappingdCodingrule == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(cusfieldsmappingdCodingrule.getName(), cusfieldsmappingdCodingrule.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = cusfieldsmappingdCodingrule.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(cusfieldsmappingdCodingrule.getIAutoId(), JBoltUserKit.getUserId(), cusfieldsmappingdCodingrule.getName())
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param cusfieldsmappingdCodingrule 要删除的model
     * @param kv                          携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(CusfieldsmappingdCodingrule cusfieldsmappingdCodingrule, Kv kv) {
        //addDeleteSystemLog(cusfieldsmappingdCodingrule.getIAutoId(), JBoltUserKit.getUserId(),cusfieldsmappingdCodingrule.getName())
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param cusfieldsmappingdCodingrule model
     * @param kv                          携带额外参数一般用不上
     */
    @Override
    public String checkInUse(CusfieldsmappingdCodingrule cusfieldsmappingdCodingrule, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    public void deleteByMultiIds(String ids) {
        delete("DELETE FROM Bd_CusFieldsMappingD_CodingRule WHERE iautoid IN (" + ids + ")");
    }

    public int getMaxIseq(Long iCusFieldsMappingDid) {
        Integer maxSeq = queryInt(selectSql().select(CusfieldsmappingdCodingrule.ISEQ).eq(CusfieldsmappingdCodingrule.ICUSFIELDSMAPPINGDID, iCusFieldsMappingDid));
        return null == maxSeq ? 0 : maxSeq;
    }
    
}