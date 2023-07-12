package cn.jbolt._admin.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.dept.DeptService;
import cn.jbolt._admin.role.RoleService;
import cn.jbolt.common.model.UserExtend;
import cn.jbolt.core.base.JBoltGlobalConfigKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.*;
import cn.jbolt.core.common.enums.UserTypeEnum;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.enumutil.JBoltEnum;
import cn.jbolt.core.kit.JBoltCurrentOfModuleKit;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.kit.OrgAccessKit;
import cn.jbolt.core.model.*;
import cn.jbolt.core.service.JBoltUserService;
import cn.jbolt.core.service.UserTypeService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;
import cn.jbolt.core.util.JBoltPinYinUtil;
import cn.jbolt.extend.config.ExtendProjectOfModule;
import cn.jbolt.extend.user.ExtendUserOfModuleLinkService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.admin.userorg.UserOrgService;
import cn.rjtech.admin.userthirdparty.UserThirdpartyService;
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.BoolCharEnum;
import cn.rjtech.enums.ThirdpartySystemEnum;
import cn.rjtech.model.main.UserOrg;
import cn.rjtech.model.main.UserThirdparty;
import cn.rjtech.model.momdata.Person;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.*;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 用户Service
 *
 * @ClassName: UserService
 * @author: JFinal学院-小木 QQ：909854136
 * @date: 2020年5月2日
 */
public class UserService extends JBoltUserService {

    @Inject
    private RoleService roleService;
    @Inject
    private DeptService deptService;
    @Inject
    private PersonService personService;
    @Inject
    private UserOrgService userOrgService;
    @Inject
    private UserTypeService userTypeService;
    @Inject
    private UserExtendService userExtendService;
    @Inject
    private UserThirdpartyService userThirdpartyService;
    @Inject
    private ExtendUserOfModuleLinkService extendUserOfModuleLinkService;

