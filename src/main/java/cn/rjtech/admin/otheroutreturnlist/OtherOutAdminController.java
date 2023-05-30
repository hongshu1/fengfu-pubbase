package cn.rjtech.admin.otheroutreturnlist;

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
import cn.rjtech.model.momdata.OtherOut;
import org.apache.commons.lang3.StringUtils;

/**
 * 出库管理-特殊领料出库 Controller
 * @ClassName: OtherOutAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-22 16:33
 */
@CheckPermission(PermissionKey.OTHER_OUT_RETURN_LIST)
@UnCheckIfSystemAdmin
@Path(value = "/admin/otheroutreturnlist", viewPath = "/_view/admin/otheroutreturnlist")
public class OtherOutAdminController extends BaseAdminController {

	@Inject
	private OtherOutService service;

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
		Kv kv =new Kv();
		kv.setIfNotNull("billno", get("billno"));
		kv.setIfNotNull("deptname", get("deptname"));
		kv.setIfNotNull("sourcebilltype", get("sourcebilltype"));
		kv.setIfNotNull("iorderstatus", get("iorderstatus"));
		kv.setIfNotNull("startdate", get("startdate"));
		kv.setIfNotNull("enddate", get("enddate"));
		renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), kv));
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
		OtherOut otherOut=service.findById(getLong(0)); 
		if(otherOut == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("status", get("status"));
		set("otherOut",otherOut);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(OtherOut.class, "otherOut")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(OtherOut.class, "otherOut")));
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
	 * JBoltTable 可编辑表格整体提交 多表格
	 */
	public void submitMulti(String param, String revokeVal ,String autoid) {
		renderJson(service.submitByJBoltTables(getJBoltTables(),param,revokeVal,autoid));
	}


	/**
	 * 出库明细删除
	 */
	public void deleteList() {
		renderJson(service.deleteList(get(0)));
	}

}
