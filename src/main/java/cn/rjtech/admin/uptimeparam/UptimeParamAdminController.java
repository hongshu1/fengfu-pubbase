package cn.rjtech.admin.uptimeparam;

import cn.hutool.core.date.DateUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.UptimeParam;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;

import java.util.List;
import java.util.stream.Collectors;

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

	/**
	 * 导出
	 */
	public void exportExcelAll() throws Exception {
		Page<Record> recordPage = service.getAdminDatas(1, 100000, getKv());
		List<Record> rows = recordPage.getList();
		if (!rows.isEmpty()) {
			rows = rows.stream().map(row -> {
				row.put("isenabled", row.getBoolean("isenabled") ? "是" : "否");
				row.put("dcreatetime", DateUtil.format(row.getDate("dcreatetime"), "yyyy-MM-dd HH:mm"));
				return row;
			}).collect(Collectors.toList());
		}
		renderJxls("uptimeparam.xlsx", Kv.by("rows", recordPage.getList()), "稼动时间参数导出_" + DateUtil.today() + ".xlsx");
	}
}
