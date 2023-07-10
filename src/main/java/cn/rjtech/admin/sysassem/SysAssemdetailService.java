package cn.rjtech.admin.sysassem;

import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.purchaseorderdbatch.PurchaseOrderDBatchService;
import cn.rjtech.admin.syspureceive.SysPureceivedetailService;
import cn.rjtech.model.momdata.*;
import cn.rjtech.model.momdata.SysAssem;
import cn.rjtech.model.momdata.SysAssemdetail;
import cn.rjtech.model.momdata.SysPuinstoredetail;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Inject
    private PurchaseOrderDBatchService purchaseOrderDBatchService;

    @Inject
    private SysAssemService sysassemservice;

    @Inject
    private SysPureceivedetailService pureceivedetailService;

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
        List<Record> records = null;
        List<Record> recordList = new ArrayList<>();
        if (null != para.getLong("masid")) {
            records = dbTemplate("sysassem.dList", para).find();
        }
        // 将转换后 生成条码的数据过滤掉 todo 表改了
        if (records != null && !records.isEmpty()) {
            for (int t = 0; t < records.size(); t++) {
                Record record = records.get(t);
                if("转换前".equals(record.getStr("AssemType")) || ("转换后".equals(record.getStr("AssemType")) && null == record.getStr("barbarcode") )){
                    recordList.add(record);
                }
            }

            for (int t = 0; t < recordList.size(); t++) {
                Record record = recordList.get(t);
                // 获取条码表的存货编码
                String barcode = record.getStr("barcode");
                // 根据条码的存货编码判断，没有存货编码 (转换后的数据)
                if (barcode == null || "".equals(barcode)) {
                    //获取从表转换后的存货编码
                    String invcode = record.getStr("invcode");
                    Record firstRecord = findFirstRecord(
                        "select t3.cinvname,t3.cInvCode ,t3.cInvCode1,t3.cInvName1,t3.cInvStd as cinvstd,\n" +
                            "t3.iAutoId,uom.cUomCode,uom.cUomName,uom.cUomName as purchasecuomname\n" +
                            "         from Bd_Inventory t3\n" +
                            "         LEFT JOIN Bd_Uom uom on t3.iInventoryUomId1 = uom.iAutoId\n" +
                            "         where t3.cInvCode = '" + invcode + "'");
                    if(Objects.nonNull(firstRecord)) {
                        record.set("cinvcode", invcode);
                        record.set("cinvcode1", firstRecord.getStr("cinvcode1"));
                        record.set("cinvname1", firstRecord.getStr("cinvname1"));
                        record.set("cinvstd", firstRecord.getStr("cinvstd"));
                        record.set("cuomname", firstRecord.getStr("cuomname"));
                        Record whcode = findFirstRecord(
                                "select * from Bd_Warehouse where cWhCode = '" + record.getStr("whcodeh") + "'");
                        if(Objects.nonNull(whcode)){
                            record.set("whcode", whcode.getStr("cwhcode"));
                            record.set("whname", whcode.getStr("cwhname"));
                        }else {
                            record.set("whcode", "");
                            record.set("whname", "");
                        }

                        Record poscode = findFirstRecord(
                                "select * from Bd_Warehouse_Area where cAreaCode = '" + record.getStr("poscodeh") + "'");
                        if (null != poscode && !"".equals(poscode)) {
                            record.set("poscode", poscode.getStr("careacode"));
                            record.set("posname", poscode.getStr("careaname"));
                        } else {
                            record.set("poscode", "");
                            record.set("posname", "");
                        }

                    }
                    //查出转换的生成条码的数量
                    Record number = findFirstRecord("select count(1) as number  from T_Sys_AssemBarcode where MasID = ? and Barcode is not null and isDeleted = '0'",record.getStr("autoiddetail"));
                    if(number != null && !"".equals(number)){
                        record.set("number",number.getStr("number"));
                    }
                }
            }
        }
        return recordList;
    }
    public List<Record> findEditTablenumberdetails(Long id){
        List<Record> record = findRecord("select a.cCompleteBarcode as barcode,a.iQty as qty,b.dcreatetime  from PS_PurchaseOrderDBatch a" +
                " left join T_Sys_AssemDetail b on a.iPurchaseOrderdQtyId = b.autoid"+
                " where a.iPurchaseOrderdQtyId ='"+id+"'");
        return record;
    }

    /**
     * 批量删除主从表
     */
    public Ret deleteRmRdByIds(String ids) {
        String[] split = ids.split(",");
        for (String s : split) {
            SysAssemdetail byId1 = findById(s);
            SysAssem byId = sysassemservice.findById(byId1.getMasID());
            if ("2".equals(String.valueOf(byId.getIAuditStatus())) || "1".equals(String.valueOf(byId.getIAuditStatus()))) {
                ValidationUtils.isTrue(false, "编号：" + byId.getBillNo() + "单据状态已改变，不可删除！");
            }
            if (!byId.getIcreateby().equals(JBoltUserKit.getUser().getId())) {
                ValidationUtils.error( "单据创建人为：" + byId.getCcreatename() + " 不可删除!!!");
            }
        }
        deleteByIds(ids);
        return SUCCESS;
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        SysAssemdetail byId1 = findById(id);
        SysAssem byId = sysassemservice.findById(byId1.getMasID());
        if ("2".equals(String.valueOf(byId.getIAuditStatus())) || "1".equals(String.valueOf(byId.getIAuditStatus()))) {
            ValidationUtils.isTrue(false, "编号：" + byId.getBillNo() + "单据状态已改变，不可删除！");
        }
        if (!byId.getIcreateby().equals(JBoltUserKit.getUser().getId())) {
            ValidationUtils.error( "单据创建人为：" + byId.getCcreatename() + " 不可删除!!!");
        }
        deleteById(id);
        return SUCCESS;
    }

    public SysAssemdetail saveSysAssemdetailModel(SysPuinstoredetail detail, String masId) {
        SysAssemdetail first = findFirst("select * from T_Sys_AssemDetail where barcode = ?", detail.getBarCode());
        Date date = new Date();
        SysAssemdetail sysAssemdetail = new SysAssemdetail();
        sysAssemdetail.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
        sysAssemdetail.setMasID(masId);
//        sysAssemdetail.setBarcode(detail.getBarCode());
        sysAssemdetail.setSourceType(detail.getSourceBillType());
        sysAssemdetail.setSourceBillNo(detail.getSourceBillNo());
        sysAssemdetail.setSourceBillNoRow(detail.getSourceBillNoRow());
        sysAssemdetail.setSourceBillID(detail.getSourceBillID());
        sysAssemdetail.setSourceBillDid(detail.getSourceBillDid());
        if (StrUtil.isNotBlank(detail.getSourceBillNo())){
            sysAssemdetail.setAssemType("转换前");//转换状态;转换前 及转换后
        }else {
            sysAssemdetail.setAssemType("转换后");//转换状态;转换前 及转换后
        }
        List<SysPureceivedetail> pureceives = pureceivedetailService
            .findBySourceBillNoAndId(detail.getSourceBillNo(), detail.getSourceBillID());
        if (!pureceives.isEmpty()){
            sysAssemdetail.setCombination(Integer.parseInt(pureceives.get(0).getCombination()));
        }
        sysAssemdetail.setWhCode(detail.getWhcode());
        sysAssemdetail.setPosCode(detail.getPosCode());
        sysAssemdetail.setRowNo(detail.getRowNo());
//        sysAssemdetail.setQty(detail.getQty());
        sysAssemdetail.setTrackType(detail.getTrackType());
        sysAssemdetail.setMemo(detail.getMemo());
//        sysAssemdetail.setBarcode(detail.getBarCode());
        sysAssemdetail.setSourceType(detail.getSourceBillType());
        sysAssemdetail.setSourceBillNo(detail.getSourceBillNo());
        sysAssemdetail.setSourceBillNoRow(detail.getSourceBillNoRow());
        sysAssemdetail.setSourceBillID(detail.getSourceBillID());
        sysAssemdetail.setSourceBillDid(detail.getSourceBillDid());
        if(Objects.nonNull(first)){
            sysAssemdetail.setAssemType(first.getAssemType());
            sysAssemdetail.setCombination(first.getCombination());
        }
        sysAssemdetail.setWhCode(detail.getWhcode());
        sysAssemdetail.setPosCode(detail.getPosCode());
        sysAssemdetail.setRowNo(detail.getRowNo());
//        sysAssemdetail.setQty(detail.getQty());
        sysAssemdetail.setTrackType(detail.getTrackType());
        sysAssemdetail.setMemo(detail.getMemo());
        sysAssemdetail.setCcreatename(JBoltUserKit.getUserName());
        sysAssemdetail.setDcreatetime(date);
        sysAssemdetail.setIcreateby(JBoltUserKit.getUserId());
        sysAssemdetail.setCupdatename(JBoltUserKit.getUserName());
        sysAssemdetail.setDupdatetime(date);
        sysAssemdetail.setIupdateby(JBoltUserKit.getUserId());
        sysAssemdetail.setIsDeleted(false);
        return sysAssemdetail;
    }

    public List<SysAssemdetail> findFirstBy(String masId) {
        return find("select * from  T_Sys_AssemDetail where MasID = ? and isDeleted = '0' and AssemType ='转换前' ", masId);
    }


    public SysAssemdetail findFirst(String masId, Integer combination) {
        return findFirst(
            "select * from  T_Sys_AssemDetail where MasID = ? and isDeleted = '0' and AssemType ='转换后' and Combination = ?",
            masId, combination);
    }

    public List<SysAssemdetail> findFirst(String masId) {
        return find("select * from  T_Sys_AssemDetail where MasID = ? and isDeleted = '0' and AssemType ='转换后' ", masId);
    }

    public List<SysAssemdetail> findFirstByall(Long masId) {
        return find("select * from  T_Sys_AssemDetail where MasID = ? and isDeleted = '0'  ", masId);
    }

}