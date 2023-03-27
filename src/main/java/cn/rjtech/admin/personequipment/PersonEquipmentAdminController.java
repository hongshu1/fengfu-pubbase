package cn.rjtech.admin.personequipment;

import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;

import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.PersonEquipment;
/**
 * 人员设备档案 Controller
 * @ClassName: PersonEquipmentAdminController
 * @author: WYX
 * @date: 2023-03-22 15:36
 */
@UnCheck
@UnCheckIfSystemAdmin
public class PersonEquipmentAdminController extends BaseAdminController {

	@Inject
	private PersonEquipmentService service;

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
		PersonEquipment personEquipment=service.findById(getLong(0)); 
		if(personEquipment == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("personEquipment",personEquipment);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(PersonEquipment.class, "personEquipment")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(PersonEquipment.class, "personEquipment")));
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
	* 切换toggleIsDeleted
	*/
	public void toggleIsDeleted() {
		renderJson(service.toggleIsDeleted(getLong(0)));
	}
	
	/**
	 * 查询可编辑表格数据 
	 * */
	public void findEditableDatas(@Para(value="iPersonId") Long iPersonId){
		renderJsonData(service.findEditableDatas(iPersonId));
	}

}
