package cn.rjtech.admin.routing;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.bomcompare.BomCompareService;
import cn.rjtech.admin.bommasterinv.BomMasterInvService;
import cn.rjtech.admin.customer.CustomerService;
import cn.rjtech.admin.equipmentmodel.EquipmentModelService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventoryrouting.InventoryRoutingService;
import cn.rjtech.admin.invpart.InvPartService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.IsOkEnum;
import cn.rjtech.model.momdata.BomMaster;
import cn.rjtech.model.momdata.InvPart;
import cn.rjtech.model.momdata.InventoryRouting;
import cn.rjtech.model.momdata.InventoryRoutingConfig;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 物料建模-工艺路线
 * @ClassName: BomMasterService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-28 16:39
 */
public class RoutingService extends BaseService<BomMaster> {
	private final BomMaster dao=new BomMaster().dao();
	
	@Inject
	private InventoryService inventoryService;
	@Inject
	private BomCompareService bomCompareService;
	@Inject
	private EquipmentModelService equipmentModelService;
	@Inject
	private VendorService vendorService;
	@Inject
	private BomMasterInvService bomMasterInvService;
	@Inject
	private CustomerService customerService;
	@Inject
	private InvPartService invPartService;
	@Inject
	private DictionaryService dictionaryService;
	@Inject
	private InventoryRoutingService inventoryRoutingService;
	
	@Override
	protected BomMaster dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @param keywords   关键词
     * @param isEnabled 是否启用
     * @param isDeleted 是否删除 1已删除
	 * @return
	 */
	public Page<BomMaster> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isEnabled, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isEnabled",isEnabled);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cDocName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}
	

	/**
	 * 删除数据后执行的回调
	 * @param bomMaster 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(BomMaster bomMaster, Kv kv) {
		//addDeleteSystemLog(bomMaster.getIAutoId(), JBoltUserKit.getUserId(),bomMaster.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param bomMaster model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(BomMaster bomMaster, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(BomMaster bomMaster, String column, Kv kv) {
		//addUpdateSystemLog(bomMaster.getIAutoId(), JBoltUserKit.getUserId(), bomMaster.getName(),"的字段["+column+"]值:"+bomMaster.get(column));
		/**
		switch(column){
		    case "isEnabled":
		        break;
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}
	
    public List<JsTreeBean> findRoutingAll(Kv kv){
    	kv.set("orgId", getOrgId());
		List<Record> recordList = dbTemplate("routing.findRoutingAll", kv).find();
		if (CollUtil.isEmpty(recordList)){
			return null;
		}
	
		Map<Long, List<Record>> listMap = recordList.stream().filter(record -> ObjUtil.isNotNull(record.getLong(InvPart.IPARENTINVID)))
				.collect(Collectors.groupingBy(record -> record.getLong(InvPart.IPARENTINVID)));
	
		subordinate(recordList, listMap);
		
		if (StrUtil.isBlank(kv.getStr("keyWords"))){
            return createJsTreeBean(kv.getStr("enableIcon"), recordList);
        }
		
  
		List<Record> allRecordList = new ArrayList<>();
    	return createJsTreeBean(kv.getStr("enableIcon"), allRecordList);
	}
	
	public List<JsTreeBean> createJsTreeBean(String enableIconStr, List<Record> recordList){
        List<JsTreeBean> trees = new ArrayList<>();
        for (Record record : recordList){
            String id = record.getStr(InventoryRoutingConfig.IAUTOID);
			String pid = record.getStr(InventoryRoutingConfig.IAUTOID);
			String partName = record.getStr(InvPart.CPARTNAME);
			
			StringBuilder text = new StringBuilder(partName);
            if (pid == null) {
                pid = "#";
                if (StrUtil.isNotBlank(enableIconStr)){
                    String enableIcon = enableIconStr;
                    enableIcon = enableIcon.replace("?", id);
                    text.append(enableIcon);
                }
            }
            JsTreeBean jsTreeBean = new JsTreeBean(id, pid, text.toString(), true);
            trees.add(jsTreeBean);
        }
       return trees;
    }
	
	
	/**
	 * 根节
	 * @param recordList
	 */
	public void subordinate(List<Record> recordList, Map<Long, List<Record>> listMap){
		
		if (CollUtil.isEmpty(recordList)){
			return;
		}
		
		List<Record> commonList = new ArrayList<>();
		
		for (Record record : recordList){
			Long iInventoryId = record.getLong(InvPart.IINVENTORYID);
			Long pId = record.getLong(InvPart.IPID);
			Long iAutoId = record.getLong(InvPart.IAUTOID);
			// 虚拟机跳过。父级id为空跳过
			if (ObjUtil.isNull(iInventoryId) || ObjUtil.isNull(pId)){
				continue;
			}
		
			// 复制值。
			if (listMap.containsKey(iInventoryId)){
				
				Map<Long, List<Record>> map = listMap.get(iInventoryId).stream().filter(obj -> ObjUtil.isNotNull(obj.getLong(InvPart.IPID))).collect(Collectors.groupingBy(obj -> obj.getLong(InvPart.IPID)));
				Map<Long, Long> newIdMap = new HashMap<>();
				for (Record partRecord : listMap.get(iInventoryId)){
					Long iPId = partRecord.getLong(InvPart.IPID);
					Long newId = iAutoId;
					if (ObjUtil.isNotNull(iPId)){
						newId = JBoltSnowflakeKit.me.nextId();
					}
					newIdMap.put(partRecord.getLong(InvPart.IAUTOID), newId);
				}
			
				for (Record partRecord : listMap.get(iInventoryId)){
				
					Long iPId = partRecord.getLong(InvPart.IPID);
					// 父id为空跳过
					if (ObjUtil.isNull(iPId)){
						continue;
					}
					// 主键id
					Long id = partRecord.getLong(InvPart.IAUTOID);
					// 获取新id
					Long newId = newIdMap.get(id);
					Record newRecord = new Record();
					newRecord.setColumns(partRecord);
					newRecord.set(InvPart.IAUTOID, newId);
					Long newPId = newIdMap.get(iPId);
					newRecord.set(InvPart.IPID, newPId);
					commonList.add(newRecord);
				}
			}
		}
		
		if (CollUtil.isNotEmpty(commonList)){
			recordList.addAll(commonList);
		}
	}
 
