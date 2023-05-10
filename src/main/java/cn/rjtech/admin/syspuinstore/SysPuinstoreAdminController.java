package cn.rjtech.admin.syspuinstore;

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
import cn.rjtech.model.momdata.SysPuinstore;
/**
 * 采购入库单
 * @ClassName: SysPuinstoreAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 15:38
 */
@CheckPermission(PermissionKey.PURCHASERECEIPTLIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/purchaseReceiptList", viewPath = "/_view/admin/sysPuinstore")
public class SysPuinstoreAdminController extends BaseAdminController {

	@Inject
	private SysPuinstoreService service;
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
		renderJson(service.save(getModel(SysPuinstore.class, "sysPuinstore")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SysPuinstore sysPuinstore=service.findById(getLong(0));
		if(sysPuinstore == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("sysPuinstore",sysPuinstore);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(SysPuinstore.class, "sysPuinstore")));
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