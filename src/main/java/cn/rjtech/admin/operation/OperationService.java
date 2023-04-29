package cn.rjtech.admin.operation;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.common.util.CACHE;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelMerge;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
//import cn.rjtech.admin.badnessclass.BadnessclassService;
//import cn.rjtech.admin.operationbadness.OperationbadnessService;
import cn.rjtech.admin.workclass.WorkClassService;
import cn.rjtech.model.momdata.Operation;
//import cn.rjtech.model.momdata.Operationbadness;
import cn.rjtech.model.momdata.Workclass;
import cn.rjtech.util.Util;
import cn.rjtech.util.ValidationUtils;

import com.bstek.ureport.expression.model.Op;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.sql.SQLException;
import java.util.*;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 料品工序档案配置 Service
 * @ClassName: OperationService
 * @author: chentao
 * @date: 2022-11-09 19:17
 */
public class OperationService extends BaseService<Operation> {
	private final Operation dao=new Operation().dao();
	@Override
	protected Operation dao() {
		return dao;
	}
	@Inject
	private WorkClassService workClassService;
//	@Inject
//	private OperationbadnessService operationbadnessService;
//	@Inject
//	private BadnessclassService badnessclassService;

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public List<Operation> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return getCommonListByKeywords(keywords,"dUpdateTime","DESC","cOperationName,cOperationCode",Okv.by("isEnabled",true).set("isDeleted",false));
		//return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "cOperationName",Okv.by("isEnabled",true).set("isDeleted",false));
	}

	public List<Record> findItemOperationDatas(Kv kv){
		return dbTemplate("operation.findItemOperationDatas", kv).find();
	}

	/**
	 * 保存
	 * @param operation
	 * @return
	 */
	public Ret save(Operation operation) {
		if(operation==null || isOk(operation.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		operation.setIautoid(JBoltSnowflakeKit.me.nextId());
		//是否存在编码
		Operation operation1 = findFirst(Okv.by("cOperationCode", operation.getCoperationcode()).set("isDeleted", 0), "iAutoid", "DESC");
		ValidationUtils.isTrue(operation1==null, "已存在编码："+operation.getCoperationcode());
		//组数据
		saveOperationHandle(operation, JBoltUserKit.getUserId(),new Date(),JBoltUserKit.getUserName(), getOrgId(),getOrgCode(), getOrgName());
		boolean save = operation.save();
		if (!save){
			return fail(JBoltMsg.FAIL);
		}
		return ret(save);
	}

	/**
	 * 更新
	 * @param operation
	 * @return
	 */
	public Ret update(Operation operation) {
		if(operation==null || notOk(operation.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Operation dbOperation=findById(operation.getIautoid());
		if(dbOperation==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}

		Operation operation1 = findFirst("SELECT * FROM Bd_Operation WHERE cOperationCode =? AND isDeleted = 0 AND iAutoId != ?", operation.getCoperationcode(), operation.getIautoid());
		ValidationUtils.isTrue(operation1==null, "已存在编码"+operation.getCoperationcode());
		operation.setIupdateby(JBoltUserKit.getUserId());
		operation.setDupdatetime(new Date());
		operation.setCupdatename(JBoltUserKit.getUserName());
		boolean result = operation.update();
		if (!result){
			return fail(JBoltMsg.FAIL);
		}
		return ret(result);
	}

	/**
	 * 删除 指定多个ID
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String id : StrSplitter.split(ids, COMMA, true, true)) {
				Operation operation = findById(id);
				ValidationUtils.notNull(operation, JBoltMsg.DATA_NOT_EXIST);
//				operationbadnessService.deleteByOperationId(operation.getIautoid());
				operation.setIsdeleted(true);
				operation.update();
			}
			return true;
		});
		return SUCCESS;
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		return deleteById(id,true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param operation 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Operation operation, Kv kv) {
		//addDeleteSystemLog(operation.getIautoid(), JBoltUserKit.getUserId(),operation.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param operation 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(Operation operation, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(operation, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	/**
	 * 切换isdeleted属性
	 */
	public Ret toggleIsdeleted(Long id) {
		return toggleBoolean(id, "isDeleted");
	}

	/**
	 * 切换isenabled属性
	 */
	public Ret toggleIsenabled(Long id) {
		return toggleBoolean(id, "isEnabled");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param operation 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(Operation operation,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Operation operation, String column, Kv kv) {
		//addUpdateSystemLog(operation.getIautoid(), JBoltUserKit.getUserId(), operation.getName(),"的字段["+column+"]值:"+operation.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param operation model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(Operation operation, Kv kv) {
		//这里用来覆盖 检测Operation是否被其它表引用
		return null;
	}

	public void saveOperationHandle(Operation operation, Long userId, Date date, String username,Long orgId,String orgCode,String orgName){
		operation.setIcreateby(userId);
		operation.setDcreatetime(date);
		operation.setCcreatename(username);
		operation.setIorgid(orgId);
		operation.setCorgcode(orgCode);
		operation.setCorgname(orgName);
		operation.setDupdatetime(date);
		operation.setIupdateby(userId);
		operation.setCupdatename(username);
	}

	public Page<Record> pageList(Kv kv) {
		return dbTemplate("operation.list", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
	}

	public List<Record> list(Kv kv) {
		return dbTemplate("operation.list", kv).find();
	}

	public Ret importExcelData(File file) {
		StringBuilder errorMsg=new StringBuilder();
		JBoltExcel jBoltExcel=JBoltExcel
				//从excel文件创建JBoltExcel实例
				.from(file)
				//设置工作表信息
				.setSheets(
						JBoltExcelSheet.create("sheet1")
								//设置列映射 顺序 标题名称
								.setHeaders(
										JBoltExcelHeader.create("coperationcode","工艺方法编码"),
										JBoltExcelHeader.create("coperationname","工艺方法名称"),
										JBoltExcelHeader.create("iworkclassid","所属工种"),
										JBoltExcelHeader.create("cmemo","备注")
								)
								//特殊数据转换器
								.setDataChangeHandler((data,index) ->{
									data.change("iworkclassid", CACHE.me.getWorkClassIdByCode(data.getStr("iworkclassid")));
								})
								//从第三行开始读取
								.setDataStartRow(2)
				);
		//从指定的sheet工作表里读取数据
		List<Operation> models = JBoltExcelUtil.readModels(jBoltExcel, "sheet1", Operation.class, errorMsg);
		if(notOk(models)) {
			if(errorMsg.length()>0) {
				return fail(errorMsg.toString());
			}else {
				return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
			}
		}
		if(errorMsg.length()>0) {
			return fail(errorMsg.toString());
		}
		//读取数据没有问题后判断必填字段
		Set<String> set = new HashSet<>();
		if(models.size()>0){
			for(Operation op:models){
				set.add(op.getCoperationcode());
				if(notOk(op.getCoperationcode())){
					return fail("工序编码不能为空");
				}
				if(notOk(op.getCoperationname())){
					return fail("工序名称不能为空");
				}
				if(notOk(op.getIworkclassid())){
					return fail("所属工种不能为空");
				}
				//数据库是否存在工序编码
				ValidationUtils.isTrue(isNull(findFirst(Okv.by("cOperationCode", op.getCoperationcode()).set("isDeleted", "0"), "iautoid", "desc")), JBoltMsg.DATA_IMPORT_FAIL+"存在重复编码数据");
			}
			ValidationUtils.isTrue(set.size()==models.size(), JBoltMsg.DATA_IMPORT_FAIL+"存在重复编码数据");
		}
		//执行批量操作
		boolean success=tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				batchSave(models);
				return true;
			}
		});
		if(!success) {
			return fail(JBoltMsg.DATA_IMPORT_FAIL);
		}
		return SUCCESS;
	}

	public List<Operation> getIdAndNameList(){
		return find("SELECT iAutoId,cOperationName FROM Bd_Operation WHERE isDeleted = '0' ");
	}

	public List<Operation> getIdAndNameListToInventoryCheckForm(){
		return find("SELECT iautoid,coperationcode,coperationname,iworkclassid FROM Bd_Operation WHERE isDeleted = '0' ");
	}

	/*
	 * 导出excel文件
	 */
	public JBoltExcel exportExcelTpl(List<Operation> datas) {
		//2、创建JBoltExcel
		JBoltExcel jBoltExcel = JBoltExcel
			.createByTpl("工序导出模板.xls")//创建JBoltExcel 从模板加载创建
			.addSheet(//设置sheet
				JBoltExcelSheet.create("工序")//创建sheet name保持与模板中的sheet一致
					.setHeaders(//sheet里添加表头
						JBoltExcelHeader.create("coperationcode", "工序编码", 20),
						JBoltExcelHeader.create("coperationname", "工序名称", 20),
						JBoltExcelHeader.create("iworkclassname", "所属工种", 20),
						JBoltExcelHeader.create("cmemo", "备注", 20),
						JBoltExcelHeader.create("ccreatename", "创建人", 20),
						JBoltExcelHeader.create("dcreatetime", "创建时间", 20)
					)
					.setDataChangeHandler((data, index) -> {//设置数据转换处理器
						//将user_id转为user_name
						data.changeWithKey("user_id", "user_username", CACHE.me.getUserUsername(data.get("user_id")));
						data.changeBooleanToStr("is_deleted", "是", "否");
					})
					.setModelDatas(3,datas)//设置数据
			)
			.setFileName("工序"+ "_"+ DateUtil.today());
		//3、返回生成的excel文件
		return jBoltExcel;
	}

	/**
	 * 生成Excel 导入模板的数据 byte[]
	 */
	public JBoltExcel getExcelImportTpl() {
		return JBoltExcel
			//创建
			.create()
			.setSheets(
				JBoltExcelSheet.create("工序导入模板")
					//设置列映射 顺序 标题名称 不处理别名
					.setHeaders(2, false,
						JBoltExcelHeader.create("工序编码", 20),
						JBoltExcelHeader.create("工序名称", 20),
						JBoltExcelHeader.create("所属工种", 20),
						JBoltExcelHeader.create("备注", 20)
					)
					.setMerges(JBoltExcelMerge.create("A", "D", 1, 1, "工序"))
			);
	}

	public List<Operation> findByIworkclassId(Long iworkclassId){
		return find("select * from Bd_Operation where iWorkClassId=?",iworkclassId);
	}

}