package cn.rjtech.admin.syssaledeliverplan;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.syssaledeliverplandetail.SysSaledeliverplandetailService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.OrderStatusEnum;
import cn.rjtech.model.momdata.RcvPlanM;
import cn.rjtech.model.momdata.SysSaledeliverplan;
import cn.rjtech.model.momdata.SysSaledeliverplandetail;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 销售出货(计划)
 * @ClassName: SysSaledeliverplanService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 10:01
 */
public class SysSaledeliverplanService extends BaseService<SysSaledeliverplan> implements IApprovalService {
    private final SysSaledeliverplan dao = new SysSaledeliverplan().dao();

    @Override
    protected SysSaledeliverplan dao() {
        return dao;
    }
    @Inject
    private SysSaledeliverplandetailService syssaledeliverplandetailservice;
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    public List<Record> getAdminDatas(Kv kv) {
        List<Record> records = dbTemplate("syssaledeliverplan.syssaledeliverplanList", kv).find();
        return records;
    }


    /**
     * 保存
     *
     * @param sysSaledeliverplan
     * @return
     */
    public Ret save(SysSaledeliverplan sysSaledeliverplan) {
        if (sysSaledeliverplan == null || isOk(sysSaledeliverplan.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // if(existsName(sysSaledeliverplan.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysSaledeliverplan.save();
        if (success) {
            // 添加日志
            // addSaveSystemLog(sysSaledeliverplan.getAutoID(), JBoltUserKit.getUserId(), sysSaledeliverplan.getName());
        }
        return ret(success);
    }
    /**
     * 批量删除主从表
     */
    public Ret deleteRmRdByIds(String ids) {
        tx(() -> {
            deleteByIds(ids);
            String[] split = ids.split(",");
            for(String s : split){
                delete("DELETE T_Sys_SaleDeliverPlanDetail   where  MasID = ?",s);
            }
            return true;
        });
        return SUCCESS;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public Ret delete(Long id) {
        tx(() -> {
            deleteById(id);
            delete("DELETE T_Sys_SaleDeliverPlanDetail   where  MasID = ?",id);
            return true;
        });
        return SUCCESS;
    }
    /**
     * 更新
     *
     * @param sysSaledeliverplan
     * @return
     */
    public Ret update(SysSaledeliverplan sysSaledeliverplan) {
        if (sysSaledeliverplan == null || notOk(sysSaledeliverplan.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // 更新时需要判断数据存在
        SysSaledeliverplan dbSysSaledeliverplan = findById(sysSaledeliverplan.getAutoID());
        if (dbSysSaledeliverplan == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        // if(existsName(sysSaledeliverplan.getName(), sysSaledeliverplan.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysSaledeliverplan.update();
        if (success) {
            // 添加日志
            // addUpdateSystemLog(sysSaledeliverplan.getAutoID(), JBoltUserKit.getUserId(), sysSaledeliverplan.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysSaledeliverplan 要删除的model
     * @param kv                 携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(SysSaledeliverplan sysSaledeliverplan, Kv kv) {
        // addDeleteSystemLog(sysSaledeliverplan.getAutoID(), JBoltUserKit.getUserId(),sysSaledeliverplan.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysSaledeliverplan model
     * @param kv                 携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkInUse(SysSaledeliverplan sysSaledeliverplan, Kv kv) {
        // 这里用来覆盖 检测是否被其它表引用
        return null;
    }

    public List<Record> getorder(Kv kv) {
        return dbTemplate("syssaledeliverplan.order", kv).find();
    }

    public List<Record> saletype(Kv kv) {
        return dbTemplate("syssaledeliverplan.saletype", kv).find();
    }

    public List<Record> department(Kv kv) {
        return dbTemplate("syssaledeliverplan.department", kv).find();
    }

    public List<Record> customeraddr(Kv kv) {
        return dbTemplate("syssaledeliverplan.customeraddr", kv).find();
    }

    public List<Record> rdstyle(Kv kv) {
        return dbTemplate("syssaledeliverplan.RdStyle", kv).find();
    }

    public List<Record> settlestyle(Kv kv) {
        return dbTemplate("syssaledeliverplan.settlestyle", kv).find();
    }

    public Object foreigncurrency(Kv kv) {
        return dbTemplate("syssaledeliverplan.foreigncurrency", kv).find();
    }


    /**
     * 执行JBoltTable表格整体提交
     *
     * @param jBoltTable
     * @return
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        if(jBoltTable.getSaveRecordList()==null && jBoltTable.getDelete() == null && jBoltTable.getUpdateRecordList()==null){
            return fail("行数据不能为空");
        }
        SysSaledeliverplan sysotherin = jBoltTable.getFormModel(SysSaledeliverplan.class,"syssaledeliverplan");
        //获取当前用户信息？
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        tx(()->{
            //通过 id 判断是新增还是修改
            if(sysotherin.getAutoID() == null){
                sysotherin.setOrganizeCode(getOrgCode());
                sysotherin.setIcreateby(user.getId());
                sysotherin.setCcreatename(user.getName());
                sysotherin.setDcreatetime(now);
                sysotherin.setIupdateby(user.getId());
                sysotherin.setCupdatename(user.getName());
                sysotherin.setDupdatetime(now);

                //主表新增
                ValidationUtils.isTrue(sysotherin.save(), ErrorMsg.SAVE_FAILED);
            }else{
                sysotherin.setIupdateby(user.getId());
                sysotherin.setCupdatename(user.getName());
                sysotherin.setDupdatetime(now);
                //主表修改
                ValidationUtils.isTrue(sysotherin.update(), ErrorMsg.UPDATE_FAILED);
            }
            //从表的操作
            // 获取保存数据（执行保存，通过 getSaveRecordList）
            saveTableSubmitDatas(jBoltTable,sysotherin);
            //获取修改数据（执行修改，通过 getUpdateRecordList）
            updateTableSubmitDatas(jBoltTable,sysotherin);
            //获取删除数据（执行删除，通过 getDelete）
            deleteTableSubmitDatas(jBoltTable);
            return true;
        });
        return SUCCESS;
    }

    //可编辑表格提交-新增数据
    private void saveTableSubmitDatas(JBoltTable jBoltTable,SysSaledeliverplan sysotherin){
        List<Record> list = jBoltTable.getSaveRecordList();
        if(CollUtil.isEmpty(list)) return;
        ArrayList<SysSaledeliverplandetail> sysproductindetail = new ArrayList<>();
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        for (int i=0;i<list.size();i++) {
            Record row = list.get(i);
            SysSaledeliverplandetail sysdetail = new SysSaledeliverplandetail();
            sysdetail.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
            sysdetail.setBarcode(row.getStr("barcode"));
            sysdetail.setInvCode(row.getStr("cinvcode"));
            sysdetail.setWhCode(row.getStr("whcode"));
            sysdetail.setQty(row.getBigDecimal("qty"));
            sysdetail.setMasID(sysotherin.getAutoID());
            sysdetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysdetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysdetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysdetail.setSourceBillID(row.getStr("sourcebilldid"));
            sysotherin.setIupdateby(user.getId());
            sysotherin.setCupdatename(user.getName());
            sysotherin.setDupdatetime(now);
            sysproductindetail.add(sysdetail);
        }
        syssaledeliverplandetailservice.batchSave(sysproductindetail);
    }
    //可编辑表格提交-修改数据
    private void updateTableSubmitDatas(JBoltTable jBoltTable,SysSaledeliverplan sysotherin){
        List<Record> list = jBoltTable.getUpdateRecordList();
        if(CollUtil.isEmpty(list)) return;
        ArrayList<SysSaledeliverplandetail> sysproductindetail = new ArrayList<>();
        Date now = new Date();
        User user = JBoltUserKit.getUser();
        for(int i = 0;i < list.size(); i++){
            Record row = list.get(i);
            SysSaledeliverplandetail sysdetail = new SysSaledeliverplandetail();
            sysdetail.setAutoID(row.getStr("autoid")    );
            sysdetail.setBarcode(row.getStr("barcode"));
            sysdetail.setInvCode(row.getStr("cinvcode"));
            sysdetail.setWhCode(row.getStr("whcode"));
            sysdetail.setQty(row.getBigDecimal("qty"));
            sysdetail.setMasID(sysotherin.getAutoID());
            sysdetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysdetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysdetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysdetail.setSourceBillID(row.getStr("sourcebilldid"));
            sysotherin.setIupdateby(user.getId());
            sysotherin.setCupdatename(user.getName());
            sysotherin.setDupdatetime(now);
            sysproductindetail.add(sysdetail);

        }
        syssaledeliverplandetailservice.batchUpdate(sysproductindetail);
    }
    //可编辑表格提交-删除数据
    private void deleteTableSubmitDatas(JBoltTable jBoltTable){
        Object[] ids = jBoltTable.getDelete();
        if(ArrayUtil.isEmpty(ids)) return;
        syssaledeliverplandetailservice.deleteByIds(ids);
    }

    /*处理审批通过的其他业务操作，如有异常返回错误信息*/
    @Override
    public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
        SysSaledeliverplan saledeliverplan = findById(formAutoId);

        return null;
    }

    /*处理审批不通过的其他业务操作，如有异常处理返回错误信息*/
    @Override
    public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
        SysSaledeliverplan saledeliverplan = findById(formAutoId);

        return null;
    }

    /*实现反审之前的其他业务操作，如有异常返回错误信息*/
    @Override
    public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        return null;
    }

    /*实现反审之后的其他业务操作, 如有异常返回错误信息*/
    @Override
    public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        SysSaledeliverplan saledeliverplan = findById(formAutoId);

        return null;
    }

    /*提审前业务，如有异常返回错误信息*/
    @Override
    public String preSubmitFunc(long formAutoId) {
        return null;
    }

    /*提审后业务处理，如有异常返回错误信息*/
    @Override
    public String postSubmitFunc(long formAutoId) {
        SysSaledeliverplan saledeliverplan = findById(formAutoId);

        return null;
    }

    /*撤回审核业务处理，如有异常返回错误信息*/
    @Override
    public String postWithdrawFunc(long formAutoId) {
        SysSaledeliverplan saledeliverplan = findById(formAutoId);

        return null;
    }

    /*从待审批中，撤回到已保存，业务实现，如有异常返回错误信息*/
    @Override
    public String withdrawFromAuditting(long formAutoId) {
        //comonWithDraw(formAutoId, OrderStatusEnum.NOT_AUDIT.getValue());
        return null;
    }

    /*从已审核，撤回到已保存，前置业务实现，如有异常返回错误信息*/
    @Override
    public String preWithdrawFromAuditted(long formAutoId) {
        return null;
    }

    /*从已审核，撤回到已保存，业务实现，如有异常返回错误信息*/
    @Override
    public String postWithdrawFromAuditted(long formAutoId) {
        return null;
    }

    /*批量审核（审批）通过，后置业务实现*/
    @Override
    public String postBatchApprove(List<Long> formAutoIds) {
        Date date = new Date();
        return null;
    }

    /*批量审批（审核）不通过，后置业务实现*/
    @Override
    public String postBatchReject(List<Long> formAutoIds) {
        Date date = new Date();
        return null;
    }

    /*批量撤销审批，后置业务实现*/
    @Override
    public String postBatchBackout(List<Long> formAutoIds) {
        Date date = new Date();
        List<SysSaledeliverplandetail> saledeliverplandetailList = new ArrayList<>();

        return null;
    }

    /*
     * 公共方法
     * */
    public void comonApproveMethods(SysSaledeliverplan saledeliverplan, int status, Date date) {

    }
}