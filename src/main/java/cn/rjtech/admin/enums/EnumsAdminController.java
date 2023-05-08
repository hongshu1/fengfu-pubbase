package cn.rjtech.admin.enums;

import cn.jbolt.core.common.enums.DataSourceEnum;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.enumutil.JBoltEnum;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.enums.AndOrEnum;
import cn.rjtech.enums.BoolCharEnum;
import cn.rjtech.enums.ThirdpartySystemEnum;
import com.jfinal.core.Path;

/**
 * 公用枚举选项
 *
 * @author Kephon
 */
@UnCheck
@Path(value = "/admin/enums")
public class EnumsAdminController extends JBoltBaseController {

    /**
     * 是否
     */
    public void boolchar() {
        renderJsonData(JBoltEnum.getEnumOptionList(BoolCharEnum.class));
    }

    /**
     * 第三方系统
     */
    public void thirdpartysystem() {
        renderJsonData(JBoltEnum.getEnumOptionList(ThirdpartySystemEnum.class));
    }

    /**
     * 数据权限 数据来源
     */
    public void datasource() {
        renderJsonData(JBoltEnum.getEnumOptionList(DataSourceEnum.class));
    }

    /**
     * 审批角色之间的关系
     */
    public void andor() {
        renderJsonData(JBoltEnum.getEnumOptionList(AndOrEnum.class));
    }

}
