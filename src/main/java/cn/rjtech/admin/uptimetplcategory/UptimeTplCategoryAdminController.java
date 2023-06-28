package cn.rjtech.admin.uptimetplcategory;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.UptimeTplCategory;
/**
 * 稼动时间建模-稼动时间模板类别
 * @ClassName: UptimeTplCategoryAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 19:17
 */
@Path(value = "/admin/uptimeTplCategory", viewPath = "/_view/admin/uptimetplcategory")
public class UptimeTplCategoryAdminController extends BaseAdminController {

	@Inject
	private UptimeTplCategoryService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
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
    @TxConfig(UptimeTplCategory.DATASOURCE_CONFIG_NAME)
	public void save(@Para("uptimeTplCategory")UptimeTplCategory uptimeTplCategory) {
		renderJson(service.save(uptimeTplCategory));
	}

   /**
	* 编辑
	*/
	public void edit() {
		UptimeTplCategory uptimeTplCategory=service.findById(getLong(0));
		if(uptimeTplCategory == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("uptimeTplCategory",uptimeTplCategory);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(UptimeTplCategory.DATASOURCE_CONFIG_NAME)
	public void update(@Para("uptimeTplCategory")UptimeTplCategory uptimeTplCategory) {
		renderJson(service.update(uptimeTplCategory));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(UptimeTplCategory.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}


}
