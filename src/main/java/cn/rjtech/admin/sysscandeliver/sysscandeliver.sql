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
	#if(id)
		and a.MasID = #para(id)
	#end
ORDER BY a.dupdatetime DESC
#end


### -- ===========重新开发
#sql("getCarData")
select d.cCarNo, d.cBarcode, d.cVersion, m.iCustomerId as cusId, c.cCusName, c.cCusCode, d.iAutoId  as SourceBillDid
from SM_RcvPlanD d
         left join SM_RcvPlanM m on d.iRcvPlanMid = m.iAutoId
         left join Bd_Customer c on m.iCustomerId = c.iAutoId
where 1=1
#if(carNo)
 and d.cCarNo like concat('%',#para(carNo),'%')
#end
  and m.IsDeleted = '0' and d.IsDeleted = '0'
#end

#sql("getLine")
select * from T_Sys_ScanDeliverDetail where MasID = '#(autoId)'
#end

#sql("getOrderLine")
select t.*,
       isnull(delivery.deliveryQty, 0) as deliveryQty
from (select d.cCarNo,
             d.cRcvDate,
             d.cRcvTime,
             d.cBarcode,
             d.cVersion,
             d.iInventoryId,
             0      as qty,
             d.iQty as planQty,
             b.cInvCode as InvCode,
             b.cInvName,
             b.cInvCode1,
             b.cInvName1,
             b.cInvStd,
             uom.cUomName
      from SM_RcvPlanD d
               left join SM_RcvPlanM m on d.iRcvPlanMid = m.iAutoId
               left join Bd_Inventory b on d.iInventoryId = b.iAutoId
               left join Bd_Uom uom on b.iPurchaseUomId = uom.iAutoId
      where cCarNo = '#(carNo)'
    and d.IsDeleted = '0'
    and m.IsDeleted = '0'
    ) t
         left join (select InvCode, sum(Qty) as deliveryQty
                    from T_Sys_SaleDeliverDetail
                    where isDeleted = '0'
                    group by InvCode) delivery on t.InvCode = delivery.InvCode
#end

#sql("getResource")
select *, 61 as qty
from (select t1.AutoID,
             t1.Barcode,
             t1.InvCode as cInvCode,
             ###t1.Qty,
             t2.Whcode,
             b.cInvCode1,
             b.cInvName1,
             b.cInvStd,
             uom.cUomName,
             w.cWhName,
             d.cBarcode,
             d.cVersion,
             wd.cCode as cusBarcode,
             wm.cOrderNo
      from T_Sys_ProductInDetail t1
               left join T_Sys_ProductIn t2 on t1.MasID = t2.AutoID
               left join Bd_Inventory b on t1.InvCode = b.cInvCode
               left join Bd_Uom uom on b.iPurchaseUomId = uom.iAutoId
               left join Bd_Warehouse w on t2.Whcode = w.cWhCode
               left join SM_RcvPlanD d on d.iInventoryId = b.iAutoId
               left join Co_WeekOrderD wd on wd.cCode = d.cBarcode
               left join Co_WeekOrderM wm on wm.iAutoId = wd.iWeekOrderMid
      where 1=1
        and t1.isDeleted = '0'
        and t2.isDeleted = '0'
        and d.isDeleted = '0'
        and wd.isDeleted = '0'
        and wm.IsDeleted = '0'
        ###and t2.iAuditStatus = '2'
        and t1.Barcode = '#(barcode)'
     ) t
where t.cusBarcode = '#(cusBarcode)'
  and t.Barcode = '#(barcode)'
#end

