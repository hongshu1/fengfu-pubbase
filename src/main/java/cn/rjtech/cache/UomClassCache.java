package cn.rjtech.cache;

import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.cache.JBoltCache;
import cn.rjtech.admin.uomclass.UomclassService;
import cn.rjtech.model.momdata.Uomclass;
import com.jfinal.aop.Aop;

/**
 * @author Kephon
 */
public class UomClassCache extends JBoltCache {

    public static final UomClassCache ME = new UomClassCache();

    private final UomclassService service = Aop.get(UomclassService.class);
    
    @Override
    public String getCacheTypeName() {
        return "uom_class";
    }

    public Long getUomClassIdByCode(String code) {
        Uomclass uomclass = service.getCacheByKey(code);
        if (ObjUtil.isNull(uomclass)) {
            return null;
        }
        return uomclass.getIAutoId();
    }
    
}
