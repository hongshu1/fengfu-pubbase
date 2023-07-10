package cn.rjtech.admin.uptimeparam;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.render.JBoltByteFileType;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.model.momdata.UptimeParam;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;
import com.jfinal.upload.UploadFile;

import java.io.File;
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
   @CheckPermission(PermissionKey.UPTIMEPARAM_ADD)
	public void add() {
		render("add.html");
	}

   /**
	* 保存
	*/
	@Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
	@CheckPermission(PermissionKey.UPTIMEPARAM_ADD)
	public void save(@Para("uptimeParam")UptimeParam uptimeParam) {
		renderJson(service.save(uptimeParam));
	}

   /**
	* 编辑
	*/
   @CheckPermission(PermissionKey.UPTIMEPARAM_EDIT)
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
    @TxConfig(DataSourceConstants.MOMDATA)
	@CheckPermission(PermissionKey.UPTIMEPARAM_EDIT)
	public void update(@Para("uptimeParam")UptimeParam uptimeParam) {
		renderJson(service.update(uptimeParam));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
	@CheckPermission(PermissionKey.UPTIMEPARAM_DELETE)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

   /**
	* 切换isEnabled
	*/
	@Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
	public void toggleIsEnabled() {
	    renderJson(service.toggleBoolean(getLong(0),"isEnabled"));
	}

   /**
	* 切换isDeleted
	*/
	@Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}

	/**
	 * 导出
	 */
	@CheckPermission(PermissionKey.UPTIMEPARAM_EXPORT)
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

	/**
	 * 模板下载
	 */
	@SuppressWarnings("unchecked")
	public void downloadTpl() {
		try {
			renderJxls("uptimeparamtemplate.xlsx", Kv.by("rows", null), "稼动时间参数.xlsx");
		} catch (Exception e) {
			ValidationUtils.error("模板下载失败");
		}
	}

	/**
	 * 数据导入
	 */
	@SuppressWarnings("unchecked")
	@CheckPermission(PermissionKey.UPTIMEPARAM_IMPORT)
	public void importExcelData() {
		UploadFile uploadFile = getFile("file");
		ValidationUtils.notNull(uploadFile, "上传文件不能为空");

		File file = uploadFile.getFile();

		List<String> list = StrUtil.split(uploadFile.getOriginalFileName(), StrUtil.DOT);

		// 截取最后一个“.”之前的文件名，作为导入格式名
		String cformatName = list.get(0);

		String extension = list.get(1);

		ValidationUtils.equals(extension, JBoltByteFileType.XLSX.suffix, "系统只支持xlsx格式的Excel文件");
		renderJson(service.importExcelData(file, cformatName));
	}
}
