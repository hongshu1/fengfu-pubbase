package cn.rjtech.admin.annualorderdqty;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.AnnualorderdQty;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

/**
 * 年度计划订单年月金额 Service
 *
 * @ClassName: AnnualorderdQtyService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 14:22
 */
public class AnnualorderdQtyService extends BaseService<AnnualorderdQty> {

    private final AnnualorderdQty dao = new AnnualorderdQty().dao();

    @Override
    protected AnnualorderdQty dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<AnnualorderdQty> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 保存
     */
    public Ret save(AnnualorderdQty annualorderdQty) {
        if (annualorderdQty == null || isOk(annualorderdQty.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(annualorderdQty.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = annualorderdQty.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(annualorderdQty.getIautoid(), JBoltUserKit.getUserId(), annualorderdQty.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(AnnualorderdQty annualorderdQty) {
        if (annualorderdQty == null || notOk(annualorderdQty.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        AnnualorderdQty dbAnnualorderdQty = findById(annualorderdQty.getIAutoId());
        if (dbAnnualorderdQty == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(annualorderdQty.getName(), annualorderdQty.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = annualorderdQty.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(annualorderdQty.getIautoid(), JBoltUserKit.getUserId(), annualorderdQty.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        return deleteByIds(ids, true);
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        return deleteById(id, true);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param annualorderdQty 要删除的model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(AnnualorderdQty annualorderdQty, Kv kv) {
        //addDeleteSystemLog(annualorderdQty.getIautoid(), JBoltUserKit.getUserId(),annualorderdQty.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param annualorderdQty 要删除的model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(AnnualorderdQty annualorderdQty, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(annualorderdQty, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换isdeleted属性
     */
    public Ret toggleIsDeleted(Long id) {
        return toggleBoolean(id, "isDeleted");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param annualorderdQty 要toggle的model
     * @param column          操作的哪一列
     * @param kv              携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(AnnualorderdQty annualorderdQty, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(AnnualorderdQty annualorderdQty, String column, Kv kv) {
        //addUpdateSystemLog(annualorderdQty.getIautoid(), JBoltUserKit.getUserId(), annualorderdQty.getName(),"的字段["+column+"]值:"+annualorderdQty.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param annualorderdQty model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    public String checkInUse(AnnualorderdQty annualorderdQty, Kv kv) {
        //这里用来覆盖 检测AnnualorderdQty是否被其它表引用
        return null;
    }

}