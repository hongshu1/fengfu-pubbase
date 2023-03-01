package cn.jbolt._admin.permission;

import cn.jbolt.core.enumutil.JBoltEnum;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.extend.config.ExtendProjectOfModule;
import com.jfinal.aop.Inject;

import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.model.Permission;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
@CheckPermission(PermissionKey.PERMISSION)
@UnCheckIfSystemAdmin
public class PermissionAdminController extends JBoltBaseController {
	@Inject
	private PermissionService service;
	public void index(){
		render("index_ajax.html");
	}
	/**
	 * ajax数据接口
	 */
	public void datas() {
		renderJsonData(service.getAdminPermissionsWithLevel(getLong("topnavId")));
	}
	/**
	 * ajax options数据接口
	 */
	public void options() {
		renderJsonData(service.getAllPermissionsOptionsWithLevel());
	}
	public void add(){
		set("pid", getLong(0,0L));
		set("level", getInt(1,1));
		render("add.html");
	}
	 
	public void edit(){
		Permission permission=service.findById(getLong(0));
		if(permission==null){
			renderFormFail("数据不存在或已被删除");
			return;
		}
		set("pid",permission.getPid());
		set("level", permission.getPermissionLevel());
		set("permission", permission);
		render("edit.html");
	}
	
	public void save(){
		renderJson(service.save(getModel(Permission.class,"permission")));
	}
	
	public void update(){
		renderJson(service.update(getModel(Permission.class,"permission")));
	}
	
	public void delete(){
		renderJson(service.delPermissionById(getLong(0),true));
	}
	
	public void up(){
		renderJson(service.up(getLong(0)));
	}
	
	public void down(){
		renderJson(service.down(getLong(0)));
	}
	
	public void initRank(){
		renderJson(service.initRank());
	}
	/**
	 * 切换是否是超管默认
	 */
	public void toggleSystemAdminDefault(){
		renderJson(service.toggleSystemAdminDefault(getLong(0)));
	}
	/**
	 * 列出模块
	 */
	@UnCheck
	public void modules(){
		renderJsonData(JBoltEnum.getEnumOptionList(ExtendProjectOfModule.class));
	}
	
}
