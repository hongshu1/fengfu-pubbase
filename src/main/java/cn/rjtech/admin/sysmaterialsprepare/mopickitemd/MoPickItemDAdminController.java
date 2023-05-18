package cn.rjtech.admin.sysmaterialsprepare.mopickitemd;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MoPickItemD;
/**
 * 生产订单-备料单明细
 * @ClassName: MoPickItemDAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 15:28
 */

@Path(value = "/admin/mopickitemd", viewPath = "/_view/admin/mopickitemd")
public class MoPickItemDAdminController extends BaseAdminController {

	@Inject
	private MoPickItemDService service;
   /**
	* 首页
	*/
	public void index() {
		render("index().html");
	}
   /**
	* 数据源
	*/
	public void datas() {
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isScanned")));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add().html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(MoPickItemD.class, "moPickItemD")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		MoPickItemD moPickItemD=service.findById(getLong(0));
		if(moPickItemD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moPickItemD",moPickItemD);
		render("edit().html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MoPickItemD.class, "moPickItemD")));
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
	* 切换isScanned
	*/
	public void toggleIsScanned() {
	    renderJson(service.toggleBoolean(getLong(0),"isScanned"));
	}


}
