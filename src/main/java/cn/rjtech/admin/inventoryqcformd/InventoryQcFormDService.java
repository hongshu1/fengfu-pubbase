package cn.rjtech.admin.inventoryqcformd;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.InventoryQcFormD;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 点检表格 Service
 * @ClassName: InventoryQcFormDService
 * @author: RJ
 * @date: 2023-04-13 16:39
 */
public class InventoryQcFormDService extends BaseService<InventoryQcFormD> {

	private final InventoryQcFormD dao = new InventoryQcFormD().dao();

	@Override
	protected InventoryQcFormD dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<InventoryQcFormD> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param inventoryQcFormD
	 * @return
	 */
	public Ret save(InventoryQcFormD inventoryQcFormD) {
		if(inventoryQcFormD==null || isOk(inventoryQcFormD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(inventoryQcFormD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryQcFormD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventoryQcFormD.getIautoid(), JBoltUserKit.getUserId(), inventoryQcFormD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param inventoryQcFormD
	 * @return
	 */
	public Ret update(InventoryQcFormD inventoryQcFormD) {
		if(inventoryQcFormD==null || notOk(inventoryQcFormD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InventoryQcFormD dbInventoryQcFormD=findById(inventoryQcFormD.getIAutoId());
		if(dbInventoryQcFormD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inventoryQcFormD.getName(), inventoryQcFormD.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryQcFormD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inventoryQcFormD.getIautoid(), JBoltUserKit.getUserId(), inventoryQcFormD.getName());
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
	 * @param inventoryQcFormD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InventoryQcFormD inventoryQcFormD, Kv kv) {
		//addDeleteSystemLog(inventoryQcFormD.getIautoid(), JBoltUserKit.getUserId(),inventoryQcFormD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inventoryQcFormD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(InventoryQcFormD inventoryQcFormD, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(inventoryQcFormD, kv);
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