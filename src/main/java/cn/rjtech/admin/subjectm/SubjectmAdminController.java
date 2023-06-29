package cn.rjtech.admin.subjectm;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.para.jbolttablemenufilter.JBoltTableMenuFilter;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.rjtech.admin.subjectd.SubjectdService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Subjectm;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 科目对照主表 Controller
 * @ClassName: SubjectmAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-01 13:53
 */
@CheckPermission(PermissionKey.SUBJECTM)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/subjectm", viewPath = "/_view/admin/subjectm")
public class SubjectmAdminController extends BaseAdminController {

	@Inject
	private SubjectmService service;
	@Inject
	private SubjectdService subjectdService;

   /**
	* 首页
	*/
	public void index() {
		Subjectm subjectm = service.findFirst("select * from Bas_SubjectM where isenabled =1");
		if(subjectm != null) set("cversion",subjectm.getCVersion());
		else set("cversion",JBoltDateUtil.getNowStr("yyyy"));
		render("index.html");
	}

  	/**
	* 数据源
	*/
	public void datas() {
		renderJsonData(service.paginateAdminDatas(getKv()));
	}

   /**
	* 新增
	*/
	public void add() {
		Kv kv = getKv();
		Long id = getLong(0, 0L);
		//String cversion = getLong(2, 2L).toString();
		if (id!=0){
			Subjectm bySubjectCode = service.findById(id);
			Integer clevel = bySubjectCode.getCLevel();
			ValidationUtils.notNull(clevel, "上级科目等级为空，请检查!");
			//根据上级id来获取最大等级
			set("clevel",clevel+1) ;
			set("csubjectname",bySubjectCode.getCSubjectName());
			set("isend",bySubjectCode.getIsEnd());
			set("pid",id);
		}else {
			if (!"2".equals(kv.getStr("cversion"))){
				set("cversions",kv.getStr("cversion"));
			}
			set("cversion",1);
			set("clevel",1);
		}
		render("add.html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		Subjectm subjectm = service.findById(useIfPresent(getLong(0)));
		ValidationUtils.notNull(subjectm, JBoltMsg.DATA_NOT_EXIST);
		set("iparentid",subjectm.getIParentId());
		set("clevel", subjectm.getCLevel());
		if (subjectm.getIParentId()!=null){
			set("csubjectname",service.findById(subjectm.getIParentId()).getCSubjectName());
		}

		set("ccodename", subjectdService.jointSubjectName(subjectm.getIAutoId()));
		set("subjectm", subjectm);
		render("edit.html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(useIfValid(Subjectm.class, "subjectm"),getKv()));
	}

   /**
	* 更新
	*/
	public void update() {


		renderJson(service.update( useIfValid(Subjectm.class, "subjectm"), getKv()));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(String.valueOf(getLong(0, 0L))));
	}

  /**
	* 切换toggleIsend
	*/
	public void toggleIsend() {
		renderJson(service.toggleIsend(getLong(0)));
	}

    @UnCheck
	public void options() {
		renderJsonData(service.getAllSubjectmsOptionsWithLevel());
	}

	/**
	 * u8科目列表
	 */
	public void minSubjectsList(){
		render("subject_min.html");
	}
	/**
	 * u8科目列表数据
	 */
	@UnCheck
	public void u8MajorSubjectsList(JBoltTableMenuFilter jboltTableMenuFilter) {
		Page<Record> pageData=null;
		if(jboltTableMenuFilter==null) {
			pageData = service.u8MajorSubjectsList(getPageNumber(),getPageSize(),getKv());
		}else {
			pageData = service.u8MajorSubjectsList(getPageNumber(),jboltTableMenuFilter.getPageSize(),getKv());
		}
		renderJsonData(pageData);
	}
	@UnCheck
	public void autocomplete() {
		renderJsonData(service.getAutocompleteList(get("q"), getInt("limit", 10)));
	}
	@UnCheck
	public void highestsubjectAutocomplete(){
		renderJsonData(service.highestsubjectAutocomplete(get("q"), getInt("limit", 10)));
	}
	@UnCheck
	public void lowestsubjectAutocomplete(@Para(value="ihighestsubjectid") Long ihighestsubjectid){
		renderJsonData(service.lowestsubjectAutocomplete(get("q"), getInt("limit", 10),ihighestsubjectid));
	}

	/**
	 * 版本启用条件页面
	 */
	public void versionEnableIndex(){
		if (getKv().getInt("isenabled")==1){
			set("isenabled",1);
		}else {
			set("isenabled",0);
		}
		render("versionEnable_index.html");

	}
	public void versionEnable(){
		renderJson(service.versionEnable(getKv()));
	}
	/**
	 * 复制报表条件页面
	 */
	public void copyDatasIndex(){
		render("copy_datas_index.html");
	}
	/**
	 * 复制数据
	 */
	public void copyDatas(){
		renderJsonData(service.copyDatas(getKv()));
	}
	
	
	@UnCheck
	public void u8SubjectAutocomplete() {
		renderJsonData(service.u8SubjectAutocomplete(get("q"), getInt("limit", 10)));
	}
	
}
