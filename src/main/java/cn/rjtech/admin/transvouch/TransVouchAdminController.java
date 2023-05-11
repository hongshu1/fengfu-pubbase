package cn.rjtech.admin.transvouch;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.TransVouch;
/**
 * 出库管理-调拨单列表 Controller
 * @ClassName: TransVouchAdminController
 * @author: RJ
 * @date: 2023-05-11 14:54
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/transvouch", viewPath = "/_view/admin/transvouch")
public class TransVouchAdminController extends BaseAdminController {

	@Inject
	private TransVouchService service;

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
		TransVouch transVouch=service.findById(getLong(0)); 
		if(transVouch == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("transVouch",transVouch);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(TransVouch.class, "transVouch")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(TransVouch.class, "transVouch")));
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
