package cn.rjtech.admin.weekorderm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.WeekOrderM;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang3.StringUtils;

/**
 * 客户订单-周间客户订单
 *
 * @ClassName: WeekOrderMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 14:37
 */
@CheckPermission(PermissionKey.WEEK_ORDERM)
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@Path(value = "/admin/weekorderm", viewPath = "/_view/admin/weekorderm")
public class WeekOrderMAdminController extends BaseAdminController {

    @Inject
    private WeekOrderMService service;

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
        WeekOrderM addData = new WeekOrderM();
        set("weekOrderM", addData);
        set("mark", "ADD");
        render("add.html");
    }

    /**
     * 保存
     */
    public void saveTableSubmit() {
        renderJson(service.saveTableSubmit(getJBoltTable()));
    }

    /**
     * 编辑
     */
    public void edit() {
        Page<Record> datas = service.getAdminDatas(1, 1, Kv.by("iAutoId", get("iautoid")));
        ValidationUtils.notNull(datas, JBoltMsg.DATA_NOT_EXIST);
        ValidationUtils.isTrue(datas.getList().size() > 0, JBoltMsg.DATA_NOT_EXIST);
        set("weekOrderM", datas.getList().get(0));
        keepPara();
        render("edit.html");
    }

    /**
     * 批量删除
     */
    public void deleteByIds(@Para(value = "ids") String ids) {
        ValidationUtils.notBlank(ids, JBoltMsg.PARAM_ERROR);

        renderJson(service.deleteByIdS(ids));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 关闭
     */
    public void closeWeekOrder(@Para(value = "iautoid") String iAutoId) {
        if (StringUtils.isEmpty(iAutoId)) {
            renderFail(JBoltMsg.PARAM_ERROR);
            return;
        }
        renderJson(service.closeWeekOrder(iAutoId));
    }

    /**
     * 打开
     */
    public void open() {
        renderJson(service.open(get("iautoid")));
    }

    /**
     * 调整计划时间
     */
    public void updateCplanTime() {
        render("update_cplan_time.html");
    }

    /**
     * 调整计划时间数据源
     */
    public void updateCplanTimeDatas() {
        renderJsonData(service.updateCplanTimeDatas(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 调整计划时间下一步
     */
    public void updateCplanTimeNext() {
        render("update_cplan_time_next.html");
    }

    public void saveUpdateCplanTime() {
        renderJson(service.saveUpdateCplanTime(getJBoltTable()));
    }

    /**
     * 模板下载
     */
    @SuppressWarnings("unchecked")
    public void downloadTpl() {
        try {
            renderJxls("weekorderm.xlsx", Kv.by("rows", null), "周间客户订单.xlsx");
        } catch (Exception e) {
            ValidationUtils.error("模板下载失败");
        }
    }
}
