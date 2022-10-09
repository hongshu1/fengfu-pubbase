package cn.jbolt._admin.codegen;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import cn.jbolt._admin.codegen.modelattr.CodeGenModelAttrService;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.model.CodeGen;
import cn.jbolt.common.model.CodeGenModelAttr;
import cn.jbolt.common.util.CACHE;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.base.config.JBoltConfig;
import cn.jbolt.core.bean.Option;
import cn.jbolt.core.bean.OptionBean;
import cn.jbolt.core.cache.JBoltCache;
import cn.jbolt.core.cache.JBoltCacheKit;
import cn.jbolt.core.cache.JBoltDeptCache;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.db.datasource.JBoltDataSourceUtil;
import cn.jbolt.core.enumutil.JBoltEnum;
import cn.jbolt.core.model.Permission;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.service.base.JBoltBaseRecordService;
import cn.jbolt.core.service.base.JBoltBaseService;
import cn.jbolt.core.service.base.JBoltCommonService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Okv;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.ehcache.IDataLoader;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
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
		renderJsonData(service.paginateAdminDatas(getInt("pid"),getPageNumber(), getPageSize(), getSortColumn("update_time"), getSortType("desc"), getKeywords(), getInt("style"), getState(),getBoolean("isDeleted"),"table_name,author,remark,version_sn,version_remark"));
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
	 * 刷新enum缓存
	 */
	public void refreshEnumsCache() {
		CACHE.me.removeCodeGenEnums();
		renderJsonSuccess();
	}
	/**
	 * 内置枚举类
	 */
	public void enums() {
		List<Option> options = CACHE.me.getCodeGenEnums();
		String keywords = get("q");
		if(isOk(options) && isOk(keywords)){
			options = options.stream().filter(op->op.getText().toLowerCase().contains(keywords.toLowerCase())).collect(Collectors.toList());
		}
		renderJsonData(options);
	}


	/**
	 * 刷新cache缓存
	 */
	public void refreshCachesCache() {
		CACHE.me.removeCodeGenCaches();
		renderJsonSuccess();
	}

	/**
	 * 刷新service缓存
	 */
	public void refreshServicesCache() {
		CACHE.me.removeCodeGenServices();
		renderJsonSuccess();
	}
	/**
	 * 内置cahce类
	 */
	public void caches() {
		String keywords = get("q");
		List<Option> options = null;
		if(isOk(keywords) && keywords.contains(":")){
			String className = keywords.substring(0,keywords.lastIndexOf(":"));
			try {
				Class<?> clazz = ClassUtil.loadClass(className);
				if(clazz == null){
					options = CACHE.me.getCodeGenCaches();
				}else{
					List<Method> methods = Arrays.asList(clazz.getDeclaredMethods());
					if(isOk(methods)){
						options = new ArrayList<>();
						for(Method m:methods){
							options.add(new OptionBean(className+":"+m.getName()));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				options = CACHE.me.getCodeGenCaches();
			}

		}else{
			options = CACHE.me.getCodeGenCaches();
		}
		if(isOk(options) && isOk(keywords)){
			options = options.stream().filter(op->op.getText().toLowerCase().contains(keywords.toLowerCase())).collect(Collectors.toList());
		}
		renderJsonData(options);
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
		set("projectPath",FileUtil.normalize(System.getProperty("user.dir")));
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
	public void save() {
		renderJson(service.save(getModel(CodeGen.class,"codeGen")));
	}
	/**
	 * 更新基础信息
	 */
	public void update() {
		renderJson(service.update(getModel(CodeGen.class,"codeGen")));
	}
	/**
	 * 详细配置里更新基础信息
	 */
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
	 * 数据源中的可选表
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
		set("hasIsDeletedColumn",codeGenModelAttrService.checkHasIsDeletedColumn(codeGen.getId()));
		set("hasIsKeywordsColumn",codeGenModelAttrService.checkHasIsKeywordsColumn(codeGen.getId()));
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

	/**
	 * 测试ajax success
	 */
	public void testAjax(){
		renderJsonSuccess();
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
	 * table portal预览 编辑模式
	 */
	public void tablePortalEditMode() {
		set("editMode", true);
		tablePortal();
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
		set("hasIsDeletedColumn",codeGenModelAttrService.checkHasIsDeletedColumn(codeGen.getId()));
		set("hasIsKeywordsColumn",codeGenModelAttrService.checkHasIsKeywordsColumn(codeGen.getId()));
		set("codeGenServiceMode",false);
		set("sortableColumns", codeGenModelAttrService.getSortableColumnsStr(codeGenId));
		set("cols", codeGenModelAttrService.getCodeGenTableColumns(codeGenId));
		set("conditions", codeGenModelAttrService.getCodeGenTableConditions(codeGenId));
		render("config/_table_portal.html");
	}

	/**
	 * table预览的demo数据
	 */
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
	@Before(Tx.class)
	public void initSortRankInform() {
		renderJson(codeGenModelAttrService.initSortRankInformById(getLong(0)));
	}
	/**
	 * 初始化form元素排序
	 */
	@Before(Tx.class)
	public void initSortRankIntable() {
		renderJson(codeGenModelAttrService.initSortRankIntableById(getLong(0)));
	}

	/**
	 * 选择字典数据
	 */
	public void formDictionaryOptions() {
		renderJsonData(dictionaryService.getOptionListByTypeKey(get("formDataValue"),true));
	}
	/**
	 * 选择字典数据
	 */
	public void searchDictionaryOptions() {
		renderJsonData(dictionaryService.getOptionListByTypeKey(get("searchDataValue"),true));
	}

	/**
	 * 路由列表数据
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

	/**
	 * 刷新routes缓存
	 */
	public void refreshRoutesCache() {
		JBoltCacheKit.remove(JBoltConfig.JBOLT_CACHE_NAME, "codeGen_routes");
		renderJsonSuccess();
	}

	/**
	 * 一次性生成model、主逻辑等内容
	 */
	@Before(Tx.class)
	public void genAll(){
		renderJson(service.genAll(getLong("id"),getBoolean("cover",false)));
	}

	/**
	 * 一次性生成model
	 */
	@Before(Tx.class)
	public void genModel(){
		renderJson(service.genModel(getLong("id"),getBoolean("cover",false)));
	}

	/**
	 * 一次性生成mainLogic
	 */
	@Before(Tx.class)
	public void genMainLogic(){
		renderJson(service.genMainLogic(getLong("id"),getBoolean("cover",false)));
	}

	/**
	 * 模拟导出模板
	 */
	public void downloadTpl(){
		Long codeGenId = getLong(0);
		if(notOk(codeGenId)){
			renderJsonFail("codeGenId未传");
			return;
		}
		List<CodeGenModelAttr> attrs = codeGenModelAttrService.getCodeGenImportColumns(codeGenId);
		if(notOk(attrs)){
			renderJsonFail("此表模型属性中未设置导入列");
			return;
		}
		List<JBoltExcelHeader> headers = new ArrayList<>();
		String label;
		for(CodeGenModelAttr attr:attrs){
			if(isOk(attr.getTableLabel())){
				label = attr.getTableLabel();
			}else{
				label = StrKit.defaultIfBlank(attr.getRemark(),attr.getAttrName());
			}
			headers.add(JBoltExcelHeader.create(label,15));
		}
		renderBytesToExcelXlsxFile(JBoltExcel.create().addSheet(JBoltExcelSheet.create("数据").setHeaders(false,headers)).setFileName("导入模板"));
	}

	/**
	 * 模拟初始化打开导入excel的dialog
	 */
	public void initImportExcel(){
		set("codeGenId",getLong(0));
		set("coeGenServiceMode",false);
		render("config/_import_excel.html");
	}

	/**
	 * 模拟导入成功
	 */
	public void importExcel(){
		renderJsonSuccess("导入成功");
	}

	/**
	 * 模拟导出
	 */
	public void exportExcel(){
		Long codeGenId = getLong(0);
		if(notOk(codeGenId)){
			renderJsonFail("codeGenId未传");
			return;
		}
		List<CodeGenModelAttr> attrs = codeGenModelAttrService.getCodeGenImportColumns(codeGenId);
		if(notOk(attrs)){
			renderJsonFail("此表模型属性中未设置导入列");
			return;
		}
		List<JBoltExcelHeader> headers = new ArrayList<>();
		String label;
		for(CodeGenModelAttr attr:attrs){
			if(isOk(attr.getTableLabel())){
				label = attr.getTableLabel();
			}else{
				label = StrKit.defaultIfBlank(attr.getRemark(),attr.getAttrName());
			}
			headers.add(JBoltExcelHeader.create(attr.getAttrName(),label,15));
		}
		List<Okv> datas = codeGenModelAttrService.getCodeGenTableColumnsDatas(codeGenId);
		renderBytesToExcelXlsxFile(JBoltExcel.create().addSheet(JBoltExcelSheet.create("数据").setHeaders(headers).setOkvDatas(2,datas)).setFileName("导出文件"));
	}

	/**
	 * 关键词搜索静态方法 类
	 */
	public void staticMethodClasses(){
		String keywords = get("q");
		List<Option> options = null;
		if(isOk(keywords) && keywords.contains(":")){
			String className = keywords.substring(0,keywords.indexOf(":"));
			try {
				Class<?> clazz = ClassUtil.loadClass(className);
				if(clazz == null){
					options =getStaticMethodClasses(keywords);
				}else{
					List<Method> methods = new ArrayList<>();
					Method[] marr = clazz.getDeclaredMethods();
					if(isOk(marr)){
						for(Method m:marr){
							methods.add(m);
						}
					}
					if(isOk(methods)){
						options = new ArrayList<>();
						for(Method m:methods){
							options.add(new OptionBean(className+":"+m.getName()));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				options = getStaticMethodClasses(keywords);
			}
		}else{
			options = getStaticMethodClasses(keywords);
		}
		renderJsonData(options);
	}

	private List<Option> getStaticMethodClasses(String keywords) {
		//没输入 或者没有点 直接忽略
		if(notOk(keywords) || !keywords.contains(".")){
			renderJsonSuccess();
			return null;
		}
		//转为小写
		String lowKeywords = keywords.toLowerCase().trim();
		String packageName = null;

		if(lowKeywords.endsWith(".")){
			packageName = lowKeywords.substring(0,lowKeywords.length()-1);
		}else{
			if(lowKeywords.contains(".")){
				packageName = lowKeywords.substring(0,lowKeywords.lastIndexOf("."));
			}
		}
		List<Option> options = new ArrayList<>();

		Set<Class<?>> classes = ClassUtil.scanPackage(packageName,filter->{
			return 	!filter.isEnum() && !(filter.getSimpleName().startsWith("Base") && JBoltBaseModel.class.equals(filter.getSuperclass())) && filter.getName().toLowerCase().startsWith(lowKeywords);
		});


		if(classes!=null && classes.size()>0) {
			classes.forEach(en->{
				options.add(new OptionBean(en.getName()));
			});
			//排序
			options.sort(new Comparator<Option>() {
				@Override
				public int compare(Option o1, Option o2) {
					return o1.getText().compareTo(o2.getText());
				}
			});
			int size = options.size();
			if(size>20){
				return options.subList(0,20);
			}else{
				return options;
			}
		}
		return null;
	}


	/**
	 * 关键词搜索service方法
	 */
	public void services(){
		String keywords = get("q");
		//没输入 或者没有点 直接忽略
		if(notOk(keywords)){
			renderJsonSuccess();
			return;
		}
		List<Option> options = null;
		if(keywords.contains(":")){
			String className = keywords.substring(0,keywords.lastIndexOf(":"));
			try {
				Class<?> clazz = ClassUtil.loadClass(className);
				if(clazz == null){
					options = CACHE.me.getCodeGenServices();
				}else{
					List<Method> methods = Arrays.asList(clazz.getDeclaredMethods());
					if(isOk(methods)){
						options = new ArrayList<>();
						for(Method m:methods){
							options.add(new OptionBean(className+":"+m.getName()));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				options = CACHE.me.getCodeGenServices();
			}

		}else{
			options = CACHE.me.getCodeGenServices();
		}

		if(isOk(options)){
			options = options.stream().filter(op->op.getText().toLowerCase().contains(keywords.toLowerCase())).collect(Collectors.toList());
		}
		renderJsonData(options);
	}

	/**
	 * 初始化 绑定permission
	 */
	public void initBindPermission(){
		set("codeGenId",getLong(0));
		render("config/_bind_permission.html");
	}

	/**
	 * 绑定permission
	 */
	public void bindPermission(){
		renderJson(service.bindPermission(getLong("codeGenId"),getLong("permissionId")));
	}

	/**
	 * 同步本项目地址
	 */
	public void syncProjectPath(){
		renderJsonData(FileUtil.normalize(System.getProperty("user.dir")));
	}

	/**
	 * 更新projectpath
	 */
	public void updateProjectPath(){
		renderJson(service.updateProjectPath(getLong("codeGenId"),get("projectPath")));
	}


}
