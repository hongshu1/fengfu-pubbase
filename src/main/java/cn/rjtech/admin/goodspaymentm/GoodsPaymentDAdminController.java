package cn.rjtech.admin.goodspaymentm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.CheckPermission;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.GoodsPaymentD;
/**
 * 货款核对明细
 * @ClassName: GoodsPaymentDAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-30 00:13
 */
@CheckPermission(PermissionKey.PAYMENT_CHECK_MANAGENOT)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/goodspaymentd", viewPath = "/_view/admin/goodspaymentd")
public class GoodsPaymentDAdminController extends BaseAdminController {

	@Inject
	private GoodsPaymentDService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize()));
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
		renderJson(service.save(getModel(GoodsPaymentD.class, "goodsPaymentD")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		GoodsPaymentD goodsPaymentD=service.findById(getLong(0));
		if(goodsPaymentD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("goodsPaymentD",goodsPaymentD);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(GoodsPaymentD.class, "goodsPaymentD")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByIdsRm(get("ids")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}

	public void findEditTableDatas(){
		renderJsonData(service.findEditTableDatas(getKv()));
	}

}
