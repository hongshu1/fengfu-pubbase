package cn.rjtech.admin.mopickitemd;

import cn.rjtech.admin.mopickitemd.MoPickItemMService;
import cn.rjtech.model.momdata.Customer;
import cn.rjtech.model.momdata.RcvPlanM;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MoPickItemM;
import com.jfinal.plugin.activerecord.Record;

/**
 * 生产订单-备料单主表
 * @ClassName: MoPickItemMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 15:30
 */
@CheckPermission(PermissionKey.ADMIN_MOPICKITEMD)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/materialPreparationList", viewPath = "/_view/admin/mopickitemm")
public class MoPickItemMAdminController extends BaseAdminController {

	@Inject
	private MoPickItemMService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
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
		renderJson(service.save(getModel(MoPickItemM.class, "moPickItemM")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		MoPickItemM moPickItemM=service.findById(getLong(0));
		if(moPickItemM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moPickItemM",moPickItemM);
		render("edit.html");
	}
	/**
	 * 编辑
	 */
	public void edit1() {
		MoPickItemM moPickItemM=service.findById(getLong(0));
		if(moPickItemM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moPickItemM",moPickItemM);
		render("edit1.html");
	}
	/**
	 * 编辑
	 */
	public void edit2() {
		MoPickItemM moPickItemM=service.findById(getLong(0));
		if(moPickItemM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moPickItemM",moPickItemM);
		render("edit2.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MoPickItemM.class, "moPickItemM")));
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
	 * 自动生成
	 */
	public void auto() {
		render("auto.html");
	}

	/**
	 * 手动生成
	 */
	public void manual() {
		render("manual.html");
	}


}
