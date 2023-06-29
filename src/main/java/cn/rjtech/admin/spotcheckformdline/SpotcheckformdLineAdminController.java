package cn.rjtech.admin.spotcheckformdline;

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
import cn.rjtech.model.momdata.SpotcheckformdLine;
/**
 * 制造管理-点检表明细列值
 * @ClassName: SpotcheckformdLineAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-29 09:17
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/spotcheckformdLine", viewPath = "/_view/admin/spotcheckformdline")
public class SpotcheckformdLineAdminController extends BaseAdminController {

	@Inject
	private SpotcheckformdLineService service;
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
    @TxConfig(SpotcheckformdLine.DATASOURCE_CONFIG_NAME)
	public void save(@Para("spotcheckformdLine")SpotcheckformdLine spotcheckformdLine) {
		renderJson(service.save(spotcheckformdLine));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SpotcheckformdLine spotcheckformdLine=service.findById(getLong(0));
		if(spotcheckformdLine == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("spotcheckformdLine",spotcheckformdLine);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(SpotcheckformdLine.DATASOURCE_CONFIG_NAME)
	public void update(@Para("spotcheckformdLine")SpotcheckformdLine spotcheckformdLine) {
		renderJson(service.update(spotcheckformdLine));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
    @TxConfig(SpotcheckformdLine.DATASOURCE_CONFIG_NAME)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(SpotcheckformdLine.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}


}
