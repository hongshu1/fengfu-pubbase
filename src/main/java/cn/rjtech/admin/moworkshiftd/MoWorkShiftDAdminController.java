package cn.rjtech.admin.moworkshiftd;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.MoWorkShiftD;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;
/**
 * 制造工单-指定日期班次人员信息明细
 * @ClassName: MoWorkShiftDAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-29 15:37
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/moWorkShiftD", viewPath = "/_view/admin/moworkshiftd")
public class MoWorkShiftDAdminController extends BaseAdminController {

	@Inject
	private MoWorkShiftDService service;
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
    @TxConfig(MoWorkShiftD.DATASOURCE_CONFIG_NAME)
	public void save(@Para("moWorkShiftD")MoWorkShiftD moWorkShiftD) {
		renderJson(service.save(moWorkShiftD));
	}

   /**
	* 编辑
	*/
	public void edit() {
		MoWorkShiftD moWorkShiftD=service.findById(getLong(0));
		if(moWorkShiftD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("moWorkShiftD",moWorkShiftD);
		render("edit.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(MoWorkShiftD.DATASOURCE_CONFIG_NAME)
	public void update(@Para("moWorkShiftD")MoWorkShiftD moWorkShiftD) {
		renderJson(service.update(moWorkShiftD));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
    @TxConfig(MoWorkShiftD.DATASOURCE_CONFIG_NAME)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(MoWorkShiftD.DATASOURCE_CONFIG_NAME)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}


}
