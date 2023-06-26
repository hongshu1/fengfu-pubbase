package cn.rjtech.admin.prodparam;

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
import cn.rjtech.model.momdata.ProdParam;
/**
 * 生产表单建模-生产表单参数
 * @ClassName: ProdParamAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-26 13:29
 */
@Before(JBoltAdminAuthInterceptor.class)
@CheckPermission(PermissionKey.PRODPARAM)
@Path(value = "/admin/prodParam", viewPath = "/_view/admin/prodparam")
public class ProdParamAdminController extends BaseAdminController {

	@Inject
	private ProdParamService service;
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
		//renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isEnabled"), getBoolean("isDeleted")));
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
    @TxConfig(ProdParam.DATASOURCE_CONFIG_NAME)
	public void save(@Para("prodParam")ProdParam prodParam) {
		renderJson(service.save(prodParam));
	}

   /**
	* 编辑
	*/
	public void edit() {
		ProdParam prodParam=service.findById(getLong(0));
		if(prodParam == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("prodParam",prodParam);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(ProdParam.DATASOURCE_CONFIG_NAME)
	public void update(@Para("prodParam")ProdParam prodParam) {
		renderJson(service.update(prodParam));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
    @TxConfig(ProdParam.DATASOURCE_CONFIG_NAME)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(ProdParam.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

   /**
	* 切换isEnabled
	*/
	@Before(Tx.class)
    @TxConfig(ProdParam.DATASOURCE_CONFIG_NAME)
	public void toggleIsEnabled() {
	    renderJson(service.toggleBoolean(getLong(0),"isEnabled"));
	}

   /**
	* 切换isDeleted
	*/
	@Before(Tx.class)
    @TxConfig(ProdParam.DATASOURCE_CONFIG_NAME)
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}


}
