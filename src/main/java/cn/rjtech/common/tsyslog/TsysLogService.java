package cn.rjtech.common.tsyslog;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.JBoltBaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.common.model.Log;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;

import java.sql.Connection;
import java.util.Date;

/**
 * @Author: lidehui
 * @Date: 2023/1/29 9:22
 * @Version: 1.0
 * @Desc:
 */
public class TsysLogService extends JBoltBaseService<Log> {
    private final Log dao = new Log().dao();

    @Override
    protected Log dao(){return dao;}

    /**
     * 保存
     *
     * @param log
     * @return
     */
    public Ret save(Log log) {
        if (log == null || isOk(log.getAutoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(log.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = log.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(log.getAutoid(), JBoltUserKit.getUserId(), log.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     *
     * @param log
     * @return
     */
    public Ret update(Log log) {
        if (log == null || notOk(log.getAutoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        Log dbLog = findById(log.getAutoid());
        if (dbLog == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = log.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(log.getAutoid(), JBoltUserKit.getUserId(), log.getName());
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
     * 检测是否可以删除
     *
     * @param itemrouting 要删除的model
     * @param kv          携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkCanDelete(Log log, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(log, kv);
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换isenabled属性
     */
    public Ret toggleIsenabled(Long id) {
        return toggleBoolean(id, "isEnabled");
    }

    /**
     * 切换istate属性
     */
    public Ret toggleIstate(Long id) {
        return toggleBoolean(id, "iState");
    }

    public void logSave(Log exchangetable)throws RuntimeException{
        exchangetable.setCreatedate(new Date());
        //dao.logsave(exchangetable);
    }

    public void saveLogInTx(Log log) {
        tx(Connection.TRANSACTION_READ_UNCOMMITTED, () -> {
            log.save();
            return true;
        });
    }
}
