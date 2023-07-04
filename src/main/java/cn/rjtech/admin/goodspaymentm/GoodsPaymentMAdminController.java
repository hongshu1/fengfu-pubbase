package cn.rjtech.admin.goodspaymentm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.customer.CustomerService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Customer;
import cn.rjtech.model.momdata.GoodsPaymentM;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.List;

/**
 * 货款核对表
 *
 * @ClassName: GoodsPaymentMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-30 00:06
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.PAYMENT_CHECK_MANAGE)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/paymentCheckManage", viewPath = "/_view/admin/goodspaymentm")
public class GoodsPaymentMAdminController extends BaseAdminController {

    @Inject
    private GoodsPaymentMService service;
    @Inject
    private CustomerService customerService;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
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
        renderJson(service.save(getModel(GoodsPaymentM.class, "goodsPaymentM")));
    }

    /**
     * 编辑
     */
    public void edit() {
        GoodsPaymentM goodsPaymentM = service.findById(getLong(0));
        if (goodsPaymentM == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        Record goodspaymentm = goodsPaymentM.toRecord();
        Customer customer = customerService.findById(goodsPaymentM.getICustomerId());
        goodspaymentm.set("ccusname", customer == null ? null : customer.getCCusName());
        set("goodspaymentm", goodspaymentm);
        render("edit.html");


    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(GoodsPaymentM.class, "goodsPaymentM")));
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
     * 切换isDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleBoolean(getLong(0), "isDeleted"));
    }

    /**
     * 新增-可编辑表格-批量提交
     */
    @Before(Tx.class)
    public void submitAll() {
        renderJson(service.submitByJBoltTable(getJBoltTable()));
    }

    /**
     * 提审批
     */
    public void submit(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.validateId(iautoid, "id");

        renderJson(service.submit(iautoid));
    }

    /**
     * 条码数据源
     */
    @UnCheck
    public void barcodeDatas() {
        String iCustomerId1 = get("iCustomerId1");
        ValidationUtils.notNull(iCustomerId1, "请优先选择客户。");
        List<Record> barcodeDatas = service.getBarcodeDatas(get("q"), getInt("limit", 10), get("orgCode", getOrgCode()),iCustomerId1 );
        renderJsonData(barcodeDatas);
    }

}
