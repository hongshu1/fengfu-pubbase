package cn.rjtech.admin.sysmaterialsprepare;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SysMaterialsprepare;
import com.jfinal.plugin.activerecord.Record;

/**
 * 材料备料表
 *
 * @ClassName: SysMaterialsprepareService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-12 18:28
 */
public class SysMaterialsprepareService extends BaseService<SysMaterialsprepare> {
    private final SysMaterialsprepare dao = new SysMaterialsprepare().dao();

    @Override
    protected SysMaterialsprepare dao() {
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
     * @param BillType   单据类型;MO 生产订单  SoDeliver销售发货单
     * @return
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("materialsprepare.recpor", kv).paginate(pageNumber, pageSize);
        return paginate;
    }

    /**
     * 保存
     *
     * @param sysMaterialsprepare
     * @return
     */
    public Ret save(SysMaterialsprepare sysMaterialsprepare) {
        if (sysMaterialsprepare == null || isOk(sysMaterialsprepare.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(sysMaterialsprepare.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysMaterialsprepare.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(sysMaterialsprepare.getAutoID(), JBoltUserKit.getUserId(), sysMaterialsprepare.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     *
     * @param sysMaterialsprepare
     * @return
     */
    public Ret update(SysMaterialsprepare sysMaterialsprepare) {
        if (sysMaterialsprepare == null || notOk(sysMaterialsprepare.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        SysMaterialsprepare dbSysMaterialsprepare = findById(sysMaterialsprepare.getAutoID());
        if (dbSysMaterialsprepare == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(sysMaterialsprepare.getName(), sysMaterialsprepare.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysMaterialsprepare.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(sysMaterialsprepare.getAutoID(), JBoltUserKit.getUserId(), sysMaterialsprepare.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysMaterialsprepare 要删除的model
     * @param kv                  携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(SysMaterialsprepare sysMaterialsprepare, Kv kv) {
        //addDeleteSystemLog(sysMaterialsprepare.getAutoID(), JBoltUserKit.getUserId(),sysMaterialsprepare.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysMaterialsprepare model
     * @param kv                  携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkInUse(SysMaterialsprepare sysMaterialsprepare, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

}