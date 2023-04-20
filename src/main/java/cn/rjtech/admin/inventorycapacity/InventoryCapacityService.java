package cn.rjtech.admin.inventorycapacity;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.InventoryCapacity;
/**
 * 物料班次产能 Service
 * @ClassName: InventoryCapacityService
 * @author: chentao
 * @date: 2023-04-18 19:04
 */
public class InventoryCapacityService extends BaseService<InventoryCapacity> {

	private final InventoryCapacity dao = new InventoryCapacity().dao();

	@Override
	protected InventoryCapacity dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<InventoryCapacity> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param inventoryCapacity
	 * @return
	 */
	public Ret save(InventoryCapacity inventoryCapacity) {
		if(inventoryCapacity==null || isOk(inventoryCapacity.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(inventoryCapacity.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryCapacity.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventoryCapacity.getIautoid(), JBoltUserKit.getUserId(), inventoryCapacity.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param inventoryCapacity
	 * @return
	 */
	public Ret update(InventoryCapacity inventoryCapacity) {
		if(inventoryCapacity==null || notOk(inventoryCapacity.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InventoryCapacity dbInventoryCapacity=findById(inventoryCapacity.getIAutoId());
		if(dbInventoryCapacity==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inventoryCapacity.getName(), inventoryCapacity.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryCapacity.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inventoryCapacity.getIautoid(), JBoltUserKit.getUserId(), inventoryCapacity.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		return deleteByIds(ids,true);
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		return deleteById(id,true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param inventoryCapacity 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InventoryCapacity inventoryCapacity, Kv kv) {
		//addDeleteSystemLog(inventoryCapacity.getIautoid(), JBoltUserKit.getUserId(),inventoryCapacity.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inventoryCapacity 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(InventoryCapacity inventoryCapacity, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(inventoryCapacity, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

}