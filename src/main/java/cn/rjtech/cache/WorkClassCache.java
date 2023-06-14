package cn.rjtech.cache;

import cn.jbolt.core.cache.JBoltCache;
import cn.rjtech.admin.workclass.WorkClassService;
import com.jfinal.aop.Aop;

/**
 * @author Kephon
 */
public class WorkClassCache extends JBoltCache {

    public static final WorkClassCache ME = new WorkClassCache();

    private final WorkClassService service = Aop.get(WorkClassService.class);
    
    @Override
    public String getCacheTypeName() {
        return "work_class";
    }

    public String getIdByCode(String code) {
        return service.getIdByCode(code);
    }
    
}
