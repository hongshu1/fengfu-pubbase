package cn.rjtech.admin.vouchtypedic;

import cn.jbolt._admin.dictionary.DictionaryTypeKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.VouchTypeDic;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

/**
 * 基础档案-单据业务类型字典
 *
 * @ClassName: VouchTypeDicService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-25 16:32
 */
public class VouchTypeDicService extends BaseService<VouchTypeDic> {
    
    private final VouchTypeDic dao = new VouchTypeDic().dao();

    @Override
    protected VouchTypeDic dao() {
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
     * @param cVTChName  单据类型中文名
     * @param cBTChName  业务类型中文名
     */
    public Page<VouchTypeDic> getAdminDatas(int pageNumber, int pageSize, String cVTChName, String cBTChName) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("cVTChName", cVTChName);
        sql.eq("cBTChName", cBTChName);
        //排序
        sql.desc("iAutoId");
        return paginate(sql);
    }

    /**
     * 保存
     */
    public Ret save(VouchTypeDic vouchTypeDic) {
        if (vouchTypeDic == null || isOk(vouchTypeDic.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        
        // 单据类型
        vouchTypeDic.setCVTChName(JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.VouchType.name(), vouchTypeDic.getCVTID()));
        // 单据业务
        vouchTypeDic.setCBTChName(JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.cbtchname.name(), vouchTypeDic.getCBTID()));

        Date now = new Date();
        String CVBTID = vouchTypeDic.getCVTID()+vouchTypeDic.getCBTID();

        VouchTypeDic vouchTypeDic1 = selectNotCvbtId(CVBTID);
        if (isOk(vouchTypeDic1)){
            ValidationUtils.error("添加失败，单据业务类型已存在");
        }
        vouchTypeDic.setCVBTID(CVBTID);
        vouchTypeDic.setICreateBy(JBoltUserKit.getUserId());
        vouchTypeDic.setCCreateName(JBoltUserKit.getUserName());
        vouchTypeDic.setDCreateTime(now);
        vouchTypeDic.setIUpdateBy(JBoltUserKit.getUserId());
        vouchTypeDic.setCUpdateName(JBoltUserKit.getUserName());
        vouchTypeDic.setDUpdateTime(now);
        vouchTypeDic.setISource(SourceEnum.MES.getValue());

        //if(existsName(vouchTypeDic.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = vouchTypeDic.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(vouchTypeDic.getIAutoId(), JBoltUserKit.getUserId(), vouchTypeDic.getName());
        }
        return ret(success);
    }

    private VouchTypeDic selectNotCvbtId(String cvbtid) {
        Kv para=Kv.by("cvbtid",cvbtid);
        return daoTemplate("vouchtypedic.selectNotCvbtId",para).findFirst();
    }

    /**
     * 更新
     */
    public Ret update(VouchTypeDic vouchTypeDic) {
        if (vouchTypeDic == null || notOk(vouchTypeDic.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        VouchTypeDic dbVouchTypeDic = findById(vouchTypeDic.getIAutoId());
        if (dbVouchTypeDic == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }

        String cvbtid = vouchTypeDic.getCVTID()+vouchTypeDic.getCBTID();
        List<Record> list = selectListCvbtid();
        for (Record row : list) {
            if (row.get("cvbtid").equals(cvbtid)){
                ValidationUtils.error("修改失败，单据业务类型已存在");
            }
        }
        // 单据类型
        vouchTypeDic.setCVTChName(JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.VouchType.name(), vouchTypeDic.getCVTID()));
        // 单据业务
        vouchTypeDic.setCBTChName(JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.cbtchname.name(), vouchTypeDic.getCBTID()));
        String CVBTID = vouchTypeDic.getCVTID()+vouchTypeDic.getCBTID();

        Date now =new Date();
        vouchTypeDic.setCVBTID(CVBTID);
        vouchTypeDic.setIUpdateBy(JBoltUserKit.getUserId());
        vouchTypeDic.setCUpdateName(JBoltUserKit.getUserName());
        vouchTypeDic.setDUpdateTime(now);
        boolean success = vouchTypeDic.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(vouchTypeDic.getIAutoId(), JBoltUserKit.getUserId(), vouchTypeDic.getName());
        }
        return ret(success);
    }

    private List<Record> selectListCvbtid() {
        return dbTemplate("vouchtypedic.selectListCvbtid").find();
    }


    /**
     * 删除数据后执行的回调
     *
     * @param vouchTypeDic 要删除的model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(VouchTypeDic vouchTypeDic, Kv kv) {
        //addDeleteSystemLog(vouchTypeDic.getIAutoId(), JBoltUserKit.getUserId(),vouchTypeDic.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param vouchTypeDic model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    public String checkInUse(VouchTypeDic vouchTypeDic, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    public List<Record> getOptionsList() {
        return dbTemplate("vouchtypedic.selectListCvbtid").find();
    }
    
}