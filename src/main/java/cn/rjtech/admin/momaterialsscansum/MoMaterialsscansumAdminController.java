package cn.rjtech.admin.momaterialsscansum;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MoMaterialsscansum;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 制造工单-齐料汇总  Controller
 * @ClassName: MoMaterialsscansumAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-22 15:46
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/momaterialsscansum", viewPath = "/_view/admin/momaterialsscansum")
public class MoMaterialsscansumAdminController extends BaseAdminController {

	@Inject
	private MoMaterialsscansumService service;

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
		MoMaterialsscansum moMaterialsscansum=service.findById(getLong(0)); 
		if(moMaterialsscansum == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moMaterialsscansum",moMaterialsscansum);
		render("edit.html");
	}

  /**
	* s
	*/
	public void save() {
		renderJson(service.save(getModel(MoMaterialsscansum.class, "moMaterialsscansum")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MoMaterialsscansum.class, "moMaterialsscansum")));
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


	public void getBarcode(){
		String barcode=get("barcode");
		Long imodocid=getLong("imodocid");
		renderJson(service.getBarcode(barcode,imodocid));
	}




}
