package cn.rjtech.admin.warehousebeginofperiod;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.barcodedetail.BarcodedetailService;
import cn.rjtech.admin.barcodemaster.BarcodemasterService;
import cn.rjtech.admin.codingrulem.CodingRuleMService;
import cn.rjtech.admin.stockbarcodeposition.StockBarcodePositionService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.admin.warehousearea.WarehouseAreaService;
import cn.rjtech.admin.warehouseposition.WarehousePositionService;
import cn.rjtech.base.service.BaseService;
import cn.rjtech.common.model.Barcodedetail;
import cn.rjtech.common.model.Barcodemaster;
import cn.rjtech.model.momdata.StockBarcodePosition;
import cn.rjtech.util.BillNoUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
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

    /**
     * 数据源
     */
    public Page<Record> datas(Integer pageNumber, Integer pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("warehousebeginofperiod.datas", kv).paginate(pageNumber, pageSize);
        for (Record record : paginate.getList()) {
            List<StockBarcodePosition> barcodePositionList = barcodePositionList(record.getStr("whcode"),
                record.getStr("invcode"));
            Long generatedstockqty = barcodePositionList.stream().map(e -> e.getQty()).count();
            Long qty = record.getLong("qty");
            if (null == generatedstockqty) {
                generatedstockqty = 0l;
            }
            record.set("ungeneratedstockqty", qty - generatedstockqty);//未生成条码库存数量
            record.set("generatedstockqty", generatedstockqty);//已生成条码库存数量
            record.set("availablestockqty", barcodePositionList.size());//可用条码数
        }
        return paginate;
    }

    public List<StockBarcodePosition> barcodePositionList(String whcode, String invcode) {
        return barcodePositionService.findByWhCodeAndInvCode(whcode, invcode);
    }

    /*
     * 自动条码明细页面加载数据
     * */
    public Page<Record> detailDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("warehousebeginofperiod.detailDatas", kv).paginate(pageNumber, pageSize);
        /*for (Record record : paginate.getList()) {
        }*/
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

    /*
     * 保存新增期初库存
     * */
    public Ret submitStock(JBoltPara jBoltPara) {
        Integer printnum = jBoltPara.getInteger("printnum");//打印张数
        boolean autoprint = jBoltPara.getBoolean("autoprint");//自动打印
        String datas = jBoltPara.getString("datas");//打印张数
        List<Kv> kvList = JSON.parseArray(datas, Kv.class);
        Date now = new Date();
        tx(() -> {
            //1、T_Sys_BarcodeMaster--条码表
            Barcodemaster barcodemaster = new Barcodemaster();
            barcodemasterService.saveBarcodemasterModel(barcodemaster, now);
            Ret masterRet = barcodemasterService.save(barcodemaster);

            ArrayList<Barcodedetail> barcodedetails = new ArrayList<>();
            ArrayList<StockBarcodePosition> positions = new ArrayList<>();
            for (Kv kv : kvList) {
                //生成条码
                String barcode = BillNoUtils.getcDocNo(getOrgId(), "WL", 5);//todo 生成条码的功能未完成，待改
                kv.set("barcode", barcode);
                //2、T_Sys_BarcodeDetail--条码明细表
                Barcodedetail barcodedetail = new Barcodedetail();
                barcodedetailService.saveBarcodedetailModel(barcodedetail, barcodemaster.getAutoid(), now, kv, printnum);
                barcodedetails.add(barcodedetail);

                //3、T_Sys_StockBarcodePosition--条码库存表s
                StockBarcodePosition position = new StockBarcodePosition();
                Record record = findPosCodeByWhcodeAndInvcode(kv.getStr("cwhcode"), kv.getStr("cinvcode"));
                kv.set("poscode", null != record ? record.getStr("poscode") : "");
                barcodePositionService.saveBarcodePositionModel(position, kv, now);
                positions.add(position);
            }
            barcodedetailService.batchSave(barcodedetails);
            barcodePositionService.batchSave(positions);
            return true;
        });
        return ret(true);
    }

    /*
     * 保存新增期初条码
     * */
    public Ret saveBarcode(JBoltPara jBoltPara) {
        JSONArray save = jBoltPara.getJSONArray("save");
        List<String> save1 = jBoltPara.getStringList("save");
        return ret(true);
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
    public Object printtpl(Kv kv) {
        return dbTemplate("warehousearea.containerPrintData", kv).find();
    }

    public List<Record> whoptions() {
        List<Record> recordList = dbTemplate("warehousebeginofperiod.whoptions").find();
        for (Record record : recordList) {
            List<StockBarcodePosition> barcodePositionList = barcodePositionList(record.getStr("whcode"),
                record.getStr("invcode"));
            Long generatedstockqty = barcodePositionList.stream().map(e -> e.getQty()).count();
            if (null == generatedstockqty) {
                generatedstockqty = 0l;
            }
            record.set("qty",record.getBigDecimal("qty").stripTrailingZeros().toPlainString());
            record.set("ungeneratedstockqty", record.getLong("qty") - generatedstockqty);//未生成条码库存数量
        }
        return recordList;
    }

    public List<Record> findAreaByWhcode(String cwhcode) {
        return dbTemplate("warehousebeginofperiod.findAreaByWhcode", Kv.by("cwhcode", cwhcode)).find();
    }

    public Record findPosCodeByWhcodeAndInvcode(String whcode, String invcode) {
        Kv kv = new Kv();
        kv.set("whcode", whcode);
        kv.set("invcode", invcode);
        return dbTemplate("warehousebeginofperiod.findPosCodeByWhcodeAndInvcode", kv).findFirst();
    }

}