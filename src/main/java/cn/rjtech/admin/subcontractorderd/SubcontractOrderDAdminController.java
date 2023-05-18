package cn.rjtech.admin.subcontractorderd;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SubcontractOrderD;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 采购/委外订单-采购订单明细
 * @ClassName: SubcontractOrderDAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-25 21:33
 */
@Path(value = "/admin/subcontractorderd", viewPath = "/_view/admin/subcontractorderd")
public class SubcontractOrderDAdminController extends BaseAdminController {

	@Inject
	private SubcontractOrderDService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getBoolean("isPresent"), getBoolean("isDeleted")));
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
		renderJson(service.save(getModel(SubcontractOrderD.class, "subcontractOrderD")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SubcontractOrderD subcontractOrderD=service.findById(getLong(0));
		if(subcontractOrderD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("subcontractOrderD",subcontractOrderD);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(SubcontractOrderD.class, "subcontractOrderD")));
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
	* 切换isPresent
	*/
	public void toggleIsPresent() {
	    renderJson(service.toggleBoolean(getLong(0),"isPresent"));
	}

   /**
	* 切换isDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}


}
