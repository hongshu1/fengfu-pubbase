package cn.rjtech.base.service.view;

import cn.jbolt.core.service.base.BaseViewService;
import cn.rjtech.constants.DataSourceConstants;

/**
 * MOM 业务数据源抽象实现
 *
 * @author Kephon
 */
public abstract class BaseMomDataViewService extends BaseViewService {

    @Override
    protected String getDataSourceConfigName() {
        return DataSourceConstants.MOMDATA;
    }

}
