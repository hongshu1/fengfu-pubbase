package cn.rjtech.admin.modocbatch;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.kit.JBoltModelKit;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.momotask.MoMotaskService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MoMotask;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

@CheckPermission(PermissionKey.MO_DOCBATCH)
@UnCheckIfSystemAdmin
@Path(value = "/admin/modocbatch", viewPath = "/_view/admin/modocbatch")
public class MoDocBatchController extends BaseAdminController {


  @Inject
  private MoMotaskService service;
  @Inject
  private MoDocService moDocService;
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
    @UnCheck
    public void datas() {
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKv()));
    }

    @UnCheck
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
    keepPara();
    set("taskId", getLong(0));
    render("person_edit.html");
  }

  /**
   * 根据taskid获取表头日期信息
   */
  public void personEditHeaderDatas() {
    renderJsonData(service.personEditHeaderDatas(getKv()));
  }

  /**
   * 制造工单人员批量编辑数据源
   */
  @UnCheck
  public void getModocStaffEditorDatas() {
    renderJsonData(service.getModocStaffEditorDatas(getKv()));
  }

  /**
   * 编辑计划
   */
  public void editPlan() {
    keepPara();
    set("taskId", getLong(0));
    render("planform.html");
  }

  /**
   * 查看
   */
  public void personShow() {
    keepPara();
    set("taskId", getLong(0));
    render("personshow.html");
  }

  /**
   * 制造工单计划批量编辑数据源
   */
  @UnCheck
  public void getEditorialPlanDatas() {
    renderJsonData(service.getEditorialPlanDatas(getKv()));
  }

  /**
   * 编辑人员页面获取用户信息
   */
  @UnCheck
  public void getUserDatas() {
    renderJsonData(service.getUserDatas(getKv()));
  }

  /**
   * 编辑计划保存
   *
   * @param modoc
   */
  public void savePlan(String modoc) {
    ValidationUtils.notBlank(modoc, "参数为空!");
    List<Record> list = JBoltModelKit.getFromRecords(JSONArray.parseArray(modoc));
    renderJsonData(moDocService.savePlan(list));
  }

  /**
   * 编辑人员保存
   *
   * @param modoc
   */
  public void savePersonnel(String modoc) {
    ValidationUtils.notBlank(modoc, "参数为空!");
    List<Record> list = JBoltModelKit.getFromRecords(JSONArray.parseArray(modoc));
    renderJsonData(moDocService.savePersonnel(list));
  }


}