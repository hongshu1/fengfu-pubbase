package cn.rjtech.admin.subcontractorderm;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SubcontractOrderM;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 采购/委外订单-采购订单主表
 * @ClassName: SubcontractOrderMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-25 21:33
 */
@Path(value = "/admin/subcontractorderm", viewPath = "/_view/admin/subcontractorderm")
public class SubcontractOrderMAdminController extends BaseAdminController {

	@Inject
	private SubcontractOrderMService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getInt("iBusType"), getLong("iPurchaseTypeId"), getInt("iPayableType"), getBoolean("IsDeleted"), getBoolean("hideInvalid")));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add.html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(SubcontractOrderM.class, "subcontractOrderM")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SubcontractOrderM subcontractOrderM=service.findById(getLong(0));
		if(subcontractOrderM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("subcontractOrderM",subcontractOrderM);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(SubcontractOrderM.class, "subcontractOrderM")));
	}

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
	* 切换IsDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"IsDeleted"));
	}

   /**
	* 切换hideInvalid
	*/
	public void toggleHideInvalid() {
	    renderJson(service.toggleBoolean(getLong(0),"hideInvalid"));
	}


}
