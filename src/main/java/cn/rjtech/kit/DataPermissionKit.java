package cn.rjtech.kit;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.cache.JBoltDeptCache;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.rjtech.admin.datapermission.DataPermissionService;
import cn.rjtech.annotations.CheckDataPermission;
import cn.rjtech.enums.BusObjectTypeEnum;
import cn.rjtech.enums.DataOperationEnum;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.jfinal.aop.Aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据权限
 *
 * @author Kephon
 */
public class DataPermissionKit {

    private static final DataPermissionService DATA_PERMISSION_SERVICE = Aop.get(DataPermissionService.class);

    /**
     * 权限部门
     */
    private static final TransmittableThreadLocal<List<String>> ACCESS_CDEPCODES_TRL = new TransmittableThreadLocal<>();

    /**
     * 校验方法是否添加了数据权限注解
     *
     * @param method 方法
     * @return true为是，false为否
     */
    public static boolean isAnnotatedWithCheckDataPermission(Method method) {
        return method.isAnnotationPresent(CheckDataPermission.class);
    }

    /**
     * 绑定当前用户具有访问权限的部门集合
     *
     * @param accessCdepcodes 权限部门集合
     */
    public static void setAccessCdepcodes(List<String> accessCdepcodes) {
        ACCESS_CDEPCODES_TRL.set(accessCdepcodes);
    }

    /**
     * 根据用户操作类型，获取权限集合
     *
     * @param user      用户
     * @param operation 操作类型
     * @param typeEnum  业务类型
     * @return 可访问的部门编码数组
     */
    public static List<String> getAccessCdepcodes(User user, DataOperationEnum operation, BusObjectTypeEnum typeEnum) {
        return DATA_PERMISSION_SERVICE.getAccessCdepcodes(user.getId(), user.getRoles(), operation, typeEnum);
    }

    /**
     * 清理线程
     */
    public static void clear() {
        ACCESS_CDEPCODES_TRL.remove();
    }

    public static List<String> getAccessCdepcodes() {
        return ACCESS_CDEPCODES_TRL.get();
    }

    /**
     * 获取sql模板
     *
     * @param alias      来源表别名
     * @param columnName 编码字段
     */
    public static String getDataPermissionSql(String alias, String columnName) {
        // 管理员不过滤
        if (JBoltUserKit.isSystemAdmin()) {
            return null;
        }

        // 具备权限的编码集合
        List<String> accessCdepcodes = getAccessCdepcodes();
        if (CollUtil.isEmpty(accessCdepcodes)) {
            return StrUtil.isBlank(alias) ? String.format(" AND ( %s IS NULL )", columnName) : String.format(" AND ( %s.%s IS NULL) ", alias, columnName);
        }

        List<String> sql = new ArrayList<>();

        if (StrUtil.isBlank(alias)) {
            for (String code : accessCdepcodes) {
                sql.add(String.format("( %s LIKE '%s", columnName, code) + "%')");
            }
        } else {
            for (String code : accessCdepcodes) {
                sql.add(String.format("( %s.%s LIKE '%s", alias, columnName, code) + "%')");
            }
        }

        return " AND (" + CollUtil.join(sql, "OR") + ")";
    }

    /**
     * 校验数据权限
     */
    public static void validateAccess(String sn) {
        // 超管不校验数据权限
        if (JBoltUserKit.isSystemAdmin()) {
            return;
        }

        List<String> accessCdepcodes = getAccessCdepcodes();
        ValidationUtils.notEmpty(accessCdepcodes, "当前用户缺少数据权限配置，请联系管理员");

        // 当前用户访问的部门编码 符合数据权限配置中编码开头
        for (String code : accessCdepcodes) {
            if (sn.startsWith(code)) {
                return;
            }
        }
        ValidationUtils.error("缺少数据权限");
    }

    /**
     * 校验部门权限
     *
     * @param idepId 部门ID
     */
    public static void validateAccess(long idepId) {
        validateAccess(JBoltDeptCache.me.getSn(idepId));
    }

}
