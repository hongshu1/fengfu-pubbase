package cn.rjtech.admin.specmaterialsrcvd;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SpecMaterialsRcvD;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;
/**
 * 制造工单-特殊领料单明细
 * @ClassName: SpecMaterialsRcvDAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-30 14:46
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/specMaterialsRcvD", viewPath = "/_view/admin/specmaterialsrcvd")
public class SpecMaterialsRcvDAdminController extends BaseAdminController {

	@Inject
	private SpecMaterialsRcvDService service;
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
    @TxConfig(SpecMaterialsRcvD.DATASOURCE_CONFIG_NAME)
	public void save(@Para("specMaterialsRcvD")SpecMaterialsRcvD specMaterialsRcvD) {
		renderJson(service.save(specMaterialsRcvD));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SpecMaterialsRcvD specMaterialsRcvD=service.findById(getLong(0));
		if(specMaterialsRcvD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("specMaterialsRcvD",specMaterialsRcvD);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(SpecMaterialsRcvD.DATASOURCE_CONFIG_NAME)
	public void update(@Para("specMaterialsRcvD")SpecMaterialsRcvD specMaterialsRcvD) {
		renderJson(service.update(specMaterialsRcvD));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
    @TxConfig(SpecMaterialsRcvD.DATASOURCE_CONFIG_NAME)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(SpecMaterialsRcvD.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}


}
