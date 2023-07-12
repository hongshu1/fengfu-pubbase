package cn.rjtech.cache;

import cn.jbolt.core.cache.JBoltCache;
import cn.rjtech.admin.cusfieldsmappingd.CusFieldsMappingDService;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.List;

/**
 * 映射字段缓存
 *
 * @author Kephon
 */
public class CusFieldsMappingdCache extends JBoltCache {
    
    public static final CusFieldsMappingdCache ME = new CusFieldsMappingdCache();

    private final CusFieldsMappingDService service = Aop.get(CusFieldsMappingDService.class);

    @Override
    public String getCacheTypeName() {
        return "cusFieldsMappingd_cache";
    }

    public List<Record> getImportRecordsByTableName(File file, String tableName) {
        return service.getImportRecordsByTableName(file, tableName);
    }

}
