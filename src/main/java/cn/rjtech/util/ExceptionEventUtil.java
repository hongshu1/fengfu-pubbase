package cn.rjtech.util;

import cn.jbolt.core.base.config.JBoltConfig;
import cn.jbolt.core.cache.JBoltCacheKit;
import cn.rjtech.base.event.ExceptionEvent;
import cn.rjtech.base.redis.RedisKeys;
import com.jfinal.log.Log;
import net.dreamlu.event.EventKit;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import static cn.jbolt.core.base.config.JBoltConfig.JBOLT_CACHE_NAME;

public class ExceptionEventUtil {

    private static final Log LOG = Log.getLog(ExceptionEventUtil.class);

    /**
     * 系统异常事件
     *
     * @param e 错误对象
     */
    public static void postExceptionEvent(Exception e) {
        if (!JBoltConfig.prop.getBoolean("error.notice.enabled", false)) {
            LOG.info("邮件提醒已被配置为禁用");
            return;
        }

        String errorCode = getErrorCode(e);
        Integer reportTimes = getExceptionNotice(errorCode);
        if (null != reportTimes) {
            setExceptionNotice(errorCode, ++reportTimes);
            LOG.info("异常已经报告: {} 次!", reportTimes);
            return;
        }
        setExceptionNotice(errorCode, 1);

        EventKit.post(new ExceptionEvent(getExceptionMsg(errorCode, e)));
    }

    public static String getErrorCode(Exception e) {
        if (e instanceof SQLIntegrityConstraintViolationException) {
            return RedisKeys.Error.DUPLICATE_KEY;
        } else if (e instanceof SQLException) {
            return RedisKeys.Error.DB;
        } else {
            return RedisKeys.Error.GLOBAL;
        }
    }

    private static String getExceptionType(String errorCode) {
        switch (errorCode) {
            case RedisKeys.Error.DUPLICATE_KEY:
                return "数据重复错误";
            case RedisKeys.Error.DB:
                return "数据库异常";
            case RedisKeys.Error.GLOBAL:
            default:
                return "全局异常";
        }
    }

    /**
     * 获取异常信息
     *
     * @param errorCode 错误代码
     * @param e         异常对象
     * @return 异常信息
     */
    private static String getExceptionMsg(String errorCode, Exception e) {
        return String.format("%s:%s", getExceptionType(errorCode), getStackMsg(e));
    }

    /**
     * 获取格式化堆栈异常信息
     */
    public static String getStackMsg(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw + "\r\n";
        } catch (Exception e2) {
            return "bad getErrorInfoFromException";
        }
    }

    /**
     * 设置异常报告次数
     *
     * @param exceptionKey 异常key
     * @param times        异常报错次数
     */
    public static void setExceptionNotice(String exceptionKey, int times) {
        JBoltCacheKit.put(JBOLT_CACHE_NAME, "LH:EXCEPTION:NOTICE:", exceptionKey, times);
    }

    /**
     * 获取异常通知次数
     *
     * @param exceptionKey 异常key
     * @return 异常报告次数
     */
    public static Integer getExceptionNotice(String exceptionKey) {
        return JBoltCacheKit.get(JBOLT_CACHE_NAME, "LH:EXCEPTION:NOTICE:" + exceptionKey);
    }

}
