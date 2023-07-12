package cn.rjtech.admin.warehouse;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.render.JBoltByteFileType;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Warehouse;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.io.File;
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
  @CheckPermission(PermissionKey.WAREHOUSE_ADD)
  public void add() {
    set("warehouse", service.getWarehouseCode());
    render("add.html");
  }

  /**
   * 保存
   */
  @CheckPermission(PermissionKey.WAREHOUSE_ADD)
  public void save() {
    renderJson(service.save(getModel(Warehouse.class, "warehouse")));
  }

  /**
   * 编辑
   */
  @CheckPermission(PermissionKey.WAREHOUSE_EDIT)
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
  @CheckPermission(PermissionKey.WAREHOUSE_EDIT)
  public void update() {
    renderJson(service.update(getModel(Warehouse.class, "warehouse")));
  }

  /**
   * 批量删除
   */
  @CheckPermission(PermissionKey.WAREHOUSE_DELETE)
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
   * 导出
   */
  @SuppressWarnings("unchecked")
  @CheckPermission(PermissionKey.WAREHOUSE_EXPORT)
  public void dataExport() throws Exception {
    List<Record> rows = service.list(getKv());

    renderJxls("warehouse.xlsx", Kv.by("rows", rows), "仓库列表_" + DateUtil.today() + ".xlsx");
  }

  @UnCheck
  public void options() {
    renderJsonData(service.options());
  }

  /**
   * 数据导入
   */
  @SuppressWarnings("unchecked")
  @CheckPermission(PermissionKey.WAREHOUSE_IMPORT)
  public void importExcelClass() {
    UploadFile uploadFile = getFile("file");
    ValidationUtils.notNull(uploadFile, "上传文件不能为空");

    File file = uploadFile.getFile();

    List<String> list = StrUtil.split(uploadFile.getOriginalFileName(), StrUtil.DOT);

    // 截取最后一个“.”之前的文件名，作为导入格式名
    String cformatName = list.get(0);

    String extension = list.get(1);

    ValidationUtils.equals(extension, JBoltByteFileType.XLSX.suffix, "系统只支持xlsx格式的Excel文件");
    renderJson(service.importExcelData(file));
  }

  /**
   * 导入模板下载
   */
  public void templateDownload() throws Exception {
    renderJxls("warehouse_import.xlsx", Kv.by("rows", null), "仓库档案导入模板.xlsx");
  }
}
