package cn.rjtech.admin.projectcard;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ProjectCard;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 项目卡片 Controller
 * @ClassName: ProjectCardAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-09 11:11
 */
@CheckPermission(PermissionKey.NONE)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/projectcard", viewPath = "/_view/admin/projectcard")
public class ProjectCardAdminController extends BaseAdminController {

	@Inject
	private ProjectCardService service;

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
		ProjectCard projectCard = service.findById(useIfPresent(getLong(0)));
		ValidationUtils.notNull(projectCard, JBoltMsg.DATA_NOT_EXIST);
		set("projectCard", projectCard);
		render("edit.html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(useIfValid(ProjectCard.class, "projectCard")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(useIfValid(ProjectCard.class, "projectCard")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}
	/**
	 * 切换是否冻结
	 * */
	public void toggleIsfreeze(){
		renderJson(service.toggleIsfreeze(getLong(0)));
	}
	/**
	 * 切换是否完成
	 * */
	public void toggleIsFinish(){
		String ccode = getKv().getStr("ccode");
		Integer istatus = getKv().getInt("istatus");
		renderJson(service.toggleIsFinish(ccode,istatus));
	}
}
