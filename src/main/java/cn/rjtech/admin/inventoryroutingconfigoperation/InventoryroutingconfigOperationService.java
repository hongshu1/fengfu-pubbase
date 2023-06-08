package cn.rjtech.admin.inventoryroutingconfigoperation;

import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.InventoryroutingconfigOperation;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 物料建模-存货工艺工序
 * @ClassName: InventoryroutingconfigOperationService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 10:03
 */
public class InventoryroutingconfigOperationService extends BaseService<InventoryroutingconfigOperation> {
	private final InventoryroutingconfigOperation dao=new InventoryroutingconfigOperation().dao();
	@Override
	protected InventoryroutingconfigOperation dao() {
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
	public Page<InventoryroutingconfigOperation> getAdminDatas(int pageNumber, int pageSize, String keywords) {
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
	 * @param inventoryroutingconfigOperation
	 * @return
	 */
	public Ret save(InventoryroutingconfigOperation inventoryroutingconfigOperation) {
		if(inventoryroutingconfigOperation==null || isOk(inventoryroutingconfigOperation.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(inventoryroutingconfigOperation.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryroutingconfigOperation.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventoryroutingconfigOperation.getIAutoId(), JBoltUserKit.getUserId(), inventoryroutingconfigOperation.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param inventoryroutingconfigOperation
	 * @return
	 */
	public Ret update(InventoryroutingconfigOperation inventoryroutingconfigOperation) {
		if(inventoryroutingconfigOperation==null || notOk(inventoryroutingconfigOperation.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InventoryroutingconfigOperation dbInventoryroutingconfigOperation=findById(inventoryroutingconfigOperation.getIAutoId());
		if(dbInventoryroutingconfigOperation==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inventoryroutingconfigOperation.getName(), inventoryroutingconfigOperation.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryroutingconfigOperation.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inventoryroutingconfigOperation.getIAutoId(), JBoltUserKit.getUserId(), inventoryroutingconfigOperation.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param inventoryroutingconfigOperation 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InventoryroutingconfigOperation inventoryroutingconfigOperation, Kv kv) {
		//addDeleteSystemLog(inventoryroutingconfigOperation.getIAutoId(), JBoltUserKit.getUserId(),inventoryroutingconfigOperation.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inventoryroutingconfigOperation model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(InventoryroutingconfigOperation inventoryroutingconfigOperation, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}
	
	public InventoryroutingconfigOperation create(Long iInventoryRoutingConfigId, Long iOperationId, Long iCreateBy, String cCreateName, String cMemo, Date dCreateTime){
		InventoryroutingconfigOperation inventoryroutingconfigOperation = new InventoryroutingconfigOperation();
		inventoryroutingconfigOperation.setIAutoId(JBoltSnowflakeKit.me.nextId());
		inventoryroutingconfigOperation.setIInventoryRoutingConfigId(iInventoryRoutingConfigId);
		inventoryroutingconfigOperation.setIOperationId(iOperationId);
		inventoryroutingconfigOperation.setICreateBy(iCreateBy);
		inventoryroutingconfigOperation.setCCreateName(cCreateName);
		inventoryroutingconfigOperation.setCMemo(cMemo);
		inventoryroutingconfigOperation.setDCreateTime(dCreateTime);
		return inventoryroutingconfigOperation;
	}

	public void deleteByRoutingConfigIds(Object[] routingConfigIds){
		delete("DELETE Bd_InventoryRoutingConfig_Operation WHERE iInventoryRoutingConfigId IN (" + ArrayUtil.join(routingConfigIds, COMMA) +")");
	}
}
