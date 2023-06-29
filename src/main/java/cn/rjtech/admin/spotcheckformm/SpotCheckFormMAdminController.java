package cn.rjtech.admin.spotcheckformm;

import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
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
import cn.rjtech.model.momdata.SpotCheckFormM;
/**
 * 始业点检表管理
 * @ClassName: SpotCheckFormMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-29 09:16
 */
@CheckPermission(PermissionKey.SPOTCHECKFORMM)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/spotCheckFormM", viewPath = "/_view/admin/spotcheckformm")
public class SpotCheckFormMAdminController extends BaseAdminController {

	@Inject
	private SpotCheckFormMService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getInt("iType"), getBoolean("IsDeleted")));
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
    @TxConfig(SpotCheckFormM.DATASOURCE_CONFIG_NAME)
	public void save(@Para("spotCheckFormM")SpotCheckFormM spotCheckFormM) {
		renderJson(service.save(spotCheckFormM));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SpotCheckFormM spotCheckFormM=service.findById(getLong(0));
		if(spotCheckFormM == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("spotCheckFormM",spotCheckFormM);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(SpotCheckFormM.DATASOURCE_CONFIG_NAME)
	public void update(@Para("spotCheckFormM")SpotCheckFormM spotCheckFormM) {
		renderJson(service.update(spotCheckFormM));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
    @TxConfig(SpotCheckFormM.DATASOURCE_CONFIG_NAME)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(SpotCheckFormM.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}


}
