package cn.rjtech.admin.purchaseorderdbatch;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.PurchaseOrderDBatch;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 采购/委外管理-采购现品票
 * @ClassName: PurchaseOrderDBatchService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-24 11:05
 */
public class PurchaseOrderDBatchService extends BaseService<PurchaseOrderDBatch> {
	private final PurchaseOrderDBatch dao=new PurchaseOrderDBatch().dao();
	@Override
	protected PurchaseOrderDBatch dao() {
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
     * @param isEffective 是否生效：0. 否  1. 是
	 * @return
	 */
	public Page<PurchaseOrderDBatch> getAdminDatas(int pageNumber, int pageSize, Boolean isEffective) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isEffective",isEffective);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param purchaseOrderDBatch
	 * @return
	 */
	public Ret save(PurchaseOrderDBatch purchaseOrderDBatch) {
		if(purchaseOrderDBatch==null || isOk(purchaseOrderDBatch.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(purchaseOrderDBatch.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=purchaseOrderDBatch.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(purchaseOrderDBatch.getIAutoId(), JBoltUserKit.getUserId(), purchaseOrderDBatch.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param purchaseOrderDBatch
	 * @return
	 */
	public Ret update(PurchaseOrderDBatch purchaseOrderDBatch) {
		if(purchaseOrderDBatch==null || notOk(purchaseOrderDBatch.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		PurchaseOrderDBatch dbPurchaseOrderDBatch=findById(purchaseOrderDBatch.getIAutoId());
		if(dbPurchaseOrderDBatch==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(purchaseOrderDBatch.getName(), purchaseOrderDBatch.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=purchaseOrderDBatch.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(purchaseOrderDBatch.getIAutoId(), JBoltUserKit.getUserId(), purchaseOrderDBatch.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param purchaseOrderDBatch 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(PurchaseOrderDBatch purchaseOrderDBatch, Kv kv) {
		//addDeleteSystemLog(purchaseOrderDBatch.getIAutoId(), JBoltUserKit.getUserId(),purchaseOrderDBatch.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param purchaseOrderDBatch model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(PurchaseOrderDBatch purchaseOrderDBatch, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(PurchaseOrderDBatch purchaseOrderDBatch, String column, Kv kv) {
		//addUpdateSystemLog(purchaseOrderDBatch.getIAutoId(), JBoltUserKit.getUserId(), purchaseOrderDBatch.getName(),"的字段["+column+"]值:"+purchaseOrderDBatch.get(column));
		/**
		switch(column){
		    case "isEffective":
		        break;
		}
		*/
		return null;
	}
	
	public PurchaseOrderDBatch getPurchaseOrderDBatch(){
		PurchaseOrderDBatch purchaseOrderDBatch = new PurchaseOrderDBatch();
		purchaseOrderDBatch.setIAutoId(JBoltSnowflakeKit.me.nextId());
		// 版本默认按 00 开始
		purchaseOrderDBatch.setCVersion("00");
		purchaseOrderDBatch.setIsEffective(true);
		return purchaseOrderDBatch;
	}
}
