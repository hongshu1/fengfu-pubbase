package cn.rjtech.admin.enumvals;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.EnumVals;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.tx.Tx;
/**
 * 系统管理-枚举值
 * @ClassName: EnumValsAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-22 09:35
 */
@CheckPermission(PermissionKey.ENUM_VALS)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/enumVals", viewPath = "/_view/admin/enumvals")
public class EnumValsAdminController extends BaseAdminController {

	@Inject
	private EnumValsService service;
   /**
	* 首页
	*/
	public void index() {
		render("index.html");
	}
   /**
	* 数据源
	*/
   @UnCheck
	public void datas() {
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getLong("iEnumTypeId")));
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
		renderJson(service.save(getModel(EnumVals.class, "enumVals")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		EnumVals enumVals=service.findById(getLong(0));
		if(enumVals == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("enumVals",enumVals);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(EnumVals.class, "enumVals")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

	/**
	 * 新增-可编辑表格-批量提交
	 */
	@Before(Tx.class)

	public void submitAll() {
		renderJson(service.submitByJBoltTable(getJBoltTable()));
	}

	@UnCheck
	public void enumvalsoptions() {
		renderJsonData(service.getCommonList("cEnumCode,cEnumName"));
	}


}
