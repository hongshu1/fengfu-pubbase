package cn.rjtech.admin.annualorderd;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.AnnualOrderD;
/**
 * 年度计划订单年汇总 Controller
 * @ClassName: AnnualOrderDAdminController
 * @author: heming
 * @date: 2023-03-28 17:06
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/annualorderd", viewPath = "/_view/admin/annualorderd")
public class AnnualOrderDAdminController extends BaseAdminController {

	@Inject
	private AnnualOrderDService service;

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
		render("add.html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		AnnualOrderD annualOrderD=service.findById(getLong(0)); 
		if(annualOrderD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("annualOrderD",annualOrderD);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(AnnualOrderD.class, "annualOrderD")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(AnnualOrderD.class, "annualOrderD")));
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

  /**
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}
	
	public void findEditTableDatas(){
		renderJsonData(service.findEditTableDatas(getKv()));
	}

}
