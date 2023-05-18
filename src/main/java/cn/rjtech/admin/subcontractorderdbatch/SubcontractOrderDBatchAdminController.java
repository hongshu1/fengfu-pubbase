package cn.rjtech.admin.subcontractorderdbatch;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SubcontractOrderDBatch;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 采购/委外管理-采购现品票
 * @ClassName: SubcontractOrderDBatchAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-25 21:32
 */
@Path(value = "/admin/subcontractorderdbatch", viewPath = "/_view/admin/subcontractorderdbatch")
public class SubcontractOrderDBatchAdminController extends BaseAdminController {

	@Inject
	private SubcontractOrderDBatchService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getBoolean("isEffective")));
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
		renderJson(service.save(getModel(SubcontractOrderDBatch.class, "subcontractOrderDBatch")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SubcontractOrderDBatch subcontractOrderDBatch=service.findById(getLong(0));
		if(subcontractOrderDBatch == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("subcontractOrderDBatch",subcontractOrderDBatch);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(SubcontractOrderDBatch.class, "subcontractOrderDBatch")));
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
	* 切换isEffective
	*/
	public void toggleIsEffective() {
	    renderJson(service.toggleBoolean(getLong(0),"isEffective"));
	}


}
