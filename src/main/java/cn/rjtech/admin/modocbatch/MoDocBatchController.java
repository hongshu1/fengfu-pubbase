package cn.rjtech.admin.modocbatch;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.momotask.MoMotaskService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Department;
import cn.rjtech.model.momdata.MoMotask;
import cn.rjtech.util.DateUtils;
import cn.rjtech.util.Util;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/modocbatch", viewPath = "/_view/admin/modocbatch")
public class MoDocBatchController extends BaseAdminController {


  @Inject
  private MoMotaskService service;
  @Inject
  private DepartmentService departmentService;

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
   * 编辑计划
   */
  public void editPlan() {
    set("editplanviewdatas", service.getOpenEditPlanViewDatas(getLong(0)));
    render("planform.html");
  }

  public void getPlanPage() {
    renderJsonData(service.getPlanPage(getKv()));
  }

  /**
   * 保存
   */
  public void save() {
    renderJson(service.save(getModel(MoMotask.class, "moMotask")));
  }

  /**
   * 人员编辑
   */
  public void personEdit() {
    Kv kv = getKv();
    set("editplanviewdatas", service.getOpenEditPlanViewDatas(kv.getLong("taskid")));
    keepPara();
    render("person_edit.html");
//    renderJsonData(service.getOpenEditPlanViewDatas(kv.getLong("taskid")));
  }

  /**
   * 制造工单人员批量编辑获取数据源
   */
  public void getModocStaffEditorDatas() {
    renderJsonData(service.getModocStaffEditorDatas(getKv()));
  }


}
