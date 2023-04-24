package cn.rjtech.admin.cusordersum;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.util.JBoltListMap;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.CusOrderSum;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

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
        render("index.html");
    }

    /**
     * 数据源
     */
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

    /**
     * 获取表格
     */
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

        JBoltListMap<String, Date> dateMap = new JBoltListMap<>();

        while (DateUtil.compare(endDate, calendar.getTime()) >= 0) {
            // 年月 - 年-月-日
            dateMap.addItem(calendar.get(Calendar.YEAR) + StrUtil.UNDERLINE + (calendar.get(Calendar.MONTH) + 1), calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        LOG.info("{}", dateMap);
        set("dateMap", dateMap);
        
        render("year.html");
    }

}
