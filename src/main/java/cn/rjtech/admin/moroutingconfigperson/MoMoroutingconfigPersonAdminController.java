package cn.rjtech.admin.moroutingconfigperson;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MoMoroutingconfigPerson;
/**
 * 制造工单-工单工艺人员配置 Controller
 * @ClassName: MoMoroutingconfigPersonAdminController
 * @author: RJ
 * @date: 2023-05-09 16:48
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/moroutingconfigperson", viewPath = "/_view/admin/moroutingconfigperson")
public class MoMoroutingconfigPersonAdminController extends BaseAdminController {

	@Inject
	private MoMoroutingconfigPersonService service;

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
		MoMoroutingconfigPerson moMoroutingconfigPerson=service.findById(getLong(0));
		if(moMoroutingconfigPerson == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moMoroutingconfigPerson",moMoroutingconfigPerson);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MoMoroutingconfigPerson.class, "moMoroutingconfigPerson")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MoMoroutingconfigPerson.class, "moMoroutingconfigPerson")));
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
