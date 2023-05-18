package cn.rjtech.admin.subcontractorderdbatchversion;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SubcontractOrderDBatchVersion;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 采购/委外管理-采购现品票版本记录
 * @ClassName: SubcontractOrderDBatchVersionAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-25 21:31
 */
@Path(value = "/admin/subcontractorderdbatchversion", viewPath = "/_view/admin/subcontractorderdbatchversion")
public class SubcontractOrderDBatchVersionAdminController extends BaseAdminController {

	@Inject
	private SubcontractOrderDBatchVersionService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords()));
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
		renderJson(service.save(getModel(SubcontractOrderDBatchVersion.class, "subcontractOrderDBatchVersion")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SubcontractOrderDBatchVersion subcontractOrderDBatchVersion=service.findById(getLong(0));
		if(subcontractOrderDBatchVersion == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("subcontractOrderDBatchVersion",subcontractOrderDBatchVersion);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(SubcontractOrderDBatchVersion.class, "subcontractOrderDBatchVersion")));
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
