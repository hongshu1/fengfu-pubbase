package cn.rjtech.admin.descflexfielddef;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.descflexsegvaluesetvalue.DescflexsegvaluesetvalueService;
import cn.rjtech.model.momdata.Descflexfielddef;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 实体扩展字段 Controller
 * @ClassName: DescflexfielddefAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-04 11:10
 */
@CheckPermission(PermissionKey.DESCFLEXFIELDDEF)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/descflexfielddef", viewPath = "/_view/admin/descflexfielddef")
public class DescflexfielddefAdminController extends JBoltBaseController {

	@Inject
	private DescflexfielddefService service;
	@Inject
	private DescflexsegvaluesetvalueService descflexsegvaluesetvalueService;

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
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(), getKv()));
	}

	public void getTableList(){
		renderJsonData(service.getTableList(getKv()));
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
		Descflexfielddef descflexfielddef=service.findById(getLong(0));
		if(descflexfielddef == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("descflexfielddef",descflexfielddef);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(Descflexfielddef.class, "descflexfielddef")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Descflexfielddef.class, "descflexfielddef")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}

  /**
	* 切换toggleIsenabled
	*/
	public void toggleIsenabled() {
		renderJson(service.toggleIsenabled(getLong(0)));
	}

	/**
	 * 根据表名查询扩展字段
	 */
	public void getDescflexfielddefAndTableName(){
		ValidationUtils.notNull(get("leftcentityname"), JBoltMsg.PARAM_ERROR);

		renderJsonData(service.getDescflexfielddefList(getKv()));
	}

	public void getByMesDataIdList(){
		renderJsonData(descflexsegvaluesetvalueService.findByMesDataIdList(getLong("imesdataid")));
	}



}
