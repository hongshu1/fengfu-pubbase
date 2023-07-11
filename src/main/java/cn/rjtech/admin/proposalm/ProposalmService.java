package cn.rjtech.admin.proposalm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.kit.DataPermissionKit;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.jbolt.core.util.ModelMap;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.barcodeencodingm.BarcodeencodingmService;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.project.ProjectService;
import cn.rjtech.admin.proposalattachment.ProposalAttachmentService;
import cn.rjtech.admin.proposalcategory.ProposalcategoryService;
import cn.rjtech.admin.proposald.ProposaldService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.*;
import cn.rjtech.model.momdata.Project;
import cn.rjtech.model.momdata.Proposald;
import cn.rjtech.model.momdata.Proposalm;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.RecordMap;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 禀议管理 Service
 *
 * @ClassName: ProposalmService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-09-22 11:56
 */
public class ProposalmService extends BaseService<Proposalm> implements IApprovalService{

    private final Proposalm dao = new Proposalm().dao();
    
    @Inject
    private UserService userService;
    @Inject
    private ProjectService projectService;
    @Inject
    private ProposaldService proposaldService;
    @Inject
    private DictionaryService dictionaryService;
    @Inject
    private DepartmentService departmentService;
    @Inject
    private ProposalcategoryService proposalcategoryService;
    @Inject
    private BarcodeencodingmService barcodeencodingmService;
    @Inject
    private ProposalAttachmentService proposalAttachmentService;
    
    @Override
    protected Proposalm dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
        User user = JBoltUserKit.getUser();

