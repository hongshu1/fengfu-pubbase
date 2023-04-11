package cn.rjtech.app.user.service;

import cn.rjtech.base.service.BaseU9RecordService;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

public class UserAppService extends BaseU9RecordService {

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
    public Record getUserByUserCode(String erpDBName, String erpDBSchemas, String userCode) {
        Kv para = Kv.by("erpDBName", erpDBName)
                .set("erpDBSchemas", erpDBSchemas)
                .set("userCode", userCode);

        return dbTemplate("userapp.getUserByUserCode", para).findFirst();
    }

    /**
     * 通过组织编码、用户编码查询权限列表
     * @param organizeCode
     * @param userCode
     * @return
     */
    /*public List<Map> getPermissionList(String organizeCode, String userCode){
        return userDao.getPermissionList(organizeCode,userCode);
    }*/

}
