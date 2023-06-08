package cn.rjtech.admin.datapermission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.common.enums.BusObjectTypeEnum;
import cn.jbolt.core.common.enums.DataOperationEnum;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltModelKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Busobject;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.BusobjectService;
import cn.jbolt.core.service.base.JBoltBaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.main.DataPermission;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSON;
import com.beust.jcommander.ParameterException;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据权限
 *
 * @ClassName: DataPermissionService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-23 16:02
 */
public class DataPermissionService extends JBoltBaseService<DataPermission> {

    private final DataPermission dao = new DataPermission().dao();

    @Inject
    private BusobjectService busobjectService;
    @Inject
    private UserService userService;


    @Override
    protected DataPermission dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.DATA_PERMISSION.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber      第几页
     * @param pageSize        每页几条数据
     * @param keywords        关键词
     * @param sortColumn      排序列名
     * @param sortType        排序方式 asc desc
     * @param busobjectId     业务对象ID
     * @param objectType      授权类型：1. 角色 2. 用户
     * @param isViewEnabled   查看权限：0. 禁用 1. 启用
     * @param isEditEnabled   编辑权限：0. 禁用 1. 启用
     * @param isDeleteEnabled 删除权限：0. 禁用 1. 启用
     * @param isDeleted       是否删除
     */
    public Page<DataPermission> getAdminDatas(int pageNumber, int pageSize, String keywords, String sortColumn, String sortType, Long busobjectId, Integer objectType, Boolean isViewEnabled, Boolean isEditEnabled, Boolean isDeleteEnabled, Boolean isDeleted) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("busobject_id", busobjectId);
        sql.eq("object_type", objectType);
        sql.eqBooleanToChar("is_view_enabled", isViewEnabled);
        sql.eqBooleanToChar("is_edit_enabled", isEditEnabled);
        sql.eqBooleanToChar("is_delete_enabled", isDeleteEnabled);
        sql.eqBooleanToChar("is_deleted", isDeleted);
        //关键词模糊查询
        sql.like("busobject_value_name", keywords);
        //排序
        sql.orderBy(sortColumn, sortType);
        return paginate(sql);
    }

    /**
     * 保存
     */
    public Ret save(DataPermission dataPermission) {
        if (dataPermission == null || isOk(dataPermission.getId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(dataPermission.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = dataPermission.save();
        if (success) {
            //添加日志
            addSaveSystemLog(dataPermission.getId(), JBoltUserKit.getUserId(), "");
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(DataPermission dataPermission) {
        if (dataPermission == null || notOk(dataPermission.getId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        DataPermission dbDataPermission = findById(dataPermission.getId());
        if (dbDataPermission == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(dataPermission.getName(), dataPermission.getId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = dataPermission.update();
        if (success) {
            //添加日志
            addUpdateSystemLog(dataPermission.getId(), JBoltUserKit.getUserId(), "数据权限");
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param dataPermission 要删除的model
     * @param kv             携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(DataPermission dataPermission, Kv kv) {
        addDeleteSystemLog(dataPermission.getId(), JBoltUserKit.getUserId(), "数据权限");
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param dataPermission model
     * @param kv             携带额外参数一般用不上
     */
    @Override
    public String checkInUse(DataPermission dataPermission, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(DataPermission dataPermission, String column, Kv kv) {
        addUpdateSystemLog(dataPermission.getId(), JBoltUserKit.getUserId(), "数据权限", "的字段[" + column + "]值:" + dataPermission.get(column));
        /*
         switch(column){
         case "is_view_enabled":
         break;
         case "is_edit_enabled":
         break;
         case "is_delete_enabled":
         break;
         }
         */
        return null;
    }

    /**
     * 恢复假删数据后执行的回调
     *
     * @param dataPermission 要恢复的model
     */
    @Override
    protected String afterRecover(DataPermission dataPermission) {
        addRecoverSystemLog(dataPermission.getId(), JBoltUserKit.getUserId(), "数据权限");
        return null;
    }

    public Page<Record> paginatePermissions(Integer pageNumber, Integer pageSize, Integer objecttype, Long objectid, Long busobjectId, String q) {
        Busobject busobject = busobjectService.findById(busobjectId);
        ValidationUtils.notNull(busobject, "业务对象不存在");
        ValidationUtils.isTrue(!busobject.getIsDeleted(), "业务对象已被删除");

        Okv para = Okv.by("objecttype", objecttype)
                .set("objectid", objectid)
                .set("busobjectid", busobjectId)
                .set("busobjectname", busobject.getBusobjectName())
                .set("datasource", busobject.getDataSource())
                .set("q", q);

        return paginateByDbSqlTemplate("datapermission.paginatePermissions", para, pageNumber, pageSize, true);
    }

    public Ret savePermissions(long busobjectId, int objecttype, long objectid, String permissionsStr, User loginUser, Date now) {
        List<DataPermission> permissions = JBoltModelKit.getBeanList(DataPermission.class, JSON.parseArray(permissionsStr));

        tx(() -> {

            List<DataPermission> dbDataPermissions = getObjectPermissions(busobjectId, objecttype, objectid);

            // 新增
            if (CollUtil.isEmpty(dbDataPermissions)) {

                for (DataPermission permission : permissions) {
                    fill(permission, busobjectId, objecttype, objectid, loginUser, now);
                }
                batchSave(permissions);
            }
            // 修改
            else {
                // 新增数据权限
                List<DataPermission> savePermissions = new ArrayList<>();
                // 修改数据权限
                List<DataPermission> updatePermissions = new ArrayList<>();

                // List 转 Map
                Map<Long, DataPermission> dbPermissionMap = dbDataPermissions.stream().collect(Collectors.toMap(DataPermission::getBusobjectValueId, Function.identity()));

                for (DataPermission permission : permissions) {

                    // 数据库已有的权限配置
                    DataPermission dbDataPermission = dbPermissionMap.get(permission.getBusobjectValueId());

                    // 不存在，则新增
                    if (null == dbDataPermission) {
                        // 数据权限
                        fill(permission, busobjectId, objecttype, objectid, loginUser, now);
                        // 追加新增项
                        savePermissions.add(permission);
                    } else {
                        // 修改权限配置
                        dbDataPermission._setAttrs(permission.keep(DataPermission.IS_VIEW_ENABLED, DataPermission.IS_EDIT_ENABLED, DataPermission.IS_DELETE_ENABLED));
                        setDefaultValues(dbDataPermission);
                        // 更新
                        updatePermissions.add(dbDataPermission);
                    }
                }

                // 批量新增
                if (CollUtil.isNotEmpty(savePermissions)) {
                    batchSave(savePermissions);
                }
                // 批量更新
                if (CollUtil.isNotEmpty(updatePermissions)) {
                    batchUpdate(updatePermissions);
                }
            }
            return true;
        });

        return SUCCESS;
    }

    private void fill(DataPermission permission, long busobjectId, int objecttype, long objectid, User loginUser, Date now) {
        permission.setBusobjectId(busobjectId)
                .setObjectType(objecttype)
                .setObjectId(objectid)
                .setCreateUserId(loginUser.getId())
                .setCreateUserName(loginUser.getName())
                .setCreateTime(now)
                .setLastUpdateId(loginUser.getId())
                .setLastUpdateName(loginUser.getName())
                .setLastUpdateTime(now);

        setDefaultValues(permission);
    }

    private void setDefaultValues(DataPermission permission) {
        if (null == permission.getIsViewEnabled()) {
            permission.setIsViewEnabled(false);
        }
        if (null == permission.getIsEditEnabled()) {
            permission.setIsEditEnabled(false);
        }
        if (null == permission.getIsDeleteEnabled()) {
            permission.setIsDeleteEnabled(false);
        }
    }

    private List<DataPermission> getObjectPermissions(long busobjectId, int objecttype, long objectid) {
        return find(selectSql().eq(DataPermission.BUSOBJECT_ID, busobjectId)
                .eq(DataPermission.OBJECT_TYPE, objecttype)
                .eq(DataPermission.OBJECT_ID, objectid)
                .eq(DataPermission.IS_DELETED, ZERO_STR));
    }

    /**
     * 根据操作类型获取用户/角色权限集合
     */
    public List<String> getAccessCdepcodes(Long userId, String roles, DataOperationEnum operation, BusObjectTypeEnum typeEnum) {
        switch (operation) {
            case VIEW:
                return getAccessCdepcodes(userId, roles, DataPermission.IS_VIEW_ENABLED, typeEnum.getValue());
            case EDIT:
                return getAccessCdepcodes(userId, roles, DataPermission.IS_EDIT_ENABLED, typeEnum.getValue());
            case DELETE:
                return getAccessCdepcodes(userId, roles, DataPermission.IS_DELETE_ENABLED, typeEnum.getValue());
            default:
                throw new ParameterException("未知操作类型");
        }
    }

    public List<String> getAccessCdepcodes(long userId, String roles, String fieldName, String busobjectCode) {
        Okv para = Okv.by("userId", userId)
                .set("roles", roles)
                .set("fieldName", fieldName)
                .set("busobjectCode", busobjectCode);

        return dbTemplate("datapermission.getAccessCdepcodes", para).query();
    }

    /**
     * 根据当前用户ID + 角色IDS 获取部门IDS
     *
     * @param operation 数据权限操作枚举
     * @return 获取部门IDS
     */
    public List<Long> selectDeptIdList(DataOperationEnum operation) {
        switch (operation) {
            case VIEW:
                return getAccessDeptIdList(DataPermission.IS_VIEW_ENABLED);
            case EDIT:
                return getAccessDeptIdList(DataPermission.IS_EDIT_ENABLED);
            case DELETE:
                return getAccessDeptIdList(DataPermission.IS_DELETE_ENABLED);
            default:
                throw new ParameterException("未知操作类型");
        }
    }

    /**
     * 根据当前用户ID + 角色IDS 获取部门IDS
     *
     * @param fieldName 数据权限操作字段
     * @return 获取部门IDS
     */
    public List<Long> getAccessDeptIdList(String fieldName) {
        String roles = JBoltUserKit.getUserRoleIds();
        if (ObjectUtil.isEmpty(roles)) {
            return null;
        }
        Long userId = JBoltUserKit.getUserId();
        Okv para = Okv.by("userId", userId)
                .set("roles", roles)
                .set("fieldName", fieldName);
        return dbTemplate("datapermission.getAccessDeptIdList", para).query();
    }


}