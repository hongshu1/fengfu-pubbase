package cn.rjtech.admin.spotcheckformd;

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
import cn.rjtech.model.momdata.SpotCheckFormD;
/**
 * 制造管理-点检表单行配置
 * @ClassName: SpotCheckFormDAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-29 09:17
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/spotCheckFormD", viewPath = "/_view/admin/spotcheckformd")
public class SpotCheckFormDAdminController extends BaseAdminController {

	@Inject
	private SpotCheckFormDService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getInt("iType")));
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
    @TxConfig(SpotCheckFormD.DATASOURCE_CONFIG_NAME)
	public void save(@Para("spotCheckFormD")SpotCheckFormD spotCheckFormD) {
		renderJson(service.save(spotCheckFormD));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SpotCheckFormD spotCheckFormD=service.findById(getLong(0));
		if(spotCheckFormD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("spotCheckFormD",spotCheckFormD);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(SpotCheckFormD.DATASOURCE_CONFIG_NAME)
	public void update(@Para("spotCheckFormD")SpotCheckFormD spotCheckFormD) {
		renderJson(service.update(spotCheckFormD));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
    @TxConfig(SpotCheckFormD.DATASOURCE_CONFIG_NAME)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(SpotCheckFormD.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}


}
