package cn.rjtech.admin.sysscandeliverone;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SysScandeliver;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * (双)扫码发货
 *
 * @ClassName: SysScandeliverAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-22 09:47
 */
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@CheckPermission(PermissionKey.SCAN_CODE_SHIPMENT)
@Path(value = "/admin/scanCodeShipment", viewPath = "/_view/admin/sysscandeliverone")
public class SysScandeliverOneAdminController extends BaseAdminController {

	@Inject
	private SysScandeliverOneService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("SourceBillType"), get("billno"), getBoolean("isDeleted"), getBoolean("state")));
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
		renderJson(service.save(getModel(SysScandeliver.class, "sysScandeliver")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		//todo 待优化
		SysScandeliver sysScandeliver=service.findById(getLong(0));
		if(sysScandeliver == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		//查询客户名称
		if(null != sysScandeliver.getBillNo()){
			Kv kv = new Kv();
			List<Record> rcvpland = service.rcvpland(kv.set("cbarcode",sysScandeliver.getBillNo()));
			if(false==rcvpland.isEmpty() && null != rcvpland.get(0).getStr("ccusname")){
				set("ccusname",rcvpland.get(0).getStr("ccusname"));
			}
		}
		//查询仓库名称
		if(null != sysScandeliver.getReceiveAddress()){
			Kv kv = new Kv();
			List<Record> rcvpland = service.customeraddr(kv.set("cdistrictname",sysScandeliver.getReceiveAddress()));
			if(false==rcvpland.isEmpty() && null != rcvpland.get(0).getStr("cwhname")){
				set("cwhname",rcvpland.get(0).getStr("cwhname"));
			}
		}
		set("sysscandeliver",sysScandeliver);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(SysScandeliver.class, "sysScandeliver")));
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
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}

   /**
	* 切换state
	*/
	public void toggleState() {
	    renderJson(service.toggleBoolean(getLong(0),"state"));
	}


	/**
	 * 客户位置
	 */
	public void orderDatatwo() {
		render("modetailtwo.html");
	}


	/**
	 *  获取 车次号/传票号数据源；从取货计划从表（SM_RcvPlanD）获取，关联主表带出客户名称
	 */
	public void rcvpland(){
		renderJsonData(service.rcvpland(getKv()));
	}

	/**
	 *  通过客户id 关联Bd_CustomerAddr（客户档案） 获取客户位置数据源，关联查出客户地址相关的仓库
	 */
	public void customeraddr(){
		renderJsonData(service.customeraddr(getKv()));
	}


	/**
	 * 新增-可编辑表格-批量提交
	 */
	public void submitAll() {
		renderJson(service.submitByJBoltTable(getJBoltTable()));
	}

	/**
	 * 获取客户信息
	 */
    @UnCheck
	public void getCustomer(){
		renderJsonData(service.getCustomer(getKv()));
	}

	/**
	 * 获取客户地址
	 */
    @UnCheck
	public void getCustAddr(){
		renderJsonData(service.getCustAddr(getKv()));
	}

	/**
	 * 获取订单Dialog
	 */
	public void orderData() {
        String cusid = get("cusid");
        set("cusid", cusid);
		ValidationUtils.isTrue(isOk(cusid), "请先选择客户名称");
        render("modetail.html");
	}

    /**
     * 获取订单数据源
     */
    @UnCheck
	public void getOrder(){
	    Kv kv = new Kv();
        String cusid = get("cusid");
        String orderNo = get("orderNo");
        kv.set("cusid",isOk(cusid)?cusid:" ");
        kv.setIfNotNull("orderNo", orderNo);
        renderJsonData(service.getOrder(kv));
    }

	/**
	 * 扫码获取资源
	 */
	public void getResource(){
		String barcode = get("barcode");
		String detailHidden = get("detailHidden");
		Kv kv = new Kv();
		kv.set("barcode",barcode);
		renderJsonData(service.getResource(kv));
	}
}
