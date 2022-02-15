package cn.jbolt.common.pdfviewer;

import com.jfinal.core.Path;

import cn.jbolt.core.controller.base.JBoltBaseController;
/**
 * JBolt平台 pdf阅读器
 * @ClassName:  JBoltPdfViewerController   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2021年3月21日   
 *    
 */
@Path(value = "/pdfviewer",viewPath = "/assets/plugins/pdfjs/web")
public class JBoltPdfViewerController extends JBoltBaseController {
	/**
	 * 查看入口
	 */
	public void index() {
		keepPara("file");
		render("viewer.html");
	}
}
