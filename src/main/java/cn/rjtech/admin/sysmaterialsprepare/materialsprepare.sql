
#sql("recpor")
SELECT mp.AutoID,
       mp.OrganizeCode,
       mp.BillNo,
       mp.BillType,
       mp.BillDate,
       mp.AuditPerson,
       mp.AuditDate,
       mp.CreatePerson,
       mp.CreateDate,
       mp.ModifyPerson,
       mp.ModifyDate,
       mp.SourceBillNo,
       mp.SourceBillID,
       mpd.InvCode,
       mpd.Barcode,
       mpd.Qty,
       mpd.Qty * md.iQty AS totalQty,
       md.cMoDocNo,
       md.dPlanDate,
       md.iQty,
       md.iStatus,
       it.cInvCode1,
       it.cInvName1,
       it.cInvStd,
       dpm.cDepName,
       wsm.cWorkShiftName,
       wsm.cWorkShiftCode,
       uom.cUomName,
       wrm.cWorkName,
       wrm.cWorkCode,
       emm.cEquipmentModelName
FROM T_Sys_MaterialsPrepare mp
         LEFT JOIN T_Sys_MaterialsPrepareDetail mpd ON mpd.MasID = mp.AutoID
         LEFT JOIN Mo_MoDoc md ON md.iAutoId = mp.SourceBillID
         LEFT JOIN Bd_Inventory it ON it.iAutoId = md.iInventoryId
         LEFT JOIN Bd_Department dpm ON dpm.iAutoId = md.iDepartmentId
         LEFT JOIN Bd_WorkShiftM wsm ON wsm.iAutoId = md.iWorkShiftMid
         LEFT JOIN Bd_Uom uom ON uom.iAutoId = it.iManufactureUomId
         LEFT JOIN Bd_WorkRegionM wrm ON wrm.iAutoId = md.iWorkRegionMid
         LEFT JOIN Bd_EquipmentModel emm ON emm.iAutoId = it.iEquipmentModelId
WHERE 1 = 1
  #if(billno)
  AND BillNo = #para(billno)
  #end
  #if(cmodocno)
  AND cMoDocNo = #para(cmodocno)
  #end
  #if(invcode)
  AND InvCode = #para(invcode)
  #end
  #if(cinvcode1)
  AND cInvCode1 = #para(cinvcode1)
  #end
  #if(cinvname1)
  AND cInvName1 = #para(cinvname1)
  #end
  #if(startTime)
  AND dPlanDate >= #para(startTime)
  #end
  #if(endTime)
  AND dPlanDate <= #para(endTime)
  #end
  #if(dplandate)
  AND dPlanDate = #para(dplandate)
  #end
  #if(cworkshiftcode)
  AND cWorkShiftCode = #para(cworkshiftcode)
  #end
  #if(cworkcode)
  AND cWorkCode = #para(cworkcode)
  #end
ORDER BY mp.CreateDate DESC
    #end



#sql("Auto")
SELECT md.iAutoId,
       md.dPlanDate,
       md.cMoDocNo,
       md.iQty,
       dpm.cDepName,
       wsm.cWorkShiftName,
       wrm.cWorkName,
       it.cInvCode,
       it.cInvCode1,
       it.cInvName1,
       uom.cUomName
FROM Mo_MoDoc md
         LEFT JOIN Bd_Department dpm ON dpm.iAutoId = md.iDepartmentId
         LEFT JOIN Bd_WorkShiftM wsm ON wsm.iAutoId = md.iWorkShiftMid
         LEFT JOIN Bd_Inventory it ON it.iAutoId = md.iInventoryId
         LEFT JOIN Bd_Uom uom ON uom.iAutoId = it.iManufactureUomId
         LEFT JOIN Bd_WorkRegionM wrm ON wrm.iAutoId = md.iWorkRegionMid
WHERE 1 = 1
  AND md.iStatus = 2
    #if(dplandate)
  AND dPlanDate = #para(dplandate)
  #end
  #if(startTime)
  AND dPlanDate >= #para(startTime)
  #end
  #if(endTime)
  AND dPlanDate <= #para(endTime)
  #end
ORDER BY md.dPlanDate DESC
    #end



#sql("getMaterialsOutLines")
SELECT mp.AutoID,
       mp.OrganizeCode,
       mp.BillNo,
       mp.BillType,
       mp.BillDate,
       mp.AuditPerson,
       mp.AuditDate,
       mp.CreatePerson,
       mp.CreateDate,
       mp.ModifyPerson,
       mp.ModifyDate,
       mp.SourceBillNo,
       mp.SourceBillID,
       mpd.InvCode,
       mpd.Barcode,
       mpd.Qty,
       md.cMoDocNo,
       md.dPlanDate,
       md.iQty,
       md.iStatus,
       it.cInvCode1,
       it.cInvName1,
       it.cInvStd,
       dpm.cDepName,
       wsm.cWorkShiftName,
       uom.cUomName,
       wrm.cWorkName,
       emm.cEquipmentModelName
FROM T_Sys_MaterialsPrepare mp
         LEFT JOIN T_Sys_MaterialsPrepareDetail mpd ON mpd.MasID = mp.AutoID
         LEFT JOIN Mo_MoDoc md ON md.iAutoId = mp.SourceBillID
         LEFT JOIN Bd_Inventory it ON it.iAutoId = md.iInventoryId
         LEFT JOIN Bd_Department dpm ON dpm.iAutoId = md.iDepartmentId
         LEFT JOIN Bd_WorkShiftM wsm ON wsm.iAutoId = md.iWorkShiftMid
         LEFT JOIN Bd_Uom uom ON uom.iAutoId = it.iManufactureUomId
         LEFT JOIN Bd_WorkRegionM wrm ON wrm.iAutoId = md.iWorkRegionMid
         LEFT JOIN Bd_EquipmentModel emm ON emm.iAutoId = it.iEquipmentModelId
WHERE 1 = 1
  AND md.iStatus = 2
  AND md.cMoDocNo = '#(cmodocno)' #end


#sql("getMaterialsOutLines1")
SELECT mpd.InvCode,
       it.cInvCode1,
       it.cInvName1,
       it.cInvStd
FROM Mo_MoDoc md
LEFT JOIN Bd_InventoryRouting ir ON ir.iAutoId= md.iInventoryRouting
LEFT JOIN Bd_Inventory it ON it.iAutoId= ir.iInventoryId
LEFT JOIN T_Sys_MaterialsPrepare mp ON mp.SourceBillID= md.iAutoId
LEFT JOIN T_Sys_MaterialsPrepareDetail mpd ON mpd.MasID= mp.AutoID
WHERE 1 = 1
  AND md.cMoDocNo = '#(cmodocno)' #end


#sql("findColumns")
SELECT *
FROM Bd_WorkRegionM wrm
WHERE wrm.isDeleted = '0' #if(isenabled)
  AND wrm.isenabled = #para(isenabled)
  #end
#end



#sql("findColumns1")
SELECT *
FROM Bd_WorkShiftM wsm
WHERE wsm.isDeleted = '0' #if(isenabled)
  AND wsm.isenabled = #para(isenabled)
  #end
#end






