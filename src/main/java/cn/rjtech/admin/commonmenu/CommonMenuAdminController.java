package cn.rjtech.admin.commonmenu;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.UnCheck;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.common.model.CommonMenu;
/**
 * 常用菜单 Controller
 * @ClassName: CommonMenuAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-19 21:02
 */
@UnCheck
@Path(value = "/admin/commonmenu", viewPath = "/_view/admin/commonmenu")
public class CommonMenuAdminController extends BaseAdminController {

	@Inject
	private CommonMenuService service;

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
		CommonMenu commonMenu=service.findById(getLong(0)); 
		if(commonMenu == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("commonMenu",commonMenu);
		render("edit.html");
	}

  /**
	* 保存
	*/
    @Before(Tx.class)
	public void save() {
		renderJson(service.save(getModel(CommonMenu.class, "commonMenu")));
	}

   /**
	* 更新
	*/
    @Before(Tx.class)
	public void update() {
		renderJson(service.update(getModel(CommonMenu.class, "commonMenu")));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}

   /**
	* 删除
	*/
    @Before(Tx.class)
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}

    public void findAllCanCheckedMenu(){
    	renderJsonData(service.findAllCanCheckedMenu());
    }
    
	public void findCheckedIds(){
		renderJson(service.findCheckedIds());
	}
	
	public void saveTableDatas(){
		renderJson(service.saveTableDatas(getKv()));
	}
}
