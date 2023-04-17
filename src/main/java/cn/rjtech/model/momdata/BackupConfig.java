package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseBackupConfig;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 开发管理-备份设置
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Base_BackupConfig" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class BackupConfig extends BaseBackupConfig<BackupConfig> {
}

