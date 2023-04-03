package cn.rjtech.admin.backuplog;

import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.BackupLog;
/**
 * 开发管理-备份记录
 * @ClassName: BackupLogAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-31 17:06
 */
@Before(JBoltAdminAuthInterceptor.class)
@CheckPermission(PermissionKey.BACKUP_LOG)
@UnCheckIfSystemAdmin
@Path(value = "/admin/backuplog", viewPath = "/_view/admin/backuplog")
public class BackupLogAdminController extends BaseAdminController {

	@Inject
	private BackupLogService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords()));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add.html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(BackupLog.class, "backupLog")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		BackupLog backupLog=service.findById(getLong(0));
		if(backupLog == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("backupLog",backupLog);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(BackupLog.class, "backupLog")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}


}
