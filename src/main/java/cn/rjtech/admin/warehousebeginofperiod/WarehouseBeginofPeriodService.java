package cn.rjtech.admin.warehousebeginofperiod;

import static cn.hutool.core.text.StrPool.COMMA;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.hiprint.HiprintTplService;
import cn.jbolt.common.model.HiprintTpl;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.barcodedetail.BarcodedetailService;
import cn.rjtech.admin.barcodemaster.BarcodemasterService;
import cn.rjtech.admin.codingrulem.CodingRuleMService;
import cn.rjtech.admin.cusfieldsmappingd.CusFieldsMappingDService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.scanlog.ScanLogService;
import cn.rjtech.admin.stockbarcodeposition.StockBarcodePositionService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.admin.warehousearea.WarehouseAreaService;
import cn.rjtech.base.service.BaseService;
import cn.rjtech.common.model.Barcodedetail;
import cn.rjtech.common.model.Barcodemaster;
import cn.rjtech.model.momdata.Inventory;
import cn.rjtech.model.momdata.ScanLog;
import cn.rjtech.model.momdata.StockBarcodePosition;
import cn.rjtech.model.momdata.Vendor;
import cn.rjtech.model.momdata.Warehouse;
import cn.rjtech.model.momdata.WarehouseArea;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.wms.utils.StringUtils;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

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
    private BarcodemasterService        barcodemasterService;//条码表
    @Inject
    private BarcodedetailService        barcodedetailService;//条码明细表
    @Inject
    private StockBarcodePositionService barcodePositionService;//条码库存表
    @Inject
    private CodingRuleMService          codingRuleMService;//编码规则
    @Inject
    private WarehouseService            warehouseService;//仓库档案
    @Inject
    private WarehouseAreaService        warehouseAreaService;//库区档案
    @Inject
    private ScanLogService              scanLogService;//扫描日志
    @Inject
    private CusFieldsMappingDService    cusFieldsMappingDService;
    @Inject
    private VendorService               vendorService;
    @Inject
    private InventoryService            inventoryService;
    @Inject
    private HiprintTplService           hiprintTplService;

    /**
     * 数据源
     */
    public Page<Record> datas(Integer pageNumber, Integer pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("warehousebeginofperiod.datas", kv).paginate(pageNumber, pageSize);
        //去重
        List<Record> list = removeDuplicate(paginate.getList());
        for (Record record : list) {
            List<Record> records = findGeneratedstockqtyByCodes(kv, record);
            BigDecimal generatedstockqty = records.stream().map(e -> e.getBigDecimal("qty"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal qty = record.getBigDecimal("qty");
            if (null == generatedstockqty) {
                generatedstockqty = new BigDecimal(0);
            }
            //record.set("ungeneratedstockqty", qty.subtract(generatedstockqty));//未生成条码库存数量
            record.set("generatedstockqty", generatedstockqty);//已生成条码库存数量
            record.set("availablestockqty", records.size());//可用条码数
            record.set("state", "");//已使用、未使用
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
        return dbTemplate("warehousebeginofperiod.findGeneratedstockqtyByCodes", kv).find();
    }

    public Barcodemaster findByAutoid(String autoid) {
        return findFirst("select * from T_Sys_BarcodeMaster where autoid=?", autoid);
    }

    /*
     * 自动条码明细页面加载数据
     * */
    public Page<Record> detailDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("warehousebeginofperiod.detailDatas", kv).paginate(pageNumber, pageSize);
        List<Dictionary> dictionaries = JBoltDictionaryCache.me.getListByTypeKey("beginningofperiod", true);
        for (Record record : paginate.getList()) {
            Dictionary dictionary = dictionaries.stream().filter(e -> e.getSn().equals(record.getStr("reportFileName")))
                .findFirst().orElse(new Dictionary());
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
            for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
                String autoId = idStr;
                //如果是已使用的，不能删除
            }
            return true;
        });
        return SUCCESS;
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
        Ret rets = new Ret();
        boolean result = tx(() -> {
            List<String> list = new ArrayList<>();
            for (Kv kv : kvList) {
                //1、T_Sys_BarcodeMaster--条码表
                kv.set("locksource", "新增期初库存");
                Long masid = null;
                List<Record> positionByKvs = barcodePositionService.findBarcodePositionByKvs(kv);
                if (positionByKvs.isEmpty()) {
                    Barcodemaster barcodemaster = new Barcodemaster();
                    barcodemasterService.saveBarcodemasterModel(barcodemaster, now);
                    Ret masterRet = barcodemasterService.save(barcodemaster);
                    if (masterRet.isFail()) {
                        return false;
                    }
                    masid = barcodemaster.getAutoid();
                } else {
                    masid = positionByKvs.get(0).getLong("locksource");
                }
                commonSaveStock(kv, now, printnum, masid);
                list.add(String.valueOf(masid));
            }
            rets.set("ids", String.join(",", list));
            return true;
        });
        rets.set(ret(result));
        return rets;
    }

    public boolean commonSaveStock(Kv kv, Date now, int printnum, Long masid) {
        ArrayList<Barcodedetail> barcodedetails = new ArrayList<>();
        ArrayList<StockBarcodePosition> positions = new ArrayList<>();

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
            String barcode = BillNoUtils.getcDocNo(getOrgId(), "WL", 5);//todo 生成条码的功能未完成，待改
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
            barcodedetailService.saveBarcodedetailModel(barcodedetail, masid, now, kv, printnum);
            barcodedetails.add(barcodedetail);

            //3、T_Sys_StockBarcodePosition--条码库存表
            StockBarcodePosition position = new StockBarcodePosition();
            position.setQty(kv.getBigDecimal("qty")); //每张条码需要打印的数量
            barcodePositionService.saveBarcodePositionModel(position, kv, now, "新增期初库存", masid);
            positions.add(position);

            //4、write log
            writeLog(barcodedetail, now);
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
        if (StringUtils.isNotBlank(barcode)) {
            fail(barcode + "：条码已存在，不能重复");
            return ret(false);
        }

        Date now = new Date();
        Ret rets = new Ret();
        boolean result = tx(() -> {
            List<String> list = new ArrayList<>();
            boolean save = commonSaveBarcode(kvList, now, printnum, list);
            rets.set("ids", String.join(",", list));
            return save;
        });
        rets.set(ret(result));
        return rets;
    }

    public boolean commonSaveBarcode(List<Kv> kvList, Date now, int printnum, List<String> list) {
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
                if (masterRet.isFail()) {
                    return false;
                }
                masid = barcodemaster.getAutoid();
            } else {
                masid = positionByKvs.get(0).getLong("locksource");
            }
            list.add(String.valueOf(masid));

            //2、T_Sys_BarcodeDetail--条码明细表
            Barcodedetail barcodedetail = new Barcodedetail();
            barcodedetail.setQty(kv.getBigDecimal("generatedstockqty"));//每张条码分配的数量
            barcodedetailService.saveBarcodedetailModel(barcodedetail, masid, now, kv, printnum);
            barcodedetails.add(barcodedetail);

            //3、T_Sys_StockBarcodePosition--条码库存表s
            StockBarcodePosition position = new StockBarcodePosition();
            position.setQty(kv.getBigDecimal("generatedstockqty")); //每张条码需要打印的数量
            barcodePositionService.saveBarcodePositionModel(position, kv, now, "新增期初条码", masid);
            positions.add(position);

            //4、记录日志
            writeLog(barcodedetail, now);
        }
        //5、保存条码表和明细表数据
        barcodedetailService.batchSave(barcodedetails);
        barcodePositionService.batchSave(positions);

        return true;
    }

    /*
     * write log
     * */
    public void writeLog(Barcodedetail barcodedetail, Date now) {
        ScanLog scanLog = new ScanLog();
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

    /**
     * 导入期初库存数据
     */
    public Ret importStockExcel(File file) {
        List<Record> records = cusFieldsMappingDService.getImportRecordsByTableName(file, table());
        if (notOk(records)) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
        }
        Date now = new Date();
        for (Record record : records) {
            //1、get值
            String qty = trimMethods(record.getStr("qty"));//生成条码库存数量
            String reportfilename = trimMethods(record.getStr("reportfilename"));//打印模板标签

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
            Ret ret = checkImportIsBlank(kv, qty, reportfilename, inventory, warehouseList,
                warehouseAreaList, vendor, hiprintTpls);
            if (ret.isFail()) {
                return ret;
            }

            kv.set("cvencode", vendor.getCVenCode());
            kv.set("cwhcode", warehouseList.get(0).getCWhCode());
            kv.set("careacode", warehouseAreaList.get(0).getCareacode());
            kv.set("poscode", warehouseAreaList.get(0).getCareacode());
            kv.set("locksource", "新增期初库存");
            kv.set("vencode", vendor.getCVenCode());
            kv.set("ipkgqty", inventory.getIPkgQty());
            kv.set("generatedstockqty", qty);
            kv.set("reportfilename", hiprintTpls.get(0).getSn());

            //5、保存
            boolean tx = tx(() -> {
                Long masid = null;
                List<Record> positionByKvs = barcodePositionService.findBarcodePositionByKvs(kv);
                if (positionByKvs.isEmpty()) {
                    Barcodemaster barcodemaster = new Barcodemaster();
                    barcodemasterService.saveBarcodemasterModel(barcodemaster, now);
                    Ret masterRet = barcodemasterService.save(barcodemaster);
                    if (masterRet.isFail()) {
                        return false;
                    }
                    masid = barcodemaster.getAutoid();
                } else {
                    masid = positionByKvs.get(0).getLong("locksource");
                }
                boolean result = commonSaveStock(kv, now, 1, masid);
                return result;
            });
            if (!tx) {
                return fail("导入失败");
            }
        }
        return SUCCESS;
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
            //1、get值
            String qty = trimMethods(record.getStr("qty"));//生成条码库存数量
            String barcode = trimMethods(record.getStr("barcode"));//条码号
            String reportfilename = trimMethods(record.getStr("reportFileName"));//打印模板标签
            if (StrUtil.isBlank(barcode)) {
                return fail("条码号不能为空");
            }
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
            Ret ret = checkImportIsBlank(kv, qty, reportfilename, inventory, warehouseList,
                warehouseAreaList, vendor, hiprintTpls);
            if (ret.isFail()) {
                return ret;
            }

            kv.set("cvencode", vendor.getCVenCode());
            kv.set("barcode", barcode);
            kv.set("cwhcode", warehouseList.get(0).getCWhCode());
            kv.set("careacode", warehouseAreaList.get(0).getCareacode());
            kv.set("qty", qty);
            kv.set("poscode", warehouseAreaList.get(0).getCareacode());
            kv.set("locksource", "新增期初库存");
            kv.set("vencode", vendor.getCVenCode());
            kv.set("generatedstockqty", qty);
            kv.set("reportfilename", hiprintTpls.get(0).getSn());
            //
            kvList.add(kv);
        }
        String barcode = checkByBarcode(kvList);
        if (StringUtils.isNotBlank(barcode)) {
            fail(barcode + "：库存中已经存在，不能重复");
            return ret(false);
        }

        //5、保存
        boolean tx = tx(() -> {
            List<String> list = new ArrayList<>();
            boolean save = commonSaveBarcode(kvList, now, 1, list);
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
    public Ret checkImportIsBlank(Kv kv, String qty, String reportfilename, Inventory inventory, List<Warehouse> warehouseList,
                                  List<WarehouseArea> warehouseAreaList, Vendor vendor, List<HiprintTpl> hiprintTpls) {
        if (StrUtil.isBlank(kv.getStr("cwhname"))) {
            return fail("仓库名称不能为空");
        }
        if (StrUtil.isBlank(kv.getStr("cinvcode"))) {
            return fail("存货编码不能为空");
        }
        if (StrUtil.isBlank(qty)) {
            return fail("生成条码库存数量不能为空");
        }
        if (StrUtil.isBlank(kv.getStr("batch"))) {
            return fail("批次号不能为空");
        }
        if (StrUtil.isBlank(reportfilename)) {
            return fail("打印模板标签不能为空");
        }
        if (inventory == null) {
            return fail(kv.getStr("cinvcode") + ": 存货编码不存在，请添加存货后再次导入");
        }
        if (warehouseList.isEmpty()) {
            return fail(kv.getStr("cwhname") + ": 仓库名称不存在，请添加仓库后再次导入");
        } else if (warehouseList.size() > 1) {
            return fail(kv.getStr("cwhname") + ": 仓库名重复，请修改名称后再次导入");
        }
        if (warehouseAreaList.isEmpty()) {
            return fail(kv.getStr("careaname") + ": 库区名不存在，请添加库区后再次导入");
        } else if (warehouseAreaList.size() > 1) {
            return fail(kv.getStr("careaname") + ": 库区名重复，请修改名称后再次导入");
        }
        if (vendor == null) {
            return fail(kv.getStr("cvenname") + ":供应商名不存在，请添加供应商后再次导入");
        }
        if (hiprintTpls.isEmpty()) {
            return fail(reportfilename + ": 打印模板标签不存在，请添加打印模板后再次导入");
        } else if (hiprintTpls.size() > 1) {
            return fail(reportfilename + ": 打印模板标签名重复，无法区分，请修改名称后再次导入");
        }
        return ret(true);
    }

    /*
     * 打印
     * */
    public Object detailPrintData(Kv kv) {
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
            i++;
        }
        return recordList;
    }

    public List<Record> whoptions(Kv kv) {
        List<Record> recordList = dbTemplate("warehousebeginofperiod.whoptions", kv).find();
        for (Record record : recordList) {
            List<Record> records = findGeneratedstockqtyByCodes(kv, record);
            BigDecimal generatedstockqty = records.stream()
                .map(e -> e.getBigDecimal("qty"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (null == generatedstockqty) {
                generatedstockqty = new BigDecimal(0);
            }
            BigDecimal ungeneratedstockqty = record.getBigDecimal("qty").subtract(generatedstockqty);
            record.set("qty", stripTrailingZeros(record.getBigDecimal("qty")));
            record.set("ungeneratedstockqty", stripTrailingZeros(ungeneratedstockqty));
            //未生成条码库存数量
        }
        return recordList;
    }

    public String stripTrailingZeros(BigDecimal bigDecimal) {
        return bigDecimal.stripTrailingZeros().toPlainString();
    }

    public List<Record> findAreaByWhcode() {
        return dbTemplate("warehousebeginofperiod.findAreaByWhcode").find();
    }

    public Page<Record> inventoryAutocomplete(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("warehousebeginofperiod.inventoryAutocomplete", kv).paginate(pageNumber, pageSize);
    }
}