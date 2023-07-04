package cn.rjtech.admin.syssaledeliverplan;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.syspuinstore.SysPuinstoreService;
import cn.rjtech.admin.syssaledeliverplandetail.SysSaledeliverplandetailService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.OrderStatusEnum;
import cn.rjtech.model.momdata.SysSaledeliverplan;
import cn.rjtech.model.momdata.SysSaledeliverplandetail;
import cn.rjtech.model.momdata.base.BaseSysSaledeliverplandetail;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDeleteDTO;
import cn.rjtech.u9.entity.syspuinstore.SysPuinstoreDeleteDTO.data;
import cn.rjtech.util.BaseInU8Util;
import cn.rjtech.util.ValidationUtils;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 销售出货(计划)
 *
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
    @Inject
    private SysPuinstoreService puinstoreService;

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
            List<SysSaledeliverplan> saledeliverplanList = getListByIds(ids);
            Date date = new Date();
            List<SysSaledeliverplan> deleteSaledeliverplanList = new ArrayList<>();
            List<SysSaledeliverplandetail> deleteSaledeliverplanDetailList = new ArrayList<>();
            for (SysSaledeliverplan saledeliverplan : saledeliverplanList) {
                commonDeleteMethods(saledeliverplan, date);
                deleteSaledeliverplanList.add(saledeliverplan);

                List<SysSaledeliverplandetail> saledeliverplandetailList = syssaledeliverplandetailservice
                    .findListByMasid(saledeliverplan.getAutoID());
                deleteSaledeliverplanDetailList.addAll(saledeliverplandetailList);
            }
            //明细表：物理删除
            commonDetailDeleteMethods(deleteSaledeliverplanDetailList);
            //主表数据：逻辑删除
            ValidationUtils.isTrue(batchUpdate(deleteSaledeliverplanList).length > 0, "批量删除失败");

            return true;
        });
        return SUCCESS;
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        tx(() -> {
            SysSaledeliverplan saledeliverplan = findById(id);
            commonDeleteMethods(saledeliverplan, new Date());

            List<SysSaledeliverplandetail> saledeliverplandetailList = syssaledeliverplandetailservice
                .findListByMasid(saledeliverplan.getAutoID());
            //明细表：物理删除
            commonDetailDeleteMethods(saledeliverplandetailList);
            //主表数据：逻辑删除
            ValidationUtils.isTrue(saledeliverplan.update(), "删除失败");
            return true;
        });
        return SUCCESS;
    }

    /*
     * 主表公共删除方法
     * */
    public void commonDeleteMethods(SysSaledeliverplan saledeliverplan, Date date) {
        ValidationUtils.equals(saledeliverplan.getIcreateby(), JBoltUserKit.getUserId(), "不可删除非本人单据!");
        ValidationUtils.equals(saledeliverplan.getIAuditStatus(), OrderStatusEnum.NOT_AUDIT.getValue(),
            saledeliverplan.getBillNo() + "：非已保存状态，不能删除!");

        saledeliverplan.setIsDeleted(true);
        saledeliverplan.setCupdatename(JBoltUserKit.getUserName());
        saledeliverplan.setDupdatetime(date);
        saledeliverplan.setIupdateby(JBoltUserKit.getUserId());
    }

    /*
     * 明细表公共删除方法
     * */
    public void commonDetailDeleteMethods(List<SysSaledeliverplandetail> saledeliverplandetailList) {
        if (saledeliverplandetailList.isEmpty()) {
            return;
        }
        String ids = saledeliverplandetailList.stream().map(BaseSysSaledeliverplandetail::getAutoID)
            .collect(Collectors.joining(","));
        syssaledeliverplandetailservice.deleteByIds(ids);
    }

    /**
     * 更新
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
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        if (jBoltTable.getSaveRecordList() == null && jBoltTable.getDelete() == null
            && jBoltTable.getUpdateRecordList() == null) {
            return fail("行数据不能为空");
        }
        SysSaledeliverplan sysotherin = jBoltTable.getFormModel(SysSaledeliverplan.class, "syssaledeliverplan");
        //获取当前用户信息？
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        tx(() -> {
            //通过 id 判断是新增还是修改
            if (sysotherin.getAutoID() == null) {
                sysotherin.setOrganizeCode(getOrgCode());
                sysotherin.setIcreateby(user.getId());
                sysotherin.setCcreatename(user.getName());
                sysotherin.setDcreatetime(now);
                sysotherin.setIupdateby(user.getId());
                sysotherin.setCupdatename(user.getName());
                sysotherin.setDupdatetime(now);

                //主表新增
                ValidationUtils.isTrue(sysotherin.save(), ErrorMsg.SAVE_FAILED);
            } else {
                sysotherin.setIupdateby(user.getId());
                sysotherin.setCupdatename(user.getName());
                sysotherin.setDupdatetime(now);
                //主表修改
                ValidationUtils.isTrue(sysotherin.update(), ErrorMsg.UPDATE_FAILED);
            }
            //从表的操作
            // 获取保存数据（执行保存，通过 getSaveRecordList）
            saveTableSubmitDatas(jBoltTable, sysotherin);
            //获取修改数据（执行修改，通过 getUpdateRecordList）
            updateTableSubmitDatas(jBoltTable, sysotherin);
            //获取删除数据（执行删除，通过 getDelete）
            deleteTableSubmitDatas(jBoltTable);
            return true;
        });
        return SUCCESS;
    }

    //可编辑表格提交-新增数据
    private void saveTableSubmitDatas(JBoltTable jBoltTable, SysSaledeliverplan sysotherin) {
        List<Record> list = jBoltTable.getSaveRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }
        ArrayList<SysSaledeliverplandetail> sysproductindetail = new ArrayList<>();
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        for (int i = 0; i < list.size(); i++) {
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
    private void updateTableSubmitDatas(JBoltTable jBoltTable, SysSaledeliverplan sysotherin) {
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }
        ArrayList<SysSaledeliverplandetail> sysproductindetail = new ArrayList<>();
        Date now = new Date();
        User user = JBoltUserKit.getUser();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            SysSaledeliverplandetail sysdetail = new SysSaledeliverplandetail();
            sysdetail.setAutoID(row.getStr("autoid"));
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
    private void deleteTableSubmitDatas(JBoltTable jBoltTable) {
        Object[] ids = jBoltTable.getDelete();
        if (ArrayUtil.isEmpty(ids)) {
            return;
        }
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
        if(isLast){
            commonReverseApproveFunc(StrUtil.toString(formAutoId));
        }
        return null;
    }

    /*
     * 反审批
     * */
    public String commonReverseApproveFunc(String autoid) {
        Date date = new Date();
        SysSaledeliverplan saledeliverplan = findById(autoid);
        List<SysSaledeliverplan> saledeliverplanList = new ArrayList<>();
        //打u8接口，通知u8删除单据，然后更新mom平台的数据
//        String json = getSysPuinstoreDeleteDTO(saledeliverplan.getU8BillNo());
        String json ="";
        try {
            String post = new BaseInU8Util().deleteVouchProcessDynamicSubmitUrl(json);
            LOG.info(post);
        } catch (Exception ex) {
            return ex.getMessage();
        }
        //
        User user = JBoltUserKit.getUser();
        //saledeliverplan.setU8BillNo(null);//将u8的单据号置为空
        saledeliverplan.setDAuditTime(date);
        saledeliverplan.setIAuditby(user.getId());
        saledeliverplan.setCAuditname(user.getUsername());
        comonApproveMethods(saledeliverplan,date);
        saledeliverplanList.add(saledeliverplan);
        //更新
        batchUpdate(saledeliverplanList);
        return null;
    }

    /*
     * 获取删除的json
     * */
    public String getSysPuinstoreDeleteDTO(String u8billno) {
        User user = JBoltUserKit.getUser();

        Record userRecord = puinstoreService.findU8UserByUserCode(user.getUsername());
        Record u8Record = puinstoreService.findU8RdRecord01Id(u8billno);

        SysPuinstoreDeleteDTO deleteDTO = new SysPuinstoreDeleteDTO();
        data data = new data();
        data.setAccid(getOrgCode());
        data.setPassword(userRecord.get("u8_pwd"));
        data.setUserID(userRecord.get("u8_code"));
        Long id = u8Record.getLong("id");
        data.setVouchId(String.valueOf(id));//u8单据id
        deleteDTO.setData(data);

        return JSON.toJSONString(deleteDTO);
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
        comonApproveMethods(saledeliverplan, new Date());

        ValidationUtils.isTrue(saledeliverplan.update(), "撤回失败");
        return null;
    }

    /*从待审批中，撤回到已保存，业务实现，如有异常返回错误信息*/
    @Override
    public String withdrawFromAuditting(long formAutoId) {
        SysSaledeliverplan saledeliverplan = findById(formAutoId);
        comonApproveMethods(saledeliverplan, new Date());

        ValidationUtils.isTrue(saledeliverplan.update(), "撤回失败");
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
        ArrayList<SysSaledeliverplan> sysSaledeliverplans = new ArrayList<>();
        for (Long autoid : formAutoIds) {
            SysSaledeliverplan saledeliverplan = findById(autoid);
            comonApproveMethods(saledeliverplan, date);
            sysSaledeliverplans.add(saledeliverplan);

            //todo 审核通过，获取u8单号
        }
        ValidationUtils.isTrue(batchUpdate(sysSaledeliverplans).length > 0, "批量审批通过失败！");
        return null;
    }

    /*批量审批（审核）不通过，后置业务实现*/
    @Override
    public String postBatchReject(List<Long> formAutoIds) {
        Date date = new Date();
        ArrayList<SysSaledeliverplan> sysSaledeliverplans = new ArrayList<>();
        for (Long autoid : formAutoIds) {
            SysSaledeliverplan saledeliverplan = findById(autoid);
            comonApproveMethods(saledeliverplan, date);
            sysSaledeliverplans.add(saledeliverplan);
        }
        ValidationUtils.isTrue(batchUpdate(sysSaledeliverplans).length > 0, "批量审批不通过失败！");
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
    public void comonApproveMethods(SysSaledeliverplan saledeliverplan, Date date) {
        saledeliverplan.setDupdatetime(date);
        saledeliverplan.setIupdateby(JBoltUserKit.getUserId());
        saledeliverplan.setCupdatename(JBoltUserKit.getUserName());
    }
}