package cn.rjtech.admin.bomm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.bean.JsTreeStateBean;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.bomd.BomDService;
import cn.rjtech.admin.bomdata.BomDataService;
import cn.rjtech.admin.bommtrl.BommTrlService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.model.momdata.base.BaseBomD;
import cn.rjtech.util.Util;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
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
	@Inject
	private VendorService vendorService;
	@Inject
	private BommTrlService bommTrlService;
	
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
	
	public List<JsTreeBean> createJsTreeForAsync(List<Record> mlist, boolean isRoot, Kv kv){
		List<JsTreeBean> jsTreeBeans = new ArrayList();
		
		if (StrUtil.isBlank(kv.getStr("pid"))){
			boolean isParent = false;
			JsTreeBean jsTreeBean;
			for(Iterator var10 = mlist.iterator(); var10.hasNext(); jsTreeBeans.add(jsTreeBean)) {
				Record record = (Record)var10.next();
				Long id = record.getLong(BomM.IAUTOID);
				
				String cInvName = record.getStr(BomM.CINVNAME);
				
				String textName = Util.getTextName(cInvName);
				StringBuilder text = new StringBuilder(textName);
				// 标志
				String enableIconStr = kv.getStr(BomM.ENABLEICON);
				if (StrUtil.isNotBlank(enableIconStr)){
					String enableIcon = enableIconStr;
					enableIcon = enableIcon.replace("?", String.valueOf(id));
					text.append(enableIcon);
				}
				jsTreeBean = new JsTreeBean(id, record.getStr(BomD.IPID), text.toString(), false);
				jsTreeBean.setState(new JsTreeStateBean(false));
				// 判断是否存在下阶数据
				isParent = bomDService.existsBomDSon(BomD.IPID, record.get(BomM.IAUTOID));
				jsTreeBean.setChildren(isParent);
				if (isParent) {
					jsTreeBean.setType(isRoot ? "root" : "parent");
				} else {
					jsTreeBean.setType("node");
				}
				jsTreeBean.setData(record);
			}
		}
		
		if (StrUtil.isNotBlank(kv.getStr("pid"))){
			List<Record> allList = dbTemplate("bomm.datas", Kv.by("orgId", getOrgId())).find();
			Map<Long, List<Record>> compareMap = allList.stream().filter(record -> StrUtil.isNotBlank(record.getStr(BomD.IPID))).collect(Collectors.groupingBy(record -> record.getLong(BomD.IPID)));
			JsTreeBean jsTreeBean2 =  new JsTreeBean("11", kv.getStr("pid"), "测试", true);
			// 判断是否存在下阶数据
			boolean flag = bomDService.existsBomDSon(BomD.IPID, "11");
			jsTreeBean2.setChildren(true);
			jsTreeBean2.setParent(kv.getStr("pid"));
			jsTreeBean2.setType("parent");
			
			JsTreeBean jsTreeBean3 =  new JsTreeBean("12", "11", "测试2", true);
			jsTreeBean3.setChildren(false);
			
			jsTreeBean3.setType("node");
			jsTreeBeans.add(jsTreeBean2);
//			jsTreeBeans.add(jsTreeBean3);
		}
		
		return jsTreeBeans;
	}
	
	public List<JsTreeBean> createJsTreeBean(String enableIconStr, List<Record> recordList){
		List<JsTreeBean> trees = new ArrayList<>();
		if (CollUtil.isNotEmpty(recordList)){
			List<Record> allList = dbTemplate("bomm.datas", Kv.by("orgId", getOrgId())).find();
			// 第一层根据pid区分子件
			Map<Long, List<Record>> compareMap = allList.stream().filter(record -> StrUtil.isNotBlank(record.getStr(BomD.IPID))).collect(Collectors.groupingBy(record -> record.getLong(BomD.IPID)));
			// 第二层及以下的通过存货编码区分下一级
			Map<Long, Long> invMap = new HashMap<>();
			for (Record record : allList){
				Long iInventoryId = record.getLong(BomD.IINVENTORYID);
				// pId不为空，则说明是子件
				if (ObjectUtil.isNotNull(record.getLong(BomD.IPID))){
					continue;
				}
				invMap.put(iInventoryId, record.getLong(BomD.IAUTOID));
			}
			for (Record record : recordList){
				Long id = record.getLong(BomM.IAUTOID);
				if (compareMap.containsKey(id)){
					recursiveTraversal(id, trees, compareMap.get(id), compareMap, invMap);
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
	public void recursiveTraversal(Long pid, List<JsTreeBean> trees, List<Record> compareList, Map<Long, List<Record>> compareMap, Map<Long, Long> invMap){
		for (Record record : compareList){
			Long id = record.getLong(BomM.IAUTOID);
			String cInvName = record.getStr(BomM.CINVNAME);
			Long iInventoryId = record.getLong(BomD.IINVENTORYID);
			// 新id
			Long newId = JBoltSnowflakeKit.me.nextId();
			// 判断当前子件是否存在 子件
			if (invMap.containsKey(iInventoryId)){
				recursiveTraversal(newId, trees, compareMap.get(invMap.get(iInventoryId)), compareMap, invMap);
			}
			addJsTreeBean(invMap.containsKey(iInventoryId) ? invMap.get(iInventoryId): id, newId, pid, cInvName, null, trees);
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
//		changeRecord(page.getList());
		return page;
	}
	
	public List<Record> getVersionRecordList(Kv kv){
		return dbTemplate("bomm.getVersionRecord", kv.set("orgId", getOrgId())).find();
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
        if (CollUtil.isEmpty(recordList)){
            return;
        }
		DateTime date = DateUtil.date();
		for (Record record : recordList){
//            AuditStatusEnum auditStatusEnum = AuditStatusEnum.toEnum(record.getInt(BomM.IAUDITSTATUS));
//            record.set(BomM.AUDITSTATUSSTR, auditStatusEnum.getText());
//			Date dEnableDate = record.getDate(BomM.DENABLEDATE);
//			Date dDisableDate = record.getDate(BomM.DDISABLEDATE);
//			String isEffectiveStr = "0";
//			if (DateUtil.compare(date, dEnableDate, DatePattern.NORM_DATE_PATTERN) >=0 && DateUtil.compare(date, dDisableDate, DatePattern.NORM_DATE_PATTERN) <= 0){
//				isEffectiveStr = "1";
//			}
//			record.set(BomM.ISEFFECTIVE, isEffectiveStr);
		}
    }
    
    public void setBomRecord(Long id, Boolean isChildren, Boolean isView, Kv kv){
		if (ObjUtil.isNull(id)){
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
		BomM bomM = findById(id);
        if (!isChildren && ObjUtil.isNotNull(id)){
           /* if (ObjUtil.isNull(bomM)){
				BomD bomD = bomDService.findById(id);
				ValidationUtils.notNull(bomD, "未找到子件");
				code = ObjUtil.isNull(kv.getInt(BomD.CCODE)) ? 1 : kv.getInt(BomD.CCODE);
				iCodeLevel = code;
				if (ObjUtil.isNotNull(bomD.getIInvPartBomMid())){
					bomM  = findById(bomD.getIInvPartBomMid());
				}
				// 当前id为 父id
				BomD parentBomd = bomDService.findByPid(bomD.getIAutoId());
				if (ObjUtil.isNull(bomM) && ObjUtil.isNull(parentBomd)){
					bomM = findById(bomD.getIPid());
				}
				if (ObjUtil.isNotNull(parentBomd)){
					code = kv.getInt("cCode");
					iCodeLevel = code;
					iInventoryId = bomD.getIInventoryId();
					cInvCode = bomD.getCInvCode();
					cInvName = bomD.getCInvName();
					iAutoId = bomD.getIAutoId();
				}
			}*/
            
            if (ObjUtil.isNotNull(bomM)){
				Integer iAuditStatus = bomM.getIAuditStatus();
				AuditStatusEnum auditStatusEnum = AuditStatusEnum.toEnum(iAuditStatus);
				ValidationUtils.notNull(auditStatusEnum, "未知状态类型");
				auditStatusStr = auditStatusEnum.getText();
				dDisableDate = DateUtil.formatDate(bomM.getDDisableDate());
				dEnableDate = DateUtil.formatDate(bomM.getDEnableDate());
				cVersion = bomM.getCVersion();
				if (StrUtil.isNotBlank(kv.getStr(BomD.CCODE))){
					code = kv.getInt(BomD.CCODE);
				}
				iInventoryId = bomM.getIInventoryId();
				cInvCode = bomM.getCInvCode();
				cInvName = bomM.getCInvName();
				iAutoId = bomM.getIAutoId();
			}
        }else if (ObjUtil.isNotNull(id)){
            BomD bomD = bomDService.findById(id);
			ValidationUtils.notNull(bomD, "未找到子件");
			code = Integer.valueOf(bomD.getCCode());
			iCodeLevel = Integer.valueOf(bomD.getICodeLevel());
            iInventoryId = bomD.getIInventoryId();
            cInvCode = bomD.getCInvCode();
            cInvName = bomD.getCInvName();
			iAutoId = bomD.getIAutoId();
            if (ObjUtil.isNotNull(bomD.getIInvPartBomMid())){
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
		if (ObjUtil.isNotNull(isAdd) && isAdd){
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
		if (CollUtil.isEmpty(versionList)){
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
		Date dEnableDate = bomMaster.getDEnableDate();
		Date dDisableDate = bomMaster.getDDisableDate();
		
		tx(() -> {
			// 校验当前日期 是否在启用跟结束日期范围内。
			if (DateUtil.compare(date, dEnableDate, DatePattern.NORM_DATE_PATTERN) >=0 && DateUtil.compare(date, dDisableDate, DatePattern.NORM_DATE_PATTERN) <= 0){
				bomMaster.setIsEffective(true);
				//
				Sql sql = updateSql().set(BomM.ISEFFECTIVE, "0").eq(BomM.IINVENTORYID, bomMaster.getIInventoryId());
				update(sql);
			}
			bomMaster.update();
//			-----------推送U8-----------------
			
			
			
			
			// 删除母件下所有子件数据
			return true;
		});
		return SUCCESS;
	}
	
	public Ret batchDel(String bomMasterIds){
		tx(() -> {
			for (String bomMasterId : bomMasterIds.split(",")){
				remove(Long.valueOf(bomMasterId));
			}
			return true;
		});
		return SUCCESS;
	}
	
	private void remove(Long bomMasterId){
		ValidationUtils.notNull(bomMasterId, JBoltMsg.PARAM_ERROR);
		BomM bomMaster = findById(bomMasterId);
		ValidationUtils.notNull(bomMaster, JBoltMsg.DATA_NOT_EXIST);
		// 校验审批中或已审批的数据不能进行删除
//			Integer iAuditStatus = bomMaster.getIAuditStatus();
//			AuditStatusEnum auditStatusEnum = AuditStatusEnum.toEnum(iAuditStatus);
//			ValidationUtils.isTrue((AuditStatusEnum.NOT_AUDIT.getValue()==iAuditStatus || AuditStatusEnum.REJECTED.getValue()==iAuditStatus), "该物料清单状态为【"+auditStatusEnum.getText()+"】不能进行删除");
		// 校验母件是否有被其他子件引用到
//			List<BomM> bomMList = findBomByPartBomMid(bomMasterId);
//			if (CollUtil.isNotEmpty(bomMList)){
//				List<String> invCodeList = bomMList.stream().map(BomM::getCInvCode).collect(Collectors.toList());
//				String format = String.format("该半成品版本记录，有存在其他地方使用【%s】", CollUtil.join(invCodeList, ","));
//				ValidationUtils.isTrue(CollUtil.isEmpty(bomMList), format);
//			}
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
	}
	
	/**
	 * 删除导入文件
	 * @param ids
	 * @return
	 */
	public Ret delFile(String ids){
		
		tx(() -> {
			for (String id : ids.split(",")){
				BommTrl bommTrl = bommTrlService.findById(id);
				Long bomMid = bommTrl.getIBomMid();
				BomData bomData = bomDataService.getBomData(bomMid);
				bommTrlService.deleteById(id);
				if (ObjectUtil.isNotNull(bomData)){
					bomDataService.deleteById(bomData.getIAutoId());
				}
			}
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
	
	public Ret saveCopy(Long bomMasterId, String dEnableDate, String dDisableDate, String cVersion) {
		ValidationUtils.notBlank(dDisableDate, JBoltMsg.JBOLTTABLE_IS_BLANK);
		ValidationUtils.notBlank(cVersion, JBoltMsg.JBOLTTABLE_IS_BLANK);
		ValidationUtils.notNull(bomMasterId, JBoltMsg.PARAM_ERROR);
		BomM bomMaster = findById(bomMasterId);
		ValidationUtils.notNull(bomMaster, JBoltMsg.DATA_NOT_EXIST);
		ValidationUtils.isTrue(!cVersion.equals(bomMaster.getCVersion()), "版本号不能一致");
		// 获取母件所有子件集合
		tx(() -> {
			saveCopyBomMaster(bomMasterId, cVersion, dEnableDate, dDisableDate, bomMaster);
			return true;
		});
		return SUCCESS;
	}
	
	private void saveCopyBomMaster(Long bomMasterId, String cVersion, String dEnableDate, String dDisableDate, BomM bomMaster){
		// 设置新的id
		long newBomMasterId = JBoltSnowflakeKit.me.nextId();
		DateTime now = DateUtil.date();
		bomMaster.setIAutoId(newBomMasterId);
		bomMaster.setDEnableDate(DateUtil.parseDate(dEnableDate));
		bomMaster.setDDisableDate(DateUtil.parseDate(dDisableDate));
		bomMaster.setDAuditTime(null);
		// 设置copy前的ID
		bomMaster.setICopyFromId(bomMasterId);
		bomMaster.setDAuditTime(now);
		bomMaster.setCVersion(cVersion);
		// 校验
		checkBomDateOrVersion(cVersion, bomMaster);
		List<BomD> compareList = bomDService.queryBomCompareList(bomMasterId, BomD.IBOMMID);
		compareList.forEach(bomD -> {
			bomD.setIBomMid(newBomMasterId);
			bomD.setIPid(newBomMasterId);
			bomD.setIAutoId(JBoltSnowflakeKit.me.nextId());
		});
		bomDService.batchSave(compareList);
		save(bomMaster,JBoltUserKit.getUserId(),JBoltUserKit.getUserName(), now, AuditStatusEnum.AWAIT_AUDIT.getValue());
		
		
	}

	
	public void  checkBomDateOrVersion(String cVersion, BomM bomM){
		// 校验版本号
		/*if (StrUtil.isNotBlank(cVersion)){
			List<Record> versionList = bomMService.findVersionByInvId(getOrgId(), bomM.getIInventoryId(), bomM.getIAutoId());
			if (CollUtil.isNotEmpty(versionList)){
			
			}
		}*/
		// 校验日期 和 版本号
		List<Record> invBomList = findByInvId(getOrgId(), bomM.getIInventoryId(), bomM.getIAutoId());
		if (CollUtil.isNotEmpty(invBomList)){
			invBomList.forEach(record -> {
				boolean overlapping = isOverlapping(bomM, record);
				String format = String.format("母件【%s】不可重复创建版本，启用日期停用日期重叠！", bomM.getCInvCode());
				ValidationUtils.isTrue(overlapping, format);
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
		BommTrl bommTrl = JSONObject.parseObject(formJsonData, BommTrl.class);
		BomM bomM = JSONObject.parseObject(formJsonData, BomM.class);
		
		Long inventoryId = bomM.getIInventoryId();
		ValidationUtils.notNull(inventoryId, "未找到成品存货id");
		List<BomM> bomMList = findByInventoryId(getOrgId(), bomM.getIInventoryId());
		ValidationUtils.isTrue(CollectionUtil.isEmpty(bomMList), "当前产品/半产品已存在物料清单记录，不允许导入新增，只能通过复制升级版本或手工新增");
		
		Long userId = JBoltUserKit.getUserId();
		String userName = JBoltUserKit.getUserName();
		DateTime now = DateUtil.date();
		// 主键id为空为 新增或者为修改
		if (ObjUtil.isNull(bomM.getIAutoId())){
			ValidationUtils.notBlank(tableJsonData, JBoltMsg.PARAM_ERROR);
			JSONArray tableData = JSONObject.parseArray(tableJsonData);
			// 校验数据
			verification(formData, tableData);
			return saveForm(bommTrl, bomM, tableData, userId, userName, now);
		}
		return SUCCESS;
	}
	
	public Ret saveForm(BommTrl bommTrl, BomM bomMaster, JSONArray tableData, Long userId, String userName, DateTime now){
		// 设置主键id
		long bomMasterId = JBoltSnowflakeKit.me.nextId();
		bomMaster.setIAutoId(bomMasterId);
		Map<BomM, List<BomD>> bomMListMap = getBomMasterMap(bomMaster, tableData);
		checkBomDateOrVersion(bomMaster.getCVersion(), bomMaster);
		bommTrl.setIBomMid(bomMasterId);
		Long orgId = getOrgId();
		tx(() -> {
			
			for (BomM bomM : bomMListMap.keySet()){
				List<BomD> bomDList = bomMListMap.get(bomM);
				Inventory inventory = inventoryService.findById(bomM.getIInventoryId());
				ValidationUtils.notNull(inventory, "产成品存货记录不存在");
				bomM.setCInvName(inventory.getCInvName());
				bomM.setCInvCode(inventory.getCInvCode());
				if(StrUtil.isBlank(bomM.getCVersion())){
					bomM.setCVersion(getNextVersion(getOrgId(), bomM.getIInventoryId()));
				}
				bomM.setDEnableDate(bomMaster.getDEnableDate());
				bomM.setDDisableDate(bomMaster.getDDisableDate());
				// 校验生成的bom日期是否会出现重叠
				boolean bomFlag = checkInventoryIsNotExistence(orgId, bomM.getIInventoryId());
				if (!bomFlag){
					save(bomM, userId, userName, now, AuditStatusEnum.NOT_AUDIT.getValue());
					bomDList.forEach(bomD -> {
						bomD.setIPid(bomM.getIAutoId());
						bomD.setIBomMid(bomM.getIAutoId());
					});
					bomDService.batchSave(bomDList);
				}
			}
			
			bommTrlService.save(bommTrl);
			BomData bomData = bomDataService.create(bomMaster.getIAutoId(), tableData.toJSONString());
			bomDataService.save(bomData);
			return true;
		});
		return SUCCESS;
	}
	
	public Ret updateForm(BommTrl bomMaster, Long userId, String userName, DateTime now){
	
	/*	BommTrl oldBomMaster = bommTrlService.findById(bomMaster.getIAutoId());
		Integer iAuditStatus = oldBomMaster.getIAuditStatus();
		AuditStatusEnum auditStatusEnum = AuditStatusEnum.toEnum(iAuditStatus);
		ValidationUtils.isTrue((AuditStatusEnum.NOT_AUDIT.getValue()==iAuditStatus || AuditStatusEnum.REJECTED.getValue()==iAuditStatus), "该物料清单状态为【"+auditStatusEnum.getText()+"】不能进行修改");
		// 复制
		copyBomMaster(bomMaster, oldBomMaster);
		tx(() -> {
			update(oldBomMaster, userId, userName, now);
			return true;
		});*/
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
		ValidationUtils.notBlank(inventoryId, "产品存货编码为空");;
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
//			ValidationUtils.isTrue(!checkInvCodeFlag, "第"+(i+1)+"行存货编码未解析出来");
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
		
		/*// 校验母件成品编码不能跟子件编码一致
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
*/
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
		if (StrUtil.isNotBlank(invItemId) || StrUtil.isNotBlank(blankingItemId) || StrUtil.isNotBlank(originalItemId) || StrUtil.isNotBlank(slicingInvItemId)){
			ValidationUtils.isTrue(!codeFlag, "选择存货后，编码栏不能为空");
		}
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
	
	private void copyBomMaster(BommTrl bomMaster, BommTrl oldBomMaster){
		// 机型id
		oldBomMaster.setIEquipmentModelId(bomMaster.getIEquipmentModelId());
		// 文件编码
		oldBomMaster.setCDocCode(bomMaster.getCDocCode());
		// 文件名称
		oldBomMaster.setCDocName(bomMaster.getCDocName());
		
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
		
		// 客户ID
		oldBomMaster.setICustomerId(bomMaster.getICustomerId());
		// 共用件备注
		oldBomMaster.setCCommonPartMemo(bomMaster.getCCommonPartMemo());
	}
	
	private Map<BomM, List<BomD>> getBomMasterMap(BomM bomM , JSONArray tableData){
		
		long bomMasterId = bomM.getIAutoId();
		Long bomMasterInvId = bomM.getIInventoryId();
		Date dEnableDate = bomM.getDEnableDate();
		Date dDisableDate = bomM.getDDisableDate();
		
		Long orgId = getOrgId();
		// 编码
		Map<String, BomD> codeBomCompareMap = createCodeBomCompareMap(bomMasterId, tableData);
		// 存货id， 子件
		Map<Long, List<BomD>> parentInvMap = getParentInvMap(bomMasterId, bomMasterInvId, codeBomCompareMap);
//		String str = null;
//		System.out.println(str.toLowerCase());
		// 获取所有的子件
		List<BomD> compareList = createCodeBomCompareList(codeBomCompareMap);
		
		// 获取有效bom版本
		Map<Long, Record> effectiveBomMap = getEffectiveBomMap(orgId, null);
		// 获取有效bom子件
		Map<Long, List<Record>> effectiveBomCompareMap = bomDService.getEffectiveBomCompareMap(orgId, null);
		
		Map<BomM, List<BomD>> bomMListMap = new HashMap<>();
		
		bomMListMap.put(bomM, parentInvMap.get(bomMasterInvId));
		
		// 编译所有母件
		for (BomD productBomD :compareList){
			// 部品存货编码
			Long inventoryId = productBomD.getIInventoryId();
			// 判断是存在 有效的版本
			if (effectiveBomMap.containsKey(inventoryId)){
				Record effectiveBom = effectiveBomMap.get(inventoryId);
				checkBomCompareList(productBomD, effectiveBom, effectiveBomMap, effectiveBomCompareMap, parentInvMap);
				continue;
			}
			// 判断是不是母件，是母件创建bom
			if (parentInvMap.containsKey(inventoryId) && !bomMasterInvId.equals(inventoryId)){
				BomM newBomM = new BomM();
				long newBomMasterId = JBoltSnowflakeKit.me.nextId();
				newBomM.setIInventoryId(inventoryId);
				newBomM.setIType(2);
				newBomM.setIAutoId(newBomMasterId);
				List<BomD> bomDList = parentInvMap.get(inventoryId);
				for (BomD bomD : bomDList){
					bomD.setIPid(newBomMasterId);
					bomD.setIBomMid(newBomMasterId);
				}
				bomMListMap.put(newBomM, bomDList);
			}
		}
		
		List<BomD> bomCompareList = new ArrayList<>();
		List<BomM> removeBoms = new ArrayList<>();
		for (BomM bom : bomMListMap.keySet()){
			List<BomD> bomDList = bomMListMap.get(bom);
			List<BomD> bomDS = new ArrayList<>();
			for (BomD bomD:bomDList){
				if (ObjectUtil.isNull(bomD.getIInventoryId())){
					continue;
				}
				bomDS.add(bomD);
			}
			if (CollectionUtil.isEmpty(bomDS)){
//				Inventory inventory = inventoryService.findById(bom.getIInventoryId());
//				ValidationUtils.notNull(inventory, "存货未找到【"+bom.getIInventoryId()+"】");
//				ValidationUtils.notEmpty(bomDS, "存货编码【"+inventory.getCInvCode()+"】未找到子件数据");
				// 删除key
//				bomMListMap.remove(bom);
				removeBoms.add(bom);
				continue;
			}
			bomCompareList.addAll(bomDS);
			bomMListMap.put(bom, bomDS);
		}
		if (CollectionUtil.isNotEmpty(removeBoms)){
			for (BomM bom : removeBoms){
				bomMListMap.remove(bom);
			}
		}
		// 存货id
		List<Long> invIds = bomCompareList.stream().filter(bomD -> ObjUtil.isNotNull(bomD.getIInventoryId())).map(BomD::getIInventoryId).collect(Collectors.toList());
		// 供应商id
		List<Long> vendorIds = bomCompareList.stream().filter(bomD -> ObjUtil.isNotNull(bomD.getIVendorId())).map(BomD::getIVendorId).collect(Collectors.toList());
		
		List<Inventory> inventoryList = inventoryService.getListByIds(CollUtil.join(invIds, ","));
		ValidationUtils.notEmpty(inventoryList, "未找到存货数据");
		List<Vendor> vendorList = vendorService.getListByIds(CollUtil.join(vendorIds, ","));
		
		Map<Long, Inventory> inventoryMap = inventoryList.stream().collect(Collectors.toMap(Inventory::getIAutoId, Function.identity()));
		Map<Long, Vendor> vendorMap = null;
		if (CollUtil.isNotEmpty(vendorList)){
			vendorMap = vendorList.stream().collect(Collectors.toMap(Vendor::getIAutoId, Function.identity()));
		}
		
		for (BomM bom : bomMListMap.keySet()){
			// 赋值
			if (ObjectUtil.isNotNull(bom.getIInventoryId())){
				for (BomD bomD : bomMListMap.get(bom)){
					Inventory inventory = inventoryService.findById(bomD.getIInventoryId());
					String cVendCode = null;
					String cVenName = null;
					if (ObjUtil.isNotNull(bomD.getIVendorId())){
						Vendor vendor = vendorMap.get(bomD.getIVendorId());
						cVendCode = vendor.getCVenCode();
						cVenName = vendor.getCVenName();
					}
					String cCode = bomD.getCCode();
					
					bomD.setDEnableDate(dEnableDate);
					bomD.setDDisableDate(dDisableDate);
					
					bomD.setIOrgId(orgId);
					bomDService.setBomCompare(bomD, inventory.getIPartType(), inventory.getIInventoryUomId1(), inventory.getCInvCode(), inventory.getCInvName(), inventory.getCInvStd(),
							cVendCode, cVenName, false);
				}
			}
		}
		return bomMListMap;
	}
	
	private Map<String, BomD> createCodeBomCompareMap(Long bomMasterId, JSONArray tableData){
		
		Map<String, BomD> codeBomCompareMap = new HashMap<>();
		// 记录所有的存货
//		List<BomD> bomDList = new ArrayList<>();
		// 先把所有数据放到集合里
		for (int i=0; i<tableData.size(); i++){
			JSONObject row = tableData.getJSONObject(i);
			// 编码
			String code = getCode(row.getString(BomCompare.CODE1),
					row.getString(BomCompare.CODE2),
					row.getString(BomCompare.CODE3),
					row.getString(BomCompare.CODE4),
					row.getString(BomCompare.CODE5),
					row.getString(BomCompare.CODE6));
			// 记录当前行的虚拟件（没有存货但是有编码栏。）
			BomD bomD = new BomD();
			bomD.setCInvName("虚拟件_".concat(JBoltSnowflakeKit.me.nextIdStr()));
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
			}
			
			BomD compare = null;
			Long invId = row.getLong(BomCompare.INVITEMID.toLowerCase());
			// 半成品/部品
			if (ObjUtil.isNotNull(invId)){
				BigDecimal invQty = row.getBigDecimal(BomCompare.INVQTY.toLowerCase());
				BigDecimal invWeight = row.getBigDecimal(BomCompare.INVWEIGHT.toLowerCase());
				Long vendorId = row.getLong(BomCompare.IVENDORID.toLowerCase());
				Boolean isOutSourced = Boolean.valueOf(row.getString(BomCompare.ISOUTSOURCED.toLowerCase()));
				String cMemo = row.getString(BomCompare.CMEMO.toLowerCase());
				compare = bomDService.createCompare(null, bomMasterId, null, invId, vendorId, invLev, 0, code, cMemo, invQty, invWeight, isOutSourced);
//				bomDList.add(compare);
			}
			
			BomD blankBomCompare = null;
			Long blankingItemId = row.getLong(BomCompare.BLANKINGITEMID.toLowerCase());
			// 片料
			if (ObjUtil.isNotNull(blankingItemId)){
				Long pid = null;
				if (ObjUtil.isNotNull(compare)){
					pid = compare.getIAutoId();
				}
				BigDecimal blankingQty = row.getBigDecimal(BomCompare.BLANKINGQTY.toLowerCase());
				BigDecimal blankingWeight = row.getBigDecimal(BomCompare.BLANKINGWEIGHT.toLowerCase());
				blankBomCompare = bomDService.createCompare(null, bomMasterId, pid, blankingItemId, null, invLev, 1, code, null, blankingQty, blankingWeight, false);
//				bomDList.add(blankBomCompare);
			}
			
			// 分条料
			BomD slicingBomCompare = null;
			Long slicingInvItemId = row.getLong(BomCompare.SLICINGINVITEMID.toLowerCase());
			if (ObjUtil.isNotNull(slicingInvItemId)){
				Long pid = null;
				if (ObjUtil.isNotNull(compare)){
					pid = compare.getIAutoId();
				}
				if (ObjUtil.isNotNull(blankBomCompare)){
					pid = blankBomCompare.getIAutoId();
				}
				
				BigDecimal slicingQty = row.getBigDecimal(BomCompare.SLICINGQTY.toLowerCase());
				BigDecimal slicingWeight = row.getBigDecimal(BomCompare.SLICINGWEIGHT.toLowerCase());
				slicingBomCompare = bomDService.createCompare(null, bomMasterId, pid, slicingInvItemId, null, invLev, 2, code, null, slicingQty, slicingWeight, false);
//				bomDList.add(slicingBomCompare);
			}
			
			// 卷料(原材料)
			Long originalItemId = row.getLong(BomCompare.ORIGINALITEMID.toLowerCase());
			BomD originalBomCompare = null;
			if (ObjUtil.isNotNull(originalItemId)){
				Long pid = null;
				if (ObjUtil.isNotNull(compare)){
					pid = compare.getIAutoId();
				}
				if (ObjUtil.isNotNull(blankBomCompare)){
					pid = blankBomCompare.getIAutoId();
					
				}
				if (ObjUtil.isNotNull(slicingBomCompare)){
					pid = slicingBomCompare.getIAutoId();
				}
				BigDecimal originalQty = row.getBigDecimal(BomCompare.ORIGINALQTY.toLowerCase());
				BigDecimal originalWeight = row.getBigDecimal(BomCompare.ORIGINALWEIGHT.toLowerCase());
				originalBomCompare = bomDService.createCompare(null, bomMasterId, pid, originalItemId, null, invLev, 3, code, null, originalQty, originalWeight, false);
//				bomDList.add(originalBomCompare);
			}
			
			if (ObjUtil.isNotNull(compare) && ObjUtil.isNotNull(blankBomCompare)){
				compare.setChildBom(blankBomCompare);
			}else if (ObjUtil.isNotNull(compare) && ObjUtil.isNull(blankBomCompare) && ObjUtil.isNotNull(slicingBomCompare)){
				compare.setChildBom(slicingBomCompare);
			}else if (ObjUtil.isNotNull(compare) && ObjUtil.isNull(blankBomCompare) && ObjUtil.isNull(slicingBomCompare) && ObjUtil.isNotNull(originalBomCompare)){
				compare.setChildBom(originalBomCompare);
			}

			if (ObjUtil.isNotNull(blankBomCompare) && ObjUtil.isNotNull(slicingBomCompare)){
				blankBomCompare.setChildBom(slicingBomCompare);
			}else if (ObjUtil.isNotNull(blankBomCompare) && ObjUtil.isNull(slicingBomCompare) && ObjUtil.isNotNull(originalBomCompare)){
				blankBomCompare.setChildBom(originalBomCompare);
			}

			if (ObjUtil.isNotNull(slicingBomCompare) && ObjUtil.isNotNull(originalBomCompare)){
				slicingBomCompare.setChildBom(originalBomCompare);
			}
			
			// 给当前编码赋值
			if (ObjUtil.isNotNull(compare)){
				codeBomCompareMap.put(code, compare);
			}else if (ObjUtil.isNotNull(blankBomCompare)){
				
				codeBomCompareMap.put(code, blankBomCompare);
			}else if (ObjUtil.isNotNull(slicingBomCompare)){
				
				codeBomCompareMap.put(code, slicingBomCompare);
			}else if (ObjUtil.isNotNull(originalBomCompare)){
				
				codeBomCompareMap.put(code, originalBomCompare);
			}else{
				bomD.setCInvLev("0");
				bomD.setCCode("0");
				codeBomCompareMap.put(code, bomD);
			}
			
//			codeBomCompareMap.put(code, ObjUtil.isNotNull(compare) ? compare :
//					ObjUtil.isNotNull(blankBomCompare) ? blankBomCompare :
//							ObjUtil.isNotNull(slicingBomCompare)? slicingBomCompare : originalBomCompare);
		}
		return codeBomCompareMap;
	}
	
	private List<BomD> createCodeBomCompareList(Map<String, BomD> codeBomCompareMap){
		List<BomD> bomDList = new ArrayList<>();
		for (String code : codeBomCompareMap.keySet()){
			BomD bomD = codeBomCompareMap.get(code);
			if (ObjUtil.isNull(bomD.getIInventoryId())){
				continue;
			}
			addBomD(bomD.getChildBom(), bomDList);
			bomDList.add(bomD);
		}
		
		return bomDList;
	}
	
	private void addBomD(BomD bomD, List<BomD> bomDList){
		if (ObjUtil.isNotNull(bomD)){
			bomDList.add(bomD);
			addBomD(bomD.getChildBom(), bomDList);
		}
	}
	
	private Map<Long, List<BomD>> getParentInvMap(Long bomMasterId, Long bomMasterInvId, Map<String, BomD> codeBomCompareMap){
		Map<Long, List<BomD>> parentInvMap = new HashMap<>();
		// 先给半成品赋值
		for (String code : codeBomCompareMap.keySet()){
			BomD bomD = codeBomCompareMap.get(code);
			// 判断下面是否存在父栏目（1）
			String perCode = getPerCode(code);
			// 没有存在父级栏，直接跳过（父级栏目无需复制pid）
			if (StrUtil.isBlank(perCode)){
				bomD.setIPid(bomMasterId);
				String codeLevel = "1";
				// 存货编码为空，则为虚拟件
				if (ObjUtil.isNull(bomD.getIInventoryId())){
					bomD.setIAutoId(bomMasterId);
					codeLevel = "0";
				}
				bomDService.setBomCodeLevel(bomD, codeLevel);
				// 部品
				addParentInvMap(parentInvMap, bomMasterInvId, bomD);
				continue;
			}
		}
		// 把 1-1下的子件去除
		for (String code : codeBomCompareMap.keySet()){
			BomD bomD = codeBomCompareMap.get(code);
			// 判断下面是否存在父栏目（1）
			String perCode = getPerCode(code);
			// 子对象存在值，说明当前行存在多个。
			if (ObjUtil.isNotNull(bomD.getChildBom())){
				// 落料
				BomD blankBomCompare = bomD.getChildBom();
				List<String> nextCodes = findNextLevelCodes(code, codeBomCompareMap.keySet());
				ValidationUtils.isTrue(CollUtil.isEmpty(nextCodes), "编码【"+code+"】已存在多个存货，不允许存在下一级");
				// 1-1
				if (ObjUtil.isNull(bomD.getICodeLevel()) && StrUtil.isNotBlank(perCode)){
					BomD parentBom = codeBomCompareMap.get(perCode);
					Integer iCodeLevel = Integer.valueOf(parentBom.getICodeLevel())+1;
					bomDService.setBomCodeLevel(bomD, String.valueOf(iCodeLevel));
				}
				Integer iCodeLevel = Integer.valueOf(bomD.getICodeLevel())+1;
				bomDService.setBomCodeLevel(blankBomCompare, String.valueOf(iCodeLevel));
				addParentInvMap(parentInvMap, bomD.getIInventoryId(), blankBomCompare);
				
				// 分条料
				if (ObjUtil.isNotNull(blankBomCompare.getChildBom())){
					BomD slicingBomCompare = blankBomCompare.getChildBom();
					
					Integer slicingCodeLevel = Integer.valueOf(bomD.getICodeLevel())+1;
					bomDService.setBomCodeLevel(slicingBomCompare, String.valueOf(slicingCodeLevel));
					addParentInvMap(parentInvMap, blankBomCompare.getIInventoryId(), slicingBomCompare);
					
					// 原材料
					if (ObjUtil.isNotNull(slicingBomCompare.getChildBom())){
						Integer organCodeLevel = Integer.valueOf(bomD.getICodeLevel())+1;
						bomDService.setBomCodeLevel(slicingBomCompare.getChildBom(), String.valueOf(organCodeLevel));
						addParentInvMap(parentInvMap, slicingBomCompare.getIInventoryId(), slicingBomCompare.getChildBom());
					}
				}
			}
			
			// 没有存在父级栏，直接跳过（父级栏目无需复制pid）
			if (StrUtil.isBlank(perCode) || !codeBomCompareMap.containsKey(perCode)){
				continue;
			}
			// 父级id
			BomD parentBom = getParentBom(perCode, codeBomCompareMap);
			if (ObjectUtil.isNull(parentBom)){
				continue;
			}
			Long pid = parentBom.getIAutoId();
			if (StrUtil.isNotBlank(parentBom.getICodeLevel())){
				Integer iCodeLevel = Integer.valueOf(parentBom.getICodeLevel())+1;
				bomDService.setBomCodeLevel(bomD, String.valueOf(iCodeLevel));
			}
			// 部品的父id
			bomD.setIPid(pid);
			addParentInvMap(parentInvMap, parentBom.getIInventoryId(), bomD);
		}
		
		return parentInvMap;
	}
	
	private BomD getParentBom(String perCode, Map<String, BomD> codeBomCompareMap){
		if (!codeBomCompareMap.containsKey(perCode)){
			return null;
		}
		BomD parentBom = codeBomCompareMap.get(perCode);
		if (ObjectUtil.isNull(parentBom.getIAutoId())){
			String newPerCode = getPerCode(perCode);
			return getParentBom(newPerCode, codeBomCompareMap);
		}
		return parentBom;
	}
	
	private void addParentInvMap(Map<Long, List<BomD>> parentInvMap, Long invId, BomD sonBomD){
//		ValidationUtils.notNull(invId, "存货编码不能为空");
		List<BomD> bomDList = parentInvMap.containsKey(invId) ? parentInvMap.get(invId) : new ArrayList<>();
		if (ObjectUtil.isNull(sonBomD)){
			return;
		}
		boolean flag = false;
		for (BomD bomD : bomDList){
			Long inventoryId = bomD.getIInventoryId();
			if (ObjectUtil.isNull(inventoryId)){
				continue;
			}
			if (inventoryId.equals(sonBomD.getIInventoryId())){
				flag = true;
				Inventory inventory = inventoryService.findById(invId);
//				checkQtyOrWeight(bomD, inventory.getCInvCode());
//				checkQtyOrWeight(inventoryId, inventoryId, bomD.getCInvLev(), inventory.getCInvCode(), bomD.getIBaseQty(), bomD.getIWeight(), sonBomD.getIBaseQty(), sonBomD.getIWeight());
			}
		}
		if (flag){
			return;
		}
		bomDList.add(sonBomD);
		parentInvMap.put(invId, bomDList);
	}
	
	private void checkQtyOrWeight(BomD bomD, BomD sonBomD, String cInvCode){
		
		BigDecimal bomCompareQty = bomD.getIBaseQty();
		BigDecimal bomCompareIWeight = bomD.getIWeight();
		String cInvLev = bomD.getCInvLev();
		ValidationUtils.notNull(bomCompareQty, qtyErrorMsg(1, cInvLev, cInvCode));
		
		if (ObjectUtil.isNotNull(bomCompareIWeight)){
			ValidationUtils.isTrue(bomCompareIWeight.compareTo(BigDecimal.ZERO) >0, qtyErrorMsg(4, cInvLev, cInvCode));
		}
		ValidationUtils.isTrue(bomCompareQty.compareTo(BigDecimal.ZERO) >0, qtyErrorMsg(3, cInvLev, cInvCode));
	}
	
	
	private String versionErrorMsg(int type, String code, String cVersion){
		if (type == 0){
			return String.format("编码栏【%s】,母件版本号【%s】子件个数不同，无法保存！", code, cVersion);
		}else if (type == 1){
			return String.format("编码栏【%s】,母件版本号【%s】子件编码不相同，无法保存！", code, cVersion);
		}
		return null;
	}
	
	/**
	 *
	 * @param bomD 当前行数据
	 * @param effectiveBomMap	母件版本集合
	 * @param effectiveBomCompareMap 子件版本集合
	 */
	private void checkBomCompareList(BomD bomD, Record bomMasterRecord, Map<Long, Record> effectiveBomMap, Map<Long, List<Record>> effectiveBomCompareMap, Map<Long, List<BomD>> parentInvMap){
		Long bomMasterRecordId = bomMasterRecord.getLong(BomM.IAUTOID);
		String cVersion = bomMasterRecord.getStr(BomM.CVERSION);
		List<Record> recordList = effectiveBomCompareMap.get(bomMasterRecordId);
		// 设置版本号
		bomD.setIInvPartBomMid(bomMasterRecord.getLong(BomM.IAUTOID));
		bomD.setCVersion(bomMasterRecord.getStr(BomM.CVERSION));
		// 当前存货编码
		Long inventoryId = bomD.getIInventoryId();
		
		// 编码栏
		String cInvLev = bomD.getCInvLev();
		
		// 子件集合
		List<BomD> bomDList = parentInvMap.get(inventoryId);
		if (CollectionUtil.isEmpty(bomDList)){
			return;
		}
//		ValidationUtils.notEmpty(bomDList, cInvLev+"行，已存在版本记录，但文件中未找到子件");
		// 按存货编码-- 子件
		Map<Long, BomD> bomDMap = bomDList.stream().collect(Collectors.toMap(BomD::getIInventoryId, bomD1 -> bomD1));
		
		// 用于校验 子件数量是否相同，以及子件存货id是否相同
		List<Long> invIds = recordList.stream().map(record -> record.getLong(BomD.IINVENTORYID)).collect(Collectors.toList());
		List<Long> compareInvIds = bomDList.stream().map(BomD::getIInventoryId).collect(Collectors.toList());
		
		// 校验子件个数是否相同
		ValidationUtils.isTrue(recordList.size() == compareInvIds.size(), versionErrorMsg(0, cInvLev, cVersion));
		// 校验子件编码是否相同
		ValidationUtils.isTrue(CollUtil.containsAll(invIds, compareInvIds), versionErrorMsg(1, cInvLev, cVersion));
		
		// 校验子件数量，重量是否相同
		for (Record record :recordList){
			// 先校验子件编码是否一致,判断当前子件是否存在版本号，不存在则无需校验下一步子集
			Long iInvPartBomMid = record.getLong(BomD.IINVPARTBOMMID);
			Long invId = record.getLong(BomD.IINVENTORYID);
			// 数量
			BigDecimal qty = record.getBigDecimal(BomD.IBASEQTY);
			// 重量
			BigDecimal weight = record.getBigDecimal(BomD.IWEIGHT);
			// 存货编码
			String cInvCode = record.getStr(BomD.CINVCODE);
			// 子件
			BomD bomCompare = bomDMap.get(invId);
			// 校验数量及重量是否相同
			checkQtyOrWeight(bomCompare, invId, cInvCode, qty, weight);
//			List<BomD> bomCompareList = parentInvMap.get(invId);
//			ValidationUtils.isTrue(ObjUtil.isEmpty(bomCompareList), cInvCode+"：存货下，不允许再有子件！");
	
			/**
			 * 1.再判断当前子件是否还存在版本号
			 * 		存在版本号，继续校验下一级子件是否相同
			 *	2.判断当前存货是否存在 母件版本号
			 *		存在继续校验下一级子件是否相同
			 *	3.不存在 版本号及母件版本时，还需要校验当前是否存在子件，存在则报错
 			 */
			
		}
	}
	

/*	private void checkNextBomCompareList(Long invId, Record bomMasterRecord, BomD bomD, Map<Long, Record> effectiveBomMap, Map<Long, List<Record>> effectiveBomCompareMap, Map<String, BomD> CodeBomCompareMap, List<String> nextLevelCodes){
		if (CollUtil.isNotEmpty(nextLevelCodes)){
			for (String code : nextLevelCodes){
				BomD bomCompare = CodeBomCompareMap.get(code);
				if (invId.equals(bomCompare.getIInventoryId())){
					checkBomCompareList(bomCompare, bomMasterRecord, effectiveBomMap, effectiveBomCompareMap);
				}
			}
		}else if (ObjUtil.isNotNull(bomD)){
			checkBomCompareList(bomD, bomMasterRecord, effectiveBomMap, effectiveBomCompareMap,);
		}
	}*/
	
	private void checkQtyOrWeight(BomD bomCompare, Long invId, String cInvCode, BigDecimal qty, BigDecimal weight){
		BigDecimal bomCompareQty = bomCompare.getIBaseQty();
		BigDecimal bomCompareIWeight = bomCompare.getIWeight();
		Long bomCompareInvId = bomCompare.getIInventoryId();
		String cInvLev = bomCompare.getCInvLev();
		
		checkQtyOrWeight(invId, bomCompareInvId, cInvLev, cInvCode, bomCompareQty, bomCompareIWeight, qty, weight);
	}
	
	private void checkQtyOrWeight(Long invId, Long bomCompareInvId, String cInvLev, String cInvCode, BigDecimal bomCompareQty, BigDecimal bomCompareIWeight, BigDecimal qty, BigDecimal weight){
		ValidationUtils.notNull(bomCompareQty, qtyErrorMsg(1, cInvLev, cInvCode));
		
		if (ObjectUtil.isNotNull(bomCompareIWeight)){
			ValidationUtils.isTrue(bomCompareIWeight.compareTo(BigDecimal.ZERO) >0, qtyErrorMsg(4, cInvLev, cInvCode));
		}
		
		ValidationUtils.isTrue(bomCompareQty.compareTo(BigDecimal.ZERO) >0, qtyErrorMsg(3, cInvLev, cInvCode));
		
		// 校验存货编码是否一致
		if (invId.equals(bomCompareInvId)){
			ValidationUtils.isTrue(bomCompareQty.compareTo(qty)==0, qtyErrorMsg(5, cInvLev, cInvCode));
			if (ObjectUtil.isNotNull(bomCompareIWeight) && ObjectUtil.isNotNull(weight)){
				ValidationUtils.isTrue(bomCompareIWeight.compareTo(weight)==0, qtyErrorMsg(6, cInvLev, cInvCode));
			}
			if (ObjectUtil.isNull(bomCompareIWeight) &&  ObjectUtil.isNotNull(weight)){
				ValidationUtils.isTrue(ObjectUtil.isNotNull(bomCompareIWeight), qtyErrorMsg(7, cInvLev, cInvCode));
			}
			if (ObjectUtil.isNotNull(bomCompareIWeight) &&  ObjectUtil.isNull(weight)){
				ValidationUtils.isTrue(ObjectUtil.isNotNull(weight), qtyErrorMsg(8, cInvLev, cInvCode));
			}
		}
	}
	
	private String qtyErrorMsg(int type, String cInvLev, String invCode){
		if (type == 1){
			return String.format("编码栏【%s】存货编码【%s】基本用量不能为空", cInvLev, invCode);
		}else if (type == 2){
			return String.format("编码栏【%s】存货编码【%s】重量不能为空", cInvLev, invCode);
		}else if (type == 3){
			return String.format("编码栏【%s】存货编码【%s】基本用量不能少于0", cInvLev, invCode);
		}else if (type == 4){
			return String.format("编码栏【%s】存货编码【%s】重量不能少于0", cInvLev, invCode);
		}else if (type == 5){
			return String.format("编码栏【%s】存货编码【%s】子件基本用量不相同", cInvLev, invCode);
		}else if (type == 6){
			return String.format("编码栏【%s】存货编码【%s】子件重量不相同", cInvLev, invCode);
		}else if (type == 7){
			return String.format("编码栏【%s】存货编码【%s】文件重量不为空，物料清单重量为空", cInvLev, invCode);
		}else if (type == 8){
			return String.format("编码栏【%s】存货编码【%s】文件重量为空，物料清单重量不为空", cInvLev, invCode);
		}
		return null;
	}
	
	private void checkBomCompareListTwo(BomD bomD, Record bomMasterRecord, Map<Long, Record> effectiveBomMap, Map<Long, List<Record>> effectiveBomCompareMap, Map<String, BomD> CodeBomCompareMap){
		Long bomMasterRecordId = bomMasterRecord.getLong(BomM.IAUTOID);
		String cVersion = bomMasterRecord.getStr(BomM.CVERSION);
		List<Record> recordList = effectiveBomCompareMap.get(bomMasterRecordId);
		// 设置版本号
		bomD.setIInvPartBomMid(bomMasterRecord.getLong(BomM.IAUTOID));
		bomD.setCVersion(bomMasterRecord.getStr(BomM.CVERSION));
		
		String cInvLev = bomD.getCInvLev();
		
		List<String> nextLevelCodes = findNextLevelCodes(cInvLev, CodeBomCompareMap.keySet());
		
		List<Long> invIds = recordList.stream().map(record -> record.getLong(BomD.IINVENTORYID)).collect(Collectors.toList());
		List<Long> compareInvIds = new ArrayList<>();
		
		if (CollUtil.isNotEmpty(nextLevelCodes)){
			for (String code : nextLevelCodes){
				BomD bomCompare = CodeBomCompareMap.get(code);
				compareInvIds.add(bomCompare.getIInventoryId());
			}
		}else if (ObjUtil.isNotNull(bomD.getChildBom())){
			BomD childBom = bomD.getChildBom();
			compareInvIds.add(childBom.getIInventoryId());
		}
		// 校验子件个数是否相同
		ValidationUtils.isTrue(recordList.size() == compareInvIds.size(), versionErrorMsg(0, cInvLev, cVersion));
		// 校验子件编码是否相同
		ValidationUtils.isTrue(CollUtil.containsAll(invIds, compareInvIds), versionErrorMsg(1, cInvLev, cVersion));
		
		// 校验子件数量，重量是否相同
		for (Record record :recordList){
			// 先校验子件编码是否一致,判断当前子件是否存在版本号，不存在则无需校验下一步子集
			Long iInvPartBomMid = record.getLong(BomD.IINVPARTBOMMID);
			Long invId = record.getLong(BomD.IINVENTORYID);
			// 数量
			BigDecimal qty = record.getBigDecimal(BomD.IBASEQTY);
			// 重量
			BigDecimal weight = record.getBigDecimal(BomD.IWEIGHT);
			// 存货编码
			String cInvCode = record.getStr(BomD.CINVCODE);
			
			if (CollUtil.isNotEmpty(nextLevelCodes)){
				for (String code : nextLevelCodes){
					BomD bomCompare = CodeBomCompareMap.get(code);
					checkQtyOrWeight(bomCompare, invId, cInvCode, qty, weight);
				}
			}else if (ObjUtil.isNotNull(bomD.getChildBom())){
				BomD childBom = bomD.getChildBom();
				checkQtyOrWeight(childBom, invId, cInvCode, qty, weight);
			}
			
			// 校验是否存在
			if (ObjUtil.isNotNull(iInvPartBomMid)){
				Record recordById = findRecordById(iInvPartBomMid);
				ValidationUtils.notNull(recordById, "未通过子件版本找到对于的母件版本");
//				checkNextBomCompareList(invId, recordById, bomD.getChildBom(), effectiveBomMap, effectiveBomCompareMap, CodeBomCompareMap, nextLevelCodes);
			
			}else if (effectiveBomMap.containsKey(invId)){
				Record bomRecord = effectiveBomMap.get(invId);
//				checkNextBomCompareList(invId, bomRecord, bomD.getChildBom(), effectiveBomMap, effectiveBomCompareMap, CodeBomCompareMap, nextLevelCodes);
			
			}else if (ObjUtil.isNotNull(bomD.getChildBom())){
				BomD childBom = bomD.getChildBom();
				ValidationUtils.isTrue(ObjUtil.isNull(childBom.getChildBom()), cInvCode+"：存货下，不允许再有子件！");
			}else if (CollUtil.isNotEmpty(nextLevelCodes)){
				for (String code : nextLevelCodes){
					BomD bomCompare = CodeBomCompareMap.get(code);
					if (invId.equals(bomCompare.getIInventoryId())){
						List<String> bomCompareNextLevelCodes = findNextLevelCodes(bomCompare.getCInvLev(), CodeBomCompareMap.keySet());
						ValidationUtils.isTrue(ObjUtil.isEmpty(bomCompareNextLevelCodes), cInvCode+"：存货下，不允许再有子件！");
						ValidationUtils.isTrue(ObjUtil.isNull(bomCompare.getChildBom()), cInvCode+"：存货下，不允许再有子件！");
					}
					
				}
			}
			
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
	
	public List<Record> getEffectiveBomList(Long orgId, List<Long> invIds){
		Okv okv = Okv.by("orgId", orgId);
		if (CollUtil.isNotEmpty(invIds) && invIds.size() < 150){
			okv.set("invIds", invIds);
		}
		return dbTemplate("bomm.getEffectiveBomM", okv).find();
	}
	
	public Map<Long, Record> getEffectiveBomMap(Long orgId, List<Long> invIds){
		List<Record> recordList = getEffectiveBomList(orgId, invIds);
		if (CollUtil.isEmpty(recordList)){
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
	
	public List<BomM> findByInventoryId(Long orgId, Long inventoryId){
		Sql sql = selectSql();
		sql.eq(BomM.ISDELETED, "0").eq(BomM.IINVENTORYID, inventoryId);
		if (ObjectUtil.isNotNull(orgId)){
			sql.eq(BomM.IORGID, orgId);
		}
		return find(sql);
	}
	
	public Page<Record> getFileRecord(Integer pageNumber, Integer pageSize, Kv kv) {
		Page<Record> page = dbTemplate("bomm.getFileRecord", kv.set("orgId", getOrgId())).paginate(pageNumber, pageSize);
		return page;
	}
	
	/**
	 * 统一导入导出模板
	 */
	private JBoltExcelSheet createJboltExcelSheetTpl() {
		JBoltExcelSheet jBoltExcelSheet = JBoltExcelSheet.create("sheet");
		jBoltExcelSheet.setHeaders(
				JBoltExcelHeader.create("cinvcode", "存货编码", 20),
				JBoltExcelHeader.create("cinvcode1", "客户部番", 20),
				JBoltExcelHeader.create("cinvname1", "部品名称", 20),
				JBoltExcelHeader.create("cinvstd", "规格", 20),
				JBoltExcelHeader.create("cuomname", "计量单位", 20),
				JBoltExcelHeader.create("iseffective", "是否有效", 15),
				JBoltExcelHeader.create("cversion", "版本/版次", 15),
				JBoltExcelHeader.create("denabledate", "启用日期", 15),
				JBoltExcelHeader.create("ddisabledate", "停用日期", 15),
				JBoltExcelHeader.create("ccreatename", "创建人", 15),
				JBoltExcelHeader.create("dcreatetime", "创建时间", 20)
				
		);
		return jBoltExcelSheet;
	}
	
	public JBoltExcel exportExcelTpl(List<Record> datas) {
		//2、创建JBoltExcel
		//3、返回生成的excel文件 //创建JBoltExcel 从模板加载创建
		return JBoltExcel.create()
				.addSheet(createJboltExcelSheetTpl().setDataChangeHandler((data, index) -> {
					String isEffective = data.getStr(BomM.ISEFFECTIVE.toLowerCase());
					if (StrUtil.isNotBlank(isEffective)) {
						data.change(BomM.ISEFFECTIVE.toLowerCase(), "1".equals(isEffective) ? "是": "否");
					}
					Date dEnableDate = data.getDate(BomM.DENABLEDATE.toLowerCase());
					if (ObjectUtil.isNotNull(dEnableDate)){
						data.change(BomM.DENABLEDATE.toLowerCase(), DateUtil.formatDate(dEnableDate));
					}
					Date dDisableDate = data.getDate(BomM.DDISABLEDATE.toLowerCase());
					if (ObjectUtil.isNotNull(dDisableDate)){
						data.change(BomM.DDISABLEDATE.toLowerCase(), DateUtil.formatDate(dDisableDate));
					}
					Date dCreateTime = data.getDate(BomM.DCREATETIME.toLowerCase());
					if (ObjectUtil.isNotNull(dCreateTime)){
						data.change(BomM.DCREATETIME.toLowerCase(), DateUtil.formatDate(dDisableDate));
					}
				}).setRecordDatas(2, datas)).setFileName("物料清单" + "_" + DateUtil.today());
	}
	
	public JBoltExcel exportExcelByForm(List<Record> recordList){
		JBoltExcelSheet jBoltExcelSheet = JBoltExcelSheet.create("sheet");
		jBoltExcelSheet.setHeaders(
				JBoltExcelHeader.create("cinvcode", "子件物料编码", 20),
				JBoltExcelHeader.create("cinvname", "子件物料名称", 20),
				JBoltExcelHeader.create("cinvstd", "规格", 20),
				JBoltExcelHeader.create("cuomname", "计量单位", 20),
				JBoltExcelHeader.create("parttypename", "材料类别", 15),
				JBoltExcelHeader.create("ibaseqty", "基本用量", 15),
				JBoltExcelHeader.create("iweight", "重量", 15),
				JBoltExcelHeader.create("cvenname", "部品加工商", 15),
				JBoltExcelHeader.create("bproxyforeignname", "虚拟件", 15),
				JBoltExcelHeader.create("isvirtalname", "是否外作", 15),
				JBoltExcelHeader.create("cmemo", "备注", 20),
				JBoltExcelHeader.create("cversion", "版本/版次", 15),
				JBoltExcelHeader.create("denabledate", "启用日期", 15),
				JBoltExcelHeader.create("ddisabledate", "停用日期", 15),
				JBoltExcelHeader.create("", "单位材料成本", 20),
				JBoltExcelHeader.create("", "材料成本", 20)
		);
		
		return JBoltExcel.create()//创建JBoltExcel 从模板加载创建
				.addSheet(jBoltExcelSheet.setDataChangeHandler((data, index) -> {
					
					Date dEnableDate = data.getDate(BomM.DENABLEDATE.toLowerCase());
					if (ObjectUtil.isNotNull(dEnableDate)){
						data.change(BomM.DENABLEDATE.toLowerCase(), DateUtil.formatDate(dEnableDate));
					}
					Date dDisableDate = data.getDate(BomM.DDISABLEDATE.toLowerCase());
					if (ObjectUtil.isNotNull(dDisableDate)){
						data.change(BomM.DDISABLEDATE.toLowerCase(), DateUtil.formatDate(dDisableDate));
					}
					Date dCreateTime = data.getDate(BomM.DCREATETIME.toLowerCase());
					if (ObjectUtil.isNotNull(dCreateTime)){
						data.change(BomM.DCREATETIME.toLowerCase(), DateUtil.formatDate(dDisableDate));
					}
				}).setRecordDatas(2, recordList)).setFileName("物料清单明细" + "_" + DateUtil.today());
	}
	
	private boolean checkInventoryIsNotExistence(Long orgId, Long inventoryId){
		Sql sql = selectSql().eq(BomM.IORGID, orgId).eq(BomM.ISDELETED, "0").eq(BomM.IINVENTORYID, inventoryId);
		BomM bomM = findFirst(sql);
		return isOk(bomM);
	}
}
