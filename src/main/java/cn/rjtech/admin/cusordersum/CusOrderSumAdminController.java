package cn.rjtech.admin.cusordersum;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.CusOrderSum;

import java.util.List;

/**
 * 客户订单-客户计划汇总
 * @ClassName: CusOrderSumAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-14 16:20
 */
@CheckPermission(PermissionKey.CUSORDER_SUM)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/cusordersum", viewPath = "/_view/admin/cusordersum")
public class CusOrderSumAdminController extends BaseAdminController {

	@Inject
	private CusOrderSumService service;

   /**
	* 首页
	*/
	public void index() {
		List<Record> records = service.dbTemplate("cusordersum.getYearMouth", getKv()).find();
		set("records", records);
		render("index.html");
	}
   /**
	* 数据源
	*/
	public void datas() {
		renderJsonData(service.findCusOrderSum(getPageNumber(), getPageSize(), getKv()));
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
		renderJson(service.save(getModel(CusOrderSum.class, "cusOrderSum")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		CusOrderSum cusOrderSum=service.findById(getLong(0));
		if(cusOrderSum == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("cusOrderSum",cusOrderSum);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(CusOrderSum.class, "cusOrderSum")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}


	/**
	 * 审批
	 */
	/*public void approve() {
		renderJson(service.approve(getLong(0)));
	}*/

}
