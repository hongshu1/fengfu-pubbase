package cn.rjtech.admin.rcvplanm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.CheckPermission;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.RcvPlanD;
/**
 * 出货管理-取货计划明细
 * @ClassName: RcvPlanDAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-27 14:25
 */
@CheckPermission(PermissionKey.PICKUP_PLAN_MANAGE)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/rcvpland", viewPath = "/_view/admin/rcvpland")
public class RcvPlanDAdminController extends BaseAdminController {

	@Inject
	private RcvPlanDService service;

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
		renderJson(service.save(getModel(RcvPlanD.class, "rcvPlanD")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		RcvPlanD rcvPlanD=service.findById(getLong(0));
		if(rcvPlanD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("rcvPlanD",rcvPlanD);
		render("edit().html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(RcvPlanD.class, "rcvPlanD")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByIdsRm(get("ids")));
	}

   /**
	* 删除
	*/
	public void delete() {

		renderJson(service.delete(getLong(0)));
	}

	public void findEditTableDatas(){
		renderJsonData(service.findEditTableDatas(getKv()));
	}

}
