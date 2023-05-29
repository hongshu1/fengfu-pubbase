package cn.rjtech.admin.barcodeencodingd;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Barcodeencodingd;
import cn.rjtech.util.ValidationUtils;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;

/**
 * 销售报价单明细扩展自定义项管理 Controller
 *
 * @ClassName: BarcodeencodingdAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-04-15 15:52
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.BARCODEENCODINGD)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/barcodeencodingd", viewPath = "/_view/admin/barcodeencodingd")
public class BarcodeencodingdAdminController extends BaseAdminController {

    @Inject
    private BarcodeencodingdService service;

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
    public void datas(@Para(value = "ibarcodeencodingmid") Long ibarcodeencodingmid) {
        renderJsonData(service.getList(ibarcodeencodingmid));
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
        Barcodeencodingd barcodeencodingd = service.findById(useIfPresent(getLong(0)));
        ValidationUtils.notNull(barcodeencodingd, JBoltMsg.DATA_NOT_EXIST);

        set("barcodeencodingd", barcodeencodingd);
        render("edit.html");
    }

    /**
     * 根据主表id查询细表数据
     */
    public void getlist(@Para(value = "barcodeencodingmid") Long barcodeencodingmid) {
        renderJsonData(service.getList(barcodeencodingmid));
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(useIfValid(Barcodeencodingd.class, "barcodeencodingd")));
    }

    /**
     * 保存
     */
    public void submit() {
        renderJson(service.saveTableSubmit(getJBoltTable()));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(useIfValid(Barcodeencodingd.class, "barcodeencodingd")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }


}
