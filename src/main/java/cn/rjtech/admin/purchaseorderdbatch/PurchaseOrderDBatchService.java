package cn.rjtech.admin.purchaseorderdbatch;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.inventorychange.InventoryChangeService;
import cn.rjtech.admin.purchaseorderdbatchversion.PurchaseOrderDBatchVersionService;
import cn.rjtech.admin.purchaseorderdqty.PurchaseorderdQtyService;
import cn.rjtech.model.momdata.InventoryChange;
import cn.rjtech.model.momdata.PurchaseOrderDBatch;
import cn.rjtech.model.momdata.PurchaseOrderDBatchVersion;
import cn.rjtech.model.momdata.PurchaseorderdQty;
import cn.rjtech.service.func.mom.MomDataFuncService;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 采购/委外管理-采购现品票
 * @ClassName: PurchaseOrderDBatchService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-24 11:05
 */
public class PurchaseOrderDBatchService extends BaseService<PurchaseOrderDBatch> {
	private final PurchaseOrderDBatch dao=new PurchaseOrderDBatch().dao();
	
	@Inject
	private MomDataFuncService momDataFuncService;
	@Inject
	private PurchaseOrderDBatchVersionService purchaseOrderDBatchVersionService;
    @Inject
	private PurchaseorderdQtyService purchaseorderdQtyService;
    @Inject
    private InventoryChangeService inventoryChangeService;
	
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
	
//	public PurchaseOrderDBatch getPurchaseOrderDBatch(){
//		PurchaseOrderDBatch purchaseOrderDBatch = new PurchaseOrderDBatch();
//
//		purchaseOrderDBatch.setCVersion("00");
//		purchaseOrderDBatch.setIsEffective(true);
//		return purchaseOrderDBatch;
//	}
	
	public PurchaseOrderDBatch createPurchaseOrderDBatch(Long purchaseOrderDid, Long inventoryId, Date planDate, BigDecimal qty, String barcode){
		PurchaseOrderDBatch purchaseOrderDBatch = new PurchaseOrderDBatch();
		purchaseOrderDBatch.setIPurchaseOrderDid(purchaseOrderDid);
		purchaseOrderDBatch.setIinventoryId(inventoryId);
		purchaseOrderDBatch.setDPlanDate(planDate);
		purchaseOrderDBatch.setIQty(qty);
		purchaseOrderDBatch.setCVersion("00");
		purchaseOrderDBatch.setCBarcode(barcode);
		purchaseOrderDBatch.setIsEffective(true);
		return purchaseOrderDBatch;
	}
	
	public String generateBarCode(){
		String prefix = "XCP";
		String format = DateUtil.format(DateUtil.date(), DatePattern.PURE_DATE_FORMAT);
		return momDataFuncService.getNextNo(prefix.concat(format), 4);
	}
	
	public Page<Record> findByPurchaseOrderMId(int pageNumber, int pageSize, Kv kv){
		// 为true 说明是看所有的
		if (Boolean.valueOf(kv.getStr("isEffective"))){
			kv.remove("isEffective");
		}else{
			kv.set("isEffective", "1");
		}
		return dbTemplate("purchaseorderdbatch.findByPurchaseOrderMId", kv).paginate(pageNumber, pageSize);
	}
	
	public Ret updateOrderBatch(Long purchaseOrderMId , Long id, String cVersion, BigDecimal qty){
        PurchaseOrderDBatch orderDBatch = findById(id);
        ValidationUtils.notNull(orderDBatch, JBoltMsg.DATA_NOT_EXIST);
        ValidationUtils.notBlank(cVersion, "版本号未获取到");
        ValidationUtils.isTrue(qty!=null && qty.compareTo(BigDecimal.ZERO)>0, "版本号未获取到");
        ValidationUtils.isTrue(!cVersion.equals(orderDBatch.getCVersion()), "版本号不能一致");
        ValidationUtils.isTrue(qty.compareTo(orderDBatch.getIQty()) > 0, "现品票的数量不可大于包装数量，只可小于包装数量");
        // 新增一个现成票后，再生产一个版本记录表，及修改详情；
        String barCode = generateBarCode();
        PurchaseOrderDBatch newBatch = createPurchaseOrderDBatch(orderDBatch.getIPurchaseOrderDid(), orderDBatch.getIinventoryId(), orderDBatch.getDPlanDate(), qty, barCode);
        // 设置新版本号
        newBatch.setCVersion(cVersion);
        // 将旧的改为失效
        orderDBatch.setIsEffective(false);
		// 查存货
		InventoryChange inventoryChange = inventoryChangeService.findByBeforeInventoryId(orderDBatch.getIinventoryId());
		
		// 将计划类型拆分成 年月日
		String yearStr = DateUtil.format(orderDBatch.getDPlanDate(), "yyyy");
		String monthStr = DateUtil.format(orderDBatch.getDPlanDate(), "MM");
		String dateStr = DateUtil.format(orderDBatch.getDPlanDate(), "dd");
		List<PurchaseorderdQty> purchaseorderdQtyList = purchaseorderdQtyService.findByPurchaseOrderDId(orderDBatch.getIPurchaseOrderDid());
		
        tx(()->{
            // 新增
            newBatch.save();
            // 保存记录
            PurchaseOrderDBatchVersion batchVersion = purchaseOrderDBatchVersionService.createBatchVersion(purchaseOrderMId, id, orderDBatch.getIinventoryId(),
                    orderDBatch.getDPlanDate(), cVersion, orderDBatch.getCVersion(), barCode, orderDBatch.getCBarcode(), qty, orderDBatch.getIQty());
            batchVersion.save();
            // 修改
            orderDBatch.update();
            
			for (PurchaseorderdQty purchaseOrderdQty : purchaseorderdQtyList){
				Integer year = purchaseOrderdQty.getIYear();
				Integer month = purchaseOrderdQty.getIMonth();
				Integer date = purchaseOrderdQty.getIDate();
				if (yearStr.equals(String.valueOf(year)) && monthStr.equals(String.valueOf(month)) && dateStr.equals(String.valueOf(date))){
					// 总数量 - 更改的数量
					BigDecimal num = purchaseOrderdQty.getIQty().subtract(orderDBatch.getIQty().subtract(qty));
					purchaseOrderdQty.setISourceQty(num);
					// 默认乘以1
					BigDecimal rate = BigDecimal.ONE;
					if (ObjectUtil.isNotNull(inventoryChange)){
						rate = inventoryChange.getIChangeRate();
					}
					// 乘以转换率；
					purchaseOrderdQty.setIQty(num.multiply(rate));
					purchaseOrderdQty.update();
				}
			}
            return true;
        });
        
        return SUCCESS;
    }
}
