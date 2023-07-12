package cn.rjtech.admin.uptimetplm;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.render.JBoltByteFileType;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.model.momdata.UptimeTplM;
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
 * 稼动时间建模-稼动时间模板主表
 * @ClassName: UptimeTplMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 19:20
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/uptimeTplM", viewPath = "/_view/admin/uptimetplm")
@CheckPermission(PermissionKey.UPTIME_TPL)
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
   @CheckPermission(PermissionKey.UPTIMETPLM_ADD)
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
   @CheckPermission(PermissionKey.UPTIMETPLM_EDIT)
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
	 * 切换isEnabled
	 */
	@Before(Tx.class)
	@TxConfig(DataSourceConstants.MOMDATA)
	public void toggleIsEnabled() {
		renderJson(service.toggleBoolean(getLong(0),"isEnabled"));
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
	@CheckPermission(PermissionKey.UPTIMETPLM_DELETE)
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}


	/**
	 * 保存
	 */
	@CheckPermission(PermissionKey.UPTIMETPLM_SUBMIT)
	public void submitAll()
	{
		renderJson(service.submitAll(getKv()));
	}

	/**
	 * 导出
	 */
    @SuppressWarnings("unchecked")
	@CheckPermission(PermissionKey.UPTIMETPLM_EXPORT)
	public void exportExcelAll() throws Exception {
		Page<Record> recordPage = service.getAdminDatas(1, 100000, getKv());
		List<Record> rows = recordPage.getList();
		if (!rows.isEmpty()) {
			rows = rows.stream().peek(row -> {
				row.put("isenabled", row.getBoolean("isenabled") ? "是" : "否");
				row.put("dcreatetime", DateUtil.format(row.getDate("dcreatetime"), "yyyy-MM-dd HH:mm"));
            }).collect(Collectors.toList());
		}
		renderJxls("uptimetplm.xlsx", Kv.by("rows", recordPage.getList()), "稼动时间模板导出_" + DateUtil.today() + ".xlsx");
	}

	/**
	 * 模板下载
	 */
	@SuppressWarnings("unchecked")
	public void downloadTpl() throws Exception {
        renderJxls("uptimetplmtemplate.xlsx", Kv.by("rows", null), "稼动时间模板.xlsx");
	}

	/**
	 * 数据导入
	 */
	@CheckPermission(PermissionKey.UPTIMETPLM_IMPORT)
	public void importExcelData() {
		UploadFile uploadFile = getFile("file");
		ValidationUtils.notNull(uploadFile, "上传文件不能为空");

		File file = uploadFile.getFile();

		List<String> list = StrUtil.split(uploadFile.getOriginalFileName(), StrUtil.DOT);
		ValidationUtils.equals(list.get(1), JBoltByteFileType.XLSX.suffix, "系统只支持xlsx格式的Excel文件");
        
		renderJson(service.importExcelData(file));
	}
    
}
