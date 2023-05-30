package cn.rjtech.admin.warehousebeginofperiod;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.barcodedetail.BarcodedetailService;
import cn.rjtech.admin.barcodemaster.BarcodemasterService;
import cn.rjtech.admin.stockbarcodeposition.StockBarcodePositionService;
import cn.rjtech.base.service.BaseService;
import cn.rjtech.common.model.Barcodemaster;

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
    private BarcodemasterService barcodemasterService;//条码表
    @Inject
    private BarcodedetailService barcodedetailService;//条码明细表
    @Inject
    private StockBarcodePositionService barcodePositionService;//条码库存表
    /**
     * 数据源
     */
    public Page<Record> datas(Integer pageNumber, Integer pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("warehousebeginofperiod.datas", kv).paginate(pageNumber, pageSize);
        return paginate;
    }

    /*
     * 保存
     * */
    public Ret save() {
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
    public List<Record> printtpl(Long iautoid) {
        return null;
    }

    public List<Record> whoptions(){
        Kv kv = new Kv();
        return dbTemplate("warehousebeginofperiod.whoptions", kv).find();
    }

}