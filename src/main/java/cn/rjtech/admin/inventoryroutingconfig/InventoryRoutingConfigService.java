package cn.rjtech.admin.inventoryroutingconfig;

import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.InventoryRoutingConfig;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 物料建模-存货工艺配置
 * @ClassName: InventoryRoutingConfigService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 10:02
 */
public class InventoryRoutingConfigService extends BaseService<InventoryRoutingConfig> {
	private final InventoryRoutingConfig dao=new InventoryRoutingConfig().dao();
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

	public List<Record> dataList(Long iinventoryid) {
		return dbTemplate("inventoryclass.getRouingConfigs", Okv.by("iinventoryid", iinventoryid)).find();
	}

	/**
	 * 删除 指定多个ID
	 *
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		if (!JBoltUserKit.isSystemAdmin()){
			ValidationUtils.isTrue(false,"当前账号为非超级管理员，不能删除!");
		}
		tx(() -> {
			String[] idarry = ids.split(",");
			InventoryRoutingConfig inventoryRoutingConfig = findById(idarry[0]);
			//工序子表
			deleteMultiByConfigidsOper(idarry);
			//设备集子表
			deleteMultiByConfigidsEquipment(idarry);
			//存货工艺工序
			deleteMultiByConfigidsInvcs(idarry);
			//存货工艺工序物料集
			deleteMultiByConfigidsSops(idarry);

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
}