package cn.rjtech.admin.subcontractorderdqty;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SubcontractorderdQty;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 采购/委外订单-采购订单明细数量
 * @ClassName: SubcontractorderdQtyAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-25 21:32
 */
@Path(value = "/admin/subcontractorderdqty", viewPath = "/_view/admin/subcontractorderdqty")
public class SubcontractorderdQtyAdminController extends BaseAdminController {

	@Inject
	private SubcontractorderdQtyService service;

   /**
	* 首页
	*/
	public void index() {
		render("index().html");
	}
   /**
	* 数据源
	*/
	public void datas() {
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize()));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add().html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(SubcontractorderdQty.class, "subcontractorderdQty")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SubcontractorderdQty subcontractorderdQty=service.findById(getLong(0));
		if(subcontractorderdQty == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("subcontractorderdQty",subcontractorderdQty);
		render("edit().html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(SubcontractorderdQty.class, "subcontractorderdQty")));
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
