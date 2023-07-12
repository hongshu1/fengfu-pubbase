package cn.rjtech.admin.padworkregion;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.PadWorkRegion;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 系统管理-平板关联产线
 *
 * @ClassName: PadWorkRegionAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-03 16:31
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.PAD)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/padworkregion", viewPath = "/_view/admin/padworkregion")
public class PadWorkRegionAdminController extends BaseAdminController {

  @Inject
  private PadWorkRegionService service;

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
    renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getBoolean("isDefault"), getBoolean("isDeleted")));
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
    renderJson(service.save(getModel(PadWorkRegion.class, "padWorkRegion")));
  }

  /**
   * 编辑
   */
  public void edit() {
    PadWorkRegion padWorkRegion = service.findById(getLong(0));
    if (padWorkRegion == null) {
      renderFail(JBoltMsg.DATA_NOT_EXIST);
      return;
    }
    set("padWorkRegion", padWorkRegion);
    render("edit.html");
  }

  /**
   * 更新
   */
  public void update() {
    renderJson(service.update(getModel(PadWorkRegion.class, "padWorkRegion")));
  }

  /**
   * 批量删除
   */
  public void deleteByIds() {
    renderJson(service.deleteByIds(get("ids")));
  }

  /**
   * 删除
   */
  public void delete() {
    renderJson(service.deleteById(getLong(0)));
  }

  /**
   * 切换isDefault
   */
  public void toggleIsDefault() {
    renderJson(service.toggleBoolean(getLong(0), "isDefault"));
  }

  /**
   * 切换isDeleted
   */
  public void toggleIsDeleted() {
    renderJson(service.toggleBoolean(getLong(0), "isDeleted"));
  }

  @UnCheck
  public void options() {
    renderJsonData(service.getDatasOfSql(getKv()));
  }

}
