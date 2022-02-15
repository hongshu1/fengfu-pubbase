package cn.jbolt._admin.topnav;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.tx.Tx;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.model.Topnav;
import cn.jbolt.core.permission.CheckPermission;
/**
 * 
 * @ClassName: TopnavAdminController   
 * @author: JFinal学院-小木
 * @date: 2020-08-28 00:48  
 */
@CheckPermission(PermissionKey.TOPNAV)
public class TopnavAdminController extends JBoltBaseController {

	@Inject
	private TopnavService service;
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
		renderJsonData(service.getAdminList(getKeywords()));
	}
	
   /**
	* options 数据源
	*/
	public void options() {
		renderJsonData(service.getOptionList());
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
		Topnav topnav=service.findById(getLong(0)); 
		if(topnav == null){
			renderDialogFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("topnav",topnav);
		render("edit.html");
	}
	
  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(Topnav.class, "topnav")));
	}
	
  /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(Topnav.class, "topnav")));
	}
	
  /**
	* 删除
	*/
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}
	
  /**
	* 排序 上移
	*/
	@Before(Tx.class)
	public void up() {
		renderJson(service.up(getLong(0)));
	}
	
  /**
	* 排序 下移
	*/
	@Before(Tx.class)
	public void down() {
		renderJson(service.down(getLong(0)));
	}
	
  /**
	* 排序 初始化
	*/
	@Before(Tx.class)
	public void initRank() {
		renderJson(service.initRank());
	}
	
  /**
	* 切换启用状态
	*/
	public void toggleEnable() {
		renderJson(service.toggleEnable(getLong(0)));
	}
	
	
}