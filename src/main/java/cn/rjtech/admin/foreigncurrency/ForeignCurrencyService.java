package cn.rjtech.admin.foreigncurrency;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.ForeignCurrency;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;

/**
 * 币种档案 Service
 *
 * @ClassName: ForeignCurrencyService
 * @author: WYX
 * @date: 2023-03-20 21:09
 */
public class ForeignCurrencyService extends BaseService<ForeignCurrency> {

    private final ForeignCurrency dao = new ForeignCurrency().dao();

    @Override
    protected ForeignCurrency dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
        para.set("iorgid", getOrgId());
        return dbTemplate("foreigncurrency.paginateAdminDatas", para).paginate(pageNumber, pageSize);
    }

    /**
     * 保存
     */
    public Ret save(ForeignCurrency foreignCurrency) {
        if (foreignCurrency == null || isOk(foreignCurrency.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        Date now = new Date();
        Long userid = JBoltUserKit.getUserId();
        String loginUserName = JBoltUserKit.getUserName();
        foreignCurrency.setIOrgId(getOrgId());
        foreignCurrency.setCOrgName(getOrgName());
        foreignCurrency.setCOrgCode(getOrgCode());
        foreignCurrency.setICreateBy(userid);
        foreignCurrency.setDCreateTime(now);
        foreignCurrency.setCCreateName(loginUserName);
        foreignCurrency.setIUpdateBy(userid);
        foreignCurrency.setDUpdateTime(now);
        foreignCurrency.setCUpdateName(loginUserName);
        foreignCurrency.setIsDeleted(false);
        foreignCurrency.setISource(SourceEnum.MES.getValue());
        boolean success = foreignCurrency.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(foreignCurrency.getIautoid(), JBoltUserKit.getUserId(), foreignCurrency.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(ForeignCurrency foreignCurrency) {
        if (foreignCurrency == null || notOk(foreignCurrency.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        ForeignCurrency dbForeignCurrency = findById(foreignCurrency.getIAutoId());
        if (dbForeignCurrency == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        foreignCurrency.setIUpdateBy(JBoltUserKit.getUserId());
        foreignCurrency.setCUpdateName(JBoltUserKit.getUserName());
        foreignCurrency.setDUpdateTime(new Date());
        boolean success = foreignCurrency.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(foreignCurrency.getIautoid(), JBoltUserKit.getUserId(), foreignCurrency.getName());
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
     * 逻辑删除
     */
    public Ret delete(Long id) {
        return updateColumn(id, "isdeleted", true);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param foreignCurrency 要删除的model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(ForeignCurrency foreignCurrency, Kv kv) {
        //addDeleteSystemLog(foreignCurrency.getIautoid(), JBoltUserKit.getUserId(),foreignCurrency.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param foreignCurrency 要删除的model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(ForeignCurrency foreignCurrency, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(foreignCurrency, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换bcal属性
     */
    public Ret toggleBcal(Long id) {
        return toggleBoolean(id, "bcal");
    }

    /**
     * 切换iotherused属性
     */
    public Ret toggleIotherused(Long id) {
        return toggleBoolean(id, "iotherused");
    }

    /**
     * 切换isdeleted属性
     */
    public Ret toggleIsDeleted(Long id) {
        return toggleBoolean(id, "isDeleted");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param foreignCurrency 要toggle的model
     * @param column          操作的哪一列
     * @param kv              携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(ForeignCurrency foreignCurrency, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(ForeignCurrency foreignCurrency, String column, Kv kv) {
        //addUpdateSystemLog(foreignCurrency.getIautoid(), JBoltUserKit.getUserId(), foreignCurrency.getName(),"的字段["+column+"]值:"+foreignCurrency.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param foreignCurrency model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    public String checkInUse(ForeignCurrency foreignCurrency, Kv kv) {
        //这里用来覆盖 检测ForeignCurrency是否被其它表引用
        return null;
    }

}