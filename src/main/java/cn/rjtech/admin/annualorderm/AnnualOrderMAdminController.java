package cn.rjtech.admin.annualorderm;

import cn.hutool.core.date.DateUtil;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.AnnualOrderM;

import java.util.Date;

/**
 * 年度计划订单
 *
 * @ClassName: AnnualOrderMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-23 17:23
 */
@CheckPermission(PermissionKey.ANNUALORDERM)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/annualorderm", viewPath = "/_view/admin/annualorderm")
public class AnnualOrderMAdminController extends BaseAdminController {

    @Inject
    private AnnualOrderMService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getSortColumn("iAutoId"), getSortType("desc"), getBoolean("cOrderNo"), getLong("iCustomerId"), getInt("iYear"), getInt("iOrderStatus"), get("cCreateName")));
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
        renderJson(service.save(getModel(AnnualOrderM.class, "annualOrderM")));
    }

    /**
     * 编辑
     */
    public void edit() {
        AnnualOrderM annualOrderM = service.findById(getLong(0));
        if (annualOrderM == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        Record annualOrderMRc = annualOrderM.toRecord();
        //annualOrderMRc.set("ccusname", );
        set("annualOrderM", annualOrderMRc);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(AnnualOrderM.class, "annualOrderM")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    /**
     * 新增-可编辑表格-批量提交
     */
    @Before(Tx.class)
    public void submitAll() {
        renderJson(service.submitByJBoltTable(getJBoltTable()));
    }

    public void dateHeader() {
        Integer iYear = getInt("iYear");
        if (iYear == null) {
            iYear = DateUtil.year(new Date());
        }
        service.dateHeader(iYear);

        render("add.html");
    }


}
