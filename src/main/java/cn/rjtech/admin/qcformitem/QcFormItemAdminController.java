package cn.rjtech.admin.qcformitem;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.QcFormItem;
/**
 * 质量建模-检验表格项目
 * @ClassName: QcFormItemAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-04 16:10
 */
@CheckPermission(PermissionKey.QCFORM)
@Path(value = "/admin/qcform/qcformitem", viewPath = "/_view/admin/qcform/qcformitem")
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
		Long headerId = getLong("qcForm.iAutoId");
		System.out.println("===="+headerId);
		renderJsonData(service.getAdminDatas(getPageSize(), getPageNumber(), getKv()));	}

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
	 * 查询表格项目
	 */
	public void qcFormItemDatas() {
		set("type", get("type"));
		set("FormItemCodes", get("FormItemCodes"));
		System.out.println("==="+get("FormItemCodes"));
		render("qcitem.html");
	}


	/**
	 * 表格项目数据源
	 */
	public void qcitemlist() {
		Okv kv =new Okv();
		kv.setIfNotNull("iQcItemId", get("FormItemCodes"));
		renderJsonData(service.qcitemlist(getPageNumber(), getPageSize(), kv));

	}


	/**
	 * 删除
	 */
	@Before(Tx.class)
	public void deletes() {
		renderJson(service.delete(getLong(0)));
	}

	/**
	 * 排序 上移
	 */
	@Before(Tx.class)
	public void up() {
		renderJson(service.up(getLong(0)));
	}

	/**
	 * 排序 下移
	 */
	@Before(Tx.class)
	public void down() {
		renderJson(service.down(getLong(0)));
	}




}
