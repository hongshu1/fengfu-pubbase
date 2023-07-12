package cn.rjtech.constants;

import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.model.JboltFile;
import cn.jbolt.core.util.JBoltRealUrlUtil;
import cn.rjtech.base.exception.ParameterException;
import cn.rjtech.config.AppConfig;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.AuditWayEnum;
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

    private static final String EXCEPTION_PREFIX = "Exception:";

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

    /**
     * 根据审批方式，动态控制显示状态的名称
     *
     * @param auditWay    审批方式
     * @param auditStatus 审核状态
     */
    public static String getAuditStatusName(int auditWay, int auditStatus) {
        switch (AuditWayEnum.toEnum(auditWay)) {
            case STATUS:
                // 审核状态
                switch (AuditStatusEnum.toEnum(auditStatus)) {
                    case NOT_AUDIT:
                    case REJECTED:
                        return "已保存";
                    case AWAIT_AUDIT:
                        return "待审核";
                    case APPROVED:
                        return "已审核";
                    default:
                        throw new ParameterException("未知审核状态");
                }
            case FLOW:
                // 审批流
                switch (AuditStatusEnum.toEnum(auditStatus)) {
                    case NOT_AUDIT:
                    case REJECTED:
                        return "已保存";
                    case AWAIT_AUDIT:
                        return "待审批";
                    case APPROVED:
                        return "审批通过";
                    default:
                        throw new ParameterException("未知审核状态");
                }
            default:
                throw new ParameterException("未知审批类型");
        }
    }
	public static BigDecimal kFormat(BigDecimal k){
		if(k == null) return k;
		return k.divide(BigDecimal.valueOf(1000)).setScale(2, RoundingMode.HALF_UP);
	}

    /**
     * 获取替换异常前缀后的异常信息
     *
     * @param msg 错误信息
     * @return 错误信息
     */
    public static String removeExceptionPrefix(String msg) {
        int index = StrUtil.indexOf(msg, EXCEPTION_PREFIX, 0, true);
        if (index < 0) {
            return msg;
        }
        return msg.substring(index + EXCEPTION_PREFIX.length());
    }
    
}
