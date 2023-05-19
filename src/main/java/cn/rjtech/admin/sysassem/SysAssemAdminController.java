package cn.rjtech.admin.sysassem;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.SysAssem;
/**
 * 组装拆卸及形态转换单
 * @ClassName: SysAssemAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 09:45
 */
@CheckPermission(PermissionKey.FORMCONVERSIONLIST)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/formConversionList", viewPath = "/_view/admin/sysAssem")
public class SysAssemAdminController extends BaseAdminController {

	@Inject
	private SysAssemService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
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
	public void save() {
		renderJson(service.save(getModel(SysAssem.class, "sysAssem")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SysAssem sysAssem=service.findById(getLong(0));
		if(sysAssem == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("sysAssem",sysAssem);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(SysAssem.class, "sysAssem")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteRmRdByIds(get("ids")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}

   /**
	* 切换IsDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"IsDeleted"));
	}

	/**
	 * 新增-可编辑表格-批量提交
	 */
	@Before(Tx.class)
	public void submitAll() {
		renderJson(service.submitByJBoltTable(getJBoltTable()));
	}

	//获取转换方式数据源 jb_dictionary 存id
	public void dictionary(){
		renderJsonData(service.getdictionary(getKv()));
	}

	//获取出入库类别数据源 Bd_Rd_Style 存 cRdCode; 路径拼接 brdflag=0(发),1(收)
	public void style(){
		renderJsonData(service.style(getKv()));
	}

	//获取现品下拉以及转换后的 数据源
	public void barcodeDatas() {
		String orgCode =  getOrgCode();
		renderJsonData(service.getBarcodeDatas(get("q"), getInt("limit",10),get("orgCode",orgCode)));
	}



}
