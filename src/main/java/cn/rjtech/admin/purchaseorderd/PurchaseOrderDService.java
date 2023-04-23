package cn.rjtech.admin.purchaseorderd;

import cn.jbolt.core.kit.JBoltSnowflakeKit;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.PurchaseOrderD;

import java.util.Date;

/**
 * 采购/委外订单-采购订单明细
 * @ClassName: PurchaseOrderDService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-13 14:33
 */
public class PurchaseOrderDService extends BaseService<PurchaseOrderD> {
	private final PurchaseOrderD dao=new PurchaseOrderD().dao();
	@Override
	protected PurchaseOrderD dao() {
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
     * @param isPresent 是否赠品： 0. 否 1. 是
     * @param isDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<PurchaseOrderD> getAdminDatas(int pageNumber, int pageSize, Boolean isPresent, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isPresent",isPresent);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param purchaseOrderD
	 * @return
	 */
	public Ret save(PurchaseOrderD purchaseOrderD) {
		if(purchaseOrderD==null || isOk(purchaseOrderD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(purchaseOrderD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=purchaseOrderD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(purchaseOrderD.getIAutoId(), JBoltUserKit.getUserId(), purchaseOrderD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param purchaseOrderD
	 * @return
	 */
	public Ret update(PurchaseOrderD purchaseOrderD) {
		if(purchaseOrderD==null || notOk(purchaseOrderD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		PurchaseOrderD dbPurchaseOrderD=findById(purchaseOrderD.getIAutoId());
		if(dbPurchaseOrderD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(purchaseOrderD.getName(), purchaseOrderD.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=purchaseOrderD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(purchaseOrderD.getIAutoId(), JBoltUserKit.getUserId(), purchaseOrderD.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param purchaseOrderD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(PurchaseOrderD purchaseOrderD, Kv kv) {
		//addDeleteSystemLog(purchaseOrderD.getIAutoId(), JBoltUserKit.getUserId(),purchaseOrderD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param purchaseOrderD model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(PurchaseOrderD purchaseOrderD, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(PurchaseOrderD purchaseOrderD, String column, Kv kv) {
		//addUpdateSystemLog(purchaseOrderD.getIAutoId(), JBoltUserKit.getUserId(), purchaseOrderD.getName(),"的字段["+column+"]值:"+purchaseOrderD.get(column));
		/**
		switch(column){
		    case "isPresent":
		        break;
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}
	
	public PurchaseOrderD createPurchaseOrderD(Long purchaseOrderMid, JSONObject jsonObject){
        PurchaseOrderD purchaseOrderD = new PurchaseOrderD();
        purchaseOrderD.setIAutoId(JBoltSnowflakeKit.me.nextId());
        purchaseOrderD.setIPurchaseOrderMid(purchaseOrderMid);
        purchaseOrderD.setIVendorAddrId(jsonObject.getLong(purchaseOrderD.IVENDORADDRID.toLowerCase()));
        purchaseOrderD.setCAddress(jsonObject.getString(purchaseOrderD.CADDRESS.toLowerCase()));
        purchaseOrderD.setCMemo(jsonObject.getString(purchaseOrderD.CMEMO.toLowerCase()));
        purchaseOrderD.setIsPresent(jsonObject.getBoolean(purchaseOrderD.ISPRESENT.toLowerCase()));
        purchaseOrderD.setIsDeleted(false);
        purchaseOrderD.setISourceInventoryId(jsonObject.getLong(purchaseOrderD.ISOURCEINVENTORYID.toLowerCase()));
        purchaseOrderD.setIInventoryId(jsonObject.getLong(purchaseOrderD.IINVENTORYID.toLowerCase()));
        purchaseOrderD.setISum(jsonObject.getBigDecimal(purchaseOrderD.ISUM.toLowerCase()));
        purchaseOrderD.setISourceSum(jsonObject.getBigDecimal(purchaseOrderD.ISOURCESUM.toLowerCase()));
        return purchaseOrderD;
	}

}
