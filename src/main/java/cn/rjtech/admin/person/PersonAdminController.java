package cn.rjtech.admin.person;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.Person;
/**
 * 人员档案 Controller
 * @ClassName: PersonAdminController
 * @author: WYX
 * @date: 2023-03-21 15:11
 */
@CheckPermission(PermissionKey.PERSON_INDEX)
@UnCheckIfSystemAdmin
public class PersonAdminController extends BaseAdminController {

	@Inject
	private PersonService service;

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
		Person person=service.findById(getLong(0)); 
		if(person == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("person",person);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(Person.class, "person")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Person.class, "person")));
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
	* 切换toggleBPsnPerson
	*/
	public void toggleBPsnPerson() {
		renderJson(service.toggleBPsnPerson(getLong(0)));
	}

  /**
	* 切换toggleBProbation
	*/
	public void toggleBProbation() {
		renderJson(service.toggleBProbation(getLong(0)));
	}

  /**
	* 切换toggleBDutyLock
	*/
	public void toggleBDutyLock() {
		renderJson(service.toggleBDutyLock(getLong(0)));
	}

  /**
	* 切换toggleBpsnshop
	*/
	public void toggleBpsnshop() {
		renderJson(service.toggleBpsnshop(getLong(0)));
	}

  /**
	* 切换toggleIsEnabled
	*/
	public void toggleIsEnabled() {
		renderJson(service.toggleIsEnabled(getLong(0)));
	}

  /**
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}


}
