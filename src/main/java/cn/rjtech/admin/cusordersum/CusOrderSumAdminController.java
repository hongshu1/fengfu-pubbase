package cn.rjtech.admin.cusordersum;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.CusOrderSum;

import java.util.Date;
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
		render("index.html");
	}
   /**
	* 数据源
	*/
	public void datas() {
		Kv kv = getKv();
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
	 * 获取表格
	 */
	public void getYear() {
		Kv kv = getKv();
		Date startTime = kv.getDate("startTime");
		Date endTime = kv.getDate("endTime");

		set("startTime", startTime);
		set("endTime", endTime);
		set("cinvcode", kv.getStr("cinvcode"));
		set("cinvcode1", kv.getStr("cinvcode1"));
		set("cinvname1", kv.getStr("cinvname1"));

		if (null != startTime && null != endTime) {
			set("iYear1", startTime.getYear() + 1900);
			set("iMonth1", startTime.getMonth());
			set("iYear2", endTime.getYear() + 1900);
			set("iMonth2", endTime.getMonth());
		} else if (null != endTime) {
			set("iYear2", endTime.getYear() + 1900);
			set("iMonth2", endTime.getMonth());
			set("iYear1", endTime.getYear() + 1900);
			set("iMonth1", 1);

		} else if (null != startTime) {
			set("iYear1", startTime.getYear() + 1900);
			set("iMonth1", startTime.getMonth());
			set("iYear2", startTime.getYear() + 1900);
			set("iMonth2", 12);
		} else {
			set("iYear", 2023);
		}

		render("year.html");
	}

}
