package cn.rjtech.admin.fitemss97sub;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.Fitemss97sub;
/**
 * 项目管理大类项目子目录 Controller
 * @ClassName: Fitemss97subAdminController
 * @author: heming
 * @date: 2023-03-27 10:22
 */
@CheckPermission(PermissionKey.FITEMSS97SUB)
@UnCheckIfSystemAdmin
public class Fitemss97subAdminController extends BaseAdminController {

	@Inject
	private Fitemss97subService service;

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
		Fitemss97sub fitemss97sub=service.findById(getLong(0)); 
		if(fitemss97sub == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("fitemss97sub",fitemss97sub);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(Fitemss97sub.class, "fitemss97sub")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Fitemss97sub.class, "fitemss97sub")));
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
	* 切换toggleIclose
	*/
	public void toggleIclose() {
		renderJson(service.toggleIclose(getLong(0)));
	}
}
