package cn.rjtech.admin.uptimeparam;

import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
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
import cn.rjtech.model.momdata.UptimeParam;
/**
 * 稼动时间参数
 * @ClassName: UptimeParamAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-26 15:14
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/uptimeParam", viewPath = "/_view/admin/uptimeparam")
@CheckPermission(PermissionKey.UPTIME_PARAM)
@UnCheckIfSystemAdmin
public class UptimeParamAdminController extends BaseAdminController {

	@Inject
	private UptimeParamService service;
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
    @TxConfig(UptimeParam.DATASOURCE_CONFIG_NAME)
	public void save(@Para("uptimeParam")UptimeParam uptimeParam) {
		renderJson(service.save(uptimeParam));
	}

   /**
	* 编辑
	*/
	public void edit() {
		UptimeParam uptimeParam=service.findById(getLong(0));
		if(uptimeParam == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("uptimeParam",uptimeParam);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(UptimeParam.DATASOURCE_CONFIG_NAME)
	public void update(@Para("uptimeParam")UptimeParam uptimeParam) {
		renderJson(service.update(uptimeParam));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(UptimeParam.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

   /**
	* 切换isEnabled
	*/
	@Before(Tx.class)
    @TxConfig(UptimeParam.DATASOURCE_CONFIG_NAME)
	public void toggleIsEnabled() {
	    renderJson(service.toggleBoolean(getLong(0),"isEnabled"));
	}

   /**
	* 切换isDeleted
	*/
	@Before(Tx.class)
    @TxConfig(UptimeParam.DATASOURCE_CONFIG_NAME)
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}


}
