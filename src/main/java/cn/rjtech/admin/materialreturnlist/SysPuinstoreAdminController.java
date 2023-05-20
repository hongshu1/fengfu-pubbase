package cn.rjtech.admin.materialreturnlist;


import cn.rjtech.util.BillNoUtils;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.SysPuinstore;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 出库管理-物料退货列表 Controller
 * @ClassName: SysPuinstoreAdminController
 * @author: RJ
 * @date: 2023-05-19 10:49
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/materialreturnlist", viewPath = "/_view/admin/materialreturnlist")
public class SysPuinstoreAdminController extends BaseAdminController {

	@Inject
	private SysPuinstoreListService service;

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
		kv.setIfNotNull("sourcebillno", get("sourcebillno"));
		kv.setIfNotNull("whcode", get("whcode"));
		kv.setIfNotNull("deptcode", get("deptcode"));
		kv.setIfNotNull("iorderstatus", get("iorderstatus"));
		kv.setIfNotNull("startdate", get("startdate"));
		kv.setIfNotNull("enddate", get("enddate"));
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),kv));
	}

	/**
	 * 新增
	 */
	public void add() {
		SysPuinstore puinstore = new SysPuinstore();
		String billNo = BillNoUtils.getcDocNo(getOrgId(), "WLTK", 5);
		Date nowDate = new Date();
		puinstore.setBillNo(billNo);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time = format.format(nowDate);
		puinstore.setBillDate(time);
		set("puinstore",puinstore);
		render("add.html");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		SysPuinstore puinstore=service.findById(getLong(0));
		if(puinstore == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("type", get("type"));
		set("puinstore",puinstore);
		render("edit.html");
	}

	/**
	 * 保存
	 */
	public void save() {
		renderJson(service.save(getModel(SysPuinstore.class, "puinstore")));
	}

	/**
	 * 更新
	 */
	public void update() {
		renderJson(service.update(getModel(SysPuinstore.class, "puinstore")));
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
	public void submitMulti(String param, String revokeVal) {
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
	 * 退货出库单列表明细
	 */
	public void getmaterialReturnListLines() {
		String autoid = get("autoid");
		String OrgCode = getOrgCode();
		Kv kv = new Kv();
		kv.set("autoid",autoid== null? "" :autoid);
		kv.set("OrgCode",OrgCode);
		renderJsonData(service.getmaterialReturnListLines(getPageNumber(), getPageSize(), kv));
	}


	public void deleteList() {
		renderJson(service.deleteList(get(0)));
	}


}
