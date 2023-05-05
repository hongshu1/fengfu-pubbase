package cn.rjtech.cache;

import cn.jbolt.core.cache.JBoltCache;
import cn.jbolt.core.model.RjApplication;
import cn.jbolt.core.service.RjApplicationService;
import com.jfinal.aop.Aop;

/**
 * 应用系统缓存
 *
 * @author Kephon
 */
public class RjApplicationCache extends JBoltCache {

    public static final RjApplicationCache ME = new RjApplicationCache();

    private static final String TYPE_NAME = "rj_application";

    private final RjApplicationService service = Aop.get(RjApplicationService.class);

    @Override
    public String getCacheTypeName() {
        return TYPE_NAME;
    }

    public RjApplication get(long id) {
        return service.findById(id);
    }

}
