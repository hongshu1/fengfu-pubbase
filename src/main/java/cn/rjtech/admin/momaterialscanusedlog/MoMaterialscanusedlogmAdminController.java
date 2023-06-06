package cn.rjtech.admin.momaterialscanusedlog;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MoMaterialscanusedlogm;
/**
 * 制造工单-材料耗用主表 Controller
 * @ClassName: MoMaterialscanusedlogmAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-25 18:06
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/momaterialscanusedlog", viewPath = "/_view/admin/momaterialscanusedlog")
public class MoMaterialscanusedlogmAdminController extends BaseAdminController {

	@Inject
	private MoMaterialscanusedlogmService service;

   /**
	* 首页
	*/
	public void index() {
		MoMaterialscanusedlogm moMaterialscanusedlogm=service.addDoc(getLong("imodocid"));
		set("moMaterialscanusedlogm",moMaterialscanusedlogm);
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
		MoMaterialscanusedlogm moMaterialscanusedlogm=service.findById(getLong(0)); 
		if(moMaterialscanusedlogm == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moMaterialscanusedlogm",moMaterialscanusedlogm);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MoMaterialscanusedlogm.class, "moMaterialscanusedlogm")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MoMaterialscanusedlogm.class, "moMaterialscanusedlogm")));
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
	 * 获取材料出库明细
	 */
	public void  getMaterialscanList(){
		renderJson(service.getMaterialscanList(getKv()));
	}

	/**
	 * 获取备料现品票明细（未扫描）
	 */
	public void getMaterialsPrepareList(){
		renderJsonData(service.getMaterialsPrepareList(getPageNumber(),getPageSize(),getKv()));
	}
	/**
	 * 获取备料现品票明细（已扫描）
	 */
	public void getmomaterialscanusedlogList(){
		renderJsonData(service.getMoMaterialscanusedlogList(getPageNumber(),getPageSize(),getKv()));
	}

	/**
	 * 现品票扫描
	 */
	public  void  addBarcode(){
		String barcode=get("barcode");
		Long imaterialscanUsedlogmid=getLong("imaterialscanusedlogmid");
         renderJsonData(service.addBarcode(barcode,imaterialscanUsedlogmid));
	}
	/**
	 * 新增单据
	 */
	public  void  addDoc(Long imodocid){
		service.addDoc(imodocid);

	}

	/**
	 * 保存产生出库单
	 */
	public void createOutDoc(){
		String cdocno=get("cdocno");
		Long imaterialscanUsedlogmid=getLong("imaterialscanusedlogmid");
		renderJsonData(service.createOutDoc(cdocno,imaterialscanUsedlogmid));
	}

}
