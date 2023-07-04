package cn.rjtech.admin.warehouse;

import cn.hutool.core.date.DateUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Warehouse;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.util.List;

/**
 * 仓库建模-仓库档案
 *
 * @ClassName: WarehouseAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-20 16:47
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.WAREHOUSE)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/warehouse", viewPath = "/_view/admin/warehouse")
public class WarehouseAdminController extends BaseAdminController {

  @Inject
  private WarehouseService service;

  /**
   * 首页
   */
  public void index() {
    render("index.html");
  }

  /**
   * 数据源
   */
  @UnCheck
  public void datas() {
    renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKv()));
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
  public void save() {
    renderJson(service.save(getModel(Warehouse.class, "warehouse")));
  }

  /**
   * 编辑
   */
  public void edit() {
    Warehouse warehouse = service.findById(getLong(0));
    if (warehouse == null) {
      renderFail(JBoltMsg.DATA_NOT_EXIST);
      return;
    }
    set("warehouse", warehouse);
    render("edit.html");
  }

  /**
   * 更新
   */
  public void update() {
    renderJson(service.update(getModel(Warehouse.class, "warehouse")));
  }

  /**
   * 批量删除
   */
  public void deleteByIds() {
    renderJson(service.deleteByBatchIds(get("ids")));
  }

  /**
   * 删除
   */
  public void delete() {
    renderJson(service.deleteById(getLong(0)));
  }

  /**
   * 切换是否库存预警
   */
  public void isStockWarnEnabled() {
    renderJson(service.toggleBoolean(getLong(0), "isStockWarnEnabled"));
  }

  /**
   * 切换是否启动
   */
  public void isEnabled() {
    renderJson(service.toggleBoolean(getLong(0), "isEnabled"));
  }

  /**
   * 切换isDeleted
   */
  public void toggleIsDeleted() {
    renderJson(service.toggleBoolean(getLong(0), "isDeleted"));
  }

  /**
   * 仓库下拉信息查询
   */
  @UnCheck
  public void list() {
    renderJsonData(service.list(getKv()));
  }

  /**
   * Excel模板下载
   */
  @SuppressWarnings("unchecked")
  public void downloadTpl() throws Exception {
    renderJxls("warehouse_import.xlsx", Kv.by("rows", null), "仓库档案导入模板.xlsx");
  }


  /**
   * 导入
   */
  public void importExcel() {
    String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
    UploadFile file = getFile("file", uploadPath);
    if (notExcel(file)) {
      renderJsonFail("请上传excel文件");
      return;
    }
    renderJson(service.importExcelData(file.getFile()));
  }

  /**
   * 导出
   */
  @SuppressWarnings("unchecked")
  public void dataExport() throws Exception {
    List<Record> rows = service.list(getKv());

    renderJxls("warehouse.xlsx", Kv.by("rows", rows), "仓库列表_" + DateUtil.today() + ".xlsx");
  }

  @UnCheck
  public void options() {
    renderJsonData(service.options());
  }

  public void importExcelClass() {
    String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
    UploadFile file = getFile("file", uploadPath);
    if (notExcel(file)) {
      renderJsonFail("请上传excel文件");
      return;
    }
    renderJson(service.importExcelClass(file.getFile()));
  }

  /**
   * 导入模板下载
   */
  public void templateDownload() throws Exception {
    renderJxls("warehouse_import.xlsx", Kv.by("rows", null), "仓库档案导入模板.xlsx");
  }
}
