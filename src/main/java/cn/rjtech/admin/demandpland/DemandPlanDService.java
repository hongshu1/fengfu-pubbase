package cn.rjtech.admin.demandpland;

import cn.hutool.core.collection.CollectionUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.DemandPlanD;
import cn.rjtech.model.momdata.DemandPlanM;
import cn.rjtech.model.momdata.PurchaseOrderRef;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 需求计划管理-到货计划细表
 * @ClassName: DemandPlanDService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-19 16:15
 */
public class DemandPlanDService extends BaseService<DemandPlanD> {
	private final DemandPlanD dao=new DemandPlanD().dao();
	@Override
	protected DemandPlanD dao() {
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
	public Page<DemandPlanD> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param demandPlanD
	 * @return
	 */
	public Ret save(DemandPlanD demandPlanD) {
		if(demandPlanD==null || isOk(demandPlanD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(demandPlanD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=demandPlanD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(demandPlanD.getIAutoId(), JBoltUserKit.getUserId(), demandPlanD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param demandPlanD
	 * @return
	 */
	public Ret update(DemandPlanD demandPlanD) {
		if(demandPlanD==null || notOk(demandPlanD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		DemandPlanD dbDemandPlanD=findById(demandPlanD.getIAutoId());
		if(dbDemandPlanD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(demandPlanD.getName(), demandPlanD.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=demandPlanD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(demandPlanD.getIAutoId(), JBoltUserKit.getUserId(), demandPlanD.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param demandPlanD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(DemandPlanD demandPlanD, Kv kv) {
		//addDeleteSystemLog(demandPlanD.getIAutoId(), JBoltUserKit.getUserId(),demandPlanD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param demandPlanD model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(DemandPlanD demandPlanD, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}
	
	public List<Record> findByDemandPlanMList(String beginDate, String endDate, String iVendorId, Integer processType){
		Okv okv = Okv.by("beginDate", beginDate).set("endDate", endDate).set("iVendorId", iVendorId).set("processType", processType);
		okv.set("orgId", getOrgId());
		return dbTemplate("demandpland.findByDemandPlanMList", okv).find();
	}
	
	/**
	 *
	 * @return 获取存货中每个日期下的数量
	 */
	public Map<Long, Map<String, BigDecimal>> getDemandPlanDMap(List<Record> demandPlanDList){
		// key:存货id 年月日 value: (key:, 数量)
		Map<Long, Map<String, BigDecimal>> demandPlanDMap = new HashMap<>();
		// 遍历所有日期，到货计划主表 ： List<年月日 : qty>
		for (Record record : demandPlanDList){
			// 按存货安全
			Long invId = record.getLong(DemandPlanM.IINVENTORYID);
			String key = getKey(record);
			// 记录当前主表下的每一个日期数量
			Map<String, BigDecimal> map = demandPlanDMap.containsKey(invId) ? demandPlanDMap.get(invId) : new HashMap<>();
			BigDecimal qty = map.containsKey(key) ? map.get(key) : BigDecimal.ZERO;
			map.put(key, qty.add(record.getBigDecimal(DemandPlanD.IQTY)));
			demandPlanDMap.put(invId, map);
		}
		return demandPlanDMap;
	}
	
	/**
	 * 记录每一个存货 中存在多个到货计划细表id
	 * @param demandPlanDList
	 * @return
	 */
	public Map<Long, List<PurchaseOrderRef>> getPuOrderRefByInvId(List<Record> demandPlanDList){
		Map<Long, List<PurchaseOrderRef>> map = new HashMap<>();
		// 遍历所有日期，到货计划主表 ： List<年月日 : qty>
		for (Record record : demandPlanDList){
			PurchaseOrderRef ref = new PurchaseOrderRef();
			ref.setIDemandPlanDid(record.getLong(DemandPlanD.IAUTOID));
			Long invId = record.getLong(DemandPlanM.IINVENTORYID);
			List<PurchaseOrderRef> list= map.containsKey(invId) ? map.get(invId) : new ArrayList<>();
			list.add(ref);
			map.put(invId, list);
		}
		
		return map;
	}
	
	
	private String getKey(Record record){
		String yearStr = record.getStr(DemandPlanD.IYEAR);
		String monthStr = "";
		if (record.getInt(DemandPlanD.IMONTH) <10){
			monthStr = monthStr.concat("0");
		}
		monthStr = monthStr.concat(record.getStr(DemandPlanD.IMONTH));
		String dateStr = "";
		if (record.getInt(DemandPlanD.IDATE) < 10){
			dateStr = dateStr.concat("0");
		}
		dateStr = dateStr.concat(record.getStr(DemandPlanD.IDATE));
		return yearStr.concat(monthStr).concat(dateStr);
	}
	
	public int updateGenTypeById(Long id, Integer genType, Integer status){
		return update("UPDATE Mrp_DemandPlanD SET iGenType = ?, iStatus = ? where iAutoId = ?", genType, status, id);
	}
	
	public void batchUpdateGenTypeByIds(List<Long> ids, Integer genType, Integer status){
		if (CollectionUtil.isEmpty(ids)){
			return;
		}
		for (Long id : ids){
			updateGenTypeById(id, genType, status);
		}
	}
	
	public Integer queryNotGenOrderNum(Long demandPlanMid, List<Long> demandPlanDIds){
		return dbTemplate("demandpland.queryNotGenOrderNum", Okv.by(DemandPlanD.IDEMANDPLANMID, demandPlanMid).set("ids", demandPlanDIds)).queryInt();
	}
	
	public List<Record> findAll(Okv okv){
		return dbTemplate("demandpland.findAll", okv).find();
	}
	
	public List<Record> findByDemandPlanMid(Long demandPlanMid){
		return dbTemplate("demandpland.findAll", Okv.by(DemandPlanD.IDEMANDPLANMID, demandPlanMid)).find();
	}
	
}
