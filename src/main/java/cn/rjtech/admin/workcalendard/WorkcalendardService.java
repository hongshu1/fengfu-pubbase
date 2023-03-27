package cn.rjtech.admin.workcalendard;

import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.Workcalendard;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 工作日历明细 Service
 *
 * @ClassName: WorkcalendardService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-27 09:32
 */
public class WorkcalendardService extends BaseService<Workcalendard> {
    
    private final Workcalendard dao = new Workcalendard().dao();

    @Override
    protected Workcalendard dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     *
     * @param pageNumber
     * @param pageSize
     * @param keywords
     * @return
     */
    public Page<Workcalendard> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iautoid", "DESC", pageNumber, pageSize, keywords, "iautoid");
    }

    public List<Record> list(Kv kv) {
        return dbTemplate("workcalendard.paginateAdminDatas", kv).find();
    }

    /**
     * 保存
     *
     * @param workcalendard
     * @return
     */
    public Ret save(Workcalendard workcalendard) {
        if (workcalendard == null || isOk(workcalendard.getIautoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(workcalendard.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = workcalendard.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(workcalendard.getIautoid(), JBoltUserKit.getUserId(), workcalendard.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     *
     * @param workcalendard
     * @return
     */
    public Ret update(Workcalendard workcalendard) {
        if (workcalendard == null || notOk(workcalendard.getIautoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        Workcalendard dbWorkcalendard = findById(workcalendard.getIautoid());
        if (dbWorkcalendard == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(workcalendard.getName(), workcalendard.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = workcalendard.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(workcalendard.getIautoid(), JBoltUserKit.getUserId(), workcalendard.getName());
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
     * 删除数据后执行的回调
     *
     * @param workcalendard 要删除的model
     * @param kv            携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(Workcalendard workcalendard, Kv kv) {
        //addDeleteSystemLog(workcalendard.getIautoid(), JBoltUserKit.getUserId(),workcalendard.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param workcalendard 要删除的model
     * @param kv            携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkCanDelete(Workcalendard workcalendard, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(workcalendard, kv);
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

    public void deleteMultiByIds(Object[] deletes) {
        delete("DELETE FROM Bd_WorkCalendarD WHERE iautoid IN (" + ArrayUtil.join(deletes, COMMA) + ") ");
    }

}