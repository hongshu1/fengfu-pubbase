package cn.rjtech.admin.momaterialsreturn;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MoMaterialsreturnd;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 制造工单-生产退料明细 Controller
 * @ClassName: MoMaterialsreturndAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-25 16:34
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/momaterialsreturnd", viewPath = "/_view/admin/momaterialsreturnd")
public class MoMaterialsreturndAdminController extends BaseAdminController {

	@Inject
	private MoMaterialsreturndService service;

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
		set("imodocid",getLong("imodocid"));
		render("add.html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		MoMaterialsreturnd moMaterialsreturnd=service.findById(getLong(0)); 
		if(moMaterialsreturnd == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moMaterialsreturnd",moMaterialsreturnd);
		render("edit.html");
	}



  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MoMaterialsreturnd.class, "moMaterialsreturnd")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MoMaterialsreturnd.class, "moMaterialsreturnd")));
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
