#sql("rcvpland")
SELECT  a.*,rm.iCustomerId,cr.cCusName
FROM SM_RcvPlanD a
LEFT JOIN SM_RcvPlanM rm on rm.iAutoId = a.iRcvPlanMid
LEFT JOIN Bd_Customer cr on rm.iCustomerId = cr.iAutoId
where 1 = 1
	#if(cbarcode)
		and a.cBarcode like concat('%',#para(cbarcode),'%')
	#end
	#if(icustomerid)
		and rm.iCustomerId = #para(icustomerid)
	#end

ORDER BY a.dUpdateTime DESC
#end



#sql("customeraddr")
SELECT  a.*,we.cWhCode,we.cWhName
FROM Bd_CustomerAddr a
LEFT JOIN Bd_Warehouse we on a.iWarehouseId = we.iAutoId
where a.isDeleted = '0'
	#if(cdistrictname)
		and a.cDistrictName like concat('%',#para(cdistrictname),'%')
	#end
	#if(cwhname)
		and we.cWhName like concat('%',#para(cwhname),'%')
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
FROM T_Sys_ScanDeliverDetail a
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
ORDER BY a.ModifyDate DESC
#end


###-------------------重新开发

#sql("getLine")
select * from T_Sys_ScanDeliverDetail where MasID = '#(autoId)'
#end

#sql("getCustomer")
SELECT  a.cCusCode as cusCode, a.cCusName as cusName, a.iAutoId as cusId
FROM Bd_Customer a
where 1=1 and a.isEnabled = '1' and a.isDeleted = '0'
	#if(q)
		and (a.cCusCode like concat('%',#para(q),'%') OR a.cCusName like concat('%',#para(q),'%'))
	#end
#end

#sql("getCustAddr")
select t1.iAutoId as addrId, cDistrictCode as addrCode, cDistrictName as addrName, t2.cWhCode as whCode, t2.cWhName
as whName
from Bd_CustomerAddr t1
	left join Bd_Warehouse t2 on t2.iAutoId = t1.iWarehouseId
where iCustomerId = '#(cusid)'
### and t1.isDeleted = '0' and t1.isEnabled = '1'
### and t2.isDeleted = '0' and t2.isEnabled = '1'
#if(q)
		and (t1.cDistrictCode like concat('%',#para(q),'%') OR t1.cDistrictCode like concat('%',#para(q),'%'))
	#end
#end

#sql("getOrder")
select *
from (select t1.cOrderNo    as SourceBillNo,
             t1.iAutoId     as SourceBillID,
             t1.iSaleTypeId as SourceBillType,
             sale.cSTName   as saleName,
             t1.iBusType    as BillType,
             dic.name       as busName
      from Co_WeekOrderM t1
               left join (
          SELECT s1.*,
                 s2.cRdName,
                 s2.cRdCode as s2RdCode
          FROM Bd_SaleType s1
                   LEFT JOIN Bd_Rd_Style s2 ON s2.cRdCode = s1.cRdCode
          WHERE s1.IsDeleted = '0'
      ) sale on sale.iAutoId = t1.iSaleTypeId
               left join UGCFF_MOM_System.dbo.jb_dictionary dic
                         ON t1.iBusType = dic.sn AND dic.type_key = 'order_business_type'
      where 1 = 1
        and t1.iCustomerId = '#(cusid)'
        and t1.iOrgId = '#(orgId)'
        ###and t1.iAuditStatus = '2'
        ###and t1.IsDeleted = '0'
      union all
      select t1.cOrderNo    as SourceBillNo,
             t1.iAutoId     as SourceBillID,
             t1.iSaleTypeId as SourceBillType,
             sale.cSTName   as saleName,
             t1.iBusType    as BillType,
             dic.name       as busName
      from Co_ManualOrderM t1
               left join (
          SELECT s1.*,
                 s2.cRdName,
                 s2.cRdCode as s2RdCode
          FROM Bd_SaleType s1
                   LEFT JOIN Bd_Rd_Style s2 ON s2.cRdCode = s1.cRdCode
          WHERE s1.IsDeleted = '0'
      ) sale on sale.iAutoId = t1.iSaleTypeId
               left join UGCFF_MOM_System.dbo.jb_dictionary dic
                         ON t1.iBusType = dic.sn AND dic.type_key = 'order_business_type'
      where 1 = 1
        and t1.iCustomerId = '#(cusid)'
        and t1.iOrgId = '#(orgId)'
        ###and t1.iAuditStatus = '2'
        ###and t1.IsDeleted = '0'
        union all
        select t1.cOrderNo as SourceBillNo,
       t1.iAutoId  as SourceBillID,
       ''          as SourceBillType,
       ''          as saleName,
       ''          as BillType,
       ''          as busName
        from Co_MonthOrderM t1
        where 1 = 1
          and t1.iCustomerId = '#(cusid)'
          and t1.iOrgId = '#(orgId)'
          ###and t1.iAuditStatus = '2'
          ###and t1.IsDeleted = '0'
        ) t
where 1=1
#if(orderNo)
and t.SourceBillNo like concat('%',#para(orderNo),'%')
#end
#end

#sql("getOrderLine")
select t.*,
       isnull(delivery.deliveryQty, 0) as deliveryQty,
       b.cInvName1,
       b.cInvCode1,
       b.cInvStd                       as cinvstd,
       uom.cUomName
from (select t1.iAutoId  as SourceBillDid,
             t1.cInvCode as InvCode,
             t1.iInventoryId,
             t1.dPlanAogDate as billDate,
             0           as qty,
             t1.iQty     as planQty
      from Co_WeekOrderD t1
      where iWeekOrderMid = '#(orderId)'
        ###and t1.isDeleted = '0'
      union all
      select t1.iAutoId  as SourceBillDid,
             t1.cInvCode as InvCode,
             t1.iInventoryId,
             ''          as billDate,
             0           as qty,
             t1.iSum     as planQty
      from Co_ManualOrderD t1
      where iManualOrderMid = '#(orderId)'
        ###and t1.isDeleted = '0'
      union all
      select t1.iAutoId  as SourceBillDid,
       t1.cInvCode as InvCode,
       t1.iInventoryId,
       ''          as billDate,
       0           as qty,
       t1.iSum     as planQty
        from Co_MonthOrderD t1
        where iMonthOrderMid = '#(orderId)'
         ###and t1.isDeleted = '0'
         ) t
         left join (
    select InvCode, sum(Qty) as deliveryQty from T_Sys_SaleDeliverDetail where isDeleted = '0' group by InvCode
) delivery on t.InvCode = delivery.InvCode
         left join Bd_Inventory b on t.iInventoryId = b.iAutoId
         left join Bd_Uom uom on b.iPurchaseUomId = uom.iAutoId
#end

#sql("getResource")
select t1.AutoID,
             t1.Barcode as oldBarcode,
             t1.cCompleteBarcode as Barcode,
             t1.InvCode as cInvCode,
             t1.Qty,
             t2.Whcode,
             b.cInvCode1,
             b.cInvName1,
             b.cInvStd,
             uom.cUomName,
             w.cWhName
      from T_Sys_ProductInDetail t1
               left join T_Sys_ProductIn t2 on t1.MasID = t2.AutoID
               left join Bd_Inventory b on t1.InvCode = b.cInvCode
               left join Bd_Uom uom on b.iPurchaseUomId = uom.iAutoId
               left join Bd_Warehouse w on t2.Whcode = w.cWhCode
where 1=1
        and t1.isDeleted = '0'
        and t2.isDeleted = '0'
        ###and t2.iAuditStatus = '2'
        and t1.cCompleteBarcode = '#(barcode)'
#end
