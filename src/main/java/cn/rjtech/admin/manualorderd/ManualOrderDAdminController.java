package cn.rjtech.admin.manualorderd;

import cn.jbolt.core.bean.JBoltDateRange;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ManualOrderD;

import java.util.Date;

/**
 * 客户订单-手配订单明细
 * @ClassName: ManualOrderDAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 15:33
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.MANUALORDER)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/manualorderd", viewPath = "/_view/admin/manualorderd")
public class ManualOrderDAdminController extends BaseAdminController {

	@Inject
	private ManualOrderDService service;

   /**
	* 首页
	*/
	public void index() {
		render("index.html");
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
		renderJson(service.save(getModel(ManualOrderD.class, "manualOrderD")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		ManualOrderD manualOrderD=service.findById(getLong(0));
		if(manualOrderD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("manualOrderD",manualOrderD);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(ManualOrderD.class, "manualOrderD")));
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
	* 切换isDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}

	public void dataList(){
		renderJsonData(service.dataList(getKv()));
	}

}
