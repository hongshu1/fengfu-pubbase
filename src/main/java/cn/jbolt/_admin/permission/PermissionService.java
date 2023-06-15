package cn.jbolt._admin.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.admin.appdevcenter.ApplicationService;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Permission;
import cn.jbolt.core.model.PermissionBtn;
import cn.jbolt.core.model.RjApplication;
import cn.jbolt.core.service.ButtonPermissionService;
import cn.jbolt.core.service.JBoltPermissionService;
import cn.jbolt.core.service.MenuPermissionService;
import cn.jbolt.core.service.RjApplicationService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.admin.permissionbtn.PermissionBtnService;
import cn.rjtech.cache.RjApplicationCache;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 系统权限资源Service
 *
 * @author 小木
 */
public class PermissionService extends JBoltPermissionService {

    @Inject
    private ApplicationService applicationService;
    @Inject
    private RjApplicationService rjApplicationService;
    @Inject
    private MenuPermissionService menuPermissionService;
    @Inject
    private ButtonPermissionService buttonPermissionService;
    @Inject
    private PermissionBtnService permissionBtnService;

    /**
     * 得到所有permission 通过级别处理
     */
    public List<Record> getAllPermissionRecordsWithLevel(Long applicationId, Long appId) {
        Sql sql = selectSql()
                .eq(Permission.IS_DELETED, ZERO_STR)
                .orderBySortRank();

        if (null != applicationId) {
            sql.eq("application_id", applicationId);
        }
        if (null != appId) {
            sql.eq("app_id", appId);
        }

        List<Record> permissions = findRecord(sql, true);
        return convertToRecordTree(permissions, ID, PID, (p) -> notOk(p.getLong(PID)));
    }

    /**
     * 得到所有permission 通过级别处理 options下拉数据专用
     */
    public List<Permission> getAllPermissionsOptionsWithLevel(long applicationId, long appId) {
        if (applicationId == 0 || appId == 0) {
            return null;
        }

        List<Permission> permissions = find(selectSql()
                .select("id,title,pid,permission_level")
                .eq("application_id", applicationId)
                .eq("app_id", appId)
                .eq("is_deleted", ZERO_STR)
                .orderBySortRank());

        // 处理分级
        List<Permission> parents = new ArrayList<>();
        processParentPermission(permissions, parents);
        if (parents.size() > 0 && permissions.size() > 0) {
            processPermissionItems(permissions, parents);
        }
        return parents;
    }

    /**
     * 得到 符合条件的数据 通过级别处理
     */
    public List<Record> getAdminPermissionRecordsWithLevel(Long applicationId, Long appId, Long topnavId) {
        if (isOk(topnavId)) {
            Sql sql = selectSql()
                    .select("this.*, rja.app_name, a.name")
                    .from(table(), "this")
                    .leftJoin(jboltTopnavMenuService.table(), "tm", "tm.permission_id = this.id")
                    .leftJoin(rjApplicationService.table(), "rja", "rja.id = this.application_id")
                    .leftJoin(applicationService.table(), "a", "a.id = this.app_id")
                    .eq("tm.topnav_id", topnavId)
                    .eq("this.is_deleted", ZERO_STR)
                    .asc("this.application_id,this.app_id,this.sort_rank");

            if (null != applicationId) {
                sql.eq("this.application_id", applicationId);
            }
            if (null != appId) {
                sql.eq("this.app_id", appId);
            }

            List<Record> temps = findRecord(sql, true);

            List<Record> permissions = new ArrayList<>();

            if (isOk(temps)) {

                List<Record> all = getAllPermissionRecordsWithLevel(applicationId, appId);

                // 关联顶部导航，拿到的是第一级
                for (Record p : temps) {
                    all.forEach(ap -> {
                        if (ap.getStr("permission_key").equals(p.getStr("permission_key"))) {
                            permissions.add(ap);
                        }
                    });
                }
            }
            return convertToRecordTree(permissions, ID, PID, (p) -> notOk(p.getLong(PID)));
        }

        Sql sql = selectSql()
                .from(table(), "this")
                .select("this.*, rja.app_name, a.name")
                .leftJoin(rjApplicationService.table(), "rja", "rja.id = this.application_id")
                .leftJoin(applicationService.table(), "a", "a.id = this.app_id")
                .eq("this.is_deleted", ZERO_STR)
                .orderBySortRank();

        if (null != applicationId) {
            sql.eq("this.application_id", applicationId);
        }
        if (null != appId) {
            sql.eq("this.app_id", appId);
        }

        List<Record> permissions = findRecord(sql, true);

        return convertToRecordTree(permissions, ID, PID, (p) -> notOk(p.getLong(PID)));
    }

