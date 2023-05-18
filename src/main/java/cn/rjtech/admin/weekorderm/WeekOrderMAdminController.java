package cn.rjtech.admin.weekorderm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.WeekOrderM;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

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
    public void save(String mark) {
        JBoltTable jBoltTable = getJBoltTable();
        if (StringUtils.isEmpty(mark)) {
            renderFail(JBoltMsg.PARAM_ERROR);
        } else if ("ADD".equals(mark)) {
            //添加
            renderJson(service.save(jBoltTable));
        } else if ("EDIT".equals(mark)) {
            //更新
            renderJson(service.update(jBoltTable));
        }
    }

    /**
     * 编辑
     */
    public void edit() {
        List<Record> weekOrderM = service.findByIdToShow(getLong(0));
        if (weekOrderM.get(0) == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("weekOrderM", weekOrderM.get(0));
        set("mark", "EDIT");
        render("edit.html");
    }

    /**
     * 查看
     */
    public void showData() {
        List<Record> weekOrderM = service.findByIdToShow(getLong(0));
        if (weekOrderM.get(0) == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        
        // 订单状态：1. 已保存 2. 待审批 3. 已审批 4. 审批不通过 5. 已发货 6. 已核对 7. 已关闭
        switch (weekOrderM.get(0).getInt("iorderstatus")) {
            case 1:
                weekOrderM.get(0).set("iorderstatus", "已保存");
                break;
            case 2:
                weekOrderM.get(0).set("iorderstatus", "待审批");
                break;
            case 3:
                weekOrderM.get(0).set("iorderstatus", "已审批");
                break;
            case 4:
                weekOrderM.get(0).set("iorderstatus", "审批不通过");
                break;
            case 5:
                weekOrderM.get(0).set("iorderstatus", "已发货");
                break;
            case 6:
                weekOrderM.get(0).set("iorderstatus", "已核对");
                break;
            case 7:
                weekOrderM.get(0).set("iorderstatus", "已关闭");
                break;
            default:
                break;
        }
        
        set("weekOrderM", weekOrderM.get(0));
        set("mark", "SHOW");
        render("showWeekOrder.html");
    }


    /**
     * 更新
     */
    public void update() {
//		renderJson(service.update(getModel(WeekOrderM.class, "weekOrderM")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByIdS(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 审批
     */
    public void approve(String iAutoId, Integer mark) {
        if (StringUtils.isEmpty(iAutoId)) {
            renderFail(JBoltMsg.PARAM_ERROR);
            return;
        }
        renderJson(service.approve(iAutoId, mark));
    }

    /**
     * 反审批
     */
    public void NoApprove(String ids) {
        if (StringUtils.isEmpty(ids)) {
            renderFail(JBoltMsg.PARAM_ERROR);
            return;
        }
        renderJson(service.NoApprove(ids));
    }

    /**
     * 撤回
     */
    public void recall(String iAutoId) {
        if (StringUtils.isEmpty(iAutoId)) {
            renderFail(JBoltMsg.PARAM_ERROR);
            return;
        }
        renderJson(service.recall(iAutoId));
    }

    /**
     * 手动关闭
     */
    public void closeWeekOrder(String iAutoId) {
        if (StringUtils.isEmpty(iAutoId)) {
            renderFail(JBoltMsg.PARAM_ERROR);
            return;
        }
        renderJson(service.closeWeekOrder(iAutoId));
    }

}
