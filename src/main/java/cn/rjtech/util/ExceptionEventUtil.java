package cn.rjtech.util;

import cn.jbolt.core.base.config.JBoltConfig;
import cn.rjtech.base.event.ExceptionEvent;
import cn.rjtech.base.redis.RedisKeys;
import cn.rjtech.base.redis.RedisUtil;
import cn.rjtech.event.EmailSendFailEvent;
import com.jfinal.log.Log;
import net.dreamlu.event.EventKit;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

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
        Long reportTimes = getExceptionNotice(errorCode);
        
        // 首次设值，设置过期时间为2小时
        if (reportTimes == 1) {
            setExpire(errorCode);
            EventKit.post(new ExceptionEvent(getExceptionMsg(errorCode, e)));
        }
        
        LOG.info("异常已经报告: {} 次!", reportTimes);
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
     * 获取异常通知次数
     *
     * @param exceptionKey 异常key
     * @return 异常报告次数
     */
    public static Long getExceptionNotice(String exceptionKey) {
        return RedisUtil.incr("LH:EXCEPTION:NOTICE:" + exceptionKey);
    }

    /**
     * 设置异常信息的过期时间
     */
    public static void setExpire(String exceptionKey) {
        RedisUtil.expire("LH:EXCEPTION:NOTICE:" + exceptionKey, RedisUtil.EXPIRES_IN_2_HOURS);
    }
    
    /**
     * 邮件发送失败错误
     */
    public static void postSendEmailFailMsg(String email, String subject, String text, String errMsg) {
        EventKit.post(new EmailSendFailEvent(email, subject, text, errMsg));
    }

}
