package cn.rjtech.admin.period;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Period;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;

import java.util.ArrayList;

/**
 * 期间管理 Controller
 *
 * @ClassName: PeriodAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-09-16 09:33
 */
@CheckPermission(PermissionKey.PERIOD)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/period", viewPath = "/_view/admin/period")
public class PeriodAdminController extends BaseAdminController {

    @Inject
    private PeriodService service;
    @Inject
    private DictionaryService dictionaryService;
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
        Period period = service.findById(useIfPresent(getLong(0)));
        ValidationUtils.notNull(period, JBoltMsg.DATA_NOT_EXIST);
        set("dstarttime", JBoltDateUtil.format(period.getDstarttime(), "yyyy-MM"));
        set("dendtime", JBoltDateUtil.format(period.getDendtime(), "yyyy-MM"));
        set("period", period);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        Period period = useIfValid(Period.class, "period");
        Kv kv = getKv();
        if(kv.getStr("dstarttime")!=null && kv.getStr("dendtime")!=null) {
            //开始年月
            String str = kv.getStr("dstarttime") + "-01";
            period.setDstarttime(JBoltDateUtil.getDate(str));
            //截止年月
            String str1 = kv.getStr("dendtime") + "-01";
            period.setDendtime(JBoltDateUtil.getDate(str1));
        }
        renderJson(service.save(period));
    }

    /**
     * 更新
     */
    public void update() {
        Period period = useIfValid(Period.class, "period");
        Kv kv = getKv();
        if(kv.getStr("dstarttime")!=null && kv.getStr("dendtime")!=null) {
            //开始年月
            String str = kv.getStr("dstarttime") + "-01";
            period.setDstarttime(JBoltDateUtil.getDate(str));
            //截止年月
            String str1 = kv.getStr("dendtime") + "-01";
            period.setDendtime(JBoltDateUtil.getDate(str1));
        }
        renderJson(service.update(period));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 切换toggleIsenabled
     */
    public void toggleIsenabled() {
        renderJson(service.toggleIsenabled(getLong(0)));
    }
    /**
     * 下拉框联动数据
     */
    public void selectBudgetType(){
        Kv kv = getKv();
        String str = kv.getStr("key");
        if (kv.getStr("key") == null) {
            renderJsonData(CollUtil.empty(ArrayList.class));
        }else {
            if (Integer.parseInt(str)==1) {
                renderJsonData(dictionaryService.getOptionListByTypeKey("expense_budget_type"));
            }
            if (Integer.parseInt(str)==2){
                renderJsonData(dictionaryService.getOptionListByTypeKey("investment_budget_type"));
            }
        }

    }
}
