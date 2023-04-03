package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseBackupLog;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 开发管理-备份记录
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Base_BackupLog" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class BackupLog extends BaseBackupLog<BackupLog> {
}
