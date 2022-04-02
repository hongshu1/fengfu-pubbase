package cn.jbolt._admin.user;

import java.util.List;

import com.jfinal.aop.Inject;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

import cn.jbolt._admin.dept.DeptService;
import cn.jbolt._admin.loginlog.LoginLogService;
import cn.jbolt._admin.loginlog.RemoteLoginLogService;
import cn.jbolt._admin.onlineuser.OnlineUserService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDeptCache;
import cn.jbolt.core.cache.JBoltPostCache;
import cn.jbolt.core.cache.JBoltRoleCache;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.JBoltUserService;
import cn.jbolt.core.util.JBoltPinYinUtil;
/**
 * 用户Service
 * @ClassName:  UserService   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2020年5月2日   
 */
public class UserService extends JBoltUserService {
	@Inject
	private LoginLogService loginLogService;
	@Inject
	private RemoteLoginLogService remoteLoginLogService;
	@Inject
	private DeptService deptService;
	@Inject
	private OnlineUserService onlineUserService;
	
	/**
	 * 保存
	 * @param user
	 * @return
	 */
	public Ret save(User user) {
		if(user==null||isOk(user.getId())||notOk(user.getName())
				||notOk(user.getUsername())){
			return fail(JBoltMsg.PARAM_ERROR);
		}
		return saveOrUpdate(user);
	}
	
	
	/**
	 * 更新
	 * @param user
	 * @return
	 */
	public Ret update(User user) {
		if(user==null||notOk(user.getId())||notOk(user.getName())
				||notOk(user.getUsername())){
			return fail(JBoltMsg.PARAM_ERROR);
		}
		
		return saveOrUpdate(user);
	}
	/**
	 * saveOrUpdate
	 * @param userId
	 * @param user
	 * @return
	 */
	private Ret saveOrUpdate( User user) {
		if(user==null||notOk(user.getName())
				||notOk(user.getUsername())){
			return fail(JBoltMsg.PARAM_ERROR);
		}
		
		boolean update=isOk(user.getId());
		if(update) {
//			if (existsName(user.getName(),user.getId())) {
//				return fail("用户【"+user.getName()+"】已经存在，请输入其它名称");
//			}
			if (exists("username",user.getUsername(),user.getId())) {
				return fail("用户名【"+user.getUsername()+"】已经存在，请输入其它名称");
			}
		}else {
			if(notOk(user.getPassword())) {
				return fail("请输入用户密码");
			}
//			if (existsName(user.getName())) {
//				return fail("用户【"+user.getName()+"】已经存在，请输入其它名称");
//			}
			if (exists("username",user.getUsername())) {
				return fail("用户名【"+user.getUsername()+"】已经存在，请输入其它名称");
			}
		}
		
		if(notOk(user.getAvatar())){
			if(isOk(user.getSex())) {
				user.setAvatar("assets/img/"+(user.getSex()==User.SEX_MALE?"nan.png":"nv.png"));
			}else {
				user.setAvatar("assets/img/nan.png");
			}
		}
		user.setName(user.getName().trim());
		user.setUsername(user.getUsername().trim());
		user.setPinyin(JBoltPinYinUtil.getSpells(user.getName()));
		user.setRoles(StrKit.isBlank(user.getRoles())?null:user.getRoles().trim());
		if(!update) {
			String password=user.getPassword().trim();
			String pwdSalt=HashKit.generateSaltForSha256();
			user.setPwdSalt(pwdSalt);
			user.setPassword(calPasswordWithSalt(password, pwdSalt));
			user.setCreateUserId(JBoltUserKit.getUserId());
			if(user.getEnable()==null) {
				user.setEnable(true);
			}
			if(user.getIsSystemAdmin()==null) {
				user.setIsSystemAdmin(false);
			}
		}
		boolean success=update?user.update():user.save();
		if(success){
			if(update) {
				addUpdateSystemLog(user.getId(), JBoltUserKit.getUserId(),user.getName());
			}else {
				addSaveSystemLog(user.getId(), JBoltUserKit.getUserId(),user.getName());
			}
		}
		return ret(success);
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		Long userId = JBoltUserKit.getUserId();
		if(isOk(id)&&userId.intValue()==id.intValue()){
			return fail("自己不能删除自己");
		}
		User user=findById(id);
		if(user==null) {
			return fail(JBoltMsg.DATA_NOT_EXIST);
		}
		Boolean isSysAdmin=user.getIsSystemAdmin();
		if(isSysAdmin!=null&&isSysAdmin) {
			return fail("不能删除超级管理员");
		}
		boolean success=user.delete();
		if(success) {
			addDeleteSystemLog(user.getId(), userId,user.getName());
		}
		return ret(success);
	}
	
	/**
	 * 后台管理查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @param sex 
	 * @param deptId 
	 * @param postId 
	 * @param roleId 
	 * @param enable 
	 * @return
	 */
	public Page<User> paginateAdminList(int pageNumber, int pageSize, String keywords, Integer sex, Long deptId, Long postId,Long roleId,Boolean enable) {
		String columns=getTableSelectColumnsWithout("password");
		Sql sql=selectSql().page(pageNumber, pageSize);
		sql.select(columns);
		//如果不是超管 就只能查询不是超管的用户
		if(!JBoltUserKit.isSystemAdmin()) {
			sql.eq("is_system_admin", FALSE);
		}
		sql.likeMulti(keywords, "name","username","phone","pinyin");
		if(enable!=null) {
			sql.eq("enable", enable?TRUE:FALSE);
		}
		if(isOk(deptId)) {
			List<Long> ids=deptService.processSonDeptIds(deptId);
			if(ids.size()>0) {
				ids.add(0,deptId);
				sql.in("dept_id",ids);
			}else {
				sql.eq("dept_id", deptId);
			}
		}
		if(isOk(roleId)) {
			sql.findInSet(roleId.toString(), "roles", true);
		}
		sql.eq("sex",sex);
		sql.findInSet(postId, "posts", true);
		sql.orderById(true);
		Page<User> userPageData = paginate(sql);
		if(userPageData.getTotalRow()>0) {
			userPageData.getList().forEach(user->{
				user.setDeptSnAndName(JBoltDeptCache.me.getSnAndName(user.getDeptId()));
				user.setUserRoles(JBoltRoleCache.me.getRoles(user.getRoles()));
				user.setUserPosts(JBoltPostCache.me.getPosts(user.getPosts()));
			});
		}
		return userPageData;
	}
	
	/**
	 * 系统通知选择用户数据源
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @param sex
	 * @param deptId
	 * @param postId
	 * @param roleId
	 * @return
	 */
	public Page<User> paginateSysNoticeList(int pageNumber, int pageSize, String keywords, Integer sex, Long deptId, Long postId,Long roleId) {
		return paginateAdminList(pageNumber, pageSize, keywords, sex, deptId, postId, roleId, true);
	}
}
