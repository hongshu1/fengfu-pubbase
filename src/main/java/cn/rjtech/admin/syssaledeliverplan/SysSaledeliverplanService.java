package cn.rjtech.admin.syssaledeliverplan;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.admin.syssaledeliverplandetail.SysSaledeliverplandetailService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.SysProductin;
import cn.rjtech.model.momdata.SysProductindetail;
import cn.rjtech.model.momdata.SysSaledeliverplandetail;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SysSaledeliverplan;
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
public class SysSaledeliverplanService extends BaseService<SysSaledeliverplan> {
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

//    /**
//     * 后台管理数据查询
//     *
//     * @param pageNumber     第几页
//     * @param pageSize       每页几条数据
//     * @param keywords       关键词
//     * @param SourceBillType 来源类型;MO生产工单
//     * @param BillType       业务类型
//     * @return
//     */
//    public Page<SysSaledeliverplan> getAdminDatas(int pageNumber, int pageSize, String keywords, String SourceBillType, String BillType) {
//        // 创建sql对象
//        Sql sql = selectSql().page(pageNumber, pageSize);
//        // sql条件处理
//        sql.eq("SourceBillType", SourceBillType);
//        sql.eq("BillType", BillType);
//        // 关键词模糊查询
//        sql.like("ExchName", keywords);
//        // 排序
//        sql.desc("AutoID");
//        return paginate(sql);
//    }


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
        return ret(true);
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
        return ret(true);
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
            return Ret.msg("行数据不能为空");
        }
        SysSaledeliverplan sysotherin = jBoltTable.getFormModel(SysSaledeliverplan.class,"syssaledeliverplan");
        //获取当前用户信息？
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        tx(()->{
            //通过 id 判断是新增还是修改
            if(sysotherin.getAutoID() == null){
                sysotherin.setOrganizeCode(getOrgCode());
                sysotherin.setCreatePerson(user.getName());
                sysotherin.setCreateDate(now);
                sysotherin.setModifyPerson(user.getName());
                sysotherin.setAuditPerson(user.getName());

                sysotherin.setModifyDate(now);
                //主表新增
                ValidationUtils.isTrue(sysotherin.save(), ErrorMsg.SAVE_FAILED);
            }else{
                sysotherin.setModifyPerson(user.getName());
                sysotherin.setModifyDate(now);
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
        Date now = new Date();
        for (int i=0;i<list.size();i++) {
            Record row = list.get(i);
            SysSaledeliverplandetail sysdetail = new SysSaledeliverplandetail();
            sysdetail.setAutoID(String.valueOf(JBoltSnowflakeKit.me.nextId()));
//            row.set("iautoid", JBoltSnowflakeKit.me.nextId());
            sysdetail.setBarcode(row.get("barcode"));
            sysdetail.setInvCode(row.get("cinvcode"));
            sysdetail.setWhCode(row.get("whcode"));
            sysdetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysdetail.setMasID(sysotherin.getAutoID());
            sysdetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysdetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysdetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysdetail.setSourceBillID(row.getStr("sourcebilldid"));
            sysdetail.setCreateDate(now);
            sysdetail.setModifyDate(now);
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
        for(int i = 0;i < list.size(); i++){
            Record row = list.get(i);
            SysSaledeliverplandetail sysdetail = new SysSaledeliverplandetail();
            sysdetail.setAutoID(row.get("autoid").toString());
            sysdetail.setBarcode(row.get("barcode"));
            sysdetail.setInvCode(row.get("cinvcode"));
            System.out.println(row.get("whcode").toString());
            sysdetail.setWhCode(row.get("whcode").toString());
            sysdetail.setQty(new BigDecimal(row.get("qty").toString()));
            sysdetail.setMasID(sysotherin.getAutoID());
            sysdetail.setSourceBillType(row.getStr("sourcebilltype"));
            sysdetail.setSourceBillNo(row.getStr("sourcebillno"));
            sysdetail.setSourceBillDid(row.getStr("sourcebilldid"));
            sysdetail.setSourceBillID(row.getStr("sourcebilldid"));
            sysdetail.setCreateDate(now);
            sysdetail.setModifyDate(now);
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

}