package cn.rjtech.admin.purchaseorderref;

import cn.hutool.core.collection.CollectionUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.rjtech.model.momdata.PurchaseOrderRef;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import com.jfinal.plugin.activerecord.Record;

import java.util.*;


/**
 * 采购/委外-采购订单与到货计划关联
 * @ClassName: PurchaseOrderRefService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-23 11:18
 */
public class PurchaseOrderRefService extends BaseService<PurchaseOrderRef> {
	private final PurchaseOrderRef dao=new PurchaseOrderRef().dao();
	@Override
	protected PurchaseOrderRef dao() {
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
	public Page<PurchaseOrderRef> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param purchaseOrderRef
	 * @return
	 */
	public Ret save(PurchaseOrderRef purchaseOrderRef) {
		if(purchaseOrderRef==null || isOk(purchaseOrderRef.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(purchaseOrderRef.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=purchaseOrderRef.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(purchaseOrderRef.getIAutoId(), JBoltUserKit.getUserId(), purchaseOrderRef.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param purchaseOrderRef
	 * @return
	 */
	public Ret update(PurchaseOrderRef purchaseOrderRef) {
		if(purchaseOrderRef==null || notOk(purchaseOrderRef.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		PurchaseOrderRef dbPurchaseOrderRef=findById(purchaseOrderRef.getIAutoId());
		if(dbPurchaseOrderRef==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(purchaseOrderRef.getName(), purchaseOrderRef.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=purchaseOrderRef.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(purchaseOrderRef.getIAutoId(), JBoltUserKit.getUserId(), purchaseOrderRef.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param purchaseOrderRef 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(PurchaseOrderRef purchaseOrderRef, Kv kv) {
		//addDeleteSystemLog(purchaseOrderRef.getIAutoId(), JBoltUserKit.getUserId(),purchaseOrderRef.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param purchaseOrderRef model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(PurchaseOrderRef purchaseOrderRef, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}
	
	public List<PurchaseOrderRef> getPurchaseOrderRefList(Long purchaseOrderDId, JSONArray orderRefJsonArray){
		List<PurchaseOrderRef> purchaseOrderRefList = new ArrayList<>();
		if (CollectionUtil.isEmpty(orderRefJsonArray)){
			return purchaseOrderRefList;
		}
		for (int i=0; i<orderRefJsonArray.size(); i++){
			JSONObject jsonObject = orderRefJsonArray.getJSONObject(i);
			PurchaseOrderRef purchaseOrderRef = createPurchaseOrderRef(purchaseOrderDId, jsonObject.getLong(PurchaseOrderRef.IDEMANDPLANDID.toLowerCase()));
			purchaseOrderRefList.add(purchaseOrderRef);
		}
		return purchaseOrderRefList;
	}
	
	public PurchaseOrderRef createPurchaseOrderRef(Long purchaseOrderDid, Long demandPlanDid){
		PurchaseOrderRef purchaseOrderRef = new PurchaseOrderRef();
		purchaseOrderRef.setIAutoId(JBoltSnowflakeKit.me.nextId());
		purchaseOrderRef.setIPurchaseOrderDid(purchaseOrderDid);
		purchaseOrderRef.setIDemandPlanDid(demandPlanDid);
		return purchaseOrderRef;
	}
	
	public List<Record> findByPurchaseOrderDIds(List<Long> purchaseOrderDIds){
		return dbTemplate("purchaseorderref.findByPurchaseOrderDIds", Okv.by("ids", purchaseOrderDIds)).find();
	}
	
	public void removeByPurchaseOrderDId(Long purchaseOrderId){
	    delete("DELETE PS_PurchaseOrderRef WHERE iPurchaseOrderDid = ?", purchaseOrderId);
	}
	
	public int removeByPurchaseOrderDIds(List<Long> purchaseOrderDIds){
		return dbTemplate("purchaseorderref.removeByPurchaseOrderDIds", Okv.by("ids", purchaseOrderDIds)).delete();
	}
	
	public List<Record> findByPurchaseOderMId(Long purchaseOrderMId){
		return dbTemplate("purchaseorderref.findByPurchaseOderMId", Okv.by("purchaseOrderMId", purchaseOrderMId)).find();
	}
	
	public int removeByPurchaseOderMId(Long purchaseOrderMId){
	   return dbTemplate("purchaseorderref.removeByPurchaseOderMId", Okv.by("purchaseOrderMId", purchaseOrderMId)).delete();
    }
}
