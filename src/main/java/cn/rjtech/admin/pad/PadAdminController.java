package cn.rjtech.admin.pad;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Pad;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;

/**
 * 系统配置-平板端设置
 *
 * @ClassName: PadAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-03 17:17
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.PAD)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/pad", viewPath = "/_view/admin/pad")
public class PadAdminController extends BaseAdminController {

  @Inject
  private PadService service;

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
    renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
  }

  /**
   * 新增
   */
  public void add() {
    if (getOrgId() == null) {
      renderFail("未选择组织!");
    } else {
      render("add.html");
    }
  }

  /**
   * 保存
   */
  public void save() {
    renderJson(service.save(getModel(Pad.class, "pad")));
  }

  /**
   * 编辑
   */
  public void edit() {
    Pad pad = service.findById(getLong(0));
    if (pad == null) {
      renderFail(JBoltMsg.DATA_NOT_EXIST);
      return;
    }
    set("pad", pad);
    render("edit.html");
  }

  /**
   * 更新
   */
  public void update() {
    renderJson(service.update(getModel(Pad.class, "pad")));
  }

  /**
   * 删除
   */
  public void delete() {
    renderJson(service.deleteByid(getLong(0)));
  }

  /**
   * 保存新增或编辑数据
   */
  public void formSubmit() {
    JBoltTable jBoltTable = getJBoltTable();
    Pad pad = jBoltTable.getFormBean(Pad.class, "pad");

    Ret result = null;

    if (pad.getIAutoId() != null) {

      result = service.updateForm(pad, jBoltTable);

    } else {
      result = service.saveForm(pad, jBoltTable);
    }

    renderJson(result);
  }
}
