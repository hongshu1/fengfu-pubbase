package cn.rjtech.admin.syspuinstore;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.otherout.OtherOutService;
import cn.rjtech.model.momdata.SysPuinstore;
import cn.rjtech.model.momdata.SysPuinstoredetail;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.StringUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

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

    @Inject
    private SysPuinstoreService sysPuinstoreService;
    @Inject
    private OtherOutService     otherOutService;


    /**
     * 后台管理数据查询
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
        boolean success = sysPuinstoredetail.save();
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
        boolean success = sysPuinstoredetail.update();
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     * @param sysPuinstoredetail 要删除的model
     * @param kv                 携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(SysPuinstoredetail sysPuinstoredetail, Kv kv) {
        return null;
    }

    /**
     * 检测是否可以删除
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
        Page<Record> paginate = dbTemplate("syspuinstore.pageDetailList", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
        for (Record record : paginate.getList()) {
            String spotticket = record.getStr("spotticket");
            if (StringUtils.isNotBlank(spotticket)) {
                List<Record> barcodeDatas = otherOutService.getBarcodeDatas(spotticket, 10, getOrgCode());
                if (!barcodeDatas.isEmpty()) {
                    Record barcode = barcodeDatas.get(0);
                    record.set("invcode", barcode.get("invcode"));
                    record.set("cinvname", barcode.get("cinvname"));
                    record.set("cinvcode1", barcode.get("cinvcode1"));
                    record.set("cinvname1", barcode.get("cinvname1"));
                    record.set("cinvstd", barcode.get("cinvstd"));
                }
            }
            Kv detailKv = new Kv();
            detailKv.set("SourceBillNo", record.get("sourcebillno"));
            detailKv.set("SourceBillID", record.get("sourcebillid"));
            detailKv.set("SourceBillDid", record.get("sourcebilldid"));
            Record detailByParam = sysPuinstoreService.findSysPODetailByParam(detailKv);
            record.set("invcode", detailByParam.get("invcode"));
            record.set("invname", detailByParam.get("invname"));
        }
        return paginate;
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
    public void saveSysPuinstoredetailModel(List<SysPuinstoredetail> detailList, Record detailRecord,
                                            SysPuinstore puinstore, String whcode, int i, Record detailByParam) {
        SysPuinstoredetail detail = new SysPuinstoredetail();
        detail.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
        detail.setSourceBillNoRow(detailByParam.getStr("sourcebillnorow")); //来源单号+行号
        detail.setRowNo(i);  //行号
        savedetailModel2(detail, puinstore, detailRecord, whcode, detailByParam);
        detail.setCreatePerson(JBoltUserKit.getUserName());
        detail.setCreateDate(puinstore.getCreateDate());
        detailList.add(detail);
    }

    public void savedetailModel2(SysPuinstoredetail detail, SysPuinstore puinstore, Record detailRecord, String whcode,
                                 Record detailByParam) {
        detail.setSourceBillType(detailByParam.get("sourcebilltype"));//采购PO  委外OM（采购类型）
        detail.setSourceBillNo(detailByParam.getStr("sourcebillno")); //来源单号（订单号）
        detail.setSourceBillID(detailByParam.getStr("sourcebillid")); //来源单据ID(订单id)
        detail.setSourceBillDid(detailByParam.getStr("sourcebilldid")); //来源单据DID;采购或委外单身ID
        detail.setSpotTicket(detailRecord.getStr("spotticket"));
        detail.setMasID(puinstore.getAutoID()); //主表ID;T_Sys_PUInStore.AutoID
        //detail.setWeight(""); //重量
        detail.setWhcode(whcode); //仓库
        //detail.setPosCode(""); //库位
        detail.setQty(detailRecord.getBigDecimal("qty")); //入库数量
        //detail.setTrackType(""); //跟单类型
        detail.setMemo(detailRecord.getStr("memo"));
    }
}