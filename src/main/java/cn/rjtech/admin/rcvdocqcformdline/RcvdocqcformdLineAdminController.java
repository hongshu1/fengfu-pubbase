package cn.rjtech.admin.rcvdocqcformdline;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.RcvdocqcformdLine;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 质量管理-来料检明细列值表 Controller
 *
 * @ClassName: RcvdocqcformdLineAdminController
 * @author: RJ
 * @date: 2023-04-13 16:53
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/rcvdocqcformdline", viewPath = "/_view/admin/rcvdocqcformdline")
public class RcvdocqcformdLineAdminController extends BaseAdminController {

	@Inject
	private RcvdocqcformdLineService service;

   /**
	* 首页
	*/
	public void index() {
		render("index().html");
	}
  	
  	/**
	* 数据源
	*/
	public void datas() {
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKeywords()));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add().html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		RcvdocqcformdLine rcvdocqcformdLine=service.findById(getLong(0)); 
		if(rcvdocqcformdLine == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("rcvdocqcformdLine",rcvdocqcformdLine);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(RcvdocqcformdLine.class, "rcvdocqcformdLine")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(RcvdocqcformdLine.class, "rcvdocqcformdLine")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}


}
