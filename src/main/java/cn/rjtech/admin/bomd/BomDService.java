package cn.rjtech.admin.bomd;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.enums.BoolCharEnum;
import cn.rjtech.enums.IsEnableEnum;
import cn.rjtech.enums.PartTypeEnum;
import cn.rjtech.model.momdata.BomD;
import cn.rjtech.model.momdata.Inventory;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
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
		if (ObjectUtil.isNull(kv.getLong(BomD.IBOMMID))){
			return emptyPage(pageSize);
		}
		Page<Record> paginate = dbTemplate("bomd.getBomComparePageData", kv).paginate(pageNumber, pageSize);
		changeRecord(paginate.getList(), kv.getStr(BomD.CCODE), kv.getStr(BomD.ICODELEVEL));
		return paginate;
	}
	
	public void changeRecord(List<Record> recordList, String code, String codeLevel){
		if (CollectionUtil.isEmpty(recordList)){
			return;
		}
		for (Record record : recordList){
			if (ObjectUtil.isNull(codeLevel)){
				// 拼接编码key
				codeLevel = record.getStr(BomD.ICODELEVEL);
			}
			if (ObjectUtil.isNull(code)){
				code =record.getStr(BomD.CCODE);
			}
//			// 拼接编码key
//			String codeLevel = record.getStr(BomD.ICODELEVEL);
			String codeKey = BomD.CCODE.concat(codeLevel);
			record.set(codeKey, code);
			
			Integer partType = record.getInt(BomD.IPARTTYPE);
			Integer isVirtual = record.getInt(BomD.ISVIRTUAL);
			Integer bProxyForeign = record.getInt(BomD.BPROXYFOREIGN);
			
			if (ObjectUtil.isNotNull(partType)){
				PartTypeEnum partTypeEnum = PartTypeEnum.toEnum(partType);
				ValidationUtils.notNull(partTypeEnum, "未知材料类别");
				record.set(Inventory.PARTTYPENAME, partTypeEnum.getText());
			}
			if (ObjectUtil.isNotNull(isVirtual)){
				IsEnableEnum isEnableEnum = IsEnableEnum.toEnum(isVirtual);
				ValidationUtils.notNull(isEnableEnum, "未知虚拟件类型");
				record.set(Inventory.ISVIRTALNAME, isEnableEnum.getText());
			}
			if (ObjectUtil.isNotNull(bProxyForeign)){
				IsEnableEnum isEnableEnum = IsEnableEnum.toEnum(bProxyForeign);
				ValidationUtils.notNull(isEnableEnum, "未知加工类型");
				record.set(Inventory.BPROXYFOREIGNNAME, isEnableEnum.getText());
			}
		}
	}
	
	public List<BomD> queryBomByPartBomMid(Long bomId){
		return queryBomCompareList(bomId, BomD.IINVPARTBOMMID);
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
	
	public List<Record> getEffectiveBomCompare(Long orgId){
        return dbTemplate("bomd.getEffectiveBomCompare", Okv.by("orgId", orgId)).find();
    }
    
    public Map<Long, List<Record>> getEffectiveBomCompareMap(Long orgId){
        List<Record> recordList = getEffectiveBomCompare(orgId);
        if (CollectionUtil.isEmpty(recordList)){
            return new HashMap<>();
        }
        return recordList.stream().collect(Collectors.groupingBy(record -> record.getLong(BomD.IPID), Collectors.toList()));
    }
    
}
