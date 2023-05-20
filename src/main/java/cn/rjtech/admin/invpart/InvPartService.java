package cn.rjtech.admin.invpart;

import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.InvPart;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 存货物料表
 * @ClassName: InvPartService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-15 11:10
 */
public class InvPartService extends BaseService<InvPart> {
	private final InvPart dao=new InvPart().dao();
	@Override
	protected InvPart dao() {
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
     * @param iType 物料类型;1. 存货 2. 虚拟件
	 * @return
	 */
	public Page<InvPart> getAdminDatas(int pageNumber, int pageSize, String keywords, Integer iType) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("iType",iType);
        //关键词模糊查询
        sql.like("cPartName",keywords);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param invPart
	 * @return
	 */
	public Ret save(InvPart invPart) {
		if(invPart==null || isOk(invPart.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(invPart.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=invPart.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(invPart.getIAutoId(), JBoltUserKit.getUserId(), invPart.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param invPart
	 * @return
	 */
	public Ret update(InvPart invPart) {
		if(invPart==null || notOk(invPart.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InvPart dbInvPart=findById(invPart.getIAutoId());
		if(dbInvPart==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(invPart.getName(), invPart.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=invPart.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(invPart.getIAutoId(), JBoltUserKit.getUserId(), invPart.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param invPart 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InvPart invPart, Kv kv) {
		//addDeleteSystemLog(invPart.getIAutoId(), JBoltUserKit.getUserId(),invPart.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param invPart model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(InvPart invPart, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}
	
	public InvPart createInvPart(Long routingId, Long orgId, Long inventoryId, Long parentInvId, Long pid, Long inventoryRoutingConfigId, Integer type, Integer seq,String partCode, String partName){
		InvPart invPart = new InvPart();
		invPart.setIAutoId(JBoltSnowflakeKit.me.nextId());
		invPart.setIOrgId(orgId);
		invPart.setIInventoryId(inventoryId);
		invPart.setIPid(pid);
		invPart.setIParentInvId(parentInvId);
		invPart.setIType(type);
		invPart.setCPartCode(partCode);
		invPart.setCPartName(partName);
		invPart.setIInventoryRoutingConfigId(inventoryRoutingConfigId);
		invPart.setIInventoryRoutingId(routingId);
		invPart.setIsEffective(false);
		invPart.setISeq(seq);
		return invPart;
	}
	
	public InvPart createInvPart(Integer type, String partName){
		InvPart invPart = new InvPart();
		invPart.setIAutoId(JBoltSnowflakeKit.me.nextId());
		invPart.setIType(type);
		invPart.setCPartName(partName);
		invPart.setIsEffective(false);
		return invPart;
	}
	
	public void setInvPartList(Long routingId, Long orgId, Long parentInvId, Long pid, Long inventoryRoutingConfigId, Integer seq, List<InvPart> invPartList){
		for (InvPart invPart : invPartList){
			setInvPart(routingId, orgId, parentInvId, pid, inventoryRoutingConfigId, seq, invPart);
		}
	}
	
	public void setInvPart(Long routingId, Long orgId, Long parentInvId, Long pid, Long inventoryRoutingConfigId, Integer seq, InvPart invPart){
		invPart.setIOrgId(orgId);
		invPart.setIPid(pid);
		invPart.setIParentInvId(parentInvId);
		invPart.setIInventoryRoutingConfigId(inventoryRoutingConfigId);
		invPart.setISeq(seq);
		invPart.setIInventoryRoutingId(routingId);
	}
	
	public int deleteByRoutingConfigIds(Object[] routingConfigIds){
		return delete("DELETE Bd_InvPart WHERE iInventoryRoutingConfigId in ("+ ArrayUtil.join(routingConfigIds, COMMA) +")");
	}
	
	public void update(Long routingId, Long parentInvId, int effective){
		update("UPDATE Bd_InvPart SET isEffective = ? WHERE iInventoryRoutingId <> ? AND iParentInvId = ?", effective, routingId, parentInvId);
	}
	
}
