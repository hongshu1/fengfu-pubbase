package cn.rjtech.admin.vouchrdcontrapose;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.VouchRdContrapose;
/**
 * 单据类型与收发类别对照表 Controller
 * @ClassName: VouchRdContraposeAdminController
 * @author: heming
 * @date: 2023-03-27 14:10
 */
@CheckPermission(PermissionKey.VOUCHRDCONTRAPOSE)
@UnCheckIfSystemAdmin
public class VouchRdContraposeAdminController extends BaseAdminController {

	@Inject
	private VouchRdContraposeService service;

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
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv()));
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
		VouchRdContrapose vouchRdContrapose=service.findById(getLong(0)); 
		if(vouchRdContrapose == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("vouchRdContrapose",vouchRdContrapose);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(VouchRdContrapose.class, "vouchRdContrapose")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(VouchRdContrapose.class, "vouchRdContrapose")));
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


}
