package cn.rjtech.filter;

import cn.hutool.core.text.StrFormatter;
import cn.hutool.extra.servlet.ServletUtil;
import com.jfinal.log.Log;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 全局过滤器
 *
 * @author Kephon
 */
public class GlobalFilter implements Filter {

    private static final Log LOG = Log.getLog(GlobalFilter.class);

    private static final String ASSETS_PREFIX = "/assets";
    private static final String UPLOAD_PREFIX = "/upload";

    @Override
    public void init(FilterConfig filterConfig) {
        LOG.info("Global Filter inited...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());

        if (!request.getRequestURI().startsWith(ASSETS_PREFIX) && !request.getRequestURI().startsWith(UPLOAD_PREFIX)) {
            Map<String, String[]> param = ServletUtil.getParams(servletRequest);

            StringBuilder params = new StringBuilder();

            for (Map.Entry<String, String[]> entry : param.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();

                for (String value : values) {
                    // 防止过长的日志输出内容
                    if (value.length() > 100) {
                        value = value.substring(0, 100);
                    }
                    params.append(key).append("=").append(value).append("&");
                }
            }
            LOG.info(StrFormatter.format("--- {}?{}\tSessionId: {}", request.getRequestURI(), params.toString(), request.getSession().getId()));
        }

        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {
        LOG.info("Global Filter destroyed...");
    }

}
