package cn.rjtech.admin.bomd;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.BomD;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
/**
 * 物料建模-BOM明细
 * @ClassName: BomDAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-08 17:03
 */
@CheckPermission(PermissionKey.BOMMASTER)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/bomD", viewPath = "/_view/admin/bomd")
public class BomDAdminController extends BaseAdminController {

	@Inject
	private BomDService service;
   /**
	* 首页
	*/
	public void index() {
		render("index.html");
	}
   /**
	* 数据源
	*/
   @UnCheck
	public void datas() {
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getInt("iPartType"), getBoolean("isVirtual"), getBoolean("bProxyForeign")));
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
	public void save(@Para("bomD")BomD bomD) {
		renderJson(service.save(bomD));
	}

   /**
	* 编辑
	*/
	public void edit() {
		BomD bomD=service.findById(getLong(0));
		if(bomD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("bomD",bomD);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update(@Para("bomD")BomD bomD) {
		renderJson(service.update(bomD));
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
	* 切换isVirtual
	*/
	public void toggleIsVirtual() {
	    renderJson(service.toggleBoolean(getLong(0),"isVirtual"));
	}

   /**
	* 切换bProxyForeign
	*/
	public void toggleBProxyForeign() {
	    renderJson(service.toggleBoolean(getLong(0),"bProxyForeign"));
	}


}
