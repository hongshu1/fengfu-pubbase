package cn.rjtech.admin.inventoryspotcheckformOperation;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.InventoryspotcheckformOperation;
/**
 * 质量建模-存货点检工序 Service
 * @ClassName: InventoryspotcheckformOperationService
 * @author: RJ
 * @date: 2023-04-03 16:24
 */
public class InventoryspotcheckformOperationService extends BaseService<InventoryspotcheckformOperation> {

	private final InventoryspotcheckformOperation dao = new InventoryspotcheckformOperation().dao();

	@Override
	protected InventoryspotcheckformOperation dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<InventoryspotcheckformOperation> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param inventoryspotcheckformOperation
	 * @return
	 */
	public Ret save(InventoryspotcheckformOperation inventoryspotcheckformOperation) {
		if(inventoryspotcheckformOperation==null || isOk(inventoryspotcheckformOperation.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(inventoryspotcheckformOperation.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryspotcheckformOperation.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventoryspotcheckformOperation.getIautoid(), JBoltUserKit.getUserId(), inventoryspotcheckformOperation.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param inventoryspotcheckformOperation
	 * @return
	 */
	public Ret update(InventoryspotcheckformOperation inventoryspotcheckformOperation) {
		if(inventoryspotcheckformOperation==null || notOk(inventoryspotcheckformOperation.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InventoryspotcheckformOperation dbInventoryspotcheckformOperation=findById(inventoryspotcheckformOperation.getIAutoId());
		if(dbInventoryspotcheckformOperation==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inventoryspotcheckformOperation.getName(), inventoryspotcheckformOperation.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryspotcheckformOperation.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inventoryspotcheckformOperation.getIautoid(), JBoltUserKit.getUserId(), inventoryspotcheckformOperation.getName());
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
	 * @param inventoryspotcheckformOperation 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InventoryspotcheckformOperation inventoryspotcheckformOperation, Kv kv) {
		//addDeleteSystemLog(inventoryspotcheckformOperation.getIautoid(), JBoltUserKit.getUserId(),inventoryspotcheckformOperation.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inventoryspotcheckformOperation 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(InventoryspotcheckformOperation inventoryspotcheckformOperation, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(inventoryspotcheckformOperation, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	public List<InventoryspotcheckformOperation> listByIinventorySpotCheckFormId(String iInventorySpotCheckFormId){
		return find("select * from Bd_InventorySpotCheckForm_Operation where iInventorySpotCheckFormId =? ",iInventorySpotCheckFormId);
	}
}