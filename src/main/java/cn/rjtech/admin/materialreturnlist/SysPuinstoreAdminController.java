package cn.rjtech.admin.materialreturnlist;


import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SysPuinstore;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

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
//		Date nowDate = new Date();
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		String time = format.format(nowDate);
//		puinstore.setBillDate(time);
		set("puinstore",puinstore);
		String edit = get("edit");
		if ("2".equals(edit)) {
			render("edit2.html");
		}else {
			render("edit1.html");
		}

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
		String autoid = puinstore.getAutoID();
		String OrgCode = getOrgCode();
		Record puinstoreName = service.getstockoutQcFormMList(autoid,OrgCode);
		set("type", get("type"));
		set("puinstoreName",puinstoreName);
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
	public void submitMulti(Integer param, String revokeVal) {
		renderJson(service.submitByJBoltTables(getJBoltTables(),param,revokeVal));
	}

	/**
	 * 条码数据源
	 */
	@UnCheck
	public void barcodeDatas() {
		renderJsonData(service.getBarcodeDatas(get("q"), getInt("limit", 10), get("orgCode", getOrgCode()), null));
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
	public void recall(String AutoId) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(AutoId)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		renderJson(service.recall(AutoId));
	}


	/**
	 * 查看所以退货出库单列表明细
	 */
	public void getmaterialReturnLists() {
		String autoid = get("autoid");
		String OrgCode = getOrgCode();
		Kv kv = new Kv();
		kv.set("autoid",autoid== null? "" :autoid);
		kv.set("OrgCode",OrgCode);
		renderJsonData(service.getmaterialReturnLists(getPageNumber(), getPageSize(), kv));
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
	/**
	 * 整单退货出库单列表明细
	 */
	public void getmaterialLines() {
		String autoid = get("autoid");
		String OrgCode = getOrgCode();
		Kv kv = new Kv();
		kv.set("autoid",autoid== null? "" :autoid);
		kv.set("OrgCode",OrgCode);
		renderJsonData(service.getmaterialLines(kv));
	}


	public void deleteList() {
		renderJson(service.deleteList(get(0)));
	}



	/**
	 * 入库号的选择数据Dialog
	 */
	public void choosePuinstoreData() {
		render("sysPuinstoreDialog.html");
	}

	/**
	 * 获取入库号明细
	 * */
	public void getSysPODetail() {
		Page<Record> recordPage = service.getSysPODetail(getKv(), getPageNumber(), getPageSize());
		renderJsonData(recordPage);
	}


}
