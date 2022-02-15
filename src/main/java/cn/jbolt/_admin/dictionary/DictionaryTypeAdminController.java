package cn.jbolt._admin.dictionary;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.tx.Tx;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltPageSize;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.model.DictionaryType;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;

/**
 * 数据字典类型管理Controller
 * @ClassName:  DictionaryTypeAdminController   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2019年11月9日   
 */
@CheckPermission({PermissionKey.DICTIONARY})
@UnCheckIfSystemAdmin
public class DictionaryTypeAdminController extends JBoltBaseController {
	@Inject
	private DictionaryTypeService service;
	/**
	 * 加载管理portal
	 */
//	public void mgr(){
//		set("dictionaryTypes",service.findAll());
//		render("mgrportal.html");
//	}
	/**
	 * 管理页面分页读取数据源
	 */
	public void datas(){
		renderJsonData(service.paginateByKeywords("id","desc", getPageNumber(), getPageSize(JBoltPageSize.PAGESIZE_ADMIN_LIST_15), getKeywords(), "name,type_key"));
	}
	
	/**
	 * 低代码模块专用
	 */
	public void codeGenOptions(){
		renderJsonData(service.getOptionList("name","type_key"));
	}
	
	public void add(){
		render("add.html");
	}
	public void edit(){
		Long id=getLong(0);
		if(notOk(id)){
			renderFormFail("数据不存在");
			return;
		}
		DictionaryType type=service.findById(id);
		if(type==null){
			if(notOk(id)){
				renderFormFail("数据不存在");
				return;
			}
		}
		set("dictionaryType",type);
		render("edit.html");
	}
	public void save(){
		renderJson(service.save(getModel(DictionaryType.class, "dictionaryType")));
	}
	@Before(Tx.class)
	public void update(){
		renderJson(service.update(getModel(DictionaryType.class, "dictionaryType")));
	}
	public void delete(){
		renderJson(service.delete(getLong(0)));
	}
}
