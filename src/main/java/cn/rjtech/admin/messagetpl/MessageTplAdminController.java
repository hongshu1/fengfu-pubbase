package cn.rjtech.admin.messagetpl;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MessageTpl;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 消息模板 Controller
 *
 * @ClassName: SysMessageTemplateAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-31 15:33
 */
@CheckPermission(PermissionKey.MESSAGETPL)
@UnCheckIfSystemAdmin
@Path(value = "/admin/messagetpl", viewPath = "_view/admin/messagetpl")
public class MessageTplAdminController extends BaseAdminController {

    @Inject
    private MessageTplService service;

    /**
     * 首页
     */
    public void index() {
        render("index.html");
    }

    /**
     * 数据源
     */
    @UnCheck
    public void datas() {
        renderJsonData(service.adminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isEnabled"), getInt("messageName"), get("ctrigger")));
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
        MessageTpl messageTpl = service.findById(getLong(0));

        if (messageTpl == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("sysMessageTemplate", messageTpl);
        render("edit.html");
    }

    /**
     * 表格提交
     */
    @UnCheck
    public void submitTable() {
        renderJson(service.submitTable(getJBoltTable()));
    }

    public void messageUserDate() {
        String messageId = get("messageId");

        renderJsonData(service.findMessage(messageId));
    }


    /**
     * 保存
     */
    @Before(Tx.class)
    public void save() {
        MessageTpl messageTpl = getModel(MessageTpl.class, "sysMessageTemplate");
        System.out.println("123" + messageTpl.toJson());
        renderJson(service.save(messageTpl));
    }

    /**
     * 删除
     */
    public void deleteByAjax() {
        renderJson(service.deleteByAjax());
    }

    /**
     * 更新
     */
    @Before(Tx.class)
    public void update() {
        renderJson(service.update(getModel(MessageTpl.class, "sysMessageTemplate")));
    }

    /**
     * 删除
     */
    @Before(Tx.class)
    public void delete() {
        renderJson(service.updateColumn(getLong(0), "del_flag", "1"));
    }

    /**
     * 切换toggleDelFlag
     */
    @Before(Tx.class)
    public void toggleDelFlag() {
        renderJson(service.toggleDelFlag(getLong(0)));
    }

    /**
     * 切换toggleIsOn
     */
    @Before(Tx.class)
    public void toggleIson() {
        renderJson(service.toggleIsOn(getLong(0)));
    }


}
