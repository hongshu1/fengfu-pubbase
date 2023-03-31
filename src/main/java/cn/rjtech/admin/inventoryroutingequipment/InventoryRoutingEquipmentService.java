package cn.rjtech.admin.inventoryroutingequipment;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.InventoryRoutingEquipment;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 物料建模-工艺档案设备集
 * @ClassName: InventoryRoutingEquipmentService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 10:04
 */
public class InventoryRoutingEquipmentService extends BaseService<InventoryRoutingEquipment> {
	private final InventoryRoutingEquipment dao=new InventoryRoutingEquipment().dao();
	@Override
	protected InventoryRoutingEquipment dao() {
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
	public Page<InventoryRoutingEquipment> getAdminDatas(int pageNumber, int pageSize, String keywords) {
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
	 * @param inventoryRoutingEquipment
	 * @return
	 */
	public Ret save(InventoryRoutingEquipment inventoryRoutingEquipment) {
		if(inventoryRoutingEquipment==null || isOk(inventoryRoutingEquipment.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(inventoryRoutingEquipment.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryRoutingEquipment.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventoryRoutingEquipment.getIAutoId(), JBoltUserKit.getUserId(), inventoryRoutingEquipment.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param inventoryRoutingEquipment
	 * @return
	 */
	public Ret update(InventoryRoutingEquipment inventoryRoutingEquipment) {
		if(inventoryRoutingEquipment==null || notOk(inventoryRoutingEquipment.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InventoryRoutingEquipment dbInventoryRoutingEquipment=findById(inventoryRoutingEquipment.getIAutoId());
		if(dbInventoryRoutingEquipment==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inventoryRoutingEquipment.getName(), inventoryRoutingEquipment.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryRoutingEquipment.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inventoryRoutingEquipment.getIAutoId(), JBoltUserKit.getUserId(), inventoryRoutingEquipment.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param inventoryRoutingEquipment 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InventoryRoutingEquipment inventoryRoutingEquipment, Kv kv) {
		//addDeleteSystemLog(inventoryRoutingEquipment.getIAutoId(), JBoltUserKit.getUserId(),inventoryRoutingEquipment.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inventoryRoutingEquipment model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(InventoryRoutingEquipment inventoryRoutingEquipment, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

    public Ret saveEquipment(JBoltTable jBoltTable, Long configid) {
		tx(() -> {
			//新增
			List<InventoryRoutingEquipment> saveRecords = jBoltTable.getSaveModelList(InventoryRoutingEquipment.class);
			if (CollUtil.isNotEmpty(saveRecords)) {
				for (int i = 0; i < saveRecords.size(); i++) {
					Long autoid = JBoltSnowflakeKit.me.nextId();
					saveRecords.get(i).setIAutoId(autoid);
					saveRecords.get(i).setIInventoryRoutingConfigId(configid);
					saveRecords.get(i).setICreateBy(JBoltUserKit.getUserId());
					saveRecords.get(i).setCCreateName(JBoltUserKit.getUserName());
					saveRecords.get(i).setDCreateTime(new Date());
				}
				batchSave(saveRecords, 500);
			}

			//修改
			List<InventoryRoutingEquipment> updateRecords = jBoltTable.getUpdateModelList(InventoryRoutingEquipment.class);
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
		delete("DELETE FROM Bd_InventoryRoutingEquipment WHERE iAutoId IN (" + ArrayUtil.join(deletes, COMMA) + ") ");
	}

	public List<Record> dataList(Kv kv) {
		return dbTemplate("inventory.getRoutingEqps",kv).find();
	}
}