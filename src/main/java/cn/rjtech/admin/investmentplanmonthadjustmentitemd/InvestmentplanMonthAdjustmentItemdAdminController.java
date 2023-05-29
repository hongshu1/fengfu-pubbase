package cn.rjtech.admin.investmentplanmonthadjustmentitemd;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.InvestmentplanMonthAdjustmentItemd;
import cn.rjtech.util.ValidationUtils;
/**
 * 投资月度实绩项目明细 Controller
 * @ClassName: InvestmentplanMonthAdjustmentItemdAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-02-02 17:54
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/investmentplanmonthadjustmentitemd", viewPath = "/_view/admin/investmentplanmonthadjustmentitemd")
public class InvestmentplanMonthAdjustmentItemdAdminController extends BaseAdminController {

	@Inject
	private InvestmentplanMonthAdjustmentItemdService service;

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
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKeywords()));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add.html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		InvestmentplanMonthAdjustmentItemd investmentplanMonthAdjustmentItemd = service.findById(useIfPresent(getLong(0)));
		ValidationUtils.notNull(investmentplanMonthAdjustmentItemd, JBoltMsg.DATA_NOT_EXIST);

		set("investmentplanMonthAdjustmentItemd", investmentplanMonthAdjustmentItemd);
		render("edit.html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(useIfValid(InvestmentplanMonthAdjustmentItemd.class, "investmentplanMonthAdjustmentItemd")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(useIfValid(InvestmentplanMonthAdjustmentItemd.class, "investmentplanMonthAdjustmentItemd")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}


}
