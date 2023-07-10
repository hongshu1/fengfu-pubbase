package cn.rjtech.admin.proditem;

import cn.hutool.core.date.DateUtil;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.admin.prodparam.ProdParamService;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.util.Util;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ProdItem;
import com.jfinal.upload.UploadFile;

import java.util.List;

/**
 * 生产表单建模-生产项目
 * @ClassName: ProdItemAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-26 15:25
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/prodItem", viewPath = "/_view/admin/proditem")
@CheckPermission(PermissionKey.PRODITEM)
public class ProdItemAdminController extends BaseAdminController {

	@Inject
	private ProdItemService service;

	@Inject
	private ProdParamService prodparamService;
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
		renderJsonData(service.pageList(getKv()));

		//renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isDeleted")));
	}

   /**
	* 新增
	*/
   @CheckPermission(PermissionKey.PRODITEM_ADD)
	public void add() {
		render("add.html");
	}

   /**
	* 保存
	*/
	@Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
	@CheckPermission(PermissionKey.PRODITEM_ADD)
	public void save(@Para("prodItem")ProdItem prodItem) {
		renderJson(service.save(prodItem));
	}

   /**
	* 编辑
	*/
   @CheckPermission(PermissionKey.PRODITEM_EDIT)
   public void edit() {
		ProdItem prodItem=service.findById(getLong(0));
		if(prodItem == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("prodItem",prodItem);
		render("edit.html");
	}

    @UnCheck
	public void options() {
		renderJsonData(service.options());
	}
   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
	@CheckPermission(PermissionKey.PRODITEM_EDIT)
	public void update(@Para("prodItem")ProdItem prodItem) {
		renderJson(service.update(prodItem));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
	@CheckPermission(PermissionKey.PRODITEM_DELETE)
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

   /**
	* 切换isDeleted
	*/
	@Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}

	@SuppressWarnings("unchecked")
	@CheckPermission(PermissionKey.PRODITEM_EXPORT)
	public void exportExcelByIds() throws Exception {
		String ids = get("ids");
		if (notOk(ids)) {
			renderJsonFail("未选择有效数据，无法导出");
			return;
		}
		List<Record> data = service.list(Kv.create().setIfNotBlank("ids", Util.getInSqlByIds(ids)));
		if (notOk(data)) {
			renderJsonFail("无有效数据导出");
			return;
		}
		renderJxls("proditem.xlsx", Kv.by("rows", data), "生产项目(选中导出)_" + DateUtil.today() + ".xlsx");
	}

	@SuppressWarnings("unchecked")
	@CheckPermission(PermissionKey.PRODITEM_EXPORT)
	public void exportExcelAll() throws Exception {
		List<Record> rows = service.list(getKv());
		if (notOk(rows)) {
			renderJsonFail("无有效数据导出");
			return;
		}
		renderJxls("proditem.xlsx", Kv.by("rows", rows), "生产项目" + DateUtil.today() + ".xlsx");
	}

	@CheckPermission(PermissionKey.PRODITEM_IMPORT)
	public void importExcelClass() {
		String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
		UploadFile file = getFile("file", uploadPath);
		if (notExcel(file)) {
			renderJsonFail("请上传excel文件");
			return;
		}
		renderJson(service.importExcelClass(file.getFile()));
	}
}
