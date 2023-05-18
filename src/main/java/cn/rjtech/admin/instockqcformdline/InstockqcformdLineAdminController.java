package cn.rjtech.admin.instockqcformdline;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.InstockqcformdLine;
/**
 * 质量管理-在库检单行配置 Controller
 * @ClassName: InstockqcformdLineAdminController
 * @author: RJ
 * @date: 2023-05-04 14:26
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/instockqcformdline", viewPath = "/_view/admin/instockqcformdline")
public class InstockqcformdLineAdminController extends BaseAdminController {

	@Inject
	private InstockqcformdLineService service;

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
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKeywords()));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add().html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		InstockqcformdLine instockqcformdLine=service.findById(getLong(0)); 
		if(instockqcformdLine == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("instockqcformdLine",instockqcformdLine);
		render("edit().html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(InstockqcformdLine.class, "instockqcformdLine")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(InstockqcformdLine.class, "instockqcformdLine")));
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


}
