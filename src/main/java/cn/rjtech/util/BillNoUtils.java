package cn.rjtech.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.rjtech.cache.FormCache;
import cn.rjtech.service.func.mom.MomDataFuncService;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

/**
 * 卡号生成工具类
 *
 * @author Kephon
 */
public class BillNoUtils {

    private static final MomDataFuncService MOM_DATA_FUNC_SERVICE = Aop.get(MomDataFuncService.class);

    /**
     * 使用 synchronized 对象锁
     */
    private static final byte[] LOCK = new byte[0];

    private BillNoUtils() {
        // ignored
    }

    /**
     * 生成禀议系统相关单号
     */
    public static String genProposalSystemNo(long orgId, String u9Docno, boolean useAllQty, int billNoLength) {
        synchronized (LOCK) {
            String prefix = u9Docno;
            return useAllQty ? prefix + "01" : MOM_DATA_FUNC_SERVICE.getNextRouteNo(orgId, prefix, billNoLength);
        }
    }


    /**
     * 生成工序卡号
     */
    public static String genRouterecordNo(long orgId, String u9Docno, boolean useAllQty, int billNoLength) {
        synchronized (LOCK) {
            String prefix = u9Docno + "-";
            return useAllQty ? prefix + "01" : MOM_DATA_FUNC_SERVICE.getNextRouteNo(orgId, prefix, billNoLength);
        }
    }

    /**
     * 生成单号，指定字符串前缀 + 6位随机数, 如20200918000000 (前缀+14位)
     *
     * @param prefix 前缀
     * @return 单号
     */
    private static String genBillNo(String prefix) {
        return prefix + DateUtil.format(new Date(), "yyyyMMdd") + RandomUtil.randomNumbers(6);
    }


    /**
     * 来料异常品记录
     */
    public static String getcDocNo(long ordId, String prefix, int billNoLength) {
        prefix = prefix + DateUtil.format(new Date(), "yyMMdd");
        return MOM_DATA_FUNC_SERVICE.getNextRouteNo(ordId, prefix, billNoLength);
    }


    /**
     * 质检问题
     */
    public static String genQcProblemNo() {
        return genBillNo("QCP");
    }

    /**
     * 生成质检日报表编号
     */
    public static String genQcDayReportNo() {
        return genBillNo("QCDR");
    }

    /**
     * 报工记录
     */
    public static String genRoutingReportNo() {
        return genBillNo("RP");
    }

    /**
     * 维修记录
     */
    public static String genRepairNo() {
        return genBillNo("RAR");
    }

    /**
     * 维修记录
     */
    public static String genPrNo() {
        return genBillNo("PR");
    }

    /**
     * 委外单号
     */
    public static String getOutsourceNo() {
        return genBillNo("WW");
    }

    /**
     * 验收
     */
    public static String getAcceptNo() {
        return genBillNo("YS");
    }

    /**
     * 盘点单
     */
    public static String genCurrentNo() {
        return genBillNo("PD");
    }

    /**
     * 条码编码
     */
    public static String getCassiGnOrderNo(String itemCategoryName, Long orgId) {
        String prefix = "";
        if ("焊接件".equals(itemCategoryName)) {
            prefix = "H";
        } else if ("冲压件".equals(itemCategoryName)) {
            prefix = "C";
        }
        return genCassiGnOrderNo(orgId, prefix, 4);
    }

    /**
     * 条料加工
     */
    public static String genCutStripProcessNo(long orgid, String prefix, int billNoLength) {
        synchronized (LOCK) {
            return MOM_DATA_FUNC_SERVICE.getNextRouteNo(orgid, prefix + DateUtil.format(new Date(), "yyyyMMdd"), billNoLength);
        }
    }

    public static String genCutStripProcessPackNo(long orgid, String prefix, int billNoLength) {
        synchronized (LOCK) {
            return MOM_DATA_FUNC_SERVICE.getNextRouteNo(orgid, prefix, billNoLength);
        }
    }

    public static String genCassiGnOrderNo(long orgId, String prefix, int billNoLength) {
        synchronized (LOCK) {
            return MOM_DATA_FUNC_SERVICE.getNextRouteNo(orgId, prefix + DateUtil.format(new Date(), "yyyyMMdd"), billNoLength);
        }
    }

    public static String getSRMOfBillNo(long orgId, String prefix, int billNoLength) {
        synchronized (LOCK) {
            return MOM_DATA_FUNC_SERVICE.getNextRouteNo(orgId, prefix + DateUtil.format(new Date(), "yyMMdd"), billNoLength);
        }
    }

