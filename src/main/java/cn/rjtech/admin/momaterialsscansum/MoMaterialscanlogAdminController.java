package cn.rjtech.admin.momaterialsscansum;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MoMaterialscanlog;
/**
 * 制造工单-齐料明细 Controller
 * @ClassName: MoMaterialscanlogAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-26 09:50
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/momaterialsscanlog", viewPath = "/_view/admin/momaterialsscanlog")
public class MoMaterialscanlogAdminController extends BaseAdminController {

	@Inject
	private MoMaterialscanlogService service;

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
		MoMaterialscanlog moMaterialscanlog=service.findById(getLong(0)); 
		if(moMaterialscanlog == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moMaterialscanlog",moMaterialscanlog);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MoMaterialscanlog.class, "moMaterialscanlog")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MoMaterialscanlog.class, "moMaterialscanlog")));
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
	 * 获取备料现品票明细（未扫描）
	 */
	public void getMoMaterialNotScanLogList(){
		renderJsonData(service.getMoMaterialNotScanLogList(getPageNumber(),getPageSize(),getKv()));
	}
	/**
	 * 获取备料现品票明细（已扫描）
	 */
	public void getMoMaterialScanLogList(){
		renderJsonData(service.getMoMaterialScanLogList(getPageNumber(),getPageSize(),getKv()));
	}
	/**
	 * 材料汇总-现品票扫描
	 */
	public  void  addBarcode(){
		String barcode=get("barcode");
		Long imodocid=getLong("imodocid");
		renderJsonData(service.addBarcode(barcode,imodocid));
	}

}
