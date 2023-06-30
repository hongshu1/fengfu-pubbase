package cn.rjtech.admin.momaterialsreturnm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MoMaterialsreturnm;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;

/**
 * 制造工单-生产退料主表 Controller
 * @ClassName: MoMaterialsreturnmAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-25 16:32
 */
@UnCheck
@UnCheckIfSystemAdmin
@Path(value = "/admin/momaterialsreturnm", viewPath = "/_view/admin/momaterialsreturnm")
public class MoMaterialsreturnmAdminController extends BaseAdminController {

	@Inject
	private MoMaterialsreturnmService service;

   /**
	* 首页
	*/
	public void index() {
		String imodocid = get("imodocid");
		set("imodocid",imodocid);
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
	public void add(@Para(value = "imodocid") Long imodocid) {
		ValidationUtils.validateId(imodocid, "imodocid");

		keepPara();
		render("add.html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		MoMaterialsreturnm moMaterialsreturnm=service.findById(getLong(0)); 
		if(moMaterialsreturnm == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moMaterialsreturnm",moMaterialsreturnm);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MoMaterialsreturnm.class, "moMaterialsreturnm")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MoMaterialsreturnm.class, "moMaterialsreturnm")));
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
	 * 新增退料
	 */
	public void addMoMaterialsreturn(){
		Long imodocid=getLong("imodocid");
		renderJsonData(service.addMoMaterialsreturn(imodocid,getJBoltTable()));
	}

	/***
	 * 明细列表
	 */

	public void getMoMaterialsreturnList(){

		renderJsonData(service.getMoMaterialsreturnList(getPageNumber(),getPageSize(),getKv()));
	}

	/**
	 * 审核
	 */
	public void approve(String iAutoId,Integer mark) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(iAutoId)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		renderJson(service.approve(iAutoId,mark));
	}

	/**
	 * 反审核
	 */
	public void NoApprove(String ids) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(ids)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		///renderJson(service.NoApprove(ids));
	}
	public void details() {
		MoMaterialsreturnm moMaterialsreturnm=service.findById(getLong(0));
		if(moMaterialsreturnm == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moMaterialsreturnm",moMaterialsreturnm);
		render("detail.html");
	}

	/**
	 * 查询现品票
	 */
	public void addBarcode() {
		renderJsonData(service.getBycBarcodeInfo(get("barcode")));
	}

	public void getmomaterialscanusedlogList() {
		renderJsonData(service.getBycBarcodeList());
	}
	public void saveTableSubmit() {
		renderJson(service.saveTableSubmit(getJBoltTable()));
	}

	public void getmomaterialscanuseMList(){
		renderJsonData(service.getModandMomlist(get("imodocid")));
	}

}
