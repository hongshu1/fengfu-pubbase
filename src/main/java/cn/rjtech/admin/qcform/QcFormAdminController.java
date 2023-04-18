package cn.rjtech.admin.qcform;

import cn.hutool.core.util.StrUtil;
import cn.rjtech.model.momdata.QcParam;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.QcForm;

import java.util.List;

/**
 * 质量建模-检验表格
 * @ClassName: QcFormAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-27 17:53
 */
@CheckPermission(PermissionKey.QCFORM)
@Path(value = "/admin/qcform", viewPath = "/_view/admin/qcform")
public class QcFormAdminController extends BaseAdminController {

	@Inject
	private QcFormService service;

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
		Okv kv =new Okv();
		kv.setIfNotNull("cQcFormName", get("cQcFormName"));
		renderJsonData(service.getAdminDatas(getPageSize(), getPageNumber(), kv));
	}

   /**
	* 新增
	*/
	public void add() {
		QcParam qcParam = new QcParam();
		Long OrgId = getOrgId();
		String orgCode = getOrgCode();
		String orgName = getOrgName();
		qcParam.setIOrgId(OrgId);
		qcParam.setCOrgCode(orgCode);
		qcParam.setCOrgName(orgName);
		set("qcparam", qcParam);
		render("add.html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(QcForm.class, "qcForm")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		QcForm qcForm=service.findById(getLong(0));
		if(qcForm == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}

		//获取已保存的表头
		List<Record> rowOneList = service.qcformtableparamOneTitle(qcForm.getStr("iautoid"));
		List<Record> rowTwoList = service.qcformtableparamTwoTitle(qcForm.getStr("iautoid"));

		set("rowOneList", rowOneList);
		set("rowTwoList", rowTwoList);


		set("qcForm",qcForm);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(QcForm.class, "qcForm")));
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

   /**
	* 切换isEnabled
	*/
	public void toggleIsEnabled() {
	    renderJson(service.toggleBoolean(getLong(0),"isEnabled"));
	}

	/**
	 * 按主表qcformparam查询列表
	 */
	public void getQcFormParamListByPId() {
		Okv kv =new Okv();
		kv.setIfNotNull("iqcformid", get("iQcFormId"));
		kv.setIfNotNull("qcformitemid", get("typeId"));
		renderJsonData(service.getQcFormParamListByPId(getPageNumber(), getPageSize(), kv));
	}

	/**
	 * 按主表qcformtableparam查询列表
	 */
	public void getQcFormTableParamListByPId() {
		Okv kv =new Okv();
		kv.setIfNotNull("iqcformid", get("iQcFormId"));
		Page<Record> recordPage = service.getQcFormTableParamListByPId(getPageNumber(), getPageSize(), kv);
		renderJsonData(recordPage);
	}


	/**
	 * 按主表qcformitem查询列表qcform
	 */
	public void getItemCombinedListByPId() {
		renderJsonData(service.getItemCombinedListByPId(Kv.by("iQcFormId", getLong("iQcFormId"))));
	}


	/**
	 * qcformitem可编辑表格提交
	 *
	 */
	public void submitByJBoltTable() {
		renderJson(service.submitByJBoltTable(getJBoltTable()));
	}


	/**
	 * qcformparam可编辑表格提交
	 *
	 */
	public void QcFormParamJBoltTable() {
		renderJson(service.QcFormParamJBoltTable(getJBoltTable()));
	}


	/**
	 * qcformtableparam可编辑表格提交
	 *
	 */
//	public void QcFormTableParamJBoltTable() {
//		renderJson(service.QcFormTableParamJBoltTable(getJBoltTable()));
//	}



	public void customerList() {
		//列表排序
		String cus = get("q");
		Kv kv = new Kv();
		kv.set("cus", StrUtil.trim(cus));
		kv.setIfNotNull("iQcFormId", get("iQcFormId"));
		// 调用采购订单的列表数据源查询
		renderJsonData(service.customerList(kv));

	}

	public void options(){
		renderJsonData(service.options());
	}
}
