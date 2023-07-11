package cn.rjtech.admin.cusordersum;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.CusOrderSum;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;

import java.text.ParseException;
import java.util.*;


/**
 * 客户订单-客户计划汇总
 *
 * @ClassName: CusOrderSumAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-14 16:20
 */
@CheckPermission(PermissionKey.CUSORDER_SUM)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/cusordersum", viewPath = "/_view/admin/cusordersum")
public class CusOrderSumAdminController extends BaseAdminController {

    @Inject
    private CusOrderSumService service;

    /**
     * 首页
     */
    public void index() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        set("beginDate", calendar.getTime());
        int lastDayOfMonth = DateUtil.getLastDayOfMonth(DateUtil.date());
        calendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
        set("endDate", calendar.getTime());
        render("index.html");
    }

    /**
     * 数据源
     */
    @UnCheck
    public void datas() {
        try {
            renderJsonData(service.findCusOrderSum(getPageNumber(), getPageSize(), getKv()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        renderJson(service.save(getModel(CusOrderSum.class, "cusOrderSum")));
    }

    /**
     * 编辑
     */
    public void edit() {
        CusOrderSum cusOrderSum = service.findById(getLong(0));
        if (cusOrderSum == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("cusOrderSum", cusOrderSum);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(CusOrderSum.class, "cusOrderSum")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }

    public void algorithmSum() {
        renderJson(service.algorithmSum());
    }

    /**
     * 获取表格
     */
    @UnCheck
    public void getYear() {
        Kv kv = getKv();
        
        Date beginDate = kv.getDate("beginDate");
        Date endDate = kv.getDate("endDate");
        
        ValidationUtils.notNull(beginDate, "开始时间不能为空");
        ValidationUtils.notNull(endDate, "结束时间不能为空");
        ValidationUtils.isTrue(DateUtil.compare(endDate, beginDate) >= 0, "结束时间必须大于等于开始时间");

        // 保留参数
        keepPara();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate);

        Map<String, List<Date>> dateMap = new LinkedHashMap<>();

        while (DateUtil.compare(endDate, calendar.getTime()) >= 0) {
            // 年月 - 年-月-日
            String key = calendar.get(Calendar.YEAR) + StrUtil.UNDERLINE + (calendar.get(Calendar.MONTH) + 1);
            List<Date> dateList = dateMap.containsKey(key) ? dateMap.get(key) : new ArrayList<>();
            dateList.add(calendar.getTime());
            dateMap.put(key, dateList);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        LOG.info("{}", dateMap);
        set("dateMap", dateMap);
        
        render("year.html");
    }

    /**
     * 导出
     */
    @CheckPermission(PermissionKey.CUSORDERSUM_EXPORT)
    public void export()
    {

    }
}
