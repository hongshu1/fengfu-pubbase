package cn.rjtech.admin.formapproval;

import cn.hutool.core.bean.BeanUtil;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Role;
import cn.jbolt.core.model.User;
import cn.rjtech.admin.approvald.ApprovalDService;
import cn.rjtech.admin.approvaldrole.ApprovaldRoleService;
import cn.rjtech.admin.approvalduser.ApprovaldUserService;
import cn.rjtech.admin.approvalm.ApprovalMService;
import cn.rjtech.admin.auditformconfig.AuditFormConfigService;
import cn.rjtech.admin.form.FormService;
import cn.rjtech.admin.formapprovald.FormApprovalDService;
import cn.rjtech.admin.formapprovaldperson.FormapprovaldPersonService;
import cn.rjtech.admin.formapprovaldrole.FormapprovaldRoleService;
import cn.rjtech.admin.formapprovalduser.FormapprovaldUserService;
import cn.rjtech.admin.formapprovalflowd.FormApprovalFlowDService;
import cn.rjtech.admin.formapprovalflowm.FormApprovalFlowMService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import com.jfinal.plugin.activerecord.Record;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 表单审批流 Service
 * @ClassName: FormApprovalService
 * @author: RJ
 * @date: 2023-04-18 17:26
 */
public class FormApprovalService extends BaseService<FormApproval> {

	private final FormApproval dao = new FormApproval().dao();

	@Override
	protected FormApproval dao() {
		return dao;
	}

