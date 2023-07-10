package cn.rjtech.admin.exch;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Exch;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 币种汇率档案 Controller
 * @ClassName: ExchAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-22 19:40
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/exch", viewPath = "/_view/admin/exch")
public class ExchAdminController extends BaseAdminController {

	@Inject
	private ExchService service;

   /**
	* 首页
	*/
	public void index() {
		render("index.html");
	}
  	
  	/**
	* 数据源
	*/
    @UnCheck
	public void datas() {
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKeywords()));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add.html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		Exch exch=service.findById(getLong(0)); 
		if(exch == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("exch",exch);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(Exch.class, "exch")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Exch.class, "exch")));
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
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}

    /**
     * 获取币种汇率
     */
    @UnCheck
    public void getNflatByExchName() {
        renderJsonData(service.getNflat(get("cexchname")));
    }	

}
