package cn.rjtech.admin.inventoryrouting;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.inventoryroutingconfig.InventoryRoutingConfigService;
import cn.rjtech.admin.inventoryroutingconfigoperation.InventoryroutingconfigOperationService;
import cn.rjtech.admin.inventoryroutingequipment.InventoryRoutingEquipmentService;
import cn.rjtech.admin.inventoryroutinginvc.InventoryRoutingInvcService;
import cn.rjtech.admin.inventoryroutingsop.InventoryRoutingSopService;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 物料建模-存货工艺档案
 * @ClassName: InventoryRoutingService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 10:02
 */
public class InventoryRoutingService extends BaseService<InventoryRouting> {
	private final InventoryRouting dao=new InventoryRouting().dao();

	@Inject
	private InventoryRoutingConfigService inventoryRoutingConfigService;
	@Inject
	private InventoryroutingconfigOperationService inventoryroutingconfigOperationService;
	@Inject
	private InventoryRoutingEquipmentService inventoryRoutingEquipmentService;
	@Inject
	private InventoryRoutingInvcService inventoryRoutingInvcService;
	@Inject
	private InventoryRoutingSopService inventoryRoutingSopService;
	@Override
	protected InventoryRouting dao() {
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
	 * @return
	 */
	public Page<InventoryRouting> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isEnabled) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isEnabled",isEnabled);
        //关键词模糊查询
        sql.likeMulti(keywords,"cRoutingName", "cCreateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param inventoryRouting
	 * @return
	 */
	public Ret save(InventoryRouting inventoryRouting) {
		if(inventoryRouting==null || isOk(inventoryRouting.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(inventoryRouting.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryRouting.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventoryRouting.getIAutoId(), JBoltUserKit.getUserId(), inventoryRouting.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param inventoryRouting
	 * @return
	 */
	public Ret update(InventoryRouting inventoryRouting) {
		if(inventoryRouting==null || notOk(inventoryRouting.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InventoryRouting dbInventoryRouting=findById(inventoryRouting.getIAutoId());
		if(dbInventoryRouting==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inventoryRouting.getName(), inventoryRouting.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryRouting.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inventoryRouting.getIAutoId(), JBoltUserKit.getUserId(), inventoryRouting.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param inventoryRouting 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InventoryRouting inventoryRouting, Kv kv) {
		//addDeleteSystemLog(inventoryRouting.getIAutoId(), JBoltUserKit.getUserId(),inventoryRouting.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inventoryRouting model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(InventoryRouting inventoryRouting, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(InventoryRouting inventoryRouting, String column, Kv kv) {
		//addUpdateSystemLog(inventoryRouting.getIAutoId(), JBoltUserKit.getUserId(), inventoryRouting.getName(),"的字段["+column+"]值:"+inventoryRouting.get(column));
		/**
		switch(column){
		    case "isEnabled":
		        break;
		}
		*/
		return null;
	}

    public List<Record> dataList(Long iinventoryid) {
		List<Record> list = dbTemplate("inventoryclass.getRouings", Okv.by("iinventoryid", iinventoryid)).find();
		if (CollUtil.isNotEmpty(list)){
			for (Record record :list){
				Integer iAuditStatus = record.getInt(InventoryRouting.IAUDITSTATUS);
				ValidationUtils.notNull(iAuditStatus, "该工艺路线缺少审批状态！");
				AuditStatusEnum auditStatusEnum = AuditStatusEnum.toEnum(iAuditStatus);
				ValidationUtils.notNull(auditStatusEnum, "未知审批状态");
				record.set(InventoryRouting.CAUDITSTATUSTEXT, auditStatusEnum.getText());
			}
		}
		return list;
	}

	public Ret saveItemRouting(JBoltTable jBoltTable, Long masterautoid) {
		tx(() -> {
			//新增
			List<InventoryRouting> inventoryRoutingList = jBoltTable.getSaveModelList(InventoryRouting.class);
			Long userId = JBoltUserKit.getUserId();
			String userName = JBoltUserKit.getUserName();
			DateTime date = DateUtil.date();
			if (CollUtil.isNotEmpty(inventoryRoutingList)) {
				for (int i = 0; i < inventoryRoutingList.size(); i++) {
					InventoryRouting inventoryRouting = inventoryRoutingList.get(i);
					//被复制工艺的id 用于查询子表数据
					Long preRoutingId = inventoryRouting.getIAutoId();
					Long autoId = JBoltSnowflakeKit.me.nextId();  //新生成工序的id
					inventoryRouting.setIAutoId(autoId);
					inventoryRouting.setIInventoryId(masterautoid);
					inventoryRouting.setIsEnabled(false);
					inventoryRouting.setICreateBy(userId);
					inventoryRouting.setCCreateName(userName);
					inventoryRouting.setDCreateTime(date);
					inventoryRouting.setIsDeleted(false);
					inventoryRouting.setIAuditStatus(AuditStatusEnum.NOT_AUDIT.getValue());
					if (isOk(preRoutingId)) {
						//更新被复制工艺为停用状态
//						update("UPDATE Bd_InventoryRouting SET isEnabled = 0 WHERE iAutoId = ?",preroutingid);
						//工序配置子表   .set("isEnabled",1)
						List<InventoryRoutingConfig> inventoryRoutingConfigList = inventoryRoutingConfigService.getCommonList(Okv.by("iInventoryRoutingId", preRoutingId));
						for (InventoryRoutingConfig inventoryRoutingConfig : inventoryRoutingConfigList) {
							Long preConfigId = inventoryRoutingConfig.getIAutoId();  //被复制工序配置的id
							Long newConfigId = JBoltSnowflakeKit.me.nextId();
							inventoryRoutingConfig.setIAutoId(newConfigId);
							inventoryRoutingConfig.setIInventoryRoutingId(autoId);

							//工序子表
							List<InventoryroutingconfigOperation> inventoryRoutingConfigOperationList = inventoryroutingconfigOperationService.getCommonList(Okv.by("iInventoryRoutingConfigId", preConfigId));
							for (InventoryroutingconfigOperation inventoryRoutingConfigOperation : inventoryRoutingConfigOperationList) {
								Long id = JBoltSnowflakeKit.me.nextId();
								inventoryRoutingConfigOperation.setIAutoId(id);
								inventoryRoutingConfigOperation.setIInventoryRoutingConfigId(newConfigId);
							}
							inventoryroutingconfigOperationService.batchSave(inventoryRoutingConfigOperationList);

							//设备集子表
							List<InventoryRoutingEquipment> inventoryRoutingEquipmentList = inventoryRoutingEquipmentService.getCommonList(Okv.by("iInventoryRoutingConfigId", preConfigId));
							for (InventoryRoutingEquipment inventoryRoutingEquipment : inventoryRoutingEquipmentList) {
								Long id = JBoltSnowflakeKit.me.nextId();
								inventoryRoutingEquipment.setIAutoId(id);
								inventoryRoutingEquipment.setIInventoryRoutingConfigId(newConfigId);
							}
							inventoryRoutingEquipmentService.batchSave(inventoryRoutingEquipmentList);

							//存货工艺工序物料集
							List<InventoryRoutingInvc> inventoryRoutingInvcList = inventoryRoutingInvcService.getCommonList(Okv.by("iInventoryRoutingConfigId", preConfigId));
							for (InventoryRoutingInvc inventoryRoutingInvc : inventoryRoutingInvcList) {
								Long id = JBoltSnowflakeKit.me.nextId();
								inventoryRoutingInvc.setIAutoId(id);
								inventoryRoutingInvc.setIInventoryRoutingConfigId(newConfigId);
							}
							inventoryRoutingInvcService.batchSave(inventoryRoutingInvcList);

							//存货工序作业指导书
							List<InventoryRoutingSop> inventoryRoutingSopList = inventoryRoutingSopService.getCommonList(Okv.by("iInventoryRoutingConfigId", preConfigId));
							for (InventoryRoutingSop inventoryRoutingSop : inventoryRoutingSopList) {
								Long id = JBoltSnowflakeKit.me.nextId();
								inventoryRoutingSop.setIAutoId(id);
								inventoryRoutingSop.setIInventoryRoutingConfigId(newConfigId);
							}
							inventoryRoutingSopService.batchSave(inventoryRoutingSopList);

						}
						inventoryRoutingConfigService.batchSave(inventoryRoutingConfigList);
					}
				}
				batchSave(inventoryRoutingList, 500);
			}
			//修改
			List<InventoryRouting> updateRecords = jBoltTable.getUpdateModelList(InventoryRouting.class);
			if (CollUtil.isNotEmpty(updateRecords)) {
				batchUpdate(updateRecords, 500);
			}
			// 删除
			Object[] deletes = jBoltTable.getDelete();
			if (ArrayUtil.isNotEmpty(deletes)) {
				deleteMultiByConfigids(deletes);
			}
			return true;
		});
		return SUCCESS;
	}

	public void deleteMultiByConfigids(Object[] deletes) {
		String ids = "";
		List<InventoryRoutingConfig> list = inventoryRoutingConfigService.find("SELECT iAutoId FROM Bd_InventoryRoutingConfig WHERE iInventoryRoutingId IN (" + ArrayUtil.join(deletes, COMMA) + ") ");
		for (int i = 0; i < list.size(); i++) {
			String id = String.valueOf(list.get(i).getIAutoId());
			if (i == (list.size() - 1)) {
				ids = ids.concat(id);
			} else {
				ids = ids.concat(id).concat(",");
			}
		}
		//删除工序配置子表以及所有下级子表数据
		if (StrUtil.isNotBlank(ids)) {
			inventoryRoutingConfigService.deleteByBatchIds(ids);
		}
		deleteMultiByIds(deletes);
	}

	public void deleteMultiByIds(Object[] deletes) {
		delete("UPDATE Bd_InventoryRouting SET isDeleted = 1 WHERE iautoid IN (" + ArrayUtil.join(deletes, COMMA) + ") ");
	}

	public String getprocessViewStr(Long iinventoryroutingid) {
		List<InventoryRoutingConfig> routingConfigs = inventoryRoutingConfigService.find(inventoryRoutingConfigService.selectSql().eq("iInventoryRoutingId", iinventoryroutingid).eq("isEnabled","1").orderBy("iSeq","ASC"));
		StringBuilder processViewStr = new StringBuilder("graph TD\nA([开始])");
		if(routingConfigs != null && routingConfigs.size() > 0){
			if(routingConfigs.get(0).getISeq() == null||StrUtil.isBlank(routingConfigs.get(0).getCOperationName())){
				return processViewStr.toString();
			}
			processViewStr.append(" --> ").append(routingConfigs.get(0).getISeq()).append("[").append(routingConfigs.get(0).getCOperationName()).append("]\n");
			List<StringBuilder> parallels = new ArrayList<>();
			InventoryRoutingConfig last = routingConfigs.get(0);
			for (int i =1;i<routingConfigs.size();i++){
				InventoryRoutingConfig config = routingConfigs.get(i);
				if (config.getIType() != null&&1 == config.getIType().intValue() && routingConfigs.get(0).getISeq() != null&&StrUtil.isNotBlank(routingConfigs.get(0).getCOperationName())){
					processViewStr.append(last.getISeq()).append(" --> ").append(config.getISeq()).append("[").append(config.getCOperationName()).append("]\n");

					if(parallels.size() > 0){
						for (StringBuilder parallel : parallels) {
							processViewStr.append(parallel).append(" --> ").append(config.getISeq()).append("\n");
						}
						parallels.clear();
					}
					last = config;
				}else if(config.getIType() != null && routingConfigs.get(0).getISeq() != null&&StrUtil.isNotBlank(routingConfigs.get(0).getCOperationName())){
					StringBuilder parallel = new StringBuilder(config.getISeq().intValue()+"").append("[").append(config.getCOperationName()).append("]");
					parallels.add(parallel);
				}
			}
		}
		return processViewStr.toString();
	}

	public List<JsTreeBean> getMgrTree(Long iinventoryroutingid) {
		Kv kv = new Kv();
		kv.set("iinventoryroutingid",iinventoryroutingid);
		List<Record> configs = dbTemplate("inventoryclass.getRouingConfigs", kv).find();
		List<JsTreeBean> jsTreeBeanList = new ArrayList<>();
		JsTreeBean parent = new JsTreeBean("1","#","成品名称",null,"",false);
		JsTreeBean parent1 = new JsTreeBean("2","#","半成品名称",null,"",false);
		jsTreeBeanList.add(parent1);
		jsTreeBeanList.add(parent);
		StringBuilder ids = new StringBuilder();
		Long finishedId = null;
		Long semiFinishedId = null;
		for (Record inventoryClass : configs){
			Long id = inventoryClass.getLong("iautoid");
			ids.append(id).append(",");
			String type = String.valueOf(inventoryClass.getInt("itype"));
			String pid = "";
			String text ="";
			if(inventoryClass.getLong("irsinventoryid") == null){
				pid="1";
				text = "虚拟件:"+ inventoryClass.getStr("coperationname");
				if(finishedId != null)
					pid = finishedId +"";
				finishedId = inventoryClass.getLong("iautoid");
			}else {
				pid="2";
				text = inventoryClass.getStr("rsinventoryname");
				if(semiFinishedId != null)
					pid = semiFinishedId +"";
				semiFinishedId = inventoryClass.getLong("iautoid");
			}

			JsTreeBean jsTreeBean = new JsTreeBean(id,pid,text,type,"",false);
			jsTreeBeanList.add(jsTreeBean);
		}
		if(StrUtil.isNotBlank(ids)){
			Kv kv1 = new Kv();
			kv1.put("idsstr",ids.substring(0,ids.length()-1));
			List<Record> invcs = dbTemplate("inventory.getRoutingInvcs", kv1).find();
			if(invcs != null && invcs.size() > 0){
				for (Record invc : invcs) {
					Long id = invc.getLong("iautoid");
					ids.append(id).append(",");
					String type = invc.getStr("cinvcode");
					String pid = invc.getLong("iinventoryroutingconfigid")+"";
					String text =invc.getStr("cinvname");
					JsTreeBean jsTreeBean = new JsTreeBean(id,pid,text,type,"",false);
					jsTreeBeanList.add(jsTreeBean);
				}
			}
		}
		return jsTreeBeanList;
	}

	public void copy(Long iAutoId,Long newId) {
		//复制工艺
		List<InventoryRouting> inventoryRoutings = find(selectSql().eq("iInventoryId", iAutoId));
		if(inventoryRoutings != null && inventoryRoutings.size() > 0){
			for (InventoryRouting inventoryRouting : inventoryRoutings) {
				inventoryRouting.setIInventoryId(newId);
				Long newconfigid = JBoltSnowflakeKit.me.nextId();
				copys(newconfigid,inventoryRouting.getIAutoId());
				inventoryRouting.setIAutoId(newconfigid);
				inventoryRouting.save();
			}
		}


	}

	public void copys(Long IautoId,Long oldId){
		List<InventoryRoutingConfig> itemroutingconfigList = inventoryRoutingConfigService.getCommonList(Okv.by("iInventoryRoutingId", oldId));
		for (InventoryRoutingConfig itemroutingconfig : itemroutingconfigList) {
			Long preconfigid = itemroutingconfig.getIAutoId();  //被复制工序配置的id
			Long newconfigid = JBoltSnowflakeKit.me.nextId();
			itemroutingconfig.setIAutoId(newconfigid);
			itemroutingconfig.setIInventoryRoutingId(IautoId);

			//工序子表
			List<InventoryroutingconfigOperation> itemroutingoperationList = inventoryroutingconfigOperationService.getCommonList(Okv.by("iInventoryRoutingConfigId", preconfigid));
			for (InventoryroutingconfigOperation table : itemroutingoperationList) {
				Long newid = JBoltSnowflakeKit.me.nextId();
				table.setIAutoId(newid);
				table.setIInventoryRoutingConfigId(newconfigid);
			}
			inventoryroutingconfigOperationService.batchSave(itemroutingoperationList);

			//设备集子表
			List<InventoryRoutingEquipment> itemroutingequipmentList = inventoryRoutingEquipmentService.getCommonList(Okv.by("iInventoryRoutingConfigId", preconfigid));
			for (InventoryRoutingEquipment table : itemroutingequipmentList) {
				Long newid = JBoltSnowflakeKit.me.nextId();
				table.setIAutoId(newid);
				table.setIInventoryRoutingConfigId(newconfigid);
			}
			inventoryRoutingEquipmentService.batchSave(itemroutingequipmentList);

			//存货工艺工序物料集
			List<InventoryRoutingInvc> inventoryRoutingInvcs = inventoryRoutingInvcService.getCommonList(Okv.by("iInventoryRoutingConfigId", preconfigid));
			for (InventoryRoutingInvc table : inventoryRoutingInvcs) {
				Long newid = JBoltSnowflakeKit.me.nextId();
				table.setIAutoId(newid);
				table.setIInventoryRoutingConfigId(newconfigid);
			}
			inventoryRoutingInvcService.batchSave(inventoryRoutingInvcs);

			//存货工序作业指导书
			List<InventoryRoutingSop> inventoryRoutingSops = inventoryRoutingSopService.getCommonList(Okv.by("iInventoryRoutingConfigId", preconfigid));
			for (InventoryRoutingSop table : inventoryRoutingSops) {
				Long newid = JBoltSnowflakeKit.me.nextId();
				table.setIAutoId(newid);
				table.setIInventoryRoutingConfigId(newconfigid);
			}
			inventoryRoutingSopService.batchSave(inventoryRoutingSops);

		}
		inventoryRoutingConfigService.batchSave(itemroutingconfigList);
	}

    public InventoryRouting getCurrentRouting(Long iAutoId) {
        Date date = new Date();
        return findFirst(selectSql().eq("iInventoryId",iAutoId).ge("dToDate",date).le("dFromDate",date));
    }

    public int updateEnable(Long invId, Integer isEnable){
		return update(" UPDATE Bd_InventoryRouting SET isEnabled = ? WHERE iInventoryId = ?", isEnable, invId);
	}
	
	
	public Integer queryAwaitAudit(Long id, Long invId){
		String sqlStr = "SELECT * FROM Bd_InventoryRouting WHERE iInventoryId = ? AND iAuditStatus IN (1)";
		if (ObjUtil.isNotNull(id)){
			sqlStr = sqlStr.concat(" AND iAutoId <> "+id);
		}
		return queryInt(sqlStr, invId);
	}
}
