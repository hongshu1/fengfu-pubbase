package cn.rjtech.admin.subcontractsaleorderdqty;

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
import cn.rjtech.model.momdata.SubcontractsaleorderdQty;
/**
 * 客户订单-委外订单数量记录
 * @ClassName: SubcontractsaleorderdQtyAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-07-12 11:05
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/subcontractsaleorderdQty", viewPath = "/_view/admin/subcontractsaleorderdqty")
public class SubcontractsaleorderdQtyAdminController extends BaseAdminController {

	@Inject
	private SubcontractsaleorderdQtyService service;
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
    @TxConfig(SubcontractsaleorderdQty.DATASOURCE_CONFIG_NAME)
	public void save(@Para("subcontractsaleorderdQty")SubcontractsaleorderdQty subcontractsaleorderdQty) {
		renderJson(service.save(subcontractsaleorderdQty));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SubcontractsaleorderdQty subcontractsaleorderdQty=service.findById(getLong(0));
		if(subcontractsaleorderdQty == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("subcontractsaleorderdQty",subcontractsaleorderdQty);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(SubcontractsaleorderdQty.DATASOURCE_CONFIG_NAME)
	public void update(@Para("subcontractsaleorderdQty")SubcontractsaleorderdQty subcontractsaleorderdQty) {
		renderJson(service.update(subcontractsaleorderdQty));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
    @TxConfig(SubcontractsaleorderdQty.DATASOURCE_CONFIG_NAME)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(SubcontractsaleorderdQty.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}


}
