package cn.rjtech.admin.demandpland;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.DemandPlanD;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 需求计划管理-到货计划细表
 * @ClassName: DemandPlanDAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-19 16:15
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/demandpland", viewPath = "/_view/admin/demandpland")
public class DemandPlanDAdminController extends BaseAdminController {

	@Inject
	private DemandPlanDService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize()));
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
		renderJson(service.save(getModel(DemandPlanD.class, "demandPlanD")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		DemandPlanD demandPlanD=service.findById(getLong(0));
		if(demandPlanD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("demandPlanD",demandPlanD);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(DemandPlanD.class, "demandPlanD")));
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
