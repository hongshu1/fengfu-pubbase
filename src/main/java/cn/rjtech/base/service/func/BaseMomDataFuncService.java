package cn.rjtech.base.service.func;

import cn.rjtech.constants.DataSourceConstants;

/**
 * MOM 业务数据源存储过程
 *
 * @author Kephon
 */
public abstract class BaseMomDataFuncService extends BaseFuncService {

    public String dataSourceConfigName() {
        return DataSourceConstants.MOMDATA;
    }

}
