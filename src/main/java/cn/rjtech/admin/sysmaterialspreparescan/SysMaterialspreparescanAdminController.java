package cn.rjtech.admin.sysmaterialspreparescan;

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
import cn.rjtech.model.momdata.SysMaterialspreparescan;
/**
 * null管理
 * @ClassName: SysMaterialspreparescanAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-17 17:36
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/sysMaterialspreparescan", viewPath = "/_view/admin/sysmaterialspreparescan")
public class SysMaterialspreparescanAdminController extends BaseAdminController {

	@Inject
	private SysMaterialspreparescanService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("State")));
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
    @TxConfig(SysMaterialspreparescan.DATASOURCE_CONFIG_NAME)
	public void save(@Para("sysMaterialspreparescan")SysMaterialspreparescan sysMaterialspreparescan) {
		renderJson(service.save(sysMaterialspreparescan));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SysMaterialspreparescan sysMaterialspreparescan=service.findById(getLong(0));
		if(sysMaterialspreparescan == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("sysMaterialspreparescan",sysMaterialspreparescan);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(SysMaterialspreparescan.DATASOURCE_CONFIG_NAME)
	public void update(@Para("sysMaterialspreparescan")SysMaterialspreparescan sysMaterialspreparescan) {
		renderJson(service.update(sysMaterialspreparescan));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
    @TxConfig(SysMaterialspreparescan.DATASOURCE_CONFIG_NAME)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(SysMaterialspreparescan.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}


}
