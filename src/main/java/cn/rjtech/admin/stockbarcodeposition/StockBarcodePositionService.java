package cn.rjtech.admin.stockbarcodeposition;

import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.StockBarcodePosition;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

/**
 * 条码库存表 Service
 *
 * @ClassName: StockBarcodePositionService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-30 13:39
 */
public class StockBarcodePositionService extends BaseService<StockBarcodePosition> {

    private final StockBarcodePosition dao = new StockBarcodePosition().dao();

    @Override
    protected StockBarcodePosition dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<StockBarcodePosition> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("AutoId", "DESC", pageNumber, pageSize, keywords, "AutoId");
    }

    /**
     * 保存
     */
    public Ret save(StockBarcodePosition stockBarcodePosition) {
        if (stockBarcodePosition == null || isOk(stockBarcodePosition.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(stockBarcodePosition.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = stockBarcodePosition.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(stockBarcodePosition.getAutoID(), JBoltUserKit.getUserId(), stockBarcodePosition.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(StockBarcodePosition stockBarcodePosition) {
        if (stockBarcodePosition == null || notOk(stockBarcodePosition.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        StockBarcodePosition dbStockBarcodePosition = findById(stockBarcodePosition.getAutoID());
        if (dbStockBarcodePosition == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(stockBarcodePosition.getName(), stockBarcodePosition.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = stockBarcodePosition.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(stockBarcodePosition.getAutoID(), JBoltUserKit.getUserId(), stockBarcodePosition.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        return deleteByIds(ids, true);
    }

    public int deletByAutoid(String autoid){
        Sql sql = deleteSql().eq(StockBarcodePosition.AUTOID, autoid);
        return delete(sql);
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
     * @param stockBarcodePosition 要删除的model
     * @param kv                   携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(StockBarcodePosition stockBarcodePosition, Kv kv) {
        //addDeleteSystemLog(stockBarcodePosition.getAutoID(), JBoltUserKit.getUserId(),stockBarcodePosition.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param stockBarcodePosition 要删除的model
     * @param kv                   携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(StockBarcodePosition stockBarcodePosition, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(stockBarcodePosition, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    public List<Record> findBarcodePositionByKvs(Kv kv) {
        String cvencode = kv.getStr("cvencode");
        if (StrUtil.isBlank(cvencode)) {
            cvencode = "NULL";
        }
        String cwhcode = kv.getStr("cwhcode");
        if (StrUtil.isBlank(cwhcode)) {
            cwhcode = "NULL";
        }
        String careacode = kv.getStr("careacode");
        if (StrUtil.isBlank(careacode)) {
            careacode = "NULL";
        }
        kv.set("organizecode", getOrgCode());
        kv.set("invcode", kv.getStr("cinvcode"));
        kv.set("vencode", cvencode);
        kv.set("whcode", cwhcode);
        kv.set("poscode", careacode);
        return dbTemplate("warehousebeginofperiod.findBarcodePositionByKvs", kv).find();
    }

    /*
     * 传参
     * */
    public void saveBarcodePositionModel(StockBarcodePosition position, Kv kv, Date now, String lockType, Long detailId) {
        position.setOrganizeCode(getOrgCode());
        position.setInvCode(kv.getStr("cinvcode"));
        position.setVenCode(StrUtil.isBlank(kv.getStr("cvencode")) ? "NULL" : kv.getStr("cvencode"));
        position.setWhCode(StrUtil.isBlank(kv.getStr("cwhcode")) ? "NULL" : kv.getStr("cwhcode"));
        position.setPosCode(StrUtil.isBlank(kv.getStr("careacode")) ? "NULL" : kv.getStr("careacode"));
        position.setBarcode(kv.getStr("barcode"));
        position.setBatch(kv.getStr("batch"));
        position.setChgDate(now);
        position.setLockSource(String.valueOf(detailId));
        position.setLockType(lockType);
    }

    public StockBarcodePosition findByLockSource(Long detailAutoid) {
        return findFirst("select * from T_Sys_StockBarcodePosition where OrganizeCode = ? and LockSource=?",
            getOrgCode(), detailAutoid);
    }

}