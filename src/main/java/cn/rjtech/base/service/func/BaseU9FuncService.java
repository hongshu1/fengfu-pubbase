package cn.rjtech.base.service.func;

import cn.rjtech.constants.DataSourceConstants;

/**
 * U9 数据源存储过程
 *
 * @author Kephon
 */
public abstract class BaseU9FuncService extends BaseFuncService {

    public String dataSourceConfigName() {
        return DataSourceConstants.U9;
    }

}
