package cn.rjtech.admin.sysmaterialsprepare;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SysMaterialspreparedetail;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 备料单明细
 * @ClassName: SysMaterialspreparedetailAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-12 18:31
 */
@CheckPermission(PermissionKey.ADMIN_MOPICKITEMD)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/sysmaterialspreparedetail", viewPath = "/_view/admin/sysmaterialspreparedetail")
public class SysMaterialspreparedetailAdminController extends BaseAdminController {

	@Inject
	private SysMaterialspreparedetailService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("SourceBillType")));
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
		renderJson(service.save(getModel(SysMaterialspreparedetail.class, "sysMaterialspreparedetail")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SysMaterialspreparedetail sysMaterialspreparedetail=service.findById(getLong(0));
		if(sysMaterialspreparedetail == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("sysMaterialspreparedetail",sysMaterialspreparedetail);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(SysMaterialspreparedetail.class, "sysMaterialspreparedetail")));
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
