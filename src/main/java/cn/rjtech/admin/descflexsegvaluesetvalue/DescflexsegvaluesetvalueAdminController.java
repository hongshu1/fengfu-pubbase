package cn.rjtech.admin.descflexsegvaluesetvalue;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.model.momdata.Descflexsegvaluesetvalue;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 实体扩展字段值集 Controller
 * @ClassName: DescflexsegvaluesetvalueAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-04 11:11
 */
@CheckPermission(PermissionKey.DESCFLEXSEGVALUESETVALUE)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/descflexsegvaluesetvalue", viewPath = "/_view/admin/descflexsegvaluesetvalue")
public class DescflexsegvaluesetvalueAdminController extends JBoltBaseController {

	@Inject
	private DescflexsegvaluesetvalueService service;

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
		Descflexsegvaluesetvalue descflexsegvaluesetvalue=service.findById(getLong(0));
		if(descflexsegvaluesetvalue == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("descflexsegvaluesetvalue",descflexsegvaluesetvalue);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(Descflexsegvaluesetvalue.class, "descflexsegvaluesetvalue")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Descflexsegvaluesetvalue.class, "descflexsegvaluesetvalue")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}


}
