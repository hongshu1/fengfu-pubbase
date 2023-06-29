package cn.rjtech.admin.stockoutqcformd;

import java.util.List;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.rcvdocqcformd.RcvDocQcFormDService;
import cn.rjtech.model.momdata.StockoutQcFormD;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

/**
 * 质量管理-出库检明细列值 Service
 *
 * @ClassName: StockoutQcFormDService
 * @author: RJ
 * @date: 2023-04-25 16:21
 */
public class StockoutQcFormDService extends BaseService<StockoutQcFormD> {

    private final StockoutQcFormD dao = new StockoutQcFormD().dao();

    @Override
    protected StockoutQcFormD dao() {
        return dao;
    }

    @Inject
    private RcvDocQcFormDService rcvDocQcFormDService;

    /**
     * 后台管理分页查询
     */
    public Page<StockoutQcFormD> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iSeq", "ASC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 保存
     */
    public Ret save(StockoutQcFormD stockoutQcFormD) {
        if (stockoutQcFormD == null || isOk(stockoutQcFormD.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(stockoutQcFormD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = stockoutQcFormD.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(stockoutQcFormD.getIAutoId(), JBoltUserKit.getUserId(), stockoutQcFormD.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(StockoutQcFormD stockoutQcFormD) {
        if (stockoutQcFormD == null || notOk(stockoutQcFormD.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        StockoutQcFormD dbStockoutQcFormD = findById(stockoutQcFormD.getIAutoId());
        if (dbStockoutQcFormD == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(stockoutQcFormD.getName(), stockoutQcFormD.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = stockoutQcFormD.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(stockoutQcFormD.getIAutoId(), JBoltUserKit.getUserId(), stockoutQcFormD.getName());
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
     * @param stockoutQcFormD 要删除的model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(StockoutQcFormD stockoutQcFormD, Kv kv) {
        //addDeleteSystemLog(stockoutQcFormD.getIAutoId(), JBoltUserKit.getUserId(),stockoutQcFormD.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param stockoutQcFormD 要删除的model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(StockoutQcFormD stockoutQcFormD, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(stockoutQcFormD, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 根据iRcvDocQcFormMid查看
     */
    public List<StockoutQcFormD> findByIStockoutQcFormMid(Long iStockoutQcFormMid) {
        return find("select * from PL_StockoutQcFormD where iStockoutQcFormMid=?", iStockoutQcFormMid);
    }

    public void saveStockQcFormDModel(StockoutQcFormD qcFormD, Long iStockoutQcFormMid, Long iqcformId, Object iseq,
                                      Object itype, Object istdVal, Object iMaxVal, Object iMinVal, Object cOptions,
                                      Object iqcformtableitemid) {
        qcFormD.setIStockoutQcFormMid(iStockoutQcFormMid);
        qcFormD.setIQcFormId(iqcformId);
        qcFormD.setIFormParamId(iqcformtableitemid != null ? Long.valueOf(iqcformtableitemid.toString()) : null);
        qcFormD.setISeq(iseq != null ? strToInt(iseq.toString()) : null);
        //qcFormD.setCQcFormParamIds(cQcFormParamIds);//String cQcFormParamIds,
        qcFormD.setIType(itype != null ? strToInt(itype.toString()) : null);
        qcFormD.setIStdVal(rcvDocQcFormDService.objToBig(istdVal));
        qcFormD.setIMaxVal(rcvDocQcFormDService.objToBig(iMaxVal));
        qcFormD.setIMinVal(rcvDocQcFormDService.objToBig(iMinVal));
        qcFormD.setCOptions(cOptions != null ? cOptions.toString() : null);
    }

    public Integer strToInt(Object obj) {
        return Integer.parseInt(obj.toString());
    }

}