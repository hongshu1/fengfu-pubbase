package cn.rjtech.admin.scheduproductplan;


import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ApsAnnualplanm;
import cn.rjtech.util.DateUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Kv;

import java.util.*;

/**
 * 生产计划排程 Controller
 * @ClassName: ScheduProductPlanMonthController
 * @author: chentao
 * @date: 2023-03-30 11:26
 */
@CheckPermission(PermissionKey.NOME)
@UnCheckIfSystemAdmin
@Path(value = "/admin/scheduproductplanmonth", viewPath = "/_view/admin/scheduproductplan")
public class ScheduProductPlanMonthController extends BaseAdminController {

    @Inject
    private ScheduProductPlanMonthService service;

    public void planmonth() {
        render("planmonth.html");
    }
    public void addview() {
        set("cplanorderno",get("cplanorderno"));
        set("icustomerid",get("icustomerid"));
        set("startyear",get("startyear"));
        render("planyear_add.html");
    }

    public void addviewparm() {
        set("startyear", DateUtils.formatDate(new Date(),"yyyy"));
        render("planyearparm.html");
    }



    public void planmonthsum() {
        String startdate = get("startdate");
        String enddate = get("enddate");
        String cworkname = get("cworkname");
        String cinvcode = get("cinvcode");
        String cinvcode1 = get("cinvcode1");
        String cinvname1 = get("cinvname1");
        set("startdate",startdate);
        set("enddate",enddate);
        set("cworkname",cworkname);
        set("cinvcode",cinvcode);
        set("cinvcode1",cinvcode1);
        set("cinvname1",cinvname1);


        List<String> list = new ArrayList<>();
        list.add("qty1");
        list.add("qty2");
        list.add("qty3");

        set("collist", list);
        //service.getApsMonthPlanSumPage(getPageNumber(),getPageSize(),getKv());
        render("planmonthsum.html");
    }


    /**
     * 数据源
     */
    public void datas() {
        renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKeywords()));
    }

    /**
     * 新增
     */
    public void add() {
        render("add.html");
    }

    /**
     * 编辑
     */
    public void edit() {
        ApsAnnualplanm apsAnnualplanm=service.findById(getLong(0));
        if(apsAnnualplanm == null){
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("apsAnnualplanm",apsAnnualplanm);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(ApsAnnualplanm.class, "apsAnnualplanm")));
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
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 切换toggleIsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleIsDeleted(getLong(0)));
    }


    //-----------------------------------------------------------------月周生产计划排产-----------------------------------------------


    public void getApsWeekscheduleList() {
        renderJsonData(service.getApsWeekscheduleList());
    }

    /**
     * 作成计划
     */
    public void scheduPlanMonth() {
        //排产层级
        int level = 1;
        //截止日期
        String endDate = "2023-06-21";
        renderJson(service.scheduPlanMonth(level,endDate));
    }


    /**
     * 查看计划
     */
    public void getScheduPlanMonthList() {
        //排产纪录id
        Long iWeekScheduleId = null;
        renderJson(service.getScheduPlanMonthList(iWeekScheduleId));
    }



    //-----------------------------------------------------------------月周生产计划汇总-----------------------------------------------


    /**
     * 获取月周生产计划汇总
     */
    public void getApsMonthPlanSumPage() {
        renderJsonData(service.getApsMonthPlanSumPage(getPageNumber(),getPageSize(),getKv()));
    }













    /**
     * 锁定计划
     */
    public void locking() {
        //renderJson(service.lockingPlan(getKv()));
    }

    /**
     * 取消锁定计划
     */
    public void lockingCancel() {
        //renderJson(service.lockingCancelPlan(getKv()));
    }

    /**
     * 获取最新BOM
     */
    public void refreshBOM() {
        //renderJson(service.refreshBOM(getKv()));
    }

    /**
     *计划选择
     */
    public void selectaprm() {
        set("month",get("month"));
        render("selectaprm.html");
    }



}