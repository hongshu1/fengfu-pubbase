package cn.rjtech.admin.auditformconfig;

import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.util.JBoltArrayUtil;
import cn.jbolt.core.util.JBoltCamelCaseUtil;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.AuditFormConfig;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 审批表单配置 Service
 * @ClassName: AuditFormConfigService
 * @author: RJ
 * @date: 2023-04-18 17:18
 */
public class AuditFormConfigService extends BaseService<AuditFormConfig> {

	private final AuditFormConfig dao = new AuditFormConfig().dao();

	@Override
	protected AuditFormConfig dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<AuditFormConfig> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<AuditFormConfig> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return daoTemplate("auditformconfig.pageList", kv).paginate(pageNumber, pageSize);
	}

	/**
	 * 保存
	 * @param auditFormConfig
	 * @return
	 */
	public Ret save(AuditFormConfig auditFormConfig) {
		if(auditFormConfig==null || isOk(auditFormConfig.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(auditFormConfig.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=auditFormConfig.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(auditFormConfig.getIautoid(), JBoltUserKit.getUserId(), auditFormConfig.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param auditFormConfig
	 * @return
	 */
	public Ret update(AuditFormConfig auditFormConfig) {
		if(auditFormConfig==null || notOk(auditFormConfig.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		AuditFormConfig dbAuditFormConfig=findById(auditFormConfig.getIAutoId());
		if(dbAuditFormConfig==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(auditFormConfig.getName(), auditFormConfig.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=auditFormConfig.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(auditFormConfig.getIautoid(), JBoltUserKit.getUserId(), auditFormConfig.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		return deleteByIds(ids,true);
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
	 * @param auditFormConfig 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(AuditFormConfig auditFormConfig, Kv kv) {
		//addDeleteSystemLog(auditFormConfig.getIautoid(), JBoltUserKit.getUserId(),auditFormConfig.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param auditFormConfig 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(AuditFormConfig auditFormConfig, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(auditFormConfig, kv);
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
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "IsDeleted");
	}

	/**
	 * 切换isenabled属性
	 */
	public Ret toggleIsEnabled(Long id) {
		return toggleBoolean(id, "isEnabled");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param auditFormConfig 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(AuditFormConfig auditFormConfig,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(AuditFormConfig auditFormConfig, String column, Kv kv) {
		//addUpdateSystemLog(auditFormConfig.getIautoid(), JBoltUserKit.getUserId(), auditFormConfig.getName(),"的字段["+column+"]值:"+auditFormConfig.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param auditFormConfig model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(AuditFormConfig auditFormConfig, Kv kv) {
		//这里用来覆盖 检测AuditFormConfig是否被其它表引用
		return null;
	}

	/**
	 * 获取存货数据
	 * @param kv
	 * @return
	 */
	public Page<Record> resourceList(Kv kv, int pageNum, int pageSize){
		Page<Record> recordPage = dbTemplate("auditformconfig.resourceList", kv).paginate(pageNum, pageSize);
		return recordPage;
	}

	/**
	 * 可编辑表格提交
	 * @param jBoltTable 编辑表格提交内容
	 * @return
	 */
	public Ret submitByJBoltTable(JBoltTable jBoltTable) {
		//当前操作人员  当前时间
		User user = JBoltUserKit.getUser();
		Date nowDate = new Date();
		Long orgId = getOrgId();
		String orgCode = getOrgCode();
		String orgName = getOrgName();
		System.out.println("saveTable===>"+jBoltTable.getSave());
		System.out.println("updateTable===>"+jBoltTable.getUpdate());
		System.out.println("deleteTable===>"+jBoltTable.getDelete());
		System.out.println("form===>"+jBoltTable.getForm());

		Db.use(dataSourceConfigName()).tx(() -> {
			JSONObject formBean = jBoltTable.getForm();
			Boolean isEnabled = formBean.getBoolean("auditFormConfig.isEnabled");
			Integer iType = formBean.getInteger("auditFormConfig.iType");

			if (jBoltTable.saveIsNotBlank()){
				List<AuditFormConfig> saveModelList = jBoltTable.getSaveModelList(AuditFormConfig.class);
				List<AuditFormConfig> saveList = new ArrayList<>();
				saveModelList.forEach(auditFormConfig -> {

					/**
					 * 类型为 审批 的话 需要先定义审批流
					 */
					if (iType==1){

						Long iFormId = auditFormConfig.getIFormId();
						String cFormName = auditFormConfig.getCFormName();
						List<Record> record = findRecord("select * from Bd_ApprovalForm where iFormId = " + iFormId);
						ValidationUtils.isTrue(record.size() > 0, "需要先在审批流配置定义表单"+cFormName+"的审批流");

					}

					auditFormConfig.setIsEnabled(isEnabled);
					auditFormConfig.setIType(iType);
					auditFormConfig.setICreateBy(user.getId());
					auditFormConfig.setCCreateName(user.getName());
					auditFormConfig.setDCreateTime(nowDate);
					auditFormConfig.setIUpdateBy(user.getId());
					auditFormConfig.setCUpdateName(user.getName());
					auditFormConfig.setDUpdateTime(nowDate);
					auditFormConfig.setIOrgId(orgId);
					auditFormConfig.setCOrgCode(orgCode);
					auditFormConfig.setCOrgName(orgName);
					saveList.add(auditFormConfig);
				});
				batchSave(saveList);
			}

			if (jBoltTable.updateIsNotBlank()){
				List<AuditFormConfig> updateModelList = jBoltTable.getUpdateModelList(AuditFormConfig.class);
				List<AuditFormConfig> updateList = new ArrayList<>();
				updateModelList.forEach(auditFormConfig -> {

					/**
					 * 类型为 审批 的话 需要先定义审批流
					 */
					if (iType==1){

						Long iFormId = auditFormConfig.getIFormId();
						String cFormName = auditFormConfig.getCFormName();
						List<Record> record = findRecord("select * from Bd_ApprovalForm where iFormId = " + iFormId);
						ValidationUtils.isTrue(record.size() > 0, "需要先在审批流配置定义表单["+cFormName+"]的审批流");

					}

					auditFormConfig.setIsEnabled(isEnabled);
					auditFormConfig.setIType(iType);
					auditFormConfig.setIUpdateBy(user.getId());
					auditFormConfig.setCUpdateName(user.getName());
					auditFormConfig.setDUpdateTime(nowDate);
					updateList.add(auditFormConfig);
				});
				batchUpdate(updateList);
			}

			if (jBoltTable.deleteIsNotBlank()){
				deleteByIds(jBoltTable.getDelete());
			}
			return true;
		});
		return SUCCESS;
	}


	/**
	 *行数据源
	 * @param kv
	 * @return
	 */
	public List<AuditFormConfig> listData(Kv kv){

		String iAutoId = kv.getStr("auditFormConfig.iAutoId");
		List<AuditFormConfig> list = new ArrayList<>();

		if (iAutoId != null) {
			long id = Long.parseLong(iAutoId);
			AuditFormConfig formConfig = findById(id);
			list.add(formConfig);
		}

		return list;
	}

	/**
	 * 通过表单编码找到审核审批配置
	 * @param formSn
	 * @return
	 */
	public AuditFormConfig findByFormSn(String formSn){
		return findFirst("select * from Bd_AuditFormConfig where cFormSn = '"+formSn+"' and isEnabled = '1' and " +
				"IsDeleted = '0'");
	}
}
