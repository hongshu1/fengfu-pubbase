package cn.rjtech.admin.momopatchweldm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MoMopatchweldm;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
/**
 * 补焊纪录管理 Controller
 * @ClassName: MoMopatchweldmAdminController
 * @author: chentao
 * @date: 2023-06-27 10:16
 */
@CheckPermission(PermissionKey.MOMOPATCHWELDM)
@UnCheckIfSystemAdmin
@Path(value = "/admin/momopatchweldm", viewPath = "/_view/admin/momopatchweldm")
public class MoMopatchweldmAdminController extends BaseAdminController {

	@Inject
	private MoMopatchweldmService service;

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
	 * 数据源
	 */
	public void getMoMopatchwelddList() {
		renderJsonData(service.getMoMopatchwelddList(getLong("iautoid")));
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
		MoMopatchweldm moMopatchweldm=service.findById(getLong("iautoid"));
		if(moMopatchweldm == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moMopatchweldm",moMopatchweldm);
		Record record = service.getMoMopatchweldmInfo(Kv.by("mid",getLong("iautoid")));
		set("recordInfo",record);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MoMopatchweldm.class, "moMopatchweldm")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MoMopatchweldm.class, "moMopatchweldm")));
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


	//-----------------------app--------------------------------------
	public void getList() {
		renderJsonData(service.getMoMopatchwelddApiList(getLong("iMoDocId")));
	}
	public void saveData() {
		renderJsonData(service.saveData(getLong("iMoDocId"),get("data")));
	}

}
