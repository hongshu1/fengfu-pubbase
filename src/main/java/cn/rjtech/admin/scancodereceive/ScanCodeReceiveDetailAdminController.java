package cn.rjtech.admin.scancodereceive;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SysPureceivedetail;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;

/**
 * 双单位扫码收货明细
 *
 * @ClassName: ScanCodeReceiveDetailAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-10 10:01
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/scancodereceivedetail", viewPath = "/_view/admin/scancodereceivedetail")
public class ScanCodeReceiveDetailAdminController extends BaseAdminController {

    @Inject
    private ScanCodeReceiveDetailService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("SourceBillType"), get("TrackType"), getBoolean("IsDeleted")));
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
        renderJson(service.save(getModel(SysPureceivedetail.class, "sysPureceivedetail")));
    }

    /**
     * 编辑
     */
    public void edit() {
        SysPureceivedetail sysPureceivedetail = service.findById(getLong(0));
        if (sysPureceivedetail == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("sysPureceivedetail", sysPureceivedetail);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(SysPureceivedetail.class, "sysPureceivedetail")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteRmRdByIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 切换IsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleBoolean(getLong(0), "IsDeleted"));
    }

    @UnCheck
    public void findEditTableDatas() {
        Kv kv = new Kv();
        Long id = getLong("sysPureceive.AutoID");
        kv.setIfNotNull("id", id==null?' ':id);
        System.out.println("id===>"+id);
        renderJsonData(service.findEditTableDatas(kv));
    }
}
