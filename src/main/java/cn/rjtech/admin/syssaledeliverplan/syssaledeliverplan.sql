#sql("syssaledeliverplanList")
SELECT
    a.*,
    sa.cSTCode,
    auditname =
    CASE WHEN a.iAuditStatus=0 THEN '已保存'
         WHEN a.iAuditStatus=1 THEN '待审核'
         WHEN a.iAuditStatus=2 THEN '审核通过'
         WHEN a.iAuditStatus=3 THEN '审核不通过' END,
    sa.cSTName,
    de.cDepCode,
    de.cDepName,
    cr.cCusName,
    cr.cCusAbbName
FROM T_Sys_SaleDeliverPlan a
         LEFT JOIN  Bd_SaleType sa on sa.cSTCode = a.RdCode
         LEFT JOIN Bd_Department de on de.cDepCode = a.DeptCode
         LEFT JOIN Bd_Customer cr on  a.iCustomerId = cr.iAutoId
where 1=1 and a.isDeleted = '0'
  #if(billno)
  and a.BillNo like concat('%',#para(billno),'%')
#end
#if(cdepname)
and de.cDepName like concat('%',#para(cdepname),'%')
#end
#if(ccusname)
and cr.cCusName like concat('%',#para(ccusname),'%')
#end
#if(iauditstatus)
and a.iauditstatus=#para(iauditstatus)
#end
#if(starttime)
and t1.dCreateTime >= #para(starttime)
#end
#if(endtime)
and t1.dCreateTime <= #para(endtime)
#end
ORDER BY a.dupdatetime DESC
#end


#sql("RdStyle")
SELECT  a.*
FROM Bd_Rd_Style a
where a.bRdFlag = '0'
	#if(q)
		and (a.cRdCode like concat('%',#para(q),'%') OR a.cRdName like concat('%',#para(q),'%'))
	#end
#end



#sql("order")
SELECT  a.*,b.cCusName as cuname
FROM Co_SubcontractSaleOrderM a
left join Bd_Customer b on  a.iCustomerId = b.iAutoId
where a.IsDeleted = '0'
	#if(icustomerid)
		and a.iCustomerId = #para(icustomerid)
	#end
	#if(cuname)
		and b.cCusName = #para(cuname)
	#end
	#if(corderno)
		and a.cOrderNo like concat('%',#para(corderno),'%')
	#end
	#if(startTime)
		and a.dOrderDate >= #para(startTime)
	#end
	#if(endTime)
		and a.dOrderDate <= #para(endTime)
	#end
#end



#sql("saletype")
SELECT  a.*
FROM Bd_SaleType a
where a.IsDeleted = '0'
	#if(q)
		and (a.cSTCode like concat('%',#para(q),'%') OR a.cSTName like concat('%',#para(q),'%'))
	#end
#end

#sql("department")
SELECT  a.*
FROM Bd_Department a
where a.IsDeleted = '0'
	#if(q)
		and (a.cDepCode like concat('%',#para(q),'%') OR a.cDepName like concat('%',#para(q),'%'))
	#end
#end


#sql("customeraddr")
SELECT  a.*
FROM Bd_CustomerAddr a
where a.IsDeleted = '0'
	#if(q)
		and (a.cDistrictCode like concat('%',#para(q),'%') OR a.cDistrictName like concat('%',#para(q),'%'))
	#end
#end


#sql("settlestyle")
SELECT  a.*
FROM Bd_SettleStyle a
where a.IsDeleted = '0'
	#if(q)
		and (a.cSSCode like concat('%',#para(q),'%') OR a.cSSName like concat('%',#para(q),'%'))
	#end
#end


#sql("foreigncurrency")
SELECT  a.*,a.cexch_code as cexchcode,a.cexch_name as cexchname
FROM Bd_ForeignCurrency a
where a.IsDeleted = '0'
	#if(q)
		and (a.cexch_code like concat('%',#para(q),'%') OR a.cexch_name like concat('%',#para(q),'%'))
	#end
#end





#sql("dList")
SELECT  a.*, t1.Barcode,
       t1.SourceID as SourceBIllNoRow,
       t1.SourceBillType as SourceBillType,
       t1.SourceBillNo,t1.Qty,t1.BarcodeDate,
       t1.BarcodeID as SourceBillID,
       t1.MasID as SourceBillDid,
       t1.pat,
       i.*,

       (SELECT cUomName FROM Bd_Uom WHERE i.iInventoryUomId1 = iautoid) as InventorycUomName,
       (SELECT cUomName FROM Bd_Uom WHERE i.iPurchaseUomId = iautoid) as PurchasecUomName,
       (SELECT cContainerCode FROM Bd_Container WHERE i.iContainerClassId = iautoid) as cContainerCode,
       u.cUomClassName,
       t3.cInvCCode,
       t3.cInvCName,
       t4.cEquipmentModelName,
        wh.cWhName as whname
FROM T_Sys_SaleDeliverPlanDetail a
LEFT JOIN  V_Sys_BarcodeDetail t1 on t1.Barcode = a.Barcode
LEFT JOIN bd_inventory i ON i.cinvcode = t1.Invcode
LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
LEFT JOIN Bd_EquipmentModel t4 ON i.iEquipmentModelId = t4.iautoid
LEFT JOIN Bd_Warehouse wh on wh.cWhCode = a.WhCode
where 1=1
	#if(masid)
		and a.MasID = #para(masid)
	#end
ORDER BY a.dupdatetime DESC
#end

#sql("getSaleDeliverBillNoList")
select
t1.*
from
(
    SELECT iAutoId,cOrderNo,iCustomerId,iOrderStatus,cCusCode,cCusName,IsDeleted from  Co_ManualOrderM  where IsDeleted='0'
    UNION ALL
    SELECT iAutoId,cOrderNo,iCustomerId,iOrderStatus,cCusCode,cCusName,IsDeleted from  Co_WeekOrderM where IsDeleted='0'
    UNION ALL
    SELECT iAutoId,cOrderNo,iCustomerId,iOrderStatus,cCusCode,cCusName,IsDeleted from  Co_MonthOrderM
) t1
where 1=1
#if(iautoid)
and t1.iautoid = #para(iautoid)
#end
#if(icustomerid)
and t1.iCustomerId = #para(icustomerid)
#end
#if(corderno)
and t1.cOrderNo like concat('%',#para(corderno),'%')
#end
#if(ccusname)
and t1.cCusName like concat('%',#para(ccusname),'%')
#end
#if(ccuscode)
and t1.cCusCode like concat('%',#para(ccuscode),'%')
#end
#end

#sql("scanBarcode")
select
    t1.*,
    t2.cinvcode as invcode,
    t2.cinvname,
    t2.cInvCode ,
    t2.cInvCode1,
    t2.cInvName1,
    t2.cinvstd,
    t3.cuomcode,
    t3.cuomname
from
(
    SELECT iAutoId,iPurchaseOrderDid,iinventoryId,cBarcode,iQty as qty,cSourceld,cCompleteBarcode from PS_PurchaseOrderDBatch
    UNION ALL
    SELECT iAutoId,iSubcontractOrderDid,iinventoryId,cBarcode,iQty,cSourceld,cCompleteBarcode from PS_SubcontractOrderDBatch
) t1
LEFT JOIN Bd_Inventory t2 on t1.iinventoryId = t2.iautoid
LEFT JOIN Bd_Uom t3 on t2.iSalesUomId = t3.iAutoId
where 1=1
#if(barcode)
and t1.cCompleteBarcode = #para(barcode)
#end
#if(q)
and (
    t2.cinvcode like concat('%',#para(q),'%') or t2.cinvcode1 like concat('%',#para(q),'%')
    or t2.cinvname1 like concat('%',#para(q),'%') or t1.cCompleteBarcode like concat('%',#para(q),'%')
      )
#end
#if(cinvcodes)
and t2.cinvcode in (#para(cinvcodes))
#end
#end

#sql("scanInvcode")
select t1.* from Bd_Inventory t1
where 1=1
#if(q)
    and (
      t1.cinvcode like concat('%',#para(q),'%') or t1.cinvcode1 like concat('%',#para(q),'%')
    or t2.cinvname1 like concat('%',#para(q),'%')
        )
#end
#if(invcode)
and t1.cinvcode = #para(invcode)
#end
#if(cinvcodes)
and t1.cinvcode in (#para(cinvcodes))
#end
#end

#sql("selectInvocodeByMaskid")
select t1.* from
(
    SELECT iAutoId,iManualOrderMid as maskid,cInvCode,iInventoryId from Co_ManualOrderD
    UNION ALL
    SELECT iAutoId,iWeekOrderMid as maskid,cInvCode,iInventoryId from Co_WeekOrderD
    UNION ALL
    SELECT iAutoId,iMonthOrderMid as maskid,cInvCode,iInventoryId from Co_MonthOrderD
) t1
where 1=1
#if(sourcebillid)
and t1.maskid = #para(sourcebillid)
#end
#end

#sql("findCOrderNoBySourceBillId")
select
    t1.*
from
   (
    SELECT iAutoId,cOrderNo,iCustomerId,iOrderStatus,cCusCode,cCusName,IsDeleted from  Co_ManualOrderM  where IsDeleted='0'
    UNION ALL
    SELECT iAutoId,cOrderNo,iCustomerId,iOrderStatus,cCusCode,cCusName,IsDeleted from  Co_WeekOrderM where IsDeleted='0'
    UNION ALL
    SELECT iAutoId,cOrderNo,iCustomerId,iOrderStatus,cCusCode,cCusName,IsDeleted from  Co_MonthOrderM
    ) t1
where 1=1
#if(iautoid)
    and t1.iautoid = #para(iautoid)
#end
#end