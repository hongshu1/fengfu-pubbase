package cn.rjtech.admin.formapproval;

import cn.hutool.core.util.ObjUtil;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.interfaces.ICallbackFunc;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.approvald.ApprovalDService;
import cn.rjtech.admin.approvaldrole.ApprovaldRoleService;
import cn.rjtech.admin.approvalduser.ApprovaldUserService;
import cn.rjtech.admin.approvalm.ApprovalMService;
import cn.rjtech.admin.auditformconfig.AuditFormConfigService;
import cn.rjtech.admin.form.FormService;
import cn.rjtech.admin.formapprovald.FormApprovalDService;
import cn.rjtech.admin.formapprovaldrole.FormapprovaldRoleService;
import cn.rjtech.admin.formapprovalduser.FormapprovaldUserService;
import cn.rjtech.admin.formapprovalflowd.FormApprovalFlowDService;
import cn.rjtech.admin.formapprovalflowm.FormApprovalFlowMService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.AuditTypeEnum;
import cn.rjtech.enums.AuditWayEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 表单审批流 Service
 *
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
     */
    public Page<FormApproval> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 保存
     */
    public Ret save(FormApproval formApproval) {
        if (formApproval == null || isOk(formApproval.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(formApproval.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = formApproval.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(formApproval.getIautoid(), JBoltUserKit.getUserId(), formApproval.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(FormApproval formApproval) {
        if (formApproval == null || notOk(formApproval.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        FormApproval dbFormApproval = findById(formApproval.getIAutoId());
        if (dbFormApproval == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(formApproval.getName(), formApproval.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = formApproval.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(formApproval.getIautoid(), JBoltUserKit.getUserId(), formApproval.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        return deleteByIds(ids, true);
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        return deleteById(id, true);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param formApproval 要删除的model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(FormApproval formApproval, Kv kv) {
        //addDeleteSystemLog(formApproval.getIautoid(), JBoltUserKit.getUserId(),formApproval.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param formApproval 要删除的model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(FormApproval formApproval, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(formApproval, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
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
     *
     * @param formApproval 要toggle的model
     * @param column       操作的哪一列
     * @param kv           携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(FormApproval formApproval, String column, Kv kv) {
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
     *
     * @param formApproval model
     * @param kv           携带额外参数一般用不上
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
     */
    public Ret judgeType(String formSn, Long formAutoId) {
        FormApproval byFormAutoId = findByFormAutoId(formAutoId);
        ValidationUtils.isTrue((notOk(byFormAutoId)), "该单据已提交审批，请勿重复提交审批！");

        // 提审人
        User user = JBoltUserKit.getUser();
        Person person = personService.findFirstByUserId(user.getId());
        // 获取单据信息
        Record formData = findFirstRecord("select * from " + formSn + " where iAutoId = ? ", formAutoId);
        ValidationUtils.notNull(formData, "单据不存在");
        ValidationUtils.isTrue(!formData.getBoolean(IS_DELETED), "单据已被删除");

        // 单据审核状态
        AuditStatusEnum auditStatusEnum = AuditStatusEnum.toEnum(formData.getInt(IAUDITSTATUS));
        // 允许提审的状态校验
        switch (auditStatusEnum) {
            case NOT_AUDIT:
            case REJECTED:
                break;
            default:
                return fail(String.format("当前状态“%s”，不可提交审核", auditStatusEnum.getText()));
        }

        // 单据创建人
        Long icreateby = formData.getLong("icreateby");
        // 提审人跟单据创建人要是同一个用户
        ValidationUtils.equals(icreateby, user.getId(), "提审人必须是单据创建人");

        Date now = new Date();

        // 根据表单编码找出审批/审核配置信息
        AuditFormConfig auditFormConfig = auditFormConfigService.findByFormSn(formSn);
        if (ObjUtil.isNull(auditFormConfig)) {
            return fail("该类型单据未配置审核/审批流程或是审核/审批设置未启用！");
        }

        tx(() -> {

            // 根据审批类型进行处理
            switch (AuditTypeEnum.toEnum(auditFormConfig.getIType())) {
                // 审批流
                case FLOW:
                    // 提审更新
                    ValidationUtils.isTrue(updateSubmit(formSn, formAutoId, AuditWayEnum.FLOW.getValue(), now), "单据当前状态不可提审");

                    // 将审批配置 单独复制出来 防止配置被修改
                    Long iFormId = auditFormConfig.getIFormId();
                    // 找出审批流主表数据
                    ApprovalM approvalM = approvalMService.findByFormId(iFormId);
                    ValidationUtils.notNull(approvalM, "未找到审批流的基础信息，请前往审批流设置检查一下");

                    Long mid = approvalM.getIAutoId();

                    // copyObj
                    FormApproval formApproval = copySetObj(approvalM, iFormId, formAutoId, user);
                    formApproval.save();

                    Long approvalIautoId = formApproval.getIAutoId();

                    // 找出审批流的所有节点
                    List<ApprovalD> listByMid = approvalDService.findListByMid(mid.toString());
                    if (listByMid.size() > 0) {
                        // 流程子表集合
                        List<FormApprovalFlowD> flowDList = new ArrayList<>();
                        listByMid.forEach(approvalD -> {
                            /*
                             * 开始梳理审批流的整体流程
                             */
                            // 流程主表
                            FormApprovalFlowM flowM = new FormApprovalFlowM();

                            // copyObj
                            FormApprovalD formApprovalD = formApprovalDService.copySetObj(approvalIautoId, approvalD);
                            formApprovalD.save();

                            // 单据节点的ID
                            Long formApprovalDid = formApprovalD.getIAutoId();
                            // 配置节点的ID
                            Long did = approvalD.getIAutoId();

                            // 单据审批流ID
                            flowM.setIApprovalId(approvalIautoId);
                            // 单据审批节点ID
                            flowM.setIApprovalDid(formApprovalDid);
                            // 节点顺序
                            flowM.setISeq(formApprovalD.getISeq());
                            // 多人审批方式
                            flowM.setIApprovalWay(formApprovalD.getIApprovalWay());
                            flowM.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                            flowM.save();

                            Long flowMId = flowM.getIAutoId();

                            /*
                             * 部门设置的配置信息
                             */
                            // 主管类型
                            Integer iSupervisorType = formApprovalD.getISupervisorType();
                            // 是否上级代审
                            Boolean isDirectOnMissing = formApprovalD.getIsDirectOnMissing();
                            // 为空是否跳过或是指定审批人
                            Integer iSkipOn = formApprovalD.getISkipOn();
                            // 指定审批人
                            Long iSpecUserId = formApprovalD.getISpecUserId();

                            // 提审人部门
                            String cdeptNum = person.getCdeptNum();

                            Kv param = Kv.by("dept", cdeptNum);

                            // 找到上级本级部门主管
                            List<Record> list = dbTemplate("formapproval.getDeptDataTree", param).find();

                            int size = list.size();

                            // 判断审批人是否为空
                            boolean isNullPerson = false;
                            // 获取集合里一级数据
                            int getStair = 0;
                            // 获取集合上级数据
                            int getSuperior = 1;

                            /*
                             * 审批人设置
                             */
                            switch (formApprovalD.getIType()) {
                                // 指定人员
                                case 1:
                                    // 查询是否存在人员配置信息
                                    List<ApprovaldUser> usersByDid = approvaldUserService.findUsersByDid(did);
                                    if (usersByDid.size() > 0) {
                                        List<FormapprovaldUser> formapprovaldUserList = new ArrayList<>();
                                        usersByDid.forEach(approvaldUser -> {
                                            FormapprovaldUser formapprovaldUser = new FormapprovaldUser();
                                            formapprovaldUser.setIFormApprovalId(formApprovalDid);
                                            formapprovaldUser.setISeq(approvaldUser.getISeq());
                                            formapprovaldUser.setIUserId(approvaldUser.getIUserId());
                                            formapprovaldUser.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                            formapprovaldUser.setIPersonId(approvaldUser.getIPersonId());
                                            formapprovaldUserList.add(formapprovaldUser);

                                            FormApprovalFlowD flowD = new FormApprovalFlowD();
                                            flowD.setIFormApprovalFlowMid(flowMId);
                                            flowD.setISeq(approvaldUser.getISeq());
                                            flowD.setIUserId(approvaldUser.getIUserId());
                                            flowD.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                            flowDList.add(flowD);
                                        });
                                        formapprovaldUserService.batchSave(formapprovaldUserList);
                                    } else {
                                        /*
                                         * 原型没有判断该审批方式时 审批人为空是否自动通过
                                         */
                                    /*if (Objects.equals(1,formApprovalD.getISkipOn())){
                                        // set审批状态与审批时间
                                        formApprovalD.setIStatus(2);
                                        formApprovalD.setDAuditTime(nowDate);
                                        formApprovalD.update();
                                    }*/
                                        ValidationUtils.error("该审批顺序为" + formApprovalD.getISeq() + "配置的指定人员未指定人员");
                                    }
                                    break;
                                case 2: //部门主管
                                    /*
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

                                    switch (iSupervisorType) {
                                        // 直接主管
                                        case 1:

                                            break;
                                        // 一级主管
                                        case 2:
                                            getStair = 1;
                                            getSuperior = 2;
                                            break;
                                        // 二级主管
                                        case 3:
                                            getStair = 2;
                                            getSuperior = 3;
                                            break;
                                        default:
                                            break;
                                    }

                                    if (size > getStair) {
                                        Record record = list.get(getStair);
                                        Long idutyuserid = record.getLong("idutyuserid");
                                        if (idutyuserid != null) {
                                            // 通过人员档案ID 找到 用户表ID
                                            Record personUser = findFirstRecord("select * from Bd_Person where iAutoId = ? ", idutyuserid);
                                            Long iuserid = personUser.getLong("iuserid");
                                            String cpsnname = personUser.getStr("cpsnname");
                                            if (iuserid != null) {
                                                FormApprovalFlowD flowD = new FormApprovalFlowD();
                                                flowD.setIFormApprovalFlowMid(flowMId);
                                                flowD.setISeq(1);
                                                flowD.setIUserId(iuserid);
                                                flowD.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                                flowDList.add(flowD);
                                            } else {
                                                ValidationUtils.error(cpsnname + "该人员还未匹配所属用户名");
                                            }
                                        } else {
                                            // 为空 检查是否开启上级代审
                                            if (isDirectOnMissing && size > getSuperior) {  // 开启 且 存在上级
                                                Record record1 = list.get(getSuperior);
                                                Long idutyuserid1 = record1.getLong("idutyuserid");
                                                if (idutyuserid1 != null) {  // 且上级负责人不为空
                                                    FormApprovalFlowD flowD = new FormApprovalFlowD();
                                                    flowD.setIFormApprovalFlowMid(flowMId);
                                                    flowD.setISeq(1);
                                                    flowD.setIUserId(idutyuserid1);
                                                    flowD.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                                    flowDList.add(flowD);
                                                } else {  // 上级负责人为空
                                                    isNullPerson = true;
                                                }
                                            } else { //未开启 或者 上级为空
                                                isNullPerson = true;
                                            }

                                            /*
                                             * 如果 直属主管为空
                                             * (1、开启上级代审 上级主管也为空
                                             * 或
                                             * 2、未开启上级代审)
                                             * 且 开启审批人为空时指定审批人
                                             *
                                             * 否则 检查是否开启审批人为空时自动通过
                                             */
                                            if (isNullPerson) {
                                                if (iSkipOn == 2 && iSpecUserId != null) {
                                                    FormApprovalFlowD flowD = new FormApprovalFlowD();
                                                    flowD.setIFormApprovalFlowMid(flowMId);
                                                    flowD.setISeq(1);
                                                    flowD.setIUserId(iSpecUserId);
                                                    flowD.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                                    flowDList.add(flowD);
                                                }

                                                if (iSkipOn == 1) {
                                                    FormApprovalFlowD flowD = new FormApprovalFlowD();
                                                    flowD.setIFormApprovalFlowMid(flowMId);
                                                    flowD.setISeq(1);
                                                    flowD.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                                    flowDList.add(flowD);
                                                }
                                            }
                                        }
                                    }
                                    break;

                                // 直属主管
                                case 3:
                                    /*
                                     * 直属主管
                                     * 判断 审批人为空的情况
                                     */
                                    if (size > getStair) {
                                        Record record = list.get(getStair);
                                        Long idutyuserid = record.getLong("idutyuserid");
                                        if (idutyuserid != null) {
                                            FormApprovalFlowD flowD = new FormApprovalFlowD();
                                            flowD.setIFormApprovalFlowMid(flowMId);
                                            flowD.setISeq(1);
                                            flowD.setIUserId(idutyuserid);
                                            flowD.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                            flowDList.add(flowD);
                                        } else {   //为空
                                            /*
                                             * 如果 直属主管为空
                                             * 且 开启审批人为空时指定审批人
                                             */
                                            if (iSkipOn == 2 && iSpecUserId != null) {
                                                FormApprovalFlowD flowD = new FormApprovalFlowD();
                                                flowD.setIFormApprovalFlowMid(flowMId);
                                                flowD.setISeq(1);
                                                flowD.setIUserId(iSpecUserId);
                                                flowD.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                                flowDList.add(flowD);
                                            }
                                        }
                                    }
                                    break;

                                // 发起人自己
                                case 4:
                                    FormApprovalFlowD flowD = new FormApprovalFlowD();
                                    flowD.setIFormApprovalFlowMid(flowMId);
                                    flowD.setISeq(1);
                                    flowD.setIUserId(user.getId());
                                    flowD.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                    flowDList.add(flowD);
                                    break;
                                // 角色
                                case 5:
                                    // 查询是否存在配置角色信息
                                    List<ApprovaldRole> rolesByDid = approvaldRoleService.findRolesByDid(did);
                                    if (rolesByDid.size() > 0) {
                                        List<FormapprovaldRole> formapprovaldRoleList = new ArrayList<>();
                                        rolesByDid.forEach(approvaldRole -> {
                                            FormapprovaldRole formapprovaldRole = new FormapprovaldRole();
                                            formapprovaldRole.setIFormApprovalId(formApprovalDid);
                                            formapprovaldRole.setISeq(approvaldRole.getISeq());
                                            formapprovaldRole.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                            formapprovaldRole.setIRoleId(approvaldRole.getIRoleId());
                                            formapprovaldRoleList.add(formapprovaldRole);
                                            List<User> users = getRoles(approvaldRole.getIRoleId());

                                            if (users.size() > 0) {
                                                users.forEach(u -> {
                                                    FormApprovalFlowD flowD1 = new FormApprovalFlowD();
                                                    flowD1.setIUserId(u.getId());
                                                    flowD1.setIFormApprovalFlowMid(flowMId);
                                                    flowD1.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                                    flowD1.setISeq(1);
                                                    flowDList.add(flowD1);
                                                });
                                            }
                                        });
                                        formapprovaldRoleService.batchSave(formapprovaldRoleList);
                                    } else {
                                        ValidationUtils.error("该审批顺序为" + formApprovalD.getISeq() + "配置的角色未指定角色");
                                    }
                                    break;
                                default:
                                    break;
                            }
                        });

                        // 保存审批流程的审批人
                        flowDService.batchSave(flowDList, flowDList.size());

                        // 流程主表数据
                        List<FormApprovalFlowM> flowMList = flowMService.find("select * from Bd_FormApprovalFlowM where iApprovalId = ? ", approvalIautoId);
                        Map<Long, FormApprovalFlowM> flowMMap = flowMList.stream().collect(Collectors.toMap(FormApprovalFlowM::getIApprovalDid, Function.identity(), (key1, key2) -> key2));

                        // 判断下一节点
                        nextNode(formApproval, approvalIautoId, flowMMap, now);

                    } else {
                        ValidationUtils.error("提交失败，该审批流未配置具体审批节点");
                    }
                    break;
                // 审核
                case STATUS:
                    ValidationUtils.isTrue(updateSubmit(formSn, formAutoId, AuditWayEnum.STATUS.getValue(), now), "当前状态不允许提审");
                    break;
                default:
                    break;
            }
            return true;
        });
        return SUCCESS;
    }

    /**
     * 获取角色人员信息
     */
    public List<User> getRoles(Long role) {
        return userService.find("select * from jb_user where (',' + roles + ',') like '%," + role + ",%' and enable = 1");
    }

    /**
     * 复制Obj
     */
    public FormApproval copySetObj(ApprovalM approvalM, Long iFormId, Long formAutoId, User user) {
        FormApproval formApproval = new FormApproval();
        formApproval.setIOrgId(approvalM.getIOrgId());
        formApproval.setCOrgCode(approvalM.getCOrgCode());
        formApproval.setCOrgName(approvalM.getCOrgName());
        formApproval.setIApprovalId(approvalM.getIAutoId());
        formApproval.setIFormId(iFormId);
        formApproval.setIFormObjectId(formAutoId);
        formApproval.setIsSkippedOnDuplicate(approvalM.getIsSkippedOnDuplicate());
        formApproval.setIsApprovedOnSame(approvalM.getIsApprovedOnSame());
        formApproval.setICreateBy(user.getId());
        formApproval.setCCreateName(user.getName());
        formApproval.setDCreateTime(new Date());
        return formApproval;
    }

    /**
     * 状态审批
     */
    public void approveByStatus(String formSn, Long formAutoId, ICallbackFunc preApprove, ICallbackFunc postApprove) {
        Record formData = Db.use(dataSourceConfigName()).findFirst("SELECT * FROM " + formSn + " WHERE iautoid = ? ", formAutoId);
        ValidationUtils.notNull(formData, "单据不存在");
        ValidationUtils.isTrue(!formData.getBoolean(IS_DELETED), "单据已被删除");
        ValidationUtils.equals(formData.getInt(IAUDITWAY), AuditWayEnum.STATUS.getValue(), "审批流审批的单据，不允许操作“审批通过”按钮");
        ValidationUtils.equals(formData.getInt(IAUDITSTATUS), AuditStatusEnum.AWAIT_AUDIT.getValue(), "非待审核状态");

        String msg = preApprove.execute();
        ValidationUtils.assertBlank(msg, msg);

        // 更新审核通过
        ValidationUtils.isTrue(updateAudit(formSn, formAutoId, AuditStatusEnum.APPROVED.getValue(), AuditStatusEnum.AWAIT_AUDIT.getValue(), new Date()), "更新审批状态失败");

        msg = postApprove.execute();
        ValidationUtils.assertBlank(msg, msg);
    }

    /**
     * 状态审批
     */
    public void rejectByStatus(String formSn, Long formAutoId, ICallbackFunc preReject, ICallbackFunc postReject) {
        Record formData = Db.use(dataSourceConfigName()).findFirst("SELECT * FROM " + formSn + " WHERE iautoid = ? ", formAutoId);
        ValidationUtils.notNull(formData, "单据不存在");
        ValidationUtils.isTrue(!formData.getBoolean(IS_DELETED), "单据已被删除");
        ValidationUtils.equals(formData.getInt(IAUDITWAY), AuditWayEnum.STATUS.getValue(), "审批流审批的单据，不能操作“审批不通过”");
        ValidationUtils.equals(formData.getInt(IAUDITSTATUS), AuditStatusEnum.AWAIT_AUDIT.getValue(), "非待审核状态");

        String msg = preReject.execute();
        ValidationUtils.assertBlank(msg, msg);

        // 更新审核通过
        ValidationUtils.isTrue(updateAudit(formSn, formAutoId, AuditStatusEnum.REJECTED.getValue(), AuditStatusEnum.AWAIT_AUDIT.getValue(), new Date()), "更新审批状态失败");

        msg = postReject.execute();
        ValidationUtils.assertBlank(msg, msg);
    }

    /**
     * 审批
     */
    public Ret approve(Long formAutoId, String formSn, int status) {
        // 查出单据对应的审批流配置
        FormApproval formApproval = findByFormAutoId(formAutoId);
        ValidationUtils.notNull(formApproval, "单据未提交审批！");

        Date now = new Date();

        Long mid = formApproval.getIAutoId();

        // 提审人
        Long iCreateBy = formApproval.getICreateBy();
        // 去重
        Boolean isSkippedOnDuplicate = formApproval.getIsSkippedOnDuplicate();
        // 审批人与发起人同一人 自动通过
        Boolean isApprovedOnSame = formApproval.getIsApprovedOnSame();

        // 流程主表数据
        List<FormApprovalFlowM> flowMList = flowMService.find("select * from Bd_FormApprovalFlowM where iApprovalId = " + mid);
        Map<Long, FormApprovalFlowM> flowMMap = flowMList.stream().collect(Collectors.toMap(FormApprovalFlowM::getIApprovalDid, Function.identity(), (key1, key2) -> key2));

        // 当前登录人
        User user = JBoltUserKit.getUser();
        Long userId = user.getId();

        /*
         * 1、按顺序查出审批节点
         * 2、过滤出不通过的节点 若有直接跳出
         * 3、过滤出未通过的节点 执行审批
         */

        // 1、
        List<FormApprovalD> formApprovalDList = formApprovalDService.findListByMid(mid);
        // 2、
        List<FormApprovalD> failNode = formApprovalDList.stream().filter(f -> Objects.equals(f.getIStatus(), 3)).collect(Collectors.toList());
        ValidationUtils.assertEmpty(failNode, "该单据已审批不通过，审批流程已结束！");
        // 3、
        List<FormApprovalD> processNode = formApprovalDList.stream().filter(f -> Objects.equals(f.getIStatus(), 1)).collect(Collectors.toList());
        int processNodeSize = processNode.size();
        // 审批
        if (processNodeSize > 0) {
            FormApprovalD formApprovalD = processNode.get(0);
            Long Did = formApprovalD.getIAutoId();
            Integer iApprovalWay = formApprovalD.getIApprovalWay(); //多人审批的方式

            /*
             * 单据流程主表
             * 找出流程及所有该节点的审批人
             */
            FormApprovalFlowM flowM = flowMMap.get(Did);
            Long fMid = flowM.getIAutoId();

            // 找出该节点的审批人
            List<FormApprovalFlowD> flowDList = flowDService.find("select * from Bd_FormApprovalFlowD where iFormApprovalFlowMid = ? order by iSeq asc", fMid);

            // 过滤出未审核的人员
            List<FormApprovalFlowD> collect = flowDList.stream().filter(f -> Objects.equals(f.getIAuditStatus(), 1)).collect(Collectors.toList());

            /*
             * 单据节点集合
             * 单据流程行表集合
             * 节点是否更改状态
             */
            List<FormApprovalD> nodeList = new ArrayList<>();
            List<FormApprovalFlowD> approvalFlowDList = new ArrayList<>();
            boolean nodeIsOk = false;

            /*
             * 若有审批人
             * 判断审批方式 1、依次审批 2、会签（所有人同意  3、或签（一人即可
             *
             * 若没有未审核的人
             * 说明还没到当前审批人
             */
            int collectSize = collect.size();
            if (collectSize > 0) {
                switch (iApprovalWay) {
                    // 依次审批
                    case 1:
                        FormApprovalFlowD flowD = collect.get(0);
                        if (Objects.equals(flowD.getIUserId(), userId)) {
                            flowD.setIAuditStatus(status);
                            approvalFlowDList.add(flowD);

                            // 该节点状态
                            nodeIsOk = collectSize <= 1;

                        } else {
                            ValidationUtils.error("上一审批人还未审批");
                        }
                        break;
                    // 会签
                    case 2:
                        for (FormApprovalFlowD formApprovalFlowD : collect) {
                            if (Objects.equals(formApprovalFlowD.getIUserId(), userId)) {
                                formApprovalFlowD.setIAuditStatus(status);
                                approvalFlowDList.add(formApprovalFlowD);

                                nodeIsOk = collectSize <= 1;
                            }
                        }
                        if (approvalFlowDList.size() <= 0) {
                            ValidationUtils.error("上一审批人还未审批");
                        }
                        break;
                    // 或签
                    case 3:
                        for (FormApprovalFlowD formApprovalFlowD : collect) {
                            if (Objects.equals(formApprovalFlowD.getIUserId(), userId)) {
                                formApprovalFlowD.setIAuditStatus(status);
                                approvalFlowDList.add(formApprovalFlowD);

                                nodeIsOk = true;
                            }
                        }
                        if (approvalFlowDList.size() <= 0) {
                            ValidationUtils.error("上一审批人还未审批");
                        }
                        break;
                    default:
                        break;
                }

            } else {
                ValidationUtils.error("上一审批人还未审批");
            }

            // 节点 更新审批状态
            if (status == 3 || nodeIsOk) {
                formApprovalD.setIStatus(status);
                formApprovalD.setDAuditTime(now);
                nodeList.add(formApprovalD);
            }

            boolean finalNodeIsOk = nodeIsOk;

            tx(() -> {
                if (nodeList.size() > 0) {
                    formApprovalDService.batchUpdate(nodeList);
                }
                if (approvalFlowDList.size() > 0) {
                    flowDService.batchUpdate(approvalFlowDList);
                }

                // 审批不通过 更新单据状态 时间
                if (status == 3) {
                    ValidationUtils.isTrue(updateAudit(formSn, formAutoId, AuditStatusEnum.REJECTED.getValue(), AuditStatusEnum.NOT_AUDIT.getValue(), now), "更新审核状态失败");

                    // 更新单据审批流主表
                    formApproval.setIsDeleted(true);
                    formApproval.update();

                } else {

                    /*
                     * 判断下一步情况 即当前一步的审批过程已结束
                     * 1、该节点已通过 判断下一节点
                     * 2、当前节点还未通过 判断下一审批人
                     */

                    // 1、
                    if (finalNodeIsOk) {
                        // 判断下一节点
                        nextNode(formApproval, mid, flowMMap, now);
                        // 2、
                    } else {
                        // 判断下一审批人
                        if (collectSize > 1) {

                            // 已审核通过的审批人
                            List<FormApprovalFlowD> successFlowDList = flowDService.find("select * " +
                                    "from Bd_FormApprovalFlowD t1 " +
                                    "where iFormApprovalFlowMid in (select iAutoId " +
                                    "                               from Bd_FormApprovalFlowM " +
                                    "                               where iApprovalId = " + mid + " ) " +
                                    "  and t1.iAuditStatus = 2 order by iSeq asc ");

                            List<Long> successUser = successFlowDList.stream().map(FormApprovalFlowD::getIUserId).collect(Collectors.toList());

                            List<FormApprovalFlowD> forApprovalFlowDList = new ArrayList<>();
                            boolean killNode = false;
                            for (int k = 1; k < collectSize; k++) {
                                FormApprovalFlowD flowD = collect.get(k);
                                Long iUserId = flowD.getIUserId();
                                if (iUserId == null) {
//                                    好像无需 判断为空是否跳过 页面限制了 与 梳理流程时已经判断
                                    flowD.setIAuditStatus(status);
                                    forApprovalFlowDList.add(flowD);
                                } else {
                                    if (isSkippedOnDuplicate) {
                                        if (successUser.contains(iUserId)) {
                                            flowD.setIAuditStatus(status);
                                            forApprovalFlowDList.add(flowD);
                                            continue;
                                        } else {
                                            killNode = true;
                                        }
                                    }
                                    if (isApprovedOnSame) {
                                        if (Objects.equals(iCreateBy, iUserId)) {
                                            flowD.setIAuditStatus(status);
                                            forApprovalFlowDList.add(flowD);
                                            continue;
                                        } else {
                                            killNode = true;
                                        }
                                    }
                                    if (killNode) {
                                        break;
                                    }
                                }
                            }

                            if (forApprovalFlowDList.size() > 0) {
                                flowDService.batchUpdate(forApprovalFlowDList);

                                // 再次找出该节点的未审批人
                                List<FormApprovalFlowD> processFlowD = flowDService.find("select * from Bd_FormApprovalFlowD where iFormApprovalFlowMid =  ? and iAuditStatus = 1 order by iSeq asc", fMid);

                                if (processFlowD.size() <= 0) {
                                    // 更新当前节点
                                    formApprovalD.setIStatus(status);
                                    formApprovalD.setDAuditTime(now);
                                    formApprovalD.update();
                                    // 判断下一节点
                                    nextNode(formApproval, mid, flowMMap, now);
                                }
                            }
                        }
                    }
                }

                return true;
            });

        } else {
            ValidationUtils.error("审批流程已结束");
        }

        return SUCCESS;
    }

    /**
     * 下个节点判断
     */
    public void nextNode(FormApproval formApproval, Long aid, Map<Long, FormApprovalFlowM> flowmMap, Date now) {

        /*
         * 找出未审批通过的节点
         */
        List<FormApprovalD> processNode = formApprovalDService.findListByAid(aid);
        int size = processNode.size();

        if (size > 0) {

            // 提审人
            Long iCreateBy = formApproval.getICreateBy();
            // 去重
            Boolean isSkippedOnDuplicate = formApproval.getIsSkippedOnDuplicate();
            // 审批人与发起人同一人 自动通过
            Boolean isApprovedOnSame = formApproval.getIsApprovedOnSame();

            FormApprovalD formApprovalD = processNode.get(0);
            Long Mid = formApprovalD.getIFormApprovalId();
            Long Did = formApprovalD.getIAutoId();
            // 多人审批的方式
            Integer iApprovalWay = formApprovalD.getIApprovalWay();

            /*
             * 单据流程主表
             * 找出流程及所有该节点的审批人
             */
            FormApprovalFlowM flowM = flowmMap.get(Did);
            Long FMid = flowM.getIAutoId();

            // 找出该节点的审批人
            List<FormApprovalFlowD> flowDList = flowDService.find("select * from Bd_FormApprovalFlowD where " + "iFormApprovalFlowMid = " + FMid + " order by iSeq asc");

            // 过滤出未审核的人员
            List<FormApprovalFlowD> collect = flowDList.stream().filter(f -> Objects.equals(f.getIAuditStatus(), 1)).collect(Collectors.toList());

            int collectSize = collect.size();
            if (collectSize > 0) {

                // 已审核通过的审批人
                List<FormApprovalFlowD> successFlowDList = flowDService.find("select * " +
                        "from Bd_FormApprovalFlowD t1 " +
                        "where iFormApprovalFlowMid in (select iAutoId " +
                        "                               from Bd_FormApprovalFlowM " +
                        "                               where iApprovalId = " + Mid + " ) " +
                        "  and t1.iAuditStatus = 2 order by iSeq asc ");

                List<Long> successUser = successFlowDList.stream().map(FormApprovalFlowD::getIUserId).collect(Collectors.toList());

                List<FormApprovalFlowD> forApprovalFlowDList = new ArrayList<>();
                boolean killNode = false;
                for (FormApprovalFlowD flowD : collect) {
                    Long iUserId = flowD.getIUserId();
                    if (iUserId == null) {
                        // 好像无需 判断为空是否跳过 页面限制了 与 梳理流程时已经判断
                        flowD.setIAuditStatus(2);
                        forApprovalFlowDList.add(flowD);
                    } else {
                        if (isSkippedOnDuplicate) {
                            if (successUser.contains(iUserId)) {
                                flowD.setIAuditStatus(2);
                                forApprovalFlowDList.add(flowD);
                                continue;
                            } else {
                                killNode = true;
                            }
                        }
                        if (isApprovedOnSame) {
                            if (Objects.equals(iCreateBy, iUserId)) {
                                flowD.setIAuditStatus(2);
                                forApprovalFlowDList.add(flowD);
                                continue;
                            } else {
                                killNode = true;
                            }
                        }
                        if (killNode) {
                            break;
                        }
                    }
                }

                if (forApprovalFlowDList.size() > 0) {
                    flowDService.batchUpdate(forApprovalFlowDList);

                    // 再次找出该节点的未审批人
                    List<FormApprovalFlowD> processFlowD = flowDService.find("select * from Bd_FormApprovalFlowD where iFormApprovalFlowMid = ? and iAuditStatus = 1 order by iSeq asc", FMid);

                    /*
                     * 1、没有未审批人 则该节点结束 更新已通过状态
                     * 2、有未审批人 但是小于该节点所有审批人且该节点为或签 则该节点结束 更新已通过状态
                     * 然后判断下个节点
                     * 否则 退出
                     */
                    if ((processFlowD.size() <= 0) || ((flowDList.size() > processFlowD.size()) && iApprovalWay == 3)) {
                        // 更新当前节点
                        formApprovalD.setIStatus(2);
                        formApprovalD.setDAuditTime(now);
                        formApprovalD.update();
                        // 判断下一节点
                        nextNode(formApproval, aid, flowmMap, now);
                    }
                }
            }
        } else {

            // 更新单据状态
            Long iFormId = formApproval.getIFormId();
            Long iFormObjectId = formApproval.getIFormObjectId();
            Form form = formService.findById(iFormId);
            String formSn = form.getCFormCode();

            if (isOk(form)) {
                ValidationUtils.isTrue(updateAudit(formSn, iFormObjectId, AuditStatusEnum.APPROVED.getValue(), AuditStatusEnum.AWAIT_AUDIT.getValue(), now), "更新审核状态失败");
            } else {
                ValidationUtils.error("找不到表单配置");
            }
        }
    }


    /**
     * 通过formAutoId 单据ID查出单据对应审批流配置
     */
    public FormApproval findByFormAutoId(Long formAutoId) {
        return findFirst("select * from Bd_FormApproval where iFormObjectId = ? AND isDeleted = '0' ", formAutoId);
    }

    /**
     * 更新提审状态
     */
    private boolean updateSubmit(String formSn, long formAutoId, int iauditWay, Date now) {
        Sql updateSql = updateSql().update(formSn)
                .set(IAUDITWAY, iauditWay)
                .set(IAUDITSTATUS, AuditStatusEnum.AWAIT_AUDIT.getValue())
                .set(DSUBMITTIME, now)
                .eq(IAUTOID, formAutoId)
                .in(IAUDITSTATUS, AuditStatusEnum.NOT_AUDIT.getValue(), AuditStatusEnum.REJECTED.getValue());

        return update(updateSql) > 0;
    }

    /**
     * 更新审批状态
     */
    public boolean updateAudit(String formSn, long formAutoId, int iAfterStatus, int iBeforeStatus, Date now) {
        Sql updateSql = updateSql().update(formSn)
                .set(IAUDITSTATUS, iAfterStatus)
                .set(DAUDITTIME, now)
                .eq(IAUTOID, formAutoId)
                .eq(IAUDITSTATUS, iBeforeStatus);

        return update(updateSql) > 0;
    }

    /**
     * 更新撤销审核
     */
    public boolean updateWithdraw(String formSn, long formAutoId) {
        Sql updateSql = updateSql().update(formSn)
                .set(IAUDITSTATUS, AuditStatusEnum.NOT_AUDIT.getValue())
                .eq(IAUTOID, formAutoId)
                .eq(IAUDITSTATUS, AuditStatusEnum.AWAIT_AUDIT.getValue());

        return update(updateSql) > 0;
    }

    /**
     * 撤销审核，支持逻辑校验处理
     *
     * @param formSn       表名
     * @param formAutoId   单据ID
     * @param preWithdraw  撤回前校验
     * @param postWithdraw 更新之后的其他逻辑处理
     */
    public void withdraw(String formSn, long formAutoId, ICallbackFunc preWithdraw, ICallbackFunc postWithdraw) {
        Record formData = Db.use(dataSourceConfigName()).findFirst("SELECT * FROM " + formSn + " WHERE iautoid = ? ", formAutoId);
        ValidationUtils.notNull(formData, "单据不存在");
        ValidationUtils.isTrue(!formData.getBoolean(IS_DELETED), "单据已被删除");
        ValidationUtils.equals(AuditStatusEnum.AWAIT_AUDIT.getValue(), formData.getInt(IAUDITSTATUS), "非待审核状态");

        switch (AuditWayEnum.toEnum(formData.getInt(IAUDITWAY))) {
            // 状态审批
            case STATUS:
                // 更新前校验
                String msg = preWithdraw.execute();
                ValidationUtils.assertBlank(msg, msg);

                // 执行状态更新
                ValidationUtils.isTrue(updateWithdraw(formSn, formAutoId), "更新撤回失败");

                // 更新后业务处理
                msg = postWithdraw.execute();
                ValidationUtils.assertBlank(msg, msg);
            //  审批流
            case FLOW:

                break;
            default:
                break;
        }
    }

}
