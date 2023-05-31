package cn.rjtech.admin.spotcheckformitem;

import cn.rjtech.model.momdata.QcFormTableItem;
import cn.rjtech.model.momdata.SpotCheckFormTableItem;
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
import cn.rjtech.model.momdata.SpotCheckFormItem;

import java.util.List;

/**
 * 质量建模-点检表格项目
 * @ClassName: SpotCheckFormItemAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-31 10:57
 */
@Path(value = "/admin/spotcheckform/qcformitem", viewPath = "/_view/admin/spotcheckform/qcformitem")
public class SpotCheckFormItemAdminController extends BaseAdminController {

	@Inject
	private SpotCheckFormItemService service;
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
	public void save(@Para("spotCheckFormItem")SpotCheckFormItem spotCheckFormItem) {
		renderJson(service.save(spotCheckFormItem));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SpotCheckFormItem spotCheckFormItem=service.findById(getLong(0));
		if(spotCheckFormItem == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("spotCheckFormItem",spotCheckFormItem);
		render("edit.html");
	}
	/**
	 * 查询表格项目
	 */
	public void qcFormItemDatas() {
		set("type", get("type"));
		set("FormItemCodes", get("FormItemCodes"));
		System.out.println("===" + get("FormItemCodes"));
		render("qcitem.html");
	}
	/**
	 * 表格项目数据源
	 */
	public void qcitemlist() {
		Okv kv = new Okv();
		kv.setIfNotNull("iQcItemId", get("FormItemCodes"));
		renderJsonData(service.qcitemlist(getPageNumber(), getPageSize(), kv));

	}
   /**
	* 更新
	*/
	public void update(@Para("spotCheckFormItem")SpotCheckFormItem spotCheckFormItem) {
		renderJson(service.update(spotCheckFormItem));
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