	public Page<Record> getPageData(int pageNumber, int pageSize, Kv kv){
		int end = pageNumber * pageSize;
		if (end <= 0) {
			end = pageSize;
		}
		
		int begin = (pageNumber - 1) * pageSize;
		if (begin < 0) {
			begin = 0;
		}
		
		Integer size = queryInt("EXEC BD_BOMTREE_COUNT @orgId=?, @pId= ?,@itemCode = ?,@itemName = ?", getOrgId(), kv.getLong("pid"), kv.getStr("invCode"), kv.getStr("invName"));
		
		if (size == null || size < 0){
			return new Page(new ArrayList(0), pageNumber, pageSize, 0, 0);
		}
		
		long totalRow = Long.valueOf(size);
		int totalPage = (int)(totalRow / (long)pageSize);
		if (totalRow % (long)pageSize != 0L) {
			++totalPage;
		}
		
		if (pageNumber > totalPage) {
			return new Page(new ArrayList(0), pageNumber, pageSize, totalPage, (int)totalRow);
		} else {
			List<Record> list = findRecord("EXEC BD_BOMTree_Page @orgId=?, @pId= ?,@itemCode = ?,@itemName = ?,@pageNumber=?,@pageSize = ?", false,
					getOrgId(),
					kv.getLong("pid"),
					kv.getStr("invCode"),
					kv.getStr("invName"),
				    begin,
					end);
			return new Page(list, pageNumber, pageSize, totalPage, (int)totalRow);
		}
		
	}
	
	public List<Record> detail (Kv kv){
		kv.set("orgId", getOrgId());
		return dbTemplate("routing.getRoutingDetails", kv).find();
	}
	
