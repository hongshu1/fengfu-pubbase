
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
       md.cMoDocNo,
       it.cInvCode1,
       it.cInvName1,
       dpm.cDepName,
       wsm.cWorkShiftName,
       md.dPlanDate,
       uom.cUomName,
       md.iQty,
       wrm.cWorkName,
       it.cInvStd
FROM T_Sys_MaterialsPrepare mp
    LEFT JOIN T_Sys_MaterialsPrepareDetail mpd ON mpd.MasID = mp.AutoID
    LEFT JOIN Mo_MoDoc md ON md.iMoTaskId = mp.SourceBillID
    LEFT JOIN Bd_Inventory it ON it.cInvCode = mpd.InvCode
    LEFT JOIN Bd_Department dpm ON dpm.iAutoId = md.iDepartmentId
    LEFT JOIN Bd_WorkShiftM wsm ON wsm.iAutoId = md.iWorkShiftMid
    LEFT JOIN Bd_Uom uom ON uom.iAutoId = it.iManufactureUomId
    LEFT JOIN Bd_WorkRegionM wrm ON wrm.iAutoId = md.iWorkRegionMid
     WHERE 1 = 1
  #if(cmodocno)
  AND cMoDocNo = #para(cmodocno)
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
ORDER BY mp.CreateDate DESC
    #end

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
