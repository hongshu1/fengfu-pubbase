package cn.rjtech.admin.momaterialsscansum;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MoMaterialscanlog;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

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
    @UnCheck
	public void getMoMaterialNotScanLogList(){
		renderJsonData(service.getMoMaterialNotScanLogList(getPageNumber(),getPageSize(),getKv()));
	}
	/**
	 * 材料汇总-现品票扫描
	 */
	public  void addBarcode(@Para(value = "barcode") String barcode,
							 @Para(value = "imodocid") Long imodocid){
		Kv kv = getKv();
		renderJson(service.addBarcode(barcode,imodocid));
	}

	/**
	 * 齐料检查主页数据
	 */
    @UnCheck
	public void getBarcodeAll(){
		Page<Record> page=service.getBarcodeAll(getPageNumber(),getPageSize(),getKv());
		renderJsonData(page);
	}

}
