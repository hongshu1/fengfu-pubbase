package cn.rjtech.admin.demandplanm;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.DemandPlanM;
/**
 * 需求计划管理-到货计划主表
 * @ClassName: DemandPlanMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-19 16:15
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/demandplanm", viewPath = "/_view/admin/demandplanm")
public class DemandPlanMAdminController extends BaseAdminController {

	@Inject
	private DemandPlanMService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("IsDeleted")));
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
		renderJson(service.save(getModel(DemandPlanM.class, "demandPlanM")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		DemandPlanM demandPlanM=service.findById(getLong(0));
		if(demandPlanM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("demandPlanM",demandPlanM);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(DemandPlanM.class, "demandPlanM")));
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
	* 切换IsDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"IsDeleted"));
	}
}
