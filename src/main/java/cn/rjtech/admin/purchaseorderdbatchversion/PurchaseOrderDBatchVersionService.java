package cn.rjtech.admin.purchaseorderdbatchversion;

import cn.hutool.core.date.DateUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.PurchaseOrderDBatchVersion;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购/委外管理-采购现品票版本记录
 * @ClassName: PurchaseOrderDBatchVersionService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-24 11:05
 */
public class PurchaseOrderDBatchVersionService extends BaseService<PurchaseOrderDBatchVersion> {
	private final PurchaseOrderDBatchVersion dao=new PurchaseOrderDBatchVersion().dao();
	@Override
	protected PurchaseOrderDBatchVersion dao() {
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
	 * @return
	 */
	public Page<PurchaseOrderDBatchVersion> getAdminDatas(int pageNumber, int pageSize, String keywords) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //关键词模糊查询
        sql.like("cCreateName",keywords);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param purchaseOrderDBatchVersion
	 * @return
	 */
	public Ret save(PurchaseOrderDBatchVersion purchaseOrderDBatchVersion) {
		if(purchaseOrderDBatchVersion==null || isOk(purchaseOrderDBatchVersion.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(purchaseOrderDBatchVersion.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=purchaseOrderDBatchVersion.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(purchaseOrderDBatchVersion.getIAutoId(), JBoltUserKit.getUserId(), purchaseOrderDBatchVersion.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param purchaseOrderDBatchVersion
	 * @return
	 */
	public Ret update(PurchaseOrderDBatchVersion purchaseOrderDBatchVersion) {
		if(purchaseOrderDBatchVersion==null || notOk(purchaseOrderDBatchVersion.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		PurchaseOrderDBatchVersion dbPurchaseOrderDBatchVersion=findById(purchaseOrderDBatchVersion.getIAutoId());
		if(dbPurchaseOrderDBatchVersion==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(purchaseOrderDBatchVersion.getName(), purchaseOrderDBatchVersion.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=purchaseOrderDBatchVersion.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(purchaseOrderDBatchVersion.getIAutoId(), JBoltUserKit.getUserId(), purchaseOrderDBatchVersion.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param purchaseOrderDBatchVersion 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(PurchaseOrderDBatchVersion purchaseOrderDBatchVersion, Kv kv) {
		//addDeleteSystemLog(purchaseOrderDBatchVersion.getIAutoId(), JBoltUserKit.getUserId(),purchaseOrderDBatchVersion.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param purchaseOrderDBatchVersion model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(PurchaseOrderDBatchVersion purchaseOrderDBatchVersion, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}
	
	public PurchaseOrderDBatchVersion createBatchVersion(Long purchaseOrderMId, Long batchId, Long	inventoryId, Date planDate, String version, String sourceVersion, String barCode, String sourceBarCode, BigDecimal qty, BigDecimal sourceQty){
		PurchaseOrderDBatchVersion purchaseOrderDBatchVersion = new PurchaseOrderDBatchVersion();
		
		purchaseOrderDBatchVersion.setIPurchaseOrderMid(purchaseOrderMId);
		purchaseOrderDBatchVersion.setIPurchaseOrderdBatchId(batchId);
		purchaseOrderDBatchVersion.setIInventoryId(inventoryId);
		purchaseOrderDBatchVersion.setDPlanDate(planDate);
		purchaseOrderDBatchVersion.setCVersion(version);
		purchaseOrderDBatchVersion.setCSourceVersion(sourceVersion);
		purchaseOrderDBatchVersion.setCBarcode(barCode);
		purchaseOrderDBatchVersion.setCSourceBarcode(sourceBarCode);
		purchaseOrderDBatchVersion.setIQty(qty);
		purchaseOrderDBatchVersion.setISourceQty(sourceQty);
		purchaseOrderDBatchVersion.setICreateBy(JBoltUserKit.getUserId());
		purchaseOrderDBatchVersion.setCCreateName(JBoltUserKit.getUserName());
		purchaseOrderDBatchVersion.setDCreateTime(DateUtil.date());
		return purchaseOrderDBatchVersion;
	}
}
