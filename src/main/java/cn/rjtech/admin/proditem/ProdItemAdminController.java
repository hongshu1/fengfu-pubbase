package cn.rjtech.admin.proditem;

import cn.rjtech.admin.prodparam.ProdParamService;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ProdItem;
/**
 * 生产表单建模-生产项目
 * @ClassName: ProdItemAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-26 15:25
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/prodItem", viewPath = "/_view/admin/proditem")
@CheckPermission(PermissionKey.PRODITEM)
public class ProdItemAdminController extends BaseAdminController {

	@Inject
	private ProdItemService service;

	@Inject
	private ProdParamService prodparamService;
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
		renderJsonData(service.pageList(getKv()));

		//renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isDeleted")));
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
    @TxConfig(ProdItem.DATASOURCE_CONFIG_NAME)
	public void save(@Para("prodItem")ProdItem prodItem) {
		renderJson(service.save(prodItem));
	}

   /**
	* 编辑
	*/
	public void edit() {
		ProdItem prodItem=service.findById(getLong(0));
		if(prodItem == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("prodItem",prodItem);
		render("edit.html");
	}

	public void options() {
		renderJsonData(service.options());
	}
   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(ProdItem.DATASOURCE_CONFIG_NAME)
	public void update(@Para("prodItem")ProdItem prodItem) {
		renderJson(service.update(prodItem));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
    @TxConfig(ProdItem.DATASOURCE_CONFIG_NAME)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(ProdItem.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

   /**
	* 切换isDeleted
	*/
	@Before(Tx.class)
    @TxConfig(ProdItem.DATASOURCE_CONFIG_NAME)
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}


}
