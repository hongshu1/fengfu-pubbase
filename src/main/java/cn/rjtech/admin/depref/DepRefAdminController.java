package cn.rjtech.admin.depref;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.DepRef;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 部门对照档案 Controller
 * @ClassName: DepRefAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-30 20:58
 */
@CheckPermission(PermissionKey.DEPREF)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/depref", viewPath = "/_view/admin/depref")
public class DepRefAdminController extends BaseAdminController {

	@Inject
	private DepRefService service;

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
		DepRef depRef=service.findById(getLong(0)); 
		if(depRef == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("depRef",depRef);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(DepRef.class, "depRef")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(DepRef.class, "depRef")));
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
	* 切换toggleIsDefault
	*/
	public void toggleIsDefault() {
		renderJson(service.toggleIsDefault(getLong(0)));
	}

	@UnCheck
	public void findEndDepData(){
		renderJsonData(service.findEndDepData(getKv()));
	}
	@UnCheck
	public void saveTableDatas(){
		renderJson(service.saveTableDatas(getKv()));
	}
	@UnCheck
	public void findCheckedIds(){
		renderJson(service.findCheckedIds(getKv()));
	}
	

}
