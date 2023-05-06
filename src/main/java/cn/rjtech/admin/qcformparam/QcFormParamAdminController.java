package cn.rjtech.admin.qcformparam;

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
import cn.rjtech.model.momdata.QcFormParam;
/**
 * 质量建模-检验表格参数
 * @ClassName: QcFormParamAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-04 16:09
 */
@CheckPermission(PermissionKey.QCFORM)
@Path(value = "/admin/qcform/qcformparam", viewPath = "/_view/admin/qcform/qcformparam")
public class QcFormParamAdminController extends BaseAdminController {

	@Inject
	private QcFormParamService service;

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
		renderJson(service.save(getModel(QcFormParam.class, "qcFormParam")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		QcFormParam qcFormParam=service.findById(getLong(0));
		if(qcFormParam == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("qcFormParam",qcFormParam);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(QcFormParam.class, "qcFormParam")));
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
	 * 表格项目数据源
	 */
	public void qcformparamlist() {
		Okv kv =new Okv();
		kv.setIfNotNull("iQcParamId", get("FormItemCodes"));
		kv.setIfNotNull("iqcformitemid", get("iqcformitemid"));
		renderJsonData(service.qcformparamlist(getPageNumber(), getPageSize(), kv));

	}

	/**
	 * 查询表格项目
	 */
	public void qcformparamDatas() {
		set("type", get("type"));
		set("FormItemCodes", get("FormItemCodes"));
		set("iqcformitemid", get("typeId"));
		render("qcformparam.html");
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
