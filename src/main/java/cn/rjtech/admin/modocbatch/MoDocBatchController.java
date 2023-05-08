package cn.rjtech.admin.modocbatch;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.momotask.MoMotaskService;
import cn.rjtech.admin.scheduproductplan.ScheduProductPlanMonthService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Department;
import cn.rjtech.model.momdata.MoMotask;
import cn.rjtech.util.DateUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
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
        renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv()));
    }


    /**
     * 编辑人员
     */
    public void editPerson() {
        MoMotask moMotask=service.findById(getLong(0));
        if(moMotask == null){
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        Department byId = departmentService.findById(moMotask.getIDepartmentId());
        moMotask.setDeptname(byId.getCDepName());
        set("moMotask",moMotask);

        //表头日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //星期
        List<String> betweenDate = ScheduProductPlanMonthService.getBetweenDate(sdf.format(moMotask.getDBeginDate()), sdf.format(moMotask.getDEndDate()));
        set("daylist",betweenDate);
        List<String> weeklist = new ArrayList<>();
        for (String s : betweenDate) {
            String weekDay = DateUtils.formatDate(DateUtils.parseDate(s),"E");
                weeklist.add(weekDay);
        }
        //工单存货产线列表
        Page<Record> docPage = service.findDoc(getPageNumber(), getPageSize(), moMotask.getIAutoId());
        //设备和工序
        for (Record record : docPage.getList()) {
            String docId = record.getStr("iautoid");
            List<Record> rutingConfig = service.findRoutingConfig(docId);
            // 工序对应的人员

        }
        render("_form.html");
    }
    /**
     * 编辑计划
     */
    public void editPlan() {
        MoMotask moMotask=service.findById(getLong(0));
        if(moMotask == null){
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        Department byId = departmentService.findById(moMotask.getIDepartmentId());
        moMotask.setDeptname(byId.getCDepName());
        set("moMotask",moMotask);

        //拼日期  weeklist  colnamelist colname2list
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //星期
        List<String> betweenDate = ScheduProductPlanMonthService.getBetweenDate(sdf.format(moMotask.getDBeginDate()), sdf.format(moMotask.getDEndDate()));
        set("daylist",betweenDate);
        List<String> weeklist = new ArrayList<>();
        List<String> classlist = new ArrayList<>();
        for (String s : betweenDate) {
            String weekDay = DateUtils.formatDate(DateUtils.parseDate(s),"E");
            weeklist.add(weekDay);
            classlist.add("1S");
            classlist.add("2S");
            classlist.add("3S");
        }
        set("weeklist",weeklist);
        set("classlist",classlist);
        render("planform.html");
    }

    public void getPlanPage(){
        renderJsonData(service.getPlanPage(getPageNumber(),getPageSize(),getKv()));
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(MoMotask.class, "moMotask")));
    }

}
