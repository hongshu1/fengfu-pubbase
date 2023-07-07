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
    cr.cCusAbbName,
    t4.cdistrictname,
    t5.crdname as issuename,
    t6.cssname,
    t7.crdname as rdname
FROM T_Sys_SaleDeliverPlan a
         LEFT JOIN  Bd_SaleType sa on sa.cSTCode = a.RdCode
         LEFT JOIN Bd_Department de on de.cDepCode = a.DeptCode
         LEFT JOIN Bd_Customer cr on  a.iCustomerId = cr.iAutoId
         LEFT JOIN Bd_CustomerAddr t4 on a.ShipAddress = t4.iAutoId
         LEFT JOIN Bd_Rd_Style t5 on a.issue = t5.cRdCode
         LEFT JOIN Bd_SettleStyle t6 on a.condition = t6.cSSCode
         LEFT JOIN Bd_Rd_Style t7 on a.rdcode = t7.cRdCode
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
and a.dCreateTime >= #para(starttime)
#end
#if(endtime)
and a.dCreateTime <= #para(endtime)
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

#sql("findRecordByAutoid")
select
t1.*,
t2.cstname,
t3.cdepname,
t3.cdepcode,
t4.cdistrictname,
t5.crdname,
t6.cssname,
t7.cCusCode,
t7.cCusName,
t7.cCusAbbName
from T_Sys_SaleDeliverPlan t1
left join Bd_SaleType t2 on t1.rdcode = t2.cSTCode
left join Bd_Department t3 on t1.deptcode = t3.cDepCode
left join Bd_CustomerAddr t4 on t1.ShipAddress = t4.cDistrictCode
left join Bd_Rd_Style t5 on t1.issue = t5.cRdCode
left join Bd_SettleStyle t6 on t1.condition = t6.cSSCode
left join Bd_Customer t7 on t1.iCustomerId = t7.iAutoId
where 1=1
#if(autoid)
and t1.autoid=#para(autoid)
#end
#end

#sql("findTableDatas")
select
    t1.* ,
    t2.cWhName as whname,
    t3.careacode,
    t3.careaname,
    t4.cinvname,
    t4.cInvCode,
    t4.cInvCode1,
    t4.cInvName1,
    t4.cinvstd,
    t5.cuomcode,
    t5.cuomname
from
    T_Sys_SaleDeliverPlanDetail t1
    left join Bd_Warehouse t2 on t1.whcode = t2.cWhCode
    left join Bd_Warehouse_Area t3 on t1.PosCode = t3.cAreaCode
    left join bd_inventory t4 on t1.invcode = t4.cinvcode
    left join bd_uom t5 on t4.iSalesUomId = t5.iAutoId
where 1=1
#if(masid)
and t1.MasID = #para(masid)
#end
#if(orgcode)
and t2.cOrgCode =#para(orgcode)
and t3.cOrgCode =#para(orgcode)
#end
#end