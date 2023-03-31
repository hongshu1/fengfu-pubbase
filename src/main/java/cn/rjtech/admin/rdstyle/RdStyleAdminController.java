package cn.rjtech.admin.rdstyle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.RdStyle;

import java.util.HashMap;
import java.util.Map;

/**
 * 收发类别 Controller
 * @ClassName: RdStyleAdminController
 * @author: WYX
 * @date: 2023-03-24 09:48
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/rdstyle", viewPath = "/_view/admin/rdstyle")
public class RdStyleAdminController extends BaseAdminController {

	@Inject
	private RdStyleService service;

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
		RdStyle rdStyle=service.findById(getLong(0)); 
		if(rdStyle == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("rdStyle",rdStyle);
		render("edit.html");
	}

	public void selectRdStyle() {
		RdStyle rdStyle=service.findById(getKv().getLong("autoid"));
		renderJsonData(rdStyle);
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(RdStyle.class, "rdStyle")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(RdStyle.class, "rdStyle")));
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
	* 切换toggleBRetail
	*/
	public void toggleBRetail() {
		renderJson(service.toggleBRetail(getLong(0)));
	}

  /**
	* 切换toggleBRdFlag
	*/
	public void toggleBRdFlag() {
		renderJson(service.toggleBRdFlag(getLong(0)));
	}

  /**
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}

	/**
	 * 树结构数据源
	 */
	public void mgrTree() {
		renderJsonData(service.getMgrTree(getLong("selectId", getLong(0)), getInt("openLevel", 0)));
	}

	/**
	 * select数据源 只需要IsDeletede=true的
	 */
	public void enableOptions() {
		renderJsonData(service.getTreeDatas(true, true));
	}

	/**
	 * 首页右边详情保存
	 */
	public void save1() {
		Long autoid = getLong("autoid");
		String crdcode = get("crdcode");
		String crdname = get("crdname");
		RdStyle rdStyle = new RdStyle();
		rdStyle.setIAutoId(autoid);
		rdStyle.setCRdCode(crdcode);
		rdStyle.setCRdName(crdname);
		renderJson(service.update(rdStyle));
	}

}