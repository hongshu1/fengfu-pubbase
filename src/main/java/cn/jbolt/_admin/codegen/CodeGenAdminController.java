package cn.jbolt._admin.codegen;

import cn.hutool.core.io.FileUtil;
import cn.jbolt._admin.codegen.modelattr.CodeGenModelAttrService;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.model.CodeGen;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.base.config.JBoltConfig;
import cn.jbolt.core.bean.Option;
import cn.jbolt.core.bean.OptionBean;
import cn.jbolt.core.cache.JBoltCacheKit;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.db.datasource.JBoltDataSourceUtil;
import cn.jbolt.core.enumutil.JBoltEnum;
import cn.jbolt.core.model.Permission;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.ehcache.IDataLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
/**
 * JBolt 开发平台 - 可视化代码生成器
 * @ClassName:  JBoltCodeGenAdminController   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2021年9月24日   
 */
@CheckPermission(PermissionKey.JBOLT_CODE_GEN)
@UnCheckIfSystemAdmin
public class CodeGenAdminController extends JBoltBaseController {
	@Inject
	private CodeGenService service;
	@Inject
	private CodeGenModelAttrService codeGenModelAttrService;
	@Inject
	private DictionaryService dictionaryService;
	/**
	 * 进入首页
	 */
	public void index() {
		setDefaultSortInfo("update_time", "desc");
		render("index.html");
	}
	/**
	 * codeGen数据源
	 */
	public void datas() {
		renderJsonData(service.paginateAdminDatas(getInt("pid"),getPageNumber(), getPageSize(), getSortColumn("update_time"), getSortType("desc"), getKeywords(), getInt("style"), getState(),getBoolean("is_deleted"),"table_name,author,remark,version_sn,version_remark"));
	}

	/**
	 * autocomplete组件默认的demo数据源
	 */
	@UnCheck
	public void autocompletedemodatas(){
		List<Option> options = new ArrayList<>();
		options.add(new OptionBean("demo数据，请更换地址","0"));
		renderJsonData(options);
	}
	
	/**
	 * 构建类型
	 */
	public void types() {
		renderJsonData(JBoltEnum.getEnumOptionList(CodeGenType.class));
	}
	
	/**
	 * 系统数据源
	 */
	public void datasources() {
		renderJsonData(JBoltDataSourceUtil.me.getAllDatasource());
	}
	
	/**
	 * 样式类型数据源
	 */
	public void styles() {
		renderJsonData(JBoltEnum.getEnumOptionList(CodeGenStyle.class));
	}
	
	/**
	 * 生成状态数据源
	 */
	public void states() {
		renderJsonData(JBoltEnum.getEnumOptionList(CodeGenState.class));
	}
	
	/**
	 * 添加主配置
	 */
	public void add() {
		set("isSubTable", false);
		set("projectPath", System.getProperty("user.dir"));
		render("add.html");
	}
	/**
	 * 添加子表配置
	 */
	public void addSubTable() {
		Integer pid = getInt(0);
		if(notOk(pid)) {
			renderFormFail("参数异常，未指定主表");
			return;
		}
		CodeGen codeGen = service.findById(pid);
		if(codeGen == null) {
			renderFormFail("参数异常，指定的主表不存在");
			return;
		}
		set("pid", pid);
		set("isSubTable", false);
		set("projectPath", System.getProperty("user.dir"));
		render("add.html");
	}
	
	/**
	 * 编辑主配置
	 */
	public void edit() {
		Long id = getLong(0);
		if(notOk(id)) {
			renderFormFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		CodeGen codeGen = service.findById(id);
		if(codeGen == null) {
			renderFormFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("codeGen", codeGen);
		set("isSubTable", codeGen.getIsSubTable());
		set("projectPath", System.getProperty("user.dir"));
		set("tableFullName", codeGen.getMainTableName()+(StrKit.isBlank(codeGen.getMainTableRemark())?"":("["+codeGen.getMainTableRemark()+"]")));
		render("edit.html");
	}
	
	/**
	 * 保存基础信息
	 */
	@Before(Tx.class)
	public void save() {
		renderJson(service.save(getModel(CodeGen.class,"codeGen")));
	}
	/**
	 * 更新基础信息
	 */
	@Before(Tx.class)
	public void update() {
		renderJson(service.update(getModel(CodeGen.class,"codeGen")));
	}
	/**
	 * 详细配置里更新基础信息
	 */
	@Before(Tx.class)
	public void updateBase() {
		renderJson(service.updateBase(getModel(CodeGen.class,"codeGen")));
	}
	/**
	 * 删除
	 */
	@Before(Tx.class)
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}
	
	/**
	 * 删除批量
	 */
	@Before(Tx.class)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids"), true));
	}

