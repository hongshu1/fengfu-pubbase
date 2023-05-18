package cn.rjtech.admin.rcvdocqcformd;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.RcvDocQcFormD;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 质量管理-来料检单行配置表 Controller
 *
 * @ClassName: RcvDocQcFormDAdminController
 * @author: RJ
 * @date: 2023-04-13 16:45
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/rcvdocqcformd", viewPath = "/_view/admin/rcvdocqcformd")
public class RcvDocQcFormDAdminController extends BaseAdminController {

	@Inject
	private RcvDocQcFormDService service;

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
		RcvDocQcFormD rcvDocQcFormD=service.findById(getLong(0)); 
		if(rcvDocQcFormD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("rcvDocQcFormD",rcvDocQcFormD);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(RcvDocQcFormD.class, "rcvDocQcFormD")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(RcvDocQcFormD.class, "rcvDocQcFormD")));
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
