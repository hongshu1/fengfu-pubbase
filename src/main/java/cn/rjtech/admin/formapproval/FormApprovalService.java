package cn.rjtech.admin.formapproval;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.approvald.ApprovalDService;
import cn.rjtech.admin.approvaldrole.ApprovaldRoleService;
import cn.rjtech.admin.approvaldroleusers.ApprovaldRoleusersService;
import cn.rjtech.admin.approvalduser.ApprovaldUserService;
import cn.rjtech.admin.approvalm.ApprovalMService;
import cn.rjtech.admin.auditformconfig.AuditFormConfigService;
import cn.rjtech.admin.form.FormService;
import cn.rjtech.admin.formapprovald.FormApprovalDService;
import cn.rjtech.admin.formapprovaldroleusers.FormapprovaldRoleusersService;
import cn.rjtech.admin.formapprovalduser.FormapprovaldUserService;
import cn.rjtech.admin.formapprovalflowd.FormApprovalFlowDService;
import cn.rjtech.admin.formapprovalflowm.FormApprovalFlowMService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.base.exception.ParameterException;
import cn.rjtech.cache.UserCache;
import cn.rjtech.constants.Constants;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.AuditTypeEnum;
import cn.rjtech.enums.AuditWayEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.hutool.core.text.StrPool.COMMA;

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
    private FormService formService;
    @Inject
    private PersonService personService;
    @Inject
    private ApprovalMService approvalMService;
    @Inject
    private ApprovalDService approvalDService;
    @Inject
    private FormApprovalFlowDService flowDService;
    @Inject
    private FormApprovalFlowMService flowMService;
    @Inject
    private ApprovaldUserService approvaldUserService;
    @Inject
    private ApprovaldRoleService approvaldRoleService;
    @Inject
    private FormApprovalDService formApprovalDService;
    @Inject
    private ApprovaldRoleusersService roleusersService;
    @Inject
    private AuditFormConfigService auditFormConfigService;
    @Inject
    private FormapprovaldUserService formapprovaldUserService;
    @Inject
    private FormapprovaldRoleusersService formapprovaldRoleusersService;

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
     * 校验参数
     * @param formAutoId
     * @param formSn
     * @param primaryKeyName
     * @return
     */
    public Ret optional(String formAutoId, String formSn, String primaryKeyName){

        ValidationUtils.isTrue(isOk(formAutoId), "请先点击保存按钮，保存好单据信息，再作提审");
        User user = JBoltUserKit.getUser();
        // 获取单据信息
        Record formData = getApprovalForm(formSn, primaryKeyName, Long.parseLong(formAutoId));

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

        return SUCCESS;
    }

    /**
     * 提交表格
     * @param jBoltTable
     * @return
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        //当前操作人员  当前时间
        User user = JBoltUserKit.getUser();
        Date nowDate = new Date();
        System.out.println("saveTable===>" + jBoltTable.getSave());
        System.out.println("updateTable===>" + jBoltTable.getUpdate());
        System.out.println("deleteTable===>" + jBoltTable.getDelete());
        System.out.println("form===>" + jBoltTable.getForm());

        System.out.println("saveTable===>" + jBoltTable.getSaveRecordList());
        System.out.println("form===>" + jBoltTable.getFormRecord());

        ValidationUtils.isTrue(jBoltTable.saveIsNotBlank(), "请先添加审批节点");

        List<Record> saveRecordList = jBoltTable.getSaveRecordList();
        Record approvalM = jBoltTable.getFormRecord();

        String formId = approvalM.getStr("formAutoId");
        ValidationUtils.isTrue(isOk(formId), "请先点击保存按钮，保存好单据信息，再作提审");

        Long formAutoId = Long.parseLong(formId);

        String formSn = approvalM.getStr("formSn");
        Boolean isApprovedOnSame = approvalM.getBoolean("isApprovedOnSame");
        String permissionKey = approvalM.getStr("permissionKey");
        String className = approvalM.getStr("className");
        String primaryKeyName = approvalM.getStr("primaryKeyName");
        Boolean isSkippedOnDuplicate = approvalM.getBoolean("isSkippedOnDuplicate");

        Form form = formService.findByCformSn(formSn);

        tx(()->{

            FormApproval byFormAutoId = findByFormAutoId(formAutoId);
            ValidationUtils.isTrue((notOk(byFormAutoId)), "该单据已提交审批，请勿重复提交审批！");

            // -----------------------------------
            // 提审前的业务调用
            // -----------------------------------
            String msg = invokeMethod(className, "preSubmitFunc", formAutoId);
            ValidationUtils.assertBlank(msg, msg);

            // 提审更新
            ValidationUtils.isTrue(updateSubmit(formSn, formAutoId, AuditWayEnum.FLOW.getValue(), nowDate, primaryKeyName),
                    "单据当前状态不可提审");

            // 流程子表集合
            List<FormApprovalFlowD> flowDList = new ArrayList<>();

            List<FormapprovaldUser> userList = new ArrayList<>();

            //        保存单据审批流信息
            FormApproval formApproval = new FormApproval();
            formApproval.setIOrgId(getOrgId());
            formApproval.setCOrgCode(getOrgCode());
            formApproval.setCOrgName(getOrgName());
            formApproval.setIFormId(form.getIAutoId());
            formApproval.setIFormObjectId(formAutoId);
            formApproval.setIsSkippedOnDuplicate(isSkippedOnDuplicate);
            formApproval.setIsApprovedOnSame(isApprovedOnSame);
            formApproval.setICreateBy(user.getId());
            formApproval.setCCreateName(user.getName());
            formApproval.setDCreateTime(new Date());
            formApproval.save();

            Long formApprovalIAutoId = formApproval.getIAutoId();

            for (int i = 0, j = 1; i < saveRecordList.size(); i++,j++) {
                Record record = saveRecordList.get(i);
                FormApprovalD formApprovalD = new FormApprovalD();
                formApprovalD.setIFormApprovalId(formApprovalIAutoId);
                formApprovalD.setIStep(record.getInt("step"));
                formApprovalD.setISeq(j);
                formApprovalD.setCName(record.getStr("cname"));
                formApprovalD.setIType(1);
                formApprovalD.setIApprovalWay(record.getInt("dway"));
                formApprovalD.setIStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                formApprovalD.setDAuditTime(nowDate);
                formApprovalD.save();

                Long formApprovalDid = formApprovalD.getIAutoId();

                // 流程主表
                FormApprovalFlowM flowM = new FormApprovalFlowM();
                // 单据审批流ID
                flowM.setIApprovalId(formApprovalIAutoId);
                // 单据审批节点ID
                flowM.setIApprovalDid(formApprovalDid);
                // 节点顺序
                flowM.setISeq(formApprovalD.getISeq());
                // 多人审批方式
                flowM.setIApprovalWay(formApprovalD.getIApprovalWay());
                flowM.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                flowM.save();

                Long flowMId = flowM.getIAutoId();

                List<JSONObject> recordList = (List<JSONObject>) record.getObject("object");
                LOG.info("recordList={}",recordList);

                for (int k = 0; k < recordList.size(); k++) {
                    JSONObject userRecord = recordList.get(k);

//                    审批用户
                    FormapprovaldUser formapprovaldUser = new FormapprovaldUser();
                    formapprovaldUser.setIFormApprovalId(formApprovalDid);
                    formapprovaldUser.setISeq(k);
                    formapprovaldUser.setIUserId(userRecord.getLong("iuserid"));
                    formapprovaldUser.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                    formapprovaldUser.setDAuditTime(nowDate);
                    formapprovaldUser.setIPersonId(userRecord.getLong("ipersonid"));
                    userList.add(formapprovaldUser);

//                流程子表
                FormApprovalFlowD flowD = new FormApprovalFlowD();
                flowD.setIFormApprovalFlowMid(flowMId);
                flowD.setISeq(k);
                flowD.setIUserId(userRecord.getLong("iuserid"));
                flowD.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                flowDList.add(flowD);
                }
            }

            // 保存审批流程的审批人
            flowDService.batchSave(flowDList, flowDList.size());
            formapprovaldUserService.batchSave(userList,userList.size());

            // 流程主表数据
            List<FormApprovalFlowM> flowMList = flowMService.find("select * from Bd_FormApprovalFlowM where " +
                    "iApprovalId = ? ", formApprovalIAutoId);
            Map<Long, FormApprovalFlowM> flowMMap = flowMList.stream().collect(Collectors.toMap(FormApprovalFlowM::getIApprovalDid, Function.identity(), (key1, key2) -> key2));

            // 判断下一节点
            nextNode(formApproval, formApprovalIAutoId, flowMMap, nowDate, primaryKeyName, className, formAutoId, false,
                    user.getId());


            // -----------------------------------
            // 提审后的业务调用
            // -----------------------------------

            String postMsg = invokeMethod(className, "postSubmitFunc", formAutoId);
            ValidationUtils.assertBlank(postMsg, postMsg);


            return true;
        });
        return SUCCESS;
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
    public Ret submit(String formSn, Long formAutoId, String primaryKeyName, String className) {
        // 提审人
        User user = JBoltUserKit.getUser();
        Person person = findPersonByUserId(user.getId());

        // 获取单据信息
        Record formData = getApprovalForm(formSn, primaryKeyName, formAutoId);

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
                    FormApproval byFormAutoId = findByFormAutoId(formAutoId);
                    ValidationUtils.isTrue((notOk(byFormAutoId)), "该单据已提交审批，请勿重复提交审批！");

                    // -----------------------------------
                    // 提审前的业务调用
                    // -----------------------------------
                    String msg = invokeMethod(className, "preSubmitFunc", formAutoId);
                    ValidationUtils.assertBlank(msg, msg);

                    // 提审更新
                    ValidationUtils.isTrue(updateSubmit(formSn, formAutoId, AuditWayEnum.FLOW.getValue(), now, primaryKeyName), "单据当前状态不可提审");

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
                            int getSuperior;

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

                                    /**
                                     * 一级主管（直属主管）：班负责人，班长
                                     * 二级主管：系负责人，系长
                                     * 三级主管：科负责人，科长
                                     * 四级主管：部负责人，部长
                                     * 五级主管：公司负责人，总经理
                                     */

                                    getStair = iSupervisorType - 1;
                                    getSuperior = iSupervisorType;

                                    /*switch (iSupervisorType) {
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
                                    }*/

                                    if (size > getStair) {
                                        Record record = list.get(getStair);
                                        String cdepperson = record.getStr("cdepperson");
                                        if (cdepperson != null) {
//                                    改成人员编码匹配
                                            List<String> stringList = StrSplitter.split(cdepperson, COMMA, true, true);
                                            for (int i = 0; i < stringList.size(); i++) {
                                                String code = stringList.get(i);
                                                // 通过人员档案ID 改成人员编码匹配 找到 用户表ID
                                                Kv kv = new Kv();
                                                kv.set("code", code);
                                                kv.setIfNotNull("orgId", getOrgId());
                                                Record personUser = dbTemplate("formapprovald.findPersonUser", kv).findFirst();
                                                Long iuserid = personUser.getLong("iuserid");
                                                String cpsnname = personUser.getStr("cpsnname");
                                                if (iuserid != null) {
                                                    FormApprovalFlowD flowD = new FormApprovalFlowD();
                                                    flowD.setIFormApprovalFlowMid(flowMId);
                                                    flowD.setISeq(i);
                                                    flowD.setIUserId(iuserid);
                                                    flowD.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                                    flowDList.add(flowD);
                                                } else {
                                                    ValidationUtils.error(cpsnname + "该人员还未匹配所属用户名");
                                                }
                                            }
                                        } else {
                                            // 为空 检查是否开启上级代审
                                            if (isDirectOnMissing && size > getSuperior) {  // 开启 且 存在上级
                                                Record record1 = list.get(getSuperior);
                                                String cdepperson1 = record1.getStr("cdepperson");
                                                if (cdepperson1 != null) {  // 且上级负责人不为空
                                                    List<String> stringList = StrSplitter.split(cdepperson1, COMMA, true, true);
                                                    for (int i = 0; i < stringList.size(); i++) {
                                                        // 通过人员档案ID 改成人员编码匹配 找到 用户表ID
                                                        String code = stringList.get(i);
                                                        Kv kv = new Kv();
                                                        kv.set("code", code);
                                                        kv.setIfNotNull("orgId", getOrgId());
                                                        Record personUser = dbTemplate("formapprovald.findPersonUser", kv).findFirst();
                                                        Long iuserid = personUser.getLong("iuserid");
                                                        String cpsnname = personUser.getStr("cpsnname");
                                                        if (iuserid != null) {
                                                            FormApprovalFlowD flowD = new FormApprovalFlowD();
                                                            flowD.setIFormApprovalFlowMid(flowMId);
                                                            flowD.setISeq(i);
                                                            flowD.setIUserId(iuserid);
                                                            flowD.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                                            flowDList.add(flowD);
                                                        } else {
                                                            ValidationUtils.error(cpsnname + "该人员还未匹配所属用户名");
                                                        }
                                                    }
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
                                        String cdepperson = record.getStr("cdepperson");
                                        if (cdepperson != null) {
//                                    改成人员编码匹配
                                            List<String> stringList = StrSplitter.split(cdepperson, COMMA, true, true);
                                            for (int i = 0; i < stringList.size(); i++) {
                                                String code = stringList.get(i);
                                                // 通过人员档案ID 改成人员编码匹配 找到 用户表ID
                                                Kv kv = new Kv();
                                                kv.set("code", code);
                                                kv.setIfNotNull("orgId", getOrgId());
                                                Record personUser = dbTemplate("formapprovald.findPersonUser", kv).findFirst();
                                                Long iuserid = personUser.getLong("iuserid");
                                                String cpsnname = personUser.getStr("cpsnname");
                                                if (iuserid != null) {
                                                    FormApprovalFlowD flowD = new FormApprovalFlowD();
                                                    flowD.setIFormApprovalFlowMid(flowMId);
                                                    flowD.setISeq(i);
                                                    flowD.setIUserId(iuserid);
                                                    flowD.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                                    flowDList.add(flowD);
                                                } else {
                                                    ValidationUtils.error(cpsnname + "该人员还未匹配所属用户名");
                                                }
                                            }
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
                                            if (iSkipOn == 1) {
                                                FormApprovalFlowD flowD = new FormApprovalFlowD();
                                                flowD.setIFormApprovalFlowMid(flowMId);
                                                flowD.setISeq(1);
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
                                        rolesByDid.forEach(approvaldRole -> {
                                            FormapprovaldRole formapprovaldRole = new FormapprovaldRole();
                                            formapprovaldRole.setIFormApprovalId(formApprovalDid);
                                            formapprovaldRole.setISeq(approvaldRole.getISeq());
                                            formapprovaldRole.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                            formapprovaldRole.setIRoleId(approvaldRole.getIRoleId());
                                            formapprovaldRole.save();

                                            List<ApprovaldRoleusers> users = roleusersService.getRoleUser(approvaldRole.getIAutoId());

                                            if (users.size() > 0) {
                                                List<FormapprovaldRoleusers> roleusersList = new ArrayList<>();
                                                users.forEach(u -> {
                                                    FormApprovalFlowD flowD1 = new FormApprovalFlowD();
                                                    flowD1.setIUserId(u.getIUserId());
                                                    flowD1.setIFormApprovalFlowMid(flowMId);
                                                    flowD1.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                                    flowD1.setISeq(u.getISeq());
                                                    flowDList.add(flowD1);

//                                                    单据的角色人员
                                                    FormapprovaldRoleusers formapprovaldRoleusers =
                                                            new FormapprovaldRoleusers();
                                                    formapprovaldRoleusers.setIFormApprovaldRoleId(formapprovaldRole.getIAutoId());
                                                    formapprovaldRoleusers.setISeq(u.getISeq());
                                                    formapprovaldRoleusers.setIUserId(u.getIUserId());
                                                    roleusersList.add(formapprovaldRoleusers);
                                                });
                                                formapprovaldRoleusersService.batchSave(roleusersList, roleusersList.size());
                                            }
                                        });
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
                        nextNode(formApproval, approvalIautoId, flowMMap, now, primaryKeyName, className, formAutoId, false, user.getId());

                    } else {
                        ValidationUtils.error("提交失败，该审批流未配置具体审批节点");
                    }
                    break;
                // 审核
                case STATUS:
                    // -----------------------------------
                    // 提审前的业务调用
                    // -----------------------------------
                    msg = invokeMethod(className, "preSubmitFunc", formAutoId);
                    ValidationUtils.assertBlank(msg, msg);

                    ValidationUtils.isTrue(updateSubmit(formSn, formAutoId, AuditWayEnum.STATUS.getValue(), now, primaryKeyName), "当前状态不允许提审");
                    break;
                default:
                    break;
            }

            // -----------------------------------
            // 提审后的业务调用
            // -----------------------------------

            String msg = invokeMethod(className, "postSubmitFunc", formAutoId);
            ValidationUtils.assertBlank(msg, msg);

            return true;
        });
        return SUCCESS;
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
     * 审批通过
     *
     * @param formAutoId     单据ID
     * @param formSn         表单
     * @param status         状态
     * @param primaryKeyName 主键名
     * @param isWithinBatch  是否批量审批处理
     * @param remark         审批意见
     */
    public Ret approve(Long formAutoId, String formSn, int status, String primaryKeyName, String className, boolean isWithinBatch,String remark) {
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
        List<FormApprovalFlowM> flowMList = flowMService.findByIapprovalId(mid);
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

        // 审批
        if (CollUtil.isNotEmpty(processNode)) {
            FormApprovalD formApprovalD = processNode.get(0);

            Long Did = formApprovalD.getIAutoId();

            // 多人审批的方式
            Integer iApprovalWay = formApprovalD.getIApprovalWay();

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
                            ValidationUtils.error("您不是当前审批节点的审批人");
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
                            ValidationUtils.error("您不是当前审批节点的审批人");
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
                            ValidationUtils.error("您不是当前审批节点的审批人");
                        }
                        break;
                    default:
                        break;
                }

            } else {
                ValidationUtils.error("您不是当前审批节点的审批人");
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
                    if (remark != null) {
                        FormApprovalFlowD flowD = approvalFlowDList.get(0);
                        String cMemo = flowD.getCMemo();
                        String memo = remark;
                        if (cMemo!=null){
                            memo = remark + ";" + cMemo;
                        }
                        flowD.setCMemo(memo);
                    }
                    flowDService.batchUpdate(approvalFlowDList);
                }

                // 审批不通过 更新单据状态 时间
                if (status == 3) {
                    ValidationUtils.isTrue(updateAudit(formSn, primaryKeyName, formAutoId, AuditStatusEnum.REJECTED.getValue(), AuditStatusEnum.AWAIT_AUDIT.getValue(), userId, now), "更新审核状态失败");

                    // 更新单据审批流主表
                    formApproval.setIsDeleted(true);
                    formApproval.update();

                    // 审批不通过的，单据业务处理
                    String msg = invokeMethod(className, "postRejectFunc", formAutoId);
                    ValidationUtils.assertBlank(msg, msg);
                } else {

                    /*
                     * 判断下一步情况 即当前一步的审批过程已结束
                     * 1、该节点已通过 判断下一节点
                     * 2、当前节点还未通过 判断下一审批人
                     */

                    // 1、
                    if (finalNodeIsOk) {
                        // 判断下一节点
                        nextNode(formApproval, mid, flowMMap, now, primaryKeyName, className, formAutoId, isWithinBatch, userId);
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
                                if (CollUtil.isEmpty(processFlowD)) {
                                    // 更新当前节点
                                    formApprovalD.setIStatus(status);
                                    formApprovalD.setDAuditTime(now);
                                    formApprovalD.update();

                                    // 判断下一节点
                                    nextNode(formApproval, mid, flowMMap, now, primaryKeyName, className, formAutoId, isWithinBatch, userId);
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
     * 审批不通过
     */
    public Ret reject(Long formAutoId, String formSn, int status, String primaryKeyName, String className,
                      String remark, boolean isWithinBatch) {
        // 查出单据对应的审批流配置
        FormApproval formApproval = findByFormAutoId(formAutoId);
        ValidationUtils.notNull(formApproval, "单据未提交审批！");

        Date now = new Date();

        Long mid = formApproval.getIAutoId();

        // 流程主表数据
        List<FormApprovalFlowM> flowMList = flowMService.findByIapprovalId(mid);
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

        // 审批
        if (CollUtil.isNotEmpty(processNode)) {
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
                            ValidationUtils.error("您不是当前审批节点的审批人");
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
                            ValidationUtils.error("您不是当前审批节点的审批人");
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
                            ValidationUtils.error("您不是当前审批节点的审批人");
                        }
                        break;
                    default:
                        break;
                }

            } else {
                ValidationUtils.error("您不是当前审批节点的审批人");
            }

            // 节点 更新审批状态
            if (status == 3 || nodeIsOk) {
                formApprovalD.setIStatus(status);
                formApprovalD.setDAuditTime(now);
                nodeList.add(formApprovalD);
            }

            tx(() -> {
                if (nodeList.size() > 0) {
                    formApprovalDService.batchUpdate(nodeList);
                }
                if (approvalFlowDList.size() > 0) {
                    if (remark != null) {
                        FormApprovalFlowD flowD = approvalFlowDList.get(0);
                        String cMemo = flowD.getCMemo();
                        String memo = remark;
                        if (cMemo!=null){
                            memo = remark + ";" + cMemo;
                        }
                        flowD.setCMemo(memo);
                    }
                    flowDService.batchUpdate(approvalFlowDList);
                }

                // 审批不通过 更新单据状态 时间
                if (status == 3) {
                    ValidationUtils.isTrue(updateAudit(formSn, primaryKeyName, formAutoId, AuditStatusEnum.REJECTED.getValue(), AuditStatusEnum.AWAIT_AUDIT.getValue(), userId, now), "更新审核状态失败");

                    // 更新单据审批流主表
                    formApproval.setIsDeleted(true);
                    ValidationUtils.isTrue(formApproval.update(), ErrorMsg.UPDATE_FAILED);

                    // 处理审批不通过的额外业务
                    String msg = invokeMethod(className, "postRejectFunc", formAutoId, isWithinBatch);
                    ValidationUtils.assertBlank(msg, msg);
                }

                return true;
            });

        } else {
            ValidationUtils.error("审批流程已结束");
        }

        return SUCCESS;
    }

    /**
     * 反审批
     *
     * @param formAutoId     单据ID
     * @param formSn         表单
     * @param primaryKeyName 主键名
     * @param status         状态
     * @param className      处理审批Service的类名
     */
    public Ret reverseApprove(Long formAutoId, String formSn, String primaryKeyName, int status, String className, String remark) {
        // 查出单据对应的审批流配置
        FormApproval formApproval = findByFormAutoId(formAutoId);
        ValidationUtils.notNull(formApproval, "单据未提交审批！");

        // 单据反审时，预留额外前置业务处理
        String msg = invokeMethod(className, "preReverseApproveFunc", formAutoId, false, false);
        ValidationUtils.assertBlank(msg, msg);

        Record formRecord = findFirstRecord("select iAuditStatus as status from " + formSn + " where " + primaryKeyName + " = " + formAutoId);

//        反审前的单据状态
        String perStatus = formRecord.getStr("status");

        Long mid = formApproval.getIAutoId();

        // 当前登录人
        User user = JBoltUserKit.getUser();
        Long userId = user.getId();

        // 按顺序查出审批节点
        List<FormApprovalD> formApprovalDList = formApprovalDService.findListByMid(mid);
        if (CollUtil.isNotEmpty(formApprovalDList)) {

            /*
             * 按照倒序找出 最新的一个流程
             */
/*            Record record = findFirstRecord("select d.iAutoId  AS flowId, " +
                    "       fd.iAutoId       AS approvalDid, " +
                    "       d.iUserId        AS userId, " +
                    "       d.iAuditStatus   AS flowStatus, " +
                    "       fd.iType         AS type, " +
                    "       fd.iApprovalWay  AS way, " +
                    "       fd.iSeq          AS seq, " +
                    "       fd.iStatus       AS approvalStatus " +
                    "from Bd_FormApprovalFlowD d " +
                    "         left join Bd_FormApprovalFlowM m on d.iFormApprovalFlowMid = m.iAutoId " +
                    "         left join Bd_FormApprovalD fd on m.iApprovalDid = fd.iAutoId " +
                    "where fd.iFormApprovalId = ? " +
                    "  and (d.iAuditStatus = 2 or d.iAuditStatus = 3) " +
                    "order by fd.iSeq desc, d.iSeq desc", mid);*/

            List<Record> list = findRecord("select d.iAutoId  AS flowId, " +
                    "       fd.iAutoId       AS approvalDid, " +
                    "       d.iUserId        AS userId, " +
                    "       d.iAuditStatus   AS flowStatus, " +
                    "       fd.iType         AS type, " +
                    "       fd.iApprovalWay  AS way, " +
                    "       fd.iSeq          AS seq, " +
                    "       fd.iStatus       AS approvalStatus " +
                    "from Bd_FormApprovalFlowD d " +
                    "         left join Bd_FormApprovalFlowM m on d.iFormApprovalFlowMid = m.iAutoId " +
                    "         left join Bd_FormApprovalD fd on m.iApprovalDid = fd.iAutoId " +
                    "where fd.iFormApprovalId = " + mid + " " +
                    "  and (d.iAuditStatus = 2 or d.iAuditStatus = 3) " +
                    "order by fd.iSeq desc, d.iSeq desc");

            int size = list.size();
            if (size <= 0) {
//            if (null == record || CollUtil.isEmpty(record.getColumns())) {
                ValidationUtils.error("你不是当前审批人");
            } else {

                List<Long> flowDid = new ArrayList<>();
                Set<Long> approvalDid = new HashSet<>();
                Long passUserId = null;

                for (Record record : list) {
                    Long userid = record.getLong("userid");
                    Long approvaldid = record.getLong("approvaldid");
                    Long flowid = record.getLong("flowid");
                    flowDid.add(flowid);
                    approvalDid.add(approvaldid);

                    if (isOk(userid)) {
                        passUserId = userid;
                        break;
                    }
                }

                // 判断是否当前审批人
                ValidationUtils.isTrue(Objects.equals(passUserId, userId), "你不是当前审批人");

                String flowIdStr = flowDid.stream().map(String::valueOf).collect(Collectors.joining(","));
                String approvaldIdStr = approvalDid.stream().map(String::valueOf).collect(Collectors.joining(","));

                Kv kv = new Kv();
                kv.set("flowIdStr", flowIdStr);
                kv.set("approvaldIdStr", approvaldIdStr);
                List<FormApprovalD> approvalDList = formApprovalDService.findListBySid(kv);
                List<FormApprovalFlowD> flowDList = flowDService.findListBySid(kv);

                approvalDList.forEach(formApprovalD -> {
                    formApprovalD.setIStatus(status);
                });

                flowDList.forEach(formApprovalFlowD -> {
                    formApprovalFlowD.setIAuditStatus(status);
                    if (remark != null) {
                        String cMemo = formApprovalFlowD.getCMemo();
                        String memo = remark;
                        if (cMemo!=null){
                            memo = remark + ";" + cMemo;
                        }
                        formApprovalFlowD.setCMemo(memo);
                    }
                });

                tx(() -> {
                    // 反审后 节点与流程的状态都是待审批
//                    FormApprovalD approvalD = formApprovalDService.findById(approvaldid);
//                    FormApprovalFlowD flowD = flowDService.findById(flowid);
//                    approvalD.setIStatus(status);
//                    flowD.setIAuditStatus(status);

//                    boolean approvalDUpdate = approvalD.update();
//                    boolean flowDUpdate = flowD.update();

                    formApprovalDService.batchUpdate(approvalDList, approvalDList.size());
                    int[] flowDUpdate = flowDService.batchUpdate(flowDList, flowDList.size());

                    if (flowDUpdate.length != 0) {
                        // 单据的审批状态，是最后一步审批之后的反审时，需更新单据状态
                        if (Objects.equals(perStatus, "2")) {
                            // 反审后 单据状态都是待审批
                            ValidationUtils.isTrue(updateAudit(formSn, formAutoId, AuditStatusEnum.AWAIT_AUDIT.getValue(), AuditStatusEnum.APPROVED.getValue(), primaryKeyName), "更新反审失败");

                            // 是否为最后一个反审
//                         单据反审时，预留额外业务处理
                            String errMsg = invokeMethod(className, "postReverseApproveFunc", formAutoId, false, true);
                            ValidationUtils.assertBlank(errMsg, errMsg);
                        } else {
                            // 是否为第一个反审
                            if (isFirstReverse(formAutoId) && flowDUpdate.length == 1) {

                                // 撤回-审批流处理
                                revocationApprove(formAutoId);

                                ValidationUtils.isTrue(updateAudit(formSn, formAutoId, AuditStatusEnum.NOT_AUDIT.getValue(), primaryKeyName), "更新反审失败");

                                // 单据反审时，预留额外业务处理
                                String errMsg = invokeMethod(className, "postReverseApproveFunc", formAutoId, true, false);
                                ValidationUtils.assertBlank(errMsg, errMsg);
                            }
                        }
                    }
                    return true;
                });
            }

        } else {
            ValidationUtils.error("你不是当前审批人");
        }

        return SUCCESS;
    }

    /**
     * 撤回 审批流
     */
    private void revocationApprove(Long formAutoId) {
        List<FormApprovalFlowD> flowDList = flowDService.daoTemplate("formapproval.revocationApprove", Okv.by("formAutoId", formAutoId)).find();
        ValidationUtils.assertEmpty(flowDList, "该订单已在审批中，不予撤回！");

        // 删除流程表
        int delete = delete("delete from Bd_FormApprovalFlowD where iFormApprovalFlowMid in ( " +
                "    select t2.iAutoId from Bd_FormApprovalFlowM t2 where t2.iApprovalDid in ( " +
                "        select t3.iAutoId from Bd_FormApprovalD t3 where t3.iFormApprovalId = ( " +
                "            select t4.iAutoId from Bd_FormApproval t4 where iFormObjectId = ? and t4.isDeleted = '0' " +
                "            ) " +
                "        ) " +
                "    )", formAutoId);

        if (delete > 0) {
            int deleteflowM = delete("delete from Bd_FormApprovalFlowM where iApprovalDid in ( " +
                    "        select t3.iAutoId from Bd_FormApprovalD t3 where t3.iFormApprovalId = ( " +
                    "            select t4.iAutoId from Bd_FormApproval t4 where iFormObjectId = ? and t4.isDeleted = '0' " +
                    "            ) " +
                    "        )", formAutoId);

            int deletedUser = delete("delete from Bd_FormApprovalD_User where iFormApprovalDid in ( " +
                    "        select t3.iAutoId from Bd_FormApprovalD t3 where t3.iFormApprovalId = ( " +
                    "            select t4.iAutoId from Bd_FormApproval t4 where iFormObjectId = ? and t4.isDeleted = '0' " +
                    "            ) " +
                    "        )", formAutoId);

            int deleteRoleUsers = delete("delete from Bd_FormApprovalD_RoleUsers where iFormApprovaldRoleId in" +
                    " ( " +
                    "    select t2.iAutoId from Bd_FormApprovalD_Role t2 where t2.iFormApprovalDid in ( " +
                    "        select t3.iAutoId from Bd_FormApprovalD t3 where t3.iFormApprovalId = ( " +
                    "            select t4.iAutoId from Bd_FormApproval t4 where iFormObjectId = ? and t4.isDeleted = '0' " +
                    "        ) " +
                    "        ) " +
                    "    )", formAutoId);

            int deleteRole = 0;
            if (deleteRoleUsers > 0) {
                deleteRole = delete("delete from Bd_FormApprovalD_Role where iFormApprovalDid in ( " +
                        "        select t3.iAutoId from Bd_FormApprovalD t3 where t3.iFormApprovalId = ( " +
                        "            select t4.iAutoId from Bd_FormApproval t4 where iFormObjectId = ? and t4.isDeleted = '0' " +
                        "            ) " +
                        "        )", formAutoId);
            }

            if (deleteflowM > 0 || deletedUser > 0 || deleteRoleUsers > 0 || deleteRole > 0) {

                int deleteApprovalD = delete("delete from Bd_FormApprovalD where iFormApprovalId = (select t4.iAutoId from Bd_FormApproval t4 where iFormObjectId = ? and t4.isDeleted = '0' )", formAutoId);

                if (deleteApprovalD > 0) {
                    delete("delete from Bd_FormApproval where iFormObjectId = ? ", formAutoId);
                }
            }
        }
    }

    /**
     * 下个节点判断
     */
    public void nextNode(FormApproval formApproval, Long aid, Map<Long, FormApprovalFlowM> flowmMap, Date now, String primaryKeyName, String className, Long formAutoId, boolean isWithinBatch, long userId) {
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
            List<FormApprovalFlowD> flowDList = flowDService.find("select * from Bd_FormApprovalFlowD where iFormApprovalFlowMid = ? order by iSeq asc", FMid);

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
                        nextNode(formApproval, aid, flowmMap, now, primaryKeyName, className, formAutoId, isWithinBatch, userId);
                    }
                }
            }
        } else {
            // 更新单据状态
            Long iFormId = formApproval.getIFormId();
            Long iFormObjectId = formApproval.getIFormObjectId();
            Form form = formService.findById(iFormId);
            ValidationUtils.notNull(form, "找不到表单配置");
            ValidationUtils.isTrue(updateAudit(form.getCFormCode(), primaryKeyName, iFormObjectId, AuditStatusEnum.APPROVED.getValue(), AuditStatusEnum.AWAIT_AUDIT.getValue(), userId, now), "更新审核状态失败");

            // 最后一步审批通过时，调用此方法，用于支持审批状态变更外的业务处理
            String msg = invokeMethod(className, "postApproveFunc", formAutoId, isWithinBatch);
            ValidationUtils.assertBlank(msg, msg);
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
    private boolean updateSubmit(String formSn, long formAutoId, int iauditWay, Date now, String primaryKeyName) {
        Sql updateSql = updateSql().update(formSn)
                .set(IAUDITWAY, iauditWay)
                .set(IAUDITSTATUS, AuditStatusEnum.AWAIT_AUDIT.getValue())
                .set(DSUBMITTIME, now)
                .eq(primaryKeyName, formAutoId)
                .in(IAUDITSTATUS, AuditStatusEnum.NOT_AUDIT.getValue(), AuditStatusEnum.REJECTED.getValue());

        return update(updateSql) > 0;
    }

    /**
     * 更新审批状态
     */
    public boolean updateAudit(String formSn, String primaryKeyName, long formAutoId, int iAfterStatus, int iBeforeStatus, long userId, Date now) {
        Sql updateSql = updateSql().update(formSn)
                .set(IAUDITSTATUS, iAfterStatus)
                .set(DAUDITTIME, now)
                .eq(primaryKeyName, formAutoId)
                .set(IAUDITBY, userId)
                .eq(IAUDITSTATUS, iBeforeStatus);

        return update(updateSql) > 0;
    }

    /**
     * 更新审批状态，不更新审批时间
     */
    public boolean updateAudit(String formSn, long formAutoId, int iAfterStatus, int iBeforeStatus, String primaryKeyName) {
        Sql updateSql = updateSql().update(formSn)
                .set(IAUDITSTATUS, iAfterStatus)
                .eq(primaryKeyName, formAutoId)
                .eq(IAUDITSTATUS, iBeforeStatus);

        return update(updateSql) > 0;
    }

    /**
     * 不管之前单据状态
     * 更新审批状态，不更新审批时间
     */
    public boolean updateAudit(String formSn, long formAutoId, int iAfterStatus, String primaryKeyName) {
        Sql updateSql = updateSql().update(formSn)
                .set(IAUDITSTATUS, iAfterStatus)
                .eq(primaryKeyName, formAutoId);

        return update(updateSql) > 0;
    }

    /**
     * 更新撤销审核
     */
    public boolean updateWithdraw(String formSn, String primaryKeyName, long formAutoId) {
        Sql updateSql = updateSql().update(formSn)
                .set(IAUDITSTATUS, AuditStatusEnum.NOT_AUDIT.getValue())
                .eq(primaryKeyName, formAutoId)
                .eq(IAUDITSTATUS, AuditStatusEnum.AWAIT_AUDIT.getValue());

        return update(updateSql) > 0;
    }

    /**
     * 撤回，已适配审核/审批流
     *
     * @param formSn         表名
     * @param primaryKeyName 主键名
     * @param formAutoId     单据ID
     * @param className      实现审批业务的类名
     */
    public Ret withdraw(String formSn, String primaryKeyName, long formAutoId, String className) {
        Record formData = getApprovalForm(formSn, primaryKeyName, formAutoId);
        ValidationUtils.equals(AuditStatusEnum.AWAIT_AUDIT.getValue(), formData.getInt(IAUDITSTATUS), "非待审核状态");

        // 当前登录人
        User user = JBoltUserKit.getUser();
        Long userId = user.getId();

        // 单据创建人
        Long icreateby = formData.getLong("icreateby");
        // 撤回人跟单据创建人要是同一个用户
        ValidationUtils.equals(icreateby, userId, "撤回操作必须是单据创建人");

        // 执行状态更新
        ValidationUtils.isTrue(updateWithdraw(formSn, primaryKeyName, formAutoId), "更新撤回失败");

        switch (AuditWayEnum.toEnum(formData.getInt(IAUDITWAY))) {
            // 状态审批
            case STATUS:
                //  审批流
            case FLOW:
                revocationApprove(formAutoId);
                break;
            default:
                break;
        }

        // 撤回后的处理业务
        String msg = invokeMethod(className, "postWithdrawFunc", formAutoId);
        ValidationUtils.assertBlank(msg, msg);

        return SUCCESS;
    }

    /**
     * 批量审批
     */
    public Ret batchApprove(String ids, String formSn, String primaryKeyName, String className) {
        List<Long> formAutoIds = new ArrayList<>();

        int status = AuditStatusEnum.APPROVED.getValue();

        tx(() -> {

            for (String id : StrSplitter.split(ids, COMMA, true, true)) {
                Long formAutoId = Long.parseLong(id);
                formAutoIds.add(formAutoId);

                approve(formAutoId, formSn, status, primaryKeyName, className, true,null);
            }

            // 批量审核后置业务处理
            String msg = invokeMethod(className, "postBatchApprove", formAutoIds);
            ValidationUtils.assertBlank(msg, msg);

            return true;
        });

        return SUCCESS;
    }

    /**
     * 批量审核不通过
     */
    public Ret batchReject(String ids, String formSn, String primaryKeyName, String className) {
        List<Long> formAutoIds = new ArrayList<>();

        int status = AuditStatusEnum.REJECTED.getValue();

        tx(() -> {

            for (String id : StrSplitter.split(ids, COMMA, true, true)) {
                Long formAutoId = Long.parseLong(id);
                formAutoIds.add(formAutoId);

                reject(formAutoId, formSn, status, primaryKeyName, className,null,true);
            }

            // 批量审核不通过后置业务处理
            String msg = invokeMethod(className, "postBatchReject", formAutoIds);
            ValidationUtils.assertBlank(msg, msg);

            return true;
        });

        return SUCCESS;
    }

//    /**
//     * 批量反审批
//     */
//    public Ret batchReverseApprove(String ids, String formSn, String primaryKeyName, String className) {
//        List<Long> formAutoIds = new ArrayList<>();
//
//        int status = AuditStatusEnum.AWAIT_AUDIT.getValue();
//
//        tx(() -> {
//
//            for (String id : StrSplitter.split(ids, COMMA, true, true)) {
//
//                long formAutoId = Long.parseLong(id);
//                formAutoIds.add(formAutoId);
//
//                reverseApprove(formAutoId, formSn, primaryKeyName, status, className);
//            }
//
//            String msg = invokeMethod(className, "", formAutoIds);
//            ValidationUtils.assertBlank(msg, msg);
//
//            return true;
//        });
//
//        return SUCCESS;
//    }

    /**
     * 批量撤销审批
     */
    public Ret batchBackout(String ids, String formSn, String primaryKeyName, String className) {
        List<Long> formAutoIds = new ArrayList<>();

        tx(() -> {

            for (String id : StrSplitter.split(ids, COMMA, true, true)) {
                long formAutoId = Long.parseLong(id);
                formAutoIds.add(formAutoId);

                backout(formAutoId, formSn, primaryKeyName, className);
            }

            String msg = invokeMethod(className, "postBatchBackout", formAutoIds);
            ValidationUtils.assertBlank(msg, msg);

            return true;
        });

        return SUCCESS;
    }

    /**
     * 撤销审批
     */
    public Ret backout(Long formAutoId, String formSn, String primaryKeyName, String className) {
        // 查出单据对应的审批流配置
        FormApproval formApproval = findByFormAutoId(formAutoId);
        ValidationUtils.notNull(formApproval, "单据未提交审批！");
        Record formData = getApprovalForm(formSn, primaryKeyName, formAutoId);

        tx(() -> {

            switch (AuditWayEnum.toEnum(formData.getInt(IAUDITWAY))) {
                // 状态审批
                case STATUS:
                    switch (AuditStatusEnum.toEnum(formData.getInt(IAUDITSTATUS))) {
                        case APPROVED:
                            String msg = invokeMethod(className, "preWithdrawFromAuditted", formAutoId);
                            ValidationUtils.assertBlank(msg, msg);

                            // 更新单据状态为未审批
                            ValidationUtils.isTrue(updateAudit(formSn, formAutoId, AuditStatusEnum.NOT_AUDIT.getValue(), formData.getInt(IAUDITSTATUS), primaryKeyName), "更新审核状态失败");

                            msg = invokeMethod(className, "postWithdrawFromAuditted", formAutoId);
                            ValidationUtils.assertBlank(msg, msg);
                            break;
                        case AWAIT_AUDIT:
                            // 更新单据状态为未审批
                            ValidationUtils.isTrue(updateAudit(formSn, formAutoId, AuditStatusEnum.NOT_AUDIT.getValue(), formData.getInt(IAUDITSTATUS), primaryKeyName), "更新审批状态失败");

                            msg = invokeMethod(className, "withdrawFromAuditting", formAutoId);
                            ValidationUtils.assertBlank(msg, msg);
                            break;
                        default:
                            throw new ParameterException("状态不合法");
                    }
                    break;

                    //  审批流
                case FLOW:
                    switch (AuditStatusEnum.toEnum(formData.getInt(IAUDITSTATUS))) {
                        case APPROVED:
                            String msg = invokeMethod(className, "preWithdrawFromAuditted", formAutoId);
                            ValidationUtils.assertBlank(msg, msg);

                            // 更新单据状态为未审批
                            ValidationUtils.isTrue(updateAudit(formSn, formAutoId, AuditStatusEnum.NOT_AUDIT.getValue(), formData.getInt(IAUDITSTATUS), primaryKeyName), "更新审批状态失败");

                            msg = invokeMethod(className, "postWithdrawFromAuditted", formAutoId);
                            ValidationUtils.assertBlank(msg, msg);
                            break;
                        case AWAIT_AUDIT:
                            // 更新单据状态为未审批
                            ValidationUtils.isTrue(updateAudit(formSn, formAutoId, AuditStatusEnum.NOT_AUDIT.getValue(), formData.getInt(IAUDITSTATUS), primaryKeyName), "更新审批状态失败");

                            msg = invokeMethod(className, "withdrawFromAuditting", formAutoId);
                            ValidationUtils.assertBlank(msg, msg);
                            break;
                        default:
                            throw new ParameterException("状态不合法");
                    }

                    // 更新单据审批流主表
                    formApproval.setIsDeleted(true);
                    ValidationUtils.isTrue(formApproval.update(), ErrorMsg.UPDATE_FAILED);
                    break;
                default:
                    break;
            }

            return true;
        });
        return SUCCESS;
    }

    /**
     * 查询审批过程待审批的人员
     */
    public List<Long> getNextApprovalUserIds(Long formAutoId, Integer size) {
        Kv kv = Kv.by("formAutoId", formAutoId)
                .set("size", size);

        return dbTemplate("formapproval.getNextApprovalUserIds", kv).query();
    }

    /**
     * 查询审批过程待审批的人员
     */
    public List<String> getNextApprovalUserNames(Long formAutoId, Integer size) {
        Kv kv = Kv.by("formAutoId", formAutoId)
                .set("size", size);

        return dbTemplate("formapproval.getNextApprovalUserNames", kv).query();
    }

    /**
     * 反射调用审批后的业务方法
     *
     * @param className  类名
     * @param methodName 方法名
     * @param args       额外参数
     */
    private String invokeMethod(String className, String methodName, Object... args) {
        try {
            // 获取Class
            Class<?> clazz = Class.forName(className);
            // 获取Service实例
            Object o = Aop.get(clazz);

            // 反审后的处理逻辑
            Method method = ReflectUtil.getMethodByName(o.getClass(), methodName);
            ValidationUtils.notNull(method, String.format("必须实现审批变更后的方法“%s”", methodName));

            // 调用方法
            return (String) method.invoke(o, args);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return Constants.removeExceptionPrefix(e.getCause().getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "审批回调异常：" + e.getLocalizedMessage();
        }
    }

    /**
     * 判断是否为审批第一人
     */
    public Boolean isFirstReverse(Long formId) {
        List<FormApprovalFlowM> approvalFlowMList = flowMService.daoTemplate("formapproval.isFirstReverse", Kv.by("formId", formId)).find();
        return CollUtil.isEmpty(approvalFlowMList);
    }

    /**
     * 根据用户ID获取人员信息
     */
    public Person findPersonByUserId(Long userId) {
        Long personId = UserCache.ME.getPersonId(userId, getOrgId());
        ValidationUtils.notNull(personId, "系统用户未设置人员信息");

        return personService.findById(personId);
    }

    /**
     * 审核通过，公用实现
     *
     * @param formSn         表单编码
     * @param formAutoId     单据ID
     * @param primaryKeyName 主键名
     * @param className      类名
     * @param isWithinBatch  是否为批量审核调用
     */
    public Ret approveByStatus(String formSn, long formAutoId, String primaryKeyName, String className, boolean isWithinBatch, long userId) {
        tx(() -> {
            Record formData = getApprovalForm(formSn, primaryKeyName, formAutoId);
            ValidationUtils.equals(formData.getInt(IAUDITWAY), AuditWayEnum.STATUS.getValue(), "审批流审批的单据，不允许操作“审核通过”按钮");
            ValidationUtils.equals(formData.getInt(IAUDITSTATUS), AuditStatusEnum.AWAIT_AUDIT.getValue(), "非待审核状态");

            // 更新审核通过
            ValidationUtils.isTrue(updateAudit(formSn, primaryKeyName, formAutoId, AuditStatusEnum.APPROVED.getValue(), AuditStatusEnum.AWAIT_AUDIT.getValue(), userId, new Date()), "更新审核状态失败");

            // 后置业务实现
            String msg = invokeMethod(className, "postApproveFunc", formAutoId, isWithinBatch);
            ValidationUtils.assertBlank(msg, msg);

            return true;
        });

        return SUCCESS;
    }

    /**
     * 审核不通过，公用实现
     *
     * @param formSn         表单编码
     * @param formAutoId     单据ID
     * @param primaryKeyName 主键名
     * @param className      类名
     * @param isWithinBatch  是否为批处理
     */
    public Ret rejectByStatus(String formSn, Long formAutoId, String primaryKeyName, String className, boolean isWithinBatch, long userId) {
        tx(() -> {
            Record formData = getApprovalForm(formSn, primaryKeyName, formAutoId);
            ValidationUtils.equals(formData.getInt(IAUDITWAY), AuditWayEnum.STATUS.getValue(), "审批流审批的单据，不允许操作“审核不通过”按钮");
            ValidationUtils.equals(formData.getInt(IAUDITSTATUS), AuditStatusEnum.AWAIT_AUDIT.getValue(), "非待审核状态");

            // 更新审核不通过
            ValidationUtils.isTrue(updateAudit(formSn, primaryKeyName, formAutoId, AuditStatusEnum.REJECTED.getValue(), AuditStatusEnum.AWAIT_AUDIT.getValue(), userId, new Date()), "更新审核状态失败");

            // 后置业务实现
            String msg = invokeMethod(className, "postRejectFunc", formAutoId, isWithinBatch);
            ValidationUtils.assertBlank(msg, msg);

            return true;
        });

        return SUCCESS;
    }

    /**
     * 反审核，公用实现
     *
     * @param formSn         表单编码
     * @param formAutoId     单据ID
     * @param primaryKeyName 主键名
     * @param className      类名
     */
    public Ret reverseApproveByStatus(String formSn, Long formAutoId, String primaryKeyName, String className, long userId) {
        tx(() -> {
            Record formData = getApprovalForm(formSn, primaryKeyName, formAutoId);
            ValidationUtils.equals(formData.getInt(IAUDITWAY), AuditWayEnum.STATUS.getValue(), "审批流审批的单据，不允许操作“反审核”按钮");
            ValidationUtils.equals(formData.getInt(IAUDITSTATUS), AuditStatusEnum.APPROVED.getValue(), "非待审核状态");

            // 前置业务实现
            String msg = invokeMethod(className, "preReverseApproveFunc", formAutoId, true, true);
            ValidationUtils.assertBlank(msg, msg);

            // 更新审核通过
            ValidationUtils.isTrue(updateAudit(formSn, primaryKeyName, formAutoId, AuditStatusEnum.NOT_AUDIT.getValue(), AuditStatusEnum.APPROVED.getValue(), userId, new Date()), "更新审核状态失败");

            // 后置业务实现
            msg = invokeMethod(className, "postReverseApproveFunc", formAutoId, true, true);
            ValidationUtils.assertBlank(msg, msg);

            return true;
        });

        return SUCCESS;
    }

    /**
     * 批量审核通过，公用实现
     *
     * @param ids            单据IDs
     * @param formSn         表单编码
     * @param primaryKeyName 主键名
     * @param className      实现审批业务的类名
     */
    public Ret batchApproveByStatus(String ids, String formSn, String primaryKeyName, String className, long userId) {
        List<Long> formAutoIds = new ArrayList<>();

        tx(() -> {

            for (String id : StrSplitter.split(ids, COMMA, true, true)) {

                long formAutoId = Long.parseLong(id);
                formAutoIds.add(formAutoId);

                approveByStatus(formSn, formAutoId, primaryKeyName, className, true, userId);
            }

            // 批量审核通过，后置业务
            String msg = invokeMethod(className, "postBatchApprove", formAutoIds);
            ValidationUtils.assertBlank(msg, msg);

            return true;
        });

        return SUCCESS;
    }

    /**
     * 批量审核不通过，公用实现
     *
     * @param ids            单据IDs
     * @param formSn         表单编码
     * @param primaryKeyName 主键名
     * @param className      实现审批业务的类名
     */
    public Ret batchRejectByStatus(String ids, String formSn, String primaryKeyName, String className, long userId) {
        List<Long> formAutoIds = new ArrayList<>();

        tx(() -> {

            for (String id : StrSplitter.split(ids, COMMA, true, true)) {
                long formAutoId = Long.parseLong(id);
                formAutoIds.add(formAutoId);

                rejectByStatus(formSn, formAutoId, primaryKeyName, className, true, userId);
            }

            // 批量审核不通过后置业务处理
            String msg = invokeMethod(className, "postBatchReject", formAutoIds);
            ValidationUtils.assertBlank(msg, msg);

            return true;
        });

        return SUCCESS;
    }

    public Record getApprovalForm(String formSn, String primaryKeyName, long formAutoId) {
        Record formData = Db.use(dataSourceConfigName()).findFirst(String.format("SELECT * FROM %s WHERE %s = %d ", formSn, primaryKeyName, formAutoId));
        ValidationUtils.notNull(formData, "单据不存在");
        ValidationUtils.isTrue(!formData.getBoolean(IS_DELETED), "单据已被删除");
        return formData;
    }

}
