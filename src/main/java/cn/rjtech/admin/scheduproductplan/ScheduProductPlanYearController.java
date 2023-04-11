package cn.rjtech.admin.scheduproductplan;


import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ApsAnnualplanm;
import cn.rjtech.util.DateUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 生产计划排程 Controller
 * @ClassName: ScheduProductPlanYearController
 * @author: chentao
 * @date: 2023-03-30 11:26
 */
@CheckPermission(PermissionKey.NOME)
@UnCheckIfSystemAdmin
@Path(value = "/admin/scheduproductplanyear", viewPath = "/_view/admin/scheduproductplan")
public class ScheduProductPlanYearController extends BaseAdminController {

    @Inject
    private ScheduProductPlanYearService service;

    public void planyear() {
        render("planyear.html");
    }
    public void addview() {
        set("cplanorderno",get("cplanorderno"));
        set("icustomerid",get("icustomerid"));
        set("startyear",get("startyear"));
        render("planyear_add.html");
    }

    public void addviewparm() {
        set("startyear", DateUtils.formatDate(new Date(),"yyyy"));
        render("addviewparm.html");
    }




    public void planyearsum() {
        render("planyearsum.html");
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


    //-----------------------------------------------------------------年度生产计划排产-----------------------------------------------


    public void getCustomerList() {
        renderJson(service.getCustomerList());
    }

    /**
     * 作成计划
     */
    public void schedulingPlan() {
        renderJson(service.scheduPlanYear(getKv()));
    }
    /**
     * 保存计划
     */
    public void saveScheduPlanYear() {
        //renderJsonData(service.saveScheduPlanYear(getJBoltTable()));
    }
    /*public void saveScheduPlanYear(JBoltPara para) {
        renderJsonData(service.saveScheduPlanYear(para.getRawData()));
    }*/

    /**
     * 获取计划
     */
    public void getApsYearPlanList() {
        String cplanorderno = get("cplanorderno");
        Long icustomerid = getLong("icustomerid");
        String startYear = get("startyear");
        renderJsonData(service.getApsYearPlanList(cplanorderno,icustomerid,startYear,getKv()));
    }

    /**
     * 获取订单计划
     */
    public void getApsYearPlanMasterPage() {
        renderJsonData(service.getApsYearPlanMasterPage(getPageNumber(),getPageSize(),getKv()));
    }


    //-----------------------------------------------------------------年度生产计划汇总-----------------------------------------------

    /**
     * 获取年度生产计划汇总
     */
    public void getApsYearPlanSumPage() {
        renderJsonData(service.getApsYearPlanSumPage(getPageNumber(),getPageSize(),getKv()));
    }





















    /**
     * 查询排程计划
     */
    public void querySchedulingData() {
        JSONObject data = new JSONObject();

        //yyyy-MM 开始月份
        String startMonth = getKv().getStr("month");



        System.err.println("开始查询排程数据！");
        long start = System.currentTimeMillis();



        long end = System.currentTimeMillis();
        System.err.println("查询总时间 = " + (end - start));

        //data.put("schedulingDataList", schedulingDataList);

        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("data",data);
        dataMap.put("state","ok");
        renderJson(dataMap);
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