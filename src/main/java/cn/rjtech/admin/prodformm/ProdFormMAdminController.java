package cn.rjtech.admin.prodformm;

import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.rjtech.admin.prodform.ProdFormService;
import cn.rjtech.admin.prodformitem.ProdFormItemService;
import cn.rjtech.admin.prodformtableparam.ProdFormTableParamService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.model.momdata.ProdFormM;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;

import java.util.List;
import java.util.Map;

/**
 * 制造管理-生产表单主表
 * @ClassName: ProdFormMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-28 10:04
 */
@CheckPermission(PermissionKey.PRODFORMM)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/prodFormM", viewPath = "/_view/admin/prodformm")
public class ProdFormMAdminController extends BaseAdminController {

	@Inject
	private ProdFormMService service;
	@Inject
	private ProdFormItemService prodFormItemService;
	@Inject
	private ProdFormService prodFormService;
	@Inject
	private ProdFormTableParamService prodFormTableParamService;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(),getKv()));
	}

	/**
	 * 新增
	 */
	@CheckPermission(PermissionKey.PRODFORMM_ADD)
	public void add() {
		render("add.html");
	}

	/**
	 * 保存
	 */
	@Before(Tx.class)
	@TxConfig(DataSourceConstants.MOMDATA)
	public void save(@Para("prodFormM")ProdFormM prodFormM) {
		renderJson(service.save(prodFormM));
	}

	/**
	 * 编辑
	 */
	@CheckPermission(PermissionKey.PRODFORMM_EDIT)
	public void edit(@Para(value = "iautoid") Long iautoid) {
		ProdFormM prodFormM = service.findById(iautoid);
		if(prodFormM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("prodFormM",prodFormM);
		keepPara();
		String s = prodFormM.getIAuditStatus() == 0 ?
				"已保存" : prodFormM.getIAuditStatus() == 1 ?
				"待审核" : prodFormM.getIAuditStatus() == 2 ? "审核通过" : "审核不通过";
		set("status",s);
		render("edit.html");
	}

	/**
	 * 更新
	 */
	@Before(Tx.class)
	@TxConfig(DataSourceConstants.MOMDATA)
	public void update(@Para("prodFormM")ProdFormM prodFormM) {
		renderJson(service.update(prodFormM));
	}

	/**
	 * 批量删除
	 */
	@Before(Tx.class)
	@TxConfig(DataSourceConstants.MOMDATA)
	@CheckPermission(PermissionKey.PRODFORMM_DELETE)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

	/**
	 * 删除
	 */
	@Before(Tx.class)
	@TxConfig(DataSourceConstants.MOMDATA)
	@CheckPermission(PermissionKey.PRODFORMM_DELETE)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

	/**
	 * 获取表格中的数据
	 */
	public void table3(@Para( value = "iprodformid") String iprodformid){
		//根据生产表格id获取项目名称
		if (StrUtil.isNotBlank(iprodformid)) {
			//生产表单项目标题
			List<Record> formItemLists = prodFormItemService.formItemLists(Kv.by("iqcformid", iprodformid));
			//行转列
			List<Map<String, Object>> columns = service.lineRoll(formItemLists,iprodformid);
			set("columns",columns);
			List<Record> byIdGetDetail = prodFormService.findByIdGetDetail(iprodformid);
			List<Map<String, Object>> maps = service.lineRoll2(byIdGetDetail);
			// 查询表头数据及参数数据
			set("dataList", maps);
		}
		render("_table3.html");
	}

	/**
	 * 新增保存
	 */
	@CheckPermission(PermissionKey.PRODFORMM_ADD_SUBMIT)
	public void submitForm(@Para(value = "formJsonData") String formJsonDataStr,
						   @Para(value = "tableJsonData") String tableJsonDataStr){
		renderJsonData(service.submitForm(formJsonDataStr, tableJsonDataStr));
	}

}