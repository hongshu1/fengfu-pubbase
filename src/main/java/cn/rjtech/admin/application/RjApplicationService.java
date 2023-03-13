package cn.rjtech.admin.application;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.jbolt._admin.permission.PermissionService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.JBoltBaseService;
import cn.jbolt.core.util.JBoltListMap;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.main.Application;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 应用系统
 *
 * @ClassName: RjApplicationService
 * @author: Kephon
 * @date: 2022-11-07 13:49
 */
public class RjApplicationService extends JBoltBaseService<Application> {

    private final Application dao = new Application().dao();

    @Inject
    private PermissionService permissionService;

    @Override
    protected Application dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.APPLICATION.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber  第几页
     * @param pageSize    每页几条数据
     * @param keywords    关键词
     * @param sortColumn  排序列名
     * @param sortType    排序方式 asc desc
     * @param isLeaf      叶节点(末级:1,其他:0)
     * @param isEffective 是否生效(0:失效,1:生效)
     * @param isDeleted   是否删除
     */
    public Page<Application> getAdminDatas(int pageNumber, int pageSize, String keywords, String sortColumn, String sortType, Boolean isLeaf, Boolean isEffective, Boolean isDeleted) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eqBooleanToChar("is_leaf", isLeaf);
        sql.eqBooleanToChar("is_effective", isEffective);
        sql.eqBooleanToChar("is_deleted", isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords, "short_name", "app_name", "description", "create_user_name", "last_update_name");
        //排序
        sql.orderBy(sortColumn, sortType);
        return paginate(sql);
    }

    private void validateRequired(Application application) {
        ValidationUtils.notBlank(application.getAppCode(), "缺少应用编码");
        ValidationUtils.notBlank(application.getShortName(), "缺少名称");
//        ValidationUtils.notNull(application.getSeq(), "缺少顺序号");
        ValidationUtils.notNull(application.getVersionNum(), "缺少版本号");
        ValidationUtils.notNull(application.getEffectiveDate(), "缺少生效日期");
        ValidationUtils.notNull(application.getDisableDate(), "缺少失效日期");
        ValidationUtils.notNull(application.getIsEffective(), "缺少生效状态");
    }

    /**
     * 检测同一个pid下的title是否存在重复数据
     */
    private boolean existsTitleWithSameParent(Long id, String title, Long parentId) {
        Sql sql = selectSql().selectId().eqQM("short_name", "parent_id").idNotEqQM().first();
        Long existId = queryLong(sql, title.trim(), parentId, id);
        return isOk(existId);
    }

    /**
     * 保存
     */
    public Ret save(Application application, User user, Date now) {
        if (application == null || isOk(application.getId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        // 必填校验
        validateRequired(application);

        application.setShortName(application.getShortName().trim());
        application.setAppName(application.getAppName().trim());
        application.setAppCode(application.getAppCode().trim());

        // 重复校验
        if (existsTitleWithSameParent(-1L, application.getShortName(), application.getParentId())) {
            return fail("同一父节点下 【" + application.getShortName() + "】已经存在，请修改");
        }
        if (exists(Application.APP_CODE, application.getAppCode())) {
            return fail("编码【" + application.getAppCode() + "】已经存在，请更换");
        }
        if (exists(Application.APP_NAME, application.getAppName())) {
            return fail("应用名称【" + application.getAppName() + "】已经存在，请更换");
        }

        if (notOk(application.getParentId())) {
            application.setParentId(0L);
        }

        application.setCreateUserId(user.getId());
        application.setCreateUserName(user.getName());
        application.setCreateTime(now);
        application.setLastUpdateId(user.getId());
        application.setLastUpdateName(user.getName());
        application.setLastUpdateTime(now);
        application.setSortRank(getNextSortRankByPid(Application.PARENT_ID, application.getParentId()));

        if (application.getParentId() == 0L) {
            application.setIsLeaf(true);
        }

        processNewLevel(application);

        boolean success = application.save();
        if (success) {
            //添加日志
            addSaveSystemLog(application.getId(), JBoltUserKit.getUserId(), application.getShortName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(Application application, User user, Date now) {
        if (application == null || notOk(application.getId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        validateRequired(application);

        if (isOk(application.getParentId()) && application.getParentId().equals(application.getId())) {
            return fail("上级应用不能是自身，请换一个");
        }

        application.setShortName(application.getShortName().trim());
        application.setAppName(application.getAppName().trim());
        application.setAppCode(application.getAppCode().trim());

        if (existsTitleWithSameParent(application.getId(), application.getShortName(), application.getParentId())) {
            return fail("同一父节点下应用【" + application.getShortName() + "】已经存在，请输入其它名称");
        }
        if (exists(Application.APP_CODE, application.getAppCode(), application.getId())) {
            return fail("编码【" + application.getAppCode() + "】已经存在，请更换");
        }
        if (exists(Application.APP_NAME, application.getAppName(), application.getId())) {
            return fail("名称【" + application.getAppName() + "】已经存在，请更换");
        }

        //更新时需要判断数据存在
        Application dbApplication = findById(application.getId());
        if (dbApplication == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        // 其他地方调用传过来的一个sort请求 指明是排序后调用update
        Boolean sort = application.getBoolean("sort");
        if (sort == null) {
            processNewLevel(application);
        }
        if (ObjUtil.notEqual(dbApplication.getParentId(), application.getParentId())) {
            application.setSortRank(getNextSortRankByPid(Application.PARENT_ID, application.getParentId()));
        }

        application.setLastUpdateId(user.getId());
        application.setLastUpdateName(user.getName());
        application.setLastUpdateTime(now);

        boolean success = application.update();
        if (success) {
            if (ObjUtil.notEqual(dbApplication.getParentId(), application.getParentId())) {
                updateSortRankAfterChangeParentNode(Okv.by(Application.PARENT_ID, dbApplication.getParentId()), dbApplication.getSortRank());
                // 说明数据的父节点变更了 level相应的跟着变化了
                processSonItemLevel(application.getId(), application.getNodeLevel());
            }

            // 添加日志
            if (null == sort) {
                addUpdateSystemLog(application.getId(), JBoltUserKit.getUserId(), application.getShortName());
            } else {
                addUpdateSystemLog(application.getId(), JBoltUserKit.getUserId(), application.getShortName(), "的顺序");
            }
        }
        return ret(success);
    }

    private void processSonItemLevel(Long id, int level) {
        List<Application> applications = getSons(Application.PARENT_ID, id, Application.SORT_RANK);
        if (isOk(applications)) {
            applications.forEach(p -> {
                p.setNodeLevel(level + 1);
                processSonItemLevel(p.getId(), p.getNodeLevel());
            });
            batchUpdate(applications);
        }
    }

    private void processNewLevel(Application permission) {
        if (notOk(permission.getParentId())) {
            permission.setNodeLevel(1);
        } else {
            int level = getParentLevel(permission.getParentId());
            permission.setNodeLevel(level + 1);
        }
    }

    private int getParentLevel(Long pid) {
        Application permission = findById(pid);
        return permission.getNodeLevel();
    }


    /**
     * 删除数据后执行的回调
     *
     * @param application 要删除的model
     * @param kv          携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Application application, Kv kv) {
        addDeleteSystemLog(application.getId(), JBoltUserKit.getUserId(), application.getShortName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param application model
     * @param kv          携带额外参数一般用不上
     */
    @Override
    public String checkInUse(Application application, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(Application application, String column, Kv kv) {
        addUpdateSystemLog(application.getId(), JBoltUserKit.getUserId(), application.getShortName(), "的字段[" + column + "]值:" + application.get(column));
        /*
         switch(column){
         case "is_leaf":
         break;
         case "is_effective":
         break;
         }
         */
        return null;
    }

    /**
     * 恢复假删数据后执行的回调
     *
     * @param application 要恢复的model
     */
    @Override
    protected String afterRecover(Application application) {
        addRecoverSystemLog(application.getId(), JBoltUserKit.getUserId(), application.getShortName());
        return null;
    }

    /**
     * 得到所有permission 通过级别处理 options下拉数据专用
     */
    public List<Application> getAllPermissionsOptionsWithLevel() {
        List<Application> permissions = find(selectSql().select("id,short_name,app_name,parent_id,node_level").eq(Application.IS_DELETED, "0").orderBySortRank("node_level"));
        // 处理分级
        List<Application> parents = new ArrayList<>();
        processParent(permissions, parents);
        if (parents.size() > 0 && permissions.size() > 0) {
            processPermissionItems(permissions, parents);
        }
        return parents;
    }

    private void processParent(List<Application> applications, List<Application> parents) {
        Application application;
        for (int i = 0; i < applications.size(); i++) {
            application = applications.get(i);
            if (application.getNodeLevel() == 1 && notOk(application.getParentId())) {
                parents.add(application);
                applications.remove(i);
                i--;
            }
        }
    }

    private void processPermissionItems(List<Application> permissions, List<Application> parents) {
        JBoltListMap<String, Application> map = new JBoltListMap<>();
        for (Application p : permissions) {
            map.addItem("p_" + p.getParentId(), p);
        }
        for (Application p : parents) {
            processSubItems(map, p);
        }

    }

    private void processSubItems(JBoltListMap<String, Application> map, Application application) {
        List<Application> items = map.get("p_" + application.getId());
        if (CollUtil.isNotEmpty(items)) {
            for (Application item : items) {
                processSubItems(map, item);
            }
        }
        application.putItems(items);
    }

    public List<Application> getDatasWithLevel() {
        List<Application> applications = find(selectSql().eq(Application.IS_DELETED, "0").orderBySortRank());

        return convertToModelTree(applications, ID, Application.PARENT_ID, (p) -> notOk(p.getParentId()));
    }

    @Override
    public String checkCanDelete(Application application, Kv kv) {
        if (permissionService.existsApplicationId(application.getId())) {
            return "该应用在权限资源中被使用，无法删除";
        }
        return null;
    }

    /**
     * 删除
     */
    public Ret delApplicationById(Long id) {
        Ret ret = deleteById(id, true);
        if (ret.isOk()) {
            Application application = ret.getAs("data");
            // 删除后需要把此数据之后的数据更新顺序
            updateSortRankAfterDelete(Okv.by(Application.PARENT_ID, application.getParentId()), application.getSortRank());
            // 删除子节点
            deleteByPid(id);
            // 根据被删除的permission去删掉给role上的数据
//            Ret delret = jboltRolePermissionService.deleteByPermission(application.getId());
//            if (delret.isFail()) {
//                return delret;
//            }

//            if (processRoleCache) {
//                JBoltPermissionCache.me.removeMenusAndPermissionsByRoleGroups();
//            }

            // 添加日志
            addDeleteSystemLog(application.getId(), JBoltUserKit.getUserId(), application.getShortName());
        }
        return ret;
    }

    private void deleteByPid(Long pid) {
        List<Application> permissions = getSons(Application.PARENT_ID, pid, Application.SORT_RANK);
        for (Application permission : permissions) {
            delApplicationById(permission.getId());
        }
    }

    /**
     * 上移
     */
    public Ret up(Long id, User user, Date now) {
        Application application = findById(id);
        if (application == null) {
            return fail("数据不存在或已被删除");
        }
        Integer rank = application.getSortRank();
        if (rank == null || rank <= 0) {
            return fail("顺序需要初始化");
        }
        if (rank == 1) {
            return fail("已经是第一个");
        }
        Application upApplication = findFirst(Okv.by("sort_rank", rank - 1).set(Application.PARENT_ID, application.getParentId()));
        if (upApplication == null) {
            return fail("顺序需要初始化");
        }
        upApplication.setSortRank(rank);
        application.setSortRank(rank - 1);
        upApplication.put("sort", true);
        application.put("sort", true);
        update(upApplication, user, now);
        update(application, user, now);
        return SUCCESS;
    }

    /**
     * 下移
     */
    public Ret down(Long id, User user, Date now) {
        Application permission = findById(id);
        if (permission == null) {
            return fail("数据不存在或已被删除");
        }
        Integer rank = permission.getSortRank();
        if (rank == null || rank <= 0) {
            return fail("顺序需要初始化");
        }
        int max = getCount(Okv.by(Application.PARENT_ID, permission.getParentId()));
        if (rank == max) {
            return fail("已经是最后一个");
        }
        Application upPermissions = findFirst(Okv.by(SORT_RANK, rank + 1).set(Application.PARENT_ID, permission.getParentId()));
        if (upPermissions == null) {
            return fail("顺序需要初始化");
        }
        upPermissions.setSortRank(rank);
        permission.setSortRank(rank + 1);
        upPermissions.put("sort", true);
        permission.put("sort", true);
        update(upPermissions, user, now);
        update(permission, user, now);
        return SUCCESS;
    }

    public Application findFirstByAppCode(String appCode) {
        return findFirst(selectSql().eq("app_code", appCode).first());
    }

    public List<Application> getAllApplications() {
        return find(selectSql().eq(Application.IS_DELETED, ZERO_STR));
    }

}