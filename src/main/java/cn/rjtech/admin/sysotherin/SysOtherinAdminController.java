package cn.rjtech.admin.sysotherin;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;

import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.SysOtherin;
/**
 * 其它入库单
 * @ClassName: SysOtherinAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-05 17:57
 */

@CheckPermission(PermissionKey.OTHER_IN_LIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/otherInList", viewPath = "/_view/admin/sysotherin")
public class SysOtherinAdminController extends BaseAdminController {

	@Inject
	private SysOtherinService service;

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
		renderJson(service.save(getModel(SysOtherin.class, "sysOtherin")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SysOtherin sysOtherin=service.findById(getLong(0));
		if(sysOtherin == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("sysotherin",sysOtherin);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(SysOtherin.class, "sysOtherin")));
	}

	/**
	 * 批量删除主从表
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
	 * 新增-可编辑表格-批量提交
	 */
	@Before(Tx.class)
	public void submitAll() {
		renderJson(service.submitByJBoltTable(getJBoltTable()));
	}


}
