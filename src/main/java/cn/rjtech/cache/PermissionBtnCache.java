package cn.rjtech.cache;

import cn.jbolt.core.cache.JBoltCache;
import cn.jbolt.core.model.PermissionBtn;
import cn.rjtech.admin.permissionbtn.PermissionBtnService;
import com.jfinal.aop.Aop;

/**
 * 菜单按钮缓存
 *
 * @author Kephon
 */
public class PermissionBtnCache extends JBoltCache {

    public static final PermissionBtnCache ME = new PermissionBtnCache();

    private static final String TYPE_NAME = "permission_btn";

    private final PermissionBtnService service = Aop.get(PermissionBtnService.class);

    @Override
    public String getCacheTypeName() {
        return TYPE_NAME;
    }

    public PermissionBtn get(long id) {
        return service.findById(id);
    }

}
