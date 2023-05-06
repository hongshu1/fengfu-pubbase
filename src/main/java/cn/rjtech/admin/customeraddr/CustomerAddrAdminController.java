package cn.rjtech.admin.customeraddr;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.CustomerAddr;
/**
 * 客户档案-联系地址
 * @ClassName: CustomerAddrAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 13:16
 */
@CheckPermission(PermissionKey.CUSTOMER)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/customeraddr", viewPath = "/_view/admin/customeraddr")
public class CustomerAddrAdminController extends BaseAdminController {

	@Inject
	private CustomerAddrService service;

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

	public void list(){
		renderJsonData(service.list(getKv()));
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
		CustomerAddr customerd=service.findById(getLong(0));
		if(customerd == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("customerd",customerd);
		render("edit.html");
	}

	/**
	 * 保存
	 */
	public void save() {
		renderJson(service.save(getModel(CustomerAddr.class, "customerd")));
	}

	/**
	 * 更新
	 */
	public void update() {
		renderJson(service.update(getModel(CustomerAddr.class, "customerd")));
	}

	/**
	 * 批量删除
	 */
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}

	/**
	 * 切换toggleIsenabled
	 */
	public void toggleIsenabled() {
		renderJson(service.toggleIsenabled(getLong(0)));
	}


}
