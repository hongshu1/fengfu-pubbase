package cn.rjtech.admin.rcvdocqcformd;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.RcvDocQcFormD;

import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * 质量管理-来料检单行配置表 Service
 *
 * @ClassName: RcvDocQcFormDService
 * @author: RJ
 * @date: 2023-04-13 16:45
 */
public class RcvDocQcFormDService extends BaseService<RcvDocQcFormD> {

    private final RcvDocQcFormD dao = new RcvDocQcFormD().dao();

    @Override
    protected RcvDocQcFormD dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<RcvDocQcFormD> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 保存
     */
    public Ret save(RcvDocQcFormD rcvDocQcFormD) {
        if (rcvDocQcFormD == null || isOk(rcvDocQcFormD.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(rcvDocQcFormD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = rcvDocQcFormD.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(rcvDocQcFormD.getIAutoId(), JBoltUserKit.getUserId(), rcvDocQcFormD.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(RcvDocQcFormD rcvDocQcFormD) {
        if (rcvDocQcFormD == null || notOk(rcvDocQcFormD.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        RcvDocQcFormD dbRcvDocQcFormD = findById(rcvDocQcFormD.getIAutoId());
        if (dbRcvDocQcFormD == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(rcvDocQcFormD.getName(), rcvDocQcFormD.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = rcvDocQcFormD.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(rcvDocQcFormD.getIAutoId(), JBoltUserKit.getUserId(), rcvDocQcFormD.getName());
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
     * @param rcvDocQcFormD 要删除的model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(RcvDocQcFormD rcvDocQcFormD, Kv kv) {
        //addDeleteSystemLog(rcvDocQcFormD.getIAutoId(), JBoltUserKit.getUserId(),rcvDocQcFormD.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param rcvDocQcFormD 要删除的model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(RcvDocQcFormD rcvDocQcFormD, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(rcvDocQcFormD, kv);
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
    public List<RcvDocQcFormD> findByIRcvDocQcFormMId(Long iRcvDocQcFormMid) {
        return find("select * from PL_RcvDocQcFormD where iRcvDocQcFormMid=?", iRcvDocQcFormMid);
    }

    /*传参*/
    public void saveRcvDocQcFormDModel(RcvDocQcFormD qcFormD, Long iRcvDocQcFormMid, Long iqcformId, Object iseq,
                                       Object itype, Object istdVal, Object iMaxVal, Object iMinVal, Object cOptions,
                                       Object iqcformtableitemid) {
        qcFormD.setIRcvDocQcFormMid(iRcvDocQcFormMid);
        qcFormD.setIQcFormId(iqcformId);
        qcFormD.setIFormParamId(iqcformtableitemid != null ? Long.valueOf(iqcformtableitemid.toString()) : null);
        qcFormD.setISeq(iseq != null ? strToInt(iseq.toString()) : null);
        //qcFormD.setCQcFormParamIds(cQcFormParamIds);//String cQcFormParamIds,
        qcFormD.setIType(iseq != null ? strToInt(itype.toString()) : null);
        qcFormD.setIStdVal(objToBig(istdVal));
        qcFormD.setIMaxVal(objToBig(iMaxVal));
        qcFormD.setIMinVal(objToBig(iMinVal));
        qcFormD.setCOptions(cOptions != null ? cOptions.toString() : null);
    }

    /*object转bigdeciaml*/
    public static BigDecimal objToBig(Object value) {
        BigDecimal bigDecimal = null;
        if (value != null) {
            if (value instanceof BigDecimal) {
                bigDecimal = (BigDecimal) value;
            } else if (value instanceof String) {
                bigDecimal = new BigDecimal((String) value);
            } else if (value instanceof BigInteger) {
                bigDecimal = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                bigDecimal = new BigDecimal(((Number) value).doubleValue());
            } else {
                throw new ClassCastException(
                    "Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal.");
            }
        }
        return bigDecimal;
    }

    public Integer strToInt(Object obj) {
        return Integer.parseInt(obj.toString());
    }
}