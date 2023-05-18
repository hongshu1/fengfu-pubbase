package cn.rjtech.app.user.service;

import cn.jbolt.core.service.base.BaseU8RecordService;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

public class UserAppService extends BaseU8RecordService {

    @Override
    protected String getTableName() {
        return null;
    }

    @Override
    protected String getPrimaryKey() {
        return null;
    }

    /**
     * 通过用户编码查询用户
     *
     * @param userCode 用户编码
     * @return 用户对象
     */
    public Record getUserByUserCode(String erpDbAlias, String erpDbName, String erpDbSchemas, String userCode) {
        Kv para = Kv.by("erpDBName", erpDbName)
                .set("erpDBSchemas", erpDbSchemas)
                .set("userCode", userCode);

        return dbTemplate(erpDbAlias, "userapp.getUserByUserCode", para).findFirst();
    }

}