    /**
     * 保存
     */
    public Ret save(User user, UserExtend extend) {
        if (user == null || isOk(user.getId()) || notOk(user.getName()) || notOk(user.getUsername())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        boolean userExtendEnable = JBoltGlobalConfigCache.me.userExtendEnable();
        if (userExtendEnable && extend != null && isOk(extend.getId())) {
            return fail("用户扩展信息" + JBoltMsg.PARAM_ERROR);
        }
        Ret ret = saveOrUpdate(user);
        if (ret.isFail()) {
            return ret;
        }
        //添加完user后处理extend
        if (userExtendEnable && extend != null) {
            extend.setId(user.getId());
            ret = userExtendService.saveByUser(extend);
            if (ret.isFail()) {
                return ret;
            }
        }
        return SUCCESS;
    }


    /**
     * 处理用户的所属模块信息
     *
     * @param user
     */
    protected void processUserOfMoudle(User user) {
        if (notOk(user.getOfModule()) || user.getOfModule().intValue() == 1) {
            //如果没设置就save 那得设置上 从当前线程获取 看看有没有 如果有就用了
            Integer jboltOfModule = JBoltCurrentOfModuleKit.getJboltOfModule();
            ExtendProjectOfModule type = null;
            if (notOk(jboltOfModule)) {
                type = ExtendProjectOfModule.PLATFORM_INNER_ADMIN;
            } else {
                type = JBoltEnum.getEnumObjectByValue(ExtendProjectOfModule.class, jboltOfModule.intValue());

            }
            if (type == null) {
                throw new RuntimeException("沒有找到对应的ExtendProjectOfModuleType");
            }
            user.setOfModule(type.getValue());
            processOfModuleLinkByType(user, type);
        }
    }

    private void processOfModuleLinkByType(User user, ExtendProjectOfModule type) {
        if (type == ExtendProjectOfModule.PLATFORM_INNER_ADMIN) {
            processOfModuleLinkIsInnerApplication(user);
        } else {
            user.setOfModuleLink(extendUserOfModuleLinkService.processOfModuleLinkByType(type));
        }
    }

    /**
     * 设置 内置模块link为内置app
     *
     * @param user
     */
    private void processOfModuleLinkIsInnerApplication(User user) {
        Application application = JBoltApplicationCache.me.getPcInnerPlatformApplication();
        user.setOfModuleLink(application.getId().toString());
    }

    /**
     * saveOrUpdate
     *
     * @param user
     * @return
     */
    private Ret saveOrUpdate(User user) {
        if (user == null || notOk(user.getName())
                || notOk(user.getUsername())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        boolean update = isOk(user.getId());
        if (update) {
//			if (existsName(user.getName(),user.getId())) {
//				return fail("用户【"+user.getName()+"】已经存在，请输入其它名称");
//			}
            if (exists("username", user.getUsername(), user.getId())) {
                return fail("用户名【" + user.getUsername() + "】已经存在，请输入其它名称");
            }
            User dbUser = findById(user.getId());
            if (dbUser == null) {
                return fail(JBoltMsg.DATA_NOT_EXIST);
            }
            dbUser.deleteKeyCache();
            //清理角色变更缓存
            if(ObjUtil.notEqual(dbUser.getRoles(), user.getRoles())) {
                //存在其他跟自己相同角色的就不清理缓存 不存在说明自己独占 就清理
                boolean existSameRoles = existsSameRoles(dbUser.getRoles(),dbUser.getId());
                if(!existSameRoles){
                    long appId = JBoltGlobalConfigCache.me.getLongConfigValue(JBoltGlobalConfigKey.MOM_APP_ID);
                    long applicationId = JBoltGlobalConfigCache.me.getLongConfigValue(JBoltGlobalConfigKey.MOM_APPLICATION_ID);

                    JBoltPermissionCache.me.removeRolesPermissionKeySet(dbUser.getRoles());
                    JBoltPermissionCache.me.removeRolesMenus(dbUser.getId(), appId, applicationId, dbUser.getRoles());
                    JBoltPermissionCache.me.removeRolesMenusWithSystemAdminDefault(dbUser.getId(), appId, applicationId, dbUser.getRoles());
                }
            }
        } else {
            if (notOk(user.getPassword())) {
                return fail("请输入用户密码");
            }
//			if (existsName(user.getName())) {
//				return fail("用户【"+user.getName()+"】已经存在，请输入其它名称");
//			}
            if (exists("username", user.getUsername())) {
                return fail("用户名【" + user.getUsername() + "】已经存在，请输入其它名称");
            }
        }

        if (notOk(user.getAvatar())) {
            if (isOk(user.getSex())) {
                user.setAvatar("assets/img/" + (user.getSex() == User.SEX_MALE ? "nan.png" : "nv.png"));
            } else {
                user.setAvatar("assets/img/nan.png");
            }
        }
        user.setName(user.getName().trim());
        user.setUsername(user.getUsername().trim());
        user.setPinyin(JBoltPinYinUtil.getSpells(user.getName()));
        user.setRoles(StrKit.isBlank(user.getRoles()) ? null : user.getRoles().trim());
        if (!update) {
            String password = user.getPassword().trim();
            String pwdSalt = HashKit.generateSaltForSha256();
            user.setPwdSalt(pwdSalt);
            user.setPassword(calPasswordWithSalt(password, pwdSalt));
            user.setCreateUserId(JBoltUserKit.getUserId());
            if (user.getEnable() == null) {
                user.setEnable(true);
            }
            if (user.getIsSystemAdmin() == null) {
                user.setIsSystemAdmin(false);
            }
        }
        //处理用户的deptPath
        if (isOk(user.getDeptId()) && notOk(user.getDeptPath())) {
            Dept dept = deptService.findById(user.getDeptId());
            if (dept == null) {
                return fail("指定部门数据不存在");
            }
            if (notOk(dept.getDeptPath())) {
                return fail("指定部门的路径数据不完整，请执行部门路径补全操作");
            }
            user.setDeptPath(dept.getDeptPath());
        }
        user.setLastPwdUpdateTime(new Date());
        if (!update) {
            processUserOfMoudle(user);
        }
        //保存或者更新
        boolean success = update ? user.update() : user.save();
        if (success) {
            if (update) {
                addUpdateSystemLog(user.getId(), JBoltUserKit.getUserId(), user.getName());
            } else {
                addSaveSystemLog(user.getId(), JBoltUserKit.getUserId(), user.getName());
            }
        }
        return ret(success);
    }

    /**
     * 更新
     *
     * @param user
     * @return
     */
    public Ret update(User user, UserExtend extend) {
        if (user == null || notOk(user.getId()) || notOk(user.getName()) || notOk(user.getUsername())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        boolean userExtendEnable = JBoltGlobalConfigCache.me.userExtendEnable();
        if (userExtendEnable) {
            if (extend != null && notOk(extend.getId())) {
                return fail("用户扩展信息" + JBoltMsg.PARAM_ERROR);
            }
            if (extend != null && !user.getId().equals(extend.getId())) {
                return fail("用户扩展信息" + JBoltMsg.PARAM_ERROR + ",与用户主键不一致");
            }
        }
        Ret ret = saveOrUpdate(user);
        if (ret.isFail()) {
            return ret;
        }
        //添加完user后处理extend
        if (userExtendEnable && extend != null) {
            extend.setId(user.getId());
            ret = userExtendService.updateByUser(extend);
            if (ret.isFail()) {
                return ret;
            }
        }
        return SUCCESS;
    }

    /**
     * 查询是否存在相同角色组用户 排除自身
     *
     * @param roles
     * @param userId
     * @return
     */
    private boolean existsSameRoles(String roles, Long userId) {
        return exists("roles", roles, userId);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public Ret delete(Long id) {
        Long userId = JBoltUserKit.getUserId();
        if (isOk(id) && userId.intValue() == id.intValue()) {
            return fail("自己不能删除自己");
        }
        User user = findById(id);
        if (user == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        Boolean isSysAdmin = user.getIsSystemAdmin();
        if (isSysAdmin != null && isSysAdmin) {
            return fail("不能删除超级管理员");
        }
        boolean success = user.delete();
        if (success) {
            addDeleteSystemLog(user.getId(), userId, user.getName());
            boolean userExtendEnable = JBoltGlobalConfigCache.me.userExtendEnable();
            if (userExtendEnable) {
                //同步删除扩展信息
                Ret ret = userExtendService.deleteById(user.getId());
                if (ret.isFail()) {
                    return ret;
                }
            }
        }
        return ret(success);
    }

    /**
     * 后台管理查询
     *
     * @param pageNumber
     * @param pageSize
     * @param keywords
     * @param ofModule
     * @param sex
     * @param assignDept
     * @param deptId
     * @param postId
     * @param roleId
     * @param enable
     * @return
     */
    public Page<User> paginateAdminList(int pageNumber, int pageSize, String keywords, Integer ofModule, Integer sex, Boolean assignDept, Long deptId, Long postId, Long roleId, Boolean enable, Long excludeRoleId) {
        String[] columns = getTableSelectColumnsWithout("password");
        Sql sql = selectSql().page(pageNumber, pageSize);
        sql.select(columns);
        sql.eqIfOk(User.OF_MODULE, ofModule);
        //如果不是超管 就只能查询不是超管的用户
        if (!JBoltUserKit.isSystemAdmin()) {
            sql.eq("is_system_admin", FALSE);
        }
        sql.likeMulti(keywords, "name", "username", "phone", "pinyin");
        if (enable != null) {
            sql.eq("enable", enable ? TRUE : FALSE);
        }
		/*if(isOk(deptId)) {
			List<Long> ids=deptService.processSonDeptIds(deptId);
			if(ids.size()>0) {
				ids.add(0,deptId);
				sql.in("dept_id",ids);
			}else {
				sql.eq("dept_id", deptId);
			}
		}*/
        if (assignDept != null && assignDept) {
            //按照部门搜索
            if (isOk(deptId)) {
                sql.findInSet(deptId, "dept_path", true);
            }
        } else {
            sql.isNull("dept_id");
        }

        if (isOk(roleId)) {
            sql.findInSet(roleId.toString(), "roles", true);
        }
        if (isOk(excludeRoleId)) {
            sql.notLike("roles", excludeRoleId.toString()).or().isNull("roles");
        }
        sql.eq("sex", sex);
        sql.findInSet(postId, "posts", true);
        sql.orderById(true);
        Page<User> userPageData = paginate(sql);
        if (userPageData.getTotalRow() > 0) {
            userPageData.getList().forEach(user -> {
                user.setDeptSnAndName(JBoltDeptCache.me.getSnAndName(user.getDeptId()));
                user.setUserRoles(JBoltRoleCache.me.getRoles(user.getRoles()));
                user.setUserPosts(JBoltPostCache.me.getPosts(user.getPosts()));
            });
        }
        return userPageData;
    }

    /**
     * 系统通知选择用户数据源
     *
     * @param pageNumber
     * @param pageSize
     * @param keywords
     * @param sex
     * @param assignDept
     * @param deptId
     * @param postId
     * @param roleId
     * @return
     */
    public Page<User> paginateSysNoticeList(int pageNumber, int pageSize, String keywords, Integer sex, Boolean assignDept, Long deptId, Long postId, Long roleId) {
        return paginateAdminList(pageNumber, pageSize, keywords, ExtendProjectOfModule.PLATFORM_INNER_ADMIN.getValue(), sex, assignDept, deptId, postId, roleId, true, null);
    }

    /**
     * 处理所有用户的deptPath
     */
    public void processAllDeptPath() {
        List<User> users = new ArrayList<>();
        dao().each(user -> {
            Dept dept = JBoltDeptCache.me.get(user.getDeptId());
            if (dept == null) {
                throw new RuntimeException("指定部门数据不存在");
            }
            if (notOk(dept.getDeptPath())) {
                throw new RuntimeException("指定部门的路径数据不完整，请执行部门路径补全操作");
            }
            user.setDeptPath(dept.getDeptPath());
            users.add(user);
            return true;
        }, selectSql().select("id", "username", "dept_id").isNotNull("dept_id").toSql());

        if (isOk(users)) {
            batchUpdate(users);
        }
    }

    public Ret saveTableSubmit(JBoltTableMulti jBoltTable, User loginUser, Date now) {
        // 组织表格
        JBoltTable thirdpartyTable = jBoltTable.getJBoltTable("thirdparty");
        // 用户信息
        User userForm = thirdpartyTable.getFormModel(User.class, "user");
        // 用户组织（人员）信息
        UserOrg userOrg = thirdpartyTable.getFormModel(UserOrg.class, "userorg");
        // 扩展信息
        UserExtend extend = thirdpartyTable.getFormModel(UserExtend.class, "extend");

        // 第三方账号
        JBoltTable orgTable = jBoltTable.getJBoltTable("org");

        // 新增
        if (null == userForm.getId()) {
            save(userForm, extend);

            // 用户组织（人员）信息维护
            if (ObjUtil.isNotNull(userOrg) && ObjUtil.isNotNull(userOrg.getIPersonId())) {
                userOrg.setUserId(userForm.getId());
                userOrg.setOrgId(OrgAccessKit.getOrgId());
                ValidationUtils.isTrue(userOrg.save(), ErrorMsg.SAVE_FAILED);
            }

            // 新增组织
            if (ObjUtil.isNotNull(orgTable)) {
                saveOrgs(orgTable.getSaveModelList(UserOrg.class), userForm.getId(), loginUser, now);
                // 校验组织不能重复
                ValidationUtils.isTrue(userOrgService.notExistsDuplicate(userForm.getId()), "组织重复定义错误");
            }

            // 新增第三方系统账号
            saveThirdpartys(thirdpartyTable.getSaveModelList(UserThirdparty.class), userForm.getId());
            // 校验第三方账号不重复
            ValidationUtils.isTrue(userThirdpartyService.notExitsDuplicate(userForm.getId()), "第三方系统账号重复定义错误");
        }
        // 修改
        else {
            // 用户组织（人员）信息维护
            if (ObjUtil.isNotNull(userOrg) && ObjUtil.isNotNull(userOrg.getIPersonId())) {
                UserOrg dbUserOrg = userOrgService.getUserOrg(userForm.getId(), OrgAccessKit.getOrgId());
                if (ObjUtil.isNull(dbUserOrg)) {
                    // 校验是否重复
                    ValidationUtils.isTrue(userOrgService.notExistsDuplicatePerson(OrgAccessKit.getOrgId(), userOrg.getIPersonId()), "选择的人员已被占用");
                    
                    ValidationUtils.isTrue(userOrg.save(), ErrorMsg.SAVE_FAILED);
                } else {
                    if (ObjUtil.notEqual(dbUserOrg.getIPersonId(), userOrg.getIPersonId())) {
                        ValidationUtils.isTrue(userOrgService.notExistsDuplicatePerson(OrgAccessKit.getOrgId(), userOrg.getIPersonId()), "选择的人员已被占用");
                    }
                    
                    dbUserOrg.setIPersonId(userOrg.getIPersonId());
                    ValidationUtils.isTrue(dbUserOrg.update(), ErrorMsg.UPDATE_FAILED);
                }
            }
            
            update(userForm, extend);

            // 组织维护
            if (ObjUtil.isNotNull(orgTable)) {
                // 新增组织
                saveOrgs(orgTable.getSaveModelList(UserOrg.class), userForm.getId(), loginUser, now);
                // 修改组织
                updateOrgs(orgTable.getUpdateModelList(UserOrg.class), loginUser, now);
                // 删除组织
                deleteOrgs(orgTable.getDelete());
                // 校验组织不能重复
                ValidationUtils.isTrue(userOrgService.notExistsDuplicate(userForm.getId()), "组织重复定义错误");
            }
            
            // 新增第三方系统账号
            saveThirdpartys(thirdpartyTable.getSaveModelList(UserThirdparty.class), userForm.getId());
            // 修改第三方系统账号
            updateThirdpartys(thirdpartyTable.getUpdateModelList(UserThirdparty.class));
            // 删除第三方系统账号
            deleteThirdpartys(thirdpartyTable.getDelete());

            // 校验第三方账号不重复
            ValidationUtils.isTrue(userThirdpartyService.notExitsDuplicate(userForm.getId()), "第三方系统账号重复定义错误");
        }

        return successWithData(userForm.keep("id"));
    }

    private void saveOrgs(List<UserOrg> saveOrgs, Long userId, User user, Date now) {
        if (CollUtil.isNotEmpty(saveOrgs)) {
            for (UserOrg org : saveOrgs) {
                if (ObjUtil.isNotNull(org.getIPersonId())) {
                    ValidationUtils.isTrue(userOrgService.notExistsDuplicatePerson(org.getOrgId(), org.getIPersonId()), "选择的人员已被占用");
                }
                
                org.setId(JBoltSnowflakeKit.me.nextId())
                        .setUserId(userId)
                        .setCreateUserId(user.getId())
                        .setCreateUserName(user.getName())
                        .setCreateTime(now)
                        .setLastUpdateId(user.getId())
                        .setLastUpdateName(user.getName());
            }
            userOrgService.batchSave(saveOrgs);
        }
    }

    private void updateOrgs(List<UserOrg> updateOrgs, User user, Date now) {
        if (CollUtil.isNotEmpty(updateOrgs)) {
            for (UserOrg org : updateOrgs) {
                UserOrg dbUserOrg = userOrgService.findById(org.getId());
                ValidationUtils.notNull(dbUserOrg, JBoltMsg.DATA_NOT_EXIST);
                ValidationUtils.isTrue(!dbUserOrg.getIsDeleted(), JBoltMsg.DATA_NOT_EXIST);
                
                // 校验人员是否已被占用
                if (ObjUtil.isNotNull(org.getIPersonId()) && ObjUtil.notEqual(dbUserOrg.getIPersonId(), org.getIPersonId())) {
                    ValidationUtils.isTrue(userOrgService.notExistsDuplicatePerson(org.getOrgId(), org.getIPersonId()), "选择的人员已被占用");
                }
                
                org.keep("id", "org_id", "position", "is_principal", "parent_psn_id", "version_num", "ipersonid")
                        .setLastUpdateId(user.getId())
                        .setLastUpdateName(user.getName())
                        .setLastUpdateTime(now);
            }
            userOrgService.batchUpdate(updateOrgs);
        }
    }

    private void deleteOrgs(Object[] delete) {
        if (ArrayUtil.isNotEmpty(delete)) {
            List<Record> delRecords = new ArrayList<>();
            for (Object id : delete) {
                delRecords.add(new Record()
                        .set("id", id)
                        .set("is_deleted", "1"));
            }
            userOrgService.batchUpdateRecords(delRecords);
        }
    }

    private void saveThirdpartys(List<UserThirdparty> saveThirdpartys, long userId) {
        if (CollUtil.isNotEmpty(saveThirdpartys)) {
            for (UserThirdparty pt : saveThirdpartys) {
                pt.setId(JBoltSnowflakeKit.me.nextId())
                        .setUserId(userId);
            }
            userThirdpartyService.batchSave(saveThirdpartys);
        }
    }

    private void updateThirdpartys(List<UserThirdparty> thirdpartyList) {
        if (CollUtil.isNotEmpty(thirdpartyList)) {
            userThirdpartyService.batchUpdate(thirdpartyList);
        }
    }

    private void deleteThirdpartys(Object[] delete) {
        if (ArrayUtil.isNotEmpty(delete)) {
            userThirdpartyService.deleteByMultiIds(delete);
        }
    }

    public Ret updateRoles(String roleId, String userIds) {
        List<User> users = new ArrayList<>();

        for (String userIdStr : StrSplitter.split(userIds, COMMA, true, true)) {
            User user = findById(userIdStr);
            ValidationUtils.notNull(user, "用户不存在");

            List<String> roleIds = StrSplitter.split(user.getRoles(), COMMA, true, true);

            // 包含
            if (CollUtil.contains(roleIds, roleId)) {
                roleIds.remove(roleId);
            } else {
                roleIds.add(roleId);
            }

            user.setRoles(CollUtil.join(roleIds, COMMA));
            users.add(user);
        }

        if (CollUtil.isNotEmpty(users)) {
            batchUpdate(users);
        }
        return SUCCESS;
    }


    /**
     * 通过用户查找
     */
    public String getCdepcode(long id) {
        return dbTemplate( "user.getcdepcode", Kv.by("id", id)).queryColumn();
    }

	public User getUserByUserName(String username) {
		return findFirst(selectSql().eq("username", username));
	}

    public List<String> getEmails(List<Long> nextUserIds) {
        return query(selectSql().select(User.EMAIL).in(User.ID, nextUserIds));
    }

    /**
     * 从系统导入字段配置，获得导入的数据
     */
    public Ret importExcel(File file, Long orgId, Long userId) {
        List<Record> users = CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, table());
        if (notOk(users)) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
        }

        // 用户类型
        UserType userType = userTypeService.findFirst(Okv.by(UserType.USERTYPE_CODE, UserTypeEnum.MOM.getValue()));
        ValidationUtils.notNull(userType, "用户类型不存在");
        
        // 第三方账号
        List<Record> thirdpartys = new ArrayList<>();
        // 用户组织（人员）
        List<Record> userOrgs = new ArrayList<>();
        
        Date now = new Date();

        for (Record user : users) {

            ValidationUtils.notBlank(user.getStr("name"), "姓名不能为空");
            ValidationUtils.notBlank(user.getStr("username"), "用户名不能为空");
            ValidationUtils.notBlank(user.getStr("password"), "密码不能为空");

            // ------------------------------------
            // 角色
            // ------------------------------------
            String roles = user.getStr("roles");
            ValidationUtils.notBlank(roles, "角色不能为空");
            
            List<Long> roleIdList = new ArrayList<>();

            List<String> roleStrList = StrSplitter.split(roles, COMMA, true, true);

            for (String roleStr : roleStrList) {
                Role role = roleService.findFirstByName(roleStr);
                ValidationUtils.notNull(role, String.format("角色“%s”不存在", roleStr));

                roleIdList.add(role.getId());
            }

            user.set("roles", CollUtil.join(roleIdList, COMMA));
            user.set("id", JBoltSnowflakeKit.me.nextId());
            user.set("user_type_id", userType.getId());

            String password = user.getStr("password").trim();
            String pwdSalt = HashKit.generateSaltForSha256();
            user.set("pwd_salt", pwdSalt);
            user.set("password", calPasswordWithSalt(password, pwdSalt));
            user.set("create_time", now);
            user.set("create_user_id", userId);
            user.set("update_time", now);
            user.set("update_user_id", userId);
            
            // ---------------------------------------
            // 用户组织
            // ---------------------------------------

            // 负责人标识
            String isPrincipal = user.getStr("is_principal");
            ValidationUtils.notBlank(isPrincipal, "缺少负责人标识");
            
            BoolCharEnum t = BoolCharEnum.textToEnum(isPrincipal);
            ValidationUtils.notNull(t, "负责人标识不合法");
            
            user.set("is_principal", t.getValue());

            String cParentPersonname = user.getStr("parent_psn_id");
            if (StrUtil.isNotBlank(cParentPersonname)) {
                // 直接上级姓名
                Person person = personService.findFirstByCpersonName(cParentPersonname);
                ValidationUtils.notNull(person, String.format("直接上级姓名“%s”不存在", cParentPersonname));

                user.set("parent_psn_id", person.getIAutoId());
            }

            // ------------------------------------
            // 人员
            // ------------------------------------
            String cpersonname = user.getStr("ipersonid");
            ValidationUtils.notBlank(cpersonname, "人员姓名不能为空");

            List<Person> personList = personService.findByCpersonName(cpersonname);
            ValidationUtils.notEmpty(personList, String.format("人员“%s”不存在", cpersonname));
            ValidationUtils.isTrue(personList.size() == 1, String.format("人员“%s”存在多个", cpersonname));

            userOrgs.add(new Record()
                    .set("id", JBoltSnowflakeKit.me.nextId())
                    .set("org_id", orgId)
                    .set("user_id", user.getLong("id"))
                    .set("ipersonid", personList.get(0).getIAutoId())
                    .set("is_principal", user.getStr("is_principal"))
                    .set("parent_psn_id", user.getLong("parent_psn_id"))
                    .set("create_time", now)
                    .set("create_user_id", userId)
                    .set("is_deleted", ZERO_STR));
            
            // ---------------------------------------
            // 第三方系统
            // ---------------------------------------
            
            String thirdpartySystem = user.getStr("thirdparty_system");
            if (StrUtil.isNotBlank(thirdpartySystem)) {
                ThirdpartySystemEnum thirdpartySystemEnum = ThirdpartySystemEnum.textToEnum(thirdpartySystem);
                ValidationUtils.notNull(thirdpartySystemEnum, String.format("第三方系统“%s”不存在", thirdpartySystem));

                user.set("thirdparty_system", thirdpartySystemEnum.getValue());

                thirdpartys.add(new Record()
                        .set("id", JBoltSnowflakeKit.me.nextId())
                        .set("thirdparty_system", user.get("thirdparty_system"))
                        .set("user_id", user.get("id"))
                        .set("thirdparty_code", user.get("thirdparty_code"))
                );
            }

            user.remove("is_principal", "ipersonid", "parent_psn_id", "thirdparty_system", "thirdparty_code");
        }

        // 执行批量操作
        tx(() -> {
            
            // 用户
            batchSaveRecords(users);
            // 用户组织
            userOrgService.batchSaveRecords(userOrgs);
            // 第三方系统
            userThirdpartyService.batchSaveRecords(thirdpartys);
            
            return true;
        });

        return SUCCESS;
    }


    public String getU8Password(User user){
        Long id = user.getId();
        Record firstRecord = findFirstRecord("select U8Password from jb_user where id=?", id);
        if(firstRecord==null){
            return null;
        }else{
            return firstRecord.getStr("u8password");
        }

    }
}
