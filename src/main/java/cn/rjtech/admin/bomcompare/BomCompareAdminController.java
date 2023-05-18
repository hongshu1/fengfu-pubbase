package cn.rjtech.admin.bomcompare;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.BomCompare;
/**
 * 物料建模-Bom清单
 * @ClassName: BomCompareAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-01 10:50
 */
@Path(value = "/admin/bomcompare", viewPath = "/_view/admin/bomcompare")
public class BomCompareAdminController extends BaseAdminController {

	@Inject
	private BomCompareService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getInt("iRawType"), getBoolean("isOutSourced"), getBoolean("isDeleted")));
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
		renderJson(service.save(getModel(BomCompare.class, "bomCompare")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		BomCompare bomCompare=service.findById(getLong(0));
		if(bomCompare == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("bomCompare",bomCompare);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(BomCompare.class, "bomCompare")));
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
	* 切换isOutSourced
	*/
	public void toggleIsOutSourced() {
	    renderJson(service.toggleBoolean(getLong(0),"isOutSourced"));
	}

   /**
	* 切换isDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}


}
