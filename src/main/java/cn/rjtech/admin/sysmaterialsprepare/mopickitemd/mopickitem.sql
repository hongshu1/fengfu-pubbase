
#sql("recpor")
SELECT mm.iAutoId,
       CASE mm.iStatus
           WHEN 1 THEN
               '待备料'
           WHEN 2 THEN
               '备料中'
           WHEN 3 THEN
               '已完成'
           WHEN 4 THEN
               '审批不通过'
           END AS statename,
       mm.iOrgId,
       mm.cOrgCode,
       mm.cOrgName,
       mm.iType,
       mm.cMoPickNo,
       mm.iMoDocId,
       mm.cMoDocNo,
       mm.dPlanDate,
       mm.iPlanQty,
       mm.iStatus,
       mm.iCreateBy,
       mm.cCreateName,
       mm.dCreateTime,
       mm.iUpdateBy,
       mm.cUpdateName,
       mm.dUpdateTime,
       mdb.cBarcode,
       ic.iWorkShiftMid,
       it.cInvCode,
       it.cInvCode1,
       dt.cDepName,
       it.iManufactureUomId,
       it.iEquipmentModelId,
       it.cInvStd,
       it.iInventoryUomId1,
       md.iWarehouseId,
       md.iWarehouseAreaId,
       md.iQty,
       md.iRemainQty,
       md.cAvailableBatch,
       mdb.dProdDate,
       CASE md.isScanned
           WHEN 0 THEN
               '未检查'
           WHEN 1 THEN
               '已检查'
           END AS scannedname
FROM Mo_MoPickItemM mm
         LEFT JOIN Mo_MoPickItemD md on md.iMoPickItemMid = mm.iAutoId
         LEFT JOIN Mo_MoPickItemD_Batch mdb on mdb.iMoPickItemDid = md.iAutoId
         LEFT JOIN Bd_InventoryCapacity ic on mdb.iInventoryId = ic.iInventoryId
         LEFT JOIN Bd_Inventory it on mdb.cBarcode = it.cBarcode
         LEFT JOIN Bd_Warehouse wh on mdb.cBarcode = wh.cBarcode
         LEFT JOIN Bd_Department dt on wh.cDepCode = dt.cDepCode
where 1 = 1
    #if(cmopickno)
		and mm.cMoPickNo like concat('%',#para(cmopickno),'%')
	#end
	#if(cmodocno)
		and mm.cMoDocNo like concat('%',#para(cmodocno),'%')
	#end
	#if(iinventoryid)
		and mdb.iInventoryId like concat('%',#para(iinventoryid),'%')
	#end
	#if(corgcode)
		and mm.cOrgCode like concat('%',#para(corgcode),'%')
	#end
	#if(corgname)
		and mm.cOrgName like concat('%',#para(corgname),'%')
	#end
	#if(dplandate)
		and mm.dPlanDate like concat('%',#para(dplandate),'%')
	#end
	#if(startTime)
		and mm.dPlanDate >= #para(startTime)
	#end
	#if(endTime)
		and mm.dPlanDate <= #para(endTime)
	#end
ORDER BY mm.dUpdateTime DESC
    #end

    #sql("dList")
SELECT a.*
FROM T_Sys_OtherInDetail a
         left join T_Sys_OtherIn i on a.MasID = i.AutoID
where 1 = 1
  and a.isdeleted = 0 #if(masid)
		and a.MasID = #para(masid)
	#end
ORDER BY a.ModifyDate DESC
    #end

