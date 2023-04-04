package cn.rjtech.admin.locListCN;

import java.util.List;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.LocListCN;
/**
 * 地区 Controller
 * @ClassName: LocListCNAdminController
 * @author: RJ
 * @date: 2023-03-27 11:03
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/locListCN", viewPath = "/_view/admin/locListCN")
public class LocListCNAdminController extends BaseAdminController {

	@Inject
	private LocListCNService service;

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
		LocListCN locListCN=service.findById(getLong(0)); 
		if(locListCN == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("locListCN",locListCN);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(LocListCN.class, "locListCN")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(LocListCN.class, "locListCN")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}

	public void QQI18NLocListSave(){
		renderJson(service.QQI18NLocListSave());
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}

	public void findByIPid(){
		Long ipid = 0L;
		String ipid1 = get("ipid");
		if(isOk(get("ccountry"))){
			ipid = getLong("ccountry");
		}else if(isOk(get("cprovince"))){
			ipid = getLong("cprovince");
		}else if(isOk(get("ccity"))){
			ipid = getLong("ccity");
		}
		List<LocListCN> list = service.findByIPid(ipid);
		renderJsonData(list);
	}

	public void findByNameChild(){
		String name = null;
		if(isOk(get("ccountry"))){ //
			name = get("ccountry");
		}else if(isOk(get("cprovince"))){
			name = get("cprovince");
		}else if(isOk(get("ccity"))){
			name = get("ccity");
		}
		List<LocListCN> byNameChild = service.findByNameChild(name);
		renderJsonData(byNameChild);
	}
}
