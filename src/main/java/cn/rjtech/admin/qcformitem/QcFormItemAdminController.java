package cn.rjtech.admin.qcformitem;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.QcFormItem;
/**
 * 质量建模-检验表格项目
 * @ClassName: QcFormItemAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-27 17:09
 */
@CheckPermission(PermissionKey.QCFORM)
@Path(value = "/admin/qcformitem", viewPath = "/_view/admin/qcformitem")
public class QcFormItemAdminController extends BaseAdminController {

	@Inject
	private QcFormItemService service;

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
	public void save() {
		renderJson(service.save(getModel(QcFormItem.class, "qcFormItem")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		QcFormItem qcFormItem=service.findById(getLong(0));
		if(qcFormItem == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("qcFormItem",qcFormItem);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(QcFormItem.class, "qcFormItem")));
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
	 * 行数据
	 */
	public void formItemList() {
		Long id = getLong("qcFormItem.iautoid");
		Kv kv = new Kv();
		kv.set("id", id);
		LOG.info("{}", id);
		renderJsonData(service.formItemList(kv));
	}


}
