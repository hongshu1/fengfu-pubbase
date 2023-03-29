package cn.rjtech.admin.bommasteraudit;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.BomMasterAudit;
/**
 * 物料建模-BOM版本审核记录
 * @ClassName: BomMasterAuditAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-28 15:28
 */

@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/bommasteraudit", viewPath = "/_view/admin/bommasteraudit")
public class BomMasterAuditAdminController extends BaseAdminController {

	@Inject
	private BomMasterAuditService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("IsDeleted")));
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
		renderJson(service.save(getModel(BomMasterAudit.class, "bomMasterAudit")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		BomMasterAudit bomMasterAudit=service.findById(getLong(0));
		if(bomMasterAudit == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("bomMasterAudit",bomMasterAudit);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(BomMasterAudit.class, "bomMasterAudit")));
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

   /**
	* 切换IsDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"IsDeleted"));
	}


}
