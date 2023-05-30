package cn.rjtech.admin.transvouch;


import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.TransVouch;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.wms.utils.StringUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;

import java.util.Date;

/**
 * 出库管理-调拨单列表 Controller
 * @ClassName: TransVouchAdminController
 * @author: RJ
 * @date: 2023-05-11 14:54
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/transvouch", viewPath = "/_view/admin/transvouch")
public class TransVouchAdminController extends BaseAdminController {

	@Inject
	private TransVouchService service;

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
		kv.setIfNotNull("selectparam", get("billno"));
		kv.setIfNotNull("selectparam", get("iwhcode"));
		kv.setIfNotNull("selectparam", get("owhcode"));
		kv.setIfNotNull("iorderstatus", get("iorderstatus"));
		kv.setIfNotNull("startdate", get("startdate"));
		kv.setIfNotNull("enddate", get("enddate"));
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),kv));
	}


	/**
	 * 调拨单列表明细
	 */
	public void getTransVouchLines() {
		String autoid = get("autoid");
		Kv kv = new Kv();
		kv.set("autoid",autoid== null? "null" :autoid);
		renderJsonData(service.getTransVouchLines(getPageNumber(), getPageSize(), kv));

	}



   /**
	* 新增
	*/
	public void add() {
		TransVouch transVouch = new TransVouch();
		String billNo = BillNoUtils.getcDocNo(getOrgId(), "LLD", 5);
		Date nowDate = new Date();
		transVouch.setBillNo(billNo);
		transVouch.setBillDate(nowDate);
		set("transVouch",transVouch);
		render("add.html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		TransVouch transVouch=service.findById(getLong(0)); 
		if(transVouch == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("transVouch",transVouch);
		set("type", get("type"));
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(TransVouch.class, "transVouch")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(TransVouch.class, "transVouch")));
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
	 * 拉取产线和产线编码
	 */
	public void workRegionMList() {
		//列表排序
		String cus = get("q");
		Kv kv = new Kv();
		kv.set("cus", StringUtils.trim(cus));
		renderJsonData(service.workRegionMList(kv));

	}

	/**
	 * JBoltTable 可编辑表格整体提交 多表格
	 */
	public void submitMulti(Integer param, String revokeVal) {
		renderJson(service.submitByJBoltTables(getJBoltTables(),param,revokeVal));
	}


	/**
	 * 审核
	 */
	public void approve(String iAutoId,Integer mark) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(iAutoId)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		renderJson(service.approve(iAutoId,mark));
	}

	/**
	 * 反审核
	 */
	public void NoApprove(String ids) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(ids)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		renderJson(service.NoApprove(ids));
	}


	/**
	 * 撤回
	 */
	public void recall(String iAutoId) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(iAutoId)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		renderJson(service.recall(iAutoId));
	}


	/**
	 * 转出仓库
	 */
	public void warehouse() {
		renderJsonData(service.warehouse(getKv()));
	}


}
