package cn.rjtech.admin;

import cn.jbolt.core.base.config.JBoltConfig;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.base.redis.RedisKeys;
import cn.rjtech.base.redis.RedisUtil;
import cn.rjtech.util.Util;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;

/**
 * 操作系统命令执行，支持linux
 *
 * @author Kephon
 */
@Path(value = "/service")
public class ServiceController extends BaseAdminController {

    public void restart(@Para(value = "exec", defaultValue = "true") Boolean exec)  {
        ValidationUtils.equals("ecs", JBoltConfig.PDEV, "非ECS环境，禁止执行");

        if (RedisUtil.exists(RedisKeys.RESTART_TIME)) {
            renderJsonFail("当前正在执行重启任务...");
            return;
        }

        RedisUtil.setEx(RedisKeys.RESTART_TIME, 1,60);

        ok();

        if (exec) {
            LOG.info("执行重启任务...");

            new Thread(() -> Util.exec("nohup /home/scripts/rj-pubbase.sh > /dev/null 2>&1 &")).start();
        }
    }

}
