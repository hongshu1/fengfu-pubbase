package cn.rjtech.admin.transvouchdetail;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.TransVouchDetail;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 出库管理-调拨单列表 Controller
 * @ClassName: TransVouchDetailAdminController
 * @author: RJ
 * @date: 2023-05-11 14:55
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/transvouchdetail", viewPath = "/_view/admin/transvouchdetail")
public class TransVouchDetailAdminController extends BaseAdminController {

	@Inject
	private TransVouchDetailService service;

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
		render("add.html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		TransVouchDetail transVouchDetail=service.findById(getLong(0)); 
		if(transVouchDetail == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("transVouchDetail",transVouchDetail);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(TransVouchDetail.class, "transVouchDetail")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(TransVouchDetail.class, "transVouchDetail")));
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