        //系统管理员 || 存在权限部门
        para.set("isSystemAdmin", user.getIsSystemAdmin());
        Page<Record> page = dbTemplate("proposalm.paginateAdminDatas", para.set("iorgid", getOrgId())).paginate(pageNumber, pageSize);
        if (CollUtil.isNotEmpty(page.getList())) {
            List<Record> purposeList = dictionaryService.getOptionsByTypeKey(DictionaryTypeKeyEnum.PURPOSE.getValue());
            ValidationUtils.notEmpty(purposeList, "缺少目的区分的字典数据");

            // 目的区分 字典数据
            RecordMap<String, String> purposeMap = new RecordMap<>(purposeList, "sn", "name");
            List<Record> proposalcategoryList = proposalcategoryService.findAllRecords();
            ValidationUtils.notEmpty(proposalcategoryList, "缺少类别区分数据");

            // 类型类别
            RecordMap<Long, String> categoryMap = new RecordMap<>(proposalcategoryList, "iautoid", "ccategoryname");
            
            ModelMap<Long, String> userMap = new ModelMap<>(userService.findAll(), "id", "name");

            for (Record row : page.getList()) {
            	String icategoryid = row.getStr("icategoryid");
            	String ccategoryname = "";
            	if(JBoltStringUtil.isNotBlank(icategoryid)){
            		String[] icategoryidList = icategoryid.split(",");
            		for (String id : icategoryidList) {
            			ccategoryname+=categoryMap.get(Long.parseLong(id))+",";
					}
            		ccategoryname = ccategoryname.substring(0,ccategoryname.lastIndexOf(","));
            	}
                row.set("cpurposename", purposeMap.get(row.getStr("cpurposesn")));
                row.set("ccategoryname", ccategoryname);
                row.set("cname", userMap.get(row.getLong("icreateby")));
                row.set("uname", userMap.get(row.getLong("iupdateby")));
            }
        }
        return page;
    }
	/**
	 * 移除行
	 */
	public Ret deleteByAjax() {
		return SUCCESS;
	}
    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        tx(() -> {
            for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
                long iAutoId = Long.parseLong(idStr);
                Proposalm dbProposalm = findById(iAutoId);
                ValidationUtils.notNull(dbProposalm, JBoltMsg.DATA_NOT_EXIST);
                DataPermissionKit.validateAccess(dbProposalm.getCDepCode());
                ValidationUtils.equals(dbProposalm.getIOrgId(), getOrgId(), ErrorMsg.ORG_ACCESS_DENIED);
                ValidationUtils.equals(dbProposalm.getIAuditStatus(), AuditStatusEnum.NOT_AUDIT.getValue(), "只能删除编辑状态的单据");

                // 删除附件
                proposalAttachmentService.deleteByIproposalmid(iAutoId);
                // 删除禀议明细
                ValidationUtils.isTrue(proposaldService.deleteByIproposalmid(iAutoId), ErrorMsg.DELETE_FAILED);

                ValidationUtils.isTrue(dbProposalm.delete(), JBoltMsg.FAIL);
            }

            return true;
        });

        // 添加日志
        // Proposalm proposalm = ret.getAs("data");
        // addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, proposalm.getName());
        return SUCCESS;
    }

    /**
     * 删除数据后执行的回调
     *
     * @param proposalm 要删除的model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Proposalm proposalm, Kv kv) {
        //addDeleteSystemLog(proposalm.getIAutoId(), JBoltUserKit.getUserId(),proposalm.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param proposalm 要删除的model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(Proposalm proposalm, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(proposalm, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param proposalm 要toggle的model
     * @param column    操作的哪一列
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(Proposalm proposalm, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(Proposalm proposalm, String column, Kv kv) {
        //addUpdateSystemLog(proposalm.getIAutoId(), JBoltUserKit.getUserId(), proposalm.getName(),"的字段["+column+"]值:"+proposalm.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param proposalm model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkInUse(Proposalm proposalm, Kv kv) {
        //这里用来覆盖 检测Proposalm是否被其它表引用
        return null;
    }

    public Page<Record> paginateDetails(Integer pageNumber, Integer pageSize, Kv para) {
    	para.set("iservicetype",ServiceTypeEnum.EXPENSE_BUDGET.getValue());
        Page<Record> recordPage = dbTemplate("proposalm.paginateDetails", para.set("iorgid", getOrgId())).paginate(pageNumber, pageSize);
        for (Record row : recordPage.getList()) {
        	row.set("cpurposename", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.PURPOSE.getValue(), row.getStr("cpurposesn")));
		}
        return recordPage;
    }
    public List<Record> findExportDatas(Kv para) {
    	para.set("iservicetype",ServiceTypeEnum.EXPENSE_BUDGET.getValue());
        List<Record> list = dbTemplate("proposalm.paginateDetails", para.set("iorgid", getOrgId())).find();
        for (Record row : list) {
        	row.set("cpurposename", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.PURPOSE.getValue(), row.getStr("cpurposesn")));
		}
        return list;
    }
    /**
     * 禀议明细导出
     */
    public JBoltExcel getExcelReport(Kv para) {
        return JBoltExcel.create().setSheets(
                JBoltExcelSheet.create("禀议明细导出数据")
                        .setHeaders(1,
                                JBoltExcelHeader.create("cproposalno", "禀议书编号", 16),
                                JBoltExcelHeader.create("cdepcode", "部门", 15),
                                JBoltExcelHeader.create("capplypersonname", "申请人", 15),
                                JBoltExcelHeader.create("dapplydate", "申请日期", 15),
                                JBoltExcelHeader.create("iauditstatus", "审批状态", 12),
                                JBoltExcelHeader.create("cprojectcode", "项目编码", 12),
                                JBoltExcelHeader.create("cprojectname", "项目名称", 12),
                                JBoltExcelHeader.create("cpurposename", "目的区分", 12),
                                JBoltExcelHeader.create("ccategoryname", "类别区分", 12),
                                JBoltExcelHeader.create("cbudgetno", "预算编号", 12),
                                JBoltExcelHeader.create("clowestsubjectcode", "明细科目", 10),
                                JBoltExcelHeader.create("budgetamount", "预算金额（含税）", 12),
                                JBoltExcelHeader.create("budgetamountnottaxinclu", "预算金额（不含税）", 12),
                                JBoltExcelHeader.create("sumamounttaxinclu", "已禀议金额（含税）", 12),
                                JBoltExcelHeader.create("sumamountnottaxinclu", "已禀议金额（不含税）", 12),
                                JBoltExcelHeader.create("citemname", "细项名称", 12),
                                JBoltExcelHeader.create("iquantity", "数量", 12),
                                JBoltExcelHeader.create("inatunitprice", "单位", 12),
                                JBoltExcelHeader.create("iamounttaxinclu", "申请金额（含税）", 12),
                                JBoltExcelHeader.create("iamountnottaxinclu", "申请金额（不含税）", 12),
                                JBoltExcelHeader.create("cvencode	", "预定供应商", 12),
                                JBoltExcelHeader.create("ddemanddate", "需求日", 12),
                                JBoltExcelHeader.create("cbudgetdepcode", "预算对应部门", 12))
                        .setRecordDatas(2, findExportDatas(para)));
    }
    public Ret saveTableSubmit(JBoltTableMulti tableMulti, User user, Date now) {
        JBoltTable proposalTable = tableMulti.getJBoltTable("proposalds");
        Proposalm proposalm = proposalTable.getFormModel(Proposalm.class, "proposalm");
        ValidationUtils.notBlank(proposalm.getCDepCode(), "缺少部门参数");
        DataPermissionKit.validateAccess(proposalm.getCDepCode());
        ValidationUtils.notNull(proposalm, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(proposalm.getDApplyDate(), "申请日期不能为空");
        ValidationUtils.notBlank(proposalm.getCApplyPersonCode(), "申请人不能为空");
        ValidationUtils.notBlank(proposalm.getCProjectName(), "项目名称不能为空");
        ValidationUtils.notNull(proposalm.getCPurposeSn(), "缺少目的区分");
        ValidationUtils.notNull(proposalm.getICategoryId(), "缺少类别区分");
        ValidationUtils.notNull(proposalm.getIsScheduled(), "缺少事业计划");
        JBoltTable attachmentsTable = tableMulti.getJBoltTable("attachments");
        if (null == proposalm.getIAutoId()) {
            ValidationUtils.notEmpty(proposalTable.getSaveRecordList(), "禀议书项目不能为空");
            tx(() -> {
                doSaveTableSubmit(proposalm, proposalTable.getSaveRecordList(), attachmentsTable.getSaveRecordList(), user.getId(), now);
                return true;
            });

            return successWithData(proposalm.keep("iautoid"));
        } else {
            Proposalm dbProposalm = findById(proposalm.getIAutoId());
            ValidationUtils.notNull(dbProposalm, "禀议书不存在");
            ValidationUtils.equals(AuditStatusEnum.NOT_AUDIT.getValue(), dbProposalm.getIAuditStatus(), "当前单据非可编辑状态");
            // 更新主表
            tx(() -> {
                dbProposalm._setAttrs(proposalm.keep("iautoid","cdepcode", "capplypersoncode", "capplypersonname", "cprojectcode", "cprojectname", "cpurposesn", "icategoryid", "isscheduled", "cdesc", "inatmoney", "inatsum", "ibudgetmoney", "ibudgetsum", "isupplementalmoney", "isupplementalsum", "csupplementaldesc"))
                        .setIUpdateBy(user.getId())
                        .setDUpdateTime(now);

                ValidationUtils.isTrue(dbProposalm.update(), ErrorMsg.UPDATE_FAILED);

                // 保存明细
                doSaveProposalds(proposalm.getIAutoId(), proposalTable.getSaveRecordList());
                // 修改明细
                doUpdateProposalds(proposalTable.getUpdateRecordList());
                // 删除明细
                doDeleteProposalds(proposalTable.getDelete());

                // 校验明细必须要有内容
                ValidationUtils.isTrue(proposaldService.hasRecords(proposalm.getIAutoId()), "禀议项目不能为空");

                // 保存禀议书附件
                doSaveProposalAttachment(proposalm.getIAutoId(), attachmentsTable.getSaveRecordList());
                // 删除禀议书附件
                doDeleteAttachments(attachmentsTable.getDelete());

                return true;
            });
        }

        return successWithData(proposalm.keep("iautoid"));
    }

    private void doDeleteProposalds(Object[] delete) {
        if (ArrayUtil.isNotEmpty(delete)) {
            proposaldService.deleteByMultiIds(delete);
        }
    }

    private void doDeleteAttachments(Object[] delete) {
        if (ArrayUtil.isNotEmpty(delete)) {
            proposalAttachmentService.deleteByMultiIds(delete);
        }
    }
    /**
     * 生成禀议书NO.和追加禀议书No.
     * */
    private String genProposalNo(Kv para){
    	String cProposalNo = null;
    	Long iautoid = para.getLong("iautoid");
    	Proposalm proposalm = findById(iautoid);
    	ValidationUtils.notNull(proposalm, "禀议书保存失败,禀议书No.未能生成,请检查!");
    	//获取追加禀议No:获取初始禀议书No,后面拼接流水号
    	if(proposalm.getIsSupplemental()){
    		Long ifirstsourceproposalid = proposalm.getIFirstSourceProposalId();
    		ValidationUtils.notNull(ifirstsourceproposalid, "初始禀议书ID为空,生成追加禀议书No.失败!");
    		Proposalm firstProposalm = findById(ifirstsourceproposalid);
    		ValidationUtils.notNull(firstProposalm, "初始禀议书为空,生成追加禀议书No.失败!");
    		String cfirstsourceproposalno = firstProposalm.getCProposalNo();
    		cProposalNo = BillNoUtils.genProposalSystemNo(getOrgId(), cfirstsourceproposalno + "-", false, 2);
    	}else{
    		cProposalNo = barcodeencodingmService.genCode(Kv.by("iautoid", iautoid), ItemEnum.PROPOSAL.getValue());
    	}
    	ValidationUtils.notBlank(cProposalNo, "禀议书No.生成失败!");
    	return cProposalNo;
    }

    private void doSaveTableSubmit(Proposalm proposalm, List<Record> proposaldSaveList, List<Record> attachmentSaveList, Long userId, Date now) {
        proposalm.setIOrgId(getOrgId());
        proposalm.setCOrgCode(getOrgCode());
        proposalm.setCProposalNo("");
        proposalm.setIAuditStatus(AuditStatusEnum.NOT_AUDIT.getValue());
        proposalm.setIEffectiveStatus(EffectiveStatusEnum.INVAILD.getValue());
        proposalm.setDCreateTime(now);
        proposalm.setDUpdateTime(now);
        proposalm.setICreateBy(userId);
        proposalm.setIUpdateBy(userId);
        Long iautoid = JBoltSnowflakeKit.me.nextId();
        proposalm.setIAutoId(iautoid);
        // 累计申请金额（无税）
        proposalm.setITotalMoney(proposalm.getINatMoney());
        // 累计申请金额（含税）
        proposalm.setITotalSum(proposalm.getINatSum());
        //追加禀议需要结转累计申购金额
        if(!proposalm.getIsSupplemental()){
        	proposalm.setIFirstSourceProposalId(proposalm.getIAutoId());
        }
        // 保存主表
        ValidationUtils.isTrue(proposalm.save(), ErrorMsg.SAVE_FAILED);
        proposalm.setCProposalNo(genProposalNo(Kv.by("iautoid",proposalm.getIAutoId())));
        ValidationUtils.isTrue(proposalm.update(), ErrorMsg.UPDATE_FAILED);
        // 保存明细
        doSaveProposalds(proposalm.getIAutoId(), proposaldSaveList);
        // 保存附件明细
        doSaveProposalAttachment(proposalm.getIAutoId(), attachmentSaveList);
    }

    private void doSaveProposalds(Long iproposalmid, List<Record> saveRecordList) {
        if (CollUtil.isNotEmpty(saveRecordList)) {
        	Set<String> columnNames = TableMapping.me().getTable(Proposald.class).getColumnNameSet();
            for (int i=0; i < saveRecordList.size(); i++) {
            	Record row = saveRecordList.get(i);
            	Integer isubitem = row.getInt("isubitem");
            	if(isubitem == IsEnableEnum.NO.getValue()){
            		row.remove("citemname","cunit","ccurrency","cvencode","ddemanddate","cbudgetdepcode");
            	}
            	//Long iprojectcardid =  projectCardService.findIdByCode(row.getStr("cbudgetno"));
                row.keep("isourceid", "itype", "citemname", "iquantity", "cvencode", "ddemanddate", "cbudgetdepcode", "cmemo", "ccurrency", "itaxrate", "nflat", "iunitprice", "itax", "imoney", "isum", "inatunitprice", "inattax", "inatsum", "inatmoney", "cunit","cbudgetno", "ibudgetmoney", "ibudgetsum","isubitem", "isourcetype","iprojectcardid")
                        .set("iautoid", JBoltSnowflakeKit.me.nextId())
                        .set("iproposalmid", iproposalmid)
                        .set("istatus", GenerateStatusEnum.N.getValue());
                if (i == 0) {
                    // 避免保存不到所有字段的问题
                    for (String columnName : columnNames) {
                        if (null == row.get(columnName)) {
                            row.set(columnName, null);
                        }
                    }
                }
            }
            proposaldService.batchSaveRecords(saveRecordList, 500);
        }
    }

    private void doSaveProposalAttachment(Long iproposalmid, List<Record> attachmentSaveList) {
        if (CollUtil.isNotEmpty(attachmentSaveList)) {
            for (Record row : attachmentSaveList) {
                row.set("iautoid", JBoltSnowflakeKit.me.nextId())
                        .set("iproposalmid", iproposalmid);
            }
            proposalAttachmentService.batchSaveRecords(attachmentSaveList, 500);
        }
    }

    private void doUpdateProposalds(List<Record> updateRecordList) {
        if (CollUtil.isNotEmpty(updateRecordList)) {
        	Set<String> columnNames = TableMapping.me().getTable(Proposald.class).getColumnNameSet();
            for (int i=0; i<updateRecordList.size(); i++) {
            	Record row = updateRecordList.get(i);
            	Integer isubitem = row.getInt("isubitem");
            	if(isubitem == IsEnableEnum.NO.getValue()){
            		row.remove("citemname","cunit","ccurrency","cvencode","ddemanddate","cbudgetdepcode");
            	}
            	row.keep("iautoid","iproposalmid","istatus","isourceid", "itype", "citemname", "iquantity", "cvencode", "ddemanddate", "cbudgetdepcode", "cmemo", "ccurrency", "itaxrate", "nflat", "iunitprice", "itax", "imoney", "isum", "inatunitprice", "inattax", "inatsum", "inatmoney", "cunit","cbudgetno", "ibudgetmoney", "ibudgetsum","isubitem", "isourcetype","iprojectcardid");
                if (i == 0) {
                    // 避免保存不到所有字段的问题
                    for (String columnName : columnNames) {
                        if (null == row.get(columnName)) {
                            row.set(columnName, null);
                        }
                    }
                }
            }
            proposaldService.batchUpdateRecords(updateRecordList, 500);
        }
    }

    /**
     * 处理审批不通过的其他业务操作，如有异常处理返回错误信息
     */
    @Override
    public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
        return null;
    }
	
    /**
     * 实现反审之后的其他业务操作, 如有异常返回错误信息
     *
     * @param formAutoId 单据ID
     * @param isFirst    是否为审批的第一个节点
     * @param isLast     是否为审批的最后一个节点
     */
    @Override
    public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
        // 反审回第一个节点，回退状态为“已保存”
        if (isFirst) {
       
        }
        // 最后一步通过的，反审，回退状态为“待审核”
        if (isLast) {
           
        }
        return null;
    }

    public Record getDetailsMoney(Kv kv) {
        return dbTemplate("proposalm.getDetailsMoney", kv).findFirst();
    }

    public Ret withdraw(Long iautoid) {
        Proposalm proposalm = findById(iautoid);
        ValidationUtils.notNull(proposalm, "禀议书不存在");
        ValidationUtils.equals(proposalm.getIAuditStatus(), AuditStatusEnum.AWAIT_AUDIT.getValue(), "非待审核状态，禁止操作");
        //ValidationUtils.isTrue(verifyprogressService.notAuditting(VeriProgressObjTypeEnum.PROPOSAL.getValue(), iautoid), "禀议书已存在审核记录，请进行弃审操作");
        tx(() -> {

            // 清理审批数据
            //verifyprogressService.deleteByIobjectid(String.valueOf(iautoid), VeriProgressObjTypeEnum.PROPOSAL.getValue());
            // 修改审批状态
            ValidationUtils.isTrue(updateStatusEditting(iautoid), "撤销提审失败");

            return true;
        });

        return SUCCESS;
    }

    private boolean updateStatusEditting(long iautoid) {
        Okv para = Okv.by("iautoid", iautoid)
                .set("auditting", AuditStatusEnum.AWAIT_AUDIT.getValue())
                .set("editting", AuditStatusEnum.NOT_AUDIT.getValue());

        return dbTemplate("proposalm.updateStatusEditting", para).update() > 0;
    }

    public boolean updateAuditted(long iautoid, Date now) {
        Okv para = Okv.by("iautoid", iautoid)
                .set("auditting", AuditStatusEnum.AWAIT_AUDIT.getValue())
                .set("approved", AuditStatusEnum.APPROVED.getValue())
                .set("now", now);

        return dbTemplate("proposalm.updateAuditted", para).update() > 0;
    }

    public Ret effect(Long iproposalmid, Long userId, Date now) {
        tx(() -> {
            Proposalm proposalm = findById(iproposalmid);
            ValidationUtils.notNull(proposalm, "禀议书不存在");
            ValidationUtils.isTrue(proposalm.getIOrgId().equals(getOrgId()), ErrorMsg.ORG_ACCESS_DENIED);
            ValidationUtils.equals(proposalm.getIAuditStatus(), AuditStatusEnum.APPROVED.getValue(), "审核未通过");
            ValidationUtils.equals(proposalm.getIEffectiveStatus(), EffectiveStatusEnum.INVAILD.getValue(), "请选择未生效禀议书进行生效操作");
            proposalm.setIEffectiveStatus(EffectiveStatusEnum.EFFECTIVED.getValue());
            proposalm.setDEffectiveTime(now);
            proposalm.setDUpdateTime(now);
            proposalm.setIUpdateBy(JBoltUserKit.getUserId());
            //追加禀议用原禀议书的项目编码，参照费用/投资计划新增的禀议书需要生成项目档案
            if(JBoltStringUtil.isBlank(proposalm.getCProjectCode())){
	            // 生成项目档案
	            Project project = new Project();
	            project.setIorgid(proposalm.getIOrgId());
	            project.setCorgcode(proposalm.getCOrgCode());
	            project.setCprojectcode(barcodeencodingmService.genCode(Kv.by("iautoid", iproposalmid), ItemEnum.PROJECT.getValue()));
	            project.setCprojectname(proposalm.getCProjectName());
	            project.setCdepcode(proposalm.getCDepCode());
	            project.setDcreatetime(now);
	            project.setIcreateby(proposalm.getICreateBy());
	            ValidationUtils.isTrue(project.save(), "生成项目档案失败");
	            proposalm.setCProjectCode(project.getCprojectcode());
            }
            ValidationUtils.isTrue(proposalm.update(), ErrorMsg.UPDATE_FAILED);
            //追加禀议生效后需要将原禀议书失效
            if(proposalm.getIsSupplemental()){
            	Proposalm sourceProposalm = findById(proposalm.getISourceProposalId());
            	ValidationUtils.notNull(sourceProposalm, "原禀议书为空");
            	ValidationUtils.isTrue(sourceProposalm.getIEffectiveStatus() == EffectiveStatusEnum.EFFECTIVED.getValue(), "原禀议书未生效!");
            	sourceProposalm.setIEffectiveStatus(EffectiveStatusEnum.EXPIRED.getValue());
            	sourceProposalm.setDUpdateTime(now);
            	sourceProposalm.setIUpdateBy(JBoltUserKit.getUserId());
            	ValidationUtils.isTrue(sourceProposalm.update(), "追加禀议书生效操作将原禀议书失效失败!");
            }
            return true;
        });
        return SUCCESS;
    }

    public Ret printData(Long iautoid) {
        Record proposalm = findRecordById(iautoid);
        // 处理细项数据
        List<Record> proposalds = proposaldService.findByiProposalMid(iautoid);
        BigDecimal sumInatMoney = BigDecimal.ZERO;
        BigDecimal sumInat = BigDecimal.ZERO;
        for (int i = 0; i < proposalds.size(); i++) {
            Record record = proposalds.get(i);
            sumInatMoney = sumInatMoney.add(record.getBigDecimal("inatmoney"));
            sumInat = sumInat.add(record.getBigDecimal("inatsum"));
            proposalm.set("index" + i, i + 1);
            proposalm.set("itypename" + i, ProposaldTypeEnum.toEnum((record.getInt("itype"))).getText());
            proposalm.set("cbudgetno" + i, record.get("cbudgetno"));
            proposalm.set("clowestsubjectname" + i, record.get("clowestsubjectname"));
            proposalm.set("ibudgetmoney" + i, record.get("ibudgetmoney"));
            proposalm.set("citemname" + i, record.get("citemname"));
            proposalm.set("iquantity" + i, record.get("iquantity"));
            proposalm.set("cunit" + i, record.get("cunit"));
            proposalm.set("ccurrency" + i, record.get("ccurrency"));
            proposalm.set("iunitprice" + i, record.get("iunitprice"));
            proposalm.set("nflat" + i, record.get("nflat"));
            proposalm.set("itaxrate" + i, record.get("itaxrate"));
            proposalm.set("itax" + i, record.get("itax"));
            proposalm.set("isum" + i, record.get("isum"));
            proposalm.set("inatunitprice" + i, record.get("inatunitprice"));
            proposalm.set("inattax" + i, record.get("inattax"));
            proposalm.set("inatmoney" + i, record.get("inatmoney"));
            proposalm.set("inatsum" + i, record.get("inatsum"));
            proposalm.set("cvenname" + i, record.get("cvenname"));
            proposalm.set("ddemanddate" + i, record.get("ddemanddate"));
            proposalm.set("cbudgetdepname" + i, record.get("cbudgetdepname"));
        }

        proposalm.set("suminatmoney", sumInatMoney);
        proposalm.set("suminat", sumInat);
        proposalm.set("cdepname", departmentService.getCdepName(proposalm.getStr("cdepcode")));
        return successWithData(Kv.by("proposalm", proposalm));
    }

    /**
     * 参照费用预算/投资计划数据源
     * @param pageNumber
     * @param pageSize
     * @param kv
     * @return
     */
    public Page<Record> paginateMdatas(Integer pageNumber, Integer pageSize, Kv kv) {
        User user = JBoltUserKit.getUser();
        kv.set("iorgid", getOrgId())
                .set("isSystemAdmin", user.getIsSystemAdmin());

        Page<Record> page = dbTemplate("proposalm.paginateMdatas", kv).paginate(pageNumber, pageSize);

        if (CollUtil.isNotEmpty(page.getList())) {
            for (Record row : page.getList()) {
                row.set("cdepname", departmentService.getCdepName(row.getStr("cdepcode")));
            }
        }
        return page;
    }

	public String isSameCdepCode(String iautoids) {
		List<Record> list = dbTemplate("proposalm.findExpenseBudgetOrInvestmentCdepcode",Kv.by("iautoids",iautoids)).find();
		ValidationUtils.isTrue(CollUtil.isNotEmpty(list) && list.size() == 1, "选定的部门不同");
		return list.get(0).getStr("cdepcode");
	}
	/**
	 * 查询费用预算项目是否被禀议
	 * */
	public List<Record> findProposalDatasByExpenseId(Long iexpenseid) {
		List<Record> list = dbTemplate("proposalm.findProposalDatasByExpenseId",Kv.by("iexpenseid", iexpenseid)).find();
		return list;
	}
	/**
	 * 查询投资计划项目是否被禀议
	 * */
	public List<Record> findProposalDatasByPlanId(Long iplanid) {
		return dbTemplate("proposalm.findProposalDatasByPlanId",Kv.by("iplanid", iplanid)).find();
	}

	public List<Proposalm> findByProjectCode(String cprojectcode) {
		return find(selectSql().eq("cprojectcode", cprojectcode));
	}
	/**
	 * 编码规则获取单据内容：部门英文名称，单据年度等
	 * */
	public String getItemOrderContent(Long iautoid,String cprojectcode) {
		String str = "";
    	Proposalm proposalm = findById(iautoid);
    	ValidationUtils.notNull(proposalm, "禀议书数据不存在,获取部门英文名称失败!");
		switch (BarCodeEnum.toEnum(cprojectcode)) {
	        case DEPT:
	        	Record record = departmentService.findByCdepcode(getOrgId(),proposalm.getCDepCode()).toRecord();
	    		String cdepnameen = record.getStr("cdepnameen");
	    		ValidationUtils.notBlank(cdepnameen, record.getStr("cdepname")+"部门的英文名称为空!");
	    		str = cdepnameen;
	            break;  
	        case ORDERYEAR:
	        	ValidationUtils.error( "禀议书无法获取"+BarCodeEnum.ORDERYEAR.getText()+",请检查编码规则!");
	            break;
	        default:
                break;
		}
		return str;
	}
	//校验是否存在未生效的申购单:默认不存在，可以继续做追加禀议
	public void isExistsInvaildPurchase(Long iproposalmid) {
		Kv para = Kv.by("iproposalmid",iproposalmid);
		para.set("ieffectivestatus",EffectiveStatusEnum.INVAILD.getValue());
		ValidationUtils.notNull(iproposalmid, "禀议书ID为空");
		List<Record> list = dbTemplate("proposalm.findInvalidPurchase",para).find();
		ValidationUtils.isTrue(CollUtil.isEmpty(list), "存在未生效的申购单，请处理后再追加禀议!");
	}

	public Ret validateProposalMoneyIsExceed(Kv para) {
		BigDecimal imoney = para.getBigDecimal("imoney");
		BigDecimal ibudgetmoney = para.getBigDecimal("ibudgetmoney");
		String iprojectcardids = para.getStr("iprojectcardids");
		ValidationUtils.notNull(imoney, "原币无税金额为空,校验是否超预算失败!");
		ValidationUtils.notNull(ibudgetmoney, "预算总额(无税)为空,校验是否超预算失败!");
		ValidationUtils.notBlank(iprojectcardids, "预算项目卡片ID为空,校验是否超预算失败!");
		para.set("ieffectivestatus",EffectiveStatusEnum.EXPIRED.getValue());
		//根据项目卡片查询预算项目已禀议的金额合计
		BigDecimal iAreadyProposalMoney = dbTemplate("proposalm.findProposalMoneySumByProjectIds",para).queryBigDecimal();
		iAreadyProposalMoney = iAreadyProposalMoney == null ? BigDecimal.ZERO : iAreadyProposalMoney;
		if(ibudgetmoney.compareTo(imoney.add(iAreadyProposalMoney)) == -1 ) 
			return fail("累计禀议金额超预算!"); 
		return SUCCESS;
	}

	@Override
	public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String preSubmitFunc(long formAutoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String postSubmitFunc(long formAutoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String postWithdrawFunc(long formAutoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String withdrawFromAuditting(long formAutoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String preWithdrawFromAuditted(long formAutoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String postWithdrawFromAuditted(long formAutoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String postBatchApprove(List<Long> formAutoIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String postBatchReject(List<Long> formAutoIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String postBatchBackout(List<Long> formAutoIds) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 失效：1.存在下游单据不能失效
	 * 	2.单据状态从已生效变更到未生效
	 *  3.删除项目档案
	 * */
	public Ret uneffect(Long iproposalmid) {
		tx(()->{
			Proposalm proposalm = findById(iproposalmid);
			Integer ieffectivestatus = proposalm.getIEffectiveStatus();
			ValidationUtils.isTrue(ieffectivestatus == EffectiveStatusEnum.EFFECTIVED.getValue(), "请操作已生效的单据!");
			ValidationUtils.isTrue(!isExistsPurchaseDatas(iproposalmid), "存在申购数据，不能失效");
			Project project = projectService.findByProjectCode(proposalm.getCProjectCode(), proposalm.getCDepCode());
			proposalm.setIEffectiveStatus(EffectiveStatusEnum.INVAILD.getValue());
			proposalm.setCProjectCode(null);
			proposalm.setIUpdateBy(JBoltUserKit.getUserId());
			proposalm.setDUpdateTime(new Date());
			ValidationUtils.isTrue(proposalm.update(), ErrorMsg.UPDATE_FAILED);
			ValidationUtils.isTrue(project.delete(), ErrorMsg.DELETE_FAILED);
			return true;
		});
		return SUCCESS;
	}
	private boolean isExistsPurchaseDatas(Long iproposalmid){
		int count = dbTemplate("proposalm.isExistsPurchaseDatas",Kv.by("iproposalmid", iproposalmid)).queryInt();
		return count > 0 ? true : false;
	}
}