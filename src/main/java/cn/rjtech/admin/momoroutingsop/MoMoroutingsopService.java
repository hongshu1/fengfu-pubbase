package cn.rjtech.admin.momoroutingsop;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.moroutingconfig.MoMoroutingconfigService;
import cn.rjtech.model.momdata.MoMoroutingsop;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Service
 *
 * @ClassName: MoMoroutingsopService
 * @author: RJ
 * @date: 2023-05-20 11:49
 */
public class MoMoroutingsopService extends BaseService<MoMoroutingsop> {

    private final MoMoroutingsop dao = new MoMoroutingsop().dao();

    @Override
    protected MoMoroutingsop dao() {
        return dao;
    }

    @Inject
    private MoMoroutingconfigService moMoroutingconfigService;

    /**
     * 后台管理分页查询
     *
     * @param pageNumber
     * @param pageSize
     * @param kv
     * @return
     */
    public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
        Sql sql = selectSql().select("a.*").from(table(), "a").

                innerJoin(moMoroutingconfigService.table(), "b", "b.iAutoId=a.iMoRoutingConfigId").

                eq("a.iMoRoutingConfigId", kv.getStr("imoroutingconfigid")).

                orderBy("a.iautoid", true).page(pageNumber, pageSize);

        return paginateRecord(sql);
        //return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 保存
     *
     * @param moMoroutingsop
     * @return
     */
    public Ret save(MoMoroutingsop moMoroutingsop) {
        if (moMoroutingsop == null || isOk(moMoroutingsop.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(moMoroutingsop.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = moMoroutingsop.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(moMoroutingsop.getIautoid(), JBoltUserKit.getUserId(), moMoroutingsop.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     *
     * @param moMoroutingsop
     * @return
     */
    public Ret update(MoMoroutingsop moMoroutingsop) {
        if (moMoroutingsop == null || notOk(moMoroutingsop.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        MoMoroutingsop dbMoMoroutingsop = findById(moMoroutingsop.getIAutoId());
        if (dbMoMoroutingsop == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(moMoroutingsop.getName(), moMoroutingsop.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = moMoroutingsop.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(moMoroutingsop.getIautoid(), JBoltUserKit.getUserId(), moMoroutingsop.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     *
     * @param ids
     * @return
     */
    public Ret deleteByBatchIds(String ids) {
        return deleteByIds(ids, true);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public Ret delete(Long id) {
        return deleteById(id, true);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param moMoroutingsop 要删除的model
     * @param kv             携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(MoMoroutingsop moMoroutingsop, Kv kv) {
        //addDeleteSystemLog(moMoroutingsop.getIautoid(), JBoltUserKit.getUserId(),moMoroutingsop.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param moMoroutingsop 要删除的model
     * @param kv             携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkCanDelete(MoMoroutingsop moMoroutingsop, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(moMoroutingsop, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     *
     * @return
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }


    public List<Record> dataList(Kv kv) {
        Sql sql = selectSql().select("a.*").from(table(), "a").

                innerJoin(moMoroutingconfigService.table(), "b", "b.iAutoId=a.iMoRoutingConfigId").

                eq("a.iMoRoutingConfigId", kv.getStr("configid")).

                orderBy("a.iautoid", true);
        return  findRecord(sql);
    }

}