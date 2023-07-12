package cn.rjtech.admin.syspuinstore;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.otherout.OtherOutService;
import cn.rjtech.model.momdata.SysPuinstore;
import cn.rjtech.model.momdata.SysPuinstoredetail;
import cn.rjtech.util.ValidationUtils;
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
    @Inject
    private InventoryService    inventoryService;

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
     *
     * @param sysPuinstoredetail 要删除的model
     * @param kv                 携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(SysPuinstoredetail sysPuinstoredetail, Kv kv) {
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
        return null;
    }

    public List<Record> findEditTableDatas(Kv para) {
        ValidationUtils.notNull(para.getLong("masid"), JBoltMsg.PARAM_ERROR);
        List<Record> records = dbTemplate("syspuinstore.dList", para).find();
        return records;
    }

    /*
     * 采购入库单明细
     * */
    public Page<Record> pageDetailList(Kv kv) {
        Page<Record> paginate = dbTemplate("syspuinstore.pageDetailList", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
        for (Record record : paginate.getList()) {
            record.set("qty", record.getBigDecimal("qty").stripTrailingZeros().toPlainString());
        }
        return paginate;
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        updateColumn(id, "isdeleted", true);
        return SUCCESS;
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
        return SUCCESS;
    }

    public List<SysPuinstoredetail> findDetailByMasID(String masid) {
        return find("select * from T_Sys_PUInStoreDetail where masid=?", masid);
    }

    public SysPuinstoredetail findFirstByBarcode(String barcode) {
        return findFirst("select * from  T_Sys_PUInStoreDetail where barcode = ? ", barcode);
    }

    /*
     * 采购入库单明细表的model
     * */
    public void saveSysPuinstoredetailModel(SysPuinstoredetail detail, Record detailRecord,
                                            SysPuinstore puinstore, int i) {
        detail.setSourceBillNo(puinstore.getSourceBillNo()); //来源单号（订单号）
        detail.setSourceBillNoRow(puinstore.getSourceBillNo() + "-" + i); //来源单号+行号
        detail.setRowNo(i);  //行号
        detail.setMasID(puinstore.getAutoID()); //主表ID;T_Sys_PUInStore.AutoID
        detail.setWhcode(puinstore.getWhCode()); //仓库
        detail.setPosCode(detailRecord.getStr("poscode"));
        detail.setQty(detailRecord.getBigDecimal("qty")); //入库数量
        detail.setCCreateName(JBoltUserKit.getUserName());
        detail.setDCreateTime(puinstore.getDCreateTime());
        detail.setBarCode(detailRecord.get("barcode"));//现品票
        detail.setIsDeleted(false);
        detail.setBrandCode(detailRecord.get("brandcode"));//品牌code
        detail.setBrandName(detailRecord.get("brandname"));//品牌名称
        detail.setPuUnitCode(detailRecord.get("puunitcode"));//采购单位code
        detail.setPuUnitName(detailRecord.get("puunitname"));//采购单位名称
        detail.setMemo(detailRecord.get("memo"));
        detail.setInvcode(detailRecord.get("invcode"));
        detail.setDUpdateTime(puinstore.getDCreateTime());
        detail.setCUpdateName(JBoltUserKit.getUserName());
    }
}