package cn.rjtech.admin.scheduproductplan;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ApsAnnualplanm;
/**
 * 年度生产计划 Controller
 * @ClassName: ApsAnnualplanmAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-31 13:20
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/scheduproductplan", viewPath = "/_view/admin/scheduproductplan")
public class ApsAnnualplanmAdminController extends BaseAdminController {

	@Inject
	private ApsAnnualplanmService service;

   /**
	* 首页
	*/
	public void index() {
		render("index().html");
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
		ApsAnnualplanm apsAnnualplanm=service.findById(getLong(0)); 
		if(apsAnnualplanm == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("apsAnnualplanm",apsAnnualplanm);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(ApsAnnualplanm.class, "apsAnnualplanm")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(ApsAnnualplanm.class, "apsAnnualplanm")));
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
