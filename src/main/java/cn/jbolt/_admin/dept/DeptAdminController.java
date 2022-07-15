package cn.jbolt._admin.dept;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.tx.Tx;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.model.Dept;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
/**
 * 
 * @ClassName: DeptAdminController   
 * @author: JFinal学院-小木
 * @date: 2021-02-07 20:34  
 */
@CheckPermission(PermissionKey.DEPT)
@UnCheckIfSystemAdmin
public class DeptAdminController extends JBoltBaseController {

	@Inject
	private DeptService service;
	
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
		renderJsonData(service.getTreeTableDatas());
	}
	/**
	 * select数据源
	 */
	@UnCheck
	public void options() {
		renderJsonData(service.getTreeDatas(false,true));
	}
	/**
	 * select数据源 只需要enable=true的
	 */
	@UnCheck
	public void enableOptions() {
		renderJsonData(service.getTreeDatas(true,true));
	}
  /**
	* JSTree树形数据源
	*/
	@UnCheck
	public void enableJsTreeDatas() {
		renderJsonData(service.getEnableJsTree(getLong("selectId"),getInt("openLevel",0)));
	}

  /**
	* 新增
	*/
	public void add() {
		set("pid", getLong(0,0L));
		render("add.html");
	}
	
  /**
	* 编辑
	*/
	public void edit() {
		Dept dept=service.findById(getLong(0)); 
		if(dept == null){
			renderDialogFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("dept",dept);
		render("edit.html");
	}
	
  /**
	* 保存
	*/
	@Before(Tx.class)
	public void save() {
		renderJson(service.save(getModel(Dept.class, "dept")));
	}
	
  /**
	* 更新
	*/
	@Before(Tx.class)
	public void update() {
		renderJson(service.update(getModel(Dept.class, "dept")));
	}
	
  /**
	* 删除
	*/
	@Before(Tx.class)
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}
	
  /**
	* 切换启用状态
	*/
	@Before(Tx.class)
	public void toggleEnable() {
		renderJson(service.toggleEnable(getLong(0)));
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
	 * 处理所有部门的deptPath
	 */
	@Before(Tx.class)
	public void processAllDeptPath() {
		service.processAllDeptPath();
		renderJsonSuccess();
	}
	
}