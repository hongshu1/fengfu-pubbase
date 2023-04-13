package cn.rjtech.admin.department;

import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.model.momdata.Person;
import cn.rjtech.util.Util;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.Department;

import java.util.List;

/**
 * 组织建模-部门档案
 * @ClassName: DepartmentAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 11:55
 */
@Before(JBoltAdminAuthInterceptor.class)
@CheckPermission(PermissionKey.DEPARTMENT)
@UnCheckIfSystemAdmin
@Path(value = "/admin/department", viewPath = "/_view/admin/department")
public class DepartmentAdminController extends BaseAdminController {

	@Inject
	private DepartmentService service;
	@Inject
	private PersonService personService;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv(), getSortColumn("iAutoId"), getSortType("desc")));
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
   @CheckPermission(PermissionKey.DEPARTMENT_ADD)
	public void save() {
		renderJson(service.save(getModel(Department.class, "department")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		Department department=service.findById(getLong(0));
		if(department == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		if (ObjectUtil.isNotNull(department.getIDutyUserId())){
			Person person = personService.findById(department.getIDutyUserId());
			if (ObjectUtil.isNotNull(person)){
				set("cdeppersonname", person.getCpsnName());
			}
		}
		if (ObjectUtil.isNotNull(department.getCDepLeader())){
			Person person = personService.findById(department.getCDepLeader());
			if (ObjectUtil.isNotNull(person)){
				set("cdepleadername", person.getCpsnName());
			}
		}
		if (ObjectUtil.isNotNull(department.getIPid())){
			Department pDepartment = service.findById(department.getIPid());
			if (ObjectUtil.isNotNull(pDepartment)){
				set("pname", pDepartment.getCDepName());
			}
		}
		set("department",department);
		render("edit.html");
	}

   /**
	* 更新
	*/
   @CheckPermission(PermissionKey.DEPARTMENT_EDIT)
	public void update() {
		renderJson(service.update(getModel(Department.class, "department")));
	}

   /**
	* 批量删除
	*/
   @CheckPermission(PermissionKey.DEPARTMENT_DEL)
	public void deleteByIds() {
	   renderJson(service.delByIds(get("ids")));
	}

   /**
	* 删除
	*/
   @CheckPermission(PermissionKey.DEPARTMENT_DEL)
	public void delete() {
	   Department department = service.findById(getLong(0));
	   ValidationUtils.notNull(department, JBoltMsg.DATA_NOT_EXIST);
	   service.checkRepetitionData(true, "ipid", get(0), "该部门存在下级部门，不允许删除");
	   department.setIsDeleted(true);
	   renderJson(service.update(department));
	}

   /**
	* 切换bDepEnd
	*/
	public void toggleBDepEnd() {
	    renderJson(service.toggleBoolean(getLong(0),"bDepEnd"));
	}

   /**
	* 切换isEnabled
	*/
	public void toggleIsEnabled() {
	    renderJson(service.toggleBoolean(getLong(0),"isEnabled"));
	}

   /**
	* 切换isApsInvoled
	*/
	public void toggleIsApsInvoled() {
	    renderJson(service.toggleBoolean(getLong(0),"isApsInvoled"));
	}

   /**
	* 切换isDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}
	
	/**
	 * 人员列表
	 */
	public void personTable() {
		render("person_table.html");
	}
	
	public void findPersonPage(){
		renderJsonData(personService.paginateDatas(getPageNumber(), getPageSize(), getKv()));
	}
	
	public void findPersonAll(){
		renderJsonData(personService.findAll(getKv()));
	}
	
	public void findParentData(Long excludeId){
		renderJsonData(service.findParentData(excludeId));
	}
	
	public void exportExcelByIds() throws Exception {
		String ids = get("ids");
		if (notOk(ids)) {
			renderJsonFail("未选择有效数据，无法导出");
			return;
		}
		List<Record> data = service.findAll(Kv.create().setIfNotBlank("ids", Util.getInSqlByIds(ids)));
		if (notOk(data)) {
			renderJsonFail("无有效数据导出");
			return;
		}
		renderBytesToExcelXlsFile(service.exportExcelTpl(data));
	}
	
	@SuppressWarnings("unchecked")
	public void exportExcelAll() throws Exception {
		List<Record> rows = service.findAll(getKv());
		if (notOk(rows)) {
			renderJsonFail("无有效数据导出");
			return;
		}
		renderBytesToExcelXlsFile(service.exportExcelTpl(rows));
	}
	
	
	
	@SuppressWarnings("unchecked")
	public void downloadTpl() throws Exception {
		renderBytesToExcelXlsFile(service.exportExcelTpl(null));
	}
	
	public void getTreeTableDatas(){
		List<Record> datas = service.findAll(getKv());
		List<Record> trees = service.convertToRecordTree(datas, Department.IAUTOID, Department.IPID, (p) -> {
			return this.notOk(p.getLong(Department.IPID));
		});
		renderJsonData(trees);
	}

	public void options(){
		renderJsonData(service.findAll());
	}

	/**
	 * 部门数据源
	 */
	public void list() {
		Okv kv = Okv.create();
		kv.set("IsEnabled", 1);
		kv.set("IsDeleted", 0);

		renderJsonData(service.getCommonList(kv, "dCreateTime", "desc"));	}
}
