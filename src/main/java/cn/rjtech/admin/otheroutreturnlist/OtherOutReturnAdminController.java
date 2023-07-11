package cn.rjtech.admin.otheroutreturnlist;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.otheroutdetail.OtherOutDetailService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.OtherOut;
import cn.rjtech.model.momdata.OtherOutDetail;
import cn.smallbun.screw.core.util.CollectionUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 出库管理-特殊领料出库 Controller
 *
 * @ClassName: OtherOutAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-22 16:33
 */
@CheckPermission(PermissionKey.OTHER_OUT_RETURN_LIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/otheroutreturnlist", viewPath = "/_view/admin/otheroutreturnlist")
public class OtherOutReturnAdminController extends BaseAdminController {

    @Inject
    private OtherOutReturnService service;

    @Inject
    private OtherOutDetailService otherOutDetailService;

    /**
     * 首页
     */
    public void index() {
        render("index.html");
    }


    /**
     * 跳转选择领料单页面
     */
    public void BillNoreturnList() {
        render("return.html");
    }


    /**
     * 选择领料单单号获得数据源
     */
    public void getReturnDataS() {
        String billno = get("billno");
        Kv kv = new Kv();
        kv.set("billno", billno == null ? "" : billno);
        renderJsonData(service.getReturnDataS(getPageNumber(), getPageSize(), kv));
    }

    /**
     * 数据源
     */
    public void datas() {
        Kv kv = new Kv();
        kv.setIfNotNull("billno", get("billno"));
        kv.setIfNotNull("deptname", get("deptname"));
        kv.setIfNotNull("sourcebilltype", get("sourcebilltype"));
        kv.setIfNotNull("iorderstatus", get("iorderstatus"));
        kv.setIfNotNull("startdate", get("startdate"));
        kv.setIfNotNull("enddate", get("enddate"));
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), kv));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.OTHER_OUT_RETURN_LIST_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.OTHER_OUT_RETURN_LIST_EDIT)
    public void edit() {
        OtherOut otherOut = service.findById(getLong(0));
        if (otherOut == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("status", get("status"));
        set("otherOut", otherOut);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(OtherOut.class, "otheroutreturn")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(OtherOut.class, "otheroutreturn")));
    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.OTHER_OUT_RETURN_LIST_DELETE)
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.OTHER_OUT_RETURN_LIST_DELETE)
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }


    /**
     * JBoltTable 可编辑表格整体提交 多表格
     */
    @CheckPermission(PermissionKey.OTHER_OUT_RETURN_LIST_ADD)
    public void submitMulti() {
        renderJson(service.submitByJBoltTables(getJBoltTables()));
    }


    /**
     * 特殊领料单列表明细
     */
    public void getOtherOutLinesReturnLines() {
        String autoid = get("autoid");
        Kv kv = new Kv();
        kv.set("autoid", autoid == null ? "null" : autoid);
        List<Record> recordList = service.treturnQty(kv);
        for (Record Lists : recordList) {
            BigDecimal qty1 = Lists.get("qty");
            int i = qty1.compareTo(BigDecimal.ZERO);
            System.out.println(i);
            if (i < 0) {
                kv.set("Detailautoid", Lists.get("autoid"));
            }
        }
        renderJsonData(service.getOtherOutLinesReturnLines(getPageNumber(), getPageSize(), kv));

    }


    /**
     * 出库明细删除
     */
    @CheckPermission(PermissionKey.OTHER_OUT_RETURN_LIST_DELETE)
    public void deleteList() {
        renderJson(service.deleteList(get(0)));
    }


}