    public Ret saveTableSubmit(JBoltTable jBoltTable) {
        Permission permission = jBoltTable.getFormModel(Permission.class, "permission");

        tx(() -> {

            if (null == permission.getId()) {
                Ret ret = save(permission);
                ValidationUtils.isTrue(ret.isOk(), ret.getStr("msg"));

                // 新增
                saveBtns(jBoltTable.getSaveModelList(PermissionBtn.class), permission.getId());

            } else {
                if(!isOk(permission.getPid())){
                    permission.setPid(0L);
                }
                Ret ret = update(permission);
                ValidationUtils.isTrue(ret.isOk(), ret.getStr("msg"));

                // 删除
                deleteBtns(jBoltTable.getDelete());
                // 新增
                saveBtns(jBoltTable.getSaveModelList(PermissionBtn.class), permission.getId());
                // 修改
                updateBtns(jBoltTable.getUpdateModelList(PermissionBtn.class));
            }

            return true;
        });

        return successWithData(permission.keep(ID));
    }

    private void saveBtns(List<PermissionBtn> saveList, long permissionId) {
        if (CollUtil.isNotEmpty(saveList)) {
            for (PermissionBtn row : saveList) {
                // 校验是否存在permission_key
                ValidationUtils.isTrue(!exists(Permission.PERMISSION_KEY, row.getPermissionKey()), "资源权限KEY【" + row.getPermissionKey() + "】已经存在，请更换");
                ValidationUtils.isTrue(!permissionBtnService.exists(PermissionBtn.PERMISSION_KEY, row.getPermissionKey()), "按钮资源权限KEY【" + row.getPermissionKey() + "】已经存在，请更换");
                
                row.setPermissionId(permissionId);
            }
            permissionBtnService.batchSave(saveList);
        }
    }

    private void updateBtns(List<PermissionBtn> permissionBtns) {
        if (CollUtil.isNotEmpty(permissionBtns)) {
            for (PermissionBtn row : permissionBtns) {
                PermissionBtn dbPerssionBtn = permissionBtnService.findById(row.getId());
                ValidationUtils.notNull(dbPerssionBtn, "按钮权限资源不存在");
                
                // 校验permission_key是否重复
                ValidationUtils.isTrue(!permissionBtnService.exists(PermissionBtn.PERMISSION_KEY, row.getPermissionKey(), row.getId()), "按钮资源权限KEY【" + row.getPermissionKey() + "】已经存在，请更换");
                
                if (ObjUtil.notEqual(row.getPermissionKey(), dbPerssionBtn.getPermissionKey())) {
                    ValidationUtils.isTrue(!exists(PermissionBtn.PERMISSION_KEY, row.getPermissionKey()), "资源权限KEY【" + row.getPermissionKey() + "】已经存在，请更换");
                }
            }
            permissionBtnService.batchUpdate(permissionBtns);
        }
    }

    private void deleteBtns(Object[] delete) {
        if (ArrayUtil.isNotEmpty(delete)) {
            // 校验按钮权限是否在使用
            ValidationUtils.isTrue(buttonPermissionService.notExistsBtnIds(ArrayUtil.join(delete, COMMA)), "菜单按钮仍在使用中");
            permissionBtnService.deleteByMultiIds(delete);
        }
    }

    /**
     * 获取菜单
     */
    public List<Record> getMenuRecordWithAlias(Long applicationId, String checkedIds) {
        Okv para = Okv.by("applicationId", applicationId)
                .set("checkedIds", checkedIds);

        return dbTemplate("permission.getMenuRecordWithAlias", para).find();
    }

    public List<JsTreeBean> getMenuJsTree(Integer openLevel, Long applicationId, String enableIcon, String checkedIds) {
        // 所有权限资源（菜单）
        List<Record> permissions = getMenuRecordWithAlias(applicationId, checkedIds);

        if (CollUtil.isNotEmpty(permissions)) {
            // 按钮
            List<Record> btns = permissionBtnService.findMenuBtnWithAlias(applicationId, checkedIds);
            if (CollUtil.isNotEmpty(btns)) {
                permissions.addAll(btns);
            }
        }

        return convertJsRecordTree(permissions, null, openLevel, "id", "title", null, enableIcon, null,"菜单列表", false);
    }

