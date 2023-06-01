package cn.rjtech.admin.spotcheckformparam;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.SpotCheckFormParam;
/**
 * 质量建模-点检表格参数
 * @ClassName: SpotCheckFormParamAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-31 11:34
 */
@Path(value = "/admin/spotcheckform/qcformparam", viewPath = "/_view/admin/spotcheckform/qcformparam")
public class SpotCheckFormParamAdminController extends BaseAdminController {

	@Inject
	private SpotCheckFormParamService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getBoolean("isDeleted")));
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
	public void save(@Para("spotCheckFormParam")SpotCheckFormParam spotCheckFormParam) {
		renderJson(service.save(spotCheckFormParam));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SpotCheckFormParam spotCheckFormParam=service.findById(getLong(0));
		if(spotCheckFormParam == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("spotCheckFormParam",spotCheckFormParam);
		render("edit.html");
	}

	public void getQcFormParamListByPId(@Para(value = "iqcformid") Long qcFormId) {
		renderJsonData(service.getQcFormParamListByPId(qcFormId));
	}
	/**
	 * 查询表格项目
	 */
	public void qcformparamDatas() {
		set("type", get("type"));
		set("FormItemCodes", get("FormItemCodes"));
		set("iqcformitemid", get("typeId"));
		set("iQcItemIds", get("iQcItemIds"));
		render("qcformparam.html");
	}
	/**
	 * 表格项目数据源
	 */
	public void qcformparamlist() {
		renderJsonData(service.qcformparamlist(getPageNumber(), getPageSize(), Okv.create().set(getKv())));

	}
   /**
	* 更新
	*/
	public void update(@Para("spotCheckFormParam")SpotCheckFormParam spotCheckFormParam) {
		renderJson(service.update(spotCheckFormParam));
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


}
