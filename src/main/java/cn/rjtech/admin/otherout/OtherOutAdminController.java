package cn.rjtech.admin.otherout;

import cn.jbolt.core.permission.UnCheck;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.OtherOut;
import org.apache.commons.lang3.StringUtils;

/**
 * 出库管理-特殊领料单列表 Controller
 * @ClassName: OtherOutAdminController
 * @author: RJ
 * @date: 2023-05-06 15:05
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/otherout", viewPath = "/_view/admin/otherout")
public class OtherOutAdminController extends BaseAdminController {

	@Inject
	private OtherOutService service;

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
		kv.setIfNotNull("selectparam", get("deptname"));
		kv.setIfNotNull("selectparam", get("sourcebilltype"));
		kv.setIfNotNull("iorderstatus", get("iorderstatus"));
		kv.setIfNotNull("startdate", get("startdate"));
		kv.setIfNotNull("enddate", get("enddate"));
		renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), kv));

	}
	/**
	* 特殊领料单列表明细
	*/
	public void getOtherOutLines() {
		String autoid = get("autoid");
				Kv kv = new Kv();
		kv.set("autoid",autoid== null? "null" :autoid);
		renderJsonData(service.getOtherOutLines(getPageNumber(), getPageSize(), kv));

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
	 * 获取存货档案列表
	 * 通过关键字匹配
	 * autocomplete组件使用
	 */
	@UnCheck
	public void autocomplete() {
		renderJsonData(service.getAutocompleteData(get("q"), getInt("limit",10)));
	}


	/**
	 * 获取条码列表
	 * 通过关键字匹配
	 * autocomplete组件使用
	 */
	@UnCheck
	public void barcodeDatas() {
		String orgCode =  getOrgCode();
		renderJsonData(service.getBarcodeDatas(get("q"), getInt("limit",10),get("orgCode",orgCode)));
	}


	/**
	 * JBoltTable 可编辑表格整体提交 多表格
	 */
	public void submitMulti(String param, String revokeVal ,String autoid) {
		renderJson(service.submitByJBoltTables(getJBoltTables(),param,revokeVal,autoid));
	}


	/**
	 * 审批
	 */
	public void approve(String iAutoId,Integer mark) {
		if (StringUtils.isEmpty(iAutoId)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		renderJson(service.approve(iAutoId,mark));
	}

	/**
	 * 反审批
	 */
	public void NoApprove(String ids) {
		if (StringUtils.isEmpty(ids)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		renderJson(service.NoApprove(ids));
	}

	/**
	 * 手动关闭
	 */
	public void closeWeekOrder(String iAutoId) {
		if (StringUtils.isEmpty(iAutoId)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		renderJson(service.closeWeekOrder(iAutoId));
	}

	/**
	 * 撤回
	 */
	public void recall(String iAutoId) {
		if (StringUtils.isEmpty(iAutoId)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		renderJson(service.recall(iAutoId));
	}



}
