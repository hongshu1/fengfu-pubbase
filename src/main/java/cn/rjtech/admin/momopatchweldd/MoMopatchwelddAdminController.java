package cn.rjtech.admin.momopatchweldd;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MoMopatchweldd;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 补焊纪录明细 Controller
 * @ClassName: MoMopatchwelddAdminController
 * @author: chentao
 * @date: 2023-06-27 10:18
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/momopatchweldd", viewPath = "/_view/admin/momopatchweldd")
public class MoMopatchwelddAdminController extends BaseAdminController {

	@Inject
	private MoMopatchwelddService service;

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
		MoMopatchweldd moMopatchweldd=service.findById(getLong(0)); 
		if(moMopatchweldd == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moMopatchweldd",moMopatchweldd);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MoMopatchweldd.class, "moMopatchweldd")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MoMopatchweldd.class, "moMopatchweldd")));
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
