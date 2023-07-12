package cn.rjtech.admin.warehousebeginofperiod;

import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.hiprint.HiprintTplService;
import cn.jbolt.common.model.HiprintTpl;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltModelKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.barcodedetail.BarcodedetailService;
import cn.rjtech.admin.barcodemaster.BarcodemasterService;
import cn.rjtech.admin.cusfieldsmappingd.CusFieldsMappingDService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.scanlog.ScanLogService;
import cn.rjtech.admin.stockbarcodeposition.StockBarcodePositionService;
import cn.rjtech.admin.uom.UomService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.admin.warehousearea.WarehouseAreaService;
import cn.rjtech.common.model.Barcodedetail;
import cn.rjtech.common.model.Barcodemaster;
import cn.rjtech.model.momdata.*;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 生成条码 Service
 *
 * @author: 佛山市瑞杰科技有限公司
 */
public class WarehouseBeginofPeriodService extends BaseService<Barcodemaster> {

    private final Barcodemaster dao = new Barcodemaster().dao();

    @Override
    protected Barcodemaster dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    @Inject
    private BarcodemasterService barcodemasterService;//条码表
    @Inject
    private BarcodedetailService barcodedetailService;//条码明细表
    @Inject
    private StockBarcodePositionService barcodePositionService;//条码库存表
    @Inject
    private WarehouseService warehouseService;//仓库档案
    @Inject
    private WarehouseAreaService warehouseAreaService;//库区档案
    @Inject
    private ScanLogService scanLogService;//扫描日志
    @Inject
    private CusFieldsMappingDService cusFieldsMappingDService;
    @Inject
    private VendorService vendorService;
    @Inject
    private InventoryService inventoryService;
    @Inject
    private HiprintTplService hiprintTplService;
    @Inject
    private CusFieldsMappingDService cusFieldsMappingdService;
    @Inject
    private UomService uomService;

    /**
     * 数据源
     */
    public Page<Record> datas(Integer pageNumber, Integer pageSize, Kv kv) {
        kv.set("organizecode", getOrgCode());
        Page<Record> paginate = dbTemplate("warehousebeginofperiod.datas", kv).paginate(pageNumber, pageSize);
        //去重
        List<Record> list = removeDuplicate(paginate.getList());
        for (Record record : list) {
            List<Record> records = findGeneratedstockqtyByCodes(kv, record);
            BigDecimal generatedstockqty = records.stream().map(e -> e.getBigDecimal("qty")).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal qty = record.getBigDecimal("qty");
            if (null == generatedstockqty) {
                generatedstockqty = new BigDecimal(0);
            }
            //record.set("ungeneratedstockqty", qty.subtract(generatedstockqty));//未生成条码库存数量
            record.set("generatedstockqty", generatedstockqty);//已生成条码库存数量
            record.set("availablestockqty", records.size());//可用条码数
            //record.set("state", "");//已使用、未使用
            if (StrUtil.isBlank(record.getStr("poscode"))) {
                record.set("poscode", "");
            }
        }
        paginate.setList(list);
        return paginate;
    }

