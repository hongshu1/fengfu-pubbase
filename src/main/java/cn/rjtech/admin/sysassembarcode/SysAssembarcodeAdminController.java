package cn.rjtech.admin.sysassembarcode;

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
import cn.rjtech.model.momdata.SysAssembarcode;
/**
 * 形态转换单条码
 * @ClassName: SysAssembarcodeAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-07-08 09:26
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/sysAssembarcode", viewPath = "/_view/admin/sysassembarcode")
public class SysAssembarcodeAdminController extends BaseAdminController {

	@Inject
	private SysAssembarcodeService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isDeleted")));
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
    @TxConfig(SysAssembarcode.DATASOURCE_CONFIG_NAME)
	public void save(@Para("sysAssembarcode")SysAssembarcode sysAssembarcode) {
		renderJson(service.save(sysAssembarcode));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SysAssembarcode sysAssembarcode=service.findById(getLong(0));
		if(sysAssembarcode == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("sysAssembarcode",sysAssembarcode);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(SysAssembarcode.DATASOURCE_CONFIG_NAME)
	public void update(@Para("sysAssembarcode")SysAssembarcode sysAssembarcode) {
		renderJson(service.update(sysAssembarcode));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
    @TxConfig(SysAssembarcode.DATASOURCE_CONFIG_NAME)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(SysAssembarcode.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

   /**
	* 切换isDeleted
	*/
	@Before(Tx.class)
    @TxConfig(SysAssembarcode.DATASOURCE_CONFIG_NAME)
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}


}
