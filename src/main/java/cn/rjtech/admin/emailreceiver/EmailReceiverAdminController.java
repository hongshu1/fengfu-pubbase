package cn.rjtech.admin.emailreceiver;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.model.EmailReceiver;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;

/**
 * 邮件接收人 Controller
 *
 * @ClassName: EmailReceiverAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-03-03 09:23
 */
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.EMAIL_RECEIVER)
@Path(value = "/admin/emailreceiver",viewPath = "/_view/admin/emailreceiver")
public class EmailReceiverAdminController extends BaseAdminController {

    @Inject
    private EmailReceiverService service;

    /**
     * 首页
     */
    public void index() {
        render("index.html");
    }

    /**
     * 数据源
     */
    public void datas() {
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKeywords()));
    }

    /**
     * 新增
     */
    public void add() {
        render("add.html");
    }

    /**
     * 编辑
     */
    public void edit() {
        EmailReceiver emailReceiver = service.findById(useIfPresent(getLong(0)));
        ValidationUtils.notNull(emailReceiver, JBoltMsg.DATA_NOT_EXIST);

        set("emailReceiver", emailReceiver);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        EmailReceiver emailReceiver = useIfValid(EmailReceiver.class, "emailReceiver");
        emailReceiver.setEnable(false);
        renderJson(service.save(emailReceiver));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(useIfValid(EmailReceiver.class, "emailReceiver")));
    }

    /**
     * 切换启用状态
     */
    public void toggleEnable() {
        renderJson(service.toggleEnable(getLong(0)));
    }

    /**
     * 删除
     */
    public void deleteByIds(@Para(value = "ids") String ids) {
        ValidationUtils.notBlank(ids, "缺少id参数");

        renderJson(service.deleteByIds(ids, ","));
    }

}
