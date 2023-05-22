package cn.rjtech.admin.inventoryroutinginvc;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ArrayUtil;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.model.momdata.Inventory;
import cn.rjtech.model.momdata.InventoryRoutingConfig;
import cn.rjtech.model.momdata.InventoryRoutingInvc;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.EncodeUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;

import cn.rjtech.model.momdata.InventoryRoutingInvc;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 物料建模-存货工艺工序物料集
 * @ClassName: InventoryRoutingInvcService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 10:05
 */
public class InventoryRoutingInvcService extends BaseService<InventoryRoutingInvc> {
	private final InventoryRoutingInvc dao=new InventoryRoutingInvc().dao();
	
	@Inject
	private InventoryService inventoryService;
	@Override
	protected InventoryRoutingInvc dao() {
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
	 * @return
	 */
	public Page<InventoryRoutingInvc> getAdminDatas(int pageNumber, int pageSize, String keywords) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //关键词模糊查询
        sql.like("cCreateName",keywords);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param inventoryRoutingInvc
	 * @return
	 */
	public Ret save(InventoryRoutingInvc inventoryRoutingInvc) {
		if(inventoryRoutingInvc==null || isOk(inventoryRoutingInvc.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(inventoryRoutingInvc.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryRoutingInvc.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventoryRoutingInvc.getIAutoId(), JBoltUserKit.getUserId(), inventoryRoutingInvc.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param inventoryRoutingInvc
	 * @return
	 */
	public Ret update(InventoryRoutingInvc inventoryRoutingInvc) {
		if(inventoryRoutingInvc==null || notOk(inventoryRoutingInvc.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InventoryRoutingInvc dbInventoryRoutingInvc=findById(inventoryRoutingInvc.getIAutoId());
		if(dbInventoryRoutingInvc==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inventoryRoutingInvc.getName(), inventoryRoutingInvc.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryRoutingInvc.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inventoryRoutingInvc.getIAutoId(), JBoltUserKit.getUserId(), inventoryRoutingInvc.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param inventoryRoutingInvc 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InventoryRoutingInvc inventoryRoutingInvc, Kv kv) {
		//addDeleteSystemLog(inventoryRoutingInvc.getIAutoId(), JBoltUserKit.getUserId(),inventoryRoutingInvc.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inventoryRoutingInvc model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(InventoryRoutingInvc inventoryRoutingInvc, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	public List<Record> dataList(Kv kv) {
		if (StrUtil.isNotBlank(kv.getStr(InventoryRoutingConfig.ITEMJSON))){
			String itemJson = EncodeUtils.decodeUrl(kv.getStr(InventoryRoutingConfig.ITEMJSON), EncodeUtils.UTF_8);
			JSONArray jsonArray = JSONObject.parseArray(itemJson);
			if (CollectionUtil.isEmpty(jsonArray)){
				return null;
			}
			List<Record> recordList = new ArrayList<>();
			for (int i=0; i<jsonArray.size(); i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Long invId = jsonObject.getLong("iinventoryid");
				Record record = new Record();
				record.setColumns(jsonObject.getInnerMap());
				if (ObjectUtil.isNotNull(invId)){
					Inventory inventory = inventoryService.findById(invId);
					ValidationUtils.notNull(inventory, "未找到存货信息");
					record.set(Inventory.CINVCODE, inventory.getCInvCode());
				}
				recordList.add(record);
			}
			return recordList;
		}
		if (StrUtil.isBlank(kv.getStr("configid")) && StrUtil.isBlank(kv.getStr("idsstr"))){
			return null;
		}
		return dbTemplate("inventory.getRoutingInvcs",kv).find();
	}
	
	public List<Record> findRoutingConfigId(Long routingConfigId){
		return dbTemplate("inventory.getRoutingInvcs", Okv.by("configid", routingConfigId)).find();
	}

	public Ret saveInvc(JBoltTable jBoltTable, Long iitemroutingconfigid) {
		tx(() -> {
			//新增
			List<InventoryRoutingInvc> saveRecords = jBoltTable.getSaveModelList(InventoryRoutingInvc.class);
			if (CollUtil.isNotEmpty(saveRecords)) {
				for (int i = 0; i < saveRecords.size(); i++) {
					Long autoid = JBoltSnowflakeKit.me.nextId();
					saveRecords.get(i).setIAutoId(autoid);
					saveRecords.get(i).setIInventoryRoutingConfigId(iitemroutingconfigid);
					saveRecords.get(i).setICreateBy(JBoltUserKit.getUserId());
					saveRecords.get(i).setCCreateName(JBoltUserKit.getUserName());
					saveRecords.get(i).setDCreateTime(new Date());
				}
				batchSave(saveRecords, 500);
			}

			//修改
			List<InventoryRoutingInvc> updateRecords = jBoltTable.getUpdateModelList(InventoryRoutingInvc.class);
			if (CollUtil.isNotEmpty(updateRecords)) {
				batchUpdate(updateRecords, 500);
			}

			// 删除
			Object[] deletes = jBoltTable.getDelete();
			if (ArrayUtil.isNotEmpty(deletes)) {
				deleteMultiByIds(deletes);
			}
			return true;
		});
		return SUCCESS;
	}
	
	public void deleteMultiByIds(Object[] deletes) {
		delete("DELETE FROM Bd_InventoryRoutingInvc WHERE iAutoId IN (" + ArrayUtil.join(deletes, COMMA) + ") ");
	}
	
	public void deleteByRoutingConfigIds(Object[] routingConfigIds){
		String sqlStr = "DELETE "+
				"FROM " +
				"Bd_InventoryRoutingInvc " +
				"WHERE " +
				"iInventoryRoutingConfigId IN ( "+ArrayUtil.join(routingConfigIds, COMMA)+" )";
		delete(sqlStr);
	}
	
	public void saveRoutingInv(Long userId, String userName, Date date, List<Record> recordList) {
		List<InventoryRoutingInvc> inventoryRoutingInvcList = new ArrayList<>();
		if (CollectionUtil.isEmpty(recordList)){
			return;
		}
		for (Record record : recordList){
			Long routingConfigId = record.getLong(InventoryRoutingConfig.IAUTOID);
			Object object = record.get(InventoryRoutingConfig.ITEMJSONSTR);
			if (ObjectUtil.isEmpty(object)){
				continue;
			}
			List<JSONObject> itemJson = null;
			if (object instanceof List){
				itemJson =(List<JSONObject> )object;
			}
			if (CollectionUtil.isEmpty(itemJson)){
				continue;
			}
			List<InventoryRoutingInvc> invcList = new ArrayList<>();
			for (JSONObject jsonObject : itemJson){
				Long id = JBoltSnowflakeKit.me.nextId();
				Long invId = jsonObject.getLong(InventoryRoutingInvc.IINVENTORYID.toLowerCase());
				BigDecimal usageUom = jsonObject.getBigDecimal(InventoryRoutingInvc.IUSAGEUOM.toLowerCase());
				InventoryRoutingInvc inventoryRoutingInvc = create(userId, id, routingConfigId, invId, userName, null, usageUom, date);
				invcList.add(inventoryRoutingInvc);
			}
			if (CollectionUtil.isNotEmpty(invcList)){
				batchSave(invcList, 500);
			}
		}
	}
	
	public InventoryRoutingInvc create(Long userId, Long id, Long routingConfigId, Long invId, String userName, String cMemo, BigDecimal usageUom, Date date){
		InventoryRoutingInvc inventoryRoutingInvc = new InventoryRoutingInvc();
		inventoryRoutingInvc.setIAutoId(id);
		inventoryRoutingInvc.setIInventoryId(invId);
		inventoryRoutingInvc.setIInventoryRoutingConfigId(routingConfigId);
		inventoryRoutingInvc.setICreateBy(userId);
		inventoryRoutingInvc.setCCreateName(userName);
		inventoryRoutingInvc.setCMemo(cMemo);
		inventoryRoutingInvc.setDCreateTime(date);
		inventoryRoutingInvc.setIUsageUOM(usageUom);
		return inventoryRoutingInvc;
	}
}
