package cn.rjtech.admin.sysotherin;

import cn.rjtech.util.ValidationUtils;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SysOtherindetail;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 其它入库单明细
 * @ClassName: SysOtherindetailService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-05 17:57
 */
public class SysOtherindetailService extends BaseService<SysOtherindetail> {
    private final SysOtherindetail dao = new SysOtherindetail().dao();

    @Override
    protected SysOtherindetail dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     * @param pageNumber      第几页
     * @param pageSize        每页几条数据
     * @param keywords        关键词
     * @param TrackType       跟单类型
     * @param SourceBillType  来源单据类型
     * @param projectTypeCode 项目大类编码
     * @param projectTypeName 项目大类名称
     * @return
     */
    public Page<SysOtherindetail> getAdminDatas(int pageNumber, int pageSize, String keywords, String TrackType, String SourceBillType, String projectTypeCode, String projectTypeName) {
        // 创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        // sql条件处理
        sql.eq("TrackType", TrackType);
        sql.eq("SourceBillType", SourceBillType);
        sql.eq("project_type_code", projectTypeCode);
        sql.eq("project_type_name", projectTypeName);
        // 关键词模糊查询
        sql.like("project_type_name", keywords);
        // 排序
        sql.desc("AutoID");
        return paginate(sql);
    }

    /**
     * 保存
     *
     * @param sysOtherindetail
     * @return
     */
    public Ret save(SysOtherindetail sysOtherindetail) {
        if (sysOtherindetail == null || isOk(sysOtherindetail.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        boolean success = sysOtherindetail.save();
        return ret(success);
    }

    /**
     * 更新
     * @param sysOtherindetail
     * @return
     */
    public Ret update(SysOtherindetail sysOtherindetail) {
        if (sysOtherindetail == null || notOk(sysOtherindetail.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // 更新时需要判断数据存在
        SysOtherindetail dbSysOtherindetail = findById(sysOtherindetail.getAutoID());
        if (dbSysOtherindetail == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = sysOtherindetail.update();
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     * @param sysOtherindetail 要删除的model
     * @param kv               携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(SysOtherindetail sysOtherindetail, Kv kv) {
        return null;
    }

    /**
     * 检测是否可以删除
     * @param sysOtherindetail model
     * @param kv               携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkInUse(SysOtherindetail sysOtherindetail, Kv kv) {
        // 这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public Ret delete(Long id) {
        deleteById(id);
        return ret(true);
    }

    /**
     * 批量删除
     */
    public Ret deleteRmRdByIds(String ids) {
        deleteByIds(ids);
        return ret(true);
    }

    public List<Record> findEditTableDatas(Kv para) {
        ValidationUtils.notNull(para.getLong("masid"), JBoltMsg.PARAM_ERROR);
        List<Record> records = dbTemplate("sysotherin.dList", para).find();
        return records;
    }
}