	@Inject
	private AuditFormConfigService auditFormConfigService;
	@Inject
	private ApprovalMService approvalMService;
	@Inject
	private ApprovalDService approvalDService;
	@Inject
	private ApprovaldUserService approvaldUserService;
	@Inject
	private ApprovaldRoleService approvaldRoleService;
	@Inject
	private FormApprovalDService formApprovalDService;
	@Inject
	private FormapprovaldUserService formapprovaldUserService;
	@Inject
	private FormapprovaldRoleService formapprovaldRoleService;
	@Inject
	private PersonService personService;
	@Inject
	private UserService userService;
	@Inject
	private FormApprovalFlowMService flowMService;
	@Inject
	private FormApprovalFlowDService flowDService;
	@Inject
    private FormService formService;

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<FormApproval> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param formApproval
	 * @return
	 */
	public Ret save(FormApproval formApproval) {
		if(formApproval==null || isOk(formApproval.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(formApproval.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formApproval.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(formApproval.getIautoid(), JBoltUserKit.getUserId(), formApproval.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param formApproval
	 * @return
	 */
	public Ret update(FormApproval formApproval) {
		if(formApproval==null || notOk(formApproval.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		FormApproval dbFormApproval=findById(formApproval.getIAutoId());
		if(dbFormApproval==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(formApproval.getName(), formApproval.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formApproval.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(formApproval.getIautoid(), JBoltUserKit.getUserId(), formApproval.getName());
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
	 * @param formApproval 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(FormApproval formApproval, Kv kv) {
		//addDeleteSystemLog(formApproval.getIautoid(), JBoltUserKit.getUserId(),formApproval.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param formApproval 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(FormApproval formApproval, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(formApproval, kv);
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
	 * 切换isskippedonduplicate属性
	 */
	public Ret toggleIsSkippedOnDuplicate(Long id) {
		return toggleBoolean(id, "isSkippedOnDuplicate");
	}

	/**
	 * 切换isapprovedonsame属性
	 */
	public Ret toggleIsApprovedOnSame(Long id) {
		return toggleBoolean(id, "isApprovedOnSame");
	}

	/**
	 * 切换isdeleted属性
	 */
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "isDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param formApproval 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(FormApproval formApproval,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(FormApproval formApproval, String column, Kv kv) {
		//addUpdateSystemLog(formApproval.getIautoid(), JBoltUserKit.getUserId(), formApproval.getName(),"的字段["+column+"]值:"+formApproval.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param formApproval model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(FormApproval formApproval, Kv kv) {
		//这里用来覆盖 检测FormApproval是否被其它表引用
		return null;
	}

	/**
	 * 判断走审核还是审批
	 * 审批的话 保存审批流的基础信息、审批节点信息、节点的详细信息
	 * 保存 审批流的审批流程的人员信息
	 * @param kv
	 * @return
	 */
		public Ret judgeType(Kv kv){

		String formSn = kv.getStr("formSn");
		String formAutoId = kv.getStr("formAutoId");

		ValidationUtils.isTrue((formSn!=null && formAutoId!=null && !Objects.equals(formAutoId, "0")),"参数异常," +
				"请保存单据后刷新重试！");


		FormApproval byFormAutoId = findByFormAutoId(formAutoId);
		ValidationUtils.isTrue((notOk(byFormAutoId)),"该单据已提交审批，请勿重复提交审批！");

//		提审人
		User user = JBoltUserKit.getUser();
		Person person = personService.findFirst("select * from Bd_Person where iUserId = '" + user.getId() + "'");
//		获取单据信息
		Record formData = findFirstRecord("select * from " + formSn + " where iAutoId = '" + formAutoId + "'");
//		单据创建人
		Long icreateby = formData.getLong("icreateby");
//		提审人跟单据创建人要是同一个用户
		ValidationUtils.isTrue(Objects.equals(icreateby, user.getId()), "提审人必须是单据创建人");

//		根据表单编码找出审批/审核配置信息
		AuditFormConfig auditFormConfig = auditFormConfigService.findByFormSn(formSn);
		if (isOk(auditFormConfig)){
			Integer iType = auditFormConfig.getIType();
			tx(() -> {
				if (iType==1){  //1为审批
					update("update "+formSn+" set iAuditWay = 2, iAuditStatus = 1, dSubmitTime = CONVERT (nvarchar" +
							"(12),GETDATE()," +
							"112) where iAutoId ='"+formAutoId+"'");

//					将审批配置 单独复制出来 防止配置被修改
					Long iFormId = auditFormConfig.getIFormId();
//					找出审批流主表数据
					ApprovalM approvalM = approvalMService.findByFormId(iFormId);
					ValidationUtils.isTrue(approvalM != null,"未找到审批流的基础信息，请前往审批流设置检查一下");
                    Long Mid = approvalM.getIAutoId();

//                    copyObj
					FormApproval formApproval = copySetObj(approvalM, iFormId, formAutoId, user);
					formApproval.save();
					Long Aid = formApproval.getIAutoId();
//					找出审批流的所有节点
					List<ApprovalD> listByMid = approvalDService.findListByMid(Mid.toString());
					if (listByMid.size() > 0){
//						流程子表集合
						List<FormApprovalFlowD> flowDList = new ArrayList<>();
						listByMid.forEach(approvalD -> {
							/**
							 * 开始梳理审批流的整体流程
							 */
//							流程主表
							FormApprovalFlowM flowM = new FormApprovalFlowM();

//							copyObj
							FormApprovalD formApprovalD = formApprovalDService.copySetObj(Aid, approvalD);
							formApprovalD.save();

//							单据节点的ID
							Long formApprovalDid = formApprovalD.getIAutoId();
//							配置节点的ID
							Long did = approvalD.getIAutoId();

							flowM.setIApprovalId(Aid); //单据审批流ID
							flowM.setIApprovalDid(formApprovalDid); //单据审批节点ID
							flowM.setISeq(formApprovalD.getISeq()); //节点顺序
							flowM.setIApprovalWay(formApprovalD.getIApprovalWay()); //多人审批方式
							flowM.setIAuditStatus(1);
							flowM.save();
							Long flowMId = flowM.getIAutoId();


							/**
							 * 部门设置的配置信息
							 */
							Integer iSupervisorType = formApprovalD.getISupervisorType(); //主管类型
							Boolean isDirectOnMissing = formApprovalD.getIsDirectOnMissing();  //是否上级代审
							Integer iSkipOn = formApprovalD.getISkipOn(); //为空是否跳过或是指定审批人
							Long iSpecUserId = formApprovalD.getISpecUserId(); //指定审批人

							String cdeptNum = person.getCdeptNum(); //提审人部门
							Kv parm = new Kv();
							parm.set("dept",cdeptNum);
							//找到上级本级部门主管
							List<Record> list = dbTemplate("formapproval.getDeptDataTree", parm).find();
							int size = list.size();

							Boolean isNullPerson = false; //判断审批人是否为空
							int getStair = 0;   //获取集合里一级数据
							int getSuperior = 1; //获取集合上级数据

							/**
							 * 审批人设置
							 */
							switch (formApprovalD.getIType()){

								case 1:  // 指定人员
//							    查询是否存在人员配置信息
								List<ApprovaldUser> usersByDid = approvaldUserService.findUsersByDid(did);
								if (usersByDid.size() > 0){
									List<FormapprovaldUser> formapprovaldUserList = new ArrayList<>();
									usersByDid.forEach(approvaldUser -> {
										FormapprovaldUser formapprovaldUser = new FormapprovaldUser();
										formapprovaldUser.setIFormApprovalId(formApprovalDid);
										formapprovaldUser.setISeq(approvaldUser.getISeq());
										formapprovaldUser.setIUserId(approvaldUser.getIUserId());
										formapprovaldUser.setIAuditStatus(1);
										formapprovaldUser.setIPersonId(approvaldUser.getIPersonId());
										formapprovaldUserList.add(formapprovaldUser);

										FormApprovalFlowD flowD = new FormApprovalFlowD();
										flowD.setIFormApprovalFlowMid(flowMId);
										flowD.setISeq(approvaldUser.getISeq());
										flowD.setIUserId(approvaldUser.getIUserId());
										flowD.setIAuditStatus(1);
										flowDList.add(flowD);
									});
									formapprovaldUserService.batchSave(formapprovaldUserList);
								} else {
									/**
									 * 原型没有判断该审批方式时 审批人为空是否自动通过
									 */
									/*if (Objects.equals(1,formApprovalD.getISkipOn())){
//										set审批状态与审批时间
										formApprovalD.setIStatus(2);
										formApprovalD.setDAuditTime(nowDate);
										formApprovalD.update();
									}*/
									ValidationUtils.isTrue(false, "该审批顺序为"+formApprovalD.getISeq()+"配置的指定人员未指定人员");
										}
									break;
								case 2: //部门主管
									/**
									 * 先判断几级主管 然后找人
									 * type =1 直接主管 =2 一级主管 =3 二级主管
									 *   1、不存在主管 查看是否开启 由上级主管代审批
									 *      11、开启
									 *        111、不存在主管  判断审批人为空的情况
									 *           11111、自动通过
									 *           22222、指定人审批
									 *        222、存在主管
									 *      22、关闭
									 *        直接判断审批人为空的情况
									 *        111、自动通过
									 * 		  222、指定人审批
									 */


									switch (iSupervisorType){
										case 1:  //直接主管
											getStair = 0;
											getSuperior = 1;
											break;
										case 2:  //一级主管
											getStair = 1;
											getSuperior = 2;
											break;
										case 3:  //二级主管
											getStair = 2;
											getSuperior = 3;
											break;
										default:
											break;
									}

									if (size>getStair){
										Record record = list.get(getStair);
										Long idutyuserid = record.getLong("idutyuserid");
										if (idutyuserid!=null){
//											通过人员档案ID 找到 用户表ID
											Record personUser = findFirstRecord("select * from Bd_Person where " +
													"iAutoId = " + idutyuserid);
											Long iuserid = personUser.getLong("iuserid");
                                            String cpsnname = personUser.getStr("cpsnname");
                                            if (iuserid!=null){
												FormApprovalFlowD flowD = new FormApprovalFlowD();
												flowD.setIFormApprovalFlowMid(flowMId);
												flowD.setISeq(1);
												flowD.setIUserId(iuserid);
												flowD.setIAuditStatus(1);
												flowDList.add(flowD);
											} else {
												ValidationUtils.isTrue(false, cpsnname+"该人员还未匹配所属用户名");
											}
										} else {   //为空 检查是否开启上级代审
											if (isDirectOnMissing && size>getSuperior){  //开启 且 存在上级
												Record record1 = list.get(getSuperior);
												Long idutyuserid1 = record1.getLong("idutyuserid");
												if (idutyuserid1!=null) {  //且上级负责人不为空
													FormApprovalFlowD flowD = new FormApprovalFlowD();
													flowD.setIFormApprovalFlowMid(flowMId);
													flowD.setISeq(1);
													flowD.setIUserId(idutyuserid1);
													flowD.setIAuditStatus(1);
													flowDList.add(flowD);
												} else {  //上级负责人为空
													isNullPerson = true;
												}
											} else { //未开启 或者 上级为空
												isNullPerson = true;
											}

											/**
											 * 如果 直属主管为空
											 * (1、开启上级代审 上级主管也为空
											 * 或
											 * 2、未开启上级代审)
											 * 且 开启审批人为空时指定审批人
											 *
											 * 否则 检查是否开启审批人为空时自动通过
											 */
											if (isNullPerson){
												if (iSkipOn==2 && iSpecUserId != null){
													FormApprovalFlowD flowD = new FormApprovalFlowD();
													flowD.setIFormApprovalFlowMid(flowMId);
													flowD.setISeq(1);
													flowD.setIUserId(iSpecUserId);
													flowD.setIAuditStatus(1);
													flowDList.add(flowD);
												}

												if (iSkipOn==1){
													FormApprovalFlowD flowD = new FormApprovalFlowD();
													flowD.setIFormApprovalFlowMid(flowMId);
													flowD.setISeq(1);
													flowD.setIAuditStatus(1);
													flowDList.add(flowD);
												}
										}
										}
									}
									break;

								case 3: //直属主管
									/**
									 * 直属主管
									 * 判断 审批人为空的情况
									 */
									if (size>getStair){
										Record record = list.get(getStair);
										Long idutyuserid = record.getLong("idutyuserid");
										if (idutyuserid!=null){
											FormApprovalFlowD flowD = new FormApprovalFlowD();
											flowD.setIFormApprovalFlowMid(flowMId);
											flowD.setISeq(1);
											flowD.setIUserId(idutyuserid);
											flowD.setIAuditStatus(1);
											flowDList.add(flowD);
										} else {   //为空
											/**
											 * 如果 直属主管为空
											 * 且 开启审批人为空时指定审批人
											 */
											if (iSkipOn==2 && iSpecUserId != null){
												FormApprovalFlowD flowD = new FormApprovalFlowD();
												flowD.setIFormApprovalFlowMid(flowMId);
												flowD.setISeq(1);
												flowD.setIUserId(iSpecUserId);
												flowD.setIAuditStatus(1);
												flowDList.add(flowD);
											}
										}
									}
									break;

								case 4: //发起人自己

									FormApprovalFlowD flowD = new FormApprovalFlowD();
									flowD.setIFormApprovalFlowMid(flowMId);
									flowD.setISeq(1);
									flowD.setIUserId(user.getId());
									flowD.setIAuditStatus(1);
									flowDList.add(flowD);

									break;

								case 5: //角色

//							        查询是否存在配置角色信息
									List<ApprovaldRole> rolesByDid = approvaldRoleService.findRolesByDid(did);
									if (rolesByDid.size() > 0){
										List<FormapprovaldRole> formapprovaldRoleList = new ArrayList<>();
										rolesByDid.forEach(approvaldRole -> {
											FormapprovaldRole formapprovaldRole = new FormapprovaldRole();
											formapprovaldRole.setIFormApprovalId(formApprovalDid);
											formapprovaldRole.setISeq(approvaldRole.getISeq());
											formapprovaldRole.setIAuditStatus(1);
											formapprovaldRole.setIRoleId(approvaldRole.getIRoleId());
											formapprovaldRoleList.add(formapprovaldRole);
											List<User> users = getRoles(approvaldRole.getIRoleId());

											if (users.size() > 0){
												users.forEach(u -> {
													FormApprovalFlowD flowD1 = new FormApprovalFlowD();
													flowD1.setIUserId(u.getId());
													flowD1.setIFormApprovalFlowMid(flowMId);
													flowD1.setIAuditStatus(1);
													flowD1.setISeq(1);
													flowDList.add(flowD1);
												});
											}
										});
										formapprovaldRoleService.batchSave(formapprovaldRoleList);
									} else {
										ValidationUtils.isTrue(false, "该审批顺序为"+formApprovalD.getISeq()+"配置的角色未指定角色");
									}

									break;
								default:
									break;
							}
						});
						flowDService.batchSave(flowDList,flowDList.size());  //保存审批流程的审批人

                        //		流程主表数据
                        List<FormApprovalFlowM> flowMList = flowMService.find("select * from Bd_FormApprovalFlowM " +
                                "where iApprovalId = " + Aid);
                        Map<Long, FormApprovalFlowM> flowMMap = flowMList.stream().collect(Collectors.toMap(FormApprovalFlowM::getIApprovalDid, Function.identity(), (key1,
                                                                                                                                                                      key2) -> key2));

//                     判断下一节点
                        nextNode(formApproval, Aid, flowMMap);

					} else {
						ValidationUtils.isTrue(false, "提交失败，该审批流未配置具体审批节点");
					}
				} else {  //审核
					update("update "+formSn+" set iAuditStatus = 1, iAuditWay = 1, dSubmitTime = CONVERT (nvarchar" +
							"(12),GETDATE()," +
							"112) where iAutoId ='"+formAutoId+"'");
				}
				return true;
			});
		} else {
			ValidationUtils.isTrue(false, "该类型单据未配置审核/审批流程或是审核/审批设置未启用！");
		}
		return SUCCESS;
	}

	/**
	 * 获取角色人员信息
	 * @param role
	 * @return
	 */
	public List<User> getRoles(Long role){
		return userService.find("select * from jb_user where (',' + roles + ',') like '%,"+role+",%' and enable = 1");
	}

	/**
	 * 复制Obj
	 * @param approvalM
	 * @param iFormId
	 * @param formAutoId
	 * @param user
	 * @return
	 */
	public FormApproval copySetObj(ApprovalM approvalM,Long iFormId, String formAutoId, User user){
		FormApproval formApproval = new FormApproval();
		formApproval.setIOrgId(approvalM.getIOrgId());
		formApproval.setCOrgCode(approvalM.getCOrgCode());
		formApproval.setCOrgName(approvalM.getCOrgName());
		formApproval.setIApprovalId(approvalM.getIAutoId());
		formApproval.setIFormId(iFormId);
		formApproval.setIFormObjectId(Long.parseLong(formAutoId));
		formApproval.setIsSkippedOnDuplicate(approvalM.getIsSkippedOnDuplicate());
		formApproval.setIsApprovedOnSame(approvalM.getIsApprovedOnSame());
		formApproval.setICreateBy(user.getId());
		formApproval.setCCreateName(user.getName());
		formApproval.setDCreateTime(new Date());
		return formApproval;
	}

	/**
	 * 审批
	 * @param kv
	 * @return
	 */
	public Ret approve(Kv kv){
		String formSn = kv.getStr("formSn");
		String formAutoId = kv.getStr("formAutoId");
		Integer status = kv.getInt("status");
		Date nowDate = new Date();
		ValidationUtils.isTrue(formAutoId!=null,"参数异常,请保存单据后刷新重试！");

//		查出单据对应的审批流配置
		FormApproval formApproval = findByFormAutoId(formAutoId);
		ValidationUtils.isTrue(formApproval!=null,"单据未提交审批！");
		Long Mid = formApproval.getIAutoId();

		Long iCreateBy = formApproval.getICreateBy(); //提审人
		Boolean isSkippedOnDuplicate = formApproval.getIsSkippedOnDuplicate(); //去重
		Boolean isApprovedOnSame = formApproval.getIsApprovedOnSame();  //审批人与发起人同一人 自动通过

//		流程主表数据
		List<FormApprovalFlowM> flowMList = flowMService.find("select * from Bd_FormApprovalFlowM where iApprovalId = " + Mid);
		Map<Long, FormApprovalFlowM> flowMMap = flowMList.stream().collect(Collectors.toMap(FormApprovalFlowM::getIApprovalDid, Function.identity(), (key1,
																																					 key2) -> key2));
//		当前登录人
		User user = JBoltUserKit.getUser();
		Long userId = user.getId();

		/**
		 * 1、按顺序查出审批节点
		 * 2、过滤出不通过的节点 若有直接跳出
		 * 3、过滤出未通过的节点 执行审批
		 */
//		1、
		List<FormApprovalD> formApprovalDList = formApprovalDService.findListByMid(Mid);
//		2、
		List<FormApprovalD> failNode = formApprovalDList.stream().filter(f -> Objects.equals(f.getIStatus(), 3)).collect(Collectors.toList());
		ValidationUtils.isTrue((failNode.size() <= 0), "该单据已审批不通过，审批流程已结束！");
//      3、
		List<FormApprovalD> processNode = formApprovalDList.stream().filter(f -> Objects.equals(f.getIStatus(), 1)).collect(Collectors.toList());
		int processNodeSize = processNode.size();
//		审批
		if ( processNodeSize > 0 ){
			FormApprovalD formApprovalD = processNode.get(0);
			Long Did = formApprovalD.getIAutoId();
			Integer iApprovalWay = formApprovalD.getIApprovalWay(); //多人审批的方式


			/**
			 * 单据流程主表
			 * 找出流程及所有该节点的审批人
			 */
			FormApprovalFlowM flowM = flowMMap.get(Did);
			Long FMid = flowM.getIAutoId();

//			找出该节点的审批人
			List<FormApprovalFlowD> flowDList = flowDService.find("select * from Bd_FormApprovalFlowD where " +
					"iFormApprovalFlowMid = " + FMid + " order by iSeq asc");

//			过滤出未审核的人员
			List<FormApprovalFlowD> collect = flowDList.stream().filter(f -> Objects.equals(f.getIAuditStatus(), 1)).collect(Collectors.toList());

			/**
			 * 单据节点集合
			 * 单据流程行表集合
			 * 节点是否更改状态
			 */
			List<FormApprovalD> nodeList = new ArrayList<>();
			List<FormApprovalFlowD> approvalFlowDList = new ArrayList<>();
			Boolean nodeIsOk = false;

			/**
			 * 若有审批人
			 * 判断审批方式 1、依次审批 2、会签（所有人同意  3、或签（一人即可
			 *
			 * 若没有未审核的人
			 * 说明还没到当前审批人
			 */
			int collectSize = collect.size();
			if (collectSize > 0) {
				switch (iApprovalWay) {
					case 1:  //依次审批
						FormApprovalFlowD flowD = collect.get(0);
						if (Objects.equals(flowD.getIUserId(), userId)) {
							flowD.setIAuditStatus(status);
							approvalFlowDList.add(flowD);

//							该节点状态
							nodeIsOk = collectSize <= 1;

						} else {
							ValidationUtils.isTrue(false, "上一审批人还未审批");
						}
						break;
					case 2:   //会签

						for (int i = 0; i < collectSize; i++) {
							FormApprovalFlowD formApprovalFlowD = collect.get(i);
							if (Objects.equals(formApprovalFlowD.getIUserId(), userId)) {
								formApprovalFlowD.setIAuditStatus(status);
								approvalFlowDList.add(formApprovalFlowD);

								nodeIsOk = collectSize <= 1;
							}
						}
						if (approvalFlowDList.size() <= 0) {
							ValidationUtils.isTrue(false, "上一审批人还未审批");
						}
						break;
					case 3:   //或签
						for (int i = 0; i < collectSize; i++) {
							FormApprovalFlowD formApprovalFlowD = collect.get(i);
							if (Objects.equals(formApprovalFlowD.getIUserId(), userId)) {
								formApprovalFlowD.setIAuditStatus(status);
								approvalFlowDList.add(formApprovalFlowD);

								nodeIsOk = true;
							}
						}
						if (approvalFlowDList.size() <= 0) {
							ValidationUtils.isTrue(false, "上一审批人还未审批");
						}
						break;
					default:
						break;
				}

			} else {
				ValidationUtils.isTrue(false, "上一审批人还未审批");
			}

//			节点 更新审批状态
			if (status==3 || nodeIsOk){
				formApprovalD.setIStatus(status);
				formApprovalD.setDAuditTime(nowDate);
				nodeList.add(formApprovalD);
			}


			Boolean finalNodeIsOk = nodeIsOk;
			tx(()->{
				if (nodeList.size()>0) {
					formApprovalDService.batchUpdate(nodeList);
				}
				if (approvalFlowDList.size()>0){
					flowDService.batchUpdate(approvalFlowDList);
				}


//				审批不通过 更新单据状态 时间
				if (status==3){
					update("update "+formSn+" set iAuditStatus = 3, dAuditTime = CONVERT (nvarchar(12)," +
							"GETDATE(),112) where iAutoId ='"+formAutoId+"'");

//					更新单据审批流主表
					formApproval.setIsDeleted(true);
					formApproval.update();

				} else {

                    /**
                     * 判断下一步情况 即当前一步的审批过程已结束
                     * 1、该节点已通过 判断下一节点
                     * 2、当前节点还未通过 判断下一审批人
                     */
//                    1、
					if (finalNodeIsOk){

//                      判断下一节点
                        nextNode(formApproval, Mid, flowMMap);

//						2、
					} else {
 //				        判断下一审批人
                        if ( collectSize > 1){

//			                已审核通过的审批人
                            List<FormApprovalFlowD> successFlowDList = flowDService.find("select *\n" +
                                    "from Bd_FormApprovalFlowD t1\n" +
                                    "where iFormApprovalFlowMid in (select iAutoId\n" +
                                    "                               from Bd_FormApprovalFlowM\n" +
                                    "                               where iApprovalId = " + Mid + " )\n" +
                                    "  and t1.iAuditStatus = 2 order by iSeq asc ");

                            List<Long> successUser = successFlowDList.stream().map(FormApprovalFlowD::getIUserId).collect(Collectors.toList());

                            List<FormApprovalFlowD> forApprovalFlowDList = new ArrayList<>();
                            Boolean killNode = false;
                            for (int k = 1; k < collectSize; k++) {
                                FormApprovalFlowD flowD = collect.get(k);
                                Long iUserId = flowD.getIUserId();
                                if (iUserId==null){
//                                    好像无需 判断为空是否跳过 页面限制了 与 梳理流程时已经判断
                                    flowD.setIAuditStatus(status);
                                    forApprovalFlowDList.add(flowD);
                                } else {
                                    if (isSkippedOnDuplicate){
                                         if (successUser.contains(iUserId)){
                                             flowD.setIAuditStatus(status);
                                             forApprovalFlowDList.add(flowD);
                                             continue;
                                         } else {
                                             killNode = true;
                                         }
                                    }
                                    if (isApprovedOnSame){
                                        if (Objects.equals(iCreateBy, iUserId)){
                                            flowD.setIAuditStatus(status);
                                            forApprovalFlowDList.add(flowD);
                                            continue;
                                        } else {
                                            killNode = true;
                                        }
                                    }
                                    if (killNode){
                                        break;
                                    }
                                }
                            }

                            if (forApprovalFlowDList.size() > 0){
                                flowDService.batchUpdate(forApprovalFlowDList);

//			                    再次找出该节点的未审批人
                                List<FormApprovalFlowD> processFlowD = flowDService.find("select * from " +
                                        "Bd_FormApprovalFlowD where " +
                                        "iFormApprovalFlowMid = " + FMid + " and iAuditStatus = 1 order by iSeq asc");

                                if (processFlowD.size() <= 0) {
//                                  更新当前节点
                                    formApprovalD.setIStatus(status);
                                    formApprovalD.setDAuditTime(nowDate);
                                    formApprovalD.update();
//                                    判断下一节点
                                    nextNode(formApproval, Mid, flowMMap);
                                }
                            }
                        }
                    }
				}

				return true;
			});

		} else {
			ValidationUtils.isTrue(false, "审批流程已结束");
		}

		return SUCCESS;
	}

    /**
     * 下个节点判断
     * @param formApproval
     * @param flowMMap
     * @param Aid
     */
	public void nextNode(FormApproval formApproval, Long Aid, Map<Long,FormApprovalFlowM> flowMMap){

        /**
         * 找出未审批通过的节点
         */
        List<FormApprovalD> processNode = formApprovalDService.findListByAid(Aid);
        int size = processNode.size();

        if (size>0){

            Long iCreateBy = formApproval.getICreateBy(); //提审人
            Boolean isSkippedOnDuplicate = formApproval.getIsSkippedOnDuplicate(); //去重
            Boolean isApprovedOnSame = formApproval.getIsApprovedOnSame();  //审批人与发起人同一人 自动通过

            FormApprovalD formApprovalD = processNode.get(0);
            Long Mid = formApprovalD.getIFormApprovalId();
            Long Did = formApprovalD.getIAutoId();
            Integer iApprovalWay = formApprovalD.getIApprovalWay(); //多人审批的方式

            /**
             * 单据流程主表
             * 找出流程及所有该节点的审批人
             */
            FormApprovalFlowM flowM = flowMMap.get(Did);
            Long FMid = flowM.getIAutoId();

//			找出该节点的审批人
            List<FormApprovalFlowD> flowDList = flowDService.find("select * from Bd_FormApprovalFlowD where " +
                    "iFormApprovalFlowMid = " + FMid + " order by iSeq asc");

//			过滤出未审核的人员
            List<FormApprovalFlowD> collect = flowDList.stream().filter(f -> Objects.equals(f.getIAuditStatus(), 1)).collect(Collectors.toList());

            int collectSize = collect.size();
            if (collectSize > 0) {

//			已审核通过的审批人
                List<FormApprovalFlowD> successFlowDList = flowDService.find("select *\n" +
                        "from Bd_FormApprovalFlowD t1\n" +
                        "where iFormApprovalFlowMid in (select iAutoId\n" +
                        "                               from Bd_FormApprovalFlowM\n" +
                        "                               where iApprovalId = " + Mid + " )\n" +
                        "  and t1.iAuditStatus = 2 order by iSeq asc ");

                List<Long> successUser = successFlowDList.stream().map(FormApprovalFlowD::getIUserId).collect(Collectors.toList());

                List<FormApprovalFlowD> forApprovalFlowDList = new ArrayList<>();
                boolean killNode = false;
                for (int k = 0; k < collectSize; k++) {
                    FormApprovalFlowD flowD = collect.get(k);
                    Long iUserId = flowD.getIUserId();
                    if (iUserId==null){
//                  好像无需 判断为空是否跳过 页面限制了 与 梳理流程时已经判断
                        flowD.setIAuditStatus(2);
                        forApprovalFlowDList.add(flowD);
                    } else {
                        if (isSkippedOnDuplicate){
                            if (successUser.contains(iUserId)){
                                flowD.setIAuditStatus(2);
                                forApprovalFlowDList.add(flowD);
                                continue;
                            } else {
                                killNode = true;
                            }
                        }
                        if (isApprovedOnSame){
                            if (Objects.equals(iCreateBy, iUserId)){
                                flowD.setIAuditStatus(2);
                                forApprovalFlowDList.add(flowD);
                                continue;
                            } else {
                                killNode = true;
                            }
                        }
                        if (killNode){
                            break;
                        }
                    }
                }

                if (forApprovalFlowDList.size() > 0){
                    flowDService.batchUpdate(forApprovalFlowDList);

//			                    再次找出该节点的未审批人
                    List<FormApprovalFlowD> processFlowD = flowDService.find("select * from " +
                            "Bd_FormApprovalFlowD where " +
                            "iFormApprovalFlowMid = " + FMid + " and iAuditStatus = 1 order by iSeq asc");

                    /**
                     * 1、没有未审批人 则该节点结束 更新已通过状态
                     * 2、有未审批人 但是小于该节点所有审批人且该节点为或签 则该节点结束 更新已通过状态
                     * 然后判断下个节点
                     * 否则 退出
                     */
                    if ((processFlowD.size() <= 0) || ((flowDList.size() > processFlowD.size()) && iApprovalWay==3)){
//                  更新当前节点
                        formApprovalD.setIStatus(2);
                        formApprovalD.setDAuditTime(new Date());
                        formApprovalD.update();
//                  判断下一节点
                    nextNode(formApproval, Aid, flowMMap);
                    }
                }
            }
        } else {

//            更新单据状态
            Long iFormId = formApproval.getIFormId();
            Long iFormObjectId = formApproval.getIFormObjectId();
            Form form = formService.findFirst("select * from Bd_Form where iAutoId = " + iFormId);
            String formSn = form.getCFormCode();
            if (isOk(form)){

                update("update "+formSn+" set iAuditStatus = 2, dAuditTime = CONVERT (nvarchar(12)," +
                        "GETDATE(),112) where iAutoId ='"+iFormObjectId+"'");

            } else {
                ValidationUtils.isTrue(false, "找不到表单配置");
            }

        }


	}

	/**
	 * 审批
	 * @param kv
	 * @return
	 */
	public Ret approveNode(Kv kv){
		String formSn = kv.getStr("formSn");
		String formAutoId = kv.getStr("formAutoId");
		Integer status = kv.getInt("status");
		Boolean judge = kv.getBoolean("judge");
		Date nowDate = new Date();
		ValidationUtils.isTrue(formAutoId!=null,"参数异常,请保存单据后刷新重试！");

//		查出单据对应的审批流配置
		FormApproval approval = findByFormAutoId(formAutoId);
		ValidationUtils.isTrue(approval!=null,"单据未提交审批！");
		Long Mid = approval.getIAutoId();

//		流程主表数据
		List<FormApprovalFlowM> flowMList = flowMService.find("select * from Bd_FormApprovalFlowM where iApprovalId = " + Mid);
		Map<Long, FormApprovalFlowM> flowMMap = flowMList.stream().collect(Collectors.toMap(FormApprovalFlowM::getIApprovalDid, Function.identity(), (key1,
																																					  key2) -> key2));
		User user = JBoltUserKit.getUser();
		Long userId = user.getId();

		/**
		 * 1、按顺序查出审批节点
		 * 2、先判断该节点是否已通过，标识为 节点审批状态IStatus为2
		 * 3、若该节点未通过，查看节点 作具体审批判断
		 */
		List<FormApprovalD> listByMid = formApprovalDService.findListByMid(Mid);
		int size = listByMid.size();

		/**
		 * 单据节点集合
		 * 单据流程主表集合
		 * 单据流程行表集合
		 */
		List<FormApprovalD> dList = new ArrayList<>();
		List<FormApprovalFlowM> approvalFlowMList = new ArrayList<>();
		List<FormApprovalFlowD> approvalFlowDList = new ArrayList<>();

//		if (size > 0){}
		listByMid.forEach(formApprovalD -> {   //已是按iSeq的asc顺序遍历
			Long Did = formApprovalD.getIAutoId();
			Integer iStatus = formApprovalD.getIStatus();  //审批节点的审批状态
			Integer iType = formApprovalD.getIType(); //审批节点定义的审批方式
			Integer iSeq = formApprovalD.getISeq(); //审批节点的顺序
			Integer iApprovalWay = formApprovalD.getIApprovalWay(); //多人审批的方式
			Integer iSkipOn = formApprovalD.getISkipOn();  //当审批人为空 1、自动通过 2、指定人
			if (iStatus==1){ //节点待审批

				/**
				 * 单据流程主表
				 * 判断状态是否审核已通过
				 * 若通过 更新该节点状态为已通过 退出当前节点 到下一节点
				 * 否则
				 * 找出流程及所有该节点的审批人
				 */
				FormApprovalFlowM flowM = flowMMap.get(Did);
				Long FMid = flowM.getIAutoId();
				Integer miAuditStatus = flowM.getIAuditStatus();
				if (miAuditStatus==2){
					formApprovalD.setIStatus(status);
					formApprovalD.setDAuditTime(nowDate);
					dList.add(formApprovalD);
					return;
				} else {
					List<FormApprovalFlowD> flowDList = flowDService.find("select * from Bd_FormApprovalFlowD where " +
							"iFormApprovalFlowMid = " + FMid +" order by iSeq asc");

//					过滤出未审核的人员
					List<FormApprovalFlowD> collect = flowDList.stream().filter(f -> Objects.equals(f.getIAuditStatus(), 1)).collect(Collectors.toList());

					/**
					 * 若有审批人
					 * 判断审批方式 1、依次审批 2、会签（所有人同意  3、或签（一人即可
					 *
					 * 若没有未审核的人
					 * 1、判断是审批人为空 自动通过 (没必要判断）
					 * 若1为否 说明该节点已通过
					 */
					int collectSize = collect.size();
					if (collectSize > 0) {

						switch (iApprovalWay){
							case 1:
								FormApprovalFlowD flowD = collect.get(0);
								if (Objects.equals(flowD.getIUserId(), userId)){
									flowD.setIAuditStatus(2);

								}
								break;
							case 2:

								break;
							case 3:
								break;
							default:
								break;
						}

						collect.forEach(formApprovalFlowD -> {

						});
					} else {
						/*if (Objects.equals(1,iSkipOn)){
						}*/

						flowM.setIAuditStatus(2);
						approvalFlowMList.add(flowM);

						formApprovalD.setIStatus(status);
						formApprovalD.setDAuditTime(nowDate);
						dList.add(formApprovalD);
					}
				}


				switch (iType){  //审批人设置
					case 1:  //指定人员
//						根据asc顺序找出该节点的所有人
						List<FormapprovaldUser> userList = formapprovaldUserService.findByDid(Did);
//						过滤出 人员信息的审批顺序与审批状态
						Map<Integer, Integer> iSeqAndStatusMap =
								userList.stream().collect(Collectors.toMap(FormapprovaldUser::getISeq,
										FormapprovaldUser::getIAuditStatus,(key1,key2)->key2));
//						找出符合当前审批人
						Optional<FormapprovaldUser> first = userList.stream().filter(formapprovaldUser -> userId.equals(formapprovaldUser.getIUserId())).findFirst();
						/**
						 * 若 该节点审批人为空已在提交审批判断该节点审批状态
						 * 逻辑闭环
						 * 所以不存在说明该审批人不存在该节点
						 */
						ValidationUtils.isTrue(first.isPresent(), "上一审批人还未审批");
						if (iApprovalWay==1){ //依次审批
							FormapprovaldUser formapprovaldUser = first.get();  //当前审批人
							Integer userISeq = formapprovaldUser.getISeq();     //当前审批人顺序
//							如果不是第一个 需判断上一审批人审批状态
							if (userISeq>1){
								Integer upSeq = userISeq-1;
								Integer upStatus = iSeqAndStatusMap.get(upSeq);
								ValidationUtils.isTrue((upStatus==2 || upStatus==3), "上一审批人还未审批");
								ValidationUtils.isTrue(upStatus==3, "上一审批人审批不通过，该单据已不通过");
							}
							/**
							 * start
							 * 当前审批人审批
							 */
							formapprovaldUser.setIAuditStatus(status);
							formapprovaldUser.setDAuditTime(nowDate);
							formapprovaldUser.update();  //保存

							/**
							 * 记录具体审批人信息
							 */
							FormapprovaldPerson formapprovaldPerson = new FormapprovaldPerson();
							formapprovaldPerson.setIApprovalDid(Did);
							formapprovaldPerson.setIUserId(userId);
							formapprovaldPerson.setIAuditStatus(status);
							formapprovaldPerson.setIAuuditTime(nowDate);
							formapprovaldPerson.save();

							/**
							 * 判断当前审批人是否最后一个审批人
							 * 若 作为最后审批人 则  该节点结束 节点审批状态更新
							 */
							if (userISeq==userList.size()){
								formApprovalD.setIStatus(status);
								formApprovalD.setDAuditTime(nowDate);
								formApprovalD.update(); //保存
							}

						} else if (iApprovalWay==2){  //会签



						} else {  //或签

						}

						break;
					case 2:

						break;
					case 3:

						break;
					default:
						break;
				}

				/**
				 * 需判断status==3 不通过
				 * 则 该节点 不通过
				 * 则为整张单据不通过 iAuditStatus=3不通过
				 * 审批流结束
				 */
				if (status==3){
					formApprovalD.setIStatus(status);
					formApprovalD.setDAuditTime(nowDate);
					formApprovalD.update(); //保存

					update("update "+formSn+" set iAuditStatus = 3, dAuditTime = CONVERT (nvarchar(12)," +
							"GETDATE(),112) where iAutoId ='"+formAutoId+"'");

					approval.setIsDeleted(false);
					approval.update();  //保存
				} else {  //通过
					/**
					 * 判断各个节点的状态 整条审批流是否完成 更新单据状态
					 */
//					 查询该审批流所有已审批的节点
					List<FormApprovalD> formApprovalDS = formApprovalDService.find("select * from Bd_FormApprovalD where iFormApprovalId = '" + Mid + "' " +
							"and " +
							" iStatus = 2");
					/**
					 * 若该审批流 已审批的节点数==所有节点数
					 * 说明该审批流结束 该单据审批通过
					 */
					if (formApprovalDS.size()==size){
						update("update "+formSn+" set iAuditStatus = 2, dAuditTime = CONVERT (nvarchar(12)," +
								"GETDATE(),112) where iAutoId ='"+formAutoId+"'");

					} else {
						/**
						 * 若还没结束 判断审批流基础配置的高级设置
						 * isSkippedOnDuplicate 1为是: 审批流程中审批人重复出现时，只需审批一次其余自定通过
						 *  每个当前审批人 找到 下个审批人是否出现在之前审批过程中 是则找到的下一个审批人通过
						 */
						if (approval.getIsSkippedOnDuplicate()){

						}
					}
				}

			} else if (iStatus==2){ //节点审批通过

			} else if (iStatus==3){ //节点审批不通过

			} else {

			}

		});


		return SUCCESS;
	}

	/**
	 * 通过formAutoId 单据ID查出单据对应审批流配置
	 * @param formAutoId
	 * @return
	 */
	public FormApproval findByFormAutoId(String formAutoId){
		return findFirst("select * from Bd_FormApproval where iFormObjectId = '"+formAutoId+"' and isDeleted = '0'");
	}
}
