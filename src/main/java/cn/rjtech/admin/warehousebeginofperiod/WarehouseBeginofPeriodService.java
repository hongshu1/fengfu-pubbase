package cn.rjtech.admin.warehousebeginofperiod;

import static cn.hutool.core.text.StrPool.COMMA;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.barcodedetail.BarcodedetailService;
import cn.rjtech.admin.barcodemaster.BarcodemasterService;
import cn.rjtech.admin.codingrulem.CodingRuleMService;
import cn.rjtech.admin.scanlog.ScanLogService;
import cn.rjtech.admin.stockbarcodeposition.StockBarcodePositionService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.admin.warehousearea.WarehouseAreaService;
import cn.rjtech.base.service.BaseService;
import cn.rjtech.common.model.Barcodedetail;
import cn.rjtech.common.model.Barcodemaster;
import cn.rjtech.model.momdata.ScanLog;
import cn.rjtech.model.momdata.StockBarcodePosition;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.wms.utils.StringUtils;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
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
        boolean result = tx(() -> {
            for (Kv kv : kvList) {
                ArrayList<Barcodedetail> barcodedetails = new ArrayList<>();
                ArrayList<StockBarcodePosition> positions = new ArrayList<>();

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
                    barcodedetailService.saveBarcodedetailModel(barcodedetail, masid, now, kv, printnum);
                    barcodedetails.add(barcodedetail);

                    //3、T_Sys_StockBarcodePosition--条码库存表
                    StockBarcodePosition position = new StockBarcodePosition();
                    barcodePositionService.saveBarcodePositionModel(position, kv, now, "新增期初库存", masid);
                    positions.add(position);
                }
                //4、write log
                writeLog(barcodedetails, now);

                //5、最终将期初库存保存在条码表和条码库存表
                barcodedetailService.batchSave(barcodedetails);
                barcodePositionService.batchSave(positions);
            }
            return true;
        });
        return ret(result);
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
            fail(barcode + "：已存在此条码，不能重复");
            return ret(false);
        }

        Date now = new Date();
        boolean result = tx(() -> {
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

                //2、T_Sys_BarcodeDetail--条码明细表
                Barcodedetail barcodedetail = new Barcodedetail();
                barcodedetailService.saveBarcodedetailModel(barcodedetail, masid, now, kv, printnum);
                barcodedetails.add(barcodedetail);

                //3、T_Sys_StockBarcodePosition--条码库存表s
                StockBarcodePosition position = new StockBarcodePosition();
                barcodePositionService.saveBarcodePositionModel(position, kv, now, "新增期初条码", masid);
                positions.add(position);
            }
            //4、记录日志
            writeLog(barcodedetails, now);

            //5、保存条码表和明细表数据
            barcodedetailService.batchSave(barcodedetails);
            barcodePositionService.batchSave(positions);

            return true;
        });
        return ret(result);
    }

    /*
     * write log
     * */
    public void writeLog(ArrayList<Barcodedetail> barcodedetails, Date now) {
        ScanLog scanLog = new ScanLog();
        Barcodedetail detail = barcodedetails.get(0);
        scanLogService.saveScanLogModel(scanLog, now, new Gson().toJson(barcodedetails), detail);
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
     * 上传excel文件
     */
    public Ret importExcelData(File file) {
        StringBuilder errorMsg = new StringBuilder();
        JBoltExcel jBoltExcel = JBoltExcel
            // 从excel文件创建JBoltExcel实例
            .from(file)
            // 设置工作表信息
            .setSheets(
                JBoltExcelSheet.create("sheet1")
                    // 设置列映射 顺序 标题名称
                    .setHeaders(
                        JBoltExcelHeader.create("cworkclasscode", "工种编码"),
                        JBoltExcelHeader.create("cworkclassname", "工种名称"),
                        JBoltExcelHeader.create("ilevel", "工种等级"),
                        JBoltExcelHeader.create("isalary", "工种薪酬"),
                        JBoltExcelHeader.create("cmemo", "备注")
                    )
                    // 特殊数据转换器
                    .setDataChangeHandler((data, index) -> {
                        try {
                            String isalary = data.getStr("isalary");
                            data.change("isalary", new BigDecimal(isalary));
                        } catch (Exception e) {
                            errorMsg.append(data.getStr("isalary") + "薪酬填写有误");
                        }
                    })
                    // 从第三行开始读取
                    .setDataStartRow(2)
            );
        // 从指定的sheet工作表里读取数据
        /*List<Workclass> models = JBoltExcelUtil.readModels(jBoltExcel, "sheet1", Workclass.class, errorMsg);
        if (notOk(models)) {
            if (errorMsg.length() > 0) {
                return fail(errorMsg.toString());
            } else {
                return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
            }
        }
        if (errorMsg.length() > 0) {
            return fail(errorMsg.toString());
        }
        // 读取数据没有问题后判断必填字段
        if (models.size() > 0) {
            for (Workclass w : models) {
                if (notOk(w.getCworkclasscode())) {
                    return fail("工种编码不能为空");
                }
                if (notOk(w.getCworkclassname())) {
                    return fail("工种名称不能为空");
                }
            }
        }
        savaModelHandle(models);
        // 执行批量操作
        boolean success = tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                batchSave(models);
                return true;
            }
        });
        if (!success) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL);
        }*/
        return SUCCESS;
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