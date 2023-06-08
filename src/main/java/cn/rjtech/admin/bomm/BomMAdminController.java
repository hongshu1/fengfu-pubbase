package cn.rjtech.admin.bomm;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.BomM;
/**
 * 物料建模-BOM主表
 * @ClassName: BomMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-08 17:03
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/bomM", viewPath = "/_view/admin/bomm")
public class BomMAdminController extends BaseAdminController {

	@Inject
	private BomMService service;
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
	* 保存
	*/
	public void save(@Para("bomM")BomM bomM) {
		renderJson(service.save(bomM));
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

   /**
	* 更新
	*/
	public void update(@Para("bomM")BomM bomM) {
		renderJson(service.update(bomM));
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


}
