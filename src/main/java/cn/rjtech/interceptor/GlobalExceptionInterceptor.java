package cn.rjtech.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.exception.CommonException;
import cn.jbolt.core.kit.JBoltControllerKit;
import cn.rjtech.base.exception.ParameterException;
import cn.rjtech.constants.Constants;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.util.ExceptionEventUtil;
import com.alibaba.fastjson.JSONException;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;

import java.text.ParseException;

/**
 * @Description JFinal 全局异常拦截器, 配置顺序上注意配置在事务拦截器前面，否则事务将失效
 * @Author Create by Alvin
 * @Date 2020-10-29 14:14
 */
public class GlobalExceptionInterceptor implements Interceptor {

    private static final Log LOG = Log.getLog(GlobalExceptionInterceptor.class);

    @Override
    public void intercept(Invocation inv) {
        Controller controller = inv.getController();
        try {
            inv.invoke();
        } catch (Exception e) {
            // 依据异常类型处理错误信息
            renderError(e, controller);
        }
    }

    /**
     * 处理全局异常, 日志级别为ERROR
     */
    private void renderError(Exception e, Controller controller) {
        if (e instanceof ParseException) {
            renderParseException((ParseException) e, controller);
        } else if (e instanceof RuntimeException) {
            // 对特定的运行时异常，特殊处理
            if (e instanceof ParameterException) {
                renderParameterException((ParameterException) e, controller);
            } else if (e instanceof CommonException) {
                renderCommonException((CommonException) e, controller);
            } else if (e instanceof IllegalArgumentException) {
                renderIllegalArgumentException((IllegalArgumentException) e, controller);
            } else if (e instanceof JSONException) {
                renderJsonException((JSONException) e, controller);
            } else {
                renderRuntimeException((RuntimeException) e, controller);
            }
        } else {
            // 未知错误
            renderException(e, controller);
        }
    }

    /**
     * 自定义异常处理, 日志级别为WARN
     */
    private void renderParameterException(ParameterException e, Controller controller) {
        String msg = Constants.removeExceptionPrefix(e.getLocalizedMessage());
        LOG.warn(msg);

        e.printStackTrace();

        // controller.render(new MyErrorRender(e.getErrorCode(), e.getLocalizedMessage(), null))
        // 使用JBolt内置错误处理适配
        JBoltControllerKit.renderFail(controller, StrUtil.blankToDefault(msg, JBoltMsg.FAIL));
    }

    /**
     * 处理公共业务异常, 日志级别为WARN
     */
    private void renderCommonException(CommonException e, Controller controller) {
        String msg = Constants.removeExceptionPrefix(e.getLocalizedMessage());
        LOG.warn(msg);
        
        e.printStackTrace();

        // controller.render(new MyErrorRender(e.getErrorCode(), e.getLocalizedMessage(), null))
        // 使用JBolt内置错误处理适配
        JBoltControllerKit.renderFail(controller, StrUtil.blankToDefault(msg, JBoltMsg.FAIL));
    }

    /**
     * 处理参数格式异常，日志级别为WARN
     */
    private void renderParseException(ParseException e, Controller controller) {
        String msg = Constants.removeExceptionPrefix(e.getLocalizedMessage());
        LOG.warn(msg);
        
        // controller.render(new MyErrorRender("400", e.getLocalizedMessage(), null))
        // 使用JBolt内置错误处理适配
        JBoltControllerKit.renderFail(controller, StrUtil.blankToDefault(msg, JBoltMsg.FAIL));
    }

    /**
     * 处理参数格式异常，日志级别为WARN
     */
    private void renderIllegalArgumentException(IllegalArgumentException e, Controller controller) {
        String msg = Constants.removeExceptionPrefix(e.getLocalizedMessage());
        LOG.warn(msg);
        
        e.printStackTrace();

        // controller.render(new MyErrorRender("400", e.getLocalizedMessage(), null))
        // 使用JBolt内置错误处理适配
        JBoltControllerKit.renderFail(controller, StrUtil.blankToDefault(msg, ErrorMsg.ILLEGAL_ARGUMENT));
    }

    /**
     * 全局异常
     */
    private void renderException(Exception e, Controller controller) {
        String msg = Constants.removeExceptionPrefix(e.getLocalizedMessage());
        LOG.error(msg);
        
        e.printStackTrace();

        ExceptionEventUtil.postExceptionEvent(e);
        // controller.render(new MyErrorRender("500",  Msg.FAIL,null))
        // 使用JBolt内置错误处理适配
        JBoltControllerKit.renderFail(controller, StrUtil.blankToDefault(msg, JBoltMsg.FAIL));
    }

    private void renderJsonException(JSONException e, Controller controller) {
        e.printStackTrace();
        LOG.error("JSON解析异常：{}", e.getLocalizedMessage());

        JBoltControllerKit.renderFail(controller, "JSON解析异常");
    }

    /**
     * 运行时异常, 日志级别为WARN
     */
    private void renderRuntimeException(RuntimeException e, Controller controller) {
        String msg = Constants.removeExceptionPrefix(e.getLocalizedMessage());
        LOG.warn(msg);

        e.printStackTrace();

        ExceptionEventUtil.postExceptionEvent(e);

        // 使用JBolt内置错误处理适配
        JBoltControllerKit.renderFail(controller, StrUtil.blankToDefault(msg, JBoltMsg.FAIL));
    }

}
