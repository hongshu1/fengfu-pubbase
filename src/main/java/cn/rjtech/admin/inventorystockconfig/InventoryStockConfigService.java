package cn.rjtech.admin.inventorystockconfig;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.InventoryStockConfig;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;

/**
 * 料品库存档案
 * @ClassName: InventoryStockConfigService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-24 13:53
 */
public class InventoryStockConfigService extends BaseService<InventoryStockConfig> {
	private final InventoryStockConfig dao=new InventoryStockConfig().dao();
	@Override
	protected InventoryStockConfig dao() {
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
     * @param isNegativeInventory 予需负库存
     * @param isLotControl 开启批号 1开启
     * @param isValid 开启有效期 1开启
     * @param isStructCode 开启一物一码管理
     * @param IsDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<InventoryStockConfig> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isNegativeInventory, Boolean isLotControl, Boolean isValid, Boolean isStructCode, Boolean IsDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isNegativeInventory",isNegativeInventory);
        sql.eqBooleanToChar("isLotControl",isLotControl);
        sql.eqBooleanToChar("isValid",isValid);
        sql.eqBooleanToChar("isStructCode",isStructCode);
        sql.eqBooleanToChar("IsDeleted",IsDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param inventoryStockConfig
	 * @return
	 */
	public Ret save(InventoryStockConfig inventoryStockConfig) {
		if(inventoryStockConfig==null || isOk(inventoryStockConfig.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(inventoryStockConfig.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		setInventoryClass(inventoryStockConfig);
		boolean success=inventoryStockConfig.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventoryStockConfig.getIAutoId(), JBoltUserKit.getUserId(), inventoryStockConfig.getName());
		}
		return ret(success);
	}

	/**
	 * 设置参数
	 * @param inventoryClass
	 * @return
	 */
	private InventoryStockConfig setInventoryClass(InventoryStockConfig inventoryClass){
		inventoryClass.setIsDeleted(false);
		Long userId = JBoltUserKit.getUserId();
		inventoryClass.setICreateBy(userId);
		inventoryClass.setIUpdateBy(userId);
		String userName = JBoltUserKit.getUserName();
		inventoryClass.setCCreateName(userName);
		inventoryClass.setCUpdateName(userName);
		Date date = new Date();
		inventoryClass.setDCreateTime(date);
		inventoryClass.setDUpdateTime(date);
		return inventoryClass;
	}

	/**
	 * 更新
	 * @param inventoryStockConfig
	 * @return
	 */
	public Ret update(InventoryStockConfig inventoryStockConfig) {
		if(inventoryStockConfig==null || notOk(inventoryStockConfig.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InventoryStockConfig dbInventoryStockConfig=findById(inventoryStockConfig.getIAutoId());
		if(dbInventoryStockConfig==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inventoryStockConfig.getName(), inventoryStockConfig.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryStockConfig.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inventoryStockConfig.getIAutoId(), JBoltUserKit.getUserId(), inventoryStockConfig.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param inventoryStockConfig 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InventoryStockConfig inventoryStockConfig, Kv kv) {
		//addDeleteSystemLog(inventoryStockConfig.getIAutoId(), JBoltUserKit.getUserId(),inventoryStockConfig.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inventoryStockConfig model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(InventoryStockConfig inventoryStockConfig, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(InventoryStockConfig inventoryStockConfig, String column, Kv kv) {
		//addUpdateSystemLog(inventoryStockConfig.getIAutoId(), JBoltUserKit.getUserId(), inventoryStockConfig.getName(),"的字段["+column+"]值:"+inventoryStockConfig.get(column));
		/**
		switch(column){
		    case "isNegativeInventory":
		        break;
		    case "isLotControl":
		        break;
		    case "isValid":
		        break;
		    case "isStructCode":
		        break;
		    case "IsDeleted":
		        break;
		}
		*/
		return null;
	}

}