	public List<Record> getRoutingDetails(Kv kv){
		
		if (StrUtil.isBlank(kv.getStr(InvPart.IAUTOID))){
			return null;
		}
		
		// 查询当前日期
		InvPart invPart = invPartService.findById(kv.getStr(InvPart.IAUTOID));
		ValidationUtils.notNull(invPart, JBoltMsg.DATA_NOT_EXIST);
		kv.set(InvPart.IPARENTINVID, invPart.getIParentInvId());
		List<Record> details = detail(kv);
		
		if (CollUtil.isEmpty(details)){
			return null;
		}
		
		// 查询所有工艺路线
		kv.remove(InvPart.IPARENTINVID);
		List<Record> records = detail(kv);
		
		Map<Long, List<Record>> listMap = records.stream().filter(record -> ObjUtil.isNotNull(record.getLong(InvPart.IPARENTINVID)))
				.collect(Collectors.groupingBy(record -> record.getLong(InvPart.IPARENTINVID)));
		
		subordinate(details, listMap);
		
		List<Dictionary> processTypeList = dictionaryService.getListByTypeKey("process_type");
		ValidationUtils.notNull(processTypeList, "数字字典未配置【工序类型-process_type】项");
		
		List<Dictionary> productTypeList = dictionaryService.getListByTypeKey("cproductsn_type");
		ValidationUtils.notNull(productTypeList, "数字字典未配置【生产方式-cproductsn_type】项");
		// 工序类型
		Map<String, Dictionary> processTypeMap = processTypeList.stream().collect(Collectors.toMap(dictionary -> dictionary.getSn(), dictionary -> dictionary));
		// 生产方式
		Map<String, Dictionary> productTypeMap = productTypeList.stream().collect(Collectors.toMap(dictionary -> dictionary.getSn(), dictionary -> dictionary));
		
		for (Record record :details){
			
			String typeStr = record.getStr(InventoryRoutingConfig.ITYPE);
			String cProductSn = record.getStr(InventoryRoutingConfig.CPRODUCTSN);
			
			if (processTypeMap.containsKey(typeStr)){
				record.set(InventoryRoutingConfig.ITYPE, processTypeMap.get(typeStr).getName());
			}
			
			if (productTypeMap.containsKey(cProductSn)){
				record.set(InventoryRoutingConfig.CPRODUCTSN, productTypeMap.get(cProductSn).getName());
			}
		}
		
		return details;
	}
	
	public Page<Record> findRoutingVersion(int pageNumber, int pageSize, Kv kv){
		kv.set("orgId", getOrgId());
		Page<Record> paginate = dbTemplate("routing.findRoutingVersion", kv).paginate(pageNumber, pageSize);
		changeData(paginate.getList());
		return paginate;
	}

	public List<Record> findRoutingVersionExport( Kv kv){
		kv.set("orgId", getOrgId());
		return dbTemplate("routing.findRoutingVersion", kv).find();
	}

	public List<Record> Versiondetail (Kv kv){
		kv.set("orgId", getOrgId());
		return dbTemplate("routing.findRoutingVersion", kv).find();
	}
	
	public Record findByIdRoutingVersion(Long id){
		Okv okv = Okv.by("orgId", getOrgId()).set("id", id);
		return dbTemplate("routing.findRoutingVersion", okv).findFirst();
	}
	
	
	public void changeData(List<Record> recordList){
		if (CollUtil.isEmpty(recordList)){
			return;
		}
		for (Record record : recordList){
			Boolean isEnable = record.getBoolean(InventoryRouting.ISENABLED);
			Integer iAuditStatus = record.getInt(InventoryRouting.IAUDITSTATUS);
			AuditStatusEnum auditStatusEnum = AuditStatusEnum.toEnum(iAuditStatus);
			record.set(InventoryRouting.CAUDITSTATUSTEXT, auditStatusEnum.getText());
		}
	}
	
	public Ret audit(Long routingId, Integer status) {
		// 先校验是否存在
		InventoryRouting inventoryRouting = inventoryRoutingService.findById(routingId);
		ValidationUtils.notNull(inventoryRouting, "工艺路线记录不存在");
		ValidationUtils.notNull(status, "缺少状态字段");
		
		AuditStatusEnum auditStatusEnum = AuditStatusEnum.toEnum(inventoryRouting.getIAuditStatus());
		// 校验是否存在审批中的数据，存在审批中的数据不允许审批
		Integer count = inventoryRoutingService.queryAwaitAudit(routingId, inventoryRouting.getIInventoryId());
		ValidationUtils.isTrue(count==null || count==0, "当前存货存在审批中的工艺路线数据");
		
		DateTime date = DateUtil.date();
//		String userName = JBoltUserKit.getUserName();
//		Long userId = JBoltUserKit.getUserId();

//		inventoryRouting.setIAuditStatus(status);
//		inventoryRouting.setDSubmitTime(date);
//		// 默认为1
//		inventoryRouting.setIAuditWay(1);
//		tx(()->{
//			// 审核通过需要更改状态（当前存货）
//			if (AuditStatusEnum.APPROVED.getValue() == status){
//				//
//				inventoryRoutingService.updateEnable(inventoryRouting.getIInventoryId(), IsOkEnum.NO.getValue());
//				inventoryRouting.setIsEnabled(true);
//				invPartService.updateByInvIdIsEffective(inventoryRouting.getIAutoId(), inventoryRouting.getIInventoryId(), IsOkEnum.NO.getValue());
//				// 批量将当前存货的全部失效
//				invPartService.updateByRoutingIdIsEffective(inventoryRouting.getIAutoId(), IsOkEnum.YES.getValue());
//				// 批量更改
//			}
//			inventoryRouting.update();
//			return true;
//		});
		return SUCCESS;
	}

	
	private void approveOperate(long formAutoId){
		// 先校验是否存在
		InventoryRouting inventoryRouting = inventoryRoutingService.findById(formAutoId);
		ValidationUtils.notNull(inventoryRouting, "工艺路线记录不存在");
		// 审核通过需要更改状态（当前存货）
		if (AuditStatusEnum.APPROVED.getValue() == inventoryRouting.getIAuditStatus()){
			// 全部设置为失效
			inventoryRoutingService.updateEnable(inventoryRouting.getIInventoryId(), IsOkEnum.NO.getValue());
			inventoryRouting.setIsEnabled(true);
			invPartService.updateByInvIdIsEffective(inventoryRouting.getIAutoId(), inventoryRouting.getIInventoryId(), IsOkEnum.NO.getValue());
			// 批量将当前存货的全部失效
			invPartService.updateByRoutingIdIsEffective(inventoryRouting.getIAutoId(), IsOkEnum.YES.getValue());
			inventoryRouting.update();
		}
		
	}
	
