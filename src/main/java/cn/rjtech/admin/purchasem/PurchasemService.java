package cn.rjtech.admin.purchasem;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjUtil;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt._admin.globalconfig.GlobalConfigService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.kit.DataPermissionKit;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.model.User;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;
import cn.jbolt.core.util.JBoltArrayUtil;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.barcodeencodingm.BarcodeencodingmService;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.depref.DepRefService;
import cn.rjtech.admin.exch.ExchService;
import cn.rjtech.admin.expensebudget.ExpenseBudgetService;
import cn.rjtech.admin.formapproval.FormApprovalService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventoryclass.InventoryClassService;
import cn.rjtech.admin.period.PeriodService;
import cn.rjtech.admin.proposalcategory.ProposalcategoryService;
import cn.rjtech.admin.purchaseattachment.PurchaseAttachmentService;
import cn.rjtech.admin.purchased.PurchasedService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.admin.vendorclass.VendorClassService;
import cn.rjtech.config.AppConfig;
import cn.rjtech.constants.Constants;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.*;
import cn.rjtech.model.momdata.ExpenseBudget;
import cn.rjtech.model.momdata.Period;
import cn.rjtech.model.momdata.Purchased;
import cn.rjtech.model.momdata.Purchasem;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.u8.api.purchaseapp.PurchaseAppApi;
import cn.rjtech.util.RecordMap;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static cn.hutool.core.text.StrPool.COMMA;


/**
 * 申购单管理 Service
 *
 * @ClassName: PurchasemService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-09-14 14:52
 */
public class PurchasemService extends BaseService<Purchasem> implements IApprovalService{

    private final Purchasem dao = new Purchasem().dao();

    @Override
    protected Purchasem dao() {
        return dao;
    }

    @Inject
    private ExchService exchService;
    @Inject
    private DepRefService depRefService;
    @Inject
    private PeriodService periodService;
    @Inject
    private InventoryService inventoryService;
    @Inject
    private PurchasedService purchasedService;
    @Inject
    private VendorService vendorRecordService;
    @Inject
    private DictionaryService dictionaryService;
    @Inject
    private DepartmentService departmentService;
    @Inject
    private VendorClassService vendorclassService;
    @Inject
    private GlobalConfigService globalConfigService;
    @Inject
    private FormApprovalService formApprovalService;
    @Inject
    private ExpenseBudgetService expenseBudgetService;
    @Inject
    private InventoryClassService inventoryClassService;
    @Inject
    private BarcodeencodingmService barcodeencodingmService;
    @Inject
    private ProposalcategoryService proposalcategoryService;
    @Inject
	private PurchaseAttachmentService purchaseAttachmentService;
    
