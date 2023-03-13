package cn.jbolt._admin.rolepermission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionService;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.model.PermissionBtn;
import cn.jbolt.core.service.JBoltRolePermissionService;
import cn.rjtech.admin.permissionbtn.PermissionBtnService;
import cn.rjtech.cache.RjApplicationCache;
import cn.rjtech.model.main.Application;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 角色权限中间表Service
 * @ClassName:  RolePermissionService   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2019年11月9日   
 */
public class RolePermissionService extends JBoltRolePermissionService {

    @Inject
    private PermissionService permissionService;
    @Inject
    private PermissionBtnService permissionBtnService;

    public List<JsTreeBean> getPermissionTree(Integer openLevel, Long applicationId, String enableIcon, Integer roletype, Long id) {
        // 所有权限资源（菜单）
        List<Record> permissions = permissionService.getMenuRecordWithAlias(applicationId, null);

        if (CollUtil.isNotEmpty(permissions)) {
            // 按钮
            List<Record> btns = permissionBtnService.findMenuBtnWithAlias(applicationId, null);
            if (CollUtil.isNotEmpty(btns)) {
                permissions.addAll(btns);
            }
        }

        return convertJsRecordTree(permissions, null, openLevel, "id", "title", null, enableIcon, "菜单列表", false);
    }

    @Override
    public String getTextColumn(Record m, String textColumn, String enableIcon) {
        // 菜单 显示所属应用系统
        if (null == m.get(PermissionBtn.VIEW_TYPE)) {
            Application application = RjApplicationCache.ME.get(m.getLong("application_id"));
            return String.format("[%s]%s%s", application.getAppName(), m.getStr(textColumn), StrUtil.blankToDefault(enableIcon, StrUtil.EMPTY));
        }
        return String.format("[按钮]%s%s", m.getStr(textColumn), StrUtil.blankToDefault(enableIcon, StrUtil.EMPTY));
    }

}
