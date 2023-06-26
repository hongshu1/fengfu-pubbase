package cn.rjtech.admin.sysassem;

import cn.hutool.core.util.ArrayUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Department;
import cn.rjtech.model.momdata.SysAssem;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.List;

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

	@Inject
	private DepartmentService departmentservice;

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
		//todo 待优化
		Kv kv = new Kv();
		kv.set("id",sysAssem.getBillType());
		List<Record> getdictionary = service.getdictionary(kv);
		if(!ArrayUtil.isEmpty(getdictionary)){
			set("zhname",getdictionary.get(0).get("name")==null ? "":getdictionary.get(0).get("name"));
		}

		Kv kv1 = new Kv();
		kv1.set("crdcode",sysAssem.getIRdCode());
		List<Record> style = service.style(kv1);
		if(!ArrayUtil.isEmpty(style)){
			set("rkname",style.get(0).get("crdname"));
		}

		Kv kv2 = new Kv();
		kv2.set("crdcode",sysAssem.getORdCode());
		List<Record> style1 = service.style(kv2);
		if(!ArrayUtil.isEmpty(style1)){
			set("ckname",style1.get(0).get("crdname"));
		}

		Department first1 = departmentservice.findFirst("select * from Bd_Department where cDepCode =?", sysAssem.getDeptCode());
		set("cdepname",first1.getCDepName());

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

	/**
	 * 获取资源
	 */
	public void getResource(){
		String q = get("q");
		if (notOk(q)){
			renderJsonSuccess();
			return;
		}
		String itemHidden = get("itemHidden");
		String groupCode = get("groupCode");
		String sourceBillType = get("sourceBillType");
		Kv kv = new Kv();
		kv.set("keywords",q);
		kv.setIfNotNull("sourceBillType", sourceBillType);
		kv.setIfNotNull("combination", groupCode);
		kv.setIfNotNull("itemHidden", itemHidden);
		kv.setIfNotNull("assemtype", "转换前");
		renderJsonData(service.getResource(kv));
	}

	/**
	 * 查询双单位条码数据
	 */
	public void getBarCodeData(){
		//存货编码
		String itemCode = get("itemCode");
		//供应商
		String supplier = get("supplier");
		//单据类型
		String sourceBillType = get("sourceBillType");
		//转换前 存货主键
		String ibeforeinventoryid = get("ibeforeinventoryid");
		String trJsonData = get("trjsondata");
		Kv kv = new Kv();
		kv.set("itemCode",itemCode);
		kv.setIfNotNull("sourceBillType", sourceBillType);
		kv.set("supplier",notOk(supplier)?' ':supplier);
		kv.set("ibeforeinventoryid",notOk(ibeforeinventoryid)?' ':ibeforeinventoryid);
		kv.set("trJsonData",notOk(trJsonData)?' ':trJsonData);
		renderJsonData(service.getBarcodeDatas(kv));
	}


}
