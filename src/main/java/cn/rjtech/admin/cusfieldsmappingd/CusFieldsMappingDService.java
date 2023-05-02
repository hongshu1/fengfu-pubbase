package cn.rjtech.admin.cusfieldsmappingd;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.CusFieldsMappingD;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

/**
 * 系统配置-导入字段配置明细
 *
 * @ClassName: CusFieldsMappingDService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-02 13:41
 */
public class CusFieldsMappingDService extends BaseService<CusFieldsMappingD> {
    private final CusFieldsMappingD dao = new CusFieldsMappingD().dao();

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
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     * @param keywords   关键词
     * @param isEncoded  是否编码字段：0. 否 1. 是
     * @param isEnabled  是否启用：0. 否 1. 是
     */
    public Page<CusFieldsMappingD> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isEncoded, Boolean isEnabled) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eqBooleanToChar("isEncoded", isEncoded);
        sql.eqBooleanToChar("isEnabled", isEnabled);
        //关键词模糊查询
        sql.like("cCusFieldName", keywords);
        //排序
        sql.desc("iAutoId");
        return paginate(sql);
    }

    /**
     * 保存
     */
    public Ret save(CusFieldsMappingD cusFieldsMappingD) {
        if (cusFieldsMappingD == null || isOk(cusFieldsMappingD.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(cusFieldsMappingD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = cusFieldsMappingD.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(cusFieldsMappingD.getIAutoId(), JBoltUserKit.getUserId(), cusFieldsMappingD.getName());
        }
        return ret(success);
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
        //if(existsName(cusFieldsMappingD.getName(), cusFieldsMappingD.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = cusFieldsMappingD.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(cusFieldsMappingD.getIAutoId(), JBoltUserKit.getUserId(), cusFieldsMappingD.getName());
        }
        return ret(success);
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

}