package cn.rjtech.admin.bomcompare;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.bomd.BomDService;
import cn.rjtech.admin.bomm.BomMService;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.BomCompare;
import cn.rjtech.model.momdata.BomD;
import cn.rjtech.model.momdata.BomM;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.*;


/**
 * 物料建模-Bom清单
 *                              @ClassName: BomCompareService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-01 10:50
 */
public class BomCompareService extends BaseService<BomCompare> {
	
	private final BomCompare dao=new BomCompare().dao();
	
	@Inject
	private BomMService bomMService;
	@Inject
	private BomDService bomDService;
	
	@Override
	protected BomCompare dao() {
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
     * @param iRawType 原材料类型：1. 卷料 2. 片料 3. 分条料
     * @param isOutSourced 是否外作：0. 否 1. 是
     * @param isDeleted 删除状态： 0. 未删除 1. 已删除
	 * @return
	 */
	public Page<BomCompare> getAdminDatas(int pageNumber, int pageSize, String keywords, Integer iRawType, Boolean isOutSourced, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("iRawType",iRawType);
        sql.eqBooleanToChar("isOutSourced",isOutSourced);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param bomCompare
	 * @return
	 */
	public Ret save(BomCompare bomCompare) {
		if(bomCompare==null || isOk(bomCompare.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(bomCompare.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=bomCompare.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(bomCompare.getIAutoId(), JBoltUserKit.getUserId(), bomCompare.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param bomCompare
	 * @return
	 */
	public Ret update(BomCompare bomCompare) {
		if(bomCompare==null || notOk(bomCompare.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		BomCompare dbBomCompare=findById(bomCompare.getIAutoId());
		if(dbBomCompare==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(bomCompare.getName(), bomCompare.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=bomCompare.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(bomCompare.getIAutoId(), JBoltUserKit.getUserId(), bomCompare.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param bomCompare 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(BomCompare bomCompare, Kv kv) {
		//addDeleteSystemLog(bomCompare.getIAutoId(), JBoltUserKit.getUserId(),bomCompare.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param bomCompare model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(BomCompare bomCompare, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(BomCompare bomCompare, String column, Kv kv) {
		//addUpdateSystemLog(bomCompare.getIAutoId(), JBoltUserKit.getUserId(), bomCompare.getName(),"的字段["+column+"]值:"+bomCompare.get(column));
		/**
		switch(column){
		    case "isOutSourced":
		        break;
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}
	
	public BomCompare createBomCompare(Long iautoId, Long userId, String userName, Date now, Long bOMMasterId, Long pid, Long itemId, int seq, int Level, String invLev,
									   int rawType, BigDecimal qty, BigDecimal weight, Long vendorId, String cMemo, boolean isOutSourced, boolean isDeleted){
		BomCompare bomCompare = new BomCompare();
		bomCompare.setIAutoId(iautoId);
		bomCompare.setICreateBy(userId);
		bomCompare.setCCreateName(userName);
		bomCompare.setDCreateTime(now);
		
		bomCompare.setIUpdateBy(userId);
		bomCompare.setCUpdateName(userName);
		bomCompare.setDUpdateTime(now);
		bomCompare.setIBOMMasterId(bOMMasterId);
		bomCompare.setIInventoryId(itemId);
		bomCompare.setISeq(seq);
		bomCompare.setILevel(Level);
		bomCompare.setCInvLev(invLev);
		bomCompare.setISource(SourceEnum.MES.getValue());
		
		bomCompare.setIPid(pid);
		
		bomCompare.setIRawType(rawType);
		bomCompare.setIQty(qty);
		bomCompare.setIWeight(weight);
		bomCompare.setIVendorId(vendorId);
		bomCompare.setCMemo(cMemo);
		bomCompare.setIsOutSourced(isOutSourced);
		bomCompare.setIsDeleted(isDeleted);
		bomCompare.setIOrgId(getOrgId());
		bomCompare.setCOrgCode(getOrgCode());
		bomCompare.setCOrgName(getOrgName());
		return bomCompare;
	}
	
	public List<Record> findByBomMasterId(Long bomMasterId) {
		ValidationUtils.notNull(bomMasterId, JBoltMsg.DATA_NOT_EXIST);
		List<Record> records = dbTemplate("bomcompare.findByBomMasterId", Okv.by("bomMasterId", bomMasterId)).find();
		if (CollUtil.isEmpty(records)){
			return records;
		}
		Map<String, List<Record>> cInvLevMap = new HashMap<>();
		List<Record> recordList = new ArrayList<>();
		for (Record record : records){
			String cInvLev = record.getStr("cInvLev");
			List<Record> seqList = cInvLevMap.containsKey(cInvLev) ? cInvLevMap.get(cInvLev) : new ArrayList<>();
			seqList.add(record);
			cInvLevMap.put(cInvLev, seqList);
		}
		// 记录下标数
		int index = 0;
		for (String cInvLev : cInvLevMap.keySet()){
			List<Record> cInvLevList = cInvLevMap.get(cInvLev);
			Record bomCompareRecord = new Record();
			for (Record record : cInvLevList){
				Long iInventoryId = record.getLong("iInventoryId");
				Long iAutoId = record.getLong("iAutoId");
				// 先获取编码栏数据
				Integer invLev = record.getInt("iLevel");
				String invLevStr = record.getStr("cInvLev");
				bomCompareRecord.set("cInvLev", invLevStr);
				if (invLev == 6){
					bomCompareRecord.set(BomCompare.CODE6, invLevStr);
				}else if (invLev == 5){
					bomCompareRecord.set(BomCompare.CODE5, invLevStr);
				}else if (invLev == 4){
					bomCompareRecord.set(BomCompare.CODE4, invLevStr);
				}else if (invLev == 3){
					bomCompareRecord.set(BomCompare.CODE3, invLevStr);
				}else if (invLev == 2){
					bomCompareRecord.set(BomCompare.CODE2, invLevStr);
				}else{
					bomCompareRecord.set(BomCompare.CODE1, invLevStr);
				}
				Integer rawType = record.getInt("iRawType");
				String iQty = getStr(record.getBigDecimal("iQty"));
				String iWeight = getStr(record.getBigDecimal("iWeight"));
				String cInvCode = record.getStr("cInvCode");
				String cInvCode1 = record.getStr("cInvCode1");
				String cInvName = record.getStr("cInvName");
				switch (rawType){
					case 0:
						bomCompareRecord.set(BomCompare.INVITEMID, iInventoryId);
						bomCompareRecord.set(BomCompare.INVBOMCOMPAREID, iAutoId);
						bomCompareRecord.set(BomCompare.INVQTY, iQty);
						bomCompareRecord.set(BomCompare.INVWEIGHT, iWeight);
						bomCompareRecord.set(BomCompare.INVITEMCODE, cInvCode);
						bomCompareRecord.set(BomCompare.CINVCODE1, cInvCode1);
						bomCompareRecord.set(BomCompare.CINVADDCODE1, record.getStr("cInvAddCode1"));
						bomCompareRecord.set(BomCompare.CINVNAME1, record.getStr("cInvName1"));
						bomCompareRecord.set(BomCompare.CINVNAME2, record.getStr("cInvName2"));
						break;
					case 1:
						bomCompareRecord.set(BomCompare.BLANKINGITEMID, iInventoryId);
						bomCompareRecord.set(BomCompare.BLANKINGBOMCOMPAREID, iAutoId);
						bomCompareRecord.set(BomCompare.BLANKINGQTY, iQty);
						bomCompareRecord.set(BomCompare.BLANKINGWEIGHT, iWeight);
						
						bomCompareRecord.set(BomCompare.BLANKINGITEMCODE, cInvCode);
						bomCompareRecord.set(BomCompare.BLANKINGINVNAME, cInvName);
						
						bomCompareRecord.set(BomCompare.BLANKINGSTD, record.getStr("cInvStd"));
						bomCompareRecord.set(BomCompare.BLANKINGVENDORNAME, record.getStr("cVenName"));
						break;
					case 2:
						bomCompareRecord.set(BomCompare.SLICINGINVITEMID, iInventoryId);
						bomCompareRecord.set(BomCompare.SLICINGBOMCOMPAREID, iAutoId);
						bomCompareRecord.set(BomCompare.SLICINGQTY, iQty);
						bomCompareRecord.set(BomCompare.SLICINGWEIGHT, iWeight);
						bomCompareRecord.set(BomCompare.SLICINGINVITEMCODE, cInvCode);
						bomCompareRecord.set(BomCompare.SLICINGINVNAME, cInvName);
						bomCompareRecord.set(BomCompare.SLICINGSTD, record.getStr("cInvStd"));
						bomCompareRecord.set(BomCompare.SLICINGVENDORNAME, record.getStr("cVenName"));
						break;
					case 3:
						bomCompareRecord.set(BomCompare.ORIGINALITEMID, iInventoryId);
						bomCompareRecord.set(BomCompare.ORIGINALBOMCOMPAREID, iAutoId);
						bomCompareRecord.set(BomCompare.ORIGINALQTY, iQty);
						bomCompareRecord.set(BomCompare.ORIGINALWEIGHT, iWeight);
						bomCompareRecord.set(BomCompare.ORIGINALITEMCODE, cInvCode);
						bomCompareRecord.set(BomCompare.ORIGINALINVNAME, cInvName);
						bomCompareRecord.set(BomCompare.ORIGINALSTD, record.getStr("cInvStd"));
						bomCompareRecord.set(BomCompare.ORIGINALVENDORNAME, record.getStr("cVenName"));
						break;
				}
				if (StrUtil.isNotBlank(record.getStr("cVenName"))){
					bomCompareRecord.set(BomCompare.VENDORNAME, record.getStr("cVenName"));
					bomCompareRecord.set(BomCompare.IVENDORID, record.getLong("iVendorId"));
				}
				if (StrUtil.isNotBlank(record.getStr("isOutSourced"))){
					bomCompareRecord.set(BomCompare.ISOUTSOURCED, record.getStr("isOutSourced"));
				}
				if (StrUtil.isNotBlank(record.getStr("cMemo"))){
					bomCompareRecord.set(BomCompare.CMEMO, record.getStr("cMemo"));
				}
				bomCompareRecord.set("index", index);
				index +=1;
			}
			recordList.add(bomCompareRecord);
		}
		recordList.sort(Comparator.comparing(obj -> obj.getStr("cInvLev")));
		return recordList;
	}
	
	private String getStr(BigDecimal value){
		if (ObjUtil.isNull(value)){
			return null;
		}
		return value.stripTrailingZeros().toPlainString();
	}
	
	public Integer queryCompareIndex(Long bomMasterId){
		return dbTemplate("bomcompare.queryCompareIndex", Okv.by("bomMasterId", bomMasterId)).queryInt();
	}
	
	public List<BomCompare> findByBomMasterIdList(Long bomMasterId){
		ValidationUtils.notNull(bomMasterId, JBoltMsg.PARAM_ERROR);
		return find("SELECT a.* FROM Bd_BomCompare a WHERE a.iBOMMasterId = ? AND a.isDeleted = '0'", bomMasterId);
	}
	
	public int deleteByBomMasterId(Long bomMasterId) {
		return delete("DELETE Bd_BomCompare WHERE iBOMMasterId = ?", bomMasterId);
	}
	
	public List<Record> getCommonInv(Long bomMasterId, String invId, String qty, String weight, Integer iAuditStatus, boolean isEffective){
        Okv okv = Okv.by("invId", invId).set("qty", qty).set("weight", weight).set("iAuditStatus", iAuditStatus).set("isEffective", true== isEffective ? 1:0);
        if (ObjUtil.isNotNull(bomMasterId)){
            okv.set("bomMasterId", bomMasterId);
        }
	    return dbTemplate("bomcompare.getCommonInv", okv).find();
    }
	
	public Ret submitForm(JBoltTable jBoltTable) {
		BomM bomM = jBoltTable.getFormBean(BomM.class);
		Long userId = JBoltUserKit.getUserId();
		String userName = JBoltUserKit.getUserName();
		DateTime now = DateUtil.date();
		JSONObject form = jBoltTable.getForm();
		// 父id
		Long pId = form.getLong(BomD.IPID);
		String codeStr = form.getString(BomD.CCODE);
		String codeLevelStr = form.getString(BomD.ICODELEVEL);

		tx(() -> {
			
			Integer code = 1;
			Integer codeLevel = 1;
			if (StrUtil.isNotBlank(codeStr)){
				code = form.getInteger(BomD.CCODE)+1;
			}
			if (StrUtil.isNotBlank(codeLevelStr)){
				codeLevel = form.getInteger(BomD.ICODELEVEL)+1;
			}
			
			String cVersion = bomM.getCVersion();
			// 校验版本号跟日期范围
			bomMService.checkBomDateOrVersion(cVersion, bomM);
			// 编码为空默认升级
			if (StrUtil.isBlank(cVersion)){
				String nextVersion = bomMService.getNextVersion(getOrgId(), bomM.getIInventoryId());
				bomM.setCVersion(nextVersion);
			}
			// 保存/修改物料清单主表
			if (ObjUtil.isNull(bomM.getIAutoId())){
			/*	// 保存时校验当前产品/半产品物料清单是否存在，存在则不允许新增，只能通过复制升级版本才行
				List<BomM> bomMList = bomMService.findByInventoryId(getOrgId(), bomM.getIInventoryId());
				ValidationUtils.isTrue(CollectionUtil.isEmpty(bomMList), "当前产品/半产品已存在物料清单记录，不允许手工新增，只能通过复制升级版本");*/
				bomMService.save(bomM, userId, userName, now, AuditStatusEnum.NOT_AUDIT.getValue());
			}else{
				bomMService.update(bomM, userId, userName, now);
			}
			
			// 更改物料清单细表
			if (ObjUtil.isNotNull(pId)){
				BomD bomD = bomDService.findById(pId);
				ValidationUtils.notNull(bomD, "未找到引用的子件数据记录");
				bomD.setIInvPartBomMid(bomM.getIAutoId());
				bomD.setCVersion(bomM.getCVersion());
				bomD.update();
			}
			if (jBoltTable.saveIsNotBlank()){
				List<BomD> saveModelList = jBoltTable.getSaveModelList(BomD.class);
				for (BomD bomD :saveModelList){
					bomD.setIOrgId(getOrgId());
					bomD.setIPid(bomM.getIAutoId());
					bomD.setIBomMid(bomM.getIAutoId());
					bomD.setIsDeleted(false);
					bomD.setCCode(String.valueOf(code));
					bomD.setICodeLevel(String.valueOf(codeLevel));
					bomD.setDEnableDate(bomM.getDEnableDate());
					bomD.setDDisableDate(bomM.getDDisableDate());
					if (ObjUtil.isNull(bomD.getBProxyForeign())){
						bomD.setBProxyForeign(false);
					}
				}
				bomDService.batchSave(saveModelList);
			}
			
			if (jBoltTable.updateIsNotBlank()){
				List<BomD> updateModelList = jBoltTable.getUpdateModelList(BomD.class);
				bomDService.batchUpdate(updateModelList);
			}
			
			if (jBoltTable.deleteIsNotBlank()){
				Object[] delIds = jBoltTable.getDelete();
				bomDService.deletByIds(delIds);
			}
			return true;
		});
		return SUCCESS;
	}
	
}
