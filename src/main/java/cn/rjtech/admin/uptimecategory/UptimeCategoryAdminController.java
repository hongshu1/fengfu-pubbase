package cn.rjtech.admin.uptimecategory;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.UptimeCategory;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;
/**
 * 稼动时间建模-稼动时间参数类别
 * @ClassName: UptimeCategoryAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-26 14:25
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/uptimeCategory", viewPath = "/_view/admin/uptimecategory")
public class UptimeCategoryAdminController extends BaseAdminController {

	@Inject
	private UptimeCategoryService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), get("cUptimeCategoryName"), getBoolean("isDeleted")));
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
	@Before(Tx.class)
    @TxConfig(UptimeCategory.DATASOURCE_CONFIG_NAME)
	public void save(@Para("uptimeCategory")UptimeCategory uptimeCategory) {
		renderJson(service.save(uptimeCategory));
	}

   /**
	* 编辑
	*/
	public void edit() {
		UptimeCategory uptimeCategory=service.findById(getLong(0));
		if(uptimeCategory == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("uptimeCategory",uptimeCategory);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(UptimeCategory.DATASOURCE_CONFIG_NAME)
	public void update(@Para("uptimeCategory")UptimeCategory uptimeCategory) {
		renderJson(service.update(uptimeCategory));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(UptimeCategory.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

   /**
	* 切换isDeleted
	*/
	@Before(Tx.class)
    @TxConfig(UptimeCategory.DATASOURCE_CONFIG_NAME)
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}


}
