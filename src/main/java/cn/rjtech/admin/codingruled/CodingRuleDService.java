package cn.rjtech.admin.codingruled;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt._admin.dictionary.DictionaryTypeKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.form.FormService;
import cn.rjtech.enums.CodingDTypeEnum;
import cn.rjtech.model.momdata.CodingRuleD;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
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
        
        if (ObjectUtil.isNull(icodingrulemid)){
            return emptyPage(pageSize);
        }
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("icodingrulemid", icodingrulemid);
        //排序
        sql.desc("iAutoId");
        Page<Record> paginates = paginateRecord(sql);
        List<Record> list = paginates.getList();
        list.forEach(row -> {
            String codingType = row.getStr("ccodingtype");
            if (codingType.equals(CodingDTypeEnum.serialNumberType.getValue())){
                row.set("ccodingtext","数量");
            }else if (codingType.equals(CodingDTypeEnum.yearType.getValue())){
                row.set("ccodingtext","yyyy");
            }else if (codingType.equals(CodingDTypeEnum.year2Type.getValue())){
                row.set("ccodingtext","yy");
            }else if (codingType.equals(CodingDTypeEnum.monthType.getValue())){
                row.set("ccodingtext","mm");
            }else if (codingType.equals(CodingDTypeEnum.dateType.getValue())){
                row.set("ccodingtext","dd");
            }
            row.set("cseparatorname", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.encoding_field_separator.name(), row.getStr("cseparator")));
            row.set("ccodingtypename", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.cCodingType.name(), codingType));
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
    
    
    public List<CodingRuleD> createCodingRuleList(Long codingRuleMid, List<Record> recordList){
        if (CollUtil.isEmpty(recordList)){
            return null;
        }
        List<CodingRuleD> codingRuleDList = new ArrayList<>();
        for (Record record :recordList){
            Long id = record.getLong(CodingRuleD.IAUTOID);
            String cCodingType = record.getStr(CodingRuleD.CCODINGTYPE);
            CodingDTypeEnum codingDTypeEnum = CodingDTypeEnum.toEnum(cCodingType);
            ValidationUtils.notNull(codingDTypeEnum, "未知编码配置细表类型");
            // 流水号
            CodingDTypeEnum serialNumberType = CodingDTypeEnum.serialNumberType;
            CodingDTypeEnum inputType = CodingDTypeEnum.inputType;
            CodingRuleD codingRuleD =null;
            
            String cCodingValue = record.getStr(CodingRuleD.CCODINGVALUE);
            String iBeginValue = record.getStr(CodingRuleD.IBEGINVALUE);
            String cSeparator = record.getStr(CodingRuleD.CSEPARATOR);
            Integer iLength = record.getInt(CodingRuleD.ILENGTH);
            Integer iseq = record.getInt(CodingRuleD.ISEQ);
            switch (codingDTypeEnum){
                case serialNumberType:
                    codingRuleD = create(id, codingRuleMid, iBeginValue, iLength, iseq, cCodingType, null, cSeparator);
                    break;
                case inputType:
                    codingRuleD =  create(id, codingRuleMid, null, null, iseq, cCodingType, cCodingValue, cSeparator);
                    break;
                default:
                    codingRuleD = create(id, codingRuleMid, null, null, iseq, cCodingType, null, cSeparator);
                    break;
            }
            codingRuleDList.add(codingRuleD);
        }
        return codingRuleDList;
    }
    
    public CodingRuleD create(Long id, Long iCodingRuleMid, String iBeginValue, Integer iLength, Integer seq, String cCodingType, String cCodingValue, String cSeparator){
        CodingRuleD codingRuleD = new CodingRuleD();
        if (ObjectUtil.isNotNull(id)){
            codingRuleD.setIAutoId(id);
        }
        codingRuleD.setICodingRuleMid(iCodingRuleMid);
        codingRuleD.setCCodingType(cCodingType);
        codingRuleD.setCCodingValue(cCodingValue);
        codingRuleD.setIBeginValue(iBeginValue);
        codingRuleD.setILength(iLength);
        codingRuleD.setISeq(seq);
        codingRuleD.setCSeparator(cSeparator);
        return codingRuleD;
    }
}
