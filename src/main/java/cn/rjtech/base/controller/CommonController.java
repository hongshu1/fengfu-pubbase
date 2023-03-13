package cn.rjtech.base.controller;

import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;

/**
 * 公共路由
 *
 * @author Kephon
 */
@Path(value = "/common")
public class CommonController extends JBoltBaseController {

    public void qrcode(@Para(value = "code") String code,
                       @Para(value = "width", defaultValue = "200") Integer width,
                       @Para(value = "height", defaultValue = "200") Integer height) {
        ValidationUtils.notBlank(code, "缺少参数code");

        renderQrCode(code, width, height);
    }

}