	/**
	 * 批量还原
	 */
	@Before(Tx.class)
	public void recoverByIds() {
		renderJson(service.recoverByIds(get("ids")));
	}

	/**
	 * 真删除
	 */
	@Before(Tx.class)
	public void realDelete() {
		renderJson(service.realDeleteById(getLong(0),true));
	}
	
	
	/**
	 * 生成状态数据源
	 */
	public void tablesAutocomplete() {
		renderJsonData(JBoltDataSourceUtil.me.getTableNameRemarkFullKvs(get("datasource"),get("q"),getInt("limit",10)));
	}
	/**
	 * 重新对数据源的表进行加载
	 */
	public void reloadTables() {
		renderJson(JBoltDataSourceUtil.me.reloadDatasourceTables(get("datasource")));
	}
	
	/**
	 * 进入代码生成详细配置UI
	 */
	public void editCodeGenConfig() {
		Long codeGenId = getLong(0);
		if(notOk(codeGenId)) {
			renderFormFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		CodeGen codeGen = service.findById(codeGenId);
		if(codeGen == null) {
			renderFormFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("codeGen", codeGen);
		set("codeGenId", codeGenId);
		set("tableFullName", StrKit.isBlank(codeGen.getMainTableRemark())?null:(codeGen.getMainTableName()+"["+codeGen.getMainTableRemark()+"]"));
		render("config/index.html");
	}
	
	/**
	 * keycache column数据源
	 */
	public void keyCacheColumns() {
		renderJsonData(codeGenModelAttrService.getKeyCacheColumnsAutocomplete(getLong(0),null,getInt("limit",10),get("q")));
	}
	
	/**
	 * keycache bindColumn数据源
	 */
	public void keyCacheBindColumns() {
		renderJsonData(codeGenModelAttrService.getKeyCacheColumnsAutocomplete(getLong("id"),get("column"),getInt("limit",10),get("q")));
	}
	
	/**
	 * 加载权限菜单设置
	 */
	public void permissionPortal() {
		Long codeGenId = getLong(0);
		if(notOk(codeGenId)) {
			renderAjaxPortalFail("参数异常 未传递codeGenId");
			return;
		}
		CodeGen codeGen = service.findById(codeGenId);
		if(codeGen == null) {
			renderAjaxPortalFail("codeGen数据不存在");
			return;
		}
		if(isOk(codeGen.getPermissionId())) {
			redirect("/admin/permission/edit/"+codeGen.getPermissionId());
		}else {
			set("pid", 0);
			set("level", 1);
//			Permission permission = new Permission();
//			permission.setUrl(codeGen.getType().toLowerCase()+"/"+codeGen.getModelName().toLowerCase());
//			permission.setTitle(codeGen.getMainTableRemark());
//			permission.setPermissionKey(codeGen.getModelName().toLowerCase());
//			set("permission", permission);
			render("/_view/_admin/permission/add.html");
		}
	}
	/**
	 * 确认清空解绑权限菜单
	 */
	public void unbindPermission() {
		renderJson(service.unbindPermission(getLong(0)));
	}
	
	/**
	 * 可以当做关键词查询匹配列的数据
	 */
	public void keywordsMatchColumns() {
		renderJsonData(codeGenModelAttrService.getKeywordsMatchColumns(getLong(0)));
	}
	/**
	 * form portal预览
	 */
	public void formPortal() {
		Long codeGenId = getLong(0);
		if(notOk(codeGenId)) {
			renderAjaxPortalFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		set("editMode", false);
		set("codeGen",service.findById(codeGenId));
		set("colDatas", codeGenModelAttrService.getCodeGenFormColDatas(codeGenId,false));
		render("config/_form_portal.html");
	}
	public void testAjax(){
		renderJsonSuccess();
	}
	/**
	 * form portal test 测试预览
	 */
	public void formPortalTest() {
		Long codeGenId = getLong(0);
		if(notOk(codeGenId)) {
			renderAjaxPortalFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		set("editMode", false);
		set("codeGenServiceModeTest", true);
		set("codeGen",service.findById(codeGenId));
		set("colDatas", codeGenModelAttrService.getCodeGenFormColDatas(codeGenId,false));
		render("config/_form_portal.html");
	}
	/**
	 * table portal预览
	 */
	public void tablePortal() {
		Long codeGenId = getLong(0);
		if(notOk(codeGenId)) {
			renderAjaxPortalFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		CodeGen codeGen = service.findById(codeGenId);
		if(codeGen == null) {
			renderAjaxPortalFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("codeGen", codeGen);
		if(isOk(codeGen.getTableDefaultSortColumn())) {
			setDefaultSortInfo(codeGen.getTableDefaultSortColumn(), codeGen.getTableDefaultSortType());
		}
		boolean hasIsDeletedColumn = codeGenModelAttrService.checkHasIsDeletedColumn(codeGen.getId());
		set("hasIsDeletedColumn",hasIsDeletedColumn);
		set("sortableColumns", codeGenModelAttrService.getSortableColumnsStr(codeGenId));
		set("cols", codeGenModelAttrService.getCodeGenTableColumns(codeGenId));
		set("conditions", codeGenModelAttrService.getCodeGenTableConditions(codeGenId));
		render("config/_table_portal.html");
	}
	
	
	public void tablePortalDatas() {
		Long  codeGenId = getLong(0);
		CodeGen codeGen = service.findById(codeGenId);
		if(codeGen == null) {
			renderJsonFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		if(codeGen.getIsPaginate()) {
			renderJsonData(codeGenModelAttrService.paginateCodeGenTableColumnsDatas(codeGenId,getPageNumber(),getPageSize()));
		}else {
			renderJsonData(codeGenModelAttrService.getCodeGenTableColumnsDatas(codeGenId));
		}
	}
	/**
	 * 初始化form元素排序
	 */
	public void initSortRankInform() {
		renderJson(codeGenModelAttrService.initSortRankInformById(getLong(0)));
	}
	/**
	 * form portal 编辑模式
	 */
	public void formPortalEditMode() {
		Long  codeGenId = getLong(0);
		if(notOk(codeGenId)) {
			renderAjaxPortalFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		set("editMode", true);
		set("colDatas", codeGenModelAttrService.getCodeGenFormColDatas(codeGenId,true));
		render("config/_form_portal.html");
	}
	/**
	 * 选择字典数据
	 */
	public void formDictionaryOptions() {
		renderJsonData(dictionaryService.getOptionListByTypeKey(get("formDataValue")));
	}
	/**
	 * 选择字典数据
	 */
	public void searchDictionaryOptions() {
		renderJsonData(dictionaryService.getOptionListByTypeKey(get("searchDataValue")));
	}
	
	/**
	 * 
	 */
	public void routesOptions() {
		Long  codeGenId = getLong(0);
		if(notOk(codeGenId)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		CodeGen codeGen = service.findById(codeGenId);
		if(codeGen == null) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		String keywords = get("q");
		String rootPath = FileUtil.normalize(codeGen.getProjectPath()+"/src/main/java/");
		List<File> files = JBoltCacheKit.get(JBoltConfig.JBOLT_CACHE_NAME, "codeGen_routes",new IDataLoader() {
			@Override
			public Object load() {
				return FileUtil.loopFiles(rootPath, new java.io.FileFilter() {
					@Override
					public boolean accept(File file) {
						return file.getName().endsWith("Routes.java");
					}
				});
			}
		});
		if(notOk(files)) {
			renderFail(JBoltMsg.PARAM_ERROR);
			return;
		}
		List<Option> options = new ArrayList<Option>();
		Stream<File> stream = files.stream();
		if(isOk(keywords)) {
			stream = stream.filter(file->file.getName().toLowerCase().indexOf(keywords.toLowerCase())!=-1);
		}
		
		stream.forEach(file->{
			options.add(new OptionBean(FileUtil.normalize(file.getAbsolutePath()).replace(rootPath, "").replace(File.separator, ".").replace("/", ".").replace(".java", "")));
		});
		renderJsonData(options);
	}
	
	public void refreshRoutesCache() {
		JBoltCacheKit.remove(JBoltConfig.JBOLT_CACHE_NAME, "codeGen_routes");
		renderJsonSuccess();
	}

	/**
	 * 一次性生成model、主逻辑等内容
	 */
	public void genAll(){
		renderJson(service.genAll(getLong("id"),getBoolean("cover",false)));
	}

	/**
	 * 一次性生成model
	 */
	public void genModel(){
		renderJson(service.genModel(getLong("id"),getBoolean("cover",false)));
	}
}
