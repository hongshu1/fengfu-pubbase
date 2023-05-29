package cn.rjtech.admin.syssaledeliverdetail;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.util.JBoltCamelCaseUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.SysSaledeliverdetail;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 销售出库明细
 * @ClassName: SysSaledeliverdetailService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 13:06
 */
public class SysSaledeliverdetailService extends BaseService<SysSaledeliverdetail> {
    private final SysSaledeliverdetail dao = new SysSaledeliverdetail().dao();

    @Override
    protected SysSaledeliverdetail dao() {
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
     * @return
     */
    public Page<SysSaledeliverdetail> getAdminDatas(int pageNumber, int pageSize, String TrackType, String SourceBillType) {
        // 创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        // sql条件处理
        sql.eq("TrackType", TrackType);
        sql.eq("SourceBillType", SourceBillType);
        // 排序
        sql.desc("AutoID");
        return paginate(sql);
    }

    /**
     * 保存
     *
     * @param sysSaledeliverdetail
     * @return
     */
    public Ret save(SysSaledeliverdetail sysSaledeliverdetail) {
        if (sysSaledeliverdetail == null || isOk(sysSaledeliverdetail.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        boolean success = sysSaledeliverdetail.save();
        return ret(success);
    }

    /**
     * 更新
     *
     * @param sysSaledeliverdetail
     * @return
     */
    public Ret update(SysSaledeliverdetail sysSaledeliverdetail) {
        if (sysSaledeliverdetail == null || notOk(sysSaledeliverdetail.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // 更新时需要判断数据存在
        SysSaledeliverdetail dbSysSaledeliverdetail = findById(sysSaledeliverdetail.getAutoID());
        if (dbSysSaledeliverdetail == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = sysSaledeliverdetail.update();
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysSaledeliverdetail 要删除的model
     * @param kv                   携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(SysSaledeliverdetail sysSaledeliverdetail, Kv kv) {
        // addDeleteSystemLog(sysSaledeliverdetail.getAutoID(), JBoltUserKit.getUserId(),sysSaledeliverdetail.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysSaledeliverdetail model
     * @param kv                   携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkInUse(SysSaledeliverdetail sysSaledeliverdetail, Kv kv) {
        // 这里用来覆盖 检测是否被其它表引用
        return null;
    }

    public List<SysSaledeliverdetail> getpushudetail(Kv kv) {
        List<SysSaledeliverdetail> datas = daoTemplate("sysSaleDeliver.pushudetail",kv).find();
        return datas;
    }

}