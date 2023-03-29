package cn.rjtech.admin.annualorderdamount;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.AnnualorderdAmount;
/**
 * 年度计划订单年月金额 Controller
 * @ClassName: AnnualorderdAmountAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 14:22
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/annualorderdamount", viewPath = "/_view/admin/annualorderdamount")
public class AnnualorderdAmountAdminController extends BaseAdminController {

	@Inject
	private AnnualorderdAmountService service;

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
		AnnualorderdAmount annualorderdAmount=service.findById(getLong(0)); 
		if(annualorderdAmount == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("annualorderdAmount",annualorderdAmount);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(AnnualorderdAmount.class, "annualorderdAmount")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(AnnualorderdAmount.class, "annualorderdAmount")));
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


}
