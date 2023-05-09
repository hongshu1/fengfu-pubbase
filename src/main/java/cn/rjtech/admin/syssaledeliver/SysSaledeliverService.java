package cn.rjtech.admin.syssaledeliver;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SysSaledeliver;

/**
 * 销售出库
 * @ClassName: SysSaledeliverService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 13:05
 */
public class SysSaledeliverService extends BaseService<SysSaledeliver> {
    private final SysSaledeliver dao = new SysSaledeliver().dao();

    @Override
    protected SysSaledeliver dao() {
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
    public Page<SysSaledeliver> getAdminDatas(int pageNumber, int pageSize, String keywords, String SourceBillType, String BillType) {
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
     * @param sysSaledeliver
     * @return
     */
    public Ret save(SysSaledeliver sysSaledeliver) {
        if (sysSaledeliver == null || isOk(sysSaledeliver.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // if(existsName(sysSaledeliver.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysSaledeliver.save();
        if (success) {
            // 添加日志
            // addSaveSystemLog(sysSaledeliver.getAutoID(), JBoltUserKit.getUserId(), sysSaledeliver.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     *
     * @param sysSaledeliver
     * @return
     */
    public Ret update(SysSaledeliver sysSaledeliver) {
        if (sysSaledeliver == null || notOk(sysSaledeliver.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // 更新时需要判断数据存在
        SysSaledeliver dbSysSaledeliver = findById(sysSaledeliver.getAutoID());
        if (dbSysSaledeliver == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        // if(existsName(sysSaledeliver.getName(), sysSaledeliver.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysSaledeliver.update();
        if (success) {
            // 添加日志
            // addUpdateSystemLog(sysSaledeliver.getAutoID(), JBoltUserKit.getUserId(), sysSaledeliver.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysSaledeliver 要删除的model
     * @param kv             携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(SysSaledeliver sysSaledeliver, Kv kv) {
        // addDeleteSystemLog(sysSaledeliver.getAutoID(), JBoltUserKit.getUserId(),sysSaledeliver.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysSaledeliver model
     * @param kv             携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkInUse(SysSaledeliver sysSaledeliver, Kv kv) {
        // 这里用来覆盖 检测是否被其它表引用
        return null;
    }

}