package cn.rjtech.admin.customerworkdays;

import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.CustomerWorkDays;
/**
 * 往来单位-客户行事日历
 * @ClassName: CustomerWorkDaysAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-31 15:16
 */
@CheckPermission(PermissionKey.CUSTOMER)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/customerworkdays", viewPath = "/_view/admin/customerworkdays")
public class CustomerWorkDaysAdminController extends BaseAdminController {

	@Inject
	private CustomerWorkDaysService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize()));
	}

	/**
	 * 根据客户主键获取数据
	 */
	public void getList(){
		renderJsonData(service.getList(getKv()));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add().html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(CustomerWorkDays.class, "customerWorkDays")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		CustomerWorkDays customerWorkDays=service.findById(getLong(0));
		if(customerWorkDays == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("customerWorkDays",customerWorkDays);
		render("edit().html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(CustomerWorkDays.class, "customerWorkDays")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}


}
