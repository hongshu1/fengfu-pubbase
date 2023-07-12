package cn.rjtech.admin.warehouseposition;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.render.JBoltByteFileType;
import cn.rjtech.model.momdata.WarehousePosition;
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
 * 库位档案 Controller
 *
 * @ClassName: WarehousePositionAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-02 20:45
 */
@CheckPermission(PermissionKey.WAREHOUSE_POSITION)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/warehouseposition", viewPath = "/_view/admin/warehouseposition")
public class WarehousePositionAdminController extends JBoltBaseController {

  @Inject
  private WarehousePositionService service;

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
    renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKv()));
  }

  /**
   * 新增
   */
  @CheckPermission(PermissionKey.WAREHOUSE_POSITION_ADD)
  public void add() {
    set("warehousePosition", service.getWarehousePositionCode());
    render("add.html");
  }

  /**
   * 编辑
   */
  @CheckPermission(PermissionKey.WAREHOUSE_POSITION_EDIT)
  public void edit() {
    WarehousePosition warehousePosition = service.findById(getLong(0));
    if (warehousePosition == null) {
      renderFail(JBoltMsg.DATA_NOT_EXIST);
      return;
    }
    set("warehousePosition", warehousePosition);
    render("edit.html");
  }

  /**
   * 保存
   */
  @CheckPermission(PermissionKey.WAREHOUSE_POSITION_ADD)
  public void save() {
    renderJson(service.save(getModel(WarehousePosition.class, "warehousePosition")));
  }

  /**
   * 更新
   */
  @CheckPermission(PermissionKey.WAREHOUSE_POSITION_EDIT)
  public void update() {
    renderJson(service.update(getModel(WarehousePosition.class, "warehousePosition")));
  }

  /**
   * 批量删除
   */
  @CheckPermission(PermissionKey.WAREHOUSE_POSITION_DELETE)
  public void deleteByIds() {
    renderJson(service.deleteByBatchIds(get("ids")));
  }

  /**
   * 切换toggleIsdeleted
   */
  public void toggleIsdeleted() {
    renderJson(service.toggleIsdeleted(getLong(0)));
  }

  /**
   * 切换toggleIsenabled
   */
  public void toggleIsenabled() {
    renderJson(service.toggleIsenabled(getLong(0)));
  }

  /**
   * 数据源
   * 查询全部启用数据
   */
  public void dataList() {
    Long iwarehouseid = getLong("iwarehouseid");
    renderJsonData(service.dataList(true, iwarehouseid));
  }

  /**
   * 导出数据
   */
  @SuppressWarnings("unchecked")
  @CheckPermission(PermissionKey.WAREHOUSE_POSITION_EXPORT)
  public void dataExport() throws Exception {
    List<Record> rows = service.list(getKv());
    renderJxls("warehouseposition.xlsx", Kv.by("rows", rows), "库位列表_" + DateUtil.today() + ".xlsx");
  }

  /**
   * Excel模板下载
   */
  @SuppressWarnings("unchecked")
  public void downloadTpl() throws Exception {
    renderJxls("warehouseposition_import.xlsx", Kv.by("rows", null), "库位档案导入模板.xlsx");
  }

  /**
   * 数据导入
   */
  @CheckPermission(PermissionKey.WAREHOUSE_POSITION_IMPORT)
  public void importExcelData() {
    UploadFile uploadFile = getFile("file");
    ValidationUtils.notNull(uploadFile, "上传文件不能为空");

    File file = uploadFile.getFile();

    List<String> list = StrUtil.split(uploadFile.getOriginalFileName(), StrUtil.DOT);
    ValidationUtils.equals( list.get(1), JBoltByteFileType.XLSX.suffix, "系统只支持xlsx格式的Excel文件");
    
    renderJson(service.importExcelData(file));
  }

  @UnCheck
  public void options() {
    renderJsonData(service.options(getKv()));
  }

  /**
   * 打印
   */
  @UnCheck
  public void selectPrint() {
    renderJson(service.selectPrint(null));
  }

  /**
   * 库区打印数据
   */
  @CheckPermission(PermissionKey.WAREHOUSE_POSITION_PRINT)
  public void printData() {
    renderJsonData(service.getPrintDataCheck(getKv()));
  }
}
