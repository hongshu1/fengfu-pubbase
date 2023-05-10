package cn.rjtech.admin.syssaledeliverplan;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SysSaledeliverplan;

/**
 * 销售出货(计划)
 * @ClassName: SysSaledeliverplanService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 10:01
 */
public class SysSaledeliverplanService extends BaseService<SysSaledeliverplan> {
    private final SysSaledeliverplan dao = new SysSaledeliverplan().dao();

    @Override
    protected SysSaledeliverplan dao() {
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
     * @param keywords       关键词
     * @param SourceBillType 来源类型;MO生产工单
     * @param BillType       业务类型
     * @return
     */
    public Page<SysSaledeliverplan> getAdminDatas(int pageNumber, int pageSize, String keywords, String SourceBillType, String BillType) {
        // 创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        // sql条件处理
        sql.eq("SourceBillType", SourceBillType);
        sql.eq("BillType", BillType);
        // 关键词模糊查询
        sql.like("ExchName", keywords);
        // 排序
        sql.desc("AutoID");
        return paginate(sql);
    }

    /**
     * 保存
     *
     * @param sysSaledeliverplan
     * @return
     */
    public Ret save(SysSaledeliverplan sysSaledeliverplan) {
        if (sysSaledeliverplan == null || isOk(sysSaledeliverplan.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // if(existsName(sysSaledeliverplan.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysSaledeliverplan.save();
        if (success) {
            // 添加日志
            // addSaveSystemLog(sysSaledeliverplan.getAutoID(), JBoltUserKit.getUserId(), sysSaledeliverplan.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     *
     * @param sysSaledeliverplan
     * @return
     */
    public Ret update(SysSaledeliverplan sysSaledeliverplan) {
        if (sysSaledeliverplan == null || notOk(sysSaledeliverplan.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // 更新时需要判断数据存在
        SysSaledeliverplan dbSysSaledeliverplan = findById(sysSaledeliverplan.getAutoID());
        if (dbSysSaledeliverplan == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        // if(existsName(sysSaledeliverplan.getName(), sysSaledeliverplan.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysSaledeliverplan.update();
        if (success) {
            // 添加日志
            // addUpdateSystemLog(sysSaledeliverplan.getAutoID(), JBoltUserKit.getUserId(), sysSaledeliverplan.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysSaledeliverplan 要删除的model
     * @param kv                 携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(SysSaledeliverplan sysSaledeliverplan, Kv kv) {
        // addDeleteSystemLog(sysSaledeliverplan.getAutoID(), JBoltUserKit.getUserId(),sysSaledeliverplan.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysSaledeliverplan model
     * @param kv                 携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkInUse(SysSaledeliverplan sysSaledeliverplan, Kv kv) {
        // 这里用来覆盖 检测是否被其它表引用
        return null;
    }

}