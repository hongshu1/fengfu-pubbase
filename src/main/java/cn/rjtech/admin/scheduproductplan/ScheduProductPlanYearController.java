package cn.rjtech.admin.scheduproductplan;


import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
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
@Path(value = "/admin/scheduproductplanyear", viewPath = "/_view/admin/scheduproductplanyear")
public class ScheduProductPlanYearController extends BaseAdminController {

    @Inject
    private ScheduProductPlanYearService service;

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


    /**
     * 生成排程计划
     */
    public void schedulingPlan() {
        //TODO: 将订单计划处理更新到排程来源表
        //service.soUpdScheduSource(getKv());
        renderJson(service.scheduPlanYear(getKv()));
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
     * 保存计划变更数
     */
    public void save() {
        String dataStr = get("datas");
        JSONArray dataJSONArr = JSONArray.parseArray(dataStr);
        //renderJson(service.savePlan(dataJSONArr));
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