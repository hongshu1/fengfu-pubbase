package cn.rjtech.admin.auditformconfig;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.AuditFormConfig;
import org.apache.commons.lang3.StringUtils;

/**
 * 审批表单配置 Controller
 * @ClassName: AuditFormConfigAdminController
 * @author: RJ
 * @date: 2023-04-18 17:18
 */
@CheckPermission(PermissionKey.AUDIT_CONFIG)
@UnCheckIfSystemAdmin
@Path(value = "/admin/auditformconfig", viewPath = "/_view/admin/auditformconfig")
public class AuditFormConfigAdminController extends BaseAdminController {

	@Inject
	private AuditFormConfigService service;

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
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv()));
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
		AuditFormConfig auditFormConfig=service.findById(getLong(0));
		if(auditFormConfig == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("auditFormConfig",auditFormConfig);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(AuditFormConfig.class, "auditFormConfig")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(AuditFormConfig.class, "auditFormConfig")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}

  /**
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}

  /**
	* 切换toggleIsEnabled
	*/
	public void toggleIsEnabled() {
		renderJson(service.toggleIsEnabled(getLong(0)));
	}

    /**
     * 行数据数据源
     */
    public void listData(){
        renderJsonData(service.listData(getKv()));
    }

    /**
     * 拉取资源Dialog
     */
    public void chooseForm(){
        String itemHidden = get("itemHidden");
        set("itemHidden", itemHidden);
        render("resource_list.html");
    }

    /**
     * 获取资源的数据源
     */
    public void resourceList(){
        String itemHidden = get("itemHidden");
        String keywords = StringUtils.trim(get("keywords"));
        Kv kv = new Kv();
        kv.setIfNotNull("itemHidden", itemHidden);
        kv.setIfNotNull("keywords", keywords);
        renderJsonData(service.resourceList(kv,getPageNumber(),getPageSize()));
    }

    /**
     * 提交数据保存方法
     */
    public void submit(){
        renderJson(service.submitByJBoltTable(getJBoltTable()));
    }

}
