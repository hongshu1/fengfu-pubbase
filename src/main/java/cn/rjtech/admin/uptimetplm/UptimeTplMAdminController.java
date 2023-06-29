package cn.rjtech.admin.uptimetplm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.model.momdata.UptimeTplM;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;
/**
 * 稼动时间建模-稼动时间模板主表
 * @ClassName: UptimeTplMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 19:20
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/uptimeTplM", viewPath = "/_view/admin/uptimetplm")
@CheckPermission(PermissionKey.UPTIME_TPL)
@UnCheckIfSystemAdmin
public class UptimeTplMAdminController extends BaseAdminController {

	@Inject
	private UptimeTplMService service;
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
    @TxConfig(DataSourceConstants.MOMDATA)
	public void save(@Para("uptimeTplM")UptimeTplM uptimeTplM) {
		renderJson(service.save(uptimeTplM));
	}

   /**
	* 编辑
	*/
	public void edit() {
		UptimeTplM uptimeTplM=service.findById(getLong(0));
		if(uptimeTplM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("uptimeTplM",uptimeTplM);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
	public void update(@Para("uptimeTplM")UptimeTplM uptimeTplM) {
		renderJson(service.update(uptimeTplM));
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
		renderJson(service.delete(getLong(0)));
	}


	/**
	 * 保存
	 */
	public void submitAll()
	{
		renderJson(service.submitAll(getKv()));
	}
}
