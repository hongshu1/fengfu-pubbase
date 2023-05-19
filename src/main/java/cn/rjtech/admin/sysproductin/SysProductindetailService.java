package cn.rjtech.admin.sysproductin;

import cn.rjtech.util.ValidationUtils;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SysProductindetail;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 产成品入库单明细
 * @ClassName: SysProductindetailService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-08 09:58
 */
public class SysProductindetailService extends BaseService<SysProductindetail> {
    private final SysProductindetail dao = new SysProductindetail().dao();

    @Override
    protected SysProductindetail dao() {
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
     * @param TrackType      跟单类型
     * @param SourceBillType 来源单据类型
     * @param IsDeleted      删除状态：0. 未删除 1. 已删除
     * @return
     */
    public Page<SysProductindetail> getAdminDatas(int pageNumber, int pageSize, String TrackType, String SourceBillType, Boolean IsDeleted) {
        // 创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        // sql条件处理
        sql.eq("TrackType", TrackType);
        sql.eq("SourceBillType", SourceBillType);
        sql.eqBooleanToChar("IsDeleted", IsDeleted);
        // 排序
        sql.desc("AutoID");
        return paginate(sql);
    }

    /**
     * 保存
     *
     * @param sysProductindetail
     * @return
     */
    public Ret save(SysProductindetail sysProductindetail) {
        if (sysProductindetail == null || isOk(sysProductindetail.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        boolean success = sysProductindetail.save();
        return ret(success);
    }

    /**
     * 更新
     *
     * @param sysProductindetail
     * @return
     */
    public Ret update(SysProductindetail sysProductindetail) {
        if (sysProductindetail == null || notOk(sysProductindetail.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // 更新时需要判断数据存在
        SysProductindetail dbSysProductindetail = findById(sysProductindetail.getAutoID());
        if (dbSysProductindetail == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = sysProductindetail.update();
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysProductindetail 要删除的model
     * @param kv                 携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(SysProductindetail sysProductindetail, Kv kv) {
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysProductindetail model
     * @param kv                 携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkInUse(SysProductindetail sysProductindetail, Kv kv) {
        // 这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(SysProductindetail sysProductindetail, String column, Kv kv) {
        return null;
    }

    public List<Record> findEditTableDatas(Kv para) {
        ValidationUtils.notNull(para.getLong("masid"), JBoltMsg.PARAM_ERROR);
        List<Record> records = dbTemplate("sysproductin.dList", para).find();
        return records;
    }

    /**
     * 批量删除主从表
     */
    public Ret deleteRmRdByIds(String ids) {
        tx(() -> {
            String[] split = ids.split(",");
            for (String s : split) {
                updateColumn(s, "isdeleted", true);
            }
            return true;
        });
        return ret(true);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public Ret delete(Long id) {
        updateColumn(id, "isdeleted", true);
        return ret(true);
    }
}