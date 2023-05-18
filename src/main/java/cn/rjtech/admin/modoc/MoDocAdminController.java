package cn.rjtech.admin.modoc;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MoDoc;
import com.jfinal.kit.Ret;

import java.util.HashMap;

/**
 * 工单管理 Controller
 * @ClassName: MoDocAdminController
 * @author: RJ
 * @date: 2023-04-26 16:15
 */
@CheckPermission(PermissionKey.MO_DOCBACTH)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/modoc", viewPath = "/_view/admin/modoc")
public class MoDocAdminController extends BaseAdminController {

	@Inject
	private MoDocService service;

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
	* 查看
	*/
	public void details() {
		MoDoc moDoc=service.findById(getLong(0));
		if(moDoc == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moDoc",moDoc);
		//拼上生产任务数据
		HashMap<String, String> stringStringHashMap = service.getJob(getLong(0));
		set("productionTasks",stringStringHashMap);
		// 拼上 科系名称,存货编码,客户部番,部品名称,生产单位,规格,产线名称,班次名称,工艺路线名称
		render("_detailsform.html");
	}




	/**
	 * 新增
	 */
	public void edit() {
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {

		renderJson(service.save(getJBoltTable()));
	}
	public void persondialog(){
		render("persondialog.html");
	}


   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MoDoc.class, "moDoc")));
	}





}
