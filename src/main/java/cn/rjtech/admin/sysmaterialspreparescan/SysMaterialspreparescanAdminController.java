package cn.rjtech.admin.sysmaterialspreparescan;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.model.momdata.SysMaterialspreparescan;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;
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
    @TxConfig(DataSourceConstants.MOMDATA)
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
    @TxConfig(DataSourceConstants.MOMDATA)
	public void update(@Para("sysMaterialspreparescan")SysMaterialspreparescan sysMaterialspreparescan) {
		renderJson(service.update(sysMaterialspreparescan));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}


}
