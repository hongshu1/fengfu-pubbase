package cn.jbolt.common.safe;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.map.MapUtil;

/**
 * 处理Xss漏洞
 * @ClassName:  HttpServletRequestWrapper   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2021年4月29日   
 */
public class HttpServletRequestWrapper extends javax.servlet.http.HttpServletRequestWrapper{

    private HttpServletRequest request;

    public HttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }
    @Override
    public String getParameter(String name) {
        String value = this.request.getParameter(name);
        if (value == null)
            return null;
        value = XssUtil.processXSS(value);
        return value;
    }
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (null == values){
            return null;
        }
        int len=values.length;
        for (int i = 0; i < len; i++) {
            values[i] = XssUtil.processXSS(values[i]);
        }
        return values;
    }
    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap =super.getParameterMap();
        Map<String, String[]> newMap = MapUtil.newHashMap(parameterMap.size());
        for (Entry<String, String[]> entry : parameterMap.entrySet()) { 
            newMap.put(entry.getKey(), new String[]{XssUtil.processXSS(entry.getValue()[0])});
        }
        return newMap;
    }
    
    @Override
    public String getHeader(String name) {
    	 return XssUtil.processXSS(super.getHeader(name));
    }
  
    
    public static void main(String[] args) {
		String html = "<script>alert(1)</script><img src='1.js' onerror = alert`1`  onload='alert(1)'>";
		long start = System.currentTimeMillis();
		for(int i=0;i<10000;i++) {
			XssUtil.processXSS(html);
		}
		long end = System.currentTimeMillis();
		System.out.println(end-start);
		System.out.println(XssUtil.processXSS(html));
	}
}
