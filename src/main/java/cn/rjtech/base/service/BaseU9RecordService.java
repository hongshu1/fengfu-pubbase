package cn.rjtech.base.service;

import cn.jbolt.core.base.JBoltIDGenMode;
import cn.jbolt.core.db.sql.DBType;
import cn.jbolt.core.kit.OrgAccessKit;
import cn.jbolt.core.service.base.JBoltBaseRecordService;
import cn.rjtech.constants.DataSourceConstants;

/**
 * U9 Record模式Service
 *
 * @author Kephon
 */
public abstract class BaseU9RecordService extends JBoltBaseRecordService {

    @Override
    protected String getDataSourceConfigName() {
        return DataSourceConstants.U9;
    }

    @Override
    protected int systemLogTargetType() {
        return 0;
    }

    @Override
    protected String dbType() {
        return DBType.SQLSERVER;
    }

    @Override
    protected String getIdGenMode() {
        return JBoltIDGenMode.AUTO;
    }

    protected Long getOrgId() {
        return OrgAccessKit.getOrgId();
    }

    protected String getOrgCode() {
        return OrgAccessKit.getOrgCode();
    }

    protected String getOrgName() {
        return OrgAccessKit.getOrgName();
    }

}
