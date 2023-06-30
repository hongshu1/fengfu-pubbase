package cn.rjtech.util;

import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.poi.excel.JBoltExcelMerge;
import com.jfinal.plugin.activerecord.Record;

import java.util.*;

public class ExcelUtils {
    /**
     * 通过查询结果集获取对应合并行列表 JBoltExcel下标从1开始
     * @param records 查询结果集
     * @param mergeMaps 需要合并的列、字段  key代表需要合并列索引,value代表需要合并字段值 和列对应
     * @param startRow 数据合并起始行
     * @return
     */
    public static List<JBoltExcelMerge> getMergesByData(List<Record> records, Map<Integer,String> mergeMaps, Integer startRow){
        if(records.size()<=1){
            //小于1行不需要合并
            return null;
        }
        List<JBoltExcelMerge> jBoltExcelMergeList=new ArrayList<>();
        Set<Integer> cols = mergeMaps.keySet();
        Map<String,String> values=new HashMap<>(16);
        Map<Integer, Map<String, Integer>> mergeColumns = getMergeColumns(cols);
        for(int i=0,end=records.size();i<end;i++){
            if(i==0){
                for(Integer col:cols){
                    String colValue = mergeMaps.get(col);
                    String value = records.get(i).getStr(colValue);
                    values.put(colValue,value);
                    Map<String, Integer> columnInfo = mergeColumns.get(col);
                    columnInfo.put("firstRow",startRow);
                    columnInfo.put("lastRow",startRow);
                }
            }else{
                for(Integer col:cols){
                    String colValue = mergeMaps.get(col);
                    String currentValue = records.get(i).getStr(colValue);
                    String saveValue = values.get(colValue);
                    Map<String, Integer> columnInfo = mergeColumns.get(col);
                    if(StrUtil.equalsIgnoreCase(currentValue,saveValue)){
                        //相邻值相等
                        columnInfo.put("lastRow",columnInfo.get("lastRow")+1);
                        //判断是否是最后一行 如果是也要输出
                        if((i+1)==end){
                            Integer firstRow = columnInfo.get("firstRow");
                            Integer lastRow = columnInfo.get("lastRow");
                            Integer firstColumn = columnInfo.get("firstColumn");
                            Integer lastColumn = columnInfo.get("lastColumn");
                            JBoltExcelMerge jBoltExcelMerge = JBoltExcelMerge.create(firstRow, lastRow, firstColumn, lastColumn, saveValue, false);
                            jBoltExcelMergeList.add(jBoltExcelMerge);
                        }
                    }else{
                        //相邻值不相等先判断firstRow是否等于lastRow，如果不等则合并
                        Integer firstRow = columnInfo.get("firstRow");
                        Integer lastRow = columnInfo.get("lastRow");
                        if(firstRow!=lastRow){
                            Integer firstColumn = columnInfo.get("firstColumn");
                            Integer lastColumn = columnInfo.get("lastColumn");
                            JBoltExcelMerge jBoltExcelMerge = JBoltExcelMerge.create(firstRow, lastRow, firstColumn, lastColumn, saveValue, false);
                            jBoltExcelMergeList.add(jBoltExcelMerge);
                        }
                        columnInfo.put("firstRow",columnInfo.get("firstRow")+1);
                        columnInfo.put("lastRow",columnInfo.get("lastRow")+1);
                        values.put(colValue,currentValue);
                    }
                }
            }
        }
        if(jBoltExcelMergeList.size()==0){
            return null;
        }
        return jBoltExcelMergeList;
    }

    public static List<JBoltExcelMerge> getMergesByData(List<Record> records, Map<Integer,String> mergeMaps){
        return getMergesByData(records,mergeMaps,2);
    }

    public static Map<Integer,Map<String,Integer>> getMergeColumns(Set<Integer> cols){
        Map<Integer,Map<String,Integer>> mergeColumns=new HashMap<>(16);
        for(Integer col:cols){
            Map<String,Integer> columnInfo=new HashMap<>();
            columnInfo.put("firstRow",0);
            columnInfo.put("lastRow",0);
            columnInfo.put("firstColumn",col);
            columnInfo.put("lastColumn",col);
            mergeColumns.put(col,columnInfo);
        }
        return mergeColumns;
    }
}
