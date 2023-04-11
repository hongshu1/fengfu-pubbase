package cn.rjtech.admin.manualorderm;

import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.bean.JBoltDateRange;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ManualOrderM;

import java.util.Date;

/**
 * 客户订单-手配订单主表
 * @ClassName: ManualOrderMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 15:18
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.MANUALORDER)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/manualorderm", viewPath = "/_view/admin/manualorderm")
public class ManualOrderMAdminController extends BaseAdminController {

	@Inject
	private ManualOrderMService service;

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
		JBoltDateRange dateRange=getDateRange();
		Date startTime=dateRange.getStartDate();
		Date endTime=dateRange.getEndDate();
		Kv kv = getKv();
		kv.put("starttime",startTime);
		kv.put("endtime",endTime);
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), kv));
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
		JBoltTable jBoltTable = getJBoltTable();
		ManualOrderM manualOrderM = jBoltTable.getFormBean(ManualOrderM.class, "manualOrderM");
		renderJson(service.saveForm(manualOrderM,jBoltTable));
	}

   /**
	* 编辑
	*/
	public void edit() {
		ManualOrderM manualOrderM=service.findById(getLong(0));
		if(manualOrderM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("manualOrderM",manualOrderM);
		render("edit.html");
	}

   /**
	* 审核
	*/
	public void audit() {
		ManualOrderM manualOrderM=service.findById(getLong(0));
		if(manualOrderM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		manualOrderM.setIAuditStatus(1);
		manualOrderM.setIOrderStatus(3);
		renderJson(service.update(manualOrderM));
	}

   /**
	* 关闭
	*/
	public void colse() {
		ManualOrderM manualOrderM=service.findById(getLong(0));
		if(manualOrderM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		manualOrderM.setIOrderStatus(6);
		renderJson(service.update(manualOrderM));
	}

   /**
	* 编辑
	*/
	public void info() {
		ManualOrderM manualOrderM=service.findById(getLong(0));
		if(manualOrderM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("manualOrderM",manualOrderM);
		set("view",1);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(ManualOrderM.class, "manualOrderM")));
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

	public void inventory_dialog_index(){
		render("inventory_dialog_index.html");
	}

	public void batchAudit(){
		renderJson(service.batchHandle(getKv(),3));
	}

	public void batchAntiAudit(){
		renderJson(service.batchHandle(getKv(),2));
	}

	public void batchClose(){
		renderJson(service.batchHandle(getKv(),6));
	}

	public void batchDetect(){
		renderJson(service.batchDetect(getKv()));
	}
}
