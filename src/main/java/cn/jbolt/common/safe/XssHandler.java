package cn.jbolt.common.safe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

import cn.jbolt.core.util.JBoltArrayUtil;
/**
 * xss攻击处理handler
 * @ClassName:  XssHandler   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2020年4月1日   
 */
public class XssHandler extends Handler{
	private String[] unlimiteds;
	/**
	 * 设置不检测的路径
	 * @param unlimiteds
	 * @return
	 */
	public XssHandler unlimited(String... unlimiteds) {
		if(JBoltArrayUtil.isEmpty(this.unlimiteds)) {
			this.unlimiteds=unlimiteds;
		}else {
			this.unlimiteds=JBoltArrayUtil.merge(this.unlimiteds, unlimiteds);
		}
		return this;
	}
	

	/**
	 * 检测是否属于忽略检测
	 * @param target
	 * @return
	 */
	private boolean checkNotInUnlimited(String target) {
		if(unlimiteds==null||unlimiteds.length==0) {
			return true;
		}
		boolean in=false;
		for(String l:unlimiteds) {
			if(target.equals(l)||target.indexOf(l)!=-1) {
				in=true;
				break;
			}
		}
		return !in;
	}
	
	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		    
		 
		//带.表示非action请求 或者 指定忽略的目录下
        if (target.indexOf(".") == -1 && checkNotInUnlimited(target)){
            request = new HttpServletRequestWrapper(request);
        }
        next.handle(target, request, response, isHandled);
        
	}
 
	 
 
}
