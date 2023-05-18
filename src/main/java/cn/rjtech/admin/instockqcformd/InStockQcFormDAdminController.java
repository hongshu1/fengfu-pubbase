package cn.rjtech.admin.instockqcformd;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.InStockQcFormD;
/**
 * 质量管理-在库检单行配置 Controller
 * @ClassName: InStockQcFormDAdminController
 * @author: RJ
 * @date: 2023-05-04 14:24
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/instockqcformd", viewPath = "/_view/admin/instockqcformd")
public class InStockQcFormDAdminController extends BaseAdminController {

	@Inject
	private InStockQcFormDService service;

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
		render("add().html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		InStockQcFormD inStockQcFormD=service.findById(getLong(0)); 
		if(inStockQcFormD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("inStockQcFormD",inStockQcFormD);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(InStockQcFormD.class, "inStockQcFormD")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InStockQcFormD.class, "inStockQcFormD")));
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
