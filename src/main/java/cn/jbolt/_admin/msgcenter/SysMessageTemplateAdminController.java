package cn.jbolt._admin.msgcenter;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.model.SysMessageTemplate;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.main.MessageUser;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.List;

/**
 * 消息模板 Controller
 * @ClassName: SysMessageTemplateAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-31 15:33
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/messageTemplate", viewPath = "/msgcenter/messagetemplate")
public class SysMessageTemplateAdminController extends BaseAdminController {

	@Inject
	private SysMessageTemplateService service;
	@Inject
	private MessageUserService messageUserService;

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
		renderJsonData(service.adminDatas(getPageNumber(),getPageSize(),getKeywords(), getBoolean("isEnabled"),getInt("messageName"),getInt("messageChance")));
		//renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKeywords()));
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
		SysMessageTemplate sysMessageTemplate=service.findById(getLong(0));

		if(sysMessageTemplate == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("sysMessageTemplate",sysMessageTemplate);
		render("edit.html");
	}

	public void messageUserDate(){
		String messageId = get("messageId");
		List<Record>  messageUsers = service.findRecord(
				"SELECT * FROM jb_message_user mu " +
						"LEFT JOIN jb_user ju on  mu.user_id = ju.id " +
						"WHERE mu.message_id = ? and del_fag = '0'",false, messageId);
		renderJsonData(messageUsers);
	}


  /**
	* 保存
	*/
    @Before(Tx.class)
	public void save() {
		SysMessageTemplate sysMessageTemplate = getModel(SysMessageTemplate.class, "sysMessageTemplate");
		System.out.println("123"+sysMessageTemplate.toJson());
		renderJson(service.save(sysMessageTemplate));
	}

   /**
	* 更新
	*/
    @Before(Tx.class)
	public void update() {

		renderJson(service.update(getModel(SysMessageTemplate.class, "sysMessageTemplate")));
	}
	/**
	 * 删除
	 */
	@Before(Tx.class)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
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