	/**
	 * 处理审批通过的其他业务操作，如有异常返回错误信息
	 */
	public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
		approveOperate(formAutoId);
		return null;
	}

	/**
	 * 处理审批不通过的其他业务操作，如有异常处理返回错误信息
	 */
	public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
		return null;
	}

	/**
	 * 实现反审之前的其他业务操作，如有异常返回错误信息
	 *
	 * @param formAutoId 单据ID
	 * @param isFirst    是否为审批的第一个节点
	 * @param isLast     是否为审批的最后一个节点
	 */
	public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
		return null;
	}

	/**
	 * 实现反审之后的其他业务操作, 如有异常返回错误信息
	 *
	 * @param formAutoId 单据ID
	 * @param isFirst    是否为审批的第一个节点
	 * @param isLast     是否为审批的最后一个节点
	 */
	public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
		if (isLast){
			// 先校验是否存在
			InventoryRouting inventoryRouting = inventoryRoutingService.findById(formAutoId);
			ValidationUtils.notNull(inventoryRouting, "工艺路线记录不存在");
			inventoryRouting.setIsEnabled(false);
			
			// 批量将当前存货的全部失效
			invPartService.updateByRoutingIdIsEffective(inventoryRouting.getIAutoId(), IsOkEnum.NO.getValue());
			inventoryRouting.update();
		}
		return null;
	}

	/**
	 * 提审前业务，如有异常返回错误信息
	 */
	public String preSubmitFunc(long formAutoId) {
		return null;
	}

	/**
	 * 提审后业务处理，如有异常返回错误信息
	 */
	public String postSubmitFunc(long formAutoId) {
		return null;
	}

	/**
	 * 撤回审核业务处理，如有异常返回错误信息
	 */
	public String postWithdrawFunc(long formAutoId) {
		return null;
	}

	/**
	 * 从审批中，撤回到已保存，业务实现，如有异常返回错误信息
	 */
	public String withdrawFromAuditting(long formAutoId) {
		return null;
	}

	/**
	 * 从已审核，撤回到已保存，前置业务实现，如有异常返回错误信息
	 */
	public String preWithdrawFromAuditted(long formAutoId) {
		return null;
	}

	/**
	 * 从已审核，撤回到已保存，业务实现，如有异常返回错误信息
	 */
	public String postWithdrawFromAuditted(long formAutoId) {
		return null;
	}

	/**
	 * 批量审批（审核）通过
	 *
	 * @param formAutoIds 单据IDs
	 * @return 错误信息
	 */
	public String postBatchApprove(List<Long> formAutoIds) {
		for (Long formAutoId : formAutoIds){
			approveOperate(formAutoId);
		}
		return null;
	}

	/**
	 * 批量审批（审核）不通过
	 *
	 * @param formAutoIds 单据IDs
	 * @return 错误信息
	 */
	public String postBatchReject(List<Long> formAutoIds) {
		return null;
	}

	/**
	 * 批量撤销审批
	 *
	 * @param formAutoIds 单据IDs
	 * @return 错误信息
	 */
	public String postBatchBackout(List<Long> formAutoIds) {
		return null;
	}

	public List<Record> getWhcodeAll(String q, Integer limit) {
		return dbTemplate("purchaseorderm.getWhcodeAll", Okv.by("q", q).set("limit", limit)).find();
	}
}
