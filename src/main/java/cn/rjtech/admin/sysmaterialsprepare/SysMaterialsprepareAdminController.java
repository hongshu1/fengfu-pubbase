package cn.rjtech.admin.sysmaterialsprepare;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.SysMaterialsprepare;
/**
 * 材料备料表
 * @ClassName: SysMaterialsprepareAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-02 00:06
 */
@CheckPermission(PermissionKey.ADMIN_SYSMATERIALSPREPARE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/sysmaterialspreparedetail", viewPath = "/_view/admin/sysmaterialsprepare")
public class SysMaterialsprepareAdminController extends BaseAdminController {

	@Inject
	private SysMaterialsprepareService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("BillType")));
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
	public void save(SysMaterialsprepare sysMaterialsprepare) {
		renderJson(service.save(sysMaterialsprepare));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SysMaterialsprepare sysMaterialsprepare=service.findById(getLong(0));
		if(sysMaterialsprepare == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("sysMaterialsprepare",sysMaterialsprepare);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update(SysMaterialsprepare sysMaterialsprepare) {
		renderJson(service.update(sysMaterialsprepare));
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
