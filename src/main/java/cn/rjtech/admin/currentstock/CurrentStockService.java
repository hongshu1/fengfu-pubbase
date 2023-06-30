package cn.rjtech.admin.currentstock;

import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;

import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.otherdeliverylist.OtherOutDeliveryService;
import cn.rjtech.admin.otheroutdetail.OtherOutDetailService;
import cn.rjtech.admin.stockcheckdetail.StockCheckDetailService;
import cn.rjtech.admin.stockcheckvouchbarcode.StockCheckVouchBarcodeService;
import cn.rjtech.admin.stockcheckvouchdetail.StockCheckVouchDetailService;
import cn.rjtech.admin.stockchekvouch.StockChekVouchService;
import cn.rjtech.admin.sysotherin.SysOtherinService;
import cn.rjtech.admin.sysotherin.SysOtherindetailService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.*;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * 盘点单Service
 *
 * @ClassName: CurrentStockService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2021-11-12 16:01
 */
public class CurrentStockService extends BaseService<StockCheckVouch> implements IApprovalService {

    private final StockCheckVouch dao = new StockCheckVouch().dao();

    @Override
    protected StockCheckVouch dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    Logger log = Logger.getLogger("CurrentStockService");
    @Inject
    StockChekVouchService         stockChekVouchService; //盘点单主表
    @Inject
    StockCheckVouchDetailService  stockCheckVouchDetailService;//T_Sys_StockCheckVouchDetail(盘点明细表)
    @Inject
    StockCheckVouchBarcodeService stockCheckVouchBarcodeService;//T_Sys_StockCheckVouchBarcode(库存盘点-条码明细)
    @Inject
    SysOtherinService             sysOtherinService;//其它入库单
    @Inject
    SysOtherindetailService       sysotherindetailservice;//其它入库单-明细表
    @Inject
    OtherOutDeliveryService       otherOutDeliveryService;//其它出库单
    @Inject
    OtherOutDetailService         otherOutDetailService;//其它出库单-明细表
    @Inject
    StockCheckDetailService       stockCheckDetailService;//todo 弃用，请改为stockCheckVouchDetailService查询

    @Inject
    UserService userService;


    public Page<Record> datas(Integer pageNumber, Integer pageSize, Kv kv) {
        kv.set("organizecode", getOrgCode());
        Page<Record> paginate = dbTemplate("currentstock.datas", kv).paginate(pageNumber, pageSize);
        return paginate;
    }

    /**
     * 应盘料品,已盘料品,未盘料品,盘盈料品,盘亏料品.实时计算
     */
    public Page<Record> datas_calculate(Integer pageNumber, Integer pageSize, Kv kv) {
        Page<Record> paginate = datas(pageNumber, pageSize, kv);
        List<Record> list = paginate.getList();
        String sqlAutoid = "";
        for (Record record : list) {
            Long autoid = record.getLong("autoid");
            sqlAutoid += "'" + autoid + "',";
        }
        sqlAutoid = sqlAutoid.substring(0, sqlAutoid.length() - 1);
        //物料清单
        List<Record> invList = invDatasByIds(sqlAutoid);

        return paginate;
    }


    public Page<Record> getdatas(Integer pageNumber, Integer pageSize, Kv kv) {
        kv.set("organizecode", getOrgCode());
        Page<Record> paginate = dbTemplate("currentstock.getdatas", kv).paginate(pageNumber, pageSize);
        return paginate;
    }


