package cn.jbolt._admin.user;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt._admin.role.RoleService;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.Option;
import cn.jbolt.core.bean.OptionBean;
import cn.jbolt.core.cache.JBoltCacheInterceptor;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.service.JBoltFileService;

@CheckPermission(PermissionKey.USER)
@UnCheckIfSystemAdmin
public class UserAdminController extends JBoltBaseController {
	@Inject
	private UserService service;
	@Inject
	private RoleService roleService;
	@Inject
	private JBoltFileService jboltFileService;
	/**
	 * 用户表数作为选项数据源
	 */
	@UnCheck
	public void options() {
		renderJsonData(service.getOptionList());
	}
	/**
	 * 管理首页
	 */
	public void index(){
		render("index_ajax.html");
	}
	/**
	 * 表格数据接口
	 */
	public void datas() {
		renderJsonData(service.paginateAdminList(getPageNumber(),getPageSize(),getKeywords(),getInt("sex"),getLong("deptId"),getLong("postId"),getLong("roleId"),getBoolean("enable")));
	}

	/**
	 *  系统通知 可用 选择用户数据接口
	 */
	public void sysnoticeUsers() {
		renderJsonData(service.paginateSysNoticeList(getPageNumber(),getPageSize(),getKeywords(),getInt("sex"),getLong("deptId"),getLong("postId"),getLong("roleId")));
	}
	/**
	  * 获取用户列表 
	  * 通过关键字匹配 
	 * autocomplete组件使用
	 */
	@UnCheck
	public void autocomplete() {
		renderJsonData(service.getAutocompleteList(get("q"), getInt("limit",10),true,"name,username,pinyin,phone"));
	}
	/**
	 * 上传头像
	 */
	public void uploadAvatar(){
		String uploadPath=JBoltUploadFolder.todayFolder(JBoltUploadFolder.USER_AVATAR);
		UploadFile file=getFile("file",uploadPath);
		if(notImage(file)){
			renderJsonFail("请上传图片类型文件");
			return;
		}
		renderJson(jboltFileService.saveImageFile(file,uploadPath));
	}
	
	/**
	 * 上传头像
	 */
	@UnCheck
	public void uploadMyAvatar(){
		String uploadPath=JBoltUploadFolder.todayFolder(JBoltUploadFolder.USER_AVATAR);
		UploadFile file=getFile("file",uploadPath);
		if(notImage(file)){
			renderJsonFail("请上传图片类型文件");
			return;
		}
		Long userId=JBoltUserKit.getUserId();
		Ret ret=jboltFileService.saveImageFile(file,uploadPath);
		if(ret.isFail()) {
			renderJson(ret);
			return;
		}
		String url=ret.getStr("data");
		renderJson(service.updateUserAvatar(userId,url));
	}
	@Before(JBoltCacheInterceptor.class)
	@UnCheck
	public void sexOptions(){
		List<Option> options=new ArrayList<Option>();
		options.add(new OptionBean("男",User.SEX_MALE));
		options.add(new OptionBean("女",User.SEX_FEMALE));
		options.add(new OptionBean("未知",User.SEX_NONE));
		renderJsonData(options);
	}
	/**
	 * 进入自身密码修改界面
	 */
	@UnCheck
	public void pwd(){
		render("pwd.html");
	}
	/**
	 * 进入自身头像修改界面
	 */
	@UnCheck
	public void avatar(){
		setAttr("avatar", JBoltUserCache.me.getAvatar(JBoltUserKit.getUserId()));
		render("avatar.html");
	}
	/**
	 * 进入重置用户密码界面
	 */
	@CheckPermission(PermissionKey.USER)
	public void editpwd(){
		set("userId", getLong(0));
		render("editpwd.html");
	}
	/**
	 * 重置用户密码
	 */
	@CheckPermission(PermissionKey.USER)
	public void submitpwd(){
		Long userId=getLong("userId");
		String newPwd=get("newPwd");
		String reNewPwd=get("reNewPwd");
		if(notOk(newPwd)||notOk(reNewPwd)){ 
			renderJsonFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		if(newPwd.equals(reNewPwd)==false){
			renderJsonFail("两次新密码输入不一致");
			return;
		}
		renderJson(service.submitPwd(userId,newPwd));
	}
	
	/**
	 * 修改用户自己的密码
	 */
	@UnCheck
	public void updatepwd(){
		String oldPwd=get("oldPwd");
		String newPwd=get("newPwd");
		String reNewPwd=get("reNewPwd");
		if(notOk(oldPwd)||notOk(newPwd)||notOk(reNewPwd)){ 
			renderJsonFail(JBoltMsg.PARAM_ERROR);
			return;
			}
		if(newPwd.equals(reNewPwd)==false){
			renderJsonFail("两次新密码输入不一致");
			return;
		}
		renderJson(service.updatePwd(oldPwd,newPwd));
	}
	
	
	/**
	 * 新增
	 */
	public void add(){
		set("roles", roleService.findAll());
		render("add.html");
	}
	/**
	 * 编辑
	 */
	public void edit(){
		Long userId=getLong(0);
		Long myId=JBoltUserKit.getUserId();
		User user=service.findById(userId);
		if(user==null) {
			renderDialogFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		User me=service.findById(myId);
		if((user.getIsSystemAdmin()!=null&&user.getIsSystemAdmin())&&(me.getIsSystemAdmin()==null||me.getIsSystemAdmin()==false)) {
			renderDialogFail("无权修改超管员信息");
			return;
		}
		set("user", user);
		render("edit.html");
	}
	/**
	 * 保存
	 */
	public void save(){
		renderJson(service.save(getModel(User.class, "user")));
	}
	/**
	 * 更新
	 */
	public void update(){
		renderJson(service.update(getModel(User.class, "user")));
	}
	/**
	 * 删除
	 */
	public void delete(){
		renderJson(service.delete(getLong(0)));
	}
	/**
	 * 切换启用状态
	 */
	@Before(Tx.class)
	public void toggleEnable(){
		renderJson(service.toggleEnable(getLong(0)));
	}
	/**
	 * 删除角色
	 */
	public void deleteRole(){
		renderJson(service.deleteUserRole(getLong(0),getLong(1)));
	}
}
