package cn.rjtech.admin.bomm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.bomd.BomDService;
import cn.rjtech.admin.bomdata.BomDataService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.BomSourceTypeEnum;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.Util;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.DbTemplate;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 物料建模-BOM主表
 * @ClassName: BomMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-08 17:03
 */
public class BomMService extends BaseService<BomM> {
	private final BomM dao=new BomM().dao();
	
	@Inject
    private BomDService bomDService;
	@Inject
	private InventoryService inventoryService;
	@Inject
	private BomDataService bomDataService;
	
	@Override
	protected BomM dao() {
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
     * @param iType 新增来源;1.导入新增 2.手工新增
     * @param isEffective 是否生效：0. 否 1. 是
     * @param isDeleted 删除状态;0. 未删除 1. 已删除
	 * @return
	 */
	public Page<BomM> getAdminDatas(int pageNumber, int pageSize, String keywords, Integer iType, Boolean isEffective, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("iType",iType);
        sql.eqBooleanToChar("isEffective",isEffective);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cInvName", "cDocName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param bomM
	 * @return
	 */
	public Ret save(BomM bomM, Long userId, String userName, Date now, int auditState) {
		if(bomM==null) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		bomM.setICreateBy(userId);
		bomM.setCCreateName(userName);
		bomM.setDCreateTime(now);
		bomM.setISource(SourceEnum.MES.getValue());
		bomM.setIUpdateBy(userId);
		bomM.setCUpdateName(userName);
		bomM.setDUpdateTime(now);
		bomM.setIsDeleted(false);
		bomM.setIOrgId(getOrgId());
		bomM.setCOrgCode(getOrgCode());
		bomM.setCOrgName(getOrgName());
		bomM.setIsEffective(false);
		bomM.setIsView(false);
		// 设置默认审批流
		bomM.setIAuditStatus(auditState);
		//if(existsName(bomMaster.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		
		boolean success=bomM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(bomM.getIAutoId(), JBoltUserKit.getUserId(), bomM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param bomM
	 * @return
	 */
	public Ret update(BomM bomM, Long userId, String userName, Date now) {
		if(bomM==null || notOk(bomM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		BomM dbBomM=findById(bomM.getIAutoId());
		if(dbBomM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		bomM.setIUpdateBy(userId);
		bomM.setCUpdateName(userName);
		bomM.setDUpdateTime(now);
		boolean success=bomM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(bomM.getIAutoId(), JBoltUserKit.getUserId(), bomM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param bomM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(BomM bomM, Kv kv) {
		//addDeleteSystemLog(bomM.getIAutoId(), JBoltUserKit.getUserId(),bomM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param bomM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(BomM bomM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(BomM bomM, String column, Kv kv) {
		//addUpdateSystemLog(bomM.getIAutoId(), JBoltUserKit.getUserId(), bomM.getName(),"的字段["+column+"]值:"+bomM.get(column));
		/**
		switch(column){
		    case "isEffective":
		        break;
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}
	
	public List<JsTreeBean> getTreeDatas(Kv kv) {
		kv.set("orgId", getOrgId());
		List<Record> recordList = dbTemplate("bomm.findMasterDatas", kv).find();
		return createJsTreeBean(kv.getStr(BomM.ENABLEICON), recordList);
	}
	
	public List<JsTreeBean> createJsTreeBean(String enableIconStr, List<Record> recordList){
		List<JsTreeBean> trees = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(recordList)){
			
			List<Record> allList = dbTemplate("bomm.datas", Kv.by("orgId", getOrgId())).find();
			Map<Long, List<Record>> compareMap = allList.stream().filter(record -> StrUtil.isNotBlank(record.getStr(BomD.IPID))).collect(Collectors.groupingBy(record -> record.getLong(BomD.IPID)));
			
			for (Record record : recordList){
				Long id = record.getLong(BomM.IAUTOID);
				if (compareMap.containsKey(id)){
					recursiveTraversal(id, trees, compareMap.get(id), compareMap);
				}
				String text = record.getStr(BomM.CINVNAME);
				// 添加子件
				addJsTreeBean(id, id, "#", text, enableIconStr, trees);
			}
        }
		return trees;
	}
	
	/**
	 *  递归遍历所有子件
	 * @param pid  父id
	 * @param trees 集合
	 * @param compareList 子件集合
	 * @param compareMap  key=父id, value = 子件集合
	 */
	public void recursiveTraversal(Long pid, List<JsTreeBean> trees, List<Record> compareList, Map<Long, List<Record>> compareMap){
		for (Record record : compareList){
			Long id = record.getLong(BomM.IAUTOID);
			String cInvName = record.getStr(BomM.CINVNAME);
			// 子件id
			Long compareId = record.getLong(BomD.IINVPARTBOMMID);
			// 新id
			Long newId = JBoltSnowflakeKit.me.nextId();
			// 判断当前子件是否存在 子件
			if (compareMap.containsKey(id)){
				recursiveTraversal(newId, trees, compareMap.get(id), compareMap);
			}
			// 判断版本号是否为空
			if (ObjectUtil.isNotNull(compareId)){
				recursiveTraversal(newId, trees, compareMap.get(compareId), compareMap);
			}
			addJsTreeBean(id, newId, pid, cInvName, null, trees);
		}
	}
	
	/**
	 *
	 * @param sourceId 源id
	 * @param id	新id
	 * @param pid	父id
	 * @param cInvName	显示名称
	 * @param trees	集合
	 */
	public void addJsTreeBean(Long sourceId, Long id, Object pid, String cInvName, String enableIconStr, List<JsTreeBean> trees){
		String textName = Util.getTextName(cInvName);
		StringBuilder text = new StringBuilder(textName);
		if (StrUtil.isNotBlank(enableIconStr)){
			String enableIcon = enableIconStr;
			enableIcon = enableIcon.replace("?", String.valueOf(sourceId));
			text.append(enableIcon);
		}
		JsTreeBean jsTreeBean = new JsTreeBean(id, pid, text.toString(), true);
		jsTreeBean.setData(sourceId);
		trees.add(jsTreeBean);
	}
	
	public Page<Record> getVersionRecord(Integer pageNumber, Integer pageSize, Kv kv) {
		Page<Record> page = dbTemplate("bomm.getVersionRecord", kv.set("orgId", getOrgId())).paginate(pageNumber, pageSize);
        changeRecord(page.getList());
		return page;
	}
	
	/**
	 * 更改是否显示字段
	 * @param id
	 * @return
	 */
	public Ret updateIsView(Long id){
		ValidationUtils.notNull(id, JBoltMsg.PARAM_ERROR);
		return toggleBoolean(id, BomM.ISVIEW);
	}
    
    public void changeRecord(List<Record> recordList){
        if (CollectionUtil.isEmpty(recordList)){
            return;
        }
        for (Record record : recordList){
            AuditStatusEnum auditStatusEnum = AuditStatusEnum.toEnum(record.getInt(BomM.IAUDITSTATUS));
            record.set(BomM.AUDITSTATUSSTR, auditStatusEnum.getText());
            
            BomSourceTypeEnum bomSourceTypeEnum = BomSourceTypeEnum.toEnum(record.getInt(BomM.ITYPE));
            record.set(BomM.TYPENAME, bomSourceTypeEnum.getText());
        }
    }
    
    public void setBomRecord(Long id, Boolean isChildren, Boolean isView, Kv kv){
		if (ObjectUtil.isNull(id)){
			return;
		}
        String cInvCode = null;
        String cInvName = null;
        Long iAutoId = null;
        Long iInventoryId = null;
        Integer code = null;
		Integer iCodeLevel = null;
		String auditStatusStr = null;
		String dDisableDate = null;
		String dEnableDate = null;
		String cVersion = null;
        if (!isChildren && ObjectUtil.isNotNull(id)){
            BomM bomM = findById(id);
            if (ObjectUtil.isNull(bomM)){
				BomD bomD = bomDService.findById(id);
				ValidationUtils.notNull(bomD, "未找到子件");
				code = ObjectUtil.isNull(kv.getInt(BomD.CCODE)) ? 1 : kv.getInt(BomD.CCODE);
				iCodeLevel = code;
				if (ObjectUtil.isNotNull(bomD.getIInvPartBomMid())){
					bomM  = findById(bomD.getIInvPartBomMid());
				}
				if (ObjectUtil.isNull(bomM)){
					bomM = findById(bomD.getIPid());
				}
			}
            Integer iAuditStatus = bomM.getIAuditStatus();
            AuditStatusEnum auditStatusEnum = AuditStatusEnum.toEnum(iAuditStatus);
            ValidationUtils.notNull(auditStatusEnum, "未知状态类型");
			auditStatusStr = auditStatusEnum.getText();
			dDisableDate = DateUtil.formatDate(bomM.getDDisableDate());
			dEnableDate = DateUtil.formatDate(bomM.getDEnableDate());
			cVersion = bomM.getCVersion();
			
            iInventoryId = bomM.getIInventoryId();
            cInvCode = bomM.getCInvCode();
            cInvName = bomM.getCInvName();
			iAutoId = bomM.getIAutoId();
        }else if (ObjectUtil.isNotNull(id)){
            BomD bomD = bomDService.findById(id);
			code = Integer.valueOf(bomD.getCCode());
			iCodeLevel = Integer.valueOf(bomD.getICodeLevel());
            iInventoryId = bomD.getIInventoryId();
            cInvCode = bomD.getCInvCode();
            cInvName = bomD.getCInvName();
			iAutoId = bomD.getIAutoId();
            if (ObjectUtil.isNotNull(bomD.getIInvPartBomMid())){
				iAutoId = bomD.getIInvPartBomMid();
			}
        }
        if (isView){
			kv.set(BomM.IAUTOID, iAutoId);
			kv.set(BomD.CCODE, code);
			kv.set(BomD.ICODELEVEL, iCodeLevel);
			kv.set(BomM.AUDITSTATUSSTR, auditStatusStr);
			kv.set(BomM.DDISABLEDATE, dDisableDate);
			kv.set(BomM.DENABLEDATE, dEnableDate);
			kv.set(BomM.CVERSION, cVersion);
		}
		Boolean isAdd = kv.getBoolean("isAdd");
		if (ObjectUtil.isNotNull(isAdd) && isAdd){
        	kv.set(BomD.IPID, id);
		}
        
        kv.set(BomM.IINVENTORYID, iInventoryId);
        kv.set(BomM.CINVCODE, cInvCode);
        kv.set(BomM.CINVNAME, cInvName);
    }
    
    public List<Record> findByVersionList(Long orgId){
		return dbTemplate("bomm.findByVersion", Okv.by("orgId", orgId)).find();
	}
	
	public List<Record> findByVersionList(Long orgId, Long invId){
		return dbTemplate("bomm.findByVersion", Okv.by("orgId", orgId).set("invId", invId)).find();
	}
	
	public Map<Long, Record> findByVersionMap(Long orgId){
		List<Record> versionList = findByVersionList(orgId);
		if (CollectionUtil.isEmpty(versionList)){
			return new HashMap<>();
		}
		return versionList.stream().collect(Collectors.toMap(record -> record.getLong(BomM.IINVENTORYID), record -> record));
	}
	
	public Ret audit(Long bomMasterId, Integer status) {
		ValidationUtils.notNull(bomMasterId, JBoltMsg.JBOLTTABLE_IS_BLANK);
		ValidationUtils.notNull(status, JBoltMsg.JBOLTTABLE_IS_BLANK);
		BomM bomMaster = findById(bomMasterId);
		ValidationUtils.notNull(bomMaster, JBoltMsg.DATA_NOT_EXIST);
		DateTime date = DateUtil.date();
		bomMaster.setIAuditStatus(status);
		bomMaster.setDAuditTime(date);
		
		tx(() -> {
			bomMaster.update();
//			-----------推送U8-----------------
			
			
			
			
			// 删除母件下所有子件数据
			return true;
		});
		return SUCCESS;
	}
	
	public Ret del(Long bomMasterId) {
		ValidationUtils.notNull(bomMasterId, JBoltMsg.PARAM_ERROR);
		BomM bomMaster = findById(bomMasterId);
		ValidationUtils.notNull(bomMaster, JBoltMsg.DATA_NOT_EXIST);
		tx(() -> {
			// 校验审批中或已审批的数据不能进行删除
			Integer iAuditStatus = bomMaster.getIAuditStatus();
//			AuditStatusEnum auditStatusEnum = AuditStatusEnum.toEnum(iAuditStatus);
//			ValidationUtils.isTrue((AuditStatusEnum.NOT_AUDIT.getValue()==iAuditStatus || AuditStatusEnum.REJECTED.getValue()==iAuditStatus), "该物料清单状态为【"+auditStatusEnum.getText()+"】不能进行删除");
			// 校验母件是否有被其他子件引用到
			List<BomM> bomMList = findBomByPartBomMid(bomMasterId);
			if (CollectionUtil.isNotEmpty(bomMList)){
				List<String> invCodeList = bomMList.stream().map(BomM::getCInvCode).collect(Collectors.toList());
				String format = String.format("该半成品版本记录，有存在其他地方使用【%s】", CollUtil.join(invCodeList, ","));
				ValidationUtils.isTrue(CollectionUtil.isEmpty(bomMList), format);
			}
			// 查询子件，将子件状态改为删除
			List<BomD> compareList = bomDService.queryBomCompareList(bomMasterId, BomD.IBOMMID);
			compareList.forEach(bomD -> bomD.setIsDeleted(true));
			bomDService.batchUpdate(compareList);
			// 删除母件
			bomMaster.setIsDeleted(true);
			bomMaster.setIUpdateBy(JBoltUserKit.getUserId());
			bomMaster.setCUpdateName(JBoltUserKit.getUserName());
			bomMaster.setDUpdateTime(DateUtil.date());
			// 设置为失效
			bomMaster.setIsEffective(false);
			bomMaster.update();
			return true;
		});
		return SUCCESS;
	}
	
	public boolean isOverlapping(BomM bomM, Record otherBom) {
		
		Date dEnableDate = otherBom.getDate(BomM.DENABLEDATE);
		Date dDisableDate = otherBom.getDate(BomM.DDISABLEDATE);
		
		Date currentDate = bomM.getDEnableDate();
		Date endDate = bomM.getDDisableDate();
		
		//如果当前日期在指定范围内，则不允许添加 启用日期在指定范围内不允许添加
		if(currentDate.compareTo(dEnableDate) >=0 && currentDate.compareTo(dDisableDate)<=0) {
			return false;
		}
		
		if (endDate.compareTo(dEnableDate) >=0 && endDate.compareTo(dDisableDate)<=0){
			return false;
		}
		return true;
	}
	
	public List<Record> findByInvId(Long orgId, Long invId, Long iAutoId){
		ValidationUtils.notNull(invId, "存货id不能为空");
		Okv okv = Okv.by("orgId", orgId)
				.set(BomM.IINVENTORYID, invId)
				.set(BomM.IAUTOID, iAutoId);
		return dbTemplate("bomm.findByInvId", okv).find();
	}
	
	public List<Record> findVersionByInvId(Long orgId, Long invId, Long iAutoId){
		ValidationUtils.notNull(invId, "存货id不能为空");
		Okv okv = Okv.by("orgId", orgId)
				.set(BomM.IINVENTORYID, invId)
				.set(BomM.IAUTOID, iAutoId);
		return dbTemplate("bomm.findVersionByInvId", okv).find();
	}
	
	public String findMaxVersionByInvId(Long orgId, Long invId){
		Okv okv = Okv.by("orgId", orgId).set(BomM.IINVENTORYID, invId);
		return dbTemplate("bomm.findMaxVersionByInvId", okv).queryStr();
	}
	
	public String getNextVersion(Long orgId, Long invId){
		String maxVersion = findMaxVersionByInvId(orgId, invId);
		// 为空直接返回 最初版本
		if (StrUtil.isBlank(maxVersion)){
			return "A/01";
		}
		
		String[] parts = maxVersion.split("/");
		ValidationUtils.isTrue(parts.length == 2, "版本号格式不正确");
		
		String prefix = parts[0];
		int number = Integer.parseInt(parts[1]);
		ValidationUtils.isTrue(!(number < 0 || number > 10), "版本号数字流水号不合法");
		
		if (number < 10){
			number+=1;
			String format = String.format("%02d", number);
			return prefix.concat("/").concat(format);
		}
		char nextPrefix = (char) (prefix.charAt(0) + 1);
		String netPrefixStr = String.valueOf(nextPrefix);
		return netPrefixStr.concat("/01");
	}
	
	public Ret saveCopy(Long bomMasterId, String dDisableDate, String cVersion) {
		ValidationUtils.notBlank(dDisableDate, JBoltMsg.JBOLTTABLE_IS_BLANK);
		ValidationUtils.notBlank(cVersion, JBoltMsg.JBOLTTABLE_IS_BLANK);
		ValidationUtils.notNull(bomMasterId, JBoltMsg.PARAM_ERROR);
		BomM bomMaster = findById(bomMasterId);
		ValidationUtils.notNull(bomMaster, JBoltMsg.DATA_NOT_EXIST);
		ValidationUtils.isTrue(!cVersion.equals(bomMaster.getCVersion()), "版本号不能一致");
		// 获取母件所有子件集合
		tx(() -> {
			saveCopyBomMaster(bomMasterId, cVersion, dDisableDate, bomMaster);
			return true;
		});
		return SUCCESS;
	}
	
	private void saveCopyBomMaster(Long bomMasterId, String cVersion, String dDisableDate, BomM bomMaster){
		// 设置新的id
		long newBomMasterId = JBoltSnowflakeKit.me.nextId();
		DateTime now = DateUtil.date();
		bomMaster.setIAutoId(newBomMasterId);
		bomMaster.setDDisableDate(DateUtil.parseDate(dDisableDate));
		bomMaster.setDAuditTime(null);
		// 设置copy前的ID
		bomMaster.setICopyFromId(bomMasterId);
		bomMaster.setDAuditTime(now);
		
		// 校验
		checkBomDateOrVersion(cVersion, bomMaster);
		List<BomD> compareList = bomDService.queryBomCompareList(bomMasterId, BomD.IBOMMID);
		compareList.forEach(bomD -> bomD.setIBomMid(newBomMasterId));
		bomDService.batchSave(compareList);
		save(bomMaster,JBoltUserKit.getUserId(),JBoltUserKit.getUserName(), now, AuditStatusEnum.AWAIT_AUDIT.getValue());
		
		
	}

	
	public void  checkBomDateOrVersion(String cVersion, BomM bomM){
		// 校验版本号
		/*if (StrUtil.isNotBlank(cVersion)){
			List<Record> versionList = bomMService.findVersionByInvId(getOrgId(), bomM.getIInventoryId(), bomM.getIAutoId());
			if (CollectionUtil.isNotEmpty(versionList)){
			
			}
		}*/
		
		// 校验日期 和 版本号
		List<Record> invBomList = findByInvId(getOrgId(), bomM.getIInventoryId(), bomM.getIAutoId());
		if (CollectionUtil.isNotEmpty(invBomList)){
			invBomList.forEach(record -> {
				boolean overlapping = isOverlapping(bomM, record);
				ValidationUtils.isTrue(overlapping, "母件不可重复创建版本，启用日期停用日期重叠！");
				String version = record.getStr(BomM.CVERSION);
				if (StrUtil.isNotBlank(cVersion)){
					ValidationUtils.isTrue(!cVersion.equals(version), "该版本号已存在！");
				}
			});
		}
	}
	
	public Ret submitForm(String formJsonData, String tableJsonData) {
		ValidationUtils.notBlank(formJsonData, JBoltMsg.PARAM_ERROR);
		JSONObject formData = JSONObject.parseObject(formJsonData);
		BomM bomMaster = JSONObject.parseObject(formJsonData, BomM.class);
		Long userId = JBoltUserKit.getUserId();
		String userName = JBoltUserKit.getUserName();
		DateTime now = DateUtil.date();
		// 主键id为空为 新增或者为修改
		if (ObjectUtil.isNull(bomMaster.getIAutoId())){
			ValidationUtils.notBlank(tableJsonData, JBoltMsg.PARAM_ERROR);
			JSONArray tableData = JSONObject.parseArray(tableJsonData);
			// 校验数据
			verification(formData, tableData);
			return saveForm(bomMaster, tableData, userId, userName, now);
		}
		return updateForm(bomMaster, userId, userName, now);
	}
	
	public Ret saveForm(BomM bomMaster, JSONArray tableData, Long userId, String userName, DateTime now){
		// 设置主键id
		long bomMasterId = JBoltSnowflakeKit.me.nextId();
		bomMaster.setIAutoId(bomMasterId);
		List<BomD> bomCompareList =getBomCompareList(bomMasterId, tableData);
		Inventory inventory = inventoryService.findById(bomMaster.getIInventoryId());
		ValidationUtils.notNull(inventory, "产成品存货记录不存在");
		
		checkBomDateOrVersion(bomMaster.getCVersion(), bomMaster);
		
		tx(() -> {
			bomMaster.setIType(BomSourceTypeEnum.IMPORT_TYPE_ADD.getValue());
			bomMaster.setCInvName(inventory.getCInvName());
			bomMaster.setCInvCode(inventory.getCInvCode());
			if(StrUtil.isBlank(bomMaster.getCVersion())){
				bomMaster.setCVersion(getNextVersion(getOrgId(), bomMaster.getIInventoryId()));
			}
			save(bomMaster, userId, userName, now, AuditStatusEnum.NOT_AUDIT.getValue());
//			bomDService.batchSave(bomCompareList);
			BomData bomData = bomDataService.create(bomMaster.getIAutoId(), tableData.toJSONString());
			bomDataService.save(bomData);
			return true;
		});
		return SUCCESS;
	}
	
	public Ret updateForm(BomM bomMaster, Long userId, String userName, DateTime now){
		BomM oldBomMaster = findById(bomMaster.getIAutoId());
		Integer iAuditStatus = oldBomMaster.getIAuditStatus();
		AuditStatusEnum auditStatusEnum = AuditStatusEnum.toEnum(iAuditStatus);
		ValidationUtils.isTrue((AuditStatusEnum.NOT_AUDIT.getValue()==iAuditStatus || AuditStatusEnum.REJECTED.getValue()==iAuditStatus), "该物料清单状态为【"+auditStatusEnum.getText()+"】不能进行修改");
		// 复制
		copyBomMaster(bomMaster, oldBomMaster);
		tx(() -> {
			update(oldBomMaster, userId, userName, now);
			return true;
		});
		return SUCCESS;
	}
	
	public void verification(JSONObject formData, JSONArray tableData){
		verificationOfForm(formData);
		verificationOfTable(formData.getString(BomMaster.IINVENTORYID), tableData);
	}
	
	public void verificationOfForm(JSONObject formData){
		String inventoryId = formData.getString(BomMaster.IINVENTORYID);
		String equipmentModelId = formData.getString(BomMaster.IEQUIPMENTMODELID);
		// 普通校验
		ValidationUtils.notBlank(inventoryId, "产品存货编码为空");
		ValidationUtils.notBlank(formData.getString(BomMaster.CBOMVERSION), "版本/版次为空");
//		ValidationUtils.notBlank(equipmentModelId, "机型为空");
		ValidationUtils.notBlank(formData.getString(BomMaster.CDOCNAME), "文件名称为空");
		ValidationUtils.notBlank(formData.getString(BomMaster.CDOCCODE), "文件编码为空");
		ValidationUtils.notBlank(formData.getString(BomMaster.DENABLEDATE), "启用日期为空");
		ValidationUtils.notBlank(formData.getString(BomMaster.DDISABLEDATE), "停用日期为空");
		// 校验当前存货是否为当前选择机型下的
		Inventory inventory = inventoryService.findById(inventoryId);
		ValidationUtils.notNull(inventory, JBoltMsg.DATA_NOT_EXIST);
//		ValidationUtils.isTrue(equipmentModelId.equals(String.valueOf(inventory.getIEquipmentModelId())), "机型跟产品编码不匹配");
	}
	
	/**
	 *
	 * @param finishedProductId 产品id
	 * @param tableData 半成品，部品，原材料
	 */
	public void verificationOfTable(String finishedProductId, JSONArray tableData){
		List<String> codes = new ArrayList<>();
		for (int i=0; i<tableData.size(); i++){
			JSONObject row = tableData.getJSONObject(i);
			boolean checkInvCodeFlag = isAdd(row);
			ValidationUtils.isTrue(!checkInvCodeFlag, "第"+(i+1)+"行存货编码未解析出来");
			verificationOfTableRow(finishedProductId, row);
			String code = getCode(row);
			codes.add(code);
			// 记录
			row.put("code", code);
		}
		Set<String> codeSet = new HashSet<>(codes);
		ValidationUtils.isTrue(codes.size() == codeSet.size(), "编码栏不能出现重复");
	}
	
	public void verificationOfTableRow(String finishedProductId, JSONObject row){
		// 部品
		String invItemId = row.getString(BomCompare.INVITEMID.toLowerCase());
		// 片料
		String blankingItemId = row.getString(BomCompare.BLANKINGITEMID.toLowerCase());
		// 卷料
		String originalItemId = row.getString(BomCompare.ORIGINALITEMID.toLowerCase());
		// 分条料
		String slicingInvItemId = row.getString(BomCompare.SLICINGINVITEMID.toLowerCase());
		
		// 校验母件成品编码不能跟子件编码一致
		ValidationUtils.isTrue(!finishedProductId.equals(invItemId), "部品存货不能跟成品存货选择一致");
		ValidationUtils.isTrue(!finishedProductId.equals(blankingItemId), "片料（落料）存货不能跟成品存货选择一致");
		ValidationUtils.isTrue(!finishedProductId.equals(originalItemId), "卷料（原材料）存货不能跟成品存货选择一致");
		ValidationUtils.isTrue(!finishedProductId.equals(slicingInvItemId), "分条料存货不能跟成品存货选择一致");
		
		// 校验部品存货是否存在一致
		ValidationUtils.isTrue(!(StrUtil.isNotBlank(invItemId) && invItemId.equals(blankingItemId)), "部品存货不能跟片料（落料）存货选择一致");
		ValidationUtils.isTrue(!(StrUtil.isNotBlank(invItemId) && invItemId.equals(originalItemId)), "部品存货不能跟卷料（原材料）存货选择一致");
		ValidationUtils.isTrue(!(StrUtil.isNotBlank(invItemId) && invItemId.equals(slicingInvItemId)), "部品存货不能跟分条料存货选择一致");
		
		// 校验（原材料）存货是否存在一致
		ValidationUtils.isTrue(!(StrUtil.isNotBlank(originalItemId) && originalItemId.equals(slicingInvItemId)), "卷料（原材料）存货不能跟分条料存货选择一致");
		ValidationUtils.isTrue(!(StrUtil.isNotBlank(originalItemId) && originalItemId.equals(blankingItemId)), "卷料（原材料）存货不能跟片料（落料）存货选择一致");
		// 分条料 校验
		ValidationUtils.isTrue(!(StrUtil.isNotBlank(slicingInvItemId) && slicingInvItemId.equals(blankingItemId)), "分条料存货不能跟片料（落料）存货选择一致");
		
		String code1 = row.getString(BomCompare.CODE1);
		String code2 = row.getString(BomCompare.CODE2);
		String code3 = row.getString(BomCompare.CODE3);
		String code4 = row.getString(BomCompare.CODE4);
		String code5 = row.getString(BomCompare.CODE5);
		String code6 = row.getString(BomCompare.CODE6);
		
		// 校验编码栏是否都为空
		boolean codeFlag = StrUtil.isBlank(code1)
				&& StrUtil.isBlank(code2)
				&& StrUtil.isBlank(code3)
				&& StrUtil.isBlank(code4)
				&& StrUtil.isBlank(code5)
				&& StrUtil.isBlank(code6);
		ValidationUtils.isTrue(!codeFlag, "选择存货后，编码栏不能为空");
		int count = 0;
		if (StrUtil.isNotBlank(code1)){
			count+=1;
		}
		
		if (StrUtil.isNotBlank(code2)){
			count+=1;
		}
		if (StrUtil.isNotBlank(code3)){
			count+=1;
		}
		if (StrUtil.isNotBlank(code4)){
			count+=1;
		}
		if (StrUtil.isNotBlank(code5)){
			count+=1;
		}
		if (StrUtil.isNotBlank(code6)){
			count+=1;
		}
		ValidationUtils.isTrue(count==1, "编号栏只能填写一列");
		
		if (StrUtil.isNotBlank(invItemId)){
			ValidationUtils.notNull(row.getBigDecimal(BomCompare.INVQTY.toLowerCase()), "部品QTY不能为空");
		}
		
	}
	
	
	/**
	 * 拼接编码栏确保唯一
	 * @param row
	 * @return
	 */
	public String getCode(JSONObject row){
		return getCode(row.getString(BomCompare.CODE1), row.getString(BomCompare.CODE2), row.getString(BomCompare.CODE3), row.getString(BomCompare.CODE4)
				,row.getString(BomCompare.CODE5), row.getString(BomCompare.CODE6));
	}
	
	/**
	 * 是否添加
	 * @param row
	 * @return
	 */
	private boolean isAdd(JSONObject row){
		// 部品
		String invItemId = row.getString(BomCompare.INVITEMID.toLowerCase());
		// 片料
		String blankingItemId = row.getString(BomCompare.BLANKINGITEMID.toLowerCase());
		// 卷料
		String originalItemId = row.getString(BomCompare.ORIGINALITEMID.toLowerCase());
		// 分条料
		String slicingInvItemId = row.getString(BomCompare.SLICINGINVITEMID.toLowerCase());
		// 判断物料是否都为空，为空全部跳过
		return StrUtil.isBlank(blankingItemId) && StrUtil.isBlank(invItemId) && StrUtil.isBlank(originalItemId) &&StrUtil.isBlank(slicingInvItemId);
	}
	
	public String getPerCode(String code){
		String split = "-";
		if (!code.contains(split)) return null;
		return code.substring(0, code.lastIndexOf(split));
	}
	
	/**
	 * 获取当前部品属于第几层级
	 * @return
	 */
	public String getCode(String code1, String code2, String code3, String code4, String code5, String code6){
		if (StrUtil.isNotBlank(code6)){
			return code6;
		}
		if (StrUtil.isNotBlank(code5)){
			return code5;
		}
		if (StrUtil.isNotBlank(code4)){
			return code4;
		}
		if (StrUtil.isNotBlank(code3)){
			return code3;
		}
		if (StrUtil.isNotBlank(code2)){
			return code2;
		}
		return code1;
	}
	
	private void copyBomMaster(BomM bomMaster, BomM oldBomMaster){
		// 机型id
		oldBomMaster.setIEquipmentModelId(bomMaster.getIEquipmentModelId());
		// 文件编码
		oldBomMaster.setCDocCode(bomMaster.getCDocCode());
		// 文件名称
		oldBomMaster.setCDocName(bomMaster.getCDocName());
		// 版本
		oldBomMaster.setCVersion(bomMaster.getCVersion());
		// 启用日期
		oldBomMaster.setDEnableDate(bomMaster.getDEnableDate());
		// 停用日期
		oldBomMaster.setDDisableDate(bomMaster.getDDisableDate());
		// 序号1
		oldBomMaster.setCNo1(bomMaster.getCNo1());
		// 序号2
		oldBomMaster.setCNo2(bomMaster.getCNo2());
		// 设变号1
		oldBomMaster.setCDcNo1(bomMaster.getCDcNo1());
		// 设变号2
		oldBomMaster.setCDcNo2(bomMaster.getCDcNo2());
		// 设备日期1
		oldBomMaster.setDDcDate1(bomMaster.getDDcDate1());
		// 设备日期2
		oldBomMaster.setDDcDate2(bomMaster.getDDcDate2());
		// 存货id
		oldBomMaster.setIInventoryId(bomMaster.getIInventoryId());
		// 客户ID
		oldBomMaster.setICustomerId(bomMaster.getICustomerId());
		// 共用件备注
		oldBomMaster.setCCommonPartMemo(bomMaster.getCCommonPartMemo());
	}
	
	private List<BomD> getBomCompareList(long bomMasterId, JSONArray tableData){
		// 获取有效bom版本
		Map<Long, Record> effectiveBomMap = getEffectiveBomMap(getOrgId());
		// 获取有效bom子件
		Map<Long, List<Record>> effectiveBomCompareMap = bomDService.getEffectiveBomCompareMap(getOrgId());
		List<BomD> bomCompareList = new ArrayList<>();
		// 记录每一个编码栏存在的存货
		Map<String, List<BomD>> CodeBomCompareMap = new HashMap<>();
		// 记录部品存货栏编码
		Set<String> codeSet = new HashSet<>();
		
		for (int i=0; i<tableData.size(); i++){
			List<BomD> rowCompareList = new ArrayList<>();
			JSONObject row = tableData.getJSONObject(i);
			// 编码
			String code = getCode(row.getString(BomCompare.CODE1),
					row.getString(BomCompare.CODE2),
					row.getString(BomCompare.CODE3),
					row.getString(BomCompare.CODE4),
					row.getString(BomCompare.CODE5),
					row.getString(BomCompare.CODE6));
			
			int invLev;
			if (code.equals(row.getString(BomCompare.CODE6))){
				invLev = 6;
			}else if (code.equals(row.getString(BomCompare.CODE5))){
				invLev = 5;
			}else if (code.equals(row.getString(BomCompare.CODE4))){
				invLev = 4;
			}else if (code.equals(row.getString(BomCompare.CODE3))){
				invLev = 3;
			}else if (code.equals(row.getString(BomCompare.CODE2))){
				invLev = 2;
			}else{
				invLev = 1;
				codeSet.add(code);
			}
			
			BomD compare = null;
			Long invId = row.getLong(BomCompare.INVITEMID.toLowerCase());
			// 半成品/部品
			if (ObjectUtil.isNotNull(invId)){
				BigDecimal invQty = row.getBigDecimal(BomCompare.INVQTY.toLowerCase());
				BigDecimal invWeight = row.getBigDecimal(BomCompare.INVWEIGHT.toLowerCase());
				Long vendorId = row.getLong(BomCompare.IVENDORID.toLowerCase());
				Boolean isOutSourced = Boolean.valueOf(row.getString(BomCompare.ISOUTSOURCED.toLowerCase()));
				String cMemo = row.getString(BomCompare.CMEMO.toLowerCase());
				compare = bomDService.createCompare(null, bomMasterId, null, invId, vendorId, invLev, 0, code, cMemo, invQty, invWeight, isOutSourced);
				rowCompareList.add(compare);
			}
			
			BomD blankBomCompare = null;
			Long blankingItemId = row.getLong(BomCompare.BLANKINGITEMID.toLowerCase());
			// 片料
			if (ObjectUtil.isNotNull(blankingItemId)){
				Long pid = null;
				if (ObjectUtil.isNotNull(compare)){
					pid = compare.getIAutoId();
				}
				BigDecimal blankingQty = row.getBigDecimal(BomCompare.BLANKINGQTY.toLowerCase());
				BigDecimal blankingWeight = row.getBigDecimal(BomCompare.BLANKINGWEIGHT.toLowerCase());
				blankBomCompare = bomDService.createCompare(null, bomMasterId, pid, blankingItemId, null, invLev, 1, code, null, blankingQty, blankingWeight, false);
				rowCompareList.add(blankBomCompare);
			}
			
			
			// 分条料
			BomD slicingBomCompare = null;
			Long slicingInvItemId = row.getLong(BomCompare.SLICINGINVITEMID.toLowerCase());
			if (ObjectUtil.isNotNull(slicingInvItemId)){
				Long pid = null;
				if (ObjectUtil.isNotNull(compare)){
					pid = compare.getIAutoId();
				}
				if (ObjectUtil.isNotNull(blankBomCompare)){
					pid = blankBomCompare.getIAutoId();
				}
				
				BigDecimal slicingQty = row.getBigDecimal(BomCompare.SLICINGQTY.toLowerCase());
				BigDecimal slicingWeight = row.getBigDecimal(BomCompare.SLICINGWEIGHT.toLowerCase());
				slicingBomCompare = bomDService.createCompare(null, bomMasterId, pid, blankingItemId, null, invLev, 2, code, null, slicingQty, slicingWeight, false);
				rowCompareList.add(slicingBomCompare);
			}
			
			// 卷料(原材料)
			Long originalItemId = row.getLong(BomCompare.ORIGINALITEMID.toLowerCase());
			if (ObjectUtil.isNotNull(originalItemId)){
				Long pid = null;
				if (ObjectUtil.isNotNull(compare)){
					pid = compare.getIAutoId();
				}
				if (ObjectUtil.isNotNull(blankBomCompare)){
					pid = blankBomCompare.getIAutoId();
				}
				if (ObjectUtil.isNotNull(slicingBomCompare)){
					pid = slicingBomCompare.getIAutoId();
				}
				BigDecimal originalQty = row.getBigDecimal(BomCompare.ORIGINALQTY.toLowerCase());
				BigDecimal originalWeight = row.getBigDecimal(BomCompare.ORIGINALWEIGHT.toLowerCase());
				BomD originalBomCompare = bomDService.createCompare(null, bomMasterId, pid, blankingItemId, null, invLev, 3, code, null, originalQty, originalWeight, false);
				
				rowCompareList.add(originalBomCompare);
			}
			CodeBomCompareMap.put(code, rowCompareList);
			/*// 记录当前行存在多少存货，存在多个则需要校验，下面是否存在子件，存在则报错
			if (bomCompareList.size() > 1){

			}*/
		}
		List<String> codeList = new ArrayList<>(codeSet);
		Collections.sort(codeList);
		for (String code : codeList){
			List<BomD> bomDList = CodeBomCompareMap.get(code);
			List<String> nextLevelCodes = findNextLevelCodes(code, CodeBomCompareMap.keySet());
			BomD firstBomD = getRawTypeBomD(bomDList, 0);
			ValidationUtils.notNull(firstBomD, "未获取到半成品数据");
			firstBomD.setIPid(bomMasterId);
			
			if (CollUtil.isNotEmpty(nextLevelCodes)){
				ValidationUtils.isTrue(bomDList.size() == 1, "编码【"+code+"】已存在多个存货，不允许存在下一级");
				continue;
			}
			
			// 添加
			bomCompareList.add(firstBomD);
			Long inventoryId = firstBomD.getIInventoryId();
			// 存在则一个个去校验是否真确
			if (effectiveBomMap.containsKey(inventoryId)){
				String cVersion = firstBomD.getCVersion();
				setBomVersion(firstBomD, effectiveBomMap);
				List<Record> recordList = effectiveBomCompareMap.get(firstBomD.getIInvPartBomMid());
				List<Long> invIds = recordList.stream().map(record -> record.getLong(BomD.IINVENTORYID)).collect(Collectors.toList());
				List<Long> compareInvIds = bomDList.stream().map(BomD::getIInventoryId).collect(Collectors.toList());
				ValidationUtils.isTrue(recordList.size() == (bomDList.size()-1), errorMsg(0, code, cVersion));
				ValidationUtils.isTrue(invIds.equals(compareInvIds), errorMsg(1, code, cVersion));
			}else{
			
			}
		}
		
		for (String code : CodeBomCompareMap.keySet()){
			List<BomD> bomDList = CodeBomCompareMap.get(code);
			if (bomDList.size() > 1){
				List<String> nextLevelCodes = findNextLevelCodes(code, CodeBomCompareMap.keySet());
			}
		}
		
		return bomCompareList;
	}
	
	private void test(List<BomD> bomDList, Map<Long, Record> effectiveBomMap, Map<Long, List<Record>> effectiveBomCompareMap, BomD firstBomD){
		Long inventoryId = firstBomD.getIInventoryId();
		String cInvLev = firstBomD.getCInvLev();
		// 存在则一个个去校验是否真确
		if (effectiveBomMap.containsKey(inventoryId)){
			String cVersion = firstBomD.getCVersion();
			setBomVersion(firstBomD, effectiveBomMap);
			List<Record> recordList = effectiveBomCompareMap.get(firstBomD.getIInvPartBomMid());
			List<Long> invIds = recordList.stream().map(record -> record.getLong(BomD.IINVENTORYID)).collect(Collectors.toList());
			List<Long> compareInvIds = bomDList.stream().map(BomD::getIInventoryId).collect(Collectors.toList());
			ValidationUtils.isTrue(recordList.size() == (bomDList.size()-1), errorMsg(0, cInvLev, cVersion));
			ValidationUtils.isTrue(invIds.equals(compareInvIds), errorMsg(1, cInvLev, cVersion));
		}else{
		
		}
	}
	
	private String errorMsg(int type, String code, String cVersion){
		if (type == 0){
			return String.format("编码栏【%s】,母件版本号【%s】子件个数不同，无法保存！", code, cVersion);
		}else if (type == 1){
			return String.format("编码栏【%s】,母件版本号【%s】子件不相同，无法保存！", code, cVersion);
		}
		return null;
	}
	
	private BomD getRawTypeBomD(List<BomD> bomDList, Integer iRawType){
		for (BomD bomD : bomDList){
			//  原材料类型：0.部品 1. 片料 2. 分条料 3. 卷料
			if (iRawType == bomD.getIRawType()){
				return bomD;
			}
		}
		return null;
	}
	
	private void checkBomCompareList(){
	
	}
	
	
	private void addBomCompare(List<BomD> bomDList, List<BomD> bomCompareList, Long pid, Boolean isFristAdd){
		
		BomD bomCompare = getRawTypeBomD(bomDList, 0);
		if (ObjectUtil.isNotNull(bomCompare)){
			if (isFristAdd){
				bomCompare.setIPid(pid);
				bomCompareList.add(bomCompare);
			}
		}
		
		// 片料
		BomD blankBomCompare = getRawTypeBomD(bomDList, 1);
		if (ObjectUtil.isNotNull(blankBomCompare)){
		
		}
		// 分条料
		BomD slicingBomCompare = getRawTypeBomD(bomDList, 2);
		if (ObjectUtil.isNotNull(slicingBomCompare)){
		
		}
		// 卷料
		BomD originalBomCompare = getRawTypeBomD(bomDList, 3);
		if (ObjectUtil.isNotNull(originalBomCompare)){
		
		}
	}
	
	public void setBomVersion(BomD compare, Map<Long, Record> effectiveBomMap){
		// 判断部品存货是否存在版本，不存在则添加，存在则添加 版本号
		if (effectiveBomMap.containsKey(compare.getIInventoryId())){
			Record record = effectiveBomMap.get(compare.getIInventoryId());
			compare.setCVersion(record.getStr(BomM.CVERSION));
			compare.setIInvPartBomMid(record.getLong(BomM.IAUTOID));
		}
	}
	
	public List<Record> getEffectiveBomList(Long orgId){
		return dbTemplate("bomm.getEffectiveBomM", Okv.by("orgId", orgId)).find();
	}
	
	public Map<Long, Record> getEffectiveBomMap(Long orgId){
		List<Record> recordList = getEffectiveBomList(orgId);
		if (CollectionUtil.isEmpty(recordList)){
			return new HashMap<>();
		}
		return recordList.stream().collect(Collectors.toMap(record -> record.getLong(BomM.IINVENTORYID), record -> record, (record1, record2)-> record1));
	}
	
	public List<String> findNextLevelCodes(String currentCode, Set<String> allCodes) {
		List<String> nextLevelCodes = new ArrayList<>();
		for (String code : allCodes) {
			if (code.startsWith(currentCode + "-")) {
				if (code.lastIndexOf("-") == currentCode.length()) {
					nextLevelCodes.add(code);
				}
			}
		}
		return nextLevelCodes;
	}
	
	public List<BomM> findBomByPartBomMid(Long bomId){
		String sqlStr = "SELECT " +
				" m.* " +
				"FROM " +
				" Bd_BomM m " +
				" INNER JOIN Bd_BomD a ON m.iAutoId = a.iBomMid " +
				"WHERE " +
				" a.isDeleted = '0' " +
				" AND m.isDeleted = '0' " +
				" AND a.iInvPartBomMid = ? ";
		return find(sqlStr, bomId);
	}
}
