package cn.rjtech.admin.subcontractorderref;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SubcontractOrderRef;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 采购/委外-委外订单与到货计划关联
 * @ClassName: SubcontractOrderRefAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-25 21:33
 */
@Path(value = "/admin/subcontractorderref", viewPath = "/_view/admin/subcontractorderref")
public class SubcontractOrderRefAdminController extends BaseAdminController {

	@Inject
	private SubcontractOrderRefService service;

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
		renderJson(service.save(getModel(SubcontractOrderRef.class, "subcontractOrderRef")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SubcontractOrderRef subcontractOrderRef=service.findById(getLong(0));
		if(subcontractOrderRef == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("subcontractOrderRef",subcontractOrderRef);
		render("edit().html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(SubcontractOrderRef.class, "subcontractOrderRef")));
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
