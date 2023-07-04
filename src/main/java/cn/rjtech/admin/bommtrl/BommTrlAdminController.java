package cn.rjtech.admin.bommtrl;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.BommTrl;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;
/**
 * 物料建模-BOM扩展
 * @ClassName: BommTrlAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-07-04 16:16
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/bommTrl", viewPath = "/_view/admin/bommtrl")
public class BommTrlAdminController extends BaseAdminController {

	@Inject
	private BommTrlService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords()));
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
    @TxConfig(BommTrl.DATASOURCE_CONFIG_NAME)
	public void save(@Para("bommTrl")BommTrl bommTrl) {
		renderJson(service.save(bommTrl));
	}

   /**
	* 编辑
	*/
	public void edit() {
		BommTrl bommTrl=service.findById(getLong(0));
		if(bommTrl == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("bommTrl",bommTrl);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(BommTrl.DATASOURCE_CONFIG_NAME)
	public void update(@Para("bommTrl")BommTrl bommTrl) {
		renderJson(service.update(bommTrl));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
    @TxConfig(BommTrl.DATASOURCE_CONFIG_NAME)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(BommTrl.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}


}
