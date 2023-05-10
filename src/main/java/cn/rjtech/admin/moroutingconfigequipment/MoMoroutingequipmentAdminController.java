package cn.rjtech.admin.moroutingconfigequipment;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MoMoroutingequipment;
/**
 * 制造工单-生产工艺路线设备 Controller
 * @ClassName: MoMoroutingequipmentAdminController
 * @author: RJ
 * @date: 2023-05-09 16:52
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/moroutingconfigequipment", viewPath = "/_view/admin/moroutingconfigequipment")
public class MoMoroutingequipmentAdminController extends BaseAdminController {

	@Inject
	private MoMoroutingequipmentService service;

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
		MoMoroutingequipment moMoroutingequipment=service.findById(getLong(0));
		if(moMoroutingequipment == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moMoroutingequipment",moMoroutingequipment);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MoMoroutingequipment.class, "moMoroutingequipment")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MoMoroutingequipment.class, "moMoroutingequipment")));
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
