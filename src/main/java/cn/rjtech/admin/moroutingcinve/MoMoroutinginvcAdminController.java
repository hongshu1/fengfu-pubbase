package cn.rjtech.admin.moroutingcinve;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MoMoroutinginvc;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 制造工单-生产工艺路线物料 Controller
 * @ClassName: MoMoroutinginvcAdminController
 * @author: RJ
 * @date: 2023-05-09 16:55
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Path(value = "/admin/moroutingcinve", viewPath = "/_view/admin/moroutingcinve")
public class MoMoroutinginvcAdminController extends BaseAdminController {

	@Inject
	private MoMoroutinginvcService service;

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
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKv()));
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
		MoMoroutinginvc moMoroutinginvc=service.findById(getLong(0));
		if(moMoroutinginvc == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moMoroutinginvc",moMoroutinginvc);
		render("edit.html");
	}

	/**
	 * 保存
	 */
	public void save() {
		renderJson(service.save(getModel(MoMoroutinginvc.class, "moMoroutinginvc")));
	}

	/**
	 * 更新
	 */
	public void update() {
		renderJson(service.update(getModel(MoMoroutinginvc.class, "moMoroutinginvc")));
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


	public void dataList() {
		renderJsonData(service.dataList(getKv()));
	}


}
