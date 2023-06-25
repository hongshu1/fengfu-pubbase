package cn.rjtech.admin.otherdeliverylist;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.OtherOut;
import cn.rjtech.util.BillNoUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;

import java.util.Date;

/**
 * 出库管理-其他出库单列表 Controller
 * @ClassName: OtherOutAdminController
 * @author: RJ
 * @date: 2023-05-17 09:35
 */
@CheckPermission(PermissionKey.OTHER_DELIVERY_LIST)
@UnCheckIfSystemAdmin
@Path(value = "/admin/otherdeliverylist", viewPath = "/_view/admin/otherdeliverylist")
public class OtherOutDeliveryAdminController extends BaseAdminController {

	@Inject
	private OtherOutDeliveryService service;

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
		Kv kv =new Kv();
		kv.setIfNotNull("billno", get("billno"));
		kv.setIfNotNull("whname", get("whname"));
		kv.setIfNotNull("deptname", get("deptname"));
		kv.setIfNotNull("iorderstatus", get("iorderstatus"));
		kv.setIfNotNull("startdate", get("startdate"));
		kv.setIfNotNull("enddate", get("enddate"));
		renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), kv));

	}


	/**
	 * 其他出库单列表-明细
	 */
	public void getOtherOutLines() {
		String autoid = get("autoid");
		Kv kv = new Kv();
		kv.set("autoid",autoid== null? "null" :autoid);
		renderJsonData(service.getOtherOutLines(kv));

	}
   /**
	* 新增
	*/
	public void add() {
		OtherOut otherOut = new OtherOut();
		String billNo = BillNoUtils.getcDocNo(getOrgId(), "QTCK", 5);
		Date nowDate = new Date();
		otherOut.setBillNo(billNo);
		otherOut.setBillDate(nowDate);
		set("otherOut",otherOut);
		render("add.html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		OtherOut otherOut=service.findById(getLong(0));
		if(otherOut == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("otherOut",otherOut);
		set("type", get("type"));
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(OtherOut.class, "otherOut")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(OtherOut.class, "otherOut")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}

	/**
	 * JBoltTable 可编辑表格整体提交 多表格
	 */
	public void submitMulti(Integer param, String revokeVal ,String autoid) {
		renderJson(service.submitByJBoltTables(getJBoltTables(),param,revokeVal,autoid));
	}

	/**
	 * 获取条码列表
	 * 通过关键字匹配
	 * autocomplete组件使用
	 */
	public void otherOutBarcodeDatas() {
		String orgCode =  getOrgCode();
		renderJsonData(service.otherOutBarcodeDatas(get("q"),get("orgCode",orgCode)));
	}
}
