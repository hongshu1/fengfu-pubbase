package cn.rjtech.admin.purchaseorderdqty;

import cn.hutool.core.collection.CollectionUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.PurchaseOrderD;
import cn.rjtech.model.momdata.PurchaseorderdQty;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 采购/委外订单-采购订单明细数量
 * @ClassName: PurchaseorderdQtyService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-13 14:53
 */
public class PurchaseorderdQtyService extends BaseService<PurchaseorderdQty> {
	private final PurchaseorderdQty dao=new PurchaseorderdQty().dao();
	@Override
	protected PurchaseorderdQty dao() {
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
	public Page<PurchaseorderdQty> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param purchaseorderdQty
	 * @return
	 */
	public Ret save(PurchaseorderdQty purchaseorderdQty) {
		if(purchaseorderdQty==null || isOk(purchaseorderdQty.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(purchaseorderdQty.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=purchaseorderdQty.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(purchaseorderdQty.getIAutoId(), JBoltUserKit.getUserId(), purchaseorderdQty.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param purchaseorderdQty
	 * @return
	 */
	public Ret update(PurchaseorderdQty purchaseorderdQty) {
		if(purchaseorderdQty==null || notOk(purchaseorderdQty.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		PurchaseorderdQty dbPurchaseorderdQty=findById(purchaseorderdQty.getIAutoId());
		if(dbPurchaseorderdQty==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(purchaseorderdQty.getName(), purchaseorderdQty.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=purchaseorderdQty.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(purchaseorderdQty.getIAutoId(), JBoltUserKit.getUserId(), purchaseorderdQty.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param purchaseorderdQty 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(PurchaseorderdQty purchaseorderdQty, Kv kv) {
		//addDeleteSystemLog(purchaseorderdQty.getIAutoId(), JBoltUserKit.getUserId(),purchaseorderdQty.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param purchaseorderdQty model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(PurchaseorderdQty purchaseorderdQty, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}
	
	public PurchaseorderdQty createPurchaseorderdQty(int year, int month, int day, BigDecimal qty){
		PurchaseorderdQty purchaseorderdQty = new PurchaseorderdQty();
		purchaseorderdQty.setIAutoId(JBoltSnowflakeKit.me.nextId());
		purchaseorderdQty.setIYear(year);
		purchaseorderdQty.setIMonth(month);
		purchaseorderdQty.setIDate(day);
		purchaseorderdQty.setIQty(qty);
		return purchaseorderdQty;
	}
	
	public PurchaseorderdQty create(Long purchaseOrderDId, int year, int month, int day, BigDecimal qty){
		PurchaseorderdQty purchaseorderdQty = createPurchaseorderdQty(year, month, day, qty);
		purchaseorderdQty.setIPurchaseOrderDid(purchaseOrderDId);
		return purchaseorderdQty;
	}
	
	public List<PurchaseorderdQty> getPurchaseorderdQty(Long purchaseOrderDId, JSONArray purchaseorderdQtyJsonArray){
		List<PurchaseorderdQty> list = new ArrayList<>();
		if (CollectionUtil.isEmpty(purchaseorderdQtyJsonArray)){
		
		}
		for (int i=0; i<purchaseorderdQtyJsonArray.size(); i++){
			JSONObject jsonObject = purchaseorderdQtyJsonArray.getJSONObject(i);
			PurchaseorderdQty purchaseOrderdQty = create(
					purchaseOrderDId,
					jsonObject.getIntValue(PurchaseorderdQty.IYEAR.toLowerCase()),
					jsonObject.getIntValue(PurchaseorderdQty.IMONTH.toLowerCase()),
					jsonObject.getIntValue(PurchaseorderdQty.IDATE.toLowerCase()),
					jsonObject.getBigDecimal(PurchaseorderdQty.IQTY.toLowerCase())
					);
			list.add(purchaseOrderdQty);
		}
		return list;
	}
	
	public List<Record> findByPurchaseOrderMId(Long purchaseOrderMId){
		return dbTemplate("purchaseorderdqty.findByPurchaseOrderMid", Okv.by(PurchaseOrderD.IPURCHASEORDERMID, purchaseOrderMId)).find();
	}
	
	public List<PurchaseorderdQty> findByPurchaseOrderDId(Long purchaseOrderDId){
		return find("SELECT * FROM PS_PurchaseOrderD_Qty WHERE iPurchaseOrderDid = ?", purchaseOrderDId);
	}
}
