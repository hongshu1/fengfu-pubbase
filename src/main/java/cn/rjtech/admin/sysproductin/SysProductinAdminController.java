package cn.rjtech.admin.sysproductin;

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
import cn.rjtech.model.momdata.SysProductin;
/**
 * 产成品入库单
 * @ClassName: SysProductinAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-08 09:56
 */
@CheckPermission(PermissionKey.PRODUCTINLIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/productInList", viewPath = "/_view/admin/sysProductin")
public class SysProductinAdminController extends BaseAdminController {

	@Inject
	private SysProductinService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
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
		renderJson(service.save(getModel(SysProductin.class, "sysProductin")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SysProductin sysProductin=service.findById(getLong(0));
		if(sysProductin == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("sysProductin",sysProductin);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(SysProductin.class, "sysProductin")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteRmRdByIds(get("ids")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}

   /**
	* 切换IsDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"IsDeleted"));
	}

	/**
	 * 新增-可编辑表格-批量提交
	 */
	@Before(Tx.class)
	public void submitAll() {
		renderJson(service.submitByJBoltTable(getJBoltTable()));
	}

}
