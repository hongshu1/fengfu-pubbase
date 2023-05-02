package cn.rjtech.admin.cusfieldsmappingm;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.CusFieldsMappingM;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

/**
 * 系统配置-导入字段配置
 *
 * @ClassName: CusFieldsMappingMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-02 13:16
 */
public class CusFieldsMappingMService extends BaseService<CusFieldsMappingM> {
    
    private final CusFieldsMappingM dao = new CusFieldsMappingM().dao();

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
    public Page<CusFieldsMappingM> getAdminDatas(int pageNumber, int pageSize, String keywords, String cFormatName, Boolean isEnabled) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("cFormatName", cFormatName);
        sql.eqBooleanToChar("isEnabled", isEnabled);
        //关键词模糊查询
        sql.like("cFormatName", keywords);
        //排序
        sql.desc("iAutoId");
        return paginate(sql);
    }

    /**
     * 保存
     */
    public Ret save(CusFieldsMappingM cusFieldsMappingM) {
        if (cusFieldsMappingM == null || isOk(cusFieldsMappingM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(cusFieldsMappingM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = cusFieldsMappingM.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(cusFieldsMappingM.getIAutoId(), JBoltUserKit.getUserId(), cusFieldsMappingM.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(CusFieldsMappingM cusFieldsMappingM) {
        if (cusFieldsMappingM == null || notOk(cusFieldsMappingM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        CusFieldsMappingM dbCusFieldsMappingM = findById(cusFieldsMappingM.getIAutoId());
        if (dbCusFieldsMappingM == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(cusFieldsMappingM.getName(), cusFieldsMappingM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = cusFieldsMappingM.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(cusFieldsMappingM.getIAutoId(), JBoltUserKit.getUserId(), cusFieldsMappingM.getName());
        }
        return ret(success);
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

}