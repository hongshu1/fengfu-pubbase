package cn.rjtech.admin.barcodedetail;

import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.common.model.Barcodedetail;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;
import java.util.List;

/**
 * 条码明细表 Service
 *
 * @ClassName: BarcodedetailService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-30 13:25
 */
public class BarcodedetailService extends BaseService<Barcodedetail> {

    private final Barcodedetail dao = new Barcodedetail().dao();

    @Override
    protected Barcodedetail dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<Barcodedetail> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 保存
     */
    public Ret save(Barcodedetail barcodedetail) {
        if (barcodedetail == null || isOk(barcodedetail.getAutoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(barcodedetail.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = barcodedetail.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(barcodedetail.getAutoid(), JBoltUserKit.getUserId(), barcodedetail.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(Barcodedetail barcodedetail) {
        if (barcodedetail == null || notOk(barcodedetail.getAutoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        Barcodedetail dbBarcodedetail = findById(barcodedetail.getAutoid());
        if (dbBarcodedetail == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(barcodedetail.getName(), barcodedetail.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = barcodedetail.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(barcodedetail.getAutoid(), JBoltUserKit.getUserId(), barcodedetail.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        return deleteByIds(ids, true);
    }

    public int deletByAutoid(String autoid) {
        Sql sql = deleteSql().eq(Barcodedetail.AUTOID, autoid);
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
     * @param barcodedetail 要删除的model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Barcodedetail barcodedetail, Kv kv) {
        //addDeleteSystemLog(barcodedetail.getAutoid(), JBoltUserKit.getUserId(),barcodedetail.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param barcodedetail 要删除的model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(Barcodedetail barcodedetail, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(barcodedetail, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    public Barcodedetail findbyBarcode(String barcode) {
        return findFirst("select * from T_Sys_BarcodeDetail where Barcode = ?", barcode);
    }

    public List<Barcodedetail> findbyMasid(String masid) {
        return find("select * from T_Sys_BarcodeDetail where masid = ?", masid);
    }

    public Barcodedetail findbyAutoid(String autoid) {
        return findFirst("select * from T_Sys_BarcodeDetail where autoid = ?", autoid);
    }


    /*
     * 传参
     * */
    public void saveBarcodedetailModel(Barcodedetail barcodedetail, Long masid, Date now, Kv kv, String sourcebilltype) {
        barcodedetail.setAutoid(JBoltSnowflakeKit.me.nextId());
        barcodedetail.setSourcebilltype(sourcebilltype);
        barcodedetail.setMasid(String.valueOf(masid));
        barcodedetail.setVencode(StrUtil.isBlank(kv.getStr("cvencode")) ? "NULL" : kv.getStr("cvencode"));
        barcodedetail.setBarcode(kv.getStr("barcode"));
        barcodedetail.setInvcode(kv.getStr("cinvcode"));
        barcodedetail.setBatch(kv.getStr("batch"));
//        barcodedetail.setPrintnum(printnum);//每张条码需要打印的次数
        barcodedetail.setCreateperson(JBoltUserKit.getUserName());
        barcodedetail.setCreatedate(kv.getDate("createdate"));//批次日期
        barcodedetail.setModifyperson(JBoltUserKit.getUserName());
        barcodedetail.setModifydate(now);
        barcodedetail.setReportFileName(kv.getStr("reportfilename"));
        barcodedetail.setBarcodedate(kv.getDate("barcodedate"));//生产日期
    }
}