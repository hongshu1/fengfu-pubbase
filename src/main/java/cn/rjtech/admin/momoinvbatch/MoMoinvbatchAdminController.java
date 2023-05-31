package cn.rjtech.admin.momoinvbatch;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MoMoinvbatch;
/**
 * 工单现品票 Controller
 * @ClassName: MoMoinvbatchAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-31 15:35
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/momoinvbatch", viewPath = "/_view/admin/momoinvbatch")
public class MoMoinvbatchAdminController extends BaseAdminController {

	@Inject
	private MoMoinvbatchService service;

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
		MoMoinvbatch moMoinvbatch=service.findById(getLong(0)); 
		if(moMoinvbatch == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moMoinvbatch",moMoinvbatch);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MoMoinvbatch.class, "moMoinvbatch")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MoMoinvbatch.class, "moMoinvbatch")));
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
