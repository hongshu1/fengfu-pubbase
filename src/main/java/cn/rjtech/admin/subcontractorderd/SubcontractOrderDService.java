package cn.rjtech.admin.subcontractorderd;

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
import cn.rjtech.model.momdata.SubcontractOrderD;
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
 * @ClassName: SubcontractOrderDService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-25 21:33
 */
public class SubcontractOrderDService extends BaseService<SubcontractOrderD> {
	private final SubcontractOrderD dao=new SubcontractOrderD().dao();
	@Override
	protected SubcontractOrderD dao() {
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
	public Page<SubcontractOrderD> getAdminDatas(int pageNumber, int pageSize, Boolean isPresent, Boolean isDeleted) {
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
	 * @param subcontractOrderD
	 * @return
	 */
	public Ret save(SubcontractOrderD subcontractOrderD) {
		if(subcontractOrderD==null || isOk(subcontractOrderD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(subcontractOrderD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractOrderD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(subcontractOrderD.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param subcontractOrderD
	 * @return
	 */
	public Ret update(SubcontractOrderD subcontractOrderD) {
		if(subcontractOrderD==null || notOk(subcontractOrderD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SubcontractOrderD dbSubcontractOrderD=findById(subcontractOrderD.getIAutoId());
		if(dbSubcontractOrderD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(subcontractOrderD.getName(), subcontractOrderD.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractOrderD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(subcontractOrderD.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderD.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param subcontractOrderD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SubcontractOrderD subcontractOrderD, Kv kv) {
		//addDeleteSystemLog(subcontractOrderD.getIAutoId(), JBoltUserKit.getUserId(),subcontractOrderD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param subcontractOrderD model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SubcontractOrderD subcontractOrderD, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SubcontractOrderD subcontractOrderD, String column, Kv kv) {
		//addUpdateSystemLog(subcontractOrderD.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderD.getName(),"的字段["+column+"]值:"+subcontractOrderD.get(column));
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
	
	public SubcontractOrderD createSubcontractOrderD(Long iSubcontractOrderMid, JSONObject jsonObject){
		SubcontractOrderD subcontractOrderD = new SubcontractOrderD();
		Long iVendorAddrId = jsonObject.getLong(SubcontractOrderD.IVENDORADDRID.toLowerCase());
		String cAddress = jsonObject.getString(SubcontractOrderD.CADDRESS.toLowerCase());
		String cMemo = jsonObject.getString(SubcontractOrderD.CMEMO.toLowerCase());
		Boolean isPresent = jsonObject.getBoolean(SubcontractOrderD.ISPRESENT.toLowerCase());
		Long iInventoryId = jsonObject.getLong(SubcontractOrderD.IINVENTORYID.toLowerCase());
		String iPkgQty = jsonObject.getString(SubcontractOrderD.IPKGQTY.toLowerCase());
		create(iSubcontractOrderMid, iVendorAddrId, iInventoryId, cAddress, cMemo, iPkgQty, isPresent);
		return subcontractOrderD;
	}
	
	public SubcontractOrderD create(Long iSubcontractOrderMid, Long iVendorAddrId, Long iInventoryId, String cAddress, String cMemo, String iPkgQty, boolean isPresent){
		SubcontractOrderD subcontractOrderD = new SubcontractOrderD();
		subcontractOrderD.setIAutoId(JBoltSnowflakeKit.me.nextId());
		subcontractOrderD.setISubcontractOrderMid(iSubcontractOrderMid);
		subcontractOrderD.setIVendorAddrId(iVendorAddrId);
		subcontractOrderD.setIInventoryId(iInventoryId);
		subcontractOrderD.setIsDeleted(false);
		subcontractOrderD.setIsPresent(isPresent);
		subcontractOrderD.setCMemo(cMemo);
		subcontractOrderD.setCAddress(cAddress);
		if (StrUtil.isNotBlank(iPkgQty)){
			subcontractOrderD.setIPkgQty(Integer.valueOf(iPkgQty));
		}
		return subcontractOrderD;
	}
	
	public SubcontractOrderD create(Long id, Long iSubcontractOrderMid, Long iVendorAddrId, Long iInventoryId, String cAddress, String cMemo, String iPkgQty, boolean isPresent) {
		SubcontractOrderD subcontractOrderD = create(iSubcontractOrderMid, iVendorAddrId, iInventoryId, cAddress, cMemo,iPkgQty , isPresent);
		subcontractOrderD.setIAutoId(id);
		return subcontractOrderD;
	}
	
	public int removeBySubcontractOrderMId(Long iSubcontractOrderMid){
		return update("update PS_SubcontractOrderD set isDeleted=1 where iSubcontractOrderMid = ?", iSubcontractOrderMid);
	}
	
	/**
	 * 获取采购细表数据
	 * @param purchaseOrderMId
	 * @return
	 */
	public List<Record> findBySubcontractOrderMId(Long purchaseOrderMId){
		return dbTemplate("subcontractorderd.findAll", Okv.by(SubcontractOrderD.ISUBCONTRACTORDERMID, purchaseOrderMId)).find();
	}
	
	/**
	 *
	 * @param subcontractOrderDList
	 * @param dateMap
	 * @param subcontractOrderdQtyMap
	 * @param ymQtyMap
	 */
	public void setSubcontractDList(List<Record> subcontractOrderDList, Map<String, Record> dateMap, Map<Long, Map<String, BigDecimal>>  subcontractOrderdQtyMap, Map<String, BigDecimal> ymQtyMap){
		// 同一种的存货编码需要汇总在一起。
		// 将日期设值。
		for (Record record : subcontractOrderDList){
			// 存货id（原存货id）
			Long invId = record.getLong(SubcontractOrderD.IINVENTORYID);
			String[] arr = new String[dateMap.keySet().size()];
			record.set(PurchaseOrderM.ARR, arr);
			// 存货编码为key，可以获取存货编码下 所有日期范围的值
			if (!subcontractOrderdQtyMap.containsKey(invId)){
				continue;
			}
			// 当前日期下的数量
			Map<String, BigDecimal> dateQtyMap = subcontractOrderdQtyMap.get(invId);
			
			for (String dateStr : dateQtyMap.keySet()){
				// 原数量
				BigDecimal qty = dateQtyMap.get(dateStr);
				// yyyyMMdd
				DateTime dateTime = DateUtil.parse(dateStr, DatePattern.PURE_DATE_FORMAT);
				// yyyy-MM-dd
				String formatDateStr = DateUtil.format(dateTime, DatePattern.CHINESE_DATE_PATTERN);
				// 当前日期存在，则取值 2023年06月04日
				if (dateMap.containsKey(formatDateStr)){
					setQty(dateMap.get(formatDateStr), arr, qty);
				}
			}
			
			// 统计合计
			for (String str: ymQtyMap.keySet()){
				String ymStr = str.split("_")[0];
				String inventoryId = str.split("_")[1];
				if (dateMap.containsKey(ymStr) && invId.equals(Long.valueOf(inventoryId))){
					setQty(dateMap.get(ymStr), arr, ymQtyMap.get(str));
				}
			}
			
			String isPresent = record.getStr(SubcontractOrderD.ISPRESENT);
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
