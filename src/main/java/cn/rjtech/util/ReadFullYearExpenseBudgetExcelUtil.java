package cn.rjtech.util;

import cn.jbolt.core.util.JBoltDateUtil;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.rjtech.enums.FullYearBudgetEnum;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Record;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 读取全年预算的费用预算excel工具类
 *
 * @author heming
 * @date 2021-08-25 21:04
 */
public class ReadFullYearExpenseBudgetExcelUtil {

    private static final Log LOG = Log.getLog(ReadFullYearExpenseBudgetExcelUtil.class);
    private static final String EXCEL_XLS = ".xls";
    private static final String EXCEL_XLSX = ".xlsx";
    public static final Integer START_ROW = 8;

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
        List<Record> dataList = new ArrayList<>();
//        FormulaEvaluator formulaEvaluator = null;
//        遍历工作簿中的sheet,第一层循环所有sheet表
        for (int index = 0; index < sheetNum; index++) {
            Sheet sheet = workbook.getSheetAt(index);
            if (sheet == null) {
                continue;
            }
            String startYearStr = sheet.getRow(3).getCell(4).getStringCellValue();
            String startMonthStr = sheet.getRow(3).getCell(5).getStringCellValue();
            String cdepcodeStr = sheet.getRow(3).getCell(6).getStringCellValue();
            String endYearStr = sheet.getRow(4).getCell(4).getStringCellValue();
            String endMonthStr = sheet.getRow(4).getCell(5).getStringCellValue();
            String cbudgetTypeStr = sheet.getRow(4).getCell(6).getStringCellValue();
            String ibudgetyear = sheet.getRow(3).getCell(9).getStringCellValue();
            cbudgetTypeStr = cbudgetTypeStr.substring(cbudgetTypeStr.indexOf("：")+1);
            Integer budgetStartYear = null;
            Integer budgetStartMonth = null;
            Integer budgetEndYear = null;
            Integer budgetEndMonth = null;
            try{
    	    	budgetStartYear = Integer.parseInt(startYearStr.replace("年", ""));
    	    }catch(Exception e){
    	    	ValidationUtils.error( "预算期间起始年份不合法,请检查导入模板!");
    	    }
    	    try{
    	    	budgetStartMonth = Integer.parseInt(startMonthStr.replace("月", ""));
    	    }catch(Exception e){
    	    	ValidationUtils.error( "预算期间起始月份不合法,请检查导入模板!!");
    	    }
    	    try{
    	    	budgetEndYear = Integer.parseInt(endYearStr.replace("年", ""));
    	    }catch(Exception e){
    	    	ValidationUtils.error( "预算期间结束年份不合法,请检查导入模板!");
    	    }
    	    try{
    	    	budgetEndMonth = Integer.parseInt(endMonthStr.replace("月", ""));
    	    }catch(Exception e){
    	    	ValidationUtils.error( "预算期间结束月份不合法,请检查导入模板!");
    	    }
    	    Date cBeginDate = JBoltDateUtil.getDate(budgetStartYear+"-"+budgetStartMonth+"-01", JBoltDateUtil.YMD);
    	    Date cEndDate = JBoltDateUtil.getDate(budgetEndYear+"-"+budgetEndMonth+"-01", JBoltDateUtil.YMD);
            map.put("startyear", startYearStr);
            map.put("startmonth", startMonthStr);
            map.put("cdepcode", cdepcodeStr);
            map.put("endyear", endYearStr);
            map.put("endmonth", endMonthStr);
            map.put("ibudgettype", cbudgetTypeStr);
            map.put("ibudgetyear", ibudgetyear);
//            如果当前行没有数据跳出循环，第二层循环单sheet表中所有行
            for (int rowIndex = START_ROW; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            	//当前行的列是否为合计列，初始不是合计列
            	boolean isTotalColumn = false;
                Row row = sheet.getRow(rowIndex);
//                // 根据文件头可以控制从哪一行读取，在下面if中进行控制
                if (row == null) {
                    continue;
                }
//              遍历每一行的每一列，第三层循环行中所有单元格
                Record cellRecord = new Record();
            	int itemDetailsColumnIndex = 1; //excel中费用预算项目明细部分除合计列的列索引，初始为1，对应是【数量】列
                for (int cellIndex = 2; cellIndex < row.getLastCellNum(); cellIndex++) {
                	if(cellIndex-1 <= FullYearBudgetEnum.values().length){//预算项目字段列逻辑处理
	                    Cell cell = row.getCell(cellIndex);
	                    // 空白单元格
	                    if (cell == null) {
	                        cellRecord.set(FullYearBudgetEnum.valueOf(cellIndex).getField(), null);
	                        continue;
	                    }
	                    cellRecord.set(FullYearBudgetEnum.valueOf(cellIndex).getField(), getCellValue(cell));
                	}else{//预算项目明细列逻辑处理
                		if(isTotalColumn) {
                			isTotalColumn = false;
                			continue;
                		}
                		Cell cell = row.getCell(cellIndex);
                		int monthIndex = (int)Math.ceil(itemDetailsColumnIndex/(double)2); //根据费用预算项目明细部分除合计列的列索引变量计算当前处于第几个月份
                		//excel中预算期间的开始日期加上当前列所处的第几个月份得出当前列所处的年月
                		Calendar calendarStartDate = JBoltDateUtil.getCalendarByDate(cBeginDate);
                		Calendar calendarCutoffTimetDate = JBoltDateUtil.getCalendarByDate(cEndDate);
        				calendarStartDate.add(Calendar.MONTH, monthIndex-1);
        				int nowYear = calendarStartDate.get(Calendar.YEAR);//当前列所处的年份
        				int nowMonth = calendarStartDate.get(Calendar.MONTH)+1;//当前列所处的月份
	                    //如果当前列所处的年份为每年的12月或者是预算期间的结束年月 并且是金额列，则跳过下次循环  ，即跳过合计列
	                    if ( itemDetailsColumnIndex%2 == 0 && (nowMonth == 12 ||
	                            (nowYear == calendarCutoffTimetDate.get(Calendar.YEAR) &&
	                                    calendarStartDate.get(Calendar.MONTH) == calendarCutoffTimetDate.get(Calendar.MONTH)))) {
	                    	isTotalColumn = true;
	                    }
	                    // 空白单元格
	                    if (cell == null) {
	                    	cellRecord.set(itemDetailsColumnIndex%2 == 1 ? "iquantity"+monthIndex : "iamount"+monthIndex, null);
	                    	itemDetailsColumnIndex++;
	                        continue;
	                    }
	                    cellRecord.set(itemDetailsColumnIndex%2 == 1 ? "iquantity"+monthIndex : "iamount"+monthIndex, getCellValue(cell));
	                    itemDetailsColumnIndex++;
                	}
                }
                dataList.add(cellRecord);
            }
        }
        is.close();
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
        if (file.getName().endsWith(EXCEL_XLS)) {
            return new HSSFWorkbook(is);
        } else if (file.getName().endsWith(EXCEL_XLSX)) {
            return new XSSFWorkbook(is);
        }
        return null;
    }

    /**
     * 校验文件是否为excel
     */
    public static void checkExcelVaild(File file) {
        ValidationUtils.isTrue(file.exists(), "文件不存在！");
        ValidationUtils.isTrue(file.isFile() && ( file.getName().endsWith(EXCEL_XLS) || file.getName().endsWith(EXCEL_XLSX)), "文件不是Excel");
    }

    private ReadFullYearExpenseBudgetExcelUtil() {

    }

}

