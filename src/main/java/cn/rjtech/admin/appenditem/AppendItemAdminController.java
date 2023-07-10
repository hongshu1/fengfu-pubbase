package cn.rjtech.admin.appenditem;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.annotation.CheckDataPermission;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.common.enums.BusObjectTypeEnum;
import cn.jbolt.core.common.enums.DataOperationEnum;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.constants.Constants;
import cn.rjtech.enums.ServiceTypeEnum;
import cn.rjtech.model.momdata.AppendItem;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Record;

/**
 * 追加项目 Controller
 * @ClassName: AppendItemAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-12-07 17:43
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.APPEND_ITEM)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/appenditem", viewPath = "/_view/admin/appenditem")
public class AppendItemAdminController extends BaseAdminController {

	@Inject
	private AppendItemService service;

   /**
	* 首页
	*/
	public void index() {
		render("index.html");
	}

  	/**
	* 数据源
	*/
    @UnCheck
    @CheckDataPermission(operation = DataOperationEnum.VIEW, type = BusObjectTypeEnum.DEPTARTMENT)
	public void datas() {
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv()));
	}

   /**
	* 新增
	*/
	@CheckPermission(PermissionKey.APPEND_ITEM_ADD)
	public void add(@Para(value="iservicetype") Integer iserviceType) {
		if(iserviceType == ServiceTypeEnum.EXPENSE_BUDGET.getValue()) render("add_expense_budget.html");
		else if(iserviceType == ServiceTypeEnum.INVESTMENT_PLAN.getValue()) render("add_investment_plan.html");
	}

   /**
	* 编辑
	*/
	@CheckPermission(PermissionKey.APPEND_ITEM_EDIT)
	public void edit() {
		Record appendItem = service.findForEditById(useIfPresent(getLong(0)));
		ValidationUtils.notNull(appendItem, JBoltMsg.DATA_NOT_EXIST);
		Integer iserviceType = appendItem.getInt("iservicetype");
		appendItem.set("ibudgetmoney", appendItem.getBigDecimal("ibudgetmoney").divide(Constants.RATIO));
		set("appendItem", appendItem);
		if(iserviceType == ServiceTypeEnum.EXPENSE_BUDGET.getValue()) render("edit_expense_budget.html");
		else if(iserviceType == ServiceTypeEnum.INVESTMENT_PLAN.getValue()) render("edit_investment_plan.html");
		
	}

   /**
	* 保存
	*/
	@CheckDataPermission(operation = DataOperationEnum.EDIT, type = BusObjectTypeEnum.DEPTARTMENT)
	public void save() {
		renderJson(service.save(useIfValid(AppendItem.class, "appendItem")));
	}

   /**
	* 更新
	*/
	@CheckDataPermission(operation = DataOperationEnum.EDIT, type = BusObjectTypeEnum.DEPTARTMENT)
	public void update() {
		renderJson(service.update(useIfValid(AppendItem.class, "appendItem")));
	}

   /**
	* 批量删除
	*/
	@CheckPermission(PermissionKey.APPEND_ITEM_DELETE)
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}

  /**
	* 切换toggleIslargeamountexpense
	*/
	public void toggleIslargeamountexpense() {
		renderJson(service.toggleIslargeamountexpense(getLong(0)));
	}

  /**
	* 切换toggleIsimport
	*/
	public void toggleIsimport() {
		renderJson(service.toggleIsimport(getLong(0)));
	}

  /**
	* 切换toggleIspriorreport
	*/
	public void toggleIspriorreport() {
		renderJson(service.toggleIspriorreport(getLong(0)));
	}
	/**
     *	追加项目生效 
     * */
    @CheckPermission(PermissionKey.APPEND_ITEM_EFFECT)
    public void effect(){
    	renderJson(service.effect(getLong(0)));
    }
    /**
     *	追加项目撤销
     * */
    @CheckPermission(PermissionKey.APPEND_ITEM_UNEFFECT)
    public void unEffect(){
    	renderJson(service.unEffect(getLong(0)));
    }
}
