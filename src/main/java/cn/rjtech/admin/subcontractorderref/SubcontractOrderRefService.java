package cn.rjtech.admin.subcontractorderref;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.SubcontractOrderD;
import cn.rjtech.model.momdata.SubcontractOrderRef;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * 采购/委外-委外订单与到货计划关联
 * @ClassName: SubcontractOrderRefService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-25 21:33
 */
public class SubcontractOrderRefService extends BaseService<SubcontractOrderRef> {
	private final SubcontractOrderRef dao=new SubcontractOrderRef().dao();
	@Override
	protected SubcontractOrderRef dao() {
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
	public Page<SubcontractOrderRef> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param subcontractOrderRef
	 * @return
	 */
	public Ret save(SubcontractOrderRef subcontractOrderRef) {
		if(subcontractOrderRef==null || isOk(subcontractOrderRef.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(subcontractOrderRef.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractOrderRef.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(subcontractOrderRef.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderRef.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param subcontractOrderRef
	 * @return
	 */
	public Ret update(SubcontractOrderRef subcontractOrderRef) {
		if(subcontractOrderRef==null || notOk(subcontractOrderRef.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SubcontractOrderRef dbSubcontractOrderRef=findById(subcontractOrderRef.getIAutoId());
		if(dbSubcontractOrderRef==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(subcontractOrderRef.getName(), subcontractOrderRef.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractOrderRef.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(subcontractOrderRef.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderRef.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param subcontractOrderRef 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SubcontractOrderRef subcontractOrderRef, Kv kv) {
		//addDeleteSystemLog(subcontractOrderRef.getIAutoId(), JBoltUserKit.getUserId(),subcontractOrderRef.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param subcontractOrderRef model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SubcontractOrderRef subcontractOrderRef, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}
	
	public List<SubcontractOrderRef> getSubcontractOrderRefList(Long iSubContractOrderDid, JSONArray orderRefJsonArray){
		List<SubcontractOrderRef> subcontractOrderRefList = new ArrayList<>();
		if (CollUtil.isEmpty(orderRefJsonArray)){
			return subcontractOrderRefList;
		}
		for (int i=0; i<orderRefJsonArray.size(); i++){
			JSONObject jsonObject = orderRefJsonArray.getJSONObject(i);
			SubcontractOrderRef subcontractOrderRef = createSubcontractOrderRef(iSubContractOrderDid, jsonObject.getLong(SubcontractOrderRef.IDEMANDPLANDID.toLowerCase()));
			subcontractOrderRefList.add(subcontractOrderRef);
		}
		return subcontractOrderRefList;
	}
	
	public SubcontractOrderRef createSubcontractOrderRef(Long iSubContractOrderDid, Long demandPlanDid){
		SubcontractOrderRef subcontractOrderRef = new SubcontractOrderRef();
		subcontractOrderRef.setIAutoId(JBoltSnowflakeKit.me.nextId());
		subcontractOrderRef.setISubContractOrderDid(iSubContractOrderDid);
		subcontractOrderRef.setIDemandPlanDid(demandPlanDid);
		return subcontractOrderRef;
	}
	
	public List<Record> findBySubContractOrderDIds(List<Long> iSubContractOrderDids){
		return dbTemplate("subcontractorderref.findBySubContractOrderDIds", Okv.by("ids", iSubContractOrderDids)).find();
	}
	
	public void removeByPurchaseOrderDId(Long iSubContractOrderDid){
		delete("DELETE PS_SubcontractOrderRef WHERE iSubContractOrderDid = ?", iSubContractOrderDid);
	}
	
	public int removeBySubContractOrderDIds(List<Long> iSubContractOrderDids){
		return dbTemplate("subcontractorderref.removeBySubContractOrderDIds", Okv.by("ids", iSubContractOrderDids)).delete();
	}
	
	public List<Record> findBySubContractOrderMId(Long iSubContractOrderMid){
		return dbTemplate("subcontractorderref.findBySubContractOrderMId", Okv.by(SubcontractOrderD.ISUBCONTRACTORDERMID, iSubContractOrderMid)).find();
	}
	
	public int removeByPurchaseOderMId(Long iSubContractOrderMid){
		return dbTemplate("subcontractorderref.removeBySubContractOrderMId", Okv.by(SubcontractOrderD.ISUBCONTRACTORDERMID, iSubContractOrderMid)).delete();
	}
}
