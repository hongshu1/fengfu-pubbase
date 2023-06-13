package cn.rjtech.admin.bomm;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.admin.bomd.BomDService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.BomM;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;

/**
 * 物料建模-BOM主表
 * @ClassName: BomMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-08 17:03
 */
@Path(value = "/admin/bomM", viewPath = "/_view/admin/bomm")
public class BomMAdminController extends BaseAdminController {

	@Inject
	private BomMService service;
	@Inject
	private BomDService bomDService;
	
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getInt("iType"), getBoolean("isEffective"), getBoolean("isDeleted")));
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
		BomM bomM=service.findById(getLong(0));
		if(bomM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("bomM",bomM);
		render("edit.html");
	}

 /*  *//**
	* 更新
	*//*
	public void update(@Para("bomM")BomM bomM) {
		renderJson(service.update(bomM));
	}*/

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

   /**
	* 切换isEffective
	*/
	public void toggleIsEffective() {
	    renderJson(service.toggleBoolean(getLong(0),"isEffective"));
	}

   /**
	* 切换isDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}

	public void getTreeDatas(){
		renderJsonData(service.getTreeDatas(getKv()));
	}
	
	public void updateIsView(){
		renderJsonData(service.updateIsView(getLong(0)));
	}
	
	public void getVersionRecord() {
		renderJsonData(service.getVersionRecord(getPageNumber(), getPageSize(), getKv()));
	}
	
	public void getBomComparePageData(){
		renderJsonData(bomDService.getBomComparePageData(getPageNumber(), getPageSize(), getKv()));
	}
	
	public void findByVersionList(@Para(value = "iinventoryid") Long invId){
		renderJsonData(service.findByVersionList(getOrgId(), invId));
	}
	
	public void audit(@Para(value = "bomMasterId") Long bomMasterId, @Para(value = "status") Integer status) {
		renderJson(service.audit(bomMasterId, status));
	}
	
	public void del() {
		renderJson(service.del(getLong(0)));
	}
	
	// 拷贝
	public void saveCopy(@Para(value = "cversion") String cVersion, @Para(value = "dDisableDate") String dDisableDate,  @Para(value = "oldId") Long oldId) {
		renderJson(service.saveCopy(oldId, dDisableDate, cVersion));
	}
}
