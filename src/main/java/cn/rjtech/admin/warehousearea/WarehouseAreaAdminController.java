package cn.rjtech.admin.warehousearea;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.render.JBoltByteFileType;
import cn.rjtech.model.momdata.WarehouseArea;
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
 * 库区档案 Controller
 *
 * @ClassName: WarehouseAreaAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-02 20:43
 */
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@CheckPermission(PermissionKey.WAREHOUSE_AREA)
@Path(value = "/admin/warehousearea", viewPath = "/_view/admin/warehousearea")
public class WarehouseAreaAdminController extends JBoltBaseController {

  @Inject
  private WarehouseAreaService service;

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

  @UnCheck
  public void list() {
    renderJsonData(service.list(getKv()));
  }

  /**
   * 新增
   */
  @CheckPermission(PermissionKey.WAREHOUSE_AREA_ADD)
  public void add() {
    set("warehousearea", service.getWarehouseAreaCode());
    render("add.html");
  }

  /**
   * 编辑
   */
  @CheckPermission(PermissionKey.WAREHOUSE_AREA_EDIT)
  public void edit() {
    WarehouseArea warehouseArea = service.findById(getLong(0));
    if (warehouseArea == null) {
      renderFail(JBoltMsg.DATA_NOT_EXIST);
      return;
    }
    set("warehouseArea", warehouseArea);
    render("edit.html");
  }

  /**
   * 保存
   */
  @CheckPermission(PermissionKey.WAREHOUSE_AREA_ADD)
  public void save() {
    renderJson(service.save(getModel(WarehouseArea.class, "warehouseArea")));
  }

  /**
   * 更新
   */
  @CheckPermission(PermissionKey.WAREHOUSE_AREA_EDIT)
  public void update() {
    renderJson(service.update(getModel(WarehouseArea.class, "warehouseArea")));
  }

  /**
   * 批量删除
   */
  @CheckPermission(PermissionKey.WAREHOUSE_AREA_DELETE)
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
   * 导出数据
   */
  @SuppressWarnings("unchecked")
  @CheckPermission(PermissionKey.WAREHOUSE_AREA_EXPORT)
  public void dataExport() throws Exception {
    renderJxls("warehousearea.xlsx", Kv.by("rows", service.list(getKv())), "库区列表_" + DateUtil.today() + ".xlsx");
  }

  /**
   * Excel模板下载
   */
  @SuppressWarnings("unchecked")
  public void downloadTpl() throws Exception {
    renderJxls("warehousearea_import.xlsx", Kv.by("rows", null), "库区档案导入模板.xlsx");
  }

  /**
   * 库区打印数据
   */
  @CheckPermission(PermissionKey.WAREHOUSE_AREA_PRINT)
  public void printData() {
    renderJsonData(service.getPrintDataCheck(getKv()));
  }

  @UnCheck
  public void options() {
    renderJsonData(service.options(getKv()));
  }

  /**
   * 库区档案excel导入
   */
  @SuppressWarnings("unchecked")
  @CheckPermission(PermissionKey.WAREHOUSE_AREA_IMPORT)
  public void importExcelClass() {
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