    /**
     * 后台管理分页查询
     */
    public List<Record> paginateAdminDatas(Kv kv) {
    	kv.set("iorgid",getOrgId());
        Page<Record> pages = dbTemplate("purchasem.paginateAdminDatas", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
        ValidationUtils.notNull(pages, JBoltMsg.DATA_NOT_EXIST);
        for (Record row : pages.getList()) {
        	row.set("createname", JBoltUserCache.me.getName(row.getLong("icreateby")));
		}
        return pages.getList(); 
    }

    /**
     * 保存
     *
     * @param purchasem
     * @return
     */
    public Ret save(Purchasem purchasem) {
        if (purchasem == null || isOk(purchasem.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        tx(() -> {
            // ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
            ValidationUtils.isTrue(purchasem.save(), ErrorMsg.SAVE_FAILED);


            // TODO 其他业务代码实现

            return true;
        });

        // 添加日志
        // addSaveSystemLog(purchasem.getIAutoId(), JBoltUserKit.getUserId(), purchasem.getName());
        return SUCCESS;
    }

    /**
     * 更新
     */
    public Ret update(Purchasem purchasem) {
        if (purchasem == null || notOk(purchasem.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        tx(() -> {
            // 更新时需要判断数据存在
            Purchasem dbPurchasem = findById(purchasem.getIAutoId());
            ValidationUtils.notNull(dbPurchasem, JBoltMsg.DATA_NOT_EXIST);

            // TODO 其他业务代码实现
            // ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);

            ValidationUtils.isTrue(purchasem.update(), ErrorMsg.UPDATE_FAILED);

            return true;
        });

        //添加日志
        //addUpdateSystemLog(purchasem.getIAutoId(), JBoltUserKit.getUserId(), purchasem.getName());
        return SUCCESS;
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        tx(() -> {
            for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
                long iAutoId = Long.parseLong(idStr);
                Purchasem dbPurchasem = findById(iAutoId);
                ValidationUtils.notNull(dbPurchasem, JBoltMsg.DATA_NOT_EXIST);
                purchasedService.deleteBy(Okv.by("ipurchaseid", dbPurchasem.getIAutoId()));
                purchaseAttachmentService.deleteByPurchaseId(dbPurchasem.getIAutoId());
                ValidationUtils.isTrue(dbPurchasem.delete(), JBoltMsg.FAIL);
            }

            return true;
        });

        // 添加日志
        // Purchasem purchasem = ret.getAs("data");
        // addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, purchasem.getName());
        return SUCCESS;
    }
    
    
    /**
     * 删除数据后执行的回调
     *
     * @param purchasem 要删除的model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Purchasem purchasem, Kv kv) {
        //addDeleteSystemLog(purchasem.getIAutoId(), JBoltUserKit.getUserId(),purchasem.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param purchasem 要删除的model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(Purchasem purchasem, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(purchasem, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     *
     * @return
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换ispurchase属性
     */
    public Ret toggleIspurchase(Long id) {
        return toggleBoolean(id, "isPurchase");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param purchasem 要toggle的model
     * @param column    操作的哪一列
     * @param kv        携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkCanToggle(Purchasem purchasem, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(Purchasem purchasem, String column, Kv kv) {
        //addUpdateSystemLog(purchasem.getIAutoId(), JBoltUserKit.getUserId(), purchasem.getName(),"的字段["+column+"]值:"+purchasem.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param purchasem model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkInUse(Purchasem purchasem, Kv kv) {
        //这里用来覆盖 检测Purchasem是否被其它表引用
        return null;
    }
    /**
     * 获取申购单编码
     * */
    private String genPurchasNo(Kv para){
    	return barcodeencodingmService.genCode(para, ItemEnum.PURCHASE.getValue());
    }
    /**
     * 参照禀议书-新增提交表单
     * @param jBoltTable
     * @return
     */
    public Ret saveTableSubmit(JBoltTableMulti tableMulti) {
        ValidationUtils.notNull(tableMulti, JBoltMsg.PARAM_ERROR);
        JBoltTable purchaseTable = tableMulti.getJBoltTable("purchaseds");
        Purchasem purchasem = purchaseTable.getFormModel(Purchasem.class, "purchasem");
        DataPermissionKit.validateAccess(purchasem.getCDepCode());
        ValidationUtils.notNull(purchasem, JBoltMsg.PARAM_ERROR);
        JBoltTable attachmentsTable = tableMulti.getJBoltTable("attachments");
        Boolean flg = true;
        flg = tx(() -> {
            // 申购主表数据处理
            if (notOk(purchasem.getIAutoId())) {
                purchasem.setIOrgId(getOrgId());
                purchasem.setCOrgCode(getOrgCode());
                purchasem.setIAuditStatus(AuditStatusEnum.NOT_AUDIT.getValue());
                purchasem.setIStatus(PurchaseStatusEnum.NOPUSH.getValue());
                purchasem.setDCreateTime(new Date());
                purchasem.setICreateBy(JBoltUserKit.getUserId());
                purchasem.setCPurchaseNo("");
                purchasem.setIRefType(PurchaseRefTypeEnum.PROPOSAL.getValue());
                ValidationUtils.isTrue(purchasem.save(), ErrorMsg.SAVE_FAILED);
                purchasem.setCPurchaseNo(genPurchasNo(Kv.by("iautoid", purchasem.getIAutoId())));
            } else if (isOk(purchasem.getIAutoId())) {
                purchasem.setDUpdateTime(new Date());
                purchasem.setIUpdateBy(JBoltUserKit.getUserId());
            }
            ValidationUtils.isTrue(purchasem.update(), ErrorMsg.UPDATE_FAILED);
            
            //校验申购单本次累计申购金额是否超出了禀议金额的10%或者500元
            if(!validatePurchaseMoneyIsExceed(purchasem)) return false;
            // 申购细表数据处理
            if (purchaseTable.saveIsNotBlank()) {
                List<Record> saveRecordList = purchaseTable.getSaveRecordList();
                saveRecordList.forEach(item->{
                	item.remove("cbudgetno","cvenname","ibudgetmoney");
                	item.set("iautoid", JBoltSnowflakeKit.me.nextId());
                    item.set("ipurchaseid", purchasem.getIAutoId());
                });
                purchasedService.batchSaveRecords(saveRecordList);
            }
            if (purchaseTable.updateIsNotBlank()) {
                List<Purchased> updatePurchaseds = purchaseTable.getUpdateModelList(Purchased.class);
                purchasedService.batchUpdate(updatePurchaseds);
            }
            if (purchaseTable.deleteIsNotBlank()) {
                purchasedService.deleteByIds(purchaseTable.getDelete());
            }
            //保存申购单附件
            saveAttachmentsTable(attachmentsTable,purchasem);
            return true;
        });
        if(flg) return successWithData(purchasem.keep("iautoid"));
        else return FAIL;
    }
    private void saveAttachmentsTable(JBoltTable attachmentsTable,Purchasem purchasem){
    	Long ipurchasemid = purchasem.getIAutoId();
    	ValidationUtils.notNull(ipurchasemid, "申购单ID为空,保存附件失败!");
    	//新增附件
    	List<Record> attachmentSaveList = attachmentsTable.getSaveRecordList();
        if (CollUtil.isNotEmpty(attachmentSaveList)) {
            for (Record row : attachmentSaveList) {
                row.set("iautoid", JBoltSnowflakeKit.me.nextId())
                        .set("ipurchasemid", ipurchasemid);
            }
            purchaseAttachmentService.batchSaveRecords(attachmentSaveList, 500);
        }
        //删除附件
        Object[] deleteIds = attachmentsTable.getDelete();
        if (ArrayUtil.isNotEmpty(deleteIds)) {
        	purchaseAttachmentService.deleteByIds(deleteIds);
        }
    }
	/**
	 * 校验申购单本次累计申购金额是否超出了禀议金额的10%或者500元
	 * */
    private Boolean validatePurchaseMoneyIsExceed(Purchasem purchasem) {
		Long ifirstsourceproposalid = purchasem.getIFirstSourceProposalId();
		ValidationUtils.notNull(ifirstsourceproposalid, "校验累计申购金额是否超出限制时，禀议书Id为空");
		Record moneyRc = getMoney(Kv.by("ifirstsourceproposalid", purchasem.getIFirstSourceProposalId()));
		BigDecimal ialreadypurchasenatmoney = moneyRc.getBigDecimal("ialreadypurchasenatmoney"); //禀议书已申购金额(本币无税)
		BigDecimal iproposalnatmoney = moneyRc.getBigDecimal("iproposalnatmoney");//禀议金额(本币无税)
		BigDecimal purchaseRatio = null;
		BigDecimal purchaseAmountRatio = null;
		try{
			purchaseRatio = new BigDecimal(globalConfigService.getConfigValue("purchase_ratio"));
			purchaseAmountRatio = new BigDecimal(globalConfigService.getConfigValue("purchase_amount_ratio"));
		}catch(Exception e){
			ValidationUtils.error( "获取申购金额的金额限制参数出错!");
		}
		if(purchaseRatio == null || purchaseAmountRatio == null) ValidationUtils.error( "申购金额的金额限制参数未配置!");
		//全局参数配置的限制比例*禀议金额（本币不含税） <= 全局参照配置的限制金额时，以限制比例*禀议金额（本币不含税）作为限制金额，否则直接取全局参数中的限制金额
		BigDecimal iExceedAmount = iproposalnatmoney.multiply(purchaseRatio);
		if(iExceedAmount.compareTo(purchaseAmountRatio) == 1) iExceedAmount = purchaseAmountRatio;
		if(ialreadypurchasenatmoney.subtract(iproposalnatmoney).compareTo(iExceedAmount) == 1) return false;
		return true;
		//ValidationUtils.error( "申购金额已超禀议金额10%或500,请追加禀议!");
	}
	/**
     * 详情
     *
     * @param kv
     * @return
     */
    public Record details(Kv kv) {
        return dbTemplate("purchasem.findPurchasemDetails", kv).findFirst();
    }

    /**
     * 获取金额信息
     *
     * @param kv
     * @return
     */
    public Record getMoney(Kv para) {
    	para.set("ieffectivestatus",EffectiveStatusEnum.EFFECTIVED.getValue());
    	return dbTemplate("purchasem.getMoney",para).findFirst();
    }

    /**
     * 根据主键获取申购单
     *
     * @param iautoid
     * @return
     */
    public Record getPurchasemByIautoid(Long iautoid) {
        Record record = details(Kv.by("iautoid", iautoid));
        // 设置金额
        Long ifirstsourceproposalid = record.get("ifirstsourceproposalid");
        if (isOk(record) && notNull(ifirstsourceproposalid)) {
            Record records = getMoney(Kv.by("ifirstsourceproposalid", ifirstsourceproposalid).set("ipurchasemid",iautoid));
            if (isOk(records)) {
                record.setColumns(records);
            }
        }

        return record;
    }

    /**
     * 提交审批
     */
    public Ret submit(Long iautoid) {
        Purchasem purchasem = findById(iautoid);
        ValidationUtils.notNull(purchasem, "申购单记录不存在");
        ValidationUtils.isTrue(purchasem.getIOrgId().equals(getOrgId()), ErrorMsg.ORG_ACCESS_DENIED);
        ValidationUtils.equals(AuditStatusEnum.NOT_AUDIT.getValue(), purchasem.getIAuditStatus(), "非编辑状态，禁止提交审批");

        if (purchasem.getIRefType() == PurchaseRefTypeEnum.PROPOSAL.getValue()) {
            ValidationUtils.isTrue(validatePurchaseMoneyIsExceed(purchasem), "申购金额已超禀议金额10%或500,请追加禀议");
        }
        
        tx(() -> {
        	if(AppConfig.isVerifyProgressEnabled()){
        		// 根据审批状态
                Ret ret = formApprovalService.submit(table(), iautoid, primaryKey(),"cn.rjtech.admin.purchasem.PurchasemService");
                ValidationUtils.isTrue(ret.isOk(), ret.getStr("msg"));
                
                //生成待办和发送邮件
        	}else{
        		purchasem.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
        		ValidationUtils.isTrue(purchasem.update(), ErrorMsg.UPDATE_FAILED);
        	}
            return true;
        });

        return SUCCESS;

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
    /**
     * 撤销审批
     *
     * @param iautoid
     * @return
     */
    public Ret withdraw(Long iautoid) {
        Purchasem purchasem = findById(iautoid);
        ValidationUtils.notNull(purchasem, "禀议书不存在");
        ValidationUtils.equals(purchasem.getIAuditStatus(), AuditStatusEnum.AWAIT_AUDIT.getValue(), "非待审核状态，禁止操作");
        tx(() -> {
            // 修改审批状态
            ValidationUtils.isTrue(updateColumn(iautoid, "iauditstatus", AuditStatusEnum.NOT_AUDIT.getValue()).isOk(), "撤销审批失败");
            return true;
        });

        return SUCCESS;
    }

    /**
     * 提交采购（申购单推送U8）
     *
     * @param iautoid
     * @return
     */
    public Ret sumbitPurchase(Long iautoid) {
        Purchasem purchasem = findById(iautoid);
        List<Purchased> purchaseds = purchasedService.findByImid(iautoid);
        // 校验数据
        ValidationUtils.equals(getOrgCode(), purchasem.getCOrgCode(), "数据源不匹配");
        ValidationUtils.equals(1, purchasem.getIStatus(), "申购单已推送");
        ValidationUtils.equals(AuditStatusEnum.APPROVED.getValue(), purchasem.getIAuditStatus(), "只允许推送已审核申购单");
        // 制单人名
        String createname = JBoltUserCache.me.get(purchasem.getICreateBy()).getName();
        purchasem.put("createname", createname);

        tx(() -> {
            tx(u8SourceConfigName(), () -> {
                // 校验是否存在
                Record checkRecord = dbTemplate(u8SourceConfigName(), "purchasem.getAppVouchId", Kv.by("ccode", purchasem.getCPurchaseNo())).findFirst();
                ValidationUtils.isTrue(checkRecord == null, "该单据已存在U8请购单中");
                //查询申购单表头部门在部门对照表中的末级部门
                Record refDepartmentRc = depRefService.findIsDefaultEndDepRecord(purchasem.getCDepCode());
                purchasem.put("cenddepcode", refDepartmentRc.getStr("cdepcode"));
                // 推送U8及校验
                ValidationUtils.equals("ok", PurchaseAppApi.add(getOrgCode(), purchasem, purchaseds), JBoltMsg.FAIL);
                Record record = dbTemplate(u8SourceConfigName(), "purchasem.getAppVouchId", Kv.by("ccode", purchasem.getCPurchaseNo())).findFirst();
                ValidationUtils.notNull(record, "推单失败");

                // U8请购单改为已审核
                ValidationUtils.isTrue(dbTemplate(u8SourceConfigName(), "purchasem.updateAppVouch", Kv.by("id", record.get("id")).set("cverifier", createname)).update() == 1, "推单审核时失败,需手动修改");
                return true;
            });


            // 修改申购单
            purchasem.setDUpdateTime(new Date());
            purchasem.setIUpdateBy(JBoltUserKit.getUserId());
            purchasem.setIStatus(PurchaseStatusEnum.PUSH.getValue());
            ValidationUtils.isTrue(purchasem.update(), JBoltMsg.FAIL);
            return true;
        });

        return SUCCESS;
    }

    /**
     * 撤销采购（删除U8请购单）
     *
     * @param iautoids
     * @return
     */
    public Ret revocationSubmit(String iautoids) {
        ValidationUtils.notBlank(iautoids, JBoltMsg.PARAM_ERROR);
        List<Purchasem> purchasems = find(selectSql().in("iautoid", iautoids));
        ValidationUtils.notEmpty(purchasems, JBoltMsg.DATA_NOT_EXIST);

        // 推单状态校验
        long counts = purchasems.stream().filter(item -> item.getIStatus() == 1).count();
        ValidationUtils.isTrue(!(counts > 0), "细项存在未推送数据");
        // 获取申购单单据号
        List<String> cPurchaseNos = purchasems.stream().map(Purchasem::getCPurchaseNo).collect(Collectors.toList());

        // 获取U8请购单主表ID
        List<Integer> ids = dbTemplate(u8SourceConfigName(), "purchasem.getAppVouchId", Kv.by("ccodes", cPurchaseNos)).find().stream().filter(Objects::nonNull).map(item -> {
            return item.getInt("id");
        }).collect(Collectors.toList());
        ValidationUtils.notEmpty(ids, "未找到对应U8请购单");

        // 校验是否有采购单信息
        List<Record> records = dbTemplate(u8SourceConfigName(), "purchasem.checkIsCreataPurchase", Kv.by("ids",ids)).find();
        String purhaseNo = JBoltArrayUtil.join(records.stream().filter(Objects::nonNull).map(item -> item.getStr("ccode")).collect(Collectors.toList()), ",");
        ValidationUtils.isTrue(CollUtil.isEmpty(records), "以下申购单已生成采购单不允许撤销:" + purhaseNo);

        // 获取对应U8采购单信息
        tx(() -> {
            Kv kv = Kv.by("ids", ids);
            tx(u8SourceConfigName(), () -> {
                int deletenumber;
                // 删除请购主表数据
                deletenumber = dbTemplate(u8SourceConfigName(), "purchasem.delectAppVouchByids", kv).delete();
                ValidationUtils.isTrue(deletenumber != 0, JBoltMsg.FAIL);
                // 删除请购细表数据
                deletenumber = dbTemplate(u8SourceConfigName(), "purchasem.delectAppVouchsByids", kv).delete();
                ValidationUtils.isTrue(deletenumber != 0, JBoltMsg.FAIL);
              /*  // 删除请购额外表数据
                deletenumber = dbTemplate(u8SourceConfigName(), "purchasem.delectAppVouchExtraDefineByids", kv).delete();
                ValidationUtils.isTrue(deletenumber != 0, JBoltMsg.FAIL);*/
                return true;
            });


            // 修改申购单信息
            purchasems.forEach(purchasem -> {
                purchasem.setIStatus(PurchaseStatusEnum.NOPUSH.getValue());
                purchasem.setDUpdateTime(new Date());
                purchasem.setIUpdateBy(JBoltUserKit.getUserId());
                ValidationUtils.isTrue(purchasem.update(), JBoltMsg.FAIL);
            });

            return true;
        });

        return SUCCESS;
    }

    /**
     * 打印预览
     *
     * @param iautoid
     * @return
     */
    public Ret printData(Long iautoid) {
        Record purchasem = getPurchasemByIautoid(iautoid);
        List<Record> purchaseds = purchasedService.findByPurchaseId(iautoid);
        // 设置合计信息
        Integer totalCount = 0;
        BigDecimal sumItaxexclusivetotalamount = BigDecimal.ZERO;
        BigDecimal sumiTotalAmount = BigDecimal.ZERO;
        for (Record record : purchaseds) {
            totalCount += record.getInt("iquantity");
            sumItaxexclusivetotalamount = sumItaxexclusivetotalamount.add(record.getBigDecimal("itaxexclusivetotalamount"));
            sumiTotalAmount = sumiTotalAmount.add(record.getBigDecimal("itotalamount"));
        }

        String ispurchased = Optional.ofNullable(dictionaryService.getCacheByKey(purchasem.getBoolean("ispurchased").toString(), "options_boolean")).map(Dictionary::getName).orElse("否");
        purchasem.put("ispurchased", ispurchased);
        purchasem.put("purchaseds",purchaseds);
        purchasem.put("totalcount",totalCount);
        purchasem.put("sumitaxexclusivetotalamount",sumItaxexclusivetotalamount);
        purchasem.put("sumitotalamount",sumiTotalAmount);
        return successWithData(Kv.by("purchasem", purchasem));
    }

    /**
     * 参照预算
     *
     * @param iautoids
     * @return
     */
    public List<Record> purchaseChoose(String iautoids) {
        return dbTemplate("purchasem.purchaseChoose", Kv.by("iautoids", iautoids)).find();
    }
    /**
     * 选择存货树形数据源
     */
    public List<JsTreeBean> inventorTree() {
        List<JsTreeBean> treeList = inventoryClassService.getTreeList();
        return treeList;
    }
    /**
     * 选择供应商分类树形数据源
     */
    public List<JsTreeBean> vendorclassTree() {
        return vendorclassService.getTreeList();
    }
    
	/**
	 * 获取部门英文名称
	 * */
	public String getItemOrderContent(Long iautoid,String cprojectcode) {
		String str = "";
    	Purchasem purchasem = findById(iautoid);
    	ValidationUtils.notNull(purchasem, "申购单数据不存在,获取部门英文名称失败!");
		switch (BarCodeEnum.toEnum(cprojectcode)) {
	        case DEPT:
	        	Record record = departmentService.findByCdepcode(getOrgId(),purchasem.getCDepCode()).toRecord();
	    		String cdepnameen = record.getStr("cdepnameen");
	    		ValidationUtils.notBlank(cdepnameen, record.getStr("cdepname")+"部门的英文名称为空!");
	    		str = cdepnameen;
	            break;  
	        case ORDERYEAR:
	        	ValidationUtils.error( "申购单无法获取"+BarCodeEnum.ORDERYEAR.getText()+",请检查编码规则!");
	            break;
	        default:
                break;
		}
		return str;
	}
    /**
     * 参照禀议书界面-禀议书主表数据查询
     * */
	public List<Record> chooseProposalmDatas(Kv para) {
        User user = JBoltUserKit.getUser();
        // 系统管理员 || 存在权限部门
        para.set("isSystemAdmin", user.getIsSystemAdmin())
        .set("iorgid", getOrgId());
        List<Record> list = dbTemplate("purchasem.chooseProposalmDatas", para.set("iorgid", getOrgId())).find();
        if (CollUtil.isEmpty(list)) return null;
        List<Record> purposeList = dictionaryService.getOptionsByTypeKey(DictionaryTypeKeyEnum.PURPOSE.getValue());
        ValidationUtils.notEmpty(purposeList, "缺少目的区分的字典数据");
        // 目的区分 字典数据
        RecordMap<String, String> purposeMap = new RecordMap<>(purposeList, "sn", "name");
        List<Record> proposalcategoryList = proposalcategoryService.findAllRecords();
        ValidationUtils.notEmpty(proposalcategoryList, "缺少类别区分数据");
        // 类型类别
        RecordMap<Long, String> categoryMap = new RecordMap<>(proposalcategoryList, "iautoid", "ccategoryname");
        for (Record row : list) {
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
        }
        return list;
    }
    /**
     * 参照禀议书界面-禀议书项目数据查询
     * */
	public List<Record> chooseProposalmDatasDetail(Kv para) {
		List<Record> list = dbTemplate("purchasem.chooseProposalmDatasDetail", para).find();
		return list;
	}
	/**
	 * 申购单生效：
	 * 	1.反写禀议书累计申购金额(本币无税)
	 * 	2.更新申购单生效状态
	 * */
/*	public Ret effect(Long ipurchasemid) {
		Date now = new Date();
		Purchasem purchasem = findById(ipurchasemid);
		Proposalm proposalm = proposalmService.findById(purchasem.getIproposalmid());
		ValidationUtils.isTrue(ObjUtil.equal(proposalm.getIAuditStatus(), AuditStatusEnum.APPROVED.getValue()), "申购单的来源禀议书已失效,操作失败!");
		ValidationUtils.isTrue(ObjUtil.equal(purchasem.getIAuditStatus(), AuditStatusEnum.APPROVED.getValue())
				&& ObjUtil.equal(purchasem.getIeffectivestatus(), EffectiveStatusEnum.INVAILD.getValue())
				, "请选择已审核,未生效的申购单进行生效操作");
		//校验是否超过禀议金额限制
		validatePurchaseMoneyIsExceed(purchasem,true);
		tx(()->{
			//反写禀议书累计申购金额
			BigDecimal ipurchasemoney = purchasem.getIpurchasemoney();
			Record moneyRc = getMoney(Kv.by("iproposalmid", purchasem.getIproposalmid()));
			BigDecimal itotalpurchasemoney = moneyRc.getBigDecimal("itotalpurchasemoney"); //禀议书的累计申购金额(本币无税)
			BigDecimal inowtotalpurchasemoney = ipurchasemoney.add(itotalpurchasemoney);//本次申购的累计申购金额 = 禀议书的累计申购金额(本币无税) + 申购单本次申购金额(本币无税)
			User loginUser = JBoltUserKit.getUser();
			proposalm.setIUpdateBy(loginUser.getId());
			proposalm.setDUpdateTime(now);
			proposalm.setItotalpurchasemoney(inowtotalpurchasemoney);
			ValidationUtils.isTrue(proposalm.update(), ErrorMsg.UPDATE_FAILED);
			purchasem.setIeffectivestatus(EffectiveStatusEnum.EFFECTIVED.getValue());
			purchasem.setIUpdateBy(loginUser.getId());
			purchasem.setDUpdateTime(now);
			ValidationUtils.isTrue(purchasem.update(), ErrorMsg.UPDATE_FAILED);
			return true;
		});
		return SUCCESS;
	}*/

    public List<Record> findExpenseBudgetItemDatas(Kv para) {
        User user = JBoltUserKit.getUser();
        // 系统管理员 || 存在权限部门
        para.set("iorgid", getOrgId())
                .set("isSystemAdmin", user.getIsSystemAdmin())
                .set("iservicetype",ServiceTypeEnum.EXPENSE_BUDGET.getValue())
				.set("istatus",FinishStatusEnum.UNFINISHED.getValue())
				.set("isfreeze",IsEnableEnum.NO.getValue());
        Long iperiodid = para.getLong("iperiodid");
        ExpenseBudget expenseBudget = new ExpenseBudget();
        Period period = periodService.findById(iperiodid);
        expenseBudget.setCBeginDate(period.getDstarttime());
        expenseBudget.setCEndDate(period.getDendtime());
        expenseBudgetService.constructDynamicsDbColumn(expenseBudget,para);
        List<Record> list = dbTemplate("purchasem.findExpenseBudgetItemDatas", para).find();
        if (CollUtil.isNotEmpty(list)) {
            for (Record row : list) {
            	row.set("ibudgetmoneyhidden", row.get("ibudgetmoney"));
            	row.set("cdepname", departmentService.getCdepName(row.getStr("cdepcode")));
                row.set("careertypename", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKeyEnum.CAREERTYPE.getValue(), row.getStr("careertype")));
            }
        }
        return list;
    }
    /**
     * 选择投资计划列表数据
     */
	public List<Record> findInvestmentPlanItemDatas(Kv para) {
        User user = JBoltUserKit.getUser();
        // 系统管理员 || 存在权限部门
        para.set("iorgid", getOrgId())
                .set("isSystemAdmin", user.getIsSystemAdmin())
                .set("iservicetype",ServiceTypeEnum.INVESTMENT_PLAN.getValue())
				.set("istatus",FinishStatusEnum.UNFINISHED.getValue())
        		.set("isfreeze",IsEnableEnum.NO.getValue());
        List<Record> list = dbTemplate("purchasem.findInvestmentPlanItemDatas", para).find();
        if (CollUtil.isNotEmpty(list)) {
            for (Record row : list) {
            	row.set("ibudgetmoneyhidden", row.get("ibudgetmoney"));
            	row.set("cdepname", departmentService.getCdepName(row.getStr("cdepcode")));
                Constants.fillPlanItem(row);
            }
        }
        return list;
    }
	//参照预算构造申购单明细数据
	public List<Record> refBudgetItemDatas(Kv para) {
		return dbTemplate("purchasem.refBudgetItemDatas",para).find();
	}
   /**
    * 参照预算保存申购单
    * */
	/**
	 * @param jBoltTable
	 * @return
	 */
	public Ret refBudgetSaveTableSubmit(JBoltTableMulti tableMulti) {
		JBoltTable jBoltTable = tableMulti.getJBoltTable("purchaseds");
		Purchasem purchasem = jBoltTable.getFormModel(Purchasem.class,"purchasem");
		DataPermissionKit.validateAccess(purchasem.getCDepCode());
		JBoltTable attachmentsTable = tableMulti.getJBoltTable("attachments");
		Date now = new Date();
		User loginUesr = JBoltUserKit.getUser();
		tx(()->{
			if(purchasem.getIAutoId() == null){
				purchasem.setIOrgId(getOrgId());
				purchasem.setCOrgCode(getOrgCode());
				purchasem.setIRefType(PurchaseRefTypeEnum.BUDGET.getValue());
				purchasem.setIAuditStatus(AuditStatusEnum.NOT_AUDIT.getValue());
				purchasem.setIStatus(PurchaseStatusEnum.NOPUSH.getValue());
				purchasem.setICreateBy(loginUesr.getId());
				purchasem.setDCreateTime(now);
				ValidationUtils.isTrue(purchasem.save(), ErrorMsg.SAVE_FAILED);
				purchasem.setCPurchaseNo(genPurchasNo(Kv.by("iautoid", purchasem.getIAutoId())));
			}else{
				purchasem.setIUpdateBy(JBoltUserKit.getUserId());
				purchasem.setDUpdateTime(now);
			}
			ValidationUtils.isTrue(purchasem.update(), ErrorMsg.UPDATE_FAILED);
			addPurchasedDatas(jBoltTable.getSaveRecordList(),purchasem);
			updatePurchasedDatas(jBoltTable.getSaveRecordList());
			deletePurchasedDatas(jBoltTable.getDelete());
			//保存申购单附件
	        saveAttachmentsTable(attachmentsTable,purchasem);
			return true;
		});
		return successWithData(purchasem.keep("iautoid"));
	}
	//表格提交-新增申购明细数据
	private void addPurchasedDatas(List<Record> saveRecordList, Purchasem purchasem) {
		if(CollUtil.isEmpty(saveRecordList)) return;
		Set<String> columnNames = TableMapping.me().getTable(Purchased.class).getColumnNameSet();
		for (int i=0; i < saveRecordList.size(); i++) {
			Record saveRecord = saveRecordList.get(i);
			saveRecord.keep("iautoid","ipurchaseid","cinvcode","cinvname","cinvstd","iquantity","cunit","caddress","iprice","itotalamount","itaxrate",
					"ddemandate","cvencode","ccurrency","cmemo","itaxexclusivetotalamount","nflat","creferencepurpose",
					"isourcetype","isourceid","iprojectcardid","itax","inatunitprice","inattax","inattax","inatsum","inatmoney","isubitem")
			.set("iautoid", JBoltSnowflakeKit.me.nextId())
			.set("ipurchaseid", purchasem.getIAutoId());
			if (i == 0) {
            //避免保存不到所有字段的问题(批量保存Record，底层是以数组中第一行的字段名称来拼接Insert sql的)
            for (String columnName : columnNames) {
                if (null == saveRecord.get(columnName)) {
                	saveRecord.set(columnName, null);
                }
            }
            }
		}
		purchasedService.batchSaveRecords(saveRecordList);
	}
	//表格提交-修改申购明细数据
	private void updatePurchasedDatas(List<Record> updateRecordList) {
		if(CollUtil.isEmpty(updateRecordList)) return;
		Set<String> columnNames = TableMapping.me().getTable(Purchased.class).getColumnNameSet();
		for (int i=0; i < updateRecordList.size(); i++) {
			Record saveRecord = updateRecordList.get(i);
			saveRecord.keep("iautoid","ipurchaseid","cinvcode","cinvname","cinvstd","iquantity","cunit","caddress","iprice","itotalamount","itaxrate",
					"ddemandate","cvencode","ccurrency","cmemo","itaxexclusivetotalamount","nflat","creferencepurpose",
					"isourcetype","isourceid","iprojectcardid","itax","inatunitprice","inattax","inattax","inatsum","inatmoney","isubitem");
			if (i == 0) {
            //避免保存不到所有字段的问题(批量保存Record，底层是以数组中第一行的字段名称来拼接Insert sql的)
            for (String columnName : columnNames) {
                if (null == saveRecord.get(columnName)) {
                	saveRecord.set(columnName, null);
                }
            }
            }
		}
		purchasedService.batchUpdateRecords(updateRecordList);
	}
	//表格提交-修改申购明细数据
	private void deletePurchasedDatas(Object[] ids) {
		if(ArrayUtil.isEmpty(ids)) return;
		purchasedService.deleteByIds(ids);
	}
   /**
    * 参照禀议-申购单明细导入
    * */
	public Ret importRefProposalPurchasedTplNotSave(File file,Long ifirstsourceproposalid) {
        StringBuilder errorMsg = new StringBuilder();
        int startRow = 2;
        JBoltExcel excel = JBoltExcel
                // 从excel文件创建JBoltExcel实例
                .from(file)
                // 设置工作表信息
                .setSheets(
                        JBoltExcelSheet.create("Sheet1")
                                // 设置列映射 顺序 标题名称
                                .setHeaders(1,
                                        JBoltExcelHeader.create("index", "序号"),
                                        JBoltExcelHeader.create("isourcetype", "预算类型"),
                                        JBoltExcelHeader.create("ibudgetmoney", "预算金额"),
                                        JBoltExcelHeader.create("cbudgetno", "预算编号"),
                                        JBoltExcelHeader.create("cinvcode", "存货编码"),
                                        JBoltExcelHeader.create("cinvname", "存货名称"),
                                        JBoltExcelHeader.create("cinvstd", "规格型号"),
                                        JBoltExcelHeader.create("cunit", "单位"),
                                        JBoltExcelHeader.create("caddress", "品牌"),
                                        JBoltExcelHeader.create("iquantity", "数量"),
                                        JBoltExcelHeader.create("iprice", "原币无税单价"),
                                        JBoltExcelHeader.create("nflat", "汇率"),
                                        JBoltExcelHeader.create("itaxrate", "税率"),
                                        JBoltExcelHeader.create("ddemandate", "需求日期"),
                                        JBoltExcelHeader.create("cvencode", "供应商"),
                                        JBoltExcelHeader.create("ccurrency", "币种"),
                                        JBoltExcelHeader.create("creferencepurpose", "参考用途")
                                )
                                // 从第三行开始读取
                                .setDataStartRow(startRow)
                );

        // 从指定的sheet工作表里读取数据
        List<Record> rows = JBoltExcelUtil.readRecords(excel, 0, true, errorMsg);
        if (notOk(rows)) {
            if (errorMsg.length() > 0) {
                return fail(errorMsg.toString());
            } else {
                return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
            }
        }
        //查询参考的禀议书下所有的预算数据(预算编号，预算金额(无税)，业务类型)
        List<Record> budgetRecordList = findEnabledImportDatasOfRefProposal(ifirstsourceproposalid);
        for (int i=0; i < rows.size(); i++) {
        	int nowRow = startRow + i;
        	Record record = rows.get(i);
        	record.remove("index");
        	String cbudgetno = record.getStr("cbudgetno");
        	if(JBoltStringUtil.isBlank(cbudgetno)) {
        		errorMsg.append("第"+nowRow+"行,预算编号不能为空<br/>");
        		continue;
        	}
        	List<Record> isExistsProposalFilterList = budgetRecordList.stream().filter(item->ObjUtil.equal(item.getStr("cbudgetno"), cbudgetno)).collect(Collectors.toList());
        	if(CollUtil.isEmpty(isExistsProposalFilterList)) {
        		errorMsg.append("第"+nowRow+"行,预算编号不存在参照禀议书中<br/>");
        		continue;
        	}
        	record.set("isourcetype", isExistsProposalFilterList.get(0).get("isourcetype"));
        	record.set("ibudgetmoney", isExistsProposalFilterList.get(0).get("ibudgetmoney"));
        	record.set("iprojectcardid", isExistsProposalFilterList.get(0).get("iprojectcardid"));
        	record.set("isourceid", isExistsProposalFilterList.get(0).get("isourceid"));
        	record.set("iproposaldid", isExistsProposalFilterList.get(0).get("iproposaldid"));
        	//excel中预算编号相同的所有行中第一行默认isubitem=0,其它行为isubitem=1
        	List<Record> isISubItemFilterList = rows.stream().filter(item->ObjUtil.equal(item.getStr("cbudgetno"), cbudgetno) && item.getInt("isubitem") != null).collect(Collectors.toList());
        	if(CollUtil.isEmpty(isISubItemFilterList)) record.set("isubitem", IsEnableEnum.NO.getValue());
        	else record.set("isubitem", IsEnableEnum.YES.getValue());
        	String cinvcode = record.getStr("cinvcode");
        	if(JBoltStringUtil.isBlank(cinvcode)){
        		errorMsg.append("第"+nowRow+"行,存货编码不能为空<br/>");
        		continue;
        	}
        	Record inventoryRc = inventoryService.findByCinvcode(cinvcode);
        	if(inventoryRc == null){
        		errorMsg.append("第"+nowRow+"行,存货编码不存在<br/>");
        		continue;
        	}
        	record.set("cinvname", inventoryRc.getStr("cinvname"));
        	record.set("cinvstd", inventoryRc.getStr("cinvstd"));
        	String cunit = JBoltStringUtil.isNotBlank(record.getStr("cunit")) ? record.getStr("cunit"):inventoryRc.getStr("ccomunitname");
        	if(JBoltStringUtil.isBlank(cunit)){
        		errorMsg.append("第"+nowRow+"行,单位不能为空<br/>");
        		continue;
        	}
        	record.set("cunit", cunit);
        	record.set("caddress", JBoltStringUtil.isNotBlank(record.getStr("caddress")) ? record.getStr("caddress"):inventoryRc.getStr("caddress"));
        	try{
        		Integer itaxrate = record.getInt("itaxrate");
        		Integer u8itaxrate = inventoryRc.getInt("itaxrate");
        		if(itaxrate == null && u8itaxrate == null) {
        			errorMsg.append("第"+nowRow+"行,税率不能为空<br/>");
        			continue;
        		}
        		itaxrate = itaxrate != null ? itaxrate : u8itaxrate;
        		if(!(itaxrate>=1 && itaxrate <= 100)){
            		errorMsg.append("第"+nowRow+"行,税率必须1-100的正整数<br/>");
            		continue;
            	}
        		record.set("itaxrate", itaxrate);
        	}catch(Exception e){
        		errorMsg.append("第"+nowRow+"行,税率必须1-100的正整数<br/>");
        		continue;
        	}
        	try{
        		Integer iquantity = record.getInt("iquantity");
        		if(iquantity == null) {
        			errorMsg.append("第"+nowRow+"行,数量不能为空<br/>");
        			continue;
        		}
        	}catch(Exception e){
        		errorMsg.append("第"+nowRow+"行,数量必须为正整数<br/>");
        		continue;
        	}
        	String ccurrency = record.getStr("ccurrency");
        	BigDecimal nflat = record.getBigDecimal("nflat");
        	BigDecimal u8nflat = null;
        	if(JBoltStringUtil.isNotBlank(ccurrency)){
        		Record exchRc = exchService.getNflat(ccurrency);
        		u8nflat = exchRc != null ? exchRc.getBigDecimal("nflat") : null;
        	}
        	nflat = nflat !=null? nflat : u8nflat;
        	if(nflat == null){
        		errorMsg.append("第"+nowRow+"行,汇率不能为空<br/>");
        		continue;
        	}
        	nflat = NumberUtil.round(nflat,4,RoundingMode.HALF_UP);
        	record.set("nflat", nflat);
        	String cvencode = record.getStr("cvencode");
        	if(JBoltStringUtil.isBlank(cvencode)){
        		errorMsg.append("第"+nowRow+"行,供应商编码不能为空<br/>");
        		continue;
        	}
        	Record vendorRc = vendorRecordService.getRecprdByCVenCode(cvencode);
        	if(vendorRc == null){
        		errorMsg.append("第"+nowRow+"行,供应商编码不存在<br/>");
        		continue;
        	}
        	record.set("cvenname", vendorRc.getStr("cvenname"));
        	try{
        		BigDecimal iprice = record.getBigDecimal("iprice");
        		BigDecimal u8Price = null;
        		Record u8PriceRc = dbTemplate(u8SourceConfigName(),"veninvprice.getiunitprice", Kv.by("cinvcode", cinvcode).set("cvencode", cvencode).set("cfree2", null)).findFirst();
        		if(u8PriceRc!=null) u8Price = u8PriceRc.getBigDecimal("iunitprice");
        		iprice = iprice != null ? iprice : u8Price;
        		if(iprice == null) {
        			errorMsg.append("第"+nowRow+"行,单价不能为空<br/>");
        			continue;
        		}
        		iprice = NumberUtil.round(iprice, 2, RoundingMode.HALF_UP);
        		record.set("iprice", iprice);
        	}catch(Exception e){
        		errorMsg.append("第"+nowRow+"行,单价必须为数字<br/>");
        		continue;
        	}
        	try{
        		if(JBoltStringUtil.isBlank(record.getStr("ddemandate"))){
        			errorMsg.append("第"+nowRow+"行,需求日期不能为空<br/>");
	        		continue;
        		}
				Date ddemandate = JBoltDateUtil.toDate(record.getStr("ddemandate"), "yyyy/MM/dd");
				if(ddemandate == null){
					errorMsg.append("第"+nowRow+"行,需求日期格式不正确,请参考:yyyy/MM/dd<br/>");
	        		continue;
				}
			} catch (Exception e) {
				errorMsg.append("第"+nowRow+"行,需求日期格式不正确,请参考:yyyy/MM/dd<br/>");
        		continue;
			}
        }
        String errorStr = errorMsg.toString();
        if(JBoltStringUtil.isNotBlank(errorStr)) return fail(errorStr);
        return Ret.ok("rows", rows);
    }
	/**
	 * 参考禀议-申购单明细导入 : 查询可导入的数据范围
	 * */
	private List<Record> findEnabledImportDatasOfRefProposal(Long ifirstsourceproposalid){
		ValidationUtils.notNull(ifirstsourceproposalid, "参照禀议书ID为空!");
		Kv para = Kv.by("ifirstsourceproposalid", ifirstsourceproposalid);
		para.set("ieffectivestatus",EffectiveStatusEnum.EFFECTIVED.getValue());
		List<Record> list = dbTemplate("purchasem.findCBudgetNosOfRefProposal",para).find();
		return list;
	}
	
   /**
    * 参照预算-申购单明细导入
    * */
	public Ret importRefBudgetPurchasedTplNotSave(File file,String cdepcode) {
        StringBuilder errorMsg = new StringBuilder();
        int startRow = 2;
        JBoltExcel excel = JBoltExcel
                // 从excel文件创建JBoltExcel实例
                .from(file)
                // 设置工作表信息
                .setSheets(
                        JBoltExcelSheet.create("Sheet1")
                                // 设置列映射 顺序 标题名称
                                .setHeaders(1,
                                        JBoltExcelHeader.create("index", "序号"),
                                        JBoltExcelHeader.create("isourcetype", "预算类型"),
                                        JBoltExcelHeader.create("ibudgetmoney", "预算金额"),
                                        JBoltExcelHeader.create("cbudgetno", "预算编号"),
                                        JBoltExcelHeader.create("cinvcode", "存货编码"),
                                        JBoltExcelHeader.create("cinvname", "存货名称"),
                                        JBoltExcelHeader.create("cinvstd", "规格型号"),
                                        JBoltExcelHeader.create("cunit", "单位"),
                                        JBoltExcelHeader.create("caddress", "品牌"),
                                        JBoltExcelHeader.create("iquantity", "数量"),
                                        JBoltExcelHeader.create("iprice", "原币无税单价"),
                                        JBoltExcelHeader.create("nflat", "汇率"),
                                        JBoltExcelHeader.create("itaxrate", "税率"),
                                        JBoltExcelHeader.create("ddemandate", "需求日期"),
                                        JBoltExcelHeader.create("cvencode", "供应商"),
                                        JBoltExcelHeader.create("ccurrency", "币种"),
                                        JBoltExcelHeader.create("creferencepurpose", "参考用途")
                                )
                                // 从第三行开始读取
                                .setDataStartRow(startRow)
                );

        // 从指定的sheet工作表里读取数据
        List<Record> rows = JBoltExcelUtil.readRecords(excel, 0, true, errorMsg);
        if (notOk(rows)) {
            if (errorMsg.length() > 0) {
                return fail(errorMsg.toString());
            } else {
                return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
            }
        }
        //查询禀议书下可导入预算数据范围(预算编号，预算金额(无税)，业务类型)
        List<Record> budgetRecordList = findEnableImportDatasOfRefBudget(cdepcode);
        for (int i=0; i < rows.size(); i++) {
        	int nowRow = startRow + i;
        	Record record = rows.get(i);
        	record.remove("index");
        	String cbudgetno = record.getStr("cbudgetno");
        	if(JBoltStringUtil.isBlank(cbudgetno)) {
        		errorMsg.append("第"+nowRow+"行,预算编号不能为空<br/>");
        		continue;
        	}
        	List<Record> isExistsProposalFilterList = budgetRecordList.stream().filter(item->ObjUtil.equal(item.getStr("cbudgetno"), cbudgetno)).collect(Collectors.toList());
        	if(CollUtil.isEmpty(isExistsProposalFilterList)) {
        		errorMsg.append("第"+nowRow+"行,预算编号不存在可导入的预算数据范围内<br/>");
        		continue;
        	}
        	record.set("isourcetype", isExistsProposalFilterList.get(0).get("isourcetype"));
        	record.set("ibudgetmoney", isExistsProposalFilterList.get(0).get("ibudgetmoney"));
        	record.set("iprojectcardid", isExistsProposalFilterList.get(0).get("iprojectcardid"));
        	record.set("isourceid", isExistsProposalFilterList.get(0).get("isourceid"));
        	//excel中预算编号相同的所有行中第一行默认isubitem=0,其它行为isubitem=1
        	List<Record> isISubItemFilterList = rows.stream().filter(item->ObjUtil.equal(item.getStr("cbudgetno"), cbudgetno) && item.getInt("isubitem") != null).collect(Collectors.toList());
        	if(CollUtil.isEmpty(isISubItemFilterList)) record.set("isubitem", IsEnableEnum.NO.getValue());
        	else record.set("isubitem", IsEnableEnum.YES.getValue());
        	String cinvcode = record.getStr("cinvcode");
        	if(JBoltStringUtil.isBlank(cinvcode)){
        		errorMsg.append("第"+nowRow+"行,存货编码不能为空<br/>");
        		continue;
        	}
        	Record inventoryRc = inventoryService.findByCinvcode(cinvcode);
        	if(inventoryRc == null){
        		errorMsg.append("第"+nowRow+"行,存货编码不存在<br/>");
        		continue;
        	}
        	record.set("cinvname", inventoryRc.getStr("cinvname"));
        	record.set("cinvstd", inventoryRc.getStr("cinvstd"));
        	String cunit = JBoltStringUtil.isNotBlank(record.getStr("cunit")) ? record.getStr("cunit"):inventoryRc.getStr("ccomunitname");
        	if(JBoltStringUtil.isBlank(cunit)){
        		errorMsg.append("第"+nowRow+"行,单位不能为空<br/>");
        		continue;
        	}
        	record.set("cunit", cunit);
        	record.set("caddress", JBoltStringUtil.isNotBlank(record.getStr("caddress")) ? record.getStr("caddress"):inventoryRc.getStr("caddress"));
        	try{
        		Integer itaxrate = record.getInt("itaxrate");
        		Integer u8itaxrate = inventoryRc.getInt("itaxrate");
        		if(itaxrate == null && u8itaxrate == null) {
        			errorMsg.append("第"+nowRow+"行,税率不能为空<br/>");
        			continue;
        		}
        		itaxrate = itaxrate != null ? itaxrate : u8itaxrate;
        		if(!(itaxrate>=1 && itaxrate <= 100)){
            		errorMsg.append("第"+nowRow+"行,税率必须1-100的正整数<br/>");
            		continue;
            	}
        		record.set("itaxrate", itaxrate);
        	}catch(Exception e){
        		errorMsg.append("第"+nowRow+"行,税率必须1-100的正整数<br/>");
        		continue;
        	}
        	try{
        		Integer iquantity = record.getInt("iquantity");
        		if(iquantity == null) {
        			errorMsg.append("第"+nowRow+"行,数量不能为空<br/>");
        			continue;
        		}
        	}catch(Exception e){
        		errorMsg.append("第"+nowRow+"行,数量必须为正整数<br/>");
        		continue;
        	}
        	String ccurrency = record.getStr("ccurrency");
        	BigDecimal nflat = record.getBigDecimal("nflat");
        	BigDecimal u8nflat = null;
        	if(JBoltStringUtil.isNotBlank(ccurrency)){
        		Record exchRc = exchService.getNflat(ccurrency);
        		u8nflat = exchRc != null ? exchRc.getBigDecimal("nflat") : null;
        	}
        	nflat = nflat !=null? nflat : u8nflat;
        	if(nflat == null){
        		errorMsg.append("第"+nowRow+"行,汇率不能为空<br/>");
        		continue;
        	}
        	nflat = NumberUtil.round(nflat,4,RoundingMode.HALF_UP);
        	record.set("nflat", nflat);
        	String cvencode = record.getStr("cvencode");
        	if(JBoltStringUtil.isBlank(cvencode)){
        		errorMsg.append("第"+nowRow+"行,供应商编码不能为空<br/>");
        		continue;
        	}
        	Record vendorRc = vendorRecordService.getRecprdByCVenCode(cvencode);
        	if(vendorRc == null){
        		errorMsg.append("第"+nowRow+"行,供应商编码不存在<br/>");
        		continue;
        	}
        	record.set("cvenname", vendorRc.getStr("cvenname"));
        	try{
        		BigDecimal iprice = record.getBigDecimal("iprice");
        		BigDecimal u8Price = null;
        		Record u8PriceRc = dbTemplate(u8SourceConfigName(),"veninvprice.getiunitprice", Kv.by("cinvcode", cinvcode).set("cvencode", cvencode).set("cfree2", null)).findFirst();
        		if(u8PriceRc!=null) u8Price = u8PriceRc.getBigDecimal("iunitprice");
        		iprice = iprice != null ? iprice : u8Price;
        		if(iprice == null) {
        			errorMsg.append("第"+nowRow+"行,单价不能为空<br/>");
        			continue;
        		}
        		iprice = NumberUtil.round(iprice, 2, RoundingMode.HALF_UP);
        		record.set("iprice", iprice);
        	}catch(Exception e){
        		errorMsg.append("第"+nowRow+"行,单价必须为数字<br/>");
        		continue;
        	}
        	try {
				Date ddemandate = JBoltDateUtil.toDate(record.getStr("ddemandate"), "yyyy/MM/dd");
				if(ddemandate == null){
					errorMsg.append("第"+nowRow+"行,需求日期不能为空<br/>");
	        		continue;
				}
			} catch (Exception e) {
				errorMsg.append("第"+nowRow+"行,需求日期格式不正确,请参考:yyyy/MM/dd<br/>");
        		continue;
			}
        }
        String errorStr = errorMsg.toString();
        if(JBoltStringUtil.isNotBlank(errorStr)) return fail(errorStr);
        return Ret.ok("rows", rows);
    }
	//参考预算-申购单明细导入 : 查询可导入的数据范围
	private List<Record> findEnableImportDatasOfRefBudget(String cdepcode){
		Kv para = Kv.by("cdepcode", cdepcode);
		para.set("iorgid",getOrgId());
		para.set("istatus",FinishStatusEnum.UNFINISHED.getValue());
		para.set("isfreeze",IsEnableEnum.NO.getValue());
		para.set("ieffectivestatus",EffectiveStatusEnum.EFFECTIVED.getValue());
		return dbTemplate("purchasem.findEnableImportDatasOfRefBudget",para).find();
	}

    /**
     * 获取供应商存货不含税价格
     *
     * @param cinvcode 存货编码
     * @param cvencode 供应商编码
     * @param cfree2   存货等级
     * @return
     */
    public Ret getIUnitPrice(String cinvcode, String cvencode, String cfree2) {
        ValidationUtils.notBlank(cinvcode, "存货编码不允许为空");
        ValidationUtils.notBlank(cvencode, "供应商编码不允许为空");
        Record record = dbTemplate(u8SourceConfigName(),"purchasem.getiunitprice", Kv.by("cinvcode", cinvcode).set("cvencode", cvencode).set("cfree2", cfree2)).findFirst();
        if (notOk(record)) {
            return null;
        }
        return successWithData(record.keep("iunitprice"));
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
} 