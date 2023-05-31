package cn.rjtech.admin.spotcheckformtableparam;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.SpotCheckFormTableParam;
/**
 * 质量建模-点检表格参数录入配置
 * @ClassName: SpotCheckFormTableParamAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-31 10:59
 */
@Path(value = "/admin/spotCheckFormTableParam", viewPath = "/_view/admin/spotcheckformtableparam")
public class SpotCheckFormTableParamAdminController extends BaseAdminController {

	@Inject
	private SpotCheckFormTableParamService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getInt("iType"), getBoolean("isDeleted")));
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
	public void save(@Para("spotCheckFormTableParam")SpotCheckFormTableParam spotCheckFormTableParam) {
		renderJson(service.save(spotCheckFormTableParam));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SpotCheckFormTableParam spotCheckFormTableParam=service.findById(getLong(0));
		if(spotCheckFormTableParam == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("spotCheckFormTableParam",spotCheckFormTableParam);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update(@Para("spotCheckFormTableParam")SpotCheckFormTableParam spotCheckFormTableParam) {
		renderJson(service.update(spotCheckFormTableParam));
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
	* 切换isDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}


}
