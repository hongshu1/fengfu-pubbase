package cn.rjtech.admin.backupconfig;

import cn.jbolt.core.base.config.JBoltConfig;
import cn.rjtech.model.momdata.BackupConfig;
import com.jfinal.aop.Aop;
import com.jfinal.log.Log;
import com.jfinal.plugin.cron4j.ITask;

public class BackupConfigTask implements ITask {

    private static final Log LOG = Log.getLog("JBoltCron4jLog");


    @Override
    public void run() {
        BackupConfigService backupConfigService = Aop.get(BackupConfigService.class);
        BackupConfig firstConfig = backupConfigService.findFirstConfig();
        if(!JBoltConfig.DEV_MODE) {
            LOG.debug("定时任务:每隔"+firstConfig.getIDayPeriod()+"天"+firstConfig.getIHour()+"小时"+firstConfig.getIMin()+"分钟备份一次 - 开始执行");
        }
        backupConfigService.copyFileTask();
        if(!JBoltConfig.DEV_MODE) {
            LOG.debug("定时任务:每隔"+firstConfig.getIDayPeriod()+"天"+firstConfig.getIHour()+"小时"+firstConfig.getIMin()+"分钟备份一次 - 执行完毕");
        }
    }

    @Override
    public void stop() {
        if(!JBoltConfig.DEV_MODE) {
            LOG.debug("stop 完成定时备份任务");
        }
    }
}
