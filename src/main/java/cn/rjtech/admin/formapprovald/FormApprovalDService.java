package cn.rjtech.admin.formapprovald;

import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;
import cn.jbolt.core.util.JBoltArrayUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.admin.formapprovaldrole.FormapprovaldRoleService;
import cn.rjtech.admin.formapprovaldroleusers.FormapprovaldRoleusersService;
import cn.rjtech.admin.formapprovalduser.FormapprovaldUserService;
import cn.rjtech.admin.formapprovalflowd.FormApprovalFlowDService;
import cn.rjtech.admin.formapprovalflowm.FormApprovalFlowMService;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 表单审批流 Service
 *
 * @ClassName: FormApprovalDService
 * @author: RJ
 * @date: 2023-04-18 17:29
 */
public class FormApprovalDService extends BaseService<FormApprovalD> {

    private final FormApprovalD dao = new FormApprovalD().dao();

    @Override
    protected FormApprovalD dao() {
        return dao;
    }

    @Inject
    private UserService userService;
    @Inject
    private FormApprovalFlowMService flowMService;
    @Inject
    private FormApprovalFlowDService flowDService;
    @Inject
    private FormApprovalService formApprovalService;
    @Inject
    private FormapprovaldUserService formapprovaldUserService;
    @Inject
    private FormapprovaldRoleService formapprovaldRoleService;
    @Inject
    private FormapprovaldRoleusersService formapprovaldRoleusersService;

    /**
     * 后台管理分页查询
     */
    public Page<FormApprovalD> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 后台管理分页查询
     */
    public Page<Record> datas(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("formapprovald.findRecordsByFormId", kv).paginate(pageNumber, pageSize);

    }

    /**
     * 历史数据源
     */
    public Page<Record> historyDatas(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("formapprovald.findRecordsByFormIdHistory", kv).paginate(pageNumber, pageSize);
    }

