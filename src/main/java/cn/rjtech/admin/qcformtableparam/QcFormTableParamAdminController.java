package cn.rjtech.admin.qcformtableparam;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.QcFormTableParam;
/**
 * 质量建模-检验表格参数录入配置
 * @ClassName: QcFormTableParamAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-04 16:12
 */
@CheckPermission(PermissionKey.QCFORM)
@Path(value = "/admin/qcform/qcformtableparam", viewPath = "/_view/admin/qcform/qcformtableparam")
public class QcFormTableParamAdminController extends BaseAdminController {

	@Inject
	private QcFormTableParamService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getInt("iType"), getBoolean("isDeleted")));
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
		renderJson(service.save(getModel(QcFormTableParam.class, "qcFormTableParam")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		QcFormTableParam qcFormTableParam=service.findById(getLong(0));
		if(qcFormTableParam == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("qcFormTableParam",qcFormTableParam);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(QcFormTableParam.class, "qcFormTableParam")));
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
