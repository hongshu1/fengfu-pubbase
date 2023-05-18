package cn.rjtech.admin.mrpdemandforecastd;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MrpDemandforecastd;
/**
 * 物料需求预示子表 Controller
 * @ClassName: MrpDemandforecastdAdminController
 * @author: chentao
 * @date: 2023-05-06 15:16
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/mrpdemandforecastd", viewPath = "/_view/admin/mrpdemandforecastd")
public class MrpDemandforecastdAdminController extends BaseAdminController {

	@Inject
	private MrpDemandforecastdService service;

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
		MrpDemandforecastd mrpDemandforecastd=service.findById(getLong(0)); 
		if(mrpDemandforecastd == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("mrpDemandforecastd",mrpDemandforecastd);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MrpDemandforecastd.class, "mrpDemandforecastd")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MrpDemandforecastd.class, "mrpDemandforecastd")));
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
	* 切换toggleIsLocked1
	*/
	public void toggleIsLocked1() {
		renderJson(service.toggleIsLocked1(getLong(0)));
	}

  /**
	* 切换toggleIsLocked2
	*/
	public void toggleIsLocked2() {
		renderJson(service.toggleIsLocked2(getLong(0)));
	}

  /**
	* 切换toggleIsLocked3
	*/
	public void toggleIsLocked3() {
		renderJson(service.toggleIsLocked3(getLong(0)));
	}


}
