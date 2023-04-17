package cn.rjtech.admin.purchaseorderm;

import cn.hutool.core.collection.CollectionUtil;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.CreateByEnum;
import cn.rjtech.enums.OrderStatusTypeEnum;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.PurchaseOrderM;
import com.jfinal.plugin.activerecord.Record;

import java.util.*;

/**
 * 采购/委外订单-采购订单主表
 * @ClassName: PurchaseOrderMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-12 15:19
 */
public class PurchaseOrderMService extends BaseService<PurchaseOrderM> {
	private final PurchaseOrderM dao=new PurchaseOrderM().dao();
	@Override
	protected PurchaseOrderM dao() {
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
     * @param cOrderNo 订单编号
     * @param iBusType 业务类型： 1. 普通采购
     * @param iPurchaseType 采购类型
     * @param iVendorId 供应商ID
     * @param iDepartmentId 部门ID
     * @param iPayableType 应付类型：1. 材料费
     * @param iOrderStatus 订单状态：1. 已保存 2. 待审批 3. 已审核 4. 审批不通过 5. 已生成现品票 6. 已关闭
     * @param iAuditStatus 审核状态：1. 审核中 2. 审核通过 3. 审核不通过
	 * @return
	 */
	public Page<PurchaseOrderM> getAdminDatas(int pageNumber, int pageSize, String cOrderNo, Integer iBusType, Integer iPurchaseType, Long iVendorId, Long iDepartmentId, Integer iPayableType, Integer iOrderStatus, Integer iAuditStatus) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("cOrderNo",cOrderNo);
        sql.eq("iBusType",iBusType);
        sql.eq("iPurchaseType",iPurchaseType);
        sql.eq("iVendorId",iVendorId);
        sql.eq("iDepartmentId",iDepartmentId);
        sql.eq("iPayableType",iPayableType);
        sql.eq("iOrderStatus",iOrderStatus);
        sql.eq("iAuditStatus",iAuditStatus);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param purchaseOrderM
	 * @return
	 */
	public Ret save(PurchaseOrderM purchaseOrderM) {
		if(purchaseOrderM==null || isOk(purchaseOrderM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		User user = JBoltUserKit.getUser();
		Date now = new Date();
		purchaseOrderM.setIOrderStatus(OrderStatusTypeEnum.ORDER_STATUS_SAVE.getValue());
		purchaseOrderM.setIAuditStatus(AuditStatusEnum.AWAIT_AUDIT.getValue());
		purchaseOrderM.setICreateBy(user.getId());
		purchaseOrderM.setCCreateName(user.getName());
		purchaseOrderM.setDCreateTime(now);
		purchaseOrderM.setIUpdateBy(user.getId());
		purchaseOrderM.setCUpdateName(user.getName());
		purchaseOrderM.setDUpdateTime(now);
		purchaseOrderM.setIsDeleted(false);
		//if(existsName(purchaseOrderM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=purchaseOrderM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(purchaseOrderM.getIAutoId(), JBoltUserKit.getUserId(), purchaseOrderM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param purchaseOrderM
	 * @return
	 */
	public Ret update(PurchaseOrderM purchaseOrderM) {
		if(purchaseOrderM==null || notOk(purchaseOrderM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		PurchaseOrderM dbPurchaseOrderM=findById(purchaseOrderM.getIAutoId());
		if(dbPurchaseOrderM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(purchaseOrderM.getName(), purchaseOrderM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=purchaseOrderM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(purchaseOrderM.getIAutoId(), JBoltUserKit.getUserId(), purchaseOrderM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param purchaseOrderM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(PurchaseOrderM purchaseOrderM, Kv kv) {
		//addDeleteSystemLog(purchaseOrderM.getIAutoId(), JBoltUserKit.getUserId(),purchaseOrderM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param purchaseOrderM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(PurchaseOrderM purchaseOrderM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	public List<Record> findByInventoryId(Long inventoryId){
		ValidationUtils.notNull(inventoryId,JBoltMsg.DATA_NOT_EXIST);
		return dbTemplate("inventory.findByInventoryId", Okv.by("inventoryId",inventoryId)).find();

	}
}