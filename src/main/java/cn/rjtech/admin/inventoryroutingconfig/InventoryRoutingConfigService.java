package cn.rjtech.admin.inventoryroutingconfig;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.inventoryroutingconfigoperation.InventoryroutingconfigOperationService;
import cn.rjtech.admin.inventoryroutingequipment.InventoryRoutingEquipmentService;
import cn.rjtech.admin.inventoryroutinginvc.InventoryRoutingInvcService;
import cn.rjtech.model.momdata.InventoryRoutingConfig;
import cn.rjtech.model.momdata.InventoryroutingconfigOperation;
import cn.rjtech.wms.utils.EncodeUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 物料建模-存货工艺配置
 * @ClassName: InventoryRoutingConfigService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 10:02
 */
public class InventoryRoutingConfigService extends BaseService<InventoryRoutingConfig> {
	private final InventoryRoutingConfig dao=new InventoryRoutingConfig().dao();
	
	@Inject
	private InventoryroutingconfigOperationService inventoryroutingconfigOperationService;
	@Inject
	private InventoryRoutingInvcService inventoryRoutingInvcService;
	@Inject
	private InventoryRoutingEquipmentService inventoryRoutingEquipmentService;
	
	@Override
	protected InventoryRoutingConfig dao() {
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
     * @param iType 工序类型: 1. 串序 2. 并序
     * @param isEnabled 是否启用
	 * @return
	 */
	public Page<InventoryRoutingConfig> getAdminDatas(int pageNumber, int pageSize, String keywords, Integer iType, Boolean isEnabled) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("iType",iType);
        sql.eqBooleanToChar("isEnabled",isEnabled);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOperationName", "cCreateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param inventoryRoutingConfig
	 * @return
	 */
	public Ret save(InventoryRoutingConfig inventoryRoutingConfig) {
		if(inventoryRoutingConfig==null || isOk(inventoryRoutingConfig.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(inventoryRoutingConfig.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryRoutingConfig.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventoryRoutingConfig.getIAutoId(), JBoltUserKit.getUserId(), inventoryRoutingConfig.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param inventoryRoutingConfig
	 * @return
	 */
	public Ret update(InventoryRoutingConfig inventoryRoutingConfig) {
		if(inventoryRoutingConfig==null || notOk(inventoryRoutingConfig.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InventoryRoutingConfig dbInventoryRoutingConfig=findById(inventoryRoutingConfig.getIAutoId());
		if(dbInventoryRoutingConfig==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inventoryRoutingConfig.getName(), inventoryRoutingConfig.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryRoutingConfig.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inventoryRoutingConfig.getIAutoId(), JBoltUserKit.getUserId(), inventoryRoutingConfig.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param inventoryRoutingConfig 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InventoryRoutingConfig inventoryRoutingConfig, Kv kv) {
		//addDeleteSystemLog(inventoryRoutingConfig.getIAutoId(), JBoltUserKit.getUserId(),inventoryRoutingConfig.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inventoryRoutingConfig model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(InventoryRoutingConfig inventoryRoutingConfig, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(InventoryRoutingConfig inventoryRoutingConfig, String column, Kv kv) {
		//addUpdateSystemLog(inventoryRoutingConfig.getIAutoId(), JBoltUserKit.getUserId(), inventoryRoutingConfig.getName(),"的字段["+column+"]值:"+inventoryRoutingConfig.get(column));
		/**
		switch(column){
		    case "isEnabled":
		        break;
		}
		*/
		return null;
	}

	public List<Record> dataList(Long iinventoryroutingid) {
		List<Record> list = dbTemplate("inventoryclass.getRouingConfigs", Okv.by("iinventoryroutingid", iinventoryroutingid)).find();
		if (CollUtil.isNotEmpty(list)) {
			for (Record record : list) {
				Long routingConfigId = record.getLong(InventoryRoutingConfig.IAUTOID);
				List<Record> routingInvList = inventoryRoutingInvcService.findRoutingConfigId(routingConfigId);
				// 物料集
				if (CollUtil.isNotEmpty(routingInvList)) {
					String jsonString = JSONObject.toJSONString(routingInvList);
					record.set(InventoryRoutingConfig.ITEMJSON, EncodeUtils.encodeUrl(jsonString, EncodeUtils.UTF_8));
					record.set(InventoryRoutingConfig.ITEMJSONSTR, routingInvList);
				}
				// 设备集
				List<Record> equipmentList = inventoryRoutingEquipmentService.findRoutingConfigId(routingConfigId);
				if (CollUtil.isNotEmpty(equipmentList)) {
					String jsonString = JSONObject.toJSONString(equipmentList);
					record.set(InventoryRoutingConfig.EQUIPMENTJSON, EncodeUtils.encodeUrl(jsonString, EncodeUtils.UTF_8));
					record.set(InventoryRoutingConfig.EQUIPMENTJSONSTR, equipmentList);
				}
			}
		}
		return list;
	}
	
	public void removeRoutingConfig(Long routingId){
		update("UPDATE Bd_InventoryRoutingConfig set isEnabled = 0 WHERE iInventoryRoutingId = ?", routingId);
	}

	public List<InventoryRoutingConfig> createInventoryRoutingConfigList(Long orgId, Long userId, String userName, boolean isEnable, Date date, List<Record> recordList){
		if (CollUtil.isEmpty(recordList)){
			return null;
		}
		List<InventoryRoutingConfig> configs = new ArrayList<>();
		for (Record record : recordList){
			InventoryRoutingConfig inventoryRoutingConfig = createInventoryRoutingConfig(orgId, userId, userName, isEnable, date);
			inventoryRoutingConfig.setIAutoId(record.getLong(InventoryRoutingConfig.IAUTOID));
			inventoryRoutingConfig.setISeq(record.getInt(InventoryRoutingConfig.ISEQ));
			inventoryRoutingConfig.setIInventoryRoutingId(record.getLong(InventoryRoutingConfig.IINVENTORYROUTINGID));
			inventoryRoutingConfig.setCMergedSeq(record.getStr(InventoryRoutingConfig.CMERGEDSEQ));
			inventoryRoutingConfig.setCOperationName(record.getStr(InventoryRoutingConfig.COPERATIONNAME));
			inventoryRoutingConfig.setIType(record.getInt(InventoryRoutingConfig.ITYPE));
//			String invIdStr = record.getStr(InventoryRoutingConfig.IRSINVENTORYID);
//			Long invId = StrUtil.isBlank(invIdStr) ? null : Long.valueOf(invIdStr);
			
			inventoryRoutingConfig.setCProductSn(record.getStr(InventoryRoutingConfig.CPRODUCTSN));
			inventoryRoutingConfig.setCProductTechSn(record.getStr(InventoryRoutingConfig.CPRODUCTTECHSN));
			inventoryRoutingConfig.setIMergedNum(record.getInt(InventoryRoutingConfig.IMERGEDNUM));
			inventoryRoutingConfig.setIMergeRate(record.getBigDecimal(InventoryRoutingConfig.IMERGERATE));
			inventoryRoutingConfig.setIMergeSecs(record.getInt(InventoryRoutingConfig.IMERGESECS));
			inventoryRoutingConfig.setISecs(record.getInt(InventoryRoutingConfig.ISECS));
			inventoryRoutingConfig.setCMemo(record.getStr(InventoryRoutingConfig.CMEMO));
			inventoryRoutingConfig.setIsAdd(record.getBoolean(InventoryRoutingConfig.ISADD));
			configs.add(inventoryRoutingConfig);
		}
		return configs;
	}

	public InventoryRoutingConfig createInventoryRoutingConfig(Long orgId, Long userId, String userName, boolean isEnable, Date date){
		InventoryRoutingConfig inventoryRoutingConfig = new InventoryRoutingConfig();
		inventoryRoutingConfig.setIAutoId(orgId);
		inventoryRoutingConfig.setICreateBy(userId);
		inventoryRoutingConfig.setCCreateName(userName);
		inventoryRoutingConfig.setIsEnabled(isEnable);
		inventoryRoutingConfig.setDCreateTime(date);
		return inventoryRoutingConfig;
	}
	
	public Ret saveItemRoutingConfig(Long inventoryRoutingId, Integer iseq) {
		tx(() -> {
			//新增
			InventoryRoutingConfig inventoryRoutingConfig = new InventoryRoutingConfig();
			Long id = JBoltSnowflakeKit.me.nextId();
			inventoryRoutingConfig.setIAutoId(id);
			inventoryRoutingConfig.setIInventoryRoutingId(inventoryRoutingId);
			inventoryRoutingConfig.setISeq(iseq);
			inventoryRoutingConfig.setIsEnabled(true);
			inventoryRoutingConfig.setICreateBy(JBoltUserKit.getUserId());
			inventoryRoutingConfig.setCCreateName(JBoltUserKit.getUserName());
			inventoryRoutingConfig.setDCreateTime(new Date());
			inventoryRoutingConfig.save();

			//修改工序配置顺序号
			updateSeq(inventoryRoutingId);

			return true;
		});
		return SUCCESS;
	}

	/**
	 * 删除 指定多个ID
	 *
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			String[] idarry = ids.split(",");
			InventoryRoutingConfig inventoryRoutingConfig = findById(idarry[0]);
//			//工序子表
//			deleteMultiByConfigidsOper(idarry);
//			//设备集子表
//			deleteMultiByConfigidsEquipment(idarry);
//			//存货工艺工序
//			deleteMultiByConfigidsInvcs(idarry);
//			//存货工艺工序物料集
//			deleteMultiByConfigidsSops(idarry);

			//工艺配置主表
			//deleteByIds(ids, true);
			update("UPDATE Bd_InventoryRoutingConfig SET isEnabled = 0 WHERE iAutoId IN (" + ArrayUtil.join(idarry, COMMA) + ") ");

			//修改工序配置顺序号
			updateSeq(inventoryRoutingConfig.getIInventoryRoutingId());
			return true;
		});
		return SUCCESS;
	}

	public void deleteMultiByConfigidsOper(Object[] deletes) {
		delete("DELETE FROM Bd_InventoryRoutingConfig_Operation WHERE iInventoryRoutingConfigId IN (" + ArrayUtil.join(deletes, COMMA) + ") ");
	}

	public void deleteMultiByConfigidsEquipment(Object[] deletes) {
		delete("DELETE FROM Bd_InventoryRoutingEquipment WHERE iInventoryRoutingConfigId IN (" + ArrayUtil.join(deletes, COMMA) + ") ");
	}

	public void deleteMultiByConfigidsInvcs(Object[] deletes) {
		delete("DELETE FROM Bd_InventoryRoutingInvc WHERE iInventoryRoutingConfigId IN (" + ArrayUtil.join(deletes, COMMA) + ") ");
	}

	public void deleteMultiByConfigidsSops(Object[] deletes) {
		delete("DELETE FROM Bd_InventoryRoutingSop WHERE iInventoryRoutingConfigId IN (" + ArrayUtil.join(deletes, COMMA) + ") ");
	}

	public void updateSeq(Long iitemroutingid) {
		//修改工序配置顺序号
		List<Record> recordList = dataList(iitemroutingid);
		if (recordList.size() > 0) {
			List<InventoryRoutingConfig> saveRecords = new ArrayList<>();
			Integer seq = 10;
			for (Record record : recordList) {
				InventoryRoutingConfig config = new InventoryRoutingConfig();
				Long configid = record.getLong("iautoid");
				config.setIAutoId(configid);
				config.setISeq(seq);
				saveRecords.add(config);
				seq += 10;
			}
			batchUpdate(saveRecords, 500);
		}
	}

    public Ret updateRoutingConfig(Record inventoryRoutingConfig) {
		tx(() -> {
			//修改工序配置
			//inventoryRoutingConfig.setIAutoId(autoid);
			String coperationname = inventoryRoutingConfig.getStr("COperationName");
			Long iAutoId = inventoryRoutingConfig.getLong("iAutoId");
			String ioperationIds = "";
			if (StrUtil.isNotBlank(coperationname)) {
				String[] arry = coperationname.split("\\^");
				inventoryRoutingConfig.set("COperationName",arry[0]);
				ioperationIds = arry[1];
			}
			updateRecord(inventoryRoutingConfig);
			//工序名称子表
			if (StrUtil.isNotBlank(ioperationIds)){
				String[] arry = ioperationIds.split("/");
				List<Long> longList = new ArrayList<>();
				for (int i = 0; i < arry.length; i++) {
					InventoryroutingconfigOperation operation = new InventoryroutingconfigOperation();
					Long operautoid = JBoltSnowflakeKit.me.nextId();
					operation.setIAutoId(operautoid);
					operation.setIInventoryRoutingConfigId(iAutoId);
					operation.setIOperationId(Long.parseLong(arry[i]));
					operation.setICreateBy(JBoltUserKit.getUserId());
					operation.setCCreateName(JBoltUserKit.getUserName());
					operation.setDCreateTime(new Date());
					longList.add(operautoid);
					operation.save();
				}
				deleteMultiByIdsOper(iAutoId, longList.toArray());
			}
			return true;
		});
		return SUCCESS;
    }

	public void deleteMultiByIdsOper(Long coonfigid, Object[] deletes) {
		delete("DELETE FROM Bd_InventoryRoutingConfig_Operation WHERE iInventoryRoutingConfigId = ? AND iautoid NOT IN (" + ArrayUtil.join(deletes, COMMA) + ") ", coonfigid);
	}


	public Ret moveDown(Long iitemroutingid, Long iitemroutingconfigid, Integer seq) {
		tx(() -> {
			Integer nextseq = seq + 10;
			Long nextid = getIdByseq(iitemroutingid, nextseq);
			if (isOk(nextid)) {
				InventoryRoutingConfig config = new InventoryRoutingConfig();
				config.setIAutoId(iitemroutingconfigid);
				config.setISeq(nextseq);
				config.update();

				InventoryRoutingConfig config2 = new InventoryRoutingConfig();
				config2.setIAutoId(nextid);
				config2.setISeq(seq);
				config2.update();
			}
			return true;
		});
		return SUCCESS;
	}

	public Ret moveUp(Long iitemroutingid, Long iitemroutingconfigid, Integer seq) {
		tx(() -> {
			Integer nextseq = seq - 10;
			Long nextid = getIdByseq(iitemroutingid, nextseq);
			if (isOk(nextid)) {
				InventoryRoutingConfig config = new InventoryRoutingConfig();
				config.setIAutoId(iitemroutingconfigid);
				config.setISeq(nextseq);
				config.update();

				InventoryRoutingConfig config2 = new InventoryRoutingConfig();
				config2.setIAutoId(nextid);
				config2.setISeq(seq);
				config2.update();
			}
			return true;
		});
		return SUCCESS;
	}

	public Long getIdByseq(Long routingId, Integer seq) {
		Long iAutoId = queryLong("select iAutoId from Bd_InventoryRoutingConfig where iInventoryRoutingId = ? and iSeq = ? ", routingId, seq);
		return iAutoId;
	}

    public List<Record> getByiInventoryRoutingId(long iInventoryRoutingId) {
        return findRecords("SELECT * FROM Bd_InventoryRoutingConfig WHERE iInventoryRoutingId = ? ORDER BY iSeq ", iInventoryRoutingId);
    }

    public void saveRoutingConfigAndOperation(Long orgId, Long userId, String userName, Date date, List<Record> recordList){
		
		List<InventoryRoutingConfig> inventoryRoutingConfigList = createInventoryRoutingConfigList(orgId, userId, userName, true, date, recordList);
		List<InventoryRoutingConfig> addConfigs = inventoryRoutingConfigList.stream().filter(obj->true == obj.getIsAdd()).collect(Collectors.toList());
		List<InventoryRoutingConfig> updateConfigs = inventoryRoutingConfigList.stream().filter(obj->false == obj.getIsAdd()).collect(Collectors.toList());
		
		List<InventoryroutingconfigOperation> inventoryroutingconfigOperationList = new ArrayList<>();
		for (InventoryRoutingConfig inventoryRoutingConfig :inventoryRoutingConfigList){
		
			String[] splitCOperationName = getSplitCOperationName(inventoryRoutingConfig.getCOperationName());
			if (splitCOperationName.length == 1){
				String cOperationName = queryColumn("SELECT cOperationName FROM Bd_InventoryRoutingConfig WHERE iAutoId = ?",inventoryRoutingConfig.getIAutoId());
				if (!splitCOperationName[0].equals(cOperationName)){
					List<Long> longList = new ArrayList<>();
					InventoryroutingconfigOperation inventoryroutingconfigOperation = inventoryroutingconfigOperationService.create(inventoryRoutingConfig.getIAutoId(), null, splitCOperationName[0], userId, userName, null, date,longList);
					inventoryroutingconfigOperation.save();
					deleteMultiByIdsOper(inventoryRoutingConfig.getIAutoId(), longList.toArray());
				}
				continue;
			}
			inventoryRoutingConfig.setCOperationName(splitCOperationName[0]);
			String operationIds = splitCOperationName[1];
			String[] arry = operationIds.split("/");


			List<Long> longList = new ArrayList<>();
			for (int i = 0; i < arry.length; i++) {
				long operationId = Long.parseLong(arry[i]);
				String cOperationName = queryColumn("SELECT cOperationName FROM Bd_Operation WHERE iAutoId = ?",operationId);
				InventoryroutingconfigOperation inventoryroutingconfigOperation = inventoryroutingconfigOperationService.create(inventoryRoutingConfig.getIAutoId(), operationId, cOperationName, userId, userName, null, date,longList);
				inventoryroutingconfigOperation.save();
				//longList.add(operationId);
			}
			deleteMultiByIdsOper(inventoryRoutingConfig.getIAutoId(), longList.toArray());
		}
		
		if (CollUtil.isNotEmpty(addConfigs)){
			batchSave(addConfigs);
		}
		
		if (CollUtil.isNotEmpty(updateConfigs)){
			batchUpdate(updateConfigs);
		}
	
	}
	
	public String[] getSplitCOperationName(String cOperationName){
		if (StrUtil.isBlank(cOperationName)){
			return null;
		}
		return cOperationName.split("\\^");
	}
}
