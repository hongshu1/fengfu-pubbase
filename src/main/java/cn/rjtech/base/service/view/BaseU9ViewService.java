package cn.rjtech.base.service.view;

import cn.jbolt.core.service.base.BaseViewService;
import cn.rjtech.base.exception.ParameterException;
import cn.rjtech.constants.DataSourceConstants;

/**
 * U9 视图Service
 *
 * @author Kephon
 */
public abstract class BaseU9ViewService extends BaseViewService {

    @Override
    protected String getDataSourceConfigName() {
        return DataSourceConstants.U9;
    }

    @Override
    protected String getPrimaryKey() {
        throw new ParameterException("禁止使用getPrimaryKey方法");
    }

}
