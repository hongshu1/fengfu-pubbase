package cn.rjtech.admin.bomdata;

import cn.jbolt.core.permission.UnCheck;
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
import cn.rjtech.model.momdata.BomData;
/**
 * 物料建模-BOM数据
 * @ClassName: BomDataAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-15 21:37
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/bomData", viewPath = "/_view/admin/bomdata")
public class BomDataAdminController extends BaseAdminController {

	@Inject
	private BomDataService service;
   /**
	* 首页
	*/
	public void index() {
		render("index.html");
	}
   /**
	* 数据源
	*/
   @UnCheck
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
    @TxConfig(BomData.DATASOURCE_CONFIG_NAME)
	public void save(@Para("bomData")BomData bomData) {
		renderJson(service.save(bomData));
	}

   /**
	* 编辑
	*/
	public void edit() {
		BomData bomData=service.findById(getLong(0));
		if(bomData == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("bomData",bomData);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(BomData.DATASOURCE_CONFIG_NAME)
	public void update(@Para("bomData")BomData bomData) {
		renderJson(service.update(bomData));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
    @TxConfig(BomData.DATASOURCE_CONFIG_NAME)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(BomData.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}


}
