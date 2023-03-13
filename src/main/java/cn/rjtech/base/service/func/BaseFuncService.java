package cn.rjtech.base.service.func;

import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbTemplate;
import com.jfinal.plugin.activerecord.ICallback;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 存储过程抽象实现
 *
 * @author Kephon
 */
public abstract class BaseFuncService {

    /**
     * 数据源
     */
    public abstract String dataSourceConfigName();

    public Object execute(ICallback callback) {
        return Db.use(dataSourceConfigName()).execute(callback);
    }

    public List<Record> findRecords(String sql, Object... para) {
        return Db.use(dataSourceConfigName()).find(sql, para);
    }

    public DbTemplate dbTemplate(String key) {
        return dbTemplate(key, Okv.create());
    }

    public DbTemplate dbTemplate(String key, Kv data) {
        return dbTemplate(key, Okv.create().set(data));
    }

    public DbTemplate dbTemplate(String key, Okv data) {
        return Db.use(dataSourceConfigName()).template(key, data);
    }

}
