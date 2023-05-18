
#sql("recpor")
select so.AutoID, CASE so.state
        WHEN 1 THEN
        '已保存'
				WHEN 2 THEN
        '待审批'
				WHEN 3 THEN
        '已审批'
				WHEN 4 THEN
        '审批不通过'
        END AS statename,so.state,so.BillNo,so.CreateDate,so.Whcode,ck.cWhName as whname,so.RdCode,rd.cRdName as rdcodename,
			so.BillType,so.VenCode,v.cVenName as venname,so.ModifyDate,so.memo,so.ModifyPerson,so.CreatePerson,so.AuditPerson,
			so.AuditDate
FROM T_Sys_OtherIn so
LEFT JOIN Bd_Warehouse ck on so.Whcode = ck.cWhCode
LEFT JOIN Bd_Rd_Style rd on so.RdCode = rd.cRdCode
LEFT JOIN Bd_Vendor v on so.VenCode = v.cVenCode
where 1=1
	#if(billno)
		and so.BillNo like concat('%',#para(billno),'%')
	#end
	#if(whname)
		and ck.cWhName like concat('%',#para(whname),'%')
	#end
	#if(state)
		and so.state = #para(state)
	#end
	#if(startTime)
		and so.CreateDate >= #para(startTime)
	#end
	#if(endTime)
		and so.CreateDate <= #para(endTime)
	#end
ORDER BY so.ModifyDate DESC
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
       t4.cEquipmentModelName
FROM T_Sys_OtherInDetail a
left join  V_Sys_BarcodeDetail t1 on t1.Barcode = a.Barcode
         LEFT JOIN bd_inventory i ON i.cinvcode = t1.Invcode
         LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
         LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
         LEFT JOIN Bd_EquipmentModel t4 ON i.iEquipmentModelId = t4.iautoid
where 1=1
	#if(masid)
		and a.MasID = #para(masid)
	#end
ORDER BY a.ModifyDate DESC
#end

