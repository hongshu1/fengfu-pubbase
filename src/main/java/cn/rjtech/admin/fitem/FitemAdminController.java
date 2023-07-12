package cn.rjtech.admin.fitem;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Fitem;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;
/**
 * 基础档案-项目大类
 * @ClassName: FitemAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-07-03 11:02
 */
@Before(JBoltAdminAuthInterceptor.class)
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.FITEM)
@Path(value = "/admin/fitem", viewPath = "/_view/admin/fitem")
public class FitemAdminController extends BaseAdminController {

	@Inject
	private FitemService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(),getKv()));
	}

	/**
	 * 获取项目大类
	 */
	 public void selectFitem(){
		 renderJsonData(service.selectFitem());
	 }

	/**
	 * 树结构数据源
	 */
	public void mgrTree() {
		renderJsonData(service.getMgrTree( getInt("openLevel", 0),get("sn",null)));
	}


	public void fitemTable(){
		render("_table_fitem.html");
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
	@Before(Tx.class)
    @TxConfig(Fitem.DATASOURCE_CONFIG_NAME)
	public void save(@Para("fitem")Fitem fitem) {
		renderJson(service.save(fitem));
	}

   /**
	* 编辑
	*/
	public void edit() {
		Fitem fitem=service.findById(getLong(0));
		if(fitem == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("fitem",fitem);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(Fitem.DATASOURCE_CONFIG_NAME)
	public void update(@Para("fitem")Fitem fitem) {
		renderJson(service.update(fitem));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
    @TxConfig(Fitem.DATASOURCE_CONFIG_NAME)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(Fitem.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

   /**
	* 得到树形结构数据
	*/
/*
	public void tree() {
		renderJsonData(service.getTreeDatas());
	}
*/

   /**
	* 切换isDeleted
	*/
	@Before(Tx.class)
    @TxConfig(Fitem.DATASOURCE_CONFIG_NAME)
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}


}