    public static String getCompleteRptNo(long orgId, String prefix, int billNoLength) {
        synchronized (LOCK) {
            return MOM_DATA_FUNC_SERVICE.getNextRouteNo(orgId, prefix + DateUtil.format(new Date(), "yyMMdd"), billNoLength);
        }
    }

    private char[] getLowerCaseLetter() {
        char[] letter = new char[26];
        for (int i = 0; i < 26; i++) {
            letter[i] = (char) ('a' + i);
        }
        return letter;
    }

    private char[] getUpperCaseLetter() {
        char[] letter = getLowerCaseLetter();
        for (int i = 0; i < letter.length; i++) {
            letter[i] = Character.toUpperCase(letter[i]);
        }
        return letter;
    }

    public String getMaterialsBill(Long orgId, Long a) {
        return null;
    }

    /**
     * 生成编码
     *
     * @param orgCode   组织编码
     * @param cformcode 表单编码，传入table()
     */
    public static String genCode(String orgCode, String cformcode) {
        return MOM_DATA_FUNC_SERVICE.generateCode(orgCode, FormCache.ME.getFormId(cformcode));
    }

    /**
     * 生成多个编码值
     *
     * @param orgCode   组织编码
     * @param cformcode 表单编码，传入table()
     */
    public static List<String> genCodes(String orgCode, String cformcode, int generateQty) {
        List<Record> codeRecords = MOM_DATA_FUNC_SERVICE.barcodeGenerateForFormID(orgCode, FormCache.ME.getFormId(cformcode), generateQty);
        return CollUtil.getFieldValues(codeRecords, "barcode", String.class);
    }

    /**
     * 生成编码记录，已知字段：barcode、seq、serial
     *
     * @param orgCode   组织编码
     * @param cformcode 表单编码，传入table()
     */
    public static Record genCodeRecord(String orgCode, String cformcode) {
        return MOM_DATA_FUNC_SERVICE.generateCodeRecord(orgCode, FormCache.ME.getFormId(cformcode));
    }

    /**
     * 生成多个编码记录，已知字段：barcode、seq、serial
     *
     * @param orgCode     组织编码
     * @param cformcode   表单编码，传入table()
     * @param generateQty 生成数量
     */
    public static List<Record> genCodeRecords(String orgCode, String cformcode, int generateQty) {
        return MOM_DATA_FUNC_SERVICE.barcodeGenerateForFormID(orgCode, FormCache.ME.getFormId(cformcode), generateQty);
    }

    // ----------------------------------------------------------------
    // 生成条码
    // ----------------------------------------------------------------

    /**
     * 生成条码
     *
     * @param orgCode        组织编码
     * @param cBarcodeTypeSn 条码类型：1. 物料现品票 2.工单现品票
     */
    public static String genBarcode(String orgCode, String cBarcodeTypeSn) {
        return MOM_DATA_FUNC_SERVICE.generateBarcode(orgCode, cBarcodeTypeSn);
    }

    /**
     * 生成多个条码
     *
     * @param orgCode        组织编码
     * @param cBarcodeTypeSn 条码类型：1. 物料现品票 2.工单现品票
     */
    public static List<String> genBarcodes(String orgCode, String cBarcodeTypeSn, int generateQty) {
        List<Record> codeRecords = MOM_DATA_FUNC_SERVICE.barcodeGenerateForBarcodeTypeSN(orgCode, cBarcodeTypeSn, generateQty);
        return CollUtil.getFieldValues(codeRecords, "barcode", String.class);
    }

    /**
     * 生成条码记录，已知字段：barcode、seq、serial
     *
     * @param orgCode        组织编码
     * @param cBarcodeTypeSn 条码类型：1. 物料现品票 2.工单现品票
     */
    public static Record genBarcodeRecord(String orgCode, String cBarcodeTypeSn) {
        return MOM_DATA_FUNC_SERVICE.generateBarcodeRecord(orgCode, cBarcodeTypeSn);
    }

    /**
     * 生成多个条码记录，已知字段：barcode、seq、serial
     *
     * @param orgCode        组织编码
     * @param cBarcodeTypeSn 条码类型：1. 物料现品票 2.工单现品票
     * @param generateQty    生成数量
     */
    public static List<Record> genBarcodeRecords(String orgCode, String cBarcodeTypeSn, int generateQty) {
        return MOM_DATA_FUNC_SERVICE.barcodeGenerateForBarcodeTypeSN(orgCode, cBarcodeTypeSn, generateQty);
    }

}
