package cn.rjtech.admin.momaterialscanusedlog;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MoMaterialscanusedlogd;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 制造工单-材料耗用明细 Controller
 * @ClassName: MoMaterialscanusedlogdAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-26 09:36
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/momaterialscanusedlogd", viewPath = "/_view/admin/momaterialscanusedlogd")
public class MoMaterialscanusedlogdAdminController extends BaseAdminController {

	@Inject
	private MoMaterialscanusedlogdService service;

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
		MoMaterialscanusedlogd moMaterialscanusedlogd=service.findById(getLong(0)); 
		if(moMaterialscanusedlogd == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moMaterialscanusedlogd",moMaterialscanusedlogd);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MoMaterialscanusedlogd.class, "moMaterialscanusedlogd")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MoMaterialscanusedlogd.class, "moMaterialscanusedlogd")));
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
