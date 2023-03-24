package cn.rjtech.admin.containerclass;

import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ContainerClass;
/**
 * 分类管理
 * @ClassName: ContainerClassAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 16:16
 */
@CheckPermission(PermissionKey.CONTAINERCLASS)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/containerclass", viewPath = "/_view/admin/containerclass")
public class ContainerClassAdminController extends BaseAdminController {

	@Inject
	private ContainerClassService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize()));
	}

	/**
	 * 数据源
	 */
	public void list() {
		Okv kv = Okv.create();
		kv.set("IsEnabled", 1);
		kv.set("IsDeleted", 0);

		renderJsonData(service.getCommonList(kv, "dCreateTime", "desc"));
//		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize()));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add.html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(ContainerClass.class, "containerClass")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		ContainerClass containerClass=service.findById(getLong(0));
		if(containerClass == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("containerClass",containerClass);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(ContainerClass.class, "containerClass")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}

	/**
	 * Excel模板下载
	 */
	@SuppressWarnings("unchecked")
	public void downloadTpl() throws Exception {
		renderJxls("container_import.xlsx", Kv.by("rows", null), "容器分类导入模板.xlsx");
	}


}