    /**
     * 历史数据源列表
     */
    public Page<Record> historyList(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("formapprovald.historyDatas", kv).paginate(pageNumber, pageSize);
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
     * @param formApprovalD 要删除的model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(FormApprovalD formApprovalD, Kv kv) {
        //addDeleteSystemLog(formApprovalD.getIautoid(), JBoltUserKit.getUserId(),formApprovalD.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param formApprovalD 要删除的model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(FormApprovalD formApprovalD, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(formApprovalD, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换isdirectonmissing属性
     */
    public Ret toggleIsDirectOnMissing(Long id) {
        return toggleBoolean(id, "isDirectOnMissing");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param formApprovalD 要toggle的model
     * @param column        操作的哪一列
     * @param kv            携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(FormApprovalD formApprovalD, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(FormApprovalD formApprovalD, String column, Kv kv) {
        //addUpdateSystemLog(formApprovalD.getIautoid(), JBoltUserKit.getUserId(), formApprovalD.getName(),"的字段["+column+"]值:"+formApprovalD.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param formApprovalD model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    public String checkInUse(FormApprovalD formApprovalD, Kv kv) {
        //这里用来覆盖 检测FormApprovalD是否被其它表引用
        return null;
    }

    /**
     * 复制相同数据
     */
    public FormApprovalD copySetObj(Long mid, ApprovalD approvalD) {
        FormApprovalD formApprovalD = new FormApprovalD();

        formApprovalD.setIFormApprovalId(mid);
        formApprovalD.setISeq(approvalD.getISeq());
        formApprovalD.setIStep(approvalD.getIStep());
        formApprovalD.setCName(approvalD.getCName());
        formApprovalD.setIType(approvalD.getIType());
        formApprovalD.setIApprovalWay(approvalD.getIApprovalWay());
        formApprovalD.setISupervisorType(approvalD.getISupervisorType());
        formApprovalD.setIsDirectOnMissing(approvalD.getIsDirectOnMissing());
        formApprovalD.setISkipOn(approvalD.getISkipOn());
        formApprovalD.setISpecUserId(approvalD.getISpecUserId());
        formApprovalD.setISelfSelectWay(approvalD.getISelfSelectWay());
        formApprovalD.setISingleType(approvalD.getISingleType());
        formApprovalD.setISingleId(approvalD.getISingleId());
        formApprovalD.setIStatus(1);

        return formApprovalD;
    }

    /**
     * 根据外键获取数据
     */
    public List<FormApprovalD> findListByMid(long id) {
        return find("select * from Bd_FormApprovalD where iFormApprovalId = ? order by iSeq asc ", id);
    }

    /**
     * 根据外键获取未审批的节点数据
     */
    public List<FormApprovalD> findListByAid(long id) {
        return find("select * from Bd_FormApprovalD where iFormApprovalId = ? and iStatus = 1 order by iSeq asc ", id);
    }

    /**
     * 审批人列表数据源
     */
    public Page<Record> userDatas(int pageNum, int pageSize, Kv kv) {
        return dbTemplate("formapprovald.userDatas", kv).paginate(pageNum, pageSize);
    }

    /**
     * 多个可编辑表格同时提交
     */
    public Ret submitByJBoltTables(JBoltTableMulti jboltTableMulti) {
        if (jboltTableMulti == null || jboltTableMulti.isEmpty()) {
            return fail(JBoltMsg.JBOLTTABLE_IS_BLANK);
        }

        // 这里可以循环遍历 保存处理每个表格 也可以 按照name自己get出来单独处理
        JBoltTable jBoltTable = jboltTableMulti.getJBoltTable("table1");
        JBoltTable jBoltTable2 = jboltTableMulti.getJBoltTable("table2");

        // 该节点的基础信息
        FormApprovalD approvalD = jBoltTable.getFormModel(FormApprovalD.class, "formApprovalD");
        ValidationUtils.notNull(approvalD, JBoltMsg.PARAM_ERROR);

        // 节点ID
        Long did = approvalD.getIAutoId();

        // 该节点 流程主表信息
        FormApprovalFlowM flowM = flowMService.findFirstByIapprovalDid(did);
        // 流程主表ID
        Long flowMid = flowM.getIAutoId();

        // 该审批流的主表
        FormApproval approval = formApprovalService.findById(approvalD.getIFormApprovalId());
        // 提审人
        long iCreateBy = approval.getICreateBy();

//        Person person = personService.findFirstByUserId(iCreateBy);
        Person person = formApprovalService.findPersonByUserId(iCreateBy);

        tx(() -> {

            FormApprovalD oldApprovalD = null;

            //修改
            if (isOk(approvalD.getIAutoId())) {
                oldApprovalD = findFirst("select * from Bd_FormApprovalD where iAutoId = " + approvalD.getIAutoId());

                ValidationUtils.isTrue(approvalD.update(), JBoltMsg.FAIL);


            } else {
                //新增
                ValidationUtils.isTrue(approvalD.save(), JBoltMsg.FAIL);
            }

            Long iApprovalDid = approvalD.getIAutoId();
            Integer iType = approvalD.getIType();

            /*
             * 比较旧配置的审批方式
             */
            if (oldApprovalD != null) {

//			    相同则为修改（只有新增 或者 删除
                if (Objects.equals(iType, oldApprovalD.getIType())) {

                    List<FormApprovalFlowD> flowds = new ArrayList<>();

                    switch (iType) {
                        // 指定人员
                        case 1:
                            if (jBoltTable.saveIsNotBlank()) {
                                List<FormapprovaldUser> saveModelList = jBoltTable.getSaveModelList(FormapprovaldUser.class);

                                List<Record> flowDList = findRecords("select * from Bd_FormApprovalFlowD where iFormApprovalFlowMid = ? ", flowMid);

                                int size = flowDList.size();

                                for (FormapprovaldUser approvaldUser : saveModelList) {
                                    approvaldUser.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                    approvaldUser.setIFormApprovalId(iApprovalDid);

                                    FormApprovalFlowD flowD = new FormApprovalFlowD();

                                    flowD.setIFormApprovalFlowMid(flowMid);
                                    flowD.setISeq(++size);
                                    flowD.setIUserId(approvaldUser.getIUserId());
                                    flowD.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());

                                    flowds.add(flowD);
                                }
                                formapprovaldUserService.batchSave(saveModelList, saveModelList.size());
                            }

                            if (jBoltTable.updateIsNotBlank()) {
                                List<FormapprovaldUser> updateModelList = jBoltTable.getUpdateModelList(FormapprovaldUser.class);
                                formapprovaldUserService.batchUpdate(updateModelList, updateModelList.size());
                            }

                            if (jBoltTable.deleteIsNotBlank()) {
                                String deleteIds = JBoltArrayUtil.join(jBoltTable.getDelete(), ",");
                                Integer integer = flowDService.deleteByMidAndUserId(iApprovalDid, deleteIds);
                                if (integer > 0) {
                                    formapprovaldUserService.deleteByIds(jBoltTable.getDelete());
                                }
                            }
                            break;
                        case 5:
                            if (jBoltTable2.saveIsNotBlank()) {
                                List<FormapprovaldRole> saveModelList = jBoltTable2.getSaveModelList(FormapprovaldRole.class);
                                saveModelList.forEach(approvaldRole -> {
                                    approvaldRole.setIFormApprovalId(iApprovalDid);
                                    approvaldRole.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());

                                    approvaldRole.save();
                                    Page<User> userPage = userService.paginateUsersByRoleId(1, 20, approvaldRole.getIRoleId());
                                    List<User> userList = userPage.getList();
                                    if (userList.size() > 0) {
                                        List<FormapprovaldRoleusers> list = new ArrayList<>();
                                        for (int i = 0; i < userList.size(); i++) {
                                            User user = userList.get(i);
                                            int j = i + 1;

                                            FormapprovaldRoleusers roleusers = new FormapprovaldRoleusers();
                                            roleusers.setIFormApprovaldRoleId(approvaldRole.getIAutoId());
                                            roleusers.setISeq(j);
                                            roleusers.setIUserId(user.getId());
                                            list.add(roleusers);

                                            FormApprovalFlowD flowD1 = new FormApprovalFlowD();
                                            flowD1.setIUserId(user.getId());
                                            flowD1.setIFormApprovalFlowMid(flowMid);
                                            flowD1.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                            flowD1.setISeq(j);
                                            flowds.add(flowD1);
                                        }
                                        formapprovaldRoleusersService.batchSave(list, list.size());
                                    }
                                });
//								formapprovaldRoleService.batchSave(saveModelList,saveModelList.size());

                            }
                            if (jBoltTable2.updateIsNotBlank()) {
                                List<FormapprovaldRole> updateModelList = jBoltTable2.getUpdateModelList(FormapprovaldRole.class);
                                formapprovaldRoleService.batchUpdate(updateModelList, updateModelList.size());
                            }

                            if (jBoltTable2.deleteIsNotBlank()) {
                                formapprovaldRoleService.deleteByIds(jBoltTable2.getDelete());
                                String deleteIds = JBoltArrayUtil.join(jBoltTable2.getDelete(), ",");
                                flowDService.deleteByMidAndRoleId(iApprovalDid, deleteIds);
                            }
                            break;
                        default:
                            break;
                    }

                    flowDService.batchSave(flowds, flowds.size());

                } else {

//				    不同则为新增
//                  需删除之前的流程  新增新的流程
                    flowDService.deleteByMid(flowMid);

                    // 流程子表集合
                    List<FormApprovalFlowD> flowDList = new ArrayList<>();

                    /*
                     * 部门设置的配置信息
                     */
                    // 主管类型
                    Integer iSupervisorType = approvalD.getISupervisorType();
                    // 是否上级代审
                    Boolean isDirectOnMissing = approvalD.getIsDirectOnMissing();
                    // 为空是否跳过或是指定审批人
                    Integer iSkipOn = approvalD.getISkipOn();
                    // 指定审批人
                    Long iSpecUserId = approvalD.getISpecUserId();

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
                    switch (iType) {
                        // 指定人员
                        case 1:
                            if (jBoltTable.saveIsNotBlank()) {
                                // 查询是否存在人员配置信息
                                List<FormapprovaldUser> saveModelList = jBoltTable.getSaveModelList(FormapprovaldUser.class);

                                saveModelList.forEach(approvaldUser -> {
//                                    FormapprovaldUser formapprovaldUser = new FormapprovaldUser();
                                    approvaldUser.setIFormApprovalId(approvalD.getIAutoId());
                                    approvaldUser.setISeq(approvaldUser.getISeq());
                                    approvaldUser.setIUserId(approvaldUser.getIUserId());
                                    approvaldUser.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                    approvaldUser.setIPersonId(approvaldUser.getIPersonId());

                                    FormApprovalFlowD flowD = new FormApprovalFlowD();
                                    flowD.setIFormApprovalFlowMid(flowMid);
                                    flowD.setISeq(approvaldUser.getISeq());
                                    flowD.setIUserId(approvaldUser.getIUserId());
                                    flowD.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                    flowDList.add(flowD);
                                });
                                formapprovaldUserService.batchSave(saveModelList);
                            }
                                List<FormapprovaldUser> byDid = formapprovaldUserService.findByDid(iApprovalDid);

                                ValidationUtils.isTrue(byDid!=null && byDid.size() > 0,"该审批顺序为" + approvalD.getISeq() +
                                        "配置的指定人员未指定人员");

                                byDid.forEach(approvaldUser -> {
                                    FormApprovalFlowD flowD = new FormApprovalFlowD();
                                    flowD.setIFormApprovalFlowMid(flowMid);
                                    flowD.setISeq(approvaldUser.getISeq());
                                    flowD.setIUserId(approvaldUser.getIUserId());
                                    flowD.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                    flowDList.add(flowD);
                                });


                            if (jBoltTable.deleteIsNotBlank()){
                                Object[] delete = jBoltTable.getDelete();
                                formapprovaldUserService.realDeleteByIds(delete);
                            }
                            break;
                        case 2: //部门主管

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

                            /*
                             * 一级主管（直属主管）：班负责人，班长
                             * 二级主管：系负责人，系长
                             * 三级主管：科负责人，科长
                             * 四级主管：部负责人，部长
                             * 五级主管：公司负责人，总经理
                             */

                            getStair = iSupervisorType - 1;
                            getSuperior = iSupervisorType;

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
                                        kv.set("code",code);
                                        kv.setIfNotNull("orgId", getOrgId());
                                        Record personUser = dbTemplate("formapprovald.findPersonUser", kv).findFirst();
                                        Long iuserid = personUser.getLong("iuserid");
                                        String cpsnname = personUser.getStr("cpsnname");
                                        if (iuserid != null) {
                                            FormApprovalFlowD flowD = new FormApprovalFlowD();
                                            flowD.setIFormApprovalFlowMid(flowMid);
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
                                                kv.set("code",code);
                                                kv.setIfNotNull("orgId", getOrgId());
                                                Record personUser = dbTemplate("formapprovald.findPersonUser", kv).findFirst();
                                                Long iuserid = personUser.getLong("iuserid");
                                                String cpsnname = personUser.getStr("cpsnname");
                                                if (iuserid != null) {
                                                    FormApprovalFlowD flowD = new FormApprovalFlowD();
                                                    flowD.setIFormApprovalFlowMid(flowMid);
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
                                            flowD.setIFormApprovalFlowMid(flowMid);
                                            flowD.setISeq(1);
                                            flowD.setIUserId(iSpecUserId);
                                            flowD.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                            flowDList.add(flowD);
                                        }

                                        if (iSkipOn == 1) {
                                            FormApprovalFlowD flowD = new FormApprovalFlowD();
                                            flowD.setIFormApprovalFlowMid(flowMid);
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
                                        kv.set("code",code);
                                        kv.setIfNotNull("orgId", getOrgId());
                                        Record personUser = dbTemplate("formapprovald.findPersonUser", kv).findFirst();
                                        Long iuserid = personUser.getLong("iuserid");
                                        String cpsnname = personUser.getStr("cpsnname");
                                        if (iuserid != null) {
                                            FormApprovalFlowD flowD = new FormApprovalFlowD();
                                            flowD.setIFormApprovalFlowMid(flowMid);
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
                                        flowD.setIFormApprovalFlowMid(flowMid);
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
                            flowD.setIFormApprovalFlowMid(flowMid);
                            flowD.setISeq(1);
                            flowD.setIUserId(iCreateBy);
                            flowD.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                            flowDList.add(flowD);
                            break;
                        // 角色
                        case 5:
                            if (jBoltTable2.saveIsNotBlank()) {
                                List<FormapprovaldRole> saveRoleList =
                                        jBoltTable2.getSaveModelList(FormapprovaldRole.class);

                                saveRoleList.forEach(approvaldRole -> {
                                    FormapprovaldRole formapprovaldRole = new FormapprovaldRole();
                                    formapprovaldRole.setIFormApprovalId(did);
                                    formapprovaldRole.setISeq(approvaldRole.getISeq());
                                    formapprovaldRole.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                    formapprovaldRole.setIRoleId(approvaldRole.getIRoleId());

                                    formapprovaldRole.save();
                                    Page<User> userPage = userService.paginateUsersByRoleId(1, 20, approvaldRole.getIRoleId());
                                    List<User> userList = userPage.getList();
                                    if (userList.size() > 0) {
                                        List<FormapprovaldRoleusers> list1 = new ArrayList<>();
                                        for (int i = 0; i < userList.size(); i++) {
                                            User user = userList.get(i);
                                            int j = i + 1;

                                            FormapprovaldRoleusers roleusers = new FormapprovaldRoleusers();
                                            roleusers.setIFormApprovaldRoleId(formapprovaldRole.getIAutoId());
                                            roleusers.setISeq(j);
                                            roleusers.setIUserId(user.getId());
                                            list1.add(roleusers);

                                            FormApprovalFlowD flowD1 = new FormApprovalFlowD();
                                            flowD1.setIUserId(user.getId());
                                            flowD1.setIFormApprovalFlowMid(flowMid);
                                            flowD1.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                            flowD1.setISeq(j);
                                            flowDList.add(flowD1);
                                        }
                                        formapprovaldRoleusersService.batchSave(list1, list1.size());
                                    }

                                });
                            }

                                List<FormapprovaldRole> roles = formapprovaldRoleService.findByDid(iApprovalDid);

                                ValidationUtils.isTrue(roles != null && roles.size() > 0,
                                        "该审批顺序为" + approvalD.getISeq() +
                                        "配置的角色未指定角色");

                                for (int i = 0; i < roles.size(); i++) {
                                    FormapprovaldRole role = roles.get(i);
                                    Long roleIAutoId = role.getIAutoId();
                                    List<FormapprovaldRoleusers> roleusers = formapprovaldRoleusersService.findByIformApprovaldRoleId(roleIAutoId);
                                    if (roleusers != null && roleusers.size() > 0) {
                                        for (int ii = 0; ii < roleusers.size(); ii++) {
                                            int j = ii + 1;
                                            FormapprovaldRoleusers formapprovaldRoleusers = roleusers.get(ii);
                                            FormApprovalFlowD flowD1 = new FormApprovalFlowD();
                                            flowD1.setIUserId(formapprovaldRoleusers.getIUserId());
                                            flowD1.setIFormApprovalFlowMid(flowMid);
                                            flowD1.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                                            flowD1.setISeq(j);
                                            flowDList.add(flowD1);
                                        }
                                    }
                                }


                            if (jBoltTable2.deleteIsNotBlank()){
                                Object[] delete = jBoltTable2.getDelete();
                                formapprovaldRoleService.deleteBenAndZi(delete);
                            }
                            break;
                        default:
                            break;
                    }
                    flowDService.batchSave(flowDList, flowDList.size());
                }
            }

            return true;
        });

        return SUCCESS;
    }

    /**
     * 人员行数据
     */
    public Page<Record> lineDatas(int pageNum, int pageSize, Kv kv) {
        return dbTemplate("formapprovald.lineDatas", kv).paginate(pageNum, pageSize);
    }

    /**
     * 角色行数据
     */
    public Page<Record> roleDatas(int pageNum, int pageSize, Kv kv) {
        return dbTemplate("formapprovald.roleDatas", kv).paginate(pageNum, pageSize);
    }

    /**
     * 判断该节点是否已在审批中
     */
    public Integer isApprovaling(Long id) {
        List<FormApprovalFlowD> flowds = flowDService.find("select * " +
                "from Bd_FormApprovalFlowD " +
                "where iFormApprovalFlowMid = ( " +
                "    select t1.iAutoId " +
                "    from Bd_FormApprovalFlowM t1 " +
                "    where iApprovalDid = ? ) " +
                "  and (iAuditStatus = 2 or iAuditStatus = 3)", id);

        return flowds.size();
    }

    public Page<Record> chooseUsers(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("formapprovald.chooseUsers", kv).paginate(pageNumber, pageSize);
    }

    /**
     * 获取角色人员信息
     */
    public Page<Record> roleUsers(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("formapprovald.roleUsers", kv).paginate(pageNumber, pageSize);
    }

    /**
     * 保存角色人员方法
     */
    public Ret saveRoleUser(JBoltTable jBoltTable) {
        ValidationUtils.notNull(jBoltTable, JBoltMsg.PARAM_ERROR);

        JSONObject form = jBoltTable.getForm();
        LOG.info("form===={}", form);

        Long autoId = form.getLong("autoId");

//		查询流程主表ID
        Record flowM = findFirstRecord("select iAutoId " +
                "from Bd_FormApprovalFlowM " +
                "where iApprovalDid = " +
                "      (select iFormApprovalDid from Bd_FormApprovalD_Role where iAutoId = " + autoId + ")");

        Long flowMId = flowM.getLong("iautoid");

        tx(() -> {

            if (jBoltTable.saveIsNotBlank()) {
                List<FormapprovaldRoleusers> saveModelList = jBoltTable.getSaveModelList(FormapprovaldRoleusers.class);

                // 流程子表集合
                List<FormApprovalFlowD> flowDList = new ArrayList<>();

                saveModelList.forEach(approvaldRoleusers -> {
                    approvaldRoleusers.setIFormApprovaldRoleId(autoId);

                    FormApprovalFlowD flowD1 = new FormApprovalFlowD();
                    flowD1.setIUserId(approvaldRoleusers.getIUserId());
                    flowD1.setIFormApprovalFlowMid(flowMId);
                    flowD1.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
                    flowD1.setISeq(approvaldRoleusers.getISeq());
                    flowDList.add(flowD1);
                });

                flowDService.batchSave(flowDList, flowDList.size());
                formapprovaldRoleusersService.batchSave(saveModelList, saveModelList.size());
            }

            if (jBoltTable.deleteIsNotBlank()) {

//			    先删除流程表数据
                int delete = delete("delete " +
                        "from Bd_FormApprovalFlowD " +
                        "where iFormApprovalFlowMid = " + flowMId +
                        "  and iUserId in (select iUserId " +
                        "                  from Bd_FormApprovalD_RoleUsers " +
                        "                  where Bd_FormApprovalD_RoleUsers.iAutoId in (" + ArrayUtil.join(jBoltTable.getDelete(), COMMA) + "))");

                if (delete > 1) {
                    formapprovaldRoleusersService.realDeleteByIds(jBoltTable.getDelete());
                }
            }

            List<FormapprovaldRoleusers> roleusers = formapprovaldRoleusersService.findByIformApprovaldRoleId(autoId);

            if (roleusers.size() > 0) {
                List<FormapprovaldRoleusers> roleusersList = new ArrayList<>();
                for (int j = 0, rank = 0; j < roleusers.size(); j++) {
                    FormapprovaldRoleusers approvaldRoleusers = roleusers.get(j);
                    approvaldRoleusers.setISeq(++rank);
                    roleusersList.add(approvaldRoleusers);
                }
                formapprovaldRoleusersService.batchUpdate(roleusersList, roleusersList.size());
            }

            return true;
        });

        return SUCCESS;
    }

    /**
     * 根据 ids 条件查数据
     * @param kv
     * @return
     */
    public List<FormApprovalD> findListBySid(Kv kv){
        return daoTemplate("formapprovald.findListBySid", kv).find();
    }
}
