package cn.rjtech.admin.settlestyle;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.SettleStyle;
/**
 * 结算方式 Controller
 * @ClassName: SettleStyleAdminController
 * @author: WYX
 * @date: 2023-03-24 09:08
 */
@CheckPermission(PermissionKey.SETTLE_STYLE)
@UnCheckIfSystemAdmin
public class SettleStyleAdminController extends BaseAdminController {

	@Inject
	private SettleStyleService service;

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
		SettleStyle settleStyle=service.findById(getLong(0)); 
		if(settleStyle == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("settleStyle",settleStyle);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(SettleStyle.class, "settleStyle")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(SettleStyle.class, "settleStyle")));
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
	* 切换toggleBSSFlag
	*/
	public void toggleBSSFlag() {
		renderJson(service.toggleBSSFlag(getLong(0)));
	}

  /**
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}


}