    public Page<Record> CurrentStockByDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        kv.set("organizecode", getOrgCode());
        Page<Record> paginate = dbTemplate("currentstock.CurrentStockByDatas", kv).paginate(pageNumber, pageSize);
        return paginate;
    }

    /**
     * 盘点单新增时候复制细表数据
     */
    public List<Record> CurrentStockd(String whcode, String poscodeSql) {
        Kv kv = Kv.by("organizecode", getOrgCode()).set("whcode", whcode).set("poscodeSql", poscodeSql);
        List<Record> list = dbTemplate("currentstock.CurrentStockd", kv).find();
        return list;
    }

    /**
     * 盘点单物料清单列表
     */
    public List<Record> invDatas(Kv kv) {
        String isApp = kv.getStr("isapp");
        kv.set("orgcode", getOrgCode());

        List<Record> list = new ArrayList<>();
        if ("1".equals(isApp)) {
            list = invAppDatas(kv);
        } else {
            list = invTotalDatas(kv);
        }
        return list;
    }


    /**
     * 盘点单物料清单列表
     */
    public List<Record> invAppDatas(Kv kv) {
        List<Record> list = dbTemplate("currentstock.invDatas", kv).find();
        String mid = kv.getStr("mid");
        //查询后迭代出想要的,减少查询次数
        List<StockCheckDetail> barcodeList = stockCheckDetailService.findByMasid_app(mid);
        for (Record record : list) {
            String did = record.getLong("autoid").toString();
            BigDecimal qty = record.getBigDecimal("qty");

            List<StockCheckDetail> stockcheckdetailList = new ArrayList<>();
            for (StockCheckDetail stockcheckdetail : barcodeList) {
                String sourceID = stockcheckdetail.getSourceID();
                if (StringUtils.isNotBlank(sourceID) && did.equals(sourceID)) {
                    stockcheckdetailList.add(stockcheckdetail);
                }
            }

            //合计真实数量
            BigDecimal totalRealQty = BigDecimal.ZERO;
            //合计调整数量
            BigDecimal totalAdjustQty = BigDecimal.ZERO;

            //核心逻辑,迭代然后计算盈亏数量
            for (StockCheckDetail model : stockcheckdetailList) {
                BigDecimal modelRealQty = model.getRealQty();
                BigDecimal modelAdjustQty = model.getAdjustQty();
                BigDecimal modelQty = model.getQty();

                if (modelAdjustQty != null) {
                    //如果调整数量不为空,则 盘点数量=库存数量+盈亏数量
                    modelRealQty = modelAdjustQty.add(modelQty);
                }
                totalRealQty = totalRealQty.add(modelRealQty);
            }

            if (totalRealQty.compareTo(BigDecimal.ZERO) == 1) {
                //大于0
                record.set("totalRealQty", totalRealQty);
                record.set("isiqc1", totalRealQty.subtract(qty));
                record.set("status", "已盘点");
            } else {
                record.set("status", "未盘点");
            }


        }

        return list;
    }


    /**
     * 盘点单物料清单列表
     */
    public List<Record> invTotalDatas(Kv kv) {
        List<Record> list = dbTemplate("currentstock.invDatas", kv).find();
        String mid = kv.getStr("mid");
        //查询后迭代出想要的,减少查询次数
        List<StockCheckDetail> barcodeList = stockCheckDetailService.findByMasid(mid);
        for (Record record : list) {
            String did = record.getLong("autoid").toString();
            BigDecimal qty = record.getBigDecimal("qty");

            List<StockCheckDetail> stockcheckdetailList = new ArrayList<>();
            for (StockCheckDetail stockcheckdetail : barcodeList) {
                String sourceID = stockcheckdetail.getSourceID();
                if (StringUtils.isNotBlank(sourceID) && did.equals(sourceID)) {
                    stockcheckdetailList.add(stockcheckdetail);
                }
            }

            //合计真实数量
            BigDecimal totalRealQty = BigDecimal.ZERO;
            //合计调整数量
            BigDecimal totalAdjustQty = BigDecimal.ZERO;

            //核心逻辑,迭代然后计算盈亏数量
            for (StockCheckDetail model : stockcheckdetailList) {
                BigDecimal modelRealQty = model.getRealQty();
                BigDecimal modelAdjustQty = model.getAdjustQty();
                BigDecimal modelQty = model.getQty();

                if (modelAdjustQty != null) {
                    //如果调整数量不为空,则 盘点数量=库存数量+盈亏数量
                    modelRealQty = modelAdjustQty.add(modelQty);
                }
                totalRealQty = totalRealQty.add(modelRealQty);
            }

            if (totalRealQty.compareTo(BigDecimal.ZERO) == 1) {
                //大于0
                record.set("totalRealQty", totalRealQty);
                record.set("isiqc1", totalRealQty.subtract(qty));
                record.set("status", "已盘点");
            } else {
                record.set("status", "未盘点");
            }
        }

        return list;
    }


    /**
     * 盘点单物料清单列表
     */
    public Page<Record> barcodeDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        //是app 还是查询全部
        String isapp2 = kv.getStr("isapp2");
        Page<Record> page = null;
        if (isapp2.equals("1")) {
            page = barcodeDatas_APP(pageNumber, pageSize, kv);
        } else {
            //查询全部
            page = barcodeDatas_total(pageNumber, pageSize, kv);
        }
        return page;
    }


    /**
     * 盘点单物料清单列表
     */
    public Page<Record> barcodeDatas_APP(Integer pageNumber, Integer pageSize, Kv kv) {
        kv.set("organizecode", getOrgCode());
        String whcode = kv.getStr("whcode");
        ValidationUtils.notNull(whcode, "仓库为空!");
        Long stockcheckvouchdid = kv.getLong("stockcheckvouchdid");
        StockCheckVouchDetail dbStockcheckvouchdetail = stockCheckVouchDetailService.findById(stockcheckvouchdid);
        if (dbStockcheckvouchdetail != null) {

            Page<Record> paginate = dbTemplate("currentstock.barcodeDatas", kv).paginate(pageNumber, pageSize);
            List<Record> list = paginate.getList();

            for (Record record : list) {
                //数量
                BigDecimal qty = record.getBigDecimal("qty");
                //实盘数量=数量+调整数量
                BigDecimal realqty = record.getBigDecimal("realqty");
                //调整数量=实盘数量-数量
                BigDecimal adjustqty = record.getBigDecimal("adjustqty");
                BigDecimal adjustqty2 = BigDecimal.ZERO;

                if (adjustqty != null) {
                    //如果调整数量为空则
                    record.set("realqty2", qty.add(adjustqty));
                    adjustqty2 = adjustqty;
                } else {

                    adjustqty2 = realqty.subtract(qty);
                    record.set("realqty2", realqty);
                }
                record.set("adjustqty2", adjustqty2);
                record.set("source", "APP");
            }

            return paginate;
        } else {
            return null;
        }

    }


    /**
     * 盘点单物料清单列表
     */
    public Page<Record> barcodeDatas_total(Integer pageNumber, Integer pageSize, Kv kv) {
        kv.set("organizecode", getOrgCode());
        String whcode = kv.getStr("whcode");
        ValidationUtils.notNull(whcode, "仓库为空!");
        Long stockcheckvouchdid = kv.getLong("stockcheckvouchdid");
        StockCheckVouchDetail dbStockcheckvouchdetail = stockCheckVouchDetailService.findById(stockcheckvouchdid);
        if (dbStockcheckvouchdetail != null) {
            String invCode = dbStockcheckvouchdetail.getInvCode();
            String posCode = dbStockcheckvouchdetail.getPosCode();

            kv.set("invcode", invCode);
            kv.set("poscode", posCode);

            Page<Record> paginate = dbTemplate("currentstock.barcodeDatas_total", kv).paginate(pageNumber, pageSize);
            List<Record> list = paginate.getList();

            for (Record record : list) {
                String source = record.getStr("source");
                if (source.equals("app")) {
                    //数量
                    BigDecimal qty = record.getBigDecimal("qty");
                    //实盘数量=数量+调整数量
                    BigDecimal realqty = record.getBigDecimal("realqty");
                    //调整数量=实盘数量-数量
                    BigDecimal adjustqty = record.getBigDecimal("adjustqty");
                    BigDecimal adjustqty2 = BigDecimal.ZERO;

                    if (adjustqty != null) {
                        //如果调整数量为空则
                        record.set("realqty2", qty.add(adjustqty));
                        adjustqty2 = adjustqty;
                    } else {

                        adjustqty2 = realqty.subtract(qty);
                        record.set("realqty2", realqty);
                    }
                    record.set("adjustqty2", adjustqty2);
                } else {
                    //web 端
                    String sourceid = record.getStr("sourceid");
                    if (sourceid != null) {
                        //数量
                        BigDecimal qty = record.getBigDecimal("qty");
                        //实盘数量=数量+调整数量
                        BigDecimal realqty = record.getBigDecimal("realqty");
                        //调整数量=实盘数量-数量
                        BigDecimal adjustqty = record.getBigDecimal("adjustqty");
                        BigDecimal adjustqty2 = BigDecimal.ZERO;

                        if (adjustqty != null) {
                            //如果调整数量为空则
                            record.set("realqty2", qty.add(adjustqty));
                            adjustqty2 = adjustqty;
                        } else {

                            adjustqty2 = realqty.subtract(qty);
                            record.set("realqty2", realqty);
                        }
                        record.set("adjustqty2", adjustqty2);
                    }
                }
            }

            return paginate;
        } else {
            return null;
        }

    }


    /**
     * 仓库
     */
    public List<Record> autocompleteWareHouse(Kv kv) {
        return dbTemplate("currentstock.autocompleteWareHouse", kv).find();
    }

    /**
     * 库位
     */
    public List<Record> autocompletePosition(Kv kv) {
        return dbTemplate("currentstock.autocompletePosition", kv).find();
    }

    /**
     * 盘点人
     */
    public List<Record> autocompleteUser(Kv kv) {
        return dbTemplate("currentstock.autocompleteUser", kv).find();
    }


    public Ret SaveStockchekvouch(Kv kv) {
        //主表
        StockCheckVouch stockchekvouch = new StockCheckVouch();
        //构造数据
        Date date = new Date();
        //创建时间
        stockchekvouch.setDCreateTime(date);
        String dateStr = JBoltDateUtil.format(date, "yyyy-MM-dd");
        stockchekvouch.setBillDate(dateStr);
        String userName = JBoltUserKit.getUserName();
        //创建人
        stockchekvouch.setCCreateName(userName);
        stockchekvouch.setICreateBy(JBoltUserKit.getUserId());
        String orgCode = getOrgCode();
        //组织编码
        stockchekvouch.setOrganizeCode(orgCode);
        String code = BillNoUtils.genCurrentNo();
        //单号
        stockchekvouch.setBillNo(code);
        //获取数据
        String whcode = kv.getStr("whcode");
        //盘点仓库
        stockchekvouch.setWhCode(whcode);
        //盘点人
        stockchekvouch.setCheckPerson(kv.getStr("id"));
        stockchekvouch.setCheckType(kv.getStr("isenabled"));
        kv.getStr("poscode");

        boolean save = stockchekvouch.save();
        return ret(save);
    }

    /**
     * 保存
     */
    public Ret save(StockCheckVouch stockchekvouch) {
        if (stockchekvouch == null || isOk(stockchekvouch.getAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        tx(() -> {
            // ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
            stockchekvouch.setOrganizeCode(getOrgCode());
            stockchekvouch.setBillNo(BillNoUtils.genCurrentNo());
            stockchekvouch.setDCreateTime(new Date());
            ValidationUtils.isTrue(stockchekvouch.save(), ErrorMsg.SAVE_FAILED);

            // TODO 其他业务代码实现

            return true;
        });
        return SUCCESS;
    }

    public Ret saveSubmit(Kv kv) {
        String datas = kv.getStr("datas");
        String barcode = kv.getStr("barcode");
        List<Kv> datasList = JSON.parseArray(datas, Kv.class);
        List<Kv> barcodeList = JSON.parseArray(barcode, Kv.class);
        return SUCCESS;

    }

    /**
     * 盘点单
     */
    public Ret save2(StockCheckVouch stockcheckvouch) {
        final Long[] AutoIDs = {null};
        tx(() -> {
            Date now = new Date();
            String userName = JBoltUserKit.getUserName();
            stockcheckvouch.setBillDate(JBoltDateUtil.format(now, "yyyy-MM-dd"));
            stockcheckvouch.setIAuditStatus(0);
            //创建人
            stockcheckvouch.setCCreateName(JBoltUserKit.getUserName());
            stockcheckvouch.setICreateBy(JBoltUserKit.getUserId());
            stockcheckvouch.setDCreateTime(now);
            String orgCode = getOrgCode() + "";
            //组织编码
            stockcheckvouch.setOrganizeCode(orgCode);
            String code = BillNoUtils.genCurrentNo();
            //单号
            stockcheckvouch.setBillNo(code);
            ValidationUtils.isTrue(stockcheckvouch.save(), "主表保存失败!");

            AutoIDs[0] = stockcheckvouch.getAutoId();
//
//
//			String poscodes = stockcheckvouch.getPoscodes();
//
//			String poscodeSql="";
//			if(poscodes!=null){
//				String[] split = poscodes.split(",");
//				for (String poscode : split) {
//					poscodeSql+="'"+poscode+"',";
//				}
//				if(poscodeSql.length()>0){
//					poscodeSql=poscodeSql.substring(0,poscodeSql.length()-1);
//				}
//			}
//
//			if(StringUtils.isEmpty(poscodeSql)){
//				poscodeSql=null;
//			}
//			String whCode = stockcheckvouch.getWhCode();
//			List<Record> list = CurrentStockd(whCode, poscodeSql);
//			for (Record record : list) {
//				String poscode = record.getStr("poscode");
//				StockCheckVouchDetail stockcheckvouchdetail=new StockCheckVouchDetail();
//				stockcheckvouchdetail.put(record);
//				stockcheckvouchdetail.setMasID(Long.valueOf(String.valueOf(mid)));
//				stockcheckvouchdetail.setCreateDate(now);
//				stockcheckvouchdetail.setCreatePerson(userName);
//				stockcheckvouchdetail.setPosCode(poscode);
//				BigDecimal num = stockcheckvouchdetail.getNum();
//				if(num==null){
//					stockcheckvouchdetail.setNum(BigDecimal.ZERO);
//				}
//				ValidationUtils.isTrue(stockcheckvouchdetail.save(),"细表保存失败!");
//			}
            return true;
        });
        return SUCCESS.set("AutoID", AutoIDs[0]);
    }

    /**
     * 盘点单新增
     */
    public Ret jboltTableSubmit(JBoltTable jBoltTable) {
        tx(() -> {
            JSONObject form = jBoltTable.getForm();
            StockCheckVouch stockcheckvouch = new StockCheckVouch();
            stockcheckvouch.setCheckType(form.getString("checktype"));
            stockcheckvouch.setWhCode(form.getString("whcode"));
            //构造数据
            Date date = new Date();
            //创建时间
            stockcheckvouch.setDCreateTime(date);
            String dateStr = JBoltDateUtil.format(date, "yyyy-MM-dd");
            stockcheckvouch.setBillDate(dateStr);
            String userName = JBoltUserKit.getUserName();
            //创建人
            stockcheckvouch.setCCreateName(userName);
            stockcheckvouch.setICreateBy(JBoltUserKit.getUserId());
            String orgId = getOrgId() + "";
            //组织编码
            stockcheckvouch.setOrganizeCode(orgId);
            String code = BillNoUtils.genCurrentNo();
            //单号
            stockcheckvouch.setBillNo(code);
            //盘点人
            stockcheckvouch.setCheckPerson(userName);
            ValidationUtils.isTrue(stockcheckvouch.save(), "主表保存失败!");

            Long autoId = stockcheckvouch.getAutoId();

            List<Record> saveRecordList = jBoltTable.getSaveRecordList();
            for (Record record : saveRecordList) {
                StockCheckVouchDetail stockcheckvouchdetail = new StockCheckVouchDetail();
                stockcheckvouchdetail.setMasID(Long.valueOf(String.valueOf(autoId)));
                //库位
                String poscode = record.getStr("poscode");
                stockcheckvouchdetail.setPosCode(poscode);
                //存货编码
                String invcode = record.getStr("invcode");
                stockcheckvouchdetail.setInvCode(invcode);
                //数量
                BigDecimal qty = record.getBigDecimal("qty");
                stockcheckvouchdetail.setQty(qty);
                //件数
                BigDecimal num = record.getBigDecimal("num");
                stockcheckvouchdetail.setNum(num);
                //创建人
                stockcheckvouchdetail.setCreatePerson(userName);
                //创建时间
                stockcheckvouchdetail.setCreateDate(date);

                ValidationUtils.isTrue(stockcheckvouchdetail.save(), "细表保存失败!");

            }

            return true;
        });

        return SUCCESS;

    }

    /**
     * 批量查找盘点物料
     */
    public List<Record> invDatasByIds(String ids) {
        return dbTemplate("currentstock.invDatasByIds", Kv.by("ids", ids).set("orgcode", getOrgCode())).find();
    }

    /**
     * 处理审批通过的其他业务操作，如有异常返回错误信息，待审核->审核通过
     *
     * @param formAutoId    单据ID
     * @param isWithinBatch 是否为批量处理
     *                      sysOtherinService 其它入库单
     *                      sysotherindetailservice 其它入库单-明细表
     *                      otherOutDeliveryService 其它出库单
     *                      otherOutDetailService 其它出库单-明细表
     */
    @Override
    public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
        StockCheckVouch stockCheckVouch = stockChekVouchService.findById(formAutoId);
        List<StockCheckVouchBarcode> stockCheckVouchBarcodeList = stockCheckVouchBarcodeService
            .findListByMasId(stockCheckVouch.getAutoId());

        Date now = new Date();
        boolean otherInFlag = true;
        boolean otherOutFlag = true;

        //其它入库
        SysOtherin otherin = new SysOtherin();
        ArrayList<SysOtherindetail> otherInDetailList = new ArrayList<>();
        //其它出库
        OtherOut otherOut = new OtherOut();
        ArrayList<OtherOutDetail> otherOutDetailList = new ArrayList<>();

        for (StockCheckVouchBarcode checkVouchBarcode : stockCheckVouchBarcodeList) {
            BigDecimal qty = checkVouchBarcode.getQty();//账面数量
            BigDecimal realQty = checkVouchBarcode.getRealQty();//实际数量
            BigDecimal bigDecimal = qty.subtract(realQty);//剩余数量

            //判断剩余数量是否为正负数
            int resultQty = bigDecimal.compareTo(BigDecimal.ZERO);
            Record record = findCheckVouchDetailByMasIdAndInvcode(stockCheckVouch.getAutoId(),
                checkVouchBarcode.getInvCode());
            if (resultQty == -1) {
                //1、盘盈要推其它入库单
                BigDecimal moreQty = bigDecimal.negate();//negate：负数转正数--盘盈数量
                saveSysOtherinMode(stockCheckVouch, checkVouchBarcode, record, otherin,
                    otherInDetailList, now, otherInFlag, moreQty);

            } else if (resultQty == 1) {
                //2、盘亏要推其它出库单
                //int lossQty = bigDecimal.intValue();//盘亏数量
                saveSysOtherOutModel(stockCheckVouch, checkVouchBarcode, otherOut, record,
                    otherOutDetailList, now, otherOutFlag, bigDecimal);
            }
        }
        if (otherin != null) {
            ValidationUtils.isTrue(otherin.save(), "推送其它入库单失败！！！");
        }
        if (!otherInDetailList.isEmpty()) {
            sysotherindetailservice.batchSave(otherInDetailList);
        }
        if (otherOut != null) {
            ValidationUtils.isTrue(otherOut.save(), "推送其它出库单失败！！！");
        }
        if (!otherOutDetailList.isEmpty()) {
            otherOutDetailService.batchSave(otherOutDetailList);
        }
        return null;
    }

    public Record findCheckVouchDetailByMasIdAndInvcode(Long masid, String invcdoe) {
        Kv kv = new Kv();
        kv.set("masid", masid);
        kv.set("invcdoe", invcdoe);
        return dbTemplate("currentstock.findCheckVouchDetailByMasIdAndInvcode", kv).findFirst();
    }

    /*
     * 传参给其它入库单
     * */
    public void saveSysOtherinMode(StockCheckVouch stockCheckVouch, StockCheckVouchBarcode checkVouchBarcode, Record record,
                                   SysOtherin otherin, List<SysOtherindetail> otherInDetailList, Date now, boolean otherInFlag,
                                   BigDecimal moreQty) {
        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        if (otherInFlag) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            otherin.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
            otherin.setSourceBillDid(StrUtil.toString(stockCheckVouch.getAutoId()));
            otherin.setRdCode("118");
            otherin.setOrganizeCode(getOrgCode());
            otherin.setBillNo(stockCheckVouch.getBillNo());
            otherin.setBillType("其它入库");
            otherin.setBillDate(formatter.format(now));
            otherin.setWhcode(stockCheckVouch.getWhCode());
            otherin.setIAuditWay(1);
            otherin.setIAuditStatus(1);//待审核
            otherin.setIsDeleted(false);
            otherin.setIcreateby(userId);
            otherin.setCcreatename(userName);
            otherin.setDcreatetime(now);
            otherin.setIupdateby(userId);
            otherin.setCupdatename(userName);
            otherin.setDupdatetime(now);
//            otherin.setSourceBillType();
//            otherin.setVenCode();
//            otherin.setMemo();
//            otherin.setDSubmitTime();
//            otherin.setDAuditTime();
//            otherin.setDeptCode();
//            otherin.setIAuditby();
//            otherin.setCAuditname();

            otherInFlag = false;
        }
        SysOtherindetail otherindetail = new SysOtherindetail();
        otherindetail.setAutoID(JBoltSnowflakeKit.me.nextId());
        otherindetail.setMasID(Long.parseLong(otherin.getAutoID()));
        otherindetail.setPosCode(record.get("poscode"));
        otherindetail.setBarcode(checkVouchBarcode.getBarcode());
        otherindetail.setInvCode(checkVouchBarcode.getInvCode());
        otherindetail.setNum(checkVouchBarcode.getNum());
        otherindetail.setQty(moreQty);
        otherindetail.setSourceBillNo(stockCheckVouch.getBillNo());
        otherindetail.setSourceBIllNoRow(stockCheckVouch.getBillNo() + "-" + (otherInDetailList.size() + 1));
        otherindetail.setSourceBillID(StrUtil.toString(stockCheckVouch.getAutoId()));
        otherindetail.setSourceBillDid(StrUtil.toString(checkVouchBarcode.getAutoID()));
        otherindetail.setIsDeleted(false);
        otherindetail.setIcreateby(userId);
        otherindetail.setCcreatename(userName);
        otherindetail.setDcreatetime(now);
        otherindetail.setIupdateby(userId);
        otherindetail.setCupdatename(userName);
        otherindetail.setDupdatetime(now);

//        otherindetail.setPackRate();
//        otherindetail.setTrackType();
//        otherindetail.setSourceBillType();
//        otherindetail.setMemo();
        //
        otherInDetailList.add(otherindetail);
    }

    /*
     * 传参给其它出库单
     * */
    public void saveSysOtherOutModel(StockCheckVouch stockCheckVouch, StockCheckVouchBarcode checkVouchBarcode, OtherOut otherOut,
                                     Record record, List<OtherOutDetail> otherOutDetailList, Date now, boolean otherOutFlag,
                                     BigDecimal lossQty) {
        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        if (otherOutFlag) {
            otherOut.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
            otherOut.setSourceBillDid(StrUtil.toString(stockCheckVouch.getAutoId()));//来源单据id
            otherOut.setRdCode("210");//其它出库
            otherOut.setOrganizeCode(getOrgCode());
            otherOut.setBillNo(stockCheckVouch.getBillNo());
            otherOut.setBillDate(now);
            otherOut.setWhcode(stockCheckVouch.getWhCode());
            otherOut.setCCreateName(userName);
            otherOut.setDCreateTime(now);
            otherOut.setCUpdateName(userName);
            otherOut.setDupdateTime(now);
            otherOut.setType("OtherOut");
            otherOut.setIAuditWay(1);
            otherOut.setIAuditStatus(1);//待审核
            otherOut.setIsDeleted(false);
            otherOut.setICreateBy(userId);
            otherOut.setIUpdateBy(userId);
            otherOut.setStatus(1);
//            otherOut.setSourceBillType();
//            otherOut.setBillType();
//            otherOut.setReason();
//            otherOut.setDeptCode();
//            otherOut.setVenCode();//供应商
//            otherOut.setMemo();
//            otherOut.setAuditPerson();
//            otherOut.setAuditDate();
//            otherOut.setDSubmitTime();
//            otherOut.setIAuditBy();
//            otherOut.setCAuditName();
//            otherOut.setDAuditTime();
            otherOutFlag = false;
        }

        OtherOutDetail otherOutDetail = new OtherOutDetail();
        otherOutDetail.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
        otherOutDetail.setMasID(otherOut.getAutoID());
        otherOutDetail.setPosCode(record.get("poscode"));
        otherOutDetail.setBarcode(checkVouchBarcode.getBarcode());
        otherOutDetail.setInvCode(checkVouchBarcode.getInvCode());
//        otherOutDetail.setNum(checkVouchBarcode.getNum());
        otherOutDetail.setQty(lossQty);//盘亏数量
//        otherOutDetail.setPackRate();//收容数量
//        otherOutDetail.setTrackType();//跟单类型
//        otherOutDetail.setSourceBillType();
        otherOutDetail.setSourceBillNo(stockCheckVouch.getBillNo());//盘点单号
        otherOutDetail.setSourceBIllNoRow(stockCheckVouch.getBillNo() + "-" + (otherOutDetailList.size() + 1));
        otherOutDetail.setSourceBillID(StrUtil.toString(stockCheckVouch.getAutoId()));
        otherOutDetail.setSourceBillDid(StrUtil.toString(checkVouchBarcode.getAutoID()));
//        otherOutDetail.setMemo();
        otherOutDetail.setIsDeleted(false);
        otherOutDetail.setIcreateby(userId);
        otherOutDetail.setCcreatename(userName);
        otherOutDetail.setDcreatetime(now);
        otherOutDetail.setIupdateby(userId);
        otherOutDetail.setCupdatename(userName);
        otherOutDetail.setDupdatetime(now);
        //
        otherOutDetailList.add(otherOutDetail);
    }

    @Override
    public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
        return null;
    }

    @Override
    public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        return null;
    }

    @Override
    public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        return null;
    }

    /**
     * 提审前业务，如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @return 错误信息
     */
    @Override
    public String preSubmitFunc(long formAutoId) {
        return null;
    }

    /**
     * 提审后业务处理，如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @return 错误信息
     */
    @Override
    public String postSubmitFunc(long formAutoId) {
        return null;
    }

    /**
     * 撤回审核业务处理，如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @return 错误信息
     */
    @Override
    public String postWithdrawFunc(long formAutoId) {
        return null;
    }

    @Override
    public String withdrawFromAuditting(long formAutoId) {
        return null;
    }

    @Override
    public String preWithdrawFromAuditted(long formAutoId) {
        return null;
    }

    @Override
    public String postWithdrawFromAuditted(long formAutoId) {
        return null;
    }

    @Override
    public String postBatchApprove(List<Long> formAutoIds) {
        return null;
    }

    @Override
    public String postBatchReject(List<Long> formAutoIds) {
        return null;
    }

    @Override
    public String postBatchBackout(List<Long> formAutoIds) {
        return null;
    }

}