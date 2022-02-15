package cn.jbolt.common.safe;

import java.util.regex.Pattern;

import cn.hutool.core.util.StrUtil;
/**
 * xss工具类
 * @ClassName:  XssUtil   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2021年11月4日   
 */
public class XssUtil {
	private static final Pattern ON_PATTERN = Pattern.compile(" on(\\w.*?)=",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    /**
     * 过滤正则表达式合集
     */
    private static final Pattern[] scriptPatterns= {
    		Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<style>(.*?)</style>", Pattern.CASE_INSENSITIVE),
    		Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?).js\\\'",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?).js\\\"",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
    		Pattern.compile("<script(.*?)>",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		Pattern.compile("eval\\((.*?)\\)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
    		Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
    		Pattern.compile("alert\\`(.*?)\\`",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		Pattern.compile("prompt\\`(.*?)\\`",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		Pattern.compile("confirm\\`(.*?)\\`",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		Pattern.compile("alert\\((.*?)\\)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		Pattern.compile("window.location(.*?)=",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		Pattern.compile("self.location(.*?)=",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		Pattern.compile("top.location(.*?)=",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		Pattern.compile("this.location(.*?)=",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		Pattern.compile("unescape\\((.*?)\\)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		Pattern.compile("execscript\\((.*?)\\)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		Pattern.compile("msgbox\\((.*?)\\)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		Pattern.compile("confirm\\((.*?)\\)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		Pattern.compile("prompt\\((.*?)\\)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
	};
    
    /**
     * 正则处理xss
     * @param value
     * @return
     */
    public static String processXSS(String value) {
        if (StrUtil.isNotBlank(value)) {
        	value = ON_PATTERN.matcher(value).replaceAll(" ");
        	for(Pattern pattern:scriptPatterns) {
        		value = pattern.matcher(value).replaceAll("");
        	}
        }
        return value;
    }
    
    /**
     * 处理excel中单元格读取到的数据
     * @param value
     * @return
     */
	public static Object processExcelString(Object value) {
		if(value instanceof String) {
			value = processXSS(value.toString());
		}
		return value;
	}
	
	public static void main(String[] args) {
		String html = "<img src='1' onerror=alert`1 />";
		System.out.println(processXSS(html));
	}
}
