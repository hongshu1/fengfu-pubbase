package cn.rjtech.admin.enumtype;

import cn.rjtech.admin.enumvals.EnumValsService;
import cn.rjtech.model.momdata.EnumVals;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.EnumType;

import java.util.List;

/**
 * 系统管理-枚举类别
 * @ClassName: EnumTypeAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-22 09:08
 */
@CheckPermission(PermissionKey.ENUM_TYPE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/enumType", viewPath = "/_view/admin/enumtype")
public class EnumTypeAdminController extends BaseAdminController {

	@Inject
	private EnumTypeService service;
	@Inject
	private EnumValsService enumValsService;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("cEnumTypeName")));
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
		renderJson(service.save(getModel(EnumType.class, "enumType")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		EnumType enumType=service.findById(getLong(0));
		if(enumType == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		System.out.println("aaaaaaaaaaa"+enumType.getCEnumTypeCode());

		set("enumType",enumType);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(EnumType.class, "enumType")));
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
		//检测是否关联使用
		String cenumtypecode = service.getOneColumnValueById(getLong(0),"cEnumTypeCode");
		List<EnumVals> ifcEnumTypeCide = enumValsService.find("SELECT * FROM Bd_EnumVals WHERE iEnumTypeId = ?",cenumtypecode);
		if(ifcEnumTypeCide.size()>0){
			renderFail(JBoltMsg.DATA_INUSE);
			return;
		}
		renderJson(service.deleteById(getLong(0)));
	}

	/**
	 * select数据源（查询、可编辑表格）
	 */
	public void options() {
		renderJsonData(service.getCommonList("cEnumTypeCode,cEnumTypeName"));
	}

	/**
	 * select数据源(编辑、新增)
	 */
	public void ajaxPortalTable() {
		set("users", service.findAll());
		render("ajaxportal_table.html");
	}



}