    @Override
    public String getTextColumn(Record m, String textColumn, String enableIcon) {
        // 菜单 显示所属应用系统
        if (null == m.get(PermissionBtn.VIEW_TYPE)) {
            RjApplication rjApplication = RjApplicationCache.ME.get(m.getLong("application_id"));
            return String.format("[%s]%s%s", rjApplication.getAppName(), m.getStr(textColumn), StrUtil.blankToDefault(enableIcon, StrUtil.EMPTY));
        }
        return String.format("[按钮]%s%s", m.getStr(textColumn), StrUtil.blankToDefault(enableIcon, StrUtil.EMPTY));
    }

    public Ret getUserOrRolePermissions(Integer roletype, Long id) {
        // 菜单权限集合
        List<Long> menuPermissions = menuPermissionService.getPermissionMenuIds(roletype, id);
        // 按钮权限集合
        List<Long> btnPermissions = buttonPermissionService.getButtonPermissions(roletype, id);
        if (CollUtil.isNotEmpty(btnPermissions)) {
            menuPermissions.addAll(btnPermissions);
        }
        return successWithData(Okv.by("ids", CollUtil.join(menuPermissions, COMMA)));
    }

    @Override
    public String checkCanDelete(Permission permission, Kv kv) {
        if (permissionBtnService.existsPermissionId(permission.getId())) {
            return "该权限资源仍在使用中，无法删除";
        }
        return null;
    }

    public List<Record> getUserPermissionList(String permissionKey, String pkey) {
        Long userId = JBoltUserKit.getUserId();
        String roleIdList = JBoltUserKit.getUserRoleIds();
        List<Long> personPermissionIdList = this.dbTemplate("permission.getPermissionIds", Kv.by("objecttype", 2).set("objectid", userId)).query();
        List<Long> rolePermissionIdList = this.dbTemplate("permission.getPermissionIds", Kv.by("objecttype", 1).set("objectid", roleIdList)).query();
        if (CollUtil.isNotEmpty(rolePermissionIdList)) {
            personPermissionIdList.addAll(rolePermissionIdList);
        }

        String ids = CollUtil.join(personPermissionIdList, COMMA);
        return this.isOk(ids) ? this.dbTemplate("permission.getPermissionList", Kv.by("permissionkey", permissionKey).set("pkey", pkey).set("ids", ids)).find() : null;
    }

    public List<Permission> getDatas() {
        //父级
        Sql sql = selectSql().select(Permission.TITLE,Permission.ID,Permission.PID).eqOr(Permission.TITLE,"组织建模","排产建模","往来单位","物料建模","仓库建模","生产建模","设备管理","点检建模","禀议建模","容器管理","质量建模","基础档案");
        List<Record> datas = findRecord(sql);
        List<Permission> permissionList = find(sql);

        if (CollUtil.isNotEmpty(datas)) {
            for (Record data : datas) {
                //子级
                Sql sql1 = selectSql().select(Permission.TITLE,Permission.ID,Permission.PID).eq(Permission.PID,data.getStr("id"));
                permissionList.addAll(find(sql1));
            }
        }
        return convertToModelTree(permissionList, "id", "pid", (p)->notOk(p.getPid()));
        }

    public List<Permission> getFormDatas() {
        //父级
        Sql sql = selectSql().select(Permission.TITLE,Permission.ID,Permission.PID).eqOr(Permission.TITLE,"质量管理","入库管理","出库管理","发货管理");
        List<Record> datas = findRecord(sql);
        List<Permission> allDatas = find(sql);

        if (CollUtil.isNotEmpty(datas)) {
            for (Record data : datas) {
                //子级与排除
                Sql sql1 = selectSql().select(Permission.TITLE,Permission.ID,Permission.PID).eq(Permission.PID,data.getStr("id")).notEq(Permission.TITLE,"生产备料").notEq(Permission.TITLE,"扫码出货").notEq(Permission.TITLE,"双码扫码出货");
                allDatas.addAll(find(sql1));
            }
        }
        Sql sql2 = selectSql().select(Permission.TITLE,Permission.ID,Permission.PID).eqOr(Permission.TITLE,"采购订单管理","委外订单管理","制造工单批量编辑","制造工单管理");
        allDatas.addAll(find(sql2));

        return convertToModelTree(allDatas,"id", "pid", (p)->notOk(p.getPid()));
    }

    }