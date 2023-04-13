package cn.rjtech.admin.subcontractsaleorderd;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.Subcontractsaleorderd;
/**
 * 委外销售订单明细 Controller
 * @ClassName: SubcontractsaleorderdAdminController
 * @author: RJ
 * @date: 2023-04-12 19:08
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/subcontractsaleorderd", viewPath = "/_view/admin/subcontractsaleorderd")
public class SubcontractsaleorderdAdminController extends BaseAdminController {

	@Inject
	private SubcontractsaleorderdService service;

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
		Subcontractsaleorderd subcontractsaleorderd=service.findById(getLong(0)); 
		if(subcontractsaleorderd == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("subcontractsaleorderd",subcontractsaleorderd);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(Subcontractsaleorderd.class, "subcontractsaleorderd")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Subcontractsaleorderd.class, "subcontractsaleorderd")));
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
