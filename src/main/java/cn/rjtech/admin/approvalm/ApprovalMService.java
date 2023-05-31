package cn.rjtech.admin.approvalm;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.approvalform.ApprovalFormService;
import cn.rjtech.admin.auditformconfig.AuditFormConfigService;
import cn.rjtech.model.momdata.ApprovalForm;
import cn.rjtech.model.momdata.ApprovalM;
import cn.rjtech.model.momdata.AuditFormConfig;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 审批流主表 Service
 * @ClassName: ApprovalMService
 * @author: RJ
 * @date: 2023-04-18 17:16
 */
public class ApprovalMService extends BaseService<ApprovalM> {

	private final ApprovalM dao = new ApprovalM().dao();

	@Override
	protected ApprovalM dao() {
		return dao;
	}

	@Inject
	private ApprovalFormService approvalFormService;
	@Inject
	private AuditFormConfigService auditFormConfigService;

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<ApprovalM> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param approvalM
	 * @return
	 */
	public Ret save(ApprovalM approvalM) {
		if(approvalM==null || isOk(approvalM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(approvalM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=approvalM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(approvalM.getIautoid(), JBoltUserKit.getUserId(), approvalM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param approvalM
	 * @return
	 */
	public Ret update(ApprovalM approvalM) {
		if(approvalM==null || notOk(approvalM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ApprovalM dbApprovalM=findById(approvalM.getIAutoId());
		if(dbApprovalM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(approvalM.getName(), approvalM.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=approvalM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(approvalM.getIautoid(), JBoltUserKit.getUserId(), approvalM.getName());
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
		boolean success = tx(()->{

			List<AuditFormConfig> formConfigs = auditFormConfigService.find("select * from Bd_AuditFormConfig where " +
					"iFormId in (select iFormId from " +
					"Bd_ApprovalForm where iApprovalMid = " + id + ")");

			if (formConfigs.size()>0){
				formConfigs.forEach(auditFormConfig -> {
					ValidationUtils.isTrue(auditFormConfig.getIType()!=1, "删除审批流需将表单["+auditFormConfig.getCFormName()+
							"]的审批类型改成审核或者删除");
				});
			}


//			并删除所有子表的数据
			delete("delete from Bd_ApprovalD where iApprovalMid = "+id);
			delete("delete from Bd_ApprovalD_Role where iApprovalDid in (select iAutoId from Bd_ApprovalD where " +
					"iApprovalMid = "+id+" )");
			delete("delete from Bd_ApprovalD_User where iApprovalDid in (select iAutoId from Bd_ApprovalD where iApprovalMid = "+id+" )");
			delete("delete from Bd_ApprovalForm where iApprovalMid = "+id);

			deleteById(id,true);
			return true;
		});
		if(!success) {
			return fail("操作失败！");
		}
		return SUCCESS;
	}

	/**
	 * 删除数据后执行的回调
	 * @param approvalM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ApprovalM approvalM, Kv kv) {
		//addDeleteSystemLog(approvalM.getIautoid(), JBoltUserKit.getUserId(),approvalM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param approvalM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(ApprovalM approvalM, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(approvalM, kv);
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
	 * 切换isenabled属性
	 */
	public Ret toggleIsEnabled(Long id) {
		return toggleBoolean(id, "isEnabled");
	}

	/**
	 * 切换isapprovedonsame属性
	 */
	public Ret toggleIsApprovedOnSame(Long id) {
		return toggleBoolean(id, "isApprovedOnSame");
	}

	/**
	 * 切换isskippedonduplicate属性
	 */
	public Ret toggleIsSkippedOnDuplicate(Long id) {
		return toggleBoolean(id, "isSkippedOnDuplicate");
	}

	/**
	 * 切换isdeleted属性
	 */
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "IsDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param approvalM 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(ApprovalM approvalM,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(ApprovalM approvalM, String column, Kv kv) {
		//addUpdateSystemLog(approvalM.getIautoid(), JBoltUserKit.getUserId(), approvalM.getName(),"的字段["+column+"]值:"+approvalM.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param approvalM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(ApprovalM approvalM, Kv kv) {
		//这里用来覆盖 检测ApprovalM是否被其它表引用
		return null;
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

		AtomicReference<Long> id = new AtomicReference<>();

		Db.use(dataSourceConfigName()).tx(() -> {
			ApprovalM approvalM = jBoltTable.getFormModel(ApprovalM.class, "approvalM");
			Long headerId;
			if (approvalM.getIAutoId() == null){
				approvalM.setICreateBy(user.getId());
				approvalM.setCCreateName(user.getName());
				approvalM.setDCreateTime(nowDate);
				approvalM.setIUpdateBy(user.getId());
				approvalM.setCUpdateName(user.getName());
				approvalM.setDUpdateTime(nowDate);
				approvalM.setIOrgId(orgId);
				approvalM.setCOrgCode(orgCode);
				approvalM.setCOrgName(orgName);
				approvalM.save();
			} else {
				approvalM.setIUpdateBy(user.getId());
				approvalM.setCUpdateName(user.getName());
				approvalM.setDUpdateTime(nowDate);
				approvalM.update();
			}
			headerId = approvalM.getIAutoId();
			id.set(approvalM.getIAutoId());

			if (jBoltTable.saveIsNotBlank()){
				List<ApprovalForm> saveModelList = jBoltTable.getSaveModelList(ApprovalForm.class);
				List<ApprovalForm> saveList = new ArrayList<>();
				Long finalHeaderId = headerId;
				saveModelList.forEach(approvalForm -> {
					approvalForm.setIApprovalMid(finalHeaderId);
					saveList.add(approvalForm);
				});
				approvalFormService.batchSave(saveList);
			}

			if (jBoltTable.deleteIsNotBlank()){
				approvalFormService.realDeleteByIds(jBoltTable.getDelete());
			}
			return true;
		});
		return SUCCESS.set("id",id);
	}

	/**
	 * 通过formId查找到审批流主表数据
	 * @param formId
	 * @return
	 */
	public ApprovalM findByFormId(Long formId){
		return findFirst("select t1.*\n" +
				"from Bd_ApprovalM t1\n" +
				"         left join\n" +
				"         (select * from Bd_ApprovalForm where iFormId = "+formId+") t2\n" +
				"         on t1.iAutoId = t2.iApprovalMid\n" +
				"where t1.isEnabled = '1'\n" +
				"  and t1.IsDeleted = '0'");
	}
}
