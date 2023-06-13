package cn.rjtech.admin.bomm;

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
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.BomSourceTypeEnum;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.BomD;
import cn.rjtech.model.momdata.BomM;
import cn.rjtech.model.momdata.PurchaseorderdQty;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

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
		dbBomM.setIInventoryId(bomM.getIInventoryId());
		dbBomM.setCInvCode(bomM.getCInvCode());
		dbBomM.setCInvName(bomM.getCInvName());
		dbBomM.setDDisableDate(bomM.getDDisableDate());
		dbBomM.setDEnableDate(bomM.getDEnableDate());
		dbBomM.setIUpdateBy(userId);
		dbBomM.setCUpdateName(userName);
		dbBomM.setDUpdateTime(now);
		boolean success=dbBomM.update();
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
		kv.set("isView", 1);
		List<Record> recordList = dbTemplate("bomm.datas", kv).find();
		return createJsTreeBean(kv.getStr(BomM.ENABLEICON), recordList);
	}
	
	public List<JsTreeBean> createJsTreeBean(String enableIconStr, List<Record> recordList){
		List<JsTreeBean> trees = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(recordList)){
			
			List<Record> allList = dbTemplate("bomm.datas", Kv.by("orgId", getOrgId())).find();
			
			Map<Long, List<Record>> compareCollect = allList.stream().filter(record -> StrUtil.isNotBlank(record.getStr(BomD.IPID))).collect(Collectors.groupingBy(record -> record.getLong(BomD.IPID)));

			List<Record> invPartBomMidList = recordList.stream().filter(record -> StrUtil.isNotBlank(record.getStr(BomD.IINVPARTBOMMID))).collect(Collectors.toList());
			// 设置
			List<Record> records = new ArrayList<>();
			for (Record record : invPartBomMidList){
				Long id = record.getLong(BomD.IAUTOID);
				Long iInvPartBomMid = record.getLong(BomD.IINVPARTBOMMID);
				if (compareCollect.containsKey(iInvPartBomMid)){
					List<Record> compareList = compareCollect.get(iInvPartBomMid);
					compareList.forEach(compare -> {
						compare.set(BomD.IPID, id);
						compare.set(BomD.SOURCEID, compare.getLong(BomD.IAUTOID));
						compare.set(BomD.IAUTOID, JBoltSnowflakeKit.me.nextId());
					});
//					recordList.addAll(compareList);
					records.addAll(compareList);
				}
			}
			
			recordList.addAll(records);
			for (Record record : recordList){
                Long id = record.getLong(BomM.IAUTOID);
                Object pid = record.get(BomD.IPID);
                StringBuilder text = new StringBuilder(record.getStr(BomM.CINVNAME));
                if (pid == null) {
                    pid = "#";
                    if (StrUtil.isNotBlank(enableIconStr)){
                        String enableIcon = enableIconStr;
                        enableIcon = enableIcon.replace("?", record.getStr(BomM.IAUTOID));
                        text.append(enableIcon);
                    }
                }
                JsTreeBean jsTreeBean = new JsTreeBean(id, pid, text.toString(), true);
                if (ObjectUtil.isNotNull(record.getLong(BomD.SOURCEID))){
					id = record.getLong(BomD.SOURCEID);
				}
                jsTreeBean.setData(id);
                trees.add(jsTreeBean);
            }
        }
		return trees;
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
				code = Integer.valueOf(bomD.getCCode())+1;
				iCodeLevel = Integer.valueOf(bomD.getICodeLevel())+1;
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
}
