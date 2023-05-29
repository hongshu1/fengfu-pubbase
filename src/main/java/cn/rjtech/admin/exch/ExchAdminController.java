package cn.rjtech.admin.exch;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.rjtech.base.controller.BaseAdminController;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 汇率档案
 * @ClassName: ExchAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-29 13:45
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/exch", viewPath = "/_view/admin/exch")
public class ExchAdminController extends BaseAdminController {

	@Inject
	private ExchService service;
   /**
	* 首页
	*/
	public void index() {
		render("index.html");
	}
   /**
	* 数据源
	*/
	public void datas() {
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getInt("itype"), getBoolean("isDeleted")));
	}

   /**
	* 切换isDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}


}
