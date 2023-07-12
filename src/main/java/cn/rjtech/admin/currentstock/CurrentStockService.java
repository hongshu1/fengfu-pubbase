package cn.rjtech.admin.currentstock;

import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.otheroutdetail.OtherOutDetailService;
import cn.rjtech.admin.stockcheckvouchbarcode.StockCheckVouchBarcodeService;
import cn.rjtech.admin.stockcheckvouchdetail.StockCheckVouchDetailService;
import cn.rjtech.admin.stockchekvouch.StockChekVouchService;
import cn.rjtech.admin.sysotherin.SysOtherindetailService;
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.*;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

    @Inject
    private StockChekVouchService         stockChekVouchService; //盘点单主表
    @Inject
    private StockCheckVouchDetailService  stockCheckVouchDetailService;//T_Sys_StockCheckVouchDetail(盘点明细表)
    @Inject
    private StockCheckVouchBarcodeService stockCheckVouchBarcodeService;//T_Sys_StockCheckVouchBarcode(库存盘点-条码明细)
    @Inject
    private SysOtherindetailService       sysotherindetailservice;//其它入库单-明细表
    @Inject
    private OtherOutDetailService         otherOutDetailService;//其它出库单-明细表

	public Page<Record> datas(Integer pageNumber, Integer pageSize, Kv kv) {
		kv.set("organizecode",getOrgCode());
        return dbTemplate("currentstock.datas", kv).paginate(pageNumber, pageSize);
	}

    /**
     * 盘点表主表 数据明细
     * */
    public Page<Record> getStockCheckVouchDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        kv.set("organizecode", getOrgCode());
        return dbTemplate("currentstock.getdatas", kv).paginate(pageNumber, pageSize);
    }


    public Page<Record> CurrentStockByDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        kv.set("organizecode", getOrgCode());
        return dbTemplate("currentstock.CurrentStockByDatas", kv).paginate(pageNumber, pageSize);
    }

	/**
	 * 盘点条码 明细
	 */
	public List<Record> getStockCheckVouchBarcodeLines(Kv kv){
		return dbTemplate("currentstock.getStockCheckVouchBarcodeLines",kv).find();
	}

	/**
	 * 获取条码列表
	 * 通过关键字匹配
	 * autocomplete组件使用
	 */
	public Record barcode(Kv kv) {
////		先查询条码是否已添加
        //多个库区处理
        String Str = kv.getStr("poscodes");
        kv.setIfNotNull("poscode",pos(Str));
		Record first = dbTemplate("currentstock.barcodeDatas", kv).findFirst();
        ValidationUtils.notNull(first, "条码为：" + kv.getStr("barcode") + "该现品票没有库存！！！");
        
		return first;
	}

    /**
     * 多个库区处理 通用方法
     * */
	public String pos(String str){
        if (str != null) {
            String[] split = str.split(",");
            StringBuilder poscode = new StringBuilder();
            for (String id : split) {
                poscode.append("'").append(id).append("',");
            }
            poscode = new StringBuilder(poscode.substring(0, poscode.length() - 1));
            return poscode.toString();
        }
        return null;
    }

    /**
     * 盘点单物料详情列表 数据明细
     * */
	public List<Record> StockCheckVouchDetailDatas(Kv kv) {
        kv.set("orgcode", getOrgCode());
        Record Detail = dbTemplate("currentstock.Detail", kv).findFirst();
        //多个库区处理
        String posCodes = kv.getStr("poscodes");
        kv.setIfNotNull("poscode", pos(posCodes));
        if (isNull(Detail.get("masid"))) {
            return warehouseData(kv);
        } else {
            return invTotalDatas(kv);
        }
	}

    /**
     * 盘点单物料清单列表
     */
	public List<Record> invTotalDatas(Kv kv) {
        return dbTemplate("currentstock.paginateAdminDatas", kv).find();
	}

    /**
     * 盘点单物料清单列表
     */
	public List<Record> warehouseData(Kv kv) {
        return dbTemplate("currentstock.WarehouseData", kv).find();
	}

    /**
     * 盘点单物料清单列表
     */
    public Page<Record> barcodeDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        //是app 还是查询全部
        String isapp2 = kv.getStr("isapp2");
        Page<Record> page;
        if ("1".equals(isapp2)) {
            page = barcodeDatas_APP(pageNumber, pageSize, kv);
        } else {
            //查询全部
            page = barcodeDatas_total(pageNumber, pageSize, kv);
        }
        return page;
    }

    /**
     * 从系统导入字段配置，获得导入的数据
     */
    public Ret importExcelClass(File file,Long autoid,String whcode,String poscodes) {
        StringBuilder errorMsg = new StringBuilder();
        JBoltExcel jBoltExcel = JBoltExcel
                //从excel文件创建JBoltExcel实例
                .from(file)
                //设置工作表信息
                .setSheets(
                        JBoltExcelSheet.create("stockcheckvouch")
                                //设置列映射 顺序 标题名称
                                .setHeaders(2,
                                        JBoltExcelHeader.create("Barcode", "现品票"),
                                        JBoltExcelHeader.create("InvCode", "存货编码"),
                                        JBoltExcelHeader.create("Qty", "数量")
                                )
                                //从第三行开始读取
                                .setDataStartRow(3)
                );

        //从指定的sheet工作表里读取数据
        List<Record> StockCheckVouchBarcode = JBoltExcelUtil.readRecords(jBoltExcel, "stockcheckvouch", true, errorMsg);
        if (errorMsg.length() > 0) {
            return fail(errorMsg.toString());
        }
        if (notOk(StockCheckVouchBarcode)) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
        }

        //存放对象的集合
        List<StockCheckVouchBarcode> newList = new ArrayList<>();

        //遍历导入的数据
        int k = 2;
        for (Record record : StockCheckVouchBarcode) {
            String barcode = record.getStr("Barcode");
            String invCode = record.getStr("InvCode");
            String qty = record.getStr("Qty");
            k++;
            if (StrUtil.isBlank(barcode)) {
                return fail("Excel文档中第" + k + "行,数据现品票不能为空");
            }
            if (StrUtil.isBlank(invCode)) {
                return fail("Excel文档中第" + k + "行,存货编码不能为空");
            }
            if (StrUtil.isBlank(qty)) {
                return fail("Excel文档中第" + k + "行,数量不能为空");
            }
            Kv kv = new Kv();
            kv.setIfNotNull("whcode",whcode);//仓库
            kv.setIfNotNull("poscode",poscodes);//库区
            kv.setIfNotNull("invcode",invCode);
            kv.setIfNotNull("barcode",barcode);
            Record first = dbTemplate("currentstock.barcodeDatas",kv).findFirst();
            System.out.println("first===="+ first);
            if (isNull(first)) {
                ValidationUtils.error( "Excel文档中第" + k + "行格式错误！！");
            }
            String iQty = first.getStr("iQty");
            if (qty.compareTo(iQty) > 0){
                ValidationUtils.error( "Excel文档中第" + k + "超出现品票数量！！");
            }
            Date now=new Date();

            //存入数据
            StockCheckVouchBarcode pojo = new StockCheckVouchBarcode();
            pojo.setBarcode(barcode);
            pojo.setInvCode(invCode);
            pojo.setQty(new BigDecimal(qty));
            pojo.setMasID(autoid);
            pojo.setAutoID(JBoltSnowflakeKit.me.nextId());
            pojo.setIcreateby(JBoltUserKit.getUserId());
            pojo.setDcreatetime(now);
            pojo.setCcreatename(JBoltUserKit.getUserName());
            pojo.setIsDeleted(false);
            pojo.setIupdateby(JBoltUserKit.getUserId());
            pojo.setDupdatetime(now);
            pojo.setCupdatename(JBoltUserKit.getUserName());
            newList.add(pojo);
        }
        // 执行批量操作
        tx(() -> {
           stockCheckVouchBarcodeService.batchSave(newList);
            return true;
        });
        return SUCCESS;
    }

    /**
     * 设置参数
     */
    private void setStockCheckVouchBarcode(Record stockCheckVouchBarcodeModel, Long autoid, long userId, String userName, Date now){
        stockCheckVouchBarcodeModel.set("masId", autoid);
        stockCheckVouchBarcodeModel.set("AutoID", JBoltSnowflakeKit.me.nextId());
        stockCheckVouchBarcodeModel.set("isdeleted", ZERO_STR);
        
        stockCheckVouchBarcodeModel.set("icreateby", userId);
        stockCheckVouchBarcodeModel.set("Iupdateby", userId);
        stockCheckVouchBarcodeModel.set("Ccreatename", userName);
        stockCheckVouchBarcodeModel.set("Cupdatename", userName);
        stockCheckVouchBarcodeModel.set("Dcreatetime", now);
        stockCheckVouchBarcodeModel.set("Dupdatetime", now);
    }

    /**
     * 读取excel文件
     */
    public Ret importExcel(File file, Long autoid, String whcode, String poscodes) {
        //使用字段配置维护
        List<Record> importData =  CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, table());
        ValidationUtils.notEmpty(importData, "导入数据不能为空");

        long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        Date now = new Date();
        
        //遍历导入的数据
        int k = 2;
        for (Record record : importData) {
            String barcode = record.getStr("Barcode");
            String invCode = record.getStr("InvCode");
            BigDecimal qty = record.getBigDecimal("Qty");
            
            k++;
            
            if (StrUtil.isBlank(barcode)) {
                return fail("Excel文档中第" + k + "行,数据现品票不能为空");
            }
            if (StrUtil.isBlank(invCode)) {
                return fail("Excel文档中第" + k + "行,存货编码不能为空");
            }
//            if (StrUtil.isBlank((CharSequence) qty)) {
//                return fail("Excel文档中第" + k + "行,数量不能为空");
//            }
            
            Kv kv = new Kv();
            kv.setIfNotNull("whcode", whcode);//仓库
            kv.setIfNotNull("poscode", poscodes);//库区
            kv.setIfNotNull("invcode", invCode);
            kv.setIfNotNull("barcode", barcode);
            
            Record first = dbTemplate("currentstock.barcodeDatas", kv).findFirst();
            System.out.println("first====" + first);
            if (isNull(first)) {
                ValidationUtils.error("Excel文档中第" + k + "行,格式错误！！");
            }
            BigDecimal iQty = first.getBigDecimal("iQty");
            if (qty.compareTo(iQty) > 0) {
                ValidationUtils.error("Excel文档中第" + k + "行,超出现品票数量！！");
            }

            setStockCheckVouchBarcode(record, autoid, userId, userName, now);
        }
        
        // 执行批量操作
        boolean success=tx(() -> {
            stockCheckVouchBarcodeService.batchSaveRecords(importData);
            return true;
        });

        if(!success) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL);
        }
        return SUCCESS;
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
                if ("app".equals(source)) {
                    //数量
                    BigDecimal qty = record.getBigDecimal("qty");
                    //实盘数量=数量+调整数量
                    BigDecimal realqty = record.getBigDecimal("realqty");
                    //调整数量=实盘数量-数量
                    BigDecimal adjustqty = record.getBigDecimal("adjustqty");
                    BigDecimal adjustqty2;

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
                        BigDecimal adjustqty2;

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
     *  库区数据源
     */
    public List<Record> getposHouseDatas(Kv kv) {
        return dbTemplate("currentstock.posHouse", kv).find();
    }
    /**
     *  料品分类数据源
     */
    public List<Record> getInventoryClassDatas(Kv kv) {
        return dbTemplate("currentstock.InventoryClass", kv).find();
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
        stockchekvouch.setDcreatetime(date);
        String dateStr = JBoltDateUtil.format(date, "yyyy-MM-dd");
        stockchekvouch.setBillDate(dateStr);
        String userName = JBoltUserKit.getUserName();
        //创建人
        stockchekvouch.setCcreatename(userName);
        stockchekvouch.setIcreateby(JBoltUserKit.getUserId());
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
            stockchekvouch.setDcreatetime(new Date());
            ValidationUtils.isTrue(stockchekvouch.save(), ErrorMsg.SAVE_FAILED);

            // TODO 其他业务代码实现

            return true;
        });
        return SUCCESS;
    }

    /**
     * 更新
     */
    public Ret update(StockCheckVouch stockchekvouch) {
        if(stockchekvouch==null || notOk(stockchekvouch.getAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        StockCheckVouch stockCheckVouch=findById(stockchekvouch.getAutoId());
        if(stockCheckVouch==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
        //if(existsName(sysPuinstore.getName(), sysPuinstore.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success=stockchekvouch.update();
        if(success) {
            //添加日志
            //addUpdateSystemLog(sysPuinstore.getAutoid(), JBoltUserKit.getUserId(), sysPuinstore.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        tx(() -> {
            //软删除盘点单
            update("UPDATE T_Sys_StockCheckVouch SET isDeleted = 1 WHERE AutoID = "+ids+" ");
            update("UPDATE T_Sys_StockCheckVouchBarcode SET isDeleted = 1 WHERE MasID = "+ids+" ");
            update("UPDATE T_Sys_StockCheckVouchDetail SET isDeleted = 1 WHERE MasID = "+ids+" ");
            return true;
        });
        return SUCCESS;
    }

    /**
     * 导出订单列表
     */
    public List<Record> download(Kv kv, String sqlTemplate) {
        kv.setIfNotNull("OrganizeCode",getOrgCode());
        return dbTemplate(sqlTemplate, kv).find();
    }

    public Ret saveSubmit(Kv kv) {
        String datas = kv.getStr("datas");
        String barcode = kv.getStr("barcode");
        List<Kv> datasList = JSON.parseArray(datas, Kv.class);
        List<Kv> barcodeList = JSON.parseArray(barcode, Kv.class);
        return SUCCESS;
    }

    public Ret saveTableSubmit(JBoltTableMulti jBoltTable) {
        // 盘点明细
        JBoltTable StockCheckVouchDetail = jBoltTable.getJBoltTable("table1");
        // 盘点条码
        JBoltTable StockCheckVouchBarcode = jBoltTable.getJBoltTable("table2");

        if (StockCheckVouchDetail.paramsIsNotBlank()) {
            System.out.println(StockCheckVouchDetail.getParams().toJSONString());
        }

        //	当前操作人员  当前时间 单号
        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        Date nowDate = new Date();
        String OrgCode =getOrgCode();

        System.out.println("saveTable===>" + StockCheckVouchDetail.getSave());
        System.out.println("updateTable===>" + StockCheckVouchDetail.getUpdate());
        System.out.println("deleteTable===>" + StockCheckVouchDetail.getDelete());
        System.out.println("form===>" + StockCheckVouchDetail.getForm());

        //  检查填写是否有误
        List<Record> detailSaveRecordList = StockCheckVouchDetail.getSaveRecordList();
        List<Record> detailUpdateRecordList = StockCheckVouchDetail.getUpdateRecordList();
        List<Record> detailRecordList = new ArrayList<>();

        if (StockCheckVouchDetail.saveIsNotBlank()) {
            detailRecordList.addAll(detailSaveRecordList);
        }

        if (StockCheckVouchDetail.updateIsNotBlank()) {
            detailRecordList.addAll(detailUpdateRecordList);
        }

        System.out.println("saveTable===>" + StockCheckVouchBarcode.getSave());
        System.out.println("updateTable===>" + StockCheckVouchBarcode.getUpdate());
        System.out.println("deleteTable===>" + StockCheckVouchBarcode.getDelete());
        System.out.println("form===>" + StockCheckVouchBarcode.getForm());

        //  检查填写是否有误
        List<Record> barcodeSaveRecordList = StockCheckVouchBarcode.getSaveRecordList();
        List<Record> barcodeUpdateRecordList = StockCheckVouchBarcode.getUpdateRecordList();
        List<Record> barcodeRecordList = new ArrayList<>();

        if (StockCheckVouchBarcode.saveIsNotBlank()) {
            barcodeRecordList.addAll(barcodeSaveRecordList);
        }

        if (StockCheckVouchBarcode.updateIsNotBlank()) {
            barcodeRecordList.addAll(barcodeUpdateRecordList);
        }

        final Long[] AutoIDs = {null};
        tx(()->{
            Long headerId = null;
            // 获取Form对应的数据
            if (StockCheckVouchDetail.formIsNotBlank()) {
                // 主表form
                StockCheckVouch stockCheckVouch = StockCheckVouchDetail.getFormModel(StockCheckVouch.class, "stockchekvouch");

                //	行数据为空 不保存
                if (stockCheckVouch.getAutoId() == null && !StockCheckVouchDetail.saveIsNotBlank() && !StockCheckVouchDetail.updateIsNotBlank() && !StockCheckVouchDetail.deleteIsNotBlank()) {
                    ValidationUtils.error( "请先添加行数据！");
                }

                if (stockCheckVouch.getAutoId() == null) {
//					保存
//					审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
                    stockCheckVouch.setIAuditStatus(0);

                    //创建人
                    stockCheckVouch.setIcreateby(userId);
                    stockCheckVouch.setCcreatename(userName);
                    stockCheckVouch.setDcreatetime(nowDate);

                    //更新人
                    stockCheckVouch.setIupdateby(userId);
                    stockCheckVouch.setCupdatename(userName);
                    stockCheckVouch.setDupdatetime(nowDate);

                    stockCheckVouch.setOrganizeCode(OrgCode);
                    stockCheckVouch.setIsDeleted(false);
                    save(stockCheckVouch);
                    headerId = stockCheckVouch.getAutoId();
                }else {
                    //更新人
//					保存
//					审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
                    stockCheckVouch.setIAuditStatus(0);
                    //更新人
                    stockCheckVouch.setIupdateby(userId);
                    stockCheckVouch.setCupdatename(userName);
                    stockCheckVouch.setDupdatetime(nowDate);
                    update(stockCheckVouch);
                    headerId = stockCheckVouch.getAutoId();
                }
                AutoIDs[0] = stockCheckVouch.getAutoId();
            }

            /***
             * 盘点明细
             * */
            // 获取待保存数据 执行保存
            if (StockCheckVouchDetail.saveIsNotBlank()) {
                List<StockCheckVouchDetail> lines = StockCheckVouchDetail.getSaveModelList(StockCheckVouchDetail.class);

                Long finalHeaderId = headerId;
                lines.forEach(stockCheckVouchDetail -> {
                    stockCheckVouchDetail.setMasID(finalHeaderId);
                    //创建人
                    stockCheckVouchDetail.setIcreateby(userId);
                    stockCheckVouchDetail.setDcreatetime(nowDate);
                    stockCheckVouchDetail.setCcreatename(userName);
                    //更新人
                    stockCheckVouchDetail.setIupdateby(userId);
                    stockCheckVouchDetail.setDupdatetime(nowDate);
                    stockCheckVouchDetail.setCupdatename(userName);
                });
                stockCheckVouchDetailService.batchSave(lines);
            }
            // 获取待更新数据 执行更新
            if (StockCheckVouchDetail.updateIsNotBlank()) {
                List<StockCheckVouchDetail> lines = StockCheckVouchDetail.getUpdateModelList(StockCheckVouchDetail.class);

                lines.forEach(stockCheckVouchDetail -> {
                    //更新人
                    stockCheckVouchDetail.setIupdateby(userId);
                    stockCheckVouchDetail.setDupdatetime(nowDate);
                    stockCheckVouchDetail.setCupdatename(userName);
                });
                stockCheckVouchDetailService.batchUpdate(lines);
            }
            // 获取待删除数据 执行删除
            if (StockCheckVouchDetail.deleteIsNotBlank()) {
                stockCheckVouchDetailService.deleteByIds(StockCheckVouchDetail.getDelete());
            }

            /***
             * 条码明细
             * */
            // 获取待保存数据 执行保存
            if (StockCheckVouchBarcode.saveIsNotBlank()) {
                List<StockCheckVouchBarcode> lines = StockCheckVouchBarcode.getSaveModelList(StockCheckVouchBarcode.class);

                Long finalHeaderId = headerId;
                lines.forEach(stockCheckVouchBarcode -> {
                    stockCheckVouchBarcode.setMasID(finalHeaderId);
                    //创建人
                    stockCheckVouchBarcode.setIcreateby(userId);
                    stockCheckVouchBarcode.setDcreatetime(nowDate);
                    stockCheckVouchBarcode.setCcreatename(userName);
                    //更新人
                    stockCheckVouchBarcode.setIupdateby(userId);
                    stockCheckVouchBarcode.setDupdatetime(nowDate);
                    stockCheckVouchBarcode.setCupdatename(userName);
                });
                stockCheckVouchBarcodeService.batchSave(lines);
            }
            // 获取待更新数据 执行更新
            if (StockCheckVouchBarcode.updateIsNotBlank()) {
                List<StockCheckVouchBarcode> lines = StockCheckVouchBarcode.getUpdateModelList(StockCheckVouchBarcode.class);

                lines.forEach(stockCheckVouchBarcode -> {
                    //更新人
                    stockCheckVouchBarcode.setIupdateby(userId);
                    stockCheckVouchBarcode.setDupdatetime(nowDate);
                    stockCheckVouchBarcode.setCupdatename(userName);
                });
                stockCheckVouchBarcodeService.batchUpdate(lines);
            }
            // 获取待删除数据 执行删除
            if (StockCheckVouchBarcode.deleteIsNotBlank()) {
                stockCheckVouchBarcodeService.deleteByIds(StockCheckVouchBarcode.getDelete());
            }

            return true;
        });
        return SUCCESS.set("AutoID", AutoIDs[0]);
    }



    /**
     * 盘点单
     */
    public Ret save2(StockCheckVouch stockcheckvouch) {
        final Long[] AutoIDs = {null};
        tx(() -> {
            Date now = new Date();
            stockcheckvouch.setBillDate(JBoltDateUtil.format(now, "yyyy-MM-dd"));
            stockcheckvouch.setIAuditStatus(0);
            //创建人
            stockcheckvouch.setCcreatename(JBoltUserKit.getUserName());
            stockcheckvouch.setIcreateby(JBoltUserKit.getUserId());
            stockcheckvouch.setDcreatetime(now);
            String orgCode = getOrgCode();
            //组织编码
            stockcheckvouch.setOrganizeCode(orgCode);
            String code = BillNoUtils.genCurrentNo();
            //单号
            stockcheckvouch.setBillNo(code);
            ValidationUtils.isTrue(stockcheckvouch.save(), "主表保存失败!");
            AutoIDs[0] = stockcheckvouch.getAutoId();
            return true;
        });
        return SUCCESS.set("AutoID", AutoIDs[0]);
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
        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        Date nowDate = new Date();
        stockCheckVouch.setIauditby(userId);
        stockCheckVouch.setCauditname(userName);
        stockCheckVouch.setDAuditTime(nowDate);
        stockCheckVouch.update();
        List<StockCheckVouchDetail> checkVouchDetailList = stockCheckVouchDetailService.findListByMasId(stockCheckVouch.getAutoId());

        Date now = new Date();
        boolean otherInFlag = true;
        boolean otherOutFlag = true;

        //其它入库
        SysOtherin otherin = new SysOtherin();
        ArrayList<SysOtherindetail> otherInDetailList = new ArrayList<>();
        //其它出库
        OtherOut otherOut = new OtherOut();
        ArrayList<OtherOutDetail> otherOutDetailList = new ArrayList<>();

        for (StockCheckVouchDetail vouchDetail : checkVouchDetailList) {
           /* BigDecimal qty = vouchDetail.getQty();//账面数量
            BigDecimal realQty = vouchDetail.getRealQty();//实际数量
            BigDecimal bigDecimal = qty.subtract(realQty);//剩余数量*/
            BigDecimal plqtyQty = vouchDetail.getPlqtyQty();//盈亏数量
            //判断剩余数量是否为正负数
            int resultQty = plqtyQty.compareTo(BigDecimal.ZERO);
            List<Record> recordList = findCheckVouchBarcodeByMasIdAndInvcode(stockCheckVouch.getAutoId(),
                    vouchDetail.getInvCode());
            if (resultQty == -1) {
                //1、盘亏要推其它出库单
                BigDecimal lossQty = plqtyQty.negate();//negate：负数转正数--盘盈数量
                //int lossQty = bigDecimal.intValue();//盘亏数量
                saveSysOtherOutModel(stockCheckVouch, vouchDetail, otherOut, recordList,
                        otherOutDetailList, now, otherOutFlag, lossQty);

            } else if (resultQty == 1) {
                //2、盘盈要推其它入库单
                saveSysOtherinMode(stockCheckVouch, vouchDetail, recordList, otherin,
                        otherInDetailList, now, otherInFlag, plqtyQty);
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

    public List<Record> findCheckVouchBarcodeByMasIdAndInvcode(Long masid, String invcdoe) {
        Kv kv = new Kv();
        kv.set("masid", masid);
        kv.set("invcdoe", invcdoe);
        return dbTemplate("currentstock.findCheckVouchBarcodeByMasIdAndInvcode", kv).find();
    }

    /*
     * 传参给其它入库单
     * */
    public void saveSysOtherinMode(StockCheckVouch stockCheckVouch, StockCheckVouchDetail vouchDetail, List<Record> recordList,
                                   SysOtherin otherin, List<SysOtherindetail> otherInDetailList, Date now, boolean otherInFlag,
                                   BigDecimal plqtyQty) {
        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        if (otherInFlag) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            otherin.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
            otherin.setSourceBillDid(StrUtil.toString(stockCheckVouch.getAutoId()));
            otherin.setRdCode("118");//入库类别
            otherin.setOrganizeCode(getOrgCode());
            otherin.setBillNo(stockCheckVouch.getBillNo());
//            otherin.setBillType("其它入库");
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
        for (Record record : recordList) {
            SysOtherindetail otherindetail = new SysOtherindetail();
            otherindetail.setAutoID(JBoltSnowflakeKit.me.nextId());
            otherindetail.setMasID(Long.parseLong(otherin.getAutoID()));
            otherindetail.setPosCode(vouchDetail.getPosCode());
            otherindetail.setBarcode(record.getStr("barcode"));
            otherindetail.setInvCode(vouchDetail.getInvCode());
            otherindetail.setNum(vouchDetail.getNum());
            otherindetail.setQty(record.getBigDecimal("qty"));
            otherindetail.setSourceBillNo(stockCheckVouch.getBillNo());
            otherindetail.setSourceBIllNoRow(stockCheckVouch.getBillNo() + "-" + (otherInDetailList.size() + 1));
            otherindetail.setSourceBillID(StrUtil.toString(stockCheckVouch.getAutoId()));
            otherindetail.setSourceBillDid(StrUtil.toString(vouchDetail.getAutoID()));
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
    }

    /*
     * 传参给其它出库单
     * */
    public void saveSysOtherOutModel(StockCheckVouch stockCheckVouch, StockCheckVouchDetail vouchDetail, OtherOut otherOut,
                                     List<Record> recordList, List<OtherOutDetail> otherOutDetailList, Date now, boolean otherOutFlag,
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
            otherOut.setType("StockCheckVouch");
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
        for (Record record : recordList) {
            OtherOutDetail otherOutDetail = new OtherOutDetail();
            otherOutDetail.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
            otherOutDetail.setMasID(otherOut.getAutoID());
            otherOutDetail.setPosCode(vouchDetail.getPosCode());
            otherOutDetail.setBarcode(record.getStr("barcode"));
            otherOutDetail.setInvCode(vouchDetail.getInvCode());
//        otherOutDetail.setNum(checkVouchBarcode.getNum());
            otherOutDetail.setQty(record.getBigDecimal("qty"));
//            otherOutDetail.setQty(lossQty);//盘亏数量
//        otherOutDetail.setPackRate();//收容数量
//        otherOutDetail.setTrackType();//跟单类型
//        otherOutDetail.setSourceBillType();
            otherOutDetail.setSourceBillNo(stockCheckVouch.getBillNo());//盘点单号
            otherOutDetail.setSourceBIllNoRow(stockCheckVouch.getBillNo() + "-" + (otherOutDetailList.size() + 1));
            otherOutDetail.setSourceBillID(StrUtil.toString(stockCheckVouch.getAutoId()));
            otherOutDetail.setSourceBillDid(StrUtil.toString(vouchDetail.getAutoID()));
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