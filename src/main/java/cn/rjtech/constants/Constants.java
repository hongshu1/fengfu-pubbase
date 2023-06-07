package cn.rjtech.constants;

import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.model.JboltFile;
import cn.jbolt.core.util.JBoltRealUrlUtil;
import cn.rjtech.config.AppConfig;
import cn.rjtech.enums.DictionaryTypeKeyEnum;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Description 通用的静态参数类
 * @Author Create by Alvin
 * @Date 2020-09-21 13:42
 */
public class Constants {

    private Constants() {
        // ignored
    }

    public static final BigDecimal RATIO = BigDecimal.valueOf(1000);

    /**
     * 获取环境名称
     */
    public static String getProdEnv(boolean isProdEnv) {
        return isProdEnv ? "生产环境" : "测试环境";
    }

    /**
     * 获取资源访问链接
     */
    public static String getResUrl(String uri) {
        return JBoltRealUrlUtil.get(uri);
    }

    /**
     * 获取http://访问的地址
     */
    public static String getFullUrl(String path) {
        return String.format("http://%s/%s", AppConfig.getDomain(), getResUrl(path));
    }

    public static void fillPlanItem(Record row) {
        row.set("cinvestmenttypedesc", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.INVESTMENT_TYPE.getValue(), row.getStr("iinvestmenttype")));
        row.set("careertypedesc", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.CAREERTYPE.getValue(), row.getStr("icareertype")));
        row.set("cinvestmentdistinctiondesc", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.INVESTMENT_DISTINCTION.getValue(), row.getStr("iinvestmentdistinction")));
        row.set("cassettypedesc", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.CASSETTYPE.getValue(), row.getStr("cassettype")));
        row.set("cpaymentprogressdesc", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.PAYMENT_PROGRESS.getValue(), row.getStr("cpaymentprogress")));
        row.set("cedittypedesc", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.EDITTYPE.getValue(), row.getStr("cedittype")));
        row.set("citemtypedesc", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.INVESTMENTITEMTYPE.getValue(), row.getStr("citemtype")));
        row.set("itotalamountplan", row.getBigDecimal("itotalamountplan") == null ? null : row.getBigDecimal("itotalamountplan").divide(Constants.RATIO, RoundingMode.HALF_UP));
        row.set("itotalamountactual", row.getBigDecimal("itotalamountactual") == null ? null : row.getBigDecimal("itotalamountactual").divide(Constants.RATIO, RoundingMode.HALF_UP));
        row.set("itotalamountdiff", row.getBigDecimal("itotalamountdiff") == null ? null : row.getBigDecimal("itotalamountdiff").divide(Constants.RATIO, RoundingMode.HALF_UP));
        row.set("iyeartotalamountplan", row.getBigDecimal("iyeartotalamountplan") == null ? null : row.getBigDecimal("iyeartotalamountplan").divide(Constants.RATIO, RoundingMode.HALF_UP));
        row.set("iyeartotalamountactual", row.getBigDecimal("iyeartotalamountactual") == null ? null : row.getBigDecimal("iyeartotalamountactual").divide(Constants.RATIO, RoundingMode.HALF_UP));
        row.set("iyeartotalamountdiff", row.getBigDecimal("iyeartotalamountdiff") == null ? null : row.getBigDecimal("iyeartotalamountdiff").divide(Constants.RATIO, RoundingMode.HALF_UP));
        row.set("iamount1", row.getBigDecimal("iamount1") != null ? row.getBigDecimal("iamount1").divide(Constants.RATIO, RoundingMode.HALF_UP) : null);
        row.set("iamount2", row.getBigDecimal("iamount2") != null ? row.getBigDecimal("iamount2").divide(Constants.RATIO, RoundingMode.HALF_UP) : null);
        row.set("iamount3", row.getBigDecimal("iamount3") != null ? row.getBigDecimal("iamount3").divide(Constants.RATIO, RoundingMode.HALF_UP) : null);
        row.set("iamount4", row.getBigDecimal("iamount4") != null ? row.getBigDecimal("iamount4").divide(Constants.RATIO, RoundingMode.HALF_UP) : null);
        row.set("iamount5", row.getBigDecimal("iamount5") != null ? row.getBigDecimal("iamount5").divide(Constants.RATIO, RoundingMode.HALF_UP) : null);
        row.set("iamount6", row.getBigDecimal("iamount6") != null ? row.getBigDecimal("iamount6").divide(Constants.RATIO, RoundingMode.HALF_UP) : null);

    }

    /**
     * 根据上传文件的扩展名，转换为JBolt的文件类型
     *
     * @param fileExt 文件扩展名
     * @return JBoltFile类型值
     */
    public static int getFileType(String fileExt) {
        switch (fileExt) {
            case "png":
            case "jpg":
            case "jpeg":
                return JboltFile.FILE_TYPE_IMAGE;
            case "mp3":
                return JboltFile.FILE_TYPE_AUDIO;
            case "mp4":
                return JboltFile.FILE_TYPE_VEDIO;
            case "doc":
            case "docx":
            case "xls":
            case "xlsx":
                return JboltFile.FILE_TYPE_OFFICE;
            default:
                return JboltFile.FILE_TYPE_ATTACHMENT;
        }
    }

    public static String getSuccessJson(String data) {
        return String.format("{\"state\":\"ok\",\"data\":%s}", data);
    }

}
