package cn.jbolt._admin.permission;

import cn.jbolt.core.enumutil.JBoltEnum;
import cn.jbolt.core.model.RjApplication;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.service.RjApplicationService;
import cn.jbolt.extend.config.ExtendProjectOfModule;
import com.jfinal.aop.Inject;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.model.Permission;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.core.paragetter.Para;

import java.util.List;

@CheckPermission(PermissionKey.PERMISSION)
@UnCheckIfSystemAdmin
public class PermissionAdminController extends JBoltBaseController {
	@Inject
	private PermissionService service;
    @Inject
    private RjApplicationService rjApplicationService;

	public void index(){
		render("index_ajax.html");
	}
	/**
	 * ajax数据接口
	 */
    @UnCheck
	public void datas() {
		renderJsonData(service.getAdminPermissionRecordsWithLevel(getLong("applicationId"), getLong("appId"), getLong("topnavId")));
	}
	/**
	 * ajax options数据接口
	 */
    @UnCheck
	public void options() {
		renderJsonData(service.getAllPermissionsOptionsWithLevel(getLong("applicationId", 0L), getLong("appId", 0L)));
	}
	public void add(){
        long pid = getLong(0, 0L);
        int level = getInt(1, 1);

		set("pid", pid);
		set("level", level);

        if (pid > 0) {
            Permission parentPermission = service.findById(pid);
            ValidationUtils.notNull(parentPermission, "当前上级不存在");

            Permission permission = new Permission();
            permission.setApplicationId(parentPermission.getApplicationId());
            permission.setAppId(parentPermission.getAppId());
            permission.setPid(pid);
            set("permission", permission);
        }

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
	

    /**
     * ajax options数据接口
     */
    @UnCheck
    public void options2(@Para(value = "applicationId") Long applicationId,
                         @Para(value = "appId") Long appId) {
        ValidationUtils.validateId(applicationId, "系统应用ID");
        ValidationUtils.validateId(appId, "开发中心应用ID");

        // 通过应用编码获取平台应用ID
        RjApplication rjApplication = rjApplicationService.findById(applicationId);
        ValidationUtils.notNull(rjApplication, "系统应用不存在");

        renderJsonData(service.getAllPermissionsOptionsWithLevel(rjApplication.getId(), appId));
    }

    public void saveTableSubmit() {
        renderJson(service.saveTableSubmit(getJBoltTable()));
    }

    public void menuJsTree(@Para(value = "openLevel") Integer openLevel,
                           @Para(value = "applicationId") Long applicationId,
                           @Para(value = "enableIcon") String enableIcon,
                           @Para(value = "checkedIds") String checkedIds,
                           @Para(value = "renderEmpty") Integer renderEmpty) {
        if (null == renderEmpty) {
            renderJsonData(service.getMenuJsTree(openLevel, applicationId, enableIcon, checkedIds));
        } else {
            renderJsonData(CollUtil.empty(List.class));
        }
    }

    /**
     * 用户或角色的 菜单及按钮权限
     */
    public void permissions(@Para(value = "roletype") Integer roletype,
                            @Para(value = "id") Long id) {
        ValidationUtils.notNull(roletype, "缺少角色类型");
        ValidationUtils.validateId(id, "参数ID");

        renderJson(service.getUserOrRolePermissions(roletype, id));
    }

	/**
	 * 数据源
	 */
    @UnCheck
	public void optionsFormId(){
		renderJsonData(service.getDatas());
	}
    @UnCheck
	public void optionsFormFieldId(){
		renderJsonData(service.getFormDatas());
	}
}