    public static List<Record> removeDuplicate(List<Record> list) {
        ArrayList<Record> listTemp = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            List<String> masid = listTemp.stream().map(e -> e.getStr("masid")).collect(Collectors.toList());
            if (!masid.contains(list.get(i).getStr("masid"))) {
                listTemp.add(list.get(i));
            }
        }
        return listTemp;
    }

    public List<Record> findGeneratedstockqtyByCodes(Kv kv, Record record) {
        kv.set("whcode", record.getStr("whcode"));
        kv.set("invcode", record.getStr("invcode"));
        kv.set("poscode", record.getStr("poscode"));
        kv.set("organizecode", getOrgCode());
        return dbTemplate("warehousebeginofperiod.findGeneratedstockqtyByCodes", kv).find();
    }

    public Barcodemaster findByAutoid(String autoid) {
        return findFirst("select * from T_Sys_BarcodeMaster where autoid=?", autoid);
    }

    /*
     * 自动条码明细页面加载数据
     * */
    public Page<Record> detailDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        kv.set("organizecode", getOrgCode());
        Page<Record> paginate = dbTemplate("warehousebeginofperiod.detailDatas", kv).paginate(pageNumber, pageSize);
        List<Dictionary> dictionaries = JBoltDictionaryCache.me.getListByTypeKey("beginningofperiod", true);
        for (Record record : paginate.getList()) {
            Dictionary dictionary = dictionaries.stream().filter(e -> e.getSn().equals(record.getStr("reportFileName"))).findFirst().orElse(new Dictionary());
            record.set("sn", record.getStr("reportFileName"));
            record.set("reportFileName", dictionary.getName());
        }
        return paginate;
    }

    /*
     * 保存
     * */
    public Ret save(Barcodemaster barcodemaster) {
        if (barcodemaster == null || isOk(barcodemaster.getAutoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(stockBarcodePosition.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = barcodemaster.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(stockBarcodePosition.getAutoID(), JBoltUserKit.getUserId(), stockBarcodePosition.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        tx(() -> {
            Date date = new Date();
            DateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String format = dateformat.format(date);
            String[] split = ids.split(",");
            List<String> positionids = new ArrayList<>();
            String masid = "";
            for (String autoid : split) {
                Barcodedetail detail = barcodedetailService.findbyAutoid(autoid);
                masid = detail.getMasid();
                //1、如果是已使用的，不能删除

                //2、删除detail表和T_Sys_StockBarcodePosition（条码库存表）
                StockBarcodePosition position = barcodePositionService.findByLockSource(detail.getAutoid());
                if (position != null) {
                    positionids.add(String.valueOf(position.getAutoID()));
                }

                //记录删除日志
                ScanLog scanLog = new ScanLog();
                scanLog.setDeleteDate(format);
                writeLog(detail, date, scanLog);
            }

            //barcodedetailService.deleteByBatchIds(String.join(",", split));
            //barcodePositionService.deleteByBatchIds(String.join(",", positionids));
            for (String autoid : split) {
                barcodedetailService.deletByAutoid(autoid);
            }
            for (String positionid : positionids) {
                barcodePositionService.deletByAutoid(positionid);
            }

            //判断主表数据是否要删除
            List<Barcodedetail> barcodedetails = barcodedetailService.findbyMasid(masid);
            //代表全部要删除，主表数据一起删除
            if (barcodedetails.size() == split.length) {
                deleteByAutoid(masid);
            }
            return true;
        });
        return SUCCESS;
    }

    public int deleteByAutoid(String autoid) {
        Sql sql = deleteSql().eq(Barcodemaster.AUTOID, autoid);
        return delete(sql);
    }

    /*
     * 保存期初库存
     * */
    public Ret submitByStock(JBoltPara jBoltPara) {
        String datas = jBoltPara.getString("datas");//打印张数
        List<Kv> kvList = JSON.parseArray(datas, Kv.class);
        Date now = new Date();
        List<String> list = new ArrayList<>();
        List<Barcodedetail> barcodeDetails = new ArrayList<>();
        List<StockBarcodePosition> positions = new ArrayList<>();
        boolean result = tx(() -> {
            for (Kv kv : kvList) {
                commonSaveStock(kv, now, list, "期初库存");
                commonSubmitMethods(kv, now, list, "期初库存", barcodeDetails, positions);
            }
            //最终将期初库存保存在条码表和条码库存表
            barcodedetailService.batchSave(barcodeDetails);
            barcodePositionService.batchSave(positions);
            return true;
        });
        return successWithData(Kv.by("ids", list));
    }

    /*
     * 保存期初条码
     * */
    public Ret submitAllBybarcode(JBoltPara jBoltPara) {
        String datas = jBoltPara.getString("datas");//打印张数
        List<Kv> kvList = JSON.parseArray(datas, Kv.class);
        Date now = new Date();
        List<String> list = new ArrayList<>();
        List<Barcodedetail> barcodeDetails = new ArrayList<>();
        List<StockBarcodePosition> positions = new ArrayList<>();
        boolean result = tx(() -> {
            for (Kv kv : kvList) {
                commonSaveStock(kv, now, list, "期初条码");
                commonSubmitMethods(kv, now, list, "期初条码", barcodeDetails, positions);
            }
            //最终将期初库存保存在条码表和条码库存表
            barcodedetailService.batchSave(barcodeDetails);
            barcodePositionService.batchSave(positions);
            return true;
        });
        return successWithData(Kv.by("ids", list));
    }

    /*
     * 公共保存方法
     * */
    public void commonSubmitMethods(Kv kv, Date now, List<String> list, String sourceBillType, List<Barcodedetail> barcodeDetails, List<StockBarcodePosition> positions) {
        //1、T_Sys_BarcodeMaster--条码表
        kv.set("LockSource", sourceBillType);
        Long masid = null;
        List<Record> positionByKvs = barcodePositionService.findBarcodePositionByKvs(kv);
        //区分期初条码、期初库存
        if (positionByKvs.isEmpty()) {
            Barcodemaster barcodemaster = new Barcodemaster();
            barcodemasterService.saveBarcodemasterModel(barcodemaster, now);
            ValidationUtils.isTrue(barcodemaster.save(), "保存失败！");
            masid = barcodemaster.getAutoid();
        } else {
            masid = positionByKvs.get(0).getLong("LockSource");
        }
        //2、T_Sys_BarcodeDetail--条码明细表
        Barcodedetail detail = new Barcodedetail();
        detail.setQty(kv.getBigDecimal("qty"));//每张条码分配的数量
        barcodedetailService.saveBarcodedetailModel(detail, masid, now, kv, sourceBillType);
        barcodeDetails.add(detail);
        list.add(detail.getAutoid().toString());

        //3、T_Sys_StockBarcodePosition--条码库存表
        StockBarcodePosition position = new StockBarcodePosition();
        barcodePositionService.saveBarcodePositionModel(position, kv, now, sourceBillType, detail.getAutoid());
        positions.add(position);

        //4、write log
        ScanLog scanLog = new ScanLog();
        writeLog(detail, now, scanLog);
    }

    /*
     * 保存新增期初库存
     * */
    public Ret submitStock(JBoltPara jBoltPara) {
        Integer printnum = jBoltPara.getInteger("printnum");//打印张数
        boolean autoprint = jBoltPara.getBoolean("autoprint");//自动打印
        String datas = jBoltPara.getString("datas");//打印张数
        List<Kv> kvList = JSON.parseArray(datas, Kv.class);
        Date now = new Date();
        List<String> list = new ArrayList<>();
        boolean result = tx(() -> {
            for (Kv kv : kvList) {
                commonSaveStock(kv, now, list, "期初库存");
            }
            return true;
        });
        return successWithData(Kv.by("ids", list));
    }

    public boolean commonSaveStock(Kv kv, Date now, List<String> list, String sourcebilltype) {
        ArrayList<Barcodedetail> barcodedetails = new ArrayList<>();
        ArrayList<StockBarcodePosition> positions = new ArrayList<>();

        //1、T_Sys_BarcodeMaster--条码表
        kv.set("locksource", sourcebilltype);
        Long masid = null;
        List<Record> positionByKvs = barcodePositionService.findBarcodePositionByKvs(kv);
        if (positionByKvs.isEmpty()) {
            Barcodemaster barcodemaster = new Barcodemaster();
            barcodemasterService.saveBarcodemasterModel(barcodemaster, now);
            ValidationUtils.isTrue(barcodemaster.save(), "保存失败！");
            masid = barcodemaster.getAutoid();
        } else {
            masid = positionByKvs.get(0).getLong("locksource");
        }

        //2、生成条码库存数量
        BigDecimal generatedStockQty = kv.getBigDecimal("generatedstockqty");
        //包装数量
        BigDecimal ipkgqty = kv.getBigDecimal("ipkgqty");
        //generatedStockQty ÷ ipkgqty
        BigDecimal divide = generatedStockQty.divide(ipkgqty, 0, BigDecimal.ROUND_UP);
        BigDecimal remainder = generatedStockQty.remainder(ipkgqty).setScale(6, BigDecimal.ROUND_HALF_UP);
        BigDecimal lastScale = remainder.compareTo(new BigDecimal("0")) == 0 ? ipkgqty : remainder;//余数，最后一张条码要打印的数量

        //3、生成条码，并保存参数
        int parseInt = Integer.parseInt(divide.toString());//要生成几次条码
        for (int i = 0; i < parseInt; i++) {
            // 生成条码
            String barcode = BillNoUtils.getcDocNo(getOrgId(), "QC", 5);//todo 生成条码的功能未完成，待改
            kv.set("barcode", barcode);
            int j = i;
            if ((j + 1) == parseInt) {
                kv.set("qty", lastScale);
            } else {
                kv.set("qty", ipkgqty);
            }

            //2、T_Sys_BarcodeDetail--条码明细表
            Barcodedetail barcodedetail = new Barcodedetail();
            barcodedetail.setQty(kv.getBigDecimal("qty"));//每张条码分配的数量
            barcodedetailService.saveBarcodedetailModel(barcodedetail, masid, now, kv, sourcebilltype);
            barcodedetails.add(barcodedetail);
            list.add(barcodedetail.getAutoid().toString());

            //3、T_Sys_StockBarcodePosition--条码库存表
            StockBarcodePosition position = new StockBarcodePosition();
            position.setQty(kv.getBigDecimal("qty")); //每张条码需要打印的数量
            barcodePositionService.saveBarcodePositionModel(position, kv, now, "新增期初库存", barcodedetail.getAutoid());
            positions.add(position);

            //4、write log
            ScanLog scanLog = new ScanLog();
            writeLog(barcodedetail, now, scanLog);
        }
        //5、最终将期初库存保存在条码表和条码库存表
        barcodedetailService.batchSave(barcodedetails);
        barcodePositionService.batchSave(positions);

        return true;
    }

    /*
     * 保存新增期初条码
     * */
    public Ret submitAddBarcode(JBoltPara jBoltPara) {
        int printnum = jBoltPara.getInteger("printnum");//打印张数
        boolean autoprint = jBoltPara.getBoolean("autoprint");//自动打印
        String datas = jBoltPara.getString("datas");//打印张数
        List<Kv> kvList = JSON.parseArray(datas, Kv.class);

        String barcode = checkByBarcode(kvList);
        ValidationUtils.isTrue(!StrUtil.isNotBlank(barcode), barcode + "：条码已存在，不能重复");
        List<String> list = new ArrayList<>();
        Date now = new Date();
        boolean result = tx(() -> {
            boolean save = commonSaveBarcode(kvList, now, printnum, list, "期初条码");
            return save;
        });
        return successWithData(Kv.by("ids", list));
    }

    public boolean commonSaveBarcode(List<Kv> kvList, Date now, int printnum, List<String> list, String sourcebilltype) {
        ArrayList<Barcodedetail> barcodedetails = new ArrayList<>();
        ArrayList<StockBarcodePosition> positions = new ArrayList<>();
        for (Kv kv : kvList) {
            //用OrganizeCode、invcode、VenCode、WhCode、PosCode查询以前是否生成过库存条码，有的话条码表的主键用同一个
            kv.set("locksource", "新增期初条码");
            List<Record> positionByKvs = barcodePositionService.findBarcodePositionByKvs(kv);

            Long masid = null;
            if (positionByKvs.isEmpty()) {
                //1、T_Sys_BarcodeMaster--条码表
                Barcodemaster barcodemaster = new Barcodemaster();
                barcodemasterService.saveBarcodemasterModel(barcodemaster, now);
                Ret masterRet = barcodemasterService.save(barcodemaster);
                ValidationUtils.isTrue(masterRet.isFail(), "保存失败");

                masid = barcodemaster.getAutoid();
            } else {
                masid = positionByKvs.get(0).getLong("locksource");
            }

            //2、T_Sys_BarcodeDetail--条码明细表
            Barcodedetail barcodedetail = new Barcodedetail();
            barcodedetail.setQty(kv.getBigDecimal("generatedstockqty"));//每张条码分配的数量
            barcodedetailService.saveBarcodedetailModel(barcodedetail, masid, now, kv, sourcebilltype);
            barcodedetails.add(barcodedetail);
            list.add(String.valueOf(barcodedetail.getAutoid()));

            //3、T_Sys_StockBarcodePosition--条码库存表s
            StockBarcodePosition position = new StockBarcodePosition();
            position.setQty(kv.getBigDecimal("generatedstockqty")); //每张条码需要打印的数量
            barcodePositionService.saveBarcodePositionModel(position, kv, now, "新增期初条码", barcodedetail.getAutoid());
            positions.add(position);

            //4、记录日志
            ScanLog scanLog = new ScanLog();
            writeLog(barcodedetail, now, scanLog);
        }
        //5、保存条码表和明细表数据
        barcodedetailService.batchSave(barcodedetails);
        barcodePositionService.batchSave(positions);

        return true;
    }

    /*
     * write log
     * */
    public void writeLog(Barcodedetail barcodedetail, Date now, ScanLog scanLog) {
        scanLogService.saveScanLogModel(scanLog, now, barcodedetail);
        scanLogService.save(scanLog);
    }

    public String checkByBarcode(List<Kv> kvList) {
        //条码不能重复
        for (Kv kv : kvList) {
            Barcodedetail findbyBarcode = barcodedetailService.findbyBarcode(kv.getStr("barcode"));
            if (null != findbyBarcode) {
                return findbyBarcode.getBarcode();
            }
        }
        return null;
    }

    public List<Record> getImportData(String list){
        List<Record> fromRecords = JBoltModelKit.getFromRecords(list);
        for (Record record : fromRecords) {
            Warehouse warehouse = warehouseService.findByCwhcode(record.getStr("cwhcode"));
            ValidationUtils.notNull(warehouse, record.getStr("cwhcode") + " 仓库编码不存在");
            Inventory inventory = inventoryService.findBycInvCode(record.getStr("cinvcode"));
            ValidationUtils.notNull(inventory, record.getStr("cinvcode") + " 存货编码不存在");
            Uom uom = uomService.findById(inventory.getIInventoryUomId1());
            List<HiprintTpl> hiprintTpls = hiprintTplService.findHiprintTplByName(record.getStr("reportfilename"));
            ValidationUtils.notEmpty(hiprintTpls, record.getStr("reportfilename") + " 标签打印模板不存在");

            if (StrUtil.isNotBlank(record.getStr("careacode"))) {
                WarehouseArea warehouseArea = warehouseAreaService.findByWhAreaCode(record.getStr("careacode"));
                ValidationUtils.notNull(warehouseArea, record.getStr("careacode") + " 库区编码不存在");
                record.set("careacode", record.getStr("careacode"));
                record.set("careaname", warehouseArea.getCareaname());
            }
            if (StrUtil.isNotBlank(record.getStr("cvencode"))) {
                Vendor vendor = vendorService.findByCode(record.getStr("cvencode"));
                ValidationUtils.notNull(vendor, record.getStr("cvencode") + " 供应商编码不存在");
                record.set("cvencode", record.getStr("cvencode"));
                record.set("cvenname", vendor.getCVenName());
            }
            record.set("generatedstockqty",record.getStr("qty"));
            record.set("cwhname", warehouse.getCWhName());
            record.set("cinvname", inventory.getCInvName());
            record.set("cinvcode1", inventory.getCInvCode1());
            record.set("cinvname1", inventory.getCInvName1());
            record.set("cinvstd", inventory.getCInvStd());
            record.set("ipkgqty", inventory.getIPkgQty());
            record.set("cuomname", uom != null ? uom.getCUomName() : null);//库存主计量单位
            record.set("reportfilename", hiprintTpls.get(0).getSn());
        }
        return fromRecords;
    }

    /*
     * 导入期初条码数据
     * */
    public Ret importBarcodeExcel(File file) {
        List<Record> records = cusFieldsMappingDService.getImportRecordsByTableName(file, table());
        if (notOk(records)) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
        }
        Date now = new Date();
        ArrayList<Kv> kvList = new ArrayList<>();
        for (Record record : records) {
            if (notOk(record.getStr("qty"))) {
                return fail("生成条码库存数量不能为空");
            }
            if (notOk(record.getStr("cWhCode"))) {
                return fail("不能为空");
            }
            if (notOk(record.getStr("cAreaCode"))) {
                return fail("不能为空");
            }
            if (notOk(record.getStr("cInvCode"))) {
                return fail("不能为空");
            }
            if (notOk(record.getStr("batch"))) {
                return fail("不能为空");
            }
            if (notOk(record.getStr("createdate"))) {
                return fail("不能为空");
            }
            if (notOk(record.getStr("barcodedate"))) {
                return fail("不能为空");
            }
            if (notOk(record.getStr("reportfilename"))) {
                return fail("不能为空");
            }
            if (notOk(record.getStr("barcode"))) {
                return fail("不能为空");
            }


            //1、get值
            String qty = trimMethods(record.getStr("qty"));//生成条码库存数量
            String barcode = trimMethods(record.getStr("barcode"));//条码号
            String reportfilename = trimMethods(record.getStr("reportFileName"));//打印模板标签
            ValidationUtils.isTrue(StrUtil.isNotBlank(barcode), barcode + "：条码号不能为空");
            //2、给kv赋值
            Kv kv = new Kv();
            setKvByRecord(kv, record);

            //3、根据名称获取对应的数据
            Inventory inventory = inventoryService.findBycInvCode(kv.getStr("cinvcode"));
            List<Warehouse> warehouseList = warehouseService.findListByWhName(kv.getStr("cwhname"));
            List<WarehouseArea> warehouseAreaList = warehouseAreaService.findAreaListByWhName(kv.getStr("careaname"));
            Vendor vendor = vendorService.findByName(kv.getStr("cvenname"));
            List<HiprintTpl> hiprintTpls = hiprintTplService.findHiprintTplByName(reportfilename);

            //4、判空
            Ret ret = checkImportIsBlank(kv, qty, reportfilename, inventory, warehouseList, warehouseAreaList, vendor, hiprintTpls);
            if (ret.isFail()) {
                return ret;
            }

            String cwhcode = "NULL";
            if (!warehouseList.isEmpty()) {
                cwhcode = warehouseList.get(0).getCWhCode();
            }
            String careacode = "NULL";
            if (!warehouseAreaList.isEmpty()) {
                careacode = warehouseAreaList.get(0).getCareacode();
            }
            String cvencode = "NULL";
            if (vendor != null) {
                cvencode = vendor.getCVenCode();
            }
            String filename = "NULL";
            if (!hiprintTpls.isEmpty()) {
                filename = hiprintTpls.get(0).getSn();
            }

            kv.set("cvencode", cvencode);
            kv.set("barcode", barcode);
            kv.set("cwhcode", cwhcode);
            kv.set("careacode", careacode);
            kv.set("qty", qty);
            kv.set("poscode", careacode);
            kv.set("locksource", "新增期初库存");
            kv.set("vencode", cvencode);
            kv.set("generatedstockqty", qty);
            kv.set("reportfilename", filename);
            //
            kvList.add(kv);
        }
        String barcode = checkByBarcode(kvList);
        ValidationUtils.isTrue(StrUtil.isBlank(barcode), barcode + "：库存中已经存在，不能重复");

        //5、保存
        boolean tx = tx(() -> {
            List<String> list = new ArrayList<>();
            boolean save = commonSaveBarcode(kvList, now, 1, list, "期初条码");
            return save;
        });

        return ret(tx);
    }

    /*
     * 给kv赋值
     * */
    public Kv setKvByRecord(Kv kv, Record record) {
        String cInvCode = trimMethods(record.getStr("cInvCode"));//存货编码
        kv.set("invcode", cInvCode);
        kv.set("cinvcode", cInvCode);
        kv.set("cwhname", trimMethods(record.getStr("cWhName")));//仓库名称
        kv.set("batch", trimMethods(record.getStr("batch")));//批次
        kv.set("careaname", trimMethods(record.getStr("cAreaName")));//库区名称
        kv.set("createDate", trimMethods(record.getStr("createDate")));//生产日期
        kv.set("cvenname", trimMethods(record.getStr("cVenName")));//供应商名称

        return kv;
    }

    /*
     * 去空格
     * */
    public String trimMethods(String str) {
        if (StrUtil.isNotBlank(str)) {
            return str.trim();
        }
        return str;
    }

    /*
     * 检查导入字段是否为空
     * */
    public Ret checkImportIsBlank(Kv kv, String qty, String reportfilename, Inventory inventory, List<Warehouse> warehouseList, List<WarehouseArea> warehouseAreaList, Vendor vendor, List<HiprintTpl> hiprintTpls) {
        //仓库名称
        ValidationUtils.notBlank(kv.getStr("cwhname"), "仓库名称不能为空");
        ValidationUtils.notBlank(kv.getStr("cinvcode"), "存货编码不能为空");
        //条码库存数量
        ValidationUtils.notBlank(qty, "生成条码库存数量不能为空");
        //批次号
        ValidationUtils.notBlank(kv.getStr("batch"), "批次号不能为空");
        //打印模板标签
        ValidationUtils.notBlank(kv.getStr("reportfilename"), "打印模板标签不能为空");
        ValidationUtils.notNull(inventory, kv.getStr("cinvcode") + ": 存货编码不存在，请添加存货后再次导入");
        //仓库名称
        ValidationUtils.notEmpty(warehouseList, kv.getStr("cwhname") + ": 仓库名称不存在，请添加仓库后再次导入");
        ValidationUtils.isTrue(!(warehouseList.size() > 1), kv.getStr("cwhname") + ": 仓库名重复，请修改名称后再次导入");
        //库区名
        ValidationUtils.notEmpty(warehouseAreaList, kv.getStr("careaname") + ": 库区名不存在，请添加库区后再次导入");
        ValidationUtils.isTrue(!(warehouseAreaList.size() > 1), kv.getStr("careaname") + ": 库区名重复，请修改名称后再次导入");
        //供应商名
        ValidationUtils.notNull(vendor, kv.getStr("cvenname") + ":供应商名不存在，请添加供应商后再次导入");
        //打印模板标签
        ValidationUtils.notEmpty(hiprintTpls, reportfilename + ": 打印模板标签不存在，请添加打印模板后再次导入");
        ValidationUtils.isTrue(!(hiprintTpls.size() > 1), reportfilename + ": 打印模板标签名重复，无法区分，请修改名称后再次导入");

        return SUCCESS;
    }

    /*
     * 打印
     * */
    public Object detailPrintData(Kv kv) {
        kv.set("organizecode", getOrgCode());
        List<Record> recordList = dbTemplate("warehousebeginofperiod.printData", kv).find();
        int i = 1;
        for (Record record : recordList) {
            record.set("produceddate", record.getDate("createdate"));//生产线生产日期
            record.set("cworkshiftname", "");//班次
            record.set("jobname", record.getStr("createperson"));//作业员
            record.set("memo", "");//备注
            record.set("workheader", record.getStr("createperson"));//生产组长
            record.set("num", i);//编号
            record.set("total", record.getStr("printnum"));//一共几张
            record.set("qty", record.getBigDecimal("qty").stripTrailingZeros().toPlainString());
            i++;
        }
        return recordList;
    }

    public Object addPrintData(Kv kv) {
        List<Record> recordList = dbTemplate("warehousebeginofperiod.addPrintData", kv).find();
        int i = 1;
        for (Record record : recordList) {
            record.set("produceddate", record.getDate("createdate"));//生产线生产日期
            record.set("cworkshiftname", "");//班次
            record.set("jobname", record.getStr("createperson"));//作业员
            record.set("memo", "");//备注
            record.set("workheader", record.getStr("createperson"));//生产组长
            record.set("num", i);//编号
            record.set("total", record.getStr("printnum"));//一共几张
            record.set("qty", record.getBigDecimal("qty").stripTrailingZeros().toPlainString());
            i++;
        }
        return recordList;
    }


    public List<Record> findAreaByWhcode() {
        Kv kv = new Kv();
        kv.set("corgcode", getOrgCode());
        return dbTemplate("warehousebeginofperiod.findAreaByWhcode", kv).find();
    }

    public List<Record> wareHouseOptions(Kv kv) {
        if (StrUtil.isNotBlank(kv.getStr("q"))) {
            kv.set("cwhcode", kv.getStr("q"));
        }
        List<Record> records = dbTemplate("warehousebeginofperiod.wareHouseOptions", kv.of("isenabled", "true")).find();
        return records;
    }
}