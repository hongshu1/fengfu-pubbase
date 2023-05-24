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
ORDER BY a.ModifyDate DESC
#end









