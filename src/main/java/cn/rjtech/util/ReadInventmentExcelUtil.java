package cn.rjtech.util;

import cn.jbolt.core.util.JBoltStringUtil;
import cn.rjtech.enums.InvestmentEnum;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Record;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Create by Alvin
 * @Description 读取投资计划excel工具类
 * @date 2021-08-25 21:04
 */
public class ReadInventmentExcelUtil {

    private static final Log LOG = Log.getLog(ReadInventmentExcelUtil.class);
    private static final String EXCEL_XLS = ".xls";
    private static final String EXCEL_XLSX = ".xlsx";
    public static final Integer START_ROW = 9;
    public static final Integer START_COLUMN = 2;

    /**
     * 读取excel数据
     *
     * @throws Exception 抛出异常
     */
    public static HashMap<String, Object> readExcelInfo(String url) throws Exception {
    	HashMap<String,Object> map = new HashMap<>();
        /*
         * workbook:工作簿,就是整个Excel文档
         * sheet:工作表
         * row:行
         * cell:单元格
         */


//        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(url)));
//        支持excel2003、2007
        // 创建excel文件对象
        File excelFile = new File(url);
        // 创建输入流对象
        InputStream is = Files.newInputStream(excelFile.toPath());
        checkExcelVaild(excelFile);
        Workbook workbook = getWorkBook(is, excelFile);
//        Workbook workbook = WorkbookFactory.create(is);//同时支持2003、2007、2010
//        获取Sheet数量
        int sheetNum = workbook.getNumberOfSheets();
        LOG.info("sheet数量：" + sheetNum);
//      创建二维数组保存所有读取到的行列数据，外层存行数据，内层存单元格数据
        List<Record> dataList = new ArrayList<Record>();
//        FormulaEvaluator formulaEvaluator = null;
//        遍历工作簿中的sheet,第一层循环所有sheet表
        String iBudgetYear = "";
        String iBudgetType = "";
        String cDepCode = "";
        for (int index = 0; index < sheetNum; index++) {
            Sheet sheet = workbook.getSheetAt(index);
            if (sheet == null) {
                continue;
            }
            iBudgetYear = sheet.getRow(3).getCell(2).getStringCellValue();
            iBudgetType = sheet.getRow(3).getCell(4).getStringCellValue();
            cDepCode = sheet.getRow(3).getCell(6).getStringCellValue();
            // logger.info("表单行数："+sheet.getLastRowNum());
//            如果当前行没有数据跳出循环，第二层循环单sheet表中所有行
            for (int rowIndex = START_ROW; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
//                // 根据文件头可以控制从哪一行读取，在下面if中进行控制
                if (row == null) {
                    continue;
                }
//                遍历每一行的每一列，第三层循环行中所有单元格
                Record cellRecord = new Record();
                for (int cellIndex = START_COLUMN,colIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++,colIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    //@Edit 2021/08/20 16:00pm
                    // 空白单元格
                    if (cell == null) {
                        cellRecord.set(InvestmentEnum.valueOf(colIndex).getField(), null);
                        continue;
                    }
                     cellRecord.set(InvestmentEnum.valueOf(colIndex).getField(), getCellValue(cell));
                }
                dataList.add(cellRecord);
            }
        }
        is.close();
        map.put("ibudgetyear", iBudgetYear);
        map.put("ibudgettype", iBudgetType);
        map.put("cdepcode", cDepCode);
        map.put("rows", dataList);
        return map;
    }

    /**
     * 获取单元格的数据,暂时不支持公式
     */
    public static String getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        String cellValue = "";
        if ("".equals(cell.toString().trim())) {
            return null;
        }

        if (cellType == CellType.STRING) {
            cellValue = cell.getStringCellValue().trim();
            return JBoltStringUtil.isEmpty(cellValue) ? "" : cellValue;
        }
        if (cellType == CellType.NUMERIC) {
            // 判断日期类型
            if (DateUtil.isCellDateFormatted(cell)) {
                // cellValue = DateFormatUtil.formatDurationYMD(cell.getDateCellValue().getTime());
                //cellValue = DateFormatUtil.formatDate(cell.getDateCellValue().getTime());
            } else {  //否
                cellValue = new DecimalFormat("#.######").format(cell.getNumericCellValue());
            }
            return cellValue;
        }
        if (cellType == CellType.BOOLEAN) {
            cellValue = String.valueOf(cell.getBooleanCellValue());
            return cellValue;
        }
        return null;
    }

    /**
     * 判断excel的版本，并根据文件流数据获取workbook
     */
    public static Workbook getWorkBook(InputStream is, File file) throws Exception {
        Workbook workbook = null;
        if (file.getName().endsWith(EXCEL_XLS)) {
            workbook = new HSSFWorkbook(is);
        } else if (file.getName().endsWith(EXCEL_XLSX)) {
            workbook = new XSSFWorkbook(is);
        }
        return workbook;
    }

    /**
     * 校验文件是否为excel
     */
    public static void checkExcelVaild(File file) throws Exception {
        ValidationUtils.isTrue(file.exists(), "文件不存在！");
        ValidationUtils.isTrue(file.isFile() && ( file.getName().endsWith(EXCEL_XLS) || file.getName().endsWith(EXCEL_XLSX)), "文件不是Excel");
    }

    private ReadInventmentExcelUtil() {

    }

}

