package cn.rjtech.admin.manualorderdqty;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ManualorderdQty;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;
/**
 * 客户订单-手配订单数量记录
 * @ClassName: ManualorderdQtyAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-07-12 11:06
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/manualorderdQty", viewPath = "/_view/admin/manualorderdqty")
public class ManualorderdQtyAdminController extends BaseAdminController {

	@Inject
	private ManualorderdQtyService service;
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
    @TxConfig(ManualorderdQty.DATASOURCE_CONFIG_NAME)
	public void save(@Para("manualorderdQty")ManualorderdQty manualorderdQty) {
		renderJson(service.save(manualorderdQty));
	}

   /**
	* 编辑
	*/
	public void edit() {
		ManualorderdQty manualorderdQty=service.findById(getLong(0));
		if(manualorderdQty == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("manualorderdQty",manualorderdQty);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(ManualorderdQty.DATASOURCE_CONFIG_NAME)
	public void update(@Para("manualorderdQty")ManualorderdQty manualorderdQty) {
		renderJson(service.update(manualorderdQty));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
    @TxConfig(ManualorderdQty.DATASOURCE_CONFIG_NAME)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(ManualorderdQty.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}


}
