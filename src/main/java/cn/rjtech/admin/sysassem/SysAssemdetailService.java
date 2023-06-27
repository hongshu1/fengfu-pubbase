package cn.rjtech.admin.sysassem;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.RcvDocQcFormM;
import cn.rjtech.model.momdata.SysAssemdetail;
import cn.rjtech.model.momdata.SysPuinstoredetail;
import cn.rjtech.util.ValidationUtils;

import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

/**
 * 组装拆卸及形态转换单明细
 *
 * @ClassName: SysAssemdetailService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 09:47
 */
public class SysAssemdetailService extends BaseService<SysAssemdetail> {

    private final SysAssemdetail dao = new SysAssemdetail().dao();

    @Override
    protected SysAssemdetail dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     * @param keywords   关键词
     * @param SourceType 来源类型
     * @param AssemType  转换状态;转换前 及转换后
     * @param TrackType  跟单类型
     * @param IsDeleted  删除状态：0. 未删除 1. 已删除
     */
    public Page<SysAssemdetail> getAdminDatas(int pageNumber, int pageSize, String keywords, String SourceType, String AssemType,
                                              String TrackType, Boolean IsDeleted) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("SourceType", SourceType);
        sql.eq("AssemType", AssemType);
        sql.eq("TrackType", TrackType);
        sql.eqBooleanToChar("IsDeleted", IsDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords, "WhCodeName", "WhCodeHName");
        //排序
        sql.desc("AutoID");
        return paginate(sql);
    }

    /**
     * 保存
     */
    public Ret save(SysAssemdetail sysAssemdetail) {
        if (sysAssemdetail == null || isOk(sysAssemdetail.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(sysAssemdetail.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysAssemdetail.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(sysAssemdetail.getAutoID(), JBoltUserKit.getUserId(), sysAssemdetail.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(SysAssemdetail sysAssemdetail) {
        if (sysAssemdetail == null || notOk(sysAssemdetail.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        SysAssemdetail dbSysAssemdetail = findById(sysAssemdetail.getAutoID());
        if (dbSysAssemdetail == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(sysAssemdetail.getName(), sysAssemdetail.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysAssemdetail.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(sysAssemdetail.getAutoID(), JBoltUserKit.getUserId(), sysAssemdetail.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysAssemdetail 要删除的model
     * @param kv             携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(SysAssemdetail sysAssemdetail, Kv kv) {
        //addDeleteSystemLog(sysAssemdetail.getAutoID(), JBoltUserKit.getUserId(),sysAssemdetail.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysAssemdetail model
     * @param kv             携带额外参数一般用不上
     */
    @Override
    public String checkInUse(SysAssemdetail sysAssemdetail, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(SysAssemdetail sysAssemdetail, String column, Kv kv) {
        //addUpdateSystemLog(sysAssemdetail.getAutoID(), JBoltUserKit.getUserId(), sysAssemdetail.getName(),"的字段["+column+"]值:"+sysAssemdetail.get(column));
        /**
         switch(column){
         case "IsDeleted":
         break;
         }
         */
        return null;
    }

    public List<Record> findEditTableDatas(Kv para) {
        List<Record> records =null;
        if(null != para.getLong("masid")){
            records = dbTemplate("sysassem.dList", para).find();
        }
        if (records != null && !records.isEmpty()) {
            for (int t = 0; t < records.size(); t++) {
                Record record = records.get(t);
                String cinvcode = record.getStr("cinvcode");
                if (cinvcode == null || "".equals(cinvcode)) {
                    String invcode = record.getStr("invcode");
                    Record firstRecord = findFirstRecord("select t3.cinvname,t3.cInvCode ,t3.cInvCode1,t3.cInvName1,t3.cInvStd as cinvstd,\n" +
                            "t3.iAutoId,uom.cUomCode,uom.cUomName as purchasecuomname,uom.cUomName as  puunitname\n" +
                            "         from Bd_Inventory t3\n" +
                            "         LEFT JOIN Bd_Uom uom on t3.iPurchaseUomId = uom.iAutoId\n" +
                            "         where t3.cInvCode = '" + invcode + "'");
                    record.set("cinvcode",invcode);
                    record.set("cinvcode1",firstRecord.getStr("cinvcode1"));
                    record.set("cinvname1",firstRecord.getStr("cinvname1"));
                    record.set("cinvstd",firstRecord.getStr("cinvstd"));
                    record.set("cuomname",firstRecord.getStr("puunitname"));
                }
            }
        }
        return records;
    }

    /**
     * 批量删除主从表
     */
    public Ret deleteRmRdByIds(String ids) {
        deleteByIds(ids);
        return ret(true);
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        deleteById(id);
        return ret(true);
    }

    public SysAssemdetail saveSysAssemdetailModel(SysPuinstoredetail puinstoredetail, String masId) {
//		sysAssemdetail.setAutoID();
        SysAssemdetail sysAssemdetail = new SysAssemdetail();
        sysAssemdetail.setMasID(masId);
        sysAssemdetail.setBarcode(puinstoredetail.getBarCode());
        sysAssemdetail.setSourceType(puinstoredetail.getSourceBillType());
        sysAssemdetail.setSourceBillNo(puinstoredetail.getSourceBillNo());
        sysAssemdetail.setSourceBillNoRow(puinstoredetail.getSourceBillNoRow());
        sysAssemdetail.setSourceBillID(puinstoredetail.getSourceBillID());
        sysAssemdetail.setSourceBillDid(puinstoredetail.getSourceBillDid());
        //sysAssemdetail.setAssemType("转换状态;转换前 及转换后");
        sysAssemdetail.setWhCode(puinstoredetail.getWhcode());
        sysAssemdetail.setPosCode(puinstoredetail.getPosCode());
        //sysAssemdetail.setCombinationNo("组号");
        sysAssemdetail.setRowNo(puinstoredetail.getRowNo());
        sysAssemdetail.setQty(puinstoredetail.getQty());
        sysAssemdetail.setTrackType(puinstoredetail.getTrackType());
        sysAssemdetail.setMemo(puinstoredetail.getMemo());
        sysAssemdetail.setCcreatename(JBoltUserKit.getUserName());
        sysAssemdetail.setDcreatetime(new Date());
		/*sysAssemdetail.setModifyPerson();
		sysAssemdetail.setModifyDate();
		sysAssemdetail.setIsDeleted();*/
		return sysAssemdetail;
    }
}