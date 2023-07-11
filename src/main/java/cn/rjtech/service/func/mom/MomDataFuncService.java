package cn.rjtech.service.func.mom;

import cn.rjtech.base.service.func.BaseMomDataFuncService;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.plugin.activerecord.Record;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * MES 存储过程
 *
 * @author Kephon
 */
public class MomDataFuncService extends BaseMomDataFuncService {

    /**
     * 获取一位流水的工序卡号（唯一） 生成RouteNO
     */
    public String getNextRouteNo(long orgId, String prefix, int billNoLength) {
        return (String) execute((conn) -> {
                    CallableStatement proc = conn.prepareCall("{ call P_Sys_GetNextRouteNo(?, ?, ?, ?) }");
                    proc.setObject(1, orgId);
                    proc.setObject(2, prefix);
                    proc.setObject(3, billNoLength);
                    // 注册输出参数
                    proc.registerOutParameter(4, Types.VARCHAR);

                    proc.execute();
                    return proc.getString(4);
                }
        );
    }

    public String getNextNo(String prefix, int billNoLength) {
        return (String) execute((conn) -> {
                    CallableStatement proc = conn.prepareCall("{ call P_Sys_GetNextNo(?, ?, ?) }");
                    proc.setObject(1, prefix);
                    proc.setObject(2, billNoLength);
                    // 注册输出参数
                    proc.registerOutParameter(3, Types.VARCHAR);
                    proc.execute();
                    return proc.getString(3);
                }
        );
    }
    
    // ----------------------------------------------------------------
    // 生成编码
    // ----------------------------------------------------------------

    /**
     * 生成编码
     *
     * @param orgCode     组织编码
     * @param formId      表单ID
     * @param generateQty 生成数量
     * @return 编码集合
     */
    @SuppressWarnings("unchecked")
    public List<Record> barcodeGenerateForFormID(String orgCode, long formId, int generateQty) {
        return (List<Record>) execute((conn) -> {
                    CallableStatement proc = conn.prepareCall("{ call P_Sys_barcodeGenerateForFormID(?, ?, ?, ?, ?) }");
                    proc.setString(1, orgCode);
                    proc.setLong(2, formId);
                    proc.setInt(3, generateQty);

                    // 注册输出参数
                    proc.registerOutParameter(4, Types.VARCHAR);
                    proc.registerOutParameter(5, Types.VARCHAR);

                    ResultSet rs = proc.executeQuery();

                    // 获取结果集列名
                    ResultSetMetaData rsm = rs.getMetaData();

                    List<String> columnNames = new ArrayList<>();

                    // 获取结果集列名集合
                    int cH = 1;
                    while (cH <= rsm.getColumnCount()) {
                        columnNames.add(rsm.getColumnName(cH).toLowerCase());
                        cH++;
                    }

                    List<Record> list = new ArrayList<>();
                    while (rs.next()) {
                        Record record = new Record();

                        for (int i = 0; i < columnNames.size(); i++) {
                            record.set(columnNames.get(i), rs.getString(i + 1));
                        }

                        list.add(record);
                    }

                    String resultCode = proc.getString(4);
                    String resultInfo = proc.getString(5);

                    ValidationUtils.equals(resultCode, "200", resultInfo);

                    return list;
                }
        );
    }

    /**
     * 生成编码
     *
     * @param orgCode 组织编码
     * @param formId  表单ID
     * @return 编码字段
     */
    public String generateCode(String orgCode, long formId) {
        List<Record> list = barcodeGenerateForFormID(orgCode, formId, 1);
        ValidationUtils.notEmpty(list, "生成编码失败");

        return list.get(0).getStr("barcode");
    }

    /**
     * 生成编码记录，已知字段：barcode、seq、serial
     *
     * @param orgCode 组织编码
     * @param formId  表单ID
     * @return 编码记录
     */
    public Record generateCodeRecord(String orgCode, long formId) {
        List<Record> list = barcodeGenerateForFormID(orgCode, formId, 1);
        ValidationUtils.notEmpty(list, "生成编码失败");

        return list.get(0);
    }
    
    // ----------------------------------------------------------------
    // 生成条码
    // ----------------------------------------------------------------

    /**
     * 生成现品票（条码）
     *
     * @param orgCode        组织编码
     * @param cBarcodeTypeSn 条码类型：1. 物料现品票 2.工单现品票
     * @param generateQty    生成数量
     * @return 编码集合
     */
    @SuppressWarnings("unchecked")
    public List<Record> barcodeGenerateForBarcodeTypeSN(String orgCode, String cBarcodeTypeSn, int generateQty) {
        return (List<Record>) execute((conn) -> {
                    CallableStatement proc = conn.prepareCall("{ call P_Sys_barcodeGenerateForBarcodeTypeSN(?, ?, ?, ?, ?) }");
                    proc.setString(1, orgCode);
                    proc.setString(2, cBarcodeTypeSn);
                    proc.setInt(3, generateQty);

                    // 注册输出参数
                    proc.registerOutParameter(4, Types.VARCHAR);
                    proc.registerOutParameter(5, Types.VARCHAR);

                    ResultSet rs = proc.executeQuery();

                    // 获取结果集列名
                    ResultSetMetaData rsm = rs.getMetaData();

                    List<String> columnNames = new ArrayList<>();

                    // 获取结果集列名集合
                    int cH = 1;
                    while (cH <= rsm.getColumnCount()) {
                        columnNames.add(rsm.getColumnName(cH).toLowerCase());
                        cH++;
                    }

                    List<Record> list = new ArrayList<>();
                    while (rs.next()) {
                        Record record = new Record();

                        for (int i = 0; i < columnNames.size(); i++) {
                            record.set(columnNames.get(i), rs.getString(i + 1));
                        }

                        list.add(record);
                    }

                    String resultCode = proc.getString(4);
                    String resultInfo = proc.getString(5);

                    ValidationUtils.equals(resultCode, "200", resultInfo);

                    return list;
                }
        );
    }

    /**
     * 生成条码
     *
     * @param orgCode        组织编码
     * @param cBarcodeTypeSn 条码类型：1. 物料现品票 2.工单现品票
     * @return 编码字段
     */
    public String generateBarcode(String orgCode, String cBarcodeTypeSn) {
        List<Record> list = barcodeGenerateForBarcodeTypeSN(orgCode, cBarcodeTypeSn, 1);
        ValidationUtils.notEmpty(list, "生成条码失败");

        return list.get(0).getStr("barcode");
    }

    /**
     * 生成条码记录，已知字段：barcode、seq、serial
     *
     * @param orgCode        组织编码
     * @param cBarcodeTypeSn 条码类型：1. 物料现品票 2.工单现品票
     * @return 编码记录
     */
    public Record generateBarcodeRecord(String orgCode, String cBarcodeTypeSn) {
        List<Record> list = barcodeGenerateForBarcodeTypeSN(orgCode, cBarcodeTypeSn, 1);
        ValidationUtils.notEmpty(list, "生成条码失败");

        return list.get(0);
    }

}
