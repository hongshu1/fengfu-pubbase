package cn.rjtech.admin.syspuinstore;

import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.rjtech.model.momdata.SysPuinstore;
import cn.rjtech.model.momdata.SysPuinstoredetail;
import cn.rjtech.util.ValidationUtils;

import com.jfinal.plugin.activerecord.Page;

import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;

import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.wms.utils.StringUtils;

import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.List;

/**
 * 采购入库单明细
 *
 * @ClassName: SysPuinstoredetailService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 15:39
 */
public class SysPuinstoredetailService extends BaseService<SysPuinstoredetail> {

    private final SysPuinstoredetail dao = new SysPuinstoredetail().dao();

    @Override
    protected SysPuinstoredetail dao() {
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
     * @param SourceBillType 来源类型;PO 采购 OM委外
     * @param TrackType      跟单类型
     * @param IsDeleted      删除状态：0. 未删除 1. 已删除
     */
    public Page<SysPuinstoredetail> getAdminDatas(int pageNumber, int pageSize, String SourceBillType, String TrackType,
                                                  Boolean IsDeleted) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("SourceBillType", SourceBillType);
        sql.eq("TrackType", TrackType);
        sql.eqBooleanToChar("IsDeleted", IsDeleted);
        //排序
        sql.desc("AutoID");
        return paginate(sql);
    }

    /**
     * 保存
     */
    public Ret save(SysPuinstoredetail sysPuinstoredetail) {
        if (sysPuinstoredetail == null || isOk(sysPuinstoredetail.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(sysPuinstoredetail.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysPuinstoredetail.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(sysPuinstoredetail.getAutoID(), JBoltUserKit.getUserId(), sysPuinstoredetail.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(SysPuinstoredetail sysPuinstoredetail) {
        if (sysPuinstoredetail == null || notOk(sysPuinstoredetail.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        SysPuinstoredetail dbSysPuinstoredetail = findById(sysPuinstoredetail.getAutoID());
        if (dbSysPuinstoredetail == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(sysPuinstoredetail.getName(), sysPuinstoredetail.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysPuinstoredetail.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(sysPuinstoredetail.getAutoID(), JBoltUserKit.getUserId(), sysPuinstoredetail.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysPuinstoredetail 要删除的model
     * @param kv                 携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(SysPuinstoredetail sysPuinstoredetail, Kv kv) {
        //addDeleteSystemLog(sysPuinstoredetail.getAutoID(), JBoltUserKit.getUserId(),sysPuinstoredetail.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysPuinstoredetail model
     * @param kv                 携带额外参数一般用不上
     */
    @Override
    public String checkInUse(SysPuinstoredetail sysPuinstoredetail, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(SysPuinstoredetail sysPuinstoredetail, String column, Kv kv) {
        //addUpdateSystemLog(sysPuinstoredetail.getAutoID(), JBoltUserKit.getUserId(), sysPuinstoredetail.getName(),"的字段["+column+"]值:"+sysPuinstoredetail.get(column));
        /**
         switch(column){
         case "IsDeleted":
         break;
         }
         */
        return null;
    }

    public List<Record> findEditTableDatas(Kv para) {
        ValidationUtils.notNull(para.getLong("masid"), JBoltMsg.PARAM_ERROR);
        List<Record> records = dbTemplate("syspuinstore.dList", para).find();
        return records;
    }

    public Page<SysPuinstoredetail> findSysPuinstoreDetailTableDatas(int pageNumber, int pageSize, String spotticket,
                                                                     String masid) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("spotticket", spotticket);//现品票
        sql.eq("masid", masid); //主键
        sql.eqBooleanToChar("isDeleted", false);
        //排序
        sql.desc("CreateDate");
        return paginate(sql);
    }

    public Page<Record> pageDetailList(Kv kv) {
        if (StringUtils.isBlank(kv.getStr("masid")) && StringUtils.isBlank(kv.getStr("spotticket"))) {
            return null;
        }
        return dbTemplate("syspuinstore.pageDetailList", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        updateColumn(id, "isdeleted", true);
        return ret(true);
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

    public List<SysPuinstoredetail> findDetailByMasID(String masid) {
        return find("select * from T_Sys_PUInStoreDetail where masid=?", masid);
    }

    /*
     * 采购入库单明细表的model
     * */
    public void saveSysPuinstoredetailModel(List<SysPuinstoredetail> detailList, List<Record> saveRecordList,
                                            SysPuinstore puinstore,String whcode) {
        for (int i = 0; i < saveRecordList.size(); i++) {
            Record record = saveRecordList.get(i);
            SysPuinstoredetail detail = new SysPuinstoredetail();
            detail.setAutoID(String.valueOf(JBoltSnowflakeKit.me.nextId()));
            detail.setSourceBillType("");//采购PO  委外OM（采购类型）
            detail.setSourceBillNo(puinstore.getSourceBillNo()); //来源单号（订单号）
            detail.setSourceBillNoRow(puinstore.getSourceBillNo() +"-"+ i); //来源单号+行号
            detail.setSourceBillID(puinstore.getSourceBillNo()); //来源单据ID(订单id)
            detail.setSourceBillDid(puinstore.getSourceBillNo()); //来源单据DID;采购或委外单身ID
            detail.setRowNo(i);  //行号
            detail.setMasID(puinstore.getAutoID()); //主表ID;T_Sys_PUInStore.AutoID
            //detail.setWeight(""); //重量
            detail.setWhcode(whcode); //仓库
            //detail.setPosCode(""); //库位
            detail.setQty(new BigDecimal(1)); //入库数量
            //detail.setTrackType(""); //跟单类型
            detail.setMemo(record.get("memo"));
            detail.setCreatePerson(JBoltUserKit.getUserName());
            detail.setCreateDate(puinstore.getCreateDate());
            detail.setModifyPerson(JBoltUserKit.getUserName());
            detail.setModifyDate(puinstore.getCreateDate());
            detail.setSpotTicket(record.get("spotticket"));
            //
            detailList.add(detail);
        }

    }
}