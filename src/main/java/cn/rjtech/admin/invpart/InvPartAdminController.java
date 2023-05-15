package cn.rjtech.admin.invpart;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.InvPart;
/**
 * 存货物料表
 * @ClassName: InvPartAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-15 11:10
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/invPart", viewPath = "/_view/admin/invPart")
public class InvPartAdminController extends BaseAdminController {

	@Inject
	private InvPartService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getInt("iType")));
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
		renderJson(service.save(getModel(InvPart.class, "invPart")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		InvPart invPart=service.findById(getLong(0));
		if(invPart == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("invPart",invPart);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InvPart.class, "invPart")));
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
