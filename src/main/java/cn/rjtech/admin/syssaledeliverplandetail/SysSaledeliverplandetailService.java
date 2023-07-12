package cn.rjtech.admin.syssaledeliverplandetail;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.SysSaledeliverplandetail;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * 销售出货(计划)明细
 *
 * @ClassName: SysSaledeliverplandetailService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 10:13
 */
public class SysSaledeliverplandetailService extends BaseService<SysSaledeliverplandetail> {

    private final SysSaledeliverplandetail dao = new SysSaledeliverplandetail().dao();

    @Override
    protected SysSaledeliverplandetail dao() {
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
     */
    public Page<SysSaledeliverplandetail> getAdminDatas(int pageNumber, int pageSize, String TrackType, String SourceBillType) {
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
     */
    public Ret save(SysSaledeliverplandetail sysSaledeliverplandetail) {
        if (sysSaledeliverplandetail == null || isOk(sysSaledeliverplandetail.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // if(existsName(sysSaledeliverplandetail.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysSaledeliverplandetail.save();
        if (success) {
            // 添加日志
            // addSaveSystemLog(sysSaledeliverplandetail.getAutoID(), JBoltUserKit.getUserId(), sysSaledeliverplandetail.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(SysSaledeliverplandetail sysSaledeliverplandetail) {
        if (sysSaledeliverplandetail == null || notOk(sysSaledeliverplandetail.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // 更新时需要判断数据存在
        SysSaledeliverplandetail dbSysSaledeliverplandetail = findById(sysSaledeliverplandetail.getAutoID());
        if (dbSysSaledeliverplandetail == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        // if(existsName(sysSaledeliverplandetail.getName(), sysSaledeliverplandetail.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysSaledeliverplandetail.update();
        if (success) {
            // 添加日志
            // addUpdateSystemLog(sysSaledeliverplandetail.getAutoID(), JBoltUserKit.getUserId(), sysSaledeliverplandetail.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysSaledeliverplandetail 要删除的model
     * @param kv                       携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(SysSaledeliverplandetail sysSaledeliverplandetail, Kv kv) {
        // addDeleteSystemLog(sysSaledeliverplandetail.getAutoID(), JBoltUserKit.getUserId(),sysSaledeliverplandetail.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysSaledeliverplandetail model
     * @param kv                       携带额外参数一般用不上
     */
    @Override
    public String checkInUse(SysSaledeliverplandetail sysSaledeliverplandetail, Kv kv) {
        // 这里用来覆盖 检测是否被其它表引用
        return null;
    }

    public List<SysSaledeliverplandetail> findListByMasid(String masid) {
        return find("select * from T_Sys_SaleDeliverPlanDetail where masid=?", masid);
    }

    public List<SysSaledeliverplandetail> findListByBarcode(String barcode){
        return find("select * from T_Sys_SaleDeliverPlanDetail where barcode=?", barcode);
    }

}