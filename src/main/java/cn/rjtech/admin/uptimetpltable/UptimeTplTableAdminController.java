package cn.rjtech.admin.uptimetpltable;

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
import cn.rjtech.model.momdata.UptimeTplTable;
/**
 * 稼动时间建模-参数表格
 * @ClassName: UptimeTplTableAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 19:15
 */
@Path(value = "/admin/uptimeTplTable", viewPath = "/_view/admin/uptimetpltable")
public class UptimeTplTableAdminController extends BaseAdminController {

	@Inject
	private UptimeTplTableService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize()));
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
    @TxConfig(UptimeTplTable.DATASOURCE_CONFIG_NAME)
	public void save(@Para("uptimeTplTable")UptimeTplTable uptimeTplTable) {
		renderJson(service.save(uptimeTplTable));
	}

   /**
	* 编辑
	*/
	public void edit() {
		UptimeTplTable uptimeTplTable=service.findById(getLong(0));
		if(uptimeTplTable == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("uptimeTplTable",uptimeTplTable);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(UptimeTplTable.DATASOURCE_CONFIG_NAME)
	public void update(@Para("uptimeTplTable")UptimeTplTable uptimeTplTable) {
		renderJson(service.update(uptimeTplTable));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(UptimeTplTable.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

	public void uptimeTplTableDatas()
	{
		renderJsonData(service.uptimeTplTableDatas(getKv()));
	}
}
