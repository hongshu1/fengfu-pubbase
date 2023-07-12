package cn.rjtech.admin.bomd;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.enums.BoolCharEnum;
import cn.rjtech.enums.IsEnableEnum;
import cn.rjtech.enums.PartTypeEnum;
import cn.rjtech.model.momdata.BomD;
import cn.rjtech.model.momdata.BomM;
import cn.rjtech.model.momdata.Inventory;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 物料建模-BOM明细
 * @ClassName: BomDService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-08 17:03
 */
public class BomDService extends BaseService<BomD> {
	private final BomD dao=new BomD().dao();
	@Override
	protected BomD dao() {
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
     * @param iPartType 材料类别
     * @param isVirtual 是否虚拟件;0.否 1. 是
     * @param bProxyForeign 是否委外;是否外作，0. 否 1. 是
	 * @return
	 */
	public Page<BomD> getAdminDatas(int pageNumber, int pageSize, String keywords, Integer iPartType, Boolean isVirtual, Boolean bProxyForeign) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("iPartType",iPartType);
        sql.eqBooleanToChar("isVirtual",isVirtual);
        sql.eqBooleanToChar("bProxyForeign",bProxyForeign);
        //关键词模糊查询
        sql.likeMulti(keywords,"cInvName", "cVenName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param bomD
	 * @return
	 */
	public Ret save(BomD bomD) {
		if(bomD==null || isOk(bomD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=bomD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(bomD.getIAutoId(), JBoltUserKit.getUserId(), bomD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param bomD
	 * @return
	 */
	public Ret update(BomD bomD) {
		if(bomD==null || notOk(bomD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		BomD dbBomD=findById(bomD.getIAutoId());
		if(dbBomD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=bomD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(bomD.getIAutoId(), JBoltUserKit.getUserId(), bomD.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param bomD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(BomD bomD, Kv kv) {
		//addDeleteSystemLog(bomD.getIAutoId(), JBoltUserKit.getUserId(),bomD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param bomD model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(BomD bomD, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(BomD bomD, String column, Kv kv) {
		//addUpdateSystemLog(bomD.getIAutoId(), JBoltUserKit.getUserId(), bomD.getName(),"的字段["+column+"]值:"+bomD.get(column));
		/**
		switch(column){
		    case "isVirtual":
		        break;
		    case "bProxyForeign":
		        break;
		}
		*/
		return null;
	}

	public boolean deletByIds(Object[] ids){
		if (ArrayUtil.isEmpty(ids)){
			return false;
		}
		for (Object id : ids){
			updateColumn(id, BomD.ISDELETED, BoolCharEnum.YES.getValue());
		}
		return true;
	}
	
	public Page<Record> getBomComparePageData(Integer pageNumber, Integer pageSize, Kv kv) {
		if (ObjUtil.isNull(kv.getLong(BomD.IBOMMID))){
			return emptyPage(pageSize);
		}
		Page<Record> paginate = dbTemplate("bomd.getBomComparePageData", kv).paginate(pageNumber, pageSize);
		changeRecord(paginate.getList(), kv.getStr(BomD.CCODE), true);
		return paginate;
	}
	
	public List<Record> getTreeTableDatas(Kv kv) {
		if (ObjUtil.isNull(kv.getLong(BomD.IBOMMID))){
			return new ArrayList<>();
		}
		List<Record> recordList = dbTemplate("bomd.getBomComparePageData", kv).find();
		List<Record> jsTreeBean = createJsTreeBean(recordList, kv.getStr(BomD.CCODE));
		
		changeRecord(jsTreeBean, kv.getStr(BomD.CCODE), false);
		List<Record> jsTableData = convertToRecordTree(jsTreeBean, BomD.IAUTOID, BomD.IPID, (p) -> notOk(p.getLong(BomD.IPID)));
		return jsTableData;
	}
	
	public List<Record> createJsTreeBean(List<Record> recordList, String code){
		List<Record> trees = new ArrayList<>();
		if (CollUtil.isNotEmpty(recordList)){
			
			List<Record> bomCompareList = dbTemplate("bomd.getBomComparePageData", Kv.by("orgId", getOrgId())).find();
			List<Record> parentBomList = dbTemplate("bomd.getParentBomList", Kv.by("orgId", getOrgId())).find();
			
			Map<Long, List<Record>> bomCompareMap = bomCompareList.stream().filter(record -> StrUtil.isNotBlank(record.getStr(BomD.IPID))).collect(Collectors.groupingBy(record -> record.getLong(BomD.IPID)));
			// 第二层及以下的通过存货编码区分下一级
			Map<Long, Long> invMap = new HashMap<>();
			for (Record record : parentBomList){
				Long iInventoryId = record.getLong(BomD.IINVENTORYID);
				// pId不为空，则说明是子件
				if (ObjUtil.isNotNull(record.getLong(BomD.IPID))){
					continue;
				}
				invMap.put(iInventoryId, record.getLong(BomD.IAUTOID));
			}
			
			for (Record record : recordList){
				Long iInventoryId = record.getLong(BomD.IINVENTORYID);
				// 设置编码
				setCodeLevel(record, code);
				if (invMap.containsKey(iInventoryId)){
					recursiveTraversal(record, trees, bomCompareMap.get(invMap.get(iInventoryId)), bomCompareMap, invMap);
				}
				record.set(BomD.IPID, "0");
				// 添加子件
				trees.add(record);
			}
		}
		return trees;
	}
	
	/**
	 *  递归遍历所有子件
	 * @param parentRecord  父对象
	 * @param trees 集合
	 * @param compareList 子件集合
	 * @param compareMap  key=父id, value = 子件集合
	 */
	public void recursiveTraversal(Record parentRecord, List<Record> trees, List<Record> compareList, Map<Long, List<Record>> compareMap, Map<Long, Long> invMap){
		for (Record record : compareList){
			Long id = record.getLong(BomM.IAUTOID);
			Long iInventoryId = record.getLong(BomD.IINVENTORYID);
			Integer iLevel = parentRecord.getInt(BomD.ILEVELStr);
			String code = String.valueOf(iLevel+1);
			setCodeLevel(record, code);
			// 判断当前子件是否存在 子件
			if (invMap.containsKey(iInventoryId)){
				recursiveTraversal(record, trees, compareMap.get(invMap.get(iInventoryId)), compareMap, invMap);
			}
			Record newRecord = new Record();
			newRecord.setColumns(record);
			newRecord.set(BomD.IPID, parentRecord.getLong(BomD.IAUTOID));
			trees.add(newRecord);
		}
	}
	
	public void changeRecord(List<Record> recordList, String code, Boolean flag){
		if (CollUtil.isEmpty(recordList)){
			return;
		}
		for (Record record : recordList){
			if (flag){
				setCodeLevel(record, code);
			}
			Integer partType = record.getInt(BomD.IPARTTYPE);
			Integer isVirtual = record.getInt(BomD.ISVIRTUAL);
			Integer bProxyForeign = record.getInt(BomD.BPROXYFOREIGN);
			
			if (ObjUtil.isNotNull(partType)){
				PartTypeEnum partTypeEnum = PartTypeEnum.toEnum(partType);
				ValidationUtils.notNull(partTypeEnum, "未知材料类别");
				record.set(Inventory.PARTTYPENAME, partTypeEnum.getText());
			}
			if (ObjUtil.isNotNull(isVirtual)){
				IsEnableEnum isEnableEnum = IsEnableEnum.toEnum(isVirtual);
				ValidationUtils.notNull(isEnableEnum, "未知虚拟件类型");
				record.set(Inventory.ISVIRTALNAME, isEnableEnum.getText());
			}
			if (ObjUtil.isNotNull(bProxyForeign)){
				IsEnableEnum isEnableEnum = IsEnableEnum.toEnum(bProxyForeign);
				ValidationUtils.notNull(isEnableEnum, "未知加工类型");
				record.set(Inventory.BPROXYFOREIGNNAME, isEnableEnum.getText());
			}
		}
	}
	
	/**
	 * 用于拼接编码栏 key1-key9
	 * @param record
	 * @param code
	 */
	private void setCodeLevel(Record record, String code){
		
		if (ObjUtil.isNull(code)){
//			code =record.getStr(BomD.CCODE);
			code = "1";
		}
		String codeKey = BomD.CCODE.concat(code);
		record.set(codeKey, code);
		record.set(BomD.ILEVELStr, code);
	}
	
	public List<BomD> queryBomCompareList(Long bomId, String fieldName){
		Sql sql = selectSql();
		sql.eq(BomD.ISDELETED, "0");
		sql.eq(fieldName, bomId);
		return find(sql);
	}
	
	public BomD createCompare(Long orgId, Long bomId, Long pid, Long iInventoryId, Long iVendorId, Integer level, Integer iRawType, String codeLevel, String cMemo,
							  BigDecimal iBaseQty, BigDecimal iWeight, Boolean bProxyForeign){
		BomD bomD = new BomD();
		bomD.setIAutoId(JBoltSnowflakeKit.me.nextId());
		bomD.setIsDeleted(false);
		bomD.setIOrgId(orgId);
		bomD.setIBomMid(bomId);
		bomD.setIPid(pid);
		// 存货相关
		bomD.setIInventoryId(iInventoryId);
		bomD.setIBaseQty(iBaseQty);
		bomD.setIWeight(iWeight);
		
		bomD.setIRawType(iRawType);
		bomD.setILevel(level);
		bomD.setCInvLev(codeLevel);
		// 供应商
		bomD.setIVendorId(iVendorId);
		// 是否委外加工
		bomD.setBProxyForeign(bProxyForeign);
		bomD.setCMemo(cMemo);
		return bomD;
	}
	
	public void setBomCompare(BomD bomD, Integer iPartType, Long iInventoryUomId1, String cInvCode, String cInvName, String cInvStd, String cVendCode, String cVenName, Boolean isVirtual){
		
		bomD.setIInventoryUomId1(iInventoryUomId1);
		bomD.setCInvCode(cInvCode);
		bomD.setCInvName(cInvName);
		bomD.setCInvStd(cInvStd);
		bomD.setIPartType(iPartType);
		// 是否虚拟件
		bomD.setIsVirtual(isVirtual);
		
		bomD.setCVendCode(cVendCode);
		bomD.setCVenName(cVenName);
	}
	
	public void setBomCodeLevel(BomD bomD, String cCode){
		// 层次编码
		bomD.setCCode(cCode);
		bomD.setICodeLevel(cCode);
	}
	
	public List<Record> getEffectiveBomCompare(Long orgId, List<Long> invIds){
		Okv okv = Okv.by("orgId", orgId);
		if (CollUtil.isNotEmpty(invIds) && invIds.size() < 150){
			okv.set("invIds", invIds);
		}
		return dbTemplate("bomd.getEffectiveBomCompare", okv).find();
    }
    
    public Map<Long, List<Record>> getEffectiveBomCompareMap(Long orgId, List<Long> invIds){
        List<Record> recordList = getEffectiveBomCompare(orgId, invIds);
        if (CollUtil.isEmpty(recordList)){
            return new HashMap<>();
        }
        return recordList.stream().collect(Collectors.groupingBy(record -> record.getLong(BomD.IPID), Collectors.toList()));
    }
    
    public BomD findByPid(Long pid){
		Sql sql = selectSql().eq(BomD.IPID, pid).eq(BomD.ISDELETED, "0");
		return findFirst(sql);
	}
	
	public boolean existsBomDSon(String pidName, Object pidValue) {
		if (!this.notOk(pidName) && !this.notOk(pidValue)) {
			Sql sql = this.selectSql().select(BomD.IAUTOID).eqQM(new String[]{pidName}).eq(BomD.ISDELETED, "0").first();
			Object existId = this.queryColumn(sql, new Object[]{pidValue});
			return this.isOk(existId);
		} else {
			throw new RuntimeException("参数异常");
		}
	}
}
