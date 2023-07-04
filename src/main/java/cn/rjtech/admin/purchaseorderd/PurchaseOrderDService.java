package cn.rjtech.admin.purchaseorderd;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.enums.BoolCharEnum;
import cn.rjtech.model.momdata.PurchaseOrderD;
import cn.rjtech.model.momdata.PurchaseOrderM;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


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
		Long iVendorAddrId = jsonObject.getLong(PurchaseOrderD.IVENDORADDRID.toLowerCase());
		String cAddress = jsonObject.getString(PurchaseOrderD.CADDRESS.toLowerCase());
		String cMemo = jsonObject.getString(PurchaseOrderD.CMEMO.toLowerCase());
		Boolean isPresent = jsonObject.getBoolean(PurchaseOrderD.ISPRESENT.toLowerCase());
		Long iInventoryId = jsonObject.getLong(PurchaseOrderD.IINVENTORYID.toLowerCase());
		String iPkgQty = jsonObject.getString(PurchaseOrderD.IPKGQTY.toLowerCase());
		return create(purchaseOrderMid, iVendorAddrId, iInventoryId, cAddress, cMemo, iPkgQty, isPresent);
	}
	
	public PurchaseOrderD create(Long purchaseOrderMid, Long iVendorAddrId, Long iInventoryId, String cAddress, String cMemo, String iPkgQty, boolean isPresent){
		PurchaseOrderD purchaseOrderD = new PurchaseOrderD();
		purchaseOrderD.setIAutoId(JBoltSnowflakeKit.me.nextId());
		purchaseOrderD.setIPurchaseOrderMid(purchaseOrderMid);
		purchaseOrderD.setIVendorAddrId(iVendorAddrId);
		purchaseOrderD.setIInventoryId(iInventoryId);
		purchaseOrderD.setIsDeleted(false);
		purchaseOrderD.setIsPresent(isPresent);
		purchaseOrderD.setCMemo(cMemo);
		purchaseOrderD.setCAddress(cAddress);
		Integer pkgQty = 0;
		if (StrUtil.isNotBlank(iPkgQty)){
			pkgQty = Integer.valueOf(iPkgQty);
		}
		purchaseOrderD.setIPkgQty(pkgQty);
		return purchaseOrderD;
	}
	
	public PurchaseOrderD create(Long id, Long purchaseOrderMid, Long iVendorAddrId, Long iInventoryId, String cAddress, String cMemo, String iPkgQty, boolean isPresent) {
		PurchaseOrderD purchaseOrderD = create(purchaseOrderMid, iVendorAddrId, iInventoryId, cAddress, cMemo, iPkgQty, isPresent);
		purchaseOrderD.setIAutoId(id);
		return purchaseOrderD;
	}
	
	public int removeByPurchaseOrderMId(Long purchaseOrderMId){
	    return update("update PS_PurchaseOrderD set isDeleted=1 where iPurchaseOrderMid = ?", purchaseOrderMId);
    }
	
	/**
	 * 获取采购细表数据
	 * @param purchaseOrderMId
	 * @return
	 */
	public List<Record> findByPurchaseOrderMId(Long purchaseOrderMId){
		return dbTemplate("purchaseorderd.findAll", Okv.by(PurchaseOrderD.IPURCHASEORDERMID, purchaseOrderMId)).find();
	}
	
	public void setPurchaseOrderDList(List<Record> purchaseOrderDList, Map<String, Record> dateMap, Map<Long, Map<String, BigDecimal>>  purchaseOrderdQtyMap, Map<String, BigDecimal> ymQtyMap){
		// 同一种的存货编码需要汇总在一起。
		// 将日期设值。
		for (Record record : purchaseOrderDList){
			// 存货id（原存货id）
			Long invId = record.getLong(PurchaseOrderD.IINVENTORYID);
			String[] arr = new String[dateMap.keySet().size()];
			record.set(PurchaseOrderM.ARR, arr);
			// 存货编码为key，可以获取存货编码下 所有日期范围的值
			if (!purchaseOrderdQtyMap.containsKey(invId)){
				continue;
			}
			// 当前日期下的数量
			Map<String, BigDecimal> dateQtyMap = purchaseOrderdQtyMap.get(invId);
			for (String dateStr : dateQtyMap.keySet()){
				// 原数量
				BigDecimal qty = dateQtyMap.get(dateStr);
				// yyyyMMdd
				DateTime dateTime = DateUtil.parse(dateStr, DatePattern.PURE_DATE_FORMAT);
				// yyyy年MM月dd日
				String formatDateStr = DateUtil.format(dateTime, DatePattern.CHINESE_DATE_PATTERN);
				// 当前日期存在，则取值 2023年06月04日
				if (dateMap.containsKey(formatDateStr)){
					setQty(dateMap.get(formatDateStr), arr, qty);
				}
			}
			// 统计合计
			for (String key: ymQtyMap.keySet()){
				String ymStr = key.split("_")[0];
				String inventoryId = key.split("_")[1];
				if (dateMap.containsKey(ymStr) && invId.equals(Long.valueOf(inventoryId))){
					setQty(dateMap.get(ymStr), arr, ymQtyMap.get(key));
				}
			}
			
			String isPresent = record.getStr(PurchaseOrderD.ISPRESENT);
			record.set("isPresentStr", BoolCharEnum.toEnum(isPresent).getText());
		}
	}
	
	public void setQty(Record dateRecord, String[] arr, BigDecimal qty){
		Integer index = dateRecord.getInt(PurchaseOrderM.INDEX);
		arr[index] = qty.stripTrailingZeros().toPlainString();
	}
	
	public Ret deleteByIds(Object[] ids){
		for (Object id : ids){
			updateColumn(id, PurchaseOrderD.ISDELETED, BoolCharEnum.YES.getValue());
		}
		return SUCCESS;
	}
}
