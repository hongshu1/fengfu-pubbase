package cn.rjtech.admin.uptimetplm;

import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.UptimeTplM;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isDeleted")));
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
    @TxConfig(UptimeTplM.DATASOURCE_CONFIG_NAME)
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
    @TxConfig(UptimeTplM.DATASOURCE_CONFIG_NAME)
	public void update(@Para("uptimeTplM")UptimeTplM uptimeTplM) {
		renderJson(service.update(uptimeTplM));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
    @TxConfig(UptimeTplM.DATASOURCE_CONFIG_NAME)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(UptimeTplM.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}


	/**
	 * 保存
	 */
	public void submitAll()
	{
		JBoltTableMulti jBoltTables = getJBoltTables();
		Kv kv = getKv();
		JBoltTable jBoltTable = getJBoltTable();
		JBoltPara para = getJBoltPara();
		renderJson(service.submitAll(kv));
	}
}
