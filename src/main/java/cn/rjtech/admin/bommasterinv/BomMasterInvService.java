package cn.rjtech.admin.bommasterinv;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.bomcompare.BomCompareService;
import cn.rjtech.model.momdata.BomCompare;
import cn.rjtech.model.momdata.BomMasterInv;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 基础档案-母件物料存货集（新增/修改版本变更时处理，定时处理更新）
 * @ClassName: BomMasterInvService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-12 10:19
 */
public class BomMasterInvService extends BaseService<BomMasterInv> {
	private final BomMasterInv dao=new BomMasterInv().dao();
	
	@Inject
	private BomCompareService bomCompareService;
	
	@Override
	protected BomMasterInv dao() {
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
	 * @return
	 */
	public Page<BomMasterInv> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param bomMasterInv
	 * @return
	 */
	public Ret save(BomMasterInv bomMasterInv) {
		if(bomMasterInv==null || isOk(bomMasterInv.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(bomMasterInv.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=bomMasterInv.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(bomMasterInv.getIAutoId(), JBoltUserKit.getUserId(), bomMasterInv.getName());
		}
		return ret(success);
	}
	
	public BomMasterInv createBomMasterInv(Long orgId, Long bomMasterId, Long masterInventoryId, Long bomCompareId, Long compareInventoryId){
		BomMasterInv bomMasterInv = new BomMasterInv();
		bomMasterInv.setIAutoId(JBoltSnowflakeKit.me.nextId());
		bomMasterInv.setIOrgId(orgId);
		bomMasterInv.setIBomMasterId(bomMasterId);
		bomMasterInv.setIMasterInventoryId(masterInventoryId);
		bomMasterInv.setIBomCompareId(bomCompareId);
		bomMasterInv.setICompareInventoryId(compareInventoryId);
		return bomMasterInv;
	}

	/**
	 * 更新
	 * @param bomMasterInv
	 * @return
	 */
	public Ret update(BomMasterInv bomMasterInv) {
		if(bomMasterInv==null || notOk(bomMasterInv.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		BomMasterInv dbBomMasterInv=findById(bomMasterInv.getIAutoId());
		if(dbBomMasterInv==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(bomMasterInv.getName(), bomMasterInv.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=bomMasterInv.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(bomMasterInv.getIAutoId(), JBoltUserKit.getUserId(), bomMasterInv.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param bomMasterInv 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(BomMasterInv bomMasterInv, Kv kv) {
		//addDeleteSystemLog(bomMasterInv.getIAutoId(), JBoltUserKit.getUserId(),bomMasterInv.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param bomMasterInv model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(BomMasterInv bomMasterInv, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}
	
	/**
	 *
	 * @param bomMasterId 母件Id
	 * @param bomMasterInvId 母件存货id
	 * @param bomCompareList 子件集合
	 */
	public List<BomMasterInv> getBomMasterInv(Long orgId, Long bomMasterId, Long bomMasterInvId, List<BomCompare> bomCompareList){
		Map<Long, BomCompare> bomCompareMap= new HashMap<>();
		for (BomCompare bomCompare :bomCompareList){
			bomCompareMap.put(bomCompare.getIAutoId(), bomCompare);
		}
		List<BomCompare> bomComparesAll = downToFind(bomCompareList, bomMasterId);
		Map<Long, List<BomCompare>> bomCompareChildMap = new HashMap<>();
		bomCompareChildMap.put(bomMasterId, bomComparesAll);
		List<BomMasterInv> bomMasterInvs = new ArrayList<>();
		for (BomCompare bomCompare : bomCompareList){
			List<BomCompare> bomCompares = downToFind(bomCompareList, bomCompare.getIAutoId());
			bomCompareChildMap.put(bomCompare.getIAutoId(), bomCompares);
		}
		for (Long id :bomCompareChildMap.keySet()){
			if (CollUtil.isEmpty(bomCompareChildMap.get(id))){
				continue;
			}
			// 说明是产成品id
			if (bomMasterId.equals(id)){
				for (BomCompare bomCompareChild : bomCompareChildMap.get(id)){
					BomMasterInv bomMasterInv = createBomMasterInv(orgId, bomMasterId, bomMasterInvId, bomCompareChild.getIAutoId(), bomCompareChild.getIInventoryId());
					bomMasterInvs.add(bomMasterInv);
				}
				continue;
			}
			BomCompare bomCompare = bomCompareMap.get(id);
			for (BomCompare bomCompareChild : bomCompareChildMap.get(id)){
				BomMasterInv bomMasterInv = createBomMasterInv(orgId, id, bomCompare.getIInventoryId(), bomCompareChild.getIAutoId(), bomCompareChild.getIInventoryId());
				bomMasterInvs.add(bomMasterInv);
			}
		}
		return bomMasterInvs;
	}
	
	
	public List<BomCompare> downToFind(List<BomCompare> bomCompareList, Long id){
		List<BomCompare> childes = new ArrayList<>();
		for (BomCompare bomCompare : bomCompareList){
			Long pid = bomCompare.getIPid();
			if (pid.equals(id)){
				childes.add(bomCompare);
				childes.addAll(downToFind(bomCompareList, bomCompare.getIAutoId()));
			}
		}
		return childes;
	}
	
	public int deleteByPIds(List<Long>  ids){
		int delCount = 0;
		for (Long id : ids){
			int delete = delete("delete Bd_BomMasterInv WHERE iBomMasterId = ?", id);
			if (delete>0){
				delCount+=1;
			}
		}
		return delCount;
	}

	public void deleteByBomMasterId  (Long bomMasterId){
		// 查询当前母件下所有子件。
		List<BomCompare> bomCompareList = bomCompareService.findByBomMasterIdList(bomMasterId);
		List<Long> bomCompareIds = new ArrayList<>();
		// 添加成品的母件Id
		bomCompareIds.add(bomMasterId);
		for (BomCompare bomCompare : bomCompareList){
			bomCompareIds.add(bomCompare.getIAutoId());
		}
		deleteByPIds(bomCompareIds);
	}
	
	public int[] saveBomMasterInv(Long bomMasterId, Long inventoryId){
		// 查询当前母件下所有子件。
		List<BomCompare> bomCompareList = bomCompareService.findByBomMasterIdList(bomMasterId);
		List<BomMasterInv> bomMasterInvs = getBomMasterInv(getOrgId(), bomMasterId, inventoryId, bomCompareList);
		return batchSave(bomMasterInvs);
	}
	
	public int[] saveBomMasterInv(Long bomMasterId, Long inventoryId, List<BomCompare> bomCompareList){
		List<BomMasterInv> bomMasterInvs = getBomMasterInv(getOrgId(), bomMasterId, inventoryId, bomCompareList);
		return batchSave(bomMasterInvs);
	}
}
