package cn.rjtech.model.main;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.main.base.BaseDataPermission;

/**
 * 数据权限
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "main" , table = "base_data_permission" , primaryKey = "id" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class DataPermission extends BaseDataPermission<DataPermission> {
}

