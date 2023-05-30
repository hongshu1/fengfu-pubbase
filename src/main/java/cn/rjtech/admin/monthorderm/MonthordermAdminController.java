package cn.rjtech.admin.monthorderm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.customer.CustomerService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Customer;
import cn.rjtech.model.momdata.MonthOrderM;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Record;

/**
 * 月度计划订单 Controller
 *
 * @ClassName: MonthordermAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 18:20
 */
@CheckPermission(PermissionKey.MONTHORDERM)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/monthorderm", viewPath = "/_view/admin/monthorderm")
public class MonthordermAdminController extends BaseAdminController {

	@Inject
	private MonthordermService service;
	@Inject
	private CustomerService customerService;

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
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv()));
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
		MonthOrderM monthorderm=service.findById(getLong(0)); 
		if(monthorderm == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		Record monthordermRc = monthorderm.toRecord();
        Customer customer = customerService.findById(monthorderm.getICustomerId());
        monthordermRc.set("ccusname", customer == null ? null:customer.getCCusName());
		set("monthorderm",monthordermRc);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MonthOrderM.class, "monthorderm")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MonthOrderM.class, "monthorderm")));
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

    /**
     * 新增-可编辑表格-批量提交
     */
    public void submitAll() {
        renderJson(service.submitByJBoltTable(getJBoltTable()));
    }

	/**
	 * 审批
	 */
	public void approve() {
		renderJson(service.approve(getLong("id")));
	}

    /**
     * 提审
     */
    public void submit(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.validateId(iautoid, "ID");
        
        renderJson(service.submit(iautoid));
    }

    /**
     * 撤回
     */
    public void withdraw(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.validateId(iautoid, "ID");

        renderJson(service.withdraw(iautoid));
    }
    
}
