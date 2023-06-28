package cn.rjtech.admin.prodformdline;

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
import cn.rjtech.model.momdata.ProdformdLine;
/**
 * 制造管理-生产表单明细列值
 * @ClassName: ProdformdLineAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-28 18:59
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/prodformdLine", viewPath = "/_view/admin/prodformdline")
public class ProdformdLineAdminController extends BaseAdminController {

	@Inject
	private ProdformdLineService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize()));
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
    @TxConfig(ProdformdLine.DATASOURCE_CONFIG_NAME)
	public void save(@Para("prodformdLine")ProdformdLine prodformdLine) {
		renderJson(service.save(prodformdLine));
	}

   /**
	* 编辑
	*/
	public void edit() {
		ProdformdLine prodformdLine=service.findById(getLong(0));
		if(prodformdLine == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("prodformdLine",prodformdLine);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(ProdformdLine.DATASOURCE_CONFIG_NAME)
	public void update(@Para("prodformdLine")ProdformdLine prodformdLine) {
		renderJson(service.update(prodformdLine));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
    @TxConfig(ProdformdLine.DATASOURCE_CONFIG_NAME)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(ProdformdLine.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}


}
