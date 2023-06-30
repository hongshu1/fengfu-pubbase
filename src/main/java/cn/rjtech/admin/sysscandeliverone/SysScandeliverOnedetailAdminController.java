package cn.rjtech.admin.sysscandeliverone;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SysScandeliverdetail;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;

/**
 * 扫码发货明细
 *
 * @ClassName: SysScandeliverdetailAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-22 09:48
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/scanCodeShipmentdetail", viewPath = "/_view/admin/sysscandeliverdetail")
public class SysScandeliverOnedetailAdminController extends BaseAdminController {

	@Inject
	private SysScandeliverOnedetailService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("TrackType"), get("SourceBillType"), getBoolean("isDeleted")));
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
		renderJson(service.save(getModel(SysScandeliverdetail.class, "sysScandeliverdetail")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SysScandeliverdetail sysScandeliverdetail=service.findById(getLong(0));
		if(sysScandeliverdetail == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("sysScandeliverdetail",sysScandeliverdetail);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(SysScandeliverdetail.class, "sysScandeliverdetail")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

   /**
	* 切换isDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}

    @UnCheck
	public void findEditTableDatas(){
		renderJsonData(service.findEditTableDatas(get("sysscandeliver.AutoID")));
	}

	/**
	 * 获取行信息
	 */
    @UnCheck
	public void getLine(){
		String autoId = get("sysscandeliver.autoid");
		Kv kv = new Kv();
		kv.set("autoId",isOk(autoId)?autoId:" ");
		renderJsonData(service.getLine(kv));
	}

    /**
     * 获取订单行信息
     */
    @UnCheck
    public void getOrderLine(){
        String orderId = get("sysscandeliver.sourcebillid");
        Kv kv = new Kv();
        kv.set("orderId",isOk(orderId)?orderId:" ");
        renderJsonData(service.getOrderLine(kv));
    }
}
