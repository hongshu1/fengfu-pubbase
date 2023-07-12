package cn.rjtech.admin.rcvpland;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.RcvPlanD;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 出货管理-取货计划明细
 *
 * @ClassName: RcvPlanDService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-27 14:25
 */
public class RcvPlanDService extends BaseService<RcvPlanD> {

    private final RcvPlanD dao = new RcvPlanD().dao();

    @Override
    protected RcvPlanD dao() {
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
     */
    public Page<RcvPlanD> getAdminDatas(int pageNumber, int pageSize) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //排序
        sql.desc("iAutoId");
        return paginate(sql);
    }

    /**
     * 保存
     */
    public Ret save(RcvPlanD rcvPlanD) {
        if (rcvPlanD == null || isOk(rcvPlanD.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(rcvPlanD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = rcvPlanD.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(rcvPlanD.getIAutoId(), JBoltUserKit.getUserId(), rcvPlanD.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(RcvPlanD rcvPlanD) {
        if (rcvPlanD == null || notOk(rcvPlanD.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        RcvPlanD dbRcvPlanD = findById(rcvPlanD.getIAutoId());
        if (dbRcvPlanD == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(rcvPlanD.getName(), rcvPlanD.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = rcvPlanD.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(rcvPlanD.getIAutoId(), JBoltUserKit.getUserId(), rcvPlanD.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param rcvPlanD 要删除的model
     * @param kv       携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(RcvPlanD rcvPlanD, Kv kv) {
        //addDeleteSystemLog(rcvPlanD.getIAutoId(), JBoltUserKit.getUserId(),rcvPlanD.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param rcvPlanD model
     * @param kv       携带额外参数一般用不上
     */
    @Override
    public String checkInUse(RcvPlanD rcvPlanD, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    public List<Record> findEditTableDatas(Kv para) {
        ValidationUtils.notNull(para.getLong("rcvplanmid"), JBoltMsg.PARAM_ERROR);
        List<Record> records = dbTemplate("rcvplanm.dList", para).find();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (Record record : records) {
            if (record.get("crcvdate") != null) {
                record.set("crcvdate", format.format(record.getDate("crcvdate")));
            }
            if (record.get("crcvtime") != null) {
                record.set("crcvtime", format.format(record.getDate("crcvtime")));
            }
        }
        return records;
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        updateColumn(id, "isdeleted", true);
        return SUCCESS;
    }


    public Ret deleteByIdsRm(String ids) {
        String[] split = ids.split(",");
        for (String s : split) {
            updateColumn(s, "isdeleted", true);
        }
        return SUCCESS;
    }

    public List<RcvPlanD> findListByMasid(String iRcvPlanMid){
        return find("select * from SM_RcvPlanD where iRcvPlanMid=?",iRcvPlanMid);
    }